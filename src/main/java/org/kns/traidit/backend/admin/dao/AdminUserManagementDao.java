/**
 * @since 12-Feb-2015
 */
package org.kns.traidit.backend.admin.dao;

import java.util.ArrayList;

import org.kns.traidit.backend.admin.exception.AllUserNotificationNotFoundException;
import org.kns.traidit.backend.admin.exception.AllUserNotificationNotSavedException;
import org.kns.traidit.backend.admin.model.AllUserNotification;


/**
 * @author Thangaraj(KNSTEK)
 *
 */

public interface AdminUserManagementDao {

	public ArrayList<AllUserNotification> getListOfAllUserNotifications() throws AllUserNotificationNotFoundException;
	
	public AllUserNotification getAllUserNotificationById(Integer notificationId) throws AllUserNotificationNotFoundException;
	
	public Integer saveOrUpdateAllUserNotification(AllUserNotification notification) throws AllUserNotificationNotSavedException;
}
