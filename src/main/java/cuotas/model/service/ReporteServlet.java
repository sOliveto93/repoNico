package cuotas.model.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;





import java.util.List;

import org.pentaho.reporting.engine.classic.core.ReportProcessingException;

import common.model.domain.datos.Estado;
import common.model.domain.datosPersonales.Documento;
import common.model.domain.datosPersonales.TipoDocumento;
import common.model.domain.fecha.Fecha;
import common.model.domain.fecha.RangoFecha;
import common.model.reports.AbstractReportGenerator;
import common.model.reports.AbstractReportModelator;
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
import cuotas.model.domain.factura.TipoPago;
import cuotas.reports.src.CancelacionesDeUnPeriodoReportModelator;
import cuotas.reports.src.CarreraIteratorReport;
import cuotas.reports.src.CarreraReportModelator;
import cuotas.reports.src.DeudasIteratorReport;
import cuotas.reports.src.DeudasPorGrupoCarreraIteratorReport;
import cuotas.reports.src.DeudasPorGrupoCarreraReportModelator;
import cuotas.reports.src.DeudasReportModelator;
import cuotas.reports.src.FacturaDuplicadoIteratorReport;
import cuotas.reports.src.FacturaDuplicadoReportModelator;
import cuotas.reports.src.FacturaIteratorReport;
import cuotas.reports.src.FacturaPorItemIteratorReport;
import cuotas.reports.src.FacturaPorItemReportModelator;
import cuotas.reports.src.FacturaReportModelator;
import cuotas.reports.src.GrupoPorCarreraIteratorReport;
import cuotas.reports.src.GrupoPorCarreraReportModelator;
import cuotas.reports.src.PagosIteratorReport;
import cuotas.reports.src.PagosReportModelator;
import cuotas.reports.src.PagosSinMontoIteratorReport;
import cuotas.reports.src.PagosSinMontoReportModelator;
import cuotas.reports.src.UnaPersonaIteratorReport;
import cuotas.reports.src.UnaPersonaReportModelator;

public class ReporteServlet extends MappingMethodServlet {

	
	private static final long serialVersionUID = 8213768759100304943L;
	private String parametros = "";
	private boolean mostrarCodigoMP = false;
	private boolean exportarExcel = false;

	
	public ReporteServlet(){
		
	}
	public void reporteCarreras(){
		parametros = "";
		Iterator<Inscripcion> inscripciones = getInscripciones();
		
		String template= null ;
		// Generate the report
		CarreraIteratorReport ar = new CarreraIteratorReport();
		
		ClassLoader classloader = ar.getClass().getClassLoader();    
		String personaConMail = request.getParameter("incluirMail");
		if(personaConMail != null){
			template = "cuotas/reports/templates/AlumnosPorCarreraConMail.prpt";			
		}else{
			template = "cuotas/reports/templates/AlumnosPorCarrera.prpt";			
		}
		
		ar.setTemplateURL(classloader.getResource(template));
		// Que iterator quiero que recorra
		ar.setResults(inscripciones);
		// Como quiero que haga el mapeo de datos entre el modelo y el reporte
		ar.setAm(new CarreraReportModelator());  
		// Genero el reporte
		try {
			response.setContentType("application/pdf");
			ar.generateReport(AbstractReportGenerator.OutputType.PDF, response.getOutputStream());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ReportProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Output the location of the file
		setSalto(CuotasServletUtils.SALTO_REPORTES);
		
	}
	
	public Iterator<Inscripcion> getInscripciones(){
		parametros = "";
		String cohorteS = request.getParameter("cohorte");
		String numeroDeDocumento = request.getParameter("numeroDeDocumento");
		String estado = request.getParameter("estado");
		String idCarrera = request.getParameter("idCarrera");
		if(idCarrera!= null && !idCarrera.equals("")){
			UsuarioCuotas usuario = (UsuarioCuotas)session.getAttribute("usuario");
			List<Carrera> carreras = FactoryDAO.carreraDAO.listAllbyUsr(usuario);
			if(!carreras.isEmpty()){
				idCarrera = carreras.get(0).getId().toString();
			}
		}
		String becados = request.getParameter("becado");	
		
		Integer cohorte=null;
		Integer nroDocumento=null;
		Carrera carrera= FactoryDAO.carreraDAO.findByLong(idCarrera);

		if(cohorteS != null &&  !cohorteS.equals("")){
			cohorte= Integer.parseInt(cohorteS);
		}
		if(numeroDeDocumento != null && !numeroDeDocumento.equals("")){
			nroDocumento = Integer.parseInt(numeroDeDocumento);
		}
		Iterator<Inscripcion> inscripciones= FactoryDAO.inscripcionDAO.buscarAlumnos(cohorte, estado, nroDocumento,carrera, becados);
		
		return inscripciones;
	}
	
	public void  reporteUnaPersona(){
		parametros = "";
		Iterator<Pago> pagos = getPagos("persona, grupo, numero");	
		String template= null ;
		// Generate the report
		UnaPersonaIteratorReport ar = new UnaPersonaIteratorReport();
		
		ClassLoader classloader = ar.getClass().getClassLoader();    

		template = "cuotas/reports/templates/UnaPersonaNormal.prpt";			
		
		ar.setTemplateURL(classloader.getResource(template));
		// Que iterator quiero que recorra
		ar.setResults(pagos);
		// Como quiero que haga el mapeo de datos entre el modelo y el reporte
		ar.setAm(new UnaPersonaReportModelator());  
		// Genero el reporte
		try {
			response.setContentType("application/pdf");
			ar.generateReport(AbstractReportGenerator.OutputType.PDF, response.getOutputStream());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ReportProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Output the location of the file
		setSalto(CuotasServletUtils.SALTO_REPORTES);
	
	}
	public void  reporteDeudas(){
		//AGREGAR EL MAIL AL LADO DEL NOMBRE DE LA PERSONA 
		parametros = "";
		String orderBy = "grupo, persona";
		Iterator<Pago> pagos = getPagos(orderBy);
		
		Boolean reporteResumido = Boolean.valueOf((request.getParameter("resumidoCheck")));
		String template= null ;
		// Generate the report
		DeudasIteratorReport ar = new DeudasIteratorReport();
		
		ClassLoader classloader = ar.getClass().getClassLoader();    
		//Que template quiero usar
		if(reporteResumido){			
			//pendiente
			template = "cuotas/reports/templates/DeudasResumido.prpt";
		}else{			
			template = "cuotas/reports/templates/DeudasNormal.prpt";			
		}
		
		ar.setTemplateURL(classloader.getResource(template));
		// Que iterator quiero que recorra
		ar.setResults(pagos);
		// Como quiero que haga el mapeo de datos entre el modelo y el reporte
		DeudasReportModelator dpm = new DeudasReportModelator();
		dpm.setParametros(parametros);
		ar.setAm(dpm);  
		// Genero el reporte
		try {
			response.setContentType("application/pdf");
			ar.generateReport(AbstractReportGenerator.OutputType.PDF, response.getOutputStream());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ReportProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Output the location of the file
		setSalto(CuotasServletUtils.SALTO_REPORTES);
	}
	public void  reporteDeudasPorGrupoCarrera(){
		parametros = "";
		String orderBy = "grupo, persona";
		Iterator<Pago> pagos = getPagos(orderBy);
		
		String template= null;
		// Generate the report
		DeudasPorGrupoCarreraIteratorReport ar = new DeudasPorGrupoCarreraIteratorReport();
		
		ClassLoader classloader = ar.getClass().getClassLoader();    
		//Que template quiero usar
		template = "cuotas/reports/templates/DeudasGrupoCarreraNormal.prpt";			
		
		
		ar.setTemplateURL(classloader.getResource(template));		
		// Que iterator quiero que recorra
		ar.setResults(pagos);
		// Como quiero que haga el mapeo de datos entre el modelo y el reporte
		DeudasPorGrupoCarreraReportModelator dpgc = new DeudasPorGrupoCarreraReportModelator();
		dpgc.setParametros(parametros);
		ar.setAm(dpgc);  
		// Genero el reporte
		try {
			response.setContentType("application/pdf");
			ar.generateReport(AbstractReportGenerator.OutputType.PDF, response.getOutputStream());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ReportProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Output the location of the file
		setSalto(CuotasServletUtils.SALTO_REPORTES);
	}

	public void reporteCancelacionesDeUnPeriodo(){
		
		parametros = "";
		String orderBy="grupo";
		String template= null ;
		Boolean reporteResumido = Boolean.valueOf((request.getParameter("resumidoCheck")));
		Boolean exportarXls = Boolean.valueOf(request.getParameter("exportarXls"));
		
		Iterator<Pago> pagos = getPagos(orderBy);

		// Generate the report
		PagosIteratorReport ar = new PagosIteratorReport();

		ClassLoader classloader = ar.getClass().getClassLoader();    
		//Que template quiero usar

		if(reporteResumido){
			template = "cuotas/reports/templates/CancelacionPorUnPeriodoResumido.prpt";
		}else{			
			if(!exportarXls){
				//template = "cuotas/reports/templates/CancelacionPorUnPeriodoNormal.prpt";
				template = "cuotas/reports/templates/CancelacionPorUnPeriodoNormalMercadoPago.prpt";
			}else if(exportarXls){
				template = "cuotas/reports/templates/CancelacionPorUnPeriodoNormalMercadoPagoExcel.prpt";	
			}
		}

		ar.setTemplateURL(classloader.getResource(template));
		// Que iterator quiero que recorra
		ar.setResults(pagos);

		// Como quiero que haga el mapeo de datos entre el modelo y el reporte
		CancelacionesDeUnPeriodoReportModelator cduprm = new CancelacionesDeUnPeriodoReportModelator();
		cduprm.setParametros(parametros);
		ar.setAm(cduprm);  

		// Genero el reporte
		try {
			if(!exportarXls){
				response.setContentType("application/pdf");
				ar.generateReport(AbstractReportGenerator.OutputType.PDF, response.getOutputStream());	
			}else if(exportarXls){
				response.setContentType("application/vnd.ms-excel");
				ar.generateReport(AbstractReportGenerator.OutputType.EXCEL, response.getOutputStream());
					
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ReportProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Output the location of the file
		setSalto(CuotasServletUtils.SALTO_REPORTES);
	}
	public void reporteCancelacionPorPartida(){
		parametros = "";
		String orderBy="partida";
		String template= null;
		Boolean reporteResumido = Boolean.valueOf((request.getParameter("resumidoCheck")));
		
		Iterator<Pago> pagos = getPagos(orderBy);

		// Generate the report
		PagosIteratorReport ar = new PagosIteratorReport();
		ClassLoader classloader = ar.getClass().getClassLoader();    
		//Que template quiero usar	
		
		template = "cuotas/reports/templates/CancelacionPorPartidaResumido.prpt";
				
		ar.setTemplateURL(classloader.getResource(template));
		// Que iterator quiero que recorra
		ar.setResults(pagos);
		// Como quiero que haga el mapeo de datos entre el modelo y el reporte
		CancelacionesDeUnPeriodoReportModelator cduprm = new CancelacionesDeUnPeriodoReportModelator();
		cduprm.setParametros(parametros);
		ar.setAm(cduprm);  
		
		// Genero el reporte
		try {
			response.setContentType("application/pdf");
			ar.generateReport(AbstractReportGenerator.OutputType.PDF, response.getOutputStream());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ReportProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Output the location of the file
		setSalto(CuotasServletUtils.SALTO_REPORTES);
	}
	public void reporteGrupoPorCarrera(){
		parametros = "";
		String idGrupo = request.getParameter("idGrupoInforme");
		Grupo grupo = FactoryDAO.grupoDAO.findByLong(idGrupo);
		
		Iterator<Persona> personasCarrera = FactoryDAO.personaDAO.personasPorCarreraGrupo(grupo).iterator();
		
		// Generate the report
		GrupoPorCarreraIteratorReport ar = new GrupoPorCarreraIteratorReport();
		ClassLoader classloader = ar.getClass().getClassLoader();    
		//Que template quiero usar
		ar.setTemplateURL(classloader.getResource("cuotas/reports/templates/GrupoPorCarrera.prpt"));
		// Que iterator quiero que recorra
		ar.setResults(personasCarrera);
		// Como quiero que haga el mapeo de datos entre el modelo y el reporte
		ar.setAm(new GrupoPorCarreraReportModelator());  
		// Genero el reporte
		try {
			response.setContentType("application/pdf");
			ar.generateReport(AbstractReportGenerator.OutputType.PDF, response.getOutputStream());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ReportProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Output the location of the file
		setSalto(CuotasServletUtils.SALTO_REPORTES);
	}
	public void reporteGrupo(Boolean mostrarMonto){
		parametros = "";
		String idGrupo = request.getParameter("idGrupoInforme");
		String cohorteSt = request.getParameter("cohorte");
		String nroDocumento = request.getParameter("nroDocumento");
		
		String plantilla = null;
		AbstractReportModelator<Pago> modelator = null ;
		GenericIteratorReport<Pago> reportIterator = null ;
		
		if(mostrarMonto){
			plantilla = "cuotas/reports/templates/CuotasPorGrupoMonto.prpt" ;
			modelator = new PagosReportModelator();
			reportIterator = new PagosIteratorReport();
		}else{ 
			plantilla = "cuotas/reports/templates/CuotasPorGrupoSinMonto.prpt" ;
			modelator = new PagosSinMontoReportModelator();
			reportIterator = new PagosSinMontoIteratorReport();
		}
		
		Integer cohorte = null;
		if(cohorteSt != null && !cohorteSt.equals(""))
			cohorte = Integer.parseInt(cohorteSt);

		Grupo grupo = FactoryDAO.grupoDAO.findByLong(idGrupo);
		System.out.println("generando informe del grupo "+ grupo.getCarrera().getNombreCarrera());
		Iterator<Pago> pagos = FactoryDAO.pagoDAO.pagosDeIntegrantesGrupo(grupo, cohorte , nroDocumento).iterator();
		
		ClassLoader classloader = reportIterator.getClass().getClassLoader();    
		//Que template quiero usar		
		reportIterator.setTemplateURL(classloader.getResource(plantilla));
		// Que iterator quiero que recorra
		reportIterator.setResults(pagos);
		// Como quiero que haga el mapeo de datos entre el modelo y el reporte
		reportIterator.setAm(modelator);   
		// Genero el reporte
		try {
			response.setContentType("application/pdf"); 
			reportIterator.generateReport(AbstractReportGenerator.OutputType.PDF, response.getOutputStream());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ReportProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Output the location of the file
		setSalto(CuotasServletUtils.SALTO_REPORTES);
	}
	public void facturaPorItem(){
		parametros = "";		
		String plantilla = "cuotas/reports/templates/FacturaPorItem.prpt" ;		
		if(request.getParameter("resumido")!=null){
			plantilla = "cuotas/reports/templates/FacturaPorItemResumido.prpt" ;	
		}
		FacturaPorItemReportModelator modelator = new FacturaPorItemReportModelator() ;
		GenericIteratorReport<FacturaItem> reportIterator =  new FacturaPorItemIteratorReport();

		Iterator<FacturaItem> facturasItem = filtrarFacturaItems("item");
		modelator.setParametros(parametros);
		
		ClassLoader classloader = reportIterator.getClass().getClassLoader();    
		//Que template quiero usar		
		reportIterator.setTemplateURL(classloader.getResource(plantilla));
		// Que iterator quiero que recorra
		reportIterator.setResults(facturasItem);
		// Como quiero que haga el mapeo de datos entre el modelo y el reporte
		reportIterator.setAm(modelator);   
		// Genero el reporte
		try {
			response.setContentType("application/pdf"); 
			reportIterator.generateReport(AbstractReportGenerator.OutputType.PDF, response.getOutputStream());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ReportProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Output the location of the file
		setSalto(CuotasServletUtils.SALTO_REPORTES);
	}
	public void reporteFactura(){
		parametros = "";
		String plantilla = "cuotas/reports/templates/Factura.prpt";
		if(request.getParameter("resumido")!=null){
			plantilla = "cuotas/reports/templates/FacturaResumido.prpt";	
		}
		FacturaReportModelator modelator = new FacturaReportModelator();
		GenericIteratorReport<Factura> reportIterator =  new FacturaIteratorReport();
	
		Iterator<Factura> facturas =filtrarFacturas();
		System.out.println("facturasHasNext: "+ facturas.hasNext());
		modelator.setParametros(parametros);
		
		ClassLoader classloader = reportIterator.getClass().getClassLoader();    
		//Que template quiero usar		
		reportIterator.setTemplateURL(classloader.getResource(plantilla));
		// Que iterator quiero que recorra
		reportIterator.setResults(facturas);
		// Como quiero que haga el mapeo de datos entre el modelo y el reporte
		reportIterator.setAm(modelator);   
		// Genero el reporte
		try {
			response.setContentType("application/pdf"); 
			reportIterator.generateReport(AbstractReportGenerator.OutputType.PDF, response.getOutputStream());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ReportProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Output the location of the file
		setSalto(CuotasServletUtils.SALTO_REPORTES);
	}
	
	
	
	
/////////////////Terminar para que haca el reporte.
	public void reporteRubroItem(){
		parametros = "";
		String plantilla = plantilla = "cuotas/reports/templates/FacturaResumidoPorPartida.prpt" ;	
		FacturaPorItemReportModelator modelator = new FacturaPorItemReportModelator() ;
		GenericIteratorReport<FacturaItem> reportIterator =  new FacturaPorItemIteratorReport();

		Iterator<FacturaItem> facturasItem = filtrarFacturaItems("tipos");
		System.out.println("parametros: " + parametros);
		modelator.setParametros(parametros);
		
		ClassLoader classloader = reportIterator.getClass().getClassLoader();    
		//Que template quiero usar		
		reportIterator.setTemplateURL(classloader.getResource(plantilla));
		// Que iterator quiero que recorra
		reportIterator.setResults(facturasItem);
		// Como quiero que haga el mapeo de datos entre el modelo y el reporte
		reportIterator.setAm(modelator);   
		// Genero el reporte
		try {
			response.setContentType("application/pdf"); 
			reportIterator.generateReport(AbstractReportGenerator.OutputType.PDF, response.getOutputStream());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ReportProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Output the location of the file
		setSalto(CuotasServletUtils.SALTO_REPORTES);
	}
	

	
	public Iterator<Factura> filtrarFacturas(){
		String numeroFactura, fechaFacturaDesde,fechaFacturaHasta, numeroDocumento,tipoDocumento,facturasAnuladas,idRubroCobro;
		Iterator<Factura> resp=null;
		
		Fecha fechaHastaBuscada = null;
		Fecha fechaDesdeBuscada = null;
		Persona personaBuscada = null;
		Factura facturaBuscada = null;
			
		facturasAnuladas = request.getParameter("facturasAnuladas");
		numeroFactura = request.getParameter("numeroFactura");
		fechaFacturaDesde = request.getParameter("fechaFacturaDesde"); 
		fechaFacturaHasta = request.getParameter("fechaFacturaHasta");
		numeroDocumento = request.getParameter("numeroDocumento");
		tipoDocumento = request.getParameter("tipoDocumento");
		idRubroCobro = request.getParameter("idrubro"); 
		String idFormaPago = request.getParameter("formaPago");
		
		FormaPago formaPago = null;
		if(idFormaPago!=null && !idFormaPago.equals("0")){
			formaPago = FactoryDAO.formaPagoDAO.findByInteger(idFormaPago);
			System.out.println("FormaPago: " + formaPago.getDescripcion());
		}
		facturaBuscada = new Factura();
		Documento documento = null;
		/*inicio prueba rubro*/
		FacturaItem facturaItem = new FacturaItem();
		FacturaItemTipo facturaItemTipo = new FacturaItemTipo();
		RubroCobro rubroCobro = null;
		if(idRubroCobro!=null){
			rubroCobro =  FactoryDAO.rubroCobroDAO.findByInteger(idRubroCobro);
		}
		
		/*fin prueba rubro*/
		if(tipoDocumento != null && !tipoDocumento.equals("") && numeroDocumento!=null && !numeroDocumento.equals("")){
			documento = new Documento();
			documento.setTipo(new TipoDocumento(tipoDocumento));	
			documento.setNumero(numeroDocumento);
			
			personaBuscada = new Persona();
			personaBuscada = FactoryDAO.personaDAO.buscarPorDocumento(documento);

			if(personaBuscada == null){
				personaBuscada = new Persona();
				personaBuscada.setDocumento(documento);
			}
			facturaBuscada.setPersona(personaBuscada);
			System.out.println("PersonaBuscada: "+ personaBuscada.toString());
		}
		if(numeroFactura!= null && !numeroFactura.equals("")){			
			facturaBuscada.setNro(Integer.parseInt(numeroFactura));
			parametros = parametros +"Numero: "+facturaBuscada+" - ";
		}
		if(fechaFacturaDesde!= null && !fechaFacturaDesde.equals("")){
			fechaDesdeBuscada = new Fecha(fechaFacturaDesde);
			parametros = parametros +"Desde: "+fechaFacturaDesde+" - ";
			//facturaBuscada.setFechaPago(fecha);
		}
		if(fechaFacturaHasta!= null && !fechaFacturaHasta.equals("")){
			fechaHastaBuscada = new Fecha(fechaFacturaHasta);
			parametros = parametros +"Hasta: "+fechaFacturaHasta+" - ";
			//facturaBuscada.setFechaPago(fecha);
		}
		Estado estado = new Estado();
		System.out.println("Estado "+facturasAnuladas);
		if(facturasAnuladas == null){
			estado.activar();
			facturaBuscada.setEstado(estado);
			parametros = parametros +"No Incluir Anuladas - ";
		}else{		
			estado.desactivar();
			facturaBuscada.setEstado(estado);
			parametros = parametros +"Anuladas - ";
		}
		if(fechaHastaBuscada == null){
			System.out.println("fechaNula");
		}
		if(fechaDesdeBuscada == null){
			System.out.println("fechaDesdeBuscada");
		}
		if(personaBuscada == null){ 
			System.out.println("personaBuscada");
		}
		if(facturaBuscada == null){ 
			System.out.println("facturaBuscada");
		}
		String idTipoPago = request.getParameter("tipoPago");
		
		TipoPago tipoPago = FactoryDAO.tipoPagoDAO.findByInteger(idTipoPago);
		//System.out.println("Filtrar por: Persona -"+personaBuscada.toString()+" , Factura - "+facturaBuscada.toString()+" , FechaDesde - "+fechaDesdeBuscada.toString()+" , FechaHasta - "+fechaHastaBuscada.toString());
		UsuarioCuotas usuario =  (UsuarioCuotas) session.getAttribute("usuario");
		resp = FactoryDAO.facturaDAO.filtrar(personaBuscada, facturaBuscada,fechaDesdeBuscada,fechaHastaBuscada ,null, null,usuario, tipoPago, formaPago, rubroCobro).iterator();
		if(resp == null){
			System.out.println("resp null");
		}
		return resp;
	}
	public Iterator<FacturaItem> filtrarFacturaItems(String orden){

		UsuarioCuotas usuario = (UsuarioCuotas)session.getAttribute("usuario");
		
		String numeroFactura, fechaFacturaDesde,fechaFacturaHasta, numeroDocumento,tipoDocumento,facturasAnuladas, iditem;
		Iterator<FacturaItem> resp=null;
		
		Fecha fechaHastaBuscada = null;
		Fecha fechaDesdeBuscada = null;
		Persona personaBuscada = null;
		Factura facturaBuscada = null;

		facturasAnuladas = request.getParameter("facturasAnuladas");
		numeroFactura = request.getParameter("numeroFactura");
		fechaFacturaDesde = request.getParameter("fechaFacturaDesde");
		fechaFacturaHasta = request.getParameter("fechaFacturaHasta");
		numeroDocumento = request.getParameter("numeroDocumento");
		tipoDocumento = request.getParameter("tipoDocumento");
		iditem = request.getParameter("iditem");
		String idrubro = request.getParameter("idrubro");		
		
		facturaBuscada = new Factura();
		Documento documento = null;
		FacturaItemTipo item = null;
		RubroCobro rubro = null;
		
		if(tipoDocumento != null && !tipoDocumento.equals("") || numeroDocumento!=null && !numeroDocumento.equals("")){
			documento = new Documento();
			documento.setTipo(new TipoDocumento(tipoDocumento));
			documento.setNumero(numeroDocumento);
			
			personaBuscada = new Persona();
			personaBuscada = FactoryDAO.personaDAO.buscarPorDocumento(documento);
			
			if(personaBuscada != null){
				facturaBuscada.setPersona(personaBuscada);
			}else{
				personaBuscada = new Persona();
				personaBuscada.setDocumento(documento);
				facturaBuscada.setPersona(personaBuscada);
			}
		}
		if(numeroFactura!= null && !numeroFactura.equals("")){
			facturaBuscada.setNro(Integer.parseInt(numeroFactura));
			parametros = parametros +"Numero: "+facturaBuscada+" - ";
		}
		
		if(fechaFacturaDesde!= null && !fechaFacturaDesde.equals("")){
			fechaDesdeBuscada = new Fecha(fechaFacturaDesde);
			parametros = parametros +"Desde: "+fechaFacturaDesde+" - ";
			//facturaBuscada.setFechaPago(fecha);
		}
		if(fechaFacturaHasta!= null && !fechaFacturaHasta.equals("")){
			fechaHastaBuscada = new Fecha(fechaFacturaHasta);
			parametros = parametros +"Hasta: "+fechaFacturaHasta+" - ";
			//facturaBuscada.setFechaPago(fecha);
		}
		Estado estado = new Estado();
		if(facturasAnuladas == null){
			estado.activar();
			facturaBuscada.setEstado(estado);
			parametros = parametros +"No Incluir Anuladas - ";
		}else{
			estado.desactivar();
			facturaBuscada.setEstado(estado);
			parametros = parametros +"Anuladas - ";
		}
		
		String idFormaPago = request.getParameter("formaPago");
		FormaPago formaPago = null;
		
		if(idFormaPago!=null && !idFormaPago.equals("0")){
			formaPago = FactoryDAO.formaPagoDAO.findByInteger(idFormaPago);
			System.out.println("FormaPago: " + formaPago.getDescripcion());
			parametros = parametros +"FormaPago: "+ formaPago.getDescripcion();
			facturaBuscada.setFormaPago(formaPago);
		}else{
			parametros = parametros +"FormaPago: Todas";
		}
		
		if(iditem != null && !iditem.equals("")){
			System.out.println("ItemID: " + iditem);
			item = FactoryDAO.facturaItemTipoDAO.findByInteger(iditem);
			parametros = parametros +"El item "+item.getDescripcion();
		
		}
		//hacer la validaciopn para que pueda filtrar DEP y DRyC
		if(idrubro != null && !idrubro.equals("")){
			rubro = FactoryDAO.rubroCobroDAO.findByInteger(idrubro);
			parametros = parametros +"El rubro "+rubro.getDescripcion();
		}
		String idTipoPago = request.getParameter("tipoPago");
		
		TipoPago tipoPago = FactoryDAO.tipoPagoDAO.findByInteger(idTipoPago);
		
		resp = FactoryDAO.facturaItemDAO.filtrar(personaBuscada, facturaBuscada,fechaDesdeBuscada,fechaHastaBuscada,item,rubro, orden,tipoPago, usuario).iterator();		
		return resp;
	}
	
	public Iterator<Pago> getPagos(String orderBy){
		
		Integer cohorte=0;
		String desdeCohorte = request.getParameter("desdeCohorte");      
		if(desdeCohorte!=null && !desdeCohorte.equals("")){			
			cohorte = Integer.parseInt(desdeCohorte);
			parametros = "Cohorte: "+cohorte+" - ";
		}
		
		String desdeNumeroGrupo= request.getParameter("desdeNumeroGrupo");   
		
		String hastaNumeroGrupo= request.getParameter("hastaNumeroGrupo");   
		
		String codigoMercadoPago = request.getParameter("codigoMercadoPago");
		/*if(codigoMercadoPago != null && !codigoMercadoPago.equals("")){
			mostrarCodigoMP = true;
			System.out.println("mostrarCodigoMP: "+mostrarCodigoMP);
		}*/
		
		if(!request.getParameter("nombreGrupo").equals("")){			
			desdeNumeroGrupo= request.getParameter("nombreGrupo");
			parametros = parametros+"Grupos: "+desdeNumeroGrupo+" - ";
		}
		
		String desdeNumeroDocumento= request.getParameter("desdeNumeroDocumento");
		Persona persona = null;
		if(!desdeNumeroDocumento.equals("")){
			Documento documentoBuscado = new Documento();
			documentoBuscado.setNumero(desdeNumeroDocumento);
			persona = FactoryDAO.personaDAO.buscarPorDocumento(desdeNumeroDocumento);
		}
		//--
		
		String desdeFechaDePago= request.getParameter("desdeFechaDePago");   
		String hastaFechaDePago= request.getParameter("hastaFechaDePago");
		RangoFecha rangoFechaPago = new RangoFecha();
		if(desdeFechaDePago!=null && !desdeFechaDePago.equals("")){			
			rangoFechaPago.setDesde(new Fecha(desdeFechaDePago));
			parametros = parametros + "Fecha Pago desde: "+desdeFechaDePago+" - ";
		}
		
		if(hastaFechaDePago!=null && !hastaFechaDePago.equals("")){
			rangoFechaPago.setHasta(new Fecha(hastaFechaDePago));
			parametros =parametros + "Fecha Pago hasta: "+hastaFechaDePago+" - ";
		}
		//--
		String desdeFechaDeCuota= request.getParameter("desdeFechaDeCuota");	
		String hastaFechaDeCuota= request.getParameter("hastaFechaDeCuota");	
		RangoFecha rangoFechaCuota = new RangoFecha();
		if(desdeFechaDeCuota!=null && !desdeFechaDeCuota.equals("")){
			rangoFechaCuota.setDesde(new Fecha(desdeFechaDeCuota));
			parametros = parametros + "Fecha Cuota desde: "+desdeFechaDeCuota+" - ";
		}
		
		if(hastaFechaDeCuota!=null && !hastaFechaDeCuota.equals("")){
			rangoFechaCuota.setHasta(new Fecha(hastaFechaDeCuota));
			parametros = parametros + "Fecha Cuota hasta: "+hastaFechaDeCuota+" - ";
		}
				
		//--
		String desdeFechaDeCargaPago= request.getParameter("desdeFechaCargaPago");
		String hastaFechaDeCargaPago= request.getParameter("hastaFechaCargaPago");
		RangoFecha rangoFechaCargaPago = new RangoFecha();
		if(desdeFechaDeCargaPago!=null && !desdeFechaDeCargaPago.equals("")){
			rangoFechaCargaPago.setDesde(new Fecha(desdeFechaDeCargaPago));
			parametros = parametros + "Fecha carga Pago desde: "+desdeFechaDeCargaPago+" - ";
		}
		if(hastaFechaDeCargaPago!=null && !hastaFechaDeCargaPago.equals("")){
			rangoFechaCargaPago.setHasta(new Fecha(hastaFechaDeCargaPago));
			parametros = parametros + "Fecha carga Pago hasta: "+hastaFechaDeCargaPago+" - ";
		}
		
		String idOrigenPago = request.getParameter("origenPago");
		OrigenPago origenPago=null;
		if(idOrigenPago!=null & !idOrigenPago.equals("0")){
			origenPago = FactoryDAO.origenPagoDAO.findByInteger(idOrigenPago);
			parametros = parametros + "Origen Pago: " +origenPago.getDescripcion();
			if(origenPago.getId()==5){
				mostrarCodigoMP = true;
			}
		}
		
		UsuarioCuotas usuario = (UsuarioCuotas) session.getAttribute("usuario");
		UsuarioCuotas usuario_aux = null;
		if(usuario!=null){
			usuario_aux  = usuario;
		}
		
		System.out.println("Grupos: "+desdeNumeroGrupo + " a " + hastaNumeroGrupo + " rangoFechaPago: "+rangoFechaPago.toString() +" OrigenPago: "+origenPago+ "rangoFechaCuota: "+rangoFechaCuota.toString());
		Iterator<Pago> pagos = FactoryDAO.pagoDAO.cancelacionesDeUnPeriodo( desdeNumeroGrupo,hastaNumeroGrupo, cohorte, persona, rangoFechaPago, rangoFechaCuota, rangoFechaCargaPago, orderBy, origenPago, usuario_aux, codigoMercadoPago).iterator();
		System.out.println("returningggg");
		return pagos ;
	}
	
	
	
	//PRUEBA DUPLICADO
	public void duplicadoFactura(){
		//parametros = "DUPLICADO";
		//String plantilla = "cuotas/reports/templates/FacturaDuplicado.prpt";
		String plantilla = "cuotas/reports/templates/Factura2.prpt";
		/*
		FacturaReportModelator modelator = new FacturaReportModelator();
		GenericIteratorReport<Factura> reportIterator =  new FacturaIteratorReport();
		*/
		FacturaDuplicadoReportModelator modelator = new FacturaDuplicadoReportModelator();
		GenericIteratorReport<Factura> reportIterator =  new FacturaDuplicadoIteratorReport();
		 
		
		Iterator<Factura> facturas =filtrarFacturas();//generarDuplicado();
		
		//modelator.setParametros(parametros);
		
		ClassLoader classloader = reportIterator.getClass().getClassLoader();    
		//Que template quiero usar		
		reportIterator.setTemplateURL(classloader.getResource(plantilla));
		
		// Que iterator quiero que recorra
		reportIterator.setResults(facturas);
		// Como quiero que haga el mapeo de datos entre el modelo y el reporte
		reportIterator.setAm(modelator);   
		// Genero el reporte
		try {
			response.setContentType("application/pdf"); 
			//reportIterator.generateReport(AbstractReportGenerator.OutputType.PDF, response.getOutputStream());
			String nombreArchivo="";
			if(facturas.hasNext()){
				Factura f = facturas.next();
				nombreArchivo = "C:/dup_facturas/"+nombreArchivo+f.getNro().toString()+".pdf";
				System.out.println("nombreArchivo: "+nombreArchivo);
			}
			
			File file = new File(nombreArchivo);
			//System.out.println("rutaArchivo: "+file.getAbsolutePath());	
			FileOutputStream fop = new FileOutputStream(file);
			reportIterator.generateReport(AbstractReportGenerator.OutputType.PDF, fop); 
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ReportProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Output the location of the file
		setSalto(CuotasServletUtils.SALTO_FACTURA_LISTAR);
	}
	//// DUPLICADO FACTURA
}
