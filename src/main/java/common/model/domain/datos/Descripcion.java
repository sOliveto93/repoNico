package common.model.domain.datos;

import java.io.Serializable;

public class Descripcion extends CampoTexto implements Serializable{
	
	private static final long serialVersionUID = -1021586646758172818L;
	private static final String LABEL = "Descripcion"; 
	
	public Descripcion(){
		this.setLabel(LABEL);
		this.setLongitudMaxima(100);
		this.setLongitudMinima(0);		
		
	}
	
	/**
	 * Permite crear una descripcion con un string, por defecto utiliza 100 caracteres como longitud maxima. 
	 * Se verifica una longitud minima de 1 caracter por defecto. Se puede modificar.
	 * @param valor
	 */
	public Descripcion(String valor){
		this.setLabel(LABEL);
		this.setLongitudMinima(1); 
		this.setLongitudMaxima(100);
		this.setValor(valor);
		
	}
	
	/**
	 * Permite crear una descripcion con un limite en la longitud, por defecto utiliza 100 caracteres
	 * @param longMax
	 * @param valor
	 */
	public Descripcion(Integer longMin, Integer longMax,String valor){
		this.setLabel(LABEL);
		this.setLongitudMaxima(longMax);
		this.setValor(valor);		
	}
	
	
}
