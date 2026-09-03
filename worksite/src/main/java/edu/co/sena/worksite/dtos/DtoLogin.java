package edu.co.sena.worksite.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class DtoLogin {
    private String correo;
    private String contrasenia;

}
