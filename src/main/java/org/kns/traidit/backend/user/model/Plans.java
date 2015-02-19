package org.kns.traidit.backend.user.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
/*
 * Created by Jeevan on July 15, 2014
 * Model class to store User Plans
 */
import javax.persistence.Table;

@Entity
@Table(name="kns_traidit_user_plans")
public class Plans implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="plan_id")
	private Integer planId;
	
	@Column(name="plan_name")
	private String planName;
	
	@Column(name="plan_period")
	private Integer subscriptionDays;
	
	@Column(name="plan_price")
	private Double price;

	public Integer getPlanId() {
		return planId;
	}

	public void setPlanId(Integer planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public Integer getSubscriptionDays() {
		return subscriptionDays;
	}

	public void setSubscriptionDays(Integer subscriptionDays) {
		this.subscriptionDays = subscriptionDays;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	
	
	

}
