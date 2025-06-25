package cuotas.model.domain.json;

import java.util.Iterator;

import net.sf.json.JSONObject;
import common.model.domain.json.GenericJson;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.factura.FacturaItemTipo;

public class FacturaItemTipoJson extends GenericJson<FacturaItemTipo> {

	@Override
	public FacturaItemTipo fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JSONObject getJson(FacturaItemTipo facturaItemTipo) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", facturaItemTipo.getId());
		jsonObj.put("codigo",facturaItemTipo.getCodigo());
		jsonObj.put("partida",FactoryJson.partidaJson.getJson(facturaItemTipo.getPartida()));
		jsonObj.put("descripcion", facturaItemTipo.getDescripcion());
		jsonObj.put("estado", facturaItemTipo.getEstado().toString());
		if(facturaItemTipo.getGrupos()!=null){
			
			Iterator<Grupo> grupos = facturaItemTipo.getGrupos().iterator();
		/*	while(grupos.hasNext()){
				Integer i=1;
				Grupo grupo = grupos.next();
				System.out.println("gruposNombre: "+ grupo.getNombreTitulo() + " GrupoID: "+ grupo.getId());
				System.out.println("I: " + i);
				i++;
			}*/
			jsonObj.put("grupos", FactoryJson.grupoJson.getJson(grupos));
		}
		jsonObj.put("precio", facturaItemTipo.getPrecio());
		
		jsonObj.put("rubroId", facturaItemTipo.getRubroCobro().getId());
		jsonObj.put("rubro", facturaItemTipo.getRubroCobro().getCodigo());
		jsonObj.put("rubroDescripcion", facturaItemTipo.getRubroCobro().getDescripcion());
		
		return jsonObj;
	}

}
