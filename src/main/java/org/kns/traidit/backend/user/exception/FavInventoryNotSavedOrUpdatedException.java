package org.kns.traidit.backend.user.exception;


/**
* Created by     : Soujanya
* Created Date	  : June 23,2014
* file Name	  : FavInventoryNotSavedOrUpdatedException.java
* Purpose		  : TO Handle Exception cases in which fav inventory is not saved or updated	
* 
* 
* 
*/

public class FavInventoryNotSavedOrUpdatedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Favourite Inventory Not Saved or Updated";

}
}