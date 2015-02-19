/**
 *  Created by     : Soujanya
 * Created Date	  : July 4th,2014
 * file Name	  : UserDashboardController.java
 * Purpose		  :Handles User Related Operations
 * Type			  : Controller
 * 
 */

package org.kns.traidit.frontend.mobile.controller;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.item.exception.ItemNotFoundException;
import org.kns.traidit.backend.notification.exception.NotificationSettingsNotSavedException;
import org.kns.traidit.backend.trade.exception.MessageNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.frontend.commission.service.CommissionService;
import org.kns.traidit.frontend.common.utility.JsonParser;
import org.kns.traidit.frontend.item.dto.ItemsDto;
import org.kns.traidit.frontend.item.service.ItemService;
import org.kns.traidit.frontend.notification.service.NotificationService;
import org.kns.traidit.frontend.trade.dto.TradeMessagesDto;
import org.kns.traidit.frontend.trade.service.TradeService;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

@Controller("UserDashboardController")
public class UserDashboardController {

	private static Logger log = Logger.getLogger(UserDashboardController.class);

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "tradeService")
	private TradeService tradeService;

	@Resource(name = "itemService")
	private ItemService itemService;

	@Resource(name = "commissionService")
	private CommissionService commissionService;

	@Resource(name = "notificationService")
	private NotificationService notificationService; 

	/*
	 * added by Soujanya on July 11,2014 Method to display user information
	 * based on userid
	 */
	@RequestMapping(value = "/displayuserdetails.htm")
	@ResponseBody
	public String displayUserDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside displayUserDetails()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			Integer userId = json.getInt("userid");
			System.out.println(userId);
			UserDto userDto = this.userService.getUserbyUserId(userId);
			System.out.println(userDto.getUserName());
			JSONObject userJSON = new JSONObject();
			userJSON.accumulate("username", userDto.getUserName());
			userJSON.accumulate("email", userDto.getEmail());
			userJSON.accumulate("displayname", userDto.getDisplayName());
			userJSON.accumulate("firstname", userDto.getFirstName());
			userJSON.accumulate("lastname", userDto.getLastName());
			userJSON.accumulate("rating", userDto.getRating());
			userJSON.accumulate("city", userDto.getCity());
			userJSON.accumulate("state", userDto.getState());
			userJSON.accumulate("country", userDto.getCountry());
			userJSON.accumulate("zip", userDto.getZip());
			ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();
			jsonList.add(userJSON);
			// JSONArray array=new JSONArray(jsonList);
			JSONObject output = new JSONObject();
			output.accumulate("userdetails", jsonList);
			return output.toString();
		} catch (UserNotFoundException e) {

			String message = "User Not Found";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		} catch (Exception e) {

			String message = "Some Error Occurred While Retrieving User Details";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
	}

	/*
	 * added by Soujanya on July 11,2014 method to display trade messages based
	 * on user id
	 */
	@RequestMapping(value = "/displayreceivedtrademessages.htm")
	@ResponseBody
	public String displayReceivedMessages(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside displayusermessages()");
		JSONObject obj = new JSONObject();

		try {
			JSONObject json = JsonParser.getJson(request, response);
			Integer receiverId = json.getInt("receiverid");
			ArrayList<TradeMessagesDto> tradeMessages = this.tradeService.viewTradeMessages(null, null, receiverId);
			System.out.println(tradeMessages);
			ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();
			for (TradeMessagesDto tradeMessage : tradeMessages) {
				JSONObject messageJSON = new JSONObject();

				messageJSON.accumulate("message", tradeMessage.getMessage());
				messageJSON.accumulate("sender", tradeMessage.getSender().getFirstName());

				jsonList.add(messageJSON);

			}
			JSONObject output = new JSONObject();
			output.accumulate("trademessages", jsonList);
			return output.toString();

		} catch (MessageNotFoundException e) {

			String message = "Trade Messages Not Found for This Receiver";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		} catch (Exception e) {

			String message = "Some Error Occurred While Retrieving Trade Messages";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}

	}

	/*
	 * added by Soujanya on july 11,2014 method to display vendor ratings
	 */
	@RequestMapping(value = "/displayuserratings.htm")
	@ResponseBody
	public String displayUserRatings(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside displayUserRatings()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			Integer userId = json.getInt("userid");
			System.out.println(userId);
			UserDto userDto = this.userService.getUserbyUserId(userId);
			System.out.println(userDto.getUserName());
			JSONObject userJSON = new JSONObject();
			// userJSON.accumulate("firstname", userDto.getFirstName());
			userJSON.accumulate("rating", userDto.getRating());
			// ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			// jsonList.add(userJSON);
			// JSONArray array=new JSONArray(jsonList);
			// JSONObject output=new JSONObject();
			// output.accumulate("ratings", jsonList);
			return userJSON.toString();

		} catch (UserNotFoundException e) {

			String message = "User Not Found";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		} catch (Exception e) {

			String message = "Some Error Occurred While Retrieving User Ratings";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}

	}

	/*
	 * added by Soujanya on July 11,2014 method to display item ratings
	 */
	@RequestMapping(value = "/displayitemratings.htm")
	@ResponseBody
	public String displayItemRatings(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside displayItemRatings()");
		JSONObject obj = new JSONObject();

		try {
			JSONObject json = JsonParser.getJson(request, response);
			Integer itemId = json.getInt("itemid");
			System.out.println(itemId);
			ItemsDto itemsDto = this.itemService.getItemById(itemId);
			System.out.println(itemsDto.getItemName());
			JSONObject itemJSON = new JSONObject();
			// userJSON.accumulate("firstname", userDto.getFirstName());
			itemJSON.accumulate("rating", itemsDto.getRatings());
			// ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			// jsonList.add(userJSON);
			// JSONArray array=new JSONArray(jsonList);
			// JSONObject output=new JSONObject();
			// output.accumulate("ratings", jsonList);
			return itemJSON.toString();

		} catch (ItemNotFoundException e) {

			String message = "Item Not Found";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		} catch (Exception e) {

			String message = "Some Error Occurred While Retrieving Item Ratings";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}

	}

	/**
	 * method to save user suggested improvement added by Soujanya on december
	 * 10,2014
	 * 
	 */
	@RequestMapping(value = "/saveusersuggestedimprovement.htm")
	@ResponseBody
	public String saveUserSuggestedImprovement(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("inside saveUserSuggestedImprovement()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			Integer userId = json.getInt("userid");
			String suggestedImprovement = json.getString("suggestion");
			Integer result = this.userService.saveUserSuggestedImprovement(userId, suggestedImprovement);
			if (result > 0) {
				obj.accumulate("status", "Thanks For The Suggestion");
				return obj.toString();
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			String message = "Some Error Occurred, Try Again";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
	}

	/**
	 * method to save user reported bugs added by Soujanya on december 10,2014
	 * 
	 */
	@RequestMapping(value = "/saveuserreportedbugs.htm")
	@ResponseBody
	public String saveUserReportedBug(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside saveUserReportedBug()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			Integer userId = json.getInt("userid");
			String reportedBug = json.getString("bug");
			Integer result = this.userService.saveUserSuggestedBugs(userId, reportedBug);
			if (result > 0) {
				obj.accumulate("status", "Thanks For Reporting The Bug, We will Take Care Of it");
				return obj.toString();
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			String message = "Some Error Occurred, Try Reporting Again";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
	}

	/**
	 * method to save user review added by Soujanya on december 10,2014
	 * 
	 */
	@RequestMapping(value = "/saveuserreviews.htm")
	@ResponseBody
	public String saveUserReviews(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside saveUserReviews()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			Integer userId = json.getInt("userid");
			String review = json.getString("review");
			Integer result = this.userService.saveUserReviews(userId, review);
			if (result > 0) {
				obj.accumulate("status", "Thanks For Your Review");
				return obj.toString();
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			String message = "Some Error Occurred, Try Reviewing Again";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
	}

	/**
	 * method to save user reported abuse added by Soujanya on jan 2,2015
	 * 
	 */
	@RequestMapping(value = "/saveuserreportedabuse.htm")
	@ResponseBody
	public String saveUserReportedAbuse(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside saveUserReportedAbuse()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			Integer userId = json.getInt("userid");
			Integer vendorId = json.getInt("vendorid");
			String comment = null;
			Integer result = this.userService.saveUserReportedAbuse(userId, vendorId, comment);
			if (result > 0) {
				obj.accumulate("status", "Report Submitted, Sorry for the inconvenience");
				return obj.toString();
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			String message = "Some Error Occurred, Try Reporting Again";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
	}

	/**
	 * returns json array having counts of trades, messages and commissions to
	 * display in dashboard
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @throws JSONException
	 * @since 06-Jan-2015
	 */
	@RequestMapping(value = "/getcountsfordashboard.htm")
	@ResponseBody
	public String getCountsForDashBoard(HttpServletRequest request, HttpServletResponse response) throws JSONException {
		log.info("inside getCountsForDashBoard()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			Integer userId = json.getInt("userid");
			UserDto user = new UserDto();
			user.setUserId(userId);
			Map<String, Integer> userMessagesCount = this.userService.getCountOfUserMessages(userId);
			obj.accumulate("messagecount", userMessagesCount.get("unread"));
			obj.accumulate("tradecount", this.tradeService.getNewTradesCountOfUser(userId));
			obj.accumulate("commission", this.commissionService.getCommissionByUser(user));
		} catch (JSONException e) {
			log.error("JSON Exception");
			obj.accumulate("status", "some error occured");
		}
		String resultString = obj.toString();
		log.info("response : " + resultString);
		System.out.println("response : " + resultString);
		return resultString;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws JSONException
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 08-Jan-2015
	 */
	@ResponseBody
	@RequestMapping(value = "/updatenotificationsounds.htm")
	public String updateNotificationSounds(HttpServletRequest request, HttpServletResponse response) throws JSONException{
		log.info("inside updatenotificationsounds()");
		JSONObject obj = new JSONObject();
		JSONObject json = JsonParser.getJson(request, response);
		log.info("request : " + json.toString());
		System.out.println("request : " + json.toString());
		try{
			Integer userId 					= json.getInt("userid");
			String sound 					= json.getString("sound");
			String soundFor					= json.getString("soundfor");
			
			this.notificationService.updateNotificationSettings(userId, sound, soundFor, null);
			obj.accumulate("status", "Settings Updated successfully");
		}
		catch(NotificationSettingsNotSavedException e){
			obj.accumulate("status", "could not update Settings, try again later");
		}
		String resultString = obj.toString();
		log.info("response : " + resultString);
		System.out.println("response : " + resultString);
		return resultString;
	}	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws JSONException
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 09-Jan-2015
	 */
	@ResponseBody
	@RequestMapping(value = "/updatenotificationsettings.htm")
	public String updateNotificationSettings(HttpServletRequest request, HttpServletResponse response) throws JSONException{
		log.info("inside updatenotificationsettings()");
		JSONObject obj = new JSONObject();
		JSONObject json = JsonParser.getJson(request, response);
		log.info("request : " + json.toString());
		System.out.println("request : " + json.toString());
		try{
			Integer userId 					= json.getInt("userid");
			Boolean notificationSoundOn 	= json.getBoolean("notificationsoundon");
			
			this.notificationService.updateNotificationSettings(userId, null, null, notificationSoundOn);
			obj.accumulate("status", "Settings Updated successfully");
		}
		catch(NotificationSettingsNotSavedException e){
			obj.accumulate("status", "could not update Settings, try again later");
		}
		String resultString = obj.toString();
		log.info("response : " + resultString);
		System.out.println("response : " + resultString);
		return resultString;
	}	

}
