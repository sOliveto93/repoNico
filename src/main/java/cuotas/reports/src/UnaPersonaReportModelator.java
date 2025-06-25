package cuotas.reports.src;

import java.util.Date;
import java.util.Iterator;

import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

import common.model.domain.datosPersonales.Documento;
import common.model.domain.datosPersonales.Domicilio;
import common.model.domain.datosPersonales.Telefono;
import common.model.domain.fecha.Fecha;
import common.model.reports.AbstractReportModelator;
import cuotas.model.domain.datos.Pago;
import cuotas.model.domain.datos.Persona;

public class UnaPersonaReportModelator extends AbstractReportModelator<Pago> {

	@Override
	public String[] createColumnNames() {
		String [] columnNames = new String[12];
		columnNames[0] = "persona.documento.tipo";
		columnNames[1] = "persona.documento.numero";
		columnNames[2] = "persona.nombreCompleto";		
		columnNames[3] = "cuota.fecha";		
		columnNames[4] = "cuota.monto1";
		columnNames[5] = "cuota.monto2";
		columnNames[6] = "cuota.monPgo";
		columnNames[7] = "grupo.descripcion";
		columnNames[8] = "numero";
		columnNames[9] = "otro";
		columnNames[10] = "observacion";
		columnNames[11] = "mail";
		return columnNames;
	}

	@Override
	public Class<?>[] createColumnTypes() {
		Class<?>[] columnTypes = new Class[12];
		columnTypes[0] = String.class;
		columnTypes[1] = String.class;
		columnTypes[2] = String.class;
		columnTypes[3] = String.class;		
		columnTypes[4] = Double.class;
		columnTypes[5] = Double.class;
		columnTypes[6] = Double.class;
		columnTypes[7] = String.class;
		columnTypes[8] = Integer.class;
		columnTypes[9] = String.class;
		columnTypes[10] = String.class;
		columnTypes[11] = String.class;
		return columnTypes;
	}


	@Override
	public TypedTableModel getModel(Iterator<Pago> pagos) {
		if(pagos == null ) 
			return null;
		
		while(pagos.hasNext()){
			Pago pago = pagos.next();
			Persona persona = pago.getPersona();
			String mail = "";
			
			if(persona.getMail()!=null){
				mail = persona.getMail();
			}
			double monto = 0.0;
			Documento documento = persona.getDocumento();
			if(documento == null)
				documento = new Documento();			
			
			
			String fechaCuota = "";
			if(pago.getFecha()!=null){
				fechaCuota = pago.getFecha().getFechaFormateada();				
			}
			if(pago.getFechaPgo()!=null){
				monto = pago.getMonPgo();
			}
			getModel().addRow(new Object[]{
						documento.getTipo(), 
						documento.getNumero(), 
						persona.getNombreCompleto() ,						 
						fechaCuota,						 
						pago.getMonto1(),
						pago.getMonto2(),
						monto,
						pago.getGrupo().getDescripcion(),
						pago.getNumero(),
						".",
						pago.getObservaciones(),mail});
		}
		return getModel();
	}

}
