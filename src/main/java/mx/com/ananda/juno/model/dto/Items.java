package mx.com.ananda.juno.model.dto;

import lombok.Data;

@Data
public class Items {
    private String itemCode;
    private String ncmCode;
    private String itemName;

    private String properties4;

    public Items() {
    }

    public Items(String itemCode, String ncmCode, String itemName) {
        this.itemCode = itemCode;
        this.ncmCode = ncmCode;
        this.itemName = itemName;
    }
}
