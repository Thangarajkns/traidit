/**
 * @since 21-Jan-2015
 */
package org.kns.traidit.backend.refferral.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kns.traidit.backend.user.model.TraidItUser;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
@Entity
@Table(name="kns_traidit_refferral_token")
public class RefferralToken {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name="refferral_id")
	private TraidItUser refferralId;
	
	@Column(name="user_ip")
	private String userIP;

	@Column(name="os_version")
	private String osVersion;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TraidItUser getRefferralId() {
		return refferralId;
	}

	public void setRefferralId(TraidItUser refferralId) {
		this.refferralId = refferralId;
	}

	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	
}
