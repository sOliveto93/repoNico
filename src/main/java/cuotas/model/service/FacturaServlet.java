package cuotas.model.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.pentaho.reporting.engine.classic.core.ReportProcessingException;

import common.model.domain.datos.Estado;
import common.model.domain.datosPersonales.Documento;
import common.model.domain.datosPersonales.TipoDocumento;
import common.model.domain.error.ErrorGrabacion;
import common.model.domain.fecha.Fecha;
//import common.model.domain.util.Evento;
import common.model.domain.validacion.UsuarioGenerico;
import common.model.reports.AbstractReportGenerator;
import common.model.reports.GenericIteratorReport;
import common.model.service.MappingMethodServlet;
import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.Inscripcion;
import cuotas.model.domain.datos.OrigenPago;
import cuotas.model.domain.datos.Pago;
import cuotas.model.domain.datos.Persona;
import cuotas.model.domain.datos.UsuarioCuotas;
import cuotas.model.domain.factura.Factura;
import cuotas.model.domain.factura.FacturaItem;
import cuotas.model.domain.factura.FacturaItemTipo;
import cuotas.model.domain.factura.FormaPago;
import cuotas.model.domain.factura.RubroCobro;
import cuotas.model.domain.factura.SucursalCobro;
import cuotas.model.domain.factura.TipoFactura;
import cuotas.model.domain.factura.TipoImpresion;
import cuotas.model.domain.factura.TipoPago;
import cuotas.model.domain.json.FactoryJson;
import cuotas.reports.src.FacturaDuplicadoIteratorReport;
import cuotas.reports.src.FacturaDuplicadoReportModelator;

public class FacturaServlet extends MappingMethodServlet {

	private static final long serialVersionUID = -8015276752346757729L;
	public FacturaServlet() {
		this.mapeoOperacionMetodo.put(CuotasServletUtils.ACTUALIZAR,
				"modificar");
	}
	
	public void listar() {
		UsuarioCuotas usuario = (UsuarioCuotas)session.getAttribute("usuario");
		usuario = FactoryDAO.usuarioDAO.findByLong(usuario.getId().toString());
		System.out.println("UsuarioOriginal: " + usuario.getNombre() + " - " + usuario.getTipo() + " - " + usuario.getId());
		if (request.getParameter("limpiarSession") != null
				&& request.getParameter("limpiarSession").equals("1")) {
			session.removeAttribute("factura");
			session.removeAttribute("personaBuscada");
			session.removeAttribute("facturaBuscada");
			session.removeAttribute("fechaDesdeBuscada");
			session.removeAttribute("fechaHastaBuscada");
			session.removeAttribute("facturasAnuladas");
			session.removeAttribute("facturas");
		}
		List<String> tiposDocumento = FactoryDAO.mapeoTipoDocumentoDAO.listTiposDocumento();
		List<TipoImpresion> tiposDeImpresion  = FactoryDAO.tipoImpresionDAO.listAll();
		List<TipoPago> tiposPago = FactoryDAO.tipoPagoDAO.listAll("id", false);
		
		request.setAttribute("tiposDocumento", tiposDocumento);
		request.setAttribute("tiposDeImpresion", tiposDeImpresion);
		request.setAttribute("tiposPago", tiposPago);
		session.setAttribute("usuario", usuario);
		setSalto(CuotasServletUtils.SALTO_FACTURA_LISTAR);
	}
	
	
	public void setearTipoImpresion() {
		  String idTipoImpresion = request.getParameter("idTipoImpresionPredeterminado");
		  TipoImpresion tipoImpresionPredeterminadoAnterior = FactoryDAO.tipoImpresionDAO.tipoImpresionPredeterminado();
		  TipoImpresion tipoImpresionPredeterminadoActual = null;
		  
		  if(idTipoImpresion!=null && !idTipoImpresion.equals("")){
			  tipoImpresionPredeterminadoActual = FactoryDAO.tipoImpresionDAO.findByInteger(idTipoImpresion);
			  tipoImpresionPredeterminadoActual.predeterminar();
			  tipoImpresionPredeterminadoAnterior.desactivar();
		  }
		  List<TipoImpresion> tiposDeImpresion = new ArrayList<TipoImpresion>();
		  tiposDeImpresion.add(tipoImpresionPredeterminadoActual);
		  tiposDeImpresion.add(tipoImpresionPredeterminadoAnterior);
		  System.out.println("TipoPredeterminado: " + tipoImpresionPredeterminadoActual.getDescripcion());
		  FactoryDAO.tipoImpresionDAO.saveOrUpdate(tiposDeImpresion);
		  listar();
	}

	public void alta() {
		int i = 0;
		List<SucursalCobro> sucursales = FactoryDAO.sucursalCobroDAO.listAll();
		List<RubroCobro> rubrosCobro = FactoryDAO.rubroCobroDAO.listAll();
		Factura factura = getEncabezadoFacturaActualizado(request);
	System.out.println("factura.FormaPago: "+ factura.getFormaPago().getId() + " - "+ factura.getFormaPago().getDescripcion());
		factura = getActualizaItemsFactura(request, factura);

		Iterator<FacturaItem> facturaItems = factura.getFacturaItems().iterator();

		List<TipoPago> tiposPago = FactoryDAO.tipoPagoDAO.listAll("id", false);
		List<FormaPago> formasPago = FactoryDAO.formaPagoDAO.listAll();
		List<OrigenPago> origenesPago = FactoryDAO.origenPagoDAO.listAll();
		List<TipoFactura> tiposFactura = FactoryDAO.tipoFacturaDAO.listAll();

		String persistir = request.getParameter("persistir");

		Iterator<OrigenPago> origenPago = origenesPago.iterator();

		String idOrigenPago = request.getParameter("origenPagoS");
		OrigenPago origenPagoFactura = FactoryDAO.origenPagoDAO.findByInteger(idOrigenPago);
		String grupo_numeros = (String) request.getParameter("numeros_grupos");
	
		request.setAttribute("grupo_numeros", grupo_numeros);
		// request.setAttribute("tiposFactura", tiposFactura);
		request.setAttribute("origenesPago", origenesPago);
		request.setAttribute("formasPago", formasPago);
		request.setAttribute("tiposFactura", tiposFactura);
		request.setAttribute("facturaItems", facturaItems);
		session.setAttribute("factura", factura);
		// request.setAttribute("facturaItems",facturaItems);
		request.setAttribute("sucursales", sucursales);
		request.setAttribute("rubrosCobro", rubrosCobro);
		request.setAttribute("tiposPago", tiposPago);

		if (persistir != null && persistir.equals("true")) {
			try {
				Integer numeroFacturaConsecutivo = cargaNumeroFactura(factura,	factura.getTipoPago());
				if (factura.getNro() == null)
					factura.setNro(numeroFacturaConsecutivo);

//				Evento evento = getEventoAuditoria(request, factura);
//				FactoryDAO.eventoDAO.save(evento, false);
				Iterator<FacturaItem> facturaItemIt = factura.getFacturaItemsIterator(); 
				factura = getActualizaItemsFactura(request, factura);
				// aca reviso los items para saber cual es codigo 9 para agregar a la descripcion del item la descripcion del rubro de ese momento.
				while(facturaItemIt.hasNext()){
					FacturaItem item = facturaItemIt.next();
					if(item.getFacturaItemTipo().getRubroCobro().getClass().equals("9")){
						item.setDescripcion(item.getDescripcion() + " - " + item.getFacturaItemTipo().getRubroCobro().getDescripcion());
						factura.getFacturaItems().remove(item);
						factura.getFacturaItems().add(item);
					}
				}
				
				
				FactoryDAO.facturaDAO.save(factura);
				//Se trae la factura nuevamente para poder imprimirla.
				factura = FactoryDAO.facturaDAO.buscarUltimaFactura(factura.getTipoPago());
				session.setAttribute("factura", factura);
				
				//System.out.println("IdFactura: "+factura.getId() + " - Numero: " + factura.getNro());
				TipoImpresion tipoImpresion  = FactoryDAO.tipoImpresionDAO.tipoImpresionPredeterminado();
				//System.out.println("TipoImpresionPredeterminado: "+tipoImpresion.toString());
				
				if(tipoImpresion.getDescripcion().toLowerCase().equals("continuo")){
					setSalto(CuotasServletUtils.SALTO_FACTURA_IMPRIMIR);
				}else if(tipoImpresion.getDescripcion().toLowerCase().equals("a4")){
					setSalto(CuotasServletUtils.SALTO_FACTURA_IMPRIMIR_A4); //SALTO_FACTURA_IMPRIMIR_A4
				}
			} catch (Exception e) {
				request.setAttribute("noBorrarFactura", "true");
				request.setAttribute("msg", e.getMessage());
				modificar();
			}
		}
		/*else{
			System.out.println("salgo factura");
			setSalto(CuotasServletUtils.SALTO_FACTURA_ALTA);
		}*/
	}
	public boolean grupoPertenecePersona(Persona persona, Grupo grupo) {
		boolean resp = false;
		Iterator<Pago> insPersona = persona.getPagos().iterator();
		while (insPersona.hasNext()) {
			Pago ins = insPersona.next();
			Grupo grupoComparar = ins.getGrupo();
			if (grupo.equals(grupoComparar)) {
				resp = true;
			}
		}
		return resp;
	}
	/*public boolean grupoPertenecePersona(Persona persona, Grupo grupo) {
		boolean resp = false;
		Iterator<Inscripcion> insPersona = persona.getInscripciones()
				.iterator();
		while (insPersona.hasNext()) {
			Inscripcion ins = insPersona.next();
			Carrera carrera = ins.getCarrera();
			Iterator<Grupo> gruposCarrera = carrera.getGrupos().iterator();
			while (gruposCarrera.hasNext()) {
				Grupo grupoComparar = gruposCarrera.next();
				if (grupo.equals(grupoComparar)) {
					resp = true;
				}
			}

		}
		return resp;
	}*/

	public void ConsultarFacturaItems() {
		String facturaItemsJson = "";
		Factura factura = (Factura) session. getAttribute("factura");
		//FacturaItem facturaItem = null;
		if (factura != null) {
			Iterator<FacturaItem> facturaItems = factura.getFacturaItemsIterator();
			facturaItemsJson = FactoryJson.facturaItemJson.toJson(facturaItems,	true, false);
		}
		System.out.println("FacturaItemsJson: " + facturaItemsJson);
		this.enviarJson(response, facturaItemsJson);
	}

	public void modificar() {
		String noBorrar = null;
		if (request.getAttribute("noBorrarFactura") != null) {
			noBorrar = (String) request.getAttribute("noBorrarFactura");
		}
		System.out.println("BORRAR: " + noBorrar);
		if (noBorrar == null || noBorrar.equals("false")) {
			session.removeAttribute("factura");
		}

		// Inicio prueba
		Factura factura = null;
		String idPersona = (String) request.getParameter("idPersona");
		if (!esNuloOVacio(idPersona)) {
			factura = new Factura();
			Persona persona = FactoryDAO.personaDAO.findByLong(idPersona);
			factura.setPersona(persona);
			request.setAttribute("factura", factura);
		}
		// fin de la prueba

		List<RubroCobro> rubrosCobro = FactoryDAO.rubroCobroDAO.listAll();
		List<SucursalCobro> sucursales = FactoryDAO.sucursalCobroDAO.listAll();
		List<TipoPago> tiposPago = FactoryDAO.tipoPagoDAO.listAll("id", false);
		List<TipoFactura> tiposFactura = FactoryDAO.tipoFacturaDAO.listAll();
		List<FormaPago> formasPago = FactoryDAO.formaPagoDAO.listAll();
		List<OrigenPago> origenesPago = FactoryDAO.origenPagoDAO.listAll();

		request.setAttribute("origenesPago", origenesPago);
		request.setAttribute("formasPago", formasPago);
		request.setAttribute("tiposFactura", tiposFactura);
		request.setAttribute("sucursales", sucursales);
		request.setAttribute("tiposPago", tiposPago);
		request.setAttribute("rubrosCobro", rubrosCobro);
		setSalto(CuotasServletUtils.SALTO_FACTURA_ALTA);
	}

	public void borrar() {
		try {
			String id = request.getParameter("id");
			Factura factura = FactoryDAO.facturaDAO.findByInteger(id);
			if (factura != null) {
				factura.getEstado().desactivar();
				factura.setDescripcionBasica("ANULADO");
				
				FactoryDAO.facturaDAO.saveOrUpdate(factura);

				factura = FactoryDAO.facturaDAO.findByInteger(factura.getId().toString(), true);
				
				
				String nombreArchivo = factura.getNro().toString() + ".pdf";
				String nombreArchivoAnulado = factura.getNro().toString()+"ANULADO"+".pdf";
				File oldfile = new File("C:/dup_facturas/"+nombreArchivo);
				File newfile = new File("C:/dup_facturas/"+nombreArchivoAnulado);
			     if (oldfile.renameTo(newfile)) {
			    	 	System.out.println("archivo renombrado");
			     } else {
			    	 	System.out.println("error");
			     }
			     
			}
		} catch (ErrorGrabacion e) {
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, e.getMSG());
		}
		filtrar();
	}

	public void informesFactura() {
		UsuarioCuotas usuario = (UsuarioCuotas)session.getAttribute("usuario");
		System.out.println("informesFacturaByUsr: "+usuario.getUsuario() + "-Tipo:"+usuario.getTipo());
		List<String> tiposDocumento = FactoryDAO.mapeoTipoDocumentoDAO.listTiposDocumento();
		List<RubroCobro> rubrosCobro =null;
		List<FacturaItemTipo> facturaItemTipos =null;
		String itemTipoUsuario = "";
		
		if(usuario.getTipo().toLowerCase().equals("admin") || usuario.getTipo().toLowerCase().equals("auditoria")){
			System.out.println("Usuario: "+usuario.toString() +" admin auditoria");
			rubrosCobro = FactoryDAO.rubroCobroDAO.listAll();	
		}else{
			System.out.println("Usuario: "+usuario.toString() + " dep drc admin ambos");
			facturaItemTipos = FactoryDAO.facturaItemTipoDAO.listAllbyUsr(usuario);
			//agregue esto para la validacion x deop and dryc
			itemTipoUsuario = FactoryJson.facturaItemTipoJson.toJson(facturaItemTipos, true, false);
			System.out.println("itemTipoUsuario: "+itemTipoUsuario);
			rubrosCobro = FactoryDAO.rubroCobroDAO.listAllbyUsr(usuario);
		}
		
		
		request.setAttribute("tiposDocumento", tiposDocumento);
		request.setAttribute("rubrosCobro", rubrosCobro);
		request.setAttribute("itemTipoUsuario",itemTipoUsuario);
		List<TipoPago> tiposPago = FactoryDAO.tipoPagoDAO.listAll("id", false);
		List<FormaPago> formasPago = FactoryDAO.formaPagoDAO.listAll();
		request.setAttribute("formasPago", formasPago);
		request.setAttribute("tiposPago", tiposPago);
		setSalto(CuotasServletUtils.SALTO_INFORMES_FACTURA);
	}

	public Factura getEncabezadoFacturaActualizado(HttpServletRequest request) {
		String descripcionBasica = request.getParameter("descripcionBasica");
		
		String descripcionExtendida = request.getParameter("descripcionExtendida");
		String idPersona = request.getParameter("idPersona");

		String fechaDePago = request.getParameter("fechaDePago");
		String idSucursalCobro = request.getParameter("idSucursalCobro");
		String idTipoPago = request.getParameter("tipoPago");
		String idFormaPago = request.getParameter("formaDePago");
		
		String responsableIva = request.getParameter("responsableIva");
		//se agrego por el item aberto
		/*String idRubroCobro = request.getParameter("idRubroCobro");
		String conceptoRubro = request.getParameter("conceptoRubro");
		RubroCobro rubroCobro = FactoryDAO.rubroCobroDAO.findByInteger(idRubroCobro);
		*/
		
		FormaPago formaPago = FactoryDAO.formaPagoDAO.findByInteger(idFormaPago);
		TipoPago tipoPago = FactoryDAO.tipoPagoDAO.findByInteger(idTipoPago);

		Fecha fecha = new Fecha(fechaDePago);

		String numeroFacturaS = request.getParameter("numeroFactura");
		Integer numeroFactura;
		System.out.println("numeroFacturaS: " + numeroFacturaS);

		Carrera carrera = FactoryDAO.carreraDAO.getCarreraPorDefecto();

		Factura factura;
		factura = (Factura) session.getAttribute("factura");

		if (factura == null)
			factura = new Factura();

		SucursalCobro sucursalCobro = FactoryDAO.sucursalCobroDAO.findByInteger(idSucursalCobro);
		Persona persona = FactoryDAO.personaDAO.findByLong(idPersona);

		numeroFactura = cargaNumeroFactura(factura, tipoPago);
		
		factura.setResponsableIva(responsableIva);
		factura.setTipoPago(tipoPago);
		factura.setPersona(persona);
		factura.setDescripcionBasica(descripcionBasica);
		
		//aca tendria que pasar la descripcion extendida de actualiza item
		String nombreCarrera = "";
		
		Iterator<Inscripcion> inscripciones = persona.getInscripciones().iterator();
		while (inscripciones.hasNext()) {
			Inscripcion inscripcion = inscripciones.next();
			if (inscripcion.getEstado().equals('A')) {
				nombreCarrera = inscripcion.getCarrera().getNombreCarrera();
			}
		}
		//factura.setDescripcionExtendida(nombreCarrera);
		
		if(descripcionExtendida == null || descripcionExtendida.equals("")){
			factura.setDescripcionExtendida(nombreCarrera);
		}else{
			factura.setDescripcionExtendida(descripcionExtendida);
		}
/*		
		*/
		factura.setSucursalCobro(sucursalCobro);
		factura.setFechaCarga(fecha);
		factura.setFechaPago(fecha);
		factura.setNro(numeroFactura);
		System.out.println("formaPago: " + formaPago.getId() + " - "+ formaPago.getDescripcion());
		factura.setFormaPago(formaPago);
		factura.setCarrera(carrera);
		
		return factura;
	}

	public Factura getActualizaItemsFactura(HttpServletRequest request,	Factura factura) {
		String idTiposRubroCobro = request.getParameter("idTiposRubroCobro");
		String cantidadItem = request.getParameter("cantidadItem");
		String idRubroCobro = request.getParameter("idRubroCobro");
		String idTipoFactura = request.getParameter("tipoFactura");
		String precioS = request.getParameter("precio");
		System.out.println("los parametros son "+idTiposRubroCobro+" "+cantidadItem+" "+idRubroCobro );
		String parametros[] = { idTiposRubroCobro, cantidadItem, idRubroCobro };

		//SE AGREGO PARA EL RUBRO 9 PLATA QUE INGRESA DE LO QUE SEA
		String conceptoRubro = request.getParameter("descripcionItem"); //en pantalla se muestran al reves.
		String descripcionItem = request.getParameter("conceptoRubro");
		//String cuit = request.getParameter("cuit");
		String razonSocial = request.getParameter("razonSocial");
		
		if (contieneNuloOVacio(parametros))
			return factura;

		Integer cantidad = Integer.parseInt(cantidadItem);
		TipoFactura tipoFactura = FactoryDAO.tipoFacturaDAO.findByInteger(idTipoFactura);
		FacturaItemTipo facturaItemTipo = FactoryDAO.facturaItemTipoDAO.findByInteger(idTiposRubroCobro);
		// si el rubro es 9 tengo que modificar la descripcion del mismo.
		//conceptoRubro
		RubroCobro rubroCobro = FactoryDAO.rubroCobroDAO.findByInteger(idRubroCobro);
		
		FacturaItem facturaItem = new FacturaItem();

		String codigo = rubroCobro.getCodigo().toString() + facturaItemTipo.getCodigo().toString();
		facturaItem.setCodigo(Integer.parseInt(codigo));

		System.out.println("CantidadItems: " + cantidadItem);

		facturaItem.setFacturaItemTipo(facturaItemTipo);

		Double precio = 0.0;
		if (precioS != null) {
			precio = Double.parseDouble(precioS);
		}

		facturaItem.setPrecio(precio);
		facturaItem.setCantidad(cantidad);
		facturaItem.setNro(factura.getNro());
		facturaItem.setFactura(factura);
		//if el codigo es 9(rubro) que cargue la descripcion de pantalla
		if(rubroCobro.getId().equals(9)){
			//EL PRIMER CAMPO TRAR LOS CODIGOS (el primer numero es el rubro y los siguientes son el Item_tipo=facturaItemTipo
			//EN EL SEGUNDO conceptoRubro TRAR LA DESCRIPCION DE FACTURA ITEM TIPO (CBR_TIPOS)
			//EN EL TERCER CAMPO TRAR LA DESCRIPCION DEL RUBRO
			//descipcion item trae la descripcion del cbr_tipos q es facturaItemTipo
			facturaItem.setDescripcion(descripcionItem+" - "+conceptoRubro); // para que se guarde en cbr_item tanto el item como el rubro los concateno y en patanlla
			rubroCobro.setDescripcion(conceptoRubro);
			FactoryDAO.rubroCobroDAO.saveOrUpdate(rubroCobro,true);
			
		}else{
			facturaItem.setDescripcion(facturaItemTipo.getDescripcion());
		}
		
		factura.setTipoFactura(tipoFactura);

		//Si el itemXgrupo es falso quiere decir que no es un item que represente 
		//una cuota, y se factura otro tipo de item
		Boolean itemXgrupo = false;

		String idOrigenPago = request.getParameter("origenPagoS");
		OrigenPago origenPagoFactura = FactoryDAO.origenPagoDAO.findByInteger(idOrigenPago);
		Double montoMora = 0.0;
		Integer cantidadItemsMora = 0;
		FacturaItem facturaItemMora = new FacturaItem();
		Persona persona = factura.getPersona();
		Iterator<Grupo> grupos = null;
		grupos = facturaItem.getFacturaItemTipo().getGrupos().iterator();
		
		while (grupos.hasNext()) {
			Grupo grupo = grupos.next();
			Integer itemsPagados = 0;
			String ItemString = "";
			if (grupo.getFacturaItemTipo() != null) {
				Boolean grupoPertenecePersona = grupoPertenecePersona(persona, grupo);
				if (grupo.getFacturaItemTipo().getId().equals(facturaItemTipo.getId()) && grupoPertenecePersona) {
					//CAMBIE EL FLAG PORQUE SI QUERIA COBRAR UN ITEM SIN GRUPO, ENTRABA POR EL RANGO DE LOS GRUPOS 8000
					//Y NO AGREGABA EL ITEM. JF 16/04/2019
					itemXgrupo = true;
					Iterator<Pago> pagosPersona = FactoryDAO.pagoDAO.pagosPorPersona(persona, grupo,true).iterator();
					while (pagosPersona.hasNext()) {
						Pago pago = pagosPersona.next();
						if (facturaItem.getCantidad() > itemsPagados && pago.isDadodebaja() == false && pago.getFechaPgo() == null 
								&&  !existePagoenFactura(factura,pago)) {
				
							System.out.println("Pagando Cuota... " + pago.toString()); //&& pago.getMonPgo() <= 0
							Fecha fechaActual = new Fecha(Fecha.getFechaActual());
							UsuarioGenerico usuario = (UsuarioGenerico) session.getAttribute("usuario");
						
							//mora INICIO
							double totalMontos = pago.getMonto1()+pago.getMonto2();
                            
							pago.setMontoMora(pago.calcularMora(grupo.getVtoDias(),pago.getFecha().getMes(), pago.getFecha().getAnio(),totalMontos));
							
							Fecha fechaPago = new Fecha (pago.getFecha().getFecha());
							//Sumo los dias de vencimiento que tenga el grupo
							Integer diasVencimientoGrupo  = grupo.getVtoDias();
							
							fechaPago.sumarDias(diasVencimientoGrupo-1);
							 if ((pago.getMontoMora() != null  && pago.getMontoMora() > 0 && grupo.getCobraMora().equals('S') && pago.isDadodebaja())|| !pago.isExceptuarMora()){
								
								 pago.setMonPgo(facturaItem.getPrecio()+ pago.getMontoMora());
								 montoMora = montoMora + pago.getMontoMora();
								 cantidadItemsMora++;
							} else {
								//mora
								pago.setMonPgo(facturaItem.getPrecio());
							}
							pago.setFechaPgo(fechaActual);
							pago.setFePgoCarga(fechaActual);
							pago.setOper(usuario.getUsuario());

							pago.setOrigenPago(origenPagoFactura);
							pago.setProcesado(false);
							
							pago.setFacturaItem(facturaItem);
							facturaItem.addPago(pago);

							itemsPagados++;
							//System.out.println("agrego el item: " + facturaItem.toString());
							//factura.addFacturaItem(facturaItem, false);
						}
					}
					factura.addFacturaItem(facturaItem, false);
					if(montoMora>0 && grupo.getCobraMora().equals('S')){
						//agregando el item mora
						RubroCobro rubroCobroMora = FactoryDAO.rubroCobroDAO.findByInteger("1");
						Integer codigoItemTipo = grupo.getFacturaItemTipoMora().getCodigo();
						
						FacturaItemTipo facturaItemTipoMora = FactoryDAO.facturaItemTipoDAO.buscarPorCodigo(codigoItemTipo,rubroCobroMora);

						String codigoMora = rubroCobroMora.getCodigo().toString() + facturaItemTipoMora.getCodigo().toString();
						facturaItemMora.setFacturaItemTipo(facturaItemTipoMora);
						facturaItemMora.setCodigo(Integer.parseInt(codigoMora));
						facturaItemMora.setFactura(factura);
						facturaItemMora.setCantidad(1);
						facturaItemMora.setNro(factura.getNro());
						facturaItemMora.setPrecio(montoMora);
						facturaItemMora.setCodigo(Integer.parseInt(codigoMora));
						facturaItemMora.setDescripcion("numero de cuotas con mora "+cantidadItemsMora);
						factura.addFacturaItem(facturaItemMora, true);
					}
				}						
			}

			/*
			if(montoMora>0 && grupo.getCobraMora().equals('S')){
				//agregando el item mora
				RubroCobro rubroCobroMora = FactoryDAO.rubroCobroDAO.findByInteger("1");
				Integer codigoItemTipo = grupo.getFacturaItemTipoMora().getCodigo();
				
				FacturaItemTipo facturaItemTipoMora = FactoryDAO.facturaItemTipoDAO.buscarPorCodigo(codigoItemTipo,rubroCobroMora);

				String codigoMora = rubroCobroMora.getCodigo().toString() + facturaItemTipoMora.getCodigo().toString();
				facturaItemMora.setFacturaItemTipo(facturaItemTipoMora);
				facturaItemMora.setCodigo(Integer.parseInt(codigoMora));
				facturaItemMora.setFactura(factura);
				facturaItemMora.setCantidad(1);
				facturaItemMora.setNro(factura.getNro());
				facturaItemMora.setPrecio(montoMora);
				facturaItemMora.setCodigo(Integer.parseInt(codigoMora));
				facturaItemMora.setDescripcion("numero de cuotas con mora "+cantidadItemsMora);
				factura.addFacturaItem(facturaItemMora, true);
			}
			*/
		}
		//ACA TEERMINA DE HACER TODO LOQUE DE BE CON LOS GRUPOS
		/*if(cuit!=null && !cuit.equals("")){
			factura.setCuit(cuit);
		}*/
		if(razonSocial!=null && !razonSocial.equals("")){
			factura.setRazonSocial(razonSocial);
		}
		System.out.println("ItemXgrupo: " + itemXgrupo);
		if(!itemXgrupo){
			factura.addFacturaItem(facturaItem, false);
		}
		return factura;
	}
	
	public boolean existePagoenFactura(Factura factura,Pago pago){
		boolean respuesta = false;
		Iterator<FacturaItem> facturaItems = factura.getFacturaItems().iterator();
		while(facturaItems.hasNext()){
			FacturaItem facturaItemAux = facturaItems.next();
			Iterator<Pago> facturaItemPagos = facturaItemAux.getPagos().iterator();
			//System.out.println(facturaItemAux.getPagos().size());
			while(facturaItemPagos.hasNext()){
				Pago fip = facturaItemPagos.next();
				if(fip.equals(pago)){
					//System.out.println("FacturaPago: " + fip.toString());	
					respuesta = true;
				}
			}
		}
		return respuesta;
	}
	public void verFacturaJson() {
		String facturaJson = "";
		String idFactura = request.getParameter("idFactura");
		Factura factura = FactoryDAO.facturaDAO.findByInteger(idFactura,true);

		if (factura != null) {
			facturaJson = FactoryJson.facturaJson.toJson(factura, true);
		}
		System.out.println("JSON: " + facturaJson);
		this.enviarJson(response, facturaJson);
	}
	// - TODO REVISAR PORQ NO CAMBIA LA FACTURA
	public void cambiaNumeroFactura() {
		String nuevoNumeroFactura = request.getParameter("nuevoNumeroFactura");
		String msj = "";
		Factura factura = (Factura) session.getAttribute("factura");
		String json = "";

		if (factura != null
				&& (nuevoNumeroFactura != null && !nuevoNumeroFactura
						.equals(""))) {
			Integer numeroFactura = Integer.parseInt(nuevoNumeroFactura);
			factura.setNro(numeroFactura);
			session.setAttribute("factura", factura);
			msj = "Cambio satisfactorio.";
		} else {
			msj = "Error en la operacion.";
		}
		json = FactoryJson.facturaJson.getJsonMsj(msj);
		System.out.println("JSONmsj: " + json);
		this.enviarJson(response, json);
	}

	public void verFacturaItemTiposJson() {
		//System.out.println("verFacturaItemTiposJson");
		String id = request.getParameter("id");
		RubroCobro rubroCorbro = FactoryDAO.rubroCobroDAO.findByInteger(id);

		String facturaItemTipoJson = "";

		if (rubroCorbro != null) {
			Iterator<FacturaItemTipo> facturaItemTipos = rubroCorbro
					.getFacturaItemTipo().iterator();
			facturaItemTipoJson = FactoryJson.facturaItemTipoJson.toJson(
					facturaItemTipos, true);
		}
		System.out.println("JSON: " + facturaItemTipoJson);
		this.enviarJson(response, facturaItemTipoJson);
	}

	public void filtrar() {
		String numeroFactura, fechaFacturaDesde, fechaFacturaHasta, numeroDocumento, tipoDocumento, facturasAnuladas, idTipoPago;
		Integer maxResults, firstResult;

		facturasAnuladas = request.getParameter("facturasAnuladas");
		numeroFactura = request.getParameter("numeroFactura");
		fechaFacturaDesde = request.getParameter("fechaFacturaDesde");
		fechaFacturaHasta = request.getParameter("fechaFacturaHasta");
		numeroDocumento = request.getParameter("numeroDocumento");
		tipoDocumento = request.getParameter("tipoDocumento");
		idTipoPago = request.getParameter("tipoPago");

		String pagNumero = request.getParameter("paginaNumero");
		Integer paginaNumero;
		maxResults = 300;

		Fecha fechaHastaBuscada = null;
		Fecha fechaDesdeBuscada = null;
		Persona personaBuscada = null;
		Factura facturaBuscada = null;
		TipoPago tipoPago = null;

		if (pagNumero == null) {
			paginaNumero = new Integer(0);

			facturaBuscada = new Factura();
			Documento documento = null;

			if ((tipoDocumento != null && !tipoDocumento.equals(""))
					|| (numeroDocumento != null && !numeroDocumento.equals(""))) {
				documento = new Documento();
				if (tipoDocumento != null && !tipoDocumento.equals("")) {
					documento.setTipo(new TipoDocumento(tipoDocumento));
				}
				documento.setNumero(numeroDocumento);

				personaBuscada = new Persona();
				personaBuscada = FactoryDAO.personaDAO
						.buscarPorDocumento(documento);

				if (personaBuscada != null) {
					facturaBuscada.setPersona(personaBuscada);
				} else {
					personaBuscada = new Persona();
					personaBuscada.setDocumento(documento);
					facturaBuscada.setPersona(personaBuscada);
				}
			}
			if (numeroFactura != null && !numeroFactura.equals(""))
				facturaBuscada.setNro(Integer.parseInt(numeroFactura));

			if (fechaFacturaDesde != null && !fechaFacturaDesde.equals("")) {
				fechaDesdeBuscada = new Fecha(fechaFacturaDesde);
				// facturaBuscada.setFechaPago(fecha);
			}
			if (fechaFacturaHasta != null && !fechaFacturaHasta.equals("")) {
				fechaHastaBuscada = new Fecha(fechaFacturaHasta);
				// facturaBuscada.setFechaPago(fecha);
			}
			tipoPago = FactoryDAO.tipoPagoDAO.findByInteger(idTipoPago);
		} else {
			personaBuscada = (Persona) session.getAttribute("personaBuscada");
			facturaBuscada = (Factura) session.getAttribute("facturaBuscada");
			fechaHastaBuscada = (Fecha) session
					.getAttribute("fechaHastaBuscada");
			fechaDesdeBuscada = (Fecha) session
					.getAttribute("fechaDesdeBuscada");
			facturasAnuladas = (String) session
					.getAttribute("facturasAnuladas");
			paginaNumero = Integer.parseInt(pagNumero);
		}
		firstResult = maxResults * paginaNumero;

		Estado estado = new Estado();
		if (facturasAnuladas == null) {
			estado.activar();
			facturaBuscada.setEstado(estado);
		} else {
			estado.desactivar();
			facturaBuscada.setEstado(estado);
		}

		// si se hace un submit sobre otro formulario que no sea el del filtro,
		// no llegan los valores de este, como los tenemos en la session los
		// cargamos desde alli
		if (facturasAnuladas == null && numeroFactura == null
				&& fechaFacturaDesde == null && fechaFacturaHasta == null
				&& numeroDocumento == null && tipoDocumento == null) {

			personaBuscada = (Persona) session.getAttribute("personaBuscada");
			facturaBuscada = (Factura) session.getAttribute("facturaBuscada");
			fechaHastaBuscada = (Fecha) session
					.getAttribute("fechaHastaBuscada");
			fechaDesdeBuscada = (Fecha) session
					.getAttribute("fechaDesdeBuscada");
			facturasAnuladas = (String) session
					.getAttribute("facturasAnuladas");

		}
		UsuarioCuotas usuario = (UsuarioCuotas) session.getAttribute("usuario");

		List<Factura> facturas = FactoryDAO.facturaDAO.filtrar(personaBuscada,
				facturaBuscada, fechaDesdeBuscada, fechaHastaBuscada,
				maxResults, firstResult, usuario, tipoPago, null, null);

		if (facturas == null) {
			request.setAttribute("msjError",
					"No se encontraron Facuturas con el/los filtro/s ingresado/s");
		}
		List<TipoPago> tiposPago = FactoryDAO.tipoPagoDAO.listAll("id", false);

		request.setAttribute("paginaNumero", paginaNumero);
		session.setAttribute("personaBuscada", personaBuscada);
		session.setAttribute("facturaBuscada", facturaBuscada);
		session.setAttribute("fechaHastaBuscada", fechaHastaBuscada);
		session.setAttribute("fechaDesdeBuscada", fechaDesdeBuscada);
		session.setAttribute("facturasAnuladas", facturasAnuladas);
		session.setAttribute("facturas", facturas);
		request.setAttribute("tiposPago", tiposPago);

		setSalto(CuotasServletUtils.SALTO_FACTURA_LISTAR);
	}

	public void verRubroJson() {

		String codigos = request.getParameter("codigos");
		System.out.println("codigos: "+codigos);
		String rubroCobroJson = "";
		RubroCobro rubroCobro = null;
		if (codigos.length() >= 4) {
			String rubro = codigos.substring(0, 2);
			System.out.println("Rubro: "+ rubro);
			Integer rubroInt = Integer.parseInt(rubro);
			//System.out.println("Aca entro: " + rubroInt);
			rubroCobro = FactoryDAO.rubroCobroDAO.buscarPorCodigo(rubroInt);
		}
		// mirar porque trae 30 km de Json.
		rubroCobroJson = FactoryJson.rubroCobroJson.toJson(rubroCobro, true);

		System.out.println("el JSON es " + rubroCobroJson);
		this.enviarJson(response, rubroCobroJson);
	}

	// hay que mover esto al servlet de persona no tiene que estar aca!
	// jforastier
	public void verPersonaJson() {
		String numeroDocumento = request.getParameter("numeroDocumento");
		String tipoDocumento = request.getParameter("tipoDocumento");
		String idPersona = request.getParameter("idPersona");
		Persona persona = null;

		if (!esNuloOVacio(idPersona))
			persona = FactoryDAO.personaDAO.findByLong(idPersona, true);
		else {
			TipoDocumento tipoBuscado = null;
			if (tipoDocumento != null && !tipoDocumento.equals("")) {
				tipoBuscado = new TipoDocumento(tipoDocumento);
			}
			Documento documentoBuscado = new Documento(numeroDocumento,
					tipoBuscado);
			persona = FactoryDAO.personaDAO
					.buscarPorDocumento(documentoBuscado);
		}
		String jsonPersona = FactoryJson.personaJson.toJson(persona, true);

		System.out.println("jsonPersona: " + jsonPersona);
		this.enviarJson(response, jsonPersona);
	}

	public void traerCuotasPersona() {
		String idPersona = request.getParameter("idPersona");
		String idGrupo = request.getParameter("idGrupo");

		System.out.println("idPersonas:" + idPersona + " - idGrupo: " + idGrupo);

		Persona persona = FactoryDAO.personaDAO.findByLong(idPersona,true);
		Grupo grupo = FactoryDAO.grupoDAO.findByLong(idGrupo,true);

		List<Pago> cuotasPersonas = FactoryDAO.pagoDAO.pagosPorPersona(persona,grupo, true);
		//Iterator<Pago> cuotasPersonasIt = cuotasPersonas.iterator();
		//System.out.println("cuotasPersonas.size(): "+ cuotasPersonas.size());
		/*while(cuotasPersonasIt.hasNext()){

		}*/
		String cuotasPersonasJson = FactoryJson.pagosJson.toJson(cuotasPersonas, true, false);
		System.out.println("cuotasPersonasJson: " + cuotasPersonasJson);
		this.enviarJson(response, cuotasPersonasJson);
	}

	public Integer cargaNumeroFactura(Factura factura, TipoPago tipoPago) {
		Integer idTipoPago = Integer.parseInt(request.getParameter("tipoPago"));
		String numeroFacturaS = request.getParameter("numeroFactura");
		Integer numeroFactura = null;

		if (numeroFacturaS != null && !numeroFacturaS.equals("")) {
			numeroFactura = Integer.parseInt(numeroFacturaS);
		}

		if (numeroFacturaS != null && !numeroFacturaS.equals("")&& (factura.getTipoPago().getId() == idTipoPago)) {
			numeroFactura = Integer.parseInt(numeroFacturaS);
		} else {
			Integer ultimoNumeroFactura = FactoryDAO.facturaDAO.buscarUltimoNumeroFactura(tipoPago);
			if (ultimoNumeroFactura != null) {
				numeroFactura = ultimoNumeroFactura + 1;
			} else {
				numeroFactura = 1;
			}

		}
		System.out.println("numeroFacturaS: " + numeroFacturaS);
		//System.out.println("IdTipoPago: " + idTipoPago);
		//if (factura.getTipoPago() != null) {
		//	System.out.println("Fac.IdTipoPago: " + factura.getTipoPago().getId());
		//}
		System.out.println("NumeroFactura: " + numeroFactura);
		return numeroFactura;
	}


	/*-----------------PRUEBA NUMERACION---------------*/

	public void modificarNroFacturas() {
		setSalto(CuotasServletUtils.SALTO_FACTURA_MODIF_NUMERO);
	}

	// mirar como recibe los parametros en grupo >> modificar >> generar o
	// imprimir facturas.
	public void consultarNumeroFacuras() {
		String desdeNumeroFactura = request.getParameter("desdeNro");
		String hastaNumeroFactura = request.getParameter("hastaNro");

		String nroFacAnulada = request.getParameter("nroFacAnulada");
		String nroBorrar = request.getParameter("nroBorrar");

		List<Factura> facturas = buscaFacturasRango(desdeNumeroFactura,
				hastaNumeroFactura);

		//System.out.println("Factura Cantidad: " + facturas.size());

		request.setAttribute("nroBorrar", nroBorrar);
		request.setAttribute("nroFacAnulada", nroFacAnulada);

		request.setAttribute("desdeNro", desdeNumeroFactura);
		request.setAttribute("hastaNro", hastaNumeroFactura);

		request.setAttribute("facturas", facturas);
		setSalto(CuotasServletUtils.SALTO_FACTURA_MODIF_NUMERO);
	}

	public List<Factura> buscaFacturasRango(String desdeNumeroFactura,
			String hastaNumeroFactura) {
		Integer desdeNro = null, hastaNro = null;
		System.out.println("desde - hasta: " + desdeNumeroFactura + " - " + hastaNumeroFactura);
		if (desdeNumeroFactura != null && !desdeNumeroFactura.equals("")) {
			desdeNro = Integer.parseInt(desdeNumeroFactura);
		}
		if (hastaNumeroFactura != null && !hastaNumeroFactura.equals("")) {
			hastaNro = Integer.parseInt(hastaNumeroFactura);
		}
		List<Factura> facturas = FactoryDAO.facturaDAO.buscarDesdeHasta(
				desdeNro, hastaNro);

		return facturas;
	}

	public void modificarNumeracionFacturas() {
		String desdeNumeroFactura = request.getParameter("desdeNro");
		String hastaNumeroFactura = request.getParameter("hastaNro");
		String numero = request
				.getParameter("incrementarDecrementarNumeroFactura");
		Integer i = 0;
		Integer numeroIncrementa = 0;
		/* INICIO PRUEBA */
		String nroFacturaborrar = request.getParameter("nroBorrar");
		String nroFacAnulada = request.getParameter("nroFacAnulada");

		String nroMover = request.getParameter("nroMover");
		String nroNuevo = request.getParameter("nroNuevo");
		Factura facturaNueva = null;
		Boolean crearFacturaAnulada = false;

		if (nroMover != null && nroNuevo != null) {
			Integer nroMoverInt = Integer.parseInt(nroMover);
			Integer nroNuevoInt = Integer.parseInt(nroNuevo);

			System.out.println("Mover numeracion de " + nroMover + " a "
					+ nroNuevo);

			Factura facturaAux = FactoryDAO.facturaDAO
					.buscarPorNumero(nroNuevoInt);
			facturaNueva = FactoryDAO.facturaDAO.buscarPorNumero(nroMoverInt);
			if (facturaAux != null && facturaAux.getNro() != null) {
				facturaNueva.setNro(facturaAux.getNro());
			}
		}

		Factura facturaBorrar = null;
		if (nroFacturaborrar != null && !nroFacturaborrar.equals("")) {
			Integer nroBorrar = Integer.parseInt(nroFacturaborrar);
			facturaBorrar = FactoryDAO.facturaDAO.buscarPorNumero(nroBorrar);
		} else {
			facturaBorrar = new Factura();
		}
		if (nroFacAnulada != null && !nroFacAnulada.equals("")) {
			System.out.println("nroFacturaAnulada: " + nroFacAnulada);
			Integer numeroAnulado = Integer.parseInt(nroFacAnulada);

			facturaBorrar.setNro(numeroAnulado);
			facturaBorrar.getEstado().desactivar();
			facturaBorrar.setDescripcionBasica("ANULADO");
			crearFacturaAnulada = true;
		} else {
			// borrar los items de la factura.
			System.out.println("FacturaBorrar: " + facturaBorrar.toString());

			Iterator<FacturaItem> facturaItems = FactoryDAO.facturaItemDAO
					.filtrar(facturaBorrar).iterator();
			while (facturaItems.hasNext()) {
				FacturaItem facturaItemBorrar = facturaItems.next();
				FactoryDAO.facturaItemDAO.delete(facturaItemBorrar);
				System.out.println("ITEM: " + facturaItemBorrar.toString());
			}
			FactoryDAO.facturaDAO.delete(facturaBorrar);
		}
		/* FIN PRUEBA */
		
		// aca muevo las facturas
		if (numero != null && !numero.equals("")) {
			numeroIncrementa = Integer.parseInt(numero);
		}
		List<Factura> facturas = buscaFacturasRango(desdeNumeroFactura,
				hastaNumeroFactura);
		while (i < facturas.size()) {
			Factura factura = facturas.get(i);
			Integer nroFactura = factura.getNro();
			Integer nuevoNumero = nroFactura + numeroIncrementa;
			factura.setNro(nuevoNumero);
			i++;
		}
		FactoryDAO.facturaDAO.saveOrUpdate(facturas);
		/* INICIO 2 */
		System.out.println("crearFacturaAnulada==" + crearFacturaAnulada);
		if (crearFacturaAnulada == true) {
			Integer nro = Integer.parseInt(nroFacAnulada);
			facturaBorrar.setNro(nro);
			FactoryDAO.facturaDAO.saveOrUpdate(facturaBorrar);
		}
		/* FIN 2 */
	}

	/*
	 * public void CantidadDeCuotasPersona(){ String idPersona =
	 * request.getParameter("idPersona"); String idGrupo =
	 * request.getParameter("idGrupo");
	 *
	 * System.out.println("idPersonas:"+idPersona+ " - idGrupo: "+ idGrupo);
	 *
	 * Persona persona = FactoryDAO.personaDAO.findByLong(idPersona); Grupo
	 * grupo = FactoryDAO.grupoDAO.findByLong(idGrupo);
	 *
	 * List<Pago> cuotasPersonas =
	 * FactoryDAO.pagoDAO.pagosPorPersona(persona,grupo); }
	 */
}