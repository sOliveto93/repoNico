package cuotas.reports.src;

import java.util.Date;
import java.util.Iterator;

import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

import common.model.domain.datosPersonales.Documento;
import common.model.reports.AbstractReportModelator;
import cuotas.model.domain.datos.Pago;
import cuotas.model.domain.datos.Persona;

public class CancelacionesDeUnPeriodoReportModelator extends AbstractReportModelator<Pago> {

	String parametros;
	
	@Override
	public String[] createColumnNames() {
		String [] columnNames = new String[13];
		columnNames[0] = "persona.documento.tipo";
		columnNames[1] = "persona.documento.numero";
		columnNames[2] = "persona.nombreCompleto";
		columnNames[3] = "cuota.fecha";
		columnNames[4] = "cuota.montoCuota";
		columnNames[5] = "cuota.fechaDePago";
		
		columnNames[6] = "grupo.codigo";
		columnNames[7] = "grupo.descripcion";
		columnNames[8] = "cuota.monPgo";
		columnNames[9] = "partida";
		columnNames[10] = "parametros";
		columnNames[11] = "otro";
		columnNames[12] = "origenPago";
		return columnNames;
	}

	@Override
	public Class[] createColumnTypes() {
		Class[] columnTypes = new Class[13];
		columnTypes[0] = String.class;
		columnTypes[1] = String.class;
		columnTypes[2] = String.class;
		columnTypes[3] = Date.class;
		columnTypes[4] = Double.class;
		columnTypes[5] = Date.class;
		columnTypes[6] = String.class;
		columnTypes[7] = String.class;
		columnTypes[8] = Double.class;
		columnTypes[9] = String.class;
		columnTypes[10] = String.class;
		columnTypes[11] = String.class;
		columnTypes[12] = String.class;
		return columnTypes;
	}

	@Override
	public TypedTableModel getModel(Iterator<Pago> pagos) {
		if(pagos == null ) 
			return null;
		while(pagos.hasNext()){
			Pago pago = pagos.next();
			Date fechaCuota=null;
			Date fechaPago=null;
			Persona persona = pago.getPersona();
			Documento documento = persona.getDocumento();
			
			if(documento == null)
				documento = new Documento();
		
			if(pago.getFecha()!=null){
				fechaCuota =  pago.getFecha().getFecha();
			}
			if(pago.getFechaPgo()!=null){
				fechaPago = pago.getFechaPgo().getFecha();
			}
			String origenPago="";
			if(pago.getOrigenPago()!=null){
				if(pago.getOrigenPago().getDescripcion().equals("Mercado Pago")){
					origenPago = "MP-" + pago.getCodigoTodoPago();
				}else{
					origenPago = pago.getOrigenPago().getDescripcion();
				}
			}
			if(pago.getFechaPgo()!=null){
				getModel().addRow(new Object[]{documento.getTipo(), documento.getNumero(), 
					persona.getNombreCompleto() ,fechaCuota,pago.getMonPgo(), fechaPago,pago.getGrupo().getCodigo(),pago.getGrupo().getDescripcion(), pago.getMonPgo(),pago.getGrupo().getPartida().getDescripcion(), parametros,".",origenPago});
			}
		}
		return getModel();
	}

	public String getParametros() {
		return parametros;
	}

	public void setParametros(String parametros) {
		this.parametros = parametros;
	}

}
