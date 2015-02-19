package org.kns.traidit.backend.commission.exception;

/**
* Created by     : Bhagya
* Created Date	  : Feb 03rd,2015
* file Name	  :  TaxNotSavedOrUpdatedException
* Purpose		  : TO Handle Exception cases in which tax is not saved or updated 	
* 
* 
* 
*/

public class TaxNotSavedOrUpdatedException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Tax Not Saved Or Updated";
	}


}
