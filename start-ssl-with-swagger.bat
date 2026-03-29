@echo off
echo Iniciando aplicación SSL con Swagger...
cd ssl-test-app
start cmd /k "mvn spring-boot:run"
timeout /t 10 /nobreak > nul
start https://localhost:8443
start https://localhost:8443/swagger-ui.html
echo.
echo Aplicación iniciada. URLs abiertas en navegador.
echo Presiona cualquier tecla para continuar...
pause > nul