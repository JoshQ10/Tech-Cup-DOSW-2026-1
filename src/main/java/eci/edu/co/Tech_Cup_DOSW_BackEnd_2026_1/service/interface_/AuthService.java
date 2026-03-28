package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.interface_;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.LoginRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.response.LoginResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    UserResponse verifyEmail(String token);

    String resendVerification(String email);

    LoginResponse refreshToken(String refreshToken);
}
