package edu.co.sena.worksite.dtos;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidatoDetalleDto {
    private int id;
    private String nombres;
    private String apellidos;
    private String correoElectronico;
    private String numeroTelefonico;
    private String ciudad;
    private String cargo;
    private String estudio;
    private String descripcion;
    private String anosExperiencia;
    private String foto;
}
