/**
 * @since 10-Dec-2014
 */
package org.kns.traidit.backend.commission.exception;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public class CommissionNotSavedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message = "Could not save the Commission";
	
	public CommissionNotSavedException() {
		super();
	}
	
	public CommissionNotSavedException(String message) {
		super();
		this.message = message;
	}


	@Override
	public String toString(){
		return message;
	}
}
