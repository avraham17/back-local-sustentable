package edu.co.sena.worksite.respositories;

import edu.co.sena.worksite.entities.ResgistroUsuarioEntity;
import edu.co.sena.worksite.entities.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends
        JpaRepository<RolEntity, Long>,
        JpaSpecificationExecutor<RolEntity> {

    Optional<RolEntity> findByRolNombre(String rolNombre);
}
