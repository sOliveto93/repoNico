package common.dataSource.repository;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import common.model.domain.error.ErrorGrabacion;


public interface GenericDAO<T, ID extends Serializable> {

	public abstract T find(ID id);
	
	public abstract Boolean exists(ID id);

	public abstract void save(T obj) throws ErrorGrabacion;

	public abstract void saveOrUpdate(T obj) throws ErrorGrabacion;
	
	/**
	* Graba todos los elementos de un iterador con la posibilidad de hacer o no commit al final
	* @param iterador
	* @throws ErrorGrabacion
	*/
	public void save(Iterator<T> iterador, boolean commitAfter)throws ErrorGrabacion;
	
	public void save(T obj, boolean commitAfter)throws ErrorGrabacion;

	public abstract void delete(T obj);
	
	public void refresh(T object);

	public abstract Iterator<T> findAll();
	
	public T findByInteger(String id);
	
	public T findByLong(String id);
	
	public T findByLong(String id, boolean refresh);
	
	public abstract List<T> buscarPorNombre(String nombre);	
	
	public T buscarPrimeroPorNombre(String nombre);
	
	public abstract List<T> listAll(String orderBy, Boolean asc);
	
	//public abstract List<T> listAll(Integer limit);
	
	public abstract List<T> listAll();
	
	/*public abstract void batch_save(T obj, boolean flushAfter);
	
	public abstract void abrirTransaccion();
	
	public abstract void cerrarTransaccion();*/
}