package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistencia.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${app.oauth2.success-redirect-uri}")
    private String successRedirectUri;

    @Value("${app.oauth2.jwt.access-token-ttl-ms:${jwt.expiration-ms}}")
    private long oauth2AccessTokenTtlMs;

    @Value("${app.oauth2.jwt.refresh-token-ttl-ms:${jwt.refresh-expiration-ms}}")
    private long oauth2RefreshTokenTtlMs;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        if (email == null || email.isBlank()) {
            log.error("OAuth2 login failed: provider did not return email");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "OAuth2 provider did not return email");
            return;
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("OAuth2 login failed: local user not found after OAuth2 processing for {}", email);
                    return new IllegalStateException("OAuth2 local user integration failed");
                });

        String accessToken = jwtService.generateAccessToken(user, oauth2AccessTokenTtlMs);
        String refreshToken = jwtService.generateRefreshToken(user, oauth2RefreshTokenTtlMs);

        String redirectUrl = UriComponentsBuilder.fromUriString(successRedirectUri)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .queryParam("tokenType", "Bearer")
                .build(true)
                .toUriString();

        log.info("OAuth2 login successful for user: {}", email);
        response.sendRedirect(redirectUrl);
    }
}
