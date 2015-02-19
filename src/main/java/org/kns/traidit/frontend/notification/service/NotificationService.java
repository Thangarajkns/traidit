/**
 * @since 07-Jan-2015
 */
package org.kns.traidit.frontend.notification.service;

import org.kns.traidit.backend.notification.exception.MobileDeviceNotFoundException;
import org.kns.traidit.backend.notification.exception.NotificationNotSentException;
import org.kns.traidit.backend.notification.exception.NotificationSettingsNotSavedException;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public interface NotificationService {
	public void sendNotification(Integer userId,String message) throws NotificationNotSentException, MobileDeviceNotFoundException;
	public void updateDeviceInfo(Integer userId,String deviceToken);
	public void removeInActiveDevicesFromDB();
	public void updateNotificationSettings(Integer userId,String sound, String soundFor,Boolean notificationSoundOn) throws NotificationSettingsNotSavedException;
	public void updateNotificationSettings(Integer userId) throws NotificationSettingsNotSavedException;
}
