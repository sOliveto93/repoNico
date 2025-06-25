package cuotas.model.domain.datos;

import java.io.Serializable;

import common.model.domain.datos.Estado;
import common.model.domain.datos.GenericBean;
import common.model.domain.fecha.Mes;

public class Cuota extends GenericBean<Long> implements Serializable, Comparable<Cuota> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2648335106435663839L;
	private TipoCuota tipo;
	private Mes mes;
	private Double monto1;
	private Double monto2;
	private Estado estado;
	private Grupo grupo;

	
	public Cuota(){
		this.estado = new Estado(Estado.ACTIVO);
	}
	
	public Cuota(TipoCuota tipo, Mes mes, Double monto1, Double monto2,Grupo grupo){
		setTipo(tipo);
		setMes(mes);
		setMonto1(monto1);
		setMonto2(monto2);
		setGrupo(grupo);
		this.estado = new Estado(Estado.ACTIVO);
	}
	
	public TipoCuota getTipo() {
		return tipo;
	}
	public void setTipo(TipoCuota tipo) {
		this.tipo = tipo;
	}
	public Mes getMes() {
		return mes;
	}
	public void setMes(Mes mes) {
		this.mes = mes;
	}
	public Double getMonto1() {
		return monto1;
	}
	public void setMonto1(Double monto1) {
		this.monto1 = monto1;
	}
	public Double getMonto2() {
		return monto2;
	}
	public void setMonto2(Double monto2) {
		this.monto2 = monto2;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	//@Override
	public int compareTo(Cuota o) {
		return this.mes.getNumero().compareTo(o.getMes().getNumero());
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	@Override
	public String getInformacionAuditoria() {		
		return toString();
	}
	public String toString(){
		return "ID: "+getId()+" - tipoCuota " + tipo.getDescripcion() + " - monto1 " + monto1 + " - monto2" + monto2 + "- Grupo "+ grupo.getDescripcion();
	}

	public boolean isMontosNulos(){
		return ( monto1 == null && monto2 == null );
	}
	
}
