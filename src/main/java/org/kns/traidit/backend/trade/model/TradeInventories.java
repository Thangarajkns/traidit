package org.kns.traidit.backend.trade.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kns.traidit.backend.inventory.model.Inventory;

/*
 * created by Soujanya on october 13,2014
 * model for traidit trade exchanges
 * contains information about trade that's happened
 * 
 */
@Entity
@Table(name="kns_traidit_trade_inventories")
public class TradeInventories implements Serializable{
	
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="trade_id")
	private Trades trade;
	
	@ManyToOne
	@JoinColumn(name="inventory_id")
	private Inventory inventory;
	
	@Column(name="offer_type")
	private String offerType;

	@Column(name="offered_by")
	private String offeredBy;
	
	@Column(name="inventory_of")
	private String inventoryOf;
	
	@Column(name="is_current")
	private Boolean isCurrent;
	
	@Column(name="level")
	private Integer level;
	
	@Column(name="date")
	private Date tradeInventoryDate;
	
	@Column(name="quantity")
	private Integer quantity;

	
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Trades getTrade() {
		return trade;
	}

	public void setTrade(Trades trade) {
		this.trade = trade;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public String getOfferType() {
		return offerType;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}

	public String getOfferedBy() {
		return offeredBy;
	}

	public void setOfferedBy(String offeredBy) {
		this.offeredBy = offeredBy;
	}

	public String getInventoryOf() {
		return inventoryOf;
	}

	public void setInventoryOf(String inventoryOf) {
		this.inventoryOf = inventoryOf;
	}

	public Boolean getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(Boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Date getTradeInventoryDate() {
		return tradeInventoryDate;
	}

	public void setTradeInventoryDate(Date tradeInventoryDate) {
		this.tradeInventoryDate = tradeInventoryDate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}
