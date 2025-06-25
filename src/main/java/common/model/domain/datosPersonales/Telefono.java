package common.model.domain.datosPersonales;

import java.io.Serializable;

public abstract class Telefono implements Serializable {

	private static final long serialVersionUID = -8936333085447803175L;
	private String tipo;
	private String numero;


	protected Telefono (){

			this.tipo = "-";
			this.setNumero("-");
	}


	protected Telefono (String tipo, String numero){

		this.tipo = tipo;
		this.setNumero(numero);
	}

	public void setNumero(String num){
		if (numeroValido(num)){

			this.numero=num;
		}

	}
	protected abstract boolean numeroValido(String num);

	public String getTipo(){
		return tipo;
	}
	
	public void setTipo(String tipo){
		this.tipo=tipo;	
	}

	public String getNumero(){
		return numero;
	}


	public String getTelefono(){

		return getTipo()+" "+getNumero();
	}
	
	public String toString(){
		return getNumero();
	}
}
