package com.ssltest;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class TomcatConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        // Habilitar conector HTTP en puerto 8080 con redirección a HTTPS
        Connector httpConnector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        httpConnector.setPort(8080);
        httpConnector.setScheme("http");
        httpConnector.setSecure(false);
        httpConnector.setRedirectPort(8443);

        factory.addAdditionalTomcatConnectors(httpConnector);
    }
}
