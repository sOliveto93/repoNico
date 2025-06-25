package common.model.domain.validacion;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class GrupoRol implements Serializable{

	private static final long serialVersionUID = 3475638222161777099L;
	private Set<Rol> roles = new HashSet<Rol>(0);
	private Set<UsuarioRol> integrantes = new HashSet<UsuarioRol>(0);
	private String nombre; 
	
	public GrupoRol(){
		
	}
	
	public GrupoRol(String nombre){
		this.nombre = nombre ;
	}

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void add(Rol rol){
		this.roles.add(rol);
	}
	
	public void remove(Rol rol){
		this.roles.remove(rol);
	}

	public Set<UsuarioRol> getIntegrantes() {
		return integrantes;
	}

	public void setIntegrantes(Set<UsuarioRol> integrantes) {
		this.integrantes = integrantes;
	}
	
	
	public void add(UsuarioRol usuarioRol){
		this.integrantes.add(usuarioRol);
		usuarioRol.setGrupo(this);
	}
	
	public void remove(UsuarioRol usuarioRol){
		this.integrantes.remove(usuarioRol);
		usuarioRol.setGrupo(null);
	}
	
}
