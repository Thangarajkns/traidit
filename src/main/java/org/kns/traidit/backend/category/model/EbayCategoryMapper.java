package org.kns.traidit.backend.category.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * 
 * @author JEEVAN
 * Created by Jeevan on September 24, 2014
 * Model for Ebay Categories
 * Created it because saving Category Ids directly in Category Table does not make much sense
 *
 */
@Entity
@Table(name="kns_traidit_ebay_categories_mapping")
public class EbayCategoryMapper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@GeneratedValue
	@Id
	@Column(name="id")
	private Integer id;
	
	@ManyToOne(cascade={CascadeType.PERSIST})
	@JoinColumn(name="category_id")
	private TraidItItemCategories category;
	
	@Column(name="ebay_id")
	private String ebayId;

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

	public String getEbayId() {
		return ebayId;
	}

	public void setEbayId(String ebayId) {
		this.ebayId = ebayId;
	}
	
	
	
}
