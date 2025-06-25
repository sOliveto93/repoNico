package cuotas.model.domain.json;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Iterator;

import common.model.domain.json.GenericJson;
import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.Persona;

public class GrupoJson extends GenericJson<Grupo> {

	@Override
	public Grupo fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JSONObject getJson(Grupo grupo) {
		JSONObject jsonObj = getJsonLight(grupo);

		Integer vtoDias = 0;
		if(grupo.getVtoDias()!=null)
			vtoDias = grupo.getVtoDias();
		Integer vtoPlus = 0;
		if(grupo.getVtoPlus()!=null)
			vtoPlus = grupo.getVtoPlus();
		Double vtoMonto = 0.0;
		if(grupo.getVtoMonto()!=null) 
			vtoMonto = grupo.getVtoMonto(); 
		Double conceptoUnoMonto,conceptoDosMonto; 
		if(grupo.getConceptoUnoMonto()!=null)
			conceptoUnoMonto =grupo.getConceptoUnoMonto();
		else
			conceptoUnoMonto = 0.0;
	
		if(grupo.getConceptoDosMonto()!=null)
			conceptoDosMonto =grupo.getConceptoDosMonto();
		else			
			conceptoDosMonto = 0.0;

		jsonObj.put("vencimientosDias", vtoDias);
		jsonObj.put("segundoVencimientosDias", vtoPlus);
		jsonObj.put("vtoMonto",vtoMonto);
		jsonObj.put("conceptoUnoMonto",conceptoUnoMonto);	
		jsonObj.put("conceptoDosMonto",conceptoDosMonto );
		if(grupo.getFacturaItemTipo()!=null){
			jsonObj.put("facturaItemTipos", grupo.getFacturaItemTipo().getId());	
		}else{
			jsonObj.put("facturaItemTipos", "");
		}
		
		jsonObj.put("grupoCuotas", FactoryJson.cuotaJson.toJson(grupo.getCuotas().iterator()));
		return jsonObj;
	}
	//FIXME - Deberiamos agregar este metodo al GenericJson para poder obtener versiones reducidas de los objetos
		//		  con los campos mas importantes.
		public JSONArray getJsonLight(Iterator<Grupo> objetos){
			JSONArray arrayJson = new JSONArray();
			
			Grupo objeto = null;
			while(objetos!=null && objetos.hasNext()){
				objeto = objetos.next();
				arrayJson.add(getJsonLight(objeto));
			}		
			return arrayJson;
		}
	
	public JSONObject getJsonLight(Grupo grupo){
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("id", grupo.getId());
		jsonObj.put("codigo", grupo.getCodigo());
		jsonObj.put("descripcion", grupo.getDescripcion());
		jsonObj.put("anioCuota", grupo.getAnioCuota());
		jsonObj.put("tipoInforme", grupo.getTipoInforme());
		jsonObj.put("conceptoNumeroUno", grupo.getConcepto1());
		jsonObj.put("conceptoNumeroDos", grupo.getConcepto2());
		jsonObj.put("carrera", grupo.getCarrera().getId());
		jsonObj.put("nombreCarrera", grupo.getCarrera().getNombreCarrera());
		jsonObj.put("nombreTitulo", grupo.getNombreTitulo());
		jsonObj.put("controlPago", grupo.getCtrlPago());
		
		return jsonObj;
	}
}
