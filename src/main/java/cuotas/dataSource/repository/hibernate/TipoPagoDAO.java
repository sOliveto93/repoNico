package cuotas.dataSource.repository.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import common.dataSource.repository.GenericHibernateDAO;
import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.factura.TipoPago;

public class TipoPagoDAO extends GenericHibernateDAO<TipoPago, Integer> {
	
	public Integer countTipoPago(){
		Criteria criterio = getSession().createCriteria(TipoPago.class);
		criterio.setProjection(Projections.rowCount());
		
	    return (Integer) criterio.uniqueResult();
	}
	public TipoPago buscarPorPeso(String peso){
		TipoPago tipoPago =  FactoryDAO.tipoPagoDAO.buscarUnicoPorCampo("pesoOrdenamiento", peso);
		return tipoPago;
	}
	
}
