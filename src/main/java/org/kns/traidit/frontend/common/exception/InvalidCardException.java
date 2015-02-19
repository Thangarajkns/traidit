/**
 * 
 */
package org.kns.traidit.frontend.common.exception;

/**
 * @author Thangaraj
 * @since 03-12-2014
 *
 */
public class InvalidCardException extends Exception {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString(){
		return "Invalid Card details";
	}
}
