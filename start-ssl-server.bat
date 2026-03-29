@echo off
echo ============================================
echo    🚀 SERVIDOR HTTPS/SSL - LISTO PARA PRUEBA
echo ============================================
echo.
echo 📍 Para iniciar el servidor HTTPS:
echo.
echo    cd ssl-test-app
echo    mvn spring-boot:run
echo.
echo 📍 Una vez iniciado, navega a:
echo    https://localhost:8443
echo.
echo 🔍 Para verificar el certificado SSL:
echo    1. Abre tu navegador web
echo    2. Ve a: https://localhost:8443
echo    3. ACEPTA la advertencia de certificado no confiable
echo    4. Haz clic en el candado 🔒 en la barra de direcciones
echo    5. Selecciona "Certificado" o "Ver certificado"
echo    6. Revisa los detalles del certificado
echo.
echo 📋 Endpoints disponibles:
echo    - https://localhost:8443/ (página principal)
echo    - https://localhost:8443/api/test/ssl
echo    - https://localhost:8443/api/test/cors
echo.
echo ⚠️  NOTA: El certificado es auto-firmado, por lo que
echo        el navegador mostrará una advertencia de seguridad.
echo        Esto es NORMAL para desarrollo.
echo.
echo ============================================
pause