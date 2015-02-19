package org.kns.traidit.backend.user.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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



/*
 * created by Soujanya on 19th June,2014
 * DAO interface for user related activities
 */

public interface UserDao {

	public Integer saveOrUpdateUser(TraidItUser user) throws UserNotSavedOrUpdatedException;
    public void deleteUser(TraidItUser user) throws UserNotFoundException ;
    public ArrayList<TraidItUser> getAllUsers(Boolean includeGuestUsers,String searchText, Integer limit,Integer startIndex,String sortBy,String sortOrder) throws UserNotFoundException;
    public TraidItUser getUserbyUserId(Integer id) throws UserNotFoundException ;
    public TraidItUser getUserByEmailAndPhoneNumber(String email,String phoneNumber) throws UserNotFoundException;
    public TraidItUser getUserbyEmail(String email) throws UserNotFoundException ;
    public TraidItUser getUserbyUserName(String userName) throws UserNotFoundException ;
    public ArrayList<Roles> getRoles()throws RoleNotFoundException;
    public ArrayList<Plans> getPlans() throws PlanNotFoundException;
    public Roles getRoleByRoleId(Integer roleId)throws RoleNotFoundException;
    public Plans getPlanByPlanId(Integer planId) throws PlanNotFoundException;
    public Integer saveOrUpdateFavouriteItem(FavouriteItems favItem) throws FavItemNotSavedOrUpdatedException;
    public void deleteFavouriteItem(FavouriteItems favItem) throws FavItemNotFoundException;
    public ArrayList<FavouriteItems> getAllFavouriteItems(Integer pageNo,Integer pageSize) throws FavItemNotFoundException;
    public FavouriteItems getFavItembyId(Integer id) throws FavItemNotFoundException;
    public FavouriteItems getFavItembyIdOfUser(Integer itemId,Integer userId) throws FavItemNotFoundException;
    public FavouriteItems getFavItembyUserId(TraidItUser userId) throws FavItemNotFoundException;
    public FavouriteItems getFavItembyItemId(TraidItItems itemId) throws FavItemNotFoundException;
    public Integer saveOrUpdateFavouriteVendor(FavouriteVendor favVendors) throws FavVendorNotSavedOrUpdatedException;
    public void deleteFavVendor(FavouriteVendor favVendors) throws FavouritesNotFoundException;
    public ArrayList<FavouriteVendor> getAllFavouriteVendors(Integer userId) throws FavVendorNotFoundException;
    public FavouriteVendor getFavVendorbyId(Integer id) throws FavouritesNotFoundException;
    public FavouriteVendor getFavVendorbyIdOfUser(Integer vendorId, Integer userId) throws FavouritesNotFoundException;
    public FavouriteVendor getFavVendorbyVendorId(TraidItUser vendorId) throws FavVendorNotFoundException;
    public FavouriteVendor getFavVendorbyUserId(TraidItUser userId) throws FavVendorNotFoundException;
    public Integer getUserIdByPasswordResetToken(String token)throws Exception;
    public TraidItUser getBasicUserDetails(String userName) throws Exception;
	public TraidItUser getUserByPaymentAuthenticationToken(String paymentAuthenticationToken) throws UserNotFoundException;
	public Integer saveOrUpdateDistance(Distances distance)throws DistanceNotSavedOrUpdatedException;
	public Distances getDistanceByZipCodes(String zip1, String zip2) throws ZipNotFoundException;
	public Integer saveOrUpdateUserMessages(UserMessages userMessage) throws UserMessageNotSavedOrUpdatedException;
	public UserMessages getUserMessageById(Integer id) throws UserMessageNotFoundException;
	public void deleteUserMessage(UserMessages userMessage) throws UserMessageNotFoundException;
	public Integer getTotalUserMessageCount(Integer userId,String flag);
	public Integer getCountOfFavVendorsOfUser(Integer userId) throws FavouritesNotFoundException;
	public ArrayList<UserMessages> getUserMessagesFromDB(Integer userId, String flag) throws UserMessageNotFoundException;
	public Integer saveUserSuggestedImprovement(UserSuggestedImprovements userSuggestedImprovement) throws Exception;
	public Integer saveuserReportedBugs(UserReportedBugs bug) throws Exception;
	public Integer saveUserReviews(UserReviews review) throws Exception;
	public Integer saveUserReportedAbuse(UserReportedAbuse userReportedAbuse) throws Exception;
	public Map<Integer,String> listMessageContacts(Integer userId) throws UserNotFoundException;
	public Boolean emptyTrash(Integer userId) throws MessageNotFoundException,MessageNotSavedOrUpdatedException;
	public Integer getNewSubscribersCountFrom(Date monthStartDate);
	public Map<String,Integer> getUserCountsByPlans();
	public Integer getExpiredUsersCount();
	public ArrayList<Object[]> getUsersCountByDateAndPlan();
	//getUsersByAccountExpiryDate method Added By bhagya On Feb 16th,2015
	public ArrayList<TraidItUser> getUsersByAccountExpiryDate(Date todayDate) throws UserNotFoundException;
	public ArrayList<TraidItUser> getUsersPaymentsBetweenDays(Date startDate,Date endDate) throws UserNotFoundException;
	
}
