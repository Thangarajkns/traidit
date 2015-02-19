/**
 * Created by     : Soujanya

 * Created Date	  : June 23,2014
 * file Name	  : InventoryDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */

package org.kns.traidit.frontend.inventory.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.kns.traidit.backend.inventory.model.Inventory;
import org.kns.traidit.frontend.item.dto.ItemsDto;
import org.kns.traidit.frontend.user.dto.UserDto;

public class InventoryDto {
	private Integer inventoryId;
	private ItemsDto itemId;
	private UserDto vendorId;
	private Boolean availableForTrade;
	private Date listedDate;
	private Integer unitsAvailable;
	private Double price;
	private Integer totalInventory;
	private Boolean isEnabled;
	private String description;
	private String photos;
	private String video;
	private BigDecimal distance;
	
	//added by Jeevan on September 24, 2014 .. support info while adding Inventory
	private Integer flagManual;
	private Integer flagEdited;
	private Integer localDBItem;
	private Integer flagImageEdited;
	private String details;
	private ArrayList<String> photoList;
	private Boolean isFavouriteInventory;
	
	
	
	
	
 	public ArrayList<String> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(ArrayList<String> photoList) {
		this.photoList = photoList;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(Integer inventoryId) {
		this.inventoryId = inventoryId;
	}

	public ItemsDto getItemId() {
		return itemId;
	}

	public void setItemId(ItemsDto itemId) {
		this.itemId = itemId;
	}

	public UserDto getVendorId() {
		return vendorId;
	}

	public void setVendorId(UserDto vendorId) {
		this.vendorId = vendorId;
	}

	

	public Boolean getAvailableForTrade() {
		return availableForTrade;
	}

	public void setAvailableForTrade(Boolean availableForTrade) {
		this.availableForTrade = availableForTrade;
	}

	public Date getListedDate() {
		return listedDate;
	}

	public void setListedDate(Date listedDate) {
		this.listedDate = listedDate;
	}

	public Integer getUnitsAvailable() {
		return unitsAvailable;
	}

	public void setUnitsAvailable(Integer unitsAvailable) {
		this.unitsAvailable = unitsAvailable;
	}
	
	public Integer getTotalInventory() {
		return totalInventory;
	}
	
	public void setTotalInventory(Integer totalInventory) {
		this.totalInventory = totalInventory;
	}
	
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
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

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}
	
	public BigDecimal getDistance() {
		return distance;
	}

	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

	public Integer getFlagManual() {
		return flagManual;
	}

	public void setFlagManual(Integer flagManual) {
		this.flagManual = flagManual;
	}

	public Integer getFlagEdited() {
		return flagEdited;
	}

	public void setFlagEdited(Integer flagEdited) {
		this.flagEdited = flagEdited;
	}

	public Integer getLocalDBItem() {
		return localDBItem;
	}

	public void setLocalDBItem(Integer localDBItem) {
		this.localDBItem = localDBItem;
	}

	public Integer getFlagImageEdited() {
		return flagImageEdited;
	}

	public void setFlagImageEdited(Integer flagImageEdited) {
		this.flagImageEdited = flagImageEdited;
	}
	

	public Boolean getIsFavouriteInventory() {
		return isFavouriteInventory;
	}

	public void setIsFavouriteInventory(Boolean isFavouriteInventory) {
		this.isFavouriteInventory = isFavouriteInventory;
	}

	/* For Populating Dtos from Model */
	public static InventoryDto populateInventoryDto(Inventory inventory) {
		InventoryDto inventoryDto = new InventoryDto();
		inventoryDto.setInventoryId(inventory.getInventoryId());
		inventoryDto.setAvailableForTrade(inventory.getAvailableForTrade());
		inventoryDto.setListedDate(inventory.getListedDate());
		inventoryDto.setPrice(inventory.getPrice());
		inventoryDto.setUnitsAvailable(inventory.getUnitsAvailable());
		inventoryDto.setItemId(ItemsDto.populateItemsDto(inventory.getItemId()));
		inventoryDto.setVendorId(UserDto.populateUserDto(inventory.getVendorId()));
		inventoryDto.setIsEnabled(inventory.getIsEnabled());
		inventoryDto.setPhotos(inventory.getPhotos());
		inventoryDto.setVideo(inventory.getVideo());
		inventoryDto.setDescription(inventory.getDescription());
		return inventoryDto;

	}

	public static ArrayList<InventoryDto> populateInventoryDto(
			ArrayList<Inventory> inventories) {
		ArrayList<InventoryDto> inventoryDto = new ArrayList<InventoryDto>();
		for (Inventory inventory : inventories) {
			InventoryDto inventoriesDto = populateInventoryDto(inventory);
			inventoryDto.add(inventoriesDto);
		}
		return inventoryDto;

	}

}
