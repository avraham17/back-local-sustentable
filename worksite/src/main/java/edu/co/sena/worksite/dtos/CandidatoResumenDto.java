package edu.co.sena.worksite.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CandidatoResumenDto {
    private int id;
    private String nombreCompleto;
    private String correoElectronico;
    private String cargo;
}
