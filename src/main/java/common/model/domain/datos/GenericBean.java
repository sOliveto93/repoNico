package common.model.domain.datos;

import java.io.Serializable;

import common.model.domain.util.Auditable;


public abstract class GenericBean<ID extends Number> implements Serializable, Auditable {
	
	private static final long serialVersionUID = -7860837837650932630L;

	private ID id ;
	
	private Boolean ejecutarValidaciones  ;

	
	protected GenericBean(){
		ejecutarValidaciones = new Boolean(false);
	}
	
	public ID getId(){
		return id;
	}
	
	public void setId(ID id){
		this.id = id;
	}
	
	public void desactivarValidaciones(){
		this.ejecutarValidaciones = new Boolean(false);
	}
	
	public void activarValidaciones(){
		this.ejecutarValidaciones = new Boolean(true);
	}
	
	public boolean deboEjecutarValidaciones(){
		return this.ejecutarValidaciones;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericBean<ID> other = (GenericBean<ID>) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public String toString() {
		return "ID: "+this.getId();
	} 
	
	/**
	 * Retorna TRUE si el ID es null , FALSE en caso contrario.
	 * @return
	 */
	public Boolean isNuevo(){
		return (id == null);
	}
	
	/**
	 * Por defecto se implementa de la misma manera que toString, redefinir en el caso que corresponda.
	 */
	@Override
	public String getInformacionAuditoria() {
		// TODO Completar informacion de auditoria si hace falta que sea != a toString
		return toString();
	}

}
