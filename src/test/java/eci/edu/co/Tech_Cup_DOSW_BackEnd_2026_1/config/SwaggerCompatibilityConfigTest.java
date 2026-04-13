package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import static org.junit.jupiter.api.Assertions.*;

class SwaggerCompatibilityConfigTest {

    @Test
    void addViewControllers_isExecuted() {
        SwaggerCompatibilityConfig config = new SwaggerCompatibilityConfig();
        ViewControllerRegistry registry = new ViewControllerRegistry(null);

        assertDoesNotThrow(() -> config.addViewControllers(registry));
    }
}