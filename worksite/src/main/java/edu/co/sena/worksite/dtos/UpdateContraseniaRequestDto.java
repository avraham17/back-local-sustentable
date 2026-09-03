package edu.co.sena.worksite.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateContraseniaRequestDto {

    @NotBlank(message = "La contraseña actual es obligatoria")
    private String contraseniaActual;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    private String contraseniaNueva;
}
