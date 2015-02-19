package org.kns.traidit.backend.item.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.kns.traidit.backend.category.model.TraidItItemCategories;

/*
 * Created by Soujanya on 18th June,2014
 * Model for TraidIt Items
 * contains all the details if Items
 */


/*
 * 
 * Modified by Jeevan on July 18, 2014
 * Added to Index and Configure Lucene
 * 
 */
@Entity
@Table(name="kns_traidit_items")
@Indexed(index="traidit_items")
public class TraidItItems implements Serializable {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@DocumentId
	@Id
	@GeneratedValue
	@Column(name="item_id")
	private Integer itemId;
	
	
	@Index(name="itemName")
	@Field
	@Boost(value=5f)
	@Column(name="item_name")
	private String itemName;
	
	
	@Index(name="description")
	@Field
	@Boost(2f)
	@Column(name="description", columnDefinition="TEXT")
	private String description;
	
	@Column(name="photos")
	private String photos;
	
	@Column(name="videos")
	private String videos;
	
	@Column(name="ratings")
	private Integer ratings;
	
	@Index(name="manufacturer")
	@Field
	@Boost(1f)
	@Column(name="manufacturer")
	private String manufacturer;
		
	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="category_id")
	private TraidItItemCategories categoryId;
	
	@Column(name = "upc", length = 20)
	private String upc;
	
	@Column(name ="details")
	private String details;
	
/* Added By Jeevan on Novemeber 10, 2014
 * As per req of Similar Categories 	
 */

	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="category_id2")
	private TraidItItemCategories category2;
	

	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="category_id_3")
	private TraidItItemCategories category3;
	
/* End of Declaration related Modifications */	
	
	
	
	

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

	public TraidItItemCategories getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(TraidItItemCategories categoryId) {
		this.categoryId = categoryId;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}


	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public TraidItItemCategories getCategory2() {
		return category2;
	}

	public void setCategory2(TraidItItemCategories category2) {
		this.category2 = category2;
	}

	public TraidItItemCategories getCategory3() {
		return category3;
	}

	public void setCategory3(TraidItItemCategories category3) {
		this.category3 = category3;
	}

	
	
	/* Added by Jeevan on November 10, 2014
	 * 
	 */
	
	 
	

}
