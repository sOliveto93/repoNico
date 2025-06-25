package cuotas.reports.src;

import java.util.Date;
import java.util.Iterator;

import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

import common.model.reports.AbstractReportModelator;
import cuotas.model.domain.datos.Persona;
import cuotas.model.domain.factura.Factura;
import cuotas.model.domain.factura.FacturaItem;
import cuotas.model.domain.factura.TipoFactura;

public class FacturaDuplicadoReportModelator extends AbstractReportModelator<Factura> {
	String parametros;
	@Override
	public String[] createColumnNames() {
		String [] columnNames = new String[15];
		columnNames[0] = "nroFactura";
		columnNames[1] = "tipoFactura";
		columnNames[2] = "fechaFactura";
		columnNames[3] = "facturaItemCodigo";
		columnNames[4] = "cantidad";
		columnNames[5] = "detalleItem";
		columnNames[6] = "precio";
		columnNames[7] = "importe";
		columnNames[8] = "nombreCompleto";
		columnNames[9] = "domicilioCompleto";
		columnNames[10] = "carrerasActivas";
		columnNames[11] = "tipoPago";
		columnNames[12] = "totalFactura";
		columnNames[13] = "responsableIva";
		columnNames[14] = "razonSocial";
		return columnNames;
	}

	@Override
	public Class[] createColumnTypes() {
		Class[] columnTypes = new Class[15];
		columnTypes[0] = Integer.class;
		columnTypes[1] = String.class;
		columnTypes[2] = Date.class;
		columnTypes[3] = Integer.class;
		columnTypes[4] = Integer.class;
		columnTypes[5] = String.class;
		columnTypes[6] = Double.class;
		columnTypes[7] = Double.class;
		columnTypes[8] = String.class;
		columnTypes[9] = String.class;
		columnTypes[10] = String.class;
		columnTypes[11] = String.class;
		columnTypes[12] = Double.class;
		columnTypes[13] = String.class;
		columnTypes[14] = String.class;
		return columnTypes;
	}
	@Override
	public TypedTableModel getModel(Iterator<Factura> facturas) {
		if(facturas == null ){
			return null;	
		}
		
		while(facturas.hasNext()){
			Factura factura = facturas.next();

			Persona persona = factura.getPersona();
			Date fechaFactura = factura.getFechaPago().getFecha();	
			Double totalFactura = factura.getMonto();
			TipoFactura tipoFactura = factura.getTipoFactura();	
			
			String nombreCompleto = persona.getNombreCompleto();
			String domicilioCompleto = "";
			String carrerasActivas = "";
			String tipoFacturaDescripcion = tipoFactura.getDescripcion();
			Iterator<FacturaItem> items = factura.getFacturaItemsIterator();
			
			
			String responsableIva ="";
			String razonSocial ="";
			
			
			if(!factura.getDescripcionExtendida().equals("")){
				carrerasActivas = factura.getDescripcionExtendida();
			}else{
				carrerasActivas = persona.getCarrerasActiva();
			}
			
			
			responsableIva = factura.getResponsableIva();
			
			if(factura.getRazonSocial() !=null && !factura.getRazonSocial().equals("")){
				razonSocial = factura.getRazonSocial();
			}
			if(persona.getDireccion()!=null){
				domicilioCompleto = persona.getDireccion().getDomicilioCompleto(); 
			}
			
			while(items.hasNext()){
				
				FacturaItem item = items.next();
				Integer nroFactura = factura.getNro();

				Double precio = item.getPrecio();
				Double importe = item.getPrecioTotal();
				Integer cantidadItem = item.getCantidad();
				Integer facturaItemCodigo = item.getCodigo();
				
				String descripcionItem = item.getDescripcion();
				
				String tipoPago = factura.getFormaPago().getDescripcion();
				// factura.formaPago.descripcion
				getModel().addRow(new Object[]{nroFactura ,tipoFacturaDescripcion ,fechaFactura,facturaItemCodigo,cantidadItem,descripcionItem,precio ,importe,  nombreCompleto,domicilioCompleto,carrerasActivas,tipoPago,totalFactura,responsableIva,razonSocial});	
				
			}
		}
		return getModel();
	}
/*	@Override
	public TypedTableModel getModel(Factura factura) {
		if(factura == null ) 
			return null;
		
			//Factura factura = facturas.next();
			System.out.println("TipoFactura: "+ factura.getTipoFactura().getId() + " - " + factura.getTipoFactura().getDescripcion());
			Iterator<FacturaItem> items = factura.getFacturaItemsIterator();
			if(items.hasNext()){
				while(items.hasNext()){
					FacturaItem item = items.next();
					String descripcion;
					Double monto;
					Integer nroFactura = factura.getNro();
					if(factura.getEstado().isActivo()){
						descripcion = item.getCantidad()+" - "+item.getFacturaItemTipo().getDescripcion();
						monto = item.getPrecioTotal();
					}else{
						descripcion = "ANULADA ("+item.getFacturaItemTipo().getDescripcion()+")";
						monto = 0.0;
					}
					
					
			
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
				
					// 							  {numeroFactura,tipoFactura             ,fechaFactura,facturaItemCodigo,cantidad   ,detalleItem     ,precio,importe,nombreCompleto.domicilioCompleto,carrerasActivas}
					getModel().addRow(new Object[]{nroFactura   ,tipoFacturaDescripcion ,fechaFactura,facturaItemCodigo,cantidadItem,descripcionItem,});
					
					
					
					//getModel().addRow(new Object[]{nroFactura,descripcion,monto,fechaDePago,personaNombreCompleto,personaDNI,".",parametros});
					//getModel().addRow(new Object[]{nroFactura,descripcion,monto,fechaDePago,personaNombreCompleto,personaDNI,".",parametros,formaPago});
	
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
	}*/

	public String getParametros() {
		return parametros;
	}

	public void setParametros(String parametros) {
		this.parametros = parametros;
	}

	

}
