package org.kns.traidit.backend.inventory.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.kns.traidit.backend.item.model.TraidItItems;
import org.kns.traidit.backend.user.model.TraidItUser;

/*
 * Created by Soujanya on 18th June,2014
 * Model for Tradeit inventory
 * contains all the seller's information
 */
@Entity
@Table(name="kns_traidit_inventory")
public class Inventory implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="inventory_id")
	private Integer inventoryId;
	
	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name="item_id")
	private TraidItItems itemId;
	
	@ManyToOne
	@JoinColumn(name="vendor_id")
	private TraidItUser vendorId;
	
	@Column(name="price")
	private Double price;
	
	@Column(name="available_for_trade")
	private Boolean availableForTrade;
	
	@Column(name="listed_date")
	private Date listedDate;
	
	@Column(name="no_of_units_available")
	private Integer unitsAvailable;

	@Column(name="is_enabled")
	private Boolean isEnabled;
	
	@Column(name="description",columnDefinition="TEXT")
	private String description;
	
	@Column(name="photos")
	private String photos;
	
	@Column(name="video")
	private String video;
	
	@Column(name="details",columnDefinition="TEXT")
	private String details;
	
	@Column(name="manufacturer")
	private String manufacturer;
	
	
	
	
	public Inventory() {
		super();
	}

	public Inventory(Integer inventoryId) {
		super();
		this.inventoryId = inventoryId;
	}

	public Integer getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(Integer inventoryId) {
		this.inventoryId = inventoryId;
	}

	public TraidItItems getItemId() {
		return itemId;
	}

	public void setItemId(TraidItItems itemId) {
		this.itemId = itemId;
	}

	public TraidItUser getVendorId() {
		return vendorId;
	}

	public void setVendorId(TraidItUser vendorId) {
		this.vendorId = vendorId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	
	public String getVideo() {
		return video;
	}

	
	public void setVideo(String video) {
		this.video = video;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	
}
