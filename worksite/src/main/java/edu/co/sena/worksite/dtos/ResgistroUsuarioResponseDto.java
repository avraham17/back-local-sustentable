package edu.co.sena.worksite.dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ResgistroUsuarioResponseDto {

    private boolean isCreated;
    private long id;
    private String token;
    private String correoElectronico;
    private String rolNombre;
}