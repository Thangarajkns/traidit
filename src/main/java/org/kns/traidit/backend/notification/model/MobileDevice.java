/**
 * @since 07-Jan-2015
 */
package org.kns.traidit.backend.notification.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kns.traidit.backend.user.model.TraidItUser;

/**
 * @author Thangaraj(KNSTEK)
 *
 */

@Entity
@Table(name = "kns_mobile_devices")
public class MobileDevice implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@javax.persistence.Id
	@GeneratedValue
	@Column(name = "id")
	private Integer Id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private TraidItUser user;
	
	@Column(name = "device_token",length = 64)
	private String deviceToken;
	
	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "last_updated_on")
	private Date lastUpdatedOn;
	
	@Column
	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public TraidItUser getUser() {
		return user;
	}

	public void setUser(TraidItUser user) {
		this.user = user;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	
	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	
	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
	
	
	
}
