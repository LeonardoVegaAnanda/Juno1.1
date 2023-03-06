package mx.com.ananda.juno.controller;

import lombok.extern.slf4j.Slf4j;
import mx.com.ananda.juno.model.entity.ItemModel;
import mx.com.ananda.juno.service.interfaces.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ananda/items")
@CrossOrigin("*")
@Slf4j
public class ItemController {

    @Autowired
    private IItemService sItem;


    @GetMapping("")
    public ResponseEntity<List<ItemModel>> traerItems(){
        return new ResponseEntity<>(sItem.findAllItems(), HttpStatus.OK);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<ItemModel> traerItemById(@PathVariable(value = "id") Long id){
        Optional<ItemModel> oItem = sItem.findById(id);
        ItemModel itemModel = oItem.get();
        return new ResponseEntity<>(itemModel,HttpStatus.OK);
    }
    @GetMapping("/itemCode/{itemCode}")
    public ResponseEntity<?> traerItemByItemCode(@PathVariable(value = "itemCode") String itemCode) throws Exception {
        ItemModel item = sItem.findByItemCode(itemCode);
        log.info("Items: {}",item);
        if(item.getItemCode() != "0") {
            return new ResponseEntity<>(item, HttpStatus.OK);
        }
        else{
            String mensaje = "No existe ese articulo";
            return new ResponseEntity<>(mensaje,HttpStatus.OK);
        }
    }

    @PreAuthorize("hasRole('RECIBO') OR hasRole('COMPRAS') OR hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<ItemModel> guardarItem(@RequestBody ItemModel itemModel){
        return new ResponseEntity<>(sItem.saveItem(itemModel),HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('COMPRAS') OR hasRole('ADMIN')")
    @PutMapping("")
    public ResponseEntity<String> actualizarItem(@RequestBody ItemModel itemModel){
        log.info("Item: {}",itemModel);
        sItem.updateItemModel(itemModel);
        return new ResponseEntity<>("Actualizacion con exito",HttpStatus.NO_CONTENT);
    }

}
