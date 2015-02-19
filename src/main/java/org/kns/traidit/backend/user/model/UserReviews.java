/*
 * created by soujanya on december 10,2014
 * model for traidit user reviews about app
 * contains user reviews about app
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
@Table(name="kns_traidit_user_reviews")
public class UserReviews implements Serializable {

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
	
	
	@Column(name="review")
	private String review;


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


	public String getReview() {
		return review;
	}


	public void setReview(String review) {
		this.review = review;
	}

	
	
}
