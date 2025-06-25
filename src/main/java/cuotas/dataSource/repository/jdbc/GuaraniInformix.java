package cuotas.dataSource.repository.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cuotas.dataSource.repository.hibernate.FactoryDAO;
import cuotas.excepcion.ErrorSincronizacion;
import cuotas.model.domain.interfaces.Guarani;
import utilitarios.Datos;
import utilitarios.XMLReaderParametros;

public class GuaraniInformix {
	
	private String base;
	private String carrera;
	private String anio;
	private Connection conn = null;
	
	public GuaraniInformix(String base, String carrera, String anio){
		this.carrera = carrera;
		this.base = base;
		this.anio = anio;
	}
	public void conectar() throws ClassNotFoundException, SQLException{	
		Datos d = XMLReaderParametros.buscar(base);	
		Class.forName("com.informix.jdbc.IfxDriver");  
		System.out.println("jdbc:informix-sqli://"+d.getIp()+":"+d.getPuerto()+"/"+d.getBase()+":INFORMIXSERVER="+d.getTipo()+";user="+d.getUsuario()+";password="+d.getPassword());
        conn = DriverManager.getConnection("jdbc:informix-sqli://"+d.getIp()+":"+d.getPuerto()+"/"+d.getBase()+":INFORMIXSERVER="+d.getTipo()+";user="+d.getUsuario()+";password="+d.getPassword());       
	}
		
	public void desconectar () {
		try{
			System.out.println("Entro a desconectar");
			if(conn!=null){
				conn.close();	
			}			
		}catch(SQLException e){
			
		}
	}
	
	public void marcarPago (String legajo, String pago, String carrera) throws SQLException{		
		System.out.println("execute procedure sp839_pago_cuota('"+legajo+"','UNLA', "+carrera+" , '"+pago+"');");
		CallableStatement cstmt = conn.prepareCall("{call sp839_pago_cuota(?,?,?,?)}");		
		cstmt.setString(1, legajo);
		cstmt.setString(2, "UNLA");
		cstmt.setInt(3,  Integer.parseInt(carrera));
		cstmt.setString(4, pago);
		cstmt.executeUpdate(); 
		cstmt.close();	
		
	}
	
	public int cargarAuxiliar() throws SQLException, ClassNotFoundException, ErrorSincronizacion {
		FactoryDAO.guaraniDAO.deleteAll();
	    int rc;
	    String cmd = null;
	    PreparedStatement pstmt = null;
	    int i = 0;
	    if(this.base.equals("posgrado")||this.base.equals("grado")){
	    	pstmt = this.consultaGradoYPosgrado();
	    	i = insertar(this.base,pstmt);	    
		}
	    if(this.base.equals("ingreso")){
	    	pstmt = this.consultaIngreso();
	    	i = i + insertar("ingreso",pstmt);
		}
		return i;
    }
	private int insertar(String cual, PreparedStatement pstmt) throws ErrorSincronizacion{
		int i = 0;
		ErrorSincronizacion err = null;
		try{
			System.out.println("Entra a ejecutar la consulta");
			ResultSet r = pstmt.executeQuery();
			int in = 0;
			Guarani guarani = null;
			FactoryDAO.guaraniDAO.abrirTransaccion();
			System.out.println("Comienzo a repetir por lo que encuentro");
			String tipo_doc = "";
			
			while(r.next()){
				try{
					if(cual.equals("ingreso")){
						guarani = new Guarani(r.getString("ua"),r.getString("n_inscripcion"),r.getString("legajo"),r.getString("tipo_documento"),
								r.getString("nro_documento"),r.getString("apellido"),r.getString("nombres"),r.getString("calle"),
								r.getString("numero"),r.getString("piso"),r.getString("dpto"),r.getString("cp"),r.getString("telefono"),
								r.getString("partido"),r.getString("provincia"),r.getString("pais"),r.getString("carrera"),
								r.getDate("fecha_inscripcion"),null,r.getInt("cohorte"),r.getString("regular"),r.getString("calidad"),cual,r.getString("email"));							
					}else{						
						tipo_doc = "DNI";
						if(r.getString("tipo_documento")!=null){
							tipo_doc = r.getString("tipo_documento");
						}
						guarani = new Guarani(r.getString("ua"),r.getString("n_inscripcion"),r.getString("legajo"),tipo_doc,
							r.getString("nro_documento"),r.getString("apellido"),r.getString("nombres"),r.getString("calle"),
							r.getString("numero"),r.getString("piso"),r.getString("dpto"),r.getString("cp"),r.getString("telefono"),
							r.getString("partido"),r.getString("provincia"),r.getString("pais"),r.getString("carrera"),
							r.getDate("fecha_inscripcion"),r.getDate("fecha_ingreso"),r.getInt("cohorte"),r.getString("regular"),r.getString("calidad"),cual,r.getString("email"));
					}
					System.out.println(r.getString("email"));
					int a = 0;					
					if ( i % 20 == 0 ) {
						FactoryDAO.guaraniDAO.batch_save(guarani,true);
					}else{
						FactoryDAO.guaraniDAO.batch_save(guarani,false);
					}	
					i++;
				}catch (Exception e){
			
					err =  new ErrorSincronizacion("Hay una persona que genera conflicto en la importaci�n, con DNI: "+guarani.getNro_documento()+". Verifique las personas, y repita el proceso de importaci�n.");
					
				}
			}
			FactoryDAO.guaraniDAO.cerrarTransaccion();
			r.close();
			pstmt.close();
						
		}catch (SQLException e){
			System.out.println("Salto la excepcion");
			e.printStackTrace();
			
		}finally{
			desconectar();
			if(err !=null){
				throw err;
			}
		}
		return i;
	}
	private PreparedStatement consultaGradoYPosgrado() throws ClassNotFoundException, SQLException{
		conectar();
		String filtro_anio = "";
		if(anio!=""){
			filtro_anio = " AND sp839_cohorte(al.fecha_ingreso) = "+anio;
		}
		String consulta = "SELECT DISTINCT  p.unidad_academica AS ua,"+
				"	p.nro_inscripcion AS n_inscripcion,"+
				"	al.legajo AS legajo,"+
				"	td.desc_abreviada AS tipo_documento,"+
				"	p.nro_documento AS nro_documento,"+
				"	p.apellido AS apellido,"+
				"	p.nombres AS nombres, "+
				"	d.calle_per_lect AS calle,"+
				"	d.numero_per_lect AS numero,"+
				"	d.piso_per_lect AS piso,"+
				"	d.dpto_per_lect AS dpto,"+
				"	d.cp_per_lect AS cp,"+
				"	d.te_per_lect AS telefono,"+
				"   d.e_mail as email, "+
				"	par.nombre AS partido, "+
				"	pcia.nombre AS provincia,"+
				"	pai.nombre AS pais,"+
				"	a.carrera AS carrera, "+
				"	a.fecha_inscripcion AS fecha_inscripcion, "+
				"	al.fecha_ingreso AS fecha_ingreso, "+
				"   sp839_cohorte(al.fecha_ingreso) as cohorte,"+
				"   al.regular as regular, "+
			    "   al.calidad as calidad "+			    
				" FROM sga_personas p, sga_datos_censales d, sga_carrera_aspira a, sga_alumnos al, "+
				"     OUTER(mdp_tipo_documento td, mug_localidades loc, mug_dptos_partidos par, mug_provincias pcia,  mug_paises pai)"+
				" WHERE p.unidad_academica = d.unidad_academica"+
				"	AND p.nro_inscripcion = d.nro_inscripcion"+
				"	AND p.unidad_academica = a.unidad_academica"+
				"	AND p.nro_inscripcion = a.nro_inscripcion"+
				"	AND a.unidad_academica = al.unidad_academica"+
				"	AND a.nro_inscripcion = al.nro_inscripcion"+
				"	AND a.carrera = al.carrera"+
				"	AND d.fecha_relevamiento = (SELECT MAX(dc1.fecha_relevamiento) FROM sga_datos_censales dc1 "+
				"				WHERE dc1.nro_inscripcion = d.nro_inscripcion "+
				"					AND dc1.unidad_academica = d.unidad_academica)	"+
				"   AND td.tipo_documento = p.tipo_documento"+
				"	AND d.loc_per_lect = loc.localidad"+
				"	AND loc.dpto_partido = par.dpto_partido  "+
				"	AND par.provincia = pcia.provincia "+
				"	AND pcia.pais = pai.pais"+
				"   AND al.legajo != '' "+filtro_anio;
		if(!carrera.equals("")){
			consulta = consulta + " AND a.carrera in ('"+this.carrera+"');";
		}
		System.out.println(consulta);
		return conn.prepareStatement(consulta);
	}
	
	//Ver la consulta, creo que falta ver que el legajo sea nulo para los ingresantes.
	private PreparedStatement consultaIngreso() throws ClassNotFoundException, SQLException{
		conectar();
		String filtro_anio = "";
		if(anio!=""){
			filtro_anio = " AND sp839_cohorte(a.fecha_inscripcion) = "+anio;
		}
		String consulta = "SELECT DISTINCT  p.unidad_academica AS ua,"+
					"	p.nro_inscripcion AS n_inscripcion,"+
					"	'' AS legajo,"+
					"	td.desc_abreviada AS tipo_documento,"+
					"	p.nro_documento AS nro_documento,"+
					"	p.apellido AS apellido,"+
					"	p.nombres AS nombres, "+
					"	d.calle_per_lect AS calle,"+
					"	d.numero_per_lect AS numero,"+
					"	d.piso_per_lect AS piso,"+
					"	d.dpto_per_lect AS dpto,"+
					"	d.cp_per_lect AS cp,"+
					"	d.te_per_lect AS telefono,"+
					"   d.e_mail as email, "+
					"	par.nombre AS partido, "+
					"	pcia.nombre AS provincia,"+
					"	pai.nombre AS pais,"+
					"	a.carrera AS carrera, "+
					"	a.fecha_inscripcion AS fecha_inscripcion, "+					
					"   sp839_cohorte(a.fecha_inscripcion) as cohorte, "+
					"   'N' as regular, "+
				    "   'I' as calidad "+				    
					" FROM sga_personas p, sga_datos_censales d, sga_carrera_aspira a,"+
					"    OUTER( mdp_tipo_documento td, mug_localidades loc, mug_dptos_partidos par, mug_provincias pcia,  mug_paises pai)"+
					" WHERE p.unidad_academica = d.unidad_academica"+
					"	AND p.nro_inscripcion = d.nro_inscripcion"+
					"	AND p.unidad_academica = a.unidad_academica"+
					"	AND p.nro_inscripcion = a.nro_inscripcion"+
					"	AND d.fecha_relevamiento = (SELECT MAX(dc1.fecha_relevamiento) FROM sga_datos_censales dc1 "+
					"				WHERE dc1.nro_inscripcion = d.nro_inscripcion "+
					"					AND dc1.unidad_academica = d.unidad_academica)	"+
					"        AND td.tipo_documento = p.tipo_documento"+
					"	AND d.loc_per_lect = loc.localidad"+
					"	AND loc.dpto_partido = par.dpto_partido  "+
					"	AND par.provincia = pcia.provincia "+
					"	AND pcia.pais = pai.pais"+
					" AND a.situacion_asp = 'AC' "+filtro_anio;
		if(!carrera.equals("")){
			consulta = consulta + " AND a.carrera in ('"+this.carrera+"');";
		}
		System.out.println(consulta);
		return conn.prepareStatement(consulta);
	}
}
