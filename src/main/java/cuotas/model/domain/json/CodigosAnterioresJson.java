package cuotas.model.domain.json;

import net.sf.json.JSONObject;
import common.model.domain.json.GenericJson;
import cuotas.model.domain.datos.CodigosAnteriores;

public class CodigosAnterioresJson extends GenericJson<CodigosAnteriores> {

	@Override
	protected JSONObject getJson(CodigosAnteriores codigosAnteriores) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", codigosAnteriores.getId());
		jsonObj.put("pago",FactoryJson.pagosJson.getJson(codigosAnteriores.getPago()));
		jsonObj.put("codigoBarra",codigosAnteriores.getCodigoBarra());
		
		return jsonObj;
	}

	@Override
	public CodigosAnteriores fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}
