/**
 * Created by     : Soujanya
 * Created Date	  : november 25,2014
 * file Name	  : UserMessagesController.java
 * Purpose		  : Handles User Messages  Related Operations
 * Type			  : Controller
 * 
 */

package org.kns.traidit.frontend.mobile.controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.trade.exception.MessageNotFoundException;
import org.kns.traidit.backend.trade.exception.MessageNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.UserMessageNotFoundException;
import org.kns.traidit.backend.user.exception.UserMessageNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.frontend.common.utility.JsonParser;
import org.kns.traidit.frontend.user.dto.UserMessagesDto;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

@Controller("UserMessagesController")
public class UserMessagesController {

	private static Logger log = Logger.getLogger(UserMessagesController.class);

	@Resource(name = "userService")
	private UserService userService;

	/**
	 * @author Soujanya
	 * @since october 1st,2014
	 * @input senderid, receiverid, message, subject purpose: method to send
	 *        messages to other users/vendors
	 * @return messageid, status
	 * @throws UserMessageNotSavedOrUpdatedException
	 */
	@RequestMapping(value = "/sendusermessage.htm")
	@ResponseBody
	public String sendUserMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside sendUserMessage()");
		JSONObject obj = new JSONObject();
		try {
			String status = "MESSAGE NOT SENT";
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());

			Integer senderId = json.getInt("senderid");
			Integer receiverId = json.getInt("receiverid");
			String message = json.getString("message");
			String subject = json.getString("subject");

			Integer result = this.userService.saveUserMessage(senderId, receiverId, message, subject, false);
			if (result > 0) {
				status = "MESSAGE SENT SUCCESSFULLY";
				obj.accumulate("status", status);
			} else {
				throw new UserMessageNotSavedOrUpdatedException();
			}

		}

		catch (UserMessageNotSavedOrUpdatedException e) {
			String message = "Error in Sending Message, try again";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
		}

		catch (Exception e) {
			String message = "Error in the process of sending message";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/**
	 * --------------------------------------------------end of
	 * "sendUserMessage() Service"
	 * ------------------------------------------------
	 * -------------------------------
	 **/

	/**
	 * method to get list of user messages based on flag: "unread" "read" "sent"
	 * "all" "deleted" "drafts"
	 * 
	 * @input: userid, flag
	 * @return arraylist of user messages
	 * @throws UserMessageNotFoundException
	 * @author Soujanya
	 * @since nov 18,2014
	 */
	@RequestMapping(value = "/listusermessages.htm")
	@ResponseBody
	public String listUserMessages(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside listUserMessages()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			
			Integer userId = json.getInt("userid");
			String flagFromMobile = json.getString("flag");
			String flag = flagFromMobile.toLowerCase();
			ArrayList<UserMessagesDto> userMessagesDtos = this.userService.getUserMessagesFromDB(userId, flag);
			ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();
			for (UserMessagesDto userMessageDto : userMessagesDtos) {
				JSONObject messageJSON = new JSONObject();

				messageJSON.accumulate("messageid", userMessageDto.getId());

				// if there's no subject for user message, populate json with
				// "No Subject"
				if (userMessageDto.getSubject() != null && !userMessageDto.getSubject().isEmpty()) {
					messageJSON.accumulate("subject", userMessageDto.getSubject());
				} else {
					messageJSON.accumulate("subject", "No Subject");
				}

				// if there's no message or message is empty populate json with
				// "Empty Message"
				if (!userMessageDto.getMessage().isEmpty() && userMessageDto.getMessage() != null) {

					messageJSON.accumulate("message", userMessageDto.getMessage());
				}

				else {
					messageJSON.accumulate("message", "Empty Message");
				}

				// if flag is read,unread,all,deleted
				// populate json with sender's name
				// also if sender is the logged in user, populate json sender's
				// name with "ME"
				if ((flag.equals("read")) || (flag.equals("unread")) || (flag.equals("all"))
						|| (flag.equals("deleted"))) {

					if (userMessageDto.getSender().getUserId().equals(userId)) {
						messageJSON.accumulate("sender", "ME");
					} else {
						messageJSON.accumulate("sender", userMessageDto.getSender().getUserName());
					}

				}

				// if flag is sent, drafts
				// populate json with receiver's name
				if ((flag.equals("sent")) || (flag.equals("drafts"))) {
					messageJSON.accumulate("receiver", userMessageDto.getReceiver().getUserName());
				}
				messageJSON.accumulate("date", userMessageDto.getDate());

				jsonList.add(messageJSON);
			}
			obj.accumulate("usermessages", jsonList);
			return obj.toString();
		} catch (UserMessageNotFoundException e) {
			e.printStackTrace();
			log.error("There are no messages" + e.toString());
			obj.accumulate("status", "There Are No Messages");
			return obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Some Error Occurred While Fetching Messages" + e.toString());
			obj.accumulate("status", "Some Error Occurred While Fetching Messages");
			return obj.toString();
		}

	}

	/**
	 * --------------------------------------------------end of
	 * "listUserMessages() Service"
	 * ----------------------------------------------
	 * ---------------------------------
	 **/

	/**
	 * method to get user messages count consists the count of user messages by
	 * state: "unread" "read" "sent" "all" "deleted" "drafts"
	 * 
	 * @input: userid
	 * @return string of counts
	 * @throws UserMessageNotFoundException
	 * @author Soujanya
	 * @since nov 24,2014
	 * 
	 */
	@RequestMapping(value = "/getusermessagescount.htm")
	@ResponseBody
	public String getUserMessagesCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside getUserMessagesCount()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			
			Integer userId = json.getInt("userid");

			Map<String, Integer> userMessagesCount = this.userService.getCountOfUserMessages(userId);

			// populate map of user message count to json
			// populates json with count of unread,read,sent,all,deleted,drafts
			// parsing the map of counts
			if (!userMessagesCount.isEmpty()) {
				for (Map.Entry<String, Integer> entry : userMessagesCount.entrySet()) {
					String state = entry.getKey();
					Integer count = entry.getValue();

					obj.accumulate(state, count);

				}
				return obj.toString();
			} else {
				throw new UserMessageNotFoundException();
			}

		} catch (UserMessageNotFoundException e) {
			log.error("There are no messages" + e.toString());
			e.printStackTrace();
			obj.accumulate("status", "There Are No Messages");
			return obj.toString();
		} catch (Exception e) {
			log.error("Some Error Occurred While Fetching Count Of Messages" + e.toString());
			e.printStackTrace();
			obj.accumulate("status", "Some Error Occurred While Fetching Count Of Messages");
			return obj.toString();
		}

	}

	/**
	 * --------------------------------------------------end of
	 * "getUserMessagesCount() Service"
	 * ------------------------------------------
	 * -------------------------------------
	 **/

	/**
	 * method to get particular user message by message id
	 * 
	 * @input: messageid
	 * @return user message
	 * @throws UserMessageNotFoundException
	 * @author Soujanya
	 * @since nov 24,2014
	 */
	@RequestMapping(value = "/getusermessageByid.htm")
	@ResponseBody
	public String getUserMessageById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside getUserMessageById()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			
			Integer messageId = json.getInt("messageid");
			Integer userId = json.getInt("userid");
			UserMessagesDto userMessageDto = this.userService.getUserMessageById(messageId);

			obj.accumulate("subject", userMessageDto.getSubject());
			obj.accumulate("message", userMessageDto.getMessage());
			obj.accumulate("sender", userMessageDto.getSender().getUserName());
			obj.accumulate("receiver", userMessageDto.getReceiver().getUserName());
			obj.accumulate("messageid", userMessageDto.getId());
			obj.accumulate("date", userMessageDto.getDate());

			if (userMessageDto.getSender().getUserId().equals(userId)) {
				obj.accumulate("flag", "sentmessage");
			} else if (userMessageDto.getReceiver().getUserId().equals(userId)) {
				obj.accumulate("flag", "receivedmessage");
			}
			return obj.toString();
		}

		catch (UserMessageNotFoundException e) {
			log.error("Message Not Found" + e.toString());
			obj.accumulate("status", "Error while Retrieving Message");
			return obj.toString();
		} catch (Exception e) {
			log.error("Some Error Occurred While Fetching Message" + e.toString());
			obj.accumulate("status", "Some Error Occurred While Fetching Message");
			return obj.toString();
		}

	}

	/**
	 * --------------------------------------------------end of
	 * "getUserMessageById() Service"
	 * --------------------------------------------
	 * -----------------------------------
	 **/

	/**
	 * method to delete a message by message id
	 * 
	 * @input: messageid, userid
	 * @return success status
	 * @throws UserMessageNotSavedOrUpdatedException
	 * @author Soujanya
	 * @since nov 24,2014
	 */
	@RequestMapping(value = "/deleteusermessage.htm")
	@ResponseBody
	public String deleteUserMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside deleteUserMessage()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			Integer messageId = json.getInt("messageid");
			Integer userId = json.getInt("userid");
			Integer deletedResult = this.userService.deleteUserMessage(userId, messageId);
			if (deletedResult > 0) {
				obj.accumulate("status", "Message Deleted Successfully");
			} else {
				throw new UserMessageNotSavedOrUpdatedException();
			}
		}

		catch (UserMessageNotSavedOrUpdatedException e) {
			log.error("Error in Deleting Message" + e.toString());
			obj.accumulate("status", "Error in Deleting Message");
		} catch (Exception e) {
			log.error("Some Error Occurred While Deleting Message" + e.toString());
			obj.accumulate("status", "Some Error Occurred While Deleting Message");
		}
		return obj.toString();

	}

	/**
	 * --------------------------------------------------end of
	 * "deleteUserMessage() Service"
	 * ----------------------------------------------
	 * ---------------------------------
	 **/

	/**
	 * method to save user message as draft
	 * 
	 * @input senderid,receiverid,message,subject
	 * @throws UserMessageNotSavedOrUpdatedException
	 * @return success status
	 * @author Soujanya
	 * @since nov 24, 2014
	 */
	@RequestMapping(value = "/saveusermessageasdraft.htm")
	@ResponseBody
	public String saveUserMessageAsDraft(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("inside saveUserMessageAsDraft()");
		JSONObject obj = new JSONObject();
		String status = null;
		try {
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			
			Integer senderId = json.getInt("senderid");
			Integer receiverId = json.getInt("receiverid");
			String message = json.getString("message");
			String subject = json.getString("subject");
			Integer result = this.userService.saveUserMessage(senderId, receiverId, message, subject, true);
			if (result > 0) {
				status = "MESSAGE SAVED AS DRAFT";
				obj.accumulate("status", status);
			} else {
				throw new UserMessageNotSavedOrUpdatedException();
			}
		}

		catch (UserMessageNotSavedOrUpdatedException e) {
			String message = "Error in Saving Draft, try again";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
		}

		catch (Exception e) {
			String message = "Error in the process of saving draft";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/**
	 * --------------------------------------------------end of
	 * "saveUserMessageAsDraft() Service"
	 * ----------------------------------------
	 * ---------------------------------------
	 * @throws JSONException 
	 **/

	/**
	 * Lists users with their user id for sending user messages
	 * 
	 * @param request
	 * @param response
	 * @throws JSONException
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 13-Jan-2015
	 */
	@RequestMapping(value = "/listmessagecontacts.htm")
	@ResponseBody
	public String listMessageContacts(HttpServletRequest request, HttpServletResponse response) throws JSONException {

		log.info("inside listMessageContacts()");
		JSONObject obj = new JSONObject();
		try { 
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			
			Integer userId = json.getInt("userid");
			Map<Integer,String> users = this.userService.listMessageContacts(userId);
			ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();
			for(Entry<Integer, String> user : users.entrySet()){
				JSONObject userJson = new JSONObject();
				userJson.accumulate("userid",user.getKey());
				userJson.accumulate("username",user.getValue());
				jsonList.add(userJson);
			}
			obj.accumulate("users", jsonList);
		} catch (UserNotFoundException e) {
			String message = "No Users found";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
		}
		
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws JSONException
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 29-Jan-2015
	 */
	@ResponseBody
	@RequestMapping(value="/emptytrash.htm")
	public String emptyTrash(HttpServletRequest request, HttpServletResponse response) throws JSONException{
		log.info("inside emptyTrash()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());

			Integer userId = json.getInt("userid");
			this.userService.emptyTrash(userId);
			obj.accumulate("status", "Trash empty");
			
		}
		catch (MessageNotFoundException e) {
			String message = "Trash is empty already";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
		}
		catch (MessageNotSavedOrUpdatedException e) {
			String message = "Could not able to delete message";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
		}
		catch (Exception e) {
			String message = "Error in the process of empty messages trash";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}
	
}
