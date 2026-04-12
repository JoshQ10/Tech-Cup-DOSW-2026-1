package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerCompatibilityConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/swagger-ui.htm", "/swagger-ui.html");
        registry.addRedirectViewController("/api-docs", "/v3/api-docs");
    }
}
