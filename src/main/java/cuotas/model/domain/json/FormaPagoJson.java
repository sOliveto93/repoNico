package cuotas.model.domain.json;

import net.sf.json.JSONObject;
import common.model.domain.json.GenericJson;
import cuotas.model.domain.factura.FormaPago;
import cuotas.model.domain.factura.TipoPago;

public class FormaPagoJson extends GenericJson<FormaPago> {

	@Override
	protected JSONObject getJson(FormaPago formaPago) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", formaPago.getId());
		jsonObj.put("descripcion",formaPago.getDescripcion());
		
		return jsonObj;
	}
	@Override
	public FormaPago fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}
