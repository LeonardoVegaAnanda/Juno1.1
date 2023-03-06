package mx.com.ananda.juno.model.dto;

import lombok.Data;

@Data
public class DocumentLines {

    private String itemCode;
    private String itemDescription;
    private double quantity;
    private double price;
    private String warehouseCode;

    public DocumentLines() {
    }

    public DocumentLines(String itemCode, String itemDescription, double quantity, double price, String warehouseCode) {
        this.itemCode = itemCode;
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.price = price;
        this.warehouseCode = warehouseCode;
    }
}
