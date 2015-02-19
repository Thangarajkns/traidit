package org.kns.traidit.backend.payments.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kns.traidit.backend.user.model.Plans;
import org.kns.traidit.backend.user.model.TraidItUser;

/*
 * Creasted by Jeevan on July 15, 2014
 * Model to hold User Subscription Payments
 */
@Entity
@Table(name = "kns_traidit_user_subscription_payments")
public class UserSubscriptionPayments implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "subscription_id")
	private Integer subscriptionId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private TraidItUser user;

	@ManyToOne
	@JoinColumn(name = "plan_id")
	private Plans plan;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "paid_date")
	private Date paidDate;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "currency")
	private String currency;

	@Column(name = "status")
	private String status;

	@Column(name = "source", nullable = false)
	private String Source;

	public Integer getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Integer subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public TraidItUser getUser() {
		return user;
	}

	public void setUser(TraidItUser user) {
		this.user = user;
	}

	public Plans getPlan() {
		return plan;
	}

	public void setPlan(Plans plan) {
		this.plan = plan;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSource() {
		return Source;
	}

	public void setSource(String source) {
		Source = source;
	}

}
