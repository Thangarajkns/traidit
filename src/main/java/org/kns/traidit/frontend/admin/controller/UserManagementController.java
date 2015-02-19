package org.kns.traidit.frontend.admin.controller;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.admin.exception.AllUserNotificationNotFoundException;
import org.kns.traidit.backend.admin.exception.AllUserNotificationNotSavedException;
import org.kns.traidit.backend.admin.model.AllUserNotification;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotSavedOrUpdatedException;
import org.kns.traidit.frontend.admin.dto.AllUserNotificationFormBean;
import org.kns.traidit.frontend.admin.service.AdminUserManagementService;
import org.kns.traidit.frontend.payments.dto.UserSubscriptionPaymentsDto;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.kns.traidit.frontend.user.dto.UserListFormBean;
import org.kns.traidit.frontend.user.service.UserService;
import org.kns.traidit.frontend.user.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;








import twitter4j.org.json.JSONObject;

/**
 * Controlles all the requests regarding user management of Web Application Admin. URL with "/web/user/" are mapped to this controller.
 * @author Thangaraj
 * @since 3-07-2014
 *
 */
@Controller("userManagement")
@RequestMapping(value="/web/user")
public class UserManagementController {

	private static Logger log=Logger.getLogger(UserServiceImpl.class);
	
	//provides all user related service
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="adminUserManagementService")
	private AdminUserManagementService adminUserManagementService;
	
	/**
	 * Handles request for listing the Users. URL with "/listUsers.htm" is mapped to this function
	 * @param map
	 * @return  string of value "listUsers" which is a view page name
	 * @since 30-07-2014
	 */
	@RequestMapping(value="/listUsers.htm")
	public String listUsers(Map<String,Object> map,@ModelAttribute("userListFormBean")UserListFormBean userListFromBean ){
		System.out.println("inside listusers() controller : userListFormBean = "+ userListFromBean.toString());
		log.info("inside listusers() controller : userListFormBean = "+ userListFromBean.toString());
		ArrayList<UserDto> users = new ArrayList<UserDto>();
		try{
			String searchText = "";
			if(userListFromBean.getSearchText()!= null)
			searchText = userListFromBean.getSearchText().trim();
			
			users = this.userService.viewUsers(
					searchText,
					userListFromBean.getPaginator().getNoOfItemsPerPage(), 
					userListFromBean.getPaginator().getStarttIndex(),
					userListFromBean.getSortBy(),
					userListFromBean.getSortOrder());
					userListFromBean.getPaginator().setTotalNoOfItems(users.get(0).getTotalUsers());
		}
		catch(UserNotFoundException e){
			System.out.println("no users found with given criteria.");
			log.info("no users found with given criteria.");
			userListFromBean.getPaginator().setTotalNoOfItems(0);
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Exception on UserManagementController -> listUsers()");
			log.info("Exception on UserManagementController -> listUsers()" + e.toString());
		}
		finally{
			//add fields which are optionally selectable to be shown on user management page for the admin
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("firstName");
			fields.add("middleName");
			fields.add("lastName");
			fields.add("email");
			fields.add("noOfRatings");
			fields.add("displayName");
			fields.add("city");
			fields.add("state");
			fields.add("country");
			fields.add("zip");
			fields.add("accountCreationDate");
			userListFromBean.setFields(fields);
		}
		map.put("users", users);
		return "listUsers";
	}

	/**
	 * Handles request to list all the payments of the given user. URL with "/listUserPayments.htm" are mapped to this function
	 * @param id user id 
	 * @param map
	 * @return string of value "listUserPayments" which is a view page name
	 * @since 30-07-2014
	 * 
	 */
	@RequestMapping(value="/listUserPayments.htm",method=RequestMethod.GET)
	public String listPaymentsOfUser(@RequestParam("id")Integer id, Map<String,Object> map){
		System.out.println("inside listPaymentsOfUser()");
		UserDto userDto = null;
		ArrayList<UserSubscriptionPaymentsDto> payments = null;
		try{
			userDto = this.userService.getUserbyUserId(id);
			payments = this.userService.getPaymentsOfUserByUserId(id);
			System.out.println(payments.size());
		}
		catch(UserNotFoundException e){
			System.out.println("UserNotFoundException in UserManagementController -> listPaymentsOfUser()");
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Exception in UserManagementController -> listPaymentsOfUser()");
		}
		finally{

			map.put("userDto", userDto);
			map.put("payments", payments);
		}
		return "listUserPayments";
	}	

	/**
	 * Handles AJAX request for updating the user status. URL with /setStatus.htm" is mapped to this function
	 * @param request Http Servelet request
	 * @param response Http Servelet response
	 * @return String of JSON encoded array consisting of status
	 * @since 30-07-2014
	 * 
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value="/setStatus.htm",method=RequestMethod.POST)
	public @ResponseBody String setStatus(HttpServletRequest request,HttpServletResponse response){
		System.out.println("inside post()");
		JSONObject obj=new JSONObject();
		try{
			Integer userId = Integer.parseInt(request.getParameter("userId"));
			Boolean status = Boolean.parseBoolean(request.getParameter("isEnabled"));
			UserDto user =  this.userService.getUserbyUserId(userId);
			user.setIsEnabled(status);
			this.userService.saveOrUpdateTraiditUser(user);
			obj.accumulate("result", 1);
		}
		catch(UserNotSavedOrUpdatedException e){
			System.out.println("cannot save user");
			obj.accumulate("result", 0);
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Exception on UserManagementController -> setStatus()");
			obj.accumulate("result", 0);
		}
		finally {
			return obj.toString();
		}
	}

	@RequestMapping(value="/listusernotifications.htm")
	public String listAllUserNotifications(Map<String,Object> map){
		ArrayList<AllUserNotification> notificationList;
		try {
			notificationList = this.adminUserManagementService.getListOfAllUserNotifications();
		} catch (AllUserNotificationNotFoundException e) {
			notificationList = new ArrayList<AllUserNotification>();
		}
		map.put("notificationList", notificationList);
		return "listUserNotifications";
	}

	@RequestMapping(value="/sendallusernotification.htm")
	public String sendAllUserNotification(@RequestParam("notificationId")Integer notificationId){
		System.out.println("inside sendAllUserNotification()");
		try {
			this.adminUserManagementService.sendAllUserNotification(notificationId);
		} catch (AllUserNotificationNotFoundException e) {
			log.error(e.toString());
		} catch (AllUserNotificationNotSavedException e) {
			log.error(e.toString());
		}
		return "redirect:/web/user/listusernotifications.htm";
	}

	/**
	 * 
	 * @param allUserNotificationFormBean
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 13-Feb-2015
	 */
	@RequestMapping(value="/addallusernotification.htm",method = RequestMethod.GET)
	public String addAllUserNotification(@ModelAttribute("allUserNotificationFormBean")AllUserNotificationFormBean allUserNotificationFormBean){
		return "addallusernotification";
	}
	
	/**
	 * 
	 * @param allUserNotificationFormBean
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 13-Feb-2015
	 */
	@RequestMapping(value="/addallusernotification.htm",method = RequestMethod.POST)
	public String ProcessaddAllUserNotification(@ModelAttribute("allUserNotificationFormBean")AllUserNotificationFormBean allUserNotificationFormBean){
		Integer notificationId;
		if(allUserNotificationFormBean.getOptionSelected().equals("cancel"))
			return "redirect:/web/user/listusernotifications.htm";
		else {
			try {
				notificationId = this.adminUserManagementService.saveAllUserNotification(allUserNotificationFormBean.getNotificationMessage());
			} catch (AllUserNotificationNotSavedException e) {
				return "addallusernotification";
			}
		}
		if(allUserNotificationFormBean.getOptionSelected().equals("send")){
			try {
				this.adminUserManagementService.sendAllUserNotification(notificationId);
			} catch (AllUserNotificationNotFoundException e) {
				log.error(e.toString());
			} catch (AllUserNotificationNotSavedException e) {
				log.error(e.toString());
			}
		}
		return "redirect:/web/user/listusernotifications.htm";
	}
}
