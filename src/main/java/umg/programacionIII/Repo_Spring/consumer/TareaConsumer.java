package umg.programacionIII.Repo_Spring.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import umg.programacionIII.model.Tarea;
import umg.programacionIII.service.TareaService;

import java.util.Map;

@Component
public class TareaConsumer {

    private final TareaService tareaService;
    private final ObjectMapper objectMapper;

    public TareaConsumer(TareaService tareaService, ObjectMapper objectMapper) {
        this.tareaService = tareaService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void procesarMensaje(Map<String, Object> mensaje) {
        try {
            String operacion = (String) mensaje.get("operacion");

            switch (operacion) {
                case "CREAR":
                    Tarea nuevaTarea = objectMapper.convertValue(mensaje.get("tarea"), Tarea.class);
                    tareaService.save(nuevaTarea);
                    break;

                case "ACTUALIZAR":
                    Tarea tareaActualizar = objectMapper.convertValue(mensaje.get("tarea"), Tarea.class);
                    tareaService.save(tareaActualizar);
                    break;

                case "ELIMINAR":
                    Long id = ((Number) mensaje.get("id")).longValue();
                    tareaService.deleteById(id);
                    break;

                default:
                    throw new IllegalArgumentException("Operación desconocida: " + operacion);
            }
        } catch (Exception e) {
            // Aquí podrías implementar un manejo de errores más robusto
            // como reintentar la operación o guardar en una cola de errores
            System.err.println("Error procesando mensaje: " + e.getMessage());
        }
    }
}
