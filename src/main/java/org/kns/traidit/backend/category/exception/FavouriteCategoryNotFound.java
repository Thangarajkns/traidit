package org.kns.traidit.backend.category.exception;

/**
* Created by     : Soujanya
* Created Date	  : september 16,2014
* file Name	  : FavouriteCategoryNotFound.java
* Purpose		  : TO Handle Exception cases in which no favourite category is found	
* 
* 
* 
*/

public class FavouriteCategoryNotFound extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Favourite Category Not Found";
	}
}
