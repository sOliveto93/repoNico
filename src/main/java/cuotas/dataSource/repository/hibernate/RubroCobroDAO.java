package cuotas.dataSource.repository.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import common.dataSource.repository.GenericHibernateDAO;
import common.model.domain.datos.Estado;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.Partida;
import cuotas.model.domain.datos.UsuarioCuotas;
import cuotas.model.domain.factura.FacturaItemTipo;
import cuotas.model.domain.factura.RubroCobro;

public class RubroCobroDAO extends GenericHibernateDAO<RubroCobro, Integer> {

	public List<RubroCobro> listAllOrder(){
		Criteria criterio = getSession().createCriteria(RubroCobro.class);
		criterio.addOrder(Property.forName("descripcion").asc());
		return (List<RubroCobro>)criterio.list();
	}
	
	public List<RubroCobro> listAllbyUsr(UsuarioCuotas usuario){
		Criteria criterio = getSession().createCriteria(RubroCobro.class);
		Integer[] idsCbrRubroDEP = { 2, 8,9 };
		Integer[] idsCbrRubroDRYC = { 4, 7 };
		System.out.println(usuario.getTipo().toLowerCase());
		if(usuario.getTipo().toLowerCase().equals("dep") || usuario.getTipo().equals("DEP_ADMIN")){
			criterio.add(Restrictions.in("id", idsCbrRubroDEP));	
			System.out.println("dep: "+idsCbrRubroDEP.length);
		}
		if(usuario.getTipo().toLowerCase().equals("driyc") || usuario.getTipo().equals("DRC_ADMIN")){
			criterio.add(Restrictions.in("id", idsCbrRubroDRYC));
			System.out.println("dryc: "+idsCbrRubroDRYC.length );
		}
		if(usuario.getTipo().toLowerCase().equals("consulta")){
			criterio.setFetchMode("facturaItemTipo", FetchMode.JOIN)
					.setFetchMode("facturaItemTipo.grupos", FetchMode.JOIN)
					.createAlias("facturaItemTipo.grupos", "grupos")
					.setFetchMode("grupos.usuariosGrupo", FetchMode.JOIN)
					.createAlias("grupos.usuariosGrupo", "usuariosGrupo")
					.add(Restrictions.eq("usuariosGrupo.id", usuario.getId()));
		}
				/*.setFetchMode("grupos.carrera", FetchMode.JOIN)
				.createAlias("grupos.carrera", "carrera")
				.add(Restrictions.eq("carrera.tipo", usuario.g etTipo().toLowerCase()));
				/*.setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("id"), "id"))).setResultTransformer(Transformers.aliasToBean(FacturaItemTipo.class));*/
				//.setProjection(Projections.distinct(Projections.property("facturaItemTipo.id")));;
		;	
		return (List<RubroCobro>)criterio.list();
	}
	
	public RubroCobro buscarPorCodigo(Integer rubro){
		System.out.println("Rubro buscado: " + rubro);
		Criteria criterio = getSession().createCriteria(RubroCobro.class);
		criterio.add(Restrictions.eq("codigo", rubro));
		//criterio.add(Restrictions.eq("id_cbr", rubro));
		Estado estado = new Estado("Activo");
		criterio.add(Restrictions.eq("estado", estado));
		return (RubroCobro)criterio.uniqueResult();
	}
	
	
}
