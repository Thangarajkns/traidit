package org.kns.traidit.backend.payments.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kns.traidit.backend.user.model.TraidItUser;

/*
 * Created by Jeevan on July -15, 2014
 * Model for User COmmissions
 */


@Entity
@Table(name="kns_traidit_user_commissions")
public class UserCommissions implements  Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue
	@Column(name="commission_id")
	private Integer commissionId;
	
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private TraidItUser user;
	
	
	@Column(name="commission_amount")
	private Double commissionAmount;
	
	@Column(name="accumulated_date")
	private Date accumulatedDate;
	
	@ManyToOne
	@JoinColumn(name="referral")
	private TraidItUser referral;

	public Integer getCommissionId() {
		return commissionId;
	}

	public void setCommissionId(Integer commissionId) {
		this.commissionId = commissionId;
	}

	public TraidItUser getUser() {
		return user;
	}

	public void setUser(TraidItUser user) {
		this.user = user;
	}

	public Double getCommissionAmount() {
		return commissionAmount;
	}

	public void setCommissionAmount(Double commissionAmount) {
		this.commissionAmount = commissionAmount;
	}

	public Date getAccumulatedDate() {
		return accumulatedDate;
	}

	public void setAccumulatedDate(Date accumulatedDate) {
		this.accumulatedDate = accumulatedDate;
	}

	public TraidItUser getReferral() {
		return referral;
	}

	public void setReferral(TraidItUser referral) {
		this.referral = referral;
	}


	



}
