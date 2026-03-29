package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.config;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtAuthenticationFilter;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2.CustomOAuth2UserService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2.OAuth2AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final CustomOAuth2UserService customOAuth2UserService;
        private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authz -> authz
                                                // Permitir acceso público a Swagger/OpenAPI
                                                .requestMatchers(
                                                                "/swagger-ui.html",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**",
                                                                "/api-docs/**",
                                                                "/oauth2/**",
                                                                "/login/oauth2/**")
                                                .permitAll()
                                                // Endpoints públicos de autenticación
                                                .requestMatchers("/api/auth/**").permitAll()
                                                // Requerir autenticación para todo lo demás
                                                .anyRequest().authenticated())
                                .csrf(csrf -> csrf.disable())
                                .httpBasic(basic -> basic.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                                .oauth2Login(oauth2 -> oauth2
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(customOAuth2UserService))
                                                .successHandler(oAuth2AuthenticationSuccessHandler))
                                .exceptionHandling(ex -> ex.authenticationEntryPoint(
                                                (request, response, authException) -> response.sendError(
                                                                HttpServletResponse.SC_UNAUTHORIZED,
                                                                "Unauthorized")))
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
