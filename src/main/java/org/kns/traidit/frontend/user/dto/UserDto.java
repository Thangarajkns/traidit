
/**
 * Created by     : Soujanya

 * Created Date	  : June 20,2014
 * file Name	  : UserDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */



package org.kns.traidit.frontend.user.dto;

import java.util.ArrayList;
import java.util.Date;

import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.payments.dto.UserSubscriptionPaymentsDto;




public class UserDto {
	
	private Integer userId;
	private String email;
	private String password;
	private String firstName;
	private String middleName;
	private String lastName;
	private String phoneNo;
	private String tradeEmail;
	private String tradePhoneNo;
	private RolesDto roles;
	/*added by Soujanya on july 24,2014,field for plans*/
	private PlansDto plans;
	private String city;
	private String state;
	private String country;
	private String zip;
	private Integer rating;
	private Integer noOfRatings;
	private String displayName;
	private Integer totalUsers;
	private String userName;
	private Boolean isLocked;
	private Boolean isEnabled;
	private Boolean isExpired;
	private String passwordToken;
	private Date passwordTokenExpiryDate;
	private Date accountExpiryDate;
	private UserDto referral;
	private String paymentStatus;
	private Integer lastPaymentId;
	private String paymentAuthenticationToken;
	//Added by Jeevan on September 23, 2014
	private Date accountCreationDate;
	
	//Added by Soujanya on october 13,2014
	private Boolean isFavouriteVendor;
	
	//added by Soujanya on november 18,2014
	private String street;
	
	//added by Soujanya on november 18,2014
	private Integer ratingRestriction;

	//added by Soujanya on december 11,2014
	private String addressline1;
	private String addressline2;

	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getTradeEmail() {
		return tradeEmail;
	}
	public void setTradeEmail(String tradeEmail) {
		this.tradeEmail = tradeEmail;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public Integer getNoOfRatings() {
		return noOfRatings;
	}
	public void setNoOfRatings(Integer noOfRatings) {
		this.noOfRatings = noOfRatings;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public RolesDto getRoles() {
		return roles;
	}
	public void setRoles(RolesDto roles) {
		this.roles = roles;
	}
	public PlansDto getPlans() {
		return plans;
	}
	public void setPlans(PlansDto plans) {
		this.plans = plans;
	}
	public Integer getTotalUsers() {
		return totalUsers;
	}
	public void setTotalUsers(Integer totalUsers) {
		this.totalUsers = totalUsers;
	}
	public Boolean getIsLocked() {
		return isLocked;
	}
	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}
	public Boolean getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public Boolean getIsExpired() {
		return isExpired;
	}
	public void setIsExpired(Boolean isExpired) {
		this.isExpired = isExpired;
	}
	public String getPasswordToken() {
		return passwordToken;
	}
	public void setPasswordToken(String passwordToken) {
		this.passwordToken = passwordToken;
	}
	public Date getPasswordTokenExpiryDate() {
		return passwordTokenExpiryDate;
	}
	public void setPasswordTokenExpiryDate(Date passwordTokenExpiryDate) {
		this.passwordTokenExpiryDate = passwordTokenExpiryDate;
	}
	public Date getAccountExpiryDate() {
		return accountExpiryDate;
	}
	public void setAccountExpiryDate(Date accountExpiryDate) {
		this.accountExpiryDate = accountExpiryDate;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getTradePhoneNo() {
		return tradePhoneNo;
	}
	public void setTradePhoneNo(String tradePhoneNo) {
		this.tradePhoneNo = tradePhoneNo;
	}
	public UserDto getReferral() {
		return referral;
	}
	public void setReferral(UserDto referral) {
		this.referral = referral;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public Integer getLastPaymentId() {
		return lastPaymentId;
	}
	public void setLastPaymentId(Integer lastPaymentId) {
		this.lastPaymentId = lastPaymentId;
	}
	public String getPaymentAuthenticationToken() {
		return paymentAuthenticationToken;
	}
	public void setPaymentAuthenticationToken(String paymentAuthenticationToken) {
		this.paymentAuthenticationToken = paymentAuthenticationToken;
	}
	public Date getAccountCreationDate() {
		return accountCreationDate;
	}
	public void setAccountCreationDate(Date accountCreationDate) {
		this.accountCreationDate = accountCreationDate;
	}
	
	
	
	public Boolean getIsFavouriteVendor() {
		return isFavouriteVendor;
	}
	public void setIsFavouriteVendor(Boolean isFavouriteVendor) {
		this.isFavouriteVendor = isFavouriteVendor;
	}
	
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	
	
	public Integer getRatingRestriction() {
		return ratingRestriction;
	}
	public void setRatingRestriction(Integer ratingRestriction) {
		this.ratingRestriction = ratingRestriction;
	}
	

	
	
	public String getAddressline1() {
		return addressline1;
	}
	public void setAddressline1(String addressline1) {
		this.addressline1 = addressline1;
	}
	public String getAddressline2() {
		return addressline2;
	}
	public void setAddressline2(String addressline2) {
		this.addressline2 = addressline2;
	}
	/*  Conversion of Model to DTO */
	public static UserDto populateUserDto(TraidItUser user){
		UserDto userDto=new UserDto();
		userDto.setUserId(user.getUserId());
		userDto.setUserName(user.getUsername());
		userDto.setEmail(user.getEmail());
		userDto.setPassword(user.getPassword());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setPhoneNo(user.getPhoneNo());
		userDto.setRoles(RolesDto.populateRolesDto(user.getRoleId()));
		userDto.setCity(user.getCity());
		userDto.setCountry(user.getCountry());
		userDto.setState(user.getState());
		userDto.setZip(user.getZip());
		userDto.setDisplayName(user.getDisplayName());
		userDto.setRating(user.getRating());
		userDto.setNoOfRatings(user.getNoOfRatings());
		userDto.setTradeEmail(user.getTradeEmail());
		userDto.setIsEnabled(user.getIsEnabled());
		userDto.setIsLocked(user.getIsLocked());
		userDto.setIsExpired(user.getIsExpired());
		userDto.setPasswordToken(user.getPasswordToken());
		userDto.setAccountExpiryDate(user.getAccountExpiryDate());
		userDto.setTradePhoneNo(user.getTradePhoneNo());
		userDto.setMiddleName(user.getMiddleName());
		userDto.setPaymentStatus(user.getPaymentStatus());
		userDto.setLastPaymentId(user.getLastPaymentId());
		userDto.setPaymentAuthenticationToken(user.getPaymentAuthenticationToken());
		userDto.setPlans(PlansDto.populatePlansDto(user.getPlan()));
		userDto.setStreet(user.getStreet());
		userDto.setRatingRestriction(user.getRatingRestriction());
		if(user.getReferral()!=null){
			UserDto referal = new UserDto();
			referal.setUserId(user.getReferral().getUserId());
			userDto.setReferral(referal);
		}
		userDto.setAddressline1(user.getAddressline1());
		userDto.setAddressline2(user.getAddressline2());
		if(null!=user.getAccountCreationDate()){
			userDto.setAccountCreationDate(user.getAccountCreationDate());
		}
		
		//userDto.setTotalUsers(user.getTotalUsers());
		return userDto;
	}
	public static ArrayList<UserDto> populateUser(ArrayList<TraidItUser> users){
		ArrayList<UserDto> userDtos=new ArrayList<UserDto>();
		for(TraidItUser user:users){
			UserDto userDto=UserDto.populateUserDto(user);
			userDtos.add(userDto);
		}
		return userDtos;
	}
}
