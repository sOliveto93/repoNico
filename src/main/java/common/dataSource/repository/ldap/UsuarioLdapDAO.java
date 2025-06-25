package common.dataSource.repository.ldap;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.apache.log4j.Logger;
import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import common.dataSource.repository.UsuarioDAO;
import common.model.domain.error.ErrorGrabacion;
import common.model.domain.error.ErrorParametro;
import common.model.domain.validacion.UsuarioGenerico;


public class UsuarioLdapDAO<T extends UsuarioGenerico> implements UsuarioDAO<T>{
	
	private LdapConfig ldap;
	private DirContext ctx;
	private Boolean reintentarConexion;
	private MappingStrategy<T> mappingStrategy;
	static Logger logger = Logger.getLogger(UsuarioLdapDAO.class);
	
	public  UsuarioLdapDAO(){
		super();
		this.ldap = LdapConfig.getInstance("WEB-INF/ldap.cfg.xml");		
	}
	
	public UsuarioLdapDAO(LdapConfig ldap){
		super();
		this.ldap = ldap;
	}
	
	/**
	 * Setea el contexto y realiza la conexión LDAP.
	 * Arroja RuntimeException en caso de no poder realizar la conexion.
	 */
	public void conectar(String user, String pass){		 
		// Seteo en ambiente para la creacion del contexto
		Hashtable<String, String> env = new Hashtable<String, String>();		
		env.put(Context.INITIAL_CONTEXT_FACTORY,ldap.getContextFactory());
		env.put(Context.PROVIDER_URL, ldap.getUrl());
			
		// - Si quiero hacer el BIND (iniciar sesion) como admin
		env.put(Context.SECURITY_AUTHENTICATION,ldap.getSecurityAuth());		
		env.put(Context.SECURITY_PRINCIPAL, user);
		env.put(Context.SECURITY_CREDENTIALS, pass);	
		// Specify timeout to be 5 seconds
		env.put("com.sun.jndi.ldap.connect.timeout", "5000");
		
		try {
			ctx = new InitialDirContext(env);
		} catch (NamingException e) {
			
			if(e.getMessage().equals("[LDAP: error code 49 - Invalid Credentials]")){
				logger.info("Hubo un error al validar al usuario "+user+ ", las credenciales son invalidas.");
				ctx = null ;
			}else{
				logger.error(e.getMessage());
				throw new RuntimeException("Imposible contactar/conectarse con el servidor LDAP "+ldap.getUrl());
			}
		}		
	}
	
	public void conectar(){
		if(!ldap.getSecurityUser().equals("") && !ldap.getSecurityPassword().equals(""))
			this.conectar(ldap.getSecurityUser(),ldap.getSecurityPassword());
		else{
			logger.info("No se definio usuario y contraseña de administrador LDAP.");
			this.conectar("","");			
		}
			
	}
	/**
	 * Temina la conexión al servidor LDAP.
	 */	
	public void desconectar(){
		this.ctx = null;  
	}
	

	@SuppressWarnings("unused")
	private Boolean esPasswordValido(byte[] bytePassword, String passRecibido){
		
		if(bytePassword == null || passRecibido == null) return false;
		String userPassword="";
		try {
				// El password viene como un array de bytes, lo convierto a String
        	   userPassword = new String(bytePassword, "US-ASCII");
        	   userPassword = userPassword.substring(userPassword.indexOf(':')+1);	        	   
		} catch (UnsupportedEncodingException e1) {
        	   e1.printStackTrace();
		}		
		
		// El valor almacenado en del tipo {algoritmo}hashDelPassword, Ej: {MD5}dR4ff2DghljC67
		
		//Primero obtengo el algoritmo para luego poder hashear con el mismo algoritmo el password recibido plano.
		String algoritmo = userPassword.substring(1).substring(0, userPassword.indexOf('}')-1);		
		String hashUserPassword="";
		try {
			hashUserPassword = this.getLDAPHashedPassword(passRecibido,algoritmo);
		} catch (NoSuchAlgorithmException e) {				
			e.printStackTrace();
		}
           
		return (hashUserPassword.equals(userPassword));
	}	
	
	
	/**
	 * Recibe el password en texto plano y el nombre del algoritmo para usar, Ej : SHA , MD5
	 * Retorna el texto hasheado, al inicio del string tiene entre llaves el algoritmo usado, Ej: {MD5}s345fs$%
	 * @param password
	 * @param algoritmo
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */

	private String getLDAPHashedPassword(String password, String algoritmo) throws NoSuchAlgorithmException {

		byte[] digest = MessageDigest.getInstance(algoritmo).digest(password.getBytes());
		String base64Encoded = Base64.getEncoder().encodeToString(digest);
		String hashUserPassword = "{" + algoritmo + "}" + base64Encoded;

		return hashUserPassword;
	}


	/**
	 * Usa por defecto el algoritmo SHA y captura la exception NoSuchAlgorithmException ya que es imposible que ocurra.
	 * @param password
	 * @return
	 //@see getLDAPHashedPassword
	 */
//TODO codigo modicado para compatibilidad con java 8 no existe mas el
	private String getLDAPHashedPassword(String password){
		String resultado = null;
		
		try {
			resultado = this.getLDAPHashedPassword(password, "SHA");
		} catch (NoSuchAlgorithmException e) {
			// NO PUEDE OCURRIR
			e.printStackTrace();
		}
		
		return resultado;
	}
//TODO no se puede hacer mas asi con un jdk distinto al de oracle.. revisar

//	private String getLDAPHashedPassword(String password, String algoritmo) throws NoSuchAlgorithmException{
//
//		String hashUserPassword;
//
//		hashUserPassword = "{"+algoritmo+"}"+new sun.misc.BASE64Encoder().encode(java.security.MessageDigest.getInstance(algoritmo).digest(password.getBytes()));
//
//		return hashUserPassword;
//	}
	
	private void verificarStrategy(){
		if(this.mappingStrategy==null) throw new RuntimeException("Debe definirse una estrategia de mapeo para los usuarios LDAP");
	}
	
	
	public T existeUsuario (String user, String pass, Boolean reconectar){
		verificarStrategy();
		if(ctx == null)this.conectar();
		this.reintentarConexion = reconectar ;
		
		T usuario = null;
		
		// Busco los objetos usando el filtro y los atributos.
		NamingEnumeration<SearchResult> answer;
		try {
			//answer = ctx.search(ldap.getContextName(), filter, ctls);
			answer = buscarUid(user);
			// Itero por los resultados - Es este caso deberia ser solo uno
			//while (answer.hasMore()) {
			SearchResult sr = (SearchResult)answer.next();
			Attributes attrs = sr.getAttributes();					
			this.desconectar();
			
			String userCn = attrs.get("cn").toString().split(": ")[1];
			String userDn = "cn="+userCn+","+ldap.getContextName();
			if(!userCn.equals("")){	
				
				this.conectar(userDn, pass);
			}
			           
			if(ctx != null ){				
				usuario = this.mappingStrategy.map(ldap, attrs);
				//logger.info("El usuario "+usuario.getNombreUsuario()+" se valido correctamente, tiene privilegios de "+usuario.getTipo());				
			}else{
				logger.info("El usuario "+user+" no se valido bien. DN: "+userDn);
				
			}			
			
		} catch (NamingException e1) {
			this.ctx = null; 
			if(reintentarConexion){
				logger.warn("Hubo un error al conectarse al LDAP, se reintentara la conexion.");
				existeUsuario (user, pass, false);
			}else{				
				throw new RuntimeException("Hubo un error al conectarse al LDAP, se reintentó la conexion pero fue inutil.");
			}
		} 
		
		return usuario;
	}
	
	
	private NamingEnumeration<SearchResult> buscarUid(String uid) throws NamingException{
		// Especifico el filtro de lo que quiero traer
		String filter = "(&(objectClass="+ldap.getFilterObject()+")(uid="+uid+"))";
		
		// Especifico que atributos quiero traer - Para evitar traerme todo, le coloco solo los usados por el sistema.		
		ArrayList <String> attribFiltro = new ArrayList<String>();
		attribFiltro.add(ldap.getAttributeName());
		attribFiltro.add(ldap.getAttributeMail());
		attribFiltro.add(ldap.getAttributeUserName());
		attribFiltro.add(ldap.getAttributePassword());
		
		// Agrego en forma dinamica los posibles atributos alternativos (variantes en cada sistema)
		
		Properties atribsAlternativos = ldap.getAtributosAlternativos();
		if(atribsAlternativos != null){
			Enumeration<Object> keys;
			keys = atribsAlternativos.keys();
			while(keys.hasMoreElements()){
				attribFiltro.add(atribsAlternativos.getProperty(keys.nextElement().toString()));
			}
		}
		
		String[] attrIDs = new String[attribFiltro.size()];
		
		attribFiltro.toArray(attrIDs);
		
		SearchControls ctls = new SearchControls();
		ctls.setReturningAttributes(attrIDs);
		ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		// Busco los objetos usando el filtro y los atributos.
		NamingEnumeration<SearchResult> answer= null;
		if(ctx == null ) this.conectar();
		answer = ctx.search(ldap.getContextName(), filter, ctls);
				
		return answer;
		
	}
	
	public T existeUsuario (String user, String pass){
		return existeUsuario(user, pass, true);
	}
	
	public void setMappingStrategy( MappingStrategy<T> strategy ){
		this.mappingStrategy = strategy;
	}

	public Iterator<T> listarUsuarios() {
		verificarStrategy();
		NamingEnumeration<SearchResult> answer = null;
		List<T> listaUsuarios = new ArrayList<T>();
		
		try {
			answer = this.buscarUid("*");
			
			while (answer.hasMore()) {
				SearchResult sr = (SearchResult)answer.next();								
				Attributes attrs = sr.getAttributes();								
				listaUsuarios.add(this.mappingStrategy.map(ldap, attrs));			
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listaUsuarios.iterator();
		
		
	}

	public T existeUsuario(String user) {
		verificarStrategy();
		if(ctx == null)this.conectar();
				
		T usuario = null;
		
		// Busco los objetos usando el filtro y los atributos.
		NamingEnumeration<SearchResult> answer;
		try {
			answer = buscarUid(user);
			SearchResult sr = (SearchResult)answer.next();
			Attributes attrs = sr.getAttributes();		
			usuario = this.mappingStrategy.map(ldap, attrs);					
			
		} catch (NamingException e1) {
			this.ctx = null; 
			throw new RuntimeException("Hubo un error al conectarse al LDAP, se reintentó la conexion pero fue inutil.");
			
		} 
		this.desconectar();
		return usuario;
	}

		
	
	/**
	 * Modifica en el Objeto usuario el password viejo por el nuevo
	 * @param usuario
	 * @param oldPass
	 * @param newPass
	 */
	public T cambiarPassword(T usuario, String oldPass, String newPass){
		
		// Me fijo que exista con ese password, ¿medio redundante?
		T otroUsuario = existeUsuario(usuario.getUsuario(),oldPass);
		
		if(otroUsuario!=null){
			otroUsuario.setPassword(this.getLDAPHashedPassword(newPass));
		}else{
			throw new ErrorParametro("No concuerda la contrase&ntilde;a ingresada con la actual.");
		}
		
		return otroUsuario;
	}


	public void saveOrUpdate(T usuario) throws ErrorGrabacion {
		// TODO Por ahora solo cambia password
		if(ctx == null)this.conectar();
		
		try{
			// Remplazo el atributo del passwrod
			ModificationItem[] modificaciones = new ModificationItem[1];  
			modificaciones[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,  
					new BasicAttribute("userPassword", usuario.getPassword()));  
			
			String uid="cn="+usuario.getApellido()+","+ldap.getContextName();
			System.out.println("NAME= "+uid);
			ctx.modifyAttributes(uid, modificaciones) ;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("No se pudo cambiar el password. ----------"+e.getMessage());
			e.printStackTrace();
		}
				
		this.desconectar();
		
	}

	public MappingStrategy<T> getMappingStrategy() {
		return mappingStrategy;
	}

	public List<T> buscarPorNombre(String nombre) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<T> listAll(String orderBy, Boolean asc) {
		// TODO Auto-generated method stub
		return null;
	}

	public T buscarPrimeroPorNombre(String nombre) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(T obj) {
		// TODO Auto-generated method stub
		
	}

	public Boolean exists(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public T find(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterator<T> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<T> listAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public void save(T obj) throws ErrorGrabacion {
		// TODO Auto-generated method stub
		
	}

	public T findByInteger(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public T findByLong(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T findByLong(String id, boolean refresh) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Iterator<T> iterador, boolean commitAfter)
			throws ErrorGrabacion {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(T obj, boolean commitAfter) throws ErrorGrabacion {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(T object) {
		// TODO Auto-generated method stub
		
	}

}
