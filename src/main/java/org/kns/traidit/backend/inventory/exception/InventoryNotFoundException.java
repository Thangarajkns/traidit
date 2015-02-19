package org.kns.traidit.backend.inventory.exception;
/**
* Created by     : Soujanya
* Created Date	  : June 19th,2014
* file Name	  : UserNotFoundException.java
* Purpose		  : TO Handle Exception cases in which no supplier exists
* 
* 
* 
*/

public class InventoryNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Inventory Not Found";
	}

}
