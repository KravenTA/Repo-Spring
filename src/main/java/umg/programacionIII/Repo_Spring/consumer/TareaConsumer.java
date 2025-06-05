package umg.programacionIII.Repo_Spring.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import umg.programacionIII.model.Historial;
import umg.programacionIII.model.Tarea;
import umg.programacionIII.model.Usuario;
import umg.programacionIII.service.HistorialService;
import umg.programacionIII.service.TareaService;
import umg.programacionIII.service.UsuarioService;
import umg.programacionIII.Repo_Spring.dto.MensajeTareaDTO;
import umg.programacionIII.estructuras.lista.Opcional;

import java.time.LocalDateTime;

@Component
public class TareaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(TareaConsumer.class);
    private final TareaService tareaService;
    private final HistorialService historialService;
    private final UsuarioService usuarioService;

    public TareaConsumer(TareaService tareaService, HistorialService historialService, UsuarioService usuarioService) {
        this.tareaService = tareaService;
        this.historialService = historialService;
        this.usuarioService = usuarioService;
    }

    @RabbitListener(queues = "${rabbitmq.queue}", concurrency = "1")
    public void procesarMensaje(MensajeTareaDTO mensaje) {
        try {
            // Registrar recepción del mensaje
            logger.info("Mensaje recibido: {}", mensaje);

            // Añadir un retraso para poder ver el mensaje en la interfaz de RabbitMQ
            try {
                Thread.sleep(5000); // Pausa de 5 segundos
                logger.info("Procesando mensaje después de pausa: {}", mensaje);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            String operacion = mensaje.getOperacion();
            Long usuarioId = null;

            switch (operacion) {
                case "CREAR":
                case "ACTUALIZAR":
                    Tarea tarea = mensaje.getTarea();
                    logger.info("Procesando tarea: {}", tarea);
                    if (tarea.getUsuario() != null) {
                        usuarioId = tarea.getUsuario().getId();
                    }
                    tareaService.save(tarea);
                    break;

                case "ELIMINAR":
                    Long id = mensaje.getId();
                    logger.info("Eliminando tarea con ID: {}", id);
                    // Obtener usuarioId antes de eliminar si no está en el mensaje
                    if (mensaje.getUsuarioId() == null) {
                        Opcional<Tarea> tareaOpcional = tareaService.findById(id);
                        if (tareaOpcional.estaPresente() && tareaOpcional.obtener().getUsuario() != null) {
                            usuarioId = tareaOpcional.obtener().getUsuario().getId();
                        }
                    } else {
                        usuarioId = mensaje.getUsuarioId();
                    }
                    tareaService.deleteById(id);
                    break;

                default:
                    logger.error("Operación desconocida: {}", operacion);
                    throw new IllegalArgumentException("Operación desconocida: " + operacion);
            }

            // Registrar en historial si es necesario
            if (mensaje.isRegistrarHistorial()) {
                Historial historial = new Historial();
                historial.setAccion(mensaje.getAccionHistorial());
                historial.setFecha(LocalDateTime.now());

                // Solo establecemos el usuario si tenemos el ID
                if (usuarioId != null) {
                    Opcional<Usuario> usuarioOpcional = usuarioService.findById(usuarioId);
                    if (usuarioOpcional.estaPresente()) {
                        historial.setUsuario(usuarioOpcional.obtener());
                    }
                }

                historialService.save(historial);
                logger.info("Registro en historial guardado: {}", historial);
            }

            logger.info("Mensaje procesado correctamente: {}", mensaje);
        } catch (Exception e) {
            logger.error("Error procesando mensaje: {}", e.getMessage(), e);
        }
    }
}