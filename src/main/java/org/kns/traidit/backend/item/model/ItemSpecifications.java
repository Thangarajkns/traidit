package org.kns.traidit.backend.item.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kns.traidit.backend.user.model.TraidItUser;

/*
 * Created by Jeevan on July 15, 2014
 * Model for Item Specifications
 */

@Entity
@Table(name="kns_traidit_item_specifications")
public class ItemSpecifications implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="specification_id")
	private Integer specificationId;
	
	
	@ManyToOne
	@JoinColumn(name="item_id")
	private TraidItItems item;
	
	
	@ManyToOne
	@JoinColumn(name="vendor_id")
	private TraidItUser vendor;
	
	
	@Column(name="item_key")
	private String key;

	
	@Column(name="item_value")
	private String value;

	@Column(name="unit_of_measure")
	private String unitOfMeasure;

	
	
	public Integer getSpecificationId() {
		return specificationId;
	}

	public void setSpecificationId(Integer specificationId) {
		this.specificationId = specificationId;
	}

	public TraidItItems getItem() {
		return item;
	}

	public void setItem(TraidItItems item) {
		this.item = item;
	}

	public TraidItUser getVendor() {
		return vendor;
	}

	public void setVendor(TraidItUser vendor) {
		this.vendor = vendor;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}
	
	
	
	
}
