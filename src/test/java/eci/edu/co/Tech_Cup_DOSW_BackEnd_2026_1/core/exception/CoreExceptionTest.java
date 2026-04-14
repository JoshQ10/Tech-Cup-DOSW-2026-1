package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Core Exception Tests")
class CoreExceptionTest {

    @Test
    @DisplayName("ErrorResponse should have all builder fields")
    void testErrorResponseBuilder() {
        // Arrange & Act
        ErrorResponse response = ErrorResponse.builder()
                .status(400)
                .message("Test error")
                .timestamp(null)
                .details(null)
                .build();

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertEquals("Test error", response.getMessage());
    }

    @Test
    @DisplayName("BusinessException with message")
    void testBusinessExceptionWithMessage() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> { throw new BusinessException("Business rule violated"); });
        assertEquals("Business rule violated", exception.getMessage());
    }

    @Test
    @DisplayName("BusinessException with message and cause")
    void testBusinessExceptionWithCause() {
        // Arrange
        Throwable cause = new Throwable("Root cause");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> { throw new BusinessException("Business failed", cause); });
        assertEquals("Business failed", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("ValidationException should inherit RuntimeException")
    void testValidationException() {
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> { throw new ValidationException("Invalid input"); });
        assertEquals("Invalid input", exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("ResourceNotFoundException should inherit custom exception")
    void testResourceNotFoundException() {
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> { throw new ResourceNotFoundException("Resource not found"); });
        assertEquals("Resource not found", exception.getMessage());
    }

    @Test
    @DisplayName("NotFoundException should be recognized")
    void testNotFoundException() {
        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> { throw new NotFoundException("Item not found"); });
        assertEquals("Item not found", exception.getMessage());
    }

    @Test
    @DisplayName("BusinessRuleException should inherit custom exception")
    void testBusinessRuleException() {
        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> { throw new BusinessRuleException("Rule violated"); });
        assertEquals("Rule violated", exception.getMessage());
    }

    @Test
    @DisplayName("ErrorResponse with all fields set")
    void testErrorResponseWithAllFields() {
        // Arrange
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.util.Map<String, String> details = java.util.Map.of("key", "value");

        // Act
        ErrorResponse response = ErrorResponse.builder()
                .status(422)
                .message("Validation failed")
                .timestamp(now)
                .details(details)
                .build();

        // Assert
        assertEquals(422, response.getStatus());
        assertEquals("Validation failed", response.getMessage());
        assertEquals(now, response.getTimestamp());
        assertEquals(details, response.getDetails());
    }

    @Test
    @DisplayName("ErrorResponse equals and hashCode")
    void testErrorResponseEqualsAndHashCode() {
        // Arrange
        ErrorResponse response1 = ErrorResponse.builder()
                .status(400)
                .message("Error")
                .build();
        ErrorResponse response2 = ErrorResponse.builder()
                .status(400)
                .message("Error")
                .build();

        // Act & Assert
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    @DisplayName("ErrorResponse setter methods")
    void testErrorResponseSetters() {
        // Arrange
        ErrorResponse response = new ErrorResponse();

        // Act
        response.setStatus(500);
        response.setMessage("Server error");

        // Assert
        assertEquals(500, response.getStatus());
        assertEquals("Server error", response.getMessage());
    }

    @Test
    @DisplayName("Multiple exception types can be thrown and caught")
    void testMultipleExceptionTypes() {
        // Test BusinessException
        assertThrows(BusinessException.class,
                () -> { throw new BusinessException("Business error"); });

        // Test ValidationException
        assertThrows(ValidationException.class,
                () -> { throw new ValidationException("Validation error"); });

        // Test ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class,
                () -> { throw new ResourceNotFoundException("Not found"); });
    }
}
