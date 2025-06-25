package common.model.domain.validacion;

import common.model.domain.datos.PersonaGenerica;
import common.model.domain.mail.DireccionMail;
import common.model.domain.util.MD5;

public class UsuarioGenerico extends PersonaGenerica {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6497978612808337042L;
	private String usuario;
	private String password;
	private DireccionMail email;
	
	public UsuarioGenerico(){
		
	}
	
	public UsuarioGenerico(String usuario, DireccionMail email){
		this.usuario = usuario ;
		this.email = email;
	}
	
	public UsuarioGenerico(String usuario){
		this(usuario, null) ;		
	}
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPassword() {
		return password;
	}
	
	/**
	 * Setea el password como texto plano, tal cual lo recibe.
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Setea el password como texto plano, tal cual lo recibe, siempre y cuando la confirmacion sea identica al password.
	 * @param password
	 */
	public void setPassword(String password, String passwordConfirmacion) {
		if(password.equals(passwordConfirmacion))
			this.password = password;
	}
	
	/**
	 * REcibe un string con el password y lo guarda con un hash MD5
	 * @param pass
	 */
	public void hashPassword(String pass){
    	MD5 md = new MD5();	
		this.password = md.getTransaccion(pass);    	
    }
	
	public DireccionMail getEmail() {
		return email;
	}
	public void setEmail(DireccionMail email) {
		this.email = email;
	}
	
}
