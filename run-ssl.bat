@echo off
echo ============================================
echo    🚀 INICIANDO SERVIDOR HTTPS/SSL
echo ============================================
echo.
echo 📍 URL del servidor: https://localhost:8443
echo.
echo 🔍 Para verificar el certificado SSL:
echo    1. Abre tu navegador
echo    2. Ve a: https://localhost:8443
echo    3. Haz clic en el candado 🔒 en la barra de direcciones
echo    4. Selecciona "Certificado" o "Ver certificado"
echo    5. Revisa los detalles: emisor, fechas, algoritmo, etc.
echo.
echo 📋 Endpoints disponibles:
echo    - https://localhost:8443/ (página principal)
echo    - https://localhost:8443/api/test/ssl
echo    - https://localhost:8443/api/test/cors
echo.
echo ⚠️  NOTA: Acepta la advertencia de certificado no confiable
echo        (es normal para certificados auto-firmados)
echo.
echo Presiona Ctrl+C para detener el servidor
echo ============================================
echo.
mvn spring-boot:run -Dspring-boot.run.main-class=eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.SSLTestApplication -Dspring-boot.run.profiles=ssl
pause