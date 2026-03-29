@echo off
echo ============================================
echo    🚀 SERVIDOR HTTPS SIMPLE
echo ============================================
echo.
echo Ejecutando servidor HTTPS básico...
echo.
echo Una vez iniciado, abre: https://localhost:8443
echo.
echo Para ver el certificado:
echo 1. Acepta la advertencia de seguridad
echo 2. Haz clic en el candado -> Ver certificado
echo.
cd ssl-test-app
javac -cp "target/classes:$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout)" src/main/java/com/ssltest/SimpleSSLServer.java
java -cp "target/classes:$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout)" com.ssltest.SimpleSSLServer
pause