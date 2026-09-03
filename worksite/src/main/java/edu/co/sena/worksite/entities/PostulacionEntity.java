package edu.co.sena.worksite.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "postulacion",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_postulacion_oferta_candidato",
                columnNames = {"id_oferta", "id_candidato"}
        )
)
public class PostulacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int idPostulacion;

    @ManyToOne
    @JoinColumn(name = "id_oferta", nullable = false)
    private OfertaEntity oferta;

    @ManyToOne
    @JoinColumn(name = "id_candidato", nullable = false)
    private ResgistroUsuarioEntity candidato;

    @Column(name = "fecha_postulacion")
    private LocalDate fechaPostulacion;

    @Column(name = "estado_postulacion")
    private String estadoPostulacion;
}