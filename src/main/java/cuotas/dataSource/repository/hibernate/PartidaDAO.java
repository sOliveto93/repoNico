package cuotas.dataSource.repository.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Property;

import common.dataSource.repository.GenericHibernateDAO;
import cuotas.model.domain.datos.Partida;
import cuotas.model.domain.factura.FacturaItemTipo;

public class PartidaDAO extends GenericHibernateDAO<Partida, Integer> {
	public List<Partida> listAllOrder(){
		Criteria criterio = getSession().createCriteria(Partida.class);
		criterio.addOrder(Property.forName("descripcion").asc());
		return (List<Partida>)criterio.list();
	}
}
