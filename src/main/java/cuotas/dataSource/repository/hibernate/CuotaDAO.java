package cuotas.dataSource.repository.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import common.dataSource.repository.GenericHibernateDAO;
import cuotas.model.domain.datos.Cuota;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.Persona;
import cuotas.model.domain.factura.Factura;

public class CuotaDAO extends GenericHibernateDAO<Cuota, Integer> {
	
	public CuotaDAO(){
		super("Mes.numero");
	}
}
