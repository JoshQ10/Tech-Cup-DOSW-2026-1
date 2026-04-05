package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PhotoValidationUtil Tests")
class PhotoValidationUtilTest {

    private PhotoValidationUtil photoValidationUtil;
    private static final String VALID_JPEG_BASE64 = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAABAAEDASIAAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAv/xAAUEAEAAAAAAAAAAAAAAAAAAAAA/8VAFQEBAQAAAAAAAAAAAAAAAAAAAAX/xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwCwAA8A/9k=";
    private static final String VALID_PNG_BASE64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==";
    private static final String VALID_JPG_BASE64 = "data:image/jpg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAABAAEDASIAAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAv/xAAUEAEAAAAAAAAAAAAAAAAAAAAA/8VAFQEBAQAAAAAAAAAAAAAAAAAAAAX/xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwCwAA8A/9k=";

    @BeforeEach
    void setUp() {
        photoValidationUtil = new PhotoValidationUtil();
    }

    // ---- Valid photo tests ----

    @Test
    @DisplayName("Should validate JPEG photo successfully")
    void testValidateJpegPhotoSuccess() {
        assertDoesNotThrow(() -> photoValidationUtil.validateBase64Photo(VALID_JPEG_BASE64));
    }

    @Test
    @DisplayName("Should validate PNG photo successfully")
    void testValidatePngPhotoSuccess() {
        assertDoesNotThrow(() -> photoValidationUtil.validateBase64Photo(VALID_PNG_BASE64));
    }

    @Test
    @DisplayName("Should validate JPG photo successfully")
    void testValidateJpgPhotoSuccess() {
        assertDoesNotThrow(() -> photoValidationUtil.validateBase64Photo(VALID_JPG_BASE64));
    }

    // ---- Null and empty tests ----

    @Test
    @DisplayName("Should throw exception for null photo")
    void testValidateNullPhoto() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> photoValidationUtil.validateBase64Photo(null));
        assertTrue(exception.getMessage().contains("vacía"));
    }

    @Test
    @DisplayName("Should throw exception for empty photo")
    void testValidateEmptyPhoto() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> photoValidationUtil.validateBase64Photo(""));
        assertTrue(exception.getMessage().contains("vacía"));
    }

    @Test
    @DisplayName("Should throw exception for blank photo")
    void testValidateBlankPhoto() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> photoValidationUtil.validateBase64Photo("   "));
        assertTrue(exception.getMessage().contains("vacía"));
    }

    // ---- Format tests ----

    @Test
    @DisplayName("Should throw exception for missing comma separator")
    void testValidatePhotoMissingComma() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> photoValidationUtil.validateBase64Photo("data:image/jpegbase64ABC123"));
        assertTrue(exception.getMessage().contains("formato base64 válido"));
    }

    @Test
    @DisplayName("Should throw exception for invalid header format")
    void testValidatePhotoInvalidHeader() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> photoValidationUtil.validateBase64Photo("image/jpeg,ABC123"));
        assertTrue(exception.getMessage().contains("header base64 inválido"));
    }

    @Test
    @DisplayName("Should throw exception for unsupported MIME type")
    void testValidatePhotoUnsupportedMimeType() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> photoValidationUtil.validateBase64Photo("data:image/gif;base64,ABC123"));
        assertTrue(exception.getMessage().contains("no permitido"));
    }

    @Test
    @DisplayName("Should throw exception for invalid base64 encoding")
    void testValidatePhotoInvalidBase64() {
        // Use invalid base64 characters
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> photoValidationUtil.validateBase64Photo("data:image/jpeg;base64,@#$%^&*()"));
        assertTrue(exception.getMessage().contains("codificación base64 es inválida"));
    }

    @Test
    @DisplayName("Should throw exception when base64 data is only padding")
    void testValidatePhotoOnlyPadding() {
        // "=" is not valid base64
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> photoValidationUtil.validateBase64Photo("data:image/jpeg;base64,==="));
        assertTrue(exception.getMessage().contains("codificación base64 es inválida"));
    }

    // ---- Getter tests ----

    @Test
    @DisplayName("Should return correct max file size in MB")
    void testGetMaxFileSizeMB() {
        long maxSize = photoValidationUtil.getMaxFileSizeMB();
        assertEquals(5, maxSize);
    }

    @Test
    @DisplayName("Should return allowed MIME types")
    void testGetAllowedMimeTypes() {
        var mimeTypes = photoValidationUtil.getAllowedMimeTypes();
        assertTrue(mimeTypes.contains("image/jpeg"));
        assertTrue(mimeTypes.contains("image/jpg"));
        assertTrue(mimeTypes.contains("image/png"));
        assertEquals(3, mimeTypes.size());
    }
}
