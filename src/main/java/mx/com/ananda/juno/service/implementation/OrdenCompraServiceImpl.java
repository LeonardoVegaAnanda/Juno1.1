package mx.com.ananda.juno.service.implementation;

import lombok.extern.slf4j.Slf4j;
import mx.com.ananda.juno.model.dto.DocumentLines;
import mx.com.ananda.juno.model.dto.Items;
import mx.com.ananda.juno.model.dto.PurchaseOrders;
import mx.com.ananda.juno.model.entity.DetalleCompraModel;
import mx.com.ananda.juno.model.entity.ItemModel;
import mx.com.ananda.juno.model.entity.OrdenCompraModel;
import mx.com.ananda.juno.repository.IDetalleOrdenRepository;
import mx.com.ananda.juno.repository.IOrdenRepository;
import mx.com.ananda.juno.repository.ItemRepository;
import mx.com.ananda.juno.service.interfaces.IItemService;
import mx.com.ananda.juno.service.interfaces.IOrdenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrdenCompraServiceImpl implements IOrdenService {

    @Autowired
    private IOrdenRepository iOrden;

    @Autowired
    private ItemRepository iItem;

    @Autowired
    private IDetalleOrdenRepository iDetalle;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IItemService sItem;

    @Value("${spring.external.service.base-url}")
    private String basePath;

    @Override
    public OrdenCompraModel saveOrden(OrdenCompraModel ordenCompraModel) {
        return iOrden.save(ordenCompraModel);
    }

    @Override
    public List<OrdenCompraModel> findAll() {
        return iOrden.findAll();
    }

    @Override
    public Optional<OrdenCompraModel> findById(Long idOrden) {
        return iOrden.findById(idOrden);
    }

    @Override
    public Optional<OrdenCompraModel> findByDocEntry(Long numEntrada) {
        OrdenCompraModel ordenCompra = mapearEntidad(restTemplate.getForObject(basePath + "/Order?docEntry=" + numEntrada, PurchaseOrders.class));
        Optional<OrdenCompraModel> oOrden = null;
        if (!ordenCompra.getDocEntry().equals(0)) {
            oOrden = iOrden.findByDocEntry(ordenCompra.getDocEntry());

            if (oOrden.isEmpty()) {
                PurchaseOrders po = restTemplate.getForObject(basePath + "/Order?docEntry=" + numEntrada, PurchaseOrders.class);
                List<DetalleCompraModel> listaDetalles = new ArrayList<>();
                for (var detalle : po.getDocumentLines()) {

                    listaDetalles.add(mapearEntidadDetalles(detalle));
                    for (DetalleCompraModel dt : listaDetalles) {

                        String itemCode = dt.getItemCode();
                        Optional<ItemModel> itemTraido = iItem.findByItemCode(itemCode);
                        if (itemTraido.isPresent()) {
                            dt.setItemModel(itemTraido.get());
                            dt.setOrden(ordenCompra);
                            iDetalle.save(dt);

                        } else {
                            ItemModel itemNuevo = null;
                            try {
                                itemNuevo = sItem.findByItemCode(itemCode);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            dt.setItemModel(itemNuevo);
                            dt.setOrden(ordenCompra);
                            iDetalle.save(dt);
                        }
                    }
                }
                iOrden.save(ordenCompra);
                Optional<OrdenCompraModel> ordenNueva = iOrden.findById(ordenCompra.getIdOrdenCompra());
                return ordenNueva;
            } else {
                Optional<OrdenCompraModel> ordenGuardada = iOrden.findById(oOrden.get().getIdOrdenCompra());
                return ordenGuardada;
            }
        } else {
            Optional<OrdenCompraModel> ordenNula = oOrden;
            ordenNula.get().setDocEntry(0L);
            return ordenNula;
        }
    }

    @Override
    public Optional<OrdenCompraModel> asignarOrden(Long idOrden, Long numEntrada) {
        OrdenCompraModel ordenCompra = mapearEntidad(restTemplate.getForObject(basePath + "/Order?docEntry=" + numEntrada, PurchaseOrders.class));
        Optional<OrdenCompraModel> oOrden = iOrden.findById(idOrden);
        if (!ordenCompra.getDocEntry().equals(0)) {
            oOrden = iOrden.findById(idOrden);
            OrdenCompraModel ordenActualizada = oOrden.get();
            if (oOrden.isPresent()) {
                PurchaseOrders po = restTemplate.getForObject(basePath + "/Order?docEntry=" + numEntrada, PurchaseOrders.class);
                List<DetalleCompraModel> listaDetalles = new ArrayList<>();
                for (var detalle : po.getDocumentLines()) {

                    listaDetalles.add(mapearEntidadDetalles(detalle));
                    for (DetalleCompraModel dt : listaDetalles) {

                        String itemCode = dt.getItemCode();
                        Optional<ItemModel> itemTraido = iItem.findByItemCode(itemCode);
                        for (var detalleB : listaDetalles) {
                            if (!detalleB.getItemCode().equals(itemCode)) {
                                if (itemTraido.isPresent()) {
                                    dt.setItemModel(itemTraido.get());
                                    dt.setOrden(ordenActualizada);
                                    iDetalle.save(dt);
                                }
                            }
                            else {
                                ItemModel itemNuevo = null;
                                try {
                                    itemNuevo = sItem.findByItemCode(itemCode);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                dt.setItemModel(itemNuevo);
                                dt.setOrden(ordenActualizada);
                                iDetalle.save(dt);
                            }
                        }
                    }
                }

                ordenActualizada.setDocTotal(ordenCompra.getDocTotal());
                ordenActualizada.setDocNum(ordenCompra.getDocNum());
                ordenActualizada.setDocEntry(ordenCompra.getDocEntry());
                ordenActualizada.setDocTime(ordenCompra.getDocTime());
                ordenActualizada.setCardName(ordenCompra.getCardName());
                ordenActualizada.setCardCode(ordenCompra.getCardCode());
                ordenActualizada.setDocDate(ordenCompra.getDocDate());
                ordenActualizada.setDetalleOrden(listaDetalles);
                iOrden.save(ordenActualizada);
                Optional<OrdenCompraModel> ordenDevuelta = iOrden.findById(ordenActualizada.getIdOrdenCompra());
                return ordenDevuelta;
            } else {
                return null;
            }
        } else {
            Optional<OrdenCompraModel> ordenNula = oOrden;
            ordenNula.get().setDocEntry(0L);
            return ordenNula;
        }
    }


    @Override
    public void updateOrden(OrdenCompraModel ordenCompraModel) {
        iOrden.save(ordenCompraModel);
    }

    private OrdenCompraModel mapearEntidad(PurchaseOrders po) {
        OrdenCompraModel ordenCompra = modelMapper.map(po, OrdenCompraModel.class);
        return ordenCompra;
    }

    private DetalleCompraModel mapearEntidadDetalles(DocumentLines documentLines) {
        DetalleCompraModel detalleOrden = new DetalleCompraModel();
        detalleOrden.setPrecio(documentLines.getPrice());
        detalleOrden.setCantidad(documentLines.getQuantity());
        detalleOrden.setItemDescription(documentLines.getItemDescription());
        detalleOrden.setItemCode(documentLines.getItemCode());
        Items items = restTemplate.getForObject(basePath + "/item?itemcode=" + detalleOrden.getItemCode(), Items.class);
        detalleOrden.setResurtido(items.getProperties4());
        return detalleOrden;
    }
}
