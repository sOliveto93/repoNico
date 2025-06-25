package cuotas.model.service;

import java.util.List;

import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.model.domain.factura.RubroCobro;
import cuotas.model.domain.json.FactoryJson;
import common.model.domain.error.ErrorParametro;
//import common.model.domain.util.Evento;
import common.model.service.MappingMethodServlet;

public class FacturaRubroCobroServlet extends MappingMethodServlet {

	private static final long serialVersionUID = 8852507759842190412L;
	
	public FacturaRubroCobroServlet(){
		this.mapeoOperacionMetodo.put(CuotasServletUtils.ACTUALIZAR, "modificar");
	}
	public void listar() {
		List<RubroCobro> rubrosCobro = FactoryDAO.rubroCobroDAO.listAll();
	
		request.setAttribute("rubrosCobro",rubrosCobro);
		setSalto(CuotasServletUtils.SALTO_RUBROCOBRO_LISTAR);
	}
	public void alta(){
		RubroCobro rubroCobro= getFacturaRubroCobroActualizado();

		/*Evento evento = getEventoAuditoria(request, rubroCobro);
		
		FactoryDAO.eventoDAO.save(evento,false);*/
		FactoryDAO.rubroCobroDAO.save(rubroCobro);
		listar();
	}
	public void modificar(){
		try{
			RubroCobro rubroCobro = null;
			rubroCobro = getFacturaRubroCobroActualizado();
			
	/*		Evento evento = getEventoAuditoria(request, rubroCobro);

			FactoryDAO.eventoDAO.save(evento,false);*/
			FactoryDAO.rubroCobroDAO.save(rubroCobro);
			listar();
		}catch (ErrorParametro e){
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, e.getMSG());
		}
	}
	public void borrar(){
		String id = request.getParameter("id");
		RubroCobro rubroCobro = FactoryDAO.rubroCobroDAO.findByInteger(id);
		rubroCobro.getEstado().desactivar();
		//AGREGAR EL CAMPO ESTADO EN LA TABLA.
/*		Evento evento = getEventoAuditoria(request, rubroCobro);
		
		FactoryDAO.eventoDAO.save(evento,false);*/
		FactoryDAO.rubroCobroDAO.saveOrUpdate(rubroCobro);
		listar();
	}
	public void verFacturaRubroCobroJson(){
		RubroCobro rubroCobro= null;
		String facturaRubroCobroJson = "";
		String id = request.getParameter("id");
				
		rubroCobro = FactoryDAO.rubroCobroDAO.findByInteger(id);
		if(rubroCobro!=null){
			facturaRubroCobroJson = FactoryJson.rubroCobroJson.toJson(rubroCobro,true);	
		}
		System.out.println("JSON: " + facturaRubroCobroJson);
		this.enviarJson(response,facturaRubroCobroJson);
	}	
	public RubroCobro getFacturaRubroCobroActualizado(){
		RubroCobro rubroCobro;
		String id = request.getParameter("id");
		String descripcion =request.getParameter("descripcion");
		String codigoS = request.getParameter("codigo");
		
		Integer codigo;
		
		if(esNuloOVacio(id)){
			rubroCobro = new RubroCobro();
		}else{
			rubroCobro = FactoryDAO.rubroCobroDAO.findByInteger(id);
		}

		if(!esNuloOVacio(codigoS)){
			codigo = Integer.parseInt(codigoS);
			rubroCobro.setCodigo(codigo);			
		}	
		rubroCobro.setDescripcion(descripcion);
		
		return rubroCobro;
	}

	
}
