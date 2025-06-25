package cuotas.dataSource.repository.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import common.dataSource.repository.GenericHibernateDAO;
import cuotas.model.domain.datos.Inscripcion;
import cuotas.model.domain.datos.PersonasBeca;
import cuotas.model.domain.factura.Factura;

public class PersonasBecaDAO extends GenericHibernateDAO<PersonasBeca, Long> {
	public PersonasBecaDAO(){
		super("PersonasBeca");	
	}
	public PersonasBeca buscarPorInscripcion(Inscripcion inscripcion){
		Criteria criterio = getSession().createCriteria(PersonasBeca.class);
		criterio.add(Restrictions.eq("inscripcion", inscripcion));
		return (PersonasBeca) criterio.uniqueResult();
	}
}
