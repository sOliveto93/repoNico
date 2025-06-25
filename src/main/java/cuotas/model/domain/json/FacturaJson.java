package cuotas.model.domain.json;

import net.sf.json.JSONObject;
import common.model.domain.json.GenericJson;
import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.model.domain.factura.Factura;

public class FacturaJson extends GenericJson<Factura> {

	@Override
	public Factura fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JSONObject getJson(Factura factura) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("numero", factura.getNro());
		jsonObj.put("fechaPago",factura.getFechaPago().getFechaFormateada());
		jsonObj.put("items", FactoryJson.facturaItemJson.getJson(factura.getFacturaItemsIterator()));
		jsonObj.put("persona",FactoryJson.personaJson.getJson(factura.getPersona()));
		jsonObj.put("descripcionBasica",factura.getDescripcionBasica());
		jsonObj.put("descripcionExtendida",factura.getDescripcionExtendida());
		jsonObj.put("monto", factura.getMonto());
		jsonObj.put("formaPago", FactoryJson.formaPagoJson.getJson(factura.getFormaPago()));
		jsonObj.put("sucursal", factura.getSucursalCobro().getDescri());
		jsonObj.put("tipoPago", FactoryJson.tipoPagoJson.getJson(factura.getTipoPago()));
		
		return jsonObj;
	}
	public String getJsonMsj(String msj) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("msj", msj);
		jsonObj = getEsqueletoJson(jsonObj);
		return jsonObj.toString();
	}
}
