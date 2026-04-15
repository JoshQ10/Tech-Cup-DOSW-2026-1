package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
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

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        if (email == null || email.isBlank()) {
            log.error("OAuth2 provider did not return email attribute");
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_user_info", "OAuth2 provider did not return email", null));
        }

        User user = userRepository.findByEmail(email)
                .map(userPersistenceMapper::toModel)
                .orElseGet(() -> createOauthUser(email, name));

        ensureDefaults(user);
        log.debug("OAuth2 user processed successfully for email: {}", email);

        return oauth2User;
    }

    private User createOauthUser(String email, String name) {
        // Separar nombre y apellido si es posible
        String firstName = name != null ? name : "User";
        String lastName = "";
        if (name != null && name.contains(" ")) {
            String[] parts = name.split(" ", 2);
            firstName = parts[0];
            lastName = parts[1];
        }

        // Determinar tipo de usuario según dominio
        UserType userType = InstitutionEmailUtils.isEscuelaingEmail(email) ? UserType.INTERNAL : UserType.EXTERNAL;

        User newUser = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(email.split("@")[0])
                .email(email)
                .password("oauth2-" + UUID.randomUUID())
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
