package common.model.domain.datosPersonales;

import java.io.Serializable;

public class Domicilio implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String pais = "Argentina";
	private String pcia = "Buenos Aires";
	private String localidad;
	private String cp;
	private String calle;
	private String numero;
	private Integer piso;
	private String depto;
	
	public Domicilio(){
		setLocalidad("");
		setCp("");
		setCalle("");
		setNumero("");
	}
	public Domicilio (String localidad,String calle, String numero){
		setLocalidad(localidad);
		setCalle(calle);
		setNumero(numero);
		
	}

	public Domicilio (String pais, String pcia,String localidad,String cp,String calle, String numero, Integer piso,String depto){

		setPais(pais);
		setPcia(pcia);
		setLocalidad(localidad);
		setCp(cp);
		setCalle(calle);
		setNumero(numero);
		setPiso(piso);
		setDepto(depto);
	}


	public boolean cpValido(String cp) {
		return true;

	}

	public boolean numeroValido(String num) {
		return true;

	}

	private void setPais(String pais){
		this.pais= pais;

	}
	private void setPcia(String pcia){
			this.pcia= pcia;

	}
	public void setLocalidad(String localidad){
				this.localidad= localidad;

	}
	private void setCp(String cp){
		this.cp= cp;
	}

	public void setCalle(String calle){

		this.calle = calle;
	}

	public void setNumero(String numero){
		this.numero = numero;
	}

	public String getPais(){
		return pais;

	}
	public String getPcia(){
			return pcia;

	}
	public String getLocalidad(){
				return localidad;

	}
	public String getCp(){
		return cp;
	}

	public String getCalle(){
		return calle;
	}

	public String getNumero(){
		return numero;
	}

	public String getCalleConNumero(){
		return calle + " " + numero;
	}
	
	public Integer getPiso() {
		return piso;
	}
	public void setPiso(Integer piso) {
		this.piso = piso;
	}
	public String getDepto() {
		return depto;
	}
	public void setDepto(String depto) {
		this.depto = depto;
	}
	public String getDomicilioCompleto(){
		String salida = "";
		if(getPais()!=null){
			salida = salida + getPais() + ", ";			
		}
		if(getPcia()!=null){
			salida = salida + getPcia() + ", ";
		}
		if(getLocalidad()!=null){
			salida = salida +getLocalidad() + ", ";
		}
		if(getCp()!=null){
			salida = salida +getCp() + ", ";
		}
		if(getCalle()!=null){
			salida = salida +getCalle() + ", ";
		}
		if(getNumero()!=null){
			salida = salida +getNumero();
		}
		//return getPais()+","+getPcia()+","+getLocalidad()+","+getCp()+","+getCalle()+","+getNumero();
		return salida;
	}


}
