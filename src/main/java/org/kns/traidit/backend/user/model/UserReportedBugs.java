/*
 * created by soujanya on december 10,2014
 * model for traidit user reported bugs
 * contains user reported bugs
 */


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
@Table(name="kns_traidit_user_reported_bugs")
public class UserReportedBugs implements Serializable{

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
	
	
	@Column(name="bug")
	private String bug;


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


	public String getBug() {
		return bug;
	}


	public void setBug(String bug) {
		this.bug = bug;
	}
	
	
	

}
