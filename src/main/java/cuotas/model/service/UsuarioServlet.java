package cuotas.model.service;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import common.dataSource.repository.ldap.LdapConfig;
import common.model.domain.util.MD5;
import common.model.domain.fecha.Fecha;
import common.model.domain.validacion.UsuarioGenerico;
import common.model.service.MappingMethodServlet;
import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.model.domain.datos.UsuarioCuotas;
import cuotas.model.domain.datos.UsuarioCuotasLdapMappingStrategy;

/**
 * Servlet implementation class ServletUsuario
 */
public class UsuarioServlet extends  HttpServlet {
	private static final long serialVersionUID = -474682602437482382L;
	private String salto ="./index.jsp";
	static Logger logger = Logger.getLogger(UsuarioServlet.class);
	HttpSession session;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		String operacion = request.getParameter("operacion");
		if(operacion != null){
			if(operacion.equals("login")){
				login(request, response);
			}else if(operacion.equals("logout")){
				logout(request, response);
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
	
	private void logout(HttpServletRequest request, HttpServletResponse response){
		session = request.getSession();
		salto = "/index.jsp";
		session.removeAttribute("usuario");
		session.setAttribute("msg", "Ya salio del Sistema");
		setSalto(salto);
	}
	
	private void login(HttpServletRequest request, HttpServletResponse response){
		System.out.println("login");
		session = request.getSession();
		String usu = request.getParameter("usuario");
		String pass =  request.getParameter("password"); 					
		String validacion = "mySql";//"ldap"; //request.getParameter("validacion");
		//Fecha hoy = new Fecha();
		
		UsuarioCuotas usuarioLocal = FactoryDAO.usuarioDAO.existeUsuario(usu);

		if(usuarioLocal != null){
			UsuarioCuotas usuario = null;
			if(validacion == null || validacion.equals("ldap")){
				FactoryDAO.usuarioLdapDAO.setMappingStrategy(new UsuarioCuotasLdapMappingStrategy());
				usuario = FactoryDAO.usuarioLdapDAO.existeUsuario(usu,pass);
				//usuario = FactoryDAO.usuarioLdapDAO.existeUsuario(usu);
				if(usuario != null){
					// Si valido correctamente contra el LDAP le seteo la contrase�a al usuario.
					// 	Esto permite mantener actualizado el password por si se cae el ldap, que pueda validar localmente.
					MD5 md5 = new MD5();							
					usuarioLocal.setPassword(md5.getTransaccion(pass));
					FactoryDAO.usuarioDAO.saveOrUpdate(usuarioLocal);
				}
			}else{
				usuarioLocal = FactoryDAO.usuarioDAO.existeUsuario(usu,pass);				
				if(usuarioLocal == null){
					salto = "/index.jsp";
				}else{
					System.out.println("usuarioTipo: "+ usuarioLocal.getTipo());
				}
			}
			if(usuarioLocal != null){ // Valido correctamente contra BD o LDAP
				session.setAttribute(CuotasServletUtils.USUARIO,usuarioLocal);
				logger.info("Usuario "+validacion+" login : "+usuarioLocal.getDatosPersonales()+ " - Desde IP : "+request.getRemoteAddr());
				session.removeAttribute("msg");
				//session.setAttribute("msg", "Usuario o Contrase�a incorrectos");
				salto = "/index.jsp";
			}else{ // FALLA VALIDACION
				logger.info("FAIL - Usuario login : "+usu+ " - Desde IP : "+request.getRemoteAddr());
				salto = "/index.jsp?msg=El usuario no es valido";
				usuarioLocal = null;
				session.setAttribute(CuotasServletUtils.USUARIO,usuario);
			}			
			setSalto(salto);
		}
	}
	// 02242
	public void setSalto(String salto) {
		this.salto = salto;
	}
	

	
}
