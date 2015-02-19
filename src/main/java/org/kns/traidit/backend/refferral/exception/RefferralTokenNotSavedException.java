/**
 * @since 21-Jan-2015
 */
package org.kns.traidit.backend.refferral.exception;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public class RefferralTokenNotSavedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString(){
		return "could not save Refferral Token";
	}
}
