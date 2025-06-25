package common.model.domain.mail;

import java.util.ArrayList;
import java.util.List;

import common.model.domain.archivo.Archivo;
import common.model.domain.validacion.UsuarioGenerico;

public class Mensaje {

	private Long id;
	private UsuarioGenerico remitente;
	private List<DireccionMail> destinatarios;
	private String body;
	private String asunto;
	private List<Archivo> adjuntos;
	private Integer limiteAdjuntos;
	
	public Mensaje(){
		
		adjuntos= new ArrayList<Archivo>();
		destinatarios= new ArrayList<DireccionMail>();
		
		// FIXME - Hardcode feos
		limiteAdjuntos=1024;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UsuarioGenerico getRemitente() {
		return remitente;
	}

	public void setRemitente(UsuarioGenerico remitente) {
		this.remitente = remitente;
	}

	public List<DireccionMail> getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(List<DireccionMail> destinatarios) {
		this.destinatarios = destinatarios;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public List<Archivo> getAdjuntos() {
		return adjuntos;
	}

	public void setAdjuntos(List<Archivo> adjuntos) {
		this.adjuntos = adjuntos;
	}
	
	public void addDestinatario(DireccionMail des){
		this.destinatarios.add(des);
	}
	
	/**
	 * Borra de los destinatarios del menasje aquel que recibe como parametro.
	 * @param des
	 */
	public void delDestinatario(String des){
		if(this.destinatarios.contains(des))this.destinatarios.remove(des);
	}
	
	/**
	 * Agrega al mensaje el archivo recibido como parametro.
	 * @param a
	 */
	public void addAdjunto(Archivo a){
		//if(this.getPesoKbAdjuntos()+a.getPesoKb() < this.limiteAdjuntos)
			this.adjuntos.add(a);
		/*else
			throw new ErrorParametro("El peso de los mensajes no puede exceder los "+limiteAdjuntos+"KB");*/
	}
	
	/**
	 * Retorna true o false dependiendo si ya se supero o no el limite de adjuntos permitido.
	 * @return
	 */
	public Boolean superaLimiteAdjuntos(){
		return (this.getPesoKbAdjuntos() > this.limiteAdjuntos);
	}
	
	/**
	 * Devuelve el peso hasta el momento de los adjuntos del mensaje.
	 * @return
	 */
	private Integer getPesoKbAdjuntos() {
		java.util.Iterator<Archivo> adj = this.adjuntos.iterator();
		Integer peso = new Integer(0);
		while(adj.hasNext()){
			peso = peso + adj.next().getPesoKb();
		}
		return peso;
	}
	
	/**
	 * Borra del mensaje el adjunto recibido como parametro.
	 * @param a
	 */
	public void delAdjunto(Archivo a){
		if(this.adjuntos.contains(a))this.adjuntos.remove(a);
	}
	public String toString(){
		String salida="";
		salida = salida.concat("From:"+this.getRemitente().getEmail()+"\nSubject:"+this.getAsunto()+"\nCuerpo:"+this.getBody()+"\n");
		salida = salida.concat("Para: "+this.getDestinatarios().toString());
		return salida;
	}
	
	/**
	 * 	Borra todos los destinatarios
	 */
	public void flushDestinatarios() {
		this.destinatarios = new ArrayList<DireccionMail>();
		
	}
	
	/**
	 * Borra todos los adjuntos del mensaje
	 */
	public void flushAdjuntos(){
		this.adjuntos =  new ArrayList<Archivo>();		
	}

	/**
	 * Devuelve en limite de tamaño permitido de adjuntos en KB
	 */
	public Integer getLimiteAdjuntos() {
		return limiteAdjuntos;
	}

	/**
	 * Setea el limite para archivos adjuntos por mensaje en KB
	 * @param limiteAdjuntos
	 */
	public void setLimiteAdjuntos(Integer limiteAdjuntos) {
		this.limiteAdjuntos = limiteAdjuntos;
	}
		
}
