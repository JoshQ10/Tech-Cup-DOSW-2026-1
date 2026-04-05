package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.exception;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.handler.GlobalExceptionHandler;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.NotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("NotFoundException returns 404 with message")
    void notFoundExceptionReturns404() throws Exception {
        mockMvc.perform(get("/test/not-found").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Player not found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @DisplayName("ResourceNotFoundException returns 404 with message")
    void resourceNotFoundExceptionReturns404() throws Exception {
        mockMvc.perform(get("/test/resource-not-found").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Resource not found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @DisplayName("BusinessException returns 400 with message")
    void businessExceptionReturns400() throws Exception {
        mockMvc.perform(get("/test/business").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Business rule violated"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @DisplayName("BusinessRuleException returns 400 with message")
    void businessRuleExceptionReturns400() throws Exception {
        mockMvc.perform(get("/test/business-rule").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Business rule exception"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @DisplayName("ValidationException returns 400 with message and field details")
    void validationExceptionReturns400WithDetails() throws Exception {
        mockMvc.perform(get("/test/validation").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details.email").value("must not be blank"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @DisplayName("ValidationException without details returns 400")
    void validationExceptionWithoutDetailsReturns400() throws Exception {
        mockMvc.perform(get("/test/validation-simple").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Simple validation error"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @DisplayName("AccessDeniedException returns 403 with message")
    void accessDeniedExceptionReturns403() throws Exception {
        mockMvc.perform(get("/test/access-denied").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value("Access denied: insufficient permissions"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @DisplayName("Generic exception returns 500 with generic message")
    void genericExceptionReturns500() throws Exception {
        mockMvc.perform(get("/test/generic").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Internal server error"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @RestController
    static class TestController {

        @GetMapping("/test/not-found")
        public void notFound() {
            throw new NotFoundException("Player not found");
        }

        @GetMapping("/test/resource-not-found")
        public void resourceNotFound() {
            throw new ResourceNotFoundException("Resource not found");
        }

        @GetMapping("/test/business")
        public void business() {
            throw new BusinessException("Business rule violated");
        }

        @GetMapping("/test/business-rule")
        public void businessRule() {
            throw new BusinessRuleException("Business rule exception");
        }

        @GetMapping("/test/validation")
        public void validation() {
            throw new ValidationException("Validation failed", Map.of("email", "must not be blank"));
        }

        @GetMapping("/test/validation-simple")
        public void validationSimple() {
            throw new ValidationException("Simple validation error");
        }

        @GetMapping("/test/access-denied")
        public void accessDenied() {
            throw new AccessDeniedException("Forbidden");
        }

        @GetMapping("/test/generic")
        public void generic() {
            throw new RuntimeException("Unexpected error");
        }
    }
}
