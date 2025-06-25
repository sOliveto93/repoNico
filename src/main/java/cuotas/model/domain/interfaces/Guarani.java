package cuotas.model.domain.interfaces;

import java.util.Date;

import common.model.domain.datos.GenericBean;
import common.model.domain.error.ErrorParametro;
import cuotas.model.domain.datos.Persona;

public class Guarani  extends GenericBean<Long>  implements java.io.Serializable {
	private static final long serialVersionUID = 6224478489834044231L;
	
	String UA= "";
	String n_inscripcion= "";
	String legajo= "";
	String tipo_documento= "";
	String nro_documento= "";
	String apellido= "";
	String nombres= "";
	String calle= "";
	String numero= "";
	String piso= "";
	String dpto= "";
	String cp= "";
	String telefono= "";
	String partido= "";
	String provincia= "";
	String pais= "";
	String carrera= "";
	Date fecha_ins;
	Date fecha_ingr;
	Integer cohorte;
	String regular= "";
	String calidad= "";
	String email = null;
	boolean procesado = false;
	boolean actualizar = false;
	String tipo = ""; //GRADO / POSGRADO
	Persona persona;
	
	public Guarani(){	
	}
	
	public Guarani(String UA, String n_inscripcion, String legajo, String tipo_documento, String nro_documento, String apellido, 
			String nombres, String calle, String numero, String piso, String dpto, String cp, String telefono, String partido, 
			String provincia, String pais, String carrera, Date fecha_ins,Date fecha_ingr,Integer cohorte, String regular, String calidad, String tipo, String email){
		this.UA  = UA ;
		if(n_inscripcion == null){
			this.n_inscripcion = "";
		}else{
			this.n_inscripcion  = n_inscripcion ;
		}
		if(legajo == null){
			this.legajo ="";
		}else{
			this.legajo  = legajo ;	
		}		
		this.tipo_documento  = tipo_documento ;
		this.nro_documento  = nro_documento ;
		if(apellido == null){
			this.apellido  = "" ;
		}else{
			this.apellido  = apellido ;	
		}		
		if(nombres == null){
			this.nombres  =  "";
		}else{
			this.nombres  = nombres ;	
		}		
		if(calle == null){
			this.calle  = "" ;
		}else{
			this.calle  = calle ;	
		}		
		if(numero == null){
			this.numero  = "" ;
		}else{
			this.numero  = numero ;	
		}		
		if(piso == null){
			this.piso  = "" ;
		}else{
			this.piso  = piso ;	
		}		
		if(dpto == null){
			this.dpto  = "" ;
		}else{
			this.dpto  = dpto ;	
		}		
		if(cp == null){
			this.cp  = "" ;
		}else{
			this.cp  = cp ;	
		}		
		if( telefono  == null){
			this.telefono  = "";
		}else{
			this.telefono  = telefono ;	
		}		
		if(partido == null){
			this.partido  = "" ;
		}else{
			this.partido  = partido ;	
		}		
		if(provincia == null){
			this.provincia  = "" ;
		}else{
			this.provincia  = provincia ;	
		}		
		if(pais  == null){
			this.pais  = "";
		}else{
			this.pais  = pais ;	
		}		
		if(carrera == null){
			this.carrera  = "";			
		}else{
			this.carrera  = carrera;
		}		
		this.regular = regular;
		this.calidad = calidad;
		this.fecha_ins = fecha_ins;		
		this.fecha_ingr = fecha_ingr;
		this.cohorte = cohorte;
		this.tipo = tipo;
		this.email = email;
	}
	public String getUA() {
		return UA;
	}
	public void setUA(String uA) {
		UA = uA;
	}
	public String getN_inscripcion() {
		return n_inscripcion;
	}
	public void setN_inscripcion(String nInscripcion) {
		n_inscripcion = nInscripcion;
	}
	public String getLegajo() {
		return legajo;
	}
	public void setLegajo(String legajo) {
		this.legajo = legajo;
	}
	public String getTipo_documento() {
		return tipo_documento;
	}
	public void setTipo_documento(String tipoDocumento) {
		tipo_documento = tipoDocumento;
	}
	public String getNro_documento() {
		return nro_documento;
	}
	public void setNro_documento(String nroDocumento) {
		nro_documento = nroDocumento;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getPiso() {
		return piso;
	}
	public void setPiso(String piso) {
		this.piso = piso;
	}
	public String getDpto() {
		return dpto;
	}
	public void setDpto(String dpto) {
		this.dpto = dpto;
	}
	public String getCp() {
		return cp;
	}
	public void setCp(String cp) {
		this.cp = cp;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getPartido() {
		return partido;
	}
	public void setPartido(String partido) {
		this.partido = partido;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getCarrera() {
		return carrera;
	}
	public void setCarrera(String carrera) {
		this.carrera = carrera;
	}
	public Date getFecha_ins() {
		return fecha_ins;
	}
	public void setFecha_ins(Date fechaIns) {
		fecha_ins = fechaIns;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getFecha_ingr() {
		return fecha_ingr;
	}

	public void setFecha_ingr(Date fechaIngr) {
		fecha_ingr = fechaIngr;
	}

	public Integer getCohorte() {
		return cohorte;
	}

	public void setCohorte(Integer cohorte) {
		this.cohorte = cohorte;
	}

	public boolean isProcesado() {
		return procesado;
	}

	public void setProcesado(boolean procesado) {
		this.procesado = procesado;
	}
	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public String getRegular() {
		return regular;
	}

	public void setRegular(String regular) {
		this.regular = regular;
	}

	public String getCalidad() {
		return calidad;
	}

	public void setCalidad(String calida) {
		this.calidad = calida;
	}

	public boolean isActualizar() {
		return actualizar;
	}

	public void setActualizar(boolean actualizar) {
		this.actualizar = actualizar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
