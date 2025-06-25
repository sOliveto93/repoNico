package common.model.domain.mail;

import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.io.*;

/**
 * Singleton que permite obtener los parametros de configuracion para el envio de mails por SMTP
 * @author pmaseda
 *
 */
public class SmtpConfig extends DefaultHandler {

	   private String smtpServer = "";
	   private String smtpPort = "";
	   private String smtpAuth = "";
	   private String smtpUser = "";
	   private String smtpPassword = "";
	   private Integer destinatarios = 0;
	   private String mailDisclaimer = "";	   
	   
	   static Logger logger = Logger.getLogger(SmtpConfig.class);

	   	   
	   private static SmtpConfig instance;

	   // Buffer for collecting data from // the "characters" SAX event.
	   private CharArrayWriter contents = new CharArrayWriter();

	   public void startElement( String namespaceURI,
	              String localName,
	              String qName,
	              Attributes attr ) throws SAXException {

	      contents.reset();

	   }

	   public void endElement( String namespaceURI,
	              String localName,
	              String qName ) throws SAXException {
		   
		   
	      if ( localName.equals( "mail-smtp-server" ) )  smtpServer = contents.toString();
	      else if ( localName.equals( "mail-smtp-port" ) ) smtpPort = contents.toString();
	      else if ( localName.equals( "mail-smtp-auth" ) ) smtpAuth = contents.toString();
	      else if ( localName.equals( "mail-smtp-user" ) ) smtpUser = contents.toString();
	      else if ( localName.equals( "mail-smtp-pass" ) ) smtpPassword = contents.toString();
	      else if ( localName.equals( "mail-destinatarios" ) ) {
	    	  try{
	    		  destinatarios = Integer.parseInt(contents.toString());	    		  
	    	  }catch(Exception e){
	    		  logger.error("Imposible obtener la cantidad de destinatarios indicada en el archivo de configuracion de mails, se usara el valor por defecto");
	    	  }
	    	   
	      }
	      else if ( localName.equals( "mail-disclaimer" ) ) mailDisclaimer = contents.toString(); 	      

	   }


	   public void characters( char[] ch, int start, int length ) throws SAXException {
	      contents.write( ch, start, length );
	   }	   


	public CharArrayWriter getContents() {
		return contents;
	}

	public void setContents(CharArrayWriter contents) {
		this.contents = contents;
	}
	
	private SmtpConfig(){
				
		this.smtpServer="mail.unla.edu.ar";
		this.smtpAuth="false";
		this.smtpUser="";
		this.smtpPassword="";
		this.smtpPort="25";
		this.destinatarios=200;		
		this.mailDisclaimer = "";
		this.mailDisclaimer = this.mailDisclaimer.concat("\n\n --------------------------------------------------------\n");
		this.mailDisclaimer = this.mailDisclaimer.concat("\nEste mensaje le fué enviado a modo informativo en base a un requerimiento que realizó en la Universidad.");
		this.mailDisclaimer = this.mailDisclaimer.concat("\nEn caso de no requerir mas esta información le solicitamos nos informe por este medio. Muchas gracias.");
	}
	
	public static SmtpConfig getInstance(String file){
		 if(SmtpConfig.instance != null ) return SmtpConfig.instance;
		 instance = new SmtpConfig();
		 
		 try {
	         // Create SAX 2 parser...
	         XMLReader xr = XMLReaderFactory.createXMLReader();
	         // Set the ContentHandler...	         
	         xr.setContentHandler( instance );
	         // Parse the file...	         
	         xr.parse( new InputSource(new FileReader( file )) );
	         logger.info("Archivo "+file+" parseado correctamente.");	         
	      }catch ( Exception e ) {
	    	  logger.error("Hubo un error al parsear el archivo "+file+" se usará la configuracion SMTP por defecto.");
	      }
	      return instance;
		
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public String getSmtpAuth() {
		return smtpAuth;
	}

	public void setSmtpAuth(String smtpAuth) {
		this.smtpAuth = smtpAuth;
	}

	public String getSmtpUser() {
		return smtpUser;
	}

	public void setSmtpUser(String smtpUser) {
		this.smtpUser = smtpUser;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	public Integer getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(Integer destinatarios) {
		this.destinatarios = destinatarios;
	}

	public String getMailDisclaimer() {
		return mailDisclaimer;
	}

	public void setMailDisclaimer(String mailDisclaimer) {
		this.mailDisclaimer = mailDisclaimer;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		SmtpConfig.logger = logger;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}
	
	public String toString(){
		String salida="";
		salida= salida.concat("SERVER: "+this.smtpServer+" - PORT: "+this.smtpPort+"\n");
		salida=salida.concat("AUTH: "+this.smtpAuth+" - USER: "+this.smtpUser+"\n");
		salida=salida.concat("DISCLAIMER: "+this.mailDisclaimer+"\n\n");
		return salida;
	}


}
