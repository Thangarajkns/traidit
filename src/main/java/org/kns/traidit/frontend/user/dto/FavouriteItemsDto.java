package org.kns.traidit.frontend.user.dto;

import java.util.ArrayList;

import org.kns.traidit.backend.user.favourites.model.FavouriteItems;
import org.kns.traidit.frontend.item.dto.ItemsDto;

/**
 * Created by     : Soujanya

 * Created Date	  : June 23,2014
 * file Name	  : FavouriteItemsDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */

public class FavouriteItemsDto {
	
	private Integer id;
	private ItemsDto itemId;
	private UserDto userId;
	private Integer totalFavItems;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public ItemsDto getItemId() {
		return itemId;
	}
	public void setItemId(ItemsDto itemId) {
		this.itemId = itemId;
	}
	public UserDto getUserId() {
		return userId;
	}
	public void setUserId(UserDto userId) {
		this.userId = userId;
	}
	
	
	public Integer getTotalFavItems() {
		return totalFavItems;
	}
	public void setTotalFavItems(Integer totalFavItems) {
		this.totalFavItems = totalFavItems;
	}
	/* For Populating Dtos from Model                        */	
	public static FavouriteItemsDto populateFavouriteItemsDto(FavouriteItems favItems){
		FavouriteItemsDto favouriteItemsDto=new FavouriteItemsDto();
		favouriteItemsDto.setId(favItems.getId());
		favouriteItemsDto.setItemId(ItemsDto.populateItemsDto(favItems.getItemId()));
		favouriteItemsDto.setUserId(UserDto.populateUserDto(favItems.getUserId()));
		return favouriteItemsDto;
	}
	
	public static ArrayList<FavouriteItemsDto> populateFavouriteItemsDto(ArrayList<FavouriteItems> favItems){
		ArrayList<FavouriteItemsDto> favouriteItemsDto=new ArrayList<FavouriteItemsDto>();
		for(FavouriteItems favItem:favItems){
			FavouriteItemsDto favouriteItemDto=populateFavouriteItemsDto(favItem);
			favouriteItemsDto.add(favouriteItemDto);
		}
		return favouriteItemsDto;
		
		
	}

}
