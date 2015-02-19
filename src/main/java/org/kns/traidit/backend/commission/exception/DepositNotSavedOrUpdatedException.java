package org.kns.traidit.backend.commission.exception;

/**
* Created by     : Bhagya
* Created Date	  : Jan 30th,2015
* file Name	  :  DepositNotSavedOrUpdatedException
* Purpose		  : TO Handle Exception cases in which deposit is not saved or updated 	
* 
* 
* 
*/

public class DepositNotSavedOrUpdatedException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Deposit Not Saved Or Updated";
	}


}
