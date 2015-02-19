package org.kns.traidit.backend.user.exception;

/**
* Created by     : Soujanya
* Created Date	  : October 1st,2014
* file Name	  : UserMessageNotFoundException.java
* Purpose		  : TO Handle Exception cases in which user message is not found in database
* 
* 
* 
*/

public class UserMessageNotFoundException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "User Message Not Found";
	}


}
