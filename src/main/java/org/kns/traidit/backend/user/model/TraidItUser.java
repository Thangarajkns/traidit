package org.kns.traidit.backend.user.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



/*
 * Created by Soujanya on June 18, 2014
 * Model for TraidIt User
 * Contians all User Credentials.
 */

@Entity
@Table(name = "kns_traidit_user")
public class TraidItUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "username", unique = true, length = 255)
	private String username;

	@Column(name = "password", length = 255)
	private String password;

	@Column(name = "email", length = 255)
	private String email;

	@Column(name = "first_name")
	private String firstName;

	 @Column(name="middle_name")
	 private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Roles roleId;

	@Column(name = "city")
	private String city;

	@Column(name = "state")
	private String state;

	@Column(name = "country")
	private String country;

	@Column(name = "zip")
	private String zip;
	
	@Column(name = "street")
	private String street;

	@Column(name = "trade_email", length = 255)
	private String tradeEmail;

	@Column(name = "rating")
	private Integer rating = 0;

	@Column(name = "no_of_ratings")
	private Integer noOfRatings = 0;
	
	@Column(name = "display_name")
	private String displayName;
	
	@Column(name = "is_enabled")
	private Boolean isEnabled;
	
	@Column(name = "is_locked")
	private Boolean isLocked;
	
	//for Handling Plan Expiry.. If an Plan is expired it shoul be 1
	@Column(name = "is_expired")
	private Boolean isExpired;

	@Column(name = "account_expiry_date")
	private Date accountExpiryDate;

	// Password Token to validate password reset link
	@Column(name = "password_token")
	private String passwordToken;

	// Date of Expiration of password token generated
	@Column(name = "password_token_expiry_date")
	private Date passwordTokenExpiryDate;

	/*
	 * Added by Jeevan on July 15, 2014 as per requirements
	 */
	@Column(name = "phone_no")
	private String phoneNo;

	/*
	 * Added by Jeevan on July 15, 2014 as per requirements
	 */
	@Column(name = "trade_phone_no")
	private String tradePhoneNo;

	@ManyToOne(cascade = { CascadeType.PERSIST },fetch = FetchType.LAZY)
	@JoinColumn(name = "plan_id")
	private Plans plan;

	@ManyToOne
	@JoinTable(name = "kns_traidit_referral", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "referrer_id") })
	private TraidItUser referral;

	/**
	 * @since 22-08-2014
	 * @author Thangaraj
	 */
	@Column(name="payment_status")
	private String paymentStatus;
	
	@Column(name="last_payment_id")
	private Integer lastPaymentId;
	
	@Column(name="payment_auth_token")
	private String paymentAuthenticationToken;
	
	//Added by Jeevan on September 23, 2014
	@Column(name="account_creation_date")
	private Date accountCreationDate;
	
	//added by Soujanya on nov 18,2014
	@Column(name="rating_restriction",nullable=false)
	private Integer ratingRestriction=0;
	
	//added by Soujanya on december 11,2014
	@Column(name="addressline_1")
	private String addressline1;
	
	@Column(name="addressline_2")
	private String addressline2;
	
	//Added by thangaraj on oct 16, 2014
	public TraidItUser(){
		
	}
	
	//Added by thangaraj on oct 16, 2014	
	public TraidItUser(Integer userId){
		this.userId = userId;
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

	public String getTradeEmail() {
		return tradeEmail;
	}

	public void setTradeEmail(String tradeEmail) {
		this.tradeEmail = tradeEmail;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Roles getRoleId() {
		return roleId;
	}

	public void setRoleId(Roles roleId) {
		this.roleId = roleId;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public Boolean getIsExpired() {
		return isExpired;
	}

	public void setIsExpired(Boolean isExpired) {
		this.isExpired = isExpired;
	}

	public Date getAccountExpiryDate() {
		return accountExpiryDate;
	}

	public void setAccountExpiryDate(Date accountExpiryDate) {
		this.accountExpiryDate = accountExpiryDate;
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

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getTradePhoneNo() {
		return tradePhoneNo;
	}

	public void setTradePhoneNo(String tradePhoneNo) {
		this.tradePhoneNo = tradePhoneNo;
	}

	public Plans getPlan() {
		return plan;
	}

	public void setPlan(Plans plan) {
		this.plan = plan;
	}

	public TraidItUser getReferral() {
		return referral;
	}

	public void setReferral(TraidItUser referral) {
		this.referral = referral;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
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
	
	
	
	
	
	

}
