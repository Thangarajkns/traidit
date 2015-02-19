package org.kns.traidit.backend.user.exception;

/**
* Created by     : Soujanya
* Created Date	  : July 2nd,2014
* file Name	  : CredentialsExpiredException.java
* Purpose		  : TO Handle Exception cases in user account credentials are expired
* 
* 
* 
*/

public class CredentialsExpiredException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Credentials are expired";
	}


}
