package cuotas.model.domain.factura;

import java.util.HashSet;
import java.util.Set;

import common.model.domain.datos.GenericBean;
import common.model.domain.fecha.Fecha;
import cuotas.model.domain.datos.Pago;



public class FacturaItem extends GenericBean<Integer> implements java.io.Serializable {


	private static final long serialVersionUID = -3688065847975976923L;

	private Factura factura;
	private FacturaItemTipo facturaItemTipo;
	private Integer nro;
	private Integer codigo;
	private String descripcion;
	private Integer cantidad;
	private Double precio;
	private Double precioTotal ;
	/*PRUEBA MORA*/
	private Double montoMora;
	private Fecha fechaCuotaMora;
	private Set<Pago> pagos = new HashSet<Pago>(0);
	
	public FacturaItem() {
	}

	public boolean equals(FacturaItem facturaItem){
		boolean respuesta = false;
		if(this.getNro().equals(facturaItem.getNro()) && this.getCodigo().equals(facturaItem.getCodigo()) 
													&& this.facturaItemTipo.getId().equals(facturaItem.getFacturaItemTipo().getId())){
			respuesta = true;
		}
		return respuesta;
	} 	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((facturaItemTipo == null) ? 0 : facturaItemTipo.hashCode());
		result = prime * result + ((nro == null) ? 0 : nro.hashCode());
		return result;
	}

	public FacturaItem(Factura factura, FacturaItemTipo facturaItemTipo, Integer nro, Integer codigo) {
		this.factura = factura;
		this.facturaItemTipo = facturaItemTipo;
		this.nro = nro;
		this.codigo = codigo;
	}

	public FacturaItem(Factura factura, FacturaItemTipo facturaItemTipo, Integer nro, Integer codigo,
			String descri, Integer cantidad, Double precio) {
		this.factura = factura;
		this.facturaItemTipo = facturaItemTipo;
		this.nro = nro;
		this.codigo = codigo;
		this.descripcion = descri;
		this.cantidad = cantidad;
		this.precio = precio;
	}

	public Factura getFactura() {
		return this.factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public FacturaItemTipo getFacturaItemTipo() {
		return this.facturaItemTipo;
	}

	public void setFacturaItemTipo(FacturaItemTipo facturaItemTipo) {
		this.facturaItemTipo = facturaItemTipo;
	}

	public Integer getNro() {
		return this.nro;
	}

	public void setNro(Integer nro) {
		this.nro = nro;
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

	public void setDescripcion(String descri) {		
			this.descripcion = descri;			
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	public void incrementarCantidad(){
		this.cantidad ++;
	}
	
	public void decrementarCantidad(){
		this.cantidad --;
	}

	public Double getPrecio() {
		return this.precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public Double getPrecioTotal() {
		return (precio * cantidad);
	}
	public String toString(){
		String facturaItem = "FacturaItem: Codigo: "+this.getCodigo().toString() +" Cantidad: " + this.cantidad + " - Descripcion "+this.descripcion+ " - ID: "+this.getId() +" - NroFactura: " + this.factura.getNro() +" - MontoMora: "+this.montoMora;
		return facturaItem  ;
	}

	public Double getMontoMora() {
		return montoMora;
	}

	public void setMontoMora(Double montoMora) {
		this.montoMora = montoMora;
	}

	public Fecha getFechaCuotaMora() {
		return fechaCuotaMora;
	}

	public void setFechaCuotaMora(Fecha fechaCuotaMora) {
		this.fechaCuotaMora = fechaCuotaMora;
	}
	public Set<Pago> getPagos() {
		return pagos;
	}

	public void setPagos(Set<Pago> pagos) {
		this.pagos = pagos;
	}
	
	public void addPago(Pago pago){
		if(!this.pagos.contains(pago)){
			this.pagos.add(pago);
		}
	}
	public void removePago(Pago pago){
		if(this.pagos.contains(pago)){
			this.pagos.remove(pago);
		}
	}
}
