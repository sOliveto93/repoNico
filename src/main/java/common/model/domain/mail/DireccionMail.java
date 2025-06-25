package common.model.domain.mail;
import java.io.Serializable;
import java.util.regex.Pattern;

import common.model.domain.error.ErrorParametro;

public class DireccionMail implements Serializable {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 8391585634928512939L;
	private static final String EMAIL_PATTERN =  "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	  private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

	  private String email;
 	  
	  public DireccionMail(){}
	  
	  public DireccionMail(String email){
		  this(email,true);
	  }
	  
	  /**
	   * Contructor de clase wrapper del dato email
	   * 
	   * @param email : Direccion para construir el objeto
	   * @param validar : booleano para validar o no al momento de la creacion
	   */
	  public DireccionMail(String email, Boolean validar){
		//String e = email.trim().replaceAll(" ","") ;
		while(email.indexOf(" ")!= -1)
			email = email.replaceAll(" ","") ;
		String e = email.trim() ;
		if((validar && PATTERN.matcher(e).matches()) || (!validar)){
			this.email = e ;
		}else{			
			throw new ErrorParametro("Se intentó validar la direccion "+email+" y es invalida");
		}  
	  }
	  
	  public void setValor(String email){
		  this.email = email;
	  }
	  
	  public String getValor(){
		  return this.email;
	  }
	  
	  public void setEmail(String email){
		  this.email = email;
	  }
	  
	  public String getEmail(){
		  return this.email;
	  }
	  
	  //TODO getDominio , getUsuario
 	  public String getDominio(){
		  return this.email;
	  }
 	  public String getUsuario(){
		  return this.email;
	  }
 	  //----------
 	  
	  public boolean esValido(){ 
		  return PATTERN.matcher(this.email).matches(); 
	  }
	  
	  public String toString(){
		  return this.email;
	  }
	  
	  
	  /**
	   * Metodo estatico que valida la existencia de una '@' y luego de ella un '.'
	   * @param mail
	   * @return
	   */
		public static boolean mailValido(String mail){		
			for (int i=0;i<mail.length();i++){
				if(mail.charAt(i)=='@'){
					for (int j=i;j<mail.length();j++){
						if(mail.charAt(j)=='.'){
							return true;
						}
					}
				}			
			}		
			return false;
		}
}