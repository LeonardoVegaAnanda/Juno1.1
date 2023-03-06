package mx.com.ananda.juno.service.interfaces;

import mx.com.ananda.juno.model.auth.UsuarioModel;
import mx.com.ananda.juno.model.auth.UsuarioRolModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface IUsuarioService {

    UsuarioModel saveUsuario(UsuarioModel usuarioModel, Set<UsuarioRolModel> usuariosRoles) throws Exception;

    UsuarioModel findUsuarioByUsername(String username);

    List<UsuarioModel> findAllUsuarios();

    void deleteUsuario(Long idUsuario);
}
