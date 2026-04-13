package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {

    private static final long MAX_AVATAR_SIZE = 5L * 1024L * 1024L; // 5MB
    private static final long MAX_FULL_PHOTO_SIZE = 10L * 1024L * 1024L; // 10MB
    private static final int REQUIRED_FULL_PHOTO_WIDTH = 1428;
    private static final int REQUIRED_FULL_PHOTO_HEIGHT = 2920;
    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/jpg", "image/png");

    private final Path uploadRoot;

    public FileStorageService(@Value("${app.storage.upload-dir:uploads}") String uploadDir) {
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public String storeAvatar(MultipartFile file, Long userId) {
        validateImage(file, MAX_AVATAR_SIZE, false);
        return store(file, "avatars", "avatar-" + userId);
    }

    public String storeFullPhoto(MultipartFile file, Long userId) {
        validateImage(file, MAX_FULL_PHOTO_SIZE, true);
        return store(file, "full-photos", "full-photo-" + userId);
    }

    private void validateImage(MultipartFile file, long maxSize, boolean requireFullPhotoDimensions) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Debes enviar un archivo de imagen");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Formato no permitido. Solo JPEG o PNG");
        }

        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("La imagen excede el tamaño máximo permitido");
        }

        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new IllegalArgumentException("El archivo no corresponde a una imagen válida");
            }

            if (requireFullPhotoDimensions
                    && (image.getWidth() != REQUIRED_FULL_PHOTO_WIDTH
                            || image.getHeight() != REQUIRED_FULL_PHOTO_HEIGHT)) {
                throw new IllegalArgumentException("La foto de cuerpo completo debe tener dimensiones 1428x2920 px");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("No fue posible leer la imagen enviada", e);
        }
    }

    private String store(MultipartFile file, String subDir, String baseName) {
        try {
            Path targetDir = uploadRoot.resolve(subDir);
            Files.createDirectories(targetDir);

            String extension = extractExtension(file.getOriginalFilename(), file.getContentType());
            String fileName = baseName + "-" + UUID.randomUUID() + extension;
            Path targetFile = targetDir.resolve(fileName);

            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            String relativeUrl = "/uploads/" + subDir + "/" + fileName;
            log.info("Imagen almacenada exitosamente en {}", targetFile);
            return relativeUrl;
        } catch (IOException e) {
            log.error("Error almacenando imagen", e);
            throw new IllegalStateException("Error al almacenar la imagen", e);
        }
    }

    private String extractExtension(String fileName, String contentType) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf('.')).toLowerCase(Locale.ROOT);
        }

        if (contentType != null && contentType.toLowerCase(Locale.ROOT).contains("png")) {
            return ".png";
        }

        return ".jpg";
    }
}