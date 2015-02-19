package org.kns.traidit.backend.user.exception;

/**
* Created by     : Soujanya
* Created Date	  : June 19th,2014
* file Name	  : UserNotFoundException.java
* Purpose		  : TO Handle Exception cases in which no user exists	
* 
* 
* 
*/
public class UserNotFoundException extends Exception{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "User Not Found";
	}

}
