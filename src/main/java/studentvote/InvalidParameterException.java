package studentvote;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Invalid Parameter exception class
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Parameter")
public class InvalidParameterException extends RuntimeException{
	
	private static final long serialVersionUID = 8255246855662501732L;

	/*
	 * Constructor
	 */
	
	public InvalidParameterException(){
	}
	
	/*
	 * Constructor
	 */
	
	public InvalidParameterException(Exception ee){
		super(ee);
		ee.printStackTrace();
	}

}
