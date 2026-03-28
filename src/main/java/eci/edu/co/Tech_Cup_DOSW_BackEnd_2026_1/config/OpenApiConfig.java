package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
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
