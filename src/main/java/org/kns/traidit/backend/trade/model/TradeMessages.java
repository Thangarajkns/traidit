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
 * created by soujanya on 18th June,2014
 * model for traidit trade messages
 * contains data of messages like sender and receiver
 */
@Entity
@Table(name="kns_traidit_trade_messages")
public class TradeMessages implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="trade_id")
	private Trades trade;
	
	@Column(name="message")
	private String message;
	
	@ManyToOne
	@JoinColumn(name="sender")
	private TraidItUser sender;
	
	@ManyToOne
	@JoinColumn(name="receiver")
	private TraidItUser receiver;
	
	@Column(name="level_id")
	private Integer levelId;
	
	@Column(name="is_read")
	private Boolean isRead;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public Trades getTrade() {
		return trade;
	}

	public void setTrade(Trades trade) {
		this.trade = trade;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public TraidItUser getSender() {
		return sender;
	}

	public void setSender(TraidItUser sender) {
		this.sender = sender;
	}

	public TraidItUser getReceiver() {
		return receiver;
	}

	public void setReceiver(TraidItUser receiver) {
		this.receiver = receiver;
	}

	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	
}
