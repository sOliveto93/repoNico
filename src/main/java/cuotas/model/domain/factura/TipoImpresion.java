package cuotas.model.domain.factura;


import common.model.domain.datos.GenericBean;

public class TipoImpresion extends GenericBean<Integer> {
	private static final long serialVersionUID = -6486937374356553152L;
	
	private String descripcion;
	private Boolean predeterminado;
	//private Set<Factura> facturas = new HashSet<Factura>(0);
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Boolean getPredeterminado() {
		return predeterminado;
	}
	public void setPredeterminado(Boolean predeterminado) {
		this.predeterminado = predeterminado;
	}
	public String toString(){
		return "ID: " + this.getId()+ " - Descripcion: " +this.getDescripcion() + " - Predeterminado: " + this.getPredeterminado();
	}
	public void predeterminar(){
		this.predeterminado = true;
	}
	public void desactivar(){
		this.predeterminado = false;
	}
	
}
