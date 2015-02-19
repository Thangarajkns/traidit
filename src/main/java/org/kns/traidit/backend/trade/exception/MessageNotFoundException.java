package org.kns.traidit.backend.trade.exception;
/**
* Created by     : Soujanya
* Created Date	  : June 20th,2014
* file Name	  : UserNotFoundException.java
* Purpose		  : TO Handle Exception cases in which no trade message exists	
* 
* 
* 
*/

public class MessageNotFoundException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Message Not Found";
	}

}
