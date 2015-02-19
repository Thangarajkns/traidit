package org.kns.traidit.backend.category.exception;

/**
* Created by     : Soujanya
* Created Date	  : June 19th,2014
* file Name	  : UserNotFoundException.java
* Purpose		  : TO Handle Exception cases in which category is not saved or updated	
* 
* 
* 
*/
public class CategoryNotSavedOrUpdatedException extends Exception{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Category Not Saved or Updated";
	}


}
