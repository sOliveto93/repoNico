package common.dataSource.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
import java.util.*;

import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import common.model.domain.error.ErrorGrabacion;



public class GenericHibernateDAO<T extends Serializable, ID extends Serializable> implements GenericDAO<T, ID> {
	//protected Validador valido;
	private Class<T> persistentClass;
	protected String defaultOrder ;
	
	@SuppressWarnings("unchecked")
	public GenericHibernateDAO() {
		this.persistentClass = (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
	}
	
	/**
	 * El constructor recibe un string con el campo por el cual se debe ordenar por defecto en las busquedas listAll y findAll
	 * @param defaultOrder
	 */
	public GenericHibernateDAO(String defaultOrder) {
		this();
		this.defaultOrder = defaultOrder;
	}
	
	protected Session getSession(){
		return HibernateUtil.getSession();
	}
	
	public void closeSession(){
		HibernateUtil.closeSession();
	}
	
	protected Class<T> getPersistentClass(){
		return this.persistentClass;
	}

	@SuppressWarnings("unchecked")
	protected T load(Serializable/*ID*/ id) {
		Session session = getSession();
		T auxi = null;
		
		try {
			auxi = (T) session.load( persistentClass , id);
			return auxi;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			ErrorGrabacion error = new ErrorGrabacion();
			error.setMSG("Error en la carga ."+ex.getMessage());
			throw error;
		}
		
	}
	
	
	public void refresh(T object) {
		Session session = getSession();
		
		try {
			session.refresh(object);
			
		} catch (HibernateException ex) {
			ex.printStackTrace();
			ErrorGrabacion error = new ErrorGrabacion();
			error.setMSG("Error al refrescar el objeto ."+ex.getMessage());
			throw error;
		}
		
	}
	
	public T find(ID id){
		T res = null ;
		
		if(id != null)
			res = this.load(id);
		
		return res ;
	}
	
	@Deprecated 
	/**
	 * NO FUNCIONA BIEN ! REVISAR !
	 * Intenta convertir el string al tipo de dato del ID usado con Generics para realizar la busqueda. 
	 * Pero no lo convierte y la busqueda retorna mal.
	 * */
	public T find(String id){
		
		ID otroId = (ID)id ;
		return this.find(otroId);
	}
	
	/**
	 * Dado un array de ids string retorna un Set con todos los objetos de esos ids
	 * @param ids
	 * @return
	 */
	public Set<T> find(String[] ids)  {
		// TODO Se puede mejorar la consulta.    	
    	Set<T> set = new HashSet<T> ();
    	for(int i = 0; i<ids.length;i++){
    		set.add(findByInteger(ids[i]));
		}
        return set;
    }
	
	
	/**
	 * Hace un find convirtiendo el string recibido como parametro a Integer.
	 * En caso de no poder convertir el string arrojara una IllegalStateException
	 * @param id
	 * @return
	 */
	public T findByInteger(String id){
		return this.findByInteger(id, false);		
	}
	
	/**
	 * Hace un find convirtiendo el string recibido como parametro a Long.
	 * En caso de no poder convertir el string arrojara una IllegalStateException
	 * @param id
	 * @return
	 */
	public T findByLong(String id){
		return this.findByLong(id, false);		
	}
	
	/**
	 * Hace un find convirtiendo el string recibido como parametro a Integer. 
	 * Si forceRefresh es true obliga a hacer un refresh del objeto en la session de hibernate.
	 * En caso de no poder convertir el string arrojara una IllegalStateException
	 * @param id
	 * @return
	 */
	public T findByInteger(String id, boolean forceRefresh){
		T res = null;
		
		if(id == null || id.equals(""))
			return res;
		
		try{
			Integer idInt = Integer.parseInt(id);
			res = this.load(idInt);
			
			if(forceRefresh)
				getSession().refresh(res);
		}catch(Exception e){
			throw new IllegalStateException("Hubo un error al convertir el Integer en el find.");
		}
			
		return res ;

	}
	
	/**
	 * Hace un find convirtiendo el string recibido como parametro a Long. 
	 * Si forceRefresh es true obliga a hacer un refresh del objeto en la session de hibernate.
	 * En caso de no poder convertir el string arrojara una IllegalStateException
	 * @param id
	 * @return
	 */
	public T findByLong(String id, boolean forceRefresh){
		T res = null;
		
		if(id == null || id.equals(""))
			return res;
		
		try{
			Long idLong = Long.parseLong(id);
			res = this.load(idLong);
			
			if(forceRefresh)
				getSession().refresh(res);
		}catch(Exception e){
			throw new IllegalStateException("Hubo un error al convertir el Long en el find.");
		}
			
		return res ;

	}

	public void save(T obj)throws ErrorGrabacion{
		save(obj, true);
	}
	
	public void save(List<T> lista)throws ErrorGrabacion{
		save(lista.iterator(),true);		
	}
	
	/**
	 * Graba todos los elementos de un iterador sin hacer commit
	 * @param iterador
	 * @throws ErrorGrabacion
	 */
	public void save(Iterator<T> iterador)throws ErrorGrabacion{
		save(iterador,true);
	}
	
	/**
	 * Graba todos los elementos de un iterador con la posibilidad de hacer o no commit al final
	 * @param iterador
	 * @throws ErrorGrabacion
	 */
	public void save(Iterator<T> iterador, boolean commitAfter)throws ErrorGrabacion{
		HibernateUtil.beginTransaction();
		Session session = getSession();
		
		while(iterador != null && iterador.hasNext()){
			T elemento = iterador.next();
			session.save(elemento);		
		}
		
		if(commitAfter)
			HibernateUtil.commitTransaction();
	}
	
	public void save(T obj, boolean commitAfter)throws ErrorGrabacion{		
		try {
			HibernateUtil.beginTransaction();
			Session session = getSession();
			session.save(obj);			
		} catch (HibernateException ex) {
			HibernateUtil.rollbackTransaction();
			ErrorGrabacion error = new ErrorGrabacion();
			ex.printStackTrace();
			error.setMSG("Error en la grabacion ."+ex.getMessage());
			throw error;
		} finally{
			if(commitAfter)
				HibernateUtil.commitTransaction();
			
		}
	}
	
	public void saveOrUpdate(T obj)throws ErrorGrabacion{
		saveOrUpdate(obj, true);
	}
	
	public void saveOrUpdate(List<T> obj)throws ErrorGrabacion{
		HibernateUtil.beginTransaction();
		Session session = getSession();
		
		int tope = obj.size() ; 
		// Graba hasta el ultimo sin hacer commit
		for(int i = 0; i < tope;i++)
			session.saveOrUpdate(obj.get(i));
		
		// Recien hace el commit al final
		HibernateUtil.commitTransaction();
	}

	public void saveOrUpdate(Iterator<T> iterador)throws ErrorGrabacion{
		save(iterador,true);
	}
	/**
	 * Graba todos los elementos de un iterador con la posibilidad de hacer o no commit al final
	 * @param iterador
	 * @throws ErrorGrabacion
	 */
	public void saveOrUpdate(Iterator<T> iterador, boolean commitAfter)throws ErrorGrabacion{
		HibernateUtil.beginTransaction();
		Session session = getSession();
		
		while(iterador != null && iterador.hasNext()){
			T elemento = iterador.next();
			session.saveOrUpdate(elemento);		
		}
		
		if(commitAfter)
			HibernateUtil.commitTransaction();
	}
	
	public void saveOrUpdate(T obj, boolean commitAfter)throws ErrorGrabacion {
		try {
			HibernateUtil.beginTransaction();
			Session session = getSession();
			session.saveOrUpdate(obj);
		} catch (HibernateException ex) {
			HibernateUtil.rollbackTransaction();
			ErrorGrabacion error = new ErrorGrabacion();
			error.setMSG("Error en la grabacion/actualizacion ."+ ex.getMessage());
			ex.printStackTrace();
			throw error;
		} finally{
			if(commitAfter)
				HibernateUtil.commitTransaction();
			
		}
	}

	public void delete(T obj)throws ErrorGrabacion{
		delete(obj, true);
	}
	
	public void delete(T obj, boolean commitAfter) {
		try {
			HibernateUtil.beginTransaction();
			Session session = getSession();
			session.delete(obj);			
		} catch (HibernateException ex) {
			HibernateUtil.rollbackTransaction();
			ex.printStackTrace();
			ErrorGrabacion error = new ErrorGrabacion();
			error.setMSG("Error en el borrado ."+ex.getMessage());
			throw error;			
		} finally{
			if(commitAfter)
				HibernateUtil.commitTransaction();			
		}
	}

	/**
	 * Permite a un objecto con configuracion lazy=false ser inicializado en todos sus valores
	 * @param objeto
	 */
	public void inicializar(T objeto){
		Hibernate.initialize(objeto);
	}
	
	/**
	 * Retorna un iterador de todos los objetos de la clase ordenados segun el valor indicado en defaultOrder.
	 * @see List<T> listAll()
	 */
	public Iterator<T> findAll() {
		return this.listAll().iterator();
	}
	
	
	/**
	 * Retorna una lista de todos los objetos de la clase ordenados segun el valor indicado en defaultOrder.
	 */
	@SuppressWarnings("unchecked")
	public List<T> listAll() {
		List<T> objects = null;
		try {
			Session session = getSession();
			
			String orden = "";
			if(getDefaultOrder() != null && getDefaultOrder().length() > 0)
				orden = " order by "+getDefaultOrder();
			//if()	
		 	
			Query query = session.createQuery("from " + persistentClass.getName() + " " + orden);
			objects = query.list();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			ErrorGrabacion error = new ErrorGrabacion();
			error.setMSG("Error en el listado de objectos ."+ex.getMessage());
			throw error;		
		}
		return objects;
	}
	
	protected Query getQuery(String consulta){
		Session session = getSession();
		return session.getNamedQuery(consulta);
	}
	
	/**
     * Dado el nombre de una consulta y un mapa de los parametros <String, Object> se retorna si existe la unica instancia del objeto
     * @param queryName
     * @param queryParams
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<T> listByNamedQuery(String queryName, Map<String, Object> queryParams) {
        Query query = getQuery(queryName);
        query.setProperties(queryParams);
        return query.list();
    }
    
    /**
     * Dado el nombre de una consulta y un mapa de los parametros <String, Object> se retorna si existe la unica instancia del objeto
     * @param queryName
     * @param queryParams
     * @return
     */
    @SuppressWarnings("unchecked")
    public T findByNamedQuery(String queryName, Map<String, Object> queryParams) {
        Query query = getQuery(queryName);
        query.setProperties(queryParams);
        return (T)query.uniqueResult();
    }
	

	/**
	 * Retorna una lista de todos los objetos que matcheen en su propiedad "nombre" con el parametro recibido.
	 * El orden de los resultados es por nombre en orden descendente.
	 * @param nombre
	 * @return
	 */
	public List<T> buscarPorNombre(String nombre){		
		return this.buscarPorCampo("nombre", nombre, true);
	}
	
	/**
	 * Idem buscarPorNombre(String nombre) , pero solo retorna la primer aparicion
	 * @param nombre
	 * @see buscarPorNombre(String nombre)
	 * @return
	 */
	public T buscarUnicoPorNombre(String nombre){		
		 T resultado = null;
		 
		 Iterator<T> nombres = this.buscarPorNombre(nombre).iterator();
		 
		 if(nombres.hasNext())
		  resultado = nombres.next();
		 
		 return resultado; 
	}
	
	public List<T> buscarPorNombreY(String nombre, Criterion otroCriterio){
		
		Criteria criterio = getSession().createCriteria(persistentClass)		
		.add( Restrictions.like("nombre", "%"+nombre+"%").ignoreCase() );
		
		if(otroCriterio != null)
			criterio.add(otroCriterio);
		
		criterio.addOrder( Property.forName("nombre").desc() );
		
		@SuppressWarnings("unchecked")
	    List<T> lista= (List<T>)criterio.list();
		
		return lista;		

	}
	/**
	 * Permite buscar un determinado 'valor' el un 'campo' indicado del objeto. El query usa EQUALS.
	 * @param campo
	 * @param valor
	 * @return
	 */
	public List<T> buscarPorCampo(String campo, String valor){
		return this.buscarPorCampo(campo, valor,false);
	}
	public T buscarUnicoPorCampo(String campo, String valor){
		 T resultado = null;
		 
		 Iterator<T> obj = this.buscarPorCampo(campo, valor,false).iterator();
		 
		 if(obj.hasNext())
		  resultado = obj.next();
		 
		 return resultado; 
	}

	
	/**
	 * Permite buscar un determinado 'valor' el un 'campo' indicado del objeto.
	 * Si la variable 'like' es true se hara la busqueda por like, en caso contrario por equals.
	 * @param campo
	 * @param valor
	 * @param like
	 * @return
	 */
	public List<T> buscarPorCampo(String campo, String valor,String campoDeOrdenamiento, boolean like){
		Criteria criterio = getSession().createCriteria(persistentClass);
	    if(like)
	        criterio.add( Restrictions.like(campo, "%"+valor+"%").ignoreCase() );
		else{
			criterio.add(((SimpleExpression) Restrictions.eq(campo, valor)).ignoreCase() );
		}
	    if(campoDeOrdenamiento != null)
	    	criterio.addOrder( Property.forName(campoDeOrdenamiento).asc() );
	    
		@SuppressWarnings("unchecked")
	    List<T> lista= (List<T>)criterio.list();
	    return lista;
	}
	
	
	
	public List<T> buscarPorCampo(String campo, String valor, boolean like){
		return buscarPorCampo(campo, valor,null, like);
	}
	
	/**
	 * Retorna el primero de la lista que concuerde con el valor recibido como parametro
	 * @param nombre
	 * @return
	 */
	public T buscarPrimeroPorNombre(String nombre) {
		
	    Iterator<T> localidades = (Iterator<T>)buscarPorNombre(nombre).iterator();
		
		T res = null;
		
		if(localidades.hasNext())
			res = localidades.next() ;
		
		return res ;
	}
	
	/**
	 * Devuelve una lista de todos los objetos de la clase en cuestion, ordenados por el parametro recibido.
	 * @param order, asc
	 * @return
	 */
	public List<T> listAll(String orderBy, Boolean asc){
		
		Criteria criterio = getSession().createCriteria(persistentClass);
		
		if(!asc)
			criterio.addOrder( Property.forName(orderBy).desc() );
		else
			criterio.addOrder( Property.forName(orderBy).asc() );
		
		@SuppressWarnings("unchecked")
	    List<T> lista= (List<T>)criterio.list();
		
		return lista;		

	}
	/**
	 * Devuelve una lista de X cantidad de objetos de la clase en cuestion.
	 * @param limit Cantidad de registros que retornara.
	 * @return 
	 */
	public List<T> listAll(Integer limit){
		
		Criteria criterio = getSession().createCriteria(persistentClass);
		
		criterio.setMaxResults(limit);
		@SuppressWarnings("unchecked")
	    List<T> lista= (List<T>)criterio.list();
		
		return lista;		

	}
	
	public String getDefaultOrder(){
		return this.defaultOrder;
	}
	

	public void updateSessionOnly(T obj){
		try {
			Session session = getSession();
			session.saveOrUpdate(obj);
		} catch (HibernateException ex) {			
			ErrorGrabacion error = new ErrorGrabacion();
			error.setMSG("Error en la actualizacion de session ."+ ex.getMessage());
			ex.printStackTrace();
			throw error;
		}
	}

	
	public Boolean exists(ID id) {
		T obj = find(id);
		return (obj != null);
	}
	
	public Boolean existsByLong(String id) {
		T obj = findByLong(id);
		return (obj != null);
	}
	
	public Boolean existsByInteger(String id) {
		T obj = findByInteger(id);
		return (obj != null);
	}
	
	/* Agregado por Bety, para intentar hacer lo que figura en la siguiente pagina: http://docs.jboss.org/hibernate/orm/3.3/reference/en/html/batch.html*/
	public void batch_save(T obj, boolean flushAfter)throws ErrorGrabacion{
		Session session = getSession();
		try {		
			session.save(obj);			
		} catch (HibernateException ex) {
			HibernateUtil.rollbackTransaction();
			ErrorGrabacion error = new ErrorGrabacion();
			ex.printStackTrace();
			error.setMSG("Error en la grabacion ."+ex.getMessage());
			throw error;
		} finally{
			if(flushAfter){
				session.flush();
				session.clear();
			}			
		}
	}
	
	public void abrirTransaccion(){
		HibernateUtil.beginTransaction();
	}
	public void cerrarTransaccion(){
		HibernateUtil.commitTransaction();
	}
	/* agregado por Betiana, permite borrar todos los elementos de la tabla. Cuidado!!!!!*/
	public void deleteAll(){
		try {
			HibernateUtil.beginTransaction();
			Session session = getSession();
			String hqlDelete = "delete "+persistentClass.getName()+" c";	
			int deletedEntities = session.createQuery( hqlDelete )	        	
	        	.executeUpdate();
		} catch (HibernateException ex) {
			HibernateUtil.rollbackTransaction();
			ex.printStackTrace();
			ErrorGrabacion error = new ErrorGrabacion();
			error.setMSG("Error en el borrado ."+ex.getMessage());
			throw error;			
		} finally{
				HibernateUtil.commitTransaction();			
		}
	}

	public boolean esNuloOVacio(String parametro){		
		return( parametro == null || parametro.equals("") );				
	}
	
	public boolean contieneNuloOVacio(String[] parametros){
		
		boolean resultado = false;		
		for(int i=0; i< parametros.length; i++){			
			if(esNuloOVacio(parametros[i])){
				resultado = true;
				break;
			}
		}
		
		return resultado;
	}

}

 