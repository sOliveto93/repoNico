package cuotas.model.domain.datos;

import java.util.HashSet;
import java.util.Set;

import common.model.domain.validacion.UsuarioRol;
import cuotas.model.domain.factura.Factura;

public class UsuarioCuotas extends UsuarioRol {

	private static final long serialVersionUID = -5194841039491712130L;

	String usuario;
	String password;
	String tipo;
	String nombre;
	String apellido;
	private Set<Grupo> grupos= new HashSet<Grupo>(0);
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public Set<Grupo> getGrupos() {
		return grupos;
	}
	public void setGrupos(Set<Grupo> grupos) {
		this.grupos = grupos;
	}
	
	public void addGrupo(Grupo grupo){
		this.grupos.add(grupo);
	}
	public void removeGrupo(Grupo grupo){
		this.grupos.remove(grupo);
	}	
	
	public String toString(){
		String respuesta = "NombreCompleto: " + this.getApellido() +","+this.getNombre() + " - Usuario: " + this.usuario + " - TipoUsuario:" + this.tipo + " - ID: " + this.getId(); 
		return respuesta;
	}
	
}
