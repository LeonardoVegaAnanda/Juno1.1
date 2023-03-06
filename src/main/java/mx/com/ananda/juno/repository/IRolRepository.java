package mx.com.ananda.juno.repository;

import mx.com.ananda.juno.model.auth.RolModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRolRepository extends JpaRepository<RolModel,Long> {

    RolModel findByNombreRol(String nombreRol);
}
