package org.kns.traidit.backend.user.favourites.model;

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
 * created by Soujanya on 18th June,2014
 * model for traidit favourite vendors of users
 * contains favourite vendors of users
 */
@Entity
@Table(name="kns_traidit_favourite_vendors")
public class FavouriteVendor implements Serializable{
	
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

}
