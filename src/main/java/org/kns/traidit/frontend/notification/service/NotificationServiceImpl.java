/**
 * @since 07-Jan-2015
 */
package org.kns.traidit.frontend.notification.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.notification.dao.NotificationDao;
import org.kns.traidit.backend.notification.exception.MobileDeviceNotFoundException;
import org.kns.traidit.backend.notification.exception.NotificationNotSentException;
import org.kns.traidit.backend.notification.exception.NotificationSettingsNotSavedException;
import org.kns.traidit.backend.notification.exception.UserNotificationSettingsNotfoundException;
import org.kns.traidit.backend.notification.model.MobileDevice;
import org.kns.traidit.backend.notification.model.UserNotificationSettings;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

/**
 * @author Thangaraj(KNSTEK)
 *
 */

@Service("notificationService")
@Transactional
public class NotificationServiceImpl implements NotificationService {
	
	private static Logger log = Logger.getLogger(NotificationServiceImpl.class);

	private static ApnsService service;
	
	@Resource(name = "notificationDao")
	private NotificationDao notificationDao;
	
	private void getPushNotificationService() throws IOException{
		log.info("inside getPushNotificationService");
		Properties properties=new Properties();
		InputStream pushProperties=this.getClass().getClassLoader().getResourceAsStream("push.properties");
		properties.load(pushProperties);
		String passPhrase=properties.getProperty("passphrase");
		String enviroment=properties.getProperty("enviroment");
		InputStream certFile=this.getClass().getClassLoader().getResourceAsStream(properties.getProperty("certfile"));
		if(enviroment.equals("sandbox")){
			service=APNS
					.newService()
					.withCert(certFile, passPhrase)
					.withSandboxDestination()
					.build();
		}
		else if(enviroment.equals("production")){
			service=APNS
					.newService()
					.withCert(certFile, passPhrase)
					.withProductionDestination()
					.build();
		}
	}
	
	/**
	 * sends the given message as a push notification to the user's currently logged in device 
	 * 
	 * @param userId
	 * @param message
	 * @throws NotificationNotSentException
	 * @throws MobileDeviceNotFoundException
	 * @return void
	 * @author Thangaraj(KNSTEK)
	 * @since 07-Jan-2015
	 */
	public void sendNotification(Integer userId,String message) throws NotificationNotSentException, MobileDeviceNotFoundException{
		log.info("sending notification {" + message + "} to the user with id "+userId);
		System.out.println("sending notification {" + message + "} to the user with id "+userId);
		//build the connection to Apple server if it is not existing
		if(service == null){
			try {
				getPushNotificationService();
			} catch (IOException e) {
				log.error("Could not build a connection with APNS server");
				throw new NotificationNotSentException();
			}
		}
		//get the target device token
		MobileDevice device = this.notificationDao.getMobileDeviceByUser(new TraidItUser(userId));
		
		//build payload(A JSON string consisting of all notification data in specified format and send it
		String payload = APNS.newPayload().alertBody(message).sound("Glass.aiff").actionKey("Play").build();
		service.push(device.getDeviceToken(), payload);
	}
	
	/**
	 * updates or adds an entry to map user(id) with device(id)
	 * 
	 * @param userId
	 * @param deviceToken
	 * @return void
	 * @author Thangaraj(KNSTEK)
	 * @since 07-Jan-2015
	 */
	public void updateDeviceInfo(Integer userId,String deviceToken){
		MobileDevice device;
		try {
			//check for device entry;if found update user data
			device = this.notificationDao.getMobileDeviceByDeviceToken(deviceToken);
			device.setUser(new TraidItUser(userId));
			device.setLastUpdatedOn(new Date());
			this.notificationDao.saveOrUpdateMobileDevice(device);
		} catch (MobileDeviceNotFoundException e) {
			try {
				//check for user entry ; if found update device data
				device = this.notificationDao.getMobileDeviceByUser(new TraidItUser(userId));
				device.setDeviceToken(deviceToken);
				device.setLastUpdatedOn(new Date());
				this.notificationDao.saveOrUpdateMobileDevice(device);
			} catch (MobileDeviceNotFoundException e1) {
				//if both not found, create new entry
				device = new MobileDevice();
				device.setUser(new TraidItUser(userId));
				device.setDeviceToken(deviceToken);
				device.setCreatedOn(new Date());
				device.setLastUpdatedOn(new Date());
				this.notificationDao.saveOrUpdateMobileDevice(device);
			}
		}
	}
	
	/**
	 * 
	 * 
	 * @return void
	 * @author Thangaraj(KNSTEK)
	 * @since 07-Jan-2015
	 */
	@Override
	public void removeInActiveDevicesFromDB(){
		//build the connection to Apple server if it is not existing
		if(service == null){
			try {
				getPushNotificationService();
			} catch (IOException e) {
				log.error("Could not build a connection with APNS server");
			}
		}
		Map<String, Date> devices=service.getInactiveDevices();
		System.out.println(devices.size());
		for(Entry<String, Date> device: devices.entrySet()){
			System.out.println(device.getKey()+device.getValue());
		}
	}

	/**
	 * 
	 * @param userId
	 * @param sound
	 * @param soundFor
	 * @param notificationSoundOn
	 * @throws NotificationSettingsNotSavedException
	 * @return void
	 * @author Thangaraj(KNSTEK)
	 * @since 08-Jan-2015
	 */
	@Override
	public void updateNotificationSettings(Integer userId,String sound, String soundFor,Boolean notificationSoundOn) throws NotificationSettingsNotSavedException{
		UserNotificationSettings notificationSettings;
		try {
			notificationSettings = this.notificationDao.getUserNotificationSettingsByUserId(userId);
		} catch (UserNotificationSettingsNotfoundException e) {
			notificationSettings = new UserNotificationSettings();
			notificationSettings.setUser(new TraidItUser(userId));
		}
		if(soundFor != null){
			if(soundFor.equals("reject"))
				notificationSettings.setTradeRejectSound(sound);
			else if(soundFor.equals("accept"))
				notificationSettings.setTradeAcceptSound(sound);
			else if(soundFor.equals("offer"))
				notificationSettings.setTradeOfferSound(sound);
			else if(soundFor.equals("message"))
				notificationSettings.setMessageSound(sound);
			
		}
		if(notificationSoundOn != null)
			notificationSettings.setNotificationSoundOn(notificationSoundOn);
		this.notificationDao.saveOrUpdateNotificationSettings(notificationSettings);
	}
	
	/**
	 * 
	 * @param userId
	 * @throws NotificationSettingsNotSavedException
	 * @return void
	 * @author Thangaraj(KNSTEK)
	 * @since 09-Jan-2015
	 */
	@Override
	public void updateNotificationSettings(Integer userId) throws NotificationSettingsNotSavedException{
		UserNotificationSettings notificationSettings;
		try {
			notificationSettings = this.notificationDao.getUserNotificationSettingsByUserId(userId);
		} catch (UserNotificationSettingsNotfoundException e) {
			notificationSettings = new UserNotificationSettings();
			notificationSettings.setUser(new TraidItUser(userId));
		}
		notificationSettings.setTradeRejectSound("");
		notificationSettings.setTradeAcceptSound("");
		notificationSettings.setTradeOfferSound("");
		notificationSettings.setMessageSound("");
		notificationSettings.setNotificationSoundOn(true);
		this.notificationDao.saveOrUpdateNotificationSettings(notificationSettings);
	}

	/**
	 * 
	 */
	public void sendNotificationToAll(String message) {
		
	}
}

