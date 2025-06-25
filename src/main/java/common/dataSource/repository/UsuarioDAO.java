package common.dataSource.repository;

import java.util.List;


import common.dataSource.repository.GenericDAO;
import common.model.domain.validacion.UsuarioGenerico;


public interface UsuarioDAO<T extends UsuarioGenerico> extends GenericDAO <T, Long>{

	//public abstract Iterator<Usuario> existenUsuarios(String usu);

	public abstract T existeUsuario(String usu, String pass);

	public abstract T existeUsuario(String usu);
	
	public abstract List<T> buscarPorNombre(String nombre);	
	
	public abstract List<T> listAll(String orderBy, Boolean asc);
	
	public abstract void saveOrUpdate(T obj);
	
	public abstract T cambiarPassword(T usuario, String oldPass, String newPass);

	//public abstract Iterator<UsuarioGenerico> listarUsuarios();

}