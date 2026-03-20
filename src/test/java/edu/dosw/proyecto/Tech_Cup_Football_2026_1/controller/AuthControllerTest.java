package edu.dosw.proyecto.Tech_Cup_Football_2026_1.controller;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.RegistroRequest;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.TipoParticipante;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegistroExitoso() throws Exception {
        RegistroRequest solicitud = new RegistroRequest(
            "Carlos Uribe",
            "carlos@escuelaing.edu.co",
            "Password123",
            "Password123",
            TipoParticipante.ESTUDIANTE
        );

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitud)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.exitoso").value(true))
                .andExpect(jsonPath("$.email").value("carlos@escuelaing.edu.co"));
    }

    @Test
    void testRegistroConEmailInvalido() throws Exception {
        RegistroRequest solicitud = new RegistroRequest(
            "Carlos Uribe",
            "emailInvalido",
            "Password123",
            "Password123",
            TipoParticipante.ESTUDIANTE
        );

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitud)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exitoso").value(false));
    }

    @Test
    void testVerificarEmail() throws Exception {
        mockMvc.perform(get("/api/auth/verificar")
                .param("token", "test-token-123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Email verificado exitosamente"));
    }
}

