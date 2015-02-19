/**
 * @since 08-Jan-2015
 */
package org.kns.traidit.backend.notification.exception;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public class NotificationSettingsNotSavedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @return
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 08-Jan-2015
	 */
	public String toString(){
		return "Could not save Notification Settings";
	}
}
