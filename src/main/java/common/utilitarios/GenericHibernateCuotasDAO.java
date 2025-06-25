package common.utilitarios;

import java.io.Serializable;

import org.hibernate.Interceptor;
import org.hibernate.Session;
import common.dataSource.repository.GenericHibernateDAO;
import common.dataSource.repository.HibernateUtil;


public class GenericHibernateCuotasDAO<T extends Serializable, ID extends Serializable> extends GenericHibernateDAO<T,ID> {
	
	
	
	public GenericHibernateCuotasDAO(String defaultOrder) {
		super();
		this.defaultOrder = defaultOrder;
	}
	
	protected Session getSession(){
		return HibernateUtil.getSession();
	}

}
