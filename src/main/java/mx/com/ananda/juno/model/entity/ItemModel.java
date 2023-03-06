package mx.com.ananda.juno.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "item")
@Data
public class ItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Long idItem;

    @Column(name = "itemCode",nullable = false)
    private String itemCode;

    @Column(name = "itemName",nullable = false)
    private String itemName;

    @Column(name = "ncmCode",nullable = false)
    private String ncmCode;

    @Column(name = "productoNuevo",nullable = false)
    private String properties4;
}
