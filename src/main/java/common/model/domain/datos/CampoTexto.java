package common.model.domain.datos;

import common.model.domain.error.ErrorParametro;

public class CampoTexto implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3891757302750726532L;
	private String valor;
	private int longitudMaxima;
	private int longitudMinima;
	private String label ;

	public CampoTexto() {
		super();
	}
	
	public CampoTexto(String label, Integer longMin, Integer longMax,String valor){
		this.setLabel(label);
		this.setLongitudMaxima(longMax);
		this.setValor(valor);		
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		
		/** TODO realizar validacion o limpieza de caracteres en descripcion **/
		if( (valor != null) && (valor.length() > getLongitudMaxima() || valor.length() < getLongitudMinima()) ){
			
			String error = this.label + " no puede superar los "+getLongitudMaxima()+ " caracteres ";
			
			if(this.longitudMinima > 0){
				error = error.concat(" y debe tener mas de "+ this.getLongitudMinima() + " caracter/es") ;
			}
			throw new ErrorParametro("Valor: "+ valor + " - " + error);
		}
		
		this.valor = valor;
	}

	public String toString() {
		return this.valor;
	}

	public int getLongitudMaxima() {
		return longitudMaxima;
	}

	public void setLongitudMaxima(int longitudMaxima) {
		if(longitudMaxima >= this.getLongitudMinima())
			this.longitudMaxima = longitudMaxima;
	}

	public int getLongitudMinima() {
		return longitudMinima;
	}

	public void setLongitudMinima(int longitudMinima) {
		if(longitudMinima >= 0)
			this.longitudMinima = longitudMinima;
	}

	public String getValue() {
		return this.valor;
	}

	public void setValue(String value) {
		this.valor = value ;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}