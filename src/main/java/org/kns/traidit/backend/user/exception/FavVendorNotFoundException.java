package org.kns.traidit.backend.user.exception;
/**
* Created by     : Soujanya
* Created Date	  : June 23,2014
* file Name	  : FavVendorNotFoundException.java
* Purpose		  : TO Handle Exception cases in which no favourite Vendor exists	
* 
* 
* 
*/

public class FavVendorNotFoundException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return " Favourite vendor not found";
	}

}
