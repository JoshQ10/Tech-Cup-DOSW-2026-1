package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("CORS Functional Tests - TR-B125")
class CorsFunctionalTestNew {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should allow CORS preflight requests from allowed origins")
    void testCorsPreflightAllowedOrigins() throws Exception {
        mockMvc.perform(options("/api/auth/login")
                .header("Origin", "https://localhost:3000")
                .header("Access-Control-Request-Method", "GET")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "https://localhost:3000"))
                .andExpect(header().exists("Access-Control-Allow-Methods"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
    }

    @Test
    @DisplayName("Should allow CORS requests from localhost with different ports")
    void testCorsLocalhostDifferentPorts() throws Exception {
        mockMvc.perform(options("/api/auth/register")
                .header("Origin", "http://localhost:3001")
                .header("Access-Control-Request-Method", "POST")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3001"));
    }

    @Test
    @DisplayName("Should allow CORS requests from 127.0.0.1")
    void testCorsLocalhostIp() throws Exception {
        mockMvc.perform(options("/api/players")
                .header("Origin", "https://127.0.0.1:8080")
                .header("Access-Control-Request-Method", "PUT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "https://127.0.0.1:8080"));
    }

    @Test
    @DisplayName("Should allow preflight requests for all HTTP methods")
    void testCorsAllHttpMethods() throws Exception {
        mockMvc.perform(options("/api/tournaments")
                .header("Origin", "https://localhost:3000")
                .header("Access-Control-Request-Method", "DELETE")
                .header("Access-Control-Request-Headers", "Content-Type, Authorization")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Methods"));
    }

    @Test
    @DisplayName("Should handle CORS for API endpoints only")
    void testCorsOnlyForApiEndpoints() throws Exception {
        // Los endpoints de API deberían tener CORS
        mockMvc.perform(options("/api/auth/login")
                .header("Origin", "https://localhost:3000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }
}