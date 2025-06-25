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


public class DeudasPorGrupoCarreraReportModelator extends
		AbstractReportModelator<Pago> {
	
	String parametros;
	@Override
	public String[] createColumnNames() {
		String [] columnNames = new String[12];
		columnNames[0] = "persona.documento.tipo";
		columnNames[1] = "persona.documento.numero";
		columnNames[2] = "persona.nombreCompleto";
		columnNames[3] = "persona.domicilio";
		columnNames[4] = "persona.localidad";
		columnNames[5] = "persona.telefono";
		columnNames[6] = "cuota.fecha";
		columnNames[7] = "cuota.monto";
		columnNames[8] = "cuota.pagado";
		columnNames[9] = "grupo";
		columnNames[10] = "parametros";
		columnNames[11] = "otro";
		return columnNames;
	}

	@Override
	public Class[] createColumnTypes() {
		Class[] columnTypes = new Class[12];
		columnTypes[0] = String.class;
		columnTypes[1] = String.class;
		columnTypes[2] = String.class;
		columnTypes[3] = String.class;
		columnTypes[4] = String.class;
		columnTypes[5] = String.class;
		columnTypes[6] = Date.class;
		columnTypes[7] = Double.class;
		columnTypes[8] = Double.class;
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
			Documento documento = persona.getDocumento();
			Domicilio direccion = persona.getDireccion();
			String domicilio = "";
			String localidad = "";
			String numeroTefono="";
			
			if(documento == null)
				documento = new Documento();
			
			if(direccion != null){
				domicilio = persona.getDireccion().getCalleConNumero();
				localidad = persona.getDireccion().getLocalidad();
			}

			Telefono telefono = persona.getTelefono();
			if(telefono != null)
				numeroTefono = telefono.getNumero(); 
			
			Date fechaCuota = null;
			if(pago.getFecha()!=null)
				fechaCuota = pago.getFecha().getFecha();	
				
			if(pago.getFechaPgo()==null){
				getModel().addRow(new Object[]{
						documento.getTipo(), 
						documento.getNumero(), 
						persona.getNombreCompleto() ,
						domicilio,
						localidad,  
						numeroTefono, 
						fechaCuota,
						pago.getMonto1()+pago.getMonto2(),
						0,
						pago.getGrupo().getDescripcion(),
						parametros,
						"."});				
			}else{
				getModel().addRow(new Object[]{
						documento.getTipo(), 
						documento.getNumero(), 
						persona.getNombreCompleto() ,
						domicilio,
						localidad,  
						numeroTefono, 
						fechaCuota,
						0.0,
						pago.getMonPgo(),
						pago.getGrupo().getDescripcion(),
						parametros,
						"."});
			}
				
		System.out.println("Grupo Nro: " + pago.getGrupo().getId() + "- Descripcion:  " + pago.getGrupo().getDescripcion());
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
