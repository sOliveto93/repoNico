package cuotas.model.domain.json;

import net.sf.json.JSONObject;
import common.model.domain.json.GenericJson;
import cuotas.model.domain.factura.TipoPago;

public class TipoPagoJson extends GenericJson<TipoPago> {

	@Override
	public TipoPago fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JSONObject getJson(TipoPago tipoPago) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", tipoPago.getId());
		jsonObj.put("descripcion",tipoPago.getDescripcion());
		
		return jsonObj;
	}

}
