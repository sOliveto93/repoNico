package cuotas.model.domain.factura;

import java.util.HashSet;
import java.util.Set;

import common.model.domain.datos.GenericBean;


public class SucursalCobro extends GenericBean<Integer> implements java.io.Serializable {

	private static final long serialVersionUID = 1729406439675382642L;

	private Integer codigo;
	private String descri;
	private Integer numero;
	private Set<Factura> factura = new HashSet<Factura>(0);

	public SucursalCobro() {
	}

	public SucursalCobro(Integer codigo) {
		this.codigo = codigo;
	}

	public SucursalCobro(Integer codigo, String descri, Integer numero, Set<Factura> facturas) {
		this.codigo = codigo;
		this.descri = descri;
		this.numero = numero;
		this.factura = facturas;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescri() {
		return this.descri;
	}

	public void setDescri(String descri) {
		this.descri = descri;
	}

	public Integer getNumero() {
		return this.numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Set<Factura> getCbrFc() {
		return this.factura;
	}

	public void setCbrFc(Set<Factura> facturas) {
		this.factura = facturas;
	}

}
