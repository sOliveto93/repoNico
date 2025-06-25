package common.model.domain.error;
public class ErrorParametro extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7071876498241198704L;
	private String msg;
	
	public ErrorParametro(){}
	
	public ErrorParametro(String string) {
		this.msg=string;
	}
	public void setMSG(String msg){
		this.msg = msg;
	}	
	public String getMSG(){
		return msg;
	}
	
	public String toString(){
		return "ERROR DE PARAMETRO : \\n "+ msg;
	}
}
