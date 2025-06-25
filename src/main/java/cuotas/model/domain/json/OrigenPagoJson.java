package cuotas.model.domain.json;

import net.sf.json.JSONObject;
import common.model.domain.json.GenericJson;
import cuotas.model.domain.datos.OrigenPago;

public class OrigenPagoJson extends GenericJson<OrigenPago> {

	@Override
	public OrigenPago fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JSONObject getJson(OrigenPago origenPago) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", origenPago.getId());
		jsonObj.put("descripcion",origenPago.getDescripcion());
		
		return jsonObj;
	}

}
