package org.kns.traidit.backend.commission.exception;
/**
* Created by     : Bhagya
* Created Date	  : Feb 03rd,2015
* file Name	  : TaxNotFoundException.java
* Purpose		  : TO Handle Exception cases in which no tax exists	
* 
* 
* 
*/

public class TaxNotFoundException extends Exception{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Tax Not Found";
	}

}
