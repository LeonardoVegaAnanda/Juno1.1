package mx.com.ananda.juno.repository;

import mx.com.ananda.juno.model.auth.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepository extends JpaRepository<UsuarioModel,Long> {

    UsuarioModel findByUsername(String username);
}
