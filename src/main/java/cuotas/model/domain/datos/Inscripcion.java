package cuotas.model.domain.datos;

import common.model.domain.datos.GenericBean;
import common.model.domain.fecha.Fecha;


public class Inscripcion extends GenericBean<Long> implements java.io.Serializable {
	private static final long serialVersionUID = 474397978739681247L;

	private Carrera carrera;
	private Persona persona;
	private Fecha fechaHoraInscripcionIngreso;
	private Character estado;
	private Integer cohorte;
	private Long NumeroCarrera;
	private PersonasBeca personasBeca;
	
	public Inscripcion() {
		cohorte = new Integer(0);
	}


	public Inscripcion(Integer cohorte) {
		this.cohorte = cohorte;
	}

	public Inscripcion(Carrera carrera, Persona persona,
			Fecha fechaHoraInscripcionIngreso, Character estado, Integer cohorte) {
		this.carrera = carrera;
		this.persona = persona;
		this.fechaHoraInscripcionIngreso = fechaHoraInscripcionIngreso;
		this.estado = estado;
		this.cohorte = cohorte;
	}

	public Carrera getCarrera() {
		return this.carrera;
	}

	public void setCarrera(Carrera carrera) {
		this.carrera = carrera;			
	}

	public Persona getPersona() {
		return this.persona;	
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	
	public Fecha getFechaHoraInscripcionIngreso() {
		return this.fechaHoraInscripcionIngreso;
	}

	public void setFechaHoraInscripcionIngreso(Fecha fechaHoraInscripcionIngreso) {
		this.fechaHoraInscripcionIngreso = fechaHoraInscripcionIngreso;
	}

	public Character getEstado() {
		return this.estado;
	}

	public void setEstado(Character estado) {
		this.estado = estado;
	}

	public Integer getCohorte() {
		return this.cohorte;
	}

	public void setCohorte(Integer cohorte) {
		this.cohorte = cohorte;
	}
	
	public Long getNumeroCarrera() {
		return NumeroCarrera;
	}

	public void setNumeroCarrera(Long numeroCarrera) {
		NumeroCarrera = numeroCarrera;
	}


	public int hashCode(){
		if(this.getId() != null)
			return this.getId().hashCode();
		else
			return 0;
	}
	
	public boolean equals(Object object){
		Inscripcion otraInscripcion = (Inscripcion) object;
		Boolean resultado = false;
		
		if(this.getId() != null)
			resultado = this.getId().equals(otraInscripcion.getId());		
		
		return resultado;
	}
	public String getCarrerasActivas(){
		String respuesta = null;
		if(persona.getCarrerasActiva()!=null){
			respuesta =persona.getCarrerasActiva();
		}
		return respuesta;
	}

	public PersonasBeca getPersonasBeca() {
		return personasBeca;
	}

	public void setPersonasBeca(PersonasBeca personaBeca) {
		this.personasBeca = personaBeca;
	}
	public void removePersonaBeca(){
		this.personasBeca = null;
	}

}
