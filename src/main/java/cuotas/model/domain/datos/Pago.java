package cuotas.model.domain.datos;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import common.model.domain.datos.Estado;
import common.model.domain.datos.GenericBean;
import common.model.domain.fecha.Fecha;
import cuotas.model.domain.factura.FacturaItem;


/**
 * @author tiana
 *
 */
/**
 * @author Pyro
 *
 */
public class Pago extends GenericBean<Long>  implements java.io.Serializable {
	private static final long serialVersionUID = -5396114564014523842L;
	
	private Grupo grupo;
	private Persona persona;
	private Integer ano;
	private String observaciones;
	private Fecha fecha;
	private Double monto1;
	private Double monto2;
	private Double vtoMonto;
	private Fecha fechaPgo;
	private String oper;
	private Double monPgo;
	private Integer numero;
	private String codigo_usuario;
	private String tipo;
	private String codigo_barra;
	private Fecha fePgoCarga;
	private boolean dadodebaja = false;
	private Estado estado;
	private OrigenPago origenPago;
	private boolean exceptuarMora;
	private Double montoMora;
	private boolean procesado;
	private Set<CodigosAnteriores> codigosAnteriores = new HashSet<CodigosAnteriores>(0);
	private FacturaItem facturaItem;
	private String estadoTodoPago;
	private String codigoTodoPago;
	private Fecha fechaCreacion;
	private String usuario;
	
	public Pago() {	
	}
	
	
	
	public boolean equals(Pago pago){
		boolean respuesta = false;

		if(this.getGrupo().getId().equals(pago.getGrupo().getId()) && this.ano.toString().equals(pago.getAno().toString())
				&& this.getFecha().getFecha().equals(pago.getFecha().getFecha())
				&& this.persona.equals(pago.getPersona())
				&& this.tipo.equals(pago.getTipo())){
			respuesta=true;
		}
		
		return respuesta;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ano == null) ? 0 : ano.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((grupo == null) ? 0 : grupo.hashCode());
		result = prime * result + ((monto1 == null) ? 0 : monto1.hashCode());
		result = prime * result + ((monto2 == null) ? 0 : monto2.hashCode());
		result = prime * result + ((persona == null) ? 0 : persona.hashCode());
		return result;
	}

	public String toString(){
		//pago.isDadodebaja() == false && pago.getFechaPgo() == null && pago.getMonPgo() 
		String pagoString = "isDadodebaja: " + this.isDadodebaja() + " - FechaCuota: "+ this.getFecha();
		  
		if(this.getFechaPgo()!=null)
			pagoString = pagoString  + " - FechaPgo: " + this.getFechaPgo();
		if(this.getMonPgo()!=null)
			pagoString = pagoString + " - MonPgo: " +this.getMonPgo();
		if(this.getFechaPgo()!=null)	
			pagoString = pagoString + " FechaPago: "+ this.getFechaPgo().getFecha();
		if(this.persona!=null){
			pagoString = "IdPersona: " + this.getPersona().getId() + " "+ pagoString;
		}
		return pagoString;
	}
	public Pago(Grupo grupo, Persona persona, Integer ano,
			String obs, Fecha fecha, Double monto1, Double monto2, Double vtomonto,
			Fecha fechaPgo, String oper, Double monPgo, Fecha fePgoCarga, String codigo_barra, String tipo, Fecha fechaCreacion, String usuario) { //,FacturaItem facturaItem
		this.grupo = grupo;
		this.persona = persona;
		this.ano = ano;
		this.observaciones = obs;
		this.fecha = fecha;
		this.monto1 = monto1;
		this.monto2 = monto2;
		this.vtoMonto = vtomonto;
		this.fechaPgo = fechaPgo;
		this.oper = oper;
		this.monPgo = monPgo;
		this.fePgoCarga = fePgoCarga;
		this.codigo_barra = codigo_barra;
		this.tipo = tipo;
		//this.usuario = usuario;
		//this.facturaItem = facturaItem;
	}

	public Grupo getGrupo() {
		return this.grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Integer getAno() {
		return this.ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String obs) {
		this.observaciones = obs;
		/*if(obs.length()< 255 && this.deboEjecutarValidaciones()){
			this.observaciones = obs;
		}else{
			throw new ErrorParametro("La observacion es invalida. \\n La observacion execedio los 255 caracteres");
		}*/
	}

	public Fecha getFecha() {
		return this.fecha;
	}

	public void setFecha(Fecha fecha) {
		this.fecha = fecha;
	}

	public Double getMonto1() {
		return this.monto1;
	}

	public void setMonto1(Double monto1) {
		this.monto1 = monto1;
	}

	public Double getMonto2() {
		return this.monto2;
	}

	public void setMonto2(Double monto2) {
		this.monto2 = monto2;
	}

	public Fecha getFechaPgo() {
		return this.fechaPgo;
	}

	public void setFechaPgo(Fecha fechaPgo) {
		this.fechaPgo = fechaPgo;
	}

	public String getOper() {
		return this.oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	public Double getMonPgo() {
		return this.monPgo;
	}

	public void setMonPgo(Double monPgo) {
		this.monPgo = monPgo;
	}

	public Fecha getFePgoCarga() {
		return this.fePgoCarga;
	}

	public void setFePgoCarga(Fecha fePgoCarga) {
		this.fePgoCarga = fePgoCarga;
	}

	public Double getVtoMonto() {
		return vtoMonto;
	}

	public void setVtoMonto(Double vtomonto) {
		this.vtoMonto = vtomonto;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public boolean isDadodebaja() {
		return dadodebaja;
	}

	public void setDadodebaja(boolean dadodebaja) {
		this.dadodebaja = dadodebaja;
	}

	public String getCodigo_barra() {
		return codigo_barra;
	}

	public void setCodigo_barra(String codigo_barra) {
		this.codigo_barra = codigo_barra;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Estado getEstado() {
		return estado;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCodigo_usuario() {
		return codigo_usuario;
	}

	public void setCodigo_usuario(String codigo_usuario) {
		this.codigo_usuario = codigo_usuario;
	}

	public OrigenPago getOrigenPago() {
		return origenPago;
	}

	public void setOrigenPago(OrigenPago origenPago) {
		this.origenPago = origenPago;
	}

	public double calcularMora(int dias, int mes, int anio, double total){
		Fecha hoy = new Fecha(new Date(System.currentTimeMillis()));
        while (dias > 30){
        	dias = dias - 30;
               mes = mes + 1;
        }
        int meses = (hoy.getAnio() - anio) * 12 + hoy.getMes() - mes;
        if(hoy.getDia()>dias){
        	meses = meses +1;
        }
        if(meses<0) return 0;
        return total * 6.41 /100 * meses; 
        
	}

	public Double getMontoMora() {
		return montoMora;
	}

	public void setMontoMora(Double montoMora) {
		this.montoMora = montoMora;
	}
	public void anularPago(){
		this.fechaPgo = null;
		this.fePgoCarga = null;
		this.oper = null;
		this.monPgo = 0.0;
	}

	public boolean isExceptuarMora() {
		return exceptuarMora;
	}

	public void setExceptuarMora(boolean exceptuarMora) {
		this.exceptuarMora = exceptuarMora;
	}
	
	public FacturaItem getFacturaItem() {
		return facturaItem;
	}

	public void setFacturaItem(FacturaItem facturaItem) {
		this.facturaItem = facturaItem;
	}
	
	public void addCodigoAnterior(CodigosAnteriores codigoAnterior){
		if(!this.codigosAnteriores.contains(codigoAnterior)){
			this.codigosAnteriores.add(codigoAnterior);
		}
	}
	public void removeCodigoAnterior(CodigosAnteriores codigoAnterior){
		if(this.codigosAnteriores.contains(codigoAnterior)){
			this.codigosAnteriores.remove(codigoAnterior);
		}
	}
	public Set<CodigosAnteriores> getCodigosAnteriores() {
		return codigosAnteriores;
	}

	public void setCodigosAnteriores(Set<CodigosAnteriores> codigosAnteriores) {
		this.codigosAnteriores = codigosAnteriores;
	}

	public boolean isProcesado() {
		return procesado;
	}

	public void setProcesado(boolean procesado) {
		this.procesado = procesado;
	}

	public String getEstadoTodoPago() {
		return estadoTodoPago;
	}

	public void setEstadoTodoPago(String estadoTodoPago) {
		this.estadoTodoPago = estadoTodoPago;
	}

	public String getCodigoTodoPago() {
		return codigoTodoPago;
	}

	public void setCodigoTodoPago(String codigoTodoPago) {
		this.codigoTodoPago = codigoTodoPago;
	}



	public Fecha getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Fecha fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}



	public String getUsuario() {
		return usuario;
	}



	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	

}
