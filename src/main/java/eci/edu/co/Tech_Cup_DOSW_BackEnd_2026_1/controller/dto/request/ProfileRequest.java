package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.DominantFoot;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para actualizar el perfil deportivo de un jugador")
public class ProfileRequest {

    @NotNull(message = "La posición es requerida")
    @Schema(description = "Posición del jugador en el campo", example = "FORWARD", requiredMode = Schema.RequiredMode.REQUIRED)
    private Position position;

    @Schema(description = "Posición secundaria del jugador en el campo", example = "MIDFIELDER")
    private Position secondaryPosition;

    @Schema(description = "Pie dominante del jugador", example = "RIGHT")
    private DominantFoot dominantFoot;

    @NotNull(message = "El dorsal es requerido")
    @Min(value = 1, message = "El dorsal debe ser mayor a 0")
    @Max(value = 99, message = "El dorsal debe ser menor a 100")
    @Schema(description = "Número de camiseta del jugador", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private int jerseyNumber;

    @Schema(description = "URL de la foto del jugador", example = "https://example.com/photo.jpg")
    private String photoUrl;

    @Schema(description = "Indica si el jugador está disponible para jugar", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean available;

    @Min(value = 1, message = "El semestre debe ser mayor a 0")
    @Max(value = 10, message = "El semestre debe ser menor a 11")
    @Schema(description = "Semestre académico del jugador", example = "5")
    private Integer semester;

    @Pattern(regexp = "^(Masculino|Femenino|Otro)$", message = "El género debe ser Masculino, Femenino u Otro")
    @Schema(description = "Género del jugador (M/F)", example = "M")
    private String gender;

    @Min(value = 15, message = "La edad mínima es 15 años")
    @Max(value = 60, message = "La edad máxima es 60 años")
    @Schema(description = "Edad del jugador en años", example = "20")
    private Integer age;
}
