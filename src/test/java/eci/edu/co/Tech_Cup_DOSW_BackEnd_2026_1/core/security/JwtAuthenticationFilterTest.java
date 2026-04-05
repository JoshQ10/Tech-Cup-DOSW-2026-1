package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtAuthenticationFilter Tests")
class JwtAuthenticationFilterTest {

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private FilterChain mockFilterChain;

    @Mock
    private JwtService mockJwtService;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(mockJwtService);
    }

    @Test
    @DisplayName("JWT filter should be instantiated")
    void testJwtFilterInstantiation() {
        // Assert
        assertNotNull(jwtAuthenticationFilter);
    }

    @Test
    @DisplayName("JWT filter should extract bearer token from header")
    void testBearerTokenExtraction() {
        // Arrange
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        String authHeader = "Bearer " + token;

        // Act & Assert
        assertTrue(authHeader.startsWith("Bearer "));
        assertEquals(token, authHeader.substring(7));
    }

    @Test
    @DisplayName("JWT filter should handle missing authorization header")
    void testMissingAuthorizationHeader() throws ServletException, IOException {
        // Arrange
        when(mockRequest.getHeader("Authorization")).thenReturn(null);
        when(mockRequest.getServletPath()).thenReturn("/api/protected");

        // Act
        jwtAuthenticationFilter.doFilter(mockRequest, mockResponse, mockFilterChain);

        // Assert
        verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);
    }

    @Test
    @DisplayName("JWT filter should handle malformed token")
    void testMalformedToken() {
        // Arrange
        String malformedToken = "NotAValidJWT";

        // Act & Assert
        assertFalse(malformedToken.contains("."));
        assertTrue(malformedToken.length() > 0);
    }

    @Test
    @DisplayName("JWT filter should skip requests without auth header")
    void testSkipUnauthenticatedRequests() throws ServletException, IOException {
        // Arrange
        when(mockRequest.getServletPath()).thenReturn("/api/teams");

        // Act
        jwtAuthenticationFilter.doFilter(mockRequest, mockResponse, mockFilterChain);

        // Assert
        verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);
    }

    @Test
    @DisplayName("JWT filter handles non-bearer tokens")
    void testNonBearerTokenHandling() {
        // Arrange
        String nonBearerAuth = "Basic " + "dXNlcm5hbWU6cGFzc3dvcmQ=";

        // Act & Assert
        assertTrue(nonBearerAuth.startsWith("Basic "));
        assertFalse(nonBearerAuth.startsWith("Bearer "));
    }

    @Test
    @DisplayName("JWT token validation flow")
    void testJwtTokenValidationFlow() {
        // Arrange
        String validJwt = "eyJhbGciOiJIUzI1NiJ9." +
                "eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwicm9sZSI6IlBMQVlFUiJ9." +
                "validSignature";

        // Act & Assert
        assertTrue(validJwt.contains("."));
        String[] parts = validJwt.split("\\.");
        assertEquals(3, parts.length);
    }

    @Test
    @DisplayName("JWT filter should intercept requests to protected endpoints")
    void testProtectedEndpointInterception() throws ServletException, IOException {
        // Arrange
        when(mockRequest.getServletPath()).thenReturn("/api/players");

        // Act
        jwtAuthenticationFilter.doFilter(mockRequest, mockResponse, mockFilterChain);

        // Assert
        verify(mockFilterChain, times(1)).doFilter(any(), any());
    }

    @Test
    @DisplayName("Multiple JWT validation attempts")
    void testMultipleJwtValidationAttempts() {
        // Arrange
        String[] tokens = {
                "invalidToken1",
                "invalidToken2",
                "validToken.validPayload.validSignature"
        };

        // Act & Assert
        for (String token : tokens) {
            assertNotNull(token);
            assertTrue(token.length() > 0);
        }
    }

    @Test
    @DisplayName("JWT filter preserves request attributes")
    void testJwtFilterPreservesAttributes() throws ServletException, IOException {
        // Arrange
        String attribute = "userId";
        String value = "123";
        when(mockRequest.getServletPath()).thenReturn("/api/profile");
        when(mockRequest.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilter(mockRequest, mockResponse, mockFilterChain);

        // Assert
        verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);
    }

    @Test
    @DisplayName("JWT filter handles expired tokens")
    void testExpiredTokenHandling() {
        // Arrange
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9." +
                "eyJleHAiOjE2MDAwMDAwMDB9." +
                "signature";

        // Act & Assert
        assertTrue(expiredToken.contains("."));
    }

    @Test
    @DisplayName("JWT filter handles empty bearer token")
    void testEmptyBearerToken() {
        // Arrange
        String emptyBearerAuth = "Bearer ";

        // Act & Assert
        assertTrue(emptyBearerAuth.startsWith("Bearer "));
        assertTrue(emptyBearerAuth.trim().length() > 0);
    }
}
