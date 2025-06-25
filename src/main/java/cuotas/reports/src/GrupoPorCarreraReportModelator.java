package cuotas.reports.src;

import java.sql.Date;
import java.util.Iterator;

import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

import common.model.domain.datosPersonales.Documento;
import common.model.domain.datosPersonales.Domicilio;
import common.model.domain.datosPersonales.Telefono;
import common.model.reports.AbstractReportModelator;
import cuotas.model.domain.datos.Persona;

public class GrupoPorCarreraReportModelator extends AbstractReportModelator<Persona> {

	@Override
	public String[] createColumnNames() {
		String [] columnNames = new String[6];
		columnNames[0] = "persona.documento.tipo";
		columnNames[1] = "persona.documento.numero";
		columnNames[2] = "persona.nombreCompleto";
		columnNames[3] = "persona.domicilio";
		columnNames[4] = "persona.localidad";
		columnNames[5] = "persona.telefono";
		return columnNames;
	}

	@Override
	public Class[] createColumnTypes() {
		Class[] columnTypes = new Class[6];
		columnTypes[0] = String.class;
		columnTypes[1] = String.class;
		columnTypes[2] = String.class;
		columnTypes[3] = String.class;
		columnTypes[4] = String.class;
		columnTypes[5] = String.class;
		return columnTypes;
	}

	@Override
	public TypedTableModel getModel(Iterator<Persona> personas) {
		if(personas == null ) 
			return null;

		while(personas.hasNext()){
			Persona persona = personas.next();
			
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
			
			String cabecera="";
			System.out.println(documento.getTipo()+","+ documento.getNumero()+","+ 
					persona.getNombreCompleto() +","+direccion.getCalleConNumero() +","+ direccion.getLocalidad()+","+ numeroTelefono);
			
			getModel().addRow(new Object[]{documento.getTipo(), documento.getNumero(), 
					persona.getNombreCompleto() ,direccion.getCalleConNumero() , direccion.getLocalidad(), numeroTelefono});
				
		}
		
		return getModel();
	}

}
