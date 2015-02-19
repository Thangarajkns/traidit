package org.kns.traidit.backend.commission.exception;
/**
* Created by     : Bhagya
* Created Date	  : Jan 30,2015
* file Name	  : DepositNotFoundException.java
* Purpose		  : TO Handle Exception cases in which no Deposit exists	
* 
* 
* 
*/

public class DepositNotFoundException extends Exception{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Deposit Not Found";
	}

}
