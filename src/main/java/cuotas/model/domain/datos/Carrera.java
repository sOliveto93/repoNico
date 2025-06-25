package cuotas.model.domain.datos;

import java.util.HashSet;
import java.util.Set;


import common.model.domain.datos.Estado;
import common.model.domain.datos.GenericBean;
import common.model.domain.error.ErrorParametro;
import common.model.domain.fecha.Fecha;
import common.model.domain.fecha.RangoFecha;

public class Carrera extends GenericBean<Long>  implements java.io.Serializable {
	private static final long serialVersionUID = 7755020536696307510L;
	
	private String nombreCarrera;
	private String nombreTitulo;
	private Integer codigo;
    private String tipo;
	private RangoFecha vigencia ;
	private Set<Inscripcion> inscripciones = new HashSet<Inscripcion>(0);
	private Set<Grupo> grupos = new HashSet<Grupo>(0);
	private Set<CarreraDep> carrerasDep = new HashSet<CarreraDep>(0);
	
	

	private Estado estado;
	
	public Carrera() {
		super();
		this.estado = new Estado(Estado.ACTIVO);
	}

	public Carrera(String nombreCarrera, String nombreTitulo,
			Fecha vigenciaCarreraDesde,
			Fecha vigenciaCarreraHasta, Set<Inscripcion> inscripcion, Integer codigo, String tipo) {
		this.nombreCarrera = nombreCarrera;
		this.nombreTitulo = nombreTitulo;
		this.inscripciones = inscripcion;
		this.estado = new Estado(Estado.ACTIVO);
		this.codigo = codigo;
		this.tipo = tipo;
	}

	public String getNombreCarrera() {
		return this.nombreCarrera;
	}

	public void setNombreCarrera(String nombreCarrera) {
		if(!this.deboEjecutarValidaciones() || nombreCarrera.length()< 255){
			this.nombreCarrera = nombreCarrera;	
		}else{
			throw new ErrorParametro("Nombre de la Carrera invalida. \\n El nombre execedio los 255 caracteres");
		}
	}
	public String getNombreTitulo() {
		return this.nombreTitulo;
	}

	public void setNombreTitulo(String nombreTitulo) {
		if(!this.deboEjecutarValidaciones() || nombreTitulo.length()< 255){
			this.nombreTitulo = nombreTitulo;	
		}else{
			throw new ErrorParametro("Nombre del Titulo invalido. \\n El titulo execedio los 255 caracteres");
		}
	}

	public Set<Inscripcion> getInscripciones() {
		return this.inscripciones;
	}

	public void setInscripciones(Set<Inscripcion> inscripciones) {
		this.inscripciones = inscripciones;
	}
	
	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public void addInscripcione(Inscripcion inscripcion){
		this.inscripciones.add(inscripcion);
	}
	public void removeInscripcion(Inscripcion inscripcion){
		this.inscripciones.remove(inscripcion);
	}
	
	public int hashCode(){
		if(this.getId() != null)
			return this.getId().hashCode();
		else
			return 0;
	}
	
	public boolean equals(Object object){
		Carrera otraCarrera = (Carrera) object;
		return this.getId().equals(otraCarrera.getId());
	}
	
	public String toString(){
		String salida = "";
		if(getId()!=null){
			salida = salida + "ID: "+getId();
		}
		salida = salida + " - Carrera "+getNombreCarrera()+ " con titulo "+getNombreTitulo();
		return salida;
	}

	public RangoFecha getVigencia() {
		return vigencia;
	}

	public void setVigencia(RangoFecha vigencia) {
		this.vigencia = vigencia;
	}

	public Set<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(Set<Grupo> grupos) {
		this.grupos = grupos;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public String getInformacionAuditoria() {		
		return toString();
	}

	public Set<CarreraDep> getCarrerasDep() {
		return carrerasDep;
	}

	public void setCarrerasDep(Set<CarreraDep> carrerasDep) {
		this.carrerasDep = carrerasDep;
	}
	
}