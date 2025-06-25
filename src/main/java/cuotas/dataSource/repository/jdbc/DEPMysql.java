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
import cuotas.model.domain.datos.CarreraDep;
import cuotas.model.domain.interfaces.Guarani;
import utilitarios.Datos;
import utilitarios.XMLReaderParametros;

public class DEPMysql {
	
	//private String base;
	private Connection conn = null;
	
	public DEPMysql(){
		//this.base = base;
	}
	public void conectar() throws ClassNotFoundException, SQLException{	
		//Datos d = XMLReaderParametros.buscar(base);	
		Class.forName("com.mysql.jdbc.Driver");  
		conn = DriverManager.getConnection("jdbc:mysql://mysqldev.unla.edu.ar/cursos?user=cuotas&password=quotas*1221");       
		//conn = DriverManager.getConnection("jdbc:mysql://localhost/cursos?user=root&password=tiana3101");
	}
		
	public void desconectar () throws SQLException{
		 conn.close();
	}
	
	public int cargarCarreras(int ultimo) throws SQLException, ClassNotFoundException {		    
	    PreparedStatement pstmt = null;	    
	    pstmt = this.buscarCarreras(ultimo);		
		return insertarCarreras(pstmt);
    }
	
	public int cargarAuxiliar(String cursos, String anio) throws SQLException, ClassNotFoundException {
		FactoryDAO.guaraniDAO.deleteAll();	    
	    PreparedStatement pstmt = null;	    
	    pstmt = this.consultaDEP(cursos, anio);		
		return insertar("dep",pstmt);
    }
	private int insertar(String cual, PreparedStatement pstmt){
		int i = 0;
		try{
			System.out.println("Entra a ejecutar la consulta");
			ResultSet r = pstmt.executeQuery();
			int in = 0;
			Guarani guarani = null;
			FactoryDAO.guaraniDAO.abrirTransaccion();
			System.out.println("Comienzo a repetir por lo que encuentro");
			String estado = "P";
			String tipo_doc = "0";
			int anio_actual = new Date(System.currentTimeMillis()).getYear()+1900;
			while(r.next()){
				try{					
					estado = "P";
					if(r.getDate("fechainicio").getYear()+1900 == anio_actual){
						estado = "A";
					}
					if(r.getString("abandono").equals("1")){
						estado = "N";
					}
					tipo_doc = r.getString("tipodoc");
					if(r.getString("tipodoc").equals("")){
						tipo_doc = "0";
					}
					guarani = new Guarani("","",r.getString("idalumno"),tipo_doc,
							r.getString("documento").replaceAll("\\.", ""),r.getString("apellido"),r.getString("nombre"),r.getString("calle"),
							r.getString("nro"),r.getString("piso"),r.getString("dpto"),r.getString("cp"),r.getString("telefono"),
							r.getString("localidad"),r.getString("provincia"),r.getString("pais"),r.getString("idcurso"),
							r.getDate("fechainicio"),r.getDate("fechafin"),r.getDate("fechainicio").getYear()+1900,"S",estado,cual, r.getString("mail"));
					if ( i % 20 == 0 ) {
						FactoryDAO.guaraniDAO.batch_save(guarani,true);
					}else{
						FactoryDAO.guaraniDAO.batch_save(guarani,false);
					}	
					i++;
				}catch (common.model.domain.error.ErrorGrabacion e){
					System.out.println("Salto el error para: "+guarani.getNro_documento());
				}
			}
			FactoryDAO.guaraniDAO.cerrarTransaccion();
			r.close();
			pstmt.close();
			desconectar();			
		}catch (SQLException e){
			System.out.println("Entro en el error");
			e.printStackTrace();
		}
		return i;
	}
	private PreparedStatement consultaDEP(String cursos, String anio) throws ClassNotFoundException, SQLException{
		conectar();		
		String filtro_anio = "";
		if(anio!=""){
			filtro_anio = " and year(fechainicio)="+anio;
		}
		String query = "";
		if(cursos.equals("")){
			query = "select  distinct  alumno.idalumno, alumno.tipodoc, alumno.documento, alumno.apellido, alumno.nombre, "+ 
				    " 'SIN DEFINIR' as calle, '' as nro, '' as piso ,'' as  dpto, '' as cp , "+ 
				    " alumno.telefono as telefono, localidad.nombre as localidad, 'Buenos Aires' as provincia, 'ARGENTINA' as pais, "+
				    " cursada.idcurso as idcurso, cursada.fechainicio as fechainicio, cursada.fechafin as fechafin, 'COHORTE' as cohorte, insc.abandono as abandono, alumno.email as mail "+
				    " from cursos.cursada as cursada , cursos.cursada_alumno as insc, "+
				    " cursos.alumno as alumno, cursos.localidad as localidad where   insc.idcursada = cursada.idcursada and "+ 
				    " insc.idalumno = alumno.idalumno and alumno.idlocalidad = localidad.idlocalidad "+filtro_anio+";";
			System.out.println("la consulta DEP es: "+query );
			return conn.prepareStatement(query);
		}else{
			query = "select  distinct  alumno.idalumno, alumno.tipodoc, alumno.documento, alumno.apellido, alumno.nombre, "+ 
				    " 'SIN DEFINIR' as calle, '' as nro, '' as piso ,'' as  dpto, '' as cp , "+ 
				    " alumno.telefono as telefono, localidad.nombre as localidad, 'Buenos Aires' as provincia, 'ARGENTINA' as pais, "+
				    " cursada.idcurso as idcurso, cursada.fechainicio as fechainicio, cursada.fechafin as fechafin, 'COHORTE' as cohorte, insc.abandono as abandono, alumno.email as mail "+
				    " from cursos.cursada as cursada , cursos.cursada_alumno as insc, "+
				    " cursos.alumno as alumno, cursos.localidad as localidad where   insc.idcursada = cursada.idcursada and "+ 
				    " insc.idalumno = alumno.idalumno and alumno.idlocalidad = localidad.idlocalidad and cursada.idcurso in ("+cursos+") "+filtro_anio+";";
			System.out.println("la consulta DEP es: "+query);
			return conn.prepareStatement(query);
		}
	}
	private int insertarCarreras(PreparedStatement pstmt){
		int i = 0;
		try{
			System.out.println("Entra a ejecutar la consulta");
			ResultSet r = pstmt.executeQuery();			
			CarreraDep carreraDep = null;
			FactoryDAO.carrerasDepDAO.abrirTransaccion();
			System.out.println("Comienzo a repetir por lo que encuentro");			
			while(r.next()){
				try{				
					carreraDep = new CarreraDep(r.getInt("idcurso"),r.getString("nombre"));
					if ( i % 20 == 0 ) {
						FactoryDAO.carrerasDepDAO.batch_save(carreraDep,true);
					}else{
						FactoryDAO.carrerasDepDAO.batch_save(carreraDep,false);
					}	
					i++;
				}catch (common.model.domain.error.ErrorGrabacion e){
					System.out.println("Salto un error ");
				}
			}
			FactoryDAO.carrerasDepDAO.cerrarTransaccion();
			r.close();
			pstmt.close();
			desconectar();			
		}catch (SQLException e){
			System.out.println("Entro en el error");
			e.printStackTrace();
		}
		return i;
	}
	private PreparedStatement buscarCarreras(int ultima) throws ClassNotFoundException, SQLException{
		conectar();	
		String query ="";
		if(ultima!=0){
			query = "SELECT idcurso, nombre FROM cursos.curso where idcurso > "+ultima+";";
		}				
		System.out.println("la consulta DEP es: "+query );
		return conn.prepareStatement(query);
		
	}
	
}
