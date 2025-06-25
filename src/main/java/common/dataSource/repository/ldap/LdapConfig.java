package common.dataSource.repository.ldap;
import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Singleton que permite obtener los parametros de configuracion para validacion en LDAP
 * @author pmaseda
 *
 */
public class LdapConfig extends DefaultHandler {

	   private String contextFactory = "";
	   private String url = "";
	   private String securityAuth = "";
	   private String contextName = "";
	   private String securityUser = "";
	   private String securityPassword = "";
	   private String filterObject = "";
	   private String attributeName = "";
	   private String attributeUserName = "";
	   private String attributePassword = "";	   
	   private String attributeMail = "";
	   
	   static Logger logger = Logger.getLogger(LdapConfig.class);
	   private static LdapConfig instance;
	   // Buffer for collecting data from // the "characters" SAX event.
	   private CharArrayWriter contents = new CharArrayWriter();
	   private boolean readAtributosAlternativos;
	   private String keyAtributosAlternativos;
	   private Properties atributosAlternativos;

	   
	   private LdapConfig(){
		   this.contextFactory="com.sun.jndi.ldap.LdapCtxFactory";
		   this.url="ldap://ldap.unla.edu.ar:389";
		   this.securityAuth="simple";
		   this.contextName="ou=qmail,dc=unla,dc=edu,dc=ar";
		   this.securityUser="";
		   this.securityPassword="";
		   this.filterObject="*";
		   this.attributeName="cn";
		   this.attributeUserName="uid";
		   this.attributeMail="mail";
		   this.attributePassword="userPassword";
		      
		   this.readAtributosAlternativos=false;
		   this.keyAtributosAlternativos="";
		   
		   
	   }

	   public void startElement( String namespaceURI,
	              String localName,
	              String qName,
	              Attributes attributes ) throws SAXException {		   
		   
		   if ( localName.equals( "attr-alts" ) ){
		    	  this.readAtributosAlternativos = true;		    	  
		   }else if(localName.equals( "attr-alt" )){
			   for (int i = 0; i < attributes.getLength(); i++) {
	    	        if (attributes.getQName(i).equalsIgnoreCase("id")) {		    	          
	    	          this.keyAtributosAlternativos=attributes.getValue(i);
	    	        }
	    	  }
		   }else{
			   contents.reset();
		   }

	   }

	   public void endElement( String namespaceURI,
	              String localName,
	              String qName ) throws SAXException {
		   
	      if ( localName.equals( "context-factory" ) )  contextFactory = contents.toString();
	      else if ( localName.equals( "url" ) ) url = contents.toString();
	      else if ( localName.equals( "security-auth" ) ) securityAuth = contents.toString();
	      else if ( localName.equals( "context-name" ) ) contextName = contents.toString();
	      else if ( localName.equals( "security-user" ) ) securityUser = contents.toString();
	      else if ( localName.equals( "security-password" ) ) securityPassword = contents.toString();
	      else if ( localName.equals( "filter-object" ) ) filterObject = contents.toString();
	      else if ( localName.equals( "attr-name" ) ) attributeName = contents.toString();
	      else if ( localName.equals( "attr-mail" ) ) attributeMail = contents.toString();
	      else if ( localName.equals( "attr-password" ) ) attributePassword = contents.toString();	      
	      else if ( localName.equals( "attr-username" ) ) attributeUserName = contents.toString();
	      else if ( localName.equals( "attr-alts" ) ){
	    	  this.readAtributosAlternativos = false;	    	  
	      }else if(localName.equals( "attr-alt" )){
	    	  this.keyAtributosAlternativos="";
	      }	      

	   }


	   public void characters( char[] ch, int start, int length ) throws SAXException {
		   
		   String chars = new String(ch, start, length).trim();
		    if (chars.length() == 0) {
		      return;
		    }
		   if(this.readAtributosAlternativos){
			   if(this.atributosAlternativos==null)
				   this.atributosAlternativos = new Properties();
			   this.atributosAlternativos.put(this.keyAtributosAlternativos, chars);
			 //System.out.println("Alternativo con id "+this.keyAtributosAlternativos+" y valor "+chars);  
		   }else{
			   contents.write( ch, start, length );
		   }
	   }

	public void parseXML(String path){
		
		File file = new File(path);
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			System.out.println("Root element " + doc.getDocumentElement().getNodeName());
			  NodeList nodeLst = doc.getElementsByTagName("employee");
			  System.out.println("Information of all employees");

			  for (int s = 0; s < nodeLst.getLength(); s++) {

			    Node fstNode = nodeLst.item(s);
			    
			    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
			  
			           Element fstElmnt = (Element) fstNode;
			      NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("firstname");
			      Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
			      NodeList fstNm = fstNmElmnt.getChildNodes();
			      System.out.println("First Name : "  + ((Node) fstNm.item(0)).getNodeValue());
			      NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("lastname");
			      Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
			      NodeList lstNm = lstNmElmnt.getChildNodes();
			      System.out.println("Last Name : " + ((Node) lstNm.item(0)).getNodeValue());
			    }

			  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		
	}
		
	public static LdapConfig getInstance(String file){
		 if(LdapConfig.instance != null ) return LdapConfig.instance;
		          
         instance = new LdapConfig();
		 try {
			 // Create SAX 2 parser...
	         XMLReader xr = XMLReaderFactory.createXMLReader();
	         // Set the ContentHandler...
	         xr.setContentHandler( instance );
	         // Parse the file...	         
	         xr.parse( new InputSource(new FileReader( file )) );	         
	         logger.info("Archivo "+file+" parseado correctamente.");
	      }catch (Exception e ) {	         
	    	  logger.error("Hubo un error al parsear el archivo "+file+" se usará la configuracion LDAP por defecto.");	    	 
	      }
	      return instance;
		
	}

	public String getContextFactory() {
		return contextFactory;
	}

	public void setContextFactory(String contextFactory) {
		this.contextFactory = contextFactory;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSecurityAuth() {
		return securityAuth;
	}

	public void setSecurityAuth(String securityAuth) {
		this.securityAuth = securityAuth;
	}

	public String getContextName() {
		return contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public String getSecurityUser() {
		return securityUser;
	}

	public void setSecurityUser(String securityUser) {
		this.securityUser = securityUser;
	}

	public String getSecurityPassword() {
		return securityPassword;
	}

	public void setSecurityPassword(String securityPassword) {
		this.securityPassword = securityPassword;
	}

	public String getFilterObject() {
		return filterObject;
	}

	public void setFilterObject(String filterObject) {
		this.filterObject = filterObject;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributePassword() {
		return attributePassword;
	}

	public void setAttributePassword(String attributePassword) {
		this.attributePassword = attributePassword;
	}

	public CharArrayWriter getContents() {
		return contents;
	}

	public void setContents(CharArrayWriter contents) {
		this.contents = contents;
	}

	public String getAttributeMail() {
		return attributeMail;
	}

	public void setAttributeMail(String attributeMail) {
		this.attributeMail = attributeMail;
	}

	public String getAttributeUserName() {
		return attributeUserName;
	}

	public void setAttributeUserName(String attributeUserName) {
		this.attributeUserName = attributeUserName;
	}

	public Properties getAtributosAlternativos() {
		return atributosAlternativos;
	}

	public void setAtributosAlternativos(Properties atributosAlternativos) {
		this.atributosAlternativos = atributosAlternativos;
	}

	
}
