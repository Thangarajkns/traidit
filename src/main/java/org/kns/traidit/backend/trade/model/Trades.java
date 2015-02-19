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
 * model for traidit trade
 * contains information about trade that's happened
 * like user,item,vendor etc
 */
@Entity
@Table(name="kns_traidit_trade")
public class Trades implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="trade_id")
	private Integer tradeId;
	
	@ManyToOne
	@JoinColumn(name="trader_id")
	private TraidItUser trader;
	
	@ManyToOne
	@JoinColumn(name="vendor_id")
	private TraidItUser vendor;
	
	@Column(name="status")
	private String status;
	
	@Column(name="is_trader_read")
	private Boolean isTraderRead = true;
	
	@Column(name="is_vendor_read")
	private Boolean isVendorRead = true;

	public Integer getTradeId() {
		return tradeId;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	public TraidItUser getTrader() {
		return trader;
	}

	public void setTrader(TraidItUser trader) {
		this.trader = trader;
	}

	public TraidItUser getVendor() {
		return vendor;
	}

	public void setVendor(TraidItUser vendor) {
		this.vendor = vendor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIsTraderRead() {
		return isTraderRead;
	}

	public void setIsTraderRead(Boolean isTraderRead) {
		this.isTraderRead = isTraderRead;
	}

	public Boolean getIsVendorRead() {
		return isVendorRead;
	}

	public void setIsVendorRead(Boolean isVendorRead) {
		this.isVendorRead = isVendorRead;
	}
	
	


}
