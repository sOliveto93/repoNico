package cuotas.dataSource.repository.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import common.dataSource.repository.GenericHibernateDAO;
import common.model.domain.fecha.Fecha;
import cuotas.model.domain.datos.Persona;
import cuotas.model.domain.datos.UsuarioCuotas;
import cuotas.model.domain.factura.Factura;
import cuotas.model.domain.factura.FacturaItem;
import cuotas.model.domain.factura.FacturaItemTipo;
import cuotas.model.domain.factura.RubroCobro;
import cuotas.model.domain.factura.TipoPago;

public class FacturaItemDAO extends GenericHibernateDAO<FacturaItem, Integer> {
	public List<FacturaItem> filtrar(Persona personaBuscada, Factura facturaBuscada,Fecha fechaDesde,Fecha fechaHasta,FacturaItemTipo item, RubroCobro rubro, String orden,TipoPago tipoPago, UsuarioCuotas usuario){
		Boolean flagFacturaItemTipo=false;
		System.out.println("flagFacturaItemTipo: "+ flagFacturaItemTipo);
		Criteria criterio = getSession().createCriteria(FacturaItem.class,"item");	
		criterio.setFetchMode("item.factura", FetchMode.JOIN).createAlias("item.factura", "factura");
	//	criterio.setFetchMode("item.facturaItemTipo", FetchMode.JOIN).createAlias("item.facturaItemTipo", "itemTipo");
	//	criterio.createAlias("facturaItemTipo", "facItemTipo");
		if(orden.equals("item")){
			criterio.addOrder(Property.forName("item.facturaItemTipo").asc());		
		}else{
			System.out.println("entro aca para el reporte");
			criterio.createAlias("factura.formaPago", "fpago").addOrder(Property.forName("fpago.descripcion").asc());
			if(!flagFacturaItemTipo){
				criterio.setFetchMode("item.facturaItemTipo", FetchMode.JOIN)
				.createAlias("item.facturaItemTipo", "itemTipo");
				flagFacturaItemTipo = true;
				System.out.println("CAMBIO FLAG: 1 " + flagFacturaItemTipo);
			}
			criterio.createAlias("itemTipo.partida", "par").addOrder(Property.forName("par.descripcion").asc());
		}
		if(item != null){
			System.out.println("el item es"+item.getDescripcion());
			criterio.add(Restrictions.eq("item.facturaItemTipo", item));
		}
		if(rubro != null){
			System.out.println("el rubro es"+rubro.getDescripcion());
			if(!flagFacturaItemTipo){
				criterio.setFetchMode("item.facturaItemTipo", FetchMode.JOIN)
				.createAlias("item.facturaItemTipo", "itemTipo");
				flagFacturaItemTipo = true;
				System.out.println("CAMBIO FLAG: 2 " + flagFacturaItemTipo);
			}
			criterio.add(Restrictions.eq("itemTipo.rubroCobro", rubro));
		}else{
				if(flagFacturaItemTipo){
					criterio.setFetchMode("itemTipo.rubroCobro", FetchMode.JOIN)
					.createAlias("itemTipo.rubroCobro","rubroCobro");
					flagFacturaItemTipo = true;
					System.out.println("CAMBIO FLAG: 3 " + flagFacturaItemTipo);
				}else{
					criterio.setFetchMode("item.facturaItemTipo", FetchMode.JOIN)
					.createAlias("item.facturaItemTipo", "itemTipo")
					.setFetchMode("itemTipo.rubroCobro", FetchMode.JOIN)
					.createAlias("itemTipo.rubroCobro","rubroCobro");
					flagFacturaItemTipo = true;
					System.out.println("CAMBIO FLAG: 4 " + flagFacturaItemTipo);
				}
			System.out.println("flagFacturaItemTipo: "+ flagFacturaItemTipo);
			if(usuario.getTipo().equals("DEP")){
				criterio.add(Restrictions.or(Restrictions.eq("rubroCobro.codigo", 9),( Restrictions.or(Restrictions.eq("rubroCobro.codigo", 2), Restrictions.eq("rubroCobro.codigo", 7)))));
			}else if(usuario.getTipo().equals("DRIYC")){
				criterio.add(Restrictions.eq("rubroCobro.codigo", 3));
			}
		}
		
		if (facturaBuscada == null && personaBuscada == null && fechaDesde == null && fechaHasta == null){
			return null ;	
		}				

		if(fechaDesde !=null){
			criterio.add(Restrictions.ge("factura.fechaPago", fechaDesde));
		}
		if(fechaHasta !=null){
			criterio.add(Restrictions.le("factura.fechaPago", fechaHasta));
		}
		if(facturaBuscada.getNro() != null){
			criterio.add(Restrictions.eq("factura.nro", facturaBuscada.getNro()));
		}
		
		if(facturaBuscada.getEstado().isActivado()== true){
			criterio.add(Restrictions.eq("factura.estado", facturaBuscada.getEstado()));
		}else{
			criterio.add(Restrictions.eq("factura.estado", facturaBuscada.getEstado()));
		}
		
		if(facturaBuscada.getFormaPago()!=null){
			System.out.println("FormaPago: "+facturaBuscada.getFormaPago().getDescripcion());
			criterio.add(Restrictions.eq("factura.formaPago", facturaBuscada.getFormaPago()));
		}
		criterio.setFetchMode("factura.persona", FetchMode.JOIN).createAlias("factura.persona", "persona");
		if(personaBuscada != null){
			criterio.add(Restrictions.eq("factura.persona", personaBuscada));
		}
		if(tipoPago != null){
			criterio.add(Restrictions.eq("factura.tipoPago", tipoPago));
		}
		return (List<FacturaItem>) criterio.list(); 
	}
	public List<FacturaItem> filtrar(Factura factura){
		Criteria criterio = getSession().createCriteria(FacturaItem.class,"item");
		criterio.add(Restrictions.eq("factura", factura));
		return (List<FacturaItem>)criterio.list();
	}
	
}

