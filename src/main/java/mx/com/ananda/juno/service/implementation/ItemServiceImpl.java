package mx.com.ananda.juno.service.implementation;

import lombok.extern.slf4j.Slf4j;
import mx.com.ananda.juno.model.dto.Items;
import mx.com.ananda.juno.model.entity.ItemModel;
import mx.com.ananda.juno.repository.ItemRepository;
import mx.com.ananda.juno.service.interfaces.IItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ItemServiceImpl implements IItemService {

    @Autowired
    private ItemRepository iItem;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${spring.external.service.base-url}")
    private String basePath;

    @Override
    public ItemModel saveItem(ItemModel itemModel) {
        log.info("Item: {}", itemModel);
        itemModel.setProperties4("tYES");
        ItemModel itemSAP = mapearEntidad(restTemplate.postForObject(basePath + "/Items", itemModel, Items.class));

        return iItem.save(itemSAP);
    }

    @Override
    public void updateItemModel(ItemModel itemModel) {
        Items items = mapearItems(itemModel);
        restTemplate.put(basePath+"/Items?itemCode="+itemModel.getItemCode(),items, Items.class);
        iItem.save(itemModel);
    }

    @Override
    public Optional<ItemModel> findById(Long id) {
        return iItem.findById(id);
    }

    @Override
    public ItemModel findByItemCode(String itemCode) {
        ItemModel itemModel = mapearEntidad(restTemplate.getForObject(basePath + "/Item?itemCode=" + itemCode, Items.class));
        if (!itemModel.getItemCode().equals("0")) {
            log.info("Item traido: {}", itemModel);
            Optional<ItemModel> oItem = iItem.findByItemCode(itemModel.getItemCode());

            Optional<ItemModel> itemIngresado = oItem.filter(i -> i.getItemCode().equals(itemCode));
            if (itemIngresado.isEmpty()) {
                iItem.save(itemModel);
                Optional<ItemModel> itemNuevo = iItem.findById(itemModel.getIdItem());
                ItemModel itemRegresado = itemNuevo.get();
                return itemRegresado;
            } else {
                Optional<ItemModel> itemNoGuardado = iItem.findById(oItem.get().getIdItem());
                ItemModel itemRegresado = itemNoGuardado.get();
                return itemRegresado;
            }
        }
        else{
            ItemModel itemNulo = new ItemModel();
            itemNulo.setItemCode("0");
            return itemNulo;
        }
    }




    @Override
    public List<ItemModel> findAllItems() {
        return iItem.findAll();
    }

    private ItemModel mapearEntidad(Items items) {
        ItemModel item = modelMapper.map(items, ItemModel.class);
        return item;
    }

    private Items mapearItems(ItemModel item) {
        Items oItems = modelMapper.map(item, Items.class);
        return oItems;
    }
}
