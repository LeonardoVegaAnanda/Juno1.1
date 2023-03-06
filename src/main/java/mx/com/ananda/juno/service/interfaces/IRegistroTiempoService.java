package mx.com.ananda.juno.service.interfaces;

import mx.com.ananda.juno.model.entity.RegistroTiempoModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IRegistroTiempoService {

    RegistroTiempoModel saveRegistroTiempo(RegistroTiempoModel registroTiempoModel);

    RegistroTiempoModel updateRegistroTiempo(RegistroTiempoModel registroTiempoModel);

    Optional<RegistroTiempoModel> findById(Long id);

    List<RegistroTiempoModel> findAllRegistros();

    void deleteRegistroById(long idRegistro);
}
