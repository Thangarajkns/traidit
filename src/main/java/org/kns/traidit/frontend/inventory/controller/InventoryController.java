package org.kns.traidit.frontend.inventory.controller;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kns.traidit.backend.inventory.exception.InventoryNotFoundException;
import org.kns.traidit.backend.inventory.exception.InventoryNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.inventory.dto.InventoryDto;
import org.kns.traidit.frontend.inventory.dto.InventoryFilterFormBeanDto;
import org.kns.traidit.frontend.inventory.service.InventoryService;
import org.kns.traidit.frontend.inventory.service.InventoryServiceImpl;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import twitter4j.org.json.JSONObject;

@Controller("inventoryController")
@RequestMapping(value = "/web/inventory")
public class InventoryController {

	@Resource(name = "inventoryService")
	private InventoryService inventoryService;

	@RequestMapping(value = "/list.htm", method = RequestMethod.GET)
	public String listInventories(
			Map<String, Object> map,
			@ModelAttribute("inventoryFilter") InventoryFilterFormBeanDto inventoryFilter) {
		try {
			//get the list of all available vendors for filter form
			ArrayList<UserDto> vendors = this.inventoryService.getAllVendors();
			
			//filter the list of inventories
			ArrayList<InventoryDto> inventories;
				inventories = this.inventoryService
						.getAllInventories(
								null,
								null,
								inventoryFilter.getPaginator().getNoOfItemsPerPage(), 
								inventoryFilter.getPaginator().getStarttIndex(),
								inventoryFilter.getSortBy(),
								inventoryFilter.getSortOrder()
							);
				
			//set the total on of inventories for given filter
			inventoryFilter.getPaginator().setTotalNoOfItems(InventoryServiceImpl.totalNoOfInventories);
			
			// add all required data to build the view to the map
			map.put("inventories", inventories);
			map.put("listOfVendors", vendors);
			
		} catch (UserNotFoundException e) {
			System.out
					.println("UserNotFoundException(vendor) in InventoryController -> listInventories()");
		} catch (Exception e) {
			System.out
					.println("Exception in InventoryController -> listInventories()");
		}
		return "listInventories";
	}

	@RequestMapping(value = "/list.htm", method = RequestMethod.POST)
	public String listFilteredInventories(
			Map<String, Object> map,
			@ModelAttribute("inventoryFilter") InventoryFilterFormBeanDto inventoryFilter) {
		System.out.println("inside listFilteredInventories");
		try {
			
			//get the list of all available vendors for filter form
			ArrayList<UserDto> vendors = this.inventoryService.getAllVendors();

			// add vendors data to build the view to the map
			map.put("listOfVendors", vendors);
			
			//if the option is filter set the Paginator and sorting data to default 
			if(inventoryFilter.getOptionChoosed() != null && inventoryFilter.getOptionChoosed().equals("filter")){
				inventoryFilter.getPaginator().setCurrentPageNo(0);
				inventoryFilter.setSortBy("");
				inventoryFilter.setSortOrder("ASC");
			}
			
			String searchText = inventoryFilter.getItemSearcText().trim();
			//filter the list of inventories
			ArrayList<InventoryDto> inventories;
				inventories = this.inventoryService
						.getAllInventories(
								inventoryFilter.getVendorId(),
								searchText,
								inventoryFilter.getPaginator().getNoOfItemsPerPage(), 
								inventoryFilter.getPaginator().getStarttIndex(),
								inventoryFilter.getSortBy(),
								inventoryFilter.getSortOrder()
							);
				
			//set the total on of inventories for given filter
			inventoryFilter.getPaginator().setTotalNoOfItems(InventoryServiceImpl.totalNoOfInventories);

			// add all required data to build the view to the map
			map.put("inventories", inventories);
			
			
		} catch (InventoryNotFoundException e) {
			inventoryFilter.getPaginator().setTotalNoOfItems(0);
			System.out
					.println("InventoryNotFoundException in InventoryController -> listInventories()");
		} catch (UserNotFoundException e) {
			System.out
					.println("UserNotFoundException(vendor) in InventoryController -> listInventories()");
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("Exception in InventoryController -> listInventories()");
		}
		return "listInventories";
	}

	@SuppressWarnings("finally")
	@RequestMapping(value = "/setInventoryEnabledStatus.htm", method = RequestMethod.POST)
	public @ResponseBody
	String setInventoryEnabledStatus(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("inside setInventoryEnabledStatus()");
		JSONObject json = new JSONObject();
		try {
			Integer inventoryId = Integer.parseInt(request
					.getParameter("inventoryId"));
			Boolean enabledStatus = Boolean.parseBoolean(request
					.getParameter("enabledStatus"));
			this.inventoryService.changeInventoryEnabledStatus(inventoryId,
					enabledStatus);
			json.accumulate("result", 1);
		} catch (InventoryNotFoundException e) {
			System.out
					.println("InventoryNotFoundException in InventoryController -> setAvailableForSaleStatus()");
			json.accumulate("result", 0);
		} catch (InventoryNotSavedOrUpdatedException e) {
			System.out
					.println("InventoryNotSavedOrUpdatedException in InventoryController -> setAvailableForSaleStatus()");
			json.accumulate("result", 0);
		} catch (Exception e) {
			System.out
					.println("Exception in InventoryController -> setAvailableForSaleStatus()");
			json.accumulate("result", 0);
		} finally {
			return json.toString();
		}
	}



}
