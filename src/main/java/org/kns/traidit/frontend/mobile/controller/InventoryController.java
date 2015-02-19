/**
\ *  Created by     : Soujanya
 * Created Date	  : July 4,2014
 * file Name	  : InventoryController.java
 * Purpose		  :Handles inventory Related Operations
 * Type			  : Controller
 * 
 */


package org.kns.traidit.frontend.mobile.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.inventory.exception.InventoryNotFoundException;
import org.kns.traidit.backend.inventory.exception.InventoryNotSavedOrUpdatedException;
import org.kns.traidit.frontend.category.dto.CategoriesDto;
import org.kns.traidit.frontend.common.utility.JsonParser;
import org.kns.traidit.frontend.inventory.dto.InventoryDto;
import org.kns.traidit.frontend.inventory.service.InventoryService;
import org.kns.traidit.frontend.item.dto.ItemsDto;
import org.kns.traidit.frontend.item.service.ItemService;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

@Controller("InventoryController")
public class InventoryController {
	
	private static Logger log=Logger.getLogger(InventoryController.class);
	
	@Resource(name="inventoryService")
	private InventoryService inventoryService;
	
	@Resource(name="itemService")
	private ItemService itemService;
	
	@Resource(name="userService")
	private UserService userService;

	/**
	 * ORIGINALLY ADDED BY SOUJANYA
	* Created by Jeevan on 25-Sep-2014 1:57:30 pm	
	*  Method for: Adding Inventory
	* @param request
	* @param response
	* @return
	* @throws JSONException
	 */
	@RequestMapping(value="/addinventory.htm")
	@ResponseBody
	 public String addInventory(HttpServletRequest request, HttpServletResponse response) throws JSONException{
		 log.info("inside addInventory()");
		 System.out.println("INSIDE INVENTORY CONTROLLER");
		 JSONObject obj=new JSONObject();
		 try{
			 JSONObject json=new JSONObject(request.getParameter("json"));
			 System.out.println("request : "+json.toString());
			 log.info("request : "+json.toString());
			 InventoryDto inventoryDto=this.populateInventoryDtofromJson(json);
			 MultipartHttpServletRequest multiRequest= (MultipartHttpServletRequest) request;	
			 this.inventoryService.addInventoryToDB(inventoryDto, multiRequest);					
			 obj.accumulate("status", "Inventory Added");
		 }
		 catch(InventoryNotSavedOrUpdatedException e){
			 String message="Error While Adding Inventory";
			 log.error(message+" "+e.toString());
			 obj.accumulate("status", message);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 String message="Error While Adding Inventory";
			 log.error(message+" "+e.toString());
			 obj.accumulate("status", message);
		 }
		 String result = obj.toString();
		 log.info("response : "+result);
		 System.out.println("response : "+result);
		 return result;
	 }
	
	/**
	 * 
	* Created by Jeevan on 24-Sep-2014 4:53:33 pm	
	*  Method for: Parsing JSON and populate InventoryDto
	* @param json
	* @return
	 */
	private InventoryDto populateInventoryDtofromJson(JSONObject json){
		InventoryDto inventoryDto=new InventoryDto();
		try {
			String description=json.getString("description");
			Integer userId=Integer.parseInt(json.getString("userid"));
			String itemName=json.getString("itemname");
			String details=json.getString("details");
			String upc=json.getString("upc");
			//remove prepended 0 from upc
			if(!upc.equals("") && upc.substring(0, 1).equals("0"))
				upc = upc.substring(1);
			Integer categoryId=json.getInt("categoryid");
			Integer subCategoryId=json.getInt("subcategoryid");
			Integer subsubCategoryId=json.getInt("subsubcategoryid");
			Integer localDBItem=json.getInt("localdbitem");
			System.out.println("LOCAL DB "+localDBItem);
			Integer flagEdited=json.getInt("flagedited");
			Integer flagImageEdited=json.getInt("flagimageedited");
			Integer flagManual=json.getInt("flagmanual");
			Integer availableForTrade=json.getInt("availablefortrade");
			Integer unitsAvailable=json.getInt("unitsavailable");
			String manufacturer=json.getString("manufacturer");
		
			ItemsDto itemDto=new ItemsDto();
			itemDto.setDescription(description);
			itemDto.setDetails(details);
			itemDto.setItemName(itemName);
			inventoryDto.setLocalDBItem(localDBItem);
			itemDto.setManufacturer(manufacturer);
			CategoriesDto categoriesDto=new CategoriesDto();
			categoriesDto.setCategoryId(categoryId);
			CategoriesDto subCategoriesDto=new CategoriesDto();
			subCategoriesDto.setCategoryId(subCategoryId);
			CategoriesDto subSubCategory=new CategoriesDto();
			subSubCategory.setCategoryId(subsubCategoryId);
			itemDto.setCategoryId(categoriesDto);
			itemDto.setSubCategory(subCategoriesDto);
			itemDto.setSubSubCategory(subSubCategory);
			UserDto userDto=new UserDto();
			userDto.setUserId(userId);
			itemDto.setUpc(upc);
			inventoryDto.setItemId(itemDto);
			inventoryDto.setVendorId(userDto);
			if(availableForTrade==1){
				inventoryDto.setAvailableForTrade(true);
			}else{
				inventoryDto.setAvailableForTrade(false);
			}			
			inventoryDto.setFlagEdited(flagEdited);
			inventoryDto.setFlagManual(flagManual);
			inventoryDto.setFlagImageEdited(flagImageEdited);
			inventoryDto.setUnitsAvailable(unitsAvailable);
			
		} catch (JSONException e) {
			log.error("Un able to parse JSON PROPERLY "+e.toString());
			e.printStackTrace();
		}
		
		return inventoryDto;
	}
	
	
	/*
	 * added by Soujanya on july 9,2014
	 * method to delete inventory
	 */
	@RequestMapping(value="/deleteinventory.htm")
	@ResponseBody
	public String deleteInventory(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside deleteInventory()");
		JSONObject obj=new JSONObject();
		try{
			String status="NOT DELETED";
			JSONObject json=JsonParser.getJson(request, response);
			System.out.println("request : "+json.toString());
			log.info("request : "+json.toString());
			Integer inventoryId=json.getInt("inventoryid");
			System.out.println(inventoryId);
			this.inventoryService.deleteInventory(inventoryId);
			status="DELETED INVENTORY";
			obj.accumulate("status", status);
		}
		catch(InventoryNotFoundException e){
			
			log.error("Item Not Found"+e.toString());
			String message="Inventory Not Found";
			obj.accumulate("status", message);
		}
		catch(Exception e){
			
			log.error("Error in deleting inventory"+e.toString());
			String message="Some Error Occurred While Deleting Inventory";
			obj.accumulate("status", message);
		}
		 String result = obj.toString();
		 log.info("response : "+result);
		 System.out.println("response : "+result);
		 return result;
	}
	
	
	
	/*
	 * added by Soujanya on july 9,2014
	 * method to edit inventory details
	 */
	@RequestMapping(value="/editinventory.htm")
	@ResponseBody
	public String editInventory(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside editInventory()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			 System.out.println("request : "+json.toString());
			 log.info("request : "+json.toString());
			Integer inventoryId=json.getInt("inventoryid");
			InventoryDto inventory=this.inventoryService.editInventory(inventoryId);
			
			JSONObject inventoryJSON=new JSONObject();
			inventoryJSON.accumulate("availableforsale", inventory.getAvailableForTrade());
			inventoryJSON.accumulate("price", inventory.getPrice());
			inventoryJSON.accumulate("unitsavailable", inventory.getUnitsAvailable());
			//inventoryJSON.accumulate("itemid", inventory.getItemId());
			//inventoryJSON.accumulate("vendorid", inventory.getVendorId());
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			jsonList.add(inventoryJSON);
			obj.accumulate("inventorylist", jsonList);
			
		}
		catch(InventoryNotFoundException e){
			
			log.error("Item Not Found"+e.toString());
			String message="Inventory Not Found";
			obj.accumulate("status", message);
		}
		catch(Exception e){
			
			log.error("Error in Editing Inventory"+e.toString());
			String message="Some Error Occurred While trying to Edit Inventory";
			obj.accumulate("status", message);
		}
		 String result = obj.toString();
		 log.info("response : "+result);
		 System.out.println("response : "+result);
		 return result;
	}
	
	
	/*
	 * added by Soujanya on july 10,2014
	 * method to save and update inventory details that are edited
	 */
	@RequestMapping(value="/saveeditedinventory.htm")
	@ResponseBody
	public String saveEditedInventory(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside saveEditedInventory()");
		JSONObject obj=new JSONObject();
		try{
			String status="NOT SAVED";
			 JSONObject json=new JSONObject(request.getParameter("json"));
			 System.out.println("request : "+json.toString());
			 log.info("request : "+json.toString());
			 InventoryDto inventoryDto=this.populateInventoryDtofromJson(json);
			
		//	log.info("request : "+json.toString());
			
			Integer inventoryId=json.getInt("inventoryid");
			
			 inventoryDto.setInventoryId(inventoryId);
			 MultipartHttpServletRequest multiRequest= (MultipartHttpServletRequest) request;	
			Integer result=this.inventoryService.saveEditedInventory(inventoryDto, multiRequest);
			if(result>0){
				status="SAVED AND UPDATED INVENTORY";
				obj.accumulate("status", status);
			}
			else{
				throw new InventoryNotSavedOrUpdatedException();
			}
		}
		catch(InventoryNotFoundException e){
		
		log.error("Inventory Not Found"+e.toString());
		
		String message="Item Not Found";
		obj.accumulate("status", message);
	}		
		catch(InventoryNotSavedOrUpdatedException e){
			
			log.error("Error in Saving Or Updating Inventory"+e.toString());
			String message="Error in the Process Of Editing inventory";
			obj.accumulate("status", message);
		}
		catch(Exception e){
			e.printStackTrace();
			log.error("Error in Updating Inventory"+e.toString());
			String message="Some Error Occurred while Editing Inventory";
			obj.accumulate("status", message);
		}
		 String result = obj.toString();
		 log.info("response : "+result);
		 System.out.println("response : "+result);
		 return result;
	}
	
	
	/*
	 * added by Soujanya on july 10,2014
	 * method to display inventory details to user
	 * 
	 * modified by Thangaraj on 29-12-2014 to accumulate category related data
	 */
	@RequestMapping(value="/displayinventorydetails.htm")
	@ResponseBody
	public String displayInventoryDetails(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside displayInventoryDetails()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			 System.out.println("request : "+json.toString());
			 log.info("request : "+json.toString());
			 
			Integer inventoryId=json.getInt("inventoryid");
			Integer userId=json.getInt("userid");
			//Needs to have a field Category ID
			Integer categoryId=json.getInt("categoryid");
			InventoryDto inventory=this.inventoryService.getInventoryToDisplay(inventoryId,userId,categoryId);	
			
			System.out.println("UNIT S :"+inventory.getUnitsAvailable() );
			obj.accumulate("inventoryid", inventory.getInventoryId());
			obj.accumulate("availablefortrade", inventory.getAvailableForTrade());			
			obj.accumulate("unitsavailable", inventory.getUnitsAvailable());
			obj.accumulate("itemname", inventory.getItemId().getItemName());
			obj.accumulate("itemdescription", inventory.getItemId().getDescription());
			if(null!=inventory.getVendorId().getFirstName() && inventory.getVendorId().getFirstName().trim().length()>0){
				obj.accumulate("vendorname", inventory.getVendorId().getFirstName());
			}
			else{
				obj.accumulate("vendorname", "null");
			}
			
			obj.accumulate("itemdetails", inventory.getItemId().getDetails());			
			obj.accumulate("distance", inventory.getDistance());
			ArrayList<JSONObject> photoJsons=new ArrayList<JSONObject>();
			for(String photo:inventory.getPhotoList()){
				JSONObject photoJson=new JSONObject();
				photoJson.accumulate("image", photo);				
				photoJsons.add(photoJson);
			}				
			obj.accumulate("photos", photoJsons);
			if(null!=inventory.getVendorId().getState() && inventory.getVendorId().getState().trim().length()>0){
				obj.accumulate("state", inventory.getVendorId().getState());
			}
			else{
				obj.accumulate("state", "null");
			}
			if(null!=inventory.getVendorId().getCity() && inventory.getVendorId().getCity().trim().length()>0){
				obj.accumulate("city", inventory.getVendorId().getCity());
			}
			else{
				obj.accumulate("city", "null");
			}
			
			//Hardcoded fields for now. needs to updated in future
			obj.accumulate("rating", "delighted");
			obj.accumulate("trades", "20");
			obj.accumulate("isfavouriteinventory", inventory.getIsFavouriteInventory());
			obj.accumulate("vendorid", inventory.getVendorId().getUserId());
			
			//accumulate category related data
			if(null!=inventory.getItemId().getCategoryId().getParentCategories() && !inventory.getItemId().getCategoryId().getParentCategories().isEmpty()  ){
				obj.accumulate("parentcategory",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());	
				if(null!=inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories()  && !inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().isEmpty()){						
					obj.accumulate("categoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().iterator().next().getCategoryName());	
					obj.accumulate("subcategoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
					obj.accumulate("subsubcategoryname", inventory.getItemId().getCategoryId().getCategoryName());
					//Added by Jeevan on December 31, 2014\
					obj.accumulate("categoryid",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().iterator().next().getCategoryId());	
					obj.accumulate("subcategoryid",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryId());
					obj.accumulate("subsubcategoryid", inventory.getItemId().getCategoryId().getCategoryId());
					
					
				}
				else{
					obj.accumulate("categoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
					obj.accumulate("subcategoryname", inventory.getItemId().getCategoryId().getCategoryName());
					obj.accumulate("subsubcategoryname","null");
					//Added by Jeevan on December 31, 2014\
					obj.accumulate("categoryid",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryId());
					obj.accumulate("subcategoryid", inventory.getItemId().getCategoryId().getCategoryId());
					obj.accumulate("subsubcategoryid","null");
				}
			}
			else{
				obj.accumulate("categoryname", inventory.getItemId().getCategoryId().getCategoryName());
				obj.accumulate("subcategoryname","null");
				obj.accumulate("subsubcategoryname","null");	
				//Added by Jeevan on December 31, 2014\
				obj.accumulate("categoryid", inventory.getItemId().getCategoryId().getCategoryId());
				obj.accumulate("subcategoryid","null");
				obj.accumulate("subsubcategoryid","null");		
			}
			
		}
		catch(InventoryNotFoundException e){			
			log.error("Item Not Found"+e.toString());
			String message="Item Not Found";
			obj.accumulate("status", message);
		}
		catch(Exception e){
			
			log.error("Error in getting Inventory Details"+e.toString());
			String message="Error Getting Inventory Details";
			e.printStackTrace();
			obj.accumulate("status", message);
		}
		 String result = obj.toString();
		 log.info("response : "+result);
		 System.out.println("response : "+result);
		 return result;
	}
	
	
	/*
	 * added by Soujanya on July 22,2014
	 * method to change availability status of inventory
	 * input: inventoryid
	 * 
	 */
	@RequestMapping(value="/changeinventoryavailability.htm")
	@ResponseBody
	public String changeInventoryAvailability(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside changeInventoryAvailability()");
		JSONObject obj=new JSONObject();
		try{
			String status="Inventory availability not changed";
			JSONObject json=JsonParser.getJson(request, response);
			 System.out.println("request : "+json.toString());
			 log.info("request : "+json.toString());
			System.out.println(request.getHeaderNames());
			System.out.println(request.getContentLength()+request.getContentType());
			Integer inventoryId=json.getInt("inventoryid");
			Boolean availableForSale=json.getBoolean("availableforsale");
			Integer result=this.inventoryService.changeInventoryAvailability(inventoryId, availableForSale);
			if(result>0){
				status="inventory availability changed";
				obj.accumulate("status", status);
			}
			else{
				throw new InventoryNotSavedOrUpdatedException();
			}
		}
		catch(InventoryNotSavedOrUpdatedException e){
			
			log.error("Error in Changing status of Inventory Availability"+e.toString());
			String message="Error in Changing status of Inventory Availability";
			obj.accumulate("status", message);
		}
		catch(InventoryNotFoundException e){
			
			log.error("Item Not Found"+e.toString());
			String message="Item Not Found";
			obj.accumulate("status", message);
		}
		catch(Exception e){
			
			log.error("Error While Changing Inventory Availability"+e.toString());
			String message="Some Error Occurred While Changing Inventory Availability Status";
			obj.accumulate("status", message);
		}
		 String result = obj.toString();
		 log.info("response : "+result);
		 System.out.println("response : "+result);
		 return result;
	}
	
	
	/**
	 * added by Soujanya on september 23,2014
	 * method to display list of inventories under a given category
	 * @throws InventoryNotFoundException
	 * @return json array of inventory list and its details
	 */
	@RequestMapping(value="/displayinventorylistbycategory.htm")
	@ResponseBody
	public String displayInventoryListByCategory(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside displayInventoryListByCategory()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			 System.out.println("request : "+json.toString());
			 log.info("request : "+json.toString());
			Integer categoryId						= json.getInt("categoryid");
			Integer userId							= json.getInt("userid");
			Boolean favVendor						= json.getBoolean("favvendor");
			Integer maxDistance						= json.getInt("maxdistance");
			ArrayList<InventoryDto> inventoryDto	= this.inventoryService.getInventoriesByCategoryId(categoryId,userId, favVendor, maxDistance);
			ArrayList<JSONObject> jsonList			= new ArrayList<JSONObject>();
			SimpleDateFormat dateFormat 			= new SimpleDateFormat("MM/dd/yyyy");
			for(InventoryDto inventory: inventoryDto){
				JSONObject inventoryJSON=new JSONObject();
				inventoryJSON.accumulate("inventoryid", inventory.getInventoryId());
				inventoryJSON.accumulate("inventoryname", inventory.getItemId().getItemName());
				inventoryJSON.accumulate("availablefortrade", inventory.getAvailableForTrade());			
				inventoryJSON.accumulate("unitsavailable", inventory.getUnitsAvailable());
				inventoryJSON.accumulate("distance", inventory.getDistance());
				inventoryJSON.accumulate("listeddate", dateFormat.format(inventory.getListedDate()));
				inventoryJSON.accumulate("traderrating", inventory.getVendorId().getRating());
				if(null!=inventory.getItemId().getCategoryId().getParentCategories() && !inventory.getItemId().getCategoryId().getParentCategories().isEmpty()){
					inventoryJSON.accumulate("parentcategory",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());	
					if(null!=inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories()  && !inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().isEmpty()){						
						inventoryJSON.accumulate("categoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().iterator().next().getCategoryName());	
						inventoryJSON.accumulate("subcategoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						inventoryJSON.accumulate("subsubcategoryname", inventory.getItemId().getCategoryId().getCategoryName());
						
					}
					else{
						inventoryJSON.accumulate("categoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						inventoryJSON.accumulate("subcategoryname", inventory.getItemId().getCategoryId().getCategoryName());
						inventoryJSON.accumulate("subsubcategoryname","null");
					}
				}
				else{
					inventoryJSON.accumulate("categoryname", inventory.getItemId().getCategoryId().getCategoryName());
					inventoryJSON.accumulate("subcategoryname","null");
					inventoryJSON.accumulate("subsubcategoryname","null");		
					
				}
				inventoryJSON.accumulate("categoryid", inventory.getItemId().getCategoryId().getCategoryId());	
				ArrayList<JSONObject> photoJsons=new ArrayList<JSONObject>();
				for(String photo:inventory.getPhotoList()){
					JSONObject photoJson=new JSONObject();
					photoJson.accumulate("image", photo);				
					photoJsons.add(photoJson);
				}				
				inventoryJSON.accumulate("photos", photoJsons);
				jsonList.add(inventoryJSON);
			}
			obj.accumulate("inventorylist", jsonList);
		}
		catch(InventoryNotFoundException e){
			log.error("Inventory Not Found"+e.toString());
			String message="No items available";
			obj.accumulate("status", message);
		}	
		//database connection error
		catch(CannotCreateTransactionException e){
			log.error("Error while connecting to database");
			String message="Database error, please try again later";
			obj.accumulate("status", message);
		}
		catch(Exception e){
			log.error("Error in listing Inventories"+e.toString());
			String message="Error While Getting Inventories";
			obj.accumulate("status", message);
		}
		 String result = obj.toString();
		 log.info("response : "+result);
		 System.out.println("response : "+result);
		 return result;
	}
	
	
	/**
	 * @author Soujanya
	 * @since october 13,2014
	 * @purpose method to list inventories of a vendor/user
	 * @throws InventoryNotFoundException
	 * @return array of inventories
	 * 
	 * 
	 * 
	 * Edited By Jeevan on Jan 02, 2015
	 * Added filter for Max distance
	 * 
	 * Jeevan changes removed by thangaraj Jan 06, 2015
	 * 
	 * 
	 */
	@RequestMapping(value="/displayinventoriesofuser.htm")
	@ResponseBody
	public String displayInventoriesOfUser(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside displayInventoriesOfUser()");
		System.out.println("inside displayInventoriesOfUser()");
		JSONObject obj=new JSONObject();
		try{
		JSONObject json=JsonParser.getJson(request, response);
		log.info("request : "+json.toString());
		System.out.println("request : "+json.toString());
		Integer userId							= json.getInt("userid");
		Integer vendorId						= json.getInt("vendorid");
		Boolean favCategory						= json.getBoolean("favcategory");
		Boolean favInventory					= json.getBoolean("favinventory");
		SimpleDateFormat dateFormat 			= new SimpleDateFormat("MM/dd/yyyy");
		ArrayList<InventoryDto> inventoryDto 	= null;
		
		if(!userId.equals(vendorId)){
			inventoryDto=this.inventoryService.getInventoriesOfUser(userId,vendorId, favCategory, favInventory);
		}
		else{
			inventoryDto=this.inventoryService.getInventoriesOfUser(userId,vendorId, false, false);
		}
		ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
		for(InventoryDto inventory: inventoryDto){
			JSONObject inventoryJSON=new JSONObject();
			inventoryJSON.accumulate("inventoryid", inventory.getInventoryId());
			inventoryJSON.accumulate("inventoryname", inventory.getItemId().getItemName());
			inventoryJSON.accumulate("availablefortrade", inventory.getAvailableForTrade());			
			inventoryJSON.accumulate("unitsavailable", inventory.getUnitsAvailable());
			inventoryJSON.accumulate("listeddate", dateFormat.format(inventory.getListedDate()));
			if(null!=inventory.getItemId().getCategoryId().getParentCategories() && !inventory.getItemId().getCategoryId().getParentCategories().isEmpty()  ){
				inventoryJSON.accumulate("parentcategory",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());	
				if(null!=inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories()  && !inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().isEmpty()){						
					inventoryJSON.accumulate("categoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().iterator().next().getCategoryName());	
					inventoryJSON.accumulate("subcategoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
					inventoryJSON.accumulate("subsubcategoryname", inventory.getItemId().getCategoryId().getCategoryName());	
					inventoryJSON.accumulate("subcategoryid",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryId());
					inventoryJSON.accumulate("subsubcategoryid", inventory.getItemId().getCategoryId().getCategoryId());
				}
				else{
					inventoryJSON.accumulate("categoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
					inventoryJSON.accumulate("subcategoryname", inventory.getItemId().getCategoryId().getCategoryName());
					inventoryJSON.accumulate("subsubcategoryname","null");
					inventoryJSON.accumulate("subcategoryid", inventory.getItemId().getCategoryId().getCategoryId());
					inventoryJSON.accumulate("subsubcategoryid","null");
				}
			}
			else{
				inventoryJSON.accumulate("categoryname", inventory.getItemId().getCategoryId().getCategoryName());
				inventoryJSON.accumulate("subcategoryname","null");
				inventoryJSON.accumulate("subsubcategoryname","null");		
				inventoryJSON.accumulate("subcategoryid", "null");
				inventoryJSON.accumulate("subsubcategoryid","null");			
			}
			inventoryJSON.accumulate("categoryid", inventory.getItemId().getCategoryId().getCategoryId());	
			ArrayList<JSONObject> photoJsons=new ArrayList<JSONObject>();
			for(String photo:inventory.getPhotoList()){
				JSONObject photoJson=new JSONObject();
				photoJson.accumulate("image", photo);				
				photoJsons.add(photoJson);
			}				
			inventoryJSON.accumulate("photos", photoJsons);
			jsonList.add(inventoryJSON);
		}
		obj.accumulate("inventorylist", jsonList);
		}
		catch(InventoryNotFoundException e){			
			System.out.println("InventoryNotFoundException");
			log.error("Inventory Not Found"+e.toString());
			String message="No items available";
			obj.accumulate("status", message);
		}		
		catch(Exception e){		
			e.printStackTrace();
			log.error("Error in listing Inventories"+e.toString());
			String message="Error While Getting Inventories";
			obj.accumulate("status", message);
		}
		String resultString = obj.toString();
		log.info("response : "+resultString);
		System.out.println("response : "+ resultString);
		return resultString;
		
	}
	
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 24-Dec-2014
	 * 
	 * modified by Thangaraj on Jan 6,2015. listing all Inventories under specific category feature added
	 */
	@RequestMapping(value="/displayentireinventoriesforuser.htm")
	@ResponseBody
	public String displayEntireInventoryListForUser(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		log.info("inside displayEntireInventoryListForUser()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());
			
			//parse all inputs from json request
			Integer userId							= json.getInt("userid");
			Boolean favCategory						= json.getBoolean("favcategory");
			Boolean favInventory					= json.getBoolean("favinventory");
			Integer maxDistance						= json.getInt("maxdistance");
			Integer categoryId						= json.getInt("category");
			
			//local variable declarations and initialisations
			SimpleDateFormat dateFormat 			= new SimpleDateFormat("MM/dd/yyyy");
			ArrayList<InventoryDto> inventoryDtos = null;
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			
			//request services
			if(categoryId != 0){
				inventoryDtos=this.inventoryService.getEntireInventoriesOfCategoryForUser(userId,favInventory,favCategory,maxDistance,categoryId);
			}
			else{
				inventoryDtos=this.inventoryService.getEntireInventoriesForUser(userId,favInventory,favCategory,maxDistance);
			}
			
			//populate output json object with data
			for(InventoryDto inventory: inventoryDtos){
				JSONObject inventoryJSON=new JSONObject();
				inventoryJSON.accumulate("inventoryid", inventory.getInventoryId());
				inventoryJSON.accumulate("inventoryname", inventory.getItemId().getItemName());
				inventoryJSON.accumulate("availablefortrade", inventory.getAvailableForTrade());			
				inventoryJSON.accumulate("unitsavailable", inventory.getUnitsAvailable());
				inventoryJSON.accumulate("listeddate", dateFormat.format(inventory.getListedDate()));
				if(null!=inventory.getItemId().getCategoryId().getParentCategories() && !inventory.getItemId().getCategoryId().getParentCategories().isEmpty()  ){
					inventoryJSON.accumulate("parentcategory",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());	
					if(null!=inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories()  && !inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().isEmpty()){						
						inventoryJSON.accumulate("categoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().iterator().next().getCategoryName());	
						inventoryJSON.accumulate("subcategoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						inventoryJSON.accumulate("subsubcategoryname", inventory.getItemId().getCategoryId().getCategoryName());
					}
					else{
						inventoryJSON.accumulate("categoryname",inventory.getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						inventoryJSON.accumulate("subcategoryname", inventory.getItemId().getCategoryId().getCategoryName());
						inventoryJSON.accumulate("subsubcategoryname","null");
					}
				}
				else{
					inventoryJSON.accumulate("categoryname", inventory.getItemId().getCategoryId().getCategoryName());
					inventoryJSON.accumulate("subcategoryname","null");
					inventoryJSON.accumulate("subsubcategoryname","null");					
				}
				inventoryJSON.accumulate("categoryid", inventory.getItemId().getCategoryId().getCategoryId());	
				ArrayList<JSONObject> photoJsons=new ArrayList<JSONObject>();
				for(String photo:inventory.getPhotoList()){
					JSONObject photoJson=new JSONObject();
					photoJson.accumulate("image", photo);				
					photoJsons.add(photoJson);
				}				
				inventoryJSON.accumulate("photos", photoJsons);
				jsonList.add(inventoryJSON);
			}
			obj.accumulate("inventorylist", jsonList);
		}
		catch(InventoryNotFoundException e){
			System.out.println("InventoryNotFoundException");
			log.error("Inventory Not Found"+e.toString());
			String message="No items available";
			obj.accumulate("status", message);
		}		
		catch(Exception e){		
			e.printStackTrace();
			log.error("Error in listing Inventories"+e.toString());
			String message="Error While Getting Inventories";
			obj.accumulate("status", message);
		}
		String resultString = obj.toString();
		log.info("response : "+resultString);
		System.out.println("response : "+ resultString);
		return resultString;
	
	}
	
}
