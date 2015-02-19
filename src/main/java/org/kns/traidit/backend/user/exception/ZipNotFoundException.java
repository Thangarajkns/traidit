package org.kns.traidit.backend.user.exception;

/**
* Created by     : Soujanya
* Created Date	  : Sept 2nd,2014
* file Name	  : ZipNotFoundException.java
* Purpose		  : TO Handle Exception cases in which zipcodes do not exist in database of distances table
* 
* 
* 
*/

public class ZipNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public String toString() {
		
		return "Zips not found";
	}
	
}
