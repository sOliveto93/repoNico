package cuotas.model.domain.factura;

import java.util.HashSet;
import java.util.Set;

import common.model.domain.datos.GenericBean;

public class TipoFactura extends GenericBean<Integer> {
	private static final long serialVersionUID = 2590572243973270906L;

	private String descripcion;
	private Set<Factura> facturas = new HashSet<Factura>(0);
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Set<Factura> getFacturas() {
		return facturas;
	}
	public void setFacturas(Set<Factura> facturas) {
		this.facturas = facturas;
	}
}
