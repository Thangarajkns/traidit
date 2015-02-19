/**
 *  Created by    : Soujanya
 * Created Date	  : July 1st,2014
 * file Name	  : UserAuthorizationController.java
 * Purpose		  : Handles User Authorization Related Operations
 * Type			  : Controller
 * 
 */

package org.kns.traidit.frontend.mobile.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.refferral.exception.RefferralTokenNotFoundException;
import org.kns.traidit.backend.refferral.model.RefferralToken;
import org.kns.traidit.backend.user.exception.AccountDisabledException;
import org.kns.traidit.backend.user.exception.AccountLockedException;
import org.kns.traidit.backend.user.exception.EmailAlreadyExistsException;
import org.kns.traidit.backend.user.exception.PasswordsDoNotMatchException;
import org.kns.traidit.backend.user.exception.PlanNotFoundException;
import org.kns.traidit.backend.user.exception.UserNameAlreadyExistsException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotSavedOrUpdatedException;
import org.kns.traidit.frontend.common.utility.JsonParser;
import org.kns.traidit.frontend.payments.service.PaymentService;
import org.kns.traidit.frontend.refferral.service.RefferralService;
import org.kns.traidit.frontend.user.dto.PlansDto;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

/**
 * 
 * Modified by Jeevan on Sept- 22, 2014 1. removed JsonParser.getJson() and made
 * it a part of JSONUTility util class. There should not be duplication of same
 * code in multiple classes. It indicate
 * 
 *
 */
@Controller("UserAuthorizationController")
public class UserAuthorizationController {

	private static Logger log = Logger.getLogger(UserAuthorizationController.class);

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "paymentService")
	private PaymentService paymentService;

	@Resource(name="refferralService")
	private RefferralService refferralService; 
	
	/*
	 * added by Soujanya on July 2nd,2014 method to perform login operation *
	 */

	/**
	 * 
	 * Modified by Jeevan on 22-Sep-2014 7:26:20 pm Method for: Performing User
	 * Login
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loginuser.htm")
	@ResponseBody
	public String loginUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside loginUser()");
		JSONObject obj = new JSONObject();
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String paramName = (String) headerNames.nextElement();
			String paramValue = request.getHeader(paramName);
			System.out.println(paramName+" :" + paramValue);
		}
		try {
			String status = "FAIL";
			JSONObject json = JsonParser.getJson(request, response);
			log.info(" request : " + json.toString());
			System.out.println(" request : " + json.toString());
			String username = json.getString("username");
			String password = json.getString("password");
			String deviceToken = json.getString("devicetoken");
			UserDto traiditUserDto = this.userService.authenticateUser(username, password, deviceToken);
			if (null != traiditUserDto) {
				status = "SUCCESS";
			}
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			obj.accumulate("status", status);
			obj.accumulate("userid", traiditUserDto.getUserId());
			obj.accumulate("expirydate", formatter.format(traiditUserDto.getAccountExpiryDate()));
			obj.accumulate("userplan", traiditUserDto.getPlans().getPlanId());
		} catch (UserNotFoundException e) {
			log.error("Error While Authenticating User " + e.toString());
			obj.accumulate("status", "Username Not Exists");
		} catch (PasswordsDoNotMatchException e) {
			log.error("Error While Authenticating User " + e.toString());
			obj.accumulate("status", "Password Entered is Wrong");
		} catch (AccountDisabledException e) {
			log.error(" User Account Disabled" + e.toString());
			obj.accumulate("status", "Your Account is Disabled, Please Contact Admin");
		} catch (AccountLockedException e) {
			log.error("User Account Locked" + e.toString());
			obj.accumulate("status", "Your Account is locked,Please Contact Admin");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error While logging in User" + e.toString());
			obj.accumulate("status", "Something went wrong while Authenticating User");
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/*
	 * added by Soujanya on august 27,2014 method to login guest user no input
	 * parameters returns userid after saving guest user with null values to
	 * database
	 */
	@RequestMapping(value = "/loginguestuser.htm")
	@ResponseBody
	public String loginGuestUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside loginGuestUser()");
		JSONObject obj = new JSONObject();
		try {
			log.info(" request : login guest user");
			System.out.println(" request : login guest user");
			String status = "FAIL";
			Integer result = this.userService.registerGuest();
			if (result > 1) {
				status = "SUCCESS";
				obj.accumulate("status", status);
				obj.accumulate("userid", result);
			} else {
				throw new UserNotSavedOrUpdatedException();
			}
		} catch (UserNotSavedOrUpdatedException e) {
			log.error("Error While Performing Guest Login" + e.toString());
			e.printStackTrace();
			obj.accumulate("status", "Error in login, try again");
		} catch (Exception e) {
			log.error("Error in Guest User Login" + e.toString());
			e.printStackTrace();
			obj.accumulate("status", "Error Occured While Logging in as Guest");
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/*
	 * added by Soujanya on july 17,2014 method to register user
	 */
	@RequestMapping(value = "/registeruser.htm")
	@ResponseBody
	public String registerUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside registerUser()");
		JSONObject obj = new JSONObject();
		String error = null;
		try {
			String status = "USER NOT REGISTERED";
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			String userName = json.getString("username");
			String email = json.getString("email");
			String password = json.getString("password");
			String firstName = json.getString("firstname");
			String lastName = json.getString("lastname");
			String city = json.getString("city");
			String state = json.getString("state");
			String country = json.getString("country");
			String displayName = json.getString("displayname");
			String tradeEmail = json.getString("tradeemail");
			Long rating = json.getLong("rating");
			String zip = json.getString("zip");
			Integer result = this.userService.registerUser(userName, email, password, firstName, lastName, city, state,
					country, displayName, tradeEmail, rating, zip);
			if (result > 0)
				status = "USER REGISTERED";
			obj.accumulate("status", status);

		} catch (UserNotSavedOrUpdatedException e) {
			error = e.toString();
			obj.accumulate("status", error);
		} catch (Exception e) {
			error = e.toString();
			obj.accumulate("status", error);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/**
	 * REGISTRATION WORK FLOW.
	 * 
	 * INSTEAD OF HANDLING REGISTRATION AT A STRETCH, IT IS HANDLED IN PARTS: 1.
	 * intialize Registration ( Basic Details Username, Password, Name Details)
	 * 2. Complete Registration (Address Related and Trade Parameters) 3.
	 * Referral Registration 4. User Plans 5. Payment 6. Payment Success
	 * 
	 */

	/*
	 * added by Soujanya on july 24,2014 method to handle initialize
	 * registration
	 */
	@RequestMapping(value = "/initializeregistration.htm")
	@ResponseBody
	public String initializeRegistrationProcess(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("inside initializeRegistrationProcess()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			String userName = json.getString("username");
			String email = json.getString("email");
			String password = json.getString("password");
			String firstName = json.getString("firstname");
			String lastName = json.getString("lastname");
			String middleName = json.getString("middlename");
			log.info("Request from user " + userName + " for  Registration step1");
			Integer result = this.userService.initializeRegistration(email, userName, password, firstName, middleName,lastName);
			//added by thangaraj; not yet completed
//			this.refferralService.mapRefferralToUser(result, request);
			if (result > 0) {
				String status = "CONTINUE REGISTRATION";
				obj.accumulate("status", status);
				obj.accumulate("userid", result);
			} else {
				throw new UserNotSavedOrUpdatedException();
			}
		} catch (UserNotSavedOrUpdatedException e) {
			log.error("Error in Saving Or Updating User" + e.toString());
			String message = "Error While Registering User";
			obj.accumulate("status", message);
		} catch (EmailAlreadyExistsException e) {
			String message = "An Account Already Exist with Email Entered";
			log.error("email already exists" + e.toString());
			obj.accumulate("status", message);
		} catch (UserNameAlreadyExistsException e) {
			String message = "Username Already Exists";
			log.error("username already exists" + e.toString());
			obj.accumulate("status", message);
		} catch (Exception e) {
			log.error("Error in registration process" + e.toString());
			String message = "Something Went Wrong While Registering User";
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/*
	 * added by Soujanya on july 24,2014 method to complete registration
	 * 
	 * modified by Thangaraj on Jan 8,2015 : added device token to the
	 * registration process request
	 */
	@RequestMapping(value = "/completeregistration.htm")
	@ResponseBody
	public String completeRegistrationProcess(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("inside  completeRegistrationProcess()");
		JSONObject obj = new JSONObject();
		try {
			String status = "USER NOT REGISTERED";
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			String city = json.getString("city");
			String state = json.getString("state");
			String country = json.getString("country");
			String displayName = json.getString("displayname");
			String tradeEmail = json.getString("tradeemail");
			String tradePhone = json.getString("tradephone");
			String zip = json.getString("zip");
			Integer userId = json.getInt("userid");
			String phone = json.getString("phone");
			String addressline1 = json.getString("addressline1");
			String addressline2 = json.getString("addressline2");
			String deviceToken = json.getString("devicetoken");
			String deviceOSVersion = json.getString("deviceversion");
			log.info("Request from user " + userId + " for  Registration step2");
			Integer result = this.userService.completeRegistration(userId, city, state, country, displayName,
					tradeEmail, zip, tradePhone, phone, addressline1, addressline2, deviceToken);
			try {
				RefferralToken refferralToken = this.refferralService.getRefferralTokenByIP(request.getRemoteAddr());
				
				if(!refferralToken.getOsVersion().trim().equals(deviceOSVersion.trim()))
					throw new RefferralTokenNotFoundException();
				
				if(this.refferralService.updateRefferral(userId, refferralToken.getRefferralId().getUserId())){
					this.refferralService.deleteRefferralToken(refferralToken);
				obj.accumulate("refferralstatus","UPDATED");
				}
			} catch (RefferralTokenNotFoundException e1) {
				System.out.println("No refferrals found for this user");
				obj.accumulate("refferralstatus","NOTUPDATED");
			}
			if (result > 0) {
				status = "USER REGISTERED";
				obj.accumulate("status", status);
			} else {
				throw new UserNotSavedOrUpdatedException();
			}
		} catch (UserNotSavedOrUpdatedException e) {
			log.error("Error in Saving Or Updating User" + e);
			String message = "Error in Saving Or Updating User";
			obj.accumulate("status", message);
		} catch (UserNotFoundException e) {
			log.error("User Not Found To Complete Registration" + e);
			String message = "User Not Found";
			obj.accumulate("status", message);
		} catch (Exception e) {
			log.error("Error while completing registration process" + e);
			String message = "Error while completing registration process";
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;

	}

	/*
	 * added by Soujanya on july 28,2014 method to save referrals
	 */
	@RequestMapping(value = "/addreferral.htm")
	@ResponseBody
	public String addReferral(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside addReferral()");
		JSONObject obj = new JSONObject();
		try {
			String status = "REFERRAL NOT ADDED";
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			String email = json.getString("email");
			Integer userId = json.getInt("userid");
			String firstName = json.getString("firstname");
			String lastName = json.getString("lastname");
			String phoneNumber = json.getString("phonenumber");
			Integer result = this.userService.addReferral(userId, email, phoneNumber);
			if (result > 0) {
				status = "REFERRAL ADDED";
				obj.accumulate("status", status);
				obj.accumulate("userid", result);
			} else {
				throw new UserNotSavedOrUpdatedException();
			}
		} catch (UserNotSavedOrUpdatedException e) {
			String message = "Error: You Cannot Enter Yourself As Referral, Please Enter Other Existing Traidit Users Details";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (UserNotFoundException e) {
			String message = "Referral Details Entered Does Not Match with Our Records";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (Exception e) {
			String message = "Something Went Wrong While Adding Referrals";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/*
	 * added by Soujanya on july 28,2014 method to send list of plans
	 */
	@RequestMapping(value = "/sendplans.htm")
	@ResponseBody
	public String sendPlans(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("request : sendPlans");
		System.out.println("request sendPlans: ");
		JSONObject obj = new JSONObject();
		try {
			ArrayList<PlansDto> plansDto = this.userService.getAllPlans();
			ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();
			for (PlansDto plan : plansDto) {
				JSONObject planJSON = new JSONObject();
				planJSON.accumulate("planid", plan.getPlanId());
				planJSON.accumulate("planname", plan.getPlanName());
				planJSON.accumulate("price", plan.getPrice());
				planJSON.accumulate("subscriptiondays", plan.getSubscriptionDays());
				jsonList.add(planJSON);
			}
			obj.accumulate("planslist", jsonList);
		} catch (PlanNotFoundException e) {

			String message = "Plan Not Found";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (Exception e) {
			String message = "Error in Sending Plans";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/**
	 * 
	 * Created by Soujanya on Method for: Performing Social Login
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/handlesocialregistration.htm")
	@ResponseBody
	public String handleSocialRegistraion(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside handleSocialRegistration()");
		JSONObject obj = new JSONObject();
		try {
			String status = "ACCESS DENIED";
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			String firstName = json.getString("firstname");
			String lastName = json.getString("lastname");
			String middleName = json.getString("middlename");
			String email = json.getString("email");
			Integer result = this.userService.processSocialRegistration(firstName, middleName, lastName, email);
			UserDto userDto = this.userService.getUserbyUserId(result);
			if (result > 0) {
				status = "ACCESS GRANTED";
				obj.accumulate("status", status);
				obj.accumulate("userid", result);
				obj.accumulate("userplan", userDto.getPlans().getPlanId());
			} else {
				throw new UserNotSavedOrUpdatedException();
			}
		} catch (UserNotSavedOrUpdatedException e) {
			log.error("Error While Registering User from Social Login, Please Try Again" + e.toString());
			String message = "Error While Registering User from Social Login, Please Try Again";
			obj.accumulate("status", message);
		} catch (Exception e) {
			log.error("Error While Performing Social Login" + e.toString());
			e.printStackTrace();
			String message = "Error in Social Login";
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/**
	 * WORK FLOW FOR FORGOT PASSWORD APP
	 * 
	 * 1. Get Email from App, Validate it and Send SMS Code using Twilio API 2.
	 * Validate SMS Code entered by mobile app User. 3. Display Reset Password
	 * Page 4. Reset Password
	 */

	/*
	 * added by Soujanya on august 4th,2014 method to initiate forgot password
	 */
	@RequestMapping(value = "/initiateforgotpassword.htm")
	@ResponseBody
	public String initiateForgotPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside initiateForgotPassword()");
		JSONObject obj = new JSONObject();
		try {
			String status = "FAIL: CANNOT PROCESS FORGOT PASSWORD MAIL";
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			String email = json.getString("email");
			System.out.println(json.toString());
			log.info(json.toString());
			Integer result = this.userService.sendPasswordResetSMS(email);
			if (result > 0) {
				status = "Please Enter Password Reset Code Sent To Your Registered Mobile";
				obj.accumulate("status", status);
			} else {
				throw new UserNotFoundException();
			}
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			String message = "No User Found with Entered Email";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (Exception e) {
			e.printStackTrace();
			String message = "Error While Retreiving Password";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/**
	 * 
	 * Created by Method for: Validating Password Token
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/validatePasswordResetToken.htm")
	@ResponseBody
	public String validatePasswordResetToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside validatePasswordResetToken()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			String passwordResetToken = json.getString("passwordResetToken");
			Integer userId = this.userService.getUserIdByPasswordResetToken(passwordResetToken);
			obj.accumulate("userId", userId);
			obj.accumulate("status", "Valid Token");
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			log.error("Invalid Token For User" + e.toString());
			obj.accumulate("status", "Invalid Token");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Some error occurred while validating password reset token" + e.toString());
			obj.accumulate("status", "Some error occurred while validating password reset token");
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/*
	 * added by Soujanya on july 18,2014 method to change password by user
	 */
	@RequestMapping(value = "/passwordresetbyuser.htm")
	@ResponseBody
	public String passwordReset(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside getTradePhoneToEdit()");
		JSONObject obj = new JSONObject();
		try {
			String status = "Password change failed";
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			Integer userId = Integer.parseInt(json.getString("userId"));
			UserDto userDto = this.userService.getUserbyUserId(userId);
			String newPassword = json.getString("password");
			String userName = userDto.getUserName();
			Integer result = this.userService.updatePassword(userName, newPassword);
			if (result > 0) {
				status = "SAVED AND UPDATED PASSWORD";
				obj.accumulate("status", status);
			} else {
				throw new UserNotSavedOrUpdatedException();
			}
		} catch (UserNotFoundException e) {
			String message = "User Not Found From Our Records";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (UserNotSavedOrUpdatedException e) {
			String message = "Error While Resetting Password";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (Exception e) {
			String message = "Some Error Occurred While Resetting Password";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/*
	 * added by Soujanya on july 17,2014 method to retrieve user profile for
	 * editing sends the profile details as a json array
	 */
	@RequestMapping(value = "/getlogininfo.htm")
	@ResponseBody
	public String editUserProfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside editUserProfile()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			Integer userId = json.getInt("userid");
			UserDto userDto = this.userService.getUserbyUserId(userId);
			JSONObject userJSON = new JSONObject();
			// userJSON.accumulate("username", userDto.getUserName());
			// userJSON.accumulate("email", userDto.getEmail());
			userJSON.accumulate("displayname", userDto.getDisplayName());
			userJSON.accumulate("firstname", userDto.getFirstName());
			userJSON.accumulate("middlename", userDto.getMiddleName());
			userJSON.accumulate("lastname", userDto.getLastName());
			userJSON.accumulate("plan", userDto.getPlans().getPlanName());
			userJSON.accumulate("plan expiry date", userDto.getAccountExpiryDate());
			userJSON.accumulate("userid", userDto.getUserId());
			ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();
			jsonList.add(userJSON);

			obj.accumulate("userdetails", jsonList);
		} catch (UserNotFoundException e) {

			String message = "User Not Found From Our Records";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (Exception e) {
			String message = "Some Error Occurred While Editing User Profile";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/*
	 * added by Soujanya on july 17,2014 method to save edited user profile to
	 * DB
	 */
	@RequestMapping(value = "/saveediteduserprofile.htm")
	@ResponseBody
	public String saveEditedUserProfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside saveEditedUserProfile()");
		JSONObject obj = new JSONObject();
		try {
			String status = "NOT SAVED";
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			Integer userId = json.getInt("userid");
			String firstName = json.getString("firstname");
			String lastName = json.getString("lastname");
			String displayName = json.getString("displayname");
			String city = json.getString("city");
			String state = json.getString("state");
			String country = json.getString("country");
			Long rating = json.getLong("rating");
			String zip = json.getString("zip");
			Integer result = this.userService.saveEditedUserProfileToDB(userId, firstName, lastName, displayName, city,
					state, country, rating, zip);
			if (result > 0) {
				status = "SAVED AND UPDATED USER";
				obj.accumulate("status", status);
			} else {
				throw new UserNotSavedOrUpdatedException();
			}

		} catch (UserNotSavedOrUpdatedException e) {

			String message = "Error in Saving Or Updating User";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (UserNotFoundException e) {

			String message = "User Not Found From Our Records";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (Exception e) {

			String message = "Error in Editing Details";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/*
	 * added by Soujanya on July 18,2014 method to change trade email of user
	 */
	@RequestMapping(value = "/getusercontactprofiletoedit.htm")
	@ResponseBody
	public String getUserContactProfileToEdit(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("inside getUserContactProfileToEdit()");
		JSONObject obj = new JSONObject();
		try {

			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			Integer userId = json.getInt("userid");
			UserDto userDto = this.userService.getUserbyUserId(userId);
			System.out.println(userDto.getUserName());
			JSONObject userJSON = new JSONObject();
			userJSON.accumulate("phone", userDto.getPhoneNo());
			userJSON.accumulate("tradephone", userDto.getTradePhoneNo());
			userJSON.accumulate("email", userDto.getEmail());
			userJSON.accumulate("tradeemail", userDto.getTradeEmail());
			// userJSON.accumulate("street", userDto.getStreet());
			userJSON.accumulate("state", userDto.getState());
			userJSON.accumulate("city", userDto.getCity());
			userJSON.accumulate("zip", userDto.getZip());
			userJSON.accumulate("country", userDto.getCountry());
			userJSON.accumulate("userid", userDto.getUserId());
			userJSON.accumulate("addressline1", userDto.getAddressline1());
			userJSON.accumulate("addressline2", userDto.getAddressline2());

			ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();
			jsonList.add(userJSON);

			obj.accumulate("usercontactinformation", jsonList);

		} catch (UserNotFoundException e) {

			String message = "User Not Found From Our Records";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (Exception e) {

			String message = "Some Error Occurred While Fetching Contact Information";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/*
	 * added by Soujanya on july 18,2014 method to save updated trade email to
	 * db
	 */
	@RequestMapping(value = "/updatecontactprofileoftrader.htm")
	@ResponseBody
	public String updateContactProfileOfTrader(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("inside updateContactProfileOfTrader()");
		JSONObject obj = new JSONObject();

		try {
			String status = "NOT SAVED";
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			Integer userId = json.getInt("userid");
			String city = json.getString("city");
			String state = json.getString("state");
			String country = json.getString("country");
			String zip = json.getString("zip");
			String email = json.getString("email");
			String phone = json.getString("phone");
			// String street=json.getString("street");
			String tradeEmail = json.getString("tradeemail");
			String tradePhone = json.getString("tradephone");
			String addressline1 = json.getString("addressline1");
			String addressline2 = json.getString("addressline2");
			System.out.println(userId);
			Integer result = this.userService.updateContactProfileOfUser(userId, email, tradeEmail, phone, tradePhone,
					zip, city, state, country, addressline1, addressline2);

			if (result > 0) {
				status = "SUCCESS: UPDATED YOUR PROFILE";
				obj.accumulate("status", status);
			} else {
				throw new UserNotSavedOrUpdatedException();
			}
		}

		catch (UserNotSavedOrUpdatedException e) {

			String message = "Error in the Process of Updating Profile";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (UserNotFoundException e) {

			String message = "User Not Found from Our Records";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (Exception e) {

			String message = "Some Error Occurred While Updating Profile";
			e.printStackTrace();
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/*
	 * added by Soujanya on July 18,2014 method to change trade phone of user
	 */
	@RequestMapping(value = "/gettradephonetoedit.htm")
	@ResponseBody
	public String getTradePhoneToEdit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside getTradePhoneToEdit()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			Integer userId = json.getInt("userid");
			UserDto userDto = this.userService.getUserbyUserId(userId);
			System.out.println(userDto.getUserName());
			System.out.println(userDto.getTradePhoneNo());
			JSONObject userJSON = new JSONObject();
			System.out.println();
			userJSON.accumulate("tradephone", userDto.getTradePhoneNo());
			ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();
			jsonList.add(userJSON);

			obj.accumulate("usertradephone", jsonList);

		} catch (UserNotFoundException e) {

			String message = "Specified User Not Found From Our Records";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (Exception e) {

			String message = "Some Error Occurred While Editing Trade Phone";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/*
	 * added by Soujanya on july 18,2014 method to save updated trade phone to
	 * db
	 */
	@RequestMapping(value = "/gettradephonetosave.htm")
	@ResponseBody
	public String saveEditedTradePhone(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside saveEditedTradeEmail()");
		JSONObject obj = new JSONObject();
		try {
			String status = "NOT SAVED";
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			Integer userId = json.getInt("userid");
			String tradePhone = json.getString("tradephone");
			System.out.println(userId);
			Integer result = this.userService.saveEditedTradePhoneToDB(userId, tradePhone);
			if (result > 0) {
				status = "SAVED AND UPDATED USER";
				obj.accumulate("status", status);
			} else {
				throw new UserNotSavedOrUpdatedException();
			}
		}

		catch (UserNotSavedOrUpdatedException e) {

			String message = "Error in the Process of Updating Trade Phone Of User";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (UserNotFoundException e) {

			String message = "Specified User Not Found From Our Records";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (Exception e) {

			String message = "Some Error Occurred While Updating Trade Phone";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/**
	 * method to add user's restriction on traders ratings dont show traders
	 * with rating less than the selected restriction rating
	 * 
	 * @input userid, ratingrestriction
	 * @return success , failure
	 * @throws Exception
	 * @author Soujanya
	 * @since nov 18,104
	 */
	@RequestMapping(value = "/addratingrestriction.htm")
	@ResponseBody
	public String addRatingRestriction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside addRatingRestriction()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			Integer userId = json.getInt("userid");
			String ratingRestriction = json.getString("ratingrestriction");
			if (ratingRestriction.contains("Not Satisfied")) {
				ratingRestriction = "Unsatisfied";
			}
			Integer result = this.userService.addRatingRestriction(userId, ratingRestriction);
			if (result > 0) {
				obj.accumulate("status", "SUCCESS: RATING RESTRICTION APPLIED ON TRADERS");
				return obj.toString();
			} else {
				throw new Exception();
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			String message = "Error in the process of adding rating restriction";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
	}

	/*
	 * added by Soujanya on july 18,2014 method to change password by user
	 */
	@RequestMapping(value = "/passwordchangebyuser.htm")
	@ResponseBody
	public String passwordChangeByUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside getTradePhoneToEdit()");
		JSONObject obj = new JSONObject();
		try {
			String status = "Password change failed";
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			Integer userId = Integer.parseInt(json.getString("userid"));

			String oldPassword = json.getString("oldpassword");
			String newPassword = json.getString("newpassword");

			Integer result = this.userService.updatePasswordChange(userId, oldPassword, newPassword);
			if (result > 0) {
				status = "SAVED AND UPDATED PASSWORD";
				obj.accumulate("status", status);
			} else {
				throw new UserNotSavedOrUpdatedException();
			}
		} catch (PasswordsDoNotMatchException e) {
			String message = "Passwords Do Not Match, Please Enter the correct Old Password";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (UserNotSavedOrUpdatedException e) {
			String message = "Error While Resetting Password";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (Exception e) {
			e.printStackTrace();
			String message = "Some Error Occurred While Resetting Password";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}
	/**
	 * Created By Bhagya on Feb10th,2015
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * Method for Checking the authorized user based on userId and password
	 */
	@RequestMapping(value ="/passwordauthentication.htm")
	@ResponseBody
	public String userPasswordVerification(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("inside userPasswordVerification()");
		JSONObject obj = new JSONObject();
		try{
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			Integer userId=json.getInt("userid");
			String password=json.getString("password");
			String message=this.userService.checkingUserPasswordAuthentication(userId, password);
			obj.accumulate("status", message);
		}
		catch(UserNotFoundException e){
			String message = "User Does Not Exist";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		}
		catch(Exception e){
			e.printStackTrace();
			String message = "Some Error Occurred While Authenticating Password";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

}
