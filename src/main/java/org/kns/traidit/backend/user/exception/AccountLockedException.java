package org.kns.traidit.backend.user.exception;

/**
* Created by     : Soujanya
* Created Date	  : July 2nd,2014
* file Name	  : AccountLockedException.java
* Purpose		  : TO Handle Exception cases in which user account is locked by admin
* 
* 
* 
*/

public class AccountLockedException extends Exception{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Account Is Locked";
	}

}
