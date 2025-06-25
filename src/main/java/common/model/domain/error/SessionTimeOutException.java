package common.model.domain.error;

public class SessionTimeOutException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SessionTimeOutException() {	
	}

	public SessionTimeOutException(String message) {
		super(message);
	}

	public SessionTimeOutException(Throwable cause) {
		super(cause);
	}

	public SessionTimeOutException(String message, Throwable cause) {
		super(message, cause);		
	}

}
