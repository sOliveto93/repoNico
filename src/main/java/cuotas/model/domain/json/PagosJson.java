package cuotas.model.domain.json;

import net.sf.json.JSONObject;
import common.model.domain.json.GenericJson;
import cuotas.model.domain.datos.Pago;

public class PagosJson extends GenericJson<Pago> {

	@Override
	public Pago fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JSONObject getJson(Pago pago) {
		JSONObject jsonObj = new JSONObject();
		
		//String personaJson = FactoryJson.personaJson.toJson(pago.getPersona());
		//jsonObj.put("persona",personaJson);
		
		jsonObj.put("id",pago.getId());
		jsonObj.put("numero",pago.getNumero());
		jsonObj.put("idPersona",pago.getPersona().getId());
		jsonObj.put("ano",pago.getAno());
		
		jsonObj.put("observaciones",pago.getObservaciones());
		//System.out.println("observaciones: "+pago.getObservaciones());
		
		if(pago.getFecha()!=null)
			jsonObj.put("fecha",pago.getFecha().getFechaFormateada());
		
		jsonObj.put("tipo",pago.getTipo());
		jsonObj.put("monto1",pago.getMonto1());
		jsonObj.put("monto2",pago.getMonto2());
		jsonObj.put("vtoMonto",pago.getVtoMonto());
		
		if(pago.getFechaPgo()!=null)
			jsonObj.put("fechaPgo",pago.getFechaPgo().getFechaFormateada());
		
		jsonObj.put("monPgo",pago.getMonPgo());
		String fechaPagoCarga ="";
		if(pago.getFePgoCarga()!=null)
			fechaPagoCarga= pago.getFePgoCarga().getFechaFormateada();
		
		
		if(pago.getFechaPgo() != null){
			String detallePago = pago.getOrigenPago().getDescripcion();
			if(pago.getOrigenPago().getId() == 5) {
				detallePago = detallePago + " " + pago.getCodigoTodoPago();
			}
			if(pago.getOrigenPago().getId() == 4) {
				detallePago = detallePago + " " + pago.getCodigoTodoPago();
			}
			if(pago.getOrigenPago().getId() == 2) {
				String tipoPagoDescripcion = "";
				String numeroFactura = "";
				if(pago.getFacturaItem()!=null){
					tipoPagoDescripcion = pago.getFacturaItem().getFactura().getTipoPago().getDescripcion();
					numeroFactura       = pago.getFacturaItem().getFactura().getNro().toString();
				}
				System.out.println( detallePago + " - "+ tipoPagoDescripcion + " - " + numeroFactura );
				detallePago = detallePago + " " + tipoPagoDescripcion+ "-" +numeroFactura;
			}
			jsonObj.put("detallePago", detallePago);
		}else{
			jsonObj.put("detallePago", "");
		}
		String codigoTodoPago = "";
		String origenPagoJson = FactoryJson.origenPagoJson.toJson(pago.getOrigenPago());
		String tipoPagoJson ="";
		if(pago.getOrigenPago().getId() == 2) {
			if(pago.getFacturaItem()!=null){
				tipoPagoJson = FactoryJson.tipoPagoJson.toJson(pago.getFacturaItem().getFactura().getTipoPago());
			}
		}
		if(pago.getCodigoTodoPago()!= null) {
			codigoTodoPago = pago.getCodigoTodoPago();
		}
		jsonObj.put("tipoPago",tipoPagoJson);		
		jsonObj.put("origenPago",origenPagoJson);		
		jsonObj.put("codigoTodoPago",codigoTodoPago);
		jsonObj.put("fePgoCarga",fechaPagoCarga);
		
		String grupoJson = FactoryJson.grupoJson.toJson(pago.getGrupo());
		jsonObj.put("grupo",grupoJson);		
		jsonObj.put("estado",pago.getEstado());
		jsonObj.put("exceptuarMora",pago.isExceptuarMora());
		if(pago.getFechaPgo() != null){
			jsonObj.put("pagada", true);	
		}else{
			jsonObj.put("pagada", false);
		}
		
		return jsonObj;
	}

}
