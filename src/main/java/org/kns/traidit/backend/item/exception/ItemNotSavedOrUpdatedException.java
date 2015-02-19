package org.kns.traidit.backend.item.exception;

/**
* Created by     : Soujanya
* Created Date	  : June 19th,2014
* file Name	  : UserNotFoundException.java
* Purpose		  : TO Handle Exception cases in which item is not saved or updated 	
* 
* 
* 
*/

public class ItemNotSavedOrUpdatedException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Item Not Saved Or Updated";
	}


}
