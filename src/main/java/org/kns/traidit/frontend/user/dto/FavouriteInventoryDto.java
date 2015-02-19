package org.kns.traidit.frontend.user.dto;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.kns.traidit.backend.inventory.model.Inventory;
import org.kns.traidit.backend.user.favourites.model.FavouriteInventory;
import org.kns.traidit.backend.user.favourites.model.FavouriteVendor;
import org.kns.traidit.frontend.inventory.dto.InventoryDto;

/**
 * 
 * Created by Jeevan on September 22, 2014
 * Dto for Inventories
 *
 */
public class FavouriteInventoryDto {

	
	private Integer id;
	private  UserDto traiditUser;
	private InventoryDto inventory;
	private ArrayList<String> photoList;
	private BigDecimal distance;
	public ArrayList<String> getPhotoList() {
		return photoList;
	}
	public void setPhotoList(ArrayList<String> photoList) {
		this.photoList = photoList;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public UserDto getTraiditUser() {
		return traiditUser;
	}
	public void setTraiditUser(UserDto traiditUser) {
		this.traiditUser = traiditUser;
	}
	public InventoryDto getInventory() {
		return inventory;
	}
	public void setInventory(InventoryDto inventory) {
		this.inventory = inventory;
	}
	public BigDecimal getDistance() {
		return distance;
	}
	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}
	
	/**
	* Created by Jeevan on 22-Sep-2014 5:57:09 pm	
	*  Method for: Populate Favourite Inventory Dto
	* @param favouriteInventory
	* @return
	 */
	public static FavouriteInventoryDto populateInventory(FavouriteInventory favouriteInventory){
		FavouriteInventoryDto favouriteInventoryDto=new FavouriteInventoryDto();
		favouriteInventoryDto.setId(favouriteInventory.getId());
		favouriteInventoryDto.setInventory(InventoryDto.populateInventoryDto(favouriteInventory.getInventory()));
		favouriteInventoryDto.setTraiditUser(UserDto.populateUserDto(favouriteInventory.getTraiditUser()));
		return favouriteInventoryDto;
	}
	
	public static ArrayList<FavouriteInventoryDto> populateFavouriteInventoryDto(ArrayList<FavouriteInventory> favInventories){
		ArrayList<FavouriteInventoryDto> favouriteInventoryDto=new ArrayList<FavouriteInventoryDto>();
		for(FavouriteInventory favInventory:favInventories){
			FavouriteInventoryDto favouriteInventoriesDto=populateInventory(favInventory);
			favouriteInventoryDto.add(favouriteInventoriesDto);
		}
		return favouriteInventoryDto;
		
		
	}
	
}
