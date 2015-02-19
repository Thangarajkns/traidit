package org.kns.traidit.backend.inventory.dao;

import java.util.ArrayList;
import java.util.List;

import org.kns.traidit.backend.category.exception.CategoryNotFoundException;
import org.kns.traidit.backend.category.model.TraidItItemCategories;
import org.kns.traidit.backend.inventory.exception.InventoryNotFoundException;
import org.kns.traidit.backend.inventory.exception.InventoryNotSavedOrUpdatedException;
import org.kns.traidit.backend.inventory.model.Inventory;
import org.kns.traidit.backend.item.exception.ItemNotFoundException;
import org.kns.traidit.backend.item.model.TraidItItems;
import org.kns.traidit.backend.user.exception.FavInventoryNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.FavouritesNotFoundException;
import org.kns.traidit.backend.user.exception.FavouritesNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.favourites.model.FavouriteInventory;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.inventory.dto.InventoryDto;

/*
 * created by Soujanya on 19th June,2014
 * DAO interface for supplier related activities
 */

public interface InventoryDao {
	public Inventory getInventoryByInventoryId(Integer inventoryId) throws InventoryNotFoundException;
	public Inventory getInventoryByItemId(TraidItItems itemId) throws InventoryNotFoundException;
	public Inventory getInventoryByVendorId(TraidItUser vendorId) throws InventoryNotFoundException;
	public Integer saveOrUpdateInventory(Inventory inventory) throws InventoryNotSavedOrUpdatedException;
	public void deleteInventory(Inventory inventory) throws InventoryNotFoundException;
	public ArrayList<Inventory> getAllInventoriesInDB(Integer page,Integer pageSize) throws InventoryNotFoundException;
	public ArrayList<Inventory> getAllInventories(TraidItUser vendor,String itemSearcText, Integer limit,Integer startIndex,String sortBy,String sortOrder) throws InventoryNotFoundException;
	public ArrayList<TraidItUser> getAllVendors() throws UserNotFoundException;
	public Integer getInventoryCountByCategoryIds(List<Integer> categoryIds,Integer userId,Integer ratingRsetriction,Boolean favVendor);
	public Integer getInventoryCountOfVendor(Integer vendorId) throws Exception;
	public Integer getTotalInventoryCount(Integer userId,Integer ratingRestriction,Boolean favVendor,Integer maxDistance);
	public ArrayList<Inventory> getInventoriesByCategory(Integer categoryId,Integer userId,Integer ratingRestriction,Boolean favVendor,Integer maxDistance) throws InventoryNotFoundException;
	public Integer saveOrUpdateFavouriteInventory(FavouriteInventory favouriteInventory) throws FavouritesNotSavedOrUpdatedException;
	public FavouriteInventory getFavouriteInventoryById(Integer favouriteInventoryId) throws FavouritesNotFoundException;
	public void deleteFavouriteInventory(FavouriteInventory inventory) throws FavouritesNotFoundException;
	public FavouriteInventory getFavouriteInventoryByInventoryIdAndUserId(Integer inventoryId, Integer userId) throws FavouritesNotFoundException;
	public ArrayList<Inventory> getInventoriesByUser(Integer userId,Integer vendorId,Boolean favCategory,Boolean favInventory) throws InventoryNotFoundException;
	public ArrayList<FavouriteInventory> getFavouriteInventoriesOfUser(Integer userId,Boolean favVendor,Boolean favcategory,Integer maxDistance) throws FavouritesNotFoundException;
	public Integer getCountOfFavInventoriesOfUser(Integer userId) throws FavouritesNotFoundException;
	public ArrayList<Inventory> getEntireInventoriesForUser(Integer userId,Boolean favInventory,Boolean favCategory,Integer maxdistance) throws InventoryNotFoundException;
	public ArrayList<Inventory> getEntireInventoriesOfCategoryForUser(Integer userId,Boolean favInventory,Boolean favCategory,Integer maxDistance,List<Integer> categoryId) throws InventoryNotFoundException;
}
