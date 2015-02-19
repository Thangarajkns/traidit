/**
 *  Created by     : Soujanya
 * Created Date	  : July 4,2014
 * file Name	  : UserFavouritesController.java
 * Purpose		  :Handles inventory Related Operations
 * Type			  : Controller
 * 
 */


package org.kns.traidit.frontend.mobile.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.trade.exception.TradeNotFoundException;
import org.kns.traidit.backend.trade.model.Ratings;
import org.kns.traidit.backend.user.exception.FavItemNotFoundException;
import org.kns.traidit.backend.user.exception.FavItemNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.FavVendorNotFoundException;
import org.kns.traidit.backend.user.exception.FavouritesNotFoundException;
import org.kns.traidit.backend.user.exception.FavouritesNotSavedOrUpdatedException;
import org.kns.traidit.frontend.category.dto.FavouriteCategoryDto;
import org.kns.traidit.frontend.category.service.CategoryService;
import org.kns.traidit.frontend.common.utility.JsonParser;
import org.kns.traidit.frontend.inventory.service.InventoryService;
import org.kns.traidit.frontend.trade.FavouriteTradesDto;
import org.kns.traidit.frontend.trade.service.TradeService;
import org.kns.traidit.frontend.user.dto.FavouriteInventoryDto;
import org.kns.traidit.frontend.user.dto.FavouriteItemsDto;
import org.kns.traidit.frontend.user.dto.FavouriteVendorDto;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import twitter4j.org.json.JSONObject;



@Controller("UserFavouritesController")
public class UserFavouritesController {
	
	private static Logger log=Logger.getLogger(UserFavouritesController.class);
	
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="inventoryService")
	private InventoryService inventoryService;
	
	@Resource(name="categoryService")
	private CategoryService categoryService;
	
	@Resource(name="tradeService")
	private TradeService tradeService;
	/*
	 * added by Soujanya on July 4,2014
	 * method to add favourite item
	 */
	@RequestMapping(value="/addfavitem.htm")
	@ResponseBody
	public String addFavouriteItem(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside addFavouriteItem()");
		JSONObject obj=new JSONObject();
		try{
			String status="NOT ADDED";
			JSONObject json=JsonParser.getJson(request, response);
			System.out.println(json.toString());
			System.out.println(request.getHeaderNames());
			System.out.println(request.getContentLength()+request.getContentType());
			Integer itemId=json.getInt("itemid");
			Integer userId=json.getInt("userid");
			System.out.println(itemId);
			System.out.println(userId);
			Integer result=this.userService.addFavouriteItem(itemId,userId);
			if(result>0){
				status="ADDED FAVOURITE ITEM";
				obj.accumulate("status", status);
				return obj.toString();	
			}
			else{
				throw new FavItemNotSavedOrUpdatedException();
			}
		}
		catch(FavItemNotSavedOrUpdatedException e){
			
			String message="Error in Saving Or Updating Favourite Item";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		catch(Exception e){
			String message="Error in the Process of Updating Favourite Item";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		
		}
	
	
	/*
	 * added by Soujanya on July 4,2014
	 * method to delete favourite Item
	 */
	@RequestMapping(value="/deletefavitem.htm")
	@ResponseBody
	public String deleteFavouriteItem(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside deleteFavouriteItem()");
		JSONObject obj=new JSONObject();
		try{
			String status="NOT DELETED";
			JSONObject json=JsonParser.getJson(request, response);
			System.out.println(json.toString());
			System.out.println(request.getHeaderNames());
			System.out.println(request.getContentLength()+request.getContentType());
			Integer itemId=json.getInt("itemid");
			Integer userId=json.getInt("userid");
			System.out.println("itemId : "+itemId);
			System.out.println("userid : "+userId);
			this.userService.deleteFavouriteItemOfUser(itemId,userId);
			status="DELETED FAVOURITE ITEM";
			obj.accumulate("status", status);
			return obj.toString();	
		}
		catch(FavItemNotFoundException e){
			String message="Favourite Item Not Found";
			obj.accumulate("status", message);
			return obj.toString();
			
		}
		catch(Exception e){
			String message="Error in the Process of Deleting Favourite Item";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
	}
	
	}
	
	/**
	 * method to add favourite vendor
	 * inputs: userid, vendorid
	 * @param request
	 * @param response
	 * @return favourite vendor id, status
	 * @throws Exception
	 */
	@RequestMapping(value="/addfavouritevendor.htm")
	@ResponseBody
	public String addFavouriteVendor(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside addFavouriteItem()");
		JSONObject obj=new JSONObject();
		try{
			String status="NOT ADDED";
			JSONObject json=JsonParser.getJson(request, response);
			System.out.println(json.toString());
			System.out.println(request.getHeaderNames());
			System.out.println(request.getContentLength()+request.getContentType());
			Integer userId=json.getInt("userid");
			Integer vendorId=json.getInt("vendorid");
			System.out.println(userId);
			System.out.println(vendorId);
			Integer result=this.userService.addFavouriteVendor(userId,vendorId);
			if(result>0){
				status="VENDOR ADDED TO FAVOURITES";
				obj.accumulate("favourite vendor id", result);
				obj.accumulate("status", status);
				return obj.toString();	
			}
			else{
				throw new FavouritesNotSavedOrUpdatedException();
			}
		}
		catch(FavouritesNotSavedOrUpdatedException e){
			
			String message="Error in Adding Vendor to Favourites";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		catch(Exception e){
			String message="Error in the Process of Adding Favourite Vendor";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
	}
		
		}
			
			

/**
 * method to delete favourite vendor
 * inputs: favvendorid
 * @param request
 * @param response
 * @return status
 * @throws Exception
 */
	@RequestMapping(value="/deletefavouritevendor.htm")
	@ResponseBody
	public String deleteFavouriteVendor(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside deleteFavouriteVendor()");
		JSONObject obj=new JSONObject();
		try{
			String status="FAVOURITE VENDOR NOT DELETED";
			JSONObject json=JsonParser.getJson(request, response);
			System.out.println(json.toString());
			System.out.println(request.getHeaderNames());
			System.out.println(request.getContentLength()+request.getContentType());
			Integer vendorId=json.getInt("vendorid");
			Integer userId=json.getInt("userid");
			System.out.println("favVendorId : "+vendorId);
			System.out.println("userid : "+userId);
			this.userService.deleteFavouriteVendorOfUser(vendorId,userId);
			status="VENDOR DELETED FROM FAVOURITES";
			obj.accumulate("status", status);
			return obj.toString();	
		}
		catch(FavouritesNotFoundException e){
			String message="Favourite Vendor Not Found";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		catch(Exception e){
			String message="Error in the Process of Deleting Favourite Vendor";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
	}
	
	
	
	
	
	/*
	 * added by Soujanya on July 8,2014
	 * method to view list of favourite items
	 */
	@RequestMapping(value="/displayallfavitems.htm")
	@ResponseBody
	public String displayAllFavouriteItems(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside displayAllFavouriteItems()");
		JSONObject obj=new JSONObject();
		try{
			ArrayList<FavouriteItemsDto> favItems=this.userService.getAllFavouriteItems(null,null);
			System.out.println(favItems);
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			
			for(FavouriteItemsDto item:favItems){
				JSONObject favJSON=new JSONObject();
				favJSON.accumulate("itemname", item.getItemId().getItemName());
				jsonList.add(favJSON);
			}
			JSONObject output=new JSONObject();
			output.accumulate("favitems", jsonList);
			return output.toString();
			
			
		}
		catch(FavItemNotFoundException e){
			
			String message="Favourite Items Not Found";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
			
		}
		catch(Exception e){
			
			String message="Error in Retrieving Favourite Items";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		
	}
	
	
	
	
	/**
	 * added by Soujanya on July 11,2014
	 * method to display all favourite vendors of user
	 * 
	 * modified by Soujanya on november 13,2014
	 * 
	 */
	@RequestMapping(value="/displayallfavvendorsofuser.htm")
	@ResponseBody
	public String displayAllFavouriteVendors(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside displayAllFavouriteItems()");
		JSONObject obj=new JSONObject();
		
		try{
			JSONObject json=JsonParser.getJson(request, response);
			Integer userId=json.getInt("userid");
			ArrayList<FavouriteVendorDto> favVendors=this.userService.getAllFavouriteVendors(userId);
			
			System.out.println(favVendors);
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			
			for(FavouriteVendorDto vendor:favVendors){
				
				BigDecimal distance=this.inventoryService.calculateDistance(userId, vendor.getVendorId().getUserId());
				Integer totalTrades=this.tradeService.getTotalTradeCountOfVendor(vendor.getVendorId().getUserId());
				this.tradeService.updateVendorRatings(vendor.getVendorId().getUserId());
				Integer positiveRatingInPercentage=this.tradeService.getPercentagePositiveRatingOfVendor(vendor.getVendorId().getUserId());
				JSONObject favJSON=new JSONObject();
				favJSON.accumulate("vendorid", vendor.getVendorId().getUserId());
				favJSON.accumulate("vendorname", vendor.getVendorId().getFirstName());
				if( null != Ratings.getState(vendor.getVendorId().getRating())){
				favJSON.accumulate("average rating", Ratings.getState(vendor.getVendorId().getRating()));
			}
			else{
				favJSON.accumulate("average rating", "vendor has no ratings");
			}
				if(null != vendor.getVendorId().getAccountCreationDate()){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = format.parse(vendor.getVendorId().getAccountCreationDate().toString());
					SimpleDateFormat df = new SimpleDateFormat("yyyy");
					String year = df.format(date);
					System.out.println(year);

				favJSON.accumulate("membersince", year);
				}
				else{
					favJSON.accumulate("membersince", "registered year not found");
				}
				favJSON.accumulate("distance", distance);
				favJSON.accumulate("totaltrades", totalTrades);
				favJSON.accumulate("percentagepositiveratings", positiveRatingInPercentage);
				jsonList.add(favJSON);
				
			}
			
			
			JSONObject output=new JSONObject();
			output.accumulate("favvendors", jsonList);
			return output.toString();
			
			
		}
		catch(FavVendorNotFoundException e){
			
			String message="There Are No Favourite Vendors";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
			
		}
		catch(Exception e){
			
			String message="Error in Retrieving Favourite Vendors";
			log.error(message+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		
	}
	
	
	
	
	
	
	
	/**
	 * @author Soujanya
	 * @since September 29,2014
	 * input:  inventoryid, userid
	 * @return favourite inventory id and status
	 * @throws FavouritesNotSavedOrUpdatedException
	 * method to add favourite inventory for an user: an inventory will be marked as favourite
	 * 
	 */
	@RequestMapping(value="/addfavouriteinventory.htm")
	@ResponseBody
	public String addFavouriteInventory(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("inside addFavouriteInventory()");
		JSONObject obj=new JSONObject();
		
		try{
			String status="NOT ADDED";
			JSONObject json=JsonParser.getJson(request, response);
			System.out.println(json.toString());
			System.out.println(request.getHeaderNames());
			System.out.println(request.getContentLength()+request.getContentType());
			Integer userId=json.getInt("userid");
			Integer inventoryId=json.getInt("inventoryid");
			System.out.println(userId);
			System.out.println(inventoryId);
			Integer result=this.inventoryService.addFavouriteInventory(userId,inventoryId);
			if(result>0){
				status="INVENTORY ADDED TO FAVOURITES";
				obj.accumulate("favourite inventory id", result);
				obj.accumulate("status", status);
				return obj.toString();	
			}
			else{
				throw new FavouritesNotSavedOrUpdatedException();
			}
		}
		catch(FavouritesNotSavedOrUpdatedException e){
			
			String message="Error in Adding Inventory to Favourites";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		catch(Exception e){
			String message="Error in the Process of Adding Favourite Inventory";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
	}
		
		
		
	}
	
	
	/**
	 * @author Soujanya
	 * @since september 29,2014
	 * input: inventoryid, userid
	 * method to delete a favourite inventory
	 * ummarks a favourite inventory from favourites
	 * @throws FavouritesNotFoundException
	 * @return status
	 */
	@RequestMapping(value="/deletefavouriteinventory.htm")
	@ResponseBody
	public String deleteFavouriteInventory(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("inside deleteFavouriteInventory()");
		JSONObject obj=new JSONObject();
		
		try{
			String status="NOT DELETED";
			JSONObject json=JsonParser.getJson(request, response);
			System.out.println(json.toString());
			System.out.println(request.getHeaderNames());
			System.out.println(request.getContentLength()+request.getContentType());
			//Integer favInventoryId=json.getInt("favinventoryid");
			Integer userId=json.getInt("userid");
			Integer inventoryId=json.getInt("inventoryid");
			//System.out.println(favInventoryId);
			this.inventoryService.deleteFavouriteInventory(inventoryId,userId);
			status="INVENTORY DELETED FROM FAVOURITES";
			obj.accumulate("status", status);
			return obj.toString();	
		}
		catch(FavouritesNotFoundException e){
			
			String message="Favourite Inventory Not Found";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
			
		}
		catch(Exception e){
			String message="Error in the Process of Deleting Favourite Inventory";
			e.printStackTrace();
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
	}
	}
	
	
	
	
	/**
	 * @author Soujanya
	 * @since september 29,2014
	 * @param categoryid, userid
	 * Purpose: method to add favourite category by user
	 * @throws FavouritesNotSavedOrUpdatedException
	 * @return favourite category id and status
	 */
	@RequestMapping(value="/addfavouritecategory.htm")
	@ResponseBody
	public String addFavouriteCategory(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside addFavouriteCategory()");
		log.info("inside addFavouriteInventory()");
		JSONObject obj=new JSONObject();
		
		try{
			String status="NOT ADDED";
			JSONObject json=JsonParser.getJson(request, response);
			System.out.println(json.toString());
			System.out.println(request.getHeaderNames());
			System.out.println(request.getContentLength()+request.getContentType());
			Integer userId=json.getInt("userid");
			Integer categoryId=json.getInt("categoryid");
			System.out.println(userId);
			System.out.println(categoryId);
			Integer result=this.categoryService.addFavouriteCategory(userId,categoryId);
			if(result>0){
				status="CATEGORY ADDED TO FAVOURITES";
				obj.accumulate("favourite category id", result);
				obj.accumulate("status", status);
				return obj.toString();	
			}
			else{
				throw new FavouritesNotSavedOrUpdatedException();
			}
		}
		catch(FavouritesNotSavedOrUpdatedException e){
			
			String message="Error in Adding Category to Favourites";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		catch(Exception e){
			String message="Error in the Process of Adding Favourite Category";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
	}
	}
	
	
	/**
	 * @author Soujanya
	 * @since september 29,2014
	 * inputs: favcategoryid
	 * purpose: method to delete favourite category by user
	 * unmarks a favourite category from favourites
	 * @throws FavouritesNotFoundException
	 * @return favourite category id and status
	 */
	@RequestMapping(value="/deletefavouritecategory.htm")
	@ResponseBody
	public String deleteFavouriteCategory(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside deleteFavouriteCategory()");
		log.info("inside deleteFavouriteInventory()");
		JSONObject obj=new JSONObject();
		
		try{
			String status="FAVOURITE CATEGORY NOT DELETED";
			JSONObject json=JsonParser.getJson(request, response);
			System.out.println(json.toString());
			System.out.println(request.getHeaderNames());
			System.out.println(request.getContentLength()+request.getContentType());
			Integer userId=json.getInt("userid");
			Integer categoryId=json.getInt("categoryid");
			this.categoryService.deleteFavouriteCategory(categoryId, userId);
			status="CATEGORY IS DELETED FROM FAVOURITES";
			obj.accumulate("status", status);
			return obj.toString();	
		}
		catch(FavouritesNotFoundException e){
			
			String message="Category Not Found in Favourites";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
			
		}
		catch(Exception e){
			String message="Error in the Process of Deleting Category From Favourites";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
	}
	}
	
	
		
		/**
		 * @author Soujanya
		 * @since november 13,2014
		 * method to display list of favourite inventories of user	
		 * @input userid
		 * @throws FavouritesNotFoundException
		 * @return arraylist of favourite inventories of user
		 */
	@RequestMapping(value="/displayallfavinventoriesofuser.htm")
	@ResponseBody
	public String displayAllFavInventories(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("inside displayAllFavInventories()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json							= JsonParser.getJson(request, response);
			Integer userId							= json.getInt("userid");
			Boolean favVendor						= json.getBoolean("favvendor");
			Boolean favCategory						= json.getBoolean("favcategory");
			Integer maxDistance						= json.getInt("maxdistance");
			ArrayList<JSONObject> jsonList			= new ArrayList<JSONObject>();
			
			ArrayList<FavouriteInventoryDto> favInventories=this.inventoryService.getFavouriteInventoriesOfUser(userId, favVendor, favCategory, maxDistance);
			
			for(FavouriteInventoryDto favInventory: favInventories){
				JSONObject inventoryJSON=new JSONObject();
				inventoryJSON.accumulate("inventoryid", favInventory.getInventory().getInventoryId());
				inventoryJSON.accumulate("inventoryname", favInventory.getInventory().getItemId().getItemName());
				inventoryJSON.accumulate("availablefortrade", favInventory.getInventory().getAvailableForTrade());			
				inventoryJSON.accumulate("unitsavailable", favInventory.getInventory().getUnitsAvailable());
				inventoryJSON.accumulate("distance", favInventory.getDistance());
				if(null!=favInventory.getInventory().getItemId().getCategoryId().getParentCategories() && !favInventory.getInventory().getItemId().getCategoryId().getParentCategories().isEmpty()  ){
					inventoryJSON.accumulate("parentcategory",favInventory.getInventory().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());	
					if(null!=favInventory.getInventory().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories()  && !favInventory.getInventory().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().isEmpty()){						
						inventoryJSON.accumulate("categoryname",favInventory.getInventory().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().iterator().next().getCategoryName());	
						inventoryJSON.accumulate("subcategoryname",favInventory.getInventory().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						inventoryJSON.accumulate("subsubcategoryname", favInventory.getInventory().getItemId().getCategoryId().getCategoryName());
						inventoryJSON.accumulate("categoryid", favInventory.getInventory().getItemId().getCategoryId().getCategoryId());
					}
					else{
						inventoryJSON.accumulate("categoryname",favInventory.getInventory().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						inventoryJSON.accumulate("subcategoryname", favInventory.getInventory().getItemId().getCategoryId().getCategoryName());
						inventoryJSON.accumulate("subsubcategoryname","null");
						inventoryJSON.accumulate("categoryid", favInventory.getInventory().getItemId().getCategoryId().getCategoryId());
					}
				}
				else{
					inventoryJSON.accumulate("categoryname", favInventory.getInventory().getItemId().getCategoryId().getCategoryName());
					inventoryJSON.accumulate("subcategoryname","null");
					inventoryJSON.accumulate("subsubcategoryname","null");
					inventoryJSON.accumulate("categoryid", favInventory.getInventory().getItemId().getCategoryId().getCategoryId());					
				}
				ArrayList<JSONObject> photoJsons=new ArrayList<JSONObject>();
				for(String photo:favInventory.getPhotoList()){
					JSONObject photoJson=new JSONObject();
					photoJson.accumulate("image", photo);				
					photoJsons.add(photoJson);
				}				
				inventoryJSON.accumulate("photos", photoJsons);
				jsonList.add(inventoryJSON);
			}
			JSONObject output=new JSONObject();
			output.accumulate("favinventorylist", jsonList);
			System.out.println(output.toString());
			return output.toString();
		}
		
        catch(FavouritesNotFoundException e){
			
			String message="There Are No Favourite Inventories";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
			
		}
		catch(Exception e){
			
			String message="Error in Retrieving Favourite Inventories";
			e.printStackTrace();
			log.error(message+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		
	}
	
	
	
	/**
	 * @author Soujanya
	 * @since november 13, 2014
	 * method to get list of favourite categories of user
	 * @input userid
	 * @return arraylist of fav categories of user
	 * @throws FavouritesNotFoundException
	 * 
	 */
	@RequestMapping(value="/displayallfavcategoriesofuser.htm")
	@ResponseBody
	public String displayFavCategoriesOfUser(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("inside displayFavCategoriesOfUser()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			Integer userId=json.getInt("userid");
			Boolean favouriteVendor=json.getBoolean("favvendor");
			Integer maxDistance=json.getInt("maxdistance");
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			ArrayList<FavouriteCategoryDto> favCategories=this.categoryService.getFavCategoriesOfUser(userId, favouriteVendor,maxDistance);
			for(FavouriteCategoryDto favCategory: favCategories){
				Boolean hasSubcategories=true;
				JSONObject planJSON=new JSONObject();
				planJSON.accumulate("categoryicon", favCategory.getCategory().getCategoryIcon());
				planJSON.accumulate("categoryid", favCategory.getCategory().getCategoryId());
				planJSON.accumulate("categoryname", favCategory.getCategory().getCategoryName());
				planJSON.accumulate("totalNoOfInventories", favCategory.getTotalNoOfInventories());
				if(null== favCategory.getCategory().getSubCategories() ||  favCategory.getCategory().getSubCategories().size()<1){
					hasSubcategories=false;
				}
				planJSON.accumulate("hassubcategories", hasSubcategories);
				
				jsonList.add(planJSON);
			}
			JSONObject output=new JSONObject();
			output.accumulate("favcategorylist", jsonList);
			System.out.println(output.toString());
			return output.toString();
		}
		 catch(FavouritesNotFoundException e){
			
				String message="There Are No Favourite Categories";
				log.error(message+""+e.toString());
			    obj.accumulate("status", message);
				return obj.toString();
				
			}
			catch(Exception e){
				
				String message="Error in Retrieving Favourite Categories";
				e.printStackTrace();
				log.error(message+e.toString());
				obj.accumulate("status", message);
				return obj.toString();
			}
	}
	
	
	
	
	
	/**
	 * method to make accepted and completed trade as favourite
	 * @input: trade id
	 * @return : status
	 * @throws exception
	 * @author Soujanya
	 * @since  december 24,2014
	 * 
	 */
	@RequestMapping(value="/savefavouritetrade.htm")
	@ResponseBody
	public String saveFavouriteTrade(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("inside saveFavouriteTrade()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			Integer userId=json.getInt("userid");
			Integer tradeId=json.getInt("tradeid");
			Integer result=this.tradeService.saveFavouriteTrade(userId,tradeId);
			String status="NOT ADDED";
			if(result>0){
				status="TRADE ADDED TO FAVOURITES";
				obj.accumulate("favourite trade id", result);
				obj.accumulate("status", status);
				return obj.toString();	
			}
			else{
				throw new FavouritesNotSavedOrUpdatedException();
			}
		}
		catch(FavouritesNotSavedOrUpdatedException e){
			
			String message="Error in Adding Trade to Favourites";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		catch(Exception e){
			String message="Error in the Process of Adding Favourite Trade";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
	}
			
		}
	
	
	
	
	
	/**
	 * method to view list of favourite trades of user
	 * @author Soujanya
	 * @since dec 24,2014
	 * 
	 */
	
	@RequestMapping(value="/viewlistoffavouritetrades.htm")
	@ResponseBody
	public String viewListOfFavouriteTrades(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside viewListOfFavouriteTrades()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			Integer userId=json.getInt("userid");
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			Boolean result=this.userService.checkThePlanExpiryOfUserByUserId(userId);
			ArrayList<FavouriteTradesDto> favouriteTradesDto=this.tradeService.getAllFavouriteTradesOfUser(userId);
			if(!(favouriteTradesDto.isEmpty())){
			for(FavouriteTradesDto favTradeDto: favouriteTradesDto){
				
				JSONObject tradeJSON=new JSONObject();
				tradeJSON.accumulate("trade id", favTradeDto.getTradeId().getTradeId());
				tradeJSON.accumulate("currentUserCanOffer", "true");
				
				//accumulate user's inventory information
				//inventory name, inventory image, inventory parent and sub category, is favourite inventory
				JSONObject tradeFromJSON=new JSONObject();
				tradeFromJSON.accumulate("inventoryname", favTradeDto.getTradeFromInventories().getItemId().getItemName());
				tradeFromJSON.accumulate("inventoryid", favTradeDto.getTradeFromInventories().getInventoryId());
				if(null!=favTradeDto.getTradeFromInventories().getItemId().getCategoryId().getParentCategories() && !favTradeDto.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().isEmpty()  ){
					tradeFromJSON.accumulate("parentcategory",favTradeDto.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());	
					if(null!=favTradeDto.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories()  && !favTradeDto.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().isEmpty()){						
						tradeFromJSON.accumulate("categoryname",favTradeDto.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().iterator().next().getCategoryName());	
						tradeFromJSON.accumulate("subcategoryname",favTradeDto.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						tradeFromJSON.accumulate("subsubcategoryname", favTradeDto.getTradeFromInventories().getItemId().getCategoryId().getCategoryName());
					}
					else{
						tradeFromJSON.accumulate("categoryname",favTradeDto.getTradeFromInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						tradeFromJSON.accumulate("subcategoryname", favTradeDto.getTradeFromInventories().getItemId().getCategoryId().getCategoryName());
						tradeFromJSON.accumulate("subsubcategoryname","null");
					}
				}
				else{
					tradeFromJSON.accumulate("categoryname", favTradeDto.getTradeFromInventories().getItemId().getCategoryId().getCategoryName());
					tradeFromJSON.accumulate("subcategoryname","null");
					tradeFromJSON.accumulate("subsubcategoryname","null");					
				}
				tradeFromJSON.accumulate("photo", favTradeDto.getTradeFromInventories().getPhotoList().get(0));
				tradeFromJSON.accumulate("isFavouriteInventory", favTradeDto.getTradeFromInventories().getIsFavouriteInventory());
				
				tradeJSON.accumulate("traderinventorydata", tradeFromJSON);
				
				
				
				//accumulate sender's(who sends offer) inventory information
				//inventory name, inventory image, inventory parent and sub category
				JSONObject tradeToJSON=new JSONObject();
				tradeToJSON.accumulate("inventoryname", favTradeDto.getTradeToInventories().getItemId().getItemName());
				tradeToJSON.accumulate("inventoryid",favTradeDto.getTradeToInventories().getInventoryId());
				if(null!=favTradeDto.getTradeToInventories().getItemId().getCategoryId().getParentCategories() && !favTradeDto.getTradeToInventories().getItemId().getCategoryId().getParentCategories().isEmpty()  ){
					tradeToJSON.accumulate("parentcategory",favTradeDto.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());	
					if(null!=favTradeDto.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories()  && !favTradeDto.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().isEmpty()){						
						tradeToJSON.accumulate("categoryname",favTradeDto.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getParentCategories().iterator().next().getCategoryName());	
						tradeToJSON.accumulate("subcategoryname",favTradeDto.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						tradeToJSON.accumulate("subsubcategoryname", favTradeDto.getTradeToInventories().getItemId().getCategoryId().getCategoryName());
					}
					else{
						tradeToJSON.accumulate("categoryname",favTradeDto.getTradeToInventories().getItemId().getCategoryId().getParentCategories().iterator().next().getCategoryName());
						tradeToJSON.accumulate("subcategoryname", favTradeDto.getTradeToInventories().getItemId().getCategoryId().getCategoryName());
						tradeToJSON.accumulate("subsubcategoryname","null");
					}
				}
				else{
					tradeToJSON.accumulate("categoryname", favTradeDto.getTradeToInventories().getItemId().getCategoryId().getCategoryName());
					tradeToJSON.accumulate("subcategoryname","null");
					tradeToJSON.accumulate("subsubcategoryname","null");					
				}
				tradeToJSON.accumulate("photo", favTradeDto.getTradeToInventories().getPhotoList().get(0));
				tradeToJSON.accumulate("isFavouriteInventory", favTradeDto.getTradeToInventories().getIsFavouriteInventory());
				
				tradeJSON.accumulate("vendorinventorydata", tradeToJSON);
				tradeJSON.accumulate("level", favTradeDto.getCurrentLevel());
				
				jsonList.add(tradeJSON);
				
			}
			}
			else{
				throw new FavouritesNotFoundException();
			}
			obj.accumulate("trades", jsonList);
		
		}
		 catch(FavouritesNotFoundException e){
			
				String message="There Are No Favourite Trades";
				log.error(message+""+e.toString());
			    obj.accumulate("status", message);
				
				
			}
			catch(Exception e){
				
				String message="Error in Retrieving Favourite Trades";
				e.printStackTrace();
				log.error(message+e.toString());
				obj.accumulate("status", message);
				
			}
		
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
		
	}
	
	
	
	
	
	
	
	/**
	 * @author Soujanya
	 * @since dec 24,2014
	 * inputs: favtradeid
	 * purpose: method to delete favourite trade by user
	 * unmarks a favourite trade from favourites
	 * @throws FavouritesNotFoundException
	 * @return favourite trade id and status
	 */
	@RequestMapping(value="/deletefavouritetrade.htm")
	@ResponseBody
	public String deleteFavouriteTrade(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside deleteFavouriteTrade()");
		//log.info("inside deleteFavouriteInventory()");
		JSONObject obj=new JSONObject();
		
		try{
			String status="FAVOURITE TRADE NOT DELETED";
			JSONObject json=JsonParser.getJson(request, response);
			System.out.println(json.toString());
			System.out.println(request.getHeaderNames());
			System.out.println(request.getContentLength()+request.getContentType());
			Integer userId=json.getInt("userid");
			Integer tradeId=json.getInt("tradeid");
			this.tradeService.deleteFavouriteTrade(tradeId, userId);
			status="TRADE IS DELETED FROM FAVOURITES";
			obj.accumulate("status", status);
			return obj.toString();	
		}
		catch(FavouritesNotFoundException e){
			
			String message="Trade Not Found in Favourites";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
			
		}
		catch(Exception e){
			String message="Error in the Process of Deleting Trade From Favourites";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
	}
	}
	}
			
			
		
	
		
	
	
	
	
	
	
	


