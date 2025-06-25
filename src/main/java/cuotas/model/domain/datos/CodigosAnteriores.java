package cuotas.model.domain.datos;

import common.model.domain.datos.GenericBean;

/**
 * @author jforastier
 *
 */
public class CodigosAnteriores extends GenericBean<Long> {
	private static final long serialVersionUID = -7201588777386488102L;
	public CodigosAnteriores(){
		
	}
	public CodigosAnteriores(Pago pago, String codigoBarra) {
		this.pago = pago;
		this.codigoBarra = codigoBarra;
	}
	
	private Pago pago;
	private String codigoBarra;
	public Pago getPago() {
		return pago;
	}
	public String getCodigoBarra() {
		return codigoBarra;
	}
	public void setPago(Pago pago) {
		this.pago = pago;
	}
	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}
	public String toString(){
		String s = "Codigo de barras: " + this.codigoBarra +" ahora tiene "+ this.pago.getCodigo_barra() + "Pago.id: " + this.pago.getId();
		return s;
		
	}

}
