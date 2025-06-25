package cuotas.model.domain.json;

import net.sf.json.JSONObject;
import common.model.domain.datosPersonales.Domicilio;
import common.model.domain.json.GenericJson;

public class DomicilioJson extends GenericJson<Domicilio> {

	@Override
	public Domicilio fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JSONObject getJson(Domicilio domicilio) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("provincia", domicilio.getPcia());
		jsonObj.put("localidad",domicilio.getLocalidad());
		jsonObj.put("cp",domicilio.getCp());
		jsonObj.put("calle",domicilio.getCalle());
		jsonObj.put("numero",domicilio.getNumero());
		jsonObj.put("depto",domicilio.getDepto());
		jsonObj.put("piso",domicilio.getPiso());
		return jsonObj;
	}

}
