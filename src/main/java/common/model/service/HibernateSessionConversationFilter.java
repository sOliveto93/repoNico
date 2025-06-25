package common.model.service;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FlushMode;
import org.hibernate.classic.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.context.ManagedSessionContext;
import org.hibernate.Transaction;
import common.dataSource.repository.HibernateUtil;


public class HibernateSessionConversationFilter
implements Filter {

private static Log log = LogFactory.getLog(HibernateSessionConversationFilter.class);

private SessionFactory sf;

public static final String HIBERNATE_SESSION_KEY = "hibernateSession";
public static final String END_OF_CONVERSATION_FLAG = "endOfConversation";
//TODO METODO MODIFICADO PARA COMPATIBILIDAD DEBAJO QUEDA COMENTADO EL ANTIGUO
public void doFilter(ServletRequest request,
					 ServletResponse response,
					 FilterChain chain)
		throws IOException, ServletException {

	org.hibernate.classic.Session currentSession = null;
	HttpSession httpSession = ((HttpServletRequest) request).getSession();
	Session disconnectedSession = (Session) httpSession.getAttribute(HIBERNATE_SESSION_KEY);

	Transaction tx = null;

	try {
		if (disconnectedSession == null) {
			log.debug(">>> New conversation");
			currentSession = sf.openSession();
			//TODO currentSession.setFlushMode(org.hibernate.FlushMode.MANUAL);
			currentSession.setFlushMode(org.hibernate.FlushMode.NEVER);
		} else {
			log.debug("< Continuing conversation");
			currentSession = (org.hibernate.classic.Session) disconnectedSession;
		}

		log.debug("Binding the current Session");
		ManagedSessionContext.bind(currentSession);

		log.debug("Starting a database transaction");
		tx = currentSession.beginTransaction();

		log.debug("Processing the event");
		chain.doFilter(request, response);

		log.debug("Unbinding Session after processing");
		ManagedSessionContext.unbind(sf);

		if (request.getAttribute(END_OF_CONVERSATION_FLAG) != null ||
				request.getParameter(END_OF_CONVERSATION_FLAG) != null) {

			log.debug("Flushing Session");
			currentSession.flush();

			log.debug("Committing the database transaction");
			tx.commit();

			log.debug("Closing the Session");
			currentSession.close();

			log.debug("Cleaning Session from HttpSession");
			httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);

			log.debug("<<< End of conversation");

		} else {
			log.debug("Committing database transaction");
			tx.commit();

			log.debug("Storing Session in the HttpSession");
			httpSession.setAttribute(HIBERNATE_SESSION_KEY, currentSession);

			log.debug("> Returning to user in conversation");
		}

	} catch (StaleObjectStateException staleEx) {
		log.error("This interceptor does not implement optimistic concurrency control!");
		log.error("Your application will not work until you add compensation actions!");
		throw staleEx;
	} catch (Throwable ex) {
		try {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			if (currentSession != null) {
				ManagedSessionContext.unbind(sf);
				currentSession.close();
			}
			httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);
		} catch (Throwable rbEx) {
			log.error("Could not rollback transaction after exception!", rbEx);
		}
		throw new ServletException(ex);
	}
}

//public void doFilter(ServletRequest request,
//                 ServletResponse response,
//                 FilterChain chain)
//    throws IOException, ServletException {
//
//org.hibernate.classic.Session currentSession = null;
//
//// Try to get a Hibernate Session from the HttpSession
//	HttpSession httpSession = ((HttpServletRequest) request).getSession();
//	Session disconnectedSession = (Session) httpSession.getAttribute(HIBERNATE_SESSION_KEY);
//
//	try {
//
//	    // Start a new conversation or in the middle?
//	    if (disconnectedSession == null) {
//	        log.debug(">>> New conversation");
//	        currentSession = sf.openSession();
//			//TODO OJO SE CAMBIO PORQUE EN VERSIONES ANTERIORES MANUAL=NEVER
//			//TODO currentSession.setFlushMode(org.hibernate.FlushMode.MANUAL);
//			currentSession.setFlushMode(org.hibernate.FlushMode.MANUAL);
//
//	    } else {
//	        log.debug("< Continuing conversation");
//	        currentSession = (org.hibernate.classic.Session) disconnectedSession;
//	    }
//
//	    log.debug("Binding the current Session");
//	    ManagedSessionContext.bind(currentSession);
//
//	    log.debug("Starting a database transaction");
//	    currentSession.beginTransaction();
//
//	    log.debug("Processing the event");
//	    chain.doFilter(request, response);
//
//	    log.debug("Unbinding Session after processing");
//	    currentSession = ManagedSessionContext.unbind(sf);
//
//	    // End or continue the long-running conversation?
//	    if (request.getAttribute(END_OF_CONVERSATION_FLAG) != null ||
//	        request.getParameter(END_OF_CONVERSATION_FLAG) != null) {
//
//	        log.debug("Flushing Session");
//	        currentSession.flush();
//
//	        log.debug("Committing the database transaction");
//
//
//			currentSession.getTransaction().commit();//.beginTransaction().commit(); //.getTransaction().commit();
//
//
//			log.debug("Closing the Session");
//	        currentSession.close();
//
//	        log.debug("Cleaning Session from HttpSession");
//	        httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);
//
//	        log.debug("<<< End of conversation");
//
//	    } else {
//
//	        log.debug("Committing database transaction");
//	        currentSession.getTransaction().commit();
//
//	        log.debug("Storing Session in the HttpSession");
//	        httpSession.setAttribute(HIBERNATE_SESSION_KEY, currentSession);
//
//	        log.debug("> Returning to user in conversation");
//	    }
//
//	} catch (StaleObjectStateException staleEx) {
//	    log.error("This interceptor does not implement optimistic concurrency control!");
//	    log.error("Your application will not work until you add compensation actions!");
//	    // Rollback, close everything, possibly compensate for any permanent changes
//	    // during the conversation, and finally restart business conversation. Maybe
//	    // give the user of the application a chance to merge some of his work with
//	    // fresh data... what you do here depends on your applications design.
//	    throw staleEx;
//	} catch (Throwable ex) {
//	    // Rollback only
//	    try {
//	        if (currentSession != null && /*sf.getCurrentSession()*/currentSession.getTransaction().isActive()) {
//	            log.debug("Trying to rollback database transaction after exception");
//	            /*sf.getCurrentSession()*/currentSession.getTransaction().rollback();
//	        }
//	    } catch (Throwable rbEx) {
//	        log.error("Could not rollback transaction after exception!", rbEx);
//	    } finally {
//	        log.error("Cleanup after exception!");
//
//	        // Cleanup
//	        log.debug("Unbinding Session after exception");
//	        currentSession = ManagedSessionContext.unbind(sf);
//
//	        if(currentSession != null){
//	        	log.debug("Closing Session after exception");
//	        	currentSession.close();
//	    	}
//
//	        log.debug("Removing Session from HttpSession");
//	        httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);
//
//	    }
//
//	    // Let others handle it... maybe another interceptor for exceptions?
//	    throw new ServletException(ex);
//	}
//
//}

public void init(FilterConfig filterConfig) throws ServletException {
log.debug("Initializing filter...");
log.debug("Obtaining SessionFactory from static HibernateUtil singleton");
sf = HibernateUtil.getSessionFactory();
}

public void destroy() {}

}