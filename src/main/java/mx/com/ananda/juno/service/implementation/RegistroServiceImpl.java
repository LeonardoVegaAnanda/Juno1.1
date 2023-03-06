package mx.com.ananda.juno.service.implementation;

import mx.com.ananda.juno.model.entity.RegistroTiempoModel;
import mx.com.ananda.juno.repository.IRegistroTiempoRepository;
import mx.com.ananda.juno.service.interfaces.IRegistroTiempoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistroServiceImpl implements IRegistroTiempoService {

    @Autowired
    private IRegistroTiempoRepository iRegistroTiempo;


    @Override
    public RegistroTiempoModel saveRegistroTiempo(RegistroTiempoModel registroTiempoModel) {
        return iRegistroTiempo.save(registroTiempoModel);
    }

    @Override
    public RegistroTiempoModel updateRegistroTiempo(RegistroTiempoModel registroTiempoModel) {
        return iRegistroTiempo.save(registroTiempoModel);
    }

    @Override
    public Optional<RegistroTiempoModel> findById(Long id) {
        return iRegistroTiempo.findById(id);
    }

    @Override
    public List<RegistroTiempoModel> findAllRegistros() {
        return iRegistroTiempo.findAll();
    }

    @Override
    public void deleteRegistroById(long idRegistro) {
        iRegistroTiempo.deleteById(idRegistro);
    }
}
