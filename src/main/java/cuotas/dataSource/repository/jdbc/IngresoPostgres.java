package cuotas.dataSource.repository.jdbc;

import java.sql.Connection;


import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.excepcion.ErrorSincronizacion;
import cuotas.model.domain.interfaces.Guarani;
import cuotas.model.domain.interfaces.Persona_Auxiliar;
import utilitarios.Datos;
import utilitarios.XMLReaderParametros;

public class IngresoPostgres {
	
	//private String base;
	private Connection conn = null;
	private String carrera;
	private String anio;
	public IngresoPostgres(String carrera, String anio){
		this.carrera = carrera;
		this.anio = anio;
		//this.base = base;
	}
	public void conectar() throws ClassNotFoundException, SQLException{	
		//Datos d = XMLReaderParametros.buscar(base);		
		Class.forName("org.postgresql.Driver");		
		conn = DriverManager.getConnection("jdbc:postgresql://172.16.0.27:5434/preinscripcion27","postgres","rip*2004");       
		//conn = DriverManager.getConnection("jdbc:mysql://localhost/cursos?user=root&password=tiana3101");
	}
		
	public void desconectar () throws SQLException{
		 conn.close();
	}
	public int cargarPreinscriptos() throws SQLException, ClassNotFoundException, ErrorSincronizacion {
		FactoryDAO.guaraniDAO.deleteAll();  
	    PreparedStatement pstmt = null;	    
	    pstmt = this.consultaPreinscriptos();
		return insertar(pstmt);
    }
	private int insertar(PreparedStatement pstmt) throws ErrorSincronizacion, SQLException{
		int i = 0;
		ErrorSincronizacion err = null;
		try{
			System.out.println("Entra a ejecutar la consulta ");
			ResultSet r = pstmt.executeQuery();
			int in = 0;
			Guarani guarani = null;
			FactoryDAO.guaraniDAO.abrirTransaccion();
			System.out.println("Comienzo a repetir por lo que encuentro");
			String tipo_doc = "";
			
			while(r.next()){
				try{	
					guarani = new Guarani("","","","DN",r.getString("nro_documento"),r.getString("apellido"),r.getString("nombres"),"","","","","","","","","",r.getString("carrera"),r.getDate("fecha_registro"),null,0,"I","N","preinscrip",r.getString("e_mail"));							
					if ( i % 20 == 0 ) {
						FactoryDAO.guaraniDAO.batch_save(guarani,true);
					}else{
						FactoryDAO.guaraniDAO.batch_save(guarani,false);
					}	
					i++;
				}catch (Exception e){			
					err =  new ErrorSincronizacion("Hay una persona que genera conflicto en la importaci�n, con DNI: "+guarani.getNro_documento()+". Verifique las personas, y repita el proceso de importacion.");
					
				}
			}
			FactoryDAO.guaraniDAO.cerrarTransaccion();
			r.close();
			pstmt.close();
						
		}catch (SQLException e){
			System.out.println("Salto la excepcion");
			e.printStackTrace();
			
		}finally{
			this.desconectar();
			if(err !=null){
				throw err;
			}
		}
		return i;
	}
	private PreparedStatement consultaPreinscriptos() throws ClassNotFoundException, SQLException{
		conectar();	
		String filtro_carrera ="%%";
		if(carrera!=""){
			filtro_carrera = carrera; 
		}
		String filtro_anio = "";
		if(anio!=""){
			filtro_anio = " and fecha_registro>= '"+anio+"-01-01'";
		}
		System.out.println("La consulta es "+"SELECT distinct apellido, nombres, tipo_documento, nro_documento, carrera, e_mail, fecha_registro  FROM sga_preinscripcion where carrera like '"+filtro_carrera+"' "+filtro_anio+";");
		return conn.prepareStatement("SELECT distinct apellido, nombres, tipo_documento, nro_documento, carrera, e_mail, fecha_registro  FROM sga_preinscripcion where carrera like '"+filtro_carrera+"' "+filtro_anio+";");		
	}
	
	
}
