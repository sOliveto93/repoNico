package cuotas.model.domain.factura;

import java.util.HashSet;
import java.util.Set;

import common.model.domain.datos.GenericBean;

public class TipoPago extends GenericBean<Integer> {
	private static final long serialVersionUID = -6486937374356553152L;
	
	private String descripcion;
	private String pesoOrdenamiento;
	private Set<Factura> facturas = new HashSet<Factura>(0);
	
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getPesoOrdenamiento() {
		return pesoOrdenamiento;
	}
	public void setPesoOrdenamiento(String pesoOrdenamiento) {
		this.pesoOrdenamiento = pesoOrdenamiento;
	}
	public Set<Factura> getFacturas() {
		return facturas;
	}
	public void setFacturas(Set<Factura> facturas) {
		this.facturas = facturas;
	}
	

	
}
