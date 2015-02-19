/**
* Created by     : Soujanya
* Created Date	  : July 24,2014
* file Name	  : EmailAlreadyExistsException.java
* Purpose		  : TO Handle Exception cases in which user account is disabled by admin
* 
* 
* 
*/



package org.kns.traidit.backend.user.exception;

public class EmailAlreadyExistsException extends Exception {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public String toString() {
		
		return "This Email Already Exists, Please register with another";
	}


}
