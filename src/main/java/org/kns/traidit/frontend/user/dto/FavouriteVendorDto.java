package org.kns.traidit.frontend.user.dto;

import java.util.ArrayList;

import org.kns.traidit.backend.user.favourites.model.FavouriteVendor;

/**
 * Created by     : Soujanya

 * Created Date	  : June 23,2014
 * file Name	  : FavouriteVendorDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */

public class FavouriteVendorDto {
	
	private Integer id;
	private UserDto vendorId;
	private UserDto userId;
	private Integer totalFavVendors;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public UserDto getVendorId() {
		return vendorId;
	}
	public void setVendorId(UserDto vendorId) {
		this.vendorId = vendorId;
	}
	public UserDto getUserId() {
		return userId;
	}
	public void setUserId(UserDto userId) {
		this.userId = userId;
	}
	
	
	public Integer getTotalFavVendors() {
		return totalFavVendors;
	}
	public void setTotalFavVendors(Integer totalFavVendors) {
		this.totalFavVendors = totalFavVendors;
	}
	/* For Populating Dtos from Model                        */	
	public static FavouriteVendorDto populateFavouriteVendorDto(FavouriteVendor favVendors){
		FavouriteVendorDto favouriteVendorDto=new FavouriteVendorDto();
		favouriteVendorDto.setId(favVendors.getId());
		favouriteVendorDto.setVendorId(UserDto.populateUserDto(favVendors.getVendorId()));
		favouriteVendorDto.setUserId(UserDto.populateUserDto(favVendors.getUserId()));
		return favouriteVendorDto;
	}
	
	public static ArrayList<FavouriteVendorDto> populateFavouriteVendorDto(ArrayList<FavouriteVendor> favVendors){
		ArrayList<FavouriteVendorDto> favouriteVendorDto=new ArrayList<FavouriteVendorDto>();
		for(FavouriteVendor favVendor:favVendors){
			FavouriteVendorDto favouriteVendorsDto=populateFavouriteVendorDto(favVendor);
			favouriteVendorDto.add(favouriteVendorsDto);
		}
		return favouriteVendorDto;
		
		
	}

}
