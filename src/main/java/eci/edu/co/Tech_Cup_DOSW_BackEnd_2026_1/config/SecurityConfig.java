package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.config;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtAccessDeniedHandler;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtAuthEntryPoint;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtAuthenticationFilter;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2.CustomOAuth2UserService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import org.springframework.context.annotation.Profile;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Profile("!test") // Solo usar esta config en perfil no-test (producción, dev)
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final JwtAuthEntryPoint jwtAuthEntryPoint;
        private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
        private final CustomOAuth2UserService customOAuth2UserService;
        private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

        @Value("${app.security.require-ssl:false}")
        private boolean requireSsl;

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                if (requireSsl) {
                        http.requiresChannel(channel -> channel.anyRequest().requiresSecure());
                }

                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .authorizeHttpRequests(authz -> authz
                                                // Permitir acceso público a Swagger/OpenAPI
                                                .requestMatchers(
                                                                "/swagger-ui.htm",
                                                                "/swagger-ui.html",
                                                                "/swagger-ui/**",
                                                                "/swagger-resources/**",
                                                                "/swagger-resources",
                                                                "/v3/api-docs/**",
                                                                "/v3/api-docs",
                                                                "/api-docs/**",
                                                                "/api-docs",
                                                                "/webjars/**",
                                                                "/h2-console/**",
                                                                "/oauth2/**",
                                                                "/login/oauth2/**",
                                                                "/actuator/health",
                                                                "/actuator/health/**")
                                                .permitAll()
                                                .requestMatchers("/api/auth/logout").authenticated()
                                                // Endpoints públicos de autenticación
                                                .requestMatchers("/api/auth/**").permitAll()
                                                // Requerir autenticación para todo lo demás
                                                .anyRequest().authenticated())
                                .csrf(csrf -> csrf.disable())
                                .httpBasic(basic -> basic.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .oauth2Login(oauth2 -> oauth2
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(customOAuth2UserService))
                                                .successHandler(oAuth2AuthenticationSuccessHandler))
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint(jwtAuthEntryPoint)
                                                .accessDeniedHandler(jwtAccessDeniedHandler))
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOriginPatterns(
                                Arrays.asList("https://localhost:*", "http://localhost:*", "https://127.0.0.1:*",
                                                "http://127.0.0.1:*"));
                configuration.setAllowedMethods(
                                Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
                configuration.setAllowedHeaders(Arrays.asList("*"));
                configuration.setAllowCredentials(true);
                configuration.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
