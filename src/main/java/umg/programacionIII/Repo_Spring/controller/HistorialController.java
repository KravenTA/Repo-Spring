package umg.programacionIII.Repo_Spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umg.programacionIII.model.Historial;
import umg.programacionIII.service.HistorialService;
import umg.programacionIII.estructuras.lista.Lista;
import umg.programacionIII.estructuras.lista.Opcional;


@RestController
@RequestMapping("/api/historiales")
public class HistorialController {

    private final HistorialService historialService;

    public HistorialController(HistorialService historialService) {
        this.historialService = historialService;
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
}