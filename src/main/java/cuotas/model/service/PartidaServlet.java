package cuotas.model.service;

import java.util.Iterator;
import java.util.List;


import common.model.service.MappingMethodServlet;
import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.Partida;
import cuotas.model.domain.json.FactoryJson;

public class PartidaServlet extends MappingMethodServlet {
	private static final long serialVersionUID = -4921629262623239500L;
	
	public PartidaServlet(){
		
	}
	public void alta(){
		Partida partida = new Partida();
		String descripcion=request.getParameter("descripcionPartida");
		partida.setDescripcion(descripcion);
		
		FactoryDAO.partidaDAO.save(partida);
	}
	public void agregarGrupoDePartida(){
		String idGrupo =request.getParameter("idgrupo");
		String idPartida =request.getParameter("idpartida");
		//System.out.println("idpartida:"+idPartida);
		Partida partida;
		Grupo grupo;
		if(idPartida!="228"){
			partida = FactoryDAO.partidaDAO.findByInteger(idPartida,true);
			grupo = FactoryDAO.grupoDAO.findByLong(idGrupo);
			System.out.println(  		partida.getDescripcion());
			//Partida sinPartida = FactoryDAO.partidaDAO.findByInteger("228");
			
			grupo.setPartida(partida);
			partida.addGrupo(grupo);
			
			FactoryDAO.partidaDAO.saveOrUpdate(partida);
			FactoryDAO.grupoDAO.saveOrUpdate(grupo);
		}	
		partida = FactoryDAO.partidaDAO.findByInteger(idPartida);
		String partidaJson = FactoryJson.partidaJson.toJson(partida, true);
		
		System.out.println("JSON: " + partidaJson);
		this.enviarJson(response, partidaJson);
	}
	public void eliminarGrupoDePartida(){
		String idGrupo =request.getParameter("idgrupo");
		String idPartida =request.getParameter("idpartida");
		Partida partida; 
		Grupo grupo; 
		if(idPartida!="228"){
			partida = FactoryDAO.partidaDAO.findByInteger(idPartida);
			grupo = FactoryDAO.grupoDAO.findByLong(idGrupo);
			
			Partida sinPartida = FactoryDAO.partidaDAO.findByInteger("228");
			grupo.setPartida(sinPartida);
			
			partida.removeGrupo(grupo);
			
			FactoryDAO.partidaDAO.saveOrUpdate(partida);
			FactoryDAO.grupoDAO.saveOrUpdate(grupo);
		}		
		partida = FactoryDAO.partidaDAO.findByInteger(idPartida);
		
		String partidaJson = FactoryJson.partidaJson.toJson(partida, true);
		
		System.out.println("JSON: " + partidaJson);
		this.enviarJson(response, partidaJson);
	}
	public void partidaJson(){
		String idPartida =request.getParameter("idpartida");
		Partida partida = FactoryDAO.partidaDAO.findByInteger(idPartida);
		
		partida = FactoryDAO.partidaDAO.findByInteger(idPartida);
		String partidaJson = FactoryJson.partidaJson.toJson(partida, true);

		System.out.println("partidaJson: "+ partidaJson);
		
		this.enviarJson(response, partidaJson);
	}
	public void listar(){
		List<Partida> partidas = FactoryDAO.partidaDAO.listAll();
		List<Grupo> grupos = FactoryDAO.grupoDAO.listAll();
		String grupo_numeros = "";
		Iterator<Partida> partidaIt = partidas.iterator();
		Iterator<Grupo> gruposIt = grupos.iterator();
		while(gruposIt.hasNext()){
			Grupo g  = gruposIt.next();
			grupo_numeros = grupo_numeros +" "+g.getId();
		}
			
			System.out.println("##############################################");
		System.out.println("Cuantas partidas existen: "+partidas.size());
		
		request.setAttribute("grupos", grupos);
		request.setAttribute("partidas", partidas);
		
		setSalto(CuotasServletUtils.SALTO_PARTIDA_LISTAR);
	}
	
}
