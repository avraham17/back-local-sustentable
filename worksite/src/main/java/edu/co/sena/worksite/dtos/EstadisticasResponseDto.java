package edu.co.sena.worksite.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasResponseDto {

    // Usuarios
    private long totalUsuarios;
    private long totalCandidatos;
    private long totalEmpresas;

    // Postulaciones
    private long totalPostulaciones;
    private long postulacionesAceptadas;
    private long postulacionesRechazadas;
    private long postulacionesPendientes;

    // Ofertas (bonus, ya que estábamos)
    private long totalOfertas;
    private long ofertasActivas;
}
