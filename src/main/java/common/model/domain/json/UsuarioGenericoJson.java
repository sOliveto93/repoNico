package common.model.domain.json;

import common.model.domain.validacion.UsuarioGenerico;
import net.sf.json.JSONObject;

public class UsuarioGenericoJson<USUARIO extends UsuarioGenerico> extends PersonaGenericaJson<USUARIO> {

	@Override
	protected JSONObject getJson(USUARIO usuario) {
		JSONObject usuarioJson = super.getJson(usuario);		
		usuarioJson.putAll(getJsonPropio(usuario));
		return usuarioJson;
		
	}
	
	/**
	 * Incluye una version reducida de los datos del usuario.
	 * [id, nombreCompleto, usuario, password, email] 
	 * @param usuario
	 * @return
	 */
	protected JSONObject getJsonLight(USUARIO usuario) {
		JSONObject usuarioJson = new JSONObject();	
		if(usuario != null){
			usuarioJson.put("nombreCompleto",usuario.getNombreCompleto());
			usuarioJson.putAll(getJsonPropio(usuario));
		}
		
		return usuarioJson;
	}
	
	/**
	 * Contiene atributos propios de la clase usuario (usuario, password, email)
	 * @param usuario
	 * @return
	 */
	protected JSONObject getJsonPropio(USUARIO usuario) {
		JSONObject usuarioJson = new JSONObject();
		if(usuario != null){
			usuarioJson.put("id", usuario.getId());
			usuarioJson.put("usuario", usuario.getUsuario());
			usuarioJson.put("password", usuario.getPassword());
			usuarioJson.put("email", usuario.getEmail().getEmail());
		}
		return usuarioJson;
	}

	@Override
	public USUARIO fromJson(String json) {
		// TODO sin implementar aun
		return null;
	}

}
