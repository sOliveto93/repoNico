package cuotas.dataSource.repository.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import common.dataSource.repository.GenericHibernateDAO;
import common.model.domain.datos.Estado;
import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.UsuarioCuotas;


public class GrupoDAO extends GenericHibernateDAO<Grupo, Integer> {
	String dafaultOrder = "codigo";
	
	@SuppressWarnings("unchecked")
	public List<Grupo> buscarGrupoCodigo(Long codigo){
		//No se hace con el buscar campor ya que solo recibe String como parametro
		//return FactoryDAO.grupoDAO.buscarPorCampo("codigo", codigo);
		Criteria criterio = getSession().createCriteria(Grupo.class);
		criterio.add(Restrictions.eq("codigo", codigo));
		
		Estado estado = new Estado(Estado.ACTIVO);
		criterio.add(Restrictions.eq("estado", estado));
		
		criterio.addOrder( Property.forName("codigo").desc());
	    
	    List<Grupo> lista= (List<Grupo>)criterio.list();
	    return lista;
	}
	public Grupo buscarGrupoCodigo(String codigo){
		//No se hace con el buscar campor ya que solo recibe String como parametro
		//return FactoryDAO.grupoDAO.buscarPorCampo("codigo", codigo);
		Criteria criterio = getSession().createCriteria(Grupo.class);
		criterio.add(Restrictions.eq("codigo",Long.parseLong(codigo)));
		
		Estado estado = new Estado(Estado.ACTIVO);
		criterio.add(Restrictions.eq("estado", estado));
		
		criterio.addOrder( Property.forName("codigo").desc());
	    
	    Iterator<Grupo> lista= criterio.list().iterator();
	    if(lista.hasNext()) return lista.next();
	    return null;
	}
	public List<Grupo> listAllbyUsr(UsuarioCuotas usuario){
		Criteria criterio = getSession().createCriteria(Grupo.class);
		//if(usuario.getTipo().toLowerCase().equals("dep") ||usuario.getTipo().toLowerCase().equals("driyc")){
			criterio.setFetchMode("carrera", FetchMode.JOIN)
			.createAlias("carrera", "c");
			
			criterio.setFetchMode("usuariosGrupo", FetchMode.JOIN)
			.createAlias("usuariosGrupo", "usrGrupo")
			.add(Restrictions.eq("usrGrupo.id", usuario.getId()));
			System.out.println("c.tipo: "+usuario.getTipo().toLowerCase());	
		//}
		criterio.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Grupo>)criterio.list();
	}
	public List<Grupo> listAllDep(){
		Criteria criterio = getSession().createCriteria(Grupo.class);
		criterio.add(Restrictions.ge("codigo", new Long(2000))).add(Restrictions.le("codigo", new Long(2999)));
		
		List<Grupo> lista= (List<Grupo>)criterio.list();
		return lista;
	}
	
}
