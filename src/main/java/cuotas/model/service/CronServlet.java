package cuotas.model.service;import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.dataSource.repository.jdbc.DEPMysql;
import cuotas.dataSource.repository.jdbc.GuaraniInformix;
import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.Pago;
import cuotas.model.domain.datos.Persona;

/**
 * Servlet implementation class CronServlet
 */
public class CronServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CronServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		System.out.println("entro en el proceso BACH");
		Pago pago = null;
		GuaraniInformix grado = new GuaraniInformix("grado","","");
		GuaraniInformix posgrado = new GuaraniInformix("posgrado","","");
		Persona personaActual = null;
		Carrera carreraActual = null;
		long idCarreraActual = 0L;
		long idPersonaActual = 0L;
		long idCarreraAnterior = 0L;
		long idPersonaAnterior = 0L;
		
		try {
			if(request.getParameter("operacion").equals("sancionar")){
				System.out.println("entro en la parte de sancionar a todos los que adeudan");
				System.out.println("busca todos los grupos que sancionan");
				Iterator<Grupo> grupos = FactoryDAO.grupoDAO.buscarPorCampo("ctrlPago", "S").iterator();
				System.out.println("Conecta a Grado");
				grado.conectar();
				System.out.println("Conecta a Posgrado");
				List<Persona> personas_lista = null;
				posgrado.conectar();
				while (grupos.hasNext()){
					personaActual = null;
					carreraActual = grupos.next().getCarrera();
					System.out.println("Comienza con la carrera: "+carreraActual.getNombreCarrera());										
					//busco a filtrar por mora con adeuda en on, para que traiga a los alumnos que adeuda de esa carrera.
					personas_lista = FactoryDAO.personaDAO.filtrarPorMora(null, carreraActual, null, null, null, null, "on",null);
					Iterator<Persona> personas = personas_lista.iterator();
					System.out.println("Busca a los que adeudan de la carrera: "+personas_lista.size());
					while (personas.hasNext()){					
						personaActual = personas.next();					
						if(carreraActual.getTipo().equals("grado")) {
							if(personaActual.getLegajoGrado()!=null){
								grado.marcarPago(personaActual.getLegajoGrado(), "NO", carreraActual.getCodigo()+"");
							}
						}
						if(carreraActual.getTipo().equals("posgrado")) {
							if(personaActual.getLegajoPosgrado()!=null){
								posgrado.marcarPago(personaActual.getLegajoPosgrado(), "NO", carreraActual.getCodigo()+"");
							}
						}
					}
				}
			}else if(request.getParameter("operacion").equals("actualizar")){//levantar la sancion
				System.out.println("Busco los pagos no procesados");
				Iterator<Pago> pagos = FactoryDAO.pagoDAO.buscarNoProcesados().iterator();
				System.out.println("Conecta a Grado");
				grado.conectar();
				System.out.println("Conecta a Posgrado");
				posgrado.conectar();
				Iterator<Pago>pagos1 =null;
				boolean seguir = true;
				while (pagos.hasNext()){
					pago = pagos.next();				
					idCarreraAnterior = idCarreraActual; 
					idPersonaAnterior = idPersonaActual;
					
					personaActual = pago.getPersona();			
					carreraActual = pago.getGrupo().getCarrera();
					
					idCarreraActual = carreraActual.getId();  
					idPersonaActual = personaActual.getId();
										
					if(idPersonaAnterior != idPersonaActual ||  idCarreraAnterior != idCarreraActual){
						System.out.println("La persona es "+personaActual.getApellido()+" en "+carreraActual.getNombreCarrera());
						if(pago.getGrupo().getCtrlPago().equals('S')){
							//Busco filtrar por mora con on, para que traiga si adeuda
							System.out.println("Busco si adeuda la persona en la carrera");
							List<Persona> personas = FactoryDAO.personaDAO.filtrarPorMora(personaActual, carreraActual, null, null, null, null, "on",null);
							//si es vacio, es porque no adeuda nada, por lo tanto levanto la sanci�n
							if(personas==null||personas.isEmpty()){
								System.out.println("Entro aca porque no adeuda, porque al buscar las deudas no trajo nada: "+personaActual.getDocumento().getNumero()+" carrera: "+carreraActual.getNombreCarrera());						
								if(carreraActual.getTipo().equals("grado")) {
									if(personaActual.getLegajoGrado()!=null){
										grado.marcarPago(personaActual.getLegajoGrado(), "SI", carreraActual.getCodigo()+"");
									}
								}
								if(carreraActual.getTipo().equals("posgrado")) {
									if(personaActual.getLegajoPosgrado()!=null){
										posgrado.marcarPago(personaActual.getLegajoPosgrado(), "SI", carreraActual.getCodigo()+"");
									}
								}
							}else{
								pagos1 = personaActual.getPagos().iterator();
								seguir = true;								
								while(pagos1.hasNext() && seguir){
									if(pagos1.next().getFechaPgo()==null){
										seguir = false;
									}
								}
								if(seguir){
									System.out.println("Entro aca porque no adeuda, porque listo todos sus pagos");						
									if(carreraActual.getTipo().equals("grado")) {
										if(personaActual.getLegajoGrado()!=null){
											grado.marcarPago(personaActual.getLegajoGrado(), "SI", carreraActual.getCodigo()+"");
										}
									}
									if(carreraActual.getTipo().equals("posgrado")) {
										if(personaActual.getLegajoPosgrado()!=null){
											posgrado.marcarPago(personaActual.getLegajoPosgrado(), "SI", carreraActual.getCodigo()+"");
										}
									}
								}
							}
							
						}
					}
					pago.setProcesado(true);								
				}
				FactoryDAO.pagoDAO.save(pagos);
			}else if(request.getParameter("operacion").equals("importarCarreraDEP")){
				int ultimo = FactoryDAO.carrerasDepDAO.buscarUltimoCodigo();
				DEPMysql dep = new DEPMysql();
				dep.cargarCarreras(ultimo);
				System.out.println("carreras cargadas");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {			
			grado.desconectar();
			posgrado.desconectar();
			System.out.println("Se desconecto de ambos guarani");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
