package cuotas.model.domain.json;

import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import common.model.domain.json.GenericJson;
import cuotas.model.domain.factura.FacturaItemTipo;
import cuotas.model.domain.factura.RubroCobro;

public class RubroCobroJson extends GenericJson<RubroCobro> {

	@Override
	public RubroCobro fromJson(String json){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JSONObject getJson(RubroCobro rubroCobro) {
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("id", rubroCobro.getId());
		jsonObj.put("codigo", rubroCobro.getCodigo());
		jsonObj.put("descripcion", rubroCobro.getDescripcion());
		
		Iterator<FacturaItemTipo> tiposRubroCobro = rubroCobro.getFacturaItemTipo().iterator();
		
		JSONArray lista = new JSONArray();
		while(tiposRubroCobro.hasNext()){
			lista.add(FactoryJson.facturaItemTipoJson.getJson(tiposRubroCobro.next()));			
		}
		jsonObj.put("tiposRubroCobro",lista );
		return jsonObj;
	}

	
}
