package org.kns.traidit.backend.user.exception;

/**
* Created by     : Soujanya
* Created Date	  : sep 2,2014
* file Name	  : DistanceNotSavedOrUpdatedException.java
* Purpose		  : TO Handle Exception cases in which no distance is not saved or updated in database
* between tow zip codes	
* 
* 
* 
*/

public class DistanceNotSavedOrUpdatedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Distance Not Saved Or Updated";
	}
}
