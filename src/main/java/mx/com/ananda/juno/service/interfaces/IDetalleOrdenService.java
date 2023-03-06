package mx.com.ananda.juno.service.interfaces;

import mx.com.ananda.juno.model.entity.DetalleCompraModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IDetalleOrdenService {

    DetalleCompraModel saveDetalles(DetalleCompraModel detalleCompraModel);

    List<DetalleCompraModel> findDetallesByIDOrdenCompra(Long idOrdenCompra);
}
