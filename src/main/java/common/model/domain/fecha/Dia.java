package common.model.domain.fecha;

import java.io.Serializable;


/**
 * Permite representar los dias con un nombre y numero.
 * No representa una fecha, sino un dia de la semana.
 * @author pmaseda
 *
 */
public class Dia implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5352548589271340569L;
	private String nombre;
	private int num;
	
	public Dia(String nombre,int numero){
		this.nombre=nombre;
		this.num =numero;
		
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	/**
	 * Del nombre original, recorta los primeros 3 caracteres
	 * @return
	 */
	public String getNombreReducido(){
		return nombre.substring(0, 3);
		
	}
	

}
