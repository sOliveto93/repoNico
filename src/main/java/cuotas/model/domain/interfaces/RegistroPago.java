package cuotas.model.domain.interfaces;

import common.model.domain.datos.GenericBean;
import common.model.domain.fecha.Fecha;
import java.util.Date;


public class RegistroPago extends GenericBean<Long>  implements java.io.Serializable {
	private static final long serialVersionUID = -2196112564114521841L;{		
	}
	
	Integer nro_colegio = 0;
	String colegio = "";
	Long nro_persona = 0L;
	Long nro_grupo = 0L;
	Fecha fecha_pago;
	Fecha fecha_proc;
	Fecha fecha_real;
	Double importe = 0.0; 
	Fecha vto_original;
	Integer periodo = 0;
	String cod_barra = "";
	Long cliente = 0L;
	String terminal = "";
	String estado = "";
	String tipo_doc = "";
    String num_doc = "";
    Fecha fecha;

	public RegistroPago (){		
	}
	
	public boolean setearRegistroPago(Object[] rowObjects){
		if(rowObjects.length<=14){
			this.nro_colegio = ((Double)rowObjects[0]).intValue();
			this.colegio = (String)rowObjects[1];
			this.cliente = ((Double)rowObjects[2]).longValue();
			this.nro_persona = ((Double)rowObjects[2]).longValue()/10000;
			this.nro_grupo = (((Double)rowObjects[2]).longValue()%10000);
			this.fecha_pago = new Fecha((Date)rowObjects[4]); 
			this.fecha_proc = new Fecha(((Date)rowObjects[5]));
			this.fecha_real = new Fecha(((Date)rowObjects[6]));			
			this.importe = (Double)rowObjects[7];
			this.vto_original = new Fecha(((Date)rowObjects[8]));
			this.periodo = ((Double)rowObjects[9]).intValue();

			this.cod_barra = (String)rowObjects[11];
			this.terminal = (String)rowObjects[12];
			return true;			
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "RegistroPago [cod_barra=" + cod_barra + ", colegio=" + colegio
			    + ", fecha_pago=" + fecha_pago
				+ ", fecha_proc=" + fecha_proc + ", fecha_real=" + fecha_real
				+ ", importe=" + importe + ", nro_cliente="
				+ nro_persona + ", nro_grupo" + nro_grupo + ", nro_colegio=" + nro_colegio + ", periodo="
				+ periodo + ", terminal="
				+ terminal + ", vto_original=" + vto_original + "]";
	}

	public Integer getNro_colegio() {
		return nro_colegio;
	}
	public void setNro_colegio(Integer nroColegio) {
		nro_colegio = nroColegio;
	}
	public String getColegio() {
		return colegio;
	}
	public void setColegio(String colegio) {
		this.colegio = colegio;
	}
		
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public Integer getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}
	public String getCod_barra() {
		return cod_barra;
	}
	public void setCod_barra(String codBarra) {
		cod_barra = codBarra;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	
	public Fecha getFecha_pago() {
		return fecha_pago;
	}

	public void setFecha_pago(Fecha fechaPago) {
		fecha_pago = fechaPago;
	}

	public Fecha getFecha_proc() {
		return fecha_proc;
	}

	public void setFecha_proc(Fecha fechaProc) {
		fecha_proc = fechaProc;
	}

	public Fecha getFecha_real() {
		return fecha_real;
	}

	public void setFecha_real(Fecha fechaReal) {
		fecha_real = fechaReal;
	}

	public Fecha getVto_original() {
		return vto_original;
	}

	public void setVto_original(Fecha vtoOriginal) {
		vto_original = vtoOriginal;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Long getNro_persona() {
		return nro_persona;
	}

	public void setNro_persona(Long nroPersona) {
		nro_persona = nroPersona;
	}

	public Long getNro_grupo() {
		return nro_grupo;
	}

	public void setNro_grupo(Long nroGrupo) {
		nro_grupo = nroGrupo;
	}

	public String getTipo_doc() {
		return tipo_doc;
	}

	public void setTipo_doc(String tipoDoc) {
		tipo_doc = tipoDoc;
	}

	public String getNum_doc() {
		return num_doc;
	}

	public void setNum_doc(String numDoc) {
		num_doc = numDoc;
	}

	public Fecha getFecha() {
		return fecha;
	}

	public void setFecha(Fecha fecha) {
		this.fecha = fecha;
	}

	public Long getCliente() {
		return cliente;
	}
	public String getClienteString() {
		return String.format("%09d", cliente);
	}

	public void setCliente(Long cliente) {
		this.cliente = cliente;
	}

	
}
