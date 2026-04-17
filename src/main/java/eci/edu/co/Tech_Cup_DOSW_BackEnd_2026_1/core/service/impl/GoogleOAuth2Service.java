package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.EmailService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.InstitutionEmailUtils;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.UserPersistenceMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuth2Service {

    private static final String GOOGLE_TOKENINFO_URL =
            "https://oauth2.googleapis.com/tokeninfo?id_token=";

    private final UserRepository userRepository;
    private final UserPersistenceMapper userPersistenceMapper;
    private final EmailService emailService;

    @Value("${spring.security.oauth2.client.registration.google.client-id:}")
    private String googleClientId;

    public User validateAndGetGoogleUser(String idToken) {
        try {
            log.debug("Verifying Google ID token via tokeninfo endpoint");

            Map<String, Object> tokenInfo = verifyGoogleIdToken(idToken);

            String email = (String) tokenInfo.get("email");
            String firstName = (String) tokenInfo.getOrDefault("given_name", "");
            String lastName = (String) tokenInfo.getOrDefault("family_name", "");

            if (email == null || email.isBlank()) {
                log.warn("Invalid Google token: missing email claim");
                throw new BusinessRuleException("Invalid Google ID token: missing email");
            }

            if (firstName == null || firstName.isBlank()) {
                firstName = email.split("@")[0];
            }
            if (lastName == null || lastName.isBlank()) {
                lastName = "";
            }

            log.debug("Google token verified successfully for email: {}", email);

            // Buscar usuario existente o crear uno nuevo
            var existingUser = userRepository.findByEmail(email)
                    .map(userPersistenceMapper::toModel);

            if (existingUser.isPresent()) {
                log.info("User found in database for Google email: {}", email);
                return existingUser.get();
            }

            // Crear nuevo usuario
            log.info("Creating new user from Google authentication for email: {}", email);
            UserType userType = InstitutionEmailUtils.isEscuelaingEmail(email) ? UserType.INTERNAL : UserType.EXTERNAL;

            User newUser = User.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .username(email.split("@")[0] + "-" + UUID.randomUUID().toString().substring(0, 8))
                    .email(email)
                    .password("oauth2-google-" + UUID.randomUUID())
                    .role(Role.PLAYER)
                    .userType(userType)
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .build();

            @SuppressWarnings("null")
            User savedUser = userPersistenceMapper.toModel(
                    userRepository.save(userPersistenceMapper.toEntity(newUser)));
            log.info("New user created from Google authentication with id: {}", savedUser.getId());

            try {
                emailService.sendOAuth2WelcomeEmail(
                        savedUser.getEmail(),
                        savedUser.getFirstName(),
                        savedUser.getLastName(),
                        savedUser.getRole(),
                        "google");
            } catch (Exception e) {
                log.warn("OAuth2 welcome email failed for {}: {}", savedUser.getEmail(), e.getMessage());
            }

            return savedUser;

        } catch (BusinessRuleException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error validating Google token: {}", e.getMessage(), e);
            throw new BusinessRuleException("Error validating Google authentication: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> verifyGoogleIdToken(String idToken) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            Map<String, Object> tokenInfo = restTemplate.getForObject(
                    GOOGLE_TOKENINFO_URL + idToken, Map.class);

            if (tokenInfo == null) {
                throw new BusinessRuleException("Google token verification returned empty response");
            }

            // Verificar que el token es para esta aplicación (evita token substitution)
            if (googleClientId != null && !googleClientId.isBlank()) {
                String aud = (String) tokenInfo.get("aud");
                if (!googleClientId.equals(aud)) {
                    log.warn("Google token audience mismatch: expected={}, got={}", googleClientId, aud);
                    throw new BusinessRuleException("Invalid Google ID token: audience mismatch");
                }
            }

            return tokenInfo;
        } catch (HttpClientErrorException e) {
            log.warn("Google token verification rejected by Google: {}", e.getResponseBodyAsString());
            throw new BusinessRuleException("Invalid or expired Google ID token");
        } catch (BusinessRuleException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error calling Google tokeninfo endpoint: {}", e.getMessage());
            throw new BusinessRuleException("Error verifying Google authentication");
        }
    }
}
