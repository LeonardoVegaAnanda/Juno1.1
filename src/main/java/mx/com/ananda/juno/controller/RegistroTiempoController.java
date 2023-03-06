package mx.com.ananda.juno.controller;

import mx.com.ananda.juno.model.entity.OrdenCompraModel;
import mx.com.ananda.juno.model.entity.RegistroTiempoModel;
import mx.com.ananda.juno.service.interfaces.IOrdenService;
import mx.com.ananda.juno.service.interfaces.IRegistroTiempoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("ananda/registros")
@CrossOrigin("*")
public class RegistroTiempoController {

    @Autowired
    private IRegistroTiempoService sRegistroTiempo;

    @Autowired
    private IOrdenService sOrden;

    @GetMapping("")
    public ResponseEntity<List<RegistroTiempoModel>> traerRegistros(){
        return new ResponseEntity<>(sRegistroTiempo.findAllRegistros(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<RegistroTiempoModel> traerRegistroById(@PathVariable(value = "id") Long id){
        Optional<RegistroTiempoModel> oRegistroTiempo = sRegistroTiempo.findById(id);
        RegistroTiempoModel registroTiempoModel = oRegistroTiempo.get();
        return new ResponseEntity<>(registroTiempoModel,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RECIBO') OR hasRole('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<RegistroTiempoModel> crearRegistro(@RequestParam(required = false,defaultValue = "0") Long numeroEntrada){
        RegistroTiempoModel registroTiempoModel = new RegistroTiempoModel();
        if(numeroEntrada !=0){
            Optional<OrdenCompraModel> oOrden = sOrden.findByDocEntry(numeroEntrada);
            OrdenCompraModel ordenCompra = oOrden.get();
            ordenCompra.setDocEntry(numeroEntrada);
            registroTiempoModel.setOrdenCompra(ordenCompra);
            registroTiempoModel.setDateReciboOCLlegada(LocalDate.now());
            registroTiempoModel.setTimeReciboOCLlegada(LocalTime.now());
            registroTiempoModel.setEstatus("Llegada a Recibo, Con Orden");
            return new ResponseEntity<>(sRegistroTiempo.saveRegistroTiempo(registroTiempoModel),HttpStatus.CREATED);
        }
        else{
            OrdenCompraModel nuevaOrden = new OrdenCompraModel();
            nuevaOrden.setDocDate(LocalDate.now().toString());
            nuevaOrden.setDocTime(LocalTime.now().toString());
            OrdenCompraModel ordenSalvada = sOrden.saveOrden(nuevaOrden);
            registroTiempoModel.setOrdenCompra(ordenSalvada);
            registroTiempoModel.setDateReciboOCLlegada(LocalDate.now());
            registroTiempoModel.setTimeReciboOCLlegada(LocalTime.now());
            registroTiempoModel.setEstatus("Llegada a Recibo");
            return new ResponseEntity<>(sRegistroTiempo.saveRegistroTiempo(registroTiempoModel),HttpStatus.CREATED);
        }

    }

    @PreAuthorize("hasRole('RECIBO') OR hasRole('ADMIN')")
    @PutMapping("/compra-m/{id}")
    public ResponseEntity<RegistroTiempoModel> generarTiempoCompraMM(@PathVariable(value = "id") Long id){
        RegistroTiempoModel registroTiempo = new RegistroTiempoModel();
        Optional<RegistroTiempoModel> oRegistroTiempo = sRegistroTiempo.findById(id);
        registroTiempo = oRegistroTiempo.get();
        registroTiempo.setEstatus("Muestra a Compras");
        registroTiempo.setDateReciboMuestraM(LocalDate.now());
        registroTiempo.setTimeReciboMuestraM(LocalTime.now());

        return new ResponseEntity<>(sRegistroTiempo.updateRegistroTiempo(registroTiempo),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('COMPRAS') OR hasRole('ADMIN')")
    @PutMapping("/compra-f/{id}")
    public ResponseEntity<RegistroTiempoModel> generarTiempoCompraMF(@PathVariable(value = "id") Long id) {
        RegistroTiempoModel registroTiempo = new RegistroTiempoModel();
        Optional<RegistroTiempoModel> oRegistroTiempo = sRegistroTiempo.findById(id);
        registroTiempo = oRegistroTiempo.get();
        registroTiempo.setEstatus("Pendiente de Foto");
        registroTiempo.setDateCompraMuestraF(LocalDate.now());
        registroTiempo.setTimeCompraMuestraF(LocalTime.now());

        return new ResponseEntity<>(sRegistroTiempo.updateRegistroTiempo(registroTiempo),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('COMPRAS') OR hasRole('ADMIN')")
    @PutMapping("compra-l/{id}")
    public ResponseEntity<RegistroTiempoModel> generarTiempoCompraMuestraL(@PathVariable(value = "id") Long id) {
        RegistroTiempoModel registroTiempo = new RegistroTiempoModel();
        Optional<RegistroTiempoModel> oRegistroTiempo = sRegistroTiempo.findById(id);
        registroTiempo = oRegistroTiempo.get();
        registroTiempo.setEstatus("Muestra Liberada");
        registroTiempo.setDateCompraMuestraL(LocalDate.now());
        registroTiempo.setTimeCompraMuestraL(LocalTime.now());

        return new ResponseEntity<>(sRegistroTiempo.updateRegistroTiempo(registroTiempo),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('FOTO') OR hasRole('ADMIN')")
    @PutMapping("foto-t/{id}")
    public ResponseEntity<RegistroTiempoModel> generarTiempoFotoT(@PathVariable(value = "id") Long id) {
        RegistroTiempoModel registroTiempo = new RegistroTiempoModel();
        Optional<RegistroTiempoModel> oRegistroTiempo =  sRegistroTiempo.findById(id);
        registroTiempo = oRegistroTiempo.get();
        registroTiempo.setEstatus("Foto Tomada");
        registroTiempo.setDateFoto(LocalDate.now());
        registroTiempo.setTimeFotoT(LocalTime.now());

        return new ResponseEntity<>(sRegistroTiempo.updateRegistroTiempo(registroTiempo),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('COMPRAS') OR hasRole('ADMIN')")
    @PutMapping("recibo-l/{id}")
    public ResponseEntity<RegistroTiempoModel> generarTiempoReciboL(@PathVariable(value = "id") Long id) {
        RegistroTiempoModel registroTiempo = new RegistroTiempoModel();
        Optional<RegistroTiempoModel> oRegistroTiempo = sRegistroTiempo.findById(id);
        registroTiempo = oRegistroTiempo.get();
        registroTiempo.setEstatus("Mercancia Liberada en Recibo");
        registroTiempo.setDateReciboL(LocalDate.now());
        registroTiempo.setTimeReciboL(LocalTime.now());

        return new ResponseEntity<>(sRegistroTiempo.updateRegistroTiempo(registroTiempo),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RECIBO') OR hasRole('ADMIN')")
    @PutMapping("transito/{id}")
    public ResponseEntity<RegistroTiempoModel> generarTiempoTransito(@PathVariable(value = "id") Long id) {
        RegistroTiempoModel registroTiempo = new RegistroTiempoModel();
        Optional<RegistroTiempoModel> oRegistroTiempo = sRegistroTiempo.findById(id);
        registroTiempo = oRegistroTiempo.get();
        registroTiempo.setEstatus("En Transito");
        registroTiempo.setDate_Transito(LocalDate.now());
        registroTiempo.setTimeTransito(LocalTime.now());

        return new ResponseEntity<>(sRegistroTiempo.updateRegistroTiempo(registroTiempo),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ALMACEN') OR hasRole('ADMIN')")
    @PutMapping("almacen/{id}")
    public ResponseEntity<RegistroTiempoModel> generarTiempoAlmacen(@PathVariable(value = "id") Long id) {
        RegistroTiempoModel registroTiempo = new RegistroTiempoModel();
        Optional<RegistroTiempoModel> oRegistroTiempo = sRegistroTiempo.findById(id);
        registroTiempo = oRegistroTiempo.get();
        registroTiempo.setEstatus("En Almacen");
        registroTiempo.setDate_Almacen(LocalDate.now());
        registroTiempo.setTimeAlmacen(LocalTime.now());

        return new ResponseEntity<>(sRegistroTiempo.updateRegistroTiempo(registroTiempo),HttpStatus.OK);
    }
}
