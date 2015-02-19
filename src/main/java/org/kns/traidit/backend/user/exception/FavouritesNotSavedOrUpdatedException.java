package org.kns.traidit.backend.user.exception;

/**
* Created by     : Soujanya
* Created Date	  : September 29,2014
* file Name	  : FavouritesNotSavedOrUpdatedException.java
* Purpose		  : TO Handle Exception cases in which no favourites are not saved or updated	
* 
* 
* 
*/

public class FavouritesNotSavedOrUpdatedException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return " Favourites not saved or updated";
	}

}
