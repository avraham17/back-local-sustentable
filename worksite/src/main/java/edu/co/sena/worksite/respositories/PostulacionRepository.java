package edu.co.sena.worksite.respositories;

import edu.co.sena.worksite.dtos.PostulacionResumenDto;
import edu.co.sena.worksite.entities.OfertaEntity;
import edu.co.sena.worksite.entities.PostulacionEntity;
import edu.co.sena.worksite.entities.ResgistroUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostulacionRepository extends
        JpaRepository<PostulacionEntity, Long>,
        JpaSpecificationExecutor<PostulacionEntity> {

    List<PostulacionEntity> findByOferta_Id(int idOferta);

    List<PostulacionEntity> findByCandidato_Id(int idCandidato);

    boolean existsByOfertaAndCandidato(OfertaEntity oferta, ResgistroUsuarioEntity candidato);

    long countByEstadoPostulacion(String estadoPostulacion);

    // Listado resumido de postulaciones para el panel de administración,
    // con el título de la oferta y el nombre completo del candidato.
    @Query("SELECT new edu.co.sena.worksite.dtos.PostulacionResumenDto(" +
            "p.idPostulacion, p.oferta.titulo, CONCAT(p.candidato.nombres, ' ', p.candidato.apellidos), " +
            "p.fechaPostulacion, p.estadoPostulacion) " +
            "FROM PostulacionEntity p")
    List<PostulacionResumenDto> listarPostulaciones();
}
