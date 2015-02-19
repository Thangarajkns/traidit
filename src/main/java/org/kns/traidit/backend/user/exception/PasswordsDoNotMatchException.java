package org.kns.traidit.backend.user.exception;

/**
* Created by     : Soujanya
* Created Date	  : July 2nd,2014
* file Name	  : PasswordsDoNotMatchException.java
* Purpose		  : TO Handle Exception cases in which passwords do not match for login	
* 
* 
* 
*/

public class PasswordsDoNotMatchException extends Exception{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Passwords Do Not Match";

}
}