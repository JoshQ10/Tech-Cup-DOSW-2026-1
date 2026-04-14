package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        private static final String BEARER_AUTH = "bearerAuth";

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
                                .components(new Components().addSecuritySchemes(BEARER_AUTH,
                                                new SecurityScheme()
                                                                .name(BEARER_AUTH)
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")))
                                .info(new Info()
                                                .title("TechCup Futbol - Backend API")
                                                .version("1.0.0")
                                                .description("Plataforma digital para la gestión del torneo semestral de fútbol")
                                                .contact(new Contact()
                                                                .name("TechCup DOSW 2026-1")
                                                                .url("https://github.com/JoshQ10/Tech-Cup-DOSW-2026-1"))
                                                .license(new License()
                                                                .name("MIT License")));
        }
}
