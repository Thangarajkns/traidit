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
 * created by Soujanya on 18th June,2014
 * model for saved trades of tradeit
 * contains saved trade information
 */
@Entity
@Table(name="kns_traidit_saved_trades")
public class SavedTrades implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="trade_id")
	private Trades tradeId;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private TraidItUser userId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Trades getTradeId() {
		return tradeId;
	}

	public void setTradeId(Trades tradeId) {
		this.tradeId = tradeId;
	}

	public TraidItUser getUserId() {
		return userId;
	}

	public void setUserId(TraidItUser userId) {
		this.userId = userId;
	}

}
