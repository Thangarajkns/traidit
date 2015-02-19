/**
 * @since 12-Feb-2015
 */
package org.kns.traidit.backend.admin.dao;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.kns.traidit.backend.admin.exception.AllUserNotificationNotFoundException;
import org.kns.traidit.backend.admin.exception.AllUserNotificationNotSavedException;
import org.kns.traidit.backend.admin.model.AllUserNotification;
import org.kns.traidit.backend.user.dao.UserDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Thangaraj(KNSTEK)
 *
 */

@Repository("adminUserManagementDao")
@Transactional
public class AdminUserManagementDaoImpl implements AdminUserManagementDao {

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private static Logger log = Logger.getLogger(UserDaoImpl.class);

	/**
	 * 
	 * @return ArrayList<AllUserNotification>
	 * @throws AllUserNotificationNotFoundException
	 * @author Thangaraj(KNSTEK)
	 * @since 12-Feb-2015
	 */
	@Override
	public ArrayList<AllUserNotification> getListOfAllUserNotifications() throws AllUserNotificationNotFoundException{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AllUserNotification.class);
		ArrayList<AllUserNotification> list = (ArrayList<AllUserNotification>) criteria.list();
		if(list == null || list.isEmpty())
			throw new AllUserNotificationNotFoundException(); 
		return list;
	}

	/**
	 * 
	 * @param notificationId
	 * @throws AllUserNotificationNotFoundException
	 * @return AllUserNotification
	 * @author Thangaraj(KNSTEK)
	 * @since 13-Feb-2015
	 */
	@Override
	public AllUserNotification getAllUserNotificationById(Integer notificationId) throws AllUserNotificationNotFoundException{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AllUserNotification.class);
		criteria.add(Restrictions.eq("id", notificationId));
		ArrayList<AllUserNotification> list = (ArrayList<AllUserNotification>)criteria.list();
		if(list == null || list.isEmpty()){
			throw new AllUserNotificationNotFoundException();
		}
		return list.get(0);
	}

	/**
	 * 
	 * @param notification
	 * @return Integer
	 * @author Thangaraj(KNSTEK)
	 * @throws AllUserNotificationNotSavedException 
	 * @since 13-Feb-2015
	 */
	@Override
	public Integer saveOrUpdateAllUserNotification(AllUserNotification notification) throws AllUserNotificationNotSavedException{
		try{
		sessionFactory.getCurrentSession().saveOrUpdate(notification);
		sessionFactory.getCurrentSession().flush();
		}
		catch(Exception e){
			throw new AllUserNotificationNotSavedException();
		}
		return notification.getId();
	}
}
