package cuotas.model.domain.json;

import net.sf.json.JSONObject;
import common.model.domain.json.GenericJson;
import cuotas.model.domain.datos.PersonasBeca;

public class PersonasBecaJson extends GenericJson<PersonasBeca> {

	@Override
	public PersonasBeca fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected JSONObject getJson(PersonasBeca personasBeca) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", personasBeca.getId());
		jsonObj.put("descuentoBeca",personasBeca.getDescuentoBeca());
		//jsonObj.put("inscripcion", FactoryJson.inscripcionesJson.getJson( personasBeca.getInscripcion())); 
		jsonObj.put("beca", FactoryJson.becaJson.getJson( personasBeca.getBeca()));
		return jsonObj;
	}

}
