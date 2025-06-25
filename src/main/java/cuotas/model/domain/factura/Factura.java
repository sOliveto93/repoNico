package cuotas.model.domain.factura;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;




import common.model.domain.datos.Estado;
import common.model.domain.datos.GenericBean;
import common.model.domain.fecha.Fecha;
import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.Pago;
import cuotas.model.domain.datos.Persona;


public class Factura extends GenericBean<Integer> implements java.io.Serializable {
	private static final long serialVersionUID = 6927552749880047592L;
	
	private Persona persona;
	private SucursalCobro sucursalCobro;
	private Integer nro;
	private String descripcionBasica;
	private String descripcionExtendida;
	private Fecha fechaPago;
	private Double monto;
	private Double pagado1;
	private String operador;
	private Fecha fechaCarga;
	private Integer comision;
	private Carrera carrera;
	private Estado estado;
	private TipoPago tipoPago;
	private TipoFactura tipoFactura;
	//private Integer formaPago;
	private FormaPago formaPago;
	//private int idcarrera;
	// agregando JF 15/3/2022
	private String cuit;
	private String razonSocial;
	private String responsableIva;
	
	

	private Set<FacturaItem> facturaItems = new HashSet<FacturaItem>(0);

	public Factura() {
		this.monto = new Double(0); 
		this.estado = new Estado();
		this.estado.activar();
	}

	public TipoPago getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

	public Factura(SucursalCobro sucursalCobro, Carrera carrera) {
		this.sucursalCobro = sucursalCobro;
		this.carrera = carrera;
	}

	public Factura(Persona persona, SucursalCobro sucursalCobro, Integer nro,
			FormaPago fpago, String descri1, String descri2, Fecha fecha,
			Double monto, Double pagado1, String oper, Fecha fecar,
			Integer comision,Carrera carrera, Set<FacturaItem> facturaItems) {
		this.persona = persona;
		this.sucursalCobro = sucursalCobro;
		this.nro = nro;
		this.formaPago = fpago;
		this.descripcionBasica = descri1;
		this.descripcionExtendida = descri2;
		this.fechaPago = fecha;
		this.monto = monto;
		this.pagado1 = pagado1;
		this.operador = oper;
		this.fechaCarga = fecar;
		this.comision = comision;
		this.carrera = carrera;
		this.facturaItems = facturaItems;
	}

	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		if(persona != null)
			this.persona = persona;
	}

	public SucursalCobro getSucursalCobro() {
		return this.sucursalCobro;
	}

	public void setSucursalCobro(SucursalCobro sucursalCobro) {
		this.sucursalCobro = sucursalCobro;
	}

	public Integer getNro() {
		return this.nro;
	}

	public void setNro(Integer nro) {
		this.nro = nro;
	}

	public FormaPago getFormaPago() {
		return this.formaPago;
	}

	public TipoFactura getTipoFactura() {
		return tipoFactura;
	}

	public void setTipoFactura(TipoFactura tipoFactura) {
		this.tipoFactura = tipoFactura;
	}

	public void setFormaPago(FormaPago fpago) {
		this.formaPago = fpago;
	}

	public String getDescripcionBasica() {
		return this.descripcionBasica;
	}

	public void setDescripcionBasica(String descripcionBasica) {
		this.descripcionBasica = descripcionBasica;
	}

	public String getDescripcionExtendida() {
		return this.descripcionExtendida;
	}

	public void setDescripcionExtendida(String descripcionExtendida) {
		this.descripcionExtendida = descripcionExtendida;
	}

	public Fecha getFechaPago() {
		return this.fechaPago;
	}

	public void setFechaPago(Fecha fecha) {
		this.fechaPago = fecha;
	}

	/**
	 * Recalcula el monto total de la factura recorriendo todos los items y sumando sus totales.
	 */
	/*public void recalcularMonto(){
		double total = 0;
		Iterator<FacturaItem> items  = this.getFacturaItems().iterator();
		while(items.hasNext()){
			total += items.next().getPrecioTotal();
		}
		this.monto = total;
	}*/
	
	public Double getMonto() {
		return this.monto;		
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public Double getPagado1() {
		return this.pagado1;
	}

	public void setPagado1(Double pagado1) {
		this.pagado1 = pagado1;
	}

	public String getOperador() {
		return this.operador;
	}

	public void setOperador(String oper) {
		this.operador = oper;
	}

	public Fecha getFechaCarga() {
		return this.fechaCarga;
	}

	public void setFechaCarga(Fecha fechaCarga) {
		this.fechaCarga = fechaCarga;
	}

	public Integer getComision() {
		return this.comision;
	}

	public void setComision(Integer comision) {
		this.comision = comision;
	}

	public Carrera getCarrera() {
		return carrera;
	}

	public void setCarrera(Carrera carrera) {
		this.carrera = carrera;
	}

	public Set<FacturaItem> getFacturaItems() {
		return this.facturaItems;
	}

	public void setFacturaItems(Set<FacturaItem> facturaItems) {
		this.facturaItems = facturaItems;
	}
	public Iterator<FacturaItem> getFacturaItemsIterator(){
		return this.facturaItems.iterator();
	}
	
	public String toString(){
		return getDatosFactura(); 
	}	
	
	private String getDatosFactura(){
		String respuesta="";
		if(getId() !=null)
			respuesta = "ID: "+getId();
		if(getNro()!=null)
			respuesta += " - numeroFactura "+ getNro();
		if(getFechaPago()!=null)
			respuesta +=" - FechaDePago "+ getFechaPago();
		if(getMonto()!=null)
			respuesta +=" - MontoFactura " +getMonto();
		if(getPagado1()!=null)
			respuesta +=" - MontoPagado "+getPagado1();
		if(getPersona()!=null){
			if(getPersona().getNombreCompleto()!=null)
				respuesta +=" - Persona: "+getPersona().getNombreCompleto();
			if(getPersona().getDocumento()!=null)
				respuesta +=" Documento "+getPersona().getDocumento().toString();
		}
		return respuesta;
	}
	public String getInformacionAuditoria() {		
		return toString();
	}

	public void addFacturaItem(FacturaItem facturaItem, Boolean itemMora){
		System.out.println("monto inicial "+this.monto);
		Iterator<FacturaItem> items = this.getFacturaItems().iterator();
		int i = 0;
		if(!facturaItem.getFacturaItemTipo().getRubroCobro().getCodigo().equals(9)){
			
			if(this.getFacturaItems().contains(facturaItem)  ){
				while (items.hasNext()){
					FacturaItem facturaItemAux = items.next();				
					if(facturaItemAux.equals(facturaItem)){
						Double precio = facturaItemAux.getPrecio() + facturaItem.getPrecio();
						Integer CantidadTotal = facturaItemAux.getCantidad() + facturaItem.getCantidad();
						System.out.println("ItemMora: " + itemMora);
						if(itemMora){
							facturaItemAux.setCantidad(1);
							facturaItemAux.setPrecio(precio);
						}else{
							facturaItemAux.setCantidad(CantidadTotal);	
							System.out.println("CantidadTotal: " + CantidadTotal);
						}
						this.facturaItems.add(facturaItemAux);
					}
					System.out.println("Entro while...Item: "+facturaItemAux.getCantidad()+ " precio"+facturaItem.getPrecioTotal());
				}
				
			}else{
				this.facturaItems.add(facturaItem);
			}
		}else{
			while (items.hasNext()){
				items.next();
				facturaItem.setCodigo(facturaItem.getCodigo()-1);	
			}
			this.facturaItems.add(facturaItem);
		}
		this.monto += facturaItem.getPrecioTotal();
	}
	
	public void removeFacturaItem(FacturaItem facturaItem){
		this.facturaItems.remove(facturaItem);
		this.monto -= facturaItem.getPrecioTotal();
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	// agregando JF 15/3/2022
	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getResponsableIva() {
		return responsableIva;
	}

	public void setResponsableIva(String responsableIva) {
		this.responsableIva = responsableIva;
	}
}
