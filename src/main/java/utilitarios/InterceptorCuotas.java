package utilitarios;

import java.io.Serializable;
import java.util.Iterator;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.type.Type;

public class InterceptorCuotas extends EmptyInterceptor {
	private static final long serialVersionUID = 4269848330046712215L;

	Session session;
	String nombreEntity ;
	
	public InterceptorCuotas(){
		this.nombreEntity =  "SIN_NOMBRE";
	}
	public InterceptorCuotas(String nombreEntity){
		this.nombreEntity = nombreEntity;
	}
	public void setSession(Session session) {
		this.session=session;
	}
 
	public boolean onSave(Object entity,Serializable id,
		Object[] state,String[] propertyNames,Type[] types)
		throws CallbackException {
		System.out.println("OnSave");
		int cantidadPropiedades = state.length ;
		for(int i=0 ; i < cantidadPropiedades ; i++){
			System.out.println("Propiedad: "+ state[i].toString());
		}	
		
		System.out.println(" Grabando "+ this.nombreEntity + " con valores " + entity.toString() ); 
		
		return false;
	}
 
	public boolean onFlushDirty(Object entity,Serializable id,
		Object[] currentState,Object[] previousState,
		String[] propertyNames,Type[] types)
		throws CallbackException {
		System.out.println("onFlushDirty");
		System.out.println("onFlushDirty: " + entity.toString());
		System.out.println("******************************************************************************");
		int cantidadPropiedades = propertyNames.length ;
		for(int i=0 ; i<cantidadPropiedades ; i++){
			System.out.println("Propiedad: "+ propertyNames[i]);
		}
		int cantidadPropiedades2 = previousState.length ;
		for(int i=0 ; i<cantidadPropiedades2 ; i++){
			System.out.println("Propiedad: "+ previousState[i].toString());
		}
		System.out.println("******************************************************************************");
		return false;
	}
 
	public void onDelete(Object entity, Serializable id, 
		Object[] state, String[] propertyNames, 
		Type[] types) {
		System.out.println("onDelete: " + entity.toString());
 	}
 
	//called before commit into database
	public void preFlush(Iterator iterator) {
		System.out.println("preFlush");
	}	
 
	//called after committed into database
	public void postFlush(Iterator iterator) {
		System.out.println("postFlush");
	
       }		

}
