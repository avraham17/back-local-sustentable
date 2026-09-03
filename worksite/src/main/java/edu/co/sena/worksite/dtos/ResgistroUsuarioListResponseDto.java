package edu.co.sena.worksite.dtos;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResgistroUsuarioListResponseDto {

    private int  id;
    private String nombres;
    private String apellidos;
    private String tipoIdentificacion;
    private String cedula;
    private LocalDate fechaNacimiento;
    private String correoElectronico;
    private String genero;
    private String numeroTelefonico;
    private String anosExperiencia;
    private String contrasenia;
    private String Ciudad;
    private String Cargo;
    private String estudio;
    private String Descripcion;
    private String rolNombre;
    private String foto;
    private String cv;
    private String token;

}