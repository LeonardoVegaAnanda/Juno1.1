package mx.com.ananda.juno.service.implementation;

import mx.com.ananda.juno.model.entity.DetalleCompraModel;
import mx.com.ananda.juno.model.entity.OrdenCompraModel;
import mx.com.ananda.juno.repository.IDetalleOrdenRepository;
import mx.com.ananda.juno.repository.IOrdenRepository;
import mx.com.ananda.juno.service.interfaces.IDetalleOrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleOrdenServiceImpl implements IDetalleOrdenService {

    @Autowired
    private IDetalleOrdenRepository iDetalleOrden;

    @Autowired
    private IOrdenRepository iOrdenCompra;

    @Override
    public DetalleCompraModel saveDetalles(DetalleCompraModel detalleCompraModel) {
        return iDetalleOrden.save(detalleCompraModel);
    }

    @Override
    public List<DetalleCompraModel> findDetallesByIDOrdenCompra(Long idOrdenCompra) {
        Optional<OrdenCompraModel> oOrden = iOrdenCompra.findById(idOrdenCompra);
        OrdenCompraModel ordenCompra = oOrden.get();
        return ordenCompra.getDetalleOrden();
    }
}
