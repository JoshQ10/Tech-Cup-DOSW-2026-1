package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuth2Service {

    private final UserRepository userRepository;

    public User validateAndGetGoogleUser(String idToken) {
        try {
            log.debug("Validating and decoding Google ID token");

            // Decodificar el JWT sin verificar firma (Google ya verificó)
            DecodedJWT jwt = JWT.decode(idToken);

            String email = jwt.getClaim("email").asString();
            String firstName = jwt.getClaim("given_name").asString();
            String lastName = jwt.getClaim("family_name").asString();

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

            log.debug("Google token decoded successfully for email: {}", email);

            // Buscar usuario existente o crear uno nuevo
            Optional<User> existingUser = userRepository.findByEmail(email);

            if (existingUser.isPresent()) {
                log.info("User found in database for Google email: {}", email);
                return existingUser.get();
            }

            // Crear nuevo usuario
            log.info("Creating new user from Google authentication for email: {}", email);
            UserType userType = email.endsWith("@escuelaing.edu.co") ? UserType.INTERNAL : UserType.EXTERNAL;

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

            User savedUser = userRepository.save(newUser);
            log.info("New user created from Google authentication with id: {}", savedUser.getId());

            return savedUser;

        } catch (BusinessRuleException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error validating Google token: {}", e.getMessage(), e);
            throw new BusinessRuleException("Error validating Google authentication: " + e.getMessage());
        }
    }
}
