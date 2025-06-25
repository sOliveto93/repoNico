package cuotas.model.domain.json;

import net.sf.json.JSONObject;
import common.model.domain.json.GenericJson;
import cuotas.model.domain.datos.Cuota;

public class CuotaJson extends GenericJson<Cuota> {

	@Override
	public Cuota fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JSONObject getJson(Cuota cuota) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", cuota.getId());
		jsonObj.put("tipo", cuota.getTipo().getLetra());
		jsonObj.put("mes", cuota.getMes());
		Double montoUno=0.0, montoDos =0.0;
		if(cuota.getMonto1()!= null){
			montoUno = cuota.getMonto1();
		}
		if(cuota.getMonto2()!= null){
			montoDos = cuota.getMonto2();
		}
		jsonObj.put("montoUno", montoUno);
		jsonObj.put("montoDos", montoDos);
		
		//String grupoJson = FactoryJson.grupoJson.toJson(cuota.getGrupo());
		//jsonObj.put("grupo",grupoJson);
		return jsonObj;
	}
}
