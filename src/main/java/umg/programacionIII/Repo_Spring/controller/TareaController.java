package umg.programacionIII.Repo_Spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umg.programacionIII.model.Tarea;
import umg.programacionIII.service.TareaService;
import umg.programacionIII.estructuras.lista.Lista;

import java.util.Optional;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @GetMapping
    public Lista<Tarea> obtenerTodas() {
        return tareaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarea> obtenerPorId(@PathVariable Long id) {
        Optional<Tarea> tarea = tareaService.findById(id);
        return tarea.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public Lista<Tarea> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return tareaService.findByUsuarioId(usuarioId);
    }

    @GetMapping("/finalizada/{estado}")
    public Lista<Tarea> obtenerPorEstadoFinalizada(@PathVariable boolean estado) {
        return tareaService.findByFinalizada(estado);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Tarea tarea) {
        if (tarea.getDescripcion() == null || tarea.getDescripcion().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("La descripción de la tarea no puede estar vacía");
        }
        return ResponseEntity.ok(tareaService.save(tarea));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tareaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
