package edu.co.sena.worksite.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "oferta")
public class OfertaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "ubicacion")
    private String ubicacion;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "salario")
    private float salario;

    @ManyToOne
    @JoinColumn(name = "id_empresa", nullable = false)
    private EmpresaEntity empresa;

    @Column(name = "fecha_publicación")
    private LocalDate fechaDePublicacion;

    @Column(name = "estado")
    private String estado;

    @Column(name = "sector")
    private String sector;

    @Column(name = "modalidad")
    private String modalidad;

    @Column(name = "responsabilidades")
    private String responsabilidades;

    @Column(name = "requisitos")
    private String requisitos;

    @Column(name = "jornada")
    private String jornada;

    @Column(name = "tipo_de_contrato")
    private String tipoDeContrato;

    @Column(name = "experiencia")
    private String experiencia;

    @Column(name = "nivel_educativo")
    private String nivelEducativo;

    @Column(name = "num_ofertas")
    private String numOfertas;

    @Column(name = "fecha_de_cierre")
    private LocalDate fechaDeCierre;

    @Builder.Default
    @OneToMany(mappedBy = "oferta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostulacionEntity> postulaciones = new java.util.ArrayList<>();

}