package org.kns.traidit.backend.user.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="kns_traidit_user_reported_abuse")
public class UserReportedAbuse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private TraidItUser userId;
	
	@ManyToOne
	@JoinColumn(name="vendorId")
	private TraidItUser vendorId;
	
	@Column(name="comment")
	private String comment;

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

	public TraidItUser getVendorId() {
		return vendorId;
	}

	public void setVendorId(TraidItUser vendorId) {
		this.vendorId = vendorId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	

}
