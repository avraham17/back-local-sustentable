package edu.co.sena.worksite.dtos;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostulacionListResponseDto {
    private int idPostulacion;

    private String oferta;
    private int idOferta;

    private String candidato;
    private String correoCandidato;
    private int idCandidato;

    private LocalDate fechaPostulacion;
    private String estadoPostulacion;
}