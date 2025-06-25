package cuotas.model.reports;

import common.model.domain.fecha.Fecha;
import cuotas.model.domain.datos.Cuota;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.Persona;

public class Chequera {
	private static final Integer NUMERO_UNLA=9370153;
	private Integer totalInt;
	Persona persona;
	Grupo grupo;
	Cuota cuota;
	String fechaSting;
	String fechaVencimientoUno;
	String fechaVencimientoDos;
	Double total = 0.0;
	Integer montoVencimiento = 0;
	Integer digito;
	String codigo;
	public Chequera (){
		
	}
	public Chequera(Persona persona,Grupo grupo,Cuota cuota){
		this.setPersona(persona);
		this.setGrupo(grupo);
		this.setCuota(cuota);
		this.setFechaSting(cargaFechaString(cuota.getMes().getNumero(),grupo.getAnioCuota())); 
		this.setFechaVencimientoUno(cargaFechaVencimientoJulian(grupo.getVtoDias().toString(),
														cuota.getMes().getNumero().toString(),
														grupo.getAnioCuota().toString()));
		this.setFechaVencimientoDos(cargaFechaVencimientoJulian(grupo.getVtoPlus().toString(),
									cuota.getMes().getNumero().toString(),
									grupo.getAnioCuota().toString()));
		this.setTotal(cuota.getMonto1() + cuota.getMonto2());
		this.setMontoVencimiento(0);
		
		this.calcularDigito();
		this.cargarCodigo();
		
	}
	
	public Double CargaRecargoVencimiento(){
		//FIXME - pendiente de desarrollo.
		return null;
	}
	public String cargaFechaVencimientoJulian(String dias,String mes,String anio){
		if(dias==null || dias.equals("") || mes== null || mes.equals("") ||anio==null || anio.equals(""))
			return null;
		return new Fecha(dias +"/"+ mes+"/"+anio).convertToJulian();
	}
	public String cargaFechaString(Integer numero,Integer anio){
		if(numero==null || anio==null)
			return null;
		return Fecha.getMesNombre(numero)+" "+ anio;
	}
	
	public String getFechaVencimientoUno() {
		return fechaVencimientoUno;
	}
	public void setFechaVencimientoUno(String fechaVencimientoUno) {
		this.fechaVencimientoUno = fechaVencimientoUno;
	}
/********************************************************/
	public void cargaChequera(){ 
		//Persona persona;
	}
	@SuppressWarnings("static-access")
	public void cargarCodigo(){
		totalInt = parseDoubleInteger(this.getTotal());
		this.setCodigo(""+this.NUMERO_UNLA+""+String.format("%06d", totalInt*100)+""+this.getFechaVencimientoUno()+""+
		this.persona.getId()+""+this.grupo+""+String.format("%06d", montoVencimiento*100)+""+this.getFechaVencimientoDos()+""+this.getDigito());
	}
	
	public Integer parseDoubleInteger(Double numeroConvertir){
		Double totalDouble = numeroConvertir*100;
		/*
		System.out.println("totalDouble: ");
		String auxStr = totalDouble.toString();
		Integer indiceDeCorte = auxStr.indexOf(".");
		auxStr = auxStr.replaceAll(".", "");
		System.out.println("AuxStr: "+auxStr.substring(0,indiceDeCorte+1));
		auxStr = auxStr.substring(0,indiceDeCorte+1);
*/		
		totalInt = totalDouble.intValue();
		return totalInt;
	}
	
	@SuppressWarnings("static-access")
	public void calcularDigito(){
		totalInt = parseDoubleInteger(this.getTotal());
		System.out.println(String.format("%06d", totalInt));
		String aux = ""+this.NUMERO_UNLA+""+String.format("%06d", totalInt)+""+this.getFechaVencimientoUno()+""+
		this.persona.getId()+""+this.grupo+""+String.format("%06d", montoVencimiento*100)+""+this.getFechaVencimientoDos()+"";
		int lon = aux.length();
		int suma = 0;
		int[] secuencia = {1,3,5,7,9,3,5,7,9,3,5,7,9,3,5,7,9,3,5,7,9,3,5,7,9,3,5,7,9,3,5,7,9,3,5,7};
		for (int i = 0; i<lon; i++){			
			suma = suma + (secuencia[i] * ((int)aux.charAt(i)-48));
		}
		suma = suma /2;
		this.digito = suma % 10;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Integer getDigito() {
		return digito;
	}
	public void setDigito(Integer digito) {
		this.digito = digito;
	}
	public String getFechaVencimientoDos() {
		return fechaVencimientoDos;
	}
	public void setFechaVencimientoDos(String fechaVencimientoDos) {
		this.fechaVencimientoDos = fechaVencimientoDos;
	}
	public String getFechaSting() {
		return fechaSting;
	}
	public void setFechaSting(String fechaSting) {
		this.fechaSting = fechaSting;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getTotal() {
		return total;
	}
	public Integer getMontoVencimiento() {
		return montoVencimiento;
	}
	public void setMontoVencimiento(Integer montoVencimiento) {
		this.montoVencimiento = montoVencimiento;
	}
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	public Cuota getCuota() {
		return cuota;
	}
	public void setCuota(Cuota cuota) {
		this.cuota = cuota;
	}
	
	
}

