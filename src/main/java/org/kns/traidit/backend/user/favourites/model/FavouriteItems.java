package org.kns.traidit.backend.user.favourites.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kns.traidit.backend.item.model.TraidItItems;
import org.kns.traidit.backend.user.model.TraidItUser;

/*
 * created by Soujanya on 18th June,2014
 * model for TraidIt favourite items of users 
 * contains user's favourite items
 */
@Entity
@Table(name="kns_traidit_favourite_items")
public class FavouriteItems implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="item_id")
	private TraidItItems itemId;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private TraidItUser userId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TraidItItems getItemId() {
		return itemId;
	}

	public void setItemId(TraidItItems itemId) {
		this.itemId = itemId;
	}

	public TraidItUser getUserId() {
		return userId;
	}

	public void setUserId(TraidItUser userId) {
		this.userId = userId;
	}

}
