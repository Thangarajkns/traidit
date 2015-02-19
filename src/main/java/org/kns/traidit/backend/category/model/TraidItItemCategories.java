package org.kns.traidit.backend.category.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
 /*
  * Created by Soujanya on 18th June,2014
  * Model for TraidIt item Categories
  */


import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


@Entity
@Table(name="kns_traidit_item_categories")
public class TraidItItemCategories implements Serializable {
	

	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue
	@Column(name="category_id")
	private Integer categoryId;
	
	//@ContainedIn
	@Column(name="category_name")
	private String categoryName;
	
	@Column(name="category_icon")
	private String categoryIcon;
	
	@Column(name="is_home_page_category")
	private Boolean isHomePageCategory;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="kns_traidit_item_subcategories",
		joinColumns={@JoinColumn(name="child_category_id")},
		inverseJoinColumns={@JoinColumn(name="parent_category_id")})
	private Set<TraidItItemCategories> parentCategories =new HashSet<TraidItItemCategories>();
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(mappedBy="parentCategories",cascade={CascadeType.PERSIST},fetch = FetchType.LAZY)
	private Set<TraidItItemCategories> subCategories=new HashSet<TraidItItemCategories>();
	
	/*@Fetch(FetchMode.SELECT)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="kns_traidit_similar_category_mapping",
			joinColumns={@JoinColumn(name="similar_category_id")},
			inverseJoinColumns={@JoinColumn(name="category_id")})
	private Set<TraidItItemCategories> similarCategoryTo = new HashSet<TraidItItemCategories>();
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(mappedBy="similarCategoryTo",cascade={CascadeType.PERSIST})
	private Set<TraidItItemCategories> similarCategoryOf = new HashSet<TraidItItemCategories>();*/
	
	
	
	
	
	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryIcon() {
		return categoryIcon;
	}

	public void setCategoryIcon(String categoryIcon) {
		this.categoryIcon = categoryIcon;
	}
	
	public Boolean getIsHomePageCategory() {
		return isHomePageCategory;
	}

	public void setIsHomePageCategory(Boolean isHomePageCategory) {
		this.isHomePageCategory = isHomePageCategory;
	}
	
	public Set<TraidItItemCategories> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(Set<TraidItItemCategories> subCategories) {
		this.subCategories = subCategories;
	}

	public Set<TraidItItemCategories> getParentCategories() {
		return parentCategories;
	}

	public void setParentCategories(Set<TraidItItemCategories> parentCategories) {
		this.parentCategories = parentCategories;
	}

/*	
	public Set<TraidItItemCategories> getSimilarCategoryTo() {
		return similarCategoryTo;
	}

	
	public void setSimilarCategoryTo(Set<TraidItItemCategories> similarCategoryTo) {
		this.similarCategoryTo = similarCategoryTo;
	}

	
	public Set<TraidItItemCategories> getSimilarCategoryOf() {
		return similarCategoryOf;
	}

	
	public void setSimilarCategoryOf(Set<TraidItItemCategories> similarCategoryOf) {
		this.similarCategoryOf = similarCategoryOf;
	}

	*/
	
	
	

}
