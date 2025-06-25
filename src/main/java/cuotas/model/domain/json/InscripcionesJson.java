package cuotas.model.domain.json;

import net.sf.json.JSONObject;
import common.model.domain.json.GenericJson;
import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.model.domain.datos.Inscripcion;
import cuotas.model.domain.datos.Persona;
import cuotas.model.domain.datos.PersonasBeca;
//import cuotas.model.domain.datos.PersonasBeca;

public class InscripcionesJson extends GenericJson<Inscripcion> {

	@Override
	public Inscripcion fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*public JSONObject getJsonLight(Inscripcion inscripcion){
		JSONObject inscripcionJson = new JSONObject();
		inscripcionJson = getJson(inscripcion);
		return inscripcionJson;
	}*/
	
	@Override
	protected JSONObject getJson(Inscripcion ins) {
		JSONObject jsonObjet = new JSONObject();
		jsonObjet.put("idCarrera",ins.getCarrera().getId());
		jsonObjet.put("nombreCarrera",ins.getCarrera().getNombreCarrera());
		jsonObjet.put("estadoCarrera",ins.getCarrera().getEstado());
		jsonObjet.put("tipoCarrera",ins.getCarrera().getTipo());
		jsonObjet.put("idPersona",ins.getPersona().getId());
		
		PersonasBeca personasBeca = ins.getPersonasBeca();
		if(personasBeca!=null){
				jsonObjet.put("personasBeca",FactoryJson.personasBecaJson.getJson(personasBeca));
		}else{
				jsonObjet.put("personasBeca","");
		}
		/*Integer descuento = 0;
		if(ins.getPersonasBeca().getDescuentoBeca()!=null){
			descuento = ins.getPersonasBeca().getDescuentoBeca();
		}
		jsonObjet.put("descuento",descuento);
		*/
		
		return jsonObjet;
	}
}
