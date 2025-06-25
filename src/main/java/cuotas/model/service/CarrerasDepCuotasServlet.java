package cuotas.model.service;

import java.util.List;

import common.model.service.MappingMethodServlet;
import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.CarreraDep;

public class CarrerasDepCuotasServlet extends MappingMethodServlet {
	private static final long serialVersionUID = -7882775457154163958L;
	
	public void listar(){
		List<CarreraDep> carrerasDep = FactoryDAO.carrerasDepDAO.listAll();
		List<Carrera> carreras = FactoryDAO.carreraDAO.listAll("codigo",true);
		
		request.setAttribute("carreras", carreras);
		request.setAttribute("carrerasDep", carrerasDep);
		setSalto(CuotasServletUtils.SALTO_CARRERAS_DEP_LISTAR);
	}
	
	public void vincularCarreras(){
		String idCarreraCuotas = request.getParameter("idCarreraCuotas");
		String id = request.getParameter("idDep");
		CarreraDep carreraDep = FactoryDAO.carrerasDepDAO.findByInteger(id);
		Carrera carrera = FactoryDAO.carreraDAO.findByLong(idCarreraCuotas);
		if(carreraDep !=null){
			carreraDep.setCarrera(carrera);
			FactoryDAO.carrerasDepDAO.saveOrUpdate(carreraDep);
		}
		listar();
	}
}
