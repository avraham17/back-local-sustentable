package edu.co.sena.worksite.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResumenDto {
    private int id;
    private String nombre;
    private String correo;
    private long ofertasPublicadas;
}
