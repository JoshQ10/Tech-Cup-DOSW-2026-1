package com.ssltest;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SSL Test API - TechCup")
                        .description("API de prueba para validar configuración HTTPS/SSL y CORS")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("TechCup Development Team")
                                .email("techcup@eci.edu.co"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("https://localhost:8443")
                                .description("Servidor HTTPS Local"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor HTTP Local (redirección automática)")));
    }
}