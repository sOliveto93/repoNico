package common.model.domain.json;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public abstract class GenericJson<T> {

	protected abstract  JSONObject getJson(T objeto);
	
	protected JSONObject getJsonLight(T objeto){
		//System.out.println("NO IMPLEMENTADO, DEBE IMPLEMENTAR EN CADA SUBCLASE");
		return null;
	}
	
	//public abstract  String toJson(T objeto);
	
	public abstract T fromJson(String json);
	
	protected static final String REEPLAZAME = "REPLACE_ME";
	
	/**
	 * Responde con un string que contiene el objeto convertido en formato JSON (sin encabezado de "status" y "lista")
	 * @param objeto
	 * @return
	 * 
	 */
	
	public String toJson(T objeto){
		return toJson(objeto,false);
	}
	
	/**
	 * Responde con un string que contiene el objeto convertido en formato JSON pero dentro del encabezado con "status" y "lista", listo para responder al request
	 * @param objeto
	 * @return
	 */
	public String toJsonAnswer(T objeto){
		return toJson(objeto,true);
	}
	
	/**
	 * Responde con un string que contiene el objeto convertido en formato JSON pero dentro del encabezado con "status" y "lista", listo para responder al request
	 * @param objeto
	 * @return
	 */
	
	public String toJsonAnswer(Iterator<T> objetos){
		return toJson(objetos,true);
	}
	
	public String toJsonLight(T objeto, boolean incluirEsqueleto){
		return toJson(objeto, incluirEsqueleto, true);
	}
	
	public String toJson(T objeto, boolean incluirEsqueleto){
		return toJson(objeto, incluirEsqueleto, false);
	}
	/**
	 * Responde con un string que contiene el objeto convertido en formato JSON , 
	 * segun el segundo parametro se incluye o no el encabezado (esqueleto) con "status" y "lista"
	 * @param objeto
	 * @param incluirEsqueleto
	 * @return
	 */ 
	public String toJson(T objeto, boolean incluirEsqueleto, boolean isLight){
		String salida = null;
		JSONObject objetoJson = null;
		
		if(objeto != null)
			if(isLight)
				objetoJson = getJsonLight(objeto);
			else
				objetoJson = getJson(objeto);
		else
			objetoJson = new JSONObject();
		
		if(incluirEsqueleto)
			salida = getEsqueletoJson(objetoJson).toString();
		else
		    salida = objetoJson.toString();		
		
		return salida ;
	}
	
	public String toJson(Iterator<T> objetos){
		return this.toJson(objetos,false);
	}
	
	public String toJsonLight(Iterator<T> objetos){
		return this.toJson(objetos,true);
	}
	
	public String toJson(Iterator<T> objetos,boolean isJsonLight ){
		return this.toJson(objetos,false, isJsonLight);
	}
	
	public String toJson(Iterator<T> objetos, boolean incluirEsqueleto,boolean isJsonLight){
		
		String salida = null;		
		JSONArray arrayJson = getJson(objetos, isJsonLight);
		
		if(incluirEsqueleto)
			salida = getEsqueletoJson(arrayJson).toString();
		else
		    salida = arrayJson.toString();		
		
		return salida ;
	}
	
	public String toJson(List<T> objetos){
		return this.toJson(objetos,false);
	}
	
	public String toJsonLight(List<T> objetos){
		return this.toJson(objetos,true);
	}
	
	public String toJson(List<T> objetos, boolean isJsonLight){
		return this.toJson(objetos,false,isJsonLight);
	}
	
	
	public String toJson(List<T> objetos, boolean incluirEsqueleto, boolean isJsonLight){
		return this.toJson(objetos.iterator(), incluirEsqueleto, isJsonLight);
	}
	
	/**/
	
	public JSONArray getJson(Iterator<T> objetos, boolean isJsonLight){
		JSONArray arrayJson = new JSONArray();
		
		T objeto = null ;
		while(objetos!=null && objetos.hasNext()){
			objeto = objetos.next();
			if(isJsonLight){
				arrayJson.add(getJsonLight(objeto));
			}else{
				arrayJson.add(getJson(objeto));
			}
		}		
		return arrayJson;
	}
	
	public JSONArray getJson(Iterator<T> objetos){
		return this.getJson(objetos, false);
	}
	
	public JSONArray getJson(List<T> objetos){
		return this.getJson(objetos.iterator(), false);
	}
	
	protected String getEsqueletoJson(){
		return "{\"status\":\"OK\",\"lista\":["+GenericJson.REEPLAZAME+"]}";
	}
	
	public String getEsqueletoJson(String completarCon){
		
		if(completarCon == null)
			completarCon = "";
		completarCon = Matcher.quoteReplacement(completarCon);
		return getEsqueletoJson().replaceAll(REEPLAZAME, completarCon);
	}
	
	public String quitarComaInicial(String cadena){
		
		if(cadena != null && cadena.length() > 1)
			cadena = cadena.substring(1);
		
		return cadena ;
	}
	
	public JSONObject getEsqueletoJson(JSONObject objetoJson){

		JSONArray arrayJson = new JSONArray();
		arrayJson.add(objetoJson);		
		return getEsqueletoJson(arrayJson);
	}
	
	public JSONObject getEsqueletoJson(JSONArray jsonLista){

		JSONObject jsonRespuesta = new JSONObject();
		jsonRespuesta.put("status", "OK");		
		jsonRespuesta.put("lista", jsonLista);
		
		return jsonRespuesta;
	}	
	
}
