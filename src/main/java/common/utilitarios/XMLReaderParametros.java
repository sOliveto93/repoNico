package common.utilitarios;
import java.io.File;
import java.util.Vector;

import javax.xml.parsers.*;
import org.w3c.dom.*;

public class XMLReaderParametros {

	public static Datos buscar(String nombre){		
		Datos d = null;
		try {
			  File file = new File("parametros.xml");
			  System.out.println(file.getAbsolutePath());
			  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			  DocumentBuilder db = dbf.newDocumentBuilder();
			  Document doc = db.parse(file);
			  doc.getDocumentElement().normalize();			  
			  NodeList nodeLst = doc.getElementsByTagName("servidor");
			  for (int s = 0; s < nodeLst.getLength(); s++) {
				    Node fstNode = nodeLst.item(s);				    
				    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {  
				    	String e = ((Node)(((Element)((Element) fstNode).getElementsByTagName("nombre").item(0)).getChildNodes()).item(0)).getNodeValue();
				        if(e.equals(nombre)){
				        	d = new Datos(((Node)(((Element)((Element) fstNode).getElementsByTagName("nombre").item(0)).getChildNodes()).item(0)).getNodeValue(),
				        			((Node)(((Element)((Element) fstNode).getElementsByTagName("ip").item(0)).getChildNodes()).item(0)).getNodeValue(),
				        			((Node)(((Element)((Element) fstNode).getElementsByTagName("puerto").item(0)).getChildNodes()).item(0)).getNodeValue(),
				        			((Node)(((Element)((Element) fstNode).getElementsByTagName("usuario").item(0)).getChildNodes()).item(0)).getNodeValue(),
				        			((Node)(((Element)((Element) fstNode).getElementsByTagName("password").item(0)).getChildNodes()).item(0)).getNodeValue(),
				        			((Node)(((Element)((Element) fstNode).getElementsByTagName("bd").item(0)).getChildNodes()).item(0)).getNodeValue(),
				        			((Node)(((Element)((Element) fstNode).getElementsByTagName("tipo").item(0)).getChildNodes()).item(0)).getNodeValue());
				        }
				    }
			  }
	    } catch (Exception e) {
	          e.printStackTrace();
	    }
	    return d;
	}
}