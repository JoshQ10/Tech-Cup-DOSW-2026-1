package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import io.swagger.v3.oas.annotations.Operation;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ForgotPasswordRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.LoginRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.LogoutRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RefreshTokenRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ResetPasswordRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ResendVerificationRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.GoogleLoginRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.LoginResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.UserResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.AuthService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Registration and login endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Creates a new user account in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("REST register endpoint called for email: {}", request.getEmail());
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates a user with email or username and returns JWT tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "401", description = "Incorrect username or password")
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("REST login endpoint called for email: {}", request.getEmail());
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify-email")
    @Operation(summary = "Verify email", description = "Verifies the user account using the received token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email successfully verified"),
            @ApiResponse(responseCode = "404", description = "User or invalid token")
    })
    public ResponseEntity<UserResponse> verifyEmail(
            @Parameter(description = "Token de verificacion", required = true) @RequestParam String token) {
        log.info("REST verify-email endpoint called with token");
        UserResponse response = authService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-verification")
    @Operation(summary = "Resend verification email", description = "Resends the verification email to a registered user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email successfully resent"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<String> resendVerification(@Valid @RequestBody ResendVerificationRequest request) {
        log.info("REST resend-verification endpoint called for email: {}", request.getEmail());
        String response = authService.resendVerification(request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh JWT token", description = "Renews the user's authentication token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token successfully renewed"),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("REST refresh-token endpoint called");
        LoginResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesion", description = "Invalida el refresh token del usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sesion cerrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Refresh token invalido"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequest request) {
        log.info("REST logout endpoint called");
        String response = authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Solicitar recuperacion de contrasena", description = "Genera y envia un enlace de recuperacion al correo del usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud procesada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("REST forgot-password endpoint called for email: {}", request.getEmail());
        String response = authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Restablecer contrasena", description = "Restablece la contrasena usando un token de recuperacion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrasena restablecida"),
            @ApiResponse(responseCode = "400", description = "Token invalido o datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Token no encontrado")
    })
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        log.info("REST reset-password endpoint called");
        String response = authService.resetPassword(request.getToken(), request.getNewPassword(),
                request.getConfirmPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/google-login")
    @Operation(summary = "Login con Google OAuth2", description = "Autentica un usuario usando Google ID Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "400", description = "Token de Google inválido"),
            @ApiResponse(responseCode = "401", description = "Token de Google expirado o inválido")
    })
    public ResponseEntity<LoginResponse> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {
        log.info("REST google-login endpoint called");
        LoginResponse response = authService.loginWithGoogle(request.getIdToken());
        return ResponseEntity.ok(response);
    }
}
