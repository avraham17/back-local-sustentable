package edu.co.sena.worksite.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResgistroUsuarioRequestDto {

    @NotNull
    private String nombres;

    @NotNull
    private String apellidos;

    @NotNull
    private String tipoIdentificacion;

    @NotNull
    private String cedula;

    @NotNull
    private LocalDate fechaNacimiento;

    @NotNull
    private String correoElectronico;

    @NotNull
    private String genero;

    @NotNull
    private String numeroTelefonico;

    @NotNull
    private String anosExperiencia;

    @NotNull
    private String contrasenia;

    @NotNull
    private String ciudad;

    @NotNull
    private String cargo;

    @NotNull
    private String estudio;

    @NotNull
    private String descripcion;

    private String rolNombre;

    private String foto;

    private String cv;

    private String codigoAdmin;

}