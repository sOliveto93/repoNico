package cuotas.model.domain.datos;

import common.model.domain.datos.GenericBean;

public class CarreraDep extends GenericBean<Integer>  implements java.io.Serializable {
	private static final long serialVersionUID = 6842559567590979415L;
	
	private Integer id;
	private Integer idDep;
	private Carrera carrera;
	private String descripcionDep;
	
	public CarreraDep (){		
	}	
	public CarreraDep (int idDep, String descripcion){
		this.idDep = idDep;
		this.descripcionDep = descripcion;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIdDep() {
		return idDep;
	}
	public void setIdDep(Integer idDep) {
		this.idDep = idDep;
	}
	public String getDescripcionDep() {
		return descripcionDep;
	}
	public void setDescripcionDep(String descripcionDep) {
		this.descripcionDep = descripcionDep;
	}
	public Carrera getCarrera() {
		return carrera;
	}
	public void setCarrera(Carrera carrera) {
		this.carrera = carrera;
	}

}
