package common.model.domain.datos;

import common.model.domain.datosPersonales.Documento;
import common.model.domain.datosPersonales.Domicilio;
import common.model.domain.datosPersonales.Telefono;
import common.model.domain.datosPersonales.TelefonoCelular;
import common.model.domain.fecha.FechaNacimiento;
import common.model.domain.mail.DireccionMail;


public class PersonaGenerica extends GenericBean<Long> implements java.io.Serializable {

	private static final long serialVersionUID = -2105460781428774572L;
	
	private String apellido;
	private String nombre;
	private TelefonoCelular celular;
	private Telefono telefono;
	private Telefono telParticular;
	private Telefono otroTelefono;
	private Telefono fax;
	private DireccionMail email;
	
	private FechaNacimiento fechaNac;
	private Documento documento ;

	private Domicilio direccion;
	private String notas;
	
	public PersonaGenerica() {
		
	}
	
	
	public PersonaGenerica(String apellido, String nombre) {
		this.apellido = apellido;
		this.nombre = nombre;
		
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TelefonoCelular getCelular() {
		return this.celular;
	}

	public void setCelular(TelefonoCelular celular) {
		this.celular = celular;
	}

	public Telefono getTelefono() {
		return this.telefono;
	}

	public void setTelefono(Telefono telefono) {
		this.telefono = telefono;
	}

	public Telefono getTelParticular() {
		return this.telParticular;
	}

	public void setTelParticular(Telefono telparticular) {
		this.telParticular = telparticular;
	}

	public Telefono getOtroTelefono() {
		return this.otroTelefono;
	}

	public void setOtroTelefono(Telefono otrotelefono) {
		this.otroTelefono = otrotelefono;
	}

	public Telefono getFax() {
		return this.fax;
	}

	public void setFax(Telefono fax) {
		this.fax = fax;
	}

	public DireccionMail getEmail() {
		return this.email;
	}

	public void setEmail(DireccionMail email) {
		this.email = email;
	}

	public FechaNacimiento getFechaNac() {
		return this.fechaNac;
	}

	public void setFechaNac(FechaNacimiento fechaNac) {
		this.fechaNac = fechaNac;
	}

	public Domicilio getDireccion() {
		return this.direccion;
	}

	public void setDireccion(Domicilio direccion) {
		this.direccion = direccion;
	}

	public String getNotas() {
		return this.notas;
	}

	public void setNotas(String notas) {
		this.notas = notas;
	}
	
	
	public String toString(){
		return getNombreCompleto() ;
	}

	public String getNombreCompleto(){
		return this.apellido+", "+this.nombre ;
	}
	
	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}
	
	public String getDatosPersonales() {
		return getNombreCompleto()+" - DOC: "+documento+" - TE: "+getTelefono();
	}
}

