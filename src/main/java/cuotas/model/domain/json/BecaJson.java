package cuotas.model.domain.json;

import net.sf.json.JSONObject;
import common.model.domain.json.GenericJson;
import cuotas.model.domain.datos.Beca;

public class BecaJson extends GenericJson<Beca> {

	@Override
	public Beca fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected JSONObject getJson(Beca beca) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", beca.getId());
		jsonObj.put("descripcion",beca.getDescripcion());
		
		return jsonObj;
	}

}
