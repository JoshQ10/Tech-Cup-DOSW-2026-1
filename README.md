# TechCup Fútbol

Plataforma digital para la gestión del torneo semestral de fútbol del Programa de **Ingeniería de Sistemas, Ingeniería de Inteligencia Artificial, Ingeniería de Ciberseguridad e Ingeniería Estadística** de la **Escuela Colombiana de Ingeniería Julio Garavito**.

---

# Descripción

**TechCup Fútbol** es una aplicación web que centraliza toda la gestión del torneo interno de fútbol, reemplazando los procesos manuales actuales (WhatsApp, formularios aislados y hojas de cálculo) por un sistema organizado y transparente.

La plataforma permitirá administrar todas las etapas del torneo desde un solo lugar.

## Funcionalidades principales

- Registro e inscripción de jugadores y equipos
- Creación y administración de torneos
- Gestión de pagos mediante comprobantes
- Configuración de calendarios, reglamentos y canchas
- Registro de partidos y resultados
- Tabla de posiciones automática
- Llaves eliminatorias
- Estadísticas y rankings
- Gestión de alineaciones

---

# Integrantes del Equipo

| Nombre |
|------|
| Joshua David Quiroga Landazabal |
| Juan David Valero Abril |
| Juan Carlos Bohorquez Monroy |
| Carlos Andres Uribe Vargas |
| Andres Felipe Savogal Wilches |

**Materia:** Ciclos de Desarrollo de Software (DOSW)  
**Profesor:** Andres Martin Cantor Urrego  
**Periodo:** 2026-1

---

# Arquitectura Tecnológica

| Capa | Tecnología |
|------|------------|
| Frontend | React con TypeScript |
| Backend | Spring Boot (Java) — API REST |
| Build Tool | Maven |
| Gestión del proyecto | Jira |

**Link Jira:**  
https://n9.cl/m2as4l

---

# Estructura del Proyecto

El proyecto se organiza en tres grandes épicas:

- **E1 — Backend**
- **E2 — Frontend**
- **E3 — Documentación y artefactos**

Cada épica contiene **features** que agrupan **historias de usuario**.

---

# E1 — Backend

## F1.1 — Autenticación y registro de usuarios

- HU-B01 — Registrar usuario en el sistema  
- HU-B02 — Verificar correo electrónico  
- HU-B03 — Iniciar sesión  

## F1.2 — Perfil deportivo y disponibilidad

- HU-B04 — Completar perfil deportivo  
- HU-B05 — Marcar disponibilidad  

## F1.3 — Gestión del torneo

- HU-B06 — Crear torneo  
- HU-B07 — Configurar torneo  
- HU-B08 — Cambiar estado del torneo  
- HU-B09 — Consultar torneo  

## F1.4 — Gestión de equipos y plantilla

- HU-B10 — Crear equipo  
- HU-B11 — Buscar jugadores disponibles  
- HU-B12 — Invitar jugador al equipo  
- HU-B13 — Responder invitación  
- HU-B14 — Quitar jugador del equipo  

## F1.5 — Inscripción y pagos

- HU-B15 — Subir comprobante de pago  
- HU-B16 — Revisar y aprobar pagos  

## F1.6 — Partidos y alineaciones

- HU-B17 — Organizar alineación del equipo  
- HU-B18 — Consultar alineación rival  
- HU-B19 — Registrar resultado del partido  
- HU-B20 — Consultar partidos asignados  

## F1.7 — Resultados y clasificación

- HU-B21 — Ver tabla de posiciones  
- HU-B22 — Ver llaves eliminatorias  

## F1.8 — Estadísticas

- HU-B23 — Consultar estadísticas del torneo  

## F1.9 — Administración

- HU-B24 — Gestionar usuarios y roles  

---

# E2 — Frontend

## F2.1 — Identidad visual y mockups

- HU-F01 — Manual de identidad visual  
- HU-F02 — Mockups de registro, login y perfil  
- HU-F03 — Mockups de torneo y configuración  
- HU-F04 — Mockups de equipos, invitaciones y pagos  
- HU-F05 — Mockups de partidos, alineaciones y resultados  
- HU-F06 — Mockups de notificaciones y administración  

## F2.2 — Registro, autenticación y perfil

- HU-F07 — Pantalla de registro  
- HU-F08 — Pantalla de login  
- HU-F09 — Verificación de correo  
- HU-F10 — Protección de rutas por rol  
- HU-F11 — Perfil deportivo  
- HU-F12 — Toggle de disponibilidad  

## F2.3 — Torneo y configuración

- HU-F13 — Formulario de creación de torneo  
- HU-F14 — Panel de configuración del torneo  
- HU-F15 — Gestión de estados del torneo  
- HU-F16 — Vista pública del torneo  

## F2.4 — Equipos, invitaciones y pagos

- HU-F17 — Creación de equipo  
- HU-F18 — Buscador de jugadores disponibles  
- HU-F19 — Sistema de invitaciones  
- HU-F20 — Gestión de plantilla  
- HU-F21 — Subir comprobante de pago  
- HU-F22 — Panel de revisión de pagos  

## F2.5 — Partidos y alineaciones

- HU-F23 — Organizar alineación  
- HU-F24 — Alineación rival  
- HU-F25 — Registro de resultado  
- HU-F26 — Panel del árbitro  

## F2.6 — Resultados, clasificación y estadísticas

- HU-F27 — Tabla de posiciones  
- HU-F28 — Bracket eliminatorio  
- HU-F29 — Vista de estadísticas  

## F2.7 — Notificaciones y administración

- HU-F30 — Bandeja de notificaciones  
- HU-F31 — Panel de administración  

---

# E3 — Documentación y Artefactos

## F3.1 — Arquitectura y diagramas

- HU-D01 — Diagrama de clases  
- HU-D02 — Diagrama de componentes general  
- HU-D03 — Diagrama de componentes específico  
- HU-D04 — Diagrama Entidad-Relación  
- HU-D05 — Diagramas de secuencia  

## F3.2 — Estructuración del proyecto

- HU-D06 — Estructurar proyecto Backend  
- HU-D07 — Estructurar proyecto Frontend  

## F3.3 — Refinamiento de artefactos

- HU-D08 — Refinamiento del análisis de requerimientos  
- HU-D09 — Documentación técnica  

---

# Conteo del Proyecto

| Épica | Features | Historias de Usuario | Tareas |
|------|------|------|------|
| Backend | 9 | 24 | 114 |
| Frontend | 7 | 31 | 96 |
| Documentación | 3 | 9 | 20 |

### Total del proyecto

- **19 Features**
- **64 Historias de Usuario**
- **230 tareas**

---

# Objetivo del Proyecto

El objetivo de **TechCup Fútbol** es modernizar la organización del torneo universitario mediante una plataforma que permita:

- Mejorar la organización del torneo
- Reducir errores administrativos
- Facilitar la comunicación entre jugadores, capitanes, árbitros y organizadores
- Brindar transparencia en resultados, estadísticas y clasificación

FIGMA
<img width="1374" height="742" alt="image" src="https://github.com/user-attachments/assets/66bf457b-7dd2-4ac0-b8ce-d7d2143e8ebe" />

