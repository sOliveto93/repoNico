package cuotas.reports.src;

import java.util.Iterator;

import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

import common.model.domain.datosPersonales.Documento;
import common.model.domain.datosPersonales.Domicilio;
import common.model.domain.datosPersonales.Telefono;
import common.model.reports.AbstractReportModelator;
import cuotas.model.domain.datos.Pago;
import cuotas.model.domain.datos.Persona;

public class PagosSinMontoReportModelator extends AbstractReportModelator<Pago> {

	@Override
	public String[] createColumnNames() {
		String [] columnNames = new String[8];
		columnNames[0] = "persona.documento.tipo";
		columnNames[1] = "persona.documento.numero";
		columnNames[2] = "persona.nombreCompleto";
		columnNames[3] = "persona.domicilio";
		columnNames[4] = "persona.localidad";
		columnNames[5] = "persona.telefono";
		columnNames[7] = "pago.monto";
		columnNames[6] = "pago.monto2";
		return columnNames;
	}

	@Override
	public Class<?>[] createColumnTypes() {
		Class<?>[] columnTypes = new Class[8];
		columnTypes[0] = String.class;
		columnTypes[1] = String.class;
		columnTypes[2] = String.class;
		columnTypes[3] = String.class;
		columnTypes[4] = String.class;
		columnTypes[5] = String.class;
		columnTypes[6] = Double.class;
		columnTypes[7] = Double.class;
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
			if(documento == null)
				documento = new Documento();
			
			Domicilio direccion = persona.getDireccion();	
			if(direccion == null)
				direccion = new Domicilio();
			
			Telefono telefono = persona.getTelefono();
			String numeroTelefono = "" ;
			
			if(telefono != null)
				numeroTelefono = telefono.getNumero();
			
			getModel().addRow(new Object[]{documento.getTipo(), documento.getNumero(), 
					persona.getNombreCompleto() ,direccion.getCalleConNumero() , direccion.getLocalidad(), numeroTelefono, pago.getMonto1(),pago.getMonto2()});
				
		}
		
		return getModel();
	}

}
