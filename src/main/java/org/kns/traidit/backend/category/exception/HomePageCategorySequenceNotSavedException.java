/**
 * @since 20-Jan-2015
 */
package org.kns.traidit.backend.category.exception;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public class HomePageCategorySequenceNotSavedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString(){
		return "Could not save Home Page Category Sequence";
	}
}
