/**
 *  Created by     : Soujanya
 * Created Date	  : June 26th,2014
 * file Name	  : InventoryService.java
 * Purpose		  : Service

 * 
 * 
 */




package org.kns.traidit.frontend.inventory.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.kns.traidit.backend.category.exception.CategoryNotFoundException;
import org.kns.traidit.backend.inventory.exception.InventoryNotFoundException;
import org.kns.traidit.backend.inventory.exception.InventoryNotSavedOrUpdatedException;
import org.kns.traidit.backend.inventory.model.Inventory;
import org.kns.traidit.backend.item.exception.ItemNotFoundException;
import org.kns.traidit.backend.user.exception.DistanceNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.FavouritesNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.frontend.category.dto.CategoriesDto;
import org.kns.traidit.frontend.inventory.dto.InventoryDto;
import org.kns.traidit.frontend.item.dto.ItemsDto;
import org.kns.traidit.frontend.user.dto.FavouriteInventoryDto;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import twitter4j.org.json.JSONException;

public interface InventoryService {
	
		public InventoryDto getInventoryByInventoryId(Integer inventoryId) throws InventoryNotFoundException;
		public Integer addInventory(Boolean availableForSale,Integer unitsAvailable,Integer itemId,Integer vendorId, String description) throws InventoryNotSavedOrUpdatedException,ItemNotFoundException,UserNotFoundException;
		public void deleteInventory(Integer inventoryId) throws InventoryNotFoundException;
		public InventoryDto editInventory(Integer inventoryId) throws InventoryNotFoundException;
		public Integer saveEditedInventory(InventoryDto inventoryDto,MultipartHttpServletRequest multiRequest) throws InventoryNotSavedOrUpdatedException,InventoryNotFoundException, Exception;
		public InventoryDto getInventoryToDisplay(Integer inventoryId,Integer userId,Integer categoryId) throws Exception;
		public Integer changeInventoryAvailability(Integer inventoryId, Boolean availableForSale) throws InventoryNotFoundException,InventoryNotSavedOrUpdatedException;
		public ArrayList<InventoryDto> getAllInventories(Integer page,Integer pageSize) throws InventoryNotFoundException;
		public ArrayList<InventoryDto> getAllInventories(Integer vendorId,String itemSearcText,Integer limit,Integer startIndex,String sortBy,String sortOrder) throws InventoryNotFoundException,UserNotFoundException;
		public Integer changeInventoryEnabledStatus(Integer inventoryId, Boolean isEnabled) throws InventoryNotFoundException,InventoryNotSavedOrUpdatedException;
		public ArrayList<UserDto> getAllVendors() throws UserNotFoundException;
		//public Integer getTotalInventoriesByCategory(Integer categoryId) throws InventoryNotFoundException, CategoryNotFoundException;
		public Integer getInventoryCount(Integer categoryId,Integer userId,Boolean favVendor,Boolean favCategory,Integer maxDistance) throws Exception;
		//public CategoriesDto populateCategoriesWithInventoryCount(CategoriesDto categoriesDto);
		public Integer saveOrUpdateInventory(InventoryDto inventoryDto) throws ItemNotFoundException, UserNotFoundException, InventoryNotSavedOrUpdatedException;
		public ArrayList<InventoryDto> getInventoriesByCategoryId(Integer categoryId,Integer userId,Boolean favVendor,Integer maxDistance) throws Exception;
		public BigDecimal calculateDistance(Integer userId1,Integer userId2) throws UserNotFoundException, JSONException, DistanceNotSavedOrUpdatedException, IOException;
		public Integer getTotalInventoryCount(Integer userId,Integer ratingRestriction,Boolean favVendor,Integer maxDistance);
		public Integer addInventoryToDB(InventoryDto inventoryDto,MultipartHttpServletRequest multiPartRequest)throws Exception;
		public Integer addFavouriteInventory(Integer vendorId, Integer inventoryId) throws Exception;
		public void deleteFavouriteInventory(Integer inventoryId, Integer userId) throws Exception;
		public ArrayList<InventoryDto> getInventoriesOfUser(Integer userId,Integer vendorId,Boolean favCategory,Boolean favInventory) throws Exception;
		public ArrayList<String> populateInventorywithImages(InventoryDto inventoryDto,Inventory inventory);
		public ArrayList<FavouriteInventoryDto> getFavouriteInventoriesOfUser(Integer userId,Boolean favVendor,Boolean favCategory,Integer maxDistance) throws FavouritesNotFoundException,Exception;
		public ArrayList<InventoryDto> getEntireInventoriesForUser(Integer userId,Boolean favInventory,Boolean favCategory,Integer maxdistance) throws InventoryNotFoundException;
		public ArrayList<InventoryDto> getEntireInventoriesOfCategoryForUser(Integer userId,Boolean favInventory,Boolean favCategory,Integer maxdistance,Integer categoryId) throws InventoryNotFoundException;
	}
