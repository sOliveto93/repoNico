package common.model.domain.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 implements Cloneable
{
  
	//TESTEO
private MessageDigest m;
  public MD5()
  {
	  try {
		m = MessageDigest.getInstance("MD5");
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
        
  }  
   
  public String getTransaccion(String entrada){	  	
	  m.update(entrada.getBytes(),0,entrada.length());
	  return new BigInteger(1,m.digest()).toString(16).toString() ;	
  }	  
}
