/**
 * @since 12-Feb-2015
 */
package org.kns.traidit.frontend.admin.service;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.admin.dao.AdminUserManagementDao;
import org.kns.traidit.backend.admin.exception.AllUserNotificationNotFoundException;
import org.kns.traidit.backend.admin.exception.AllUserNotificationNotSavedException;
import org.kns.traidit.backend.admin.model.AllUserNotification;
import org.kns.traidit.backend.notification.exception.MobileDeviceNotFoundException;
import org.kns.traidit.backend.notification.exception.NotificationNotSentException;
import org.kns.traidit.frontend.notification.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Thangaraj(KNSTEK)
 *
 */

@Transactional
@Service("adminUserManagementService")
public class AdminUserManagementServiceImpl implements AdminUserManagementService {

	private static Logger log = Logger.getLogger(AdminUserManagementServiceImpl.class);
	
	@Resource(name="adminUserManagementDao")
	private AdminUserManagementDao adminUserManagementDao;

	@Resource(name="notificationService")
	private NotificationService notificationService;
	
	/**
	 * 
	 * @return ArrayList<AllUserNotification>
	 * @throws AllUserNotificationNotFoundException
	 * @author Thangaraj(KNSTEK)
	 * @since 12-Feb-2015
	 */
	@Override
 	public ArrayList<AllUserNotification> getListOfAllUserNotifications() throws AllUserNotificationNotFoundException{
		return this.adminUserManagementDao.getListOfAllUserNotifications();
	}

	/**
	 * 
	 * @param notificationId
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @throws AllUserNotificationNotFoundException 
	 * @throws AllUserNotificationNotSavedException 
	 * @since 13-Feb-2015
	 */
	@Override
	public Boolean sendAllUserNotification(Integer notificationId) throws AllUserNotificationNotFoundException, AllUserNotificationNotSavedException{
		try {
			AllUserNotification notification = this.adminUserManagementDao.getAllUserNotificationById(notificationId);
			this.processSendAllUserNotification(notification);
			notification.setNotificationSent(true);
			notification.setNotificationSentOn(new Date());
			this.adminUserManagementDao.saveOrUpdateAllUserNotification(notification);
		} catch (AllUserNotificationNotFoundException e) {
			throw new AllUserNotificationNotFoundException();
		}
		return true;
	}
	
	/**
	 * 
	 * @param notification
	 * @return Boolean
	 * @author Thangaraj(KNSTEK)
	 * @since 13-Feb-2015
	 */
	private Boolean processSendAllUserNotification(AllUserNotification notification){
		try {
			this.notificationService.sendNotification(413, "hi");
		} catch (NotificationNotSentException e) {
			System.out.println("NotificationNotSentException");
		} catch (MobileDeviceNotFoundException e) {
			System.out.println("MobileDeviceNotFoundException");
		}
		return null;
	}
	
	/**
	 * 
	 * @param notificationMessage
	 * @return Integer
	 * @author Thangaraj(KNSTEK)
	 * @throws AllUserNotificationNotSavedException 
	 * @since 13-Feb-2015
	 */
	@Override
	public Integer saveAllUserNotification(String notificationMessage) throws AllUserNotificationNotSavedException{
		AllUserNotification notification = new AllUserNotification();
		notification.setNotificationMessage(notificationMessage);
		notification.setCreatedOn(new Date());
		notification.setNotificationSent(false);
		return this.adminUserManagementDao.saveOrUpdateAllUserNotification(notification);
	}
}
