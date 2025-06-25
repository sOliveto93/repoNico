package common.model.domain.datosPersonales;

import java.io.Serializable;

public class Cuit implements Serializable {

	
	private static final long serialVersionUID = -3737176025517612465L;


	public Cuit(){
		
	}
	
	
	/**
	 * Valida un cuit de la forma basica, los tres digitos concatenados sin serapadores.
	 * NOTA: traida de la clase Validador , verificar su correcto funcionamiento.
	 */	
	public static boolean cuitValido(String cuit,String numdoc){
		if (cuit.length()!=11){
			return false;
		}
		int[] coeficiente = {5,4,3,2,7,6,5,4,3,2,1};		
		int suma = 0;
		int resultado = 0;
		int digito;
		if(cuit.length()!=11) return false;	
		for (int i=0;i<11;i++){
			digito = ((int)cuit.charAt(i))-48;			
			suma = suma + (digito)*coeficiente[i];
		}		
		resultado = suma % 11;				
		if (resultado==0) return true;
		return false;
	}
}
