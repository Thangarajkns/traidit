/**
 *  Created by     : Soujanya
 * Created Date	  : July 22,2014
 * file Name	  : CommissionController.java
 * Purpose		  :Handles commission Related Operations
 * Type			  : Controller
 * 
 */



package org.kns.traidit.frontend.mobile.controller;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.frontend.commission.dto.CommissionDto;
import org.kns.traidit.frontend.commission.service.CommissionService;
import org.kns.traidit.frontend.common.utility.JsonParser;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

@Controller("commissionController")
public class CommissionController {
	
	private static Logger log=Logger.getLogger(CommissionController.class);

	@Resource(name = "commissionService")
	private CommissionService commissionService;
	
	@Resource(name = "userService")
	private UserService userService;
	
	@ResponseBody
	@RequestMapping(value="/getcommissionofuser.htm")
	public String getCommissionOfUser(HttpServletRequest request,HttpServletResponse response) throws JSONException {
		log.info("inside getCommissionOfUser()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			log.info(" request : " + json.toString());
			System.out.println(" request : " + json.toString());
			Integer userId=json.getInt("userid");
			UserDto user = this.userService.getUserbyUserId(userId);
			BigDecimal totalCommission = this.commissionService.getCommissionByUser(user);
			obj.accumulate("status", "success");
			obj.accumulate("commission", totalCommission);
		}catch (JSONException e) {
			log.error("Error While parsing input data");
			obj.accumulate("status", "Error While parsing input data");
		} catch (UserNotFoundException e) {
			log.error("User not found");
			obj.accumulate("status", "User not found");
		} catch (Exception e) {
			log.error("some error while fetching commission");
			obj.accumulate("status", "some error while fetching commission");
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @return String
	 * @author Bhagya
	 * @throws JSONException 
	 * @since 05-Jan-2015
	 * 
	 * Method For getting Commissions of user based on userId
	 */
	@RequestMapping(value="/usercommissions.htm")
	@ResponseBody
	public String commissionsOfUser(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside commissionsOfUser");
		JSONObject obj=new JSONObject();
		
		try{
			JSONObject json=JsonParser.getJson(request, response);
			Integer userId = json.getInt("userid");
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			ArrayList<CommissionDto>  commissions=this.commissionService.getCommissionsOfUser(userId);
			for(CommissionDto commission:commissions){
				JSONObject commissionJSON=new JSONObject();
				commissionJSON.accumulate("commissionid", commission.getId());
				commissionJSON.accumulate("commissionamount", commission.getCommissionAmount());
				commissionJSON.accumulate("commissionof", commission.getCommissionOf());
				jsonList.add(commissionJSON);
			}
			JSONObject output=new JSONObject();
			output.accumulate("usercommissions", jsonList);
			System.out.println(output.toString());
			return output.toString();
		}
		catch (JSONException e) {
			String message="Error While parsing input data";
			obj.accumulate("status", message);
			return obj.toString();
		}
		catch(UserNotFoundException e){
			String message="User Not Found";
			obj.accumulate("status", message);
			return obj.toString();
		}
		catch(Exception e){
			String message="Some Error Occurred While Retrieving User Commissions";
			obj.accumulate("status", message);
			return obj.toString();
		}
		
		
	}
}
