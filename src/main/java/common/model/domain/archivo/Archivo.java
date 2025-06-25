package common.model.domain.archivo;

import java.util.Date;

import common.model.domain.validacion.UsuarioGenerico;

public class Archivo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String nombre;
	private UsuarioGenerico owner;
	private String duenio;
	private Date fechaCreacion;
	private Long peso;	
	private String path;
	private String md5;
	
	public Archivo(){
		
	}
	public Archivo(String path){
		this.path = path;
	}
	
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
	public UsuarioGenerico getOwner() {
		return owner;
	}
	
	public void setOwner(UsuarioGenerico owner) {
		this.owner = owner;
	}
	
	public String getDuenio() {
		return duenio;
	}
	
	public void setDuenio(String duenio) {
		this.duenio = duenio;
	}
	
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	public Long getPeso() {
		return peso;
	}
	
	public void setPeso(Long peso) {
		this.peso = peso;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getMd5() {
		return md5;
	}

	/**
	 * Guardar el md5 para comprobar la existencia de un archivo identico aunque tenga otro nombre.
	 * @param md5
	 */
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	public Integer getPesoKb(){
		return Math.round(this.peso/1024);
	}
	
	public boolean equals(Object o){
		Archivo i = (Archivo)o;
		return (this.getPath().equals(i.getPath()));		
	}
	
	public int hashCode() {
		return this.getPath().hashCode();
	}
	
	public String toString() {
		return ""+this.getPath();
	}
	
}
