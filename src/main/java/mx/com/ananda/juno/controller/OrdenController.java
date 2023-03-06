package mx.com.ananda.juno.controller;

import mx.com.ananda.juno.model.entity.DetalleCompraModel;
import mx.com.ananda.juno.model.entity.ItemModel;
import mx.com.ananda.juno.model.entity.OrdenCompraModel;
import mx.com.ananda.juno.service.interfaces.IDetalleOrdenService;
import mx.com.ananda.juno.service.interfaces.IItemService;
import mx.com.ananda.juno.service.interfaces.IOrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ananda/ordenes")
@CrossOrigin("*")
public class OrdenController {
    @Autowired
    private IOrdenService sOrden;

    @Autowired
    private IItemService sItem;

    @Autowired
    private IDetalleOrdenService sDetalle;

    List<DetalleCompraModel > detallesOrdenes = new ArrayList<DetalleCompraModel>();

    @PreAuthorize("hasRole('COMPRAS') OR hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<OrdenCompraModel>> traerOrdenes(){
       return new ResponseEntity<>(sOrden.findAll(), HttpStatus.OK);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<OrdenCompraModel> traerOrdenById(@PathVariable(value = "id") Long id){
        Optional<OrdenCompraModel> oOrden = sOrden.findById(id);
        OrdenCompraModel ordenCompraModel = oOrden.get();
        return new ResponseEntity<>(ordenCompraModel,HttpStatus.OK);
    }
    @GetMapping("/docEntry/{docEntry}")
    public ResponseEntity<?> traerOrdenByDocEntry(@PathVariable(value = "docEntry") Long docEntry){
        Optional<OrdenCompraModel> oOrden = sOrden.findByDocEntry(docEntry);
        if(!oOrden.get().getDocEntry().equals(0)) {
            OrdenCompraModel ordenCompraModel = oOrden.get();
            return new ResponseEntity<>(ordenCompraModel, HttpStatus.OK);
        }
        else{
            String mensaje = "No existe la orden de compra";
            return new ResponseEntity<>(mensaje,HttpStatus.OK);
        }
    }
    @GetMapping("/asignarOrden/{id}")
    public ResponseEntity<?> asignarOrdenSAP(@PathVariable(value = "id") Long id, @RequestParam Long docEntry){
        Optional<OrdenCompraModel> oOrden = sOrden.asignarOrden(id,docEntry);
        if(!oOrden.get().getDocEntry().equals(0)) {
            OrdenCompraModel ordenCompraModel = oOrden.get();
            return new ResponseEntity<>(ordenCompraModel, HttpStatus.OK);
        }
        else{
            String mensaje = "No existe la orden de compra";
            return new ResponseEntity<>(mensaje,HttpStatus.OK);
        }
    }
    @PreAuthorize("hasRole('COMPRAS') OR hasRole('ADMIN')")
    @GetMapping("/detalles/{id}")
    public ResponseEntity<List<DetalleCompraModel>> traerDetallerByIdOrdenCompra(@PathVariable(value = "id") Long id){
        return new ResponseEntity<>(sDetalle.findDetallesByIDOrdenCompra(id),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('COMPRAS') OR hasRole('ADMIN')")
    @PostMapping("/agregarItemN/{id}")
    public ResponseEntity<?> agregarItemsNuevosOrden (@PathVariable(value = "id") Long idOrdenCompra,@RequestParam String itemCode,@RequestParam double precio, double cantidad){
        ItemModel oItemTraido = null;
        try {
            oItemTraido = sItem.findByItemCode(itemCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(oItemTraido!=null){
            DetalleCompraModel detelleOrden = new DetalleCompraModel();
            Optional<ItemModel> oItem = sItem.findById(oItemTraido.getIdItem());
            ItemModel itemModel = oItem.get();
            Optional<OrdenCompraModel> oOrden = sOrden.findById(idOrdenCompra);
            detelleOrden.setItemCode(itemModel.getItemCode());
            detelleOrden.setItemDescription(itemModel.getItemName());
            detelleOrden.setPrecio(precio);
            detelleOrden.setCantidad(cantidad);
            detelleOrden.setItemModel(itemModel);
            detelleOrden.setResurtido("tYES");
            OrdenCompraModel ordenCompra = oOrden.get();

            String itemCodeBuscar  = oItemTraido.getItemCode();
            boolean itemIngresado = detallesOrdenes.stream().anyMatch(detalle -> detalle.getItemModel().equals(itemCodeBuscar));

            if(!itemIngresado){
                detallesOrdenes.add(detelleOrden);

                for(DetalleCompraModel dt : detallesOrdenes){
                    dt.setOrden(ordenCompra);
                    sDetalle.saveDetalles(dt);
                }
            }
            return new ResponseEntity<>("Se agrego el item",HttpStatus.OK);
        }
        else if(oItemTraido==null){
            ItemModel nuevoItem = sItem.saveItem(oItemTraido);
            DetalleCompraModel detalleOrden = new DetalleCompraModel();
            Optional<ItemModel> oItem = sItem.findById(nuevoItem.getIdItem());
            ItemModel itemModel = oItem.get();
            Optional<OrdenCompraModel> oOrden = sOrden.findById(idOrdenCompra);
            detalleOrden.setItemCode(itemModel.getItemCode());
            detalleOrden.setItemModel(itemModel);
            detalleOrden.setResurtido("tYES");
            OrdenCompraModel ordenCompra = oOrden.get();

            String itemCodeBuscar  = oItemTraido.getItemCode();
            boolean itemIngresado = detallesOrdenes.stream().anyMatch(detalle -> detalle.getItemModel().getItemCode().equals(itemCodeBuscar));

            if(!itemIngresado){
                detallesOrdenes.add(detalleOrden);

                for(DetalleCompraModel dt: detallesOrdenes){
                    dt.setOrden(ordenCompra);
                    sDetalle.saveDetalles(dt);
                }
            }
            return new ResponseEntity<>("Se agrego el item",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Error",HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('COMPRAS') OR hasRole('ADMIN')")
    @PostMapping("/agregarItemR/{id}")
    public ResponseEntity<?> agregarItemsResurtido (@PathVariable(value = "id") Long idOrdenCompra,@RequestParam String itemCode,@RequestParam double precio, @RequestParam double cantidad){
        ItemModel oItemTraido = null;
        try {
            oItemTraido = sItem.findByItemCode(itemCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(oItemTraido!=null){
            DetalleCompraModel detelleOrden = new DetalleCompraModel();
            Optional<ItemModel> oItem = sItem.findById(oItemTraido.getIdItem());
            ItemModel itemModel = oItem.get();
            Optional<OrdenCompraModel> oOrden = sOrden.findById(idOrdenCompra);
            detelleOrden.setItemCode(itemModel.getItemCode());
            detelleOrden.setItemDescription(itemModel.getItemName());
            detelleOrden.setPrecio(precio);
            detelleOrden.setCantidad(cantidad);
            detelleOrden.setItemModel(itemModel);
            detelleOrden.setResurtido("tNO");
            OrdenCompraModel ordenCompra = oOrden.get();

            String itemCodeBuscar  = oItemTraido.getItemCode();
            boolean itemIngresado = detallesOrdenes.stream().anyMatch(detalle -> detalle.getItemModel().equals(itemCodeBuscar));

            if(!itemIngresado){
                detallesOrdenes.add(detelleOrden);

                for(DetalleCompraModel dt : detallesOrdenes){
                    dt.setOrden(ordenCompra);
                    sDetalle.saveDetalles(dt);
                }
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else if(oItemTraido==null){
            ItemModel nuevoItem = sItem.saveItem(oItemTraido);
            DetalleCompraModel detalleOrden = new DetalleCompraModel();
            Optional<ItemModel> oItem = sItem.findById(nuevoItem.getIdItem());
            ItemModel itemModel = oItem.get();
            Optional<OrdenCompraModel> oOrden = sOrden.findById(idOrdenCompra);
            detalleOrden.setItemCode(itemModel.getItemCode());
            detalleOrden.setItemDescription(itemModel.getItemName());
            detalleOrden.setPrecio(precio);
            detalleOrden.setCantidad(cantidad);
            detalleOrden.setItemModel(itemModel);
            detalleOrden.setResurtido("tNO");
            OrdenCompraModel ordenCompra = oOrden.get();

            String itemCodeBuscar  = oItemTraido.getItemCode();
            boolean itemIngresado = detallesOrdenes.stream().anyMatch(detalle -> detalle.getItemModel().getItemCode().equals(itemCodeBuscar));

            if(!itemIngresado){
                detallesOrdenes.add(detalleOrden);

                for(DetalleCompraModel dt: detallesOrdenes){
                    dt.setOrden(ordenCompra);
                    sDetalle.saveDetalles(dt);
                }
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Error",HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('RECIBO') OR hasRole('COMPRAS') OR hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<OrdenCompraModel> guardarOrden(@RequestBody OrdenCompraModel ordenCompraModel){
        return new ResponseEntity<>(sOrden.saveOrden(ordenCompraModel),HttpStatus.OK);
    }
}
