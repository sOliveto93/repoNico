package cuotas.dataSource.repository.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import common.dataSource.repository.GenericHibernateDAO;
import cuotas.model.domain.factura.Factura;
import cuotas.model.domain.factura.TipoImpresion;
import cuotas.model.domain.factura.TipoPago;


public class TipoImpresionDAO extends GenericHibernateDAO<TipoImpresion, Integer> {
	
	public TipoImpresion tipoImpresionPredeterminado(){
    	Criteria criterio = getSession().createCriteria(TipoImpresion.class);
    	
    	criterio.add(Restrictions.eq("predeterminado", true));
    	return (TipoImpresion) criterio.uniqueResult();
	}
		
}
