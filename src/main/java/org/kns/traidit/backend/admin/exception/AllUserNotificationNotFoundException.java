/**
 * @since 12-Feb-2015
 */
package org.kns.traidit.backend.admin.exception;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public class AllUserNotificationNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString(){
		return "AllUserNotification not found";
	}
}
