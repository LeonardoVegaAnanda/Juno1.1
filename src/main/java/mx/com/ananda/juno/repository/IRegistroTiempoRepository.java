package mx.com.ananda.juno.repository;

import mx.com.ananda.juno.model.entity.RegistroTiempoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRegistroTiempoRepository extends JpaRepository<RegistroTiempoModel,Long> {
}
