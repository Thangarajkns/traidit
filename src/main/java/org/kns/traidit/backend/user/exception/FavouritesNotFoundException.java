package org.kns.traidit.backend.user.exception;

/**
* Created by     : Soujanya
* Created Date	  : September 29,2014
* file Name	  : FavouritesNotFoundException.java
* Purpose		  : TO Handle Exception cases in which no favourites are found	
* 
* 
* 
*/

public class FavouritesNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return " Favourites Not Found";
	}


}
