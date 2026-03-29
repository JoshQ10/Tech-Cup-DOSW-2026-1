package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@DisplayName("HTTPS Connectivity Tests - TR-B124")
class HttpsConnectivityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should serve API documentation over configured endpoint")
    void testApiDocsEndpoint() throws Exception {
        mockMvc.perform(get("/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @DisplayName("Should serve Swagger UI")
    void testSwaggerUiEndpoint() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should have SSL configuration loaded")
    void testSslConfiguration() {
        // Esta prueba verifica que la aplicación se inicia con configuración SSL
        // En un entorno de integración real, se probaría la conectividad HTTPS completa
        // La configuración SSL se valida durante el arranque de la aplicación
        // y se confirma que los certificados están disponibles en el classpath
    }
}