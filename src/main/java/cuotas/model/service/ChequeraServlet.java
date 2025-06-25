package cuotas.model.service;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pentaho.reporting.engine.classic.core.ReportProcessingException;

import common.model.domain.datosPersonales.Documento;
import common.model.domain.datosPersonales.TipoDocumento;
import common.model.domain.fecha.Fecha;
import common.model.domain.validacion.UsuarioGenerico;
import common.model.reports.AbstractReportGenerator;
import common.model.service.AbmServlet;
import common.model.service.MappingMethodServlet;
import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.CarreraDep;
import cuotas.model.domain.datos.Cheque;
import cuotas.model.domain.datos.Chequera;
import cuotas.model.domain.datos.Cuota;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.Pago;
import cuotas.model.domain.datos.Persona;
import cuotas.model.domain.datos.UsuarioCuotas;
import cuotas.model.domain.json.FactoryJson;
import cuotas.reports.src.ChequeraIteratorReport;
import cuotas.reports.src.ChequeraReportModelator;
/**
 * Servlet implementation class ChequeraServlet
 */
public class ChequeraServlet extends MappingMethodServlet {
	private static final long serialVersionUID = 9034224279231991131L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChequeraServlet() {
    	this.mapeoOperacionMetodo.put(CuotasServletUtils.ACTUALIZAR,"modificar");
        setSalto(CuotasServletUtils.SALTO_CHEQUERA_INGRESO);
    }

	public void listar() {
		List<TipoDocumento> tipoDocumentos = FactoryDAO.tipoDocumentoDAO.listAll();
		List<Carrera> carreras = FactoryDAO.carreraDAO.listAll();
		request.setAttribute("tipoDocumentos", tipoDocumentos);
		request.setAttribute("carreras", carreras);
    	setSalto(CuotasServletUtils.SALTO_CHEQUERA_INGRESO);
    }
	public void otroProceso() {
		String operacion = request.getParameter("operacion");
		System.out.println("operacion123: "+operacion);
		if(operacion.equals("buscaPersonasPorCarrera")){
			buscaPersonasPorCarrera();
		}if(operacion.equals("buscarPersonasConChequera")){
			filtroPersonasConChequera();
		}if(operacion.equals("generarChequerasPersona")){
			generarChequerasPersona();
		} if(operacion.equals("generarChequerasGrupo")){
			generarChequerasGrupo();
		}
	}
	public void generarChequerasGrupo() {
		Integer i=0;
		String idPersonas[] = request.getParameterValues("idPersonaCheck");
		String idGrupo = request.getParameter("grupoChequera");
		String[] meses = null;
		String[] meses2 = null;
		String[] tipos = null;
		boolean forzado = false;
			
		//UsuarioCuotas usuario = (UsuarioCuotas) session.getAttribute("usuario");
		UsuarioCuotas usuario = (UsuarioCuotas)session.getAttribute("usuario");
		System.out.println("usuarioTipo: "+ usuario.getTipo() );
				
		usuario = FactoryDAO.usuarioDAO.findByLong(usuario.getId().toString());
		// SI ES DIFERENTE A DEP
		if (usuario.getTipo().equals("admin") || usuario.getTipo().equals("DEP_ADMIN")|| usuario.getTipo().equals("DRC_ADMIN")){
			if(request.getParameter("filtrar_meses").equals("no")){
				meses = request.getParameterValues("mes");		
			}
			if(request.getParameter("filtrar_meses2").equals("no")){
				meses2 = request.getParameterValues("mes2");		
			}
			if(request.getParameter("filtrar_tipos").equals("no")){
				tipos = request.getParameterValues("tipo");		
			}
		}
		if (usuario.getTipo().equals("admin")){
			if(request.getParameter("forzado").equals("si")){
				forzado = true;
			}
		}
		Persona persona=null;
		Grupo grupo=FactoryDAO.grupoDAO.findByLong(idGrupo);
		List<Pago> pagos = new LinkedList<Pago>();
		
		List<Chequera> chequeras = new LinkedList<Chequera>();
		boolean imprimir = false;

		if(request.getParameter("imprimir").equals("si")){
			imprimir = true;
		}
		
		while(i< idPersonas.length){
			persona = FactoryDAO.personaDAO.findByLong(idPersonas[i]);
			if(persona != null && grupo != null){
				if (usuario.getTipo().equals("admin") || usuario.getTipo().equals("DEP_ADMIN")|| usuario.getTipo().equals("DRC_ADMIN")){
						pagos = FactoryDAO.pagoDAO.pagosPorPersona(persona, grupo,false);		
						
						System.out.println("pagos.length: "+pagos.size());				
						Chequera chequera = new Chequera(persona,grupo,pagos, forzado,FactoryDAO.pagoDAO.numeroPagoPorPersona(persona, grupo,false),imprimir, meses,meses2,tipos, false);
						//FIXME - debo llamarlo para que se cargue antes de cerrar la sesion Hibernate
						persona.getNombreCompleto();
						chequeras.add(chequera);
						pagos = getPagosActualizado(chequera);
						FactoryDAO.pagoDAO.saveOrUpdate(pagos);
					i++;
				}else if(usuario.getTipo().equals("consulta") || usuario.getTipo().equals("DEP") || usuario.getTipo().equals("DRIYC")){
					// perfil consulta
					String anio = request.getParameter("anioBuscado");
					Integer ano = Integer.parseInt(anio);
					
					pagos = FactoryDAO.pagoDAO.pagosPorAnoPersona(persona, grupo, ano);		
					System.out.println("pagos.length: "+pagos.size());				
					Chequera chequera = new Chequera(persona,grupo,pagos, forzado,FactoryDAO.pagoDAO.numeroPagoPorPersona(persona, grupo,false),imprimir, meses,meses2,tipos, true);
					//FIXME - debo llamarlo para que se cargue antes de cerrar la sesion Hibernate
					persona.getNombreCompleto();
					chequeras.add(chequera);
					pagos = getPagosActualizado(chequera);
					FactoryDAO.pagoDAO.saveOrUpdate(pagos);
				i++;
				}
			}
		}
		pruebaRerporteChequeras(chequeras.iterator());
	}

	//Cambiarlo para que se seleccion el grupo, no la carrera (por si una carrera tiene mas de un grupo
	public void generarChequerasPersona() {
		String idPersona = request.getParameter("idPersonaChequera");
		String idCarrera = request.getParameter("idCarreraChequera");
		boolean soloImp = true;
		if(request.getParameter("soloImprimir")!=null && request.getParameter("soloImprimir").equals("no")){			
			soloImp = false;
		}
		boolean aFactura = false;
		if(request.getParameter("saltarAFactura")!=null && request.getParameter("saltarAFactura").equals("si")){			
			aFactura = true;
		}
		List<Pago> pagos = new LinkedList<Pago>();
		List<Chequera> chequeras = new ArrayList<Chequera>();
		
		Persona persona = FactoryDAO.personaDAO.findByLong(idPersona,true);
		Carrera carrera = FactoryDAO.carreraDAO.findByLong(idCarrera,true);
		if(carrera != null && carrera.getGrupos() != null){
			Grupo grupo = carrera.getGrupos().iterator().next();
			pagos = FactoryDAO.pagoDAO.pagosPorPersona(persona, grupo,true);	
			Chequera chequera = new Chequera(persona,grupo,pagos, false,FactoryDAO.pagoDAO.numeroPagoPorPersona(persona, grupo,true), soloImp,null,null, null, true);			
			chequeras.add(chequera);			
			pagos = getPagosActualizado(chequera);			
			FactoryDAO.pagoDAO.saveOrUpdate(pagos);	
		
			
		/*	Iterator<Chequera> chequerasIt = chequeras.iterator();
			Iterator<Cheque> cheques = chequera.getCheques().iterator();	
			Cheque cheque;
			while(chequerasIt.hasNext()){
				cheque = cheques.next();			
				System.out.println("Letra: " + cheque.getCuota().getTipo().getLetra());	
											
			}*/
			if(!aFactura){
				pruebaRerporteChequeras(chequeras.iterator());
			}else{
				Iterator<Pago> pagoIt = pagos.iterator();
				String grupo_numeros = "";
				while(pagoIt.hasNext()){
					Pago pago = pagoIt.next();
					grupo_numeros = grupo_numeros +" "+ pago.getGrupo().getId();
				}
				request.setAttribute("grupo_numeros",grupo_numeros); 
				
				System.out.println("persona: " + idPersona);
				setSalto("/FacturaServlet?id=&operacion=actualizar&idPersona="+idPersona);
			}
		}
	}
	
	public List<Pago> getPagosActualizado(Chequera chequera){
			List<Pago> pagos =new LinkedList<Pago>();
			Cheque cheque;
			Pago pago;
			Persona persona = chequera.getPersona();
			//UsuarioGenerico usuario =(UsuarioGenerico) session.getAttribute("usuario");
			UsuarioCuotas usuario = (UsuarioCuotas) session.getAttribute("usuario");
			usuario = FactoryDAO.usuarioDAO.findByLong(usuario.getId().toString());
			
			Iterator<Cheque> cheques = chequera.getCheques().iterator();			
			while(cheques.hasNext()){
				cheque = cheques.next();			
				pago = cheque.getPago_m();
				pago.setUsuario(usuario.getUsuario());
				pagos.add(pago);
											
			}
		return pagos;
	}
		
	/**
	 * Busca las personas pertenecientes a la carrera y las envia por Json.
	 * @param request
	 * @param response
	 */
	public void buscaPersonasPorCarrera() {
		String idGrupo = request.getParameter("idGrupo");
		String cohorte = request.getParameter("cohorteFiltro");
		Character estado = request.getParameter("estado").charAt(0);	
		if(estado=='T'){
			estado = null;
		}
		String numeroDeDocumento = request.getParameter("numeroDeDocumentoFiltro");
		Persona personaBuscada = null; 
		
		if (numeroDeDocumento != null && !numeroDeDocumento.equals("")){
			personaBuscada = FactoryDAO.personaDAO.buscarPorDocumento(numeroDeDocumento);
		}
		Integer cohorteInt=null;
		if(cohorte != null && !cohorte.equals("")){
			cohorteInt = Integer.parseInt(cohorte);
		}
		Grupo grupo = FactoryDAO.grupoDAO.findByLong(idGrupo);
		Carrera carreraBuscada = null;
		if(grupo!=null){
			carreraBuscada = grupo.getCarrera(); 
		}
		//String jsonPersonas = "";
		JSONObject respuestaJson = new JSONObject() ;
		JSONArray listaPersonaJson = null ;
		List<Persona> personas = null;
		if(carreraBuscada != null){
			if (personaBuscada !=null){
				personas = FactoryDAO.personaDAO.personasPorCarrera(carreraBuscada,personaBuscada,cohorteInt,estado);
			}else{
				personas = FactoryDAO.personaDAO.personasPorCarrera(carreraBuscada,null,cohorteInt,estado);
			}
			if(personas!=null){
				listaPersonaJson = FactoryJson.personaJson.getJsonLight(personas.iterator());
			}else{
				listaPersonaJson = new JSONArray();
			}
		}
		respuestaJson.put("personas", listaPersonaJson);
		System.out.println("Json: "+respuestaJson.toString());
		this.enviarJson(response,respuestaJson.toString());
	}
	
	public void filtroPersonasConChequera(){
		System.out.println("entro filtroPersonasConChequera");
		
		UsuarioCuotas usuario = (UsuarioCuotas)session.getAttribute("usuario");
		List<CarreraDep> carrerasDep = FactoryDAO.carrerasDepDAO.listAll();
		List<Carrera> carreras = FactoryDAO.carreraDAO.listAll();
		List<Grupo> grupos = FactoryDAO.grupoDAO.listAllbyUsr(usuario) ;
		
		Date date = new Date();
	    SimpleDateFormat formatoAnio= new SimpleDateFormat("yyyy");
	    String anioActual = formatoAnio.format(date);
	    
	    
	    request.setAttribute("anioBuscado", anioActual);
		request.setAttribute("grupos", grupos);
		request.setAttribute("carreras", carreras);
		request.setAttribute("carrerasDep", carrerasDep);
		System.out.println("saltando a: "+ CuotasServletUtils.SALTO_BUSCAR_PERSONAS_CON_CHEQUERA_DEP);
		setSalto(CuotasServletUtils.SALTO_BUSCAR_PERSONAS_CON_CHEQUERA_DEP);
	}
	
	public void buscaPersonasConChequeraPorCarrera() {
		UsuarioCuotas usuario = (UsuarioCuotas)session.getAttribute("usuario");
		
		String idGrupo = request.getParameter("grupoFiltro");
		String numeroDeDocumento = request.getParameter("numeroDeDocumentoFiltro");
		String meses = request.getParameter("mesesFiltro");
		String anio = request.getParameter("anioFiltro");
		Integer ano = Integer.parseInt(anio);
		
	    Persona personaBuscada = null; 
		
		if (numeroDeDocumento != null && !numeroDeDocumento.equals("")){
			personaBuscada = FactoryDAO.personaDAO.buscarPorDocumento(numeroDeDocumento);
			request.setAttribute("numeroDocumentoBuscado",personaBuscada.getDocumento().getNumero());
		}
		Grupo grupo = FactoryDAO.grupoDAO.findByLong(idGrupo);
		List<Persona> personas = null;
		
		if(grupo != null){
			personas = FactoryDAO.personaDAO.personasConChequeraPorGrupo(grupo,ano,meses,personaBuscada);
		}
		List<Grupo> grupos = FactoryDAO.grupoDAO.listAllbyUsr(usuario) ;

		request.setAttribute("anioBuscado", ano);
		request.setAttribute("mesBuscado",meses);
		request.setAttribute("grupoBuscado", grupo);
		
		request.setAttribute("personas",personas);
		request.setAttribute("grupos", grupos);

		setSalto(CuotasServletUtils.SALTO_BUSCAR_PERSONAS_CON_CHEQUERA_DEP);
	} 
	
	
	//	ESTO VA EN EL SERVELT DE REPORTE.
	public void pruebaRerporteChequeras(Iterator<Chequera> chequeras){
		String template= "cuotas/reports/templates/definicion_chequera.prpt";
	    ChequeraIteratorReport ar = new ChequeraIteratorReport();
	    ClassLoader classloader = ar.getClass().getClassLoader();
	    ar.setTemplateURL(classloader.getResource(template));
	    
	    ar.setResults(chequeras);
	    ar.setAm(new ChequeraReportModelator());
	    try {
			response.setContentType("application/pdf");
			ar.generateReport(AbstractReportGenerator.OutputType.PDF, response.getOutputStream());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReportProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setSalto(CuotasServletUtils.SALTO_REPORTES);
	}
}
