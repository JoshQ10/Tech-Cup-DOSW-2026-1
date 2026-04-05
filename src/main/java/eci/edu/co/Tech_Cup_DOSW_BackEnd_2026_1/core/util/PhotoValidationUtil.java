package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class PhotoValidationUtil {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final Set<String> ALLOWED_MIME_TYPES = new HashSet<>();
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>();

    static {
        ALLOWED_MIME_TYPES.add("image/jpeg");
        ALLOWED_MIME_TYPES.add("image/jpg");
        ALLOWED_MIME_TYPES.add("image/png");
        
        ALLOWED_EXTENSIONS.add(".jpg");
        ALLOWED_EXTENSIONS.add(".jpeg");
        ALLOWED_EXTENSIONS.add(".png");
    }

    /**
     * Valida si la foto en base64 es válida en formato, tipo y tamaño
     *
     * @param base64Photo la foto en formato base64 (data:image/...;base64,...)
     * @return true si la foto es válida, false en caso contrario
     * @throws IllegalArgumentException si la foto no cumple los requisitos
     */
    public void validateBase64Photo(String base64Photo) {
        if (base64Photo == null || base64Photo.isBlank()) {
            throw new IllegalArgumentException("La foto no puede estar vacía");
        }

        // Verificar formato base64
        if (!base64Photo.contains(",")) {
            throw new IllegalArgumentException("La foto debe estar en formato base64 válido");
        }

        String[] parts = base64Photo.split(",", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Formato base64 inválido");
        }

        String mimeType = extractMimeType(parts[0]);
        String base64Data = parts[1];

        // Validar tipo MIME
        if (!ALLOWED_MIME_TYPES.contains(mimeType)) {
            throw new IllegalArgumentException(
                String.format("Tipo de archivo no permitido. Formatos soportados: %s", 
                    ALLOWED_MIME_TYPES));
        }

        // Validar tamaño del archivo
        long estimatedSize = estimateBase64Size(base64Data);
        if (estimatedSize > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                String.format("El archivo excede el tamaño máximo permitido de %d MB", 
                    MAX_FILE_SIZE / (1024 * 1024)));
        }

        // Validar que sea base64 válido
        try {
            Base64.getDecoder().decode(base64Data);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("La codificación base64 es inválida", e);
        }

        log.info("Foto validada exitosamente. MIME type: {}, Tamaño estimado: {} bytes", 
            mimeType, estimatedSize);
    }

    /**
     * Extrae el tipo MIME del header base64
     *
     * @param header el header base64 (data:image/...;base64)
     * @return el tipo MIME extraído
     */
    private String extractMimeType(String header) {
        // Extraer entre "data:" y ";base64"
        if (header.startsWith("data:") && header.endsWith(";base64")) {
            return header.substring(5, header.length() - 7);
        }
        throw new IllegalArgumentException("Formato de header base64 inválido");
    }

    /**
     * Estima el tamaño real del archivo decodificado desde base64
     *
     * @param base64Data los datos en base64
     * @return tamaño estimado en bytes
     */
    private long estimateBase64Size(String base64Data) {
        // Remover padding
        int padding = 0;
        if (base64Data.endsWith("==")) {
            padding = 2;
        } else if (base64Data.endsWith("=")) {
            padding = 1;
        }
        return (base64Data.length() * 3) / 4 - padding;
    }

    /**
     * Obtiene el tamaño máximo permitido en MB
     *
     * @return tamaño máximo en MB
     */
    public long getMaxFileSizeMB() {
        return MAX_FILE_SIZE / (1024 * 1024);
    }

    /**
     * Obtiene los tipos MIME permitidos
     *
     * @return conjunto de tipos MIME permitidos
     */
    public Set<String> getAllowedMimeTypes() {
        return new HashSet<>(ALLOWED_MIME_TYPES);
    }
}
