/**
 *  Created by     : Soujanya
 * Created Date	  : July 22,2014
 * file Name	  : TradeController.java
 * Purpose		  :Handles trade Related Operations
 * Type			  : Controller
 * 
 */


package org.kns.traidit.frontend.mobile.controller;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.kns.traidit.backend.inventory.exception.InventoryNotFoundException;
import org.kns.traidit.backend.notification.exception.MobileDeviceNotFoundException;
import org.kns.traidit.backend.notification.exception.NotificationNotSentException;
import org.kns.traidit.backend.trade.exception.MessageNotSavedOrUpdatedException;
import org.kns.traidit.backend.trade.exception.PaidPlanNotFoundException;
import org.kns.traidit.backend.trade.exception.TradeInventoryNotFoundException;
import org.kns.traidit.backend.trade.exception.TradeNotFoundException;
import org.kns.traidit.backend.trade.exception.TradeNotSavedOrUpdatedException;
import org.kns.traidit.backend.trade.exception.TradeStateNotFoundException;
import org.kns.traidit.backend.trade.model.OfferState;
import org.kns.traidit.backend.trade.model.OfferedBy;
import org.kns.traidit.backend.trade.model.Ratings;
import org.kns.traidit.backend.trade.model.TradeStates;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.frontend.common.utility.JsonParser;
import org.kns.traidit.frontend.inventory.dto.InventoryDto;
import org.kns.traidit.frontend.inventory.service.InventoryService;
import org.kns.traidit.frontend.notification.service.NotificationService;
import org.kns.traidit.frontend.refferral.service.RefferralService;
import org.kns.traidit.frontend.trade.dto.OffersDto;
import org.kns.traidit.frontend.trade.dto.TradesDto;
import org.kns.traidit.frontend.trade.dto.VendorRatingsDto;
import org.kns.traidit.frontend.trade.service.TradeService;
import org.kns.traidit.frontend.trade.service.TradeServiceImpl;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import twitter4j.org.json.JSONArray;
import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;
import twitter4j.org.json.JSONTokener;

@Controller("TradeController")
public class TradeController {
	
	private static Logger log=Logger.getLogger(TradeController.class);
		
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="tradeService")
	private TradeService tradeService;
	
	@Resource(name="inventoryService")
	private InventoryService inventoryService;

	@Resource(name = "notificationService")
	private NotificationService notificationService;
	
	@Resource(name = "refferralService")
	private RefferralService refferralService;
	
	/*
	 * originally added by Soujanya on July 22,2014
	 * method to make offer by user
	 * TRADE information will be saved to database i.e,itemid,traderid,vendorid
	 * 
	 * modified by thangaraj on 03-11-2014
	 * modified by bhagya on Dec 03rd,2014.checking The Plan Expiry of user
	 */
	@RequestMapping(value="/makeoffer.htm")
	@ResponseBody
	public String makeOffer(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside makeOffer()");
		JSONObject obj=new JSONObject();
		try{
			
			String status="OFFER NOT SENT";
			JSONObject json=getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());
			
			Integer tradeId ;
			
				try{
					tradeId = json.getInt("tradeid");
				}
				catch(JSONException e){
					tradeId = null;
				}
				Integer senderId=json.getInt("userid");
				Integer receiverId=json.getInt("receiverid");
				String messageCode=json.getString("message");
				//checking the plan expiry of user
				Boolean result=this.userService.checkThePlanExpiryOfUserByUserId(senderId);
				if(result==false){
					throw new PaidPlanNotFoundException();
				}
				JSONArray senderInventory = json.getJSONArray("senderinventoryids");
				JSONArray receiverInventory = json.getJSONArray("receiverinventoryids");
				
				ArrayList<InventoryDto> senderInventories = populateInventoriesFromJsonArray(senderInventory);
				ArrayList<InventoryDto> receiverInventories = populateInventoriesFromJsonArray(receiverInventory);
				
				Integer savedTradeId=this.tradeService.sendOffer(tradeId, senderInventories, receiverInventories, senderId, receiverId, messageCode,OfferState.OFFER);
				
				if(savedTradeId > 0){
					status="SUCCESS: OFFER SENT";
					obj.accumulate("status", status);
				}
				else{
					throw new TradeNotSavedOrUpdatedException();
				}
			
		}
		catch(TradeNotSavedOrUpdatedException e){
			
			String message="Error in Making an Offer, Try again";
			e.printStackTrace();
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		catch(InventoryNotFoundException e){
			
			String message="Trade Item doesn't Exist";
			e.printStackTrace();
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		catch(UserNotFoundException e){
			
			String message="Error While Making an Offer to the Vendor";
			e.printStackTrace();
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		catch(PaidPlanNotFoundException e){
			
			String message="You no longer have access to Trade, Please Renew/Upgrade account to continue";
			e.printStackTrace();
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		catch(Exception e){
			
			String message="Error in the process of Making an Offer";
			e.printStackTrace();
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
		
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @author Thangaraj
	 * @throws JSONException 
	 * @since 15-10-2014
	 * modified by bhagya on Dec 03rd,2014.checking The Plan Expiry of user
	 */
	@ResponseBody
	@RequestMapping(value="/makecounteroffer.htm")
	public String makeCounterOffer(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		log.info("make counter offer initiated");
		System.out.println("make counter offer initiated");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());

			
			//receive all inputs from request
			Integer senderId 	= json.getInt("userid");
			Integer receiverId	= json.getInt("receiverid");
			Integer tradeId 	= json.getInt("tradeid");
			String 	message 	= json.getString("message");
			//checking the plan expiry of user
			Boolean result=this.userService.checkThePlanExpiryOfUserByUserId(senderId);
			if(result==false){
				throw new PaidPlanNotFoundException();
			}
			//validate trade id
			if(tradeId ==0)
				throw new TradeNotFoundException();
			JSONArray senderInventoryIds = json.getJSONArray("senderinventoryids");
			JSONArray receiverInventoryIds = json.getJSONArray("receiverinventoryids");
			ArrayList<InventoryDto> senderInventories = populateInventoriesFromJsonArray(senderInventoryIds);
			ArrayList<InventoryDto> receiverInventories = populateInventoriesFromJsonArray(receiverInventoryIds);
			
			//Integer result=this.tradeService.addCounterOfferToDB(userId,tradeId,message,traderInventories,vendorInventories);
			this.tradeService.sendOffer(tradeId, senderInventories, receiverInventories, senderId, receiverId, message,OfferState.COUNTER_OFFER);
			obj.accumulate("status","SUCCESS: COUNTER OFFER SENT" );

		} catch (JSONException e) {
			log.error("JSONException : "+e.toString());
			System.out.println("JSONException : "+e.toString());
			obj.accumulate("status","Error in the process of making a counter offer" );
		} catch (TradeNotFoundException e) {
			e.printStackTrace();
			log.error("TradeNotFoundException : "+e.toString());
			System.out.println("TradeNotFoundException : "+e.toString());
			obj.accumulate("status","Given trade not found" );
		} catch (TradeNotSavedOrUpdatedException e) {
			e.printStackTrace();
			log.error("TradeNotSavedOrUpdatedException : "+e.toString());
			System.out.println("TradeNotSavedOrUpdatedException : "+e.toString());
			obj.accumulate("status","Cannot save given Trade" );
		} catch (InventoryNotFoundException e) {
			log.error("InventoryNotFoundException : "+e.toString());
			System.out.println("InventoryNotFoundException : "+e.toString());
			obj.accumulate("status","Given Inventory not found" );
		} catch (MessageNotSavedOrUpdatedException e) {
			log.error("MessageNotSavedOrUpdatedException : "+e.toString());
			System.out.println("MessageNotSavedOrUpdatedException : "+e.toString());
			obj.accumulate("status","Cannot save trade message" );
		} catch (UserNotFoundException e) {
			log.error("JSONException : "+e.toString());
			System.out.println("JSONException : "+e.toString());
			obj.accumulate("status","User Not Found" );
		}catch(PaidPlanNotFoundException e){
			String message="You no longer have access to Trade, Please Renew/Upgrade account to continue";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		  catch(Exception e){
			log.error("Exception : "+e.toString());
			System.out.println("Exception : "+e.toString());
			obj.accumulate("status","Error in Making a Counter Offer" );
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
	}
	
	/*
	 * added by Soujanya on july 22,2014
	 * method to accept an offer made
	 * modified by bhagya on Dec 03rd,2014.checking The Plan Expiry of user
	 */
	@RequestMapping(value="/acceptoffer.htm")
	@ResponseBody
	public String acceptOffer(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside acceptOffer()");
		JSONObject obj=new JSONObject();
		try{
			String status="OFFER NOT ACCEPTED";
			JSONObject json=getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());

			Integer tradeId=json.getInt("tradeid");
			Integer userId=json.getInt("userid");
			String tradeMessage=json.getString("message");
			
			//checking the plan expiry of user
			Boolean planResult=this.userService.checkThePlanExpiryOfUserByUserId(userId);
			if(planResult==false){
				throw new PaidPlanNotFoundException();
			}
			
			Integer result=this.tradeService.acceptOffer(tradeId,userId,tradeMessage);
			if(result>0){
				status="OFFER ACCEPTED";
				obj.accumulate("status", status);
			}	
			else{
				throw new TradeNotSavedOrUpdatedException();
			}
			
		}
		catch(TradeNotSavedOrUpdatedException e){
			
			String message="Error in Accepting Offer";
			obj.accumulate("status", message);
		}
		catch(TradeNotFoundException e){
			
			String message="Trade Not Found";
			obj.accumulate("status", message);
		}
		catch(PaidPlanNotFoundException e){
			
			String message="You no longer have access to Trade, Please Renew/Upgrade account to continue";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		catch(Exception e){
			
			String message="Error in the Process of Accepting Offer";
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
		
		
	}
	
	/*
	 * added by Soujanya on july 22,2014
	 * method to reject an offer made
	 * modified by bhagya on Dec 03rd,2014.checking The Plan Expiry of user
	 */
	@RequestMapping(value="/rejectoffer.htm")
	@ResponseBody
	public String rejectOffer(HttpServletRequest request,HttpServletResponse response) throws JSONException {
		log.info("inside rejectOffer()");
		JSONObject obj=new JSONObject();
		try{
			String status="OFFER NOT REJECTED";
			JSONObject json=getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());

			Integer tradeId=json.getInt("tradeid");
			Integer userId=json.getInt("userid");
			String tradeMessage=json.getString("message");
			//checking the plan expiry of user
			Boolean planResult=this.userService.checkThePlanExpiryOfUserByUserId(userId);
			if(planResult==false){
				throw new PaidPlanNotFoundException();
			}
			Integer result=this.tradeService.rejectOffer(tradeId,userId,tradeMessage);
			if(result>0){
				status="OFFER REJECTED";
				obj.accumulate("status", status);
			}
			else{
				throw new TradeNotSavedOrUpdatedException();
			}
		}
		catch(TradeNotSavedOrUpdatedException e){
			String message="Error in rejecting trade";
			log.error(message+" : "+e.toString());
			obj.accumulate("status", message);
		}
		catch(TradeNotFoundException e){
			String message="Trade Not Found";
			log.error(message+" : "+e.toString());
			obj.accumulate("status", message);
		}
		catch(PaidPlanNotFoundException e){
			
			String message="You no longer have access to Trade, Please Renew/Upgrade account to continue";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		catch(UserNotFoundException e){
			String message="Could not found given user";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		catch(Exception e){
			String message="Error in the process of rejecting offer, try again";
			log.error(message+" : "+e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException 
	 * modified by bhagya on Dec 03rd,2014.checking The Plan Expiry of user
	 */
	@RequestMapping(value="/saveoffer.htm")
	@ResponseBody
	public String saveoffer(HttpServletRequest request, HttpServletResponse response) throws JSONException{
		log.info("inside saveOffer()");
		JSONObject result = new JSONObject();
		JSONObject json = JsonParser.getJson(request, response);
		log.info("request : "+ json.toString());
		System.out.println("request : "+ json.toString());
		
		
		try {
			Integer tradeId ;
			try{
				tradeId = json.getInt("tradeid");
			}
			catch(JSONException e){
				tradeId = null;
			}
			Integer senderId=json.getInt("userid");
			Integer receiverId=json.getInt("receiverid");
			String message=json.getString("message");
			//checking the plan expiry of user
			Boolean planResult=this.userService.checkThePlanExpiryOfUserByUserId(senderId);
			if(planResult==false){
				throw new PaidPlanNotFoundException();
			}
			JSONArray senderInventory = json.getJSONArray("senderinventoryids");
			JSONArray receiverInventory = json.getJSONArray("receiverinventoryids");
			
			ArrayList<InventoryDto> senderInventories = populateInventoriesFromJsonArray(senderInventory);
			ArrayList<InventoryDto> receiverInventories = populateInventoriesFromJsonArray(receiverInventory);
			
			Integer savedTradeId =this.tradeService.sendOffer(tradeId, senderInventories, receiverInventories, senderId, receiverId, message,OfferState.SAVED);
			
			if(savedTradeId > 0){
				String status="SUCCESS: OFFER SAVED";
				result.accumulate("tradeid", savedTradeId);
				result.accumulate("status", status);
			}
			else{
				throw new TradeNotSavedOrUpdatedException();
			}
			
		} catch (JSONException e) {
			log.info("JSONException " + e);
			e.printStackTrace();
			result.accumulate("status", "Error while parsing Input");
		} catch (TradeNotSavedOrUpdatedException e) {
			log.info("TradeNotSavedOrUpdatedException " + e);
			e.printStackTrace();
			result.accumulate("status", "Cannot save Trade. Please try again later");
		}catch(PaidPlanNotFoundException e){
			String message="You no longer have access to Trade, Please Renew/Upgrade account to continue";
			log.error(message+""+e.toString());
			result.accumulate("status", message);
		}catch (Exception e) {
			log.info("Exception " + e);
			e.printStackTrace();
			result.accumulate("status", "Error in the process of saving Trade");
		}
		
		String resultString = result.toString();
		log.info("response : " + resultString);
		System.out.println("response : " + resultString);
		return resultString;
	}
	
	/*
	 * added by Soujanya on July 23,2014
	 * method to send trade message
	 * input: message,senderid,receiverid,tradeid
	 * 
	 * modified by thangaraj on Jan 7, 2015 : added send notification feature
	 */
	@RequestMapping(value="/sendtrademessages.htm")
	@ResponseBody
	public String sendTradeMessages(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside sendTradeMessages()");
		JSONObject obj=new JSONObject();
		try{
			String status="FAIL";
			JSONObject json=getJson(request, response);
			System.out.println(json.toString());
			System.out.println(request.getHeaderNames());
			System.out.println(request.getContentLength()+request.getContentType());
			Integer tradeId=json.getInt("tradeid");
			Integer senderId=json.getInt("senderid");
			Integer receiverId=json.getInt("receiverid");
			String message=json.getString("message");
			
			UserDto sender = this.userService.getUserbyUserId(senderId); 
			String notificationMessage = "Your got a trade message from "+sender.getDisplayName();

			//checking the plan expiry of user
			Boolean planResult=this.userService.checkThePlanExpiryOfUserByUserId(senderId);
			if(planResult==false){
				throw new PaidPlanNotFoundException();
			}
			
			//has to be changed : thangaraj : 16-10-2014
			Integer level = 1;
			Integer result=this.tradeService.addTradeMessageToDB(message,senderId,receiverId,tradeId,level);

			//send notification to the user
			try {
				this.notificationService.sendNotification(receiverId, notificationMessage);
			} catch (NotificationNotSentException e1) {
				log.error(e1.toString()+" {message : " + notificationMessage + ", receiverId : " + receiverId + "}");
			} catch (MobileDeviceNotFoundException e) {
				log.error(e.toString() + " {message : " + notificationMessage + ", receiverId : " + receiverId + "}");
			} catch (Exception e) {
				log.error(e.toString() + " {message : " + notificationMessage + ", receiverId : " + receiverId + "}");
			}
			if(result>0){
				status="SUCCESS";
				obj.accumulate("status", status);
			}
			else{
				throw new MessageNotSavedOrUpdatedException();
			}
			
		}
		catch(MessageNotSavedOrUpdatedException e){
			
			String message="Error in Sending Trade Message";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		catch(TradeNotFoundException e){
			
			String message="Trade Not Found";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		catch(UserNotFoundException e){
			
			String message="User Not Found";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		catch(PaidPlanNotFoundException e){
			
			String message="You no longer have access to Trade, Please Renew/Upgrade account to continue";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		catch(Exception e){
			
			String message="Error in the process of sending trade message";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
	}
	
	/**
	 * added by Soujanya on july 23,2014
	 * 
	 * edited by Soujanya on november 3rd,2014
	 * method to retrieve trades according to criteria i.e, pending trades,accepted trades,rejected trades
	 * @input: status string(status can be PENDING,ACCECTED,REJECTED), userid
	 * @return: arraylist of trades
	 * @throws TradeNotFoundException
	 * 
	 */
	@RequestMapping(value="/gettradesbycriteria.htm")
	@ResponseBody
	public String getTradesByCriteria(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside gettradesbycriteria");
		System.out.println("inside gettradesbycriteria");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=getJson(request, response);
			String status=json.getString("status");
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());
			Integer traderId=json.getInt("userid");
			this.userService.checkThePlanExpiryOfUserByUserId(traderId);
			ArrayList<TradesDto> trades = new ArrayList<TradesDto>();
			if(status.toUpperCase().equals("SAVED"))
				trades=this.tradeService.getTradesBySavedCriteria(traderId);
			else
				trades=this.tradeService.getTradesByCriteria(status.toUpperCase(),traderId);
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			if (!(trades.isEmpty())){
			for(TradesDto trade:trades){
				JSONObject tradeJSON=new JSONObject();
				tradeJSON.accumulate("trade id", trade.getTradeId());
				tradeJSON.accumulate("currentUserCanOffer", "true");
				
				//accumulate user's inventory information
				//inventory name, inventory image, inventory parent and sub category, is favourite inventory
				JSONObject tradeFromJSON=new JSONObject();
				tradeFromJSON.accumulate("inventoryname", trade.getTradeFromInventories().getItemId().getItemName());
				tradeFromJSON.accumulate("inventoryid", trade.getTradeFromInventories().getInventoryId());
				if(null!=trade.getTradeFromInventories().getItemId().getCategoryId().getParentCategories() && !trade.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().isEmpty()  ){
					tradeFromJSON.accumulate("parentcategory",trade.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());	
					if(null!=trade.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories()  && !trade.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().isEmpty()){						
						tradeFromJSON.accumulate("categoryname",trade.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().iterator().next().getCategoryName());	
						tradeFromJSON.accumulate("subcategoryname",trade.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						tradeFromJSON.accumulate("subsubcategoryname", trade.getTradeFromInventories().getItemId().getCategoryId().getCategoryName());
					}
					else{
						tradeFromJSON.accumulate("categoryname",trade.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						tradeFromJSON.accumulate("subcategoryname", trade.getTradeFromInventories().getItemId().getCategoryId().getCategoryName());
						tradeFromJSON.accumulate("subsubcategoryname","null");
					}
				}
				else{
					tradeFromJSON.accumulate("categoryname", trade.getTradeFromInventories().getItemId().getCategoryId().getCategoryName());
					tradeFromJSON.accumulate("subcategoryname","null");
					tradeFromJSON.accumulate("subsubcategoryname","null");					
				}
				tradeFromJSON.accumulate("photo", trade.getTradeFromInventories().getPhotoList().get(0));
				tradeFromJSON.accumulate("isFavouriteInventory", trade.getTradeFromInventories().getIsFavouriteInventory());
				
				tradeJSON.accumulate("traderinventorydata", tradeFromJSON);
				
				
				
				//accumulate sender's(who sends offer) inventory information
				//inventory name, inventory image, inventory parent and sub category
				JSONObject tradeToJSON=new JSONObject();
				tradeToJSON.accumulate("inventoryname", trade.getTradeToInventories().getItemId().getItemName());
				tradeToJSON.accumulate("inventoryid", trade.getTradeToInventories().getInventoryId());
				if(null!=trade.getTradeToInventories().getItemId().getCategoryId().getParentCategories() && !trade.getTradeToInventories().getItemId().getCategoryId().getParentCategories().isEmpty()  ){
					tradeToJSON.accumulate("parentcategory",trade.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());	
					if(null!=trade.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories()  && !trade.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().isEmpty()){						
						tradeToJSON.accumulate("categoryname",trade.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().iterator().next().getCategoryName());	
						tradeToJSON.accumulate("subcategoryname",trade.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						tradeToJSON.accumulate("subsubcategoryname", trade.getTradeToInventories().getItemId().getCategoryId().getCategoryName());
					}
					else{
						tradeToJSON.accumulate("categoryname",trade.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						tradeToJSON.accumulate("subcategoryname", trade.getTradeToInventories().getItemId().getCategoryId().getCategoryName());
						tradeToJSON.accumulate("subsubcategoryname","null");
					}
				}
				else{
					tradeToJSON.accumulate("categoryname", trade.getTradeToInventories().getItemId().getCategoryId().getCategoryName());
					tradeToJSON.accumulate("subcategoryname","null");
					tradeToJSON.accumulate("subsubcategoryname","null");					
				}
				tradeToJSON.accumulate("photo", trade.getTradeToInventories().getPhotoList().get(0));
				tradeToJSON.accumulate("isFavouriteInventory", trade.getTradeToInventories().getIsFavouriteInventory());
				
				tradeJSON.accumulate("vendorinventorydata", tradeToJSON);
				tradeJSON.accumulate("level", trade.getCurrentLevel());
				
				jsonList.add(tradeJSON);
				
			}
			}
			else{
				throw new TradeNotFoundException();
			}
			obj.accumulate("trades", jsonList);
		}
		catch(TradeNotFoundException e){
			System.out.println("TradeNotFoundException");
			String message="No Trades Exist";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		catch(TradeStateNotFoundException e){
			e.printStackTrace();
			String message = e.toString();
			log.error(message);
			obj.accumulate("status", message);
		}
		catch(TradeInventoryNotFoundException e){
			e.printStackTrace();
			log.error("TradeInventoryNotFoundException");
			obj.accumulate("status", "TradeInventoryNotFoundException");
		}
		catch(Exception e){
			e.printStackTrace();
			String message="Error in Fetching Trades";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
		
	}
	
	/**
	 * @author Soujanya
	 * @since october 8,2014
	 * purpose: method to rate a vendor by user
	 * @input  vendorid, userid, rating, comment
	 * @return rating, status
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="/ratevendor.htm")
	@ResponseBody
	public String rateVendor(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("inside rateVendor()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			Integer vendorId=json.getInt("vendorid");
			Integer userId=json.getInt("userid");
			String rating=json.getString("rating");
			
			//checking the plan expiry of user
			Boolean planResult=this.userService.checkThePlanExpiryOfUserByUserId(userId);
			if(planResult==false){
				throw new PaidPlanNotFoundException();
			}
			
			if(rating.contains("Not Satisfied")){
				rating="Unsatisfied";
			}
			String comment=json.getString("comment");
			Integer result=this.tradeService.saveVendorRating(vendorId,userId,rating,comment);
			if(result>0)
			{
				obj.accumulate("status", "Success: Thanks For Your Feedback");
			}
			else{
				throw new Exception();
			}
		}
		catch(PaidPlanNotFoundException e){
			String message="You no longer have access to Trade, Please Renew/Upgrade account to continue";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		catch(Exception e){
			e.printStackTrace();
			String message="Error in Rating the Vendor";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
	}
	
	/**
	 * @author Soujanya
	 * @since october 9,2014
	 * purpose: method to send vendor details 
	 * @input: vendorid
	 * @throws: UserNotFoundException
	 * @return vendor details
	 *
	 */
	@RequestMapping(value="/getvendordetails.htm")
	@ResponseBody
	public String getVendorDetails(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside getVendorDetails()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());

			Integer userId=json.getInt("userid");
			Integer vendorId=json.getInt("vendorid");
			this.tradeService.updateVendorRatings(vendorId);
			UserDto userDto=this.userService.getUserbyUserId(vendorId);
			UserDto userDtoOfFavouriteVendor=this.tradeService.getIsFavouriteVendor(userDto, userId);
			BigDecimal distance=this.inventoryService.calculateDistance(userId, vendorId);
			Integer totalTrades=this.tradeService.getTotalTradeCountOfVendor(vendorId);
			Integer positiveRatingInPercentage=this.tradeService.getPercentagePositiveRatingOfVendor(vendorId);
			Integer inventoryCount=this.tradeService.getInventoryCountOfVendor(vendorId);
			Boolean userHasTradedWithVendor = this.tradeService.isUserHasTradedWithVendor(userId,vendorId);
			JSONObject userJSON=new JSONObject();
			userJSON.accumulate("vendor id", vendorId);
			userJSON.accumulate("vendor name", userDto.getUserName());
			userJSON.accumulate("city", userDto.getCity());
			userJSON.accumulate("state", userDto.getState());
			userJSON.accumulate("distance", distance);
			userJSON.accumulate("userHasTradedWithVendor", userHasTradedWithVendor);
			if( null != Ratings.getState(userDto.getRating())){

			userJSON.accumulate("average rating", Ratings.getState(userDto.getRating()));
			}
			else{
				userJSON.accumulate("average rating", "vendor has no ratings");
			}
			
			if(null != userDto.getAccountCreationDate()){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = format.parse(userDto.getAccountCreationDate().toString());
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				String year = df.format(date);
				System.out.println(year);

			userJSON.accumulate("member since", year);
			}
			else{
				userJSON.accumulate("member since", "registered year not found");
			}
			userJSON.accumulate("total trades", totalTrades);

			

			userJSON.accumulate("total feedback ratings", userDto.getNoOfRatings());

			Map<String, Integer> ratingsMap=this.tradeService.getIndividualRatingCountOfVendor(vendorId);
			for (Map.Entry<String, Integer> entry : ratingsMap.entrySet())
			{
			    String rating=entry.getKey();
//			    if(rating.contains("Unsatisfied")){
//			    	rating="Not Satisfied";
//			    }
			    Integer count=entry.getValue();
			    userJSON.accumulate(rating, count);
			}
			
			userJSON.accumulate("% positive rating", positiveRatingInPercentage);
			userJSON.accumulate("is favourite vendor", userDtoOfFavouriteVendor.getIsFavouriteVendor());
			userJSON.accumulate("inventory count", inventoryCount);
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			jsonList.add(userJSON);
			obj.accumulate("vendor details", jsonList);
	
		}

			
		catch(Exception e){
			e.printStackTrace();
			String message="Some Error Occurred While Fetching Vendor Details";
			log.error(message+" "+e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
	}
	
	/**
	 * 
	 * @author Soujanya
	 * @since 31st october,2014
	 * @purpose: method to get list of ratings given for a vendor
	 * @input vendorid
	 * @return arraylist of ratings of vendor
	 * @throws Exception
	 */
	@RequestMapping(value="/getvendorratings.htm")
	@ResponseBody
	public String getVendorRatings(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("inside getVenodorRatings()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());

			
			Integer vendorId=json.getInt("vendorid");
			String rating=json.getString("rating");
			if(rating.contains("Not Satisfied")){
				rating="Unsatisfied";
			}
			ArrayList<VendorRatingsDto> vendorRatings=this.tradeService.getVendorRatings(vendorId,rating);
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			for(VendorRatingsDto vendorRating : vendorRatings){
				String ratingString=null;
				JSONObject ratingJSON=new JSONObject();
				System.out.println(vendorRating.getRating());
				System.out.println(Ratings.getState(vendorRating.getRating()));
				if( null != Ratings.getState(vendorRating.getRating())){
					
					 ratingString=Ratings.getState(vendorRating.getRating()).toString();
//					if(ratingString.contains("Unsatisfied")){
//						ratingString= "Not Satisfied";
//					}
					System.out.println(ratingString);
					ratingJSON.accumulate("rating", ratingString);
					}
					else{
						ratingJSON.accumulate("rating", "vendor has no ratings");
					}
				
				ratingJSON.accumulate("comment", vendorRating.getComment());
				jsonList.add(ratingJSON);
				
			}
			obj.accumulate("rating list", jsonList);
			
		}
		
		catch(Exception e){
			
			String message="Vendor Has No Ratings";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
		
	}
	
	/**
	 * this method returns the details of a trade.
	 * 
	 * Input : 
	 *         userid
	 *         tradeid
	 *         
	 * Output: 
	 *         offertype 
	 *         list of vendorinventories(id,itemName,description,image)
	 *         list of traderinventories(id,itemName,description,image)
	 *         
	 * @param request
	 * @param response
	 * @return
	 * @author Thangaraj
	 * @throws JSONException 
	 * @since 03-11-2014
	 */
	@ResponseBody
	@RequestMapping(value="/getoffer.htm")
	public String getOffer(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		log.info("inside getOffer()");
		System.out.println("inside getOffer()");
		JSONObject result = new JSONObject();
		try{
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());
			
			Integer tradeId = json.getInt("tradeid");
			Integer userId = json.getInt("userid");

			result.accumulate("tradeId", tradeId);
			
			TradesDto trade = this.tradeService.getTradeByTradeId(tradeId);
			System.out.println("trade fetched");
			OfferedBy currentUserType = null;
			if(trade.getTrader().getUserId().equals(userId)){
				currentUserType = OfferedBy.valueOf("TRADER");
			}
			else if(trade.getVendor().getUserId().equals(userId)){
				currentUserType = OfferedBy.valueOf("VENDOR");
			}
			else{
				log.info("traderId : "+trade.getTrader().getUserId());
				log.info("vendorrId : "+trade.getVendor().getUserId());
				throw new UserNotFoundException();
			}
			//unset new offer for user
			this.tradeService.unsetIsReadTrade(userId, tradeId);
			
			result.accumulate("offerType", this.tradeService.getCurrentOfferType(tradeId));
			ArrayList<JSONObject> vendorInventories = populateJsonFromInventories(this.tradeService.getVendorInventoriesOfTrade(tradeId,userId));
			ArrayList<JSONObject> traderInventories = populateJsonFromInventories( this.tradeService.getTraderInventoriesOfTrade(tradeId,userId));

			//decide current user can involve in offering action or not . It should happen immediately loading invenoties
			if(!TradeServiceImpl.currentUserCanOffer.equals(currentUserType.toString()) && !TradeServiceImpl.currentUserCanOffer.equals(""))
				result.accumulate("currentUserCanOffer", "true");
			else
				result.accumulate("currentUserCanOffer", "false");
			result.accumulate("userId",userId);
			
			result.accumulate("senderId",currentUserType.equals(OfferedBy.TRADER)?trade.getTrader().getUserId():trade.getVendor().getUserId());
			result.accumulate("receiverId",currentUserType.equals(OfferedBy.VENDOR)?trade.getTrader().getUserId():trade.getVendor().getUserId());
			result.accumulate("senderName",currentUserType.equals(OfferedBy.TRADER)?trade.getTrader().getDisplayName():trade.getVendor().getDisplayName());
			result.accumulate("receiverName",currentUserType.equals(OfferedBy.VENDOR)?trade.getTrader().getDisplayName():trade.getVendor().getDisplayName());
			result.accumulate("senderinventories",currentUserType.equals(OfferedBy.TRADER)?traderInventories:vendorInventories );
			result.accumulate("receiverinventories",currentUserType.equals(OfferedBy.VENDOR)?traderInventories:vendorInventories  );
			
		}
		catch(InventoryNotFoundException e){
			log.info("no offer found for given trade id");
			System.out.println("no offer found for given trade id");
			result.accumulate("status", "not found");
		}
		catch(TradeNotFoundException e){
			log.info("trade found for given trade id");
			System.out.println("trade found for given trade id");
			result.accumulate("status", "not found");
		}
		catch(JSONException e){
			log.info("Error while parsing JSON");
			System.out.println("Error while parsing JSON");
			result.accumulate("status", "Error while getting offer");
		}
		catch(UserNotFoundException e){
			log.info("Given user not found or not related with this trade");
			System.out.println("Given user not found or not related with this trade");
			result.accumulate("status", "Given user not found or not related with this trade");			
		}
		catch(Exception e){
			log.info(e.toString());
			log.info(ExceptionUtils.getStackTrace(e));
			System.out.println(e.toString());
			result.accumulate("status", "Error while getting offer");
		}
		String resultString = result.toString();
		log.info("response : "+resultString);
		System.out.println("response : "+resultString);
		return resultString;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 * @author Thangaraj
	 * @since 24-11-2014
	 */
	@ResponseBody
	@RequestMapping(value="/getsavedoffer.htm")
	public String getSavedOffer(HttpServletRequest request,HttpServletResponse response)throws JSONException{
		log.info("inside getOffer()");
		System.out.println("inside getOffer()");
		JSONObject result = new JSONObject();
		try{
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());

			Integer tradeId = json.getInt("tradeid");
			Integer userId = json.getInt("userid");
			Integer level = json.getInt("level");

			result.accumulate("tradeId", tradeId);
			
			TradesDto trade = this.tradeService.getTradeByTradeId(tradeId);
			System.out.println("trade fetched");
			OfferedBy currentUserType = null;
			if(trade.getTrader().getUserId().equals(userId)){
				currentUserType = OfferedBy.valueOf("TRADER");
			}
			else if(trade.getVendor().getUserId().equals(userId)){
				currentUserType = OfferedBy.valueOf("VENDOR");
			}
			else{
				log.info("traderId : "+trade.getTrader().getUserId());
				log.info("vendorrId : "+trade.getVendor().getUserId());
				throw new UserNotFoundException();
			}
			
			result.accumulate("offerType", "SAVED");
			ArrayList<JSONObject> vendorInventories = populateJsonFromInventories(this.tradeService.getSavedVendorInventoriesOfTrade(userId, tradeId, level));
			ArrayList<JSONObject> traderInventories = populateJsonFromInventories(this.tradeService.getSavedTraderInventoriesOfTrade(userId, tradeId, level));

			//decide current user can involve in offering action or not . It should happen immediately loading invenoties
			if(!TradeServiceImpl.currentUserCanOffer.equals(currentUserType.toString()) && !TradeServiceImpl.currentUserCanOffer.equals(""))
				result.accumulate("currentUserCanOffer", "true");
			else
				result.accumulate("currentUserCanOffer", "false");
			result.accumulate("userId",userId);
			
			result.accumulate("usercanmake",trade.getStatus().equals(TradeStates.SAVED.toString())?OfferState.OFFER.toString():OfferState.COUNTER_OFFER.toString());
			result.accumulate("senderId",currentUserType.equals(OfferedBy.TRADER)?trade.getTrader().getUserId():trade.getVendor().getUserId());
			result.accumulate("receiverId",currentUserType.equals(OfferedBy.VENDOR)?trade.getTrader().getUserId():trade.getVendor().getUserId());
			result.accumulate("senderName",currentUserType.equals(OfferedBy.TRADER)?trade.getTrader().getDisplayName():trade.getVendor().getDisplayName());
			result.accumulate("receiverName",currentUserType.equals(OfferedBy.VENDOR)?trade.getTrader().getDisplayName():trade.getVendor().getDisplayName());
			result.accumulate("senderinventories",currentUserType.equals(OfferedBy.TRADER)?traderInventories:vendorInventories);
			result.accumulate("receiverinventories",currentUserType.equals(OfferedBy.VENDOR)?traderInventories:vendorInventories);
			
		} catch (TradeNotFoundException e) {
			log.info("trade found for given trade id");
			System.out.println("trade found for given trade id");
			result.accumulate("status", "Trade not found");
		} catch (UserNotFoundException e) {
			log.info("Given user not found or not related with this trade");
			System.out.println("Given user not found or not related with this trade");
			result.accumulate("status", "Given user not found or not related with this trade");	
		} catch (InventoryNotFoundException e) {
			log.info("no offer found for given trade id");
			System.out.println("no offer found for given trade id");
			result.accumulate("status", "Inventories not found");
		}
		String resultString = result.toString();
		log.info("response : "+resultString);
		System.out.println("response : "+resultString);
		return resultString;
	}
	
	/**
	 * 
	 * @param inventories
	 * @return
	 * @throws JSONException
	 * @author Thangaraj
	 * @since 03-11-2014
	 */
	private ArrayList<JSONObject> populateJsonFromInventories(ArrayList<InventoryDto> inventories) throws JSONException{
		ArrayList<JSONObject> vendorInventories = new ArrayList<JSONObject>();
		for(InventoryDto inventory :  inventories){
			JSONObject inventoryJson = new JSONObject();
			inventoryJson.accumulate("inventoryid", inventory.getInventoryId());
			inventoryJson.accumulate("itemname", inventory.getItemId().getItemName());
			inventoryJson.accumulate("description", inventory.getDescription());
			inventoryJson.accumulate("image", inventory.getPhotoList().get(0));
			
			if(null!=inventory.getItemId().getCategoryId().getParentCategories() && !inventory.getItemId().getCategoryId().getParentCategories().isEmpty()  ){
				inventoryJson.accumulate("parentcategory",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());	
				if(null!=inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories()  && !inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().isEmpty()){						
					inventoryJson.accumulate("categoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().iterator().next().getCategoryName());	
					inventoryJson.accumulate("subcategoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
					inventoryJson.accumulate("subsubcategoryname", inventory.getItemId().getCategoryId().getCategoryName());
				}
				else{
					inventoryJson.accumulate("categoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
					inventoryJson.accumulate("subcategoryname", inventory.getItemId().getCategoryId().getCategoryName());
					inventoryJson.accumulate("subsubcategoryname","null");
				}
			}
			else{
				inventoryJson.accumulate("categoryname", inventory.getItemId().getCategoryId().getCategoryName());
				inventoryJson.accumulate("subcategoryname","null");
				inventoryJson.accumulate("subsubcategoryname","null");					
			}
			
			
			vendorInventories.add(inventoryJson);
		}
		return vendorInventories;
	}
	
	/*
	 * added by Soujanya on July 2nd,2014
	 * Method to parse Json input as a stream 
	 */
	public JSONObject getJson(HttpServletRequest request,HttpServletResponse response)
	{
		try
		{
			
			InputStream stream=request.getInputStream();		
			StringBuffer buffer = new StringBuffer();
			byte[] b = new byte[4096];			
			for (int n; (n = stream.read(b)) != -1;) 
			{
				buffer.append(new String(b,0,n));
			}
			String s=buffer.toString();
			JSONTokener tokenizer=new JSONTokener(s);
			JSONObject json = new JSONObject(tokenizer);
			return json;			
		}
		catch(Exception e)
		{
			log.error("Error in getting JSON OBject"+e);
			e.printStackTrace();
			return null;
		}
		
	}

	private ArrayList<InventoryDto> populateInventoriesFromJsonArray(JSONArray inventoryIds) throws JSONException{
		ArrayList<InventoryDto> inventoryDtos=new ArrayList<InventoryDto>();
		for (int i = 0; i <  inventoryIds.length(); i++) {
			    
			JSONObject vendorInventoryId = inventoryIds.getJSONObject(i); 
		
			Integer id = vendorInventoryId.getInt("id");
			Integer quantity = vendorInventoryId.getInt("quantity");
		    InventoryDto inventoryDto=new InventoryDto();
		    inventoryDto.setInventoryId(id);
		    inventoryDto.setUnitsAvailable(quantity);
		    inventoryDtos.add(inventoryDto);
		}
		return inventoryDtos;
	}
	
	/**
	 * @author Soujanya
	 * @since 12 november, 2014
	 * method to mark a trade as complete
	 * 
	 * Modified by Thangaraj : 29-01-2015 : added new flag complete to denote trade completed or withdrawn
	 */
	@RequestMapping(value="/completetrade.htm")
	@ResponseBody
	public String completeTrade(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside completeTrade()");
		JSONObject obj=new JSONObject();
		try{
			String status="TRADE NOT COMPLETED";
			JSONObject json=getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());

			Integer tradeId		= json.getInt("tradeid");
			Integer userId		= json.getInt("userid");
			String tradeMessage	= json.getString("message");
			Boolean complete 	= json.getBoolean("complete");
			
			//checking the plan expiry of user
			Boolean planResult=this.userService.checkThePlanExpiryOfUserByUserId(userId);
			if(planResult==false){
				throw new PaidPlanNotFoundException();
			}
			
			Integer result=this.tradeService.completeTrade(tradeId,userId,tradeMessage,complete);
			if(result>0){
				status="TRADE COMPLETED";
				obj.accumulate("status", status);
			}	
			else{
				throw new TradeNotSavedOrUpdatedException();
			}
			
		}
		catch(TradeNotSavedOrUpdatedException e){
			e.printStackTrace();
			String message="Error in Making Trade Complete";
			obj.accumulate("status", message);
		}
		catch(TradeNotFoundException e){
			
			String message="This Trade Doesn't Exist";
			obj.accumulate("status", message);
		}
		catch(PaidPlanNotFoundException e){
			
			String message="You no longer have access to Trade, Please Renew/Upgrade account to continue";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		catch(Exception e){
			e.printStackTrace();
			String message="Error in the Process of Making Trade Complete";
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
		
		
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException 
	 * @author Thangaraj
	 * @since 18-11-2014
	 */
	@ResponseBody
	@RequestMapping(value="/gettradehistorylist.htm")
	public String getTradeHistoryList(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		log.info("inside getTradeHistoryList()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());

			Integer userId=json.getInt("userid");
			ArrayList<TradesDto> trades=this.tradeService.getTradeHistoryList(userId);
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			if (!(trades.isEmpty())){
			for(TradesDto trade:trades){
				JSONObject tradeJSON=new JSONObject();

				OfferedBy currentUserType = trade.getCurrentUserIs();
				tradeJSON.accumulate("trade id", trade.getTradeId());
				tradeJSON.accumulate("currentUserCanOffer", trade.getIsCurrentUserCanOffer());
				
				//accumulate user's inventory information
				//inventory name, inventory image, inventory parent and sub category, is favourite inventory
				JSONObject tradeFromJSON=new JSONObject();
				tradeFromJSON.accumulate("inventoryname", trade.getTradeFromInventories().getItemId().getItemName());
				tradeFromJSON.accumulate("inventoryid", trade.getTradeFromInventories().getInventoryId());
				if(null!=trade.getTradeFromInventories().getItemId().getCategoryId().getParentCategories() && !trade.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().isEmpty()  ){
					tradeFromJSON.accumulate("parentcategory",trade.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());	
					if(null!=trade.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories()  && !trade.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().isEmpty()){						
						tradeFromJSON.accumulate("categoryname",trade.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().iterator().next().getCategoryName());	
						tradeFromJSON.accumulate("subcategoryname",trade.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						tradeFromJSON.accumulate("subsubcategoryname", trade.getTradeFromInventories().getItemId().getCategoryId().getCategoryName());
					}
					else{
						tradeFromJSON.accumulate("categoryname",trade.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						tradeFromJSON.accumulate("subcategoryname", trade.getTradeFromInventories().getItemId().getCategoryId().getCategoryName());
						tradeFromJSON.accumulate("subsubcategoryname","null");
					}
				}
				else{
					tradeFromJSON.accumulate("categoryname", trade.getTradeFromInventories().getItemId().getCategoryId().getCategoryName());
					tradeFromJSON.accumulate("subcategoryname","null");
					tradeFromJSON.accumulate("subsubcategoryname","null");					
				}
				tradeFromJSON.accumulate("photo", trade.getTradeFromInventories().getPhotoList().get(0));
				tradeFromJSON.accumulate("isFavouriteInventory", trade.getTradeFromInventories().getIsFavouriteInventory());
				
				
				
				
				//accumulate sender's(who sends offer) inventory information
				//inventory name, inventory image, inventory parent and sub category
				JSONObject tradeToJSON=new JSONObject();
				tradeToJSON.accumulate("inventoryname", trade.getTradeToInventories().getItemId().getItemName());
				tradeToJSON.accumulate("inventoryid", trade.getTradeToInventories().getInventoryId());
				if(null!=trade.getTradeToInventories().getItemId().getCategoryId().getParentCategories() && !trade.getTradeToInventories().getItemId().getCategoryId().getParentCategories().isEmpty()  ){
					tradeToJSON.accumulate("parentcategory",trade.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());	
					if(null!=trade.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories()  && !trade.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().isEmpty()){						
						tradeToJSON.accumulate("categoryname",trade.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().iterator().next().getCategoryName());	
						tradeToJSON.accumulate("subcategoryname",trade.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						tradeToJSON.accumulate("subsubcategoryname", trade.getTradeToInventories().getItemId().getCategoryId().getCategoryName());
					}
					else{
						tradeToJSON.accumulate("categoryname",trade.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						tradeToJSON.accumulate("subcategoryname", trade.getTradeToInventories().getItemId().getCategoryId().getCategoryName());
						tradeToJSON.accumulate("subsubcategoryname","null");
					}
				}
				else{
					tradeToJSON.accumulate("categoryname", trade.getTradeToInventories().getItemId().getCategoryId().getCategoryName());
					tradeToJSON.accumulate("subcategoryname","null");
					tradeToJSON.accumulate("subsubcategoryname","null");					
				}
				tradeToJSON.accumulate("photo", trade.getTradeToInventories().getPhotoList().get(0));
				tradeToJSON.accumulate("isFavouriteInventory", trade.getTradeToInventories().getIsFavouriteInventory());

				tradeJSON.accumulate("senderinventories",currentUserType.equals(OfferedBy.TRADER)?tradeFromJSON:tradeToJSON );
				tradeJSON.accumulate("receiverinventories",currentUserType.equals(OfferedBy.VENDOR)?tradeFromJSON:tradeToJSON  );
				
				
				jsonList.add(tradeJSON);
				
			}
			}
			else{
				throw new TradeNotFoundException();
			}
			obj.accumulate("trades", jsonList);
		}  catch (JSONException e) {
			log.error("Some exception occured while parsing input");
			System.out.println("Some exception occured while parsing input");
			obj.accumulate("status", "Error while parsing Input");
		}  catch (TradeNotFoundException e) {
			log.error("Given trade not found");
			System.out.println("Given trade not found");
			obj.accumulate("status", "Given trade not found");
		}  catch (Exception e) {
			e.printStackTrace();
			log.error("Some exception occured. please check catalina.out");
			System.out.println("Some exception occured. please check catalina.out");
			obj.accumulate("status", "Error while fetching history list");
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 * @author Thangaraj
	 * @since 18-11-2014
	 */
	@ResponseBody
	@RequestMapping(value="/gettradehistory.htm")
	public String getTradeHistory(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		log.info("inside getTradeHistory()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());

			Integer userId=json.getInt("userid");
			Integer tradeId=json.getInt("tradeid");
			TradesDto trade = this.tradeService.getTradeHistory(tradeId, userId);

			OfferedBy currentUserType = trade.getCurrentUserIs();
			obj.accumulate("tradeid", trade.getTradeId());
			obj.accumulate("senderid", currentUserType.equals(OfferedBy.TRADER)?trade.getTrader().getUserId():trade.getVendor().getUserId());
			obj.accumulate("sendername", currentUserType.equals(OfferedBy.TRADER)?trade.getTrader().getUserName():trade.getVendor().getUserName());
			obj.accumulate("receiverid", currentUserType.equals(OfferedBy.VENDOR)?trade.getTrader().getUserId():trade.getVendor().getUserId());
			obj.accumulate("receivername",currentUserType.equals(OfferedBy.VENDOR)?trade.getTrader().getUserName():trade.getVendor().getUserName());
			obj.accumulate("tradestatus", trade.getStatus());
			obj.accumulate("currentusercanoffer", trade.getIsCurrentUserCanOffer());
			obj.accumulate("currentuseris", trade.getCurrentUserIs());
			ArrayList<JSONObject> offers = new ArrayList<JSONObject>();
			for(OffersDto offer : trade.getOffers()){
				JSONObject offerJson = new JSONObject();
				offerJson.accumulate("offeredby", offer.getOfferedBy());
				offerJson.accumulate("level", offer.getLevel());
				offerJson.accumulate("offertype", offer.getOfferState());

					InventoryDto inventory = offer.getTraderInventories().get(0);
					JSONObject traderInventory = new JSONObject();
					traderInventory.accumulate("inventoryname", inventory.getItemId().getItemName());
					traderInventory.accumulate("inventoryid", inventory.getInventoryId());
					if(null!=inventory.getItemId().getCategoryId().getParentCategories() && !inventory.getItemId().getCategoryId().getParentCategories().isEmpty()  ){
						//having some doubt in this
						traderInventory.accumulate("parentcategory",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());	
						if(null!=inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories()  && !inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().isEmpty()){						
							traderInventory.accumulate("categoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().iterator().next().getCategoryName());	
							traderInventory.accumulate("subcategoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
							traderInventory.accumulate("subsubcategoryname", inventory.getItemId().getCategoryId().getCategoryName());
						}
						else{
							traderInventory.accumulate("categoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
							traderInventory.accumulate("subcategoryname", inventory.getItemId().getCategoryId().getCategoryName());
							traderInventory.accumulate("subsubcategoryname","null");
						}
					}
					else{
						traderInventory.accumulate("categoryname", inventory.getItemId().getCategoryId().getCategoryName());
						traderInventory.accumulate("subcategoryname","null");
						traderInventory.accumulate("subsubcategoryname","null");					
					}
					traderInventory.accumulate("parentcategoryid", inventory.getItemId().getCategoryId().getCategoryId());
					traderInventory.accumulate("photo", inventory.getPhotoList().get(0));
					traderInventory.accumulate("isFavouriteInventory", inventory.getIsFavouriteInventory());
					

					inventory = offer.getVendorInventories().get(0);
					JSONObject vendorInventory = new JSONObject();
					vendorInventory.accumulate("inventoryname", inventory.getItemId().getItemName());
					vendorInventory.accumulate("inventoryid", inventory.getInventoryId());
					if(null!=inventory.getItemId().getCategoryId().getParentCategories() && !inventory.getItemId().getCategoryId().getParentCategories().isEmpty()  ){
						//having some doubt in this
						vendorInventory.accumulate("parentcategory",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());	
						if(null!=inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories()  && !inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().isEmpty()){						
							vendorInventory.accumulate("categoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().iterator().next().getCategoryName());	
							vendorInventory.accumulate("subcategoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
							vendorInventory.accumulate("subsubcategoryname", inventory.getItemId().getCategoryId().getCategoryName());
						}
						else{
							vendorInventory.accumulate("categoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
							vendorInventory.accumulate("subcategoryname", inventory.getItemId().getCategoryId().getCategoryName());
							vendorInventory.accumulate("subsubcategoryname","null");
						}
					}
					else{
						vendorInventory.accumulate("categoryname", inventory.getItemId().getCategoryId().getCategoryName());
						vendorInventory.accumulate("subcategoryname","null");
						vendorInventory.accumulate("subsubcategoryname","null");					
					}
					vendorInventory.accumulate("parentcategoryid", inventory.getItemId().getCategoryId().getCategoryId());
					vendorInventory.accumulate("photo", inventory.getPhotoList().get(0));
					vendorInventory.accumulate("isFavouriteInventory", inventory.getIsFavouriteInventory());


				offerJson.accumulate("senderinventories",currentUserType.equals(OfferedBy.TRADER)?traderInventory:vendorInventory );
				offerJson.accumulate("receiverinventories",currentUserType.equals(OfferedBy.VENDOR)?traderInventory:vendorInventory  );
				offers.add(offerJson);
				//
			}
			obj.accumulate("offers", offers);
			
		} catch (JSONException e) {
			log.error("Some exception occured while parsing input");
			System.out.println("Some exception occured while parsing input");
			obj.accumulate("status", "Error while parsing Input");
		} catch (TradeNotFoundException e) {
			log.error("Given trade not found");
			System.out.println("Given trade not found");
			obj.accumulate("status", "Given trade not found");
		} catch (TradeInventoryNotFoundException e) {
			log.error("No Inventories found for given trade");
			System.out.println("No Inventories found for given trade");
			obj.accumulate("status", "No Inventories found for given trade");
		} catch (InventoryNotFoundException e) {
			log.error("Inventory found for given trade");
			System.out.println("Inventory found for given trade");
			obj.accumulate("status", "Inventory found for given trade");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Some exception occured. please check catalina.out");
			System.out.println("Some exception occured. please check catalina.out");
			obj.accumulate("status", "Error while fetching history");
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
	}
	
	/**
	 * @author Soujanya
	 * @since november 12, 2014
	 * method to calculate jiuce per month and year
	 * input x
	 * response  jm jy(jiucepermonth, jiuceperyear)
	 */
	@RequestMapping(value="/jiucecalculator.htm")
	@ResponseBody
	public String calculateJiuce(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("inside calculateJiuce()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=getJson(request, response);
			log.info("request : "+json.toString());
			Integer xValue=json.getInt("x");
			Double jm=this.tradeService.calculateJiucePerMonth(xValue);
			Double jy=this.tradeService.calculateJiucePerYear(xValue);
			if(jm>=0){
				obj.accumulate("jm", Math.round(jm));
			}
			else{
				throw new Exception();
			}
			if(jy>=0){
				obj.accumulate("jy", Math.round(jy));
			}
		}
		catch(Exception e){
			log.info("Error While Calculating Juice"+e.toString());
			obj.accumulate("status", "Error in calculation");
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
	}
	
	/**
	 * method to get trader's summary
	 * @input userid
	 * @response arraylist of trader's information
	 * @throws Exception
	 * @author Soujanya
	 * @since november 17,2014
	 */
	@RequestMapping(value="/displaytradersummary.htm")
	@ResponseBody
	public String displayTraderSummary(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("inside displayTraderSummary()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			
			Integer userId=json.getInt("userid");
			//check the plan expiry of user and update if he has enough commission to subscribe.
			this.userService.checkThePlanExpiryOfUserByUserId(userId);
			this.tradeService.updateVendorRatings(userId);
			UserDto userDto=this.userService.getUserbyUserId(userId);
			Integer positiveRatingInPercentage=this.tradeService.getPercentagePositiveRatingOfVendor(userId);
			Map<String, Integer> countsForTraderSummary=this.tradeService.populateCountsForSummary(userId);
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			JSONObject userJSON=new JSONObject();
			userJSON.accumulate("trader id", userId);
			userJSON.accumulate("tradername", userDto.getUserName());
			userJSON.accumulate("city", userDto.getCity());
			userJSON.accumulate("state", userDto.getState());
			userJSON.accumulate("averagerating", Ratings.getState(userDto.getRating()));
			userJSON.accumulate("planname", userDto.getPlans().getPlanName().trim());
			
			userJSON.accumulate("totalUserMessages", countsForTraderSummary.get("totalUserMessages"));
			userJSON.accumulate("totalPendingOffers", countsForTraderSummary.get("totalPendingOffers"));
			userJSON.accumulate("totalNewOffers", countsForTraderSummary.get("totalNewOffers"));
			userJSON.accumulate("totalFavourites", countsForTraderSummary.get("totalFavourites"));
			userJSON.accumulate("totalHistoryTrades", countsForTraderSummary.get("totalHistoryTrades"));
			userJSON.accumulate("totalSavedTrades", countsForTraderSummary.get("totalSavedTrades"));
			userJSON.accumulate("totalnooftrades",  countsForTraderSummary.get("totalnooftrades"));
			if(null != userDto.getAccountExpiryDate()){
				userJSON.accumulate("expirydate", format.format(userDto.getAccountExpiryDate()));
			}
			else{
				userJSON.accumulate("expirydate", "");
			}
			
			if(null != userDto.getAccountCreationDate()){
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				String year = df.format(userDto.getAccountCreationDate());

			userJSON.accumulate("member since", year);
			}
			else{
				userJSON.accumulate("member since", "registered year not found");
			}
			
			userJSON.accumulate("total feedback ratings", userDto.getNoOfRatings());
			userJSON.accumulate("% positive rating", positiveRatingInPercentage);
			
			//iterating over map of counts: contains 
			//total user message count
			//total new offers count
			//total pending offer count
			//total favourites count
			//total saved trade count
			//total trades history count
			/*for (Map.Entry<String, Integer> entry : countsForTraderSummary.entrySet())
			{
			    String countName=entry.getKey();
			    Integer count=entry.getValue();
			    userJSON.accumulate(countName, count);
			}*/
			
			// hardcoded for now, need to change after handling commissions
			userJSON.accumulate("no of referrals", this.refferralService.getRefferralCountByUserId(userId));
			userJSON.accumulate("monthly commission", "4567");
			userJSON.accumulate("yearly commission", "76890");
			
			jsonList.add(userJSON);
			obj.accumulate("trader summary", jsonList);
			
		}
		catch(Exception e){
			e.printStackTrace();
			String message="Some Error Occurred While Fetching Summary";
			log.error(message+" "+e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
	}
	
	/**
	 * method to delete saved trade from saved trades
	 * @input: tradeid, level
	 * @return success status
	 * @throws TradeNotFoundException
	 * @author Soujanya
	 * @since nov 26,2014
	 */
	@RequestMapping(value="/deletesavedtrade.htm")
	@ResponseBody
	public String deleteSavedTrade(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("inside deleteSavedTrade()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			Integer tradeId=json.getInt("tradeid");
			Integer level=json.getInt("level");
		    this.tradeService.deleteSavedTradeInventories(tradeId, level);
			obj.accumulate("status", "Saved Trade is Deleted Successfully");
			}
		catch(TradeInventoryNotFoundException e){
		      log.error("Error in Deleting Saved Trade"+e.toString());
		      obj.accumulate("status", "Error in Deleting Saved Trade");
				}
		catch(Exception e){
			log.error("Some Error Occurred While Deleting Saved Trade"+e.toString());
			obj.accumulate("status", "Some Error Occurred While Deleting Saved Trade");
			}
		return obj.toString();
		
	}
	
	/**
	 * 
	 * @author Soujanya
	 * @since december 7th,2014
	 * @purpose: method to get recent list of ratings given for a vendor
	 * @input userid
	 * @return arraylist of recent ratings of vendor
	 * @throws Exception
	 */
	@RequestMapping(value="/getrecentfeedbacks.htm")
	@ResponseBody
	public String getRecentFeedbacks(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("inside getRecentFeedbacks()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());

			
			Integer vendorId=json.getInt("userid");
			
			ArrayList<VendorRatingsDto> vendorRatings=this.tradeService.getRecentVendorRatings(vendorId);
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			for(VendorRatingsDto vendorRating : vendorRatings){
				String ratingString=null;
				JSONObject ratingJSON=new JSONObject();
				System.out.println(vendorRating.getRating());
				System.out.println(Ratings.getState(vendorRating.getRating()));
				if( null != Ratings.getState(vendorRating.getRating())){
					
					 ratingString=Ratings.getState(vendorRating.getRating()).toString();
//					if(ratingString.contains("Unsatisfied")){
//						ratingString= "Not Satisfied";
//					}
					System.out.println(ratingString);
					ratingJSON.accumulate("rating", ratingString);
					}
					else{
						ratingJSON.accumulate("rating", "vendor has no ratings");
					}
				ratingJSON.accumulate("id", vendorRating.getId());
				ratingJSON.accumulate("comment", vendorRating.getComment());
				jsonList.add(ratingJSON);
				
			}
			obj.accumulate("rating list", jsonList);
			
		}
		
		catch(Exception e){
			
			String message="Vendor Has No Ratings";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
		
	}
	
	
	
	
	
}
