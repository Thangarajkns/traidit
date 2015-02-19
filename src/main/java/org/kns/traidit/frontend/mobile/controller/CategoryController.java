/**
 *  Created by     : Soujanya
 * Created Date	  : July 30,2014
 * file Name	  : CategoryController.java
 * Purpose		  :Handles Category Related Operations
 * Type			  : Controller
 * 
 */



package org.kns.traidit.frontend.mobile.controller;

import java.util.ArrayList;
import java.util.Collections;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.category.exception.CategoryNotFoundException;
import org.kns.traidit.frontend.category.dto.CategoriesDto;
import org.kns.traidit.frontend.category.service.CategoryService;
import org.kns.traidit.frontend.common.utility.JsonParser;
import org.kns.traidit.frontend.inventory.service.InventoryService;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.kns.traidit.frontend.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;


@Controller("CategoryController")
public class CategoryController {
	
private static Logger log=Logger.getLogger(CategoryController.class);
	
	@Resource(name="categoryService")
	private CategoryService categoryService;
	
	@Resource(name="inventoryService")
	private InventoryService inventoryService;
	
	@Resource(name="userService")
	private UserService userService;
	
	
	/*
	 * added by Soujanya on august 6,2014
	 * method to display list of subcategories of a category
	 */
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return a JSON String containing  entire list of category DTO objects.
	 * @throws Exception
	 * @author Thangaraj(KNSTEK)
	 * @since 24-09-2014
	 */
	@RequestMapping(value="/viewsubcategories.htm")
	@ResponseBody
	public String viewSubCategories(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("inside viewSubCategories() controller");
		System.out.println("inside viewSubCategories() controller");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());
			Integer categoryId=json.getInt("categoryid");
			Integer userId=json.getInt("userid");
			Boolean favVendor=json.getBoolean("favvendor");
			Boolean favCategory=json.getBoolean("favcategory");
			Integer maxDistance=json.getInt("maxdistance");
			Boolean noEmptyCategories=json.getBoolean("noemptycategories");
			ArrayList<CategoriesDto> categoryDtos=this.categoryService.getSubCategoryWithFavourites(categoryId, userId, favVendor,favCategory, maxDistance);
			ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
			for(CategoriesDto category: categoryDtos){
				if (noEmptyCategories.equals(true)  && category.getTotalNoOfInventories()>0){
					JSONObject plainJSON=new JSONObject();
					plainJSON.accumulate("categoryicon", category.getCategoryIcon());
					plainJSON.accumulate("isfavouritecategory", category.getIsFavouriteCategory());
					plainJSON.accumulate("categoryid", category.getCategoryId());
					plainJSON.accumulate("categoryname", category.getCategoryName());
					plainJSON.accumulate("totalNoOfInventories", category.getTotalNoOfInventories());
					plainJSON.accumulate("hassubcategories",category.getHasSubCategories());				
					jsonList.add(plainJSON);
				}
				else if(noEmptyCategories.equals(false)){
					JSONObject plainJSON=new JSONObject();
					plainJSON.accumulate("categoryicon", category.getCategoryIcon());
					plainJSON.accumulate("isfavouritecategory", category.getIsFavouriteCategory());
					plainJSON.accumulate("categoryid", category.getCategoryId());
					plainJSON.accumulate("categoryname", category.getCategoryName());
					plainJSON.accumulate("totalNoOfInventories", category.getTotalNoOfInventories());
					plainJSON.accumulate("hassubcategories",category.getHasSubCategories());				
					jsonList.add(plainJSON);
				}
			}
			
			if(!jsonList.isEmpty()){
				obj.accumulate("categorieslist", jsonList);
			}
			else{
				throw new CategoryNotFoundException();
			}
		}
		catch(CategoryNotFoundException e){
			log.error(e.toString());
			String message="CATEGORIES NOT FOUND";
			obj.accumulate("status", message);
		}
		catch(Exception e){e.printStackTrace();
			log.error(e.toString());
			String message="SOME ERROR OCCURRED WHILE RETRIEVING CATEGORIES";
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
	}
	
	
	
	
	
	/**
	 * Returns list of categories marked as home page categories along with corresponding inventory count and total inventory count
	 * @param request
	 * @param response
	 * @returna JSON String containing  entire list of category DTO objects.
	 * @throws Exception
	 * @author Thangaraj(KNSTEK)
	 * @since 24-09-2014
	 */
	@RequestMapping(value="/viewhomepagecategories.htm")
	@ResponseBody
	public String viewHomePageCategories(HttpServletRequest request,HttpServletResponse response) throws Exception{
		/*Enumeration headerNames = request. getHeaderNames();
		while (headerNames.hasMoreElements()) {
		String paramName = (String) headerNames.nextElement();
		String paramValue = request.getHeader(paramName);
		System.out.println(paramName+" :" + paramValue);
		}*/

		log.info("inside viewHomePageCategories() controller");
		JSONObject json							= JsonParser.getJson(request, response);
		log.info(" request : " + json.toString());
		System.out.println(" request : " + json.toString());
		Integer userId							= json.getInt("userid");
		Boolean favVendor						= json.getBoolean("favvendor");
		Integer maxDistance						= json.getInt("maxdistance");
		UserDto user 							= this.userService.getUserbyUserId(userId);
		ArrayList<CategoriesDto> categoriesDto	= new ArrayList<CategoriesDto>();
		
		categoriesDto.addAll(this.categoryService.getAllHomePageCategories(userId,favVendor,false,maxDistance));		
		ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();		
		Collections.sort(categoriesDto,CategoriesDto.SequenceComparator);
		//populate each categories data into arraylist as a json object
		for(CategoriesDto category: categoriesDto){
			Boolean hasSubcategories=true;
			JSONObject planJSON=new JSONObject();
			planJSON.accumulate("categoryicon", category.getCategoryIcon()+"");
			planJSON.accumulate("categoryid", category.getCategoryId());
			planJSON.accumulate("categoryname", category.getCategoryName());
			planJSON.accumulate("sequenceno",category.getSequenceNo());
			planJSON.accumulate("totalNoOfInventories", category.getTotalNoOfInventories());
			planJSON.accumulate("isfavouritecategory", category.getIsFavouriteCategory());
			if(null==category.getSubCategories() || category.getSubCategories().size()<1){
				hasSubcategories=false;
			}
			planJSON.accumulate("hassubcategories", hasSubcategories);
			jsonList.add(planJSON);
		}
		
		//prepare output json object to hold the list of categories and total inventory count
		JSONObject output=new JSONObject();			
		output.accumulate("categorieslist", jsonList);
		output.accumulate("totalInventoryCount", this.inventoryService.getTotalInventoryCount(user.getUserId(),user.getRatingRestriction(),favVendor,null));
		String result = output.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
	}
	
	/**
	 * Returns list of first level parent categories along with corresponding inventory count
	 * 
	 * @param request
	 * @param response
	 * @return a JSON String containing  entire list of category DTO objects.
	 * @throws JSONException
	 * @author Thangaraj(KNSTEK)
	 * @since 24-09-2014
	 */
	@RequestMapping(value="/getparentcategories.htm")
	@ResponseBody
	public String viewParentCategories(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
		try{

			log.info("inside viewParentCategories() controller");
			JSONObject json				= JsonParser.getJson(request, response);
			log.info(" request : " + json.toString());
			System.out.println(" request : " + json.toString());
			
			Integer userId				= json.getInt("userid");
			Boolean favVendor			= json.getBoolean("favvendor");
			Boolean favCategory			= json.getBoolean("favcategory");
			Boolean noEmptyCategories	= json.getBoolean("noemptycategories");
			Integer maxDistance			= json.getInt("maxdistance");
			//SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss SSS");
			//System.out.println("b4 getAllParentCategories "+format.format(new Date()));
			ArrayList<CategoriesDto> categories = (ArrayList<CategoriesDto>)this.categoryService.getAllParentCategories(userId,favCategory);
			//System.out.println("after getAllParentCategories "+format.format(new Date()));
			categories = this.categoryService.populateFavHasSubAndInventoryCount(categories, userId, favVendor, favCategory,maxDistance);
			//System.out.println("after populateFavHasSubAndInventoryCount "+format.format(new Date()));
			for(CategoriesDto category : categories){
				if (noEmptyCategories.equals(true)  && category.getTotalNoOfInventories()>0){
					Boolean hasSubcategories=true;
					JSONObject planJSON=new JSONObject();
					planJSON.accumulate("categoryicon", category.getCategoryIcon());
					planJSON.accumulate("categoryid", category.getCategoryId());
					planJSON.accumulate("categoryname", category.getCategoryName());
					planJSON.accumulate("totalNoOfInventories", category.getTotalNoOfInventories());
					planJSON.accumulate("isfavouritecategory", category.getIsFavouriteCategory());
					planJSON.accumulate("hassubcategories", hasSubcategories);
					jsonList.add(planJSON);	
				}
				else if(noEmptyCategories.equals(false)){
					Boolean hasSubcategories=true;
					JSONObject planJSON=new JSONObject();
					planJSON.accumulate("categoryicon", category.getCategoryIcon());
					planJSON.accumulate("categoryid", category.getCategoryId());
					planJSON.accumulate("categoryname", category.getCategoryName());
					planJSON.accumulate("totalNoOfInventories", category.getTotalNoOfInventories());
					planJSON.accumulate("isfavouritecategory", category.getIsFavouriteCategory());
					planJSON.accumulate("hassubcategories", hasSubcategories);
					jsonList.add(planJSON);	
				}
			}
		}
		catch(CategoryNotFoundException e){
			log.error("No parent category found or CategoryNotFoundException on fetching parent category details");
		}
		catch(Exception e){
			JSONObject output=new JSONObject();	
			log.error("ERROR WHILE FETCHING PARENT CATEGORIES" + e.toString());
			e.printStackTrace();
			String message="ERROR WHILE FETCHING PARENT CATEGORIES";
			output.accumulate("status", message);
			return output.toString();
		}
		//prepare output json object to hold the list of categories and total inventory count
		JSONObject output=new JSONObject();			
		output.accumulate("categorieslist", jsonList);
		String result = output.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
	}	
}
