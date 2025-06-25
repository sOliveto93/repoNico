package common.dataSource.repository;

import org.hibernate.*;
import org.hibernate.cfg.*;

public class HibernateUtil {
	private static SessionFactory sessionFactory;
	private static final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();
	private static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();

	static {
	//Initialize SessionFactory...
		try {
			/*Configuration cfg = new Configuration();
			sessionFactory = cfg.configure().buildSessionFactory();*/
			System.out.println("Pasa por static  de HibernateUtil");
			sessionFactory =  buildSessionFactory();
		} catch (Throwable ex) {
			ex.printStackTrace(System.out);
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	protected static SessionFactory buildSessionFactory(Interceptor interceptor) throws Exception {
		SessionFactory res = null;
		
		Configuration configuration = new  Configuration();
		if(interceptor != null)
			configuration.setInterceptor(interceptor);
		SessionFactory sessionFactory =   configuration.configure().buildSessionFactory();
		res = sessionFactory;
		return res;
	}
	
	protected static Interceptor getInterceptor(){
		System.out.println("Retornando interceptor nulo");
		return null;
	}
	
	protected static SessionFactory buildSessionFactory() throws Exception {
		return buildSessionFactory(null);
	}

	public static Session getSession(Interceptor interceptor) {
		Session s = (Session) threadSession.get();
		try {
			if (s == null) {			
				s = sessionFactory.openSession(interceptor);
				threadSession.set(s);
			}
		} catch (HibernateException ex) {
			throw new HibernateException(ex);
		}
		return s;
	}
	
	public static Session getSession() {
		Session s = (Session) threadSession.get();
		try {
			if (s == null) {			
				s = sessionFactory.openSession();
				threadSession.set(s);
			}
		} catch (HibernateException ex) {
			throw new HibernateException(ex);
		}
		return s;
	}

	public static void closeSession() {
		try {
			Session s = (Session) threadSession.get();
			threadSession.set(null);
			if (s != null && s.isOpen()) s.close();
		} catch (HibernateException ex) {
			throw new HibernateException(ex);
		}
	}

	public static void beginTransaction() {
		Transaction tx = (Transaction) threadTransaction.get();
		try {
			if (tx == null) {
				tx = getSession().beginTransaction();
				threadTransaction.set(tx);
			}
		} catch (HibernateException ex) {
			throw new HibernateException(ex);
		}
	}

	public static void commitTransaction() {
		Transaction tx = (Transaction) threadTransaction.get();
		try {
			if ( tx != null && !tx.wasCommitted()&& !tx.wasRolledBack() ) tx.commit();
			threadTransaction.set(null);
		} catch (HibernateException ex) {
			rollbackTransaction();
			throw new HibernateException(ex);
		}finally{
			closeSession();
		}
	}

	public static void rollbackTransaction() {
		Transaction tx = (Transaction) threadTransaction.get();
		try {
			threadTransaction.set(null);
			if ( tx != null && !tx.wasCommitted()&& !tx.wasRolledBack() ) {
				tx.rollback();
			}
		} catch (HibernateException ex) {
			throw new HibernateException(ex);
		} finally {
			closeSession();
		}
	}
	
	public static SessionFactory getSessionFactory(){  
        return sessionFactory;  
    }
}
