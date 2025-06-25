package cuotas.dataSource.repository.hibernate;


import java.util.List;


import org.hibernate.Criteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import common.dataSource.repository.GenericHibernateDAO;

import cuotas.model.domain.interfaces.Persona_Auxiliar;
//
public class Persona_AuxiliarDAO extends GenericHibernateDAO<Persona_Auxiliar, Long> {

	public Persona_AuxiliarDAO(){
		super("documento");	
		//setMiInterceptor(new InterceptorCuotas("Carrera"));
	}
	/*
	public List<Persona_Auxiliar> buscarCarrerasCodigo(Integer codigo){
		Criteria criterio = getSession().createCriteria(Carrera.class);
		criterio.add(Restrictions.eq("codigo", codigo));
		criterio.addOrder( Property.forName("codigo").desc());
	    
	    List<Carrera> lista= (List<Carrera>)criterio.list();
	    return lista;
	}
	public Carrera buscarCarreraNombre(String nombre){
		Carrera resultado = super.buscarUnicoPorCampo("nombreCarrera", nombre);
		return resultado;
		
    }*/
    

}
