package cuotas.reports.src;

import java.util.Date;
import java.util.Iterator;

import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

import common.model.reports.AbstractReportModelator;
import cuotas.model.domain.factura.Factura;
import cuotas.model.domain.factura.FacturaItem;
import cuotas.model.domain.factura.TipoFactura;

public class FacturaReportModelator extends AbstractReportModelator<Factura> {
	String parametros;
	@Override
	public String[] createColumnNames() {
		String [] columnNames = new String[9];
		columnNames[0] = "nroFactura";
		columnNames[1] = "descripcion";
		columnNames[2] = "monto";
		columnNames[3] = "fechaDePago";
		columnNames[4] = "persona";
		columnNames[5] = "dni";
		columnNames[6] = "otro";
		columnNames[7] = "parametros";
		columnNames[8] = "formaPago";
		return columnNames;
	}

	@Override
	public Class[] createColumnTypes() {
		Class[] columnTypes = new Class[9];
		columnTypes[0] = Integer.class;
		columnTypes[1] = String.class;
		columnTypes[2] = Double.class;
		columnTypes[3] = Date.class;
		columnTypes[4] = String.class;
		columnTypes[5] = String.class;
		columnTypes[6] = String.class;
		columnTypes[7] = String.class;
		columnTypes[8] = String.class;
		return columnTypes;
	}

	@Override
	public TypedTableModel getModel(Iterator<Factura> facturas) {
		if(facturas == null ) 
			return null;
		while(facturas.hasNext()){
			Factura factura = facturas.next();
			System.out.println("TipoFactura: "+ factura.getTipoFactura().getId() + " - " + factura.getTipoFactura().getDescripcion());
			Iterator<FacturaItem> items = factura.getFacturaItemsIterator();
			if(items.hasNext()){
				while(items.hasNext()){
					FacturaItem item = items.next();
					String descripcion;
					Double monto;
					Integer nroFactura = factura.getNro();
					if(factura.getEstado().isActivo()){
						descripcion = item.getCantidad()+" - ";
						if(item.getCodigo().equals(999)){
							descripcion = descripcion + item.getDescripcion();
						}else{
							descripcion = descripcion +item.getFacturaItemTipo().getDescripcion(); 
						}
						monto = item.getPrecioTotal();
					}else{
						descripcion = "ANULADA ("+item.getFacturaItemTipo().getDescripcion()+")";
						monto = 0.0;
					}
					TipoFactura tipoFactura = factura.getTipoFactura();
					//SI EL TIPO DE FACTURA ES NOTA DE CREDITO -1
					if(tipoFactura!=null){
						if(tipoFactura.getId()==2){
							monto = monto * (-1);
							//System.out.println("monto: "+monto + "ID: " );
						}
					}
					
					Date fechaDePago = factura.getFechaPago().getFecha();
					String personaDNI = factura.getPersona().getDocumento().getNumero();
					String personaNombreCompleto = factura.getPersona().getNombreCompleto();
					String formaPago = factura.getFormaPago().getDescripcion();
					System.out.println("item: "+descripcion+" precio "+monto+" factura "+nroFactura +" - tipoFac: "+ tipoFactura.getId() + " Desc: " + tipoFactura.getDescripcion() + "FormaPago: "+formaPago);
					//getModel().addRow(new Object[]{nroFactura,descripcion,monto,fechaDePago,personaNombreCompleto,personaDNI,".",parametros});
					getModel().addRow(new Object[]{nroFactura,descripcion,monto,fechaDePago,personaNombreCompleto,personaDNI,".",parametros,formaPago});
	
				}
			}else{				
				String descripcion = "Factura Sin Items";
				Double monto;
				Integer nroFactura = factura.getNro();
				if(!factura.getEstado().isActivo()){
					descripcion = "ANULADA";
				}
				monto = 0.0;
				Date fechaDePago = factura.getFechaPago().getFecha();
				String personaNombreCompleto = factura.getPersona().getNombreCompleto();
				String personaDNI = factura.getPersona().getDocumento().getNumero();
				String formaPago = factura.getFormaPago().getDescripcion();
				//getModel().addRow(new Object[]{nroFactura,descripcion,monto,fechaDePago,personaNombreCompleto,personaDNI,".",parametros});
				getModel().addRow(new Object[]{nroFactura,descripcion,monto,fechaDePago,personaNombreCompleto,personaDNI,".",parametros,formaPago});
			
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
