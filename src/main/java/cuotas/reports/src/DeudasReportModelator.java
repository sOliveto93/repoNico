package cuotas.reports.src;
import java.util.Date;
import java.util.Iterator;

import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

import common.model.domain.datosPersonales.Documento;
import common.model.domain.datosPersonales.Domicilio;
import common.model.domain.datosPersonales.Telefono;
import common.model.reports.AbstractReportModelator;
import cuotas.model.domain.datos.Pago;
import cuotas.model.domain.datos.Persona;

public class DeudasReportModelator extends AbstractReportModelator<Pago> {
	String parametros;
	@Override
	public String[] createColumnNames() {
		String [] columnNames = new String[9];
		columnNames[0] = "persona.documento.tipo";
		columnNames[1] = "persona.documento.numero";
		columnNames[2] = "persona.nombreCompleto";
		columnNames[3] = "cuota.fecha";		
		columnNames[4] = "cuota.monto";
		columnNames[5] = "grupo.nombre";
		columnNames[6] = "parametros";
		columnNames[7] = "otro";
		columnNames[8] = "mail";
		return columnNames;
	}

	@Override
	public Class[] createColumnTypes() {
		Class[] columnTypes = new Class[9];
		columnTypes[0] = String.class;
		columnTypes[1] = String.class;
		columnTypes[2] = String.class;
		columnTypes[3] = String.class;
		columnTypes[4] = Double.class;
		columnTypes[5] = String.class;
		columnTypes[6] = String.class;
		columnTypes[7] = String.class;
		columnTypes[8] = String.class;
		return columnTypes;
	}

	@Override
	public TypedTableModel getModel(Iterator<Pago> pagos) {
		if(pagos == null ) 
			return null;
		while(pagos.hasNext()){
			Pago pago = pagos.next();
			
			Persona persona = pago.getPersona();
			Documento documento = persona.getDocumento();			
			if(documento == null){
				documento = new Documento();
			}
						
			String fecha = "";
			if(pago.getFechaPgo()==null){	
				String mail = persona.getMail();
				System.out.println("Mail: "+ mail);
				fecha = pago.getFecha().getFechaFormateada();			
				getModel().addRow(new Object[]{
						documento.getTipo(), 
						documento.getNumero(), 
						persona.getNombreCompleto() ,						 
						fecha,
						pago.getMonto1()+pago.getMonto2(),
						pago.getGrupo().getDescripcion(),
						parametros,
						".",mail});
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
