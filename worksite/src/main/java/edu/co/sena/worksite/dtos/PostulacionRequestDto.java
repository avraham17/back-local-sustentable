package edu.co.sena.worksite.dtos;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostulacionRequestDto {

    private int idOferta;
    private int idCandidato;
    private LocalDate fechaPostulacion;
    private String estadoPostulacion;
}