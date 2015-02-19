package org.kns.traidit.backend.user.exception;
/**
* Created by     : Soujanya
* Created Date	  : June 23,2014
* file Name	  : FavVendorNotSavedOrUpdatedException.java
* Purpose		  : TO Handle Exception cases in which no favourite Vendor is not saved or updated	
* 
* 
* 
*/

public class FavVendorNotSavedOrUpdatedException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return " Favourite vendor not saved or updated";
	}


}
