package cuotas.model.domain.datos;

import java.util.HashSet;
import java.util.Set;

import common.model.domain.datos.GenericBean;
import common.model.domain.error.ErrorParametro;

public class TipoCuota  extends GenericBean<Integer>  implements java.io.Serializable {
	private static final long serialVersionUID = 5124478989834044731L;
	
	private String letra;
	private String descripcion;
	private Set<Cuota> cuotas = new HashSet<Cuota>(0);
	
	public TipoCuota(){
		super();
	}
	public TipoCuota(String letra, String desc){
		this.letra = letra;
		this.descripcion = desc;
	}
	
	public String getLetra() {
		return letra;
	}
	public void setLetra(String letra) {
		if(!this.deboEjecutarValidaciones() || letra.length() < 255){
			this.letra = letra;	
		}else{
			throw new ErrorParametro("La letra del tipo cuota es invalida. \\n La letra execedio los 255 caracteres");
		}
		
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		if(!this.deboEjecutarValidaciones() || descripcion.length() < 255){
			this.descripcion = descripcion;
		}else{
			throw new ErrorParametro("La descripcion del tipo cuota es invalida. \\n La descripcion execedio los 255 caracteres");
		}
	}
	public Set<Cuota> getCuotas() {
		return cuotas;
	}
	public void setCuotas(Set<Cuota> cuotas) {
		this.cuotas = cuotas;
	}

	
	
}
