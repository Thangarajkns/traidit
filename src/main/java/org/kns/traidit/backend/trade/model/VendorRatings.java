package org.kns.traidit.backend.trade.model;

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
 * created by Soujanya on 8th october,2014
 * model for traidit vendor ratings
 * contains information about ratings of a particular vendor
 * 
 */
@Entity
@Table(name="kns_traidit_vendor_ratings")
public class VendorRatings implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	
	
	@ManyToOne
	@JoinColumn(name="vendor_id")
	private TraidItUser vendorId;
	
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private TraidItUser userId;
	
	
	@Column(name="rating")
	private Integer rating;
	
	@Column(name="comment")
	private String comment;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TraidItUser getVendorId() {
		return vendorId;
	}

	public void setVendorId(TraidItUser vendorId) {
		this.vendorId = vendorId;
	}

	public TraidItUser getUserId() {
		return userId;
	}

	public void setUserId(TraidItUser userId) {
		this.userId = userId;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
