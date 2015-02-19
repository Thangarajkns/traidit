/**
 *  Created by     : Soujanya
 * Created Date	  : June 25th,2014
 * file Name	  : UserService.java
 * Purpose		  : Service

 * 
 * 
 */

package org.kns.traidit.frontend.user.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.kns.traidit.backend.payments.exceptions.PaymentNotFoundException;
import org.kns.traidit.backend.trade.exception.MessageNotFoundException;
import org.kns.traidit.backend.trade.exception.MessageNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.EmailAlreadyExistsException;
import org.kns.traidit.backend.user.exception.FavItemNotFoundException;
import org.kns.traidit.backend.user.exception.FavItemNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.FavVendorNotFoundException;
import org.kns.traidit.backend.user.exception.FavouritesNotFoundException;
import org.kns.traidit.backend.user.exception.MailNotSentException;
import org.kns.traidit.backend.user.exception.PlanNotFoundException;
import org.kns.traidit.backend.user.exception.RoleNotFoundException;
import org.kns.traidit.backend.user.exception.UserMessageNotFoundException;
import org.kns.traidit.backend.user.exception.UserMessageNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.UserNameAlreadyExistsException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.common.exception.SMSNotSentException;
import org.kns.traidit.frontend.payments.dto.UserSubscriptionPaymentsDto;
import org.kns.traidit.frontend.user.dto.FavouriteItemsDto;
import org.kns.traidit.frontend.user.dto.FavouriteVendorDto;
import org.kns.traidit.frontend.user.dto.PlansDto;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.kns.traidit.frontend.user.dto.UserMessagesDto;

public interface UserService {

	public Integer registerUser(String userName, String email, String password, String firstName, String lastName,
			String city, String state, String country, String displayName, String tradeEmail, Long rating, String zip)
			throws UserNotSavedOrUpdatedException, org.kns.traidit.backend.user.exception.RoleNotFoundException;

	public void deleteUser(Integer userId) throws UserNotFoundException;

	public ArrayList<UserDto> viewUsers(String searchText, Integer limit, Integer startIndex, String sortBy,
			String sortOrder) throws UserNotFoundException;

	public ArrayList<PlansDto> getAllPlans() throws PlanNotFoundException;

	public Integer getEdit(UserDto userDto) throws Exception;

	public UserDto getUserbyUserId(Integer userId) throws UserNotFoundException;

	public Integer saveEditedUserProfileToDB(Integer userId, String firstName, String lastName, String displayName,
			String city, String state, String country, Long rating, String zip) throws UserNotFoundException,
			UserNotSavedOrUpdatedException;

	public UserDto getUserbyUserName(String userName) throws UserNotFoundException;

	public UserDto getUserbyEmail(String email) throws UserNotFoundException;

	public FavouriteItemsDto getFavItembyId(Integer id) throws FavItemNotFoundException;

	public FavouriteVendorDto getFavVendorbyId(Integer id) throws FavVendorNotFoundException;

	// public Integer getPwdMatch(String userName, String password) throws
	// PasswordsDoNotMatchException,CredentialsExpiredException,AccountDisabledException,AccountLockedException;
	public UserDto authenticateUser(String username, String password, String deviceToken) throws Exception;

	public Integer addFavouriteItem(Integer itemId, Integer userId) throws FavItemNotSavedOrUpdatedException;

	public void deleteFavouriteItem(Integer favItemId) throws FavItemNotFoundException;

	public void deleteFavouriteItemOfUser(Integer favItemId, Integer userId) throws FavItemNotFoundException;

	public Integer addFavouriteVendor(Integer userId, Integer vendorId) throws Exception;

	public void deleteFavouriteVendor(Integer favVendorId) throws FavouritesNotFoundException;

	public void deleteFavouriteVendorOfUser(Integer favVendorId, Integer userId) throws FavouritesNotFoundException;

	public ArrayList<FavouriteItemsDto> getAllFavouriteItems(Integer pageNo, Integer pageSize)
			throws FavItemNotFoundException;

	public ArrayList<FavouriteVendorDto> getAllFavouriteVendors(Integer userId) throws FavVendorNotFoundException;

	public Integer sendPasswordResetMail(String email) throws UserNotFoundException, UserNotSavedOrUpdatedException,
			MailNotSentException;

	public Integer sendPasswordResetSMS(String email) throws UserNotFoundException, UserNotSavedOrUpdatedException,
			SMSNotSentException;

	public Integer getUserIdByPasswordResetToken(String token) throws Exception;

	public Integer handlePasswordReset(Integer userId, String password) throws Exception;

	public Integer updateContactProfileOfUser(Integer userId, String email, String tradeEmail, String phone,
			String tradePhone, String zip, String city, String state, String country, String addressline1,
			String addressline2) throws UserNotFoundException, UserNotSavedOrUpdatedException;

	public Integer saveEditedTradePhoneToDB(Integer userId, String tradePhone) throws UserNotFoundException,
			UserNotSavedOrUpdatedException;

	public Integer updatePassword(String userName, String password) throws UserNotSavedOrUpdatedException,
			UserNotFoundException;

	public Integer updatePasswordChange(Integer userId, String oldPassword, String newPassword) throws Exception;

	public TraidItUser getBasicUserDetails(String userName) throws Exception;

	public Boolean checkIfEmailExists(String email) throws EmailAlreadyExistsException, UserNotFoundException;

	public Boolean checkIfUserNameExists(String userName) throws UserNameAlreadyExistsException, UserNotFoundException;

	public Integer initializeRegistration(String email, String userName, String password, String firstName,
			String middleName, String lastName) throws UserNotSavedOrUpdatedException, EmailAlreadyExistsException,
			UserNotFoundException, UserNameAlreadyExistsException;

	public Integer completeRegistration(Integer userId, String city, String state, String country, String displayName,
			String tradeEmail, String zip, String tradePhone, String phone, String addressline1, String addressline2,
			String deviceToken) throws UserNotSavedOrUpdatedException, UserNotFoundException, RoleNotFoundException,
			org.kns.traidit.backend.user.exception.RoleNotFoundException;

	public Integer savePlanOfUser(Integer userId, Integer planId) throws UserNotFoundException,
			UserNotSavedOrUpdatedException, PlanNotFoundException;

	public Integer addReferral(Integer userId, String email, String phoneNumber) throws UserNotFoundException,
			UserNotSavedOrUpdatedException;

	public Integer processSocialRegistration(String firstName, String middleName, String lastName, String email)
			throws UserNotSavedOrUpdatedException, org.kns.traidit.backend.user.exception.RoleNotFoundException,
			PlanNotFoundException;

	public Integer saveOrUpdateTraiditUser(UserDto userDto) throws RoleNotFoundException,
			UserNotSavedOrUpdatedException, PlanNotFoundException;

	public ArrayList<UserSubscriptionPaymentsDto> getPaymentsOfUserByUserId(Integer userId) throws PaymentNotFoundException, UserNotFoundException;

	public UserDto generatePaymentAuthenticationToken(Integer userId) throws UserNotFoundException,
			UserNotSavedOrUpdatedException, PlanNotFoundException;

	public UserDto getUserByPaymentAuthenticationToken(String paymentAuthenticationToken) throws UserNotFoundException;

	public PlansDto getPlanById(Integer planId) throws PlanNotFoundException;

	public Integer registerGuest() throws UserNotSavedOrUpdatedException, RoleNotFoundException, PlanNotFoundException;

	public Integer saveUserMessage(Integer senderId, Integer receiverId, String message, String subject, Boolean isDraft)
			throws Exception;

	public Integer addRatingRestriction(Integer userId, String ratingRestriction) throws Exception;

	public ArrayList<UserMessagesDto> getUserMessagesFromDB(Integer userId, String flag)
			throws UserMessageNotFoundException;

	public UserMessagesDto getUserMessageById(Integer messageId) throws UserMessageNotFoundException;

	public Integer deleteUserMessage(Integer userId, Integer messageId) throws UserMessageNotFoundException,
			UserNotFoundException, UserMessageNotSavedOrUpdatedException;

	public Map<String, Integer> getCountOfUserMessages(Integer userId);

	public Boolean checkThePlanExpiryOfUserByUserId(Integer userId) throws UserNotFoundException;

	public Integer saveUserSuggestedImprovement(Integer userId, String suggestedImprovement) throws Exception;

	public Integer saveUserSuggestedBugs(Integer userId, String reportedBug) throws Exception;

	public Integer saveUserReviews(Integer userId, String review) throws Exception;

	public Integer saveUserReportedAbuse(Integer userId, Integer vendorId, String comment) throws Exception;

	public Map<Integer, String> listMessageContacts(Integer userId) throws UserNotFoundException;

	public Boolean emptyTrash(Integer userId) throws MessageNotFoundException, MessageNotSavedOrUpdatedException;

	public Boolean updateRefferral(Integer userId, Integer refferralId) throws UserNotFoundException,
			RoleNotFoundException, UserNotSavedOrUpdatedException, PlanNotFoundException;

	public Integer getNewSubscribersCountFrom(Date monthStartDate);
	
	public Map<String,Integer> getTradersCountWithSubscriptionType();
	
	public ArrayList<Object[]> getUsersCountByDateAndPlan();
	
	//added checkingUserPasswordAuthentication method by bhagya on feb 10th,2014
	public String checkingUserPasswordAuthentication(Integer userId,String password) throws UserNotFoundException;
	//added todaypayments dues method added by bhagya on Feb 16th,2015
	public ArrayList<UserDto> getUsersBasedOnTodayPaymentsDues() throws UserNotFoundException;
	public ArrayList<UserDto> getPaymentReportsByTerm(String term) throws UserNotFoundException;
}
