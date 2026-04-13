package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.StandingRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StandingController.class)
class StandingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StandingRepository standingRepository;

    @Test
    void listExecuted() throws Exception {
        mockMvc.perform(get("/api/standings"))
                .andExpect(status().isUnauthorized());
    }
}