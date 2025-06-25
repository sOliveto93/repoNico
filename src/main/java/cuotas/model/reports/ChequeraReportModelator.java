/*package cuotas.model.reports;


import java.util.Date;
import java.util.Iterator;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

import common.model.reports.AbstractReportModelator;

public class ChequeraReportModelator<T extends Chequera> extends AbstractReportModelator<Chequera> {

	public ChequeraReportModelator(){
		super();		
	}
			
	public TypedTableModel getModel(Iterator<Chequera> chequeras){		
		Integer i=0;
		if(chequeras == null ) return null;
		while(chequeras.hasNext()){
			Chequera chequera = chequeras.next();
						getModel().addRow(new Object[]{ 
											chequera.getPersona().getNombreCompleto(),
											chequera.getPersona().getDocumento().getTipo(),
											chequera.getPersona().getDocumento().getNumero(),
											chequera.getGrupo().getConcepto1(),
											chequera.getCuota().getMonto1(),//chequera.getGrupo().getVtoMonto(),
											chequera.getGrupo().getConcepto2(),
											chequera.getCuota().getMonto2(),//chequera.getGrupo().getVtoMontoPlus(),
											chequera.getFechaSting(),
											chequera.getFechaVencimientoUno(),
											chequera.getFechaVencimientoDos(),
											chequera.getTotal(),
											chequera.getGrupo().getCarrera().getNombreCarrera(),
											chequera.getCodigo()
										}
									);
									i++;
								}
		return getModel();
		
	}


	@Override
	public String[] createColumnNames() {
		String [] columnNames = new String[13];
		columnNames[0] = "nombre";
		columnNames[1] = "tipo_documento";
		columnNames[2] = "nro_documento";
		columnNames[3] = "concepto1";
		columnNames[4] = "monto1";
		columnNames[5] = "concepto2";
		columnNames[6] = "monto2";
		columnNames[7] = "fecha";
		columnNames[8] = "fecha1";
		columnNames[9] = "fecha2";
		columnNames[10] = "total";
		columnNames[11] = "carrera";
		columnNames[12] = "codigo";
		return columnNames;
				
	}


	@SuppressWarnings("unchecked")
	@Override
	public Class[] createColumnTypes() {
		Class[] columnTypes = new Class[13];
		columnTypes[0] = String.class;
		columnTypes[1] = String.class;
		columnTypes[2] = String.class;
		columnTypes[3] = String.class;
		columnTypes[4] = Integer.class;
		columnTypes[5] = String.class;
		columnTypes[6] = Integer.class;
		columnTypes[7] = String.class;
		columnTypes[8] = Date.class;
		columnTypes[9] = Date.class;
		columnTypes[10] = Integer.class;
		columnTypes[11] = String.class;
		columnTypes[12] = String.class;
		return columnTypes;		
	}

}*/