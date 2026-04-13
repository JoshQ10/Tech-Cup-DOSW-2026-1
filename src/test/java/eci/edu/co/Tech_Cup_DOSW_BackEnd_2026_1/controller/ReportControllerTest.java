package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean TournamentRepository tournamentRepository;
    @MockBean TeamRepository teamRepository;
    @MockBean MatchRepository matchRepository;
    @MockBean UserRepository userRepository;
    @MockBean PaymentRepository paymentRepository;
    @MockBean SanctionRepository sanctionRepository;
    @MockBean StandingRepository standingRepository;

    @Test
    void overviewExecuted() throws Exception {
        mockMvc.perform(get("/api/reports/overview"))
                .andExpect(status().isUnauthorized());
    }
}
