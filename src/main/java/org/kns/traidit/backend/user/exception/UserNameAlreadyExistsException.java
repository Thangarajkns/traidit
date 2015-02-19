/**
* Created by     : Soujanya
* Created Date	  : July 24,2014
* file Name	  : UserNameAlreadyExistsException.java
* Purpose		  : TO Handle Exception cases in which user account is disabled by admin
* 
* 
* 
*/



package org.kns.traidit.backend.user.exception;

public class UserNameAlreadyExistsException extends Exception {

	
	
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public String toString() {
		
		return "This Username Already Exists, Please register with another";
	}

}
