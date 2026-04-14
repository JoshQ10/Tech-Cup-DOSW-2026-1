# Matriz de Permisos - Tech Cup DOSW 2026

## Leyenda
- **R** = Leer
- **C** = Crear
- **E** = Editar
- **D** = Eliminar

---

## Matriz de Permisos por Rol y Módulo

| Módulo | Administrador | Organizador | Capitán | Árbitro | Jugador |
|--------|---------------|-------------|---------|---------|---------|
| **Usuarios y Perfiles** | R, C, E, D | R, C, E | R, E | R | R, E |
| **Torneos** | R, C, E, D | R, C, E, D | R | R | R |
| **Equipos** | R, C, E, D | R, C, E, D | R, C, E | R | R |
| **Invitaciones** | R, E, D | R, E, D | R, C, E | R | R, E |
| **Plantilla Equipo** | R, E, D | R, E, D | R, E | R | R |
| **Partidos** | R, C, E, D | R, C, E, D | R | R, E | R |
| **Alineaciones** | R, E, D | R, E | R, C, E | R | R |
| **Resultados** | R, E, D | R, E, D | R | R, C, E | R |
| **Standings/Clasificación** | R | R | R | R | R |
| **Pagos** | R, E, D | R, C, E, D | R | R | R, C |
| **Sanciones** | R, C, E, D | R, C, E | R | R, C | R |
| **Eventos de Partido** | R, E, D | R, E, D | R | R, C, E | R |
| **Reportes y Estadísticas** | R | R | R | R | R |
| **Administración General** | R, C, E, D | R, E | R | R | R |
| **Auditoría y Logs** | R, D | R | - | - | - |

---

## Descripción de Roles

### Administrador
- Control total del sistema
- Gestión de usuarios, roles y permisos
- Administración de torneos
- Acceso a logs y auditoría
- Puede sancionar
- Aprobación final de pagos

### Organizador
- Gestión completa de torneos
- Crear y editar equipos
- Gestionar invitaciones a jugadores
- Registrar partidos y resultados
- Revisar y aprobar pagos
- Crear sanciones

### Capitán
- Crear su equipo
- Invitar jugadores
- Gestionar su plantilla
- Organizar alineación
- Ver resultados
- Pagar la inscripción
- Ver sanciones

### Árbitro
- Ver información de partidos asignados
- Registrar resultados
- Registrar eventos de partido (goles, tarjetas, etc.)
- Ver sanciones
- Acceso limitado a reportes

### Jugador
- Ver su perfil
- Aceptar/rechazar invitaciones
- Ver equipos de los que es miembro
- Ver partidos en los que participa
- Ver clasificación y resultados
- Subir comprobante de pago
- Ver sanciones personales

---

## Notas Importantes

1. **Lectura de Datos Sensibles:** Cada rol solo puede leer datos relevantes a su función
2. **Edición de Perfil:** Los usuarios pueden editar solo su propio perfil
3. **Eliminación:** Solo Administrador puede eliminar usuarios; Organizador puede eliminar torneos, equipos (si no tienen partidos)
4. **Equipos:** Un Capitán solo puede editar su propio equipo
5. **Alineaciones:** Solo el Capitán del equipo puede crear/editar alineaciones de su equipo
6. **Resultados:** Organizador aprueba, Árbitro registra
7. **Pagos:** Los Jugadores suben comprobantes, Organizador revisa y aprueba
8. **Sanciones:** Solo Administrador, Organizador y Árbitro pueden crear; todos pueden consultar

---

## Módulos Detallados

### Usuarios y Perfiles
| Rol | Acción | Descripción |
|-----|--------|-------------|
| Admin | R, C, E, D | Control total |
| Organizador | R, C, E | Gestionar usuarios del torneo |
| Capitán | R, E | Solo editar su perfil |
| Árbitro | R | Ver perfiles |
| Jugador | R, E | Ver y editar su perfil |

### Torneos
| Rol | Acción | Descripción |
|-----|--------|-------------|
| Admin | R, C, E, D | Control total |
| Organizador | R, C, E, D | Crear y gestionar torneos |
| Capitán | R | Solo consulta |
| Árbitro | R | Información básica |
| Jugador | R | Ver torneos activos |

### Equipos
| Rol | Acción | Descripción |
|-----|--------|-------------|
| Admin | R, C, E, D | Control total |
| Organizador | R, C, E, D | Validar y gestionar equipos |
| Capitán | R, C, E | Crear/editar su equipo |
| Árbitro | R | Ver equipos en partidos |
| Jugador | R | Ver equipos inscriptos |

### Partidos
| Rol | Acción | Descripción |
|-----|--------|-------------|
| Admin | R, C, E, D | Control total |
| Organizador | R, C, E, D | Crear partidos, asignar arbitros |
| Capitán | R | Ver partidos de su equipo |
| Árbitro | R, E | Ver y actualizar estado |
| Jugador | R | Ver su calendario |

### Alineaciones
| Rol | Acción | Descripción |
|-----|--------|-------------|
| Admin | R, E, D | Override de sistemas |
| Organizador | R, E | Revisión general |
| Capitán | R, C, E | Crear alineación de su equipo |
| Árbitro | R | Ver alineaciones en partido |
| Jugador | R | Ver su disposición |

### Resultados
| Rol | Acción | Descripción |
|-----|--------|-------------|
| Admin | R, E, D | Correcciones finales |
| Organizador | R, E, D | Revisar y aprobar |
| Capitán | R | Ver resultado de su equipo |
| Árbitro | R, C, E | Registrar resultado en vivo |
| Jugador | R | Ver resultado final |

### Pagos
| Rol | Acción | Descripción |
|-----|--------|-------------|
| Admin | R, E, D | Aprobación final, auditoría |
| Organizador | R, C, E, D | Procesar y revisar |
| Capitán | R | Ver estado de pago |
| Árbitro | R | - |
| Jugador | R, C | Subir comprobante |

### Sanciones
| Rol | Acción | Descripción |
|-----|--------|-------------|
| Admin | R, C, E, D | Control total |
| Organizador | R, C, E | Crear y gestionar |
| Capitán | R | Ver sanciones de su equipo |
| Árbitro | R, C | Reportar infracciones |
| Jugador | R | Ver sus sanciones |

---

Actualizado: 2026-03-29
Proyecto: Tech Cup DOSW 2026-1
