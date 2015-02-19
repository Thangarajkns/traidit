/**
 * @since 07-Jan-2015
 */
package org.kns.traidit.backend.notification.dao;

import org.kns.traidit.backend.notification.exception.MobileDeviceNotFoundException;
import org.kns.traidit.backend.notification.exception.NotificationNotSentException;
import org.kns.traidit.backend.notification.exception.NotificationSettingsNotSavedException;
import org.kns.traidit.backend.notification.exception.UserNotificationSettingsNotfoundException;
import org.kns.traidit.backend.notification.model.MobileDevice;
import org.kns.traidit.backend.notification.model.UserNotificationSettings;
import org.kns.traidit.backend.user.model.TraidItUser;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public interface NotificationDao {
	public Integer saveOrUpdateMobileDevice(MobileDevice device);
	public Integer deleteMobileDevice(MobileDevice device);
	public MobileDevice getMobileDeviceById(Integer deviceId) throws MobileDeviceNotFoundException;
	public MobileDevice getMobileDeviceByDeviceToken(String deviceToken) throws MobileDeviceNotFoundException;
	public MobileDevice getMobileDeviceByUser(TraidItUser user) throws MobileDeviceNotFoundException;
	public Integer saveOrUpdateNotificationSettings(UserNotificationSettings notificationSettings) throws NotificationSettingsNotSavedException;
	public UserNotificationSettings getUserNotificationSettingsByUserId(Integer userId) throws UserNotificationSettingsNotfoundException;
}
