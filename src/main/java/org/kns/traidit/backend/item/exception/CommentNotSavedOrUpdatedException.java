package org.kns.traidit.backend.item.exception;
/**
* Created by     : Soujanya
* Created Date	  : June 19th,2014
* file Name	  : UserNotFoundException.java
* Purpose		  : TO Handle Exception cases in which comment is not saved or updated	
* 
* 
* 
*/

public class CommentNotSavedOrUpdatedException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		
		return "Comment Not Saved Or Updated";
	}

}
