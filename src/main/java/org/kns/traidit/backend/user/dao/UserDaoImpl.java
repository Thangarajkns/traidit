package org.kns.traidit.backend.user.dao;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.hibernate.type.StandardBasicTypes;
import org.kns.traidit.backend.item.model.TraidItItems;
import org.kns.traidit.backend.trade.exception.MessageNotFoundException;
import org.kns.traidit.backend.trade.exception.MessageNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.DistanceNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.FavItemNotFoundException;
import org.kns.traidit.backend.user.exception.FavItemNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.FavVendorNotFoundException;
import org.kns.traidit.backend.user.exception.FavVendorNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.FavouritesNotFoundException;
import org.kns.traidit.backend.user.exception.PlanNotFoundException;
import org.kns.traidit.backend.user.exception.RoleNotFoundException;
import org.kns.traidit.backend.user.exception.UserMessageNotFoundException;
import org.kns.traidit.backend.user.exception.UserMessageNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.ZipNotFoundException;
import org.kns.traidit.backend.user.favourites.model.FavouriteItems;
import org.kns.traidit.backend.user.favourites.model.FavouriteVendor;
import org.kns.traidit.backend.user.model.Distances;
import org.kns.traidit.backend.user.model.Plans;
import org.kns.traidit.backend.user.model.Roles;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.backend.user.model.UserMessages;
import org.kns.traidit.backend.user.model.UserReportedAbuse;
import org.kns.traidit.backend.user.model.UserReportedBugs;
import org.kns.traidit.backend.user.model.UserReviews;
import org.kns.traidit.backend.user.model.UserSuggestedImprovements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
 * created by Soujanya on 19th June,2014
 * DAO class for user related activities
 */

@Repository("userDao")
@Transactional
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;

	public static Integer totalUsers;
	public static Integer totalFavItems;
	public static Integer totalFavVendors;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private static Logger log = Logger.getLogger(UserDaoImpl.class);

	/*--------------------------------------------------------------------------------------------------------------------------------*/

	/*
	 * 
	 * method to save or update Traidit user performs save performs update
	 */
	public Integer saveOrUpdateUser(TraidItUser user)
			throws UserNotSavedOrUpdatedException {
		sessionFactory.getCurrentSession().saveOrUpdate(user);
		sessionFactory.getCurrentSession().flush();
		return user.getUserId();
	}

	/**
	 * --------------------------------------------------end of
	 * "saveOrUpdateUser()"
	 * ------------------------------------------------------
	 * -------------------------
	 **/

	/*
	 * added by Soujanya on sep 2,2014 saves distances between zip codes
	 */
	public Integer saveOrUpdateDistance(Distances distance)
			throws DistanceNotSavedOrUpdatedException {
		sessionFactory.getCurrentSession().saveOrUpdate(distance);
		sessionFactory.getCurrentSession().flush();
		return distance.getId();
	}

	/**
	 * --------------------------------------------------end of
	 * "saveOrUpdateDistance()"
	 * --------------------------------------------------
	 * -----------------------------
	 **/

	/*
	 * added by Soujanya on october 1st, 2014 saves user messages
	 */
	public Integer saveOrUpdateUserMessages(UserMessages userMessage)
			throws UserMessageNotSavedOrUpdatedException {
		sessionFactory.getCurrentSession().saveOrUpdate(userMessage);
		sessionFactory.getCurrentSession().flush();
		return userMessage.getId();
	}

	/**
	 * --------------------------------------------------end of
	 * "saveOrUpdateUserMessages()"
	 * ----------------------------------------------
	 * ---------------------------------
	 **/

	/*
	 * method to delete Traidit user deletes user
	 */
	public void deleteUser(TraidItUser user) throws UserNotFoundException {
		sessionFactory.getCurrentSession().delete(user);
		sessionFactory.getCurrentSession().flush();
	}

	/**
	 * --------------------------------------------------end of
	 * "deleteUser()"----
	 * --------------------------------------------------------
	 * -------------------
	 **/

	/*
	 * added by Soujanya on October 1st, 2014 deletes user message
	 */
	public void deleteUserMessage(UserMessages userMessage)
			throws UserMessageNotFoundException {
		sessionFactory.getCurrentSession().delete(userMessage);
		sessionFactory.getCurrentSession().flush();
	}

	/**
	 * --------------------------------------------------end of
	 * "deleteUserMessage()"
	 * ------------------------------------------------------
	 * -------------------------
	 **/

	/*
	 * method to get all users of Traidit lists all users
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TraidItUser> getAllUsers(Boolean includeGuestUsers,
			String searchText, Integer limit, Integer startIndex,
			String sortBy, String sortOrder) throws UserNotFoundException {
		log.info("inside getAllUsers()");
		ArrayList<TraidItUser> users = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				TraidItUser.class);
		criteria.createCriteria("roleId").add(
				Restrictions.ne("roleName", "ROLE_ADMIN"));
		if (!includeGuestUsers) {
			criteria.add(Restrictions.ne("plan.planId", 1));
		}
		if (searchText != null && !searchText.isEmpty()) {
			// criteria.add(Restrictions.ilike("firstName", searchText,
			// MatchMode.ANYWHERE));
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.ilike("firstName", searchText,
					MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("lastName", searchText,
					MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("username", searchText,
					MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("displayName", searchText,
					MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("middleName", searchText,
					MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("email", searchText,
					MatchMode.ANYWHERE));
			criteria.add(disjunction);
		}
		totalUsers = criteria.list().size();
		if (sortBy == null || sortBy.isEmpty())
			sortBy = "firstName";
		if (sortOrder != null && sortOrder.equals("DESC"))
			criteria.addOrder(Order.desc(sortBy));
		else
			criteria.addOrder(Order.asc(sortBy));
		criteria.setMaxResults(limit).setFirstResult(startIndex);
		users = (ArrayList<TraidItUser>) criteria.list();
		if (!users.isEmpty()) {
			return users;

		} else {
			throw new UserNotFoundException();
		}

	}

	/**
	 * --------------------------------------------------end of
	 * "getAllUsers()"--
	 * ----------------------------------------------------------
	 * -------------------
	 **/

	/*
	 * method to get user based on userid returns user by userid
	 */
	@SuppressWarnings("unchecked")
	public TraidItUser getUserbyUserId(Integer id) throws UserNotFoundException {
		ArrayList<TraidItUser> user = (ArrayList<TraidItUser>) sessionFactory
				.getCurrentSession().createCriteria(TraidItUser.class)
				.add(Restrictions.eq("userId", id)).list();

		if (!user.isEmpty()) {
			return user.get(0);
		} else {
			log.error("UserNotFoundException userId : " + id);
			throw new UserNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getUserbyUserId()"
	 * --------------------------------------------------------
	 * -----------------------
	 **/

	/*
	 * added by Soujanya on october 1st, 2014 method to get user message by id
	 */
	@SuppressWarnings("unchecked")
	public UserMessages getUserMessageById(Integer id)
			throws UserMessageNotFoundException {
		log.info("inside getUserMessageById()");
		ArrayList<UserMessages> userMessages = (ArrayList<UserMessages>) sessionFactory
				.getCurrentSession().createCriteria(UserMessages.class)
				.add(Restrictions.eq("id", id)).list();

		if (!userMessages.isEmpty()) {
			return userMessages.get(0);
		} else {
			throw new UserMessageNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getUserMessageById()"
	 * ----------------------------------------------------
	 * ---------------------------
	 **/

	/*
	 * method to get user based on email returns user by email
	 */
	@SuppressWarnings("unchecked")
	public TraidItUser getUserbyEmail(String email)
			throws UserNotFoundException {
		log.info("inside getUserbyEmail()");
		ArrayList<TraidItUser> user = (ArrayList<TraidItUser>) sessionFactory
				.getCurrentSession().createCriteria(TraidItUser.class)
				.add(Restrictions.eq("email", email)).list();
		if (!user.isEmpty()) {
			return user.get(0);
		} else {
			throw new UserNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getUserbyEmail()"
	 * --------------------------------------------------------
	 * -----------------------
	 **/

	/*
	 * ADDED by Soujanya on july 28,2014 method to get user by email and phone
	 * number
	 */
	@SuppressWarnings("unchecked")
	public TraidItUser getUserByEmailAndPhoneNumber(String email,
			String phoneNumber) throws UserNotFoundException {
		log.info("inside getUserByEmailAndPhoneNumber()");
		ArrayList<TraidItUser> users = new ArrayList<TraidItUser>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				TraidItUser.class);

		criteria.add(Restrictions.disjunction()
				.add(Restrictions.eq("email", email))
				.add(Restrictions.eq("phoneNo", phoneNumber))
				.add(Restrictions.eq("tradeEmail", email))
				.add(Restrictions.eq("tradePhoneNo", phoneNumber)));
		users = (ArrayList<TraidItUser>) criteria.list();
		if (!users.isEmpty()) {
			System.out.println(users.size());
			return users.get(0);
		} else {
			throw new UserNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getUserByEmailAndPhoneNumber()"
	 * ------------------------------------------
	 * -------------------------------------
	 **/

	/*
	 * added by Soujanya on sep 2.2014 method to retrieve distance between two
	 * zip codes
	 * 
	 * modified by Thangaraj : 04-02-2015 fetched appropriate distance from db
	 */
	@SuppressWarnings("unchecked")
	public Distances getDistanceByZipCodes(String zip1, String zip2)
			throws ZipNotFoundException {
		log.info("inside getDistanceByZipCodes()");
		ArrayList<Distances> distances = new ArrayList<Distances>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Distances.class);
		criteria.add(Restrictions.or(Restrictions.eq("zip1", zip1),
				Restrictions.eq("zip2", zip1)));
		criteria.add(Restrictions.or(Restrictions.eq("zip1", zip2),
				Restrictions.eq("zip2", zip2)));
		distances = (ArrayList<Distances>) criteria.list();
		if (!distances.isEmpty()) {
			for (Distances temp : distances) {
				if ((temp.getZip1().equals(zip1) || temp.getZip1().equals(zip2))
						&& (temp.getZip2().equals(zip1) || temp.getZip2()
								.equals(zip2))) {
					return temp;
				}
			}
		}
		throw new ZipNotFoundException();
	}

	/**
	 * --------------------------------------------------end of
	 * "getDistanceByZipCodes()"
	 * --------------------------------------------------
	 * -----------------------------
	 **/

	/*
	 * method to get user based on username returns user bt username
	 */
	@SuppressWarnings("unchecked")
	public TraidItUser getUserbyUserName(String userName)
			throws UserNotFoundException {
		log.info("inside getUserbyUserName()");
		ArrayList<TraidItUser> user = (ArrayList<TraidItUser>) sessionFactory
				.getCurrentSession().createCriteria(TraidItUser.class)
				.add(Restrictions.eq("username", userName)).list();
		if (!user.isEmpty()) {
			// System.out.println(user.get(0).getUsername()+"ssss");
			return user.get(0);
		} else {
			throw new UserNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getUserbyUserName()"
	 * ------------------------------------------------------
	 * -------------------------
	 **/

	/*
	 * Retrieves all Roles saved in the System..
	 */

	@SuppressWarnings("unchecked")
	public ArrayList<Roles> getRoles() throws RoleNotFoundException {
		log.info("inside getRoles()");
		ArrayList<Roles> roles = (ArrayList<Roles>) sessionFactory
				.getCurrentSession().createCriteria(Roles.class).list();
		if (!roles.isEmpty()) {
			return roles;
		} else {
			throw new RoleNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getRoles()"------
	 * -------------------------------------------------------------------------
	 **/

	/*
	 * added by Soujanya on july 28,2014 method to get all plans
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Plans> getPlans() throws PlanNotFoundException {
		log.info("inside getPlans()");
		ArrayList<Plans> plans = (ArrayList<Plans>) sessionFactory
				.getCurrentSession().createCriteria(Plans.class)
				// restriction added to remove the guest user from the retrieved
				// list
				.add(Restrictions.ne("planId", 1)).list();
		if (!plans.isEmpty()) {
			return plans;
		} else {
			throw new PlanNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getPlans()"------
	 * -------------------------------------------------------------------------
	 **/

	/*
	 * Gets Roles by Role Id.
	 */
	@SuppressWarnings("unchecked")
	public Roles getRoleByRoleId(Integer roleId) throws RoleNotFoundException {
		log.info("inside getRolesById()");
		ArrayList<Roles> roles = (ArrayList<Roles>) sessionFactory
				.getCurrentSession().createCriteria(Roles.class)
				.add(Restrictions.eq("roleId", roleId)).list();
		if (!roles.isEmpty()) {
			return roles.get(0);
		} else {
			throw new RoleNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getRoleByRoleId()"
	 * --------------------------------------------------------
	 * -----------------------
	 **/

	/*
	 * added by Soujanya on July 24,2014 method to get plan by plan id
	 */
	@SuppressWarnings("unchecked")
	public Plans getPlanByPlanId(Integer planId) throws PlanNotFoundException {
		log.info("inside getPlanByPlanId()");
		ArrayList<Plans> plans = (ArrayList<Plans>) sessionFactory
				.getCurrentSession().createCriteria(Plans.class)
				.add(Restrictions.eq("planId", planId)).list();
		if (!plans.isEmpty()) {
			return plans.get(0);
		} else {
			throw new PlanNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getPlanByPlanId()"
	 * --------------------------------------------------------
	 * -----------------------
	 **/

	/*
	 * (non-Javadoc) method to save or update favourite item
	 * 
	 * @see
	 * org.kns.traidit.backend.user.dao.UserDao#saveOrUpdateFavouriteItem(org
	 * .kns.traidit.backend.user.favourites.model.FavouriteItems)
	 */
	public Integer saveOrUpdateFavouriteItem(FavouriteItems favItem)
			throws FavItemNotSavedOrUpdatedException {
		sessionFactory.getCurrentSession().saveOrUpdate(favItem);
		sessionFactory.getCurrentSession().flush();
		return favItem.getId();
	}

	/**
	 * --------------------------------------------------end of
	 * "saveOrUpdateFavouriteItem()"
	 * ----------------------------------------------
	 * ---------------------------------
	 **/

	/*
	 * method to delete favourite item of user (non-Javadoc)
	 * 
	 * @see
	 * org.kns.traidit.backend.user.dao.UserDao#deleteFavouriteItem(org.kns.
	 * traidit.backend.user.favourites.model.FavouriteItems)
	 */
	public void deleteFavouriteItem(FavouriteItems favItem)
			throws FavItemNotFoundException {
		sessionFactory.getCurrentSession().delete(favItem);
		sessionFactory.getCurrentSession().flush();
	}

	/**
	 * --------------------------------------------------end of
	 * "deleteFavouriteItem()"
	 * ----------------------------------------------------
	 * ---------------------------
	 **/

	/*
	 * method to retrieve all favourite items of users
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<FavouriteItems> getAllFavouriteItems(Integer pageNo,
			Integer pageSize) throws FavItemNotFoundException {
		log.info("inside getAllFavouriteItems()");
		ArrayList<FavouriteItems> favItems = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				FavouriteItems.class);

		criteria.addOrder(Order.asc("itemId"));
		totalFavItems = criteria.list().size();
		if (null != pageNo && null != pageSize) {
			criteria.setFirstResult(pageNo * pageSize);
			criteria.setMaxResults(pageSize);
		}
		favItems = (ArrayList<FavouriteItems>) criteria.list();
		if (!favItems.isEmpty()) {
			return favItems;

		} else {
			throw new FavItemNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getAllFavouriteItems()"
	 * --------------------------------------------------
	 * -----------------------------
	 **/

	/*
	 * method to retrieve fav item by id
	 */
	@SuppressWarnings("unchecked")
	public FavouriteItems getFavItembyId(Integer id)
			throws FavItemNotFoundException {
		log.info("inside getFavItembyId()");
		ArrayList<FavouriteItems> favItem = (ArrayList<FavouriteItems>) sessionFactory
				.getCurrentSession().createCriteria(FavouriteItems.class)
				.add(Restrictions.eq("id", id)).list();
		if (!favItem.isEmpty()) {
			return favItem.get(0);
		} else {
			throw new FavItemNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getFavItembyId()"
	 * --------------------------------------------------------
	 * -----------------------
	 **/

	/**
	 * @author Thangaraj
	 * @since 06-10-2014
	 * 
	 */
	@SuppressWarnings("unchecked")
	public FavouriteItems getFavItembyIdOfUser(Integer itemId, Integer userId)
			throws FavItemNotFoundException {
		log.info("inside getFavItembyId()");
		ArrayList<FavouriteItems> favItem = (ArrayList<FavouriteItems>) sessionFactory
				.getCurrentSession().createCriteria(FavouriteItems.class)
				.add(Restrictions.eq("itemId.itemId", itemId))
				.add(Restrictions.eq("userId.userId", userId)).list();
		if (!favItem.isEmpty()) {
			return favItem.get(0);
		} else {
			throw new FavItemNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getFavItembyIdOfUser()"
	 * --------------------------------------------------
	 * -----------------------------
	 **/

	/*
	 * method to retrieve fav item by user id
	 */
	@SuppressWarnings("unchecked")
	public FavouriteItems getFavItembyUserId(TraidItUser userId)
			throws FavItemNotFoundException {
		log.info("inside getFavItembyUserId()");
		ArrayList<FavouriteItems> favItem = (ArrayList<FavouriteItems>) sessionFactory
				.getCurrentSession().createCriteria(FavouriteItems.class)
				.add(Restrictions.eq("userId", userId)).list();
		if (!favItem.isEmpty()) {
			return favItem.get(0);
		} else {
			throw new FavItemNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getFavItembyUserId()"
	 * ----------------------------------------------------
	 * ---------------------------
	 **/

	/*
	 * method to retrieve fav item by item id
	 */
	@SuppressWarnings("unchecked")
	public FavouriteItems getFavItembyItemId(TraidItItems itemId)
			throws FavItemNotFoundException {
		log.info("inside getFavItembyUserId()");
		ArrayList<FavouriteItems> favItem = (ArrayList<FavouriteItems>) sessionFactory
				.getCurrentSession().createCriteria(FavouriteItems.class)
				.add(Restrictions.eq("item_id", itemId)).list();
		if (!favItem.isEmpty()) {
			return favItem.get(0);
		} else {
			throw new FavItemNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getFavItembyItemId()"
	 * ----------------------------------------------------
	 * ---------------------------
	 **/

	/*
	 * method to save or update fav vendor
	 */
	public Integer saveOrUpdateFavouriteVendor(FavouriteVendor favVendors)
			throws FavVendorNotSavedOrUpdatedException {
		sessionFactory.getCurrentSession().saveOrUpdate(favVendors);
		sessionFactory.getCurrentSession().flush();
		return favVendors.getId();
	}

	/**
	 * --------------------------------------------------end of
	 * "saveOrUpdateFavouriteVendor()"
	 * --------------------------------------------
	 * -----------------------------------
	 **/

	/*
	 * method to delete fav vendor of user
	 */
	public void deleteFavVendor(FavouriteVendor favVendors)
			throws FavouritesNotFoundException {
		sessionFactory.getCurrentSession().delete(favVendors);
		sessionFactory.getCurrentSession().flush();
	}

	/**
	 * --------------------------------------------------end of
	 * "deleteFavVendor()"
	 * --------------------------------------------------------
	 * -----------------------
	 **/

	/*
	 * method to retrieve all fav vendors of users
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<FavouriteVendor> getAllFavouriteVendors(Integer userId)
			throws FavVendorNotFoundException {
		log.info("inside getAllFavouriteVendors()");
		ArrayList<FavouriteVendor> favVendors = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				FavouriteVendor.class);
		criteria.add(Restrictions.eq("userId.userId", userId));
		criteria.addOrder(Order.asc("id"));
		totalFavVendors = criteria.list().size();

		favVendors = (ArrayList<FavouriteVendor>) criteria.list();
		if (!favVendors.isEmpty()) {
			return favVendors;

		} else {
			throw new FavVendorNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getAllFavouriteVendors()"
	 * ------------------------------------------------
	 * -------------------------------
	 **/

	/*
	 * method to retrive fav vendors by id
	 */
	@SuppressWarnings("unchecked")
	public FavouriteVendor getFavVendorbyId(Integer id)
			throws FavouritesNotFoundException {
		log.info("inside getFavVendorbyId()");
		ArrayList<FavouriteVendor> favVendor = (ArrayList<FavouriteVendor>) sessionFactory
				.getCurrentSession().createCriteria(FavouriteVendor.class)
				.add(Restrictions.eq("id", id)).list();
		if (!favVendor.isEmpty()) {
			return favVendor.get(0);
		} else {
			throw new FavouritesNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getFavVendorbyId()"
	 * ------------------------------------------------------
	 * -------------------------
	 **/

	/**
	 * 
	 * @param vendorId
	 * @param userId
	 * @return
	 * @throws FavouritesNotFoundException
	 * @author Thangaraj
	 * @since 06-10-2014
	 */
	@SuppressWarnings("unchecked")
	public FavouriteVendor getFavVendorbyIdOfUser(Integer vendorId,
			Integer userId) throws FavouritesNotFoundException {
		log.info("inside getFavVendorbyId()");
		ArrayList<FavouriteVendor> favVendor = (ArrayList<FavouriteVendor>) sessionFactory
				.getCurrentSession().createCriteria(FavouriteVendor.class)
				.add(Restrictions.eq("vendorId.userId", vendorId))
				.add(Restrictions.eq("userId.userId", userId)).list();
		if (!favVendor.isEmpty()) {
			return favVendor.get(0);
		} else {
			throw new FavouritesNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getFavVendorbyIdOfUser()"
	 * ------------------------------------------------
	 * -------------------------------
	 **/

	/*
	 * method to retreive fav vendor by vendor id
	 */
	@SuppressWarnings("unchecked")
	public FavouriteVendor getFavVendorbyVendorId(TraidItUser vendorId)
			throws FavVendorNotFoundException {
		log.info("inside getFavVendorbyVendorId()");
		ArrayList<FavouriteVendor> favItem = (ArrayList<FavouriteVendor>) sessionFactory
				.getCurrentSession().createCriteria(FavouriteVendor.class)
				.add(Restrictions.eq("vendor_id", vendorId)).list();
		if (!favItem.isEmpty()) {
			return favItem.get(0);
		} else {
			throw new FavVendorNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getFavVendorbyVendorId()"
	 * ------------------------------------------------
	 * -------------------------------
	 **/

	/*
	 * method to retrieve fav vendor by user id
	 */
	@SuppressWarnings("unchecked")
	public FavouriteVendor getFavVendorbyUserId(TraidItUser userId)
			throws FavVendorNotFoundException {
		log.info("inside getFavVendorbyUserId()");
		ArrayList<FavouriteVendor> favItem = (ArrayList<FavouriteVendor>) sessionFactory
				.getCurrentSession().createCriteria(FavouriteVendor.class)
				.add(Restrictions.eq("user_id", userId)).list();
		if (!favItem.isEmpty()) {
			return favItem.get(0);
		} else {
			throw new FavVendorNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getFavVendorbyUserId()"
	 * --------------------------------------------------
	 * -----------------------------
	 **/

	/**
	 * Returns the user's id one who has given Password reset token
	 * 
	 * @param token
	 *            password reset token sent to the user
	 * @throws Exception
	 *             if there is no user with given token OR any kind of exception
	 *             while interacting with db
	 * @return <code>userId</code> if the user with the given token exists
	 * @since 1.0
	 * @author Thangaraj
	 */
	public Integer getUserIdByPasswordResetToken(String token) throws Exception {
		System.out.println("UserDaoImpl -> getUserIdByPasswordResetToken()");
		ArrayList<Integer> users = (ArrayList<Integer>) sessionFactory
				.getCurrentSession().createCriteria(TraidItUser.class)
				.add(Restrictions.eq("passwordToken", token))
				.setProjection(Projections.property("userId")).list();
		if (!users.isEmpty()) {
			return users.get(users.size() - 1);
		} else {
			throw new UserNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getUserIdByPasswordResetToken()"
	 * ------------------------------------------
	 * -------------------------------------
	 **/

	@Override
	public TraidItUser getUserByPaymentAuthenticationToken(
			String paymentAuthenticationToken) throws UserNotFoundException {
		System.out
				.println("UserDaoImpl -> getUserByPaymentAuthenticationToken()");
		ArrayList<TraidItUser> users = (ArrayList<TraidItUser>) sessionFactory
				.getCurrentSession()
				.createCriteria(TraidItUser.class)
				.add(Restrictions.eq("paymentAuthenticationToken",
						paymentAuthenticationToken)).list();
		if (!users.isEmpty()) {
			return users.get(users.size() - 1);
		} else {
			throw new UserNotFoundException();
		}
	}

	/**
	 * --------------------------------------------------end of
	 * "getUserByPaymentAuthenticationToken()"
	 * ------------------------------------
	 * -------------------------------------------
	 **/

	public TraidItUser getBasicUserDetails(String userName) throws Exception {
		System.out.println("UserDaoImpl -> getBasicUserDetails()");
		ArrayList<Object> results = (ArrayList<Object>) sessionFactory
				.getCurrentSession()
				.createCriteria(TraidItUser.class)
				.add(Restrictions.eq("username", userName))
				.setProjection(
						Projections.projectionList()
								.add(Projections.property("userId"))
								.add(Projections.property("username"))
								.add(Projections.property("firstName"))
								.add(Projections.property("lastName"))).list();
		Object user = results.get(0);
		TraidItUser traidItUser = new TraidItUser();
		traidItUser.setUserId((Integer) Array.get(user, 0));
		traidItUser.setUsername((String) Array.get(user, 1));
		traidItUser.setFirstName((String) Array.get(user, 2));
		traidItUser.setLastName((String) Array.get(user, 3));
		return traidItUser;

	}

	/**
	 * --------------------------------------------------end of
	 * "getBasicUserDetails()"
	 * ----------------------------------------------------
	 * ---------------------------
	 **/

	/**
	 * method to get count of user's favourite inventories
	 * 
	 * @author Soujanya
	 * @since nov 17,2014
	 */
	public Integer getCountOfFavVendorsOfUser(Integer userId)
			throws FavouritesNotFoundException {
		log.info("inside getCountOfFavVendorsOfUser()");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				FavouriteVendor.class);
		criteria.add(Restrictions.eq("userId.userId", userId));
		criteria.setProjection(Projections.count("id"));
		Integer count = Integer.parseInt(criteria.list().get(0).toString());
		return count;
	}

	/**
	 * --------------------------------------------------end of
	 * "getCountOfFavVendorsOfUser()"
	 * --------------------------------------------
	 * -----------------------------------
	 **/

	/**
	 * method to get count of total user messages of user based on flag
	 * 
	 * @author Soujanya
	 * @since nov 24,2014 refer comments for: getUserMessagesFromDB() method
	 *        below
	 */
	public Integer getTotalUserMessageCount(Integer userId, String flag) {
		Integer count = 0;
		try {
			Criteria criteria = sessionFactory.getCurrentSession()
					.createCriteria(UserMessages.class);

			if (flag.equals("read")) {
				criteria.add(Restrictions.eq("receiver.userId", userId));
				criteria.add(Restrictions.eq("isRead", true));
				criteria.add(Restrictions.eq("isReceiverDeleted", 0));
				criteria.add(Restrictions.eq("isDraft", false));
			}

			else if (flag.equals("unread")) {
				criteria.add(Restrictions.eq("receiver.userId", userId));
				criteria.add(Restrictions.eq("isRead", false));
				criteria.add(Restrictions.eq("isReceiverDeleted", 0));
				criteria.add(Restrictions.eq("isDraft", false));
			}

			else if (flag.equals("sent")) {
				criteria.add(Restrictions.eq("sender.userId", userId));
				criteria.add(Restrictions.eq("isSenderDeleted", 0));
				criteria.add(Restrictions.eq("isDraft", false));
			}

			else if (flag.equals("all")) {
				criteria.add(Restrictions.or(Restrictions.and(
						Restrictions.eq("sender.userId", userId),
						Restrictions.eq("isSenderDeleted", 0)), Restrictions
						.and(Restrictions.eq("receiver.userId", userId),
								Restrictions.eq("isReceiverDeleted", 0))));
				criteria.add(Restrictions.eq("isDraft", false));
			} else if (flag.equals("deleted")) {
				criteria.add(Restrictions.or(Restrictions.and(
						Restrictions.eq("sender.userId", userId),
						Restrictions.eq("isSenderDeleted", 1)), Restrictions
						.and(Restrictions.eq("receiver.userId", userId),
								Restrictions.eq("isReceiverDeleted", 1))));
				criteria.add(Restrictions.eq("isDraft", false));
			}

			else if (flag.equals("drafts")) {
				criteria.add(Restrictions.eq("sender.userId", userId));
				criteria.add(Restrictions.eq("isSenderDeleted", 0));
				criteria.add(Restrictions.eq("isDraft", true));
			}

			criteria.setProjection(Projections.count("id"));
			count = Integer.parseInt(criteria.list().get(0).toString());
		} catch (Exception e) {
			log.error(e.toString() + " on getTotalUserMessageCount(" + userId
					+ ", " + flag + ")");
		}
		return count;
	}

	/**
	 * --------------------------------------------------end of
	 * " getTotalUserMessageCount()"
	 * ----------------------------------------------
	 * ---------------------------------
	 **/

	/**
	 * method to get all user messages from db based on flag: "unread" "read"
	 * "sent" "all" "deleted" "drafts"
	 * 
	 * @author Soujanya
	 * @since nov 24,2014
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<UserMessages> getUserMessagesFromDB(Integer userId,
			String flag) throws UserMessageNotFoundException {
		log.info("inside getUserMessagesFromDB()");
		ArrayList<UserMessages> userMessages = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				UserMessages.class);

		/**
		 * flag is read(received read messages of user(inbox read)) check
		 * following conditions: 1) receiver should be logged in user 2) message
		 * should be read by user 3) message should not be deleted by
		 * receiver(user) 4) message should not be draft
		 **/
		if (flag.equals("read")) {
			criteria.add(Restrictions.eq("receiver.userId", userId));
			criteria.add(Restrictions.eq("isRead", true));
			criteria.add(Restrictions.eq("isReceiverDeleted", 0));
			criteria.add(Restrictions.eq("isDraft", false));
		}

		/**
		 * flag is unread(received unread messages of user(inbox unread)) check
		 * the following conditions: 1) receiver should be logged in user 2)
		 * message should be unread by user 3) message should not be deleted by
		 * receiver(user) 4) message should not be draft
		 **/
		else if (flag.equals("unread")) {
			criteria.add(Restrictions.eq("receiver.userId", userId));
			criteria.add(Restrictions.eq("isRead", false));
			criteria.add(Restrictions.eq("isReceiverDeleted", 0));
			criteria.add(Restrictions.eq("isDraft", false));
		}

		/**
		 * flag is sent check the following conditions: 1)sender should be
		 * logged in user 2)message should not be deleted by sender(user)
		 * 3)message should not be draft
		 **/
		else if (flag.equals("sent")) {
			criteria.add(Restrictions.eq("sender.userId", userId));
			criteria.add(Restrictions.eq("isSenderDeleted", 0));
			criteria.add(Restrictions.eq("isDraft", false));
		}

		/**
		 * flag is all check the following conditions 1)fetch the messages where
		 * user is both sender and receiver(sent and received ) 2)message should
		 * not be sender/receiver deleted according to user's sent/received
		 * messsages 3)message should not be draft
		 **/
		else if (flag.equals("all")) {
			criteria.add(Restrictions.or(Restrictions.and(
					Restrictions.eq("sender.userId", userId),
					Restrictions.eq("isSenderDeleted", 0)), Restrictions.and(
					Restrictions.eq("receiver.userId", userId),
					Restrictions.eq("isReceiverDeleted", 0))));
			criteria.add(Restrictions.eq("isDraft", false));
		}

		/**
		 * flag is deleted 1)fetch the messages where user is both sender and
		 * receiver(sent and received ) 2)message should be sender/receiver
		 * deleted according to user's sent/received messsages 3)message should
		 * not be draft
		 **/
		else if (flag.equals("deleted")) {
			criteria.add(Restrictions.or(Restrictions.and(
					Restrictions.eq("sender.userId", userId),
					Restrictions.eq("isSenderDeleted", 1)), Restrictions.and(
					Restrictions.eq("receiver.userId", userId),
					Restrictions.eq("isReceiverDeleted", 0))));
			criteria.add(Restrictions.eq("isDraft", false));
		}

		/**
		 * flag is drafts 1)sender should be logged in user 2)message should not
		 * be sender deleted 3)message should be draft
		 */
		else if (flag.equals("drafts")) {
			criteria.add(Restrictions.eq("sender.userId", userId));
			criteria.add(Restrictions.eq("isSenderDeleted", 0));
			criteria.add(Restrictions.eq("isDraft", true));
		}

		criteria.addOrder(Order.desc("id"));

		userMessages = (ArrayList<UserMessages>) criteria.list();
		if (!userMessages.isEmpty()) {
			return userMessages;

		} else {
			throw new UserMessageNotFoundException();
		}

	}

	/**
	 * -----------------------------------------------------end of
	 * getUserMessagesFromDB()
	 * method----------------------------------------------------
	 **/

	/**
	 * method to save user suggested improvement to db
	 * 
	 * @author Soujanya
	 * @since dec 10,2014
	 */
	public Integer saveUserSuggestedImprovement(
			UserSuggestedImprovements userSuggestedImprovement)
			throws Exception {
		sessionFactory.getCurrentSession().saveOrUpdate(
				userSuggestedImprovement);
		sessionFactory.getCurrentSession().flush();
		return userSuggestedImprovement.getId();
	}

	/**
	 * -------------------------------------------END OF
	 * saveUserSuggestedImprovement()
	 * METHOD-------------------------------------------
	 **/

	/**
	 * method to save user reported bugs to db
	 * 
	 * @author Soujanya
	 * @since dec 10,2014
	 */
	public Integer saveuserReportedBugs(UserReportedBugs bug) throws Exception {
		sessionFactory.getCurrentSession().saveOrUpdate(bug);
		sessionFactory.getCurrentSession().flush();
		return bug.getId();
	}

	/**
	 * -------------------------------------------END OF saveuserReportedBugs()
	 * METHOD-------------------------------------------
	 **/

	/**
	 * method to save user reviews to db
	 * 
	 * @author Soujanya
	 * @since dec 10,2014
	 */
	public Integer saveUserReviews(UserReviews review) throws Exception {
		sessionFactory.getCurrentSession().saveOrUpdate(review);
		sessionFactory.getCurrentSession().flush();
		return review.getId();
	}

	/**
	 * method to save user reported abuse to db
	 * 
	 * @author Soujanya
	 * @since jan 2nd,2014
	 */
	public Integer saveUserReportedAbuse(UserReportedAbuse userReportedAbuse)
			throws Exception {
		sessionFactory.getCurrentSession().saveOrUpdate(userReportedAbuse);
		sessionFactory.getCurrentSession().flush();
		return userReportedAbuse.getId();
	}

	/**
	 * Returns a Map consisting of users display names mapped to user id's
	 * 
	 * @param userId
	 * @throws UserNotFoundException
	 * @return Map<Integer,String>
	 * @author Thangaraj(KNSTEK)
	 * @since 13-Jan-2015
	 */
	public Map<Integer, String> listMessageContacts(Integer userId)
			throws UserNotFoundException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				TraidItUser.class);
		criteria.add(Restrictions.ne("userId", userId))
				.add(Restrictions.eq("isEnabled", true))
				.add(Restrictions.eq("isLocked", false))
				.add(Restrictions.eq("isExpired", false))
				.add(Restrictions.ne("plan.planId", 1))
				.add(Restrictions.ne("roleId.roleId", 1));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("userId"))
				.add(Projections.property("displayName")));
		ArrayList<Object[]> list = (ArrayList<Object[]>) criteria.list();
		if (list.isEmpty())
			throw new UserNotFoundException();
		Map<Integer, String> users = new HashMap<Integer, String>();
		for (Object[] user : list) {
			users.put((Integer) user[0], (String) user[1]);
		}
		return users;
	}

	/**
	 * 
	 * @param userId
	 * @throws MessageNotFoundException
	 * @throws MessageNotSavedOrUpdatedException
	 * @return Boolean
	 * @author Thangaraj(KNSTEK)
	 * @since 29-Jan-2015
	 */
	public Boolean emptyTrash(Integer userId) throws MessageNotFoundException,
			MessageNotSavedOrUpdatedException {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserMessages.class);
		criteria.createAlias("receiver", "receiver");
		criteria.add(Restrictions.or(Restrictions.and(
				Restrictions.eq("sender.userId", userId),
				Restrictions.eq("isSenderDeleted", 1)), Restrictions.and(
				Restrictions.eq("receiver.userId", userId),
				Restrictions.eq("isReceiverDeleted", 1))));
		@SuppressWarnings({ "unchecked" })
		ArrayList<UserMessages> deletedMessages = (ArrayList<UserMessages>) criteria
				.list();
		if (deletedMessages == null || deletedMessages.isEmpty())
			throw new MessageNotFoundException();
		try {
			for (UserMessages message : deletedMessages) {
				if (message.getSender().getUserId().equals(userId)) {
					message.setIsSenderDeleted(2);
				} else if (message.getReceiver().getUserId().equals(userId)) {
					message.setIsReceiverDeleted(2);
				}
				session.save(message);
			}
			session.flush();
		} catch (Exception e) {
			throw new MessageNotSavedOrUpdatedException();
		}
		return true;
	}

	/**
	 * =================== Start : User DashBoard Services ======================
	 */
	
	/**
	 * 
	 * @return Integer count of new subscribers created account this month
	 * @author Thangaraj(KNSTEK)
	 * @since 05-Feb-2015
	 */
	@Override
	public Integer getNewSubscribersCountFrom(Date monthStartDate){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TraidItUser.class);
		criteria.add(Restrictions.ge("accountCreationDate", monthStartDate));
		criteria.setProjection(Projections.count("userId"));
		Integer subscribersThisMonth = Integer.parseInt(criteria.list().get(0).toString());
		return subscribersThisMonth;
	}

	/**
	 * 
	 * @return Map consisting count of users on each plan 
	 * @author Thangaraj(KNSTEK)
	 * @since 06-Feb-2015
	 */
	@Override
	public Map<String,Integer> getUserCountsByPlans(){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TraidItUser.class);
		criteria.add(Restrictions.eq("isEnabled", true));
		criteria.add(Restrictions.eq("isLocked", false));
		criteria.add(Restrictions.eq("isExpired", false));
		criteria.add(Restrictions.ge("accountExpiryDate", new Date()));
		criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("plan")).add(Projections.count("userId")));
		ArrayList<Object[]> list = (ArrayList<Object[]>) criteria.list();
		Map<String,Integer> map = new HashMap<String, Integer>();
		for(Object[] record : list){
			map.put(((Plans)record[0]).getPlanName(), Integer.parseInt(record[1].toString()));
		}
		return map;
	}
	
	/**
	 * 
	 * @return Integer count of Expired
	 * @author Thangaraj(KNSTEK)
	 * @since 06-Feb-2015
	 */
	@Override
	public Integer getExpiredUsersCount(){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TraidItUser.class);
		criteria.add(Restrictions.eq("isEnabled", true));
		criteria.add(Restrictions.eq("isLocked", false));
		criteria.add(Restrictions.lt("accountExpiryDate", new Date()));
		criteria.setProjection(Projections.count("userId"));
		Integer count = Integer.parseInt(criteria.list().get(0).toString());
		return count;
	}
	
	public ArrayList<Object[]> getUsersCountByDateAndPlan(){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TraidItUser.class);
		criteria.add(Restrictions.eq("isEnabled", true));
		criteria.add(Restrictions.eq("isLocked", false));
		criteria.add(Restrictions.eq("isExpired", false));
		criteria.add(Restrictions.ge("accountExpiryDate", new Date()));
		criteria.setProjection(
				Projections.projectionList()
				.add(Projections.groupProperty("plan.planId"))
				.add(Projections.sqlGroupProjection("date(account_creation_date) as createdDate", "createdDate", new String[] { "createdDate" }, new Type[] { StandardBasicTypes.DATE }))
				.add(Projections.count("userId")));
		ArrayList<Object[]> list = (ArrayList<Object[]>) criteria.list();
		return list;
	}
	
	/**
	 * =================== End : User DashBoard Services ======================
	 * 
	 */

	/**
	 * Created By Bhagya On Feb 16th,2015
	 * 
	 * @param todayDate
	 * @return
	 * @throws UserNotFoundException
	 * 
	 *             Method For getting the list of users by the restriction of
	 *             accountexpiry date is equal to todays date. This method we
	 *             used at get payments Dues Of Today.
	 */

	@SuppressWarnings("unchecked")
	public ArrayList<TraidItUser> getUsersByAccountExpiryDate(Date todayDate)
			throws UserNotFoundException {
		log.info("inside getUsersByAccountExpiryDate");
		Date today = getDateWithoutTime(todayDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date tomorrow = getDateWithoutTime(cal.getTime());
		Criteria criteria = (Criteria) sessionFactory.getCurrentSession()
				.createCriteria(TraidItUser.class)
				.add(Restrictions.between("accountExpiryDate", today, tomorrow));
		ArrayList<TraidItUser> users = (ArrayList<TraidItUser>) criteria.list();
		if (!users.isEmpty()) {
			return users;

		} else {
			throw new UserNotFoundException();
		}

	}

	private static Date getDateWithoutTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	/**
	 * Craeted By Bhagya On Feb 16th,2015
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws UserNotFoundException
	 * 
	 * Method For getting the User PAyments Between startDAte and end DAte
	 */
	
	@SuppressWarnings("unchecked")
	public ArrayList<TraidItUser> getUsersPaymentsBetweenDays(Date startDate,Date endDate) throws UserNotFoundException{
		log.info("inside getUsersPaymentsBetweenDays()");
		Criteria criteria = (Criteria) sessionFactory.getCurrentSession()
				.createCriteria(TraidItUser.class)
				.add(Restrictions.between("accountExpiryDate", startDate, endDate));
		ArrayList<TraidItUser> users = (ArrayList<TraidItUser>) criteria.list();
		if (!users.isEmpty()) {
			return users;

		} else {
			throw new UserNotFoundException();
		}
	}
}
