/**
 * @since 07-Jan-2015
 */
package org.kns.traidit.frontend.notification.dto;

import java.util.Date;

import org.kns.traidit.backend.notification.model.MobileDevice;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.user.dto.UserDto;

/**
 * @author Thangaraj(KNSTEK)
 *
 */
public class MobileDeviceDto {

	private Integer Id;
	
	private UserDto user;
	
	private String deviceToken;
	
	private Date createdOn;

	private Date lastUpdatedOn;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
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

	public static MobileDeviceDto populateDto(MobileDevice device){
		MobileDeviceDto deviceDto = new MobileDeviceDto();
		deviceDto.setId(device.getId());
		deviceDto.setCreatedOn(device.getCreatedOn());
		deviceDto.setDeviceToken(device.getDeviceToken());
		deviceDto.setLastUpdatedOn(device.getLastUpdatedOn());
		deviceDto.setUser(UserDto.populateUserDto(device.getUser()));
		return deviceDto;
	}
	
}
