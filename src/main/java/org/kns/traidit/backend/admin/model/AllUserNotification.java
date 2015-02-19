/**
 * @since 12-Feb-2015
 */
package org.kns.traidit.backend.admin.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Thangaraj(KNSTEK)
 *
 */

@Entity
@Table(name = "kns_traidit_all_users_notifications")
public class AllUserNotification {

	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@Column(name="notification_message")
	private String notificationMessage;
	
	@Column(name="created_on")
	private Date createdOn;
	
	@Column(name="notification_sent")
	private Boolean notificationSent;
	
	@Column(name="notification_sent_on")
	private Date notificationSentOn;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNotificationMessage() {
		return notificationMessage;
	}

	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Boolean getNotificationSent() {
		return notificationSent;
	}

	public void setNotificationSent(Boolean notificationSent) {
		this.notificationSent = notificationSent;
	}

	
	public Date getNotificationSentOn() {
		return notificationSentOn;
	}

	
	public void setNotificationSentOn(Date notificationSentOn) {
		this.notificationSentOn = notificationSentOn;
	}
}
	
