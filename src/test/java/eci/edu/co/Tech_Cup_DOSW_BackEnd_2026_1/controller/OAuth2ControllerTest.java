package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2.CustomOAuth2UserService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OAuth2Controller.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("OAuth2Controller Tests")
class OAuth2ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Test
    @DisplayName("Should return Google authorization URL")
    void testGoogleAuthorizationUrl() throws Exception {
        mockMvc.perform(get("/api/auth/oauth2/google"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorizationUrl").value("/oauth2/authorization/google"));
    }

    @Test
    @DisplayName("Should return success response with valid tokens")
    void testSuccessCallbackWithTokens() throws Exception {
        mockMvc.perform(get("/api/auth/oauth2/success")
                        .param("accessToken", "my-access-token")
                        .param("refreshToken", "my-refresh-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("my-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("my-refresh-token"));
    }

    @Test
    @DisplayName("Should return 400 when access token is missing")
    void testSuccessCallbackWithoutToken() throws Exception {
        mockMvc.perform(get("/api/auth/oauth2/success"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Missing access token in OAuth2 callback"));
    }

    @Test
    @DisplayName("Should return failure response with error message")
    void testFailureCallback() throws Exception {
        mockMvc.perform(get("/api/auth/oauth2/failure")
                        .param("error", "access_denied"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("access_denied"));
    }

    @Test
    @DisplayName("Should return default failure message when no error param")
    void testFailureCallbackDefault() throws Exception {
        mockMvc.perform(get("/api/auth/oauth2/failure"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("OAuth2 authentication failed"));
    }
}
