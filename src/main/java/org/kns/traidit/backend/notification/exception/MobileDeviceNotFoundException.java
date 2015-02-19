/**
 * @since 07-Jan-2015
 */
package org.kns.traidit.backend.notification.exception;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public class MobileDeviceNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString(){
		return "Mobile Device not found Exception";
	}

}
