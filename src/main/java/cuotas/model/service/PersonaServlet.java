package cuotas.model.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.types.CommandlineJava.SysProperties;
import common.model.domain.datos.Estado;
import common.model.domain.datosPersonales.Documento;
import common.model.domain.datosPersonales.Domicilio;
import common.model.domain.datosPersonales.TelefonoFijo;
import common.model.domain.datosPersonales.TipoDocumento;
import common.model.domain.error.ErrorGrabacion;
import common.model.domain.error.ErrorParametro;
import common.model.domain.fecha.Anio;
import common.model.domain.fecha.Fecha;
//import common.model.domain.util.Evento;
import common.model.domain.validacion.UsuarioGenerico;
import common.model.service.MappingMethodServlet;
import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.Inscripcion;
import cuotas.model.domain.datos.OrigenPago;
import cuotas.model.domain.datos.Pago;
import cuotas.model.domain.datos.Persona;
import cuotas.model.domain.datos.Beca;
import cuotas.model.domain.datos.PersonasBeca;
import cuotas.model.domain.datos.UsuarioCuotas;
import cuotas.model.domain.json.FactoryJson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PersonaServlet extends MappingMethodServlet {
	private static final long serialVersionUID = -9199942798403444031L;

	public PersonaServlet() {
		this.mapeoOperacionMetodo.put("ver_persona", "verPersonaJson");
		this.mapeoOperacionMetodo.put("mantenimiento_persona", "mantenimientoPersona");
		this.mapeoOperacionMetodo.put(CuotasServletUtils.ACTUALIZAR, "modificar");
		this.mapeoOperacionMetodo.put("actualizarMail", "actualizarMail");

	}

	public void listar() {

		// AGREGAR VALIDACION DEP DRYC
		UsuarioCuotas usuario = (UsuarioCuotas) session.getAttribute("usuario");
		usuario = FactoryDAO.usuarioDAO.findByLong(usuario.getId().toString());
		System.out.println(
				"UsuarioOriginal: " + usuario.getNombre() + " - " + usuario.getTipo() + " - " + usuario.getId());

		List<String> tiposDocumento = FactoryDAO.mapeoTipoDocumentoDAO.listTiposDocumento();
		List<Carrera> carreras = null;

		if (usuario.getTipo().equals("admin") || (usuario.getTipo().equals("auditoria"))) {
			carreras = FactoryDAO.carreraDAO.listAll("codigo", true);
		} else if ( (usuario.getTipo().equals("DEP")) || (usuario.getTipo().equals("DRIYC")) || (usuario.getTipo().equals("DEP_ADMIN"))|| (usuario.getTipo().equals("DRC_ADMIN")) || (usuario.getTipo().equals("consulta")) ) {
			carreras = FactoryDAO.carreraDAO.listAllbyUsr(usuario);
		}
		if (request.getParameter("limpiarBusqueda") != null && request.getParameter("limpiarBusqueda").equals("1")) {
			System.out.println("removeLimpiarBusqueda");
			session.removeAttribute("personaBuscada");
			session.removeAttribute("carreraBuscada");
		}
		Persona personaBuscada = (Persona) session.getAttribute("personaBuscada");
		Carrera carreraBuscada = (Carrera) session.getAttribute("carreraBuscada");

		if (personaBuscada != null || carreraBuscada != null) {
			System.out.println("filtrando...");
			List<Persona> personas = FactoryDAO.personaDAO.filtrar(personaBuscada, carreraBuscada, null, 0, 300, null,
					usuario);
			request.setAttribute("personas", personas);
			System.out.println("FILTRO");
		}
		request.setAttribute("tiposDocumento", tiposDocumento);
		request.setAttribute("carreras", carreras);
		session.setAttribute("usuario", usuario);
		if (usuario.getTipo().equals("admin") || (usuario.getTipo().equals("DEP_ADMIN"))|| (usuario.getTipo().equals("DRC_ADMIN")) || (usuario.getTipo().equals("consulta")) ) {
			setSalto(CuotasServletUtils.SALTO_PERSONA_LISTAR);
		} else if (usuario.getTipo().equals("DRIYC") || (usuario.getTipo().equals("DEP")) || (usuario.getTipo().equals("auditoria"))) {
			setSalto(CuotasServletUtils.SALTO_PERSONA_LISTAR_DEP_DRYC);
		}
	}

	public void generaPagoPersona() {
		UsuarioCuotas usuario = (UsuarioCuotas) session.getAttribute("usuario");
		usuario = FactoryDAO.usuarioDAO.findByLong(usuario.getId().toString());
		
		Iterator<Grupo> gUsuario = usuario.getGrupos().iterator();
		
		List<String> tiposDocumento = FactoryDAO.mapeoTipoDocumentoDAO.listTiposDocumento();
		Iterator<Grupo> grupos = FactoryDAO.grupoDAO.listAll().iterator();
		session.removeAttribute("pagosGenerados");
		
		request.setAttribute("gruposUsuario",gUsuario);
		request.setAttribute("tiposDocumento", tiposDocumento);
		request.setAttribute("grupos", grupos);

		setSalto(CuotasServletUtils.SALTO_GENERAR_PAGO_PERSONA);
	}
	/*public void validarMail() {
		String mail = request.getParameter("mail");
		Boolean existeMail = FactoryDAO.personaDAO.existeMail(mail);
		Persona persona = null;
		if(existeMail){
			persona = FactoryDAO.personaDAO.buscarPorMail(mail);
			
		}
		
		JSONObject errorJson = new JSONObject();
		errorJson.put("existe", existeMail);
		String jsonMail = errorJson.toString();
		
		this.enviarJson(response, jsonMail);
		 
	}*/
	public void altaPagoPersona() {
		String idPersona = request.getParameter("idpersona");
		String idgrupo = request.getParameter("grupos");
		String montoPago = request.getParameter("montoPago");
		String fechaCuota = request.getParameter("fechaCuota");
		String mail = request.getParameter("mail");
		String observaciones = request.getParameter("observaciones");
		
		JSONArray pagosGenerados = (JSONArray) session.getAttribute("pagosGenerados");
		UsuarioCuotas usuario = (UsuarioCuotas) session.getAttribute("usuario");
		Iterator<Grupo> gUsuario = usuario.getGrupos().iterator();
		
		Fecha fcuota = null;
		Grupo grupo = null;
		Pago pago = new Pago();
		Estado estado = new Estado(1);
		Persona persona = FactoryDAO.personaDAO.findByLong(idPersona,true);
		OrigenPago origenPago = FactoryDAO.origenPagoDAO.findByInteger("1");

		
		if(persona.getMail()==null){
			persona.setMail(mail);
			persona.setHabilitadoTodoPago("S");
		}
		pago.setNumero(persona.numeroMaximoCuota()+1);
		pago.setPersona(persona);

		if(observaciones != null && !observaciones.equals("")){
			pago.setObservaciones(observaciones);
		}
		if (origenPago != null) {
			pago.setOrigenPago(origenPago);
		}
		if (fechaCuota != null && !fechaCuota.equals("")) {
			fcuota = new Fecha(fechaCuota);
			pago.setFecha(fcuota);
		}
		if (montoPago != null && !montoPago.equals("")) {
			Double montoPagoD = Double.parseDouble(montoPago);
			pago.setMonto1(montoPagoD);
			pago.setMonto2(0.0);
		}
		if (idgrupo!= null && !idgrupo.equals("")) {
			grupo = FactoryDAO.grupoDAO.findByLong(idgrupo);
			pago.setGrupo(grupo);
		}
		if (fechaCuota != null && montoPago != null && grupo != null && persona != null && origenPago != null) {
			pago.setEstado(estado);
			pago.setTipo("E");
			pago.setProcesado(false);
			pago.setExceptuarMora(true);
			pago.setVtoMonto(0.0);
			pago.setDadodebaja(false);
			pago.setCodigo_barra("");
			pago.setCodigo_usuario(persona.getId().toString());
		}
		
		JSONObject jsonObj = new JSONObject();
		if(pagosGenerados == null){
			pagosGenerados = new JSONArray();
			jsonObj.put("nombre", persona.getNombreCompleto());
			jsonObj.put("documento", persona.getDocumento().getTipoyNumeroFormateado());
			jsonObj.put("observaciones", pago.getObservaciones());
			jsonObj.put("fecha", pago.getFecha().getFechaFormateada());
			jsonObj.put("monto", pago.getMonto1());
		}else{
			jsonObj.put("nombre", persona.getNombreCompleto());
			jsonObj.put("documento", persona.getDocumento().getTipoyNumeroFormateado());
			jsonObj.put("observaciones", pago.getObservaciones());
			jsonObj.put("fecha", pago.getFecha().getFechaFormateada());
			jsonObj.put("monto", pago.getMonto1());
		}
		pagosGenerados.add(jsonObj);
		
		FactoryDAO.pagoDAO.save(pago);
		
		Iterator<String> tiposDocumento = FactoryDAO.mapeoTipoDocumentoDAO.listTiposDocumento().iterator();
		Iterator<Grupo> grupos = FactoryDAO.grupoDAO.listAll().iterator();

		request.setAttribute("gruposUsuario",gUsuario);
		session.setAttribute("pagosGenerados", pagosGenerados);
		request.setAttribute("tiposDocumento", tiposDocumento);
		request.setAttribute("grupos", grupos);
		setSalto(CuotasServletUtils.SALTO_GENERAR_PAGO_PERSONA);
	}

	public void alta() {
		try {
			String operacionAnterior = request.getParameter("operacionAnterior");

			Persona persona = getPersonaActualizada();
			FactoryDAO.personaDAO.save(persona);
			
			request.setAttribute("operacion", operacionAnterior);
			listar();
		} catch (ErrorParametro e) {
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, "La Persona ya existe");
		}
	}

	public void modificar() {
		Persona persona;
		try {
			String operacionAnterior = request.getParameter("operacionAnterior");
			persona = getPersonaActualizada();
			FactoryDAO.personaDAO.saveOrUpdate(persona);
			List<String> tiposDocumento = FactoryDAO.mapeoTipoDocumentoDAO.listTiposDocumento();

			request.setAttribute("tiposDocumento", tiposDocumento);
			request.setAttribute("operacion", operacionAnterior);
			request.setAttribute("msg", "Persona guardada con exito");
		} catch (ErrorParametro e) {
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, e.getMSG());
		}
		setSalto("/PersonaServlet?operacion=mantenimiento_persona");
	}

	public void borrar() {
		try {
			String id = request.getParameter("id");
			Persona persona = FactoryDAO.personaDAO.findByLong(id);
			if (persona != null) {
				persona.getEstado().desactivar();
				FactoryDAO.personaDAO.saveOrUpdate(persona);
			}
			listar();
		} catch (ErrorGrabacion e) {
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, e.getMSG());
		}
	}

	public void actualizarMail() {
		String jsonPersona = "";
		String idPersona = request.getParameter("idPersona");
		Persona persona = null;

		persona = FactoryDAO.personaDAO.findByLong(idPersona);
		String mail = request.getParameter("mail");
		boolean existeMail = FactoryDAO.personaDAO.existeMail(mail);
		System.out.println("existeMail: " + existeMail + " - Mail: " + mail);
		if (!existeMail) {
			persona.setMail(mail);
			System.out.println("OK: El mail no existe y va a insertarlo o actualizarlo: " + mail);
			FactoryDAO.personaDAO.saveOrUpdate(persona);
			persona = FactoryDAO.personaDAO.findByLong(idPersona);
			jsonPersona = FactoryJson.personaJson.toJson(persona, true);
		} else {
			JSONObject errorJson = new JSONObject();
			String msj = "";
			System.out.println("ERROR: email ya registrado" + mail);
			List<Persona> PersonasMailList = FactoryDAO.personaDAO.buscarPorMail(mail);
			if (PersonasMailList.size() == 1) {
				Persona personaDueniapersona = PersonasMailList.get(0);
				if(!personaDueniapersona.getId().toString().equals(idPersona)){
					msj = "ERROR: email ya registrado a nombre de " + personaDueniapersona.getNombreCompleto()
					+ " Numero DNI: " + personaDueniapersona.getDocumento().getNumero();
				}else{
					msj=personaDueniapersona.getDocumento().getNumero() + " " + mail +" ingreso el mismo mail.";
				}
				
			} else {
				msj = "ERROR GRAVE: email esta asiganado a mas de una persona, COMUNIQUESE CON EL ADMINISTRADOR DEL SISTEMA";
			}

			errorJson.put("msj", msj);
			jsonPersona = FactoryJson.personaJson.getEsqueletoJson(errorJson.toString());
		}
		System.out.println("jsonPersona: " + jsonPersona);
		this.enviarJson(response, jsonPersona);
	}

	public void altaPersonaFactura() {
		String nombreCompleto = request.getParameter("nombrePersona");
		String numeroDocumento = request.getParameter("numeroDocumento");
		String tipoDocumentoS = request.getParameter("tipoDocumento");

		//prueba jf
		String  mail = request.getParameter("mail");
		
		Persona persona = null;
		TipoDocumento tipoDocumento = new TipoDocumento(tipoDocumentoS);
		Documento documento = new Documento(numeroDocumento, tipoDocumento);

		persona = FactoryDAO.personaDAO.buscarPorDocumento(documento);
		if (persona == null) {
			persona = new Persona();
			persona.setDocumento(documento);
			persona.setNombre(nombreCompleto);
		} else {
			if ((persona.getNombre() == null || persona.getNombre().equals(""))
					&& (persona.getApellido() == null || persona.getApellido().equals(""))) {
				persona.setNombre(nombreCompleto);
				if(mail != null && !mail.equals("")){
					persona.setMail(mail);
					persona.setHabilitadoTodoPago("S");
				}
			}
		}
		FactoryDAO.personaDAO.saveOrUpdate(persona);

		persona = FactoryDAO.personaDAO.buscarPorDocumento(documento);
		String jsonPersona = FactoryJson.personaJson.toJson(persona, true);
		System.out.println("Persona.toString: " + persona.toString());
		System.out.println("json: " + jsonPersona);
		this.enviarJson(response, jsonPersona);
	}

	public Persona getPersonaActualizada() {
		String idPersona = request.getParameter("id");
		Persona persona = FactoryDAO.personaDAO.findByLong(idPersona);
		String numeroDocumento = request.getParameter("numeroDocumento");
		String tipoDocumentoStr = request.getParameter("tipoDocumento");
		TipoDocumento tipoDocumento = new TipoDocumento(tipoDocumentoStr);
		Documento documento = new Documento(numeroDocumento, tipoDocumento);
		if (persona == null) {
			// si ya existe la persona no hago nada
			persona = FactoryDAO.personaDAO.buscarPorDocumento(documento);
			if (persona == null) {
				persona = new Persona();
			} else {
				return persona;
			}
		}

		persona.setApellido(request.getParameter("apellido"));
		persona.setNombre(request.getParameter("nombre"));

		String pais = request.getParameter("pais");
		String pcia = request.getParameter("provincia");
		String localidad = request.getParameter("localidad");
		String codigoPostal = request.getParameter("codigoPostal");
		String calle = request.getParameter("calle");
		String numeroCalle = request.getParameter("numeroCalle");
		String departamento = request.getParameter("depto");
		String pisoStr = request.getParameter("piso");
		String telefono = request.getParameter("telefono");
		String nroOrdenStr = request.getParameter("numeroOrden");
		String fechaIngreso = request.getParameter("fechaIngreso");
		String idCarrera = request.getParameter("carreras");
		Integer piso;

		if (pisoStr == null || pisoStr.equals("")) {
			piso = 0;
		} else {
			piso = Integer.parseInt(pisoStr);
		}
		if (!fechaIngreso.equals("")) {
			persona.setFechaIngreso(new Fecha(fechaIngreso));
		} else {
			persona.setFechaIngreso(new Fecha(new Date(System.currentTimeMillis())));
		}
		Integer nroOrden = null;
		if (nroOrdenStr != null && !nroOrdenStr.equals("")) {
			nroOrden = Integer.parseInt(nroOrdenStr);
		}

		persona.setDocumento(documento);
		persona.setDireccion(
				new Domicilio(pais, pcia, localidad, codigoPostal, calle, numeroCalle, piso, departamento));
		persona.setTelefono(new TelefonoFijo(telefono));

		if (idCarrera != null) {
			Carrera carrera = FactoryDAO.carreraDAO.findByLong(idCarrera);
			Inscripcion ins = new Inscripcion();
			ins.setEstado('A');
			ins.setFechaHoraInscripcionIngreso(new Fecha(new Date(System.currentTimeMillis())));
			ins.setCarrera(carrera);
			ins.setPersona(persona);
			persona.addInscripcion(ins);
		}
		return persona;
	}

	public void guardarInscripcion(String idCarrera, Persona persona) {
		Carrera carrera = FactoryDAO.carreraDAO.findByLong(idCarrera);
		if (!persona.estaInscriptoEn(carrera)) {
			Inscripcion ins = new Inscripcion();
			ins.setEstado('A');
			ins.setCarrera(carrera);
			ins.setNumeroCarrera(carrera.getId());
			ins.setPersona(persona);
			ins.setFechaHoraInscripcionIngreso(new Fecha(new Date(System.currentTimeMillis())));
			persona.addInscripcion(ins);

			FactoryDAO.personaDAO.saveOrUpdate(persona);
		}
	}

	public void cargarDescuentoAcademico() {
		try {
			String descuento = request.getParameter("descuentoPorcentaje");
			Integer descuentoInt = 0;
			String idCarrera = request.getParameter("carreraDescuento");
			String idPersona = request.getParameter("idPersona");
			String idBeca = request.getParameter("idBeca");
			PersonasBeca personaBeca; // = new PersonasBeca();

			Persona persona = FactoryDAO.personaDAO.findByLong(idPersona);
			Carrera carrera = FactoryDAO.carreraDAO.findByLong(idCarrera);

			Beca beca = FactoryDAO.becaDAO.findByInteger(idBeca);

			Inscripcion inscripcion = FactoryDAO.inscripcionDAO.findInscripcion(persona, carrera);
			// asigna los valores de personasBeca
			personaBeca = cargaPersonaBeca(inscripcion, beca, descuento);

			inscripcion.setPersonasBeca(personaBeca);

			FactoryDAO.inscripcionDAO.saveOrUpdate(inscripcion);
			System.out.println("ya se guardo");
			persona = FactoryDAO.personaDAO.findByLong(idPersona);

			inscripcion = FactoryDAO.inscripcionDAO.findInscripcion(persona, carrera);

			String jsonPersona = FactoryJson.personaJson.toJson(persona, true);// inscripcionesJson.toJson(inscripcion,
																				// true);

			System.out.println("jsonPersona: " + jsonPersona);
			this.enviarJson(response, jsonPersona);
		} catch (ErrorGrabacion e) {
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, e.getMSG());
		}
	}

	public void eliminarDescuentoAcademico() {
		try {
			String descuento = request.getParameter("descuentoPorcentaje");
			Integer descuentoInt = 0;
			String idCarrera = request.getParameter("carreraDescuento");
			String idPersona = request.getParameter("idPersona");
			String idBeca = request.getParameter("idBeca");
			PersonasBeca personaBeca;

			Persona persona = FactoryDAO.personaDAO.findByLong(idPersona);
			Carrera carrera = FactoryDAO.carreraDAO.findByLong(idCarrera);

			Beca beca = FactoryDAO.becaDAO.findByInteger(idBeca);

			Inscripcion inscripcion = FactoryDAO.inscripcionDAO.findInscripcion(persona, carrera);
			personaBeca = FactoryDAO.personasBecaDAO.findByLong(inscripcion.getId().toString());

			inscripcion.removePersonaBeca();
			FactoryDAO.inscripcionDAO.saveOrUpdate(inscripcion);
			FactoryDAO.personasBecaDAO.delete(personaBeca);

			persona = FactoryDAO.personaDAO.findByLong(idPersona);
			inscripcion = FactoryDAO.inscripcionDAO.findInscripcion(persona, carrera);
			String jsonPersona = FactoryJson.personaJson.toJson(persona, true);
			System.out.println("jsonPersona: " + jsonPersona);
			this.enviarJson(response, jsonPersona);
		} catch (ErrorGrabacion e) {
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, "Erorr en el Baja de la Beca");
		}

	}

	public PersonasBeca cargaPersonaBeca(Inscripcion inscripcion, Beca beca, String descuento) {
		PersonasBeca personaBeca = null;
		if (inscripcion.getPersonasBeca() == null) {
			personaBeca = new PersonasBeca();
		} else {
			personaBeca = inscripcion.getPersonasBeca();
		}
		Integer descuentoInt = 0;

		if (!esNuloOVacio(descuento)) {
			descuentoInt = Integer.parseInt(descuento);
			personaBeca.setDescuentoBeca(descuentoInt);
		}
		if (beca != null) {
			personaBeca.setBeca(beca);
		}
		if (inscripcion != null) {
			personaBeca.setInscripcion(inscripcion);
		}
		return personaBeca;
	}

	public void buscaDescuento() {
		String idCarrera = request.getParameter("idCarrera");
		String idPersona = request.getParameter("idPersona");

		Persona persona = FactoryDAO.personaDAO.findByLong(idPersona);
		Carrera carrera = FactoryDAO.carreraDAO.findByLong(idCarrera);

		Inscripcion inscripcion = FactoryDAO.inscripcionDAO.findInscripcion(persona, carrera);
		inscripcion = FactoryDAO.inscripcionDAO.findByLong(inscripcion.getId().toString(), true);

		String jsonInscripcion = FactoryJson.inscripcionesJson.toJson(inscripcion, true);
		System.out.println("JsonInscripcion: " + jsonInscripcion);
		this.enviarJson(response, jsonInscripcion);
	}

	public void buscaDescuentoPorInscripcion() {
		String idInscripcion = request.getParameter("idInscripcion");

		Inscripcion inscripcion = FactoryDAO.inscripcionDAO.findByLong(idInscripcion, true);

		String jsonInscripcion = FactoryJson.inscripcionesJson.toJson(inscripcion, true);
		System.out.println("JsonInscripcion: " + jsonInscripcion);
		this.enviarJson(response, jsonInscripcion);
	}

	public void listarUnificarPersona() {
		String nroDocumento = request.getParameter("nroDocumento");
		List<Persona> personas = FactoryDAO.personaDAO.buscarPersonasPorDocumento(nroDocumento);

		request.setAttribute("personas", personas);
		setSalto(CuotasServletUtils.SALTO_UNIFICAR_PERSONA_LISTAR);
	}

	public void unificarPersona() {

	}

	/*
	 * public void moraPersonasJson() { Integer maxResults,firstResult; String
	 * jsonPersonas = null; Integer paginaNumero = null; maxResults = 300;
	 * firstResult = 0;
	 * 
	 * String pagNumero =request.getParameter("paginaNumero");
	 * 
	 * 
	 * if(pagNumero != null && !pagNumero.equals("")){ paginaNumero =
	 * Integer.parseInt(pagNumero); } if(pagNumero!=null &&
	 * !pagNumero.equals("0")){ firstResult = maxResults * paginaNumero; }
	 * //fijarse donde llama a esta bosta para ver si tengo que levantar el
	 * parametro...bla bla bla List<Persona> personas =
	 * FactoryDAO.personaDAO.buscarPersonasMora(maxResults,firstResult,"true");
	 * 
	 * Iterator<Persona> personasIt = personas.iterator();
	 * 
	 * JSONObject respuestaJson = new JSONObject(); if(personas != null){
	 * respuestaJson.put("personas",
	 * FactoryJson.personaJson.getJsonLight(personasIt));
	 * respuestaJson.put("numeroPagina", paginaNumero); }
	 * 
	 * System.out.println("personaJson: "+ respuestaJson.toString());
	 * this.enviarJson(response,respuestaJson.toString());
	 * 
	 * } public void moraPersonas() {
	 * if(request.getParameter("limpiarBusqueda")!= null &&
	 * request.getParameter("limpiarBusqueda").equals("1")){
	 * session.removeAttribute("carreraBuscada");
	 * session.removeAttribute("personas"); } String adeuda =
	 * request.getParameter("adeuda"); if(adeuda == null) adeuda = "on";
	 * 
	 * List<Persona> personas =
	 * FactoryDAO.personaDAO.buscarPersonasMora(8000,0,adeuda); List<Carrera>
	 * carreras = FactoryDAO.carreraDAO.listAll();
	 * 
	 * //Carrera carreraBuscada = (Carrera)
	 * session.getAttribute("carreraBuscada");
	 * request.setAttribute("adeudaCuotas", adeuda);
	 * request.setAttribute("personas", personas);
	 * request.setAttribute("carreras", carreras);
	 * System.out.println("sizePersonas: "+ personas.size());
	 * setSalto(CuotasServletUtils.SALTO_PERSONA_MORA); }
	 */
	public void borrarCuotaPersona() {
		try {
			String id = request.getParameter("id");
			String comentarioDelete = request.getParameter("comentarioDelete");

			System.out.println("Comentario: " + comentarioDelete);

			if (FactoryDAO.pagoDAO.existsByLong(id)) {
				Pago pago = FactoryDAO.pagoDAO.findByLong(id);
				//Evento evento = getEventoAuditoria(request, pago);

				if (comentarioDelete != null && !comentarioDelete.equals("")) {
					Estado estado = new Estado();
					estado.setValorNumerico(Estado.INCIERTO);
					pago.setEstado(estado);
					pago.setObservaciones(comentarioDelete);
					FactoryDAO.pagoDAO.saveOrUpdate(pago);
				} else {
					//FactoryDAO.eventoDAO.save(evento, false);
					FactoryDAO.pagoDAO.delete(pago);
				}

			}
			cuotasPersonaJson();
		} catch (ErrorGrabacion e) {
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, e.getMSG());
		}
	}

	// DEVUELVE LAS CUOTAS DE UNA PERSONA.
	public void cuotasPersona() {
		UsuarioCuotas usuario = (UsuarioCuotas) session.getAttribute("usuario");
		String id = request.getParameter("id");
		String operacion = request.getParameter("operacion");
		FactoryDAO.personaDAO.closeSession();
		Persona persona = FactoryDAO.personaDAO.findByLong(id, true);

		List<String> tiposDocumento = FactoryDAO.mapeoTipoDocumentoDAO.listTiposDocumento();
		List<Carrera> carreras = null;
		if(usuario.getTipo().equals("admin")){
			carreras = FactoryDAO.carreraDAO.listAll("codigo", true);
		}else{
			carreras = FactoryDAO.carreraDAO.listAllbyUsr(usuario);
		}
		Iterator<Pago> pagos = persona.getPagos().iterator();
		List<OrigenPago> origenesPago = FactoryDAO.origenPagoDAO.listAll();
		// FactoryDAO.pagoDAO.pagosPorPersona(persona).iterator();
		request.setAttribute("origenesPago", origenesPago);

		request.setAttribute("tiposDocumento", tiposDocumento);
		request.setAttribute("carreras", carreras);
		request.setAttribute("pagos", pagos);
		request.setAttribute("persona", persona);
		request.setAttribute("operacion", operacion);

		if (usuario.getTipo().equals("admin")) {
			System.out.println("Salto: " + CuotasServletUtils.SALTO_PERSONA_MANTENIMIENTO);
			setSalto(CuotasServletUtils.SALTO_PERSONA_MANTENIMIENTO);
		} else if ( (usuario.getTipo().equals("DEP")) || (usuario.getTipo().equals("DRIYC"))|| (usuario.getTipo().equals("auditoria")) 
													  || (usuario.getTipo().equals("DEP_ADMIN")) || (usuario.getTipo().equals("DRC_ADMIN") || (usuario.getTipo().equals("consulta")) )){
			System.out.println("Salto: " + CuotasServletUtils.SALTO_PERSONA_MANTENIMIENTO_DEP_DRY);
			setSalto(CuotasServletUtils.SALTO_PERSONA_MANTENIMIENTO_DEP_DRY);
		}
	}

	public void cuotasPersonaJson() {
		List<OrigenPago> origenesPago = FactoryDAO.origenPagoDAO.listAll();
		session.setAttribute("origenesPago", origenesPago);
		
		String id = request.getParameter("idPersona");
		System.out.println("idPersona: " + id);
		Persona persona = FactoryDAO.personaDAO.findByLong(id, true);
		String jsonPersona = FactoryJson.personaJson.toJson(persona, true);
		System.out.println("json: " + jsonPersona);
		this.enviarJson(response, jsonPersona);
	}

	public void actualizaCuotaPersona() {
		String id = request.getParameter("idCuota");
		Pago pago = FactoryDAO.pagoDAO.findByLong(id);
		
		String idOrigenPago = request.getParameter("origenPagoS");
		OrigenPago origenPago = FactoryDAO.origenPagoDAO.findByInteger(idOrigenPago);
		
		String codigoTodoPago = request.getParameter("codigoTodoPago");

		
		String fechaPgo = request.getParameter("fechaPgo");
		if (!fechaPgo.equals("")) {
			Fecha fechaPago = new Fecha(fechaPgo);
			pago.setFechaPgo(fechaPago);
			
			Fecha fechaPagoCarga = new Fecha(new Date(System.currentTimeMillis()));
			pago.setFePgoCarga(fechaPagoCarga);
			System.out.println("fechaPgo"+ fechaPgo + " - fechaPagoCarga: " + fechaPagoCarga);
		}

		String montoPgo = request.getParameter("montoPgo");
		if (!montoPgo.equals("")) {
			Double montoPago = Double.parseDouble(montoPgo);
			pago.setMonPgo(montoPago);
		}
		String exceptuarMora = request.getParameter("ExceptuarCuota");
		boolean exceptuarMoraB;
		String monto1S = request.getParameter("monto1");
		String monto2S = request.getParameter("monto2");
		String vtoMontoS = request.getParameter("vtoMonto");
		String observacion = request.getParameter("observacionesModificar");

		Double monto1 = Double.parseDouble(monto1S);
		Double monto2 = Double.parseDouble(monto2S);
		Double vtoMonto = Double.parseDouble(vtoMontoS);

		if (observacion != null && !observacion.equals("")) {
			pago.setObservaciones(observacion);
		}
		pago.setMonto1(monto1);
		pago.setMonto2(monto2);
		pago.setVtoMonto(vtoMonto);
		pago.setTipo(request.getParameter("pagoTipo"));

		System.out.println("exceptuarMora: " + exceptuarMora);
		if (exceptuarMora != null && exceptuarMora.equals("on")) {
			exceptuarMoraB = true;
		} else {
			exceptuarMoraB = false;
		}
		pago.setExceptuarMora(exceptuarMoraB);

		
		pago.setProcesado(false);
		pago.setOrigenPago(origenPago);
		if(codigoTodoPago!=null && !codigoTodoPago.equals("")){
			pago.setCodigoTodoPago(codigoTodoPago);
		}
		FactoryDAO.pagoDAO.saveOrUpdate(pago);
		
		pago = FactoryDAO.pagoDAO.findByLong(pago.getId().toString(),true);
		
		String jsonPago = FactoryJson.pagosJson.toJson(pago, true);
		System.out.println("ExceptuarCuota: " + request.getParameter("ExceptuarCuota"));
		System.out.println("JsonPago2: " + jsonPago);
		this.enviarJson(response, jsonPago);
	}

	public void informePago() {
		UsuarioCuotas usuario = (UsuarioCuotas) session.getAttribute("usuario");
		usuario = FactoryDAO.usuarioDAO.findByLong(usuario.getId().toString());
		List<Grupo> grupos = null;
		List<Long> usuarioCodigoGrupos = new ArrayList<Long>();
		if ((usuario.getTipo().equals("DRC")) || (usuario.getTipo().equals("DEP")) || (usuario.getTipo().equals("consulta"))|| (usuario.getTipo().equals("DEP_ADMIN")) || (usuario.getTipo().equals("DRC_ADMIN")) ) {
			// grupos = FactoryDAO.grupoDAO.listAllDep();
			Iterator<Grupo> gruposIt = usuario.getGrupos().iterator();
			grupos = new ArrayList<Grupo>();
			while (gruposIt.hasNext()) {
				Grupo GrupoAux = gruposIt.next();
				usuarioCodigoGrupos.add(GrupoAux.getCodigo());
				grupos.add(GrupoAux);
			}
			// Iterator<Grupo> gruposUsuario= usuario.getGrupos();
		} else {
			grupos = FactoryDAO.grupoDAO.listAll();
		}
		List<OrigenPago> origenesPago = FactoryDAO.origenPagoDAO.listAll();
		session.setAttribute(CuotasServletUtils.USUARIO, usuario);
		request.setAttribute("usuarioCodigoGrupos", usuarioCodigoGrupos);
		request.setAttribute("origenesPago", origenesPago);
		request.setAttribute("grupos", grupos);
		setSalto(CuotasServletUtils.SALTO_INFORME_PAGO);
	}

	public void verPersonaJson() {
		Persona persona = FactoryDAO.personaDAO.findByLong(request.getParameter("id"), true);
		String jsonPersona = FactoryJson.personaJson.toJson(persona, true);
		System.out.println("jsonPersona: " + jsonPersona);
		this.enviarJson(response, jsonPersona);
	}

	public void anularCuotaPersona() {
		String idCuota = (String) request.getParameter("idCuota");
		Pago cuota = FactoryDAO.pagoDAO.findByLong(idCuota);
		cuota.anularPago();
		String jsonCuota = FactoryJson.pagosJson.toJson(cuota, true);
		FactoryDAO.pagoDAO.saveOrUpdate(cuota);

		System.out.println("jsonPago : " + jsonCuota);
		this.enviarJson(response, jsonCuota);
	}

	public void mantenimientoPersona() {
		UsuarioCuotas usuario = (UsuarioCuotas) session.getAttribute("usuario");

		String operacion = request.getParameter("operacion");
		request.setAttribute("operacion", operacion);
		String idPersona = request.getParameter("id");
		String paginaAnterior = request.getParameter("paginaAnterior");
		List<OrigenPago> origenPago = FactoryDAO.origenPagoDAO.listAll(); 
		List<Carrera> carreras = null;
		
		if(usuario.getTipo().equals("admin")){
			carreras = FactoryDAO.carreraDAO.listAll("codigo", true);	
		}else if((usuario.getTipo().equals("DEP_ADMIN"))|| (usuario.getTipo().equals("DRC_ADMIN")) || (usuario.getTipo().equals("DEP")) || (usuario.getTipo().equals("consulta")) ){
			carreras = FactoryDAO.carreraDAO.listAllbyUsr(usuario);
		}
		
		System.out.println("idpersonas. " + idPersona);
		Persona persona = FactoryDAO.personaDAO.findByLong(idPersona, true);
		if (persona != null) {
			persona.getInscripciones();
			Iterator<Inscripcion> insT = persona.getInscripciones().iterator();

			while (insT.hasNext()) {
				Inscripcion instAux = insT.next();
				System.out.println("			insAux: " + instAux.getCarrera().getNombreCarrera());
				String idInscripcion = instAux.getId().toString();
				Inscripcion ins = FactoryDAO.inscripcionDAO.findByLong(idInscripcion);
				if (ins != null)
					System.out.println("ins: " + ins.getCarrera().getNombreCarrera());
			}
		}
		List<String> tiposDocumento = FactoryDAO.mapeoTipoDocumentoDAO.listTiposDocumento();
		List<Beca> becas = FactoryDAO.becaDAO.listAll();
		Iterator<Inscripcion> inscripciones = null;
		if (persona != null) {
			inscripciones = FactoryDAO.inscripcionDAO.buscarPorPersona(persona).iterator();
		}
		List<PersonasBeca> personasBeca = null;
		if (inscripciones != null) {
			personasBeca = cargaPersonasBeca(inscripciones);
			System.out.println("Cargo las becas");
		}
		// persona.getInscripciones();
		request.setAttribute("origenPago", origenPago);
		request.setAttribute("personasBecas", personasBeca);
		request.setAttribute("paginaAnterior", paginaAnterior);
		request.setAttribute("tiposDocumento", tiposDocumento);
		request.setAttribute("becas", becas);
		request.setAttribute("persona", persona);
		request.setAttribute("carreras", carreras);
		System.out.println("Usuario: " + usuario.getTipo());
		if ((usuario.getTipo().equals("admin") )) {
			System.out.println("Salto: " + CuotasServletUtils.SALTO_PERSONA_MANTENIMIENTO);
			setSalto(CuotasServletUtils.SALTO_PERSONA_MANTENIMIENTO);
		} else if ((usuario.getTipo().equals("DEP")) || (usuario.getTipo().equals("DRIYC")) || (usuario.getTipo().equals("consulta"))
				|| (usuario.getTipo().equals("auditoria") || (usuario.getTipo().equals("DEP_ADMIN"))|| (usuario.getTipo().equals("DRC_ADMIN")))) {
			System.out.println("Salto: " + CuotasServletUtils.SALTO_PERSONA_MANTENIMIENTO_DEP_DRY);
			setSalto(CuotasServletUtils.SALTO_PERSONA_MANTENIMIENTO_DEP_DRY);
		}

	}

	public List<PersonasBeca> cargaPersonasBeca(Iterator<Inscripcion> inscripciones) {
		List<PersonasBeca> personasBeca = new ArrayList<PersonasBeca>();
		PersonasBeca personaBecaAdd = null;
		while (inscripciones.hasNext()) {
			Inscripcion inscripcion = inscripciones.next();
			//
			if (inscripcion.getPersonasBeca() != null) {
				personaBecaAdd = inscripcion.getPersonasBeca();
				personasBeca.add(personaBecaAdd);
				// System.out.println("personaBeca: " + personaBecaAdd.getId() +
				// "- "+personaBecaAdd.getDescuentoBeca());
			}
		}
		System.out.println("salio?");
		return personasBeca;

	}

	public void filtrar() {
		UsuarioCuotas usuario = (UsuarioCuotas) session.getAttribute("usuario");

		System.out.println("usuario: " + usuario.getUsuario() + " - " + usuario.getId() + " - " + usuario.getTipo());

		String apellido, nombre, tipoDocumento, numeroDocumento, cohorte, idCarrera, email;
		Integer maxResults, firstResult;
		// Se cambia el salto al salto de chequera.
		String salto;
		String operacionDos = request.getParameter("operacionDos");
		if (operacionDos == null) {
			operacionDos = (String) request.getAttribute("operacionDos");
		}
		salto = request.getParameter("salto");
		apellido = request.getParameter("apellido");
		nombre = request.getParameter("nombre");
		tipoDocumento = request.getParameter("tipoDocumento");
		numeroDocumento = request.getParameter("numeroDocumento");
		cohorte = request.getParameter("cohorte");
		idCarrera = request.getParameter("idCarrera");
		email = request.getParameter("email");

		maxResults = 300;
		firstResult = 0;

		String pagNumero = request.getParameter("paginaNumero");
		Integer paginaNumero;

		// Borrar
		System.out.println("tipoDocumento: " + tipoDocumento + "- pagNumero: " + pagNumero);

		// Dependiendo si es la 1ra pagina o las siguientes cargo del form o la
		// session.
		Persona personaBuscada = null;
		Carrera carreraBuscada = null;

		if (pagNumero == null) {
			paginaNumero = new Integer(0);
			// Si es la primera pagina cargo los valores de busqueda
			if (idCarrera != null && !idCarrera.equals("0")) {
				carreraBuscada = FactoryDAO.carreraDAO.findByLong(idCarrera);
			}
			Documento documentoBuscado = new Documento();
			if (tipoDocumento != null && !tipoDocumento.equals("")) {
				documentoBuscado.setTipo(new TipoDocumento(tipoDocumento));
			}
			if (numeroDocumento != null && !numeroDocumento.equals("")) {
				documentoBuscado.setNumero(numeroDocumento);
			}

			personaBuscada = new Persona();
			if (nombre != null && !nombre.equals("")) {
				personaBuscada.setNombre(nombre);
			}
			if (apellido != null && !apellido.equals("")) {
				personaBuscada.setApellido(apellido);
			}
			if (documentoBuscado != null && documentoBuscado != null) {
				personaBuscada.setDocumento(documentoBuscado);
				System.out.println(documentoBuscado.getNumero());
			}
			/* PRUEBA BUSCAR POR MAIL */
			if (email != null && !email.equals("")) {
				email = "%" + email + "%";
				personaBuscada.setMail(email);
			}

		} else {

			personaBuscada = (Persona) session.getAttribute("personaBuscada");
			carreraBuscada = (Carrera) session.getAttribute("carreraBuscada");

			paginaNumero = Integer.parseInt(pagNumero);
		}

		firstResult = maxResults * paginaNumero;
		List<Persona> personas = null;
		Integer cohorteInt = null;

		if (cohorte != null && !cohorte.equals("")) {
			cohorteInt = Integer.parseInt(cohorte);
			System.out.println("cohorte: " + cohorteInt);
		}

		if (operacionDos != null && operacionDos.equals("filtrarMora")) {
			System.out.println("EntroOperacionDos: " + operacionDos);
			String adeuda = request.getParameter("adeudaCuotasCheck");
			String vieneDelServlet = request.getParameter("vieneDelServlet");
			String sancionarPersonas = request.getParameter("sancionarPersonas");
			String aniosTope = request.getParameter("aniosTope");
			String filtrarJsp = request.getParameter("filtrarJsp");

			System.out.println("sancionarPersonas: " + sancionarPersonas + " aniosTope: " + aniosTope);
			System.out.println("idcarrera " + idCarrera);

			maxResults = null;
			firstResult = null;
			if (adeuda == null && vieneDelServlet == null) {
				adeuda = "off";
			}
			if (aniosTope == null && vieneDelServlet == null) {
				aniosTope = "off";
			}
			if (vieneDelServlet != null && vieneDelServlet.equals("no")) {
				adeuda = "on";
				aniosTope = "on";
			}
			// List<Integer> anios = Anio.listAnios(1995, new
			// GregorianCalendar().get(Calendar.YEAR)+5);
			if ((apellido != null && nombre != null && numeroDocumento != null && idCarrera != null)
					|| (filtrarJsp != null)) {
				// if(!apellido.equals("") || !nombre.equals("") ||
				// !numeroDocumento.equals("") || !idCarrera.equals("") ||
				// (filtrarJsp.equals("true") && !aniosTope.equals("0"))){
				personas = FactoryDAO.personaDAO.filtrarPorMora(personaBuscada, carreraBuscada, cohorteInt, maxResults,
						firstResult, null, adeuda, aniosTope);
				System.out.println("cohorte: " + cohorteInt);

				// }
			}
			request.setAttribute("aniosTope", aniosTope);
			request.setAttribute("sancionarPersonas", sancionarPersonas);
			request.setAttribute("adeudaCuotas", adeuda);
			request.setAttribute("cohorte", cohorte);
			System.out.println("Va a ir al JSP");
			salto = "/persona/PersonaCuotasMora.jsp";
		} else {
			System.out.println("usuario: " + usuario.toString());
			personas = FactoryDAO.personaDAO.filtrar(personaBuscada, carreraBuscada, cohorteInt, maxResults,
					firstResult, null, usuario);
			System.out.println("cohorte: " + cohorteInt);
		}

		request.setAttribute("paginaNumero", paginaNumero);

		List<String> tiposDocumento = FactoryDAO.mapeoTipoDocumentoDAO.listTiposDocumento();
		// List<TipoDocumento> tipoDocumentos =
		// FactoryDAO.tipoDocumentoDAO.listAll();
		// List<Carrera> carreras = FactoryDAO.carreraDAO.listAll();
		List<Carrera> carreras = null;
		if (usuario.getTipo().equals("admin") || usuario.getTipo().equals("auditoria")) {
			carreras = FactoryDAO.carreraDAO.listAll("codigo", true);
		} else {
			carreras = FactoryDAO.carreraDAO.listAllbyUsr(usuario);
			/*
			 * Iterator<Carrera> carrerasIt = carreras.iterator(); while
			 * (carrerasIt.hasNext()){ Carrera c = carrerasIt.next();
			 * System.out.println("Carrrera: " + c.getCodigo() + " - " +
			 * c.getNombreCarrera()); }
			 */
		}

		request.setAttribute("tiposDocumento", tiposDocumento);
		session.setAttribute("personaBuscada", personaBuscada);
		session.setAttribute("carreraBuscada", carreraBuscada);
		request.setAttribute("personas", personas);
		request.setAttribute("tipoDocumentos", tiposDocumento);
		request.setAttribute("carreras", carreras);

		if (usuario.getTipo().equals("admin") || (usuario.getTipo().equals("DEP_ADMIN"))|| (usuario.getTipo().equals("DRC_ADMIN")) || (usuario.getTipo().equals("consulta"))) {
			System.out.println(salto);
			setSalto(salto);
		} else {
			System.out.println(CuotasServletUtils.SALTO_PERSONA_LISTAR_DEP_DRYC + " - " + usuario.getTipo());
			setSalto(CuotasServletUtils.SALTO_PERSONA_LISTAR_DEP_DRYC);
		}
	}

	/**
	 * FIXME -- corregir codigo
	 */
	public void filtrarJson() {
		String apellido, nombre, tipoDocumento, numeroDocumento, cohorte, idCarrera;
		Integer maxResults, firstResult;
		// Se cambia el salto al salto de chequera.
		String salto;
		salto = request.getParameter("salto");
		System.out.println("Salto: " + salto);
		apellido = request.getParameter("apellido");
		nombre = request.getParameter("nombre");
		tipoDocumento = request.getParameter("tipoDocumento");
		numeroDocumento = request.getParameter("numeroDocumento");
		// cohorte = request.getParameter("cohorte");
		idCarrera = request.getParameter("idCarrera");
		maxResults = 300;
		firstResult = 0;

		String pagNumero = request.getParameter("paginaNumero");

		Integer paginaNumero;

		// Dependiendo si es la 1ra pagina o las siguientes cargo del form o la
		// session.
		Persona personaBuscada = null;
		Carrera carreraBuscada = null;

		if (pagNumero == null) {
			paginaNumero = new Integer(0);

			// Si es la primera pagina cargo los valores de busqueda
			carreraBuscada = FactoryDAO.carreraDAO.findByLong(idCarrera);
			Documento documentoBuscado = new Documento();
			if (tipoDocumento != null)
				documentoBuscado.setTipo(new TipoDocumento(tipoDocumento));
			if (numeroDocumento != null)
				documentoBuscado.setNumero(numeroDocumento);
			personaBuscada = new Persona();
			personaBuscada.setNombre(nombre);
			personaBuscada.setApellido(apellido);
			personaBuscada.setDocumento(documentoBuscado);
		} else {
			personaBuscada = (Persona) session.getAttribute("personaBuscada");
			carreraBuscada = (Carrera) session.getAttribute("carreraBuscada");

			paginaNumero = Integer.parseInt(pagNumero);
		}
		firstResult = maxResults * paginaNumero;
		List<Persona> personas = FactoryDAO.personaDAO.filtrar(personaBuscada, carreraBuscada, null, maxResults,
				firstResult, null, null);

		// Convertir a JSON las personas
		// Enviar por JSON paginaNumero y personas
		JSONObject respuestaJson = new JSONObject();

		Iterator<Persona> itPersonas = null;
		if (personas != null)
			itPersonas = personas.iterator();

		respuestaJson.put("personas", FactoryJson.personaJson.getJsonLight(itPersonas));
		System.out.println("respuestaJson.toString: " + respuestaJson.toString());
		respuestaJson.put("paginaNumero", paginaNumero);

		System.out.println(respuestaJson.toString());
		this.enviarJson(response, respuestaJson.toString());
	}

	public void agregarInscripcion() {
		UsuarioCuotas usuario = (UsuarioCuotas) session.getAttribute("usuario");
		
		
		String idCarrera = request.getParameter("idCarrera");
		String idPersona = request.getParameter("idPersona");
		Persona persona = FactoryDAO.personaDAO.findByLong(idPersona, true);
		String operacionAnterior = request.getParameter("operacionAnterior");

		guardarInscripcion(idCarrera, persona);
		persona = FactoryDAO.personaDAO.findByLong(persona.getId().toString(), true);
		// List<Carrera> carreras = FactoryDAO.carreraDAO.listAll();
		usuario = FactoryDAO.usuarioDAO.findByLong(usuario.getId().toString());
		List<Carrera> carreras = null;
		if(usuario.getTipo().equals("admin")){
			carreras = FactoryDAO.carreraDAO.listAll("codigo", true);
		}else{
			carreras = FactoryDAO.carreraDAO.listAllbyUsr(usuario);
		}
		
		List<String> tiposDocumento = FactoryDAO.mapeoTipoDocumentoDAO.listTiposDocumento();
		List<Beca> becas = FactoryDAO.becaDAO.listAll();

		List<PersonasBeca> personasBeca = null;
		if (persona != null) {
			personasBeca = cargaPersonasBeca(persona.getInscripciones().iterator());
		}
		request.setAttribute("personasBecas", personasBeca);
		request.setAttribute("becas", becas);
		request.setAttribute("tiposDocumento", tiposDocumento);
		request.setAttribute("persona", persona);
		request.setAttribute("carreras", carreras);

		request.setAttribute("operacion", operacionAnterior);
		request.setAttribute("msg", "La carrera fue guardada con exito");
	
		if( (usuario.getTipo().equals("admin")) ){
			setSalto(CuotasServletUtils.SALTO_PERSONA_MANTENIMIENTO);
		}else if( (usuario.getTipo().equals("DEP_ADMIN"))|| (usuario.getTipo().equals("DRC_ADMIN")) ){
			setSalto(CuotasServletUtils.SALTO_PERSONA_MANTENIMIENTO_DEP_DRY);
		}
	}

	public void borrarInscripcion() {
		String idCarrera = request.getParameter("idCarrera");
		String idPersona = request.getParameter("idPersona");
		String operacionAnterior = request.getParameter("operacionAnterior");
		/*
		 * String idInscripcion = request.getParameter("idInscripcion");
		 * 
		 * PersonasBeca perBeca =
		 * FactoryDAO.personasBecaDAO.findByLong(idInscripcion);
		 * if(perBeca!=null){ FactoryDAO.personasBecaDAO.delete(perBeca); }
		 */
		Persona persona = FactoryDAO.personaDAO.findByLong(idPersona, true);
		Carrera carrera = FactoryDAO.carreraDAO.findByLong(idCarrera, true);

		Inscripcion inscripcion = FactoryDAO.inscripcionDAO.findInscripcion(persona, carrera);
		if (inscripcion != null) {
			persona.getInscripciones().remove(inscripcion);
			FactoryDAO.inscripcionDAO.delete(inscripcion);
			FactoryDAO.personaDAO.saveOrUpdate(persona);
		}
		persona = FactoryDAO.personaDAO.findByLong(persona.getId().toString(), true);

		// List<Carrera> carreras = FactoryDAO.carreraDAO.listAll();
		List<Carrera> carreras = FactoryDAO.carreraDAO.listAll("codigo", true);
		List<String> tiposDocumento = FactoryDAO.mapeoTipoDocumentoDAO.listTiposDocumento();
		List<Beca> becas = FactoryDAO.becaDAO.listAll();

		List<PersonasBeca> personasBeca = null;
		if (persona != null) {
			personasBeca = cargaPersonasBeca(persona.getInscripciones().iterator());
		}
		request.setAttribute("personasBecas", personasBeca);
		request.setAttribute("becas", becas);
		request.setAttribute("tiposDocumento", tiposDocumento);
		request.setAttribute("operacion", operacionAnterior);
		request.setAttribute("persona", persona);
		request.setAttribute("carreras", carreras);
		setSalto(CuotasServletUtils.SALTO_PERSONA_MANTENIMIENTO);
	}

	public void exceptuarPersonas() {
		String lista = request.getParameter("liPersonas");
		String operacion = request.getParameter("operacion");
		Persona p = new Persona();
		System.out.println(lista);
		// public void marcarPago (String legajo, String pago, String carrera)
		p.sancionarExceptuarPersonas("Si", lista);
		request.setAttribute("operacionDos", "filtrarMora");
		filtrar();

	}

	public void sancionarPersonas() {
		String lista = request.getParameter("liPersonas");
		String operacion = request.getParameter("operacion");
		Persona p = new Persona();
		p.sancionarExceptuarPersonas("No", lista);
		request.setAttribute("operacionDos", "filtrarMora");
		filtrar();
	}

	public List<String> buscaNroLegajos() {
		List<String> nroLegajos = null;

		return nroLegajos;
	}
}
