package common.model.domain.datos;

import java.io.Serializable;

public class Estado implements Serializable{
	
	private static final long serialVersionUID = -3503636911452739833L;
	
	public static final int ACTIVO=1 ;
	public static final int INACTIVO=0 ;
	public static final int INCIERTO=-1 ;
	
	private static final String ACTIVO_STR="Activo";
	private static final String INACTIVO_STR="Inactivo";
	private static final String INCIERTO_STR="Incierto";
	
	private int valor;

	public Estado(){
		
	}
	
	public Estado(int valor){
		this.setValorNumerico(valor); 
	}
	
	public Estado(String valor){
		this.setValor(valor);
	}
	
	public String getValor() {
		return this.toString();
	}

	public void setValorNumerico(int valor) {
		this.valor = valor;
	}
	
	public Integer getValorNumerico(){
		return this.valor;
	}
	
	public void setValor(String valor){
		
		valor = valor.toLowerCase();
		
		if(valor.equals(ACTIVO_STR.toLowerCase())){			
			this.valor = ACTIVO;
		}else if(valor.equals(INACTIVO_STR.toLowerCase())){
			this.valor = INACTIVO;
		}else{
			this.valor = INCIERTO;
		}
		
	}
	
	public String toString(){
		String res;
		switch (this.valor) {
	        case ACTIVO:  res = ACTIVO_STR;
	                 break;
	        case INACTIVO:  res = INACTIVO_STR;
	                 break;
	        case INCIERTO:  res = INCIERTO_STR;
	                 break;        
	        default: res = INCIERTO_STR;
	                 break;
		}
		return res;
		
	}
	
	public void activar(){
		this.valor = ACTIVO;
	}
	
	public void desactivar(){
		this.valor = INACTIVO;
	}
	
	public Boolean isActivo(){
		return (this.valor == ACTIVO);
	}
	
	public boolean isActivado(){
		return this.isActivo().booleanValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + valor;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estado other = (Estado) obj;
		if (valor != other.valor)
			return false;
		return true;
	}
	
	

}
