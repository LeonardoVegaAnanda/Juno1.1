package mx.com.ananda.juno;

import lombok.extern.slf4j.Slf4j;
import mx.com.ananda.juno.exception.UsuarioFoundException;
import mx.com.ananda.juno.model.auth.RolModel;
import mx.com.ananda.juno.model.auth.UsuarioModel;
import mx.com.ananda.juno.model.auth.UsuarioRolModel;
import mx.com.ananda.juno.service.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@Slf4j
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	private IUsuarioService sUsuario;

	@Autowired
	private BCryptPasswordEncoder bcPassword;
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	try {
		if(sUsuario.findUsuarioByUsername("admin1")!=null){
			UsuarioModel usuarioModel= sUsuario.findUsuarioByUsername("admin1");
			log.info("INICIO DE APLICACION CON USUARIO: {}",usuarioModel.getNombre());
		}
		else{
			UsuarioModel usuario = new UsuarioModel();
			usuario.setNombre("AdministradorTI");
			usuario.setUsername("admin1");
			usuario.setPassword(bcPassword.encode("12345"));
			usuario.setArea("TI");
			RolModel rol = new RolModel();
			rol.setIdRol(1L);
			rol.setNombreRol("ROLE_ADMIN");
			Set<UsuarioRolModel> usuariosRoles = new HashSet<>();
			UsuarioRolModel usuarioRol = new UsuarioRolModel();
			usuarioRol.setRol(rol);
			usuarioRol.setUsuario(usuario);
			usuariosRoles.add(usuarioRol);
			UsuarioModel usuarioGuardado = sUsuario.saveUsuario(usuario, usuariosRoles);
			log.info("USUARIO NUEVO: {}",usuarioGuardado.getNombre());
		}

	}
	catch (UsuarioFoundException exception){
		log.info("USUARIO YA CREADO");
	}
	}
}
