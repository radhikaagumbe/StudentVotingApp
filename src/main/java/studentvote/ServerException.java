package studentvote;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Server Exception class
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerException extends Exception{
	
	private static final long serialVersionUID = -7272927424836337323L;

	/**
	 * Constructor
	 */
	public ServerException() {
		
	}
	
	/*
	 * Constructor
	 */
	
	public ServerException(Exception ee){
		super(ee);
		ee.printStackTrace();
	}

}
