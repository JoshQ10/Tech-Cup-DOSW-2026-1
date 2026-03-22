# Tech-Cup-DOSW-2026-1

Proyecto integrador de conocimientos del segundo corte de ciclos de vida.

## Clases implementadas y que hace cada una

### Aplicacion y configuracion
- `TechCupFootball20261Application`: clase principal de Spring Boot. Inicia la aplicacion y ejecuta el `CommandLineRunner` para sembrar roles base en la base de datos (`JUGADOR`, `CAPITAN`, etc.) si no existen.
- `SecurityConfig`: define la configuracion de seguridad del proyecto (reglas de acceso, filtros, y comportamiento de autenticacion/autorizacion segun lo implementado en tu proyecto).
- `ConfiguracionCORS`: habilita y configura CORS para permitir llamadas desde frontend u otros orígenes al backend.
- `PasswordEncoderConfig` / `PasswordEncoderUtil`: provee la codificacion de contrasenas con BCrypt para no guardar passwords en texto plano.

### Controladores
- `AuthController`: expone endpoints de autenticacion. Actualmente maneja `POST /api/auth/register` para registrar usuarios y `GET /api/auth/verificar` para verificacion de correo.
- `Controller`: controlador base/general del proyecto (estructura inicial, util para endpoints comunes si se amplía).

### Servicios
- `RegistroService`: contiene la logica de negocio del registro. Valida datos, valida reglas de email por tipo de participante, verifica duplicados, selecciona rol, codifica password, guarda usuario, crea token de verificacion y solicita envio de correo.
- `EmailService`: encapsula el envio de correos del sistema (por ejemplo, correo de verificacion al registrarse).

### Repositorios
- `UsuarioRepository`: acceso a datos de usuarios. Permite consultar por email, validar existencia, listar por tipo de participante y listar activos.
- `RolRepository`: acceso a datos de roles. Permite buscar y validar existencia de roles por nombre.
- `TokenVerificacionRepository`: acceso a datos de tokens de verificacion. Permite buscar token por valor, consultar por usuario y eliminar tokens por usuario (util para reenvio/expiracion).

### Modelos (entidades)
- `Usuario`: entidad principal de cuenta de usuario. Almacena datos personales, email, password cifrada, estado activo/verificado, tipo de participante, rol y `descripcionRelacionExterna` cuando aplica.
- `Rol`: entidad de roles del sistema. Define el enum de roles permitidos (`JUGADOR`, `CAPITAN`, etc.) y su persistencia.
- `TokenVerificacion`: entidad para confirmar email. Guarda token unico, usuario asociado, fechas de creacion/expiracion y estado de uso.

### DTOs
- `RegistroRequest`: objeto de entrada del endpoint de registro. Transporta nombre, email, password, confirmacion, tipo de participante, rol solicitado y descripcion de relacion externa.
- `RegistroResponse`: objeto de salida del registro. Devuelve id, datos basicos del usuario, rol, tipo de participante, mensaje de resultado y bandera de exito.

### Validadores
- `EmailValidator`: valida formato de correo y aplica reglas de negocio por tipo de participante (institucional para internos y Gmail para externos, segun tu logica actual).

### Pruebas unitarias creadas/actualizadas
- `RegistroServiceTest`: valida los escenarios clave del registro (casos exitosos y errores de validacion/reglas de negocio).
- `AuthControllerTest`: prueba el comportamiento del controlador de autenticacion en forma unitaria (sin MockMvc).
- `EmailValidatorTest`: verifica las reglas de validacion de correos por formato y por tipo de participante.

## Estado funcional actual del modulo de registro
- Endpoint de registro implementado: `POST /api/auth/register`.
- Validaciones activas: nombre, email, tipo de participante, password y confirmacion.
- Regla de correo por tipo de participante.
- Soporte de rol solicitado (`JUGADOR` / `CAPITAN`) con valor por defecto.
- Generacion de token de verificacion y asociacion al usuario.
- Envio de correo de verificacion.
- Soporte para `descripcionRelacionExterna` cuando el usuario es `EXTERNO`.
