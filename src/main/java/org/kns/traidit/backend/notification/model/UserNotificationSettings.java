/**
 * @since 08-Jan-2015
 */
package org.kns.traidit.backend.notification.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.kns.traidit.backend.user.model.TraidItUser;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
@Entity
@Table(name = "kns_traidit_user_notification_settings")
public class UserNotificationSettings {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private TraidItUser user;
	
	@Column(name = "trade_reject_sound")
	private String tradeRejectSound;
	
	@Column(name = "trade_accept_sound")
	private String tradeAcceptSound;
	
	@Column(name = "trade_offer_sound")
	private String tradeOfferSound;
	
	@Column(name = "message_sound")
	private String messageSound;
	
	@Column(name = "notification_sound_on")
	private Boolean NotificationSoundOn;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TraidItUser getUser() {
		return user;
	}

	public void setUser(TraidItUser user) {
		this.user = user;
	}

	public String getTradeRejectSound() {
		return tradeRejectSound;
	}

	public void setTradeRejectSound(String tradeRejectSound) {
		this.tradeRejectSound = tradeRejectSound;
	}

	public String getTradeAcceptSound() {
		return tradeAcceptSound;
	}

	public void setTradeAcceptSound(String tradeAcceptSound) {
		this.tradeAcceptSound = tradeAcceptSound;
	}

	public String getTradeOfferSound() {
		return tradeOfferSound;
	}

	public void setTradeOfferSound(String tradeOfferSound) {
		this.tradeOfferSound = tradeOfferSound;
	}

	public String getMessageSound() {
		return messageSound;
	}

	public void setMessageSound(String messageSound) {
		this.messageSound = messageSound;
	}

	public Boolean getNotificationSoundOn() {
		return NotificationSoundOn;
	}

	public void setNotificationSoundOn(Boolean notificationSoundOn) {
		NotificationSoundOn = notificationSoundOn;
	}
	
	
}
