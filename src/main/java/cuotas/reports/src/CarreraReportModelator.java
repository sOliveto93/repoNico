package cuotas.reports.src;

import java.util.Iterator;

import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

import common.model.domain.datosPersonales.Documento;
import common.model.reports.AbstractReportModelator;
import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.Inscripcion;
import cuotas.model.domain.datos.Persona;

public class CarreraReportModelator extends	AbstractReportModelator<Inscripcion> {

	@Override
	public String[] createColumnNames() {
		String [] columnNames = new String[7];
		columnNames[0] = "numeroDocumento";
		columnNames[1] = "nombreCompleto";		
		columnNames[2] = "nombreCarrera";
		columnNames[3] = "cohorte";
		columnNames[4] = "estado";
		columnNames[5] = "otro";
		columnNames[6] = "mail";
		return columnNames;
	}

	@Override
	public Class[] createColumnTypes() {
		Class<?>[] columnTypes = new Class[7];
		columnTypes[0] = Integer.class;
		columnTypes[1] = String.class;
		columnTypes[2] = String.class;
		columnTypes[3] = Integer.class;		
		columnTypes[4] = String.class;
		columnTypes[5] = String.class;
		columnTypes[6] = String.class;
		return columnTypes;
	}

	@Override
	public TypedTableModel getModel(Iterator<Inscripcion> inscripciones) {
		if(inscripciones==null)
			return null;
		
		while(inscripciones.hasNext()){
			Inscripcion inscripcion = inscripciones.next();
		
			Persona persona = inscripcion.getPersona();
			Carrera carrera = inscripcion.getCarrera(); 
			char estadoLetra = inscripcion.getEstado();
			String beca = " - ";
			if(inscripcion.getPersonasBeca()!=null){
				beca = inscripcion.getPersonasBeca().getDescuentoBeca()+" %";
			}
			String mail = "";
			if(persona.getMail()!=null){
				mail = persona.getMail();
			}
				
			String estado="";
			switch (estadoLetra){
				case 'A':
					estado = "Activo";
					break;
				case 'N':
					estado = "Abandono";
					break;
				case 'P':
					estado = "Pasivo";
					break;
				case 'I':
					estado = "Ingresante";
					break;
				case 'E':
					estado = "Egresado";
					break;
			}

			getModel().addRow(new Object[]{persona.getDocumento().getNumero(),
							persona.getNombreCompleto(),
							carrera.getNombreCarrera(),
							inscripcion.getCohorte(),
							estado,beca,mail});
		}
		return getModel();
	}

}
