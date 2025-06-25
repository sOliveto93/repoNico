package common.model.domain.json;

import net.sf.json.JSONObject;
import common.model.domain.datosPersonales.Telefono;

public class TelefonoJson extends GenericJson<Telefono> {

	@Override
	protected JSONObject getJson(Telefono telefono) {
		JSONObject telefonoJson = null;
		
		if(telefono != null){
			telefonoJson = new JSONObject();
			telefonoJson.put("numero", telefono.getNumero() );
			telefonoJson.put("tipo", telefono.getTipo());
		}
		return telefonoJson ;
	}

	@Override
	public Telefono fromJson(String json) {
		// TODO sin implementar aun
		return null;
	}

}
