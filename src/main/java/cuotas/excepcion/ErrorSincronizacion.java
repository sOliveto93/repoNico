package cuotas.excepcion;


public class ErrorSincronizacion extends Exception {

	String mensage = "";
	public ErrorSincronizacion (String mensaje){
		this.mensage = mensaje;
	}
	public String getMensage() {
		return mensage;
	}
	public void setMensage(String mensage) {
		this.mensage = mensage;
	}
	
}
