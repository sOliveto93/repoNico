package common.model.domain.mail;

import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends javax.mail.Authenticator {
	String username;
	String password;
	
	public SMTPAuthenticator(String user , String pass){
		this.username = user;
		this.password = pass; 
	}
	
    public PasswordAuthentication getPasswordAuthentication() {
       return new PasswordAuthentication(username, password);
    }
    
}

