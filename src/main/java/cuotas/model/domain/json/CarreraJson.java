package cuotas.model.domain.json;

import net.sf.json.JSONObject;
import common.model.domain.json.GenericJson;
import cuotas.model.domain.datos.Carrera;

public class CarreraJson extends GenericJson<Carrera> {

	@Override
	public Carrera fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JSONObject getJson(Carrera carrera) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", carrera.getId());
		jsonObj.put("nombreCarrera",carrera.getNombreCarrera());
		jsonObj.put("nombreTitulo",carrera.getNombreTitulo());
		
		String vigenciaDesde="";
		if(carrera.getVigencia()!= null){
			vigenciaDesde =carrera.getVigencia().getDesde().getFechaFormateada(); 
		}
		jsonObj.put("vigenciaDesde",vigenciaDesde);

		String vigenciaHasta="";
		if(carrera.getVigencia()!= null){
				if(carrera.getVigencia().getHasta()!=null)
					vigenciaHasta=carrera.getVigencia().getHasta().getFechaFormateada();
		}
		jsonObj.put("vigenciaHasta",vigenciaHasta);
		
		jsonObj.put("codigo",carrera.getCodigo());
		jsonObj.put("estado",carrera.getEstado());
		return jsonObj;
	}

}
