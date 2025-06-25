package common.model.domain.json;

import net.sf.json.JSONObject;
import common.model.domain.datosPersonales.Documento;
import common.model.domain.datosPersonales.TipoDocumento;

public class DocumentoJson extends GenericJson<Documento> {

	@Override
	public Documento fromJson(String json) {
		// Sin implementar
		return null;
	}
	
	public String tipoDocumentoJson(TipoDocumento tipoDocumento){
		JSONObject tipoDocumentoJson = null;
		String salida = null ;
		if(tipoDocumento != null){
			tipoDocumentoJson = new JSONObject();
			tipoDocumentoJson.put("abreviacion", tipoDocumento.getAbreviacion());
			tipoDocumentoJson.put("nombre", tipoDocumento.getNombre());
			tipoDocumentoJson.put("id", tipoDocumento.getId());
			tipoDocumentoJson.put("mascara", tipoDocumento.getMascara());
			salida = tipoDocumentoJson.toString();
		}		
		return salida;
	}


	@Override
	protected JSONObject getJson(Documento documento) {
		JSONObject documentoJson = null;
		
		if(documento != null){
			documentoJson = new JSONObject();
			documentoJson.put("id", documento.getId());
			documentoJson.put("numero", documento.getNumeroFormateado());			
			documentoJson.put("tipoDocumento", tipoDocumentoJson(documento.getTipo()));		
		}
		return documentoJson;
	}
	
}
