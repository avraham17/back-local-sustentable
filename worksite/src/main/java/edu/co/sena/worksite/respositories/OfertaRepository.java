package edu.co.sena.worksite.respositories;

import edu.co.sena.worksite.dtos.OfertaResumenDto;
import edu.co.sena.worksite.entities.OfertaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfertaRepository extends
        JpaRepository<OfertaEntity, Long>,
        JpaSpecificationExecutor<OfertaEntity> {

    List<OfertaEntity> findByEmpresa_Id(int idEmpresa);

    long countByEstado(String estado);

    // Listado resumido de ofertas para el panel de administración,
    // incluyendo el nombre de la empresa y el conteo de postulaciones.
    @Query("SELECT new edu.co.sena.worksite.dtos.OfertaResumenDto(" +
            "o.id, o.titulo, o.empresa.nombre, o.estado, COUNT(p.idPostulacion)) " +
            "FROM OfertaEntity o " +
            "LEFT JOIN o.postulaciones p " +
            "GROUP BY o.id, o.titulo, o.empresa.nombre, o.estado")
    List<OfertaResumenDto> listarOfertas();
}
