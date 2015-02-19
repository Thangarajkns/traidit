/**
 * @since 14-Jan-2015
 */
package org.kns.traidit.backend.category.exception;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public class HomePageCategorySequenceNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString(){
		return "Home page category sequence not found";
	}
}
