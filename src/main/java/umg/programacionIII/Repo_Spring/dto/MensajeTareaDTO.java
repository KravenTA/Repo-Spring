package umg.programacionIII.Repo_Spring.dto;

import umg.programacionIII.model.Tarea;

public class MensajeTareaDTO {
    private String operacion;
    private Tarea tarea;
    private Long id;
    private int idMensaje;

    // Constructores
    public MensajeTareaDTO() {}

    // Constructor para operaciones CREAR/ACTUALIZAR
    public MensajeTareaDTO(String operacion, Tarea tarea, int idMensaje) {
        this.operacion = operacion;
        this.tarea = tarea;
        this.idMensaje = idMensaje;
    }

    // Constructor para operación ELIMINAR
    public MensajeTareaDTO(String operacion, Long id, int idMensaje) {
        this.operacion = operacion;
        this.id = id;
        this.idMensaje = idMensaje;
    }

    // Getters y setters
    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }
}
