package edu.co.sena.worksite.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class OfertaDetalleDto {
    private int id;
    private String titulo;
    private String ubicacion;
    private String descripcion;
    private float salario;
    private LocalDate fechaDePublicacion;
    private String estado;
    private String sector;
    private String modalidad;
    private String responsabilidades;
    private String requisitos;
    private String jornada;
    private String tipoDeContrato;
    private String experiencia;
    private String nivelEducativo;
    private LocalDate fechaDeCierre;
    private int postulaciones;

    private int idEmpresa;
    private String nombreEmpresa;
    private String sectorEmpresa;
    private String ubicacionEmpresa;
    private String telefonoEmpresa;
    private String correoEmpresa;
    private String fotoEmpresa;
}