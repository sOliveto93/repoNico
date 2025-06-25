package cuotas.model.domain.datos;

import java.io.Serializable;

public class PagoSumarizado implements Serializable {

	private static final long serialVersionUID = -8063001432411780620L;
	
	private Persona persona;
	private Grupo grupoPago;
	private Double total ;
	
	public PagoSumarizado(){}
	
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	
	public Grupo getGrupoPago() {
		return grupoPago;
	}

	public void setGrupoPago(Grupo grupo) {
		this.grupoPago = grupo;
	}
	
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}

}
