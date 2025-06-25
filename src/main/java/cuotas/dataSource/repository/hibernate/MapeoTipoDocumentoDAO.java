package cuotas.dataSource.repository.hibernate;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import common.dataSource.repository.GenericHibernateDAO;
import cuotas.model.domain.datos.MapeoTipoDocumento;

public class MapeoTipoDocumentoDAO extends GenericHibernateDAO<MapeoTipoDocumento, Long> {
	
	@SuppressWarnings("unchecked")
	public List<String> listTiposDocumento(){
		List<String> respuesta = null;
		
		Criteria criterio = getSession().createCriteria(MapeoTipoDocumento.class);
		criterio.setProjection(Projections.distinct(Projections.property("tipoDocumentoCuotas")));

		respuesta = (List<String>)criterio.list();
		return respuesta;
	}
}
