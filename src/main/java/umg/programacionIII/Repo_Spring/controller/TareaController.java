package umg.programacionIII.Repo_Spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umg.programacionIII.model.Tarea;
import umg.programacionIII.service.TareaService;
import umg.programacionIII.estructuras.lista.Lista;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.core.MessageProperties;
import umg.programacionIII.estructuras.lista.Opcional;
import umg.programacionIII.Repo_Spring.dto.MensajeTareaDTO;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private static final Logger logger = LoggerFactory.getLogger(TareaController.class);
    private final TareaService tareaService;
    private final RabbitTemplate rabbitTemplate;
    private final AtomicInteger messageCounter = new AtomicInteger(0);

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
        Opcional<Tarea> tarea = tareaService.findById(id);
        return tarea.estaPresente() ?
                ResponseEntity.ok(tarea.obtener()) :
                ResponseEntity.notFound().build();
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

        int idMensaje = messageCounter.incrementAndGet();
        MensajeTareaDTO mensaje = new MensajeTareaDTO("CREAR", tarea, idMensaje);

        logger.info("Enviando mensaje para crear tarea: {}", mensaje);

        // Enviamos el mensaje con una prioridad media (5)
        rabbitTemplate.convertAndSend(exchange, routingKey, mensaje, message -> {
            MessageProperties props = message.getMessageProperties();
            props.setPriority(5);
            return message;
        });

        return ResponseEntity.ok().body("Tarea enviada para procesamiento con ID: " + idMensaje);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Tarea tarea) {
        if (tarea.getDescripcion() == null || tarea.getDescripcion().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("La descripción de la tarea no puede estar vacía");
        }

        tarea.setId(id);
        int idMensaje = messageCounter.incrementAndGet();
        MensajeTareaDTO mensaje = new MensajeTareaDTO("ACTUALIZAR", tarea, idMensaje);

        logger.info("Enviando mensaje para actualizar tarea: {}", mensaje);

        rabbitTemplate.convertAndSend(exchange, routingKey, mensaje);

        return ResponseEntity.ok().body("Tarea enviada para actualización con ID: " + idMensaje);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        int idMensaje = messageCounter.incrementAndGet();
        MensajeTareaDTO mensaje = new MensajeTareaDTO("ELIMINAR", id, idMensaje);

        logger.info("Enviando mensaje para eliminar tarea: {}", mensaje);

        rabbitTemplate.convertAndSend(exchange, routingKey, mensaje);

        return ResponseEntity.ok("Operación de eliminación enviada con ID: " + idMensaje);
    }
}