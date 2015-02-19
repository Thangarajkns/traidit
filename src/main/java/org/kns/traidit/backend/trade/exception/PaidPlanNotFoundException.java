package org.kns.traidit.backend.trade.exception;
/**
* Created by     : Bhagya
* Created Date	  : Dec 03rd,2014
* file Name	  : PaidPlanNotFoundException.java
* Purpose		  : TO Handle Exception cases in which no Paid Plan exists	
* 
* 
* 
*/
public class PaidPlanNotFoundException extends Exception {
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "PaidPlan Not Found";
	}

}