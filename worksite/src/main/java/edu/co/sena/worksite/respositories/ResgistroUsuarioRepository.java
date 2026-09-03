package edu.co.sena.worksite.respositories;

import edu.co.sena.worksite.dtos.CandidatoResumenDto;
import edu.co.sena.worksite.entities.ResgistroUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResgistroUsuarioRepository extends
        JpaRepository<ResgistroUsuarioEntity, Long>,
        JpaSpecificationExecutor<ResgistroUsuarioEntity> {

    Optional<ResgistroUsuarioEntity> findById(long id);

    Optional<ResgistroUsuarioEntity> findByCorreoElectronico(String correoElectronico);

    long countByRolNombre_RolNombre(String rolNombre);

    // Listado resumido de candidatos para el panel de administración.
    // Se filtra por rol para no mezclar cuentas de tipo EMPRESA en este listado.
    @Query("SELECT new edu.co.sena.worksite.dtos.CandidatoResumenDto(" +
            "u.id, CONCAT(u.nombres, ' ', u.apellidos), u.correoElectronico, u.cargo) " +
            "FROM ResgistroUsuarioEntity u " +
            "WHERE u.rolNombre.rolNombre = 'CANDIDATO'")
    List<CandidatoResumenDto> listarCandidatos();
}
