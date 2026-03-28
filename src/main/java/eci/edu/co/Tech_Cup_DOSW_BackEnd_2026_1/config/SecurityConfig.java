package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // Permitir acceso público a Swagger/OpenAPI
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api-docs/**")
                        .permitAll()
                        // Permitir acceso público a los endpoints de API
                        .requestMatchers("/api/**").permitAll()
                        // Requerir autenticación para todo lo demás
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para facilitar testing
                .httpBasic(basic -> basic.disable()); // Deshabilitar HTTP Basic Auth

        return http.build();
    }
}
