package cuotas.reports.src;


import java.util.Date;
import java.util.Iterator;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

import common.model.reports.AbstractReportModelator;
import cuotas.model.domain.datos.Cheque;
import cuotas.model.domain.datos.Chequera;
import cuotas.model.domain.datos.Persona;

public class ChequeraReportModelator extends AbstractReportModelator<Chequera> {

	public ChequeraReportModelator(){
		super();		
	}
			
	public TypedTableModel getModel(Iterator<Chequera> chequeras){		
		if(chequeras == null ) 
			return null;
		while(chequeras != null && chequeras.hasNext()){
			
			Chequera chequera = chequeras.next();
			if(chequera.getPersona()!=null){
				System.out.println("NombreDesdeChequera: "+chequera.getPersona().getId());
			}else{
				System.out.println("NombreDesdeChequera: no existe tu vieja");
			}
			Persona titularChequera = chequera.getPersona();
			
			//System.out.println("Nombre: " + titularChequera.getNombre());
			Cheque cheque = null;
			Iterator<Cheque> cheques =  null;
			if(chequera != null && chequera.getCheques() != null)
				cheques = chequera.getCheques().iterator();
			
			
			
			while(cheques != null && cheques.hasNext()){
				cheque = cheques.next();
				Double vtoMonto1 = cheque.getMontoVencimiento1();
				Double vtoMonto2 = cheque.getMontoVencimiento2();
				Double total =cheque.getTotal(); 
				Double montoPrimerVencimiento = vtoMonto1 + total; 
				Double montoSegundoVencimiento = vtoMonto2 + total;		
				
				/*System.out.println("cheque.getPago_m().getTipo(): "+cheque.getPago_m().getTipo());*/
				String fecha1ven = "1 Vencimiento: "+cheque.getFechaVencimientoUno().getFechaFormateada();
				String fecha2ven = "2 Vencimiento: "+cheque.getFechaVencimientoDos().getFechaFormateada();
				String concepto1 = "";
				

				/**/
				if(cheque.getPago_m().getTipo().equals("E")){
					concepto1 = "Matricula $";
				}else{
					concepto1 =chequera.getGrupo().getConcepto1();
				}
				
				System.out.println("Concepto: " + concepto1 + " - "+ cheque.getPago_m().getTipo() );
				if(cheque.getMonto1()!=0.0 || cheque.getMonto2()!=0.0){
					getModel().addRow(new Object[]{ 
											titularChequera.getNombreCompleto(),
											titularChequera.getDocumento().getTipo(),
											titularChequera.getDocumento().getNumero(),
											concepto1,
											cheque.getMonto1(),
											chequera.getGrupo().getConcepto2(),
											cheque.getMonto2(),
											cheque.getFechaSting(),
											fecha1ven,
											fecha2ven,
											cheque.getTotal(),
											chequera.getGrupo().getCarrera().getNombreCarrera(),
											cheque.getCodigo(),
											montoPrimerVencimiento,
											montoSegundoVencimiento,
											"Cuota "+cheque.getNumero()+" de "+cheque.getChequera().getGrupo().getCantidadCuotas(),
											"."
										}
									);
				}
				System.out.println("cheque.getCodigo(): "+ cheque.getCodigo());
			}
		}
		return getModel();
		
	}

	@Override
	public String[] createColumnNames() {
		String [] columnNames = new String[17];
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
		columnNames[13] = "montoPrimerVencimiento";
		columnNames[14] = "montoSegundoVencimiento";
		columnNames[15] = "cuota.numero";
		columnNames[16] = "codigo2";
		return columnNames;
				
	}


	@SuppressWarnings("unchecked")
	@Override
	public Class[] createColumnTypes() {
		Class[] columnTypes = new Class[17];
		columnTypes[0] = String.class;
		columnTypes[1] = String.class;
		columnTypes[2] = String.class;
		columnTypes[3] = String.class;
		columnTypes[4] = Integer.class;
		columnTypes[5] = String.class;
		columnTypes[6] = Integer.class;
		columnTypes[7] = String.class;
		columnTypes[8] = String.class;
		columnTypes[9] = String.class;
		columnTypes[10] = Integer.class;
		columnTypes[11] = String.class;
		columnTypes[12] = String.class;
		columnTypes[13] = Double.class;
		columnTypes[14] = Double.class;
		columnTypes[15] = String.class;
		columnTypes[16] = String.class;
		return columnTypes;		
	}

}