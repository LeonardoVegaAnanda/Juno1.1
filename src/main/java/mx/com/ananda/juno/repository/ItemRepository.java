package mx.com.ananda.juno.repository;

import mx.com.ananda.juno.model.entity.ItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemModel, Long> {

    Optional<ItemModel> findByItemCode(String itemCode);
}
