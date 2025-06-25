package cuotas.model.domain.datos;

import java.util.Date;
import java.util.Iterator;

import common.model.domain.datos.Estado;
import common.model.domain.fecha.Fecha;
import common.model.domain.fecha.Mes;
import cuotas.dataSource.repository.hibernate.FactoryDAO;

/**
 * @author bpade
 *
 */
public class Cheque{
	private static final String NUMERO_UNLA="09370153";	
	private Cuota cuota;
	private int numero;
	private String fechaSting;
	private Fecha fechaVencimientoUno;
	private Fecha fechaVencimientoDos;
	private Double monto1 = 0.0;
	private Double monto2 = 0.0;
	private Double total = 0.0;
	private Double total2 = 0.0;
	private Double montoVencimiento1 = 0.0;
	private Double montoVencimiento2 = 0.0;
	private Integer digito;
	private String codigo;
	private Chequera chequera;
	private String fechaVencimientoUnoJulian;
	private String fechaVencimientoDosJulian;
	private Pago pago_m = null;

	private boolean tieneSegundoVencimiento;
	
	public Cheque (){
		
	}
	private Double aplicarDescuento(Chequera chequera,Double monto, boolean aplicar_descuento){
		if(aplicar_descuento){
			Iterator<Inscripcion> ins = chequera.getPersona().getInscripciones().iterator();
			Grupo grupo = chequera.getGrupo();
			Integer descuento=0;
			while(ins.hasNext()){
				Inscripcion inscripcion = ins.next();
				Carrera carrera = inscripcion.getCarrera();
				if(carrera.equals(grupo.getCarrera())){
					if(inscripcion.getPersonasBeca()!=null){
						descuento = inscripcion.getPersonasBeca().getDescuentoBeca();
					}
				}
			}
			/*System.out.println("Descuento ");
			System.out.println("Monto sin descuento: " + monto);*/
			if(descuento!=null && descuento!=0){
				monto = (monto-((monto*descuento)/100));
			}
//			System.out.println("Monto con descuento: " + monto + " el decuento es de un: %"+descuento);
		}
		return monto;
	}
	
	public Cheque(Chequera chequera,Pago pago, Cuota cuota, boolean forzado, int cont, boolean excepcion, String tipo, boolean desdePersona){
		System.out.println("############################################");
		System.out.println("#FORZADO: " + forzado);
		System.out.println("############################################");
		this.chequera = chequera;	
		Fecha hoy = new Fecha(new Date(System.currentTimeMillis()));
		String codigo_viejo = null;
		boolean aplicar_descuento =  true;
		if(cuota == null){
			cuota = new Cuota();
			cuota.setMonto1(pago.getMonto1());
			cuota.setMonto2(pago.getMonto2());
			cuota.setGrupo(chequera.getGrupo());
			cuota.setMes(new Mes(pago.getFecha().getMes()+1));
		}	
		if(desdePersona && pago != null){
			aplicar_descuento = false;
		}
		if(pago == null){
			pago = new Pago();
			pago.setEstado(new Estado("Activo"));
			pago.setMonto1(aplicarDescuento(chequera,cuota.getMonto1(), aplicar_descuento));
			pago.setMonto2(aplicarDescuento(chequera,cuota.getMonto2(), aplicar_descuento));
			pago.setMonPgo(0.0);
			pago.setGrupo(chequera.getGrupo());
			pago.setPersona(chequera.getPersona());
			pago.setVtoMonto(chequera.getGrupo().getVtoMonto());
			pago.setAno(chequera.getGrupo().getAnioCuota());
			pago.setFecha(new Fecha("1-"+cuota.getMes().getNumero()+"-"+chequera.getGrupo().getAnioCuota()));
			pago.setFechaCreacion(hoy);
			pago.setOrigenPago(FactoryDAO.origenPagoDAO.findByInteger("1"));
			/*if(tipo.equals("M")){
				pago.setFecha(new Fecha("1-"+cuota.getMes().getNumero()+"-"+chequera.getGrupo().getAnioCuota()));		
			}else{
				pago.setFecha(new Fecha("10-"+cuota.getMes().getNumero()+"-"+chequera.getGrupo().getAnioCuota()));
			}*/
			
		}else{
			codigo_viejo = pago.getCodigo_barra();
		}
		
		int pago_anio;
		int pago_anio2;
		if(desdePersona){
			pago_anio = pago.getAno();
			pago_anio2 = pago.getAno();
		}else{
			pago_anio = chequera.getGrupo().getAnioCuota();
			pago_anio2 = chequera.getGrupo().getAnioCuota();
		}
		//int pago_anio = chequera.getGrupo().getAnioCuota();
		pago.setExceptuarMora(excepcion);
		
		if(!forzado){
			//pago_anio = pago.getFecha().getAnio();
			this.monto1 = pago.getMonto1();//aplicarDescuento(chequera,pago.getMonto1(), aplicar_descuento);
			this.monto2 = pago.getMonto2();//aplicarDescuento(chequera,pago.getMonto2(), aplicar_descuento);
			int dias = 0;
			int pago_mes = pago.getFecha().getMes()+1;
			int pago_mes2 = pago.getFecha().getMes()+1;
			//this.setTotal(aplicarDescuento(chequera,pago.getMonto1(), aplicar_descuento) + aplicarDescuento(chequera,pago.getMonto2(), aplicar_descuento));		
			this.setTotal(pago.getMonto1()+pago.getMonto2());
			this.setMontoVencimiento1(0.0);
			this.setMontoVencimiento2(0.0);
			
			if(chequera.getGrupo().getCobraMora().equals('S')&& !excepcion){
				this.setMontoVencimiento1(this.calcularMora(pago_mes, pago_anio, this.getTotal()));							
				this.setMontoVencimiento2(pago.getGrupo().getVtoMonto()+this.calcularMora(pago_mes, pago_anio, this.getTotal()));
				if(pago_anio<hoy.getAnio()){
					pago_anio2 = hoy.getAnio();
					pago_mes2 = hoy.getMes()+1;
				}else{
					if(pago_mes<hoy.getMes()+1){
						pago_mes2=hoy.getMes()+1;
					}
				}	
				pago_anio = pago_anio2;
				pago_mes = pago_mes2;	
				
			}else{
				if(pago_anio<hoy.getAnio()){				
					pago_anio2 = hoy.getAnio();
					pago_mes2 = hoy.getMes()+1;
				}else{
					if(pago_mes<hoy.getMes()+1){						
						pago_mes2=hoy.getMes()+1;
						
					}
				}
				pago_anio = pago.getAno();
				//comento esta linea ya que se le asigno un valor a pago_mes mas arriba.
				//pago_mes = pago.getFecha().getMes();
				dias = pago.getFecha().getDia();
			}					
			if(tipo.equals("E")){
				dias = dias +12;
			}
			System.out.println("pago.getFecha().getMes(): " + pago_mes);
			if(chequera.getGrupo().getVtoDias()!=0){
				
				this.setFechaVencimientoUno(cargaFechaString(dias,
														chequera.getGrupo().getVtoDias(),
														pago_mes,
														pago_anio));
				this.setFechaVencimientoUnoJulian(cargaFechaVencimientoJulian(dias,chequera.getGrupo().getVtoDias(),
						pago_mes,
						pago_anio));
			}
			if(chequera.getGrupo().getVtoPlus()!=0){
				this.setFechaVencimientoDos(cargaFechaString(0,
														chequera.getGrupo().getVtoPlus(),
														pago_mes2,
														pago_anio2));		
				this.setFechaVencimientoDosJulian(cargaFechaVencimientoJulian(0,chequera.getGrupo().getVtoPlus(),
										pago_mes2,
										pago_anio2));
				this.tieneSegundoVencimiento = true;
			}
		}else{
			pago.setEstado(new Estado("Activo"));
			pago.setObservaciones("");
			pago.setMonto1(aplicarDescuento(chequera,cuota.getMonto1(), aplicar_descuento));
			pago.setMonto2(aplicarDescuento(chequera,cuota.getMonto2(), aplicar_descuento));
			pago.setMonPgo(0.0);
			pago.setVtoMonto(chequera.getGrupo().getVtoMonto());
			pago.setAno(chequera.getGrupo().getAnioCuota());
			pago.setFecha(new Fecha("1-"+cuota.getMes().getNumero()+"-"+chequera.getGrupo().getAnioCuota()));
			
			//para poder identificar las cuotas mal generadas.	
			pago.setFechaCreacion(hoy);
			
			this.monto1 = aplicarDescuento(chequera,cuota.getMonto1(), aplicar_descuento);
			this.monto2 = aplicarDescuento(chequera,cuota.getMonto2(), aplicar_descuento);
						
			int cuota_mes = cuota.getMes().getNumero();
			int cuota_mes2 = cuota.getMes().getNumero();
			//int pago_mes = pago.getFecha().getMes()+1;
			int dias = 0;
			this.setTotal(aplicarDescuento(chequera,cuota.getMonto1(), aplicar_descuento) + aplicarDescuento(chequera,cuota.getMonto2(), aplicar_descuento));
			this.setMontoVencimiento1(0.0);
			this.setMontoVencimiento2(0.0);
			
			if(chequera.getGrupo().getCobraMora().equals('S') && !excepcion){
				this.setMontoVencimiento1(this.calcularMora(cuota_mes, pago_anio, this.getTotal()));
				this.setMontoVencimiento2(cuota.getGrupo().getVtoMonto()+this.calcularMora(cuota_mes, pago_anio, this.getTotal()));	

				if(pago_anio<hoy.getAnio()){
					pago_anio2 = hoy.getAnio();
					//dias =  hoy.getMes() - cuota_mes +1;
					cuota_mes2 = hoy.getMes()+1;
				}else{
					if(cuota_mes < hoy.getMes()+1){
						//dias = hoy.getMes() - cuota_mes +1;
						cuota_mes2 = hoy.getMes()+1;
					}
				}
				pago_anio = pago_anio2;
				cuota_mes = cuota_mes2;
				dias = pago.getFecha().getDia();
			}else{
				if(pago_anio<hoy.getAnio()){
					pago_anio2 = hoy.getAnio();
					//dias =  hoy.getMes() - cuota_mes +1;
					cuota_mes2 = hoy.getMes()+1;
				}else{
					if(cuota_mes < hoy.getMes()+1){
						//dias = hoy.getMes() - cuota_mes +1;
						cuota_mes2 = hoy.getMes()+1;
					
					}
				}
				pago_anio = pago.getAno();
				cuota_mes = cuota.getMes().getNumero();
				dias = pago.getFecha().getDia();
			}
			if(tipo.equals("E")){
				dias = dias +12;
			}
			System.out.println(" - cuota.getMes().getNumero(): "+ cuota_mes);
			if(chequera.getGrupo().getVtoDias()!=0){
				this.setFechaVencimientoUno(cargaFechaString(
						dias,
						chequera.getGrupo().getVtoDias(),
						cuota_mes,
						pago_anio));
				this.setFechaVencimientoUnoJulian(cargaFechaVencimientoJulian(
						dias,
						chequera.getGrupo().getVtoDias(),
						cuota_mes,
						pago_anio));
			}		
			if(chequera.getGrupo().getVtoPlus()!=0){
				this.setFechaVencimientoDos(cargaFechaString(0,
						chequera.getGrupo().getVtoPlus(),
						cuota_mes2,
						pago_anio2));
				this.setFechaVencimientoDosJulian(cargaFechaVencimientoJulian(0,chequera.getGrupo().getVtoPlus(),		
						cuota_mes2,
						pago_anio2));
			}		
		}
		this.setCuota(cuota);
		this.setFechaSting(cargaFechaString(cuota.getMes().getNumero(),	pago.getFecha().getAnio())); //chequera.getGrupo().getAnioCuota()));
		pago.setNumero(cont);
		this.numero = cont;

		this.pago_m = pago;
		this.pago_m.setTipo(tipo);
	
		this.calcularDigito();
		this.cargarCodigo();
		pago.setCodigo_barra(this.codigo);
		
		if(codigo_viejo != null && !codigo_viejo.equals(this.codigo)){
			//en este caso, crear un nuevo codigo anterior, 
			//setearselos a pago_m que tenga el valor del codigo de barra viejo.... 
			//despues hacemos el buscar para pago facil.
			CodigosAnteriores codigoAnterior = new CodigosAnteriores(pago_m,codigo_viejo);
			pago_m.addCodigoAnterior(codigoAnterior);
			System.out.println("Tenia el codigo: "+codigo_viejo+" ahora tiene "+pago_m.getCodigo_barra());			
		}else{
			System.out.println("Tiene "+pago_m.getCodigo_barra());
		}
	}
	public Double CargaRecargoVencimiento(){
		//FIXME - pendiente de desarrollo.
		return null;
	}
	public String cargaFechaVencimientoJulian(int dia_ex, Integer dias,Integer mes,Integer anio){
		if(dias==null || mes==null || anio==null){
			return null;
		}
		while(dias > 30){
			mes++;
			dias = dias - 30;
		}
		dias = dias + dia_ex;		
		return new Fecha(dias +"/"+ mes+"/"+anio).convertToJulian();
	}
	public String cargaFechaString(Integer numero,Integer anio){
		if(numero==null || anio==null)
			return null;
		
		return Fecha.getMesNombre(numero-1)+" "+ anio;
	}
	public Fecha cargaFechaString(Integer dia_ex,Integer dias,Integer mes,Integer anio){		
		if(dias==null || mes==null || anio==null){
			return null;
		}
		while(dias > 30){
			mes++;
			dias = dias - 30;
		}
		dias = dias + dia_ex;
		
		return new Fecha(dias +"/"+ mes+"/"+anio);
	}
	public Fecha getFechaVencimientoUno() {
		return fechaVencimientoUno;
	}
	public void setFechaVencimientoUno(Fecha fechaVencimientoUno) {
		this.fechaVencimientoUno = fechaVencimientoUno;
	}
/********************************************************/
	public void cargaChequera(){ 
		//Persona persona;
	}
	@SuppressWarnings("static-access")
	public void cargarCodigo(){
		this.setCodigo(this.codigo.concat(this.getDigito().toString()));
	}
	
	public Integer parseDoubleInteger(Double numeroConvertir){
		Integer totalInt;
		Double totalDouble = numeroConvertir*100;
		totalInt = totalDouble.intValue();
		return totalInt;
	}
	
	@SuppressWarnings("static-access")
	public void calcularDigito(){
		Integer totalInt;
		Integer totalInt1;
		Integer totalInt2;
		totalInt = parseDoubleInteger(this.getTotal());
		totalInt1 = parseDoubleInteger(this.montoVencimiento1);
		totalInt2 = parseDoubleInteger(this.montoVencimiento2);
		//System.out.println(String.format("%06d", totalInt));
		String aux = "";
		aux = aux.concat(this.NUMERO_UNLA.toString());
		aux = aux.concat(String.format("%06d", totalInt + totalInt1));
		aux = aux.concat(this.getFechaVencimientoUnoJulian());
		aux = aux.concat(String.format("%06d",this.chequera.persona.getId()));
		aux = aux.concat(String.format("%03d",this.chequera.grupo.getNumero()));
		this.pago_m.setCodigo_usuario(String.format("%06d",this.chequera.persona.getId()).concat(String.format("%03d",this.chequera.grupo.getNumero())));
		//aux = aux.concat(String.format("%05d",this.chequera.persona.getId()));
		//aux = aux.concat(String.format("%04d",this.chequera.grupo.getId()));
		aux = aux.concat(String.format("%06d", totalInt + totalInt2));
		aux = aux.concat(this.getFechaVencimientoDosJulian());
		int lon = aux.length();
		System.out.println ("longitud del codigo: "+lon);
		int suma = 0;
		int[] secuencia = {1,3,5,7,9,3,5,7,9,3,5,7,9,3,5,7,9,3,5,7,9,3,5,7,9,3,5,7,9,3,5,7,9,3,5,7,9,3,5,7,9,3,5,7,9};
		for (int i = 0; i<lon; i++){			
			suma = suma + (secuencia[i] * ((int)aux.charAt(i)-48));
		}
		suma = suma /2;
		this.codigo = aux;
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
	public Fecha getFechaVencimientoDos() {
		return fechaVencimientoDos;
	}
	public void setFechaVencimientoDos(Fecha fechaVencimientoDos) {
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
	public Double getMontoVencimiento1() {
		return montoVencimiento1;
	}
	public void setMontoVencimiento1(Double montoVencimiento) {
		this.montoVencimiento1 = montoVencimiento;
	}
	public Double getMontoVencimiento2() {
		return montoVencimiento2;
	}
	public void setMontoVencimiento2(Double montoVencimiento) {
		this.montoVencimiento2 = montoVencimiento;
	}
	public Cuota getCuota() {
		return cuota;
	}
	public void setCuota(Cuota cuota) {
		this.cuota = cuota;
	}
	
	public Chequera getChequera() {
		return chequera;
	}
	public void setChequera(Chequera chequera) {
		this.chequera = chequera;
	}
	public String getFechaVencimientoUnoJulian() {
		return fechaVencimientoUnoJulian;
	}
	public void setFechaVencimientoUnoJulian(String fechaVencimientoUnoJulian) {
		this.fechaVencimientoUnoJulian = fechaVencimientoUnoJulian;
	}
	public String getFechaVencimientoDosJulian() {
		return fechaVencimientoDosJulian;
	}
	public void setFechaVencimientoDosJulian(String fechaVencimientoDosJulian) {
		this.fechaVencimientoDosJulian = fechaVencimientoDosJulian;
	}
	public Double getMonto1() {
		return monto1;
	}
	public void setMonto1(Double monto1) {
		this.monto1 = monto1;
	}
	public Double getMonto2() {
		return monto2;
	}
	public void setMonto2(Double monto2) {
		this.monto2 = monto2;
	}
	public Pago getPago_m() {
		return pago_m;
	}
	public void setPago_m(Pago pago) {
		this.pago_m = pago;
	}

	public boolean isTieneSegundoVencimiento() {
		return tieneSegundoVencimiento;
	}
	public void setTieneSegundoVencimiento(boolean tieneSegundoVencimiento) {
		this.tieneSegundoVencimiento = tieneSegundoVencimiento;
	}
	public double calcularMora(int mes, int anio, double total){
		Fecha hoy = new Fecha(new Date(System.currentTimeMillis()));
		System.out.println("mensage "+hoy.getAnio()+" - "+anio+" "+ hoy.getMes()+1+" - "+mes);
		int meses = (hoy.getAnio() - anio) * 12 + hoy.getMes()+1 - mes;
		if(meses<0) return 0;
		return total * 2 /100 * meses;
	}


	public int getNumero() {
		return numero;
	}


	public void setNumero(int numero) {
		this.numero = numero;
	}

	
}

