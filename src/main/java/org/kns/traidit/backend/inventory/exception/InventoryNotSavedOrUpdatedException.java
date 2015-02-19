package org.kns.traidit.backend.inventory.exception;
/**
* Created by     : Soujanya
* Created Date	  : June 19th,2014
* file Name	  : InventoryNotSavedOrUpdatedException.java
* Purpose		  : TO Handle Exception cases in which inventory is not saved or updated
* 
* 
* 
*/

public class InventoryNotSavedOrUpdatedException extends Exception{

	
	
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Inventory Not Found";
	}
}
