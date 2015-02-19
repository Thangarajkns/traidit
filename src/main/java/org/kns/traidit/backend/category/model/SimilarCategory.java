package org.kns.traidit.backend.category.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;


/**
 * Created by Jeevan on November 05, 2014
 * Model for Similar Categories
 * 
 * @author KNS-ACCONTS
 *
 */

@Entity
@Table(name="kns_traidit_similar_category",uniqueConstraints={@UniqueConstraint(columnNames={"category","similar_category"})})
public class SimilarCategory implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	@Index(name="cat")
	@Column(name="category")
	private Integer category;
	
	@Index(name="sim_cat")
	@Column(name="similar_category")
	private Integer similarCategory;
		
	@Column(name="is_bidirectional_mapping")
	private Boolean isBidirectional;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public Integer getSimilarCategory() {
		return similarCategory;
	}

	public void setSimilarCategory(Integer similarCategory) {
		this.similarCategory = similarCategory;
	}

	public Boolean getIsBidirectional() {
		return isBidirectional;
	}

	public void setIsBidirectional(Boolean isBidirectional) {
		this.isBidirectional = isBidirectional;
	}
		
}
