package org.kns.traidit.frontend.category.dto;

import java.util.ArrayList;

import org.kns.traidit.backend.category.model.TraidItItemCategories;
import org.kns.traidit.backend.user.favourites.model.FavouriteCategory;
import org.kns.traidit.backend.user.favourites.model.FavouriteVendor;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.user.dto.FavouriteVendorDto;
import org.kns.traidit.frontend.user.dto.UserDto;

/**
 * Created by     : Soujanya

 * Created Date	  : november 13,2014
 * file Name	  : FavouriteCategoryDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */

public class FavouriteCategoryDto {
	
	private Integer id;
	private  CategoriesDto category;
	private UserDto user;
	
	//added by Soujanya on november 19,2014
	private Integer totalNoOfInventories;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public CategoriesDto getCategory() {
		return category;
	}
	public void setCategory(CategoriesDto category) {
		this.category = category;
	}
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
		this.user = user;
	}
	
	
	
	public Integer getTotalNoOfInventories() {
		return totalNoOfInventories;
	}
	public void setTotalNoOfInventories(Integer totalNoOfInventories) {
		this.totalNoOfInventories = totalNoOfInventories;
	}
	/* For Populating Dtos from Model                        */	
	public static FavouriteCategoryDto populateFavouriteCategoryDto(FavouriteCategory favCategory){
		FavouriteCategoryDto favouriteCategoryDto=new FavouriteCategoryDto();
		favouriteCategoryDto.setId(favCategory.getId());
		favouriteCategoryDto.setCategory(CategoriesDto.populateCategoriesDto(favCategory.getCategory()));
		favouriteCategoryDto.setUser(UserDto.populateUserDto(favCategory.getUserId()));
		return favouriteCategoryDto;
	}
	
	public static ArrayList<FavouriteCategoryDto> populateFavouritecategoryDto(ArrayList<FavouriteCategory> favCategories){
		ArrayList<FavouriteCategoryDto> favouriteCategoryDto=new ArrayList<FavouriteCategoryDto>();
		for(FavouriteCategory favCategory:favCategories){
			FavouriteCategoryDto favouriteCategoriesDto=populateFavouriteCategoryDto(favCategory);
			favouriteCategoryDto.add(favouriteCategoriesDto);
		}
		return favouriteCategoryDto;
		
		
	}

}
