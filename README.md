# Sistema de Arriendo de Bicicletas
### Evaluación 3 — Desarrollo FullStack | Arquitectura de Microservicios

---

## Contexto del Proyecto

Sistema de arriendo de bicicletas desarrollado bajo arquitectura de microservicios con **Spring Boot 3.5** y **Spring Cloud**. El sistema gestiona el ciclo completo de arriendo: registro de usuarios, disponibilidad de bicicletas, reservas, préstamos activos, cálculo de pagos y notificaciones de eventos del dominio.

Cada funcionalidad está encapsulada en un microservicio independiente que se comunica a través de REST usando **OpenFeign** y se registra en un servidor de descubrimiento **Eureka**. El acceso externo se centraliza a través de un **API Gateway** con autenticación **JWT**.

```
Cliente HTTP
     │
     ▼
API Gateway  :8080  ── JWT Filter 
     │
     ▼
Eureka Server  :8761  ── Servicio de descubrimiento (lb://)
     │
     ├── ms-auth         :8088
     ├── ms-user         :8081
     ├── ms-bicicleta    :8082
     ├── ms-reservas     :8083
     ├── ms-rental       :8084
     ├── ms-pago         :8085
     └── ms-notificacion :8087
```

---

## Integrantes

| Nombre | Rol |
|---|---|
| Isaias Collao | Desarrollador FullStack |

---

## Microservicios Implementados

| Microservicio | Puerto | Base de datos | Descripción |
|---|---|---|---|
| `eureka-server` | 8761 | — | Servidor de descubrimiento de servicios Spring Cloud |
| `api-gateway` | 8080 | — | Gateway centralizado con filtro JWT y enrutamiento |
| `ms-auth` | 8088 | — | Autenticación y generación de tokens JWT con Spring Security |
| `ms-user` | 8081 | `users_db` | CRUD de usuarios con BCrypt para hashing de contraseñas |
| `ms-bicicleta` | 8082 | `bikes_db` | Gestión de bicicletas con máquina de estados DISPONIBLE/OCUPADA |
| `ms-reservas` | 8083 | `reservations_db` | Reservas con validaciones de negocio y control de duplicados |
| `ms-rental` | 8084 | `rentals_db` | Ciclo de préstamo con comunicación Feign hacia 4 microservicios |
| `ms-pago` | 8085 | `payments_db` | Cálculo de tarifas por tiempo de uso y registro de pagos |
| `ms-notificacion` | 8087 | — | Dispatcher de notificaciones para eventos del dominio |

---

## Rutas Principales del Gateway

Todas las rutas pasan por `http://localhost:8080`. El JWT se requiere en todas excepto `/api/auth/login` y `/api/usuarios` (registro).

| Método | Ruta | Microservicio | Descripción |
|---|---|---|---|
| `POST` | `/api/auth/login` | ms-auth | Autenticación — retorna JWT |
| `GET` | `/api/usuarios` | ms-user | Listar todos los usuarios |
| `POST` | `/api/usuarios` | ms-user | Registrar nuevo usuario |
| `GET` | `/api/usuarios/{id}` | ms-user | Buscar usuario por ID |
| `PUT` | `/api/usuarios/{id}` | ms-user | Actualizar datos del usuario |
| `DELETE` | `/api/usuarios/{id}` | ms-user | Eliminar usuario |
| `GET` | `/api/bicicletas` | ms-bicicleta | Listar bicicletas |
| `POST` | `/api/bicicletas` | ms-bicicleta | Registrar bicicleta |
| `PUT` | `/api/bicicletas/{id}` | ms-bicicleta | Actualizar datos |
| `PUT` | `/api/bicicletas/{id}/ocupar` | ms-bicicleta | Cambiar estado a OCUPADA |
| `PUT` | `/api/bicicletas/{id}/liberar` | ms-bicicleta | Cambiar estado a DISPONIBLE |
| `DELETE` | `/api/bicicletas/{id}` | ms-bicicleta | Eliminar bicicleta |
| `GET` | `/api/reservas` | ms-reservas | Listar reservas |
| `POST` | `/api/reservas` | ms-reservas | Crear reserva (valida disponibilidad) |
| `PUT` | `/api/reservas/{id}/cancelar` | ms-reservas | Cancelar reserva activa |
| `PUT` | `/api/reservas/{id}/utilizar` | ms-reservas | Completar reserva |
| `POST` | `/api/rentals/iniciar` | ms-rental | Iniciar préstamo de bicicleta |
| `PUT` | `/api/rentals/{id}/finalizar` | ms-rental | Finalizar préstamo activo |
| `GET` | `/api/rentals` | ms-rental | Listar préstamos |
| `POST` | `/api/pagos/calcular` | ms-pago | Calcular monto según tiempo de uso |
| `PUT` | `/api/pagos/{id}/pagar` | ms-pago | Registrar pago como PAGADO |
| `GET` | `/api/pagos/usuario/{id}` | ms-pago | Pagos de un usuario |
| `POST` | `/api/notificacion/enviar` | ms-notificacion | Enviar notificación de evento |

---

## Documentación Swagger / OpenAPI

Cada microservicio expone su documentación interactiva en `/swagger-ui/index.html` (no requiere autenticación):

| Microservicio | URL Swagger local |
|---|---|
| ms-auth | http://localhost:8088/swagger-ui/index.html |
| ms-user | http://localhost:8081/swagger-ui/index.html |
| ms-bicicleta | http://localhost:8082/swagger-ui/index.html |
| ms-reservas | http://localhost:8083/swagger-ui/index.html |
| ms-rental | http://localhost:8084/swagger-ui/index.html |
| ms-pago | http://localhost:8085/swagger-ui/index.html |
| ms-notificacion | http://localhost:8087/swagger-ui/index.html |

---

## Ejecución Local

### Requisitos previos

- Java 21+
- Maven 3.9+
- MySQL 8.0 corriendo en `localhost:3306`

### 1. Crear bases de datos

```sql
CREATE DATABASE users_db;
CREATE DATABASE bikes_db;
CREATE DATABASE reservations_db;
CREATE DATABASE rentals_db;
CREATE DATABASE payments_db;
```

> Las tablas se crean automáticamente al primer arranque (`ddl-auto: update`).

### 2. Configurar credenciales MySQL

En cada `src/main/resources/application.yaml` ajusta si tu MySQL tiene contraseña:

```yaml
spring:
  datasource:
    username: root
    password: "tu_password"
```

### 3. Orden de arranque

```bash
# Terminal 1 — Eureka Server (primero siempre)
cd eureka-server/eureka-server
./mvnw spring-boot:run

# Terminal 2-8 — Microservicios de dominio (cualquier orden)
cd ms-user/ms-user && ./mvnw spring-boot:run
cd ms-auth/ms-auth && ./mvnw spring-boot:run
cd ms-bicicleta/ms-bicicleta && ./mvnw spring-boot:run
cd ms-reservas/ms-reservas && ./mvnw spring-boot:run
cd ms-rental/ms-rental && ./mvnw spring-boot:run
cd ms-pago/ms-pago && ./mvnw spring-boot:run
cd ms-notificacion/ms-notificacion && ./mvnw spring-boot:run

# Terminal 9 — API Gateway (último)
cd api-gateway/api-gateway && ./mvnw spring-boot:run
```

### 4. Verificar que todo está levantado

- **Eureka Dashboard**: http://localhost:8761 — deben aparecer los 7 microservicios registrados
- **API Gateway**: http://localhost:8080

### 5. Primer request de prueba

```bash
# Crear usuario
POST http://localhost:8080/api/usuarios
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan@test.com",
  "password": "123456",
  "estado": "ACTIVO"
}

# Login
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "juan@test.com",
  "password": "123456"
}
# Respuesta: { "token": "eyJ..." }
```


## Pruebas Unitarias

El proyecto incluye pruebas unitarias distribuidas en 7 microservicios usando **JUnit 5 + Mockito**.

```bash
# Ejecutar tests de un microservicio
cd ms-bicicleta/ms-bicicleta
./mvnw test

# Ejecutar una clase específica
./mvnw test -Dtest=BicicletaServiceTest

# Ejecutar un método específico
./mvnw test -Dtest=BicicletaServiceTest#crearBicicleta_codigoDuplicado_lanzaExcepcion

## Stack Tecnológico

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 21 | Lenguaje principal |
| Spring Boot | 3.5.x | Framework base |
| Spring Cloud Gateway | 2025.0.x | API Gateway |
| Spring Cloud Eureka | 2025.0.x | Service Discovery |
| Spring Security + JJWT | — | Autenticación JWT |
| Spring Data JPA | — | Persistencia |
| MySQL | 8.0 | Base de datos |
| OpenFeign | — | Comunicación REST inter-servicios |
| Springdoc OpenAPI | 2.8.9 | Documentación Swagger |
| JUnit 5 + Mockito | — | Pruebas unitarias |
| Lombok | — | Reducción de boilerplate |
| Docker + Compose | — | Contenedores y despliegue |
