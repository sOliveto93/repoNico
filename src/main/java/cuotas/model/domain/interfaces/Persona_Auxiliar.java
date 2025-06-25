package cuotas.model.domain.interfaces;

import java.util.Date;

import common.model.domain.datos.GenericBean;
import common.model.domain.error.ErrorParametro;
import cuotas.model.domain.datos.Persona;

public class Persona_Auxiliar  extends GenericBean<Long>  implements java.io.Serializable {
	private static final long serialVersionUID = 6224478489834044231L;
	
	
	Integer documento;
	String apellido= "";
	String nombre= "";
	
	public Persona_Auxiliar(){	
	}
	
	public Persona_Auxiliar(String nombre, String apellido, Integer documento){
		this.nombre = nombre;
		this.apellido = apellido;
		this.documento = documento;
	}

	public Integer getDocumento() {
		return documento;
	}

	public void setDocumento(Integer documento) {
		this.documento = documento;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
