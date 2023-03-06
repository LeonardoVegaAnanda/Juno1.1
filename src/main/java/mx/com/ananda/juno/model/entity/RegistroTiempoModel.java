package mx.com.ananda.juno.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "registro_tiempo")
@Data
public class RegistroTiempoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro",nullable = false)
    private Long idRegistro;

    @Column(name = "date_ReciboOC_Llegada",nullable = true)
    private LocalDate dateReciboOCLlegada;

    @Column(name = "date_Recibo_MuestraM",nullable = true)
    private LocalDate dateReciboMuestraM;

    @Column(name = "date_Compra_MuestraF",nullable = true)
    private LocalDate dateCompraMuestraF;

    @Column(name = "date_Compra_MuestraL",nullable = true)
    private LocalDate dateCompraMuestraL;

    @Column(name = "date_FotoT",nullable = true)
    private LocalDate dateFoto;

    @Column(name = "date_ReciboL",nullable = true)
    private LocalDate dateReciboL;

    @Column(name = "date_Transito",nullable = true)
    private LocalDate date_Transito;

    @Column(name = "date_Almacen",nullable = true)
    private LocalDate date_Almacen;

    @Column(name = "time_ReciboOC_Llegada",nullable = true)
    private LocalTime timeReciboOCLlegada;

    @Column(name = "time_Recibo_MuestraM",nullable = true)
    private LocalTime timeReciboMuestraM;

    @Column(name = "time_Compra_MuestraF",nullable = true)
    private LocalTime timeCompraMuestraF;

    @Column(name = "time_Compra_MuestraL",nullable = true)
    private LocalTime timeCompraMuestraL;

    @Column(name = "time_FotoT",nullable = true)
    private LocalTime timeFotoT;

    @Column(name = "time_ReciboL",nullable = true)
    private LocalTime timeReciboL;

    @Column(name = "time_Transito",nullable = true)
    private LocalTime timeTransito;

    @Column(name = "time_Almacen",nullable = true)
    private LocalTime timeAlmacen;

    @Column(name = "estatus",nullable = false)
    private String estatus;

    @JsonIgnoreProperties({"docEntry","docDate","docTime","docTotal","cardName"})
    @OneToOne
    private OrdenCompraModel ordenCompra;
}
