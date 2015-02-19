/**
 *  Created by     : Soujanya
 * Created Date	  : July 4,2014
 * file Name	  : ItemController.java
 * Purpose		  :Handles items Related Operations
 * Type			  : Controller
 * 
 */



package org.kns.traidit.frontend.mobile.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.category.exception.CategoryNotFoundException;
import org.kns.traidit.backend.item.exception.CommentNotSavedOrUpdatedException;
import org.kns.traidit.backend.item.exception.ItemNotFoundException;
import org.kns.traidit.backend.item.exception.ItemNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.FavItemNotFoundException;
import org.kns.traidit.backend.user.exception.FavItemNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.frontend.category.dto.CategoriesDto;
import org.kns.traidit.frontend.common.utility.JsonParser;
import org.kns.traidit.frontend.inventory.dto.InventoryDto;
import org.kns.traidit.frontend.inventory.service.InventoryService;
import org.kns.traidit.frontend.item.dto.ItemsDto;
import org.kns.traidit.frontend.item.service.ItemService;
import org.kns.traidit.frontend.user.dto.FavouriteItemsDto;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import twitter4j.org.json.JSONArray;
import twitter4j.org.json.JSONObject;
import twitter4j.org.json.JSONTokener;

@Controller("ItemController")
public class ItemController {
	
	
	private static Logger log=Logger.getLogger(ItemController.class);
	
	
	@Resource(name="itemService")
	private ItemService itemService;
	
	@Resource(name="inventoryService")
	private InventoryService inventoryService;
	
	@Resource(name="userService")
	private UserService userService;
	
	/*
	 * added by Soujanya on July 4,2014
	 * method to add an item
	 */
	
	@RequestMapping(value="/additem.htm")
	@ResponseBody
	public String addItem(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside addItem()");
		JSONObject obj=new JSONObject();
		String error=null;
		try{
			String status="NOT ADDED";
			JSONObject json=new JSONObject(request.getParameter("json"));
			System.out.println(json.toString());
			System.out.println(request.getHeaderNames());
			System.out.println(request.getContentLength()+request.getContentType());
			String description=json.getString("description");
			Integer userId=Integer.parseInt(json.getString("userid"));
			String itemName=json.getString("itemname");
			String details=json.getString("details");
			String manufacturer=json.getString("manufacturer");
			
			Integer categoryId=json.getInt("categoryid");
			Integer subCategoryId=json.getInt("subcategoryid");
			Integer localDBItem=json.getInt("localdbitem");
			Integer flagEdited=json.getInt("flagedited");
			
			MultipartHttpServletRequest multiRequest= (MultipartHttpServletRequest) request;
			
			List<MultipartFile> image=multiRequest.getFiles("itemphoto");
			String photos = "";
			String videos = "";
			for (MultipartFile multipartFile : image) {
				String imgName = multipartFile.getOriginalFilename();//this.itemService.saveItemPhotos(multipartFile);
				if(!imgName.equals(""))
					photos += imgName+";";
			}
			System.out.println(photos);	

//			MultipartFile video=multiRequest.getFile("itemVideo");
//			videos=this.itemService.getItemVideos(multiRequest,video);
//			System.out.println(videos);
			System.out.println(description);
			System.out.println(itemName);
			System.out.println(manufacturer);
			System.out.println(categoryId);
			Integer result;
			if(localDBItem == 0){
				result=this.itemService.addItem(userId,description,itemName,manufacturer,details,photos,videos,categoryId);
				for (MultipartFile multipartFile : image) {
					this.itemService.saveItemPhotos(multipartFile,result);
				}
			}
			else if(flagEdited !=0){
				InventoryDto inventory = new InventoryDto();
				inventory.setAvailableForTrade(false);
				inventory.setIsEnabled(false);
				inventory.setItemId(this.itemService.getItemById(localDBItem));
				inventory.setListedDate(new Date());
				inventory.setVendorId(this.userService.getUserbyUserId(userId));
				inventory.setDescription(description);
				inventory.setPhotos(photos);
				inventory.setVideo(videos);
				result = this.inventoryService.saveOrUpdateInventory(inventory);// addItem(description,itemName,manufacturer,photos,videos,categoryId);
			}
			else{
				status="Nothing has been edited to save";
				obj.accumulate("status", status);
				return obj.toString();
			}
			if(result>0)
				status="ITEM ADDED";
				obj.accumulate("status", status);
				return obj.toString();
		}
		catch(CategoryNotFoundException e){
			log.error(e.toString());
			e.printStackTrace();
			obj.accumulate("status", "Invalid Category");
			return obj.toString();
		}
		catch(UserNotFoundException e){
			e.printStackTrace();
			log.error(e.toString());
			obj.accumulate("status", "Invalid Vendor");
			return obj.toString();
		}
		catch(ItemNotFoundException e){
			e.printStackTrace();
			log.error(e.toString());
			obj.accumulate("status", "Invalid Item");
			return obj.toString();
		}
		catch(ItemNotSavedOrUpdatedException e){
			e.printStackTrace();
			error=e.toString();
			log.error(error);
			String message="Error in Saving Or Updating Item";
			obj.accumulate("status", message);
			return obj.toString();
		}
		catch(Exception e){
			e.printStackTrace();
			log.info(e.toString());
			obj.accumulate("status", "some error on provided data");
			return obj.toString();
		}
	}
	

	/*
	 * added by Soujanya on July 4,2014
	 * method to delete Item
	 */
	@RequestMapping(value="/deleteitem.htm")
	@ResponseBody
	public String deleteItem(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside deleteItem()");
		JSONObject obj=new JSONObject();
		String error=null;
		try{
			String status="NOT DELETED";
			JSONObject json=JsonParser.getJson(request, response);
			System.out.println(json.toString());
			System.out.println(request.getHeaderNames());
			System.out.println(request.getContentLength()+request.getContentType());
			Integer itemId=json.getInt("itemid");
			System.out.println(itemId);
			this.itemService.deleteItem(itemId);
			status="DELETED ITEM";
			obj.accumulate("status", status);
			return obj.toString();	
		}
		catch(ItemNotFoundException e){
			
			String message="Item Not Found";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
			
		}
		catch(Exception e){
			
			log.error("Error While Deleting Item"+e.toString());
			obj.accumulate("status", "some error while deleting item");
			return obj.toString();
		}
	}
	
	
	
	
	/*
	 * added by Soujanya on July 9,2014
	 * method to add  user's message or review on a item
	 */
	@RequestMapping(value="/addcomment.htm")
	@ResponseBody
	public String addComment(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside addMessage()");
		JSONObject obj=new JSONObject();
		String error=null;
		try{
			String status="NOT ADDED";
			JSONObject json=JsonParser.getJson(request, response);
			System.out.println(json.toString());
			System.out.println(request.getHeaderNames());
			System.out.println(request.getContentLength()+request.getContentType());
			String message=json.getString("message");
			Integer rating=json.getInt("rating");
			Integer userId=json.getInt("userid");
			Integer itemId=json.getInt("itemid");
			System.out.println(rating);
			System.out.println(userId);
			System.out.println(itemId);
			System.out.println(message);
			Integer result=this.itemService.addComment(message,rating,userId,itemId);
			if(result>0){
			status="ADDED MESSAGE";
			obj.accumulate("status", status);
			return obj.toString();
			}
			else{
				throw new CommentNotSavedOrUpdatedException();
			}
			
		}
		catch(CommentNotSavedOrUpdatedException e){
			
			String message="Error in Saving Or Updating Comment";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		catch(UserNotFoundException e){
			
			String message="User Not Found";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		catch(ItemNotFoundException e){
			
			String message="Item Not Found";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		catch(Exception e){
			
			String message="Error While Adding Comment";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		
		
	}
	
	
	
	/*
	 * added by Soujanya on July 11,2014
	 * method to list details of an item for user to view
	 */
	@RequestMapping(value="/displayitemdetails.htm")
	@ResponseBody
	public String displayItemDetails(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside displayItemDetails()");
		JSONObject obj=new JSONObject();
		String error=null;
		try{
			JSONObject json=JsonParser.getJson(request, response);
			Integer itemId=json.getInt("itemid");
			System.out.println(itemId);
			ItemsDto itemsDto=this.itemService.getItemById(itemId);
			System.out.println(itemsDto.getItemName());
			JSONObject itemJSON=new JSONObject();
			itemJSON.accumulate("itemname", itemsDto.getItemName());
			itemJSON.accumulate("description", itemsDto.getDescription());
			itemJSON.accumulate("manufacturer", itemsDto.getManufacturer());
			itemJSON.accumulate("photos", itemsDto.getPhotos());
			itemJSON.accumulate("videos", itemsDto.getVideos());
			itemJSON.accumulate("ratings", itemsDto.getRatings());
			itemJSON.accumulate("itemcategory", itemsDto.getCategoryId().getCategoryName());
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			jsonList.add(itemJSON);
			//JSONArray array=new JSONArray(jsonList);
			JSONObject output=new JSONObject();
			output.accumulate("itemdetails", jsonList);
			return output.toString();
			
			
		}
		catch(ItemNotFoundException e){
			
			String message="Item Not Found";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		catch(Exception e){
			String message="Error While Retrieving Item Details";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
	}
	
	
	
	/*
	 * added by Soujanya on July 11,2014
	 * method to view all items
	 */
	@RequestMapping(value="/displayallitems.htm")
	@ResponseBody
	public String displayAllItems(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside displayAllItems()");
		JSONObject obj=new JSONObject();
		String error=null;
		try{
			ArrayList<ItemsDto> itemsDto=this.itemService.getAllItems(null, null);
			System.out.println(itemsDto);
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			
			for(ItemsDto item:itemsDto){
				JSONObject itemJSON=new JSONObject();
				itemJSON.accumulate("itemname", item.getItemName());
				
				jsonList.add(itemJSON);
				
			}
			JSONArray array=new JSONArray(jsonList);
			JSONObject output=new JSONObject();
			output.accumulate("itemslist", jsonList);
			return output.toString();
			
		}
		catch(ItemNotFoundException e){
			
			String message="Item Not Found";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		catch(Exception e){
			
			String message="Some Error Occurred while Listing Items";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
	}
	
	
	/*
	 * added by Soujanya on July 11,2014
	 * method to view all items
	 */
	@RequestMapping(value="/displayitemsbycategory.htm")
	@ResponseBody
	public String displayItemsByCategory(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside displayItemsByCategory()");
		JSONObject obj=new JSONObject();
		String error=null;
		try{
			
			Properties properties = new Properties();
			String propFileName = "filePath.properties";
	        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
	        properties.load(inputStream);

			JSONObject json=JsonParser.getJson(request, response);
			Integer categoryId=json.getInt("categoryid");
			System.out.println(categoryId);
			ArrayList<ItemsDto> itemsDto=this.itemService.getItemsByCategoryId(categoryId);
			System.out.println(itemsDto);
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			
			for(ItemsDto item:itemsDto){
				JSONObject itemJSON=new JSONObject();
				itemJSON.accumulate("itemname", item.getItemName());
				String photo=item.getPhotos();
				System.out.println(photo);
				String[] photoList=photo.split(";");
				System.out.println(photoList[1]);
			
				//System.out.println(firstPhoto);
				System.out.println(item.getItemId());
				Integer itemNo=item.getItemId();
				String imageUrl= properties.getProperty("itemimagepath")+itemNo+"/"+photoList[1];
				System.out.println(imageUrl);
				itemJSON.accumulate("image", imageUrl);
				itemJSON.accumulate("subcategory", item.getCategoryId().getCategoryName());
				HashSet<CategoriesDto> parents=(HashSet<CategoriesDto>) item.getCategoryId().getParentCategories();
				System.out.println(parents.iterator().next().getCategoryName());
				itemJSON.accumulate("parentcategory", parents.iterator().next().getCategoryName());
				jsonList.add(itemJSON);
				
			}
			JSONArray array=new JSONArray(jsonList);
			JSONObject output=new JSONObject();
			output.accumulate("itemslist", jsonList);
			return output.toString();
			
		}
		catch(ItemNotFoundException e){
			
			String message="Item Not Found";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		catch(Exception e){
			
			String message="Some Error Occurred While Listing Items";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
	}
	
	/*
	 * added by Soujanya on 4th august,2014
	 * method to get all searched items based on keyword
	 */
	@RequestMapping(value="/performItemSearch.htm")
	@ResponseBody
	public String PerformItemSearch(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside PerformItemSearch()");
		JSONObject obj=new JSONObject();
		String error=null;
		try{
			JSONObject json=JsonParser.getJson(request, response);
			final String keyword=json.getString("keyword");
			//String sortBy=json.getString("sortBy");
			//String direction=json.getString("direction");
			ArrayList<ItemsDto> itemsDto=this.itemService.getSearchItems(keyword, null, null, null, null);
			System.out.println(itemsDto);
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			for(ItemsDto item:itemsDto){
				JSONObject itemJSON=new JSONObject();
				System.out.println(item.getItemName());
				itemJSON.accumulate("itemname", item.getItemName());
				
				jsonList.add(itemJSON);
				
			}
			JSONArray array=new JSONArray(jsonList);
			JSONObject output=new JSONObject();
			output.accumulate("itemslist", jsonList);
			return output.toString();
		}
		catch(ItemNotFoundException e){
			
			String message="Item Not Found";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		catch(Exception e){
			
			String message="Some Error Occurred While Searching";
			log.error(message+""+e.toString());
			obj.accumulate("status", message);
			return obj.toString();
		}
		
	}
	
	
	
	
	
	
	
	/**
	 * Handles request to Rebuild entire index of TraiditItem class
	 * @author Thangaraj(KNSTEK)
	 * @since 02-09-2014
	 */
	@RequestMapping(value="/rebuilditemindex.htm")
	@ResponseBody
	public String reBuildIndex(){
		try{
			this.itemService.reBuildIndex();
			return "success";
		}
		catch(Exception e){
			e.printStackTrace();
			return "exception";
		}
	}
	
	
	
	
}
