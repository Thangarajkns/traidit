package org.kns.traidit.backend.user.favourites.model;


/*
 * created by Soujanya on 24th dec,2014
 * model for traidit favourite trades of users
 * contains favourite trades of users
 */

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kns.traidit.backend.trade.model.Trades;
import org.kns.traidit.backend.user.model.TraidItUser;

@Entity
@Table(name="kns_traidit_favourite_trades")
public class FavouriteTrades implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private TraidItUser userId;
	
	@ManyToOne
	@JoinColumn(name="trade_id")
	private Trades tradeId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TraidItUser getUserId() {
		return userId;
	}

	public void setUserId(TraidItUser userId) {
		this.userId = userId;
	}

	public Trades getTradeId() {
		return tradeId;
	}

	public void setTradeId(Trades tradeId) {
		this.tradeId = tradeId;
	}
	
	
	

}
