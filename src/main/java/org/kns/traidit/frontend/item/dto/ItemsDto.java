/**
 * Created by     : Soujanya

 * Created Date	  : June 20,2014
 * file Name	  : ItemsDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */




package org.kns.traidit.frontend.item.dto;

import java.util.ArrayList;
import org.kns.traidit.backend.item.model.TraidItItems;
import org.kns.traidit.frontend.category.dto.CategoriesDto;

public class ItemsDto {
	private Integer itemId;
	private String itemName;
	private String description;
	private String photos;
	private String videos;
	private Integer ratings;
	private String manufacturer;
	private CategoriesDto categoryId;
	private Integer totalItems;
	private ArrayList<String> photoList;
	private Integer localDBItem = 0;
	private CategoriesDto grandParent;
	private String details;
	private CategoriesDto parentCategory;
	//for Ebay API
	private String categoryNo;
	
	//Added by Jeevan on September 24, 2014.. Since this Terminology makes much Sense
	private CategoriesDto subCategory;
	private CategoriesDto subSubCategory;
	
	private String upc;
	
	
	//Added by Jeevan on November 10, 2014.. as per Similar Category Requirements
	private CategoriesDto category2;
	private CategoriesDto category3;
	
	
	
	
	
	
	public String getCategoryNo() {
		return categoryNo;
	}
	public void setCategoryNo(String categoryNo) {
		this.categoryNo = categoryNo;
	}
	public CategoriesDto getParentCategory() {
		return parentCategory;
	}
	public void setParentCategory(CategoriesDto parentCategory) {
		this.parentCategory = parentCategory;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPhotos() {
		return photos;
	}
	public void setPhotos(String photos) {
		this.photos = photos;
	}
	public String getVideos() {
		return videos;
	}
	public void setVideos(String videos) {
		this.videos = videos;
	}
	public Integer getRatings() {
		return ratings;
	}
	public void setRatings(Integer ratings) {
		this.ratings = ratings;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public CategoriesDto getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(CategoriesDto categoryId) {
		this.categoryId = categoryId;
	}
	public ArrayList<String> getPhotoList() {
		return photoList;
	}
	public void setPhotoList(ArrayList<String> photoList) {
		this.photoList = photoList;
	}
	public Integer getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}
	public Integer getLocalDBItem() {
		return localDBItem;
	}
	public void setLocalDBItem(Integer localDBItem) {
		this.localDBItem = localDBItem;
	}
	public CategoriesDto getGrandParent() {
		return grandParent;
	}
	public void setGrandParent(CategoriesDto grandParent) {
		this.grandParent = grandParent;	}

	
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}

	public CategoriesDto getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(CategoriesDto subCategory) {
		this.subCategory = subCategory;
	}
	public CategoriesDto getSubSubCategory() {
		return subSubCategory;
	}
	public void setSubSubCategory(CategoriesDto subSubCategory) {
		this.subSubCategory = subSubCategory;
	}
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}	
	
	public CategoriesDto getCategory2() {
		return category2;
	}
	public void setCategory2(CategoriesDto category2) {
		this.category2 = category2;
	}
	public CategoriesDto getCategory3() {
		return category3;
	}
	public void setCategory3(CategoriesDto category3) {
		this.category3 = category3;
	}
	
	
	/* For Populating Dtos from Model                        */	
	public static ItemsDto populateItemsDto(TraidItItems items){
		ItemsDto itemsDto=new ItemsDto();
		itemsDto.setItemId(items.getItemId());
		itemsDto.setItemName(items.getItemName());
		itemsDto.setDescription(items.getDescription());
		itemsDto.setManufacturer(items.getManufacturer());
		itemsDto.setPhotos(items.getPhotos());
		itemsDto.setVideos(items.getVideos());
		itemsDto.setRatings(items.getRatings());
		itemsDto.setDetails(items.getDetails());
		itemsDto.setUpc(items.getUpc());
		itemsDto.setCategoryId(CategoriesDto.populateCategoriesDto(items.getCategoryId()));
		return itemsDto;
		
	}
	
	public static ArrayList<ItemsDto> populateItemsDto(ArrayList<TraidItItems> items){
		ArrayList<ItemsDto> itemsDto=new ArrayList<ItemsDto>();
		for(TraidItItems item:items){
			ItemsDto itemDto=populateItemsDto(item);
			itemsDto.add(itemDto);
		}
		return itemsDto;
		
		
	}
	
	

}
