package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Config Tests")
class ConfigTest {

    @Test
    @DisplayName("OpenApiConfig can be instantiated")
    void testOpenApiConfigInstantiation() {
        // Act
        OpenApiConfig config = new OpenApiConfig();

        // Assert
        assertNotNull(config);
    }

    @Test
    @DisplayName("OpenApiConfig bean creation")
    void testOpenApiConfigCreation() {
        // Arrange & Act
        OpenApiConfig config = new OpenApiConfig();

        // Assert
        assertNotNull(config);
        assertTrue(config.getClass().getName().contains("OpenApiConfig"));
    }

    @Test
    @DisplayName("Multiple config instances")
    void testMultipleConfigInstances() {
        // Act
        OpenApiConfig config1 = new OpenApiConfig();
        OpenApiConfig config2 = new OpenApiConfig();

        // Assert
        assertNotNull(config1);
        assertNotNull(config2);
        assertEquals(config1.getClass(), config2.getClass());
    }
}
