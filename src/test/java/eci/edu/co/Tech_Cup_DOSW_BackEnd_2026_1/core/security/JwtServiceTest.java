package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtService Tests")
class JwtServiceTest {

    private JwtService jwtService;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Create JwtService with test configuration
        String jwtSecret = "d3BlcnQtdGhlLmRvb3ItdG8tZmlyZS1kYW1nZS1maWZlLWZvcm1lZC1zaW5ndWxhci1ucw=="; // Base64 encoded
        long jwtExpirationMs = 3600000; // 1 hour
        long refreshExpirationMs = 86400000; // 24 hours

        jwtService = new JwtService(jwtSecret, jwtExpirationMs, refreshExpirationMs);

        testUser = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .role(Role.PLAYER)
                .build();
    }

    @Test
    @DisplayName("Should generate access token successfully")
    void testGenerateAccessToken() {
        // Act
        String token = jwtService.generateAccessToken(testUser);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    @DisplayName("Should generate refresh token successfully")
    void testGenerateRefreshToken() {
        // Act
        String token = jwtService.generateRefreshToken(testUser);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    @DisplayName("Should extract email from access token")
    void testExtractEmailFromAccessToken() {
        // Arrange
        String token = jwtService.generateAccessToken(testUser);

        // Act
        String extractedEmail = jwtService.extractEmail(token);

        // Assert
        assertEquals("test@example.com", extractedEmail);
    }

    @Test
    @DisplayName("Should extract token type from access token")
    void testExtractAccessTokenType() {
        // Arrange
        String token = jwtService.generateAccessToken(testUser);

        // Act
        String tokenType = jwtService.extractTokenType(token);

        // Assert
        assertEquals("access", tokenType);
    }

    @Test
    @DisplayName("Should extract token type from refresh token")
    void testExtractRefreshTokenType() {
        // Arrange
        String token = jwtService.generateRefreshToken(testUser);

        // Act
        String tokenType = jwtService.extractTokenType(token);

        // Assert
        assertEquals("refresh", tokenType);
    }

    @Test
    @DisplayName("Should extract role from access token")
    void testExtractRoleFromAccessToken() {
        // Arrange
        String token = jwtService.generateAccessToken(testUser);

        // Act
        String role = jwtService.extractRole(token);

        // Assert
        assertEquals("PLAYER", role);
    }

    @Test
    @DisplayName("Should validate valid access token")
    void testValidateValidAccessToken() {
        // Arrange
        String token = jwtService.generateAccessToken(testUser);

        // Act
        boolean isValid = jwtService.isTokenValid(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should identify valid access token")
    void testIsValidAccessToken() {
        // Arrange
        String token = jwtService.generateAccessToken(testUser);

        // Act
        boolean isAccessToken = jwtService.isAccessToken(token);

        // Assert
        assertTrue(isAccessToken);
    }

    @Test
    @DisplayName("Should identify valid refresh token")
    void testIsValidRefreshToken() {
        // Arrange
        String token = jwtService.generateRefreshToken(testUser);

        // Act
        boolean isRefreshToken = jwtService.isRefreshToken(token);

        // Assert
        assertTrue(isRefreshToken);
    }

    @Test
    @DisplayName("Should not identify refresh token as access token")
    void testRefreshTokenIsNotAccessToken() {
        // Arrange
        String token = jwtService.generateRefreshToken(testUser);

        // Act
        boolean isAccessToken = jwtService.isAccessToken(token);

        // Assert
        assertFalse(isAccessToken);
    }

    @Test
    @DisplayName("Should not identify access token as refresh token")
    void testAccessTokenIsNotRefreshToken() {
        // Arrange
        String token = jwtService.generateAccessToken(testUser);

        // Act
        boolean isRefreshToken = jwtService.isRefreshToken(token);

        // Assert
        assertFalse(isRefreshToken);
    }

    @Test
    @DisplayName("Should validate malformed token as invalid")
    void testValidateMalformedToken() {
        // Arrange
        String malformedToken = "invalid.token.here";

        // Act
        boolean isValid = jwtService.isTokenValid(malformedToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should validate empty token as invalid")
    void testValidateEmptyToken() {
        // Act
        boolean isValid = jwtService.isTokenValid("");

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should generate access token with custom expiration")
    void testGenerateAccessTokenWithCustomExpiration() {
        // Arrange
        long customExpirationMs = 1800000; // 30 minutes

        // Act
        String token = jwtService.generateAccessToken(testUser, customExpirationMs);

        // Assert
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    @DisplayName("Should generate refresh token with custom expiration")
    void testGenerateRefreshTokenWithCustomExpiration() {
        // Arrange
        long customExpirationMs = 604800000; // 7 days

        // Act
        String token = jwtService.generateRefreshToken(testUser, customExpirationMs);

        // Assert
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    @DisplayName("Should generate tokens with different content for different users")
    void testGenerateDifferentTokensForDifferentUsers() {
        // Arrange
        User anotherUser = User.builder()
                .id(2L)
                .firstName("Another")
                .lastName("User")
                .username("anotheruser")
                .email("another@example.com")
                .password("password")
                .role(Role.CAPTAIN)
                .build();

        // Act
        String token1 = jwtService.generateAccessToken(testUser);
        String token2 = jwtService.generateAccessToken(anotherUser);

        // Assert
        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2);
        assertNotEquals(jwtService.extractEmail(token1), jwtService.extractEmail(token2));
    }
}
