package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.LoginRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.LoginResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    UserResponse verifyEmail(String token);

    String resendVerification(String email);

    String forgotPassword(String email);

    String resetPassword(String token, String newPassword, String confirmPassword);

    String logout(String refreshToken);

    LoginResponse refreshToken(String refreshToken);

    LoginResponse loginWithGoogle(String idToken);
}
