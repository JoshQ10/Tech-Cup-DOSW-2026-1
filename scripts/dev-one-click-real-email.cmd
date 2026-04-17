@echo off
setlocal
cd /d "%~dp0.."
start "TechCup Backend HTTPS" cmd /k "scripts\start-backend-real-email.cmd"
timeout /t 15 /nobreak >nul
start "" "https://localhost:8443/swagger-ui.html"
echo Backend iniciado en otra ventana y Swagger abierto en el navegador.
echo Si es la primera vez con HTTPS, acepta el certificado local del navegador.
endlocal
