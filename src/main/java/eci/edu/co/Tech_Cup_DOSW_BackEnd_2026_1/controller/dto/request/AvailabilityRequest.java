package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityRequest {
    
    @NotNull(message = "El estado de disponibilidad es requerido")
    private boolean available;
    
    @Size(max = 255, message = "La razón no puede exceder 255 caracteres")
    private String reason;
}
