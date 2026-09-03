package edu.co.sena.worksite.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "resgistro_usuario")
public class ResgistroUsuarioEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "tipo_identificación")
    private String tipoIdentificacion;

    @Column(name = "cedula")
    private String cedula;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    @Column(name = "genero")
    private String genero;

    @Column(name = "numero_telefono")
    private String numeroTelefonico;

    @Column(name = "anos_experiencia")
    private String anosExperiencia;

    @Column(name = "contrasenia")
    private String contrasenia;

    @Column(name = "Ciudad")
    private String ciudad;

    @Column(name = "Cargo")
    private String cargo;

    @Column(name = "estudio")
    private String estudio;

    @Column(name = "Descripcion")
    private String descripcion;

    @Column(name = "foto", columnDefinition = "LONGTEXT")
    private String foto;

    @Column(name = "cv", columnDefinition = "LONGTEXT")
    private String cv;

    @ManyToOne
    @JoinColumn(name = "name_rol")
    private RolEntity rolNombre;

    @Builder.Default
    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostulacionEntity> postulaciones = new ArrayList<>();
}