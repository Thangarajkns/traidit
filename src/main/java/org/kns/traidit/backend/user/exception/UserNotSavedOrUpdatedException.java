package org.kns.traidit.backend.user.exception;

/**
* Created by     : Soujanya
* Created Date	  : June 19th,2014
* file Name	  : UserNotFoundException.java
* Purpose		  : TO Handle Exception cases in which user is not saved or updated	
* 
* 
* 
*/

public class UserNotSavedOrUpdatedException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "User Not Saved or Updated";
	}


}
