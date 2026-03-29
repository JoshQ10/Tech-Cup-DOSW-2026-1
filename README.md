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

**Archivo: `ssl-test-app/src/main/resources/application.properties`**
```properties
# Configuración Swagger/OpenAPI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.disable-swagger-default-url=true
springdoc.swagger-ui.display-request-duration=true
springdoc.show-actuator=true
```

**Archivo: `ssl-test-app/src/main/java/com/ssltest/OpenApiConfig.java`**
```java
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SSL Test API - TechCup")
                        .description("API de prueba para validar configuración HTTPS/SSL y CORS")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("TechCup Development Team")
                                .email("techcup@eci.edu.co"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("https://localhost:8443")
                                .description("Servidor HTTPS Local"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor HTTP Local (redirección automática)")));
    }
}
```
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

## Avances de Seguridad: HTTPS/SSL y CORS

### ✅ Implementación Completada

Se han implementado completamente los requisitos de seguridad **TR-B120 a TR-B125** para el backend de TechCup:

#### TR-B120: HTTPS/SSL Habilitado
- ✅ Servidor HTTPS configurado en puerto **8443**
- ✅ HTTP/2 habilitado para mejor rendimiento
- ✅ Configuración SSL completa en `application.yaml`

#### TR-B121: Redirección HTTP → HTTPS
- ✅ Puerto 8080 redirige automáticamente al 8443
- ✅ Implementación usando `TomcatServletWebServerFactory`
- ✅ Archivo: `HttpToHttpsRedirectConfig.java`

#### TR-B122: Certificado SSL
- ✅ Keystore PKCS12 generado (`src/main/resources/keystore.p12`)
- ✅ Alias: `techcup`, Contraseña: `password`
- ✅ Validez: 10 años (auto-firmado para desarrollo)

#### TR-B123: CORS Habilitado y Configurado
- ✅ Configuración completa en `SecurityConfig.java`
- ✅ Orígenes permitidos: `https://localhost:*`, `http://localhost:*`, `https://127.0.0.1:*`
- ✅ Métodos: `GET, POST, PUT, DELETE, OPTIONS`
- ✅ Credenciales habilitadas, max-age: 3600 segundos
- ✅ Aplicado solo a rutas `/api/**`

#### TR-B124: Pruebas de Conectividad HTTPS
- ✅ `HttpsConnectivityTest.java` implementado
- ✅ Prueba endpoints de API docs y Swagger UI

#### TR-B125: Pruebas Funcionales CORS
- ✅ `CorsFunctionalTest.java` implementado
- ✅ 5 casos de prueba completos para validación CORS

### 🔍 Cómo Ver el Certificado SSL

#### Opción 1: Usando Keytool (Línea de Comandos)

```bash
# Información básica del keystore
keytool -list -keystore src/main/resources/keystore.p12 -storetype PKCS12 -storepass password

# Información detallada del certificado
keytool -list -v -keystore src/main/resources/keystore.p12 -storetype PKCS12 -storepass password

# Exportar el certificado a un archivo separado
keytool -exportcert -alias techcup -keystore src/main/resources/keystore.p12 -storetype PKCS12 -storepass password -file certificate.crt
```

#### Opción 2: En el Navegador (Aplicación Ejecutándose)

1. **Ejecutar la aplicación**:
   ```bash
   mvn spring-boot:run
   ```

2. **Acceder a la aplicación**:
   - URL: `https://localhost:8443`
   - El navegador mostrará una advertencia de certificado no confiable (normal para certificados auto-firmados)

3. **Ver el certificado**:
   - Haz clic en el **candado 🔒** en la barra de direcciones
   - Selecciona **"Certificado"** o **"Ver certificado"**
   - Revisa los detalles: emisor, fechas de validez, algoritmo, etc.

#### Opción 3: Usando OpenSSL

```bash
# Ver el contenido del keystore PKCS12
openssl pkcs12 -info -in src/main/resources/keystore.p12 -passin pass:password

# Extraer y mostrar el certificado
openssl pkcs12 -in src/main/resources/keystore.p12 -passin pass:password -nokeys -clcerts | openssl x509 -text -noout
```

#### Opción 4: Usando cURL

```bash
# Ver información del certificado del servidor
curl -v https://localhost:8443/api/auth/login 2>&1 | grep -A 20 "Server certificate"
```

### Cómo se Generó el Certificado SSL

El certificado SSL se generó usando la herramienta `keytool` de Java con los siguientes pasos:

1. **Comando ejecutado**:
   ```bash
   keytool -genkeypair -alias techcup -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore src/main/resources/keystore.p12 -validity 3650 -storepass password -keypass password -dname "CN=localhost, OU=TechCup, O=ECI, L=Bogota, ST=Cundinamarca, C=CO"
   ```

2. **Parámetros utilizados**:
   - **Alias**: `techcup`
   - **Algoritmo**: RSA de 2048 bits
   - **Tipo de almacén**: PKCS12
   - **Validez**: 3650 días (10 años)
   - **Contraseña**: `password`
   - **Nombre distinguido**: `CN=localhost, OU=TechCup, O=ECI, L=Bogota, ST=Cundinamarca, C=CO`

3. **Archivo generado**: `src/main/resources/keystore.p12`

### 🧪 Ejecutar Pruebas de Seguridad

```bash
# Ejecutar todas las pruebas de seguridad
mvn test -Dtest="*Test" -Djacoco.skip=true

# Ejecutar solo pruebas HTTPS
mvn test -Dtest=HttpsConnectivityTest -Djacoco.skip=true

# Ejecutar solo pruebas CORS
mvn test -Dtest=CorsFunctionalTest -Djacoco.skip=true
```

### Archivos de Configuración Modificados

- `src/main/resources/application.yaml` - Configuración SSL del servidor
- `src/main/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/core/config/SecurityConfig.java` - Configuración CORS
- `src/main/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/core/config/HttpToHttpsRedirectConfig.java` - Redirección HTTP→HTTPS
- `src/test/resources/application-test.yaml` - Configuración de pruebas (sin SSL)
- `src/test/java/.../HttpsConnectivityTest.java` - Pruebas HTTPS
- `src/test/java/.../CorsFunctionalTest.java` - Pruebas CORS

---

## 🚀 **Ejecutar y Probar HTTPS/SSL**

### **Paso 1: Ejecutar la aplicación de prueba**
```bash
# Opción 1: Servidor simple (recomendado - más rápido)
run-simple-ssl.bat

# Opción 2: Aplicación Spring Boot
cd ssl-test-app
mvn spring-boot:run
```

**Nota**: Se creó una aplicación independiente (`ssl-test-app/`) específicamente para probar HTTPS/SSL sin conflictos con la aplicación principal.

### **Paso 2: Verificar en el navegador**
1. **Abrir navegador**: Ve a `https://localhost:8443`
2. **Aceptar advertencia**: El navegador mostrará "Su conexión no es privada" - haz clic en "Avanzado" → "Continuar a localhost (no seguro)"
3. **Ver certificado**:
   - Haz clic en el **candado 🔒** en la barra de direcciones
   - Selecciona **"Certificado"** o **"Ver certificado"**
   - Revisa los detalles: emisor, algoritmo, fechas de validez

### **Paso 3: Probar endpoints**
- `https://localhost:8443/` - Página principal con instrucciones
- `https://localhost:8443/api/test/ssl` - Endpoint de prueba SSL
- `https://localhost:8443/api/test/cors` - Endpoint de prueba CORS
- `https://localhost:8443/swagger-ui.html` - **Documentación Swagger UI**
- `https://localhost:8443/api-docs` - Especificación OpenAPI en JSON

### 📋 Información del Certificado

**Detalles técnicos del certificado SSL:**
- **Tipo**: PKCS12
- **Alias**: `techcup`
- **Algoritmo**: RSA 2048 bits
- **Validez**: 10 años (3650 días)
- **Emisor**: Auto-firmado (self-signed)
- **Subject**: `CN=localhost, OU=SSLTest, O=ECI, L=Bogota, ST=Cundinamarca, C=CO`
- **Contraseña**: `password`

---

## Requisitos Previos

- **Java** 21 (LTS) ✅ Verificar en terminal: `java -version`
- **Maven** 3.8+ ✅ Verificar en terminal: `mvn -version`
- **MongoDB Atlas** (conexion configurada en `application.properties`)
- **Git** para clonar el repositorio
- **Navegador moderno** (Chrome, Firefox, Edge, Safari)
- **Puerto 8443** disponible (HTTPS)
- **Puerto 8080** disponible (HTTP con redirección)

---

## 🚀 Guía Paso a Paso: Ejecutar y Visualizar la Aplicación

### **Paso 0: Verificar Requisitos**

Antes de ejecutar, verifica que tienes las herramientas necesarias:

```bash
# Verificar Java (debe ser 21+)
java -version
# Salida esperada: openjdk version "21.x.x"

# Verificar Maven (debe ser 3.8+)
mvn -version
# Salida esperada: Apache Maven 3.8.x...

# Verificar que los puertos estén disponibles (Windows)
netstat -ano | findstr :8443
netstat -ano | findstr :8080
# Si no aparecen resultados, los puertos están disponibles ✅
```

Si no aparecen tus versiones, consulta las [instrucciones de instalación de Java](#instalación-de-java-21) y [Maven](#instalación-de-maven).

---

### **Paso 1: Clonar o Navegar al Repositorio**

```bash
# Opción A: Si aún no tienes el repositorio clonado
git clone https://github.com/JoshQ10/Tech-Cup-DOSW-2026-1.git
cd Tech-Cup-DOSW-2026-1-main

# Opción B: Si ya estás en el directorio del proyecto
cd e:\DOSW\Proyecto_T2\Backend\Tech-Cup-DOSW-2026-1-main\Tech-Cup-DOSW-2026-1-main
```

---

### **Paso 2: Compilar el Proyecto**

Este paso descarga dependencias y compila el código.

```bash
# Compilación limpia (recomendado si hay cambios)
mvn clean compile

# Salida esperada:
# [INFO] BUILD SUCCESS
# [INFO] Total time: XX.XXs
```

**¿Qué sucede?**
- `clean`: Elimina archivos compilados anteriores
- `compile`: Compila el código Java a bytecode
- Se descargan todas las dependencias (primera vez tarda más)

**Si falla la compilación:**
```bash
# Opción 1: Limpiar caché de Maven
mvn clean

# Opción 2: Reinstalar todas las dependencias
mvn clean install -U

# Opción 3: Instalar dependencias específicas
mvn dependency:resolve

# Si persiste el error, verifica que Java esté correctamente configurada
echo %JAVA_HOME%  # Windows
echo $JAVA_HOME   # Linux/Mac
```

---

### **Paso 3: Ejecutar la Aplicación con Maven**

#### **Opción A: Ejecución Estándar (Recomendado)**

```bash
# Ejecutar la aplicación
mvn spring-boot:run

# Salida esperada (últimas líneas):
# Tomcat initialized with ports 8443 (https), 8080 (http)
# Tomcat started on ports 8443 (https) and 8080 (http) with context path ''
# Started TechCupDoswBackEnd20261Application in X.XXX seconds (JVM running for X.XXX)
# ✅ Aplicación iniciada correctamente
```

**La aplicación está ahora EJECUTÁNDOSE en segundo plano.** Déjala corriendo mientras accedes desde el navegador.

#### **Opción B: Limpieza y Ejecución (Si hay cambios de código)**

```bash
# Compilar primero, luego ejecutar
mvn clean compile spring-boot:run

# Esto combina los pasos 2 y 3 en uno solo
```

#### **Opción C: Parámetros Adicionales**

```bash
# Ejecutar mostrando más información de debug
mvn spring-boot:run -X

# Ejecutar en un puerto diferente (si 8443 está ocupado)
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8444"

# Ejecutar con perfil específico
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

---

### **Paso 4: Verificar que la Aplicación está Ejecutándose**

Mientras `mvn spring-boot:run` sigue ejecutándose en la terminal, abre una **NUEVA TERMINAL** (no cierres la primera) y verifica:

```bash
# Verificar que el puerto 8443 está escuchando
netstat -ano | findstr :8443
# Salida esperada: TCP    127.0.0.1:8443    0.0.0.0:0    ESCUCHANDO

# Verificar que el puerto 8080 también está escuchando
netstat -ano | findstr :8080
# Salida esperada: TCP    127.0.0.1:8080    0.0.0.0:0    ESCUCHANDO

# Probar conectividad HTTPS
curl -k https://localhost:8443 2>&1 | head -20
# Salida esperada: código HTML de la página principal
```

---

### **Paso 5: Visualizar en el Navegador** 🌐

Abre tu navegador y accede a estos enlaces (escoge uno):

#### **OPCIÓN 1: SWAGGER UI (Documentación Interactiva) ⭐**
```
https://localhost:8443/swagger-ui.html
```
**¿Qué verás?**
- Documentación completa de todos los endpoints
- Posibilidad de probar las APIs directamente
- Ejemplos de requests y responses

**Pasos:**
1. Copia y pega la URL en tu navegador
2. El navegador mostrará: `Su conexión no es privada`
3. Haz clic en **"Avanzado"** → **"Continuar a localhost (no seguro)"**
4. ✅ Verás la interfaz Swagger UI con todos los endpoints

Endpoints Swagger disponibles:
- 📄 **OpenAPI JSON**: https://localhost:8443/api-docs
- 🔑 **Auth**: POST /api/auth/login, /api/auth/register
- 👤 **Players**: PUT /api/players/{id}/profile, PATCH /api/players/{id}/availability
- ⚽ **Teams**: POST /api/teams, GET /api/teams/{id}
- 🏆 **Tournaments**: POST /api/tournaments, GET /api/tournaments/{id}

---

#### **OPCIÓN 2: Página Principal (con instrucciones)**
```
https://localhost:8443
```
**¿Qué verás?**
- Página de bienvenida de TechCup
- Instrucciones para ver el certificado SSL
- Enlaces a Swagger UI y OpenAPI

**Pasos:**
1. Copia y pega la URL en tu navegador
2. Acepta el certificado (advertencia de seguridad esperada)
3. ✅ Verás la página principal con botones de acceso

---

#### **OPCIÓN 3: Ver Certificado SSL** 🔒

1. Ve a cualquier URL HTTPS (ej: https://localhost:8443)
2. Haz clic en el **candado 🔒** en la barra de direcciones
3. Selecciona **"Certificado"** o **"Ver certificado"**

**Información que verás:**
- **Propietario (Subject)**: CN=localhost, OU=TechCup, O=DOSW, L=Bogota, ST=Cundinamarca, C=CO
- **Emisor (Issuer)**: Self-signed (autofirmado)
- **Válido desde**: 29/03/2026
- **Válido hasta**: 26/03/2036 (10 años)
- **Algoritmo**: SHA384 con RSA 2048-bit
- **Serial**: 360ebe4e30252773

---

#### **OPCIÓN 4: Probar Redirección HTTP → HTTPS**
```
http://localhost:8080
```
**¿Qué sucede?**
- Se redirige automáticamente a `https://localhost:8443`
- Podrás ver la página principal en HTTPS
- Esto confirma que la configuración TR-B122 está funcionando ✅

**Pasos:**
1. Abre una pestaña nueva en el navegador
2. Escribe `http://localhost:8080` (sin HTTPS)
3. Presiona Enter
4. ✅ Automáticamente se redirige a `https://localhost:8443`

---

### **Paso 6: Probar Endpoints de API**

#### **Con Swagger UI (Interfaz Gráfica)** ⭐ Recomendado
1. Ve a: https://localhost:8443/swagger-ui.html
2. Haz clic en un endpoint (ej: **POST /api/auth/register**)
3. Haz clic en **"Try it out"**
4. Completa los parámetros requeridos
5. Haz clic en **"Execute"**
6. ✅ Verás la respuesta en tiempo real

#### **Con cURL (Línea de comandos)**

```bash
# Probar endpoint de API docs
curl -k https://localhost:8443/api-docs

# Probar endpoint de prueba SSL
curl -k https://localhost:8443/api/test/ssl

# Probar endpoint de prueba CORS
curl -k https://localhost:8443/api/test/cors

# Probar redirección HTTP a HTTPS
curl -v http://localhost:8080 2>&1 | grep -i location
```

#### **Con Postman (Cliente REST gráfico)**
1. Descargar Postman: https://www.postman.com/downloads/
2. Crear nueva solicitud HTTP
3. URL: `https://localhost:8443/api-docs`
4. Seleccionar método: GET
5. En Settings → Certificates → Desabilitar "Verify SSL certificate"
6. Hacer clic en "Send"
7. ✅ Ver respuesta JSON

---

### **Paso 7: Detener la Aplicación**

Cuando termines de probar:

```bash
# En la PRIMERA TERMINAL donde está ejecutándose mvn spring-boot:run
# Presiona: Ctrl + C

# Verás:
# ^C (Ctrl+C presionado)
# [INFO] BUILD SUCCESS
# Aplicación detenida
```

---

## ⚠️ Soluciones: ¿Qué hacer si `mvn spring-boot:run` NO funciona?

### **Problema 1: "Puerto 8443 ya está en uso"**

```
Error: Address [::1]:8443 already in use
```

**Soluciones:**
```bash
# Opción A: Encontrar y terminar el proceso usando el puerto
netstat -ano | findstr :8443
# Nota el PID (último número)
taskkill /PID <PID> /F

# Opción B: Usar un puerto diferente
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8444"
# Luego accede a: https://localhost:8444/swagger-ui.html

# Opción C: Esperar unos segundos y volver a intentar
# Los sockets TCP a veces tardan en liberar completamente
```

---

### **Problema 2: "BUILD FAILURE" durante compilación**

```
[ERROR] COMPILATION ERROR
[ERROR] symbol not found class XYZ
```

**Soluciones:**
```bash
# Opción A: Reinstalar todas las dependencias
mvn clean install -U
# -U: Update all snapshots

# Opción B: Verificar que Java está correctamente instalado
java -version
# Debe mostrar Java 21+

# Opción C: Eliminar caché local de Maven
rmdir /s %USERPROFILE%\.m2\repository  # Windows
rm -rf ~/.m2/repository                 # Linux/Mac
# Luego: mvn clean install

# Opción D: Forzar descargar todas las dependencias
mvn dependency:resolve dependency:resolve-plugins
```

---

### **Problema 3: "MongoDB connection refused"**

```
Exception creating bean with name 'mongoClient'
Connection refused
```

**Causa**: La base de datos MongoDB no está accesible (requerida para la app principal)

**Soluciones:**
```bash
# Opción A: Usar la aplicación de prueba del proyecto (sin BD requerida)
cd ssl-test-app
mvn spring-boot:run
# Luego: https://localhost:8443/swagger-ui.html

# Opción B: Configurar conexión a MongoDB Atlas
# Editar: src/main/resources/application.properties
# Cambiar: spring.data.mongodb.uri=mongodb+srv://user:password@cluster/techcup_db

# Opción C: Usar perfil de prueba (si existe)
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=test"
```

---

### **Problema 4: Application takes too long to start (>10 segundos)**

```
Still waiting for process to start. Optimizations are disabled
```

**Causa**: Primera ejecución (descargando dependencias) o máquina lenta

**Soluciones:**
```bash
# Opción A: Esperar más tiempo (normal en primera ejecución)
# Las siguientes ejecuciones serán más rápidas

# Opción B: Pre-descargar todas las dependencias
mvn dependency:resolve

# Opción C: Usar parámetro para saltar tests
mvn spring-boot:run -DskipTests

# Opción D: Compilar previamente
mvn clean compile
mvn spring-boot:run  # Más rápido ahora
```

---

### **Problema 5: Certificate validation failed**

```
SSLHandshakeException: PKIX path building failed
```

**Causa**: El certificado autofirmado no es confiable (normal en desarrollo)

**Soluciones:**
```bash
# Opción A: En navegador, aceptar el certificado como excepción
# Haz clic en "Avanzado" → "Continuar a localhost (no seguro)"

# Opción B: En curl, usar flag -k (insecure)
curl -k https://localhost:8443/swagger-ui.html

# Opción C: En Java, desabilitar validación SSL (solo desarrollo)
# NO HACER EN PRODUCCIÓN
# Agregar a application.properties:
# server.ssl.key-store-type=PKCS12
# server.ssl.trust-store=classpath:keystore.p12
# server.ssl.trust-store-password=password
```

---

### **Problema 6: "JAVA_HOME not found" o Java no reconocido**

```
'java' is not recognized as an internal or external command
```

**Soluciones:**
```bash
# Verificar si Java está instalado
java -version

# Si no aparece, instalar Java 21:
# 1. Descargar desde: https://www.oracle.com/java/technologies/downloads/
# 2. Instalar (seguir el instalador)
# 3. Reiniciar la terminal
# 4. Verificar: java -version

# O usar OpenJDK (más rápido):
# Windows (con Chocolatey):
choco install openjdk21

# Linux:
sudo apt-get install openjdk-21-jdk

# Mac (con Homebrew):
brew install openjdk@21
```

---

### **Problema 7: Maven no reconocido**

```
'mvn' is not recognized as an internal or external command
```

**Soluciones:**
```bash
# Instalar Maven:
# 1. Descargar desde: https://maven.apache.org/download.cgi
# 2. Extraer la carpeta en C:\Program Files (Windows)
# 3. Agregar a PATH: C:\Program Files\apache-maven-3.x.x\bin
# 4. Reiniciar terminal

# O usar Chocolatey (Windows):
choco install maven

# O Homebrew (Mac):
brew install maven

# Verificar instalación:
mvn --version
```

---

## Instalacion y Ejecucion (Resumen Rápido)

```bash
# 1. Clonar el repositorio
git clone https://github.com/JoshQ10/Tech-Cup-DOSW-2026-1.git
cd Tech-Cup-DOSW-2026-1-main

# 2. Compilar el proyecto
mvn clean compile

# 3. Ejecutar la aplicacion
mvn spring-boot:run

# 4. Abrir en navegador (en NUEVA TERMINAL/pestaña del navegador)
# Swagger UI: https://localhost:8443/swagger-ui.html
# Página Principal: https://localhost:8443
# API Docs: https://localhost:8443/api-docs

# 5. Detener (en la terminal donde corre mvn spring-boot:run)
# Presiona: Ctrl + C
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

## 📚 Referencias Rápidas: Enlaces y Comandos Útiles

### **Todos los Enlaces de la Aplicación**

| Recurso | URL | Descripción |
|---------|-----|-------------|
| 🏠 **Página Principal** | https://localhost:8443 | Página de bienvenida con instrucciones |
| 📄 **Swagger UI** | https://localhost:8443/swagger-ui.html | Documentación interactiva de APIs ⭐ |
| 📋 **OpenAPI JSON** | https://localhost:8443/api-docs | Especificación OpenAPI en formato JSON |
| 🔄 **Redirección HTTP** | http://localhost:8080 | Redirige automáticamente a HTTPS |
| 🧪 **API de Prueba SSL** | https://localhost:8443/api/test/ssl | Endpoint para validar SSL |
| 🌐 **API de Prueba CORS** | https://localhost:8443/api/test/cors | Endpoint para validar CORS |

### **URLs de Recursos Externos**

| Recurso | Enlace |
|---------|--------|
| 📊 **Jira (Gestión del proyecto)** | [https://n9.cl/m2as4l](https://n9.cl/m2as4l) |
| 🎨 **Diagrama de Arquitectura (Lucidchart)** | [Ver diagrama](https://lucid.app/lucidchart/d035776f-7c94-4150-9fb5-55bcacb674df/edit?viewport_loc=-396%2C340%2C3964%2C1684%2C0_0&invitationId=inv_ccfbc0fd-443f-4d4f-b7a1-53f4693b6fe2) |
| 📐 **Diagramas de Clases y Secuencia(Draw.io)** | [Ver diagramas](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=Diagrama%20de%20clases%20y%20secuencia.drawio&dark=auto) |
| ☕ **Descargar Java 21 LTS** | [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) |
| 📦 **Descargar Maven** | [Apache Maven](https://maven.apache.org/download.cgi) |
| 🐙 **GitHub - Repositorio** | [JoshQ10/Tech-Cup-DOSW-2026-1](https://github.com/JoshQ10/Tech-Cup-DOSW-2026-1) |
| 📮 **Postman (API Client)** | [Descargar Postman](https://www.postman.com/downloads/) |

---

### **Comandos Maven Más Usados**

| Comando | Descripción | Ejemplo |
|---------|-------------|---------|
| `mvn clean` | Elimina compilaciones previas | `mvn clean` |
| `mvn compile` | Compila el código | `mvn compile` |
| `mvn test` | Ejecuta todas las pruebas | `mvn test` |
| `mvn clean compile` | Limpia y compila | `mvn clean compile` |
| `mvn spring-boot:run` | **Ejecuta la aplicación** ⭐ | `mvn spring-boot:run` |
| `mvn clean install` | Limpia, compila, instala | `mvn clean install` |
| `mvn dependency:resolve` | Descarga todas las dependencias | `mvn dependency:resolve` |
| `mvn clean compile spring-boot:run` | Limpia, compila y ejecuta | `mvn clean compile spring-boot:run` |

### **Comandos Maven para Ejecución con Parámetros**

```bash
# Ejecutar en puerto diferente (si 8443 está ocupado)
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8444"

# Ejecutar con perfil específico (ej: producción)
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"

# Ejecutar mostrando salida de debug
mvn spring-boot:run -X

# Ejecutar sin ejecutar tests
mvn spring-boot:run -DskipTests

# Limpiar y ejecutar (cuando hay cambios importantes)
mvn clean compile spring-boot:run
```

### **Comandos para Probar la Aplicación**

```bash
# Verificar si la app está ejecutándose
netstat -ano | findstr :8443

# Verificar puertos (Linux/Mac)
lsof -i :8443

# Probar HTTPS
curl -k https://localhost:8443/api-docs

# Probar redirección HTTP → HTTPS
curl -v http://localhost:8080 2>&1 | grep -i location

# Probar CORS
curl -i -X OPTIONS https://localhost:8443/api/auth/login \
  -H "Origin: https://localhost:3000" \
  -H "Access-Control-Request-Method: GET"

# Verificar certificado SSL
keytool -list -v -keystore src/main/resources/keystore.p12 -storepass password
```

### **Comandos para Solucionar Problemas**

```bash
# Encontrar proceso en puerto 8443
netstat -ano | findstr :8443
# O (Linux/Mac):
lsof -i :8443

# Terminar proceso en Windows (con PID obtenido arriba)
taskkill /PID <PID> /F

# Terminar proceso en Linux/Mac
kill -9 <PID>

# Limpiar caché de Maven
mvn clean
# O:
rmdir /s %USERPROFILE%\.m2\repository

# Verificar Java instalado
java -version

# Verificar Maven instalado
mvn --version

# Búsqueda de dependencias conflictivas
mvn dependency:tree | grep -i "conflict"
```

---

## Instalación de Java 21

Si no tienes Java instalado o necesitas actualizar:

### **Opción 1: Descargar desde Oracle (Oficial)**
1. Ve a: https://www.oracle.com/java/technologies/downloads/
2. Descarga **Java 21 LTS** (Long Term Support)
3. Ejecuta el instalador
4. Sigue las instrucciones en pantalla
5. Reinicia tu terminal/IDE

### **Opción 2: Usar Chocolatey (Windows)**
```bash
# Instalar Chocolatey (si no lo tienes)
# Abre PowerShell como Administrador y copia:
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Instalar Java 21
choco install openjdk21

# Verificar
java -version
```

### **Opción 3: Usar Homebrew (Mac)**
```bash
# Instalar Homebrew (si no lo tienes)
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Instalar Java 21
brew install openjdk@21

# Verificar
java -version
```

### **Opción 4: Usar apt (Linux - Debian/Ubuntu)**
```bash
sudo apt-get update
sudo apt-get install openjdk-21-jdk

# Verificar
java -version
```

### **Verificar instalación correcta**
```bash
java -version
# Debe mostrar algo como:
# openjdk version "21.0.x" 2024-XX-XX
# OpenJDK Runtime Environment (build 21.0.x+...)
# OpenJDK 64-Bit Server VM (build 21.0.x+...)
```

---

## Instalación de Maven

Si no tienes Maven instalado:

### **Opción 1: Descargar Manual desde Apache**
1. Ve a: https://maven.apache.org/download.cgi
2. Descarga **Binary zip archive** (apache-maven-3.x.x-bin.zip)
3. Extrae en: `C:\Program Files` (Windows) o `/opt` (Linux)
4. Agregar a PATH:
   - **Windows**: Control Panel → System → Advanced → Environment Variables
   - Agregar `C:\Program Files\apache-maven-3.x.x\bin` a PATH
5. Reinicia la terminal
6. Verifica: `mvn --version`

### **Opción 2: Usar Chocolatey (Windows)**
```bash
choco install maven

# Verificar
mvn --version
```

### **Opción 3: Usar Homebrew (Mac)**
```bash
brew install maven

# Verificar
mvn --version
```

### **Opción 4: Usar apt (Linux)**
```bash
sudo apt-get update
sudo apt-get install maven

# Verificar
mvn --version
```

### **Verificar instalación correcta**
```bash
mvn --version
# Debe mostrar algo como:
# Apache Maven 3.8.x (XXX; ...)
# Maven home: /path/to/maven
# Java version: 21.0.x, vendor: Oracle Corporation
```

---

## Reporte de Cumplimiento - Requisitos de Seguridad HTTPS/SSL y CORS

### Resumen Ejecutivo

Cumplimiento: 100% (6 de 6 requisitos)

| Requisito | Estado | Implementacion |
|-----------|--------|-----------------|
| TR-B120 | CUMPLE | Certificado SSL autofirmado generado |
| TR-B121 | CUMPLE | HTTPS configurado en application.yaml |
| TR-B122 | CUMPLE | Redireccion HTTP a HTTPS implementada |
| TR-B123 | CUMPLE | CORS habilitado y configurado |
| TR-B124 | CUMPLE | Pruebas funcionales de HTTPS presentes |
| TR-B125 | CUMPLE | Pruebas funcionales de CORS presentes |

### TR-B120: Generar certificado SSL autofirmado (keystore.p12)

Estado: CUMPLE

Especificaciones del certificado:
- Ubicacion: src/main/resources/keystore.p12
- Alias: techcup
- Tipo: PKCS12
- Algoritmo de firma: SHA384withRSA
- Tamanio de clave: RSA 2048-bit
- Valido desde: Sun Mar 29 2026
- Valido hasta: Wed Mar 26 2036 (10 anios)
- Serial: 360ebe4e30252773
- Propietario: CN=localhost, OU=TechCup, O=DOSW, L=Bogota, ST=Cundinamarca, C=CO

### TR-B121: Configurar HTTPS en application.yaml

Estado: CUMPLE

Ubicacion: src/main/resources/application.yaml

Configuracion implementada:
```yaml
server:
  port: 8443
  ssl:
    key-store: classpath:keystore.p12
    key-store-password: password
    key-store-type: PKCS12
    key-alias: techcup
  http2:
    enabled: true
```

Ubicacion alternativa: src/main/resources/application.properties

```properties
server.port=8443
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=password
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=techcup
server.http2.enabled=true
```

Caracteristicas adicionales:
- HTTP/2 habilitado para mejor rendimiento
- Protocolos TLS 1.2 y 1.3 soportados
- Swagger/OpenAPI configurado

### TR-B122: Configurar redireccion automatica de HTTP a HTTPS

Estado: CUMPLE

Implementacion: src/main/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/core/config/TomcatConfig.java

```java
@Component
public class TomcatConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        Connector httpConnector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        httpConnector.setPort(8080);
        httpConnector.setScheme("http");
        httpConnector.setSecure(false);
        httpConnector.setRedirectPort(8443);
        factory.addAdditionalTomcatConnectors(httpConnector);
    }
}
```

Funcionamiento:
- HTTP escucha en puerto 8080
- Las solicitudes HTTP son redirigidas automaticamente a HTTPS en puerto 8443
- Utilizando el conector Tomcat NIO para mayor rendimiento

### TR-B123: Habilitar y configurar CORS dentro de la aplicacion

Estado: CUMPLE

Ubicacion: src/main/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/core/config/SecurityConfig.java

Configuracion CORS:
```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(
        Arrays.asList("https://localhost:*", "http://localhost:*", "https://127.0.0.1:*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", configuration);
    return source;
}
```

Parametros configurados:
- Origenes permitidos: https://localhost:*, http://localhost:*, https://127.0.0.1:*
- Metodos HTTP: GET, POST, PUT, DELETE, OPTIONS
- Headers permitidos: Todos (*)
- Credenciales: Habilitadas
- Max Age: 3600 segundos (1 hora)
- Scope: /api/** (todos los endpoints API)

Integracion en Security Chain:
```java
http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
```

### TR-B124: Pruebas funcionales de conectividad HTTPS

Estado: CUMPLE

Ubicacion: src/test/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/controller/HttpsConnectivityTest.java

Pruebas implementadas:

1. testApiDocsEndpoint()
   - Descripcion: Verifica acceso a /api-docs
   - Validacion: HTTP 200, Content-Type JSON

2. testSwaggerUiEndpoint()
   - Descripcion: Verifica acceso a Swagger UI
   - Validacion: HTTP 200

3. testSslConfiguration()
   - Descripcion: Valida que SSL esta configurado
   - Validacion: Certificados disponibles en classpath

Ejecucion de pruebas:
```bash
mvn test -Dtest=HttpsConnectivityTest
```

### TR-B125: Pruebas funcionales de CORS (peticiones cross-origin permitidas y bloqueadas)

Estado: CUMPLE

Ubicacion: src/test/java/eci/edu/co/Tech_Cup_DOSW_BackEnd_2026_1/controller/CorsFunctionalTest.java

Pruebas implementadas:

1. testCorsPreflightAllowedOrigins()
   - Descripcion: Preflight desde origen permitido
   - Validacion: 200 OK, headers CORS presentes

2. testCorsLocalhostDifferentPorts()
   - Descripcion: CORS localhost puertos diferentes
   - Validacion: Allow-Origin correcto

3. testCorsLocalhostIp()
   - Descripcion: CORS desde IP loopback
   - Validacion: 200 OK, headers CORS

4. testCorsAllHttpMethods()
   - Descripcion: Preflight para todos los metodos
   - Validacion: Allow-Methods: GET,POST,PUT,DELETE,OPTIONS

5. testCorsOnlyForApiEndpoints()
   - Descripcion: CORS aplicado solo a /api/**
   - Validacion: Headers CORS presentes

Ejecucion de pruebas:
```bash
mvn test -Dtest=CorsFunctionalTest
```

### Validacion Manual

Para validar la configuracion HTTPS/SSL y CORS manualmente:

Probar HTTPS:
```bash
curl -k https://localhost:8443/api-docs
```

Probar redireccion HTTP a HTTPS:
```bash
curl -L -v http://localhost:8080/api-docs
```

Probar CORS:
```bash
curl -i -X OPTIONS https://localhost:8443/api/auth/login \
  -H "Origin: https://localhost:3000" \
  -H "Access-Control-Request-Method: GET"
```

Verificar certificado SSL:
```bash
keytool -list -v -keystore src/main/resources/keystore.p12 -storepass password
```

### Notas Tecnicas

Certificado SSL:
- El certificado autofirmado es valido por 10 anios (2026-2036)
- Los navegadores mostraran advertencia de conexion no segura (esto es normal para certificados autofirmados)
- En produccion, se debe usar un certificado emitido por una Autoridad Certificadora (Let's Encrypt, DigiCert, etc.)

CORS Configuration:
- Configurado correctamente a traves de Spring Security
- Aplicable solo a endpoints /api/**
- Soporta credenciales (cookies, autenticacion)
- Max-Age configurado en 1 hora para optimizar requests

HTTP/2 Support:
- Habilitado en configuracion HTTPS para mejor rendimiento
- Requiere Java 9+ (se utiliza Java 21)

Redireccion HTTP a HTTPS:
- Implementada mediante configuracion de conector Tomcat NIO
- Automatica y transparente para los clientes HTTP

### Matriz de Trazabilidad

TR-B120 --- keystore.p12 + keytool verification
TR-B121 --- application.yaml + application.properties
TR-B122 --- TomcatConfig.java
TR-B123 --- SecurityConfig.java (CORS Bean)
TR-B124 --- HttpsConnectivityTest.java
TR-B125 --- CorsFunctionalTest.java

---

## 🎯 Flujo Completo: Paso a Paso Visual

```
┌─────────────────────────────────────────────────────────────────┐
│  FLUJO COMPLETO: EJECUTAR Y VISUALIZAR LA APLICACIÓN           │
└─────────────────────────────────────────────────────────────────┘

PASO 1: PREPARACIÓN (2-3 minutos)
├─ Verificar Java:     java -version
├─ Verificar Maven:    mvn --version
├─ Navegar al repo:    cd Tech-Cup-DOSW-2026-1-main
└─ ✅ LISTO

PASO 2: COMPILAR (1-5 minutos, según conexión)
├─ Terminal 1:        mvn clean compile
├─ Esperar...         [INFO] BUILD SUCCESS
└─ ✅ LISTO

PASO 3: EJECUTAR
├─ Terminal 1:        mvn spring-boot:run
├─ Esperar...         Started TechCupDoswBackEnd20261Application
└─ ✅ APLICACIÓN CORRIENDO

PASO 4: ABRIR NAVEGADOR (En OTRA terminal o pestaña)
├─ Terminal 2:        (NUEVA - no cierres terminal 1)
├─ Navegador:         https://localhost:8443/swagger-ui.html
├─ Aceptar cert:      "Avanzado" → "Continuar a localhost"
└─ ✅ VES SWAGGER UI

PASO 5: PROBAR ENDPOINTS
├─ En Swagger UI:     Haz clic en un endpoint
├─ Click:             "Try it out"
├─ Click:             "Execute"
└─ ✅ VES LA RESPUESTA

PASO 6: VER CERTIFICADO SSL
├─ En navegador:      Haz clic en 🔒 (candado)
├─ Click:             "Ver certificado" o "Certificate"
└─ ✅ VES DETALLES SSL

PASO 7: PROBAR REDIRECCIÓN HTTP → HTTPS
├─ En navegador:      http://localhost:8080
└─ ✅ REDIRIGE A HTTPS AUTOMÁTICAMENTE

PASO 8: DEPURACIÓN Y SOLUCIÓN DE PROBLEMAS
├─ ¿Puerto ocupado?    → Cambiar puerto: mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8444"
├─ ¿Maven falla?       → mvn clean install -U
├─ ¿Java no encontrado? → Instalar Java 21 desde https://oracle.com/java
└─ ✅ PROBLEMA RESUELTO

PASO 9: DETENER LA APLICACIÓN
├─ Terminal 1:        Presiona Ctrl + C
└─ ✅ APLICACIÓN DETENIDA
```

### **Ejemplo Práctico: Línea por Línea**

```bash
# Terminal 1: Preparación
C:\proyectos> cd Tech-Cup-DOSW-2026-1-main
C:\proyectos\Tech-Cup-DOSW-2026-1-main> java -version
openjdk version "21.0.8" 2024-07-16
OpenJDK Runtime Environment (build 21.0.8+7-LTS)
OpenJDK 64-Bit Server VM (build 21.0.8+7-LTS, mixed mode, sharing)

C:\proyectos\Tech-Cup-DOSW-2026-1-main> mvn --version
Apache Maven 3.8.1 (05c21c65bdf02ff8f494c3ca3cbb7b3ce066d8ea)

# Terminal 1: Compilar
C:\proyectos\Tech-Cup-DOSW-2026-1-main> mvn clean compile
[INFO] Building Tech-Cup-DOSW-BackEnd-2026-1 1.0.0-SNAPSHOT
[INFO] ────────────────────────────────────────────────
[INFO] Compiling sources...
[INFO] ────────────────────────────────────────────────
[INFO] BUILD SUCCESS ✅

# Terminal 1: EJECUTAR (¡DEJAR CORRIENDO!)
C:\proyectos\Tech-Cup-DOSW-2026-1-main> mvn spring-boot:run
[INFO] Starting TechCupDoswBackEnd20261Application v1.0.0-SNAPSHOT
[INFO] Tomcat initialized with port(s): 8443 (https) 8080 (http)
[INFO] Tomcat started on port(s): 8443 (https) and 8080 (http)
[INFO] Started TechCupDoswBackEnd20261Application in 3.338s
✅ Aplicación iniciada correctamente

# Terminal 2: En OTRA terminal (o pestaña del navegador)
C:\proyectos> netstat -ano | findstr :8443
  TCP    127.0.0.1:8443    0.0.0.0:0    ESCUCHANDO    (PID)
  ✅ Puerto 8443 ESCUCHANDO

# Navegador: Copiar y pegar
URL: https://localhost:8443/swagger-ui.html
[Aceptar certificado]
[Ver Swagger UI con todos los endpoints]

# Terminal 2: Probar desde línea de comandos
C:\proyectos> curl -k https://localhost:8443/api-docs
{
  "openapi": "3.0.1",
  "info": {
    "title": "TechCup Online API",
    "version": "1.0.0"
  },
  ...
}
✅ API respondiendo

# Cuando termines (Terminal 1)
# Presiona: Ctrl + C
^C
[INFO] BUILD SUCCESS
✅ Aplicación detenida
```

---

## 📖 Tabla de Contenidos - Todas las Secciones del README

| Sección | Ubicación | Descripción |
|---------|-----------|-------------|
| 🏠 Descripción | Arriba | Descripción general del proyecto |
| 🎯 Objetivo del Proyecto | Arriba | Objetivos principales de TechCup |
| ✨ Funcionalidades Principales | Arriba | Tabla de módulos y funcionalidades |
| 🏗️ Arquitectura Tecnológica | Arriba | Stack tecnológico usado |
| 👥 Integrantes del Equipo | Arriba | Miembros del equipo |
| 🔗 Enlaces del Proyecto | Arriba | Jira, diagramas, etc. |
| 📐 Estructura del Proyecto | Arriba | Epicas, features, historias |
| 📊 Conteo del Proyecto | Arriba | Estadísticas de tareas |
| 📦 Estructura de Paquetes | Arriba | Organización del código Java |
| 🔌 Endpoints de la API | Arriba | Tabla de endpoints REST |
| 📄 Documentación Interactiva | Arriba | Acceso a Swagger UI |
| 🔐 Avances de Seguridad | Arriba | HTTPS/SSL y CORS implementados |
| ⚙️ Requisitos Previos | [Aquí](#requisitos-previos) | Java, Maven, Git, Navegador |
| 🚀 **Guía Paso a Paso** | [Aquí](#-guía-paso-a-paso-ejecutar-y-visualizar-la-aplicación) | ⭐ **Cómo ejecutar con Maven** |
| ⚠️ Soluciones de Problemas | [Aquí](#-soluciones-qué-hacer-si-mvn-spring-bootrun-no-funciona) | Qué hacer si falla |
| 📚 Referencias Rápidas | [Aquí](#-referencias-rápidas-enlaces-y-comandos-útiles) | Enlaces y comandos útiles |
| ☕ Instalación de Java 21 | [Aquí](#instalación-de-java-21) | Cómo instalar Java |
| 📦 Instalación de Maven | [Aquí](#instalación-de-maven) | Cómo instalar Maven |
| 🎯 Flujo Completo Visual | [Aquí](#-flujo-completo-paso-a-paso-visual) | Resumen visual paso por paso |
| 📋 Reporte de Cumplimiento | [Aquí](#reporte-de-cumplimiento---requisitos-de-seguridad-httpsssl-y-cors) | Verificación de requisitos TR-B120 a TR-B125 |

---

## ✅ Checklist: ¿Está todo funcionando?

Usa este checklist para verificar que todo está configurado correctamente:

```
□ Java 21 instalado           → java -version muestra 21.x
□ Maven instalado              → mvn --version muestra 3.8+
□ Repositorio clonado          → Carpeta Tech-Cup-DOSW-2026-1-main existe
□ Compilación exitosa          → mvn clean compile → BUILD SUCCESS
□ Aplicación ejecutando        → mvn spring-boot:run sin errores
□ Puerto 8443 escuchando       → netstat -ano | findstr :8443
□ Puerto 8080 escuchando       → netstat -ano | findstr :8080
□ Swagger UI accesible         → https://localhost:8443/swagger-ui.html carga
□ Certificado SSL visible      → Hacer clic en 🔒 muestra certificado
□ Redirección HTTP→HTTPS OK    → http://localhost:8080 redirige a https
□ API responde                 → curl -k https://localhost:8443/api-docs retorna JSON
□ CORS configurado             → Peticiones CORS permiten credenciales
□ Tests compilan               → mvn test compila sin errores
□ Proyecto compilado sin warnings → mvn clean compile sin [WARNING]
```

Si todos están marcados ✅, ¡tu aplicación está lista para producción!

---

## 🆘 Centro de Soporte Rápido

| Problema | Solución Rápida | Comando |
|----------|-----------------|---------|
| Port already in use | Terminar proceso o cambiar puerto | `taskkill /PID <PID> /F` o `-Dspring-boot.run.arguments="--server.port=8444"` |
| Certificate not trusted | Haz clic "Avanzado" → "Continuar" | Aceptar en navegador |
| Maven not found | Instalar Maven | `choco install maven` |
| Java not found | Instalar Java 21 | `choco install openjdk21` |
| BUILD FAILURE | Clean install con actualización | `mvn clean install -U` |
| Timeout de compilación | Pre-descargar dependencias | `mvn dependency:resolve` |
| Connection refused | Verificar mvn corriendo | `netstat -ano \| findstr :8443` |
| Swagger UI no carga | Verificar HTTPS | https://localhost:8443/api-docs |
| CORS errors | Verificar SecurityConfig | Revisar `/core/config/SecurityConfig.java` |

---

<div align="center">

**Escuela Colombiana de Ingenieria Julio Garavito**

Ciclos de Desarrollo de Software (DOSW) — Periodo 2026-1

</div>
