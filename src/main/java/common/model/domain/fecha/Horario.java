package common.model.domain.fecha;

import java.io.Serializable;


public class Horario implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3571247081256048706L;
	private Integer hora;
	private Integer minutos;
	private String separador;
	
	/**
	 * Se inicializan el cero las horas y minutos. El separador por defecto es ":"
	 */
	public Horario(){
		hora = new Integer(0);
		minutos = new Integer(0);
		setSeparador(":"); 
	}
	
	/**
	 * Se recibe h horas y m minutos como parametro.
	 * @param h
	 * @param m
	 */
	public Horario(Integer h, Integer m){
		this();
		hora = h;
		minutos = m ;		
	}
	
	/**
	 * Se recibe h horas y m minutos como parametro.
	 * @param h
	 * @param m
	 */
	public Horario(String h, String m){
		this();
		hora = Integer.parseInt(h);
		minutos = Integer.parseInt(m) ;		
	}
	
	/**
	 * Se recibe un string de la forma 1430 para indicar las 14:30 horas.
	 * @param hm
	 */
	public Horario(String hm){
		this();
		this.setHorarioSimple(hm);		
	}

	public Integer getHora() {
		return hora;
	}

	public void setHora(Integer hora) {
		this.hora = hora;
	}
	
	public void setHora(String h) {
		this.hora = Integer.parseInt(h);;
	}

	public Integer getMinutos() {
		return minutos;
	}

	public void setMinutos(Integer minutos) {
		this.minutos = minutos;
	}

	public void setMinutos(String m) {
		this.minutos = Integer.parseInt(m);
	}

	/**
	 * String que se usará para separar horas y minutos.
	 * @param separador
	 */
	public void setSeparador(String separador) {
		this.separador = separador;
	}

	public String getSeparador() {
		return separador;
	}
	
	/**
	 * Devuelve un string conteniendo horas+separador+minutos. El separador por default es ':'
	 * @return
	 */
	public String getHorario(){
		return getHorarioNormalizado(this.hora.toString())+this.separador+getHorarioNormalizado(this.minutos.toString()); 
	}
	
	
	/**
	 * Deja 2 caracteres, agregando un cero adelante si el un nro. de una cifra.
	 * @param horario
	 * @return
	 */
	private String getHorarioNormalizado(String horario) {		
		if(horario.length()==1){
			return "0"+horario.substring(0, 1);
		}else{
			return horario.substring(0,2);
		}
	}
	
	/**
	 * Retorna un string de la forma 1430 para indicar las 14:30 horas. *
	 * Realizado para facilitar el mapeo con la base 
	 */
	public String getHorarioSimple(){
		return getHorarioNormalizado(this.hora.toString())+getHorarioNormalizado(this.minutos.toString()); 
	}
	
	/**
	 * Se recibe un string de la forma 1430 para indicar las 14:30 horas.
	 * Realizado para facilitar el mapeo con la base
	 * @param hm
	 */
	public void setHorarioSimple(String hm){
		if(hm.length() < 4) return;
		hora = Integer.parseInt(hm.substring(0, 2));
		minutos = Integer.parseInt(hm.substring(2, 4));		
	}
	
}
