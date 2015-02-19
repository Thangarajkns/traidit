package org.kns.traidit.frontend.mobile.controller;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.item.exception.ItemNotFoundException;
import org.kns.traidit.frontend.common.utility.JsonParser;
import org.kns.traidit.frontend.ebay.service.EbayAPIService;
import org.kns.traidit.frontend.item.dto.ItemsDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;


/**
 * 
 *Created by Jeevan on September 23, 2014
 *
 *Controllerfor EBAY API
 *
 */

@Controller("ebayAPIController")
public class EbayAPIController {

	private static Logger log=Logger.getLogger(EbayAPIController.class);
	
	@Resource(name="ebayAPIService")
	private EbayAPIService ebayAPIService;
	
	
	@RequestMapping(value="/getupc.htm")
	@ResponseBody
	public String addItemByUpc(HttpServletRequest request,HttpServletResponse response) throws JSONException {
		log.info("inside addItemByUpc()");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			String upc=json.getString("upc");
			if(upc.substring(0, 1).equals("0"))
				upc = upc.substring(1);
			System.out.println(upc.substring(0, 1));
			 ArrayList<ItemsDto> itemsDto=new ArrayList<ItemsDto>();
			 itemsDto.add(this.ebayAPIService.getItemByUPC(upc));			 //Needs to Change the implementation
				ArrayList<JSONObject> jsonList=new ArrayList<JSONObject>();
				for(ItemsDto item:itemsDto){
					JSONObject itemJSON=new JSONObject();					
					itemJSON.accumulate("itemname", item.getItemName());
					itemJSON.accumulate("manufacturer", item.getManufacturer());					
					itemJSON.accumulate("description", item.getDescription());			
					itemJSON.accumulate("details", item.getDetails());
					itemJSON.accumulate("photo", item.getPhotos());
					itemJSON.accumulate("photolist", item.getPhotoList());						
					itemJSON.accumulate("localDBItem", item.getLocalDBItem());
					itemJSON.accumulate("manufacturer", item.getManufacturer());
					itemJSON.accumulate("categoryname", item.getCategoryId().getCategoryName());
					itemJSON.accumulate("categoryid", item.getCategoryId().getCategoryId());
					if(null!=item.getSubCategory()){
						itemJSON.accumulate("subcategoryname", item.getSubCategory().getCategoryName());
						itemJSON.accumulate("subcategoryid", item.getSubCategory().getCategoryId());
					}
					else{
						itemJSON.accumulate("subcategoryname", "null");
						itemJSON.accumulate("subcategoryid", "null");
					}
					if(null!=item.getSubSubCategory()){
						itemJSON.accumulate("subsubcategoryname", item.getSubSubCategory().getCategoryName());
						itemJSON.accumulate("subsubcategoryid", item.getSubSubCategory().getCategoryId());
					}
					else{
						itemJSON.accumulate("subsubcategoryname", "null");
						itemJSON.accumulate("subsubcategoryid", "null");
					}					
					jsonList.add(itemJSON);
				}
				obj.accumulate("itemslist", jsonList);
		}
		catch (ItemNotFoundException e) {
			String message = "Item not found in ebay and system. Enter manually";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
		}
		catch (Exception e) {
			String message = "Some Error Occurred";
			log.error(message + "" + e.toString());
			obj.accumulate("status", message);
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}
	
	
	
}
