package common.model.domain.fecha;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Semana implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5255125624441025568L;
	List <Dia> dias ;
	List <Integer> horas ;
	List <Integer> minutos ;
	Boolean[] semana;
	private String[] diasArreglo;
	
	public Boolean[] getSemana() {
		return semana;
	}

	public void setSemana(Boolean[] semana) {
		this.semana = semana;
	}

	public Semana(){
		semana = new Boolean[7];
		dias = new ArrayList<Dia>();
		
		// FIXME - pasar a xml
		dias.add(new Dia("Lunes",2));
		dias.add(new Dia("Martes",3));
		dias.add(new Dia("Miercoles",4));
		dias.add(new Dia("Jueves",5));
		dias.add(new Dia("Viernes",6));
		dias.add(new Dia("Sabado",7));
		dias.add(new Dia("Domingo",1));
		
		horas = new ArrayList<Integer>();
		for(int i=7; i<=23; i++)
			horas.add(new Integer(i));
		
		minutos = new ArrayList<Integer>();
		minutos.add(new Integer(0));
		minutos.add(new Integer(15));
		minutos.add(new Integer(30));
		minutos.add(new Integer(45));
		
		this.inicializarDias();
	}

	public List<Dia> getDias() {
		return dias;
	}
	
	public Iterator<Dia>getDiasIterator(){
		return dias.iterator();		
	}
	
	public List<Integer> getHoras() {
		return horas;
	}
	
	public List<Integer> getMinutos() {
		return minutos;
	}
	
	public Iterator<String> getNombresDias(){
		List <String> nombreDias = new ArrayList<String>();
		Iterator<Dia> itDias = dias.iterator();
		while(itDias.hasNext()){
			Dia dia = itDias.next();
			if(semana[dia.getNum()-1]) nombreDias.add(dia.getNombre());
		}
		return nombreDias.iterator();
	}
	
	/**
	 * En base a un numero de dia retorna el String con el nombre.
	 * @param d
	 * @return
	 */
	public String getNombreDia(Integer d){
		Iterator<Dia> it =this.dias.iterator(); 
		String diaN="";
		while(it.hasNext()){
				Dia dia=it.next();
				if(dia.getNum()== d){
					diaN=dia.getNombre();
				}
		}
		return diaN;
	}
	
	/**
	 * Recibe los dias como un String de longitud 7, en un pseudo binario, donde la posicion 0 representa
	 * el lunes , la 1 el martes, etc. Ejemplo 1010100 , seria lunes,miercoles y viernes.
	 * @param dias
	 */
	public void setDias(String dias) {
		
		for(int i=0;i<dias.length();i++){
			if(dias.charAt(i)=='0'){
				this.getSemana()[i] = new Boolean(false);
			}else{
				this.getSemana()[i] = new Boolean(true);
			}
			
		}
	}
	
	/**
	 * Recibe un String[] con los numero equivalentes a los dias, 1-lunes, 2-martes, etc
	 * Ejemplo [1,3,7] seria lunes, miercoles, viernes. El resto de los dias los setea en false.
	 * @param dias
	 */
	public void setDias(String []dias){
		this.setDiasArreglo(dias);
		this.inicializarDias();
		if(dias==null)return;
		for(int i=0; i<dias.length;i++){
			this.getSemana()[Integer.parseInt(dias[i])-1]=new Boolean(true);
			
		}
		
		
	}
	
	/**
	 * Devuelve un String de longitud 7 con formato pseduo binario donde cada posicion corresponde a un dia,
	 * y la misma esta seteada en 1 si la cursada se da ese dia. EJemplo : 1100000 , o sea se da Lunes y Martes.
	 * @return String
	 */
	
	public String getDiasBinario() {
		String res="";
		for(int i=0;i<this.getSemana().length;i++){
			if(this.getSemana()[i]){
				res+="1";
			}else{
				res+="0";
			}
			
		}
		return res;
	}

	public String[] getDiasArreglo() {
		return diasArreglo;
	}

	public void setDiasArreglo(String[] diasArreglo) {
		this.diasArreglo = diasArreglo;
	}
	
	/**
	 * Inicializa el arreglo de dias en falso.
	 */
	public void inicializarDias(){		
		for(int i=0;i<this.getSemana().length;i++){
			this.getSemana()[i]= new Boolean(false);
		}
	}
	
	/**
	 * Recibe un string con un numero entre 1 y 7 correspondiente a los dias de la semana, setea ese dia, el resto lo deja intacto
	 * @param dia
	 */
	public void setDiaCod(String dia){
		if(dia==null)return;
		if(Integer.parseInt(dia)>7)return;
		int p = Integer.parseInt(dia);
		this.getSemana()[p-1]= new Boolean(true);		
	}	
	
	/**
	 * Recibe un string con un valor entre 1 y 7 y devuelve true o false segun si ese dia se da la cursada o no 
	 * @param dia
	 * @return boolean
	 */
	public boolean getDiaCod(String dia){
		if(dia==null)return false;
		if(Integer.parseInt(dia)>7)return false;
		int p = Integer.parseInt(dia);
		return this.getSemana()[p-1];
	}
}
