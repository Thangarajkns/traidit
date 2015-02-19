package org.kns.traidit.backend.user.exception;

/**
* Created by     : Soujanya
* Created Date	  : June 23,2014
* file Name	  : FavItemNotfOUNDException.java
* Purpose		  : TO Handle Exception cases in which fav item is not Found or doesn't exist
* 
* 
* 
*/

public class FavItemNotFoundException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Favourite Item Not Found";


}
}