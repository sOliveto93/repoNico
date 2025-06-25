package cuotas.model.domain.datos;

import java.util.HashSet;
import java.util.Set;

import common.model.domain.datos.GenericBean;
import common.model.domain.error.ErrorParametro;

public class OrigenPago  extends GenericBean<Integer>  implements java.io.Serializable {
	private static final long serialVersionUID = 5124478989834044731L;
	
	private String descripcion;
	private Set<Pago> pagos = new HashSet<Pago>(0);
	
	public OrigenPago(){
		super();
	}
	public OrigenPago(String desc){
		this.descripcion = desc;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		if(!this.deboEjecutarValidaciones() || descripcion.length() < 255){
			this.descripcion = descripcion;
		}else{
			throw new ErrorParametro("La descripcion del Origen de Pago es invalida. \\n La descripcion execedio los 255 caracteres");
		}
	}
	public Set<Pago> getPagos() {
		return pagos;
	}

	public void setPagos(Set<Pago> pagos) {
		this.pagos = pagos;
	}

	
	
}
