package common.model.domain.error;
public class ErrorGrabacion extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3560462199554869620L;
	private String msg;
	public void setMSG(String msg){
		this.msg = msg;
	}	
	public String getMSG(){
		return msg;
	}
}
