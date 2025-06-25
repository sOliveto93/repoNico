package cuotas.model.domain.datos;

import java.util.HashSet;
import java.util.Set;

import common.model.domain.datos.GenericBean;
import common.model.domain.error.ErrorParametro;

/**
 * @author jforastier
 *
 */
public class PersonasBeca  extends GenericBean<Long>  implements java.io.Serializable {
	private static final long serialVersionUID = 5124478989834044731L;
	
	private Beca beca;
	private Integer descuentoBeca;
	private Inscripcion inscripcion;
	
	
	public Beca getBeca() {
		return beca;
	}
	public void setBeca(Beca beca) {
		this.beca = beca;
	}
	public Integer getDescuentoBeca() {
		return descuentoBeca;
	}
	public void setDescuentoBeca(Integer descuentoBeca) {
		this.descuentoBeca = descuentoBeca;
	}
	public Inscripcion getInscripcion() {
		return inscripcion;
	}
	public void setInscripcion(Inscripcion inscripcion) {
		this.inscripcion = inscripcion;
	}
	
	/*
	public void addInscripcione(Inscripcion inscripcion){
		this.inscripciones.add(inscripcion);
	}
	public void removeInscripcion(Inscripcion inscripcion){
		this.inscripciones.remove(inscripcion);
	}
	 * */
	
	
	}
