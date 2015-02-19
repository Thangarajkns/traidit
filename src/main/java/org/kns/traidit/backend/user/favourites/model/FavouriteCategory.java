package org.kns.traidit.backend.user.favourites.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kns.traidit.backend.category.model.TraidItItemCategories;

import org.kns.traidit.backend.user.model.TraidItUser;



@Entity
@Table(name="kns_traidit_favourite_category")
public class FavouriteCategory implements Serializable{

	
	
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private  TraidItItemCategories category;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private TraidItUser userId;

	@ManyToOne
	@JoinColumn(name="parent_category_id")
	private  TraidItItemCategories parentCategory;
	
	@ManyToOne
	@JoinColumn(name="grand_parent_category_id")
	private  TraidItItemCategories grandParentCategory;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TraidItItemCategories getCategory() {
		return category;
	}

	public void setCategory(TraidItItemCategories category) {
		this.category = category;
	}

	public TraidItUser getUserId() {
		return userId;
	}

	public void setUserId(TraidItUser userId) {
		this.userId = userId;
	}

	
	public TraidItItemCategories getParentCategory() {
		return parentCategory;
	}

	
	public void setParentCategory(TraidItItemCategories parentCategory) {
		this.parentCategory = parentCategory;
	}

	
	public TraidItItemCategories getGrandParentCategory() {
		return grandParentCategory;
	}

	
	public void setGrandParentCategory(TraidItItemCategories grandParentCategory) {
		this.grandParentCategory = grandParentCategory;
	}
	
	
	
	
	
}
