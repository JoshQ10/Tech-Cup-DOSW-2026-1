package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoUploadRequest {
    
    @NotBlank(message = "La foto en base64 es requerida")
    @Pattern(regexp = "^data:image/(jpeg|jpg|png);base64,[A-Za-z0-9+/=]+$", 
             message = "La foto debe estar en formato base64 válido (jpeg, jpg o png)")
    private String photoUrl;
}
