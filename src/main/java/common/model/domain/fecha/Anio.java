package common.model.domain.fecha;

import java.util.ArrayList;
import java.util.List;

import common.model.domain.error.ErrorParametro;

public class Anio {

	private Integer anio;
	public Anio(){
		
	}
	
	public Anio(String anio){
		if (anio.length()==4){
			this.anio = Integer.parseInt(anio);
		}else{
			ErrorParametro e = new ErrorParametro();
			e.setMSG("Existe un error en el anio ingresado.");
			throw (e);
		}
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	
	public String toString(){
		return anio.toString();
	}
	
	public static List<Integer> listAnios(Integer anioInicio, Integer anioFin){
		List<Integer> anios = new ArrayList<Integer>();
		while (anioInicio <= anioFin){
			anios.add(anioInicio);
			anioInicio ++;
		}
		return anios;
	}
}
