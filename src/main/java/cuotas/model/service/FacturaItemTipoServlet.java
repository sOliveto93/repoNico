package cuotas.model.service;

import java.util.List;

import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.model.domain.datos.Partida;
import cuotas.model.domain.datos.UsuarioCuotas;
import cuotas.model.domain.factura.FacturaItemTipo;
import cuotas.model.domain.factura.RubroCobro;
import cuotas.model.domain.json.FactoryJson;
import common.model.domain.datos.Estado;
import common.model.domain.error.ErrorParametro;
//import common.model.domain.util.Evento;
import common.model.service.MappingMethodServlet;

public class FacturaItemTipoServlet extends MappingMethodServlet {

	private static final long serialVersionUID = 8852507759842190412L;
	
	public FacturaItemTipoServlet(){
		this.mapeoOperacionMetodo.put(CuotasServletUtils.ACTUALIZAR, "modificar");
	}
	public void listar() {
		UsuarioCuotas usuario = (UsuarioCuotas)session.getAttribute("usuario");
		usuario = FactoryDAO.usuarioDAO.findByLong(usuario.getId().toString());
		System.out.println("usuario.tipo: " + usuario.getTipo());
		
		List<FacturaItemTipo> facturaItemTipos =null;//FactoryDAO.facturaItemTipoDAO.listAllOrder();
		List<Partida> partidas = FactoryDAO.partidaDAO.listAllOrder();
		List<RubroCobro> rubrosCobro = FactoryDAO.rubroCobroDAO.listAllOrder();

		if(usuario.getTipo().equals("DEP")|| usuario.getTipo().equals("DRIYC") || usuario.getTipo().equals("consulta")){
			facturaItemTipos = FactoryDAO.facturaItemTipoDAO.listAllbyUsr(usuario);
		}else{
			facturaItemTipos = FactoryDAO.facturaItemTipoDAO.listAllOrder();
		}
		request.setAttribute("partidas", partidas);
		request.setAttribute("rubrosCobro",rubrosCobro);
		request.setAttribute("facturaItemTipos", facturaItemTipos);
		
		setSalto(CuotasServletUtils.SALTO_FACTURAITEMTIPO_LISTAR);
	}
	public void alta(){
		FacturaItemTipo facturaItemTipo = getFacturaItemTipoActualizado();

/*		Evento evento = getEventoAuditoria(request, facturaItemTipo);
		
		FactoryDAO.eventoDAO.save(evento,false);*/
		FactoryDAO.facturaItemTipoDAO.save(facturaItemTipo);
		listar();
	}
	public void modificar(){
		try{
			System.out.println("entroModificar");
			FacturaItemTipo facturaItemTipo = null;
			String id = request.getParameter("id");
			facturaItemTipo= getFacturaItemTipoActualizado();
			
		/*	Evento evento = getEventoAuditoria(request, facturaItemTipo);

			FactoryDAO.eventoDAO.save(evento,false);*/
			FactoryDAO.facturaItemTipoDAO.save(facturaItemTipo);
			listar();
		}catch (ErrorParametro e){
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, e.getMSG());
		}
	}
	public void borrar(){
		String id = request.getParameter("id");
		FacturaItemTipo facturaItemTipo = FactoryDAO.facturaItemTipoDAO.findByInteger(id);
		facturaItemTipo.getEstado().desactivar();
		//AGREGAR EL CAMPO ESTADO EN LA TABLA.
/*		Evento evento = getEventoAuditoria(request, facturaItemTipo);
		
		FactoryDAO.eventoDAO.save(evento,false);*/
		
		FactoryDAO.facturaItemTipoDAO.saveOrUpdate(facturaItemTipo);
		listar();
	}
	public void verFacturaItemJson(){
		FacturaItemTipo facturaItemTipo= null;
		String facturaItemTipoJson = "";
		String id = request.getParameter("id");
		facturaItemTipo = FactoryDAO.facturaItemTipoDAO.findByInteger(id);
		if(facturaItemTipo!=null){
			facturaItemTipoJson = FactoryJson.facturaItemTipoJson.toJson(facturaItemTipo,true);	
		}
		
		System.out.println("JSON: " + facturaItemTipoJson);
		this.enviarJson(response,facturaItemTipoJson);
	}	
	public FacturaItemTipo getFacturaItemTipoActualizado(){
		FacturaItemTipo facturaItemTipo;
		RubroCobro rubroCobro;
		String id = request.getParameter("id");
		String idRubro = request.getParameter("rubroCobro");
		String descripcion =request.getParameter("descripcion");
		String codigoS = request.getParameter("codigo");
		String precioS = request.getParameter("precio");
		String idPartida = request.getParameter("partida");
		
		Integer codigo;
		Double precio;
		
		Partida partida = FactoryDAO.partidaDAO.findByInteger(idPartida);		
		if(esNuloOVacio(id)){
			facturaItemTipo = new FacturaItemTipo();
		}else{
			facturaItemTipo = FactoryDAO.facturaItemTipoDAO.findByInteger(id);
		}
		if(partida != null){
			facturaItemTipo.setPartida(partida);
		}
		rubroCobro = FactoryDAO.rubroCobroDAO.findByInteger(idRubro);
		if(rubroCobro != null)
			facturaItemTipo.setRubroCobro(rubroCobro);
		
		if(!esNuloOVacio(precioS)){
			precio = Double.parseDouble(precioS);
			facturaItemTipo.setPrecio(precio);
		}
		if(!esNuloOVacio(codigoS)){
			codigo = Integer.parseInt(codigoS);
			facturaItemTipo.setCodigo(codigo);			
		}	
		facturaItemTipo.setDescripcion(descripcion);
		Estado estado = new Estado(Estado.ACTIVO);
		facturaItemTipo.setEstado(estado);
		
		return facturaItemTipo;
	}
	public void altaPartidaJson(){
		String descripcion = request.getParameter("descripcion");
		Partida partida = FactoryDAO.partidaDAO.buscarUnicoPorCampo("descripcion", descripcion);
		if(partida == null){
			partida = new Partida();
			partida.setDescripcion(descripcion);
			FactoryDAO.partidaDAO.save(partida);
			partida = FactoryDAO.partidaDAO.buscarUnicoPorCampo("descripcion", descripcion);
		}		
		String jsonPartida = FactoryJson.partidaJson.toJson(partida,true);
		System.out.println("JsonPartida: "+ jsonPartida);
		this.enviarJson(response,jsonPartida);
	}
	public void modificarPartidaJson(){
		String descripcion = request.getParameter("descripcion");
		String idPartida = request.getParameter("idPartida");

		Partida partida = FactoryDAO.partidaDAO.findByInteger(idPartida);
		if(partida !=null){
			partida.setDescripcion(descripcion);
			FactoryDAO.partidaDAO.save(partida);
			partida = FactoryDAO.partidaDAO.findByInteger(idPartida);
			if(partida != null){
				System.out.println("Partida.Descripcion: "+ partida.getDescripcion());
			}
		}
		String jsonPartida = FactoryJson.partidaJson.toJson(partida,true);
		System.out.println("JsonPartida: "+ jsonPartida);
		this.enviarJson(response,jsonPartida);
	}
}
