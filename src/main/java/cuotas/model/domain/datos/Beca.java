package cuotas.model.domain.datos;

import java.util.HashSet;
import java.util.Set;

import common.model.domain.datos.GenericBean;
import common.model.domain.error.ErrorParametro;

/**
 * @author jforastier
 *
 */
public class Beca  extends GenericBean<Integer>  implements java.io.Serializable {
	private static final long serialVersionUID = 5124478989834044731L;
	
	private String descripcion;
	private Set<PersonasBeca> personasBeca  = new HashSet<PersonasBeca>(0);
	
	public Beca(){
		super();
	}
	public Beca(String desc){
		this.descripcion = desc;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		if(!this.deboEjecutarValidaciones() || descripcion.length() < 255){
			this.descripcion = descripcion;
		}else{
			throw new ErrorParametro("La descripcion del tipo Beca es invalida. \\n La descripcion execedio los 255 caracteres");
		}
	}
	public Set<PersonasBeca> getPersonasBeca() {
		return personasBeca;
	}
	public void setPersonasBeca(Set<PersonasBeca> personaBecas) {
		this.personasBeca = personaBecas;
	}
	
}
