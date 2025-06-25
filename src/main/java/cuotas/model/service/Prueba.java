package cuotas.model.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestResult;

import org.pentaho.reporting.engine.classic.core.ReportProcessingException;

import bsh.This;

import common.model.domain.datosPersonales.Documento;
import common.model.domain.datosPersonales.TipoDocumento;
import common.model.domain.fecha.Fecha;
import common.model.domain.fecha.RangoFecha;
import common.model.reports.AbstractReportGenerator;

import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.dataSource.repository.hibernate.GrupoDAO;
import cuotas.dataSource.repository.hibernate.InscripcionDAO;
import cuotas.dataSource.repository.hibernate.PagoDAO;
import cuotas.model.domain.datos.Carrera;
import cuotas.model.domain.datos.Cheque;
import cuotas.model.domain.datos.Cuota;
import cuotas.model.domain.datos.Grupo;
import cuotas.model.domain.datos.Inscripcion;
import cuotas.model.domain.datos.Pago;
import cuotas.model.domain.datos.PagoSumarizado;
import cuotas.model.domain.datos.Persona;
import cuotas.model.domain.factura.Factura;
import cuotas.model.domain.factura.FacturaItem;
import cuotas.model.domain.factura.FacturaItemTipo;
import cuotas.model.domain.factura.RubroCobro;
import cuotas.model.domain.factura.SucursalCobro;
import cuotas.model.domain.grupo.RangoGrupo;
import cuotas.model.domain.json.FactoryJson;
import cuotas.reports.src.PagosIteratorReport;
import cuotas.reports.src.PagosReportModelator;

public class Prueba {

	/**
	 * @param args
	 */
	
	public static void pruebaReporteCompleto(){
		 Grupo grupoTest = FactoryDAO.grupoDAO.findByLong("4");
		 Iterator<Pago> pagos = FactoryDAO.pagoDAO.pagosDeIntegrantesGrupo(grupoTest,2011,null).iterator();
		 int i = 0;
		 Persona personaAnterior = null ;
		 Double total = 0.0;
		   while(pagos.hasNext()){
		    	i++;
		    	Pago pago = pagos.next();	    	
		    	
		    	if(pago.getPersona() != personaAnterior || personaAnterior == null)	
		    		System.out.println("TipoDNI: " + pago.getPersona().getDocumento().getTipo().getAbreviacion() +
	    					   "  nro.Documento: " + pago.getPersona().getDocumento().getNumero() +
	    					   "  Nombre: "+ pago.getPersona().getNombre()+
	    					   "  Apellido: "+pago.getPersona().getApellido()+
	    					   "  Domicilio"+ pago.getPersona().getDireccion().getDomicilioCompleto());
		    	
		    	if(pago.getPersona() == personaAnterior || personaAnterior == null){
		    		total += pago.getMonto1();
		    		personaAnterior = pago.getPersona();
		    	}else{
		    		System.out.println("TOTAL PERSONA : " + total);
		    		total = 0.0;
		    		personaAnterior = pago.getPersona();
		    	}
		    	
		    	
		    /*	System.out.println("TipoDNI: " + pago.getPersona().getDocumento().getTipo().getAbreviacion() +
		    					   "nro.Documento: " + pago.getPersona().getDocumento().getNumero() +
		    					   "Nombre: "+ pago.getPersona().getNombre()+
		    					   "Apellido: "+pago.getPersona().getApellido()+
		    					   "Domicilio"+ pago.getPersona().getDireccion().getDomicilioCompleto()	    					   
		    						);*/
		    	System.out.println(" _ FECHA : "+ pago.getFecha() +
		    					   " - MONTO : " +pago.getMonto1() + " - monPgo: " + pago.getMonPgo());		
		    	/*
		    	 System.out.println(pago.getPersona().getApellido() +", "+ 
		    						pago.getPersona().getNombre() + 
		    						" _ FECHA : "+ pago.getFecha() +
		    						" - MONTO : " +pago.getMonto1()+ 
		    						" _ GRUPO: " + pago.getGrupo().getDescripcion());
		    	*/
		    	if(i>60)
		    		break;
		    }
	}
	
	public static void pruebaReporte(){
		// Create an output filename
	    final File outputFilename = new File(PagosIteratorReport.class.getSimpleName() + ".pdf");
	    final File outputFilename2 = new File(PagosIteratorReport.class.getSimpleName() + "2.xls");

	    // Generate the report
	    PagosIteratorReport ar = new PagosIteratorReport();
	    
	    Grupo grupoTest = FactoryDAO.grupoDAO.findByLong("4");
	    Iterator<Pago> pagos = FactoryDAO.pagoDAO.pagosDeIntegrantesGrupo(grupoTest,2011,null).iterator();
	    	    
	    ClassLoader classloader = ar.getClass().getClassLoader();    
	    // Que template quiero usar
	    ar.setTemplateURL(classloader.getResource("cuotas/reports/templates/CuotasPorGrupoMonto.prpt"));
	    // Que iterator quiero que recorra
	    ar.setResults(pagos);
	    // Como quiero que haga el mapeo de datos entre el modelo y el reporte
	    ar.setAm(new PagosReportModelator());   
	    // Genero el reporte
	    try {
			ar.generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReportProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Output the location of the file
	    System.err.println("yesssss. Generated the report [" + outputFilename.getAbsolutePath() + "]");
		
		/*
	    // Limpio el modelo para poner nuevos resultados
	    ar.getAm().flushModel();
	    // Un nuevo template
	    ar.setTemplateURL(classloader.getResource("cursos/reports/templates/Alumno.prpt"));
	    // nuevos resultados
	    ar.setResults(daoAlumno.buscarPorApellido("Gonzalez"));
	    // Genero otro
	    ar.generateReport(AbstractReportGenerator.OutputType.EXCEL, outputFilename2);
	    */
	}
	 public static void cancelacionesDeUnPeriodo(){
	    	/*Grupo grupoTest = FactoryDAO.grupoDAO.findByLong("4");
	    	Iterator<Pago> pagos = FactoryDAO.pagoDAO.cancelacionesDeUnPeriodo(grupoTest, null,null, null, null, null,true).iterator();
	    	while (pagos.hasNext()){
	    		Pago pago = pagos.next();
	    		System.out.println("Codigo: " + pago.getGrupo().getId() + " Descripcion: "+pago.getGrupo().getDescripcion() + pago.getMonto1());
	    	}*/
	    }
		public void armarChequerasDesdeGrupo(){
		   /* Grupo grupo = FactoryDAO.grupoDAO.findByLong("4");
		    Carrera carrera= FactoryDAO.carreraDAO.findByLong(grupo.getCarrera().toString());
		    Iterator<Inscripcion> inscripciones = FactoryDAO.inscripcionDAO.personasPorGrupo(carrera).iterator();
		    Persona persona = null;
		    Iterator<Cuota> cuotasIt;
		    
		    List<Chequera> chequeras = new ArrayList<Chequera>(); 
		    while(inscripciones.hasNext()){
		    	Inscripcion inscripcion = inscripciones.next();
		    	persona = inscripcion.getPersona();
		    	cuotasIt = grupo.getCuotas().iterator();
		    	while (cuotasIt.hasNext()){
		    		Cuota cuota = cuotasIt.next();
		    		chequeras.add(new Chequera(persona,grupo,cuota));
		    	}
		    }
		    System.out.println("SizeChequera: "+chequeras.size());
		    */
		}
	
	public static void main(String[] args) {
	    Grupo grupo = FactoryDAO.grupoDAO.findByLong("4");
	    Carrera carrera= FactoryDAO.carreraDAO.findByLong(grupo.getCarrera().getId().toString());
	    Iterator<Inscripcion> inscripciones = FactoryDAO.inscripcionDAO.personasPorGrupo(carrera).iterator();
	    Persona persona = null;
	    Iterator<Cuota> cuotasIt;
	    
	    List<Cheque> chequeras = new ArrayList<Cheque>(); 
	    while(inscripciones.hasNext()){
	    	Inscripcion inscripcion = inscripciones.next();
	    	persona = inscripcion.getPersona();
	    	cuotasIt = grupo.getCuotas().iterator();
	    	while (cuotasIt.hasNext()){
	    		Cuota cuota = cuotasIt.next();
	    		//chequeras.add(new Cheque(persona,grupo,cuota));
	    	}
	    }
	    System.out.println("SizeChequera: "+chequeras.size());
		/*String idRubroCobro = "01";
		String idFacturaItemTipo = "09";
		String cantidadItem = "1";
		Integer cantidad = Integer.parseInt(cantidadItem);
		String descripcion = "prueba jforastier";
		String dniPersona  = "23091404"; //
		String formaDePago = "1";
		String fechaDePago = "02/12/2012";
		String idSucursalCobro = "1";
		Integer numeroFactura = FactoryDAO.facturaDAO.buscarUltimoNumeroFactura() + 1;
		Carrera carrera = FactoryDAO.carreraDAO.findByLong("1");
		Fecha fecha = new Fecha(fechaDePago);
		
		
		// Cuando entras a la ventana de generacion de factura y habriaa que guardarlo en la session/request
		Factura factura = new Factura();
		
		//Get factura actualizada
		factura.setFormaPago(Integer.parseInt(formaDePago));
		factura.setFechaCarga(fecha);
		factura.setFechaPago(fecha);
		factura.setNro(numeroFactura);
		factura.setDescripcionBasica(descripcion);
		
		//Cuando ingresa el documento en el form , levantar la factura de la session/request
		Documento documento = new Documento(dniPersona,new TipoDocumento("DN"));
		Persona persona = FactoryDAO.personaDAO.buscarPorDocumento(documento);
		factura.setPersona(persona);
	

		SucursalCobro sucursalCobro = FactoryDAO.sucursalCobroDAO.findByInteger(idSucursalCobro);
		factura.setSucursalCobro(sucursalCobro);
		
		System.out.println("numeroFactura: "+numeroFactura);
		//TODO - De donde saca el idCarrera?
		factura.setCarrera(carrera);
		
		
		//EL monto 
		//factura.setMonto(monto);	
		
		System.out.println("Persona"+factura.getPersona().getId());
		
		FactoryDAO.facturaDAO.save(factura);
		System.out.println("IdFactura: " + factura.getId() + " - Nro: " + factura.getNro());

		
		//FactoryDAO.facturaItemDAO.save(facturaItem);
		*/
		
		/*Grupo grupoTest = FactoryDAO.grupoDAO.findByLong("4");
		PagoDAO pagoDAO = new PagoDAO();
		Fecha fechaDesde = new Fecha("01/01/2012");
		RangoFecha rangoFechaPago = new RangoFecha();
		rangoFechaPago.setFechaDesde(fechaDesde);
		//rangoFechaPago.setFechaHasta(fechaDesde);
		Integer cohorte = 2010;
		String numeroDocumento = "26186925";
		Persona persona = FactoryDAO.personaDAO.buscarPorDocumento(new Documento(numeroDocumento, new TipoDocumento("DN")));
		
		List<Pago> pagos = pagoDAO.cancelacionesDeUnPeriodo(null,null,persona,null,null,null); 
		Iterator<Pago> pagosIt = pagos.iterator();
		Integer i=0;
		while(pagosIt.hasNext()){
			Pago pago = pagosIt.next();
			System.out.println("PAGO : "+pago.getPersona().getId() + " - "  + pago.getPersona().getNombreCompleto() + " -Grupo: " + pago.getGrupo().getId() + " - " + pago.getGrupo().getDescripcion());
			i++;
		}
		System.out.println("I="+i);
		
		//if()
		
		*/
		/*
		Grupo grupoTest = FactoryDAO.grupoDAO.findByLong("4");
		PagoDAO pagoDAO = new PagoDAO();
		List<Pago> pagos = pagoDAO.deudasGrupos(grupoTest);
		Iterator<Pago> pagosIt = pagos.iterator();
		while(pagosIt.hasNext()){
			Pago pago = pagosIt.next();
			//System.out.println("PAGO : "+pago.);
		}
		
		
		Carrera carreraTest = FactoryDAO.carreraDAO.findByLong("4");
		InscripcionDAO inscripcionDAO = new InscripcionDAO();
		List<Inscripcion> inscripciones = inscripcionDAO.personasPorGrupo(carreraTest);
		
		Iterator<Inscripcion> InscripcionIt = inscripciones.iterator();
		while(InscripcionIt.hasNext()){
			Inscripcion ins = InscripcionIt.next();
			System.out.println(ins.getPersona().getNombreCompleto());
		}
		*/
		/*
		Persona persona = new Persona();
		
		TipoDocumento tipo = new TipoDocumento("DN");
		String numeroDocumento = "32994753";
		Documento documento = new Documento(numeroDocumento,tipo);
		
		persona = FactoryDAO.personaDAO.buscarPorDocumento(documento);
		
		System.out.println("Persona: "+persona.getDocumento().getNumero());
		*/
		
		//pruebaReporte();
		
		//pruebaReporteCompleto();
	}

}
