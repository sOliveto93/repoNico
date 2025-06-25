package common.model.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.model.domain.util.Auditable;
import common.model.domain.util.Evento;
import common.model.domain.validacion.UsuarioGenerico;

public class MappingMethodServlet extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 6875336822895824620L;
	
	
	private String salto;
	private Boolean llamadaAsincronica;
	
	private String saltoPrevio ;
	private String operacionPrevia ;
	private boolean huboError ;
	
	protected HttpSession session;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected Map<String,String> mapeoOperacionMetodo = new HashMap<String,String>();
	
	public MappingMethodServlet(){
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String operacion = request.getParameter(this.getOperacionLabel());
		
		try{
		
			this.request = request;
			this.response = response;
			
			//this.salto = getSaltoDefault();			
			session = request.getSession();
			
			UsuarioGenerico usuario = (UsuarioGenerico)session.getAttribute(this.getUsuarioLabel());
			
			if(operacion!=null && usuario != null){
				
				if(operacionPrevia == null && !huboError){
					operacionPrevia = operacion ;					
					
					System.out.println("OPERACION : "+operacion);
				}/*else if(huboError){
					huboError = false;
					operacion = operacionPrevia ;
					llamadaAsincronica = false;
				}*/
				
				Class<?> c = this.getClass();

				String methodName = mapeoOperacionMetodo.get(operacion);
				if(esNuloOVacio(methodName)){
					methodName = operacion;
				}
				
				System.out.println("Clase ="+c.getName());
				System.out.println("Operacion ="+operacion);
				System.out.println("MethodName ="+methodName);
				Method method;
				try {
					method = c.getDeclaredMethod(methodName);
					method.invoke (this);
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}else if(usuario == null){
					//this.setLlamadaAsincronica(true);
					//response.sendError(response.SC_FORBIDDEN, "Primero ingrese al sistema");
					request.setAttribute("msg", "Primero ingrese al sistema");
					
			}
			
			// Si llegó hasta aca la operacion no tuvo problemas
			//operacionPrevia = operacion ;
			
			if(!llamadaAsincronica){
				// SI lo seteo con el "choclo" de JSON tiene problemas.
				saltoPrevio = salto;
				operacionPrevia = operacion ;
			}	
			
		}catch(RuntimeException re){
			UrlServlet urlServlet = new UrlServlet(request);
			
			String urlCompleta = urlServlet.getServletPath()+"?"+this.getOperacionLabel()+"="+operacionPrevia ;
			setSalto(urlCompleta);
			
			//getSession().setAttribute("msg", re);
			request.setAttribute("msg", re);
			System.out.println("URL: "+urlCompleta);
			re.printStackTrace();
			
			//operacionPrevia = null;
			huboError=true;
			/*response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.println(re);*/
			
		}
		
		
		if(huboError){
			huboError = false;
			operacion = operacionPrevia ;
			salto = saltoPrevio;
			llamadaAsincronica = false;
		}	
		
				
		if(!llamadaAsincronica){
			RequestDispatcher dispacher = request.getRequestDispatcher(salto);
    		dispacher.forward(request, response);
		}else{
			// Una vez que salteo el dispatcher lo reseteo
			llamadaAsincronica = false;
		}		
		
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
		
	}
	
	public String getSaltoDefault(){
		return "/index.jsp";
	}
	
	public void init() throws ServletException {		
		super.init();
		llamadaAsincronica = new Boolean(false);
		huboError = new Boolean(false);
		this.salto = getSaltoDefault();
		operacionPrevia = null;
	}
	
	public void destroy() {		
		super.destroy();		
	}
	

	
	public String getOperacionLabel(){
		return "operacion";
	}

	public String getSalto() {
		return salto;
	}

	public void setSalto(String salto) {
		this.salto = salto;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public Boolean getLlamadaAsincronica() {
		return llamadaAsincronica;
	}

	public void setLlamadaAsincronica(Boolean llamadaAsincronica) {
		this.llamadaAsincronica = llamadaAsincronica;
	}
	
	protected void enviarJson(HttpServletResponse response, String json){
		
		this.setLlamadaAsincronica(true);
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("application/json");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Boolean isLlamadaAsincronica(){
		return llamadaAsincronica;
	}
	
	public String getUsuarioLabel(){
		return new String("usuario");
	}
	
	public boolean esNuloOVacio(String parametro){
		
		return( parametro == null || parametro.equals("") );
				
	}
	
	public boolean contieneNuloOVacio(String[] parametros){
		
		boolean resultado = false;		
		for(int i=0; i< parametros.length; i++){			
			if(esNuloOVacio(parametros[i])){
				resultado = true;
				break;
			}
		}
		
		return resultado;
	} 
	
	protected Evento getEventoAuditoria(HttpServletRequest request, Auditable auditable){
		
		Evento evento = null;
		String operacion = request.getParameter(this.getOperacionLabel());
		UsuarioGenerico usuario = (UsuarioGenerico)session.getAttribute(this.getUsuarioLabel());
		
		// Obtengo el nombre de la clase		
		String nombreCompletoClase, nombreClase;		
		Class<?> enclosingClass = auditable.getClass().getEnclosingClass();

		if(enclosingClass != null)
			nombreCompletoClase = enclosingClass.getName();
		else
			nombreCompletoClase = auditable.getClass().getName();
		
		// Obtengo del path completo el que viene despues del ultimo punto
		// EJ nombreCompletoClase="cuotas.model.domain.datos.Carrera" y nombreClase="Carrera"
		//Pattern pattern = Pattern.compile(".*\\.(.*)");
		//Matcher matcher = pattern.matcher(nombreCompletoClase);
		int posicionGuionBajo = nombreCompletoClase.indexOf("_$$");
		
		if(posicionGuionBajo > 0)
			nombreCompletoClase = nombreCompletoClase.substring(0,posicionGuionBajo);
		
		System.out.println("nombreCompletoClase: "+ nombreCompletoClase );
		
		nombreClase = nombreCompletoClase.substring(nombreCompletoClase.lastIndexOf(".")+1);
		
		
		String descripcionEvento = operacion +" "+nombreClase+": " + auditable.getInformacionAuditoria();
		
		if(!esNuloOVacio(operacion)){
			evento = new Evento(usuario.getNombreCompleto(), descripcionEvento);
			//FactoryDAO.eventoDAO.save(evento,false);
		}
		return evento;
	}
}
