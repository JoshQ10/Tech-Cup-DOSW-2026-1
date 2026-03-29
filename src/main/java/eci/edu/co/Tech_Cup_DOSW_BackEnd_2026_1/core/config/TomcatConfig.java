package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * Configuración de Tomcat para habilitar redirección automática de HTTP a
 * HTTPS.
 * 
 * Esta clase implementa la redirección de HTTP (puerto 8080) a HTTPS (puerto
 * 8443),
 * permitiendo a los clientes HTTP ser redirigidos automáticamente a la versión
 * segura.
 * 
 * Requisito: TR-B122 - Configurar redirección automática de HTTP a HTTPS
 */
@Component
public class TomcatConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        // Habilitar conector HTTP en puerto 8080 con redirección a HTTPS en puerto 8443
        Connector httpConnector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        httpConnector.setPort(8080);
        httpConnector.setScheme("http");
        httpConnector.setSecure(false);
        httpConnector.setRedirectPort(8443);

        factory.addAdditionalTomcatConnectors(httpConnector);
    }
}
