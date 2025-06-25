package cuotas.model.service;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import common.model.domain.error.ErrorGrabacion;
import common.model.domain.error.ErrorParametro;
import common.model.domain.fecha.Anio;
import common.model.domain.fecha.Mes;
import common.model.service.MappingMethodServlet;
import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.Cuota;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.TipoCuota;
import cuotas.model.domain.datos.UsuarioCuotas;
import cuotas.model.domain.json.FactoryJson;

public class GrupoCuotasServlet extends MappingMethodServlet {
	private static final long serialVersionUID = 5277354045515224530L;

	public GrupoCuotasServlet(){
		this.mapeoOperacionMetodo.put("ver_GrupoCuotas", "verGrupoCuotas");
		this.mapeoOperacionMetodo.put(CuotasServletUtils.ACTUALIZAR, "modificar");
	}
	
	public void alta() {
		//List<Cuota> grupoCuota = null;
		try{
			String idGrupo =request.getParameter("idGrupo");
			Grupo grupo=FactoryDAO.grupoDAO.findByLong(idGrupo);
			
			grupo = getGrupoCuotaActualizada(grupo);
			FactoryDAO.grupoDAO.saveOrUpdate(grupo);
			//grupoCuota = getGrupoCuotaActualizada();
			/* PENDIENTE HASTA PREGUNTAR COMO SE VA A GUARDAR LAS CUOTAS EN LA TABLA DE AUDITORIA.
			Evento evento = getEventoAuditoria(request, grupoCuota);
			FactoryDAO.eventoDAO.save(evento,false);
			*/
			//FactoryDAO.cuotaDAO.save(grupoCuota);
		}catch(ErrorParametro e){
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, e.getMSG());
		}
	}

	public void borrar() {
		try{
			String id =  request.getParameter("id");
			Cuota cuota = FactoryDAO.cuotaDAO.findByLong(id);
			if (cuota!=null){
				cuota.getEstado().desactivar();
				FactoryDAO.cuotaDAO.saveOrUpdate(cuota);
			}
			ingresar();
		}catch(ErrorGrabacion e){
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, e.getMSG());
		}
	}

	
	public void ingresar() {
		UsuarioCuotas usuario = (UsuarioCuotas)session.getAttribute("usuario");
		System.out.println("usuarioTipo: "+ usuario.getTipo() );
		
		List<Cuota> gruposCuotas = FactoryDAO.cuotaDAO.listAll();
		List<Grupo> grupos= null;
		List<Carrera> carreras = null;
		if(usuario.getTipo().equals("DEP_ADMIN")|| usuario.getTipo().equals("DRC_ADMIN")){
			carreras = FactoryDAO.carreraDAO.listAllbyUsr(usuario);
			grupos = FactoryDAO.grupoDAO.listAllbyUsr(usuario); 
		}else{
			carreras = FactoryDAO.carreraDAO.listAll();
			grupos = FactoryDAO.grupoDAO.listAll();
		}
		
		
		List<TipoCuota> tiposCuota = FactoryDAO.tipoCuotaDAO.listAll();
		
		List<Integer> anios = Anio.listAnios(1995, new GregorianCalendar().get(Calendar.YEAR)+5);
		
		String idGrupoAnterior = request.getParameter("idGrupo");
		request.setAttribute("idGrupoAnterior",idGrupoAnterior);
		
		request.setAttribute("carreras", carreras);
		request.setAttribute("anios",anios);
		request.setAttribute("gruposCuotas", gruposCuotas);
		request.setAttribute("grupos", grupos);
		request.setAttribute("tiposCuota", tiposCuota);
		
		if(usuario.getTipo().equals("DEP_ADMIN")|| usuario.getTipo().equals("DRC_ADMIN")){
			setSalto(CuotasServletUtils.SALTO_GRUPO_LISTAR_DEP_ADMIN);
		}else{
			setSalto(CuotasServletUtils.SALTO_GRUPO_LISTAR);	
		}
	}

	
	public void modificar() {
		try{
			String idGrupo =request.getParameter("idGrupo");
			Grupo grupo=FactoryDAO.grupoDAO.findByLong(idGrupo);
			
			grupo = getGrupoCuotaActualizada(grupo);		
			FactoryDAO.grupoDAO.saveOrUpdate(grupo);
			
		}catch (ErrorParametro e){
			request.setAttribute(CuotasServletUtils.MENSAJE_ERROR, e.getMSG());
		}
		ingresar();
	}
	
	public Grupo getGrupoCuotaActualizada(Grupo grupo){

		Cuota cuota = null;
		
		if(grupo != null && grupo.getCuotas() != null){
			Iterator<Cuota> cuotas  = grupo.getCuotas().iterator();
			
			/// Para actualizar las cuotas pre-existentes
			while (cuotas.hasNext()){
				cuota = cuotas.next();
				cuota = getCuotaActualizada(cuota);
				if(cuota.isMontosNulos()){
					cuotas.remove();
				}
				
			}
			// Para agregar las cuotas nuevas
			int j = grupo.getCuotas().size() +1;		
			
			String idCuotaNueva ;
			String labelMontoUno = "-montoUno";
			String labelTipo = "-tipo";
			for(int i=j; i < 23 ; i++){
				
				idCuotaNueva = "N"+i;
				String parametroMonto =  idCuotaNueva + labelMontoUno;
				String parametroTipo = idCuotaNueva + labelTipo;
				
				String montoUno = request.getParameter(parametroMonto);
				String tipo = request.getParameter(parametroTipo);
				
				// Aca solo llego si la cuota es nueva
				if(!esNuloOVacio(montoUno) && !esNuloOVacio(tipo)){
					Cuota cuotaActualizada = new Cuota();
					cuotaActualizada.setGrupo(grupo);
					cuota = getCuotaActualizada(cuotaActualizada,idCuotaNueva);

					if(!cuotaActualizada.isMontosNulos()){
						grupo.addCuota(cuotaActualizada);	
						//AGREGADO BETY....
						FactoryDAO.cuotaDAO.saveOrUpdate(cuotaActualizada);
					}else{
						System.out.println("cuota con montos nulos...");
					}
				}
			}
		}
		
		return grupo;
	}
	/**
	 * 
	 * @param request
	 * @param cuota recibe la cuota que desea actualizar
	 * @param idNuevo - si es una cuota nueva, recibe un "ID" ficticio "N"+i para buscar los datos en el formulario
	 * @return
	 */		
	protected Cuota getCuotaActualizada(Cuota cuota, String idNuevo){

		if(request == null)
			return null;
		
		String id = null;
		
		if(!esNuloOVacio(idNuevo)){
			id = idNuevo;
		}
		if(cuota == null){
			cuota = new Cuota();
		}
		if( cuota.getId() != null){	
			id = cuota.getId().toString();			
		}
		
		String montoUno = request.getParameter(id+"-montoUno");
		String montoDos = request.getParameter(id+"-montoDos");
		String tipo = request.getParameter(id+"-tipo");
		String mes = request.getParameter(id+"-mes");
		
		Integer nroMes = null;
		Double monto1=null;
		Double monto2=null;
		if(!esNuloOVacio(mes)){
			nroMes = Integer.parseInt(mes);
		}
		if(!esNuloOVacio(montoUno)){ 
			monto1 = Double.parseDouble(montoUno);
		}
		if(!esNuloOVacio(montoDos)){
			monto2= Double.parseDouble(montoDos);
		}
		
		
		
		TipoCuota tipoCuota = FactoryDAO.tipoCuotaDAO.findByInteger(tipo);
		
		cuota.setMonto1(monto1);
		cuota.setMonto2(monto2);
		cuota.setTipo(tipoCuota);
		// Por si quiero evitar que se reescriba si el mes es el mismo
		/*(cuota.getMes()== null || (cuota.getMes()!= null && !cuota.getMes().getNumero().equals(nroMes))) &&*/
		if(nroMes != null){
			cuota.setMes(new Mes(nroMes));
		}
		return cuota;
	}
	
	/**
	 * 
	 * @param cuota recibe la cuota que desea actualizar
	 * @return
	 */	
	protected Cuota getCuotaActualizada(Cuota cuota){
		return this.getCuotaActualizada(cuota, null);
	}
	
	/**
	 *  
	 * @param idNuevo - si es una cuota nueva, recibe un "ID" ficticio "N"+i para buscar los datos en el formulario
	 * @return
	 */	
	protected Cuota getCuotaActualizada(String idNuevo){
		return this.getCuotaActualizada(null, idNuevo);
	}
	

	public void verGrupoCuotas(){
		Grupo grupo = FactoryDAO.grupoDAO.findByLong(request.getParameter("idGrupo"),true);
		String jsonGrupo = FactoryJson.grupoJson.toJson(grupo,true);
		System.out.println("jsonGrupo: " + jsonGrupo );
		List<TipoCuota> tiposCuota = FactoryDAO.tipoCuotaDAO.listAll();
		request.setAttribute("tiposCuota", tiposCuota);
		this.enviarJson(response,jsonGrupo);
	}
	
}