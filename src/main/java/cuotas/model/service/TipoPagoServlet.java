package cuotas.model.service;

import common.model.service.MappingMethodServlet;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Servlet;

import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.model.domain.datos.Chequera;
import cuotas.model.domain.factura.TipoPago;

/**
 * Servlet implementation class TipoPagoServlet
 */
public class TipoPagoServlet extends MappingMethodServlet{
       
    /**
	 * 
	 */
	private static final long serialVersionUID = -5938980298486572281L;
	/**
     * @see MappingMethodServlet#MappingMethodServlet()
     */
    public TipoPagoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	public void listar(){
		List<TipoPago> tiposPago = FactoryDAO.tipoPagoDAO.listAll("id",true);
		Integer countTipoPago = FactoryDAO.tipoPagoDAO.countTipoPago();
		System.out.println("CountTipoPago:  " +countTipoPago);

		request.setAttribute("tiposPago", tiposPago);
		request.setAttribute("countTipoPago", countTipoPago);
		setSalto(CuotasServletUtils.SALTO_TIPO_PAGO);

	}
	public void alta(){
		TipoPago tipoPago = getTipoPagoActualizado();
		FactoryDAO.tipoPagoDAO.saveOrUpdate(tipoPago);
		listar();
	}
	public void borrar(){
		String id = request.getParameter("idTipoPago");
		TipoPago tipoPago = FactoryDAO.tipoPagoDAO.findByInteger(id);
		FactoryDAO.tipoPagoDAO.delete(tipoPago);
		listar();
	}
	public void modificar(){
		TipoPago tipoPago = getTipoPagoActualizado();
		FactoryDAO.tipoPagoDAO.saveOrUpdate(tipoPago);
		listar();
 	}
	public TipoPago getTipoPagoActualizado(){
		String idTipoPago = request.getParameter("idTipoPago");
		String peso = request.getParameter("peso");
		String descripcion = request.getParameter("descripcion");
		TipoPago tipoPago = FactoryDAO.tipoPagoDAO.findByInteger(idTipoPago);
		System.out.println("descripcion:" + descripcion + " - Peso:" + peso + " -ID: " +idTipoPago);
		if(tipoPago==null){
			tipoPago = new TipoPago();
			if(!descripcion.equals("")){
				tipoPago.setDescripcion(descripcion);
			}
			String PesoOrdenamiento = String.valueOf( FactoryDAO.tipoPagoDAO.countTipoPago() + 1);
			tipoPago.setPesoOrdenamiento(PesoOrdenamiento);
		}else{
			System.out.println("descripcion:" + descripcion + " - " + peso);
			if(tipoPago.getDescripcion().equals("descripcion")){
				tipoPago.setDescripcion(descripcion);
			}
			if(!tipoPago.getPesoOrdenamiento().equals(peso)){
				System.out.println("Busca por peso");
				TipoPago tipoPagoAux = FactoryDAO.tipoPagoDAO.buscarPorPeso(peso);
				tipoPagoAux.setPesoOrdenamiento(tipoPago.getPesoOrdenamiento());
				FactoryDAO.tipoPagoDAO.saveOrUpdate(tipoPagoAux);
				tipoPago.setPesoOrdenamiento(peso);
			}
		}
		return tipoPago;
	}
}
