package mx.com.ananda.juno.repository;

import mx.com.ananda.juno.model.entity.OrdenCompraModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IOrdenRepository extends JpaRepository<OrdenCompraModel,Long> {

    Optional<OrdenCompraModel> findByDocEntry(Long docEntry);
}
