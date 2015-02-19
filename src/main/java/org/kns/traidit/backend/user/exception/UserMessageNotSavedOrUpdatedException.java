package org.kns.traidit.backend.user.exception;

/**
* Created by     : Soujanya
* Created Date	  : October 1st,2014
* file Name	  : UserMessageNotSavedOrUpdatedException.java
* Purpose		  : TO Handle Exception cases in which user message is not saved or updated
* 
* 
* 
*/

public class UserMessageNotSavedOrUpdatedException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "User Message Not Saved Or Updated";
	}

}
