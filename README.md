# 📦 Repo-Spring

**Repo-Spring** es un proyecto construido con **Java 17** y **Spring Boot 3.4.5** para la gestión de tareas. Integra estructuras de datos personalizadas (listas enlazadas), persistencia con MySQL y mensajería asincrónica usando **RabbitMQ**, con una arquitectura RESTful clara y escalable.

---

## 🔧 Tecnologías Utilizadas

- Java 17  
- Spring Boot 3.4.5  
- Maven  
- Spring Web
- Spring Data JPA
- MySQL  
- Spring AMQP (RabbitMQ)  
- Springdoc OpenAPI (Swagger)
- SLF4J  
- Estructuras personalizadas (`Lista`, `Nodo`, `Opcional`)

---

## 📁 Estructura del Proyecto

src/main/java/umg/programacionIII/
├── Repo_Spring/
│   ├── controller/          # TareaController, HistorialController, UsuarioController
│   ├── consumer/            # TareaConsumer para mensajes RabbitMQ
│   ├── config/              # RabbitMQConfig, SwaggerConfig
│   ├── dto/                 # MensajeTareaDTO
│   ├── serializer/          # ListaSerializer
│   └── RepoSpringApplication.java
├── model/                   # Tarea, Historial, Usuario
├── service/                 # TareaService, HistorialService, UsuarioService
├── repository/              # Interfaces para acceso a datos
└── estructuras/lista/       # Lista.java, Nodo.java, Opcional.java (estructuras personalizadas)

---

## 🧠 Funcionalidad Principal

- Utiliza **estructuras de datos personalizadas** (Lista enlazada) para manejar colecciones
- Permite operaciones **CRUD** sobre tareas, usuarios e historial
- Implementa arquitectura de **mensajería asincrónica** con RabbitMQ
- Registra cada acción en el historial para auditoría
- Incluye funcionalidad para **deshacer acciones** previas
- Ofrece **documentación interactiva** con Swagger/OpenAPI
- Persistencia en base de datos MySQL

---

## 📡 Endpoints de la API REST

### Tareas
| Método | Ruta                            | Descripción                          |
|--------|--------------------------------|--------------------------------------|
| GET    | `/api/tareas`                  | Lista todas las tareas               |
| GET    | `/api/tareas/{id}`             | Obtiene una tarea por ID             |
| GET    | `/api/tareas/usuario/{id}`     | Lista tareas por usuario             |
| GET    | `/api/tareas/finalizada/{estado}` | Filtra por estado de finalización |
| POST   | `/api/tareas`                  | Crea una tarea (vía RabbitMQ)        |
| PUT    | `/api/tareas/{id}`             | Actualiza una tarea existente        |
| DELETE | `/api/tareas/{id}`             | Elimina una tarea                    |

### Historial
| Método | Ruta                            | Descripción                          |
|--------|--------------------------------|--------------------------------------|
| GET    | `/api/historiales`             | Lista todo el historial              |
| GET    | `/api/historiales/{id}`        | Obtiene un registro por ID           |
| GET    | `/api/historiales/usuario/{id}`| Filtra historial por usuario         |
| POST   | `/api/historiales/deshacer/{id}`| Deshace la última acción de un usuario |
| POST   | `/api/historiales`             | Crea un registro de historial        |
| DELETE | `/api/historiales/{id}`        | Elimina un registro                  |

### Usuarios
| Método | Ruta                            | Descripción                          |
|--------|--------------------------------|--------------------------------------|
| GET    | `/api/usuarios`                | Lista todos los usuarios             |
| GET    | `/api/usuarios/{id}`           | Obtiene un usuario por ID            |
| POST   | `/api/usuarios`                | Crea un nuevo usuario                |
| DELETE | `/api/usuarios/{id}`           | Elimina un usuario                   |

---

## 📨 Integración con RabbitMQ

El proyecto implementa un patrón de mensajería asincrónica mediante RabbitMQ para las operaciones CRUD de tareas:

1. **TareaController** envía mensajes con las operaciones a realizar
2. **TareaConsumer** procesa los mensajes y ejecuta las operaciones
3. Se registran todas las acciones en el historial

### Configuración de RabbitMQ:

```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

rabbitmq.queue=tarea.queue
rabbitmq.exchange=tarea.exchange
rabbitmq.routingkey=tarea.routingkey

Ejecución de RabbitMQ con Docker: 
docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:3-management
Interfaz de administración: http://localhost:15672
Usuario: guest
Contraseña: guest

📝 Documentación de la API
La API está documentada con OpenAPI/Swagger:  
URL: http://localhost:8081/swagger-ui.html
Especificación JSON: http://localhost:8081/api-docs
