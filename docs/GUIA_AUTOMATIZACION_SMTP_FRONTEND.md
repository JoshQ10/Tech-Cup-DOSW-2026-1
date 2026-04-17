# GUIA DE AUTOMATIZACION SMTP Y PRUEBAS SIN POWERSHELL MANUAL

## Objetivo

Dejar un flujo simple para:

1. Iniciar backend en HTTPS.
2. Probar registro y reenvio de verificacion.
3. Ejecutar smoke test del flujo auth.
4. Integrar backend con frontend sin depender de comandos manuales en PowerShell.

## Prerrequisitos minimos

1. Completar credenciales SMTP reales en .env (o usar el wizard SMTP).
2. Tener backend abierto en VS Code.
3. Usar las tareas de VS Code (Run Task) o los .cmd de scripts.

### Resend API (recomendado para verificacion)

Configura en .env:

1. RESEND_ENABLED=true
2. RESEND_PREFER_API=true
3. RESEND_API_KEY=tu_api_key_real
4. RESEND_FROM=onboarding@resend.dev (o dominio verificado en Resend)
5. RESEND_FALLBACK_FROM=onboarding@resend.dev

Si tu dominio personalizado no esta verificado, el backend reintentara con RESEND_FALLBACK_FROM automaticamente.

## Opcion A (recomendada): un clic desde VS Code

Abrir Command Palette y ejecutar Tasks: Run Task. Luego usar estas tareas:

1. Backend: Start HTTPS (real email forced)
2. Backend: Open Swagger
3. Backend: Smoke auth flow

Si quieres configuracion guiada de proveedor real:

1. Backend: Resend API setup wizard
2. Luego Backend: Start HTTPS (real email forced)

## Opcion B: doble clic sin terminal manual

Desde la carpeta scripts:

1. dev-one-click-real-email.cmd
2. smoke-auth-flow.cmd

## Paso a paso para probar registro y verificacion en Swagger

1. Abrir Swagger en https://localhost:8443/swagger-ui.html.
2. Ejecutar POST /api/auth/register con un email real.
3. Verificar respuesta:
   - 201: registro correcto y correo de verificacion enviado.
   - 503: proveedor de correo rechazo o timeout.
4. Ejecutar POST /api/auth/resend-verification con el mismo email.
5. Revisar correo de entrada y spam.
6. En entorno dev-http, validar token con GET /api/auth/dev/user-status?email=...

### Prueba concreta solicitada

Para validar el correo registrado:

1. Ejecuta POST /api/auth/resend-verification con email `juan.bohorquez-m@mail.escuelaing.edu.co`.
2. Debe responder 200 con mensaje de reenvio.
3. Confirma que llega correo a `juan.bohorquez-m@mail.escuelaing.edu.co` (inbox/spam/promociones).
4. Si no llega, revisa en logs si aparece `provider resend-api` y el status devuelto por Resend.

## Automatizacion para conectar frontend con backend

### Flujo sugerido en frontend

1. Registro:
   - Llamar POST /api/auth/register.
   - Si 201, mostrar pantalla "Revisa tu correo".

2. Reenvio:
   - Boton "Reenviar verificacion" llama POST /api/auth/resend-verification.
   - Mostrar resultado de confirmacion.

3. Estado en dev (solo pruebas):
   - Si necesitas diagnostico, usar GET /api/auth/dev/user-status?email=...
   - No usar endpoint dev en produccion.

### Recomendacion operativa

1. Equipo backend inicia con tarea "Backend: Start HTTPS (real email forced)".
2. Equipo frontend arranca su app y consume API en https://localhost:8443.
3. QA ejecuta "Backend: Smoke auth flow" antes de pruebas E2E.

## Resultado esperado

Con esta configuracion no necesitas escribir comandos manuales en PowerShell para ejecutar el flujo de registro/verificacion durante desarrollo.
