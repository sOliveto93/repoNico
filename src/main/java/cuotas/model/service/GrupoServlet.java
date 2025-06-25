package cuotas.model.service;

import common.model.domain.error.ErrorGrabacion;
import common.model.domain.error.ErrorParametro;
import common.model.domain.fecha.Anio;
//import common.model.domain.util.Evento;
import common.model.service.MappingMethodServlet;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.Partida;
import cuotas.model.domain.datos.TipoCuota;
import cuotas.model.domain.datos.UsuarioCuotas;
import cuotas.model.domain.factura.FacturaItemTipo;
import cuotas.model.domain.json.FactoryJson;



/**
 * Servlet implementation class GrupoServlet
 */
public class GrupoServlet extends MappingMethodServlet{
	private static final long serialVersionUID = 1L;
       
	public GrupoServlet(){
		this.mapeoOperacionMetodo.put(CuotasServletUtils.ACTUALIZAR,"modificar");
	}

	public void alta() {
		try{
			// TODO - ver los parametros de busqueda para ver si el Grupo ya existia y solamente ACTIVARLO.
			String id=request.getParameter("id");
			Grupo grupo = FactoryDAO.grupoDAO.findByLong(id);
		    grupo = getGrupoActualizada(request, grupo);
		    //saveEventoAuditoria(request, grupo);
//		    Evento evento = getEventoAuditoria(request, grupo);
//			FactoryDAO.eventoDAO.save(evento,false);
			
		    FactoryDAO.grupoDAO.save(grupo);
			listar();
		}catch(ErrorParametro e){
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, "La Grupo ya existe");
		}
	}
	public void borrar() {
		try{
			String id= request.getParameter("id");
			
			Grupo grupo = FactoryDAO.grupoDAO.findByLong(id);
			if(grupo!=null){
				grupo.getEstado().desactivar();
				//saveEventoAuditoria(request, grupo);
//				Evento evento = getEventoAuditoria(request, grupo);
//				FactoryDAO.eventoDAO.save(evento,false);
				FactoryDAO.grupoDAO.saveOrUpdate(grupo);
			}
			listar();
		} catch(ErrorGrabacion e){
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, e.getMSG());
		}
	}
	public void listar() {
		UsuarioCuotas usuario = (UsuarioCuotas)session.getAttribute("usuario");
		System.out.println("usuarioTipo: "+ usuario.getTipo() );
		List<Grupo> grupos = null;
		if(usuario.getTipo().equals("DEP")|| usuario.getTipo().equals("DEP_ADMIN") || usuario.getTipo().equals("DRC_ADMIN") || usuario.getTipo().equals("DRIYC")  || usuario.getTipo().equals("consulta")){
			grupos = FactoryDAO.grupoDAO.listAllbyUsr(usuario);//FactoryDAO.grupoDAO.listAllDep(); 
		}else{
			grupos = FactoryDAO.grupoDAO.listAll();
		}
		System.out.println("usuarioTipo: "+ usuario.getTipo() + "- NumerosDeGrupos: "+grupos.size());
		List<TipoCuota> tiposCuota = FactoryDAO.tipoCuotaDAO.listAll("descripcion",true);
		
		List<Carrera> carreras = FactoryDAO.carreraDAO.listAll("codigo",true);
		
		List<Integer> anios = Anio.listAnios(1995, new GregorianCalendar().get(Calendar.YEAR)+5);
		List<Partida> partidas = FactoryDAO.partidaDAO.listAll();
		List<FacturaItemTipo> facturaItemTipos = FactoryDAO.facturaItemTipoDAO.listAll("rubroCobro",true);
		//session.setAttribute(CuotasServletUtils.USUARIO,usuarioLocal);
		request.setAttribute("facturaItemTipos", facturaItemTipos);
		request.setAttribute("partidas", partidas);
		request.setAttribute("tiposCuota", tiposCuota);
		request.setAttribute("grupos", grupos);
		request.setAttribute("carreras", carreras);
		request.setAttribute("anios",anios);
		String idGrupoAnterior = request.getParameter("id");
		request.setAttribute("idGrupoAnterior",idGrupoAnterior);
		
		if(usuario.getTipo().equals("DEP")  || usuario.getTipo().equals("DRIYC")|| usuario.getTipo().equals("consulta")){
			setSalto(CuotasServletUtils.SALTO_GRUPO_LISTAR_DEP);
		}else if(usuario.getTipo().equals("DEP_ADMIN")|| usuario.getTipo().equals("DRC_ADMIN")){
			setSalto(CuotasServletUtils.SALTO_GRUPO_LISTAR_DEP_ADMIN);
		}else{
			setSalto(CuotasServletUtils.SALTO_GRUPO_LISTAR);	
		}
		
	}

	public void modificar() {
		try{
			Grupo grupo= FactoryDAO.grupoDAO.findByLong(request.getParameter("id"));
			grupo = getGrupoActualizada(request, grupo);
			//saveEventoAuditoria(request, grupo);
			//Evento evento = getEventoAuditoria(request, grupo);
			//FactoryDAO.eventoDAO.save(evento,false);
			FactoryDAO.grupoDAO.saveOrUpdate(grupo);
		}catch (ErrorParametro e){
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, e.getMSG());
		}
		listar();
	}
	public Grupo getGrupoActualizada(HttpServletRequest request, Grupo grupo){
		UsuarioCuotas usuario = (UsuarioCuotas)session.getAttribute("usuario");
		System.out.println("usuarioTipo: "+ usuario.getTipo() );
		Grupo gru;
		if(grupo==null){
			gru = new Grupo();
		}else{
			gru = grupo;
		}
		
		String codigo = request.getParameter("codigo");
		String anioCuotas = request.getParameter("anioCuotas");
		String diasPrimerVencimiento = request.getParameter("diasPrimerVencimiento");
		String conceptoNumeroUno=request.getParameter("conceptoNumeroUno");
		
		String diasSegundoVencimiento = request.getParameter("diasSegundoVencimiento");
		String vencimientoMonto = request.getParameter("vencimientoMonto");

		String conceptoUnoMonto = request.getParameter("conceptoUnoMonto");
		String conceptoDosMonto = request.getParameter("conceptoDosMonto");
		String cantidadCuotas = request.getParameter("cantidadCuotas");
		//*****************************************************************************
		
		String conceptoNumeroDos = request.getParameter("conceptoNumeroDos");
		String descripcionGrupo = request.getParameter("descripcionGrupo");
		String nombreTitulo = request.getParameter("nombreTitulo");
		
		String idPartida = request.getParameter("partida");
		String idTipo = request.getParameter("tipo");
		String idTipoMora = request.getParameter("tipoMora");
		
		FacturaItemTipo facturaItemTipo = FactoryDAO.facturaItemTipoDAO.findByInteger(idTipo);
		FacturaItemTipo facturaItemTipoMora = FactoryDAO.facturaItemTipoDAO.findByInteger(idTipoMora);
		Partida partida = FactoryDAO.partidaDAO.findByInteger(idPartida);
			
		if(usuario.getTipo().equals("admin")){
			if(facturaItemTipo != null){
				gru.setFacturaItemTipo(facturaItemTipo);
			}
			if(facturaItemTipoMora != null){
				gru.setFacturaItemTipoMora(facturaItemTipoMora);
			}
				
			if(partida != null){
				if(!idPartida.equals("228")){
					gru.setPartida(partida);
				}
			}
		
			if(!esNuloOVacio(codigo)){
				Long codigoLong = Long.parseLong(codigo);
				gru.setCodigo(codigoLong);
			}
			if(!esNuloOVacio(anioCuotas))
				gru.setAnioCuota(Integer.parseInt(anioCuotas));
			if(!esNuloOVacio(conceptoNumeroUno))
				gru.setConcepto1(conceptoNumeroUno);
			
			if(!esNuloOVacio(descripcionGrupo))
				gru.setDescripcion(descripcionGrupo);
			if(!esNuloOVacio(diasPrimerVencimiento))
				gru.setVtoDias(Integer.parseInt(diasPrimerVencimiento));
			if(!esNuloOVacio(diasSegundoVencimiento) && !diasSegundoVencimiento.equals("0") ){
				gru.setVtoPlus(Integer.parseInt(diasSegundoVencimiento));
			}else{
				//Si es 0 o null se pone un numero grande para que no venza.
				gru.setVtoPlus(0);
			}
			gru.setConcepto2(conceptoNumeroDos);
	
			if(cantidadCuotas != null & !cantidadCuotas.equals("")){
				Integer cantidadCuotasInt = Integer.parseInt(cantidadCuotas);
				gru.setCantidadCuotas(cantidadCuotasInt);
			}
			if(!esNuloOVacio(conceptoUnoMonto)){
				gru.setConceptoUnoMonto(Double.parseDouble(conceptoUnoMonto));
			}
			if(!esNuloOVacio(conceptoDosMonto)){
				gru.setConceptoDosMonto(Double.parseDouble(conceptoDosMonto));
			}else{
				gru.setConceptoDosMonto(0.0);	
			}
		
			
			if(!esNuloOVacio(vencimientoMonto)){
				gru.setVtoMonto(Double.parseDouble(vencimientoMonto));
			}
			if(!esNuloOVacio(nombreTitulo))
				gru.setNombreTitulo(nombreTitulo);
			Carrera carrera = FactoryDAO.carreraDAO.findByLong(request.getParameter("carreraGrupo"));
			if(carrera!=null){
				gru.setCarrera(carrera);
				UsuarioCuotas usu  = null;
				if(carrera.getTipo().equals("dep")){
					System.out.println("entro dep");
					usu  = FactoryDAO.usuarioDAO.findByLong("3");
					gru.addUsuarioGrupo(usu);
				}else if(carrera.getTipo().equals("driyc")){
					System.out.println("entro driyc");
					usu  = FactoryDAO.usuarioDAO.findByLong("10");
					gru.addUsuarioGrupo(usu);
				}
				
				//gru.addUsuarioGrupo(usuario);
			}
			if(request.getParameter("ctrlPago")==null || request.getParameter("ctrlPago").equals("off")){
				gru.setCtrlPago('N');
			}else{
				gru.setCtrlPago('S');
			}
			if(request.getParameter("cobraMora")==null || request.getParameter("cobraMora").equals("off")){
				gru.setCobraMora('N');
			}else{
				gru.setCobraMora('S');
			}
			gru.getEstado().activar();
		}else if(usuario.getTipo().equals("DEP_ADMIN")|| usuario.getTipo().equals("DRC_ADMIN")){
			if(cantidadCuotas != null & !cantidadCuotas.equals("")){
				Integer cantidadCuotasInt = Integer.parseInt(cantidadCuotas);
				gru.setCantidadCuotas(cantidadCuotasInt);
			}
			if(!esNuloOVacio(conceptoUnoMonto)){
				gru.setConceptoUnoMonto(Double.parseDouble(conceptoUnoMonto));
			}
			if(!esNuloOVacio(conceptoDosMonto)){
				gru.setConceptoDosMonto(Double.parseDouble(conceptoDosMonto));
			}else{
				gru.setConceptoDosMonto(0.0);	
			}
		}
	
		
		// ESTO ES PARA CUANDO TERMINEN EL CAMBIO DE 3W PARA PODER MARCAR QUE GRUPOS PARA TRAMITES.
		//gru.setTipoInforme(Integer.parseInt(request.getParameter("tipoInforme")));
		/*if(request.getParameter("tramite_ingreso")==null || request.getParameter("tramite_ingreso").equals("off")){
			gru.setTramiteIngreso('N');
		}else{
			gru.setTramiteIngreso('S');
		}
		
		if(request.getParameter("tramite_grado")==null || request.getParameter("tramite_grado").equals("off")){
			gru.setTramiteGrado('N');
		}else{
			gru.setTramiteGrado('S');
		}
		
		if(request.getParameter("tramite_posgrado")==null || request.getParameter("tramite_posgrado").equals("off")){
			gru.setTramitePosgrado('N');
		}else{
			gru.setTramitePosgrado('S');
		}*/
		
		
		return gru;
	}
	
	public void buscarGrupoPorCodigo() {
		String codigo = request.getParameter("codigo");
		Long codigoLong;
		List<Grupo> Grupos = null;
		String grupoJson="";
		
		if(codigo != null && !codigo.equals("")){
			codigoLong = Long.parseLong(codigo);
			Grupos = FactoryDAO.grupoDAO.buscarGrupoCodigo(codigoLong);
		}
		if(Grupos != null){
			grupoJson = FactoryJson.grupoJson.toJson(Grupos,true,false);
		}
		System.out.println("GrupoJson: "+grupoJson);
		this.enviarJson(response,grupoJson);
	}
	public void altaPartidaGrupoJson(){
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
	public void modificarPartidaGrupoJson(){
		String descripcion = request.getParameter("descripcion");
		String idPartida = request.getParameter("idPartida");

		Partida partida = FactoryDAO.partidaDAO.findByInteger(idPartida);
		if(partida !=null){
			partida.setDescripcion(descripcion);
			FactoryDAO.partidaDAO.save(partida);
			System.out.println("idPartida: "+ idPartida);
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
