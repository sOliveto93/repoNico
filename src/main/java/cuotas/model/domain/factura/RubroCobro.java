package cuotas.model.domain.factura;
// default package
// Generated 11/06/2012 12:24:34 by Hibernate Tools 3.2.4.GA

import java.util.HashSet;
import java.util.Set;

import common.model.domain.datos.Estado;
import common.model.domain.datos.GenericBean;


public class RubroCobro extends GenericBean<Integer> implements java.io.Serializable {
	private static final long serialVersionUID = 5610048204094250581L;
	
	private Integer codigo;
	private String descripcion;
	private Set<FacturaItemTipo> facturaItemTipo = new HashSet<FacturaItemTipo>(0);
	private Estado estado;
	public RubroCobro() {
		super();
		this.estado = new Estado(Estado.ACTIVO);
	}

	public RubroCobro(Integer codigo) {
		this.codigo = codigo;
	}

	public RubroCobro(Integer codigo, String descripcion, Set<FacturaItemTipo> cbrTiposes) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.facturaItemTipo = cbrTiposes;
	}

	public Integer getCodigo() {
		return this.codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Set<FacturaItemTipo> getFacturaItemTipo() {
		return this.facturaItemTipo;
	}

	public void setFacturaItemTipo(Set<FacturaItemTipo> facturaItemTipo) {
		this.facturaItemTipo = facturaItemTipo;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

}
