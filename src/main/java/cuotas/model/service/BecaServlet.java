package cuotas.model.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import common.model.domain.error.ErrorParametro;
//import common.model.domain.util.Evento;
import common.model.service.MappingMethodServlet;
import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.model.domain.datos.Beca;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.json.FactoryJson;

public class BecaServlet extends MappingMethodServlet {
	private static final long serialVersionUID = -2468695281362203401L;
	
	public void listar(){
		List<Beca> becas = FactoryDAO.becaDAO.listAll();
		
		request.setAttribute("becas", becas);
		setSalto(CuotasServletUtils.SALTO_BECA_LISTAR);
	}
	
	
	public void alta() {
		try{
			// TODO - ver los parametros de busqueda para ver si el Grupo ya existia y solamente ACTIVARLO.
			String id=request.getParameter("id");
			Beca beca = FactoryDAO.becaDAO.findByInteger(id);
		    beca = getBecaActualizada(request, beca);
		    //saveEventoAuditoria(request, grupo);
		  //  Evento evento = getEventoAuditoria(request, beca);
			//FactoryDAO.eventoDAO.save(evento,false);
			
		    FactoryDAO.becaDAO.save(beca);
			listar();
		}catch(ErrorParametro e){
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, "La Grupo ya existe");
		}
	}
	public void modificar() {
		try{
			Beca beca= FactoryDAO.becaDAO.findByLong(request.getParameter("id"));
			beca = getBecaActualizada(request, beca);
			//saveEventoAuditoria(request, grupo);
			//Evento evento = getEventoAuditoria(request, beca);
			//FactoryDAO.eventoDAO.save(evento,false);
			FactoryDAO.becaDAO.saveOrUpdate(beca);
		}catch (ErrorParametro e){
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, e.getMSG());
		}
		listar();
	}
	public Beca getBecaActualizada(HttpServletRequest request,Beca beca){
				
		return beca;
		
	}
	public void altaBecaJson(){		
		String descripcion = request.getParameter("descripcion");
		Beca beca = null;
		String becaJson = "";
		if(descripcion != null && !descripcion.equals("")){
			beca = new Beca(descripcion);
			FactoryDAO.becaDAO.saveOrUpdate(beca);
			beca = FactoryDAO.becaDAO.buscarUnicoPorCampo("descripcion", descripcion);
			becaJson = FactoryJson.becaJson.toJson(beca, true);
		}
		System.out.println("JsonBeca: "+ becaJson);
		this.enviarJson(response,becaJson);
	}
	public void modificarBecaJson(){
		Beca beca = null;
		String id = request.getParameter("idBeca");
		String descripcion = request.getParameter("descripcion");
		beca = FactoryDAO.becaDAO.findByInteger(id);
		if(beca!=null){
			beca.setDescripcion(descripcion);
			FactoryDAO.becaDAO.saveOrUpdate(beca);
			beca = FactoryDAO.becaDAO.findByInteger(id);
		}
		String becaJson = FactoryJson.becaJson.toJson(beca, true);
		System.out.println("JsonBeca: "+ becaJson);
		this.enviarJson(response,becaJson);
		
	}
}
