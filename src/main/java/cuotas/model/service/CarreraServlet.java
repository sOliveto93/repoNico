package cuotas.model.service;

import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import common.model.domain.error.ErrorGrabacion;
import common.model.domain.error.ErrorParametro;
import common.model.domain.fecha.Fecha;
import common.model.domain.fecha.RangoFecha;
//import common.model.domain.util.Evento;
import common.model.service.MappingMethodServlet;

import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.UsuarioCuotas;
import cuotas.model.domain.json.FactoryJson;


public class CarreraServlet extends MappingMethodServlet {
	
	static Logger logger = Logger.getLogger(CarreraServlet.class);
	private static final long serialVersionUID = -4160350454712538392L;
	
	public CarreraServlet(){
		// MAPEO "operacion"->"metodo"
		//this.mapeoOperacionMetodo.put(CuotasServletUtils.ALTA, "agregar");
		//this.mapeoOperacionMetodo.put(CuotasServletUtils.BORRAR,"eliminar");
		//this.mapeoOperacionMetodo.put(CuotasServletUtils.LISTAR,"ingresar");
		this.mapeoOperacionMetodo.put(CuotasServletUtils.ACTUALIZAR,"modificar");
	}

	
	public void alta() {
		try{
			String nombre = request.getParameter("nombreCarrera");
			Carrera carrera = FactoryDAO.carreraDAO.buscarCarreraNombre(nombre);
		    
			carrera = getCarreraActualizada(request, carrera);
			//evento 
			//saveEventoAuditoria(request, carrera);
			
			/*Evento evento = getEventoAuditoria(request, carrera);
			FactoryDAO.eventoDAO.save(evento,false);*/
			
			logger.debug("Se grabo "+carrera.toString());
		    FactoryDAO.carreraDAO.save(carrera);

		    listar();

		}catch (ErrorParametro e){
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, e.getMSG());
		}
		//TODO - Pendiente el salto hasta saber cual va a ser
		listar();
		setSalto(CuotasServletUtils.SALTO_CARRERA_BUSCAR);
	}

	
	public void borrar() {
		try{
			String id = request.getParameter("id");
			
			if (FactoryDAO.carreraDAO.existsByLong(id)){				
				Carrera carrera = FactoryDAO.carreraDAO.findByLong(id);				
				carrera.getEstado().desactivar();
				//evento grabando...
				//saveEventoAuditoria(request, carrera);
				
				/*Evento evento = getEventoAuditoria(request, carrera);
				FactoryDAO.eventoDAO.save(evento,false);
				*/
				FactoryDAO.carreraDAO.saveOrUpdate(carrera);			
			}
			
			listar();
			
		}catch(ErrorGrabacion e){
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, e.getMSG());
		}
	}
	
	
	public void listar() {
		List<Carrera> carreras = FactoryDAO.carreraDAO.listAll();
		request.setAttribute("carreras", carreras);
		setSalto(CuotasServletUtils.SALTO_CARRERA_LISTAR);
	}
	
	public void informesCarrera(){
		List<Carrera> carreras = null;
		UsuarioCuotas usuario = (UsuarioCuotas)session.getAttribute("usuario");
		if((usuario != null && usuario.getTipo().equals("admin")) || (usuario != null && usuario.getTipo().equals("auditoria"))){
			carreras = FactoryDAO.carreraDAO.listAll("codigo",true);
		}else{
			carreras = FactoryDAO.carreraDAO.listAllbyUsr(usuario);	
		}
		
		
		request.setAttribute("carreras", carreras);
		setSalto(CuotasServletUtils.SALTO_INFORMES_CARRERA); 
	}
	
	public void modificar() {
		Carrera carrera = null;
		try{
			carrera = getCarreraActualizada(request);
			/*Evento evento = getEventoAuditoria(request, carrera);
			
			FactoryDAO.eventoDAO.save(evento,false);*/
			FactoryDAO.carreraDAO.saveOrUpdate(carrera);
		}catch (ErrorParametro e){
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, e.getMSG());
		}
		listar();
	}
	
	public Carrera getCarreraActualizada(HttpServletRequest request){
		return getCarreraActualizada(request,null);
	}
	/**
	 * Carga el objecto de la Cursada con los campos del request
	 * @param request
	 * @param id TODO
	 * @return
	 */
	public Carrera getCarreraActualizada(HttpServletRequest request, Carrera otraCarrera){
		
		String idCarrera =  request.getParameter("id");	
		Fecha fechaVigenciaDesde = Fecha.parse(request.getParameter("vigenciaCarreraDesde"));
		Fecha fechaVigenciaHasta = Fecha.parse(request.getParameter("vigenciaCarreraHasta"));
		
		Carrera carrera ;
		
		if (otraCarrera != null){
			carrera = otraCarrera;
			carrera.getEstado().activar();
		}else{			
			carrera = FactoryDAO.carreraDAO.findByLong(idCarrera);			
			if(carrera == null)
				carrera = new Carrera();
		}		
		carrera.setNombreCarrera(request.getParameter("nombreCarrera"));
		carrera.setNombreTitulo(request.getParameter("nombreTitulo"));	
		carrera.setTipo(request.getParameter("tipo"));
		
		if ( request.getParameter("codigo") != null && !request.getParameter("codigo").equals("")){			
			carrera.setCodigo(Integer.parseInt(request.getParameter("codigo")));
		}
		
		RangoFecha vigenciaCarrera = new RangoFecha(fechaVigenciaDesde,fechaVigenciaHasta);
		
		
		carrera.setVigencia(vigenciaCarrera);
		//carrera.setVigenciaCarrera(fechaVigenciaDesde,fechaVigenciaHasta);		

		return carrera;
	}
	
	public void carrerasJson(){
		UsuarioCuotas usuario = (UsuarioCuotas)session.getAttribute("usuario");
		System.out.println("usuarioTipo: "+ usuario.getTipo() );
		Iterator<Carrera> carrerasIt = null;
		String carrerasJson = null;
		if(usuario.getTipo().equals("admin")){
			carrerasIt = FactoryDAO.carreraDAO.findAll();
		}else if(usuario.getTipo().equals("DEP")|| usuario.getTipo().equals("DEP_ADMIN")|| usuario.getTipo().equals("DRC_ADMIN")){
			carrerasIt = FactoryDAO.carreraDAO.listAllbyUsr(usuario).iterator();
		}
		
		carrerasJson = FactoryJson.carreraJson.toJson(carrerasIt,false);
		System.out.println("CarreraJson: " +carrerasJson);
		this.enviarJson(response,carrerasJson);
	}
	
	public void carreraJson(){
		//String idCarrera = request.getParameter("idCarrera");
		String codigoCarrera = request.getParameter	("codigo");
		Integer codigoCarreraInteger;
		List<Carrera> carreras = null;
		String carreraJson="";
		
		System.out.println("codigoCarrera: " +codigoCarrera);
		if(codigoCarrera != null && !codigoCarrera.equals("")){
			codigoCarreraInteger = Integer.parseInt(codigoCarrera);
			//carreras = FactoryDAO.carreraDAO.buscarPorCampo("codigo", codigoCarreraInteger);	
			//carreras = FactoryDAO.carreraDAO.buscarPorCampo("codigo", codigoCarrera);
			carreras = FactoryDAO.carreraDAO.buscarCarrerasCodigo(codigoCarreraInteger);
		}
		
		//carrera = FactoryDAO.carreraDAO.findByLong(idCarrera);
		if(carreras != null){
			carreraJson = FactoryJson.carreraJson.toJson(carreras,true,false);
			System.out.println("carreraJson: " + carreraJson);
			System.out.println("Carreras : " + carreras.size());
		}
		this.enviarJson(response,carreraJson);
	}
	
	/*public void otroProceso(HttpServletRequest request,	HttpServletResponse response) {
		String operacion = request.getParameter("operacion");
		
		System.out.println("operacion: "+ operacion);
		
		if(operacion.equals("carrerasJson")){
			Iterator<Carrera> carrerasIt = FactoryDAO.carreraDAO.findAll();
			String carrerasJson = FactoryJson.carreraJson.toJson(carrerasIt,true);
			
			System.out.println("CarreraJson: " +carrerasJson);
			this.enviarJson(response,carrerasJson);
		}
	}*/
	/*private void saveEventoAuditoria(HttpServletRequest request, Carrera carrera){
		String operacion = request.getParameter("operacion");
		UsuarioGenerico usuario = (UsuarioGenerico)session.getAttribute("usuario");
		String descripcion = operacion +" "+carrera.getClass().getSimpleName()+": ";
		
		if(operacion != null && !operacion.equals("")){
			Evento eventoModificarCarrera = new Evento(usuario.getNombreCompleto(), descripcion + carrera.toString());
			FactoryDAO.eventoDAO.save(eventoModificarCarrera,false);
		}
	}*/
	
	
	/*
	@Override
	public String getAgregarLabel() {
		return CuotasServletUtils.ALTA;
	}

	@Override
	public String getEliminarLabel() {
		return CuotasServletUtils.BORRAR;
	}

	@Override
	public String getIngresarLabel() {
		return CuotasServletUtils.LISTAR;
	}

	@Override
	public String getModificarLabel() {
		return CuotasServletUtils.ACTUALIZAR;
	}

	@Override
	public String getSaltoDefault() {
		return CuotasServletUtils.SALTO_DEFAULT; 
	}
*/
}
