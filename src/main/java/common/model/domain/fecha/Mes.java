package common.model.domain.fecha;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import common.model.domain.error.ErrorParametro;


public class Mes implements Serializable{
	private static final long serialVersionUID = -8873110452353651265L;
	
	private String nombre;
	private Integer numero;
	private static Map<Integer,Mes> mesesValidos;
	
	static{
		mesesValidos = new HashMap<Integer,Mes>();
		Integer numero = new Integer(1);
		mesesValidos.put(numero, new Mes("Enero",numero));
		numero++;
		mesesValidos.put(numero, new Mes("Febrero",numero));
		numero++;
		mesesValidos.put(numero, new Mes("Marzo",numero));
		numero++;
		mesesValidos.put(numero, new Mes("Abril",numero));
		numero++;
		mesesValidos.put(numero, new Mes("Mayo",numero));
		numero++;
		mesesValidos.put(numero, new Mes("Junio",numero));
		numero++;
		mesesValidos.put(numero, new Mes("Julio",numero));
		numero++;
		mesesValidos.put(numero, new Mes("Agosto",numero));
		numero++;
		mesesValidos.put(numero, new Mes("Septiembre",numero));
		numero++;
		mesesValidos.put(numero, new Mes("Octubre",numero));
		numero++;
		mesesValidos.put(numero, new Mes("Noviembre",numero));
		numero++;
		mesesValidos.put(numero, new Mes("Diciembre",numero));
	}
	
	/**
	 * Contructor vacio para hibernate
	 */
	public Mes(){
		
	}
	
	/**
	 * Se crea un mes con un nombre y un numero sin validar ninguno de los datos
	 * @param nombre
	 * @param num
	 */
	public Mes(String nombre, Integer num){
		this.nombre = nombre;
		this.numero = num;
	}
	
	/**
	 * Dado un numero se verifica este y se crea el mes con su nombre correspondiente.
	 * @param num
	 */
	public Mes (Integer num){
		this.setNumero(num);
	}
	/**
	 * @return Lista de meses del a�o.
	 * 
	 */
	public static List<Mes> getMesesValidos(){
		
		Iterator<Map.Entry<Integer, Mes>> it = mesesValidos.entrySet().iterator();
		List<Mes> listaMeses = new ArrayList<Mes>();
	    while (it.hasNext()) {
	        listaMeses.add(it.next().getValue());
	    }
	    return listaMeses;
	}
	
	/**
	 * Dado un entero retorna un objeto con el mes correspondiente
	 */
	public static Mes getMes(Integer num){
		Mes resp = null;		
		Mes aux = mesesValidos.get(num);
		
		if(aux != null)
			resp =  new Mes(new String(aux.getNombre()),new Integer(aux.getNumero()));
		
		return resp;
			
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Integer getNumero() {
		return numero;
	}
	
	/**
	 * Al setear el numero de mes, este sera validado y tambien cargara el nombre correspondiente.
	 * @param num
	 */
	public void setNumero(Integer num) {
		
		Mes aux = mesesValidos.get(num);
		
		if(aux != null){
			this.numero = num;
			this.nombre = new String(aux.getNombre());
		}else{
			throw new ErrorParametro("No se indico un mes valido");
		}
		
	}
	
	
}
