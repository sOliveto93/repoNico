package cuotas.dataSource.repository.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hsqldb.lib.Storage;

import utilitarios.GenericHibernateCuotasDAO;
import common.dataSource.repository.GenericHibernateDAO;
import common.model.domain.datos.Estado;
import common.model.domain.datosPersonales.Documento;
import common.model.domain.fecha.Fecha;
import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.Inscripcion;
import cuotas.model.domain.datos.Pago;
import cuotas.model.domain.datos.Persona;
import cuotas.model.domain.datos.UsuarioCuotas;

public class PersonaDAO extends GenericHibernateCuotasDAO<Persona, Long> {
	

	public PersonaDAO() {
		super("apellido, nombre");
	}
	public List<Persona> buscarPorMail(String mail){
		Criteria criterio = getSession().createCriteria(Persona.class);
		criterio.add(Restrictions.eq("mail", mail));
		return (List<Persona>)criterio.list();
	}
	
	public Boolean existeMail(String mail){
		Boolean respuesta=null;
		List<Persona> personas = this.buscarPorMail(mail);
		//System.out.println("personas.size: " + personas.size());
		if(personas!=null && personas.size()>0){
			respuesta = true;
		}else{
			respuesta = false;
		}
		return respuesta;
	}
	
	public Integer numeroMaximoCuota(Persona persona){
		Criteria criterio = getSession().createCriteria(Persona.class);
		criterio.createAlias("pagos", "pago")
		.add(Restrictions.eq("pago.persona", persona))
		.setProjection(Projections.max("pago.numero"));
		
		return (Integer)criterio.uniqueResult();
	}
	
	public List<Persona> personasPorGrupo(Grupo grupo){
		if(grupo==null)
			return null;
		
		Criteria criterio = getSession().createCriteria(Persona.class);
		criterio.createAlias("pagos", "pago");
		criterio.setFetchMode("pago", FetchMode.JOIN)
				.setFetchMode("pago.grupo", FetchMode.JOIN)
				.add(Restrictions.eq("pago.grupo",grupo));
		criterio.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Persona>)criterio.list();
	}
	public List<Persona> personasConChequeraPorGrupo(Grupo grupo,Integer ano,String meses,Persona personaBuscada){
		if(grupo==null)
			return null;
		
		Criteria criterio = getSession().createCriteria(Persona.class);
		if(personaBuscada!=null){
			criterio.add(Restrictions.eq("documento.numero", personaBuscada.getDocumento().getNumero()));
		}
		criterio.createAlias("pagos", "pago");
		criterio.setFetchMode("pago", FetchMode.JOIN)
				.add(Restrictions.eq("pago.ano",ano))
				.add(Restrictions.eq("pago.monPgo",Double.parseDouble("0.0")));
		
		Fecha fechaInicio;
		Fecha fechaFin;
		
		if(meses.equals("1-6")){
			fechaInicio = new Fecha("01-01-"+ano.toString());
			fechaFin = new Fecha("01-06-"+ano.toString());
			
			criterio.add(Restrictions.ge("pago.fecha", fechaInicio))
			.add(Restrictions.le("pago.fecha", fechaFin));
		}else if(meses.equals("7-12")){
			fechaInicio = new Fecha("01-07-"+ano.toString());
			fechaFin = new Fecha("01-12-"+ano.toString());
			
			criterio.add(Restrictions.ge("pago.fecha", fechaInicio))
			.add(Restrictions.le("pago.fecha", fechaFin));
		}
		
		criterio.setFetchMode("pago.grupo", FetchMode.JOIN)
				.add(Restrictions.eq("pago.grupo",grupo));
		criterio.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Persona>)criterio.list();
	}
	
	public List<Persona> personasPorCarreraGrupo(Grupo grupo){
		if(grupo==null){
			return null;
		}
		
		Criteria criterio = getSession().createCriteria(Persona.class);
		criterio.createAlias("inscripciones", "inscripcion");
		criterio.setFetchMode("inscripcion", FetchMode.JOIN) //.createAlias("inscripcion.carrera", carrera)
				.setFetchMode("inscripcion.carrera", FetchMode.JOIN)
				.createAlias("inscripcion.carrera.grupos", "grupo")
				.setFetchMode("grupo", FetchMode.JOIN)
				.add(Restrictions.eq("grupo",grupo));
		criterio.add(Restrictions.eq("inscripcion.estado",'A'));
		criterio.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Persona>)criterio.list();
	}
	public List<Persona> buscarPersonasPorDocumento(String nroDocumento){
		List<Persona>personas = buscarPorCampo("documento.numero", nroDocumento, "estado", false);		
		return personas;
	}
	
	// le tengo que poner un parametro mas para que sepa buscar 
	// las personas que adeudan cuotas de mas 60 dias o las persona tienen pagos
	// en los ultimos 60 dias pra poder exceptuarlos de las cuotas.
	/**
	 * 
	 * @param personaBuscada
	 * @param carreraBuscada
	 * @param cohorte
	 * @param maxResults
	 * @param firstResult
	 * @param estado: para esta funcion se lo esta pasando en null
	 * @param mora: string de valor on o off para indicar si se controla que adeude o no.
	 * @param anioTope: Viene en On si hay que filtrar solo el a�o actual, sino viene en of
	 * @return
	 */	
	public List<Persona> filtrarPorMora(Persona personaBuscada, Carrera carreraBuscada, Integer cohorte,
											Integer maxResults,Integer firstResult, Character estado,String mora,String anioTope){
		Criteria criterio = filtrarPersonaCriterio(personaBuscada, carreraBuscada, cohorte, maxResults, firstResult, null, null);
		System.out.println("criterio: " + criterio.toString());
		Fecha fechaVencimiento = new Fecha(Fecha.getFechaActual());
				
		Fecha tope = null;
		Fecha topeFin = null;
		if(anioTope!=null && anioTope.equals("on")){
			tope = new Fecha("01-01-"+fechaVencimiento.getAnio());
			topeFin = new Fecha("31-12-"+fechaVencimiento.getAnio());
		}
		
		//Antes tenia 65 dias menos ahora tiene 60.
		fechaVencimiento.sumarDias(-60);
		System.out.println("fechaVencimiento: "+fechaVencimiento);
		criterio.createAlias("pagos", "pago");
		if(tope!=null){
			criterio.add(Restrictions.ge("pago.fecha",tope))
					.add(Restrictions.le("pago.fecha",topeFin));			
		}
		
		if(mora != null && mora.equals("on")){
			criterio.add(Restrictions.le("pago.fecha", fechaVencimiento))
					.add(Restrictions.isNull("pago.fechaPgo"));
					//.add(Restrictions.eq("pago.exceptuarMora", false));
		}else{
			//trae todos los que no adeudan...preguntar cuantos dias desde la fecha de hoy bla bla bla			
			//fechaVencimiento.sumarDias(+30);
			System.out.println(fechaVencimiento);
			criterio.add(Restrictions.le("pago.fecha", fechaVencimiento))
					.add(Restrictions.isNotNull("pago.fechaPgo"));
		}		
		criterio.add(Restrictions.eq("pago.dadodebaja", false))
			.setFetchMode("pago", FetchMode.JOIN)
			.setFetchMode("pago.grupo", FetchMode.JOIN)
			.createAlias("pago.grupo", "grupo")
			.add(Restrictions.eq("grupo.ctrlPago",'S'));
		
		/*if(carreraBuscada != null){
			criterio.add(Restrictions.eq("grupo.carrera",carreraBuscada));
		}*/
		System.out.println("criterio2: " + criterio.toString());
		criterio.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return (List<Persona>)criterio.list();
	}
	public Criteria filtrarPersonaCriterio(Persona personaBuscada, Carrera carreraBuscada, Integer cohorte,Integer maxResults,Integer firstResult, Character estado, UsuarioCuotas usuario){
		Criteria criterio = getSession().createCriteria(Persona.class);
		if(personaBuscada != null){
			if( personaBuscada.getNombre() != null && !personaBuscada.getNombre().equals("")){
				criterio.add(Restrictions.like("nombre", "%"+personaBuscada.getNombre()+"%"));
			}
			if( personaBuscada.getApellido() != null && !personaBuscada.getApellido().equals("")){
				criterio.add(Restrictions.like("apellido", "%"+personaBuscada.getApellido()+"%"));
			}
			if(personaBuscada.getDocumento().getTipo() !=null && !personaBuscada.getDocumento().getTipo().equals("")){
				System.out.println("tipoDocumento: "+ personaBuscada.getDocumento().getTipo());
				criterio.add(Restrictions.eq("documento.tipo.abreviacion",personaBuscada.getDocumento().getTipo().getAbreviacion()));
			}
			if(personaBuscada.getDocumento().getNumero() != null && !personaBuscada.getDocumento().getNumero().equals("")){
				criterio.add(Restrictions.eq("documento.numero", personaBuscada.getDocumento().getNumero()));	
			}
			
			if(personaBuscada.getMail() != null && !personaBuscada.getMail().equals("")){
				System.out.println("mail: "+ personaBuscada.getMail());
				criterio.add(Restrictions.like("mail", personaBuscada.getMail()));
			}
			
			Estado estadoP = new Estado("activo");
			criterio.add(Restrictions.like("estado",estadoP ));
			System.out.println("estadoP: "+estadoP);
		}
		
		if(maxResults != null && !maxResults.equals("")){
			//System.out.println("entro al limit: " +maxResults);
			criterio.setFirstResult(firstResult)
					.setMaxResults(maxResults);
		}
		boolean flagFetchIncripcionCarrera = false;
		if (carreraBuscada != null || cohorte!=null){
			
			criterio.createAlias("inscripciones", "inscripcion");			
			criterio.setFetchMode("inscripcion", FetchMode.JOIN)
					.setFetchMode("inscripcion.carrera", FetchMode.JOIN)
					.createAlias("inscripcion.carrera", "carrera");
			flagFetchIncripcionCarrera = true;
					if(cohorte!=null){
			System.out.println("cohorteDAO: "+cohorte);
						criterio.add(Restrictions.eq("inscripcion.cohorte", cohorte));
					}
					
					if(carreraBuscada != null ){
						criterio.add(Restrictions.eq("inscripcion.carrera",carreraBuscada));
					}
					if(estado!=null){
						criterio.add(Restrictions.eq("inscripcion.estado",estado));
					}
		}
		
		if(usuario != null){//|| (usuario.getTipo().equals("auditoria")
			if((usuario.getTipo().equals("DEP")) || (usuario.getTipo().equals("DRIYC"))){
				
				if(!flagFetchIncripcionCarrera){
					criterio.createAlias("inscripciones", "inscripcion");			
					criterio.setFetchMode("inscripcion", FetchMode.JOIN)
							.setFetchMode("inscripcion.carrera", FetchMode.JOIN)
							.createAlias("inscripcion.carrera", "carrera");
				}
				System.out.println("UsuarioID:" + usuario.getId());	
				criterio.setFetchMode("carrera.grupos", FetchMode.JOIN)
				.createAlias("carrera.grupos", "grupo")
				.createAlias("grupo.usuariosGrupo", "usuario")								
				.add(Restrictions.eq("usuario.id", usuario.getId()));
				System.out.println("TipoUsuario: " + usuario.getTipo());
			}
		}
		
		criterio.addOrder(Property.forName("apellido").asc());
	//	System.out.println("criterio: " + criterio.toString());
		return criterio;
	}
	public List<Persona> filtrar(Persona personaBuscada){
		
		return null;
	}
	public List<Persona> filtrar(Persona personaBuscada, Carrera carreraBuscada, Integer cohorte,Integer maxResults,Integer firstResult, Character estado, UsuarioCuotas usuario){
		Criteria criterio;

		if (personaBuscada == null && carreraBuscada == null && cohorte == null)
			return null;
		
		criterio = filtrarPersonaCriterio(personaBuscada, carreraBuscada, cohorte, maxResults, firstResult, estado, usuario);
		
		List<Persona> respuesta;
		
		if(criterio.list().iterator().hasNext()){
			respuesta = (List<Persona>)criterio.list();
		}
		else{
			respuesta = null;
		}
		return respuesta;
	}
	
	public List<Persona> personasPorCarrera(Carrera carreraBuscada,Persona personaBuscada,Integer cohorte,Character estado){
		return this.filtrar(personaBuscada,carreraBuscada,cohorte,null,null, estado, null);
		
	}
	
	public Persona buscarPorDocumento(Documento documento){
		
		Persona personaBuscada = new Persona();
		personaBuscada.setDocumento(documento);
		List<Persona>personas = this.filtrar(personaBuscada, null,null, 1, 0,null, null);
		Persona persona = null;
		if(personas != null)
			
			persona = personas.iterator().next();
		
		return persona;
	}
		
	public Persona buscarPorDocumento(String nroDocumento){
		List<Persona>personas = buscarPorCampo("documento.numero", nroDocumento, "estado", false);		
		if(personas.iterator().hasNext()){
			return personas.iterator().next();
		}
		return null;
	}

	public String buscarTipoMora(Persona personaBuscada){
		Fecha fechaDeHoy = new Fecha(Fecha.getFechaActual());
		//Antes tenia 65 dias menos ahora tiene 60.
		fechaDeHoy.sumarDias(-60);
		Criteria criterio = getSession().createCriteria(Grupo.class)
					.add(Restrictions.eq("ctrlPago",'S'));
		criterio.setFetchMode("pagos", FetchMode.JOIN)
			.createAlias("pagos", "pago")			
			.setFetchMode("pago.persona", FetchMode.JOIN)			
			//.createAlias("pago.persona", "persona")
			.add(Restrictions.eq("pago.persona",personaBuscada))
			.add(Restrictions.le("pago.fecha", fechaDeHoy));
			//.add(Restrictions.isNull("pago.fechaPgo"));					
		criterio.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		String salida = "";
		Iterator<Grupo> grupos = criterio.list().iterator();
		Grupo grupo;
		
		while (grupos.hasNext()){
			grupo = grupos.next();			
			if(grupo.getCarrera().getTipo().equals("grado")){
				if(personaBuscada.getLegajoGrado()!=null){
					salida = salida + grupo.getCarrera().getTipo()+","+personaBuscada.getLegajoGrado()+","+ grupo.getCarrera().getCodigo() + "|";
				}
			}
			if(grupo.getCarrera().getTipo().equals("posgrado")){
				if(personaBuscada.getLegajoPosgrado()!=null){
					salida = salida + grupo.getCarrera().getTipo()+","+personaBuscada.getLegajoPosgrado()+","+ grupo.getCarrera().getCodigo() + "|";
				}
			}
		}
		return salida;
			
		
	}
}
