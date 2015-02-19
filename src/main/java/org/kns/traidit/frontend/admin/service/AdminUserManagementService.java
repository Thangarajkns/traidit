/**
 * @since 12-Feb-2015
 */
package org.kns.traidit.frontend.admin.service;

import java.util.ArrayList;

import org.kns.traidit.backend.admin.exception.AllUserNotificationNotFoundException;
import org.kns.traidit.backend.admin.exception.AllUserNotificationNotSavedException;
import org.kns.traidit.backend.admin.model.AllUserNotification;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public interface AdminUserManagementService {

	public ArrayList<AllUserNotification> getListOfAllUserNotifications() throws AllUserNotificationNotFoundException;
	
	public Boolean sendAllUserNotification(Integer notificationId) throws AllUserNotificationNotFoundException, AllUserNotificationNotSavedException;
	
	public Integer saveAllUserNotification(String notificationMessage) throws AllUserNotificationNotSavedException;
}
