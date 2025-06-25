package cuotas.reports.src;

import java.sql.Date;
import java.util.Iterator;

import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

import common.model.domain.datosPersonales.Documento;
import common.model.domain.datosPersonales.Domicilio;
import common.model.domain.datosPersonales.Telefono;
import common.model.reports.AbstractReportModelator;
import cuotas.model.domain.datos.Pago;
import cuotas.model.domain.datos.Persona;

public class PagosReportModelator extends AbstractReportModelator<Pago> {

	@Override
	public String[] createColumnNames() {
		String [] columnNames = new String[8];
		columnNames[0] = "persona.documento.tipo";
		columnNames[1] = "persona.documento.numero";
		columnNames[2] = "persona.nombreCompleto";
		columnNames[3] = "persona.domicilio";
		columnNames[4] = "persona.localidad";
		columnNames[5] = "persona.telefono";
		columnNames[6] = "pago.fecha";
		columnNames[7] = "pago.monto";
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
		columnTypes[6] = Date.class;
		columnTypes[7] = Double.class;
		return columnTypes;
	}

	@Override
	public TypedTableModel getModel(Iterator<Pago> pagos) {
		if(pagos == null ) 
			return null;
		
		Persona personaAnterior = null ;
		Double total = 0.0;
		//Boolean correspondePonerTotal =false;
		while(pagos.hasNext()){
			Pago pago = pagos.next();
			
			Persona persona = pago.getPersona();
			
			//-----
			if(persona == personaAnterior || personaAnterior == null){
	    		total += pago.getMonto1();
	    		personaAnterior = persona;
	    	}/*else{
	    		correspondePonerTotal = true;
	    	}*/
			//-------
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
			/*if(correspondePonerTotal){
				correspondePonerTotal = false;
				getModel().addRow(new Object[]{documento.getTipo(), documento.getNumero(), 
						persona.getNombreCompleto() ,"TOTAL" , null,total});
				total = 0.0;
				personaAnterior = persona;
			}*/
			getModel().addRow(new Object[]{documento.getTipo(), documento.getNumero(), 
					persona.getNombreCompleto() ,direccion.getCalleConNumero() , direccion.getLocalidad(), numeroTelefono, pago.getFecha().getFecha(),pago.getMonto1()});
				
		}
		
		return getModel();
	}

}
