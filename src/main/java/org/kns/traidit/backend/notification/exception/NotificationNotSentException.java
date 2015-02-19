/**
 * @since 07-Jan-2015
 */
package org.kns.traidit.backend.notification.exception;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public class NotificationNotSentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString(){
		return "Could not send Notification";
	}

}
