package common.model.domain.fecha;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class Fecha implements Serializable, Comparable<Fecha>{
   

	private static final long serialVersionUID = -902202182983454198L;	
	private Date fecha;
	private Calendar cal = new GregorianCalendar();
	public static final String  FORMATO_BARRAS = "dd/MM/yyyy";
	public static final String  FORMATO_GUIONES = "dd-MM-yyyy";

   
	/**
	 * Crea un objeto de Fecha inicializado en el momento actual
	 */
    public Fecha(){
        fecha = new Date();       
    }
    
    /**
     * Crea un objecto de fecha inicializado con la fecha indicada.
     * @param fecha
     */
    public Fecha(Date fecha){
        this.fecha = fecha;
    }
   
    /**
     * Crea un objecto fecha inicializado con el String dd-mm-yyyy. Idem new Fecha("dd-mm-yyyy",false)
     * @param a
     */
    public Fecha(String fecha) {
        this(fecha, false);      

    }
    
    /**
     * Crea un objecto fecha inicializado con el String dd-mm-yyyy si invertdo FALSE
     * Crea un objecto fecha inicializado con el String yyyy-mm-dd si invertdo TRUE
     * @param fecha 
     * @param invertido
     */
    public Fecha(String fecha, Boolean invertido ){
    	setFecha(fecha, invertido);
    }
    
    
	public static String getFechaActual(){
		Calendar toDay = Calendar.getInstance();
		int anio = toDay.get(Calendar.YEAR);
		int mes = toDay.get(Calendar.MONTH)+1;
		int dia = toDay.get(Calendar.DAY_OF_MONTH);
		return dia+"/"+mes+"/"+anio;
	} 
	
	public static String getHoraActual(){
		Calendar toDay = Calendar.getInstance();
		int hora = toDay.get(Calendar.HOUR_OF_DAY);
		int min = toDay.get(Calendar.MINUTE);
		int seg = toDay.get(Calendar.SECOND);
		
		String horario="";
		if(hora < 10) horario+="0";
		horario+= hora+":";
		if(min < 10) horario+="0";
		horario+=min+":";
		if(seg < 10) horario+="0";
		horario+=seg;
		
		return horario;
		
	}
    
    private Date convertirFecha(String fecha, Boolean invertido){
    	Date resultado = null;
        Calendar aux;
        String dia = "";
        String mes = "";
        String anio = "";
        aux = new GregorianCalendar();
        
        if (fecha == null || fecha.equals("")){
        	return resultado;
        }else{
            int i = 0;            
            while ((fecha.length()>i) && (fecha.charAt(i)!='-')&& (fecha.charAt(i)!='/')&& (fecha.charAt(i)!='.')){           
                dia = dia+fecha.charAt(i);
                i++;
            }
            i++;
            
            while ((fecha.length()>i) && (fecha.charAt(i)!='-')&& (fecha.charAt(i)!='/')&& (fecha.charAt(i)!='.')){           
                mes = mes+fecha.charAt(i);
                i++;
            }
            i++;
            
            while ((fecha.length()>i) && (fecha.charAt(i)!='-')&& (fecha.charAt(i)!='/')&& (fecha.charAt(i)!='.')){           
                anio = anio+fecha.charAt(i);
                i++;
            }            
        }
        try{        
	    	if(!invertido){    		
	    		aux.set(Integer.parseInt(anio),(Integer.parseInt(mes))-1,Integer.parseInt(dia));
	    	}else{
	    		aux.set(Integer.parseInt(dia),(Integer.parseInt(mes))-1,Integer.parseInt(anio));
	    	}
        }catch(RuntimeException e){
	    	System.out.println("Clase FECHA - fecha invalida !!");
	    }
    	if(aux != null)resultado = aux.getTime();
    	return resultado;
    }
   
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public void setFecha(String fecha){
    	setFecha(fecha,false);
    }
    
    public void setFecha(String fecha, Boolean invertido){
    	if(fecha != null){    		
    		if ( ((fecha.split("[-/]"))[0].length()>2) && (invertido == false) ){
    			invertido = true;
    		}
    		if ( ((fecha.split("[-/]"))[0].length()==2) && (invertido == true) ){
    			invertido = false;
    		}
    		this.fecha = this.convertirFecha(fecha, invertido);
    	}
    }
    /**
     * Convierte la fecha a formato Juliano de 5 caracteres YYDDD (YY: año, DDD: Dia del año )
     * @return
     */
    public void setFromJulian(String injulian){
       int[] dias_meses = {31,28,31,30,31,30,31,31,30,31,30,31};
       int day = Integer.parseInt(injulian.substring(2, 5));
       int month = 1;
       int year = Integer.parseInt(injulian.substring(0, 2))+2000;
       int cont = 0;
       while(day>dias_meses[cont]){
       	   month++;
    	   day = day-dias_meses[cont];
    	   cont++;
       }
	   this.setFecha(day+"-"+month+"-"+year);
    	   
    }
	public String getDiaNombre() {
		Semana sem = new Semana();
		cal.setTime(this.fecha);
		return sem.getNombreDia(cal.get(Calendar.DAY_OF_WEEK));
	}
	
    /*public Dia getDia() {
        return dia;
    }
    public void setDia(Dia dia) {
        this.dia = dia;
    }
    
    public void setDia(String dia){
        this.dia= dia;
    }*/
   
    public Integer getAnio(){
        cal.setTime(this.fecha);
        return cal.get(Calendar.YEAR);
    }
   
    public Integer getMes(){
        cal.setTime(this.fecha);
        return cal.get(Calendar.MONTH);
    }
   
    public Integer getDia(){
        cal.setTime(this.fecha);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
   
    /**
     * Devuelve un string con la fecha en un formato estandar "dd-MM-yyyy"     * 
     * @return String
     * 
     */
    public String getFechaFormateada(){
    	return this.getFechaFormateada("dd-MM-yyyy");               
    }
    /**
     * Devuelve un string con la fecha en el formato que se indique, 'dd' corresponde a dia , 'MM' a mes y 'yyyy' a año.
     * Ej dd/MM/yyyy , ver getFechaFormateada() 
     * @return String
     * 
     */
    public String getFechaFormateada(String formatoSalida){
        if(this.fecha==null) return "";
        SimpleDateFormat formato = new SimpleDateFormat(formatoSalida);
        String cadenaFecha = formato.format(this.fecha);
        return cadenaFecha;       
    }
   
    
   
    public String toString(){
        return getFechaFormateada();
    }
    
    /**
     * Dado un año de inicio y otro de fin retorna una lista con todos los años intermedios.
     * @param inicio
     * @param hasta
     * @return
     */
    public static List<Integer> getAniosDesdeHasta(Integer inicio, Integer hasta) {
    	List<Integer> anios = new ArrayList<Integer>();        
    	while(hasta>=inicio){
    		anios.add(hasta);
    		hasta--;        
    	}
    	Collections.sort(anios);
    	return anios;
    }
    
    public void sumarDias(Integer cantidad){
    	Calendar c1 = Calendar.getInstance();
    	c1.setTime(this.fecha);
    	c1.add(Calendar.DATE,cantidad); //fecha dentro de 30 dias    	
    	this.setFecha(c1.getTime());
    	
    }
    public boolean isValida(){
    	return (this.fecha != null);
    }
    
    public static Fecha parse(String fecha){
    	return new Fecha(fecha);
    }
    
    public String getMesNombre(){
    	//return Mes.getMes(this.getMes()).getNombre();
    	return getMesNombre(this.getMes());
    }
    
    /**
     * Convierte la fecha a formato Juliano de 5 caracteres YYDDD (YY: año, DDD: Dia del año )
     * @return
     */
    public String convertToJulian(){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(this.getFecha());
    	
    	String dayOfYear = String.format("%03d", calendar.get(Calendar.DAY_OF_YEAR));
    	String year = this.getAnio().toString().substring(2,4);
    	return year+dayOfYear ; 
    }
    
    /**
	 * Verifica si el String recibido como parametro es efectivamente una fecha valida, puede tener alguno de estos formatos:
	 * dd/mm/yyyy o dd-mm-yyyy.
	 * @param a
	 * @return boolean
	 */
	public  static  boolean isDate(String a){
		if(a == null){
			return false;
		}
		if (a.compareTo("")==0){
			return false;
		}
		else{
			int i = 0;
			String dia = "";
			while ((a.length()>i) && (a.charAt(i)!='-')&& (a.charAt(i)!='/')&& (a.charAt(i)!='.')){			
				dia = dia+a.charAt(i);
				i++;
			}
			i++;
			String mes = "";
			while ((a.length()>i) && (a.charAt(i)!='-')&& (a.charAt(i)!='/')&& (a.charAt(i)!='.')){			
				mes = mes+a.charAt(i);
				i++;
			}
			i++;
			String anio = "";
			while ((a.length()>i)){			
				anio = anio+a.charAt(i);
				i++;
			}
			
			try{
				int d = Integer.parseInt(dia);
				int m = Integer.parseInt(mes);
				int n = Integer.parseInt(anio);
				if (d<1 || d>31) return false;
				if (m<1 || m>12) return false;
				if ((m==4 || m==6 || m==9 || m==11)&&(d==31)) return false;
				if ((n % 4 != 0)&&(m==2)&& (d>28)) return false;
				if ((n % 4 == 0)&&(m==2)&& (d>29)) return false;
				if (n<1900||n>3000) return false;
			}catch(NumberFormatException e){
				return false;
			}
			
			
		}
		return (true);
	}
	
	/**
	 * Devuelve el nombre del mes en base al numero usado por Calendar, 0-Enero, 1-Febrero, etc
	 * @param num - numero mes
	 * @return String - nombre mes
	 */
	public static String getMesNombre(int num){
		switch(num){
			case 0: return "Enero";
			case 1: return "Febrero";
			case 2: return "Marzo";
			case 3: return "Abril";
			case 4: return "Mayo";
			case 5: return "Junio";
			case 6: return "Julio";
			case 7: return "Agosto";
			case 8: return "Septiembre";
			case 9: return "Octubre";
			case 10: return "Noviembre";
			case 11: return "Diciembre";
			default: return "Invalido";
		}
		
	}

	public int compareTo(Fecha o) {
		return this.getFecha().compareTo(o.getFecha());
	}
}
