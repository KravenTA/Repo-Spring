package umg.programacionIII.Repo_Spring.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import umg.programacionIII.model.Tarea;
import umg.programacionIII.service.TareaService;
import umg.programacionIII.Repo_Spring.dto.MensajeTareaDTO;

@Component
public class TareaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(TareaConsumer.class);
    private final TareaService tareaService;

    public TareaConsumer(TareaService tareaService) {
        this.tareaService = tareaService;
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

            switch (operacion) {
                case "CREAR":
                case "ACTUALIZAR":
                    Tarea tarea = mensaje.getTarea();
                    logger.info("Procesando tarea: {}", tarea);
                    tareaService.save(tarea);
                    break;

                case "ELIMINAR":
                    Long id = mensaje.getId();
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