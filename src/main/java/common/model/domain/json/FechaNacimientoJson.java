package common.model.domain.json;

import net.sf.json.JSONObject;

import common.model.domain.fecha.FechaNacimiento;

public class FechaNacimientoJson extends FechaJson {

	
	protected JSONObject getJson(FechaNacimiento fecha) {
		JSONObject fechaJson = super.getJson(fecha);
		
		String edadStr = "";
		if(fecha != null)
			edadStr = fecha.getEdad().toString();
		
		fechaJson.put("edad", edadStr);
		return fechaJson;
	}
}
