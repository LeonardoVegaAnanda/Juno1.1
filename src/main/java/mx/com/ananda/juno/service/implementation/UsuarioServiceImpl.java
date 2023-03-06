package mx.com.ananda.juno.service.implementation;

import mx.com.ananda.juno.exception.UsuarioFoundException;
import mx.com.ananda.juno.model.auth.UsuarioModel;
import mx.com.ananda.juno.model.auth.UsuarioRolModel;
import mx.com.ananda.juno.repository.IRolRepository;
import mx.com.ananda.juno.repository.IUsuarioRepository;
import mx.com.ananda.juno.service.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IRolRepository iRol;

    @Autowired
    private IUsuarioRepository iUsuario;

    @Override
    public UsuarioModel saveUsuario(UsuarioModel usuarioModel, Set<UsuarioRolModel> usuariosRoles) throws Exception {
        UsuarioModel usuarioLocal = iUsuario.findByUsername(usuarioModel.getUsername());
        if (usuarioLocal != null) {
            throw new UsuarioFoundException("El usuario ya esta presente");
        } else {
            for (UsuarioRolModel usuarioRol : usuariosRoles) {
                iRol.save(usuarioRol.getRol());
            }
            usuarioModel.getUsuarioRoles().addAll(usuariosRoles);
            usuarioLocal = iUsuario.save(usuarioModel);
        }
        return usuarioLocal;
    }

    @Override
    public UsuarioModel findUsuarioByUsername(String username) {
        return iUsuario.findByUsername(username);
    }

    @Override
    public List<UsuarioModel> findAllUsuarios() {
        return iUsuario.findAll();
    }

    @Override
    public void deleteUsuario(Long idUsuario) {
        iUsuario.deleteById(idUsuario);
    }
}
