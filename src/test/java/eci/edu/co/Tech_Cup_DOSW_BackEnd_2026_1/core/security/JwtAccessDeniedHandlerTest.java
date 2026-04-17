package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.io.OutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAccessDeniedHandlerTest {

    @Mock
    private ObjectMapper mockObjectMapper;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private AccessDeniedException mockAccessDeniedException;

    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @BeforeEach
    void setUp() {
        jwtAccessDeniedHandler = new JwtAccessDeniedHandler(mockObjectMapper);
    }

    @Test
    void testHandleMethodExecution() throws Exception {
        when(mockResponse.getOutputStream()).thenReturn(mock(ServletOutputStream.class));
        jwtAccessDeniedHandler.handle(mockRequest, mockResponse, mockAccessDeniedException);

        assertThat(jwtAccessDeniedHandler).isNotNull();
        verify(mockResponse).setStatus(403);
        verify(mockResponse).setContentType("application/json");
        verify(mockObjectMapper).writeValue(isA(OutputStream.class), any());
    }
}
