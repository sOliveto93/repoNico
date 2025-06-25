package common.model.domain.datosPersonales;

import common.model.domain.error.ErrorParametro;


public class Documento implements java.io.Serializable {
	
	private static final long serialVersionUID = 7063009942547115855L;

	private Integer id;
	private TipoDocumento tipo;
	private String numero;

	public Documento() {
	}
	
	
	
	public Documento(String numero, TipoDocumento tipo) {
		this.tipo = tipo;
		this.setNumero(numero);
		
		if(tipo != null && ! tipo.isValido(this))
			throw new ErrorParametro("El formato del documento no es valido.");
	}


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer idTipoDocumento) {
		this.id = idTipoDocumento;
	}


	public TipoDocumento getTipo() {
		return tipo;
	}

	public void setTipo(TipoDocumento tipoDocumento) {
		this.tipo = tipoDocumento;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero.replaceAll("\\.", "");
		//this.numero = numero;
	}
	
	public String getNumeroFormateado(){
		// TODO formatear el numero usando la mascara del TipoDocumento.
		return stringToDocumento(this.numero);
	}
	
	/**
	 * Recibe un string del tipo 3456789 y retorna 03.456.789
	 */
	public static String stringToDocumento(String s){
		
		if(s==null) 
			return "00.000.000";
		
		
		
		int docLong =s.length();
		String docAux = s.replaceAll("\\.", "");
		if(docLong < 8){
			while(s.length()<8)s="0"+s;			
		}
		docLong =s.length();
		docAux =s.substring(0,docLong-6)+"."+s.substring(docLong-6,docLong-3)+"."+s.substring(docLong-3);
		
		return docAux;
	}
	
	/**
	 * Recibe un string del tipo 03.456.789 y retorna 3456789
	 */
	public static String documentoToString(String doc){
		while(doc.charAt(0)=='0')doc=doc.substring(1);
		doc = doc.replace(".","");
		return doc;
	}

	
	public String getTipoyNumeroFormateado(){
		String res="";
		
		if(this.getTipo()!= null)
			res = this.getTipo().getAbreviacion() + " - ";
		
		res = res.concat(this.getNumeroFormateado());
		return res;
		
	}
	
	public String toString(){
		return stringToDocumento(numero);
	
	}
}
