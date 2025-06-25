package common.model.domain.json;

import net.sf.json.JSONObject;

import common.model.domain.datos.PersonaGenerica;
import common.model.domain.fecha.Fecha;



public abstract class PersonaGenericaJson<PERSONA extends PersonaGenerica> extends GenericJson<PERSONA> {

	private static final DocumentoJson documentoJson;
	private static final DomicilioJson direccionJson;
	private static final TelefonoJson telefonoJson;
	private static final FechaNacimientoJson fechaNacimientoJson;
	static {
		documentoJson = new DocumentoJson();
		direccionJson = new DomicilioJson();
		telefonoJson = new TelefonoJson();
		fechaNacimientoJson = new FechaNacimientoJson();
	}
	
	protected JSONObject getJson(PERSONA persona) {

		JSONObject personaGenericaJson = null;
		if(persona != null){
			personaGenericaJson = new JSONObject();
			personaGenericaJson.put("id", persona.getId());
			personaGenericaJson.put("nombre",persona.getNombre());
			personaGenericaJson.put("apellido",persona.getApellido());
			personaGenericaJson.put("documento", documentoJson.toJson(persona.getDocumento()));
			personaGenericaJson.put("direccion", direccionJson.toJson(persona.getDireccion()));
			personaGenericaJson.put("telefono",telefonoJson.toJson(persona.getTelefono()));
			personaGenericaJson.put("celular",telefonoJson.toJson(persona.getCelular()));
			personaGenericaJson.put("telParticular",telefonoJson.toJson(persona.getTelParticular()));
			personaGenericaJson.put("otroTelefono",telefonoJson.toJson(persona.getOtroTelefono()));
			personaGenericaJson.put("fax",telefonoJson.toJson(persona.getFax()));
			
			String email = "";
			if(persona.getEmail() != null)
				email = persona.getEmail().getEmail();
			
			personaGenericaJson.put("email",email);
	
			/*String fechaNac = "";
			if(persona.getFechaNac() != null)
					fechaNac = persona.getFechaNac().getFechaFormateada(Fecha.FORMATO_BARRAS);
			*/
			
			personaGenericaJson.put("fechaNacimiento",fechaNacimientoJson.getJson(persona.getFechaNac()));
			personaGenericaJson.put("notas",persona.getNotas());
		}
			
		return personaGenericaJson;
	}
	
	protected JSONObject getJsonLight(PERSONA persona) {
		JSONObject personaGenericaJson = new JSONObject();
		if(persona != null){
			personaGenericaJson.put("id", persona.getId());
			personaGenericaJson.put("nombre",persona.getNombre());
			personaGenericaJson.put("apellido",persona.getApellido());
			
			String email = "";
			if(persona.getEmail() != null)
				email = persona.getEmail().getEmail();
			
			personaGenericaJson.put("email",email);
		}
		return personaGenericaJson;
	}
	
	@Override
	public PERSONA fromJson(String json) {
		// TODO Sin implementar para las PERSONAS
		return null;
	}
}
