package cuotas.dataSource.repository.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import common.dataSource.repository.GenericHibernateDAO;
import common.dataSource.repository.HibernateUtil;
import common.model.domain.datosPersonales.Documento;
import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.Inscripcion;
import cuotas.model.domain.datos.Persona;

public class InscripcionDAO extends GenericHibernateDAO<Inscripcion, Long> {
	
	public List<Inscripcion> personasPorGrupo(Carrera carrera){
		Carrera carreraBuscada = carrera;
		Session session = HibernateUtil.getSession();
		Criteria criterio = session.createCriteria(Inscripcion.class);
			criterio.add(Restrictions.eq("carrera", carreraBuscada));
			
		return (List<Inscripcion>) criterio.list();
	}
	
	public List<Inscripcion> buscarPorPersona(Persona personaBuscada){
		Session session = HibernateUtil.getSession();
		Criteria criterio = session.createCriteria(Inscripcion.class);
			criterio.setFetchMode("persona", FetchMode.JOIN)
				.add(Restrictions.eq("persona", personaBuscada));
		return (List<Inscripcion>) criterio.list();
	}
	
	
	public Inscripcion findInscripcion(Persona persona, Carrera carrera){
		Criteria criterio = getSession().createCriteria(Inscripcion.class)
				.add(Restrictions.and(Restrictions.eq("carrera", carrera), Restrictions.eq("persona", persona)));
		
		return (Inscripcion) criterio.uniqueResult();
	}
	
	public Iterator<Inscripcion> buscarAlumnos(Integer cohorte,String estado,Integer nroDocumento,Carrera carrera, String becados){
		Criteria criterio = getSession().createCriteria(Inscripcion.class);
			if(cohorte !=null && !cohorte.equals("")){
				criterio.add(Restrictions.eq("cohorte", cohorte));
			}
			if(estado != null && !estado.equals("")){
				criterio.add(Restrictions.eq("estado", estado));
			}
			if(carrera!=null){
				criterio.add(Restrictions.eq("carrera", carrera));
			}
			if(nroDocumento != null){
				criterio.setFetchMode("persona", FetchMode.JOIN)
				.createAlias("persona", "p")
				.add(Restrictions.like("p.documento.numero", nroDocumento));
			}
			//Inscripcion i;
			//i.
			if(becados!=null){
				criterio.setFetchMode("personasBeca", FetchMode.JOIN)
				.createAlias("personasBeca", "pb")
				.add(Restrictions.gt("pb.descuentoBeca", 0));
			}
			criterio.addOrder(Order.asc("carrera")).addOrder(Order.asc("cohorte")).addOrder(Order.asc("persona"));
			return (Iterator<Inscripcion>) criterio.list().iterator();
	}

	/*public void dar_de_baja_inscripciones (String tipo){
		Query exQuery = getSession().createSQLQuery("CALL actualizar_alumnos(:tipo);");
		exQuery.setParameter("tipo", tipo);
		int exRows = exQuery.executeUpdate();
	}*/
	
	
}
