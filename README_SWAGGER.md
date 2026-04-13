# 🚀 TECHCUP BACKEND - GUÍA RÁPIDA DE SWAGGER

## ✅ PROYECTO LIMPIADO Y LISTO

Se han **eliminado TODOS los archivos innecesarios**:
- ❌ Todos los scripts `.bat` (run-ssl.bat, run-simple-ssl.bat, etc.)
- ❌ Scripts de prueba SSL duplicados
- ❌ Archivos basura (ProfileResponse, TeamResponse, Ver, test-output.log, build.log)
- ❌ SWAGGER_SETUP_GUIDE.md (reemplazado por esta guía)

---

## 🎯 COMANDO PARA EJECUTAR (ELIGE UNO)

### **OPCIÓN 1 - Script PowerShell (✅ RECOMENDADO)**

```powershell
cd "E:\DOSW\Proyecto_T2\Backend\Tech-Cup-DOSW-2026-1-main\Tech-Cup-DOSW-2026-1-main"

.\run-dev.ps1
```

**Esto hace:**
1. Limpia compilaciones anteriores
2. Compila el proyecto
3. Inicia el servidor en `http://localhost:8080`

---

### **OPCIÓN 2 - Comando Directo (Sin Script)**

```powershell
cd "E:\DOSW\Proyecto_T2\Backend\Tech-Cup-DOSW-2026-1-main\Tech-Cup-DOSW-2026-1-main"

.\mvnw.cmd clean

.\mvnw.cmd spring-boot:run -- --spring.profiles.active=dev-http
```

---

### **OPCIÓN 3 - Solo Ejecutar (Sin Limpiar - Más Rápido)**

```powershell
cd "E:\DOSW\Proyecto_T2\Backend\Tech-Cup-DOSW-2026-1-main\Tech-Cup-DOSW-2026-1-main"

.\mvnw.cmd spring-boot:run -- --spring.profiles.active=dev-http
```

---

## 🌐 ACCESO A SWAGGER

### **Paso 1: Esperar a que el servidor inicie**

Verás en la consola:
```
Tomcat started on port(s): 8080 (http) with context path ''
Started TechCupDoswBackEnd20261Application
```

### **Paso 2: Abrir en navegador**

Copia esta URL en tu navegador:
```
http://localhost:8080/swagger-ui.html
```

### **Paso 3: Explorar Endpoints**

Deberías ver:
```
┌─────────────────────────────────────┐
│  TechCup Futbol - Backend API v1.0  │
│                                     │
│  ✓ Authentication                   │
│  ✓ Players                          │
│  ✓ Teams                            │
│  ✓ Tournaments                      │
│  ✓ Profiles                         │
└─────────────────────────────────────┘
```

---

## 📱 URLs ÚTILES

| Recurso | URL |
|---------|-----|
| **Swagger UI** | `http://localhost:8080/swagger-ui.html` |
| **OpenAPI Spec (JSON)** | `http://localhost:8080/v3/api-docs` |
| **Swagger Resources** | `http://localhost:8080/swagger-resources` |
| **Base de Datos H2** | `http://localhost:8080/h2-console` |
| **API Base** | `http://localhost:8080` |

---

## 🔧 CONFIGURACIÓN APLICADA

### **application.yaml (por defecto ahora)**
- Perfil activo: `dev-http`
- Puerto: `8080`
- SSL: Deshabilitado
- Base de datos: H2 en memoria
- OAuth2 Google deshabilitado (puede configurarse)

### **application-dev-http.yaml**
- Puerto HTTP: `8080`
- Swagger: Habilitado y público
- H2 Console: Habilitada
- Logs: DEBUG level para el proyecto

### **application-dev.yaml (alternativa)**
- Puerto HTTPS: `8443`
- SSL: Habilitado con certificado autofirmado
- Swagger: Habilitado en HTTPS

### **Controladores Disponibles**
- `AuthController` - Autenticación y registro
- `OAuth2Controller` - Google OAuth2
- `PlayerController` - Jugadores
- `TeamController` - Equipos
- `TournamentController` - Torneos
- `ProfileController` - Perfiles

---

## ⚠️ TROUBLESHOOTING

### **"Port 8080 already in use"**
```powershell
# Encuentra el proceso
Get-Process | Where-Object {$_.Name -like "*java*"}

# Mata el proceso (reemplaza PID)
Stop-Process -Id <PID> -Force
```

### **"Can't reach server" / "Connection refused"**
- Verifica que NO cerraste PowerShell (el servidor sigue corriendo)
- Espera 15-20 segundos después de ver "Tomcat started"
- Limpia caché: `Ctrl+Shift+Del` y recarga `Ctrl+F5`

### **Swagger UI en blanco**
```powershell
# Recarga fuerza:
# En navegador: Ctrl+F5

# O reinicia el servidor:
# En PowerShell: Ctrl+C
# Luego ejecuta: .\run-dev.ps1
```

### **Error de compliación**
```powershell
# Limpia completamente:
.\mvnw.cmd clean

# Intenta compilar nuevamente:
.\mvnw.cmd compile -DskipTests
```

---

## 📝 NOTAS IMPORTANTES

1. **Perfil activo por defecto:** `dev-http` (HTTP sin SSL)
2. **Base de datos:** H2 en memoria (se reinicia con el servidor)
3. **Los datos desaparecen** al detener el servidor (intencional para dev)
4. **JWT en Swagger:** Botón "Authorize" en esquina superior derecha
5. **Logs:** Carpeta `logs/` en la raíz del proyecto

---

## 🎯 RESUMEN RÁPIDO

| Acción | Comando |
|--------|---------|
| **Ejecutar servidor** | `.\run-dev.ps1` |
| **Detener servidor** | `Ctrl+C` en PowerShell |
| **Acceder a Swagger** | `http://localhost:8080/swagger-ui.html` |
| **Acceder a H2 DB** | `http://localhost:8080/h2-console` |
| **Compilar solo** | `.\mvnw.cmd compile -DskipTests` |
| **Ver OpenAPI JSON** | `http://localhost:8080/v3/api-docs` |

---

## ✨ ¡LISTO!

Tu proyecto está completamente limpio y optimizado para desarrollo. 

**Próximos pasos:**
1. Ejecuta: `.\run-dev.ps1`
2. Espera a que inicie
3. Abre: `http://localhost:8080/swagger-ui.html`
4. ¡Explora los endpoints!

