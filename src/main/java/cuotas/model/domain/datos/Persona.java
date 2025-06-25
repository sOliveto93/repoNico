package cuotas.model.domain.datos;
// default package
// Generated 11/06/2012 12:24:34 by Hibernate Tools 3.2.4.GA

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import common.model.domain.datos.Estado;
import common.model.domain.datos.PersonaGenerica;
import common.model.domain.error.ErrorParametro;
import common.model.domain.fecha.Fecha;
import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.dataSource.repository.jdbc.GuaraniInformix;
import cuotas.model.domain.factura.Factura;
import cuotas.model.domain.interfaces.Guarani;


public class Persona  extends PersonaGenerica  implements java.io.Serializable {
	private static final long serialVersionUID = -7870662065963036234L;

	//private Integer nroOrden;
	private Fecha fechaIngreso;
	private Integer fondoBeca1;
	private Fecha feUtlPago; 
	private Fecha fePago01;
	private Integer fePago01tipo;
	private Fecha fePago02;
	private Fecha fePgoA;
	private Character fePgoB;
	private Character sedes;
	private String legajo;
	private String nro_inscripcion;
	private String legajoGrado;
	private String legajoPosgrado;
	private String iddep;
	private Set<Inscripcion> inscripciones = new HashSet<Inscripcion>(0);
	private Set<Pago> pagos = new HashSet<Pago>(0);
	private Set<Guarani> importados = new HashSet<Guarani>(0);
	private Set<Factura> facturas = new HashSet<Factura>(0);
	private Estado estado;
	private String mail;
	private String password;
	private String carreraAdeudada;
	private String habilitadoTodoPago;
	
	public Persona() {
		this.estado = new Estado(Estado.ACTIVO);
	}
	public Persona(String tipoDocumento, Integer nroDocumento,
			//Integer nroOrden, 
			String domicilio, String localidad,
			String provincia, String codigoPostal, String telefonoParticular,
			Fecha fechaIngreso, Integer fondoBeca1, Fecha feUtlPago,
			Fecha fePago01, Integer fePago01tipo, Fecha fePago02, Fecha fePgoA,
			Character fePgoB, Character sedes, String telefono, String legajo, String inscripcion,
			Set<Inscripcion> inscripciones, Set<Pago> pagos, Set<Factura> factura) {

		//this.nroOrden = nroOrden;
		this.fechaIngreso = fechaIngreso;
		this.fondoBeca1 = fondoBeca1;
		this.feUtlPago = feUtlPago;
		this.fePago01 = fePago01;
		this.fePago01tipo = fePago01tipo;
		this.fePago02 = fePago02;
		this.fePgoA = fePgoA;
		this.fePgoB = fePgoB;
		this.legajo = legajo;
		this.nro_inscripcion = inscripcion;
		this.sedes = sedes;
		this.inscripciones = inscripciones;
		this.pagos = pagos;
		this.facturas = factura;
	}

	public String getLegajoGrado() {
		return legajoGrado;
	}
	public void setLegajoGrado(String legajoGrado) {
		this.legajoGrado = legajoGrado;
	}
	public String getLegajoPosgrado() {
		return legajoPosgrado;
	}
	public void setLegajoPosgrado(String legajoPosgrado) {
		this.legajoPosgrado = legajoPosgrado;
	}
	/*public Integer getNroOrden() {
		return this.nroOrden;
	}

	public void setNroOrden(Integer nroOrden) {
		this.nroOrden = nroOrden;
	}
	 */
	public String getMail() {
			return mail;
	}
	public void setMail(String mail) {
		if(mail!=null && mail.length()<=99){
			this.mail = mail;	
		}
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		if(password!=null && password.length()<=25){
			this.password = password;	
		}
	}
		
	public Fecha getFechaIngreso() {
		return this.fechaIngreso;
	}

	public String getCarreraAdeudada() {
		return FactoryDAO.personaDAO.buscarTipoMora(this);
	}
	
	public void setFechaIngreso(Fecha fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Integer getFondoBeca1() {
		return this.fondoBeca1;
	}

	public void setFondoBeca1(Integer fondoBeca1) {
		this.fondoBeca1 = fondoBeca1;
	}

	public Fecha getFeUtlPago() {
		return this.feUtlPago;
	}

	public void setFeUtlPago(Fecha feUtlPago) {
		this.feUtlPago = feUtlPago;
	}

	public Fecha getFePago01() {
		return this.fePago01;
	}

	public void setFePago01(Fecha fePago01) {
		this.fePago01 = fePago01;
	}

	public Integer getFePago01tipo() {
		return this.fePago01tipo;
	}

	public void setFePago01tipo(Integer fePago01tipo) {
		this.fePago01tipo = fePago01tipo;
	}

	public Fecha getFePago02() {
		return this.fePago02;
	}

	public void setFePago02(Fecha fePago02) {
		this.fePago02 = fePago02;
	}

	public Fecha getFePgoA() {
		return this.fePgoA;
	}

	public void setFePgoA(Fecha fePgoA) {
		this.fePgoA = fePgoA;
	}

	public Character getFePgoB() {
		return this.fePgoB;
	}

	public void setFePgoB(Character fePgoB) {
		this.fePgoB = fePgoB;
	}

	public Character getSedes() {
		return this.sedes;
	}

	public void setSedes(Character sedes) {
		this.sedes = sedes;
	}


	public void setInscripciones(Set<Inscripcion> inscripciones) {
		this.inscripciones = inscripciones;
	}
	
	public Set<Inscripcion> getInscripciones() {
		return inscripciones;
	}

	public void setAlumnosCarreras(Set<Inscripcion> inscripciones) {
		this.inscripciones = inscripciones;
	}

	public Set<Pago> getPagos() {
		return pagos;
	}

	public void setPagos(Set<Pago> pagos) {
		this.pagos = pagos;
	}

	public Set<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(Set<Factura> facturas) {
		this.facturas = facturas;
	}
	
	public void addFactura(Factura factura){
		this.facturas.add(factura);
	}
	public void removeFactura(Factura factura){
		this.facturas.remove(factura);
	}	
	
	public void addPagos(Pago pago){
		this.pagos.add(pago);
	}
	public void removePagos(Pago pago){
		this.pagos.remove(pago);
	}
	
	public void addInscripcion(Inscripcion inscripcion){
		this.inscripciones.add(inscripcion);
	}
	public void removeInscripcion(Inscripcion inscripcion){
		this.inscripciones.remove(inscripcion);
	}
	
	public Boolean estaInscriptoEn(Carrera carrera){
		Boolean resultado = false;
		if(this.inscripciones!=null){
			Iterator<Inscripcion> inscripciones = this.inscripciones.iterator();
			while(inscripciones.hasNext()){
				Inscripcion inscripcion = inscripciones.next();
				if(inscripcion.getCarrera().equals(carrera)){
					resultado = true;
					break;
				}
			}
		}
		return resultado ;
	}
	
	public void blanquearIncripciones(){
		
		this.inscripciones.clear();
	}

	public Estado getEstado() {
		return estado;
	}
	

	public String getLegajo() {
		return legajo;
	}
	public void setLegajo(String legajo) {
		this.legajo = legajo;
	}
	public String getNro_inscripcion() {
		return nro_inscripcion;
	}
	public void setNro_inscripcion(String nroInscripcion) {
		nro_inscripcion = nroInscripcion;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public int hashCode(){
		if(this.getId() != null)
			return this.getId().hashCode();
		else
			return 0;
	}
	
	public boolean equals(Object object){
		Persona otraPersona = (Persona) object;
		return this.getId().equals(otraPersona.getId());
	}
	public String toString() {
		return new String( this.getId() +" - "+this.getDatosPersonales() );
	}
	public String getNombreCompleto(){
		String apellidoNombre="";
		if (getApellido()!= null && !getApellido().equals("")){
			apellidoNombre = getApellido();
		}
		
		if(getNombre() !=null && !getNombre().equals("")){
			if(apellidoNombre!="")
				apellidoNombre = apellidoNombre +","+getNombre();
			else
				apellidoNombre = getNombre();
		}
		return apellidoNombre;
	}
	public Set<Guarani> getImportados() {
		return importados;
	}
	public void setImportados(Set<Guarani> importados) {
		this.importados = importados;
	}
	public String getIddep() {
		return iddep;
	}
	public void setIddep(String iddep) {
		this.iddep = iddep;
	}
	
	public String getHabilitadoTodoPago() {
		return habilitadoTodoPago;
	}
	public void setHabilitadoTodoPago(String habilitadoTodPago) {
		this.habilitadoTodoPago = habilitadoTodPago;
	}
	
	public String getCarrerasActiva(){
		String carrerasActiva=null;
		Iterator<Inscripcion> inscripciones = this.inscripciones.iterator();
		while(inscripciones.hasNext()){
			Inscripcion inscripcion = inscripciones.next();
			if(inscripcion.getEstado()!=null){
				if(inscripcion.getEstado().equals('A') || inscripcion.getEstado().equals('P') || inscripcion.getEstado().equals('I')){
					Carrera carrera = inscripcion.getCarrera();
					String nombreCarrera = "";
					if(carrera.getNombreCarrera()!=null && !carrera.getNombreCarrera().equals("")){
						nombreCarrera  = carrera.getNombreCarrera();
					}
					if(carrerasActiva == null){
						carrerasActiva = nombreCarrera; 
					}else{
						carrerasActiva = carrerasActiva + " - " + nombreCarrera;
					}
				}
			}
		}
		return carrerasActiva;
	}
	public String getCarreraTipo(){
		String carreraTipo=null;
		Iterator<Inscripcion> inscripciones = this.inscripciones.iterator();
		while(inscripciones.hasNext()){
			Inscripcion inscripcion = inscripciones.next();
			if(inscripcion.getEstado().equals('A') || inscripcion.getEstado().equals('P') || inscripcion.getEstado().equals('I')){
				if(carreraTipo == null){
					carreraTipo = inscripcion.getCarrera().getTipo();
				}
			}
		}
		return carreraTipo;
	}
	/***
	 * Concatena el legajo que corresponda ( grado - posgrado ) con el tipo de carrera.
	 * @return
	 */
	public String getLegajoTipo(){
		String legajo = this.getCarreraTipo();
		if(legajo != null && legajo.equals("grado")){
			legajo = legajo +" - "+ this.getLegajoGrado();
		}else  if(legajo != null && legajo.equals("posgrado")){
			legajo = legajo +" - "+ this.getLegajoPosgrado();
		}
		return legajo;
	}
	public Integer numeroMaximoCuota(){
		Integer resp = FactoryDAO.personaDAO.numeroMaximoCuota(this);
		if(resp==null){
			resp=0;
		}
		return resp;
	}
	public void sancionarExceptuarPersonas(String operacion, String lista){
		try {
			System.out.println("entro en Sancionar:");
			System.out.println(lista);
			GuaraniInformix guaraniInformixGrado = new GuaraniInformix("grado","0","");
			GuaraniInformix guaraniInformixPosgrado = new GuaraniInformix("posgrado","0","");		
			guaraniInformixGrado.conectar();			
			guaraniInformixPosgrado.conectar();
			
			String[] l = lista.split("\\|");
			String[] datos;
			for (int i = 0; i < l.length; i++){
				datos = l[i].split(",");
				if(!datos[1].equals("null")){			
					if(datos[0].equals("grado")){						
						guaraniInformixGrado.marcarPago(datos[1], operacion, datos[2]);
					}
					if(datos[0].equals("posgrado")){
						
						guaraniInformixPosgrado.marcarPago(datos[1], operacion, datos[2]);
					}
				}
			}
						
			System.out.println("Operacion: " + operacion);			
			guaraniInformixGrado.desconectar();
			guaraniInformixPosgrado.desconectar();
		} catch (SQLException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
}