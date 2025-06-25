package cuotas.model.service;

import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.OrigenPago;
import cuotas.model.domain.datos.Pago;
import cuotas.model.domain.datos.Persona;
import cuotas.model.domain.datos.UsuarioCuotas;
import cuotas.model.domain.interfaces.RegistroPago;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import com.missiondata.fileupload.MonitoredDiskFileItemFactory;

import utilitarios.FileUploadListener;
import common.model.domain.fecha.Fecha;
import common.model.domain.validacion.UsuarioGenerico;
import common.model.service.AbmServlet;
import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.dataSource.repository.jdbc.DEPMysql;
import cuotas.dataSource.repository.jdbc.GuaraniInformix;
import cuotas.dataSource.repository.jdbc.IngresoPostgres;
import cuotas.excepcion.ErrorSincronizacion;
import cuotas.model.domain.interfaces.Guarani;

/**
 * Servlet implementation class InterfaceServlet
 */
public class InterfaceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String salto ="index.jsp";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InterfaceServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		HttpSession session = request.getSession();
		UsuarioCuotas usuario = (UsuarioCuotas) session.getAttribute("usuario");
		usuario = FactoryDAO.usuarioDAO.findByLong(usuario.getId().toString());
		
		System.out.println(	"UsuarioOriginal: " + usuario.getNombre() + " - " + usuario.getTipo() + " - " + usuario.getId());

		if(request.getParameter("operacion")!=null){
			System.out.println("La operacion es: "+request.getParameter("operacion"));
			if(request.getParameter("operacion").equals("importar_alumnos")){
				
				List<Grupo> grupos = null;
				List<Long> usuarioCodigoGrupos = new ArrayList<Long>();
				if ( (usuario.getTipo().equals("DEP_ADMIN"))|| (usuario.getTipo().equals("DRC_ADMIN"))) {
					Iterator<Grupo> gruposIt = usuario.getGrupos().iterator();
					grupos = new ArrayList<Grupo>();
					while (gruposIt.hasNext()) {
						Grupo GrupoAux = gruposIt.next();
						usuarioCodigoGrupos.add(GrupoAux.getCodigo());
					}
				}
				request.setAttribute("usuarioCodigoGrupos", usuarioCodigoGrupos);
				
				
				
				salto = "interfaces/importar_alumnos.jsp";
			}
			if(request.getParameter("operacion").equals("realizar_importacion")){
				String msg = "";
				if(request.getParameter("sistema").equals("preinscripcion")){
					IngresoPostgres ip = new IngresoPostgres(request.getParameter("carrera"), request.getParameter("anio"));
					int resultado = 0;
					try {					
						resultado = ip.cargarPreinscriptos();
												
					} catch (ClassNotFoundException e) {
						System.out.println("Error ");
						e.printStackTrace();
					} catch (SQLException e) {
						System.out.println("Error ");
						e.printStackTrace();
					} catch (ErrorSincronizacion e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					request.setAttribute("resultado", resultado);
					request.setAttribute("msg", msg);
					session.setAttribute("sistema", request.getParameter("sistema"));
					session.setAttribute("resultados",FactoryDAO.guaraniDAO.buscarNoSincronizados());
				}else if(request.getParameter("sistema").equals("cursos")){
					DEPMysql dep = new DEPMysql(); //request.getParameter("dep")
					int resultado = 0;
					try {						
						resultado = dep.cargarAuxiliar(FactoryDAO.carrerasDepDAO.buscarCarrerasCodigo(request.getParameter("carrera")), request.getParameter("anio"));							
					} catch (ClassNotFoundException e) {
						System.out.println("Error ");
						e.printStackTrace();
					} catch (SQLException e) {
						System.out.println("Error ");
						e.printStackTrace();
					}					
					request.setAttribute("resultado", resultado);
					request.setAttribute("msg", msg);
					session.setAttribute("sistema", request.getParameter("sistema"));
					session.setAttribute("resultados",FactoryDAO.guaraniDAO.buscarNoSincronizados());
				}else{
					
					GuaraniInformix guaraniInformix = new GuaraniInformix(request.getParameter("sistema"),request.getParameter("carrera"), request.getParameter("anio"));
					int resultado = 0;
					try {
						resultado = guaraniInformix.cargarAuxiliar();							
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (ErrorSincronizacion e) {
						msg = e.getMensage();
					}				
					request.setAttribute("resultado", resultado);
					request.setAttribute("msg", msg);
					session.setAttribute("sistema", request.getParameter("sistema"));
					session.setAttribute("resultados",FactoryDAO.guaraniDAO.buscarNoSincronizados());
				}	
				salto = "interfaces/confirmacion_importacion_alumnos.jsp";
			}
			if(request.getParameter("operacion").equals("confirmar_importacion")){		
				System.out.println("Entro en la confirmaci�n");
				ArrayList<String> lista = null;				
				if(request.getParameterValues("id_seleccionados")!=null){
					lista = new ArrayList<String>(Arrays.asList(request.getParameterValues("id_seleccionados")));					
				}
				Guarani guarani;
				System.out.println("Va a buscar los a procesar");
				Iterator<Guarani> aProcesar = FactoryDAO.guaraniDAO.buscarNoSincronizados().iterator();
				FactoryDAO.guaraniDAO.abrirTransaccion();
				while (aProcesar.hasNext()){					
					guarani = aProcesar.next();					
					boolean actualizar = false;
					if(lista!= null && lista.contains(guarani.getId()+"")){
						actualizar = true;					
					}
					guarani.setProcesado(true);
					guarani.setActualizar(actualizar);
					FactoryDAO.guaraniDAO.batch_save(guarani, false);	
					
					//FactoryDAO.guaraniDAO.confirmar_guarani(guarani.getId(), actualizar);
				}
				FactoryDAO.guaraniDAO.cerrarTransaccion();
				salto = "interfaces/confirmacion_importacion_alumnos.jsp";		
				session.setAttribute("sistema", request.getParameter("sistema"));
				session.setAttribute("resultados",FactoryDAO.guaraniDAO.buscarNoSincronizados());
			}
			if(request.getParameter("operacion").equals("pago_facil")){		
				System.out.println("llama a pago facil");
				salto = "interfaces/pago_facil.jsp";
			}
			if(request.getParameter("operacion").equals("upload")){
				try {					
					System.out.println("La fecha de pago facil es: "+new Fecha(request.getParameter("fecha")));
					//sube el archivo de pago facil que se adjunta
					FileUploadListener listener = new FileUploadListener(request.getContentLength());
					session.setAttribute("FILE_UPLOAD_STATS", listener.getFileUploadStats());
					FileItemFactory factory = new MonitoredDiskFileItemFactory(listener);
					ServletFileUpload upload = new ServletFileUpload(factory);
					     
					List items = upload.parseRequest(request);
					boolean hasError = false;
					
					Iterator<FileItem> itemsIt = items.iterator();
					
					FileItem fileItem = itemsIt.next();
					
					String miFichero = fileItem.getName();
					int i=0;
					int ultimaBarra = 0;
					while (i < miFichero.length()){
					       if (miFichero.substring(i,i+1).equals("\\")){
					             ultimaBarra=i;
					       }
					       i=i+1;
					}
					
					String nombreArchivo = miFichero.substring(ultimaBarra, miFichero.length());
					nombreArchivo = nombreArchivo.replace(" ", "");
					
					int j=0;
					i=0;
					while (i < nombreArchivo.length()){
					       if (nombreArchivo.charAt(i) == '.'){
					              j = i;
					        }
					       i++;
					}                          
					
					FileOutputStream miFicheroSt = new FileOutputStream(getServletContext().getRealPath("")+"\\Archivos"+nombreArchivo);
					miFicheroSt.write(fileItem.get());
					miFicheroSt.close();                           
					fileItem.delete();
					
					//Segunda parte leo ese archivo y lo pongo en la base de datos
					FactoryDAO.registroPagoDAO.deleteAll();
					InputStream inputStream  = new FileInputStream(getServletContext().getRealPath("")+"\\Archivos"+nombreArchivo); // take dbf file as program argument
					DBFReader reader = new DBFReader(inputStream); 
					
					int numberOfFields = reader.getFieldCount();
					for(i=0; i<numberOfFields; i++) {
					         DBFField field = reader.getField( i);
					}
					Fecha fechaMora = new Fecha(Fecha.getFechaActual());
					//Antes tenia 65 dias menos ahora tiene 60.
					fechaMora.sumarDias(-89);
					Object []rowObjects;
					RegistroPago rp = null;
					String dato ="";
					
					//Por cada registro en el archivo de pago facil, veo se es una que ya existe, o si es un pago nuevo.
					while( (rowObjects = reader.nextRecord()) != null) {
					
						rp = new RegistroPago();
				
					    rp.setearRegistroPago(rowObjects);
				
					    //Busca el pago por el numero de la persona y el grupo, y setea el pago	
					    System.out.println("Datos - Cliente: "+rp.getClienteString()+ ", codigo de barra: " + rp.getCod_barra());
					    Pago p = FactoryDAO.pagoDAO.filtrar(rp);//rp.getClienteString(), rp.getCod_barra());
                        if(p == null){
                        	 System.out.println("Entro ACA");
                        	//Persona per = FactoryDAO.personaDAO.find(rp.getNro_persona()); 
                        	 rp.setEstado("No existe");
                        	 rp.setFecha(new Fecha());
                        	 //rp.setTipo_doc(per.getDocumento().getTipo().getAbreviacion());
					         //rp.setNum_doc(per.getDocumento().getNumero());
					    }else{
					    	System.out.println("Entro ACA 222");
					         if(p.getMonPgo() == 0){
					        	 
					                p.setFechaPgo(rp.getFecha_pago());
					                System.out.println(new Fecha(request.getParameter("fecha")));
					                p.setFePgoCarga(new Fecha(request.getParameter("fecha")));
					                p.setMonPgo(rp.getImporte());
					                //----------------------------------------------------------------------------------------
					                //--------------------------------------ORIGEN DE PAGO------------------------------------
					                //----------------------------------------------------------------------------------------
					                //Pago Facil es id 2 en la tabla OrigenPago
					                OrigenPago origenPago = FactoryDAO.origenPagoDAO.findByInteger("3");
					                p.setOrigenPago(origenPago);
					                //COMENTARIO ------------------------------------	
					                //te dejo comentada una segunda opcion, lo ideal seria que ya desde el jsp salga con codigo 2 o el que le corresponda a pago facil
					                //aca buscarlo y poner el que corresponde.
					                /*Iterator<OrigenPago> origenesPago = FactoryDAO.origenPagoDAO.listAll().iterator();
					                while(origenesPago.hasNext()){
					                	OrigenPago origenPago = origenesPago.next();
					                	if(origenPago.getDescripcion().equals("pago facil")){
					                		p.setOrigenPago(origenPago);
					                	}
					                }*/
					                //----------------------------------------------------------------------------------------
					                //--------------------------FIN CAMBIO ORIGEN DE PAGO-------------------------------------
					                //----------------------------------------------------------------------------------------
					                /*if(p.getFecha().getFecha().after(fechaMora.getFecha())){
						                if(p.getGrupo().getCarrera().getTipo().equals("grado")){
						                	dato = dato + "grado,"+p.getPersona().getLegajoGrado()+","+p.getGrupo().getCarrera().getCodigo()+"|";
						                }
						                if(p.getGrupo().getCarrera().getTipo().equals("posgrado")){
						                	dato = dato + "posgrado,"+p.getPersona().getLegajoGrado()+","+p.getGrupo().getCarrera().getCodigo()+"|";						                	
						                }
					                }*/
					                p.setProcesado(false);
					                rp.setEstado("Pago");
					         }else{
					        	 rp.setEstado("Ya Pago");
					         }
					         	rp.setTipo_doc(p.getPersona().getDocumento().getTipo().getAbreviacion());
					            rp.setNum_doc(p.getPersona().getDocumento().getNumero());
					            rp.setNro_grupo(p.getGrupo().getCodigo());
					    }                                
					    if(p!=null){ 
					    	
					    	FactoryDAO.pagoDAO.saveOrUpdate(p);					    	
					    	rp.setFecha(p.getFecha());
					    }					    
					    
					    FactoryDAO.registroPagoDAO.save(rp);                                 
				 }
				 /*if(!dato.equals("")){
					 Persona p = new Persona();				 
					 p.sancionarExceptuarPersonas("Si", dato);					 
				 }
				 System.out.println("entro en Exceptura a "+dato);*/				 
				 inputStream.close();                           
				 Iterator<RegistroPago> pagos = FactoryDAO.registroPagoDAO.findAll();
				 session.setAttribute("pagos", pagos);
				 salto = "interfaces/pago_facil_resultado.jsp";

				} catch (FileUploadException e) {
					e.printStackTrace();
				} catch( DBFException e) {
					System.out.println( e.getMessage());
				} catch( IOException e) {
					System.out.println( e.getMessage());
				}
			}
		}
		RequestDispatcher dispacher = request.getRequestDispatcher(salto);
    	dispacher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
