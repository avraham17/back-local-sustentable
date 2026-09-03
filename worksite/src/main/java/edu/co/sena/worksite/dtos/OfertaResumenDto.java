package edu.co.sena.worksite.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfertaResumenDto {
    private int id;
    private String titulo;
    private String empresa;
    private String estado;
    private Long postulaciones;
}
