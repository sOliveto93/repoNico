package common.model.domain.json;

import common.model.domain.datosPersonales.Domicilio;
import net.sf.json.JSONObject;


public class DomicilioJson extends GenericJson<Domicilio> {

	@Override
	protected JSONObject getJson(Domicilio domicilio) {
		JSONObject domicilioJson = null;
		
		if(domicilio != null){
			domicilioJson = new JSONObject();
			domicilioJson.put("pais", domicilio.getPais());
			domicilioJson.put("pcia", domicilio.getPcia());
			domicilioJson.put("localidad", domicilio.getLocalidad());
			domicilioJson.put("cp", domicilio.getCp());
			domicilioJson.put("calle", domicilio.getCalle());
			domicilioJson.put("numero", domicilio.getNumero());
			domicilioJson.put("piso", domicilio.getPiso());
			domicilioJson.put("depto", domicilio.getDepto());
		}
		return domicilioJson;
	}
	
	@Override
	public Domicilio fromJson(String json) {
		// TODO sin implementar
		return null;
	}

}
