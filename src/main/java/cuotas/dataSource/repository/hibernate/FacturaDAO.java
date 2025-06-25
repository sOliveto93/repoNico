package cuotas.dataSource.repository.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import common.dataSource.repository.GenericHibernateDAO;
import common.model.domain.fecha.Fecha;
import cuotas.model.domain.datos.Persona;
import cuotas.model.domain.datos.UsuarioCuotas;
import cuotas.model.domain.factura.Factura;
import cuotas.model.domain.factura.FacturaItem;
import cuotas.model.domain.factura.FacturaItemTipo;
import cuotas.model.domain.factura.FormaPago;
import cuotas.model.domain.factura.RubroCobro;
import cuotas.model.domain.factura.TipoPago;

public class FacturaDAO extends GenericHibernateDAO<Factura, Integer> {
	
	public List<Factura> filtrar(Persona personaBuscada, Factura facturaBuscada,Fecha fechaDesde,Fecha fechaHasta, Integer maxResults,Integer firstResult,UsuarioCuotas usuario,TipoPago tipoPago, FormaPago formaPago, RubroCobro rubroCobro){
		Criteria criterio = getSession().createCriteria(Factura.class);
		if (facturaBuscada == null && personaBuscada == null && fechaDesde == null && fechaHasta == null)
				return null;

		if(fechaDesde !=null)
			criterio.add(Restrictions.ge("fechaPago", fechaDesde));
		if(fechaHasta !=null)
			criterio.add(Restrictions.le("fechaPago", fechaHasta));
		if(facturaBuscada.getNro() != null)
			criterio.add(Restrictions.eq("nro", facturaBuscada.getNro()));
		if(facturaBuscada.getEstado()!=null){
			if(facturaBuscada.getEstado().isActivado()== true){
				criterio.add(Restrictions.eq("estado", facturaBuscada.getEstado()));
			}else{
				criterio.add(Restrictions.eq("estado", facturaBuscada.getEstado()));
			}
		}
		/*
		if(facturaBuscada.getFechaPago() != null && !facturaBuscada.getFechaPago().equals(""))
			criterio.add(Restrictions.eq("fechaPago", facturaBuscada.getFechaPago()));
		*/
		if(personaBuscada != null){
			if(personaBuscada.getId()!=null){
				criterio.add(Restrictions.eq("persona", personaBuscada));
			}else if(personaBuscada.getDocumento() !=null){
				criterio.createAlias("persona","p")			
				.setFetchMode("p", FetchMode.JOIN)
				.add(Restrictions.eq("p.documento", personaBuscada.getDocumento()));
			}
		}
			
		
		if(maxResults != null && !maxResults.equals("")){
			criterio.setFirstResult(firstResult)
					.setMaxResults(maxResults);
		}
		//facturaBuscada.getFacturaItemsIterator().next().getFacturaItemTipo().getRubroCobro()
		//3. 7. 9. codigo de rubro.
		
		
		criterio.setFetchMode("formaPago", FetchMode.JOIN).createAlias("formaPago", "fp");
		if(formaPago!=null){
			criterio.add(Restrictions.eq("formaPago", formaPago));
		}
		if((usuario != null && usuario.getTipo().equals("DEP")) || (usuario != null && usuario.getTipo().equals("DRIYC"))|| (usuario != null && usuario.getTipo().equals("DRC_ADMIN"))|| (usuario != null && usuario.getTipo().equals("DEP_ADMIN"))){
				criterio.createAlias("FacturaItems", "FacturaItem")			
					.setFetchMode("FacturaItem", FetchMode.JOIN)
					.setFetchMode("FacturaItem.facturaItemTipo", FetchMode.JOIN)
				.createAlias("FacturaItem.facturaItemTipo", "facItemTipo")
					.setFetchMode("facItemTipo.rubroCobro", FetchMode.JOIN)
					.createAlias("facItemTipo.rubroCobro", "rubroCobro");
				
				if(usuario.getTipo().equals("DEP") || usuario.getTipo().equals("DEP_ADMIN") ){
					criterio.add(Restrictions.or(Restrictions.eq("rubroCobro.codigo", 9),( Restrictions.or(Restrictions.eq("rubroCobro.codigo", 2), Restrictions.eq("rubroCobro.codigo", 7)))));
				}else if(usuario.getTipo().equals("DRIYC") || usuario.getTipo().equals("DRC_ADMIN")){
					criterio.add(Restrictions.eq("rubroCobro.codigo", 3));
				}
					
		}else if((usuario != null && usuario.getTipo().equals("admin")) || (usuario != null && usuario.getTipo().equals("auditoria"))){
			if(rubroCobro != null){
				criterio.createAlias("FacturaItems", "FacturaItem")			
				.setFetchMode("FacturaItem", FetchMode.JOIN)
				.setFetchMode("FacturaItem.facturaItemTipo", FetchMode.JOIN)
					.createAlias("FacturaItem.facturaItemTipo", "facItemTipo")
				.setFetchMode("facItemTipo.rubroCobro", FetchMode.JOIN)
					.createAlias("facItemTipo.rubroCobro", "rubroCobro")
					.add(Restrictions.eq("rubroCobro.id", rubroCobro.getId()));
			}
		}
		if(tipoPago != null){
			criterio.add(Restrictions.eq("tipoPago", tipoPago));
		}
		criterio.addOrder(Property.forName("nro").asc());
		return (List<Factura>)criterio.list();
	}

	public Integer buscarUltimoNumeroFactura(TipoPago tipoPago){

    	Criteria criterio = getSession().createCriteria(Factura.class);
    	criterio.setProjection(Projections.max("nro"))
    			.add(Restrictions.eq("tipoPago", tipoPago));
    	return (Integer) criterio.uniqueResult();
	}
	public Factura buscarUltimaFactura(TipoPago tipoPago){
    	Criteria criterio = getSession().createCriteria(Factura.class);
    	Integer nroFactura = buscarUltimoNumeroFactura(tipoPago);
    	System.out.println("nroFactura:"+nroFactura );
    	criterio.add(Restrictions.eq("nro",nroFactura))
    			.add(Restrictions.eq("tipoPago", tipoPago));
    	return (Factura) criterio.uniqueResult();
	}
	public List<Factura> buscarDesdeHasta(Integer desdeNro, Integer hastaNro){
		Criteria criterio = getSession().createCriteria(Factura.class);
		criterio.add(Restrictions.ge("nro", desdeNro)).add(Restrictions.le("nro", hastaNro));
				//.between("nro", desdeNro, hastaNro));
		return (List<Factura>)criterio.list();
	}
	public Factura buscarPorNumero(Integer nro){
		Criteria criterio = getSession().createCriteria(Factura.class);
		criterio.add(Restrictions.eq("nro", nro));
				//.between("nro", desdeNro, hastaNro));
		return (Factura) criterio.uniqueResult();
	}
	
}
