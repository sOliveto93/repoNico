package common.model.domain.json;

import common.model.domain.datosPersonales.Localidad;

import net.sf.json.JSONObject;

public class LocalidadJson extends GenericJson<Localidad> {

	@Override
	public JSONObject getJson(Localidad localidad) {
		JSONObject localidadJson = new JSONObject();
		localidadJson.put("id", localidad.getId());
		localidadJson.put("nombre", localidad.getNombre());		
		return localidadJson;
	}

	@Override
	public Localidad fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}
