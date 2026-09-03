package edu.co.sena.worksite.dtos;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostulacionResumenDto {
    private int id;
    private String ofertaTitulo;
    private String candidatoNombre;
    private LocalDate fechaPostulacion;
    private String estadoPostulacion;
}