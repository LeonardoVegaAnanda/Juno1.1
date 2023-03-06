package mx.com.ananda.juno.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseOrders {

    private Long docEntry;
    private Long docNum;
    private String docDate;
    private String docTime;
    private double docTotal;
    private String cardCode;
    private String cardName;
    private List<DocumentLines> documentLines;

    public PurchaseOrders() {
    }

    public PurchaseOrders(Long docEntry, Long docNum, String docDate, String docTime, double docTotal, String cardCode, String cardName, List<DocumentLines> documentLines) {
        this.docEntry = docEntry;
        this.docNum = docNum;
        this.docDate = docDate;
        this.docTime = docTime;
        this.docTotal = docTotal;
        this.cardCode = cardCode;
        this.cardName = cardName;
        this.documentLines = documentLines;
    }
}
