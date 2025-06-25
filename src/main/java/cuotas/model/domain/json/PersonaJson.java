package cuotas.model.domain.json;

import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import common.model.domain.json.GenericJson;
import cuotas.model.domain.datos.Inscripcion;
import cuotas.model.domain.datos.Pago;
import cuotas.model.domain.datos.Persona;

public class PersonaJson extends GenericJson<Persona> {

	@Override
	public Persona fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public JSONObject getJsonLight(Persona persona){
		
		String tipoDocumento = "", numeroDocumento = "";
		if(persona.getDocumento() != null){
			numeroDocumento = persona.getDocumento().getNumero();
			if(persona.getDocumento().getTipo() != null)
				tipoDocumento = persona.getDocumento().getTipo().getAbreviacion();
		}
		
		String telefono = "";
		if(persona.getTelefono() !=null)
			telefono = persona.getTelefono().getNumero();
				
		JSONObject personaJson = new JSONObject();
		personaJson.put("id", persona.getId());
		personaJson.put("nombreCompleto", persona.getNombreCompleto());
		personaJson.put("nombre",persona.getNombre());
		personaJson.put("apellido",persona.getApellido());
		personaJson.put("tipoDocumento",tipoDocumento);
		personaJson.put("numeroDocumento",numeroDocumento);
		personaJson.put("telefono",telefono);
		personaJson.put("carrerasActiva", persona.getCarrerasActiva());
		personaJson.put("estadoPersona", persona.getEstado());
		personaJson.put("fondoBeca1", persona.getFondoBeca1());
		//Iterator<Inscripcion> inscripciones = persona.getInscripciones().iterator();
		personaJson.put("mail", persona.getMail());
		personaJson.put("inscripciones", FactoryJson.inscripcionesJson.toJson(persona.getInscripciones().iterator()));
		
		return personaJson;
	}	
	
	//FIXME - Deberiamos agregar este metodo al GenericJson para poder obtener versiones reducidas de los objetos
	//		  con los campos mas importantes.
	public JSONArray getJsonLight(Iterator<Persona> objetos){
		JSONArray arrayJson = new JSONArray();
		
		Persona objeto = null;
		while(objetos!=null && objetos.hasNext()){
			objeto = objetos.next();
			arrayJson.add(getJsonLight(objeto));
		}		
		return arrayJson;
	}

	@Override
	protected JSONObject getJson(Persona persona) {
		//La inicializo asi porque si no da error.
		String domicilio="";

		JSONObject jsonObj = getJsonLight(persona);
		
		jsonObj.put("numeroOrden",persona.getId());
		if(persona.getDireccion()!=null)
			domicilio = FactoryJson.domicilioJson.toJson(persona.getDireccion());
		jsonObj.put("direccion",domicilio);
		if(persona.getFechaIngreso()!=null)
			jsonObj.put("fechaIngreso", persona.getFechaIngreso().getFechaFormateada("dd-MM-yyyy"));
		

		JSONArray inscripciones = new JSONArray();		
		Iterator<Inscripcion> inscripcionesIt =persona.getInscripciones().iterator(); 
		while(inscripcionesIt.hasNext()){
			inscripciones.add(FactoryJson.inscripcionesJson.toJson(inscripcionesIt.next()));				
		}
		jsonObj.put("inscripciones",inscripciones);
		
		JSONArray cuotas = new JSONArray();
		Iterator<Pago> cuotasIt = persona.getPagos().iterator();
		while(cuotasIt.hasNext()){
			cuotas.add(FactoryJson.pagosJson.toJson(cuotasIt.next()));
		}
		jsonObj.put("cuotas", cuotas);
		jsonObj.put("nombreCompleto",persona.getNombreCompleto());
		jsonObj.put("estadoPersona", persona.getEstado());
		return jsonObj;
	}

}
