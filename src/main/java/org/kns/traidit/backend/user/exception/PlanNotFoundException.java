/**
* Created by     : Soujanya
* Created Date	  : July 24,2014
* file Name	  : PlanNotFoundException.java
* Purpose		  : TO Handle Exception cases in which no plan exists	
* 
* 
* 
*/



package org.kns.traidit.backend.user.exception;

public class PlanNotFoundException extends Exception {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Plan Not Found";
	}

}
