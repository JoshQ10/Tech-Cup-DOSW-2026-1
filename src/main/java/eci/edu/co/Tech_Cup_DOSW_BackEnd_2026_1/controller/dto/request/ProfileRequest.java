package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Position;
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
public class ProfileRequest {
    
    @NotNull(message = "La posición es requerida")
    private Position position;
    
    @NotNull(message = "El dorsal es requerido")
    @Min(value = 1, message = "El dorsal debe ser mayor a 0")
    @Max(value = 99, message = "El dorsal debe ser menor a 100")
    private int jerseyNumber;
    
    private String photoUrl;
    
    private boolean available;
    
    @Min(value = 1, message = "El semestre debe ser mayor a 0")
    @Max(value = 10, message = "El semestre debe ser menor a 11")
    private Integer semester;
    
    @Pattern(regexp = "^(Masculino|Femenino|Otro)$", message = "El género debe ser Masculino, Femenino u Otro")
    private String gender;
    
    @Min(value = 15, message = "La edad mínima es 15 años")
    @Max(value = 60, message = "La edad máxima es 60 años")
    private Integer age;
}
