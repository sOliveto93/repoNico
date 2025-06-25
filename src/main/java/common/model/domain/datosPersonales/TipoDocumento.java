package common.model.domain.datosPersonales;

import common.model.domain.datos.Estado;


public class TipoDocumento implements java.io.Serializable{

	private static final long serialVersionUID = -7054033472813719560L;
	private Integer id;
	private String nombre;
	private String abreviacion;
	private String mascara;
	private Estado estado;
	
	public TipoDocumento(){
		this.estado = new Estado(Estado.ACTIVO);
	}

	public TipoDocumento(String abreviacion){
		this.abreviacion = abreviacion;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getAbreviacion() {
		return abreviacion;
	}

	public void setAbreviacion(String abreviacion) {
		this.abreviacion = abreviacion;
	}

	public String getMascara() {
		return mascara;
	}

	public void setMascara(String mascara) {
		this.mascara = mascara;
	}
	

	public Boolean isValido(Documento doc){
		//FIXME hay que implementarlo 
		return true;
	}
	
	public String toString(){
		return this.abreviacion ;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	/**
	 * Un TipoDocumento es igual a otro si tiene la misma abreviacion.
	 */
	public boolean equals(Object o){
		boolean res = false;
		
		try{
			
			TipoDocumento td = (TipoDocumento)o;
			if(td.getAbreviacion().equals(this.getAbreviacion()))
				res = true;
			
		}catch(Exception e){
			res = false;
		}
		return res;
	}
}
