package common.model.domain.validacion;

import java.util.HashSet;
import java.util.Set;

import common.model.domain.mail.DireccionMail;

public class UsuarioRol extends UsuarioGenerico {

	private static final long serialVersionUID = -5968435821750278525L;

	private Set<Rol> roles = new HashSet<Rol>(0);
	private GrupoRol grupo;
	
	public UsuarioRol(){
		
	}
	
	public UsuarioRol(String usuario, DireccionMail email){
		super(usuario, email);
	}
	
	public UsuarioRol(String usuario){
		super(usuario);
	}

	/**
	 * Retorna los roles indicados para el usuario, no contempla los propios del grupo al que pertenece.
	 * @return
	 */
	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}
	
	/**
	 * Permite agregar un rol a los permitidos por el usuario.
	 * @param rol
	 */
	public void add(Rol rol){
		this.roles.add(rol);
	}
	
	/**
	 * Permite eliminar un rol de los permitidos por el usuario.
	 * @param rol
	 */
	public void remove(Rol rol){
		this.roles.remove(rol);
	}

	public GrupoRol getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoRol grupo) {
		this.grupo = grupo;
	}
	
	
	/**
	 * Verifica en los roles del usuario y del grupo si existe el recibido como parametro
	 * @param rol
	 * @return
	 */
	public boolean puedePonerseComo(Rol rol){
		return (this.roles.contains(rol) || (this.grupo != null && this.grupo.getRoles().contains(rol)));
	}
	
}
