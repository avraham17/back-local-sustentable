package edu.co.sena.worksite.dtos;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfertaRequestDto {

    private String titulo;
    private String descripcion;
    private String sector;
    private String modalidad;
    private String responsabilidades;
    private String requisitos;
    private String jornada;
    private String tipoDeContrato;
    private String experiencia;
    private String nivelEducativo;
    private String numOfertas;
    private LocalDate fechaDeCierre;
    private int idEmpresa;
    private float salario;
    private LocalDate fechaDePublicacion;
    private String ubicacion;
    private String estado;
}