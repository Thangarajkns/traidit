package org.kns.traidit.backend.commission.model;

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
/**
 * Created By Bhagya on Jan 30th,2015
 * Model for the direct deposit information
 */


@Entity
@Table(name="kns_traidit_deposit_information")
public class DirectDepositInformation implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="deposit_id")
	private Integer depositId;
	
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private TraidItUser traiditUser;
	
	@Column(name="bank_name")
	private String bankName;
	
	@Column(name="account_type")
	private String accountType;
	
	@Column(name="routing_number")
	private String routingNumber;
	
	@Column(name="account_number")
	private String accountNumber;
	
	@Column(name="created_date")
	private  Date createdDate;
	
	@Column(name="edited_date")
	private Date editedDate;
	
	

	public Integer getDepositId() {
		return depositId;
	}

	public void setDepositId(Integer depositId) {
		this.depositId = depositId;
	}

	public TraidItUser getTraiditUser() {
		return traiditUser;
	}

	public void setTraiditUser(TraidItUser traiditUser) {
		this.traiditUser = traiditUser;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getRoutingNumber() {
		return routingNumber;
	}

	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getEditedDate() {
		return editedDate;
	}

	public void setEditedDate(Date editedDate) {
		this.editedDate = editedDate;
	}

	
	
	
}