package org.kns.traidit.backend.user.model;

/*
 * created by soujanya on october 1st,2014
 * model for traidit user messages
 * contains user related messages with sender and receiver
 */

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "kns_traidit_user_messages")
public class UserMessages implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;

	@Column(name = "message")
	private String message;

	@ManyToOne
	@JoinColumn(name = "sender")
	private TraidItUser sender;

	@ManyToOne
	@JoinColumn(name = "receiver")
	private TraidItUser receiver;

	@Column(name = "is_read")
	private Boolean isRead;

	// added by Soujanya on november 24,2014
	@Column(name = "subject")
	private String subject;

	@Column(name = "is_sender_deleted",length=1)
	private Integer isSenderDeleted;

	@Column(name = "is_receiver_deleted",length=1)
	private Integer isReceiverDeleted;

	@Column(name = "is_draft")
	private Boolean isDraft;

	@Column(name = "date")
	private Date date;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
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

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getIsSenderDeleted() {
		return isSenderDeleted;
	}

	public void setIsSenderDeleted(Integer isSenderDeleted) {
		this.isSenderDeleted = isSenderDeleted;
	}

	public Integer getIsReceiverDeleted() {
		return isReceiverDeleted;
	}

	public void setIsReceiverDeleted(Integer isReceiverDeleted) {
		this.isReceiverDeleted = isReceiverDeleted;
	}

	public Boolean getIsDraft() {
		return isDraft;
	}

	public void setIsDraft(Boolean isDraft) {
		this.isDraft = isDraft;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
