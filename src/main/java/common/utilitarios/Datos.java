package common.utilitarios;


public class Datos {
	private String nombre;
	private String ip;
	private String puerto;
	private String usuario;
	private String password;
	private String base;
	private String tipo;
	
	public Datos(String nombre, String ip,String puerto, String usuario, String password, String base, String tipo){
		this.nombre = nombre;
		this.ip = ip;
		this.puerto = puerto;
		this.usuario = usuario;
		this.password = password;
		this.base = base;
		this.tipo = tipo;
	}
	public String toString (){
		return "Nombre: "+nombre+" - Ip: "+ip+" - puerto: "+puerto+" - usuario: "+usuario+" - password: "+password+" - base: "+base+" - tipo: "+tipo;
	}
	public Datos(){
		
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
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
	public void setPassword(String password) {
		this.password = password;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getPuerto() {
		return puerto;
	}

	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}
	
}
