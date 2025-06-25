package cuotas.dataSource.repository.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hsqldb.lib.Iterator;

import common.dataSource.repository.GenericHibernateDAO;
import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.CarreraDep;

public class CarrerasDepDAO extends GenericHibernateDAO<CarreraDep, Integer> {
	
	public int buscarUltimoCodigo(){
		String resultado = "";		
		Criteria criterio = getSession().createCriteria(CarreraDep.class).setProjection(Projections.max("idDep") );				
	    List<Integer> lista= (List<Integer>)criterio.list();
	    int cont = lista.size();
	   if(cont>0) return lista.get(0);
	   else return 0;
	
	    
	
	}
	
	public String buscarCarrerasCodigo(String codigo){
		String resultado = "";
		if(!codigo.equals("")){
			Criteria criterio = getSession().createCriteria(CarreraDep.class);
			criterio.createAlias("carrera", "car")		
			.add(Restrictions.eq("car.codigo", Integer.parseInt(codigo)));	
			
		    List<CarreraDep> lista= (List<CarreraDep>)criterio.list();
		    int cont = lista.size();
		    
		    boolean primero = true;
		    for (int i = 0; i< cont; i++){
		    	
		    	if(primero) {
		    		resultado = resultado +lista.get(i).getIdDep();
		    		primero = false;
		    	}else{
		    		resultado = resultado +","+lista.get(i).getIdDep();
		    	}
		    }		    	
		   
		}
	    return resultado;
	}
}
