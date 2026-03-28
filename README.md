<div align="center">

# TechCup Futbol

**Plataforma digital para la gestion del torneo semestral de futbol**

Programa de Ingenieria de Sistemas, Ingenieria de Inteligencia Artificial, Ingenieria de Ciberseguridad e Ingenieria Estadistica

**Escuela Colombiana de Ingenieria Julio Garavito**

---

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.4-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-47A248?logo=mongodb)](https://www.mongodb.com/atlas)
[![React](https://img.shields.io/badge/React-TypeScript-61DAFB?logo=react)](https://react.dev/)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven)](https://maven.apache.org/)
[![Jira](https://img.shields.io/badge/Jira-Project-0052CC?logo=jira)](https://n9.cl/m2as4l)

</div>

---

## Descripcion

**TechCup Futbol** es una aplicacion web que centraliza toda la gestion del torneo interno de futbol, reemplazando los procesos manuales actuales (WhatsApp, formularios aislados y hojas de calculo) por un sistema organizado y transparente.

La plataforma permite administrar todas las etapas del torneo desde un solo lugar.

---

## Objetivo del Proyecto

Modernizar la organizacion del torneo universitario mediante una plataforma que permita:

- **Mejorar la organizacion** del torneo
- **Reducir errores** administrativos
- **Facilitar la comunicacion** entre jugadores, capitanes, arbitros y organizadores
- **Brindar transparencia** en resultados, estadisticas y clasificacion

---

## Funcionalidades Principales

| Modulo | Funcionalidad |
|--------|---------------|
| Usuarios | Registro e inscripcion de jugadores y equipos |
| Torneos | Creacion y administracion de torneos |
| Pagos | Gestion de pagos mediante comprobantes |
| Configuracion | Calendarios, reglamentos y canchas |
| Partidos | Registro de partidos y resultados |
| Clasificacion | Tabla de posiciones automatica |
| Eliminatorias | Llaves eliminatorias |
| Estadisticas | Estadisticas y rankings |
| Alineaciones | Gestion de alineaciones |

---

## Arquitectura Tecnologica

| Capa | Tecnologia |
|------|------------|
| Backend | Spring Boot (Java) — API REST con MongoDB |
| Base de Datos | MongoDB Atlas |
| Build Tool | Maven |
| Gestion del proyecto | Jira |

---

## Integrantes del Equipo

| Nombre |
|--------|
| Joshua David Quiroga Landazabal |
| Juan David Valero Abril |
| Juan Carlos Bohorquez Monroy |
| Carlos Andres Uribe Vargas |
| Andres Felipe Savogal Wilches |

- **Materia:** Ciclos de Desarrollo de Software (DOSW)
- **Profesor:** Andres Martin Cantor Urrego
- **Periodo:** 2026-1

---

## Enlaces del Proyecto

| Recurso | Enlace |
|---------|--------|
| Jira (Gestion del proyecto) | [https://n9.cl/m2as4l](https://n9.cl/m2as4l) |
| Diagrama de Arquitectura (Lucidchart) | [Ver diagrama](https://lucid.app/lucidchart/d035776f-7c94-4150-9fb5-55bcacb674df/edit?viewport_loc=-396%2C340%2C3964%2C1684%2C0_0&invitationId=inv_ccfbc0fd-443f-4d4f-b7a1-53f4693b6fe2) |
| Diagrama de Clases y Secuencia (Draw.io) | [Ver diagramas](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=Diagrama%20de%20clases%20y%20secuencia.drawio&dark=auto) |

---

## Estructura del Proyecto

El proyecto se organiza en tres grandes epicas:

```
E1 — Backend       (9 features, 24 HUs, 114 tareas)
E2 — Mobile/Frontend (7 features, 31 HUs,  96 tareas) [TBD]
E3 — Documentacion (3 features,  9 HUs,  20 tareas)
```

### E1 — Backend

#### F1.1 — Autenticacion y registro de usuarios
| ID | Historia de Usuario |
|----|-------------------|
| HU-B01 | Registrar usuario en el sistema |
| HU-B02 | Verificar correo electronico |
| HU-B03 | Iniciar sesion |

#### F1.2 — Perfil deportivo y disponibilidad
| ID | Historia de Usuario |
|----|-------------------|
| HU-B04 | Completar perfil deportivo |
| HU-B05 | Marcar disponibilidad |

#### F1.3 — Gestion del torneo
| ID | Historia de Usuario |
|----|-------------------|
| HU-B06 | Crear torneo |
| HU-B07 | Configurar torneo |
| HU-B08 | Cambiar estado del torneo |
| HU-B09 | Consultar torneo |

#### F1.4 — Gestion de equipos y plantilla
| ID | Historia de Usuario |
|----|-------------------|
| HU-B10 | Crear equipo |
| HU-B11 | Buscar jugadores disponibles |
| HU-B12 | Invitar jugador al equipo |
| HU-B13 | Responder invitacion |
| HU-B14 | Quitar jugador del equipo |

#### F1.5 — Inscripcion y pagos
| ID | Historia de Usuario |
|----|-------------------|
| HU-B15 | Subir comprobante de pago |
| HU-B16 | Revisar y aprobar pagos |

#### F1.6 — Partidos y alineaciones
| ID | Historia de Usuario |
|----|-------------------|
| HU-B17 | Organizar alineacion del equipo |
| HU-B18 | Consultar alineacion rival |
| HU-B19 | Registrar resultado del partido |
| HU-B20 | Consultar partidos asignados |

#### F1.7 — Resultados y clasificacion
| ID | Historia de Usuario |
|----|-------------------|
| HU-B21 | Ver tabla de posiciones |
| HU-B22 | Ver llaves eliminatorias |

#### F1.8 — Estadisticas
| ID | Historia de Usuario |
|----|-------------------|
| HU-B23 | Consultar estadisticas del torneo |

#### F1.9 — Administracion
| ID | Historia de Usuario |
|----|-------------------|
| HU-B24 | Gestionar usuarios y roles |

---

### E2 — Mobile/Frontend (TBD)

#### F2.1 — Identidad visual y mockups
| ID | Historia de Usuario |
|----|-------------------|
| HU-F01 | Manual de identidad visual |
| HU-F02 | Mockups de registro, login y perfil |
| HU-F03 | Mockups de torneo y configuracion |
| HU-F04 | Mockups de equipos, invitaciones y pagos |
| HU-F05 | Mockups de partidos, alineaciones y resultados |
| HU-F06 | Mockups de notificaciones y administracion |

#### F2.2 — Registro, autenticacion y perfil
| ID | Historia de Usuario |
|----|-------------------|
| HU-F07 | Pantalla de registro |
| HU-F08 | Pantalla de login |
| HU-F09 | Verificacion de correo |
| HU-F10 | Proteccion de rutas por rol |
| HU-F11 | Perfil deportivo |
| HU-F12 | Toggle de disponibilidad |

#### F2.3 — Torneo y configuracion
| ID | Historia de Usuario |
|----|-------------------|
| HU-F13 | Formulario de creacion de torneo |
| HU-F14 | Panel de configuracion del torneo |
| HU-F15 | Gestion de estados del torneo |
| HU-F16 | Vista publica del torneo |

#### F2.4 — Equipos, invitaciones y pagos
| ID | Historia de Usuario |
|----|-------------------|
| HU-F17 | Creacion de equipo |
| HU-F18 | Buscador de jugadores disponibles |
| HU-F19 | Sistema de invitaciones |
| HU-F20 | Gestion de plantilla |
| HU-F21 | Subir comprobante de pago |
| HU-F22 | Panel de revision de pagos |

#### F2.5 — Partidos y alineaciones
| ID | Historia de Usuario |
|----|-------------------|
| HU-F23 | Organizar alineacion |
| HU-F24 | Alineacion rival |
| HU-F25 | Registro de resultado |
| HU-F26 | Panel del arbitro |

#### F2.6 — Resultados, clasificacion y estadisticas
| ID | Historia de Usuario |
|----|-------------------|
| HU-F27 | Tabla de posiciones |
| HU-F28 | Bracket eliminatorio |
| HU-F29 | Vista de estadisticas |

#### F2.7 — Notificaciones y administracion
| ID | Historia de Usuario |
|----|-------------------|
| HU-F30 | Bandeja de notificaciones |
| HU-F31 | Panel de administracion |

---

### E3 — Documentacion y Artefactos

#### F3.1 — Arquitectura y diagramas
| ID | Historia de Usuario |
|----|-------------------|
| HU-D01 | Diagrama de clases |
| HU-D02 | Diagrama de componentes general |
| HU-D03 | Diagrama de componentes especifico |
| HU-D04 | Diagrama Entidad-Relacion |
| HU-D05 | Diagramas de secuencia |

#### F3.2 — Estructuracion del proyecto
| ID | Historia de Usuario |
|----|-------------------|
| HU-D06 | Estructurar proyecto Backend |
| HU-D07 | Estructurar proyecto Frontend |

#### F3.3 — Refinamiento de artefactos
| ID | Historia de Usuario |
|----|-------------------|
| HU-D08 | Refinamiento del analisis de requerimientos |
| HU-D09 | Documentacion tecnica |

---

## Conteo del Proyecto

| Epica | Features | Historias de Usuario | Tareas |
|-------|----------|---------------------|--------|
| Backend | 9 | 24 | 114 |
| Mobile/Frontend (TBD) | 7 | 31 | 96 |
| Documentacion | 3 | 9 | 20 |
| **Total** | **19** | **64** | **230** |

---

## Estructura de Paquetes (Backend)

```
src/main/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/
|
|-- config/                     # Configuracion (MongoDB)
|   +-- MongoConfig.java
|
|-- controller/                 # Controladores REST
|   |-- AuthController.java         POST /api/auth/register, /login
|   |-- PlayerController.java       PUT /api/players/{id}/profile
|   |-- TeamController.java         POST /api/teams, GET /{id}, DELETE /{id}/players/{pid}
|   +-- TournamentController.java   POST /api/tournaments, GET /{id}, PATCH /{id}/status
|
|-- dto/
|   |-- request/                # DTOs de entrada
|   |   |-- LoginRequest.java
|   |   |-- RegisterRequest.java
|   |   |-- ProfileRequest.java
|   |   |-- AvailabilityRequest.java
|   |   |-- TeamRequest.java
|   |   |-- TournamentRequest.java
|   |   +-- ChangeStatusRequest.java
|   +-- response/               # DTOs de salida
|       |-- LoginResponse.java
|       |-- UserResponse.java
|       |-- ProfileResponse.java
|       |-- TeamResponse.java
|       +-- TournamentResponse.java
|
|-- exception/                  # Manejo global de excepciones
|   |-- BusinessRuleException.java
|   |-- ResourceNotFoundException.java
|   |-- ErrorResponse.java
|   +-- GlobalExceptionHandler.java
|
|-- model/                      # Entidades y enums
|   |-- User.java
|   |-- Team.java
|   |-- Tournament.java
|   |-- SportProfile.java
|   |-- Role.java
|   |-- Position.java
|   +-- TournamentStatus.java
|
|-- repository/                 # Repositorios MongoDB
|   |-- UserRepository.java
|   |-- TeamRepository.java
|   |-- TournamentRepository.java
|   +-- SportProfileRepository.java
|
+-- service/
    |-- interface_/             # Interfaces de servicio
    |   |-- AuthService.java
    |   |-- PlayerService.java
    |   |-- TeamService.java
    |   +-- TournamentService.java
    +-- impl/                   # Implementaciones
        |-- AuthServiceImpl.java
        |-- PlayerServiceImpl.java
        |-- TeamServiceImpl.java
        +-- TournamentServiceImpl.java
```

---

## Endpoints de la API

### Auth (`/api/auth`)
| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| `POST` | `/api/auth/register` | Registrar nuevo usuario |
| `POST` | `/api/auth/login` | Iniciar sesion |
| `GET` | `/api/auth/verify-email?token=xxx` | Verificar correo electronico |
| `POST` | `/api/auth/resend-verification` | Reenviar correo de verificacion |
| `POST` | `/api/auth/refresh-token` | Renovar token JWT |

### Players (`/api/players`)
| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| `PUT` | `/api/players/{id}/profile` | Completar/actualizar perfil deportivo |
| `POST` | `/api/players/{id}/photo` | Subir foto de perfil |
| `PATCH` | `/api/players/{id}/availability` | Cambiar disponibilidad |
| `GET` | `/api/players/{id}/profile` | Consultar perfil de un jugador |

### Teams (`/api/teams`)
| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| `POST` | `/api/teams` | Crear equipo |
| `GET` | `/api/teams/{id}` | Obtener equipo por ID |
| `GET` | `/api/teams/{id}/roster` | Ver plantilla del equipo |
| `DELETE` | `/api/teams/{id}/players/{playerId}` | Quitar jugador del equipo |

### Tournaments (`/api/tournaments`)
| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| `POST` | `/api/tournaments` | Crear torneo |
| `GET` | `/api/tournaments/{id}` | Obtener torneo por ID |
| `PUT` | `/api/tournaments/{id}/config` | Configurar torneo |
| `PATCH` | `/api/tournaments/{id}/status` | Cambiar estado del torneo |

### Documentacion interactiva

#### Acceder a Swagger UI

La documentación interactiva de los endpoints está disponible a través de **Swagger UI / OpenAPI**.

**Pasos para acceder:**

1. **Ejecutar la aplicación** (si no está corriendo):
   ```bash
   mvn spring-boot:run
   ```

    Si vienes de cambios de código y quieres reiniciar limpio:
    ```bash
    mvn clean
    mvn clean compile
    mvn spring-boot:run
    ```

2. **Abrir el navegador** y acceder a:
   - **Swagger UI**: `http://localhost:8080/swagger-ui.html`
   - **OpenAPI JSON**: `http://localhost:8080/api-docs`

3. **Ver los endpoints** organizados por categorías:
- ✅ Autenticación (register, login, verify-email, resend-verification, refresh-token)
- ✅ Jugadores (profile, photo, availability, consulta de perfil)
- ✅ Equipos (crear, consultar, roster, quitar jugador)
- ✅ Torneos (crear, consultar, configurar, cambiar estado)

4. **Probar endpoints directamente** desde la UI:
   - Hacer clic en un endpoint para expandirlo
   - Hacer clic en "Try it out"
   - Completar los parámetros requeridos
   - Hacer clic en "Execute"

#### Configuración de Swagger (Ya habilitado)

La configuración de OpenAPI/Swagger está lista en los siguientes archivos:

**Archivo: `src/main/resources/application.properties`**
```properties
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
```

**Archivo: `src/main/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/config/OpenApiConfig.java`**
```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("TechCup Futbol - Backend API")
                    .version("1.0.0")
                    .description("Plataforma digital para la gestión del torneo semestral de fútbol")
                    ...);
    }
}
```

#### Dependencia en pom.xml

La dependencia de SpringDoc OpenAPI ya está configurada:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

#### Documentación de Endpoints

Todos los controladores tienen anotaciones de OpenAPI:


**Ejemplo en AuthController:**
```java
@PostMapping("/login")
@Operation(summary = "Iniciar sesión", 
           description = "Autentica un usuario y retorna un token JWT")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
})
public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    ...
}
```

#### Configuración de Seguridad

**Archivo: `src/main/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/config/SecurityConfig.java`**

La aplicación cuenta con una configuración de seguridad que permite acceso público a:
- ✅ **Swagger UI**: `/swagger-ui.html` y `/swagger-ui/**`
- ✅ **OpenAPI Spec**: `/v3/api-docs/**` y `/api-docs/**`
- ✅ **Todos los endpoints API**: `/api/**`

**No se requiere autenticación** para acceder a Swagger o probar los endpoints en desarrollo.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", 
                                 "/v3/api-docs/**", "/api/**")
                .permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())
            .httpBasic(basic -> basic.disable());
        return http.build();
    }
}
```
---
---

## Requisitos Previos

- **Java** 17+
- **Maven** 3.8+
- **MongoDB Atlas** (conexion configurada en `application.properties`)

---

## Instalacion y Ejecucion

```bash
# 1. Clonar el repositorio
git clone <url-del-repositorio>
cd Tech-Cup-DOSW-BackEnd-2026-1

# 2. Compilar el proyecto
mvn clean install

# 3. Ejecutar la aplicacion
mvn spring-boot:run

# 4. Acceder a Swagger UI
# Abrir en el navegador: http://localhost:8080/swagger-ui.html
```

---

## Configuracion

El archivo `src/main/resources/application.properties` contiene:

```properties
server.port=8080
spring.data.mongodb.uri=mongodb+srv://<user>:<password>@<cluster>/techcup_db
spring.data.mongodb.database=techcup_db
spring.jackson.time-zone=America/Bogota
```

---

<div align="center">

**Escuela Colombiana de Ingenieria Julio Garavito**

Ciclos de Desarrollo de Software (DOSW) — Periodo 2026-1

</div>
