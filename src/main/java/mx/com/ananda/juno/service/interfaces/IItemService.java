package mx.com.ananda.juno.service.interfaces;

import mx.com.ananda.juno.model.entity.ItemModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IItemService {
    ItemModel saveItem(ItemModel itemModel);

    void updateItemModel(ItemModel itemModel);

    Optional<ItemModel> findById(Long id);

    ItemModel findByItemCode(String itemCode) ;

    List<ItemModel> findAllItems();
}
