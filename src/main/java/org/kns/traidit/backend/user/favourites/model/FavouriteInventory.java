package org.kns.traidit.backend.user.favourites.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kns.traidit.backend.inventory.model.Inventory;
import org.kns.traidit.backend.user.model.TraidItUser;

/**
 * 
 * Created by Jeevan on September 22, 2014
 * Model for Favourite Inventories as Their wont be Favourite Items as they are not visible to users
 *
 */

@Entity
@Table(name="kns_traidit_favourite_inventories")
public class FavouriteInventory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@GeneratedValue
	@Id
	@Column(name="id")
	private Integer id;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.PERSIST)	
	@JoinColumn(name="user_id")
	private TraidItUser traiditUser;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.PERSIST)
	@JoinColumn(name="inventory_id")
	private Inventory inventory;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TraidItUser getTraiditUser() {
		return traiditUser;
	}

	public void setTraiditUser(TraidItUser traiditUser) {
		this.traiditUser = traiditUser;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	
}
