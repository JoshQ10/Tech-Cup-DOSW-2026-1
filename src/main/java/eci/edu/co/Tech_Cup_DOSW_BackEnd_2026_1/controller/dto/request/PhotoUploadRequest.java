package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para actualizar la foto de perfil de un jugador")
public class PhotoUploadRequest {

    @Schema(description = "URL pública de la foto del jugador", example = "https://example.com/photos/player1.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String photoUrl;
}
