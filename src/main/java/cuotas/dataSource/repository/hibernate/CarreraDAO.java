package cuotas.dataSource.repository.hibernate;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import common.dataSource.repository.GenericHibernateDAO;

import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.UsuarioCuotas;
//
public class CarreraDAO extends GenericHibernateDAO<Carrera, Long> {

	public CarreraDAO(){
		super("nombreCarrera");	
		//setMiInterceptor(new InterceptorCuotas("Carrera"));
	}
	
	public List<Carrera> buscarCarrerasCodigo(Integer codigo){
		Criteria criterio = getSession().createCriteria(Carrera.class);
		criterio.add(Restrictions.eq("codigo", codigo));
		criterio.addOrder( Property.forName("codigo").desc());
	    
	    List<Carrera> lista= (List<Carrera>)criterio.list();
	    return lista;
	}
	public List<Carrera> listAllbyUsr(UsuarioCuotas usuario){
		Criteria criterio = getSession().createCriteria(Carrera.class);
		criterio.createAlias("grupos", "grupo");
		criterio.setFetchMode("grupo", FetchMode.JOIN)
				.createAlias("grupo.usuariosGrupo", "usuariosGrupo")
				.setFetchMode("usuariosGrupo", FetchMode.JOIN)
				.add(Restrictions.eq("usuariosGrupo.id",usuario.getId()));
		//criterio.addOrder(Order.asc("codigo"));
		criterio.addOrder(Property.forName("codigo").asc());
		criterio.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	    List<Carrera> lista= (List<Carrera>)criterio.list();
	    System.out.println("Cantidad de carreras: "+ criterio.list().size());
	    return lista;
	}
	
	public Carrera buscarCarreraNombre(String nombre){
		Carrera resultado = super.buscarUnicoPorCampo("nombreCarrera", nombre);
		return resultado;
		
    }
	/**
	 * Para las pruebas se uso una carrera 1 , no deberia aparecer la carrera en la factura 
	 * @return
	 */
	public Carrera getCarreraPorDefecto(){
		
		return find(new Long(1));
	}
}
