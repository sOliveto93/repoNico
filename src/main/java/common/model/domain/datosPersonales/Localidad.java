package common.model.domain.datosPersonales;

import common.model.domain.datos.GenericBean;

public class Localidad extends GenericBean<Long>{

	private static final long serialVersionUID = -3790118960682012817L;
	private String nombre;
	
	public Localidad(){
		// FIXME - Hardcode feo
		setNombre("0-Sin Especificar");		
	}
	
	public Localidad(String nombre){
			setNombre(nombre);			
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
