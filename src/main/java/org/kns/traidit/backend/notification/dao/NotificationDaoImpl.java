/**
 * @since 07-Jan-2015
 */
package org.kns.traidit.backend.notification.dao;


import java.lang.reflect.Array;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.kns.traidit.backend.notification.exception.MobileDeviceNotFoundException;
import org.kns.traidit.backend.notification.exception.NotificationNotSentException;
import org.kns.traidit.backend.notification.exception.NotificationSettingsNotSavedException;
import org.kns.traidit.backend.notification.exception.UserNotificationSettingsNotfoundException;
import org.kns.traidit.backend.notification.model.MobileDevice;
import org.kns.traidit.backend.notification.model.UserNotificationSettings;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Thangaraj(KNSTEK)
 *
 */

@Transactional
@Repository("notificationDao")
public class NotificationDaoImpl implements NotificationDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	private static Logger log = Logger.getLogger(NotificationDaoImpl.class);
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	
	/**
	 * saves or updates existing given mobile device
	 * 
	 * @param device
	 * @return Integer
	 * @author Thangaraj(KNSTEK)
	 * @since 07-Jan-2015
	 */
	@Override
	public Integer saveOrUpdateMobileDevice(MobileDevice device){
		sessionFactory.getCurrentSession().saveOrUpdate(device);
		sessionFactory.getCurrentSession().flush();
		return device.getId();
	}
	
	/**
	 * deletes the given mobile object from database
	 * 
	 * @param device
	 * @return Integer
	 * @author Thangaraj(KNSTEK)
	 * @since 07-Jan-2015
	 */
	@Override
	public Integer deleteMobileDevice(MobileDevice device){
		sessionFactory.getCurrentSession().delete(device);
		return device.getId();
	}
	
	/**
	 * 
	 * @param deviceId
	 * @throws MobileDeviceNotFoundException
	 * @return MobileDevice
	 * @author Thangaraj(KNSTEK)
	 * @since 07-Jan-2015
	 */
	@Override
	public MobileDevice getMobileDeviceById(Integer deviceId) throws MobileDeviceNotFoundException{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MobileDevice.class);
		criteria.add(Restrictions.eq("id", deviceId));
		@SuppressWarnings("unchecked")
		ArrayList<MobileDevice> list = (ArrayList<MobileDevice>)criteria.list();
		if(list.isEmpty())
			throw new MobileDeviceNotFoundException();
		return list.get(0);
	}
	
	/**
	 * 
	 * @param deviceToken
	 * @throws MobileDeviceNotFoundException
	 * @return MobileDevice
	 * @author Thangaraj(KNSTEK)
	 * @since 07-Jan-2015
	 */
	@Override
	public MobileDevice getMobileDeviceByDeviceToken(String deviceToken) throws MobileDeviceNotFoundException{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MobileDevice.class);
		criteria.add(Restrictions.eq("deviceToken", deviceToken));
		@SuppressWarnings("unchecked")
		ArrayList<MobileDevice> list = (ArrayList<MobileDevice>)criteria.list();
		if(list.isEmpty())
			throw new MobileDeviceNotFoundException();
		return list.get(0);
	}
	
	/**
	 * 
	 * @param user
	 * @throws MobileDeviceNotFoundException
	 * @return MobileDevice
	 * @author Thangaraj(KNSTEK)
	 * @since 07-Jan-2015
	 */
	@Override
	public MobileDevice getMobileDeviceByUser(TraidItUser user) throws MobileDeviceNotFoundException{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MobileDevice.class);
		criteria.add(Restrictions.eq("user", user));
		@SuppressWarnings("unchecked")
		ArrayList<MobileDevice> list = (ArrayList<MobileDevice>)criteria.list();
		if(list.isEmpty())
			throw new MobileDeviceNotFoundException();
		return list.get(0);
	}

	/**
	 * 
	 * @param notificationSettings
	 * @return Integer
	 * @author Thangaraj(KNSTEK)
	 * @throws NotificationSettingsNotSavedException 
	 * @since 08-Jan-2015
	 */
	@Override
	public Integer saveOrUpdateNotificationSettings(UserNotificationSettings notificationSettings) throws NotificationSettingsNotSavedException{
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(notificationSettings);
			sessionFactory.getCurrentSession().flush();
			return notificationSettings.getId();
		}
		catch(Exception e){
			log.error("Could not save Notification Settings");
		}
		throw new NotificationSettingsNotSavedException();
	}
	
	/**
	 * 
	 * @param userId
	 * @throws UserNotificationSettingsNotfoundException
	 * @return UserNotificationSettings
	 * @author Thangaraj(KNSTEK)
	 * @since 08-Jan-2015
	 */
	@Override
	public UserNotificationSettings getUserNotificationSettingsByUserId(Integer userId) throws UserNotificationSettingsNotfoundException{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserNotificationSettings.class);
		criteria.createAlias("user", "user");
		criteria.add(Restrictions.eq("user.userId", userId));
		ArrayList<UserNotificationSettings> list = (ArrayList<UserNotificationSettings>)criteria.list();
		if(!list.isEmpty() && list != null){
			return list.get(0);
		}
		throw new UserNotificationSettingsNotfoundException();
	}
	
	/**
	 * 
	 * @throws MobileDeviceNotFoundException
	 * @return ArrayList<MobileDevice>
	 * @author Thangaraj(KNSTEK)
	 * @since 16-Feb-2015
	 */
	public ArrayList<MobileDevice> getAllDevices() throws MobileDeviceNotFoundException{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MobileDevice.class);
		ArrayList<MobileDevice> list = (ArrayList<MobileDevice>) criteria.list();
		if(list == null || !list.isEmpty()){
			throw new MobileDeviceNotFoundException();
		}
		return list;
	}
}
