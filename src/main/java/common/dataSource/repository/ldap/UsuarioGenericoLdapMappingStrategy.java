package common.dataSource.repository.ldap;

import java.lang.reflect.ParameterizedType;

import javax.naming.directory.Attributes;

import common.dataSource.repository.ldap.LdapConfig;
import common.dataSource.repository.ldap.MappingStrategy;
import common.model.domain.mail.DireccionMail;
import common.model.domain.validacion.UsuarioGenerico;

/**
 * Clase generada para acotar los potenciales cambios necesarios con el fin de adaptar la 
 * validacion LDAP en todos los sistemas. 
 * Solo es necesario heredad de esta clase y redefir el metodo map agregando ademas de los atributos comunes los propios de cada sistema.
 * - La clave para el LDAP se gurda con el formato "{algoritmo}hAsHdElPassWoRd" , ej : {MD5}sdk&#$3lmKccm
 * @author Pablo
 *
 */
public class UsuarioGenericoLdapMappingStrategy<T extends UsuarioGenerico> implements MappingStrategy<T> {

	private Class<T> persistentClass;
	
	@SuppressWarnings("unchecked")
	public UsuarioGenericoLdapMappingStrategy(){
			this.persistentClass = (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	
	
	
	/**
	 * Mapea los atributos de la siguiente manera: 
	 * 
	 * uid -> Nombre de Usuario 
	 * cn -> Apellido
	 * cn -> Nombre
	 * mail -> Email
	 * 
	 */
	
	public T map(LdapConfig ldap, Attributes attrs) {
		
		String cn = obtenerValorAtributoLdap(attrs.get(ldap.getAttributeName()));		
		String mail = obtenerValorAtributoLdap(attrs.get(ldap.getAttributeMail()));
		
		String nombreUsuario=obtenerValorAtributoLdap(attrs.get(ldap.getAttributeUserName()));
		

		T usuario = null ;
		try {
			usuario = persistentClass.newInstance();
			
			usuario.setUsuario(nombreUsuario);
			usuario.setApellido(cn);
			usuario.setNombre(cn);
			usuario.setEmail(new DireccionMail(mail));
			
			/* Guardo la clave en el objeto Usuario de la forma "{algoritmo}hAsHdElPassWoRd" 
			EJ: {SHA}1Drk+RqAfD7M1R7hIb6rm/BySPU=
			 */
			String password = new String((byte [])attrs.get(ldap.getAttributePassword()).get(), "US-ASCII");
			usuario.setPassword(password);
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
		
		return usuario;
	}
	
	/**
	 * Desde el atributo obtiene el valor concreto en string.
	 * Ej1: "param : valor" --> retorna "valor"
	 * Ej2 "valor" --> retorna "valor"
	 * @param attribute
	 * @return
	 */
	protected String obtenerValorAtributoLdap(javax.naming.directory.Attribute attribute){		
		String res="";		
		res= attribute.toString();
		// SI no existe indexOf da -1 , sumandole 1 da 0, osea el string completo
		res = res.substring(res.indexOf(':')+1);
		res= res.trim();		
		return res;
	}
	
	

}
