package umg.programacionIII.Repo_Spring.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umg.programacionIII.model.Tarea;
import umg.programacionIII.service.TareaService;
import umg.programacionIII.estructuras.lista.Lista;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    public TareaController(TareaService tareaService, RabbitTemplate rabbitTemplate) {
        this.tareaService = tareaService;
        this.rabbitTemplate = rabbitTemplate;
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

        Map<String, Object> mensaje = new HashMap<>();
        mensaje.put("operacion", "CREAR");
        mensaje.put("tarea", tarea);

        rabbitTemplate.convertAndSend(exchange, routingKey, mensaje);

        return ResponseEntity.ok().body("Tarea enviada para procesamiento");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Tarea tarea) {
        if (tarea.getDescripcion() == null || tarea.getDescripcion().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("La descripción de la tarea no puede estar vacía");
        }

        tarea.setId(id);
        Map<String, Object> mensaje = new HashMap<>();
        mensaje.put("operacion", "ACTUALIZAR");
        mensaje.put("tarea", tarea);

        rabbitTemplate.convertAndSend(exchange, routingKey, mensaje);

        return ResponseEntity.ok().body("Tarea enviada para actualización");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Map<String, Object> mensaje = new HashMap<>();
        mensaje.put("operacion", "ELIMINAR");
        mensaje.put("id", id);

        rabbitTemplate.convertAndSend(exchange, routingKey, mensaje);

        return ResponseEntity.noContent().build();
    }
}