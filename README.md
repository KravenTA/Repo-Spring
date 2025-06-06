# Repo-Spring

**Repo-Spring** es un proyecto desarrollado con **Java 17** y **Spring Boot 3.4.5**, orientado a la gestión de tareas utilizando estructuras de datos personalizadas y comunicación asincrónica mediante **RabbitMQ**. Este proyecto implementa una arquitectura REST, integra lógica de negocio con listas enlazadas propias y una capa de mensajería para mayor escalabilidad.

---

## 🔧 Tecnologías utilizadas

- Java 17
- Spring Boot 3.4.5
- Maven
- Spring Web
- Spring AMQP (RabbitMQ)
- SLF4J (Logging)
- Estructuras de datos personalizadas (`Lista`, `Nodo`, `Opcional`)

---

## 📁 Estructura del Proyecto

src/
├── controller/ # Controladores REST (TareaController)
├── model/ # Modelo de dominio (Tarea)
├── service/ # Servicios (TareaService, HistorialService)
├── estructuras/lista/ # Lista enlazada propia, Nodo y Opcional
├── dto/ # Objeto para envío de mensajes (MensajeTareaDTO)
└── resources/
└── application.properties # Configuración general y RabbitMQ


---

## 🧠 Funcionalidad

- Almacena tareas en una lista enlazada propia (sin usar colecciones Java).
- Permite operaciones básicas de CRUD (crear, leer, actualizar y eliminar tareas).
- Envía un mensaje a una cola RabbitMQ cada vez que se registra una nueva tarea.
- Mantiene historial de acciones mediante estructuras propias.

---

## 📡 Endpoints de la API REST

| Método | Ruta                     | Descripción                          |
|--------|--------------------------|--------------------------------------|
| GET    | `/api/tareas`            | Obtiene la lista completa de tareas |
| GET    | `/api/tareas/{id}`       | Devuelve una tarea por su ID        |
| POST   | `/api/tareas`            | Crea una nueva tarea y envía mensaje a RabbitMQ |
| PUT    | `/api/tareas/{id}`       | Actualiza una tarea existente       |
| DELETE | `/api/tareas/{id}`       | Elimina una tarea                   |
| GET    | `/api/historiales`       | Muestra el historial de acciones    |

---

## 📨 Integración con RabbitMQ

Cada vez que se crea una tarea, el sistema genera y envía un `MensajeTareaDTO` a RabbitMQ.

### Configuración esperada en `application.properties`:

```properties
server.port=8081

spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

---

## 🧠 Funcionalidad

- Almacena tareas en una lista enlazada propia (sin usar colecciones Java).
- Permite operaciones básicas de CRUD (crear, leer, actualizar y eliminar tareas).
- Envía un mensaje a una cola RabbitMQ cada vez que se registra una nueva tarea.
- Mantiene historial de acciones mediante estructuras propias.

---

## 📡 Endpoints de la API REST

| Método | Ruta                     | Descripción                          |
|--------|--------------------------|--------------------------------------|
| GET    | `/api/tareas`            | Obtiene la lista completa de tareas |
| GET    | `/api/tareas/{id}`       | Devuelve una tarea por su ID        |
| POST   | `/api/tareas`            | Crea una nueva tarea y envía mensaje a RabbitMQ |
| PUT    | `/api/tareas/{id}`       | Actualiza una tarea existente       |
| DELETE | `/api/tareas/{id}`       | Elimina una tarea                   |
| GET    | `/api/historiales`       | Muestra el historial de acciones    |

---

## 📨 Integración con RabbitMQ

Cada vez que se crea una tarea, el sistema genera y envía un `MensajeTareaDTO` a RabbitMQ.

### Configuración esperada en `application.properties`:

```properties
server.port=8081

spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

Ejecución de RabbitMQ con Docker:
docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:3-management
Interfaz de administración:
📍 http://localhost:15672
Usuario: guest | Contraseña: guest

▶️ Cómo ejecutar el proyecto

1.Clona el repositorio:
git clone https://tu-repo.com/Repo-Spring.git
cd Repo-Spring
2. Ejecuta el proyecto:
./mvnw spring-boot:run
3.La aplicación estará disponible en:
🌐 http://localhost:8081/api/tareas
