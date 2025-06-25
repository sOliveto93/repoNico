package cuotas.dataSource.repository.hibernate;

import java.util.Iterator;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import common.dataSource.repository.GenericHibernateDAO;
import common.dataSource.repository.ldap.UsuarioLdapDAO;
import common.model.domain.validacion.UsuarioGenerico;
import cuotas.model.domain.datos.Persona;
import cuotas.model.domain.datos.UsuarioCuotas;

public class UsuarioDAO extends GenericHibernateDAO<UsuarioCuotas,Integer> {

	public UsuarioDAO(){
		
	}
	public UsuarioCuotas existeUsuario(String usuario,String password){
		UsuarioCuotas usuarioCuotas = null;
		Criteria criterio = getSession().createCriteria(UsuarioCuotas.class);
			criterio.add(Restrictions.and(Restrictions.eq("usuario", usuario),Restrictions.eq("password", password))); 

		usuarioCuotas = (UsuarioCuotas)criterio.uniqueResult();
		
    	return usuarioCuotas; 
	}
	
	public UsuarioCuotas existeUsuario(String usuario){
		UsuarioCuotas usuarioCuotas = null;
		Criteria criterio = getSession().createCriteria(UsuarioCuotas.class);
			criterio.add(Restrictions.eq("usuario", usuario)); 
		usuarioCuotas = (UsuarioCuotas)criterio.uniqueResult();
    	return usuarioCuotas;
	}
}
