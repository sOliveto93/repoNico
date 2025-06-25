package common.model.domain.util;

/**
 * @author Betiana Pade
 * Sistemas - UNLa
 * 
 */
public class Validador {
	
	public static Boolean isInteger(String aux){		
		int i = 0;
		if (aux.length()==0) return false;		
		while (aux.length()>i){
			if (aux.charAt(i)<47||aux.charAt(i)>58) return false;
			i++;
		}		
		return true;
	}
	
	public static Boolean isDouble(String a){
		if (a.length()==0) return false;		
		int i = 0;		
		boolean salir  = false;
		while(a.length()>i && !salir){
			if (a.charAt(i)=='.') salir=true;
			else{
				if (a.charAt(i)<48||a.charAt(i)>57) return false;
			}
			i++;		
		}		
		while(a.length()>i){		
			if (a.charAt(i)<48||a.charAt(i)>57) return false;
			i++;
		}		
		return true;
	}

	public static String intToStr(int i){
		String aux = "";
		int auxi = 0;
		while (i>0){
			auxi = i % 10;
			i = i / 10;
			aux = (char)(auxi+48)+aux;			
		}
		return aux;
	}
	
	/*
	public static Integer strToInt(String a){		
		Integer in = new Integer(a);
		return in;
	}
	public static long strToLong(String a){
		Long lo = new Long (a);
		return lo.longValue();
	}
	public static double strToDouble(String a){
		Double aux = new Double(a);		
		return (aux.doubleValue());
	}
	 */

	
	

		
}