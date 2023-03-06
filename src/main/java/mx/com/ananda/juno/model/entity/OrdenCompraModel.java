package mx.com.ananda.juno.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "orden_compra")
public class OrdenCompraModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_OrdenCompra")
    private Long idOrdenCompra;

    @Column(name = "numero_entrada")
    private Long docEntry;

    @Column(name = "numero_Orden")
    private Long docNum;

    @Column(name = "fecha_Orden")
    private String docDate;

    @Column(name = "hora_Orden")
    private String docTime;

    @Column(name = "total_Orden")
    private Double docTotal;

    @Column(name = "nombre_proveedor")
    private String cardName;

    @Column(name = "codigo_proveedor")
    private String cardCode;

    @JsonIgnoreProperties({"idDetalleOrden","almacen"})
    @OneToMany(mappedBy = "orden")
    private List<DetalleCompraModel> detalleOrden;

    @JsonIgnore
    @OneToOne(mappedBy = "ordenCompra")
    private RegistroTiempoModel registroTiempo;
}
