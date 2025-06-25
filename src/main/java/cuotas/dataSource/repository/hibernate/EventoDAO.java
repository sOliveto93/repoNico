package cuotas.dataSource.repository.hibernate;

import common.dataSource.repository.GenericHibernateDAO;
import common.model.domain.util.Evento;

public class EventoDAO extends GenericHibernateDAO<Evento, Long> {
	
	public EventoDAO(){
		super("fecha");		
	}

}
