package common.model.domain.util;

import common.model.domain.fecha.Fecha;

public class Evento implements java.io.Serializable {

	private static final long serialVersionUID = 2185983190656853657L;

	private Long id;
	private Fecha fecha; 
	private String usuario;
	private String descripcion;

	public Evento() {
		this.fecha = new Fecha();
	}

	public Evento(String usuario, String descripcion) {
		this.fecha = new Fecha();
		System.out.println(this.fecha);
		this.usuario = usuario;
		this.descripcion = descripcion;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Fecha getFecha() {
		return this.fecha;
	}

	public void setFecha(Fecha fecha) {
		this.fecha = fecha;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String operador) {
		this.usuario = operador;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descri) {
		this.descripcion = descri;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Evento))
			return false;
		Evento castOther = (Evento) other;

		return ((this.getFecha() == castOther.getFecha()) || (this.getFecha() != null
				&& castOther.getFecha() != null && this.getFecha().equals(
				castOther.getFecha())))
				&& ((this.getUsuario() == castOther.getUsuario()) || (this
						.getUsuario() != null
						&& castOther.getUsuario() != null && this
						.getUsuario().equals(castOther.getUsuario())))
				&& ((this.getDescripcion() == castOther.getDescripcion()) || (this
						.getDescripcion() != null
						&& castOther.getDescripcion() != null && this.getDescripcion()
						.equals(castOther.getDescripcion())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getFecha() == null ? 0 : this.getFecha().hashCode());
		result = 37 * result
				+ (getUsuario() == null ? 0 : this.getUsuario().hashCode());
		result = 37 * result
				+ (getDescripcion() == null ? 0 : this.getDescripcion().hashCode());
		return result;
	}

}
