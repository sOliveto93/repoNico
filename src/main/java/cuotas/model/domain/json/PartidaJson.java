package cuotas.model.domain.json;

import net.sf.json.JSONObject;

import java.util.Iterator;

import common.model.domain.json.GenericJson;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.Partida;

public class PartidaJson extends GenericJson<Partida> {

	@Override
	public Partida fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JSONObject getJson(Partida partida) {
		JSONObject jsonObj = new JSONObject();

		jsonObj.put("id",partida.getId());
		jsonObj.put("descripcion",partida.getDescripcion());
		if(partida.getGrupos()!=null){
			Iterator<Grupo> gIt = partida.getGrupos().iterator();
			/*while(gIt.hasNext()){
				Grupo p = gIt.next();
				System.out.println("ID: "+ p.getId() + " Nombre: " + p.getDescripcion());
			}*/
			jsonObj.put("grupos", FactoryJson.grupoJson.getJsonLight(gIt));
		}
		
		return jsonObj;
	}

}
