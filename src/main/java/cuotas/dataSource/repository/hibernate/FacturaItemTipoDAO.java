package cuotas.dataSource.repository.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import common.dataSource.repository.GenericHibernateDAO;
import common.model.domain.datos.Estado;
import common.model.domain.fecha.Fecha;
import cuotas.model.domain.datos.Pago;
import cuotas.model.domain.datos.Persona;
import cuotas.model.domain.datos.UsuarioCuotas;
import cuotas.model.domain.factura.Factura;
import cuotas.model.domain.factura.FacturaItemTipo;
import cuotas.model.domain.factura.RubroCobro;

public class FacturaItemTipoDAO extends GenericHibernateDAO<FacturaItemTipo, Integer> {

	public List<FacturaItemTipo> listAllOrder(){
		Criteria criterio = getSession().createCriteria(FacturaItemTipo.class);
		criterio.addOrder(Property.forName("rubroCobro").asc());
		criterio.addOrder(Property.forName("codigo").asc());

		return (List<FacturaItemTipo>)criterio.list();
	}
	public FacturaItemTipo buscarPorCodigo(Integer codigo,RubroCobro rubro){
		Criteria criterio = getSession().createCriteria(FacturaItemTipo.class);
		criterio.add(Restrictions.eq("codigo", codigo))
				.add(Restrictions.eq("rubroCobro", rubro));
		
		return (FacturaItemTipo) criterio.uniqueResult();
	}

	public List<FacturaItemTipo> listAllbyUsr(UsuarioCuotas usuario){
		Criteria criterio = getSession().createCriteria(FacturaItemTipo.class);
		Estado estado = new Estado("Activo");
		
		System.out.println("Usuario tipo: " + usuario.getTipo() + " en listAllbyUsr");
		
		if(usuario.getTipo().equals("DEP") || usuario.getTipo().equals("DRIYC") || usuario.getTipo().equals("DRC_ADMIN") || usuario.getTipo().equals("DEP_ADMIN")){
		criterio.setFetchMode("rubroCobro", FetchMode.JOIN)
			.createAlias("rubroCobro", "rubro");
		}
		if(usuario.getTipo().equals("DEP") || usuario.getTipo().equals("DEP_ADMIN")){
			criterio.add(Restrictions.in("rubro.id",  new Integer[] { 2,8,9 }));
			System.out.println("criterio.add(Restrictions.in(rubro.id,  new Integer[] { 2,8,9 }));");
		}else if(usuario.getTipo().equals("DRIYC") || usuario.getTipo().equals("DRC_ADMIN")){
			criterio.add(Restrictions.in("rubro.id",  new Integer[] { 4,7 }));
			System.out.println("Restrictions.in(rubro.id,  new Integer[] { 4,7 })");
		}else if(usuario.getTipo().equals("consulta")){
			System.out.println("perfil de consulta");
			criterio.setFetchMode("grupos", FetchMode.JOIN)
					.setFetchMode("grupos.usuariosGrupo", FetchMode.JOIN)
					.createAlias("grupos.usuariosGrupo", "usuariosGrupo")
					.add(Restrictions.eq("usuariosGrupo.id", usuario.getId()));
			
		}
		criterio.add(Restrictions.eq("rubro.estado", estado));

		return (List<FacturaItemTipo>)criterio.list();
	}
	
	
	public List<FacturaItemTipo> listFacturasItemsTipo(Factura facturaBuscada,Persona personaBuscada,Fecha fechaDesdeBuscada,Fecha fechaHastaBuscada){
		Criteria criterio = getSession().createCriteria(FacturaItemTipo.class);

		if (facturaBuscada == null && personaBuscada == null && fechaDesdeBuscada == null && fechaHastaBuscada == null)
			return null;
		
		criterio.createAlias("cbrItems", "cbrItem")			
			.setFetchMode("cbrItem", FetchMode.JOIN)
			.setFetchMode("cbrItem.factura", FetchMode.JOIN)
		.createAlias("cbrItem.factura", "factura")
			.setFetchMode("factura.persona", FetchMode.JOIN);
		//.createAlias("factura.persona", "persona")
		if(personaBuscada!=null){
			criterio.add(Restrictions.eq("factura.persona.id", personaBuscada.getId()));
		}
		if(fechaDesdeBuscada !=null)
			criterio.add(Restrictions.ge("factura.fechaPago", fechaDesdeBuscada));
		if(fechaHastaBuscada !=null)
			criterio.add(Restrictions.le("factura.fechaPago", fechaHastaBuscada));
		if(facturaBuscada.getNro() != null)
			criterio.add(Restrictions.eq("factura.nro", facturaBuscada.getNro()));
		if(facturaBuscada.getEstado()!=null){
			if(facturaBuscada.getEstado().isActivado()== true){
				criterio.add(Restrictions.eq("factura.estado", facturaBuscada.getEstado()));
			}else{
				criterio.add(Restrictions.eq("factura.estado", facturaBuscada.getEstado()));
			}
		}
		return (List<FacturaItemTipo>)criterio.list();
	}
}
