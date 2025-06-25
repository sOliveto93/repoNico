package cuotas.dataSource.repository.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import common.dataSource.repository.GenericHibernateDAO;
import common.dataSource.repository.HibernateUtil;
import common.model.domain.datos.Estado;
import common.model.domain.fecha.Fecha;
import common.model.domain.fecha.RangoFecha;
import cuotas.model.domain.datos.CodigosAnteriores;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.OrigenPago;
import cuotas.model.domain.datos.Pago;
import cuotas.model.domain.datos.PagoSumarizado;
import cuotas.model.domain.datos.Persona;
import cuotas.model.domain.datos.UsuarioCuotas;
import cuotas.model.domain.interfaces.RegistroPago;


public class PagoDAO extends GenericHibernateDAO<Pago, Integer> {

	//RangoFecha fechaCuota,
	@SuppressWarnings("unchecked")
	public List<Pago> cancelacionesDeUnPeriodo(String desdeNumeroGrupo,String hastaNumeroGrupo,Integer cohorte, Persona persona,RangoFecha rangoFechaPago, RangoFecha rangoFechaDeCuota,RangoFecha rangoFechaDeCarga,String orderBy, OrigenPago origenPago, UsuarioCuotas usuario, String codigoMercadoPago){
		Criteria criterio = getSession().createCriteria(Pago.class);
		
		criterio.setFetchMode("grupo", FetchMode.JOIN).createAlias("grupo", "g");
		//criterio.add(Restrictions.isNotNull("fechaPgo"));
		if(!desdeNumeroGrupo.equals("") && hastaNumeroGrupo.equals("")){
			criterio.add(Restrictions.eq("g.codigo", Long.parseLong(desdeNumeroGrupo)));
		}else{
			if(!desdeNumeroGrupo.equals("")){
				criterio.add(Restrictions.ge("g.codigo", Long.parseLong(desdeNumeroGrupo)));
			}
			if(!hastaNumeroGrupo.equals("")){
				criterio.add(Restrictions.le("g.codigo", Long.parseLong(hastaNumeroGrupo)));
			}	
		}
		if(usuario!=null && !usuario.getTipo().equals("admin")){
			criterio.setFetchMode("grupo", FetchMode.JOIN)
					.createAlias("grupo.usuariosGrupo", "usuario")								
					.add(Restrictions.eq("usuario.id", usuario.getId()));			
		}
		criterio.setFetchMode("origenPago", FetchMode.JOIN).createAlias("origenPago", "op");
		
		if(origenPago!=null){
			//System.out.println("origenDePago: "+ origenPago.getDescripcion());
			if(origenPago.getDescripcion().equals("pago facil")){
				OrigenPago	origenPago2 = FactoryDAO.origenPagoDAO.findByInteger("1");
				criterio.add(Restrictions.or(Restrictions.eq("origenPago", origenPago),Restrictions.eq("origenPago", origenPago2)));
			}else if(origenPago.getId().equals(5)){
				criterio.add(Restrictions.eq("origenPago", origenPago));
				if(codigoMercadoPago != null && !codigoMercadoPago.equals("")){
					criterio.add(Restrictions.eq("codigoTodoPago",codigoMercadoPago));	
				}
			}else{
				criterio.add(Restrictions.eq("origenPago", origenPago));
			}
		}
		
		if(cohorte!=null && !cohorte.equals("") && cohorte!=0){
			criterio.setFetchMode("persona", FetchMode.JOIN);
				criterio.createAlias("persona.inscripciones", "inscripcion");
				criterio.setFetchMode("inscripcion", FetchMode.JOIN)
				.add(Restrictions.eq("inscripcion.cohorte",cohorte));
		}
		if (rangoFechaPago!=null && rangoFechaPago.getDesde()!= null)
			criterio.add(Restrictions.ge("fechaPgo", rangoFechaPago.getDesde()));
		if(rangoFechaPago != null && rangoFechaPago.getHasta()!=null)
			criterio.add(Restrictions.le("fechaPgo", rangoFechaPago.getHasta()));
		
		if(rangoFechaDeCuota != null && rangoFechaDeCuota.getDesde()!=null){
			criterio.add(Restrictions.ge("fecha", rangoFechaDeCuota.getDesde()));
		}
		if(rangoFechaDeCuota!=null &&  rangoFechaDeCuota.getHasta()!=null){
			criterio.add(Restrictions.le("fecha", rangoFechaDeCuota.getHasta()));
		}
		if(rangoFechaDeCarga != null && rangoFechaDeCarga.getDesde()!=null){
			criterio.add(Restrictions.ge("fePgoCarga", rangoFechaDeCarga.getDesde()));
		}
		if(rangoFechaDeCarga != null && rangoFechaDeCarga.getHasta()!=null){
			criterio.add(Restrictions.le("fePgoCarga", rangoFechaDeCarga.getHasta()));
		}
		
		if (persona != null){
			criterio.setFetchMode("persona", FetchMode.JOIN);
				Estado estado = new Estado(1);
				criterio.add(Restrictions.eq("persona",persona));
				
				criterio.setFetchMode("persona", FetchMode.JOIN);
				criterio.createAlias("persona", "p");
				criterio.add(Restrictions.eq("p.estado", estado));
		}
		if(orderBy!=null && !orderBy.equals("")){
			if(orderBy.equals("persona, grupo, numero")){
				criterio.addOrder(Order.asc("persona")).addOrder(Order.asc("g.codigo")).addOrder(Order.asc("numero"));
			}else if(orderBy.equals("grupo, persona")){
				if(persona ==null){
					criterio.setFetchMode("persona", FetchMode.JOIN);
					criterio.createAlias("persona", "p");
				}				
				criterio.addOrder(Order.asc("g.codigo")).addOrder(Order.asc("p.apellido"));
			}else if(orderBy.equals("partida")){
				criterio.addOrder(Order.asc("g.partida"));
			}else if(orderBy.equals("persona")){
				criterio.addOrder(Order.asc("fecha"));
			}else if(orderBy.equals("grupo")){
				criterio.addOrder(Order.asc("g.codigo"));				
			}else if(orderBy.equals("numero")){
				criterio.addOrder(Order.asc("numero"));
			}else{
				criterio.addOrder(Order.asc(orderBy));
			}
			
		}
		criterio.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Pago>) criterio.list();
	}
	
	private Criteria obtenerCriteriaPagos(Grupo grupo,Integer cohorte,String nroDocumento){
		Session session = HibernateUtil.getSession();
		Criteria criterio = session.createCriteria(Pago.class);
		
		criterio.createAlias("persona", "pers");
		criterio.setFetchMode("pers", FetchMode.JOIN);
		criterio.addOrder(Order.asc("pers.apellido"));
		criterio.addOrder(Order.asc("pers.nombre"));
		criterio.addOrder(Order.asc("fecha"));

		criterio.add(Restrictions.eq("grupo", grupo));
		//criterio.addOrder(Order.asc("persona.apellido"));
		
		if (nroDocumento != null && !nroDocumento.equals("")){
			   //criterio.setFetchMode("persona.documento", FetchMode.JOIN);
				criterio.add(Restrictions.eq("pers.documento.numero",nroDocumento));
				
		}
		if(cohorte != null && !cohorte.equals("")){
			//criterio.setFetchMode("persona", FetchMode.JOIN);
			criterio.createAlias("persona.inscripciones", "inscripcion");
			criterio.setFetchMode("inscripcion", FetchMode.JOIN)
					.add(Restrictions.eq("inscripcion.cohorte",cohorte));
		}
		return criterio;
	}
	
	public List<Pago> pagosDeIntegrantesGrupo(Grupo grupo,Integer cohorte,String nroDocumento){
		Criteria criterio = obtenerCriteriaPagos(grupo, cohorte, nroDocumento);
		return (List<Pago>)criterio.list();
	}

	public List<PagoSumarizado> sumarizadoPersonaPorGrupo(Grupo grupo){
		Criteria criterio = obtenerCriteriaPagos(grupo, null, null);
		 
		criterio.setProjection(Projections.projectionList()
				.add(Projections.property("grupo"),"grupoPago")
				.add(Projections.property("persona"),"persona")			
                .add(Projections.groupProperty("persona"))
                .add(Projections.groupProperty("grupo"))
                .add(Projections.sum("monto1").as("total")));
		
		criterio.setResultTransformer(Transformers.aliasToBean(PagoSumarizado.class));
		 
		return (List<PagoSumarizado>)criterio.list();
	}
	
	public Pago filtrar(RegistroPago rp){
		
		String cliente = rp.getClienteString();
		
		String codigo = rp.getCod_barra();
		
		//primero busco por codigo de barra
		
		Pago p = filtrar_codigo(codigo);		
		 if (p != null){
			 System.out.println("Entro 5 a");
			 rp.setNum_doc(p.getPersona().getDocumento().getNumero());
			 System.out.println("Entro 5 a"+p.getPersona().getDocumento().getNumero());
     		 rp.setNro_grupo(p.getGrupo().getCodigo());
     		 System.out.println("Entro 5 a"+p.getGrupo().getCodigo());
			 return p;
		 }
		 p = filtrar_codigo_anterior(codigo);
		 if (p != null){			 
			 rp.setNum_doc(p.getPersona().getDocumento().getNumero());			 
     		 rp.setNro_grupo(p.getGrupo().getCodigo());     		
			 return p;
		 }
		 Long idpersona = Long.parseLong(cliente)/1000;
 		 Long idgrupo = Long.parseLong(cliente)%1000;
 		 p = filtrar(idpersona, idgrupo);
 		 
 		 if (p != null){
 			System.out.println("el id de la persona y el grupo son: "+idpersona+" y "+idgrupo+", ");
 			rp.setNum_doc(p.getPersona().getDocumento().getNumero());
			rp.setNro_grupo(p.getGrupo().getCodigo());
 			p = filtrar(idpersona, idgrupo, buscarFechaCuota(codigo, p.getMonto1()+p.getMonto2())); 			
 			if(p!=null){
 				
 				return p;
 			}
 		 }
 		
 		 p = filtrar(cliente);
 		 
 		 if (p != null){    
 			System.out.println("el cliente es: "+cliente);
 			rp.setNum_doc(p.getPersona().getDocumento().getNumero());
 	 		rp.setNro_grupo(p.getGrupo().getCodigo());
        	//filtra por cliente viejo y fecha de cuota        	
        	p = filtrar(cliente, buscarFechaCuota(codigo, p.getMonto1()+p.getMonto2()));        	
        	if(p!= null){        		
        		return p;  	
        	}
        
        }
 		 return null;
        	
  }
	
public Pago filtrar_codigo(String codigo){		
	System.out.println("Entro 1");
        Criteria criterio = getSession().createCriteria(Pago.class);
        System.out.println("Entro 2");    
        criterio.add(Restrictions.eq("codigo_barra", codigo));
        System.out.println("Entro 3");
        criterio.addOrder(Order.desc("fecha"));
        if(((List<Pago>)criterio.list()).size() > 1){
        	return null;
        }
        System.out.println("Entro 4");
        Iterator<Pago> i = ((List<Pago>)criterio.list()).iterator();
        System.out.println("Entro 5");
        if(i.hasNext()) return i.next();         
        return null;
        	
  }
  public Pago filtrar_codigo_anterior(String codigo){		
    Criteria criterio = getSession().createCriteria(CodigosAnteriores.class);
    criterio.add(Restrictions.eq("codigoBarra", codigo));
    //criterio.addOrder(Order.desc("fecha"));
    if(((List<CodigosAnteriores>)criterio.list()).size() > 1){
    	return null;
    }
    Iterator<CodigosAnteriores> i = ((List<CodigosAnteriores>)criterio.list()).iterator();    
    if(i.hasNext()) return i.next().getPago();              
    return null;
    	
  }
  public Fecha buscarFechaCuota(String codigo, double valor_cuota){
	  
	  String importe1 = codigo.substring(8, 14);
	  String fecha1 = codigo.substring(14, 19);
	  Fecha fecha =new Fecha();

	  fecha.setFromJulian(fecha1);
	  String importe2 = codigo.substring(28, 34);
	  System.out.println("la fecha es: "+fecha);
	  int dia = fecha.getDia();

	  //por cada dia que tengo de mas le resto 1 al mes, hasta llegar al dia 10. Despues le resto 1 mas a les 
	  //tener en cuenta las que no empiezan en 10 (dobles cuotas)	 
	  while (dia>10){
		  //resto 12 para los casos de los cheques de tipo E (que tienen 12 dias de mas)
		  if(dia>22){
			  dia = dia -12;
		  }
		 dia--;
		 fecha.setFecha(1+"-"+fecha.getMes()+"-"+fecha.getAnio());
      }
	  System.out.println("la fecha es: "+fecha);
	  double monto_pago = Double.parseDouble(importe1)/100;
	  double mora = valor_cuota*0.02;
	  System.out.println("valor cuota "+valor_cuota+" monto pago "+monto_pago+" mora "+ mora);
	  if(valor_cuota!= 0.0){
		  while(valor_cuota<monto_pago){		  
			  monto_pago = monto_pago-mora;
			  fecha.setFecha(1+"-"+fecha.getMes()+"-"+fecha.getAnio());
		  }
	  }	  

	   System.out.println("la fecha es: "+fecha);
	  return fecha;
	  
  }
  public Pago filtrar(String cliente, Fecha fecha){	  
        Criteria criterio = getSession().createCriteria(Pago.class);
        //criterio.add(Restrictions.eq("persona.id", persona));       
        //criterio.add(Restrictions.eq("grupo.id", grupo));
        criterio.add(Restrictions.eq("codigo_usuario", cliente));
        criterio.add(Restrictions.eq("fecha", fecha));
        criterio.addOrder(Order.desc("fecha"));        
        Iterator<Pago> i = ((List<Pago>)criterio.list()).iterator();        
        if(i.hasNext()) return i.next();        
        return null;
  }
  public Pago filtrar(Long idpersona, Long idgrupo, Fecha fecha){	  
      Criteria criterio = getSession().createCriteria(Pago.class);
      criterio.add(Restrictions.eq("persona.id", idpersona));       
      criterio.add(Restrictions.eq("grupo.id", idgrupo));
      //criterio.add(Restrictions.eq("codigo_usuario", cliente));
      criterio.add(Restrictions.eq("fecha", fecha));
      criterio.addOrder(Order.desc("fecha"));        
      Iterator<Pago> i = ((List<Pago>)criterio.list()).iterator();        
      if(i.hasNext()) return i.next();        
      return null;
}
  public Pago filtrar(String cliente){	  
      Criteria criterio = getSession().createCriteria(Pago.class);
      //criterio.add(Restrictions.eq("persona.id", persona));       
      //criterio.add(Restrictions.eq("grupo.id", grupo));      
      criterio.add(Restrictions.eq("codigo_usuario", cliente));
      criterio.addOrder(Order.asc("fecha"));   
      Iterator<Pago> i = ((List<Pago>)criterio.list()).iterator();      
      if(i.hasNext()) return i.next();
      return null;
}
  public Pago filtrar(Long idpersona, Long idgrupo){	  
	  
      Criteria criterio = getSession().createCriteria(Pago.class);
      criterio.add(Restrictions.eq("persona.id", idpersona));  
      criterio.setFetchMode("grupo", FetchMode.JOIN).createAlias("grupo", "g").add(Restrictions.eq("g.numero", idgrupo));
      criterio.addOrder(Order.asc("fecha"));   
      Iterator<Pago> i = ((List<Pago>)criterio.list()).iterator();
      if(i.hasNext()) return i.next();
      return null;
}
	
	public List<Pago> pagosPorPersona(Persona persona, Grupo grupo,boolean desdePersona){
		Criteria criterio = getSession().createCriteria(Pago.class);
		criterio.add(Restrictions.eq("estado", new Estado(1)));
		criterio.add(Restrictions.eq("persona.id", persona.getId()));
	System.out.println("criterio.add(Restrictions.eq(persona.id,"+persona.getId()+"));");
		criterio.add(Restrictions.eq("grupo.id", grupo.getId()));
	System.out.println("criterio.add(Restrictions.eq(grupo.id,"+grupo.getId()+"))");
		if(!desdePersona){
	System.out.println("criterio.add(Restrictions.eq(ano,"+ grupo.getAnioCuota()+"))");
			criterio.add(Restrictions.eq("ano", grupo.getAnioCuota()));
		}
		criterio.addOrder(Order.asc("fecha"));
		criterio.addOrder(Order.asc("numero"));
		return (List<Pago>)criterio.list();		
	}
	public List<Pago> pagosPorAnoPersona(Persona persona, Grupo grupo,Integer ano){
		Criteria criterio = getSession().createCriteria(Pago.class);
		
		criterio.add(Restrictions.eq("estado", new Estado(1)));
		criterio.add(Restrictions.eq("ano", ano));
		criterio.add(Restrictions.eq("persona.id", persona.getId()));
		criterio.add(Restrictions.eq("grupo.id", grupo.getId()));
		criterio.addOrder(Order.asc("fecha"));
		criterio.addOrder(Order.asc("numero"));
		return (List<Pago>)criterio.list();		
	}
	public int numeroPagoPorPersona(Persona persona, Grupo grupo, boolean desdePersona){
		Criteria criterio = getSession().createCriteria(Pago.class);
		criterio.add(Restrictions.eq("persona.id", persona.getId()));		
		criterio.add(Restrictions.eq("grupo.id", grupo.getId()));
		if(!desdePersona){
			criterio.add(Restrictions.lt("ano", grupo.getAnioCuota()));
		}else{
			criterio.add(Restrictions.ne("monPgo",0.0));
		}
		criterio.addOrder(Order.desc("numero"));
		criterio.setMaxResults(1);
		if(criterio.list().iterator().hasNext()) return ((Pago)criterio.list().iterator().next()).getNumero();
		return 0;
	}
	public List<Pago> pagosPorPersona(Persona personaBuscada){
		Session session = HibernateUtil.getSession();
		Criteria criterio = session.createCriteria(Pago.class);
			criterio.add(Restrictions.eq("persona", personaBuscada));
		return (List<Pago>) criterio.list();
	}
	public List<Pago> buscarNoProcesados(){
		Session session = HibernateUtil.getSession();
		Criteria criterio = session.createCriteria(Pago.class);
			criterio.add(Restrictions.eq("procesado", false));
			//criterio.createAlias("grupo", "grupo");
			//criterio.setFetchMode("grupo", FetchMode.JOIN);			
			//criterio.add(Restrictions.eq("grupo.ctrlPago",'S' ));
		return (List<Pago>) criterio.list();

	}
}