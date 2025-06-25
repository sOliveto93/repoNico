package common.model.domain.mail;

import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import common.model.domain.archivo.Archivo;

public class Mensajero {

	private String smtp;
	private Integer limiteDestinatarios;
	private String smtpPort;
	private String smtpAuth;
	private String smtpAuthUser;
	private String smtpAuthPass;
	private String mailDisclaimer;
	
	public Mensajero(SmtpConfig config){
		this.limiteDestinatarios = config.getDestinatarios();
		this.mailDisclaimer = config.getMailDisclaimer();
		this.smtp = config.getSmtpServer();
		this.smtpAuth = config.getSmtpAuth();
		this.smtpAuthUser = config.getSmtpUser();
		this.smtpAuthPass = config.getSmtpPassword();
		this.smtpPort = config.getSmtpPort();
	}
	
	public String getSmtp() {
		return smtp;
	}

	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}

	public void enviar(Mensaje m) throws MessagingException{
		
		Properties props=new Properties();
		props.put("mail.smtp.host",this.smtp);
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth",this.smtpAuth);
		props.put("mail.smtp.port", this.smtpPort);
		props.put("mail.from", m.getRemitente().getEmail() );
		
		Authenticator auth = null;
		if(this.smtpAuth.equals("true")) auth = new SMTPAuthenticator(this.smtpAuthUser,this.smtpAuthPass);		
		Session   session  =  Session.getDefaultInstance(props,auth);
		
		String cuerpo = m.getBody();
		cuerpo = cuerpo.concat(this.mailDisclaimer);
		// create and fill the first message part
		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setText(cuerpo);

		//create the Multipart and add its parts to it
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);

		Iterator<Archivo> archivos =m.getAdjuntos().iterator();
		System.out.println(m);
		// Por cada archivo obtengo el path, lo levanto y lo adjunto al msg.
		while(archivos.hasNext()){
	    	  String path = archivos.next().getPath();
	    	  FileDataSource fds = new FileDataSource(path);
	    	  //
	    	  MimeBodyPart mbp2 = new MimeBodyPart();
	    	  mbp2.setDataHandler(new DataHandler(fds));
	    	  mbp2.setFileName(fds.getName());
	    	  // attach the file to the message
	    	  mp.addBodyPart(mbp2);
	    	  	    	  
	  	}
		Iterator<DireccionMail> direcciones = m.getDestinatarios().iterator();
	    
		//Creo 1 mensaje cada n destinatarios, lo envio, y sigo creando el siguiente.
	    while(direcciones.hasNext()){
	    	  // 	Creo el mensaje 
	    	  MimeMessage msg = new MimeMessage(session);
	    	  msg.setFrom(new InternetAddress(m.getRemitente().getEmail().toString()));
	    	  msg.setSubject(m.getAsunto());	      

	    	  // 	add the Multipart to the message
	    	  msg.setContent(mp);	    	  
	    	  
	    	  // 	Pongo el header con la fecha actual
	    	  msg.setSentDate(new Date());
	    	  
	    	  //	Agrego los destinatarios
	    	  for(int cant=0;(cant<this.limiteDestinatarios && direcciones.hasNext());cant++){
	    		  DireccionMail dm = direcciones.next();
	    		  if(dm.esValido())
	    			  msg.addRecipient(Message.RecipientType.BCC,new InternetAddress(dm.getValor())); 
	    	  }    	  
	    	  
	    	  //Cada vez que llego al limite lo mando
	    	  Transport.send(msg);
	    	 //System.out.println("MEnsaje"+msg);
	    	  
	    }	    
	}

	public String getSmtpAuthUser() {
		return smtpAuthUser;
	}

	public void setSmtpAuthUser(String smtpAuthUser) {
		this.smtpAuthUser = smtpAuthUser;
	}

	public String getSmtpAuthPass() {
		return smtpAuthPass;
	}

	public void setSmtpAuthPass(String smtpAuthPass) {
		this.smtpAuthPass = smtpAuthPass;
	}
}


