package umg.programacionIII.Repo_Spring.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import umg.programacionIII.model.Tarea;
import umg.programacionIII.service.TareaService;

import java.util.Map;

@Component
public class TareaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(TareaConsumer.class);
    private final TareaService tareaService;
    private final ObjectMapper objectMapper;

    public TareaConsumer(TareaService tareaService, ObjectMapper objectMapper) {
        this.tareaService = tareaService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue}", concurrency = "1")
    public void procesarMensaje(Map<String, Object> mensaje) {
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

            String operacion = (String) mensaje.get("operacion");

            switch (operacion) {
                case "CREAR":
                    Tarea nuevaTarea = objectMapper.convertValue(mensaje.get("tarea"), Tarea.class);
                    logger.info("Creando tarea: {}", nuevaTarea);
                    tareaService.save(nuevaTarea);
                    break;

                case "ACTUALIZAR":
                    Tarea tareaActualizar = objectMapper.convertValue(mensaje.get("tarea"), Tarea.class);
                    logger.info("Actualizando tarea: {}", tareaActualizar);
                    tareaService.save(tareaActualizar);
                    break;

                case "ELIMINAR":
                    Long id = ((Number) mensaje.get("id")).longValue();
                    logger.info("Eliminando tarea con ID: {}", id);
                    tareaService.deleteById(id);
                    break;

                default:
                    logger.error("Operación desconocida: {}", operacion);
                    throw new IllegalArgumentException("Operación desconocida: " + operacion);
            }

            logger.info("Mensaje procesado correctamente: {}", mensaje);
        } catch (Exception e) {
            logger.error("Error procesando mensaje: {}", e.getMessage(), e);
        }
    }
}