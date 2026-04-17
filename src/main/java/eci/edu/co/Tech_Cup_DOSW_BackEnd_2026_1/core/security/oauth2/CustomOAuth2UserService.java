package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.EmailService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.InstitutionEmailUtils;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.UserPersistenceMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final UserPersistenceMapper userPersistenceMapper;
    private final EmailService emailService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String email = oauth2User.getAttribute("email");
        if (email == null || email.isBlank()) {
            // Microsoft puede retornar el email en preferred_username
            email = oauth2User.getAttribute("preferred_username");
        }
        String name = oauth2User.getAttribute("name");
        String givenName = oauth2User.getAttribute("given_name");
        String familyName = oauth2User.getAttribute("family_name");

        if (email == null || email.isBlank()) {
            log.error("OAuth2 provider did not return email attribute");
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_user_info", "OAuth2 provider did not return email", null));
        }

        final String finalEmail = email;
        final String finalRegistrationId = registrationId;
        final String finalName = name;
        final String finalGivenName = givenName;
        final String finalFamilyName = familyName;

        boolean isNewUser = userRepository.findByEmail(finalEmail).isEmpty();

        User user = userRepository.findByEmail(finalEmail)
                .map(userPersistenceMapper::toModel)
                .orElseGet(() -> createOauthUser(finalEmail, finalName, finalGivenName, finalFamilyName, finalRegistrationId));

        if (isNewUser) {
            try {
                emailService.sendOAuth2WelcomeEmail(
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getRole(),
                        finalRegistrationId);
            } catch (Exception e) {
                log.warn("OAuth2 welcome email failed for {}: {}", user.getEmail(), e.getMessage());
            }
        }

        ensureDefaults(user);
        log.debug("OAuth2 user processed successfully for email: {}", finalEmail);

        return oauth2User;
    }

    private User createOauthUser(String email, String name, String givenName, String familyName,
            String registrationId) {
        String firstName = givenName != null && !givenName.isBlank() ? givenName
                : (name != null ? name : "User");
        String lastName = familyName != null && !familyName.isBlank() ? familyName : "";
        if ((familyName == null || familyName.isBlank()) && name != null && name.contains(" ")) {
            String[] parts = name.split(" ", 2);
            firstName = parts[0];
            lastName = parts[1];
        }

        // Determinar tipo de usuario según dominio
        UserType userType = InstitutionEmailUtils.isEscuelaingEmail(email) ? UserType.INTERNAL : UserType.EXTERNAL;

        User newUser = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(email.split("@")[0] + "-" + UUID.randomUUID().toString().substring(0, 8))
                .email(email)
                .password("oauth2-" + registrationId + "-" + UUID.randomUUID())
                .role(Role.PLAYER)
                .userType(userType)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        @SuppressWarnings("null")
        User saved = userPersistenceMapper.toModel(
                userRepository.save(userPersistenceMapper.toEntity(newUser)));
        log.info("Created local account for OAuth2 user: {} with type: {}", email, userType);
        return saved;
    }

    private void ensureDefaults(User user) {
        boolean needsUpdate = false;

        if (user.getRole() == null) {
            user.setRole(Role.PLAYER);
            needsUpdate = true;
        }
        if (user.getUserType() == null) {
            user.setUserType(UserType.STUDENT);
            needsUpdate = true;
        }
        if (!user.isActive()) {
            user.setActive(true);
            needsUpdate = true;
        }

        if (needsUpdate) {
            log.debug("Normalizing OAuth2 user defaults for: {}", user.getEmail());
            userRepository.save(userPersistenceMapper.toEntity(user));
        }
    }
}
