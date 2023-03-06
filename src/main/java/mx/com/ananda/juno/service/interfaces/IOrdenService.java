package mx.com.ananda.juno.service.interfaces;

import mx.com.ananda.juno.model.entity.OrdenCompraModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IOrdenService {

    OrdenCompraModel saveOrden(OrdenCompraModel ordenCompraModel);

    List<OrdenCompraModel> findAll();

    Optional<OrdenCompraModel> findById(Long idOrden);

    Optional<OrdenCompraModel> findByDocEntry(Long numEntrada);

    Optional<OrdenCompraModel> asignarOrden(Long idOrden, Long numEntrada);

    void updateOrden(OrdenCompraModel ordenCompraModel);

}
