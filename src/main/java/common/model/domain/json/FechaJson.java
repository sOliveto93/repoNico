package common.model.domain.json;

import net.sf.json.JSONObject;
import common.model.domain.fecha.Fecha;

public class FechaJson extends GenericJson<Fecha> {

	@Override
	protected JSONObject getJson(Fecha fecha) {
		JSONObject fechaJson = new JSONObject();
		
		String fechaStr = "",  diaStr = "";
		
		if(fecha != null){
			fechaStr = fecha.getFechaFormateada(Fecha.FORMATO_BARRAS);
			diaStr = fecha.getDiaNombre();
		}
		
		fechaJson.put("fecha", fechaStr);
		fechaJson.put("dia", diaStr);
		return fechaJson;
	}

	@Override
	public Fecha fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}
