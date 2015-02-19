package org.kns.traidit.backend.trade.exception;
/**
* Created by     : Soujanya
* Created Date	  : June 20thth,2014
* file Name	  : UserNotFoundException.java
* Purpose		  : TO Handle Exception cases in which no Trade exists	
* 
* 
* 
*/
public class TradeNotFoundException extends Exception {
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Trade Not Found";
	}

}
