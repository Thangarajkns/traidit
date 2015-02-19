package org.kns.traidit.backend.item.model;

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
 * created by Soujanya on 18th June,2014
 * Model for User comments on items
 * contains user comments on items
 */
@Entity
@Table(name="kns_traidit_item_comments")
public class UserComments implements Serializable {
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="comment_id")
	private Integer commentId;
	
	@ManyToOne
	@JoinColumn(name="commented_user")
	private TraidItUser commentedUser;
	
	@Column(name="comment")
	private String comment;
	
	@Column(name="rating")
	private Integer rating;
	
	@Column(name="commented_date")
	private Date commentedDate;
	
	@ManyToOne
	@JoinColumn(name="item_id")
	private TraidItItems itemId;
	
	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public TraidItUser getCommentedUser() {
		return commentedUser;
	}

	public void setCommentedUser(TraidItUser commentedUser) {
		this.commentedUser = commentedUser;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Date getCommentedDate() {
		return commentedDate;
	}

	public void setCommentedDate(Date commentedDate) {
		this.commentedDate = commentedDate;
	}

	public TraidItItems getItemId() {
		return itemId;
	}

	public void setItemId(TraidItItems itemId) {
		this.itemId = itemId;
	}

}
