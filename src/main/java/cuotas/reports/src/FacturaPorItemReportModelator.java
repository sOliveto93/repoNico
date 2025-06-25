package cuotas.reports.src;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

import common.model.reports.AbstractReportModelator;
import cuotas.model.domain.factura.Factura;
import cuotas.model.domain.factura.FacturaItem;
import cuotas.model.domain.factura.TipoFactura;

public class FacturaPorItemReportModelator extends AbstractReportModelator<FacturaItem> {
	String parametros;
	@Override
	public String[] createColumnNames() {
		String [] columnNames = new String[10];
		columnNames[0] = "item";
		columnNames[1] = "precio";
		columnNames[2] = "fecha";
		columnNames[3] = "dni";
		columnNames[4] = "nombre";
		columnNames[5] = "cantidad";
		columnNames[6] = "otro";
		columnNames[7] = "parametros";
		columnNames[8] = "partida";
		columnNames[9] = "formaPago";
		return columnNames;
	}

	@Override
	public Class[] createColumnTypes() {
		Class[] columnTypes = new Class[10];
		columnTypes[0] = String.class;
		columnTypes[1] = Double.class;
		columnTypes[2] = Date.class;
		columnTypes[3] = String.class;
		columnTypes[4] = String.class;
		columnTypes[5] = String.class;
		columnTypes[6] = String.class;
		columnTypes[7] = String.class;
		columnTypes[8] = String.class;
		columnTypes[9] = String.class;
		return columnTypes;
	}

	@Override
	public TypedTableModel getModel(Iterator<FacturaItem> items) {
		if(items == null ){ 
			return null;
		}	
		
		while(items.hasNext()){
			FacturaItem item = items.next();
			String itemS = ""; 
			if(item.getCodigo().equals(999)){
				itemS = item.getDescripcion();
			}else{
				itemS = item.getFacturaItemTipo().getDescripcion();	
			}
			Date fecha = item.getFactura().getFechaCarga().getFecha();
			if(item.getFactura().getEstado().isActivo()){
				double precio = item.getPrecioTotal();
				TipoFactura tipoFactura = item.getFactura().getTipoFactura();
				//SI EL TIPO DE FACTURA ES NOTA DE CREDITO -1
				if(tipoFactura!=null){
					if(tipoFactura.getId()==2){
						precio = precio * (-1);
					}
				}
				String formaPago = item.getFactura().getFormaPago().getDescripcion();
				
				getModel().addRow(new Object[]{itemS,precio,fecha,item.getFactura().getPersona().getDocumento(),item.getFactura().getPersona().getNombreCompleto(),item.getCantidad()+"",".",parametros,item.getFacturaItemTipo().getPartida().getDescripcion(),formaPago});
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
