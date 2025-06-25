package cuotas.model.domain.datos;

import common.model.domain.datos.GenericBean;

public class MapeoTipoDocumento extends GenericBean<Long> implements java.io.Serializable {
	private static final long serialVersionUID = 6970047077884295793L;
	
	private String tipoDocumentoCuotas;
	private String tipoDocumentoCursos;
	private String tipoDocumentoGuarani;
	
	public String getTipoDocumentoCuotas() {
		return tipoDocumentoCuotas;
	}
	public void setTipoDocumentoCuotas(String tipoDocumentoCuotas) {
		this.tipoDocumentoCuotas = tipoDocumentoCuotas;
	}
	public String getTipoDocumentoCursos() {
		return tipoDocumentoCursos;
	}
	public void setTipoDocumentoCursos(String tipoDocumentoCursos) {
		this.tipoDocumentoCursos = tipoDocumentoCursos;
	}
	public String getTipoDocumentoGuarani() {
		return tipoDocumentoGuarani;
	}
	public void setTipoDocumentoGuarani(String tipoDocumentoGuarani) {
		this.tipoDocumentoGuarani = tipoDocumentoGuarani;
	}
}
