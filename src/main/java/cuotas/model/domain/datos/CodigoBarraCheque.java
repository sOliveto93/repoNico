package cuotas.model.domain.datos;

import java.util.Date;
import java.util.GregorianCalendar;

import common.model.domain.fecha.Fecha;

/**
 * @author jforastier
 *
 */
public class CodigoBarraCheque {

	private String identificadorUnla;
	private Double importeCuota;
	private Fecha primerVencimiento;
	private String identificacionAlumno;
	private Double segundoImporte;
	private Fecha segundoVencimiento;
	private String digitoVerificador;
	
	public CodigoBarraCheque(String codigoBarras){
		
		String aux ="";
		String primerImporteS = "";
		String segundoImporteS = "";
		
		Fecha fecha1 = new Fecha();
		Fecha fecha2 = new Fecha();
		
		//system.out.println("Codigo Barras: "+codigoBarras + " Size: "+codigoBarras.length());
		this.identificadorUnla = codigoBarras.substring(0, 8);
			//system.out.println("identificadorUnla= "+this.identificadorUnla);
		primerImporteS = codigoBarras.substring(8, 12)+"."+codigoBarras.substring(12, 14);
		//system.out.println("PrimerImporteString: " + codigoBarras.substring(8, 12)+"."+codigoBarras.substring(12, 14));
		this.importeCuota = Double.parseDouble(primerImporteS) ;
			//system.out.println("importe1: " + this.importeCuota);
		fecha1.setFromJulian(codigoBarras.substring(14,19));
		this.primerVencimiento = fecha1;
		//system.out.println("primerVencimiento: "+this.primerVencimiento.getFechaFormateada());

		this.identificacionAlumno = codigoBarras.substring(19,28);
			//system.out.println("identificadorAlumno: " + this.identificacionAlumno);
		segundoImporteS = codigoBarras.substring(28, 32)+"."+codigoBarras.substring(32, 34);
		//system.out.println("PrimerImporteString: " + codigoBarras.substring(28, 32)+"."+codigoBarras.substring(32, 34));
		this.segundoImporte = Double.parseDouble(segundoImporteS);
			//system.out.println("Segundo Importe: " + this.segundoImporte);
		
		fecha2.setFromJulian(codigoBarras.substring(34,39));
		this.segundoVencimiento = fecha2;
			//system.out.println("SegundoVencimiento: "+this.segundoVencimiento.getFechaFormateada());
		this.digitoVerificador = codigoBarras.substring(39,40);
//system.out.println("DigitoVerificador: " + this.digitoVerificador);
	}
	
	public String getIdentificadorUnla() {
		return identificadorUnla;
	}
	public void setIdentificadorUnla(String identificadorUnla) {
		this.identificadorUnla = identificadorUnla;
	}
	public Double getImporteCuota() {
		return importeCuota;
	}
	public void setImporteCuota(Double importeCuota) {
		this.importeCuota = importeCuota;
	}
	public Fecha getPrimerVencimiento() {
		return primerVencimiento;
	}
	public void setPrimerVencimiento(Fecha primerVencimiento) {
		this.primerVencimiento = primerVencimiento;
	}
	public String getIdentificacionAlumno() {
		return identificacionAlumno;
	}
	public void setIdentificacionAlumno(String identificacionAlumno) {
		this.identificacionAlumno = identificacionAlumno;
	}
	public Double getSegundoImporte() {
		return segundoImporte;
	}
	public void setSegundoImporte(Double segundoImporte) {
		this.segundoImporte = segundoImporte;
	}
	public Fecha getSegundoVencimiento() {
		return segundoVencimiento;
	}
	public void setSegundoVencimiento(Fecha segundoVencimiento) {
		this.segundoVencimiento = segundoVencimiento;
	}
	public String getDigitoVerificador() {
		return digitoVerificador;
	}
	public void setDigitoVerificador(String digitoVerificador) {
		this.digitoVerificador = digitoVerificador;
	}
}
