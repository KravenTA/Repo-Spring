package umg.programacionIII.Repo_Spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umg.programacionIII.estructuras.lista.Nodo;
import umg.programacionIII.model.Historial;
import umg.programacionIII.service.HistorialService;
import umg.programacionIII.service.TareaService;
import umg.programacionIII.estructuras.lista.Lista;
import umg.programacionIII.estructuras.lista.Opcional;

@RestController
@RequestMapping("/api/historiales")
public class HistorialController {

    private final HistorialService historialService;
    private final TareaService tareaService;

    @Autowired
    public HistorialController(HistorialService historialService, TareaService tareaService) {
        this.historialService = historialService;
        this.tareaService = tareaService; // ✅ inyección correcta
    }

    @GetMapping
    public Lista<Historial> obtenerTodos() {
        return historialService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Historial> obtenerPorId(@PathVariable Long id) {
        Opcional<Historial> historial = historialService.findById(id);
        return historial.estaPresente() ?
                ResponseEntity.ok(historial.obtener()) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public Lista<Historial> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return historialService.findByUsuarioId(usuarioId);
    }

    @PostMapping
    public Historial guardar(@RequestBody Historial historial) {
        return historialService.save(historial);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        historialService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deshacer/{usuarioId}")
    public ResponseEntity<String> deshacerUltimaAccion(@PathVariable Long usuarioId) {
        Lista<Historial> historiales = historialService.findAll();
        Historial ultimaAccion = null;
        String tipoAccion = "";
        String descripcionDetectada = "";

        Nodo<Historial> actual = historiales.leerPrimero();
        while (actual != null) {
            Historial h = actual.leerDato();

            if (h.getUsuario() != null && h.getUsuario().getId().equals(usuarioId)) {
                String accionLower = h.getAccion().toLowerCase();

                if (accionLower.contains("crear") || accionLower.contains("creación")) {
                    ultimaAccion = h;
                    tipoAccion = "CREAR_TAREA";
                    int idx = h.getAccion().indexOf(":");
                    if (idx != -1) {
                        descripcionDetectada = h.getAccion().substring(idx + 1).trim();
                    }
                }
            }

            actual = actual.siguiente();
        }

        if (ultimaAccion == null || descripcionDetectada.isEmpty()) {
            return ResponseEntity.badRequest().body("No se encontró una acción válida para deshacer para el usuario con ID: " + usuarioId);
        }

        if (tipoAccion.equals("CREAR_TAREA")) {
            boolean eliminada = tareaService.eliminarPorDescripcion(descripcionDetectada);
            if (eliminada) {
                return ResponseEntity.ok("Tarea \"" + descripcionDetectada + "\" eliminada correctamente.");
            } else {
                return ResponseEntity.badRequest().body("No se encontró la tarea para eliminar: \"" + descripcionDetectada + "\"");
            }
        }

        return ResponseEntity.ok("Acción reconocida, pero aún no implementada para deshacer: " + tipoAccion);
    }
}
