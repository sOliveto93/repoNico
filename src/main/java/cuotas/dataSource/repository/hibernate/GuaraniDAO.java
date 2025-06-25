package cuotas.dataSource.repository.hibernate;

import java.util.List;



import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import common.dataSource.repository.GenericHibernateDAO;
import common.dataSource.repository.HibernateUtil;


import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.Inscripcion;
import cuotas.model.domain.interfaces.Guarani;

public class GuaraniDAO extends GenericHibernateDAO<Guarani, Integer> {
	public List<Guarani> buscarNoSincronizados(){		
		Session session = HibernateUtil.getSession();
		Criteria criterio = session.createCriteria(Guarani.class);
			criterio.add(Restrictions.eq("procesado", false));			
		return (List<Guarani>) criterio.list();
	}
}
