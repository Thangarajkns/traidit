package org.kns.traidit.backend.user.exception;
/**
* Created by     : Soujanya
* Created Date	  : July 2nd,2014
* file Name	  : AccountDisabledException.java
* Purpose		  : TO Handle Exception cases in which user account is disabled by admin
* 
* 
* 
*/

public class AccountDisabledException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Account Is Disabled";
	}


}
