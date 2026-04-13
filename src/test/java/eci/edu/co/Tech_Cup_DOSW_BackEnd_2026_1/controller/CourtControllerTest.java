package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.CourtRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourtController.class)
class CourtControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CourtRepository courtRepository;

    @MockBean
    TournamentRepository tournamentRepository;

    @Test
    void listExecuted() throws Exception {
        mockMvc.perform(get("/api/courts"))
                .andExpect(status().isUnauthorized());
    }
}