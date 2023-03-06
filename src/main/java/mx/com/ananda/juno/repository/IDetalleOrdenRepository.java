package mx.com.ananda.juno.repository;

import mx.com.ananda.juno.model.entity.DetalleCompraModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDetalleOrdenRepository extends JpaRepository<DetalleCompraModel,Long> {
}
