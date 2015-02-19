/*
 * Created by Soujanya on June 25,2014..
 * Service for User Related Activites..
 */

package org.kns.traidit.frontend.user.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.kns.traidit.backend.commission.exception.CommissionNotSavedException;
import org.kns.traidit.backend.commission.exception.CommissionPaymentNotSavedException;
import org.kns.traidit.backend.inventory.dao.InventoryDao;
import org.kns.traidit.backend.item.dao.ItemDao;
import org.kns.traidit.backend.item.exception.ItemNotFoundException;
import org.kns.traidit.backend.item.model.TraidItItems;
import org.kns.traidit.backend.notification.exception.MobileDeviceNotFoundException;
import org.kns.traidit.backend.notification.exception.NotificationNotSentException;
import org.kns.traidit.backend.notification.exception.NotificationSettingsNotSavedException;
import org.kns.traidit.backend.payments.dao.PaymentDao;
import org.kns.traidit.backend.payments.exceptions.PaymentNotFoundException;
import org.kns.traidit.backend.payments.model.PaymentStatus;
import org.kns.traidit.backend.payments.model.UserSubscriptionPayments;
import org.kns.traidit.backend.trade.exception.MessageNotFoundException;
import org.kns.traidit.backend.trade.exception.MessageNotSavedOrUpdatedException;
import org.kns.traidit.backend.trade.exception.PaidPlanNotFoundException;
import org.kns.traidit.backend.trade.model.Ratings;
import org.kns.traidit.backend.user.dao.UserDao;
import org.kns.traidit.backend.user.dao.UserDaoImpl;
import org.kns.traidit.backend.user.exception.AccountDisabledException;
import org.kns.traidit.backend.user.exception.AccountLockedException;
import org.kns.traidit.backend.user.exception.EmailAlreadyExistsException;
import org.kns.traidit.backend.user.exception.FavItemNotFoundException;
import org.kns.traidit.backend.user.exception.FavItemNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.FavVendorNotFoundException;
import org.kns.traidit.backend.user.exception.FavouritesNotFoundException;
import org.kns.traidit.backend.user.exception.MailNotSentException;
import org.kns.traidit.backend.user.exception.PasswordsDoNotMatchException;
import org.kns.traidit.backend.user.exception.PlanNotFoundException;
import org.kns.traidit.backend.user.exception.RoleNotFoundException;
import org.kns.traidit.backend.user.exception.UserMessageNotFoundException;
import org.kns.traidit.backend.user.exception.UserMessageNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.UserNameAlreadyExistsException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.favourites.model.FavouriteItems;
import org.kns.traidit.backend.user.favourites.model.FavouriteVendor;
import org.kns.traidit.backend.user.model.Plans;
import org.kns.traidit.backend.user.model.Roles;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.backend.user.model.UserMessageStates;
import org.kns.traidit.backend.user.model.UserMessages;
import org.kns.traidit.backend.user.model.UserReportedAbuse;
import org.kns.traidit.backend.user.model.UserReportedBugs;
import org.kns.traidit.backend.user.model.UserReviews;
import org.kns.traidit.backend.user.model.UserSuggestedImprovements;
import org.kns.traidit.frontend.commission.dto.UserCommissionsFormBean;
import org.kns.traidit.frontend.commission.service.CommissionService;
import org.kns.traidit.frontend.common.exception.SMSNotSentException;
import org.kns.traidit.frontend.common.utility.EmailSender;
import org.kns.traidit.frontend.common.utility.SMSSender;
import org.kns.traidit.frontend.notification.service.NotificationService;
import org.kns.traidit.frontend.payments.dto.UserSubscriptionPaymentsDto;
import org.kns.traidit.frontend.user.dto.FavouriteItemsDto;
import org.kns.traidit.frontend.user.dto.FavouriteVendorDto;
import org.kns.traidit.frontend.user.dto.PlansDto;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.kns.traidit.frontend.user.dto.UserMessagesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {

	private static Logger log = Logger.getLogger(UserServiceImpl.class);

	@Resource(name = "userDao")
	private UserDao userDao;

	@Resource(name = "itemDao")
	private ItemDao itemDao;

	@Resource(name = "inventoryDao")
	private InventoryDao inventoryDao;

	@Resource(name = "paymentDao")
	private PaymentDao paymentDao;

	@Autowired
	private org.springframework.security.crypto.password.PasswordEncoder bCryptEncoder;

	@Resource(name = "emailSender")
	private EmailSender emailSender;

	@Resource(name = "SMSSender")
	private SMSSender smsSender;

	@Resource(name = "commissionService")
	private CommissionService commissionService;

	@Resource(name = "notificationService")
	private NotificationService notificationService;

	/**
	 * @throws RoleNotFoundException
	 ********************************************************************************************/

	/*
	 * for User Registrations.... 1. Populates User model from JSON input
	 * parameters 2. Adds role information 3. Saves it in DB
	 */
	public Integer registerUser(String userName, String email, String password, String firstName, String lastName,
			String city, String state, String country, String displayName, String tradeEmail, Long rating, String zip)
			throws UserNotSavedOrUpdatedException, RoleNotFoundException {
		log.info("inside registerUser()");
		TraidItUser user = new TraidItUser();
		user.setDisplayName(displayName);
		user.setCity(city);
		user.setCountry(country);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		String encodedPassword = bCryptEncoder.encode(password);
		user.setPassword(encodedPassword);
		if (null != zip) {
			user.setZip(zip);
		}
		user.setState(state);
		user.setUsername(userName);
		user.setTradeEmail(tradeEmail);
		user.setRating(Ratings.Average.getRate());
		user.setAccountExpiryDate(new Date());
		Roles role = this.userDao.getRoleByRoleId(1);
		user.setRoleId(role);
		user.setIsEnabled(false);
		user.setIsExpired(false);
		user.setIsLocked(false);
		Integer result = this.userDao.saveOrUpdateUser(user);
		return result;

	}

	/*
	 * added by Soujanya on july 24,204 method to check if email already exists
	 * in db
	 */
	public Boolean checkIfEmailExists(String email) throws EmailAlreadyExistsException, UserNotFoundException {
		log.info("inside checkIfEmailAlreadyExists()");
		TraidItUser user = this.userDao.getUserbyEmail(email);
		if (null != user) {
			// false : exist
			return false;
		} else
			// true: doesn't exist
			return true;

	}

	/*
	 * added by Soujanya on july 24,2014 method to check if username exists in
	 * db
	 */
	public Boolean checkIfUserNameExists(String userName) throws UserNameAlreadyExistsException, UserNotFoundException {
		log.info("inside checkIfUserNameAlreadyExists()");
		TraidItUser user = this.userDao.getUserbyUserName(userName);
		if (null != user) {
			return false;
		} else
			return true;
	}

	/*
	 * added by Soujanya on july 30,2014 method to process registration through
	 * social networks like facebook and google saves user fetched data to
	 * database checks if user already exists with fetched email id
	 */
	public Integer processSocialRegistration(String firstName, String middleName, String lastName, String email)
			throws UserNotSavedOrUpdatedException, RoleNotFoundException, PlanNotFoundException {
		log.info("inside processSocialRegistration()");
		Boolean emailExist = true;
		Integer result = null;
		try {
			TraidItUser userOnEmail = this.userDao.getUserbyEmail(email);
			if (null != userOnEmail) {
				return userOnEmail.getUserId();
			}
		} catch (UserNotFoundException e) {
			emailExist = false;
		}

		if (emailExist == false) {
			TraidItUser user = new TraidItUser();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setMiddleName(middleName);
			user.setEmail(email);
			// changed by Jeevan on Sep 22,2014 from role 1 t o2.
			Roles role = this.userDao.getRoleByRoleId(2);
			user.setRoleId(role);
			user.setAccountCreationDate(new Date());
			user.setIsEnabled(true);
			user.setIsExpired(false);
			user.setIsLocked(true);
			user.setPlan(this.userDao.getPlanByPlanId(1));
			Plans plan = this.userDao.getPlanByPlanId(1);
			user.setPlan(plan);
			user.setUsername(email);
			result = this.userDao.saveOrUpdateUser(user);
		}
		return result;

	}

	/*
	 * added by Soujanya on july 24,2014 method to initialize registration
	 * checks if user already registered with same email id or username if
	 * not,saves the info to database
	 */
	public Integer initializeRegistration(String email, String userName, String password, String firstName,
			String middleName, String lastName) throws UserNotSavedOrUpdatedException, EmailAlreadyExistsException,
			UserNotFoundException, UserNameAlreadyExistsException {
		log.info("inside initializeRegistration()");
		Boolean emailExist = true;
		Boolean usernameExist = true;
		Integer result = null;
		try {
			TraidItUser userOnEmail = this.userDao.getUserbyEmail(email);
			if (null != userOnEmail) {
				throw new EmailAlreadyExistsException();
			}
		} catch (UserNotFoundException e) {
			emailExist = false;
		}
		try {
			TraidItUser userOnUsername = this.userDao.getUserbyUserName(userName);
			if (null != userOnUsername) {
				throw new UserNameAlreadyExistsException();
			}
		} catch (UserNotFoundException e) {
			usernameExist = false;
		}
		if (emailExist == false && usernameExist == false) {
			TraidItUser user = new TraidItUser();
			user.setEmail(email);
			user.setUsername(userName);
			String encodedPassword = bCryptEncoder.encode(password);
			user.setPassword(encodedPassword);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setMiddleName(middleName);
			user.setAccountCreationDate(new Date());
			user.setIsEnabled(true);
			user.setIsExpired(false);
			user.setIsLocked(true);
			result = this.userDao.saveOrUpdateUser(user);
		}
		return result;

	}

	/*
	 * added by Soujanya on july 24,2014 method to complete user registration
	 * once user registration is validated
	 */
	public Integer completeRegistration(Integer userId, String city, String state, String country, String displayName,
			String tradeEmail, String zip, String tradePhone, String phone, String addressline1, String addressline2,String deviceToken)
			throws UserNotSavedOrUpdatedException, UserNotFoundException, RoleNotFoundException {
		log.info("inside completeRegistration()");
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		user.setDisplayName(displayName);
		user.setCity(city);
		user.setCountry(country);
		if (null != zip) {
			user.setZip(zip);
		}
		user.setState(state);
		user.setTradeEmail(tradeEmail);
		user.setTradePhoneNo(tradePhone);
		Roles role = this.userDao.getRoleByRoleId(2);
		user.setRoleId(role);
		user.setIsEnabled(true);
		user.setIsExpired(false);
		user.setIsLocked(false);
		user.setPhoneNo(phone);
		user.setAddressline1(addressline1);
		user.setAddressline2(addressline2);
		Integer result = this.userDao.saveOrUpdateUser(user);
		if(result>0){
			this.notificationService.updateDeviceInfo(result, deviceToken);
			try {
				this.notificationService.updateNotificationSettings(userId);
			} catch (NotificationSettingsNotSavedException e) {
				log.error("could not update notification sound settings of userid: "+userId);
			}
		}
		return result;
	}

	/*
	 * added by Soujanya on july 28,2014 method to save user plan to db updates
	 * user with selected plan
	 */
	public Integer savePlanOfUser(Integer userId, Integer planId) throws UserNotFoundException,
			UserNotSavedOrUpdatedException, PlanNotFoundException {
		log.info("inside savePlanOfUser() userId :" + userId + " planId:" + planId);
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		Plans plan = this.userDao.getPlanByPlanId(planId);
		user.setPlan(plan);
		Date accountExpiryDate;
		if(plan.getPlanId().equals(2) && user.getReferral() != null)
			accountExpiryDate = DateUtils.addDays(new Date(), plan.getSubscriptionDays()+60);
		else
			accountExpiryDate = DateUtils.addDays(new Date(), plan.getSubscriptionDays());
		user.setAccountExpiryDate(accountExpiryDate);
		Integer result = this.userDao.saveOrUpdateUser(user);
		return result;
	}

	public UserDto generatePaymentAuthenticationToken(Integer userId) throws UserNotFoundException,
			UserNotSavedOrUpdatedException, PlanNotFoundException {
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		user.setPaymentStatus(PaymentStatus.PENDING.toString());
		user.setPaymentAuthenticationToken(this.generatePasswordToken());
		Plans plan = this.userDao.getPlanByPlanId(1);
		user.setPlan(plan);
		log.info("PaymentAuthenticationToken : " + user.getPaymentAuthenticationToken() + " has generated for user : "
				+ userId);
		this.userDao.saveOrUpdateUser(user);
		return UserDto.populateUserDto(user);
	}

	/*
	 * added by Soujanya on july 28,2014 method to add referral to db
	 */
	public Integer addReferral(Integer userId, String email, String phoneNumber) throws UserNotFoundException,
			UserNotSavedOrUpdatedException {
		log.info("inside addReferral()");
		TraidItUser referral = this.userDao.getUserByEmailAndPhoneNumber(email, phoneNumber);
		System.out.println(referral.getUserId());
		TraidItUser user = this.userDao.getUserbyUserId(userId);

		if (!referral.getUserId().equals(userId)) {
			user.setReferral(referral);
		} else {
			throw new UserNotSavedOrUpdatedException();
		}
		Integer result = this.userDao.saveOrUpdateUser(user);

		return result;

	}

	/*
	 * added by Soujanya on july 17,2014 method to save edited user profile to
	 * DB Edited fields are sent through json from mobile device
	 */
	public Integer saveEditedUserProfileToDB(Integer userId, String firstName, String lastName, String displayName,
			String city, String state, String country, Long rating, String zip) throws UserNotFoundException,
			UserNotSavedOrUpdatedException {
		log.info("inside save EditedUserProfileToDB()");
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setDisplayName(displayName);
		user.setCity(city);
		user.setState(state);
		user.setCountry(country);
		// user.setRating(rating);
		user.setZip(zip);
		Integer result = this.userDao.saveOrUpdateUser(user);
		return result;
	}

	/*
	 * added by Soujanya on july 18,2014 method to save updated trade email of
	 * user to DB
	 */
	public Integer updateContactProfileOfUser(Integer userId, String email, String tradeEmail, String phone,
			String tradePhone, String zip, String city, String state, String country, String addressline1,
			String addressline2) throws UserNotFoundException, UserNotSavedOrUpdatedException {
		log.info("inside saveEditedTradeEmailToDB()");
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		System.out.println(user.getUsername());
		user.setPhoneNo(phone);
		user.setTradePhoneNo(tradePhone);
		user.setEmail(email);
		user.setCity(city);
		user.setZip(zip);
		user.setCountry(country);
		// user.setStreet(street);
		user.setState(state);
		user.setTradeEmail(tradeEmail);
		user.setAddressline1(addressline1);
		user.setAddressline2(addressline2);
		Integer result = this.userDao.saveOrUpdateUser(user);
		return result;
	}

	/*
	 * added by Soujanya on july 18,2014 method to save updated trade phone of
	 * user to DB
	 */
	public Integer saveEditedTradePhoneToDB(Integer userId, String tradePhone) throws UserNotFoundException,
			UserNotSavedOrUpdatedException {
		log.info("inside saveEditedTradePhoneToDB()");
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		System.out.println(user.getUsername());
		user.setTradeEmail(tradePhone);
		Integer result = this.userDao.saveOrUpdateUser(user);
		return result;
	}

	/*
	 * added by Soujanya on july 18,2014 method to update password when user
	 * changes his password
	 */
	public Integer updatePassword(String userName, String password) throws UserNotSavedOrUpdatedException,
			UserNotFoundException {
		log.info("inside updatePassword()");
		TraidItUser user = this.userDao.getUserbyUserName(userName);
		String newPassword = bCryptEncoder.encode(password);
		user.setPassword(newPassword);
		Integer result = this.userDao.saveOrUpdateUser(user);
		return result;
	}

	public Integer updatePasswordChange(Integer userId, String oldPassword, String newPassword) throws Exception {
		log.info("inside updatePasswordChange()");
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		if (!bCryptEncoder.matches(oldPassword, user.getPassword())) {
			throw new PasswordsDoNotMatchException();
		} else {
			String newPasswordEncoded = bCryptEncoder.encode(newPassword);
			user.setPassword(newPasswordEncoded);
			Integer result = this.userDao.saveOrUpdateUser(user);
			return result;
		}

	}

	/*
	 * method to delete user
	 */
	public void deleteUser(Integer userId) throws UserNotFoundException {
		log.info("inside deleteUser");
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		this.userDao.deleteUser(user);

	}

	/**
	 * modified by thangaraj : 17-10-2014 method to list all users (non-Javadoc)
	 * 
	 * @see org.kns.traidit.frontend.user.service.UserService#viewUsers(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	public ArrayList<UserDto> viewUsers(String searchText, Integer limit, Integer startIndex, String sortBy,
			String sortOrder) throws UserNotFoundException {

		log.info("calls this.userDao.getAllUsers(false," + searchText + "," + limit + "," + startIndex + "," + sortBy
				+ "," + sortOrder + ")");
		System.out.println("calls this.userDao.getAllUsers(false," + searchText + "," + limit + "," + startIndex + ","
				+ sortBy + "," + sortOrder + ")");

		ArrayList<TraidItUser> user = this.userDao.getAllUsers(false, searchText, limit, startIndex, sortBy, sortOrder);
		ArrayList<UserDto> userDtos = new ArrayList<UserDto>();
		Integer totalUsers = UserDaoImpl.totalUsers;

		log.info("total no of users found " + totalUsers + ". Populating dtos");
		System.out.println("total no of users found " + totalUsers + ". Populating dtos");

		for (TraidItUser traidItUser : user) {
			UserDto userDto = UserDto.populateUserDto(traidItUser);
			userDto.setTotalUsers(totalUsers);
			userDtos.add(userDto);
		}
		return userDtos;
	}

	/*
	 * added by Soujanya on july 28,2014 method to get all plans
	 */
	public ArrayList<PlansDto> getAllPlans() throws PlanNotFoundException {
		log.info("inside getAllPlans()");
		ArrayList<Plans> plan = this.userDao.getPlans();
		ArrayList<PlansDto> planDtos = new ArrayList<PlansDto>();
		// Integer totalUsers=UserDaoImpl.totalUsers;
		for (Plans plans : plan) {

			PlansDto planDto = PlansDto.populatePlansDto(plans);
			// userDto.setTotalUsers(totalUsers);
			planDtos.add(planDto);

		}
		return planDtos;
	}

	/*
	 * method to edit user data
	 */
	public Integer getEdit(UserDto userDto) throws Exception {
		log.info("inside getEdit()");
		TraidItUser user = this.userDao.getUserbyUserId(userDto.getUserId());
		user.setCity(userDto.getCity());
		user.setCountry(userDto.getCountry());

		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		if (null != userDto.getZip()) {
			user.setZip(userDto.getZip());
		}
		user.setState(userDto.getState());

		// Date dateModified =new Date();
		// user.setDateModified(dateModified);
		Integer result = this.userDao.saveOrUpdateUser(user);
		return result;

	}

	/*
	 * method to get user by user id
	 */
	public UserDto getUserbyUserId(Integer userId) throws UserNotFoundException {
		log.info("inside getUserbyUserId()");
		try {
			TraidItUser user = this.userDao.getUserbyUserId(userId);
			UserDto userDto = UserDto.populateUserDto(user);
			return userDto;
		} catch (Exception e) {
			log.error("Error While Validating User " + e.toString());
			return null;
		}
	}

	public TraidItUser getBasicUserDetails(String userName) throws Exception {
		return this.userDao.getBasicUserDetails(userName);
	}

	/*
	 * method to get user by user name
	 */
	public UserDto getUserbyUserName(String userName) throws UserNotFoundException {
		log.info("inside getUserbyUserName()");
		try {
			TraidItUser user = this.userDao.getUserbyUserName(userName);
			// System.out.println(user.getUsername()+"dddd");
			UserDto userDto = UserDto.populateUserDto(user);
			// System.out.println(userDto.getUserName()+"hhh");
			return userDto;
		} catch (Exception e) {
			log.error("Error While Validating User " + e.toString());
			return null;
		}
	}

	/*
	 * method to get user by email
	 */
	public UserDto getUserbyEmail(String email) throws UserNotFoundException {
		log.info("inside getUserbyEmail()");
		try {
			TraidItUser user = this.userDao.getUserbyEmail(email);
			UserDto userDto = UserDto.populateUserDto(user);
			return userDto;
		} catch (Exception e) {
			log.error("Error While Validating User " + e.toString());
			return null;
		}
	}

	/**
	  * 
	  */
	public Integer getUserIdByPasswordResetToken(String token) throws Exception {
		Integer userId = this.userDao.getUserIdByPasswordResetToken(token);
		return userId;
	}

	/**
	  * 
	  */
	@Override
	public UserDto getUserByPaymentAuthenticationToken(String paymentAuthenticationToken) throws UserNotFoundException {
		return UserDto.populateUserDto(this.userDao.getUserByPaymentAuthenticationToken(paymentAuthenticationToken));
	}

	/*
	 * method to get favourite item by Id
	 */
	public FavouriteItemsDto getFavItembyId(Integer id) throws FavItemNotFoundException {
		log.info("inside getFavItembyId()");
		try {
			FavouriteItems favItem = this.userDao.getFavItembyId(id);
			FavouriteItemsDto favItemDto = FavouriteItemsDto.populateFavouriteItemsDto(favItem);
			return favItemDto;
		} catch (Exception e) {
			log.error("Error While Validating User " + e.toString());
			return null;
		}
	}

	/*
	 * method to get favourite vendor by id
	 */
	public FavouriteVendorDto getFavVendorbyId(Integer id) throws FavVendorNotFoundException {
		log.info("inside getFavVendorbyId()");
		try {
			FavouriteVendor favVendor = this.userDao.getFavVendorbyId(id);
			FavouriteVendorDto favVendorDto = FavouriteVendorDto.populateFavouriteVendorDto(favVendor);
			return favVendorDto;
		} catch (Exception e) {
			log.error("Error While Validating User " + e.toString());
			return null;
		}
	}

	/**
	 * 
	 * Created by Jeevan on 22-Sep-2014 7:07:48 pm Method for:authenticating
	 * User
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public UserDto authenticateUser(String username, String password, String deviceToken) throws Exception {
		log.info("inside authenticateUser()");
		TraidItUser traiditUser = this.userDao.getUserbyUserName(username);
		if (!bCryptEncoder.matches(password, traiditUser.getPassword())) {
			throw new PasswordsDoNotMatchException();
		} // handling multiple exceptions not needed for mobile app
		if (!traiditUser.getIsEnabled()) {
			throw new AccountDisabledException();
		}
		if (traiditUser.getIsLocked()) {
			throw new AccountLockedException();
		}// Proper naming convention should have followed for the DTo, This is
			// not at all recommended
		UserDto userDto = UserDto.populateUserDto(traiditUser);
		this.notificationService.updateDeviceInfo(traiditUser.getUserId(), deviceToken);
		return userDto;
	}

	/*
	 * added by Soujanya on july 4,2014 method to add favourite item
	 */
	public Integer addFavouriteItem(Integer itemId, Integer userId) throws FavItemNotSavedOrUpdatedException {
		log.info("inside addFavouriteItem()");
		try {
			TraidItItems item = this.itemDao.getItemById(itemId);
			TraidItUser user = this.userDao.getUserbyUserId(userId);
			FavouriteItems favouriteitem = new FavouriteItems();
			favouriteitem.setItemId(item);
			favouriteitem.setUserId(user);
			Integer result = this.userDao.saveOrUpdateFavouriteItem(favouriteitem);
			return result;
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (ItemNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * added by Soujanya on july 4,2014 method to delete favourite item
	 */
	public void deleteFavouriteItem(Integer favItemId) throws FavItemNotFoundException {
		log.info("inside deleteFavouriteItem()");
		FavouriteItems item = this.userDao.getFavItembyId(favItemId);
		this.userDao.deleteFavouriteItem(item);
	}

	/**
	 * @author Thangaraj
	 * @since 06-10-2014
	 */
	public void deleteFavouriteItemOfUser(Integer favItemId, Integer userId) throws FavItemNotFoundException {
		log.info("inside deleteFavouriteItemOfUser()");
		FavouriteItems item = this.userDao.getFavItembyIdOfUser(favItemId, userId);
		this.userDao.deleteFavouriteItem(item);
	}

	/*
	 * added by Soujanya on July 4,2014 method to add favourite vendor
	 */
	public Integer addFavouriteVendor(Integer userId, Integer vendorId) throws Exception {
		log.info("inside addFavouriteVendor()");

		TraidItUser user = this.userDao.getUserbyUserId(userId);
		TraidItUser vendor = this.userDao.getUserbyUserId(vendorId);
		FavouriteVendor favouriteVendor = new FavouriteVendor();
		favouriteVendor.setUserId(user);
		favouriteVendor.setVendorId(vendor);
		Integer result = this.userDao.saveOrUpdateFavouriteVendor(favouriteVendor);
		return result;

	}

	/*
	 * added by Soujanya on July 4,2014 method to delete favourite vendor
	 */
	public void deleteFavouriteVendor(Integer favVendorId) throws FavouritesNotFoundException {
		log.info("inside deleteFavouriteVendor()");
		FavouriteVendor vendor = this.userDao.getFavVendorbyId(favVendorId);
		this.userDao.deleteFavVendor(vendor);
	}

	/**
	 * 
	 * @param favVendorId
	 * @throws FavouritesNotFoundException
	 * @author Thangaraj
	 * @since 06-10-2014
	 */
	public void deleteFavouriteVendorOfUser(Integer favVendorId, Integer userId) throws FavouritesNotFoundException {
		log.info("inside deleteFavouriteVendor()");
		FavouriteVendor vendor = this.userDao.getFavVendorbyIdOfUser(favVendorId, userId);
		this.userDao.deleteFavVendor(vendor);
	}

	/*
	 * added by Soujanya on July 8,2014 method to retrieve all favourite items
	 * from database and display as a list
	 */
	public ArrayList<FavouriteItemsDto> getAllFavouriteItems(Integer pageNo, Integer pageSize)
			throws FavItemNotFoundException {
		log.info("inside getAllFavouriteItems()");
		ArrayList<FavouriteItems> favouriteItems = this.userDao.getAllFavouriteItems(pageNo, pageSize);
		ArrayList<FavouriteItemsDto> favouriteItemsDtos = new ArrayList<FavouriteItemsDto>();
		Integer totalFavItems = UserDaoImpl.totalFavItems;
		for (FavouriteItems favouriteItem : favouriteItems) {
			FavouriteItemsDto favouriteItemsDto = FavouriteItemsDto.populateFavouriteItemsDto(favouriteItem);
			favouriteItemsDto.setTotalFavItems(totalFavItems);
			favouriteItemsDtos.add(favouriteItemsDto);
		}
		return favouriteItemsDtos;
	}

	/*
	 * added by Soujanya on July 11,2014 method to retrieve all favourite
	 * vendors from database and display as a list
	 */
	public ArrayList<FavouriteVendorDto> getAllFavouriteVendors(Integer userId) throws FavVendorNotFoundException {
		log.info("inside getAllFavouriteVendors()");
		ArrayList<FavouriteVendor> favouriteVendors = this.userDao.getAllFavouriteVendors(userId);
		ArrayList<FavouriteVendorDto> favouriteVendorDtos = new ArrayList<FavouriteVendorDto>();
		Integer totalFavVendors = UserDaoImpl.totalFavVendors;
		for (FavouriteVendor favouriteVendor : favouriteVendors) {
			FavouriteVendorDto favouriteVendorDto = FavouriteVendorDto.populateFavouriteVendorDto(favouriteVendor);
			favouriteVendorDto.setTotalFavVendors(totalFavVendors);
			favouriteVendorDtos.add(favouriteVendorDto);
		}
		return favouriteVendorDtos;

	}

	/*
		  * 
		  */
	public Integer sendPasswordResetMail(String email) throws UserNotFoundException, UserNotSavedOrUpdatedException,
			MailNotSentException {
		System.out.println("userServiceImpl -> sendPasswordResetMail()" + email);
		log.info("userServiceImpl -> sendPasswordResetMail()" + email);
		TraidItUser traidItUser = this.userDao.getUserbyEmail(email);
		String passwordToken = this.generatePasswordToken();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 1);
		Date validity = cal.getTime();
		traidItUser.setPasswordTokenExpiryDate(validity);
		traidItUser.setPasswordToken(passwordToken);
		log.info("save user info " + traidItUser.toString());
		this.userDao.saveOrUpdateUser(traidItUser);
		log.info("populate dto");
		UserDto userDto = UserDto.populateUserDto(traidItUser);
		log.info("send email");
		this.emailSender.sendForgotPasswordMail(userDto);
		log.info("mail sent");
		return 1;
	}

	/**
		  * 
		  */
	@Override
	public Integer sendPasswordResetSMS(String email) throws UserNotFoundException, UserNotSavedOrUpdatedException,
			SMSNotSentException {
		System.out.println("userServiceImpl -> sendPasswordResetSMS()" + email);
		TraidItUser traidItUser = this.userDao.getUserbyEmail(email);
		Random randomGenerator = new Random();
		Integer temp = randomGenerator.nextInt(1000000);
		String passwordToken = temp.toString();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 1);
		Date validity = cal.getTime();
		traidItUser.setPasswordTokenExpiryDate(validity);
		traidItUser.setPasswordToken(passwordToken);
		this.userDao.saveOrUpdateUser(traidItUser);
		UserDto userDto = UserDto.populateUserDto(traidItUser);
		this.smsSender.sendPasswordResetSMS(userDto);
		return 1;
	}

	private String generatePasswordToken() {
		String uuid = UUID.randomUUID().toString();
		String token = uuid.toString().replaceAll("-", "").toUpperCase();
		return token;
	}

	/*
		  * 
		  */
	public Integer handlePasswordReset(Integer userId, String password) throws Exception {
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		Date dateExpired = user.getPasswordTokenExpiryDate();
		if (dateExpired.after(new Date())) {
			String encryptedPassword = bCryptEncoder.encode(password);
			user.setPassword(encryptedPassword);
			user.setPasswordToken(null);
			user.setPasswordTokenExpiryDate(null);
			Integer result = this.userDao.saveOrUpdateUser(user);
			return result;
		}
		return 0;
	}

	/**
	 * save or update the given user object of type UserDto to database
	 * <p>
	 * takes all the data from user dto object and saves or updates it to
	 * Traidituser
	 * </p>
	 * 
	 * @param userDto
	 *            Dto object of the user to be saved or updated
	 * @throws UserNotSavedOrUpdatedException
	 *             on failure of saving given user
	 * @return Integer - (1) if success
	 * @author Thangaraj
	 * @throws PlanNotFoundException
	 */
	public Integer saveOrUpdateTraiditUser(UserDto userDto) throws RoleNotFoundException,
			UserNotSavedOrUpdatedException, PlanNotFoundException {
		log.info("inside save EditedUserProfileToDB()");
		TraidItUser user = new TraidItUser();
		user.setUserId(userDto.getUserId());
		user.setUsername(userDto.getUserName());
		user.setPassword(userDto.getPassword());
		user.setEmail(userDto.getEmail());
		user.setFirstName(userDto.getFirstName());
		user.setMiddleName(userDto.getMiddleName());
		user.setLastName(userDto.getLastName());
		user.setRoleId(this.userDao.getRoleByRoleId(userDto.getRoles().getRoleId()));
		user.setCity(userDto.getCity());
		user.setState(userDto.getState());
		user.setCountry(userDto.getCountry());
		user.setZip(userDto.getZip());
		user.setStreet(userDto.getStreet());
		user.setTradeEmail(userDto.getTradeEmail());
		user.setRating(userDto.getRating());
		user.setNoOfRatings(userDto.getNoOfRatings());
		user.setDisplayName(userDto.getDisplayName());
		user.setIsEnabled(userDto.getIsEnabled());
		user.setIsLocked(userDto.getIsLocked());
		user.setIsExpired(userDto.getIsExpired());
		user.setAccountExpiryDate(userDto.getAccountExpiryDate());
		user.setPasswordToken(userDto.getPasswordToken());
		user.setPasswordTokenExpiryDate(userDto.getPasswordTokenExpiryDate());
		user.setPhoneNo(userDto.getPhoneNo());
		user.setTradePhoneNo(userDto.getTradePhoneNo());
		user.setPlan(this.userDao.getPlanByPlanId(userDto.getPlans().getPlanId()));
		if (null != userDto.getReferral())
			user.setReferral(new TraidItUser(userDto.getReferral().getUserId()));
		user.setPaymentStatus(userDto.getPaymentStatus());
		user.setLastPaymentId(userDto.getLastPaymentId());
		user.setPaymentAuthenticationToken(userDto.getPaymentAuthenticationToken());
		user.setAccountCreationDate(userDto.getAccountCreationDate());
		user.setRatingRestriction(userDto.getRatingRestriction());
		Integer result = this.userDao.saveOrUpdateUser(user);
		return result;
	}

	/**
	 * @throws UserNotFoundException 
		  * 
		  */
	public ArrayList<UserSubscriptionPaymentsDto> getPaymentsOfUserByUserId(Integer userId) throws PaymentNotFoundException, UserNotFoundException {
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		ArrayList<UserSubscriptionPayments> payments = this.paymentDao.getPaymentsOfUserByUser(user);
		ArrayList<UserSubscriptionPaymentsDto> paymentDtos = UserSubscriptionPaymentsDto
				.populateUserSubscriptionPaymentsDtos(payments);
		return paymentDtos;
	}

	@Override
	public PlansDto getPlanById(Integer planId) throws PlanNotFoundException {
		return PlansDto.populatePlansDto(this.userDao.getPlanByPlanId(planId));
	}

	/*
	 * added by Soujanya on august 27th 2014 method to register guest user in db
	 * with null values and return userid
	 * 
	 * Modified by Jeevan on September 23, 2014 Added Fields for
	 * Enabled,Locked,Active, Account Creation Date
	 */
	public Integer registerGuest() throws UserNotSavedOrUpdatedException, RoleNotFoundException, PlanNotFoundException {
		log.info("inside registerGuest()");
		TraidItUser user = new TraidItUser();
		Roles role = this.userDao.getRoleByRoleId(2);
		user.setRoleId(role);
		user.setAccountCreationDate(new Date());
		user.setIsEnabled(true);
		user.setIsExpired(false);
		user.setIsLocked(false);
		user.setPlan(this.userDao.getPlanByPlanId(1));
		Integer result = this.userDao.saveOrUpdateUser(user);
		return result;
	}

	/**
	 * method to send message to other users saves message with sender and
	 * receiver in db
	 * 
	 * @author Soujanya
	 * @since october 1st,2014
	 * @input senderId, receiverId, message
	 * @throws UserMessageNotSavedOrUpdatedException
	 * @return user message id
	 * 
	 * modified by Thangaraj on Jan 7, 2015 : added push notification feature 
	 */
	public Integer saveUserMessage(Integer senderId, Integer receiverId, String message, String subject, Boolean isDraft)
			throws Exception {
		log.info("inside sendUserMessage()");
		UserMessages userMessage = new UserMessages();
		TraidItUser sender = this.userDao.getUserbyUserId(senderId);
		TraidItUser receiver = this.userDao.getUserbyUserId(receiverId);
		String notificationMessage = "You got a message from "+sender.getDisplayName();
		userMessage.setMessage(message);
		userMessage.setSender(sender);
		userMessage.setReceiver(receiver);
		userMessage.setIsRead(false);
		userMessage.setSubject(subject);
		userMessage.setIsSenderDeleted(0);
		userMessage.setIsReceiverDeleted(0);
		Date sentDate = new Date();

		if (isDraft.equals(false)) {
			userMessage.setIsDraft(false);
			userMessage.setDate(sentDate);
		} else if (isDraft.equals(true)) {
			userMessage.setIsDraft(true);
			userMessage.setDate(sentDate);
		}

		Integer result = this.userDao.saveOrUpdateUserMessages(userMessage);
		
		// send notification to the user
		try {
			this.notificationService.sendNotification(receiverId, notificationMessage);
		} catch (NotificationNotSentException e1) {
			log.error(e1.toString() + " {message : " + notificationMessage + ", receiverId : " + receiverId + "}");
		} catch (MobileDeviceNotFoundException e1) {
			log.error(e1.toString() + " {message : " + notificationMessage + ", receiverId : " + receiverId + "}");
		} catch (Exception e) {
			log.error(e.toString() + " {message : " + notificationMessage + ", receiverId : " + receiverId + "}");
		}

		return result;
	}

	/**
	 * method to add rating restriction set by user for other traders
	 * 
	 * @author Soujanya
	 * @since nov 18,2014
	 */
	public Integer addRatingRestriction(Integer userId, String ratingRestriction) throws Exception {
		log.info("inside addRatingRestriction()");
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		Ratings ratings = Ratings.valueOf(ratingRestriction);
		user.setRatingRestriction(ratings.getRate());
		Integer result = this.userDao.saveOrUpdateUser(user);
		return result;
	}

	/**
	 * method to get user messages from db based on flag
	 * 
	 * @author Soujanya
	 * @since nov 24,2014
	 */
	public ArrayList<UserMessagesDto> getUserMessagesFromDB(Integer userId, String flag)
			throws UserMessageNotFoundException {
		log.info("inside getUserMessagesFromDB()");
		ArrayList<UserMessages> userMessages = this.userDao.getUserMessagesFromDB(userId, flag);
		ArrayList<UserMessagesDto> userMessagesDtos = new ArrayList<UserMessagesDto>();
		for (UserMessages userMessage : userMessages) {
			UserMessagesDto userMessagesDto = UserMessagesDto.populateUserMessagesDto(userMessage);
			userMessagesDtos.add(userMessagesDto);
		}
		return userMessagesDtos;

	}

	/**
	 * method to get user message by message id
	 * 
	 * @author Soujanya
	 * @since nov 24,2014
	 */
	public UserMessagesDto getUserMessageById(Integer messageId) throws UserMessageNotFoundException {
		log.info("inside getUserMessageById");
		UserMessages userMessage = this.userDao.getUserMessageById(messageId);
		userMessage.setIsRead(true);

		try {
			this.userDao.saveOrUpdateUserMessages(userMessage);
		} catch (UserMessageNotSavedOrUpdatedException e) {
			userMessage.setIsRead(true);
		}
		UserMessagesDto userMessageDto = UserMessagesDto.populateUserMessagesDto(userMessage);
		return userMessageDto;

	}

	/**
	 * method to delete a user message
	 * 
	 * @author Soujanya
	 * @since nov 24,2014
	 */
	public Integer deleteUserMessage(Integer userId, Integer messageId) throws UserMessageNotFoundException,
			UserNotFoundException, UserMessageNotSavedOrUpdatedException {
		log.info("inside deleteUserMessage()");
		UserMessages userMessage = this.userDao.getUserMessageById(messageId);
		if (userMessage.getSender().getUserId().equals(userId)) {
			userMessage.setIsSenderDeleted(1);
		} else if (userMessage.getReceiver().getUserId().equals(userId)) {
			userMessage.setIsReceiverDeleted(1);
		} else {
			throw new UserNotFoundException();
		}

		Integer deletedResult = this.userDao.saveOrUpdateUserMessages(userMessage);
		return deletedResult;
	}

	/**
	 * method to get all user message count and populate it in a map
	 * 
	 * @author Soujanya
	 * @since nov 24,2014
	 * 
	 */
	public Map<String, Integer> getCountOfUserMessages(Integer userId) {
		log.info("inside getCountOfUserMessages()");

		Map<String, Integer> values = new HashMap<String, Integer>();
		Integer count = 0;
		log.info("for each UserMessageStates, calls {userDao.getTotalUserMessageCount(userId,State)} and populates data");
		for (UserMessageStates state : UserMessageStates.values()) {
			count = this.userDao.getTotalUserMessageCount(userId, state.name().toString());
			values.put(state.name().toString(), count);
		}
		return values;
	}

	/**
	 * method For checking the Expiry date with current date
	 * 
	 * @author Bhagya
	 * @throws UserNotFoundException
	 * @throws PaidPlanNotFoundException
	 * @since dec 02,2014
	 */
	public Boolean checkThePlanExpiryOfUserByUserId(Integer userId) throws UserNotFoundException {
		log.info("inside checkingTheExpiryDateOfUserByUserId()");
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		Date dateExpired = user.getAccountExpiryDate();
		Boolean result;
		if (user.getPlan().getPlanId() == 2 || user.getPlan().getPlanId() == 3 || user.getPlan().getPlanId() == 4) {
			if (dateExpired.after(new Date())) {
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		if (!result) {
			try {
				result = renewPlanWithCommissionEarned(userId);
			} catch (CommissionPaymentNotSavedException e) {
				result = false;
			} catch (CommissionNotSavedException e) {
				result = false;
			} catch (PlanNotFoundException e) {
				result = false;
			}
		}
		return result;
	}

	private Boolean renewPlanWithCommissionEarned(Integer userId) throws UserNotFoundException,
			CommissionPaymentNotSavedException, CommissionNotSavedException, PlanNotFoundException {
		Boolean result = false;
		UserDto user = this.getUserbyUserId(userId);
		result = this.commissionService.paySubscriptionFromCommission(user);
		return result;
	}

	/**
	 * method to save user suggested improvements to db
	 * 
	 * @author Soujanya
	 * @since dec 10,2014
	 * @throws Exception
	 * 
	 */
	public Integer saveUserSuggestedImprovement(Integer userId, String suggestedImprovement) throws Exception {
		log.info("inside saveUserSuggestedImprovement()");
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		UserSuggestedImprovements userSuggestedImprovement = new UserSuggestedImprovements();
		userSuggestedImprovement.setUserId(user);
		userSuggestedImprovement.setSuggestedImprovement(suggestedImprovement);
		Integer result = this.userDao.saveUserSuggestedImprovement(userSuggestedImprovement);
		return result;
	}

	/**
	 * method to save user reported bugs to db
	 * 
	 * @author Soujanya
	 * @since dec 10,2014
	 * @throws Exception
	 * 
	 */
	public Integer saveUserSuggestedBugs(Integer userId, String reportedBug) throws Exception {
		log.info("inside saveUserSuggestedBugs()");
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		UserReportedBugs userReportedBug = new UserReportedBugs();
		userReportedBug.setUserId(user);
		userReportedBug.setBug(reportedBug);
		Integer result = this.userDao.saveuserReportedBugs(userReportedBug);
		return result;
	}

	/**
	 * method to save user reviews to db
	 * 
	 * @author Soujanya
	 * @since dec 10,2014
	 * @throws Exception
	 * 
	 */
	public Integer saveUserReviews(Integer userId, String review) throws Exception {
		log.info("inside saveUserReviews()");
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		UserReviews userReview = new UserReviews();
		userReview.setUserId(user);
		userReview.setReview(review);
		Integer result = this.userDao.saveUserReviews(userReview);
		return result;
	}

	/**
	 * method to save user reported abuse to db
	 * 
	 * @author Soujanya
	 * @since jan 2nd,2015
	 * @throws Exception
	 * 
	 */
	public Integer saveUserReportedAbuse(Integer userId, Integer vendorId, String comment) throws Exception {
		 log.info("inside saveUserReportedAbuse()");
		 TraidItUser user=this.userDao.getUserbyUserId(userId);
		 TraidItUser vendor=this.userDao.getUserbyUserId(vendorId);
		 
		 UserReportedAbuse userReportedAbuse=new UserReportedAbuse();
		 userReportedAbuse.setComment(comment);
		 userReportedAbuse.setUserId(user);
		 userReportedAbuse.setVendorId(vendor);
		 Integer result=this.userDao.saveUserReportedAbuse(userReportedAbuse);
		 return result;

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
	public Map<Integer,String> listMessageContacts(Integer userId) throws UserNotFoundException{
		return this.userDao.listMessageContacts(userId);
	}
	
	public Boolean emptyTrash(Integer userId) throws MessageNotFoundException, MessageNotSavedOrUpdatedException{
		return this.userDao.emptyTrash(userId);
	}

	/**
	 * 
	 * @param userId
	 * @param refferralId
	 * @throws UserNotFoundException
	 * @throws RoleNotFoundException
	 * @throws UserNotSavedOrUpdatedException
	 * @throws PlanNotFoundException
	 * @return Boolean
	 * @author Thangaraj(KNSTEK)
	 * @since 02-Feb-2015
	 */
	public Boolean updateRefferral(Integer userId,Integer refferralId) throws UserNotFoundException, RoleNotFoundException, UserNotSavedOrUpdatedException, PlanNotFoundException{
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		TraidItUser refferrer = this.userDao.getUserbyUserId(refferralId);
		user.setReferral(refferrer);
		this.userDao.saveOrUpdateUser(user);
		return true;
	}

	/**
	 * =================== Start : User DashBoard Services ======================
	 */
	
	@Override
	public Integer getNewSubscribersCountFrom(Date monthStartDate){
		return this.userDao.getNewSubscribersCountFrom(monthStartDate);
	}
	
	@Override
	public Map<String,Integer> getTradersCountWithSubscriptionType(){
		Map<String,Integer> tradersCount = new HashMap<String, Integer>();
		Map<String,Integer> data = this.userDao.getUserCountsByPlans();
		tradersCount.put("Guests", data.get("PLAN_GUEST")==null?0:data.get("PLAN_GUEST"));
		tradersCount.put("Trial", data.get("PLAN_TRIAL")==null?0:data.get("PLAN_TRIAL"));
		tradersCount.put("Subscriber", data.get("PLAN_PAID")==null?0:data.get("PLAN_PAID"));
		tradersCount.put("Enterprise", data.get("PLAN_ENTERPRISE")==null?0:data.get("PLAN_ENTERPRISE"));
		tradersCount.put("Expired", this.userDao.getExpiredUsersCount());
		return tradersCount;
	}
	
	/**
	 * 
	 */
	@Override
	public ArrayList<Object[]> getUsersCountByDateAndPlan(){
		return this.userDao.getUsersCountByDateAndPlan();
	}
	/**
	 * =================== End : User DashBoard Services ======================
	 * 
	 */
	
	
		/**
		 * Created By Bhagya On Feb 10th,2015
		 * @param userId
		 * @param password
		 * @return
		 * @throws UserNotFoundException
		 * 
		 * Mobile service for checking the user the authorized user or not based on userid and password
		 */
	
	public String checkingUserPasswordAuthentication(Integer userId,String password) throws UserNotFoundException{
		log.info("inside userPasswordAuthentication()");
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		String authMsg;
		if (!bCryptEncoder.matches(password, user.getPassword())) {
			authMsg="Unauthorized User";
		}
		else{
			authMsg="Authorized User";
		}
		return authMsg;
	}
	
	/**
	 * Craeted By Bhagya On Feb 16th,2015
	 * @return
	 * @throws UserNotFoundException
	 * Method for getting the USErs with the restriction of account expiry date
	 * ..It is used at get the payments dues of users for today
	 * @throws ParseException 
	 */
	public ArrayList<UserDto> getUsersBasedOnTodayPaymentsDues() throws UserNotFoundException{
		log.info("inside getUsersBasedOnTodayPaymentsDues()");
		ArrayList<UserDto> userDtos = new ArrayList<UserDto>();
		try{
			ArrayList<TraidItUser> user=this.userDao.getUsersByAccountExpiryDate(new Date());
			for (TraidItUser traidItUser : user) {
				UserDto userDto = UserDto.populateUserDto(traidItUser);
				userDtos.add(userDto);
			}
		}
		catch(UserNotFoundException e){
			
		}
		return userDtos;
	}
	/**
	 * Created By Bhagya On Feb 16th,2015
	 * @param term
	 * @return
	 * @throws UserNotFoundException
	 * 
	 * Method for getting the current week  repots of payment users
	 */
	public ArrayList<UserDto> getPaymentReportsByTerm(String term) throws UserNotFoundException{
		log.info("inside getPaymentReportsByTerm()");
		Calendar cal=Calendar.getInstance();
		cal.clear();
		cal.setTime(new Date());
		Date startDate = null,endDate = null;
		if(term.equalsIgnoreCase("week")){
			Integer dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
			cal.add(Calendar.DATE,-(dayOfWeek-1));
			startDate=cal.getTime();
			cal.add(Calendar.DATE, 7);
			endDate=cal.getTime();			
		}
		ArrayList<UserDto> userDtos=new ArrayList<UserDto>();
		ArrayList<TraidItUser> users=this.userDao.getUsersPaymentsBetweenDays(startDate, endDate);
		
		for (TraidItUser traidItUser : users) {
			UserDto userDto = UserDto.populateUserDto(traidItUser);
			userDtos.add(userDto);
		}
		return userDtos;
	}
	
}
