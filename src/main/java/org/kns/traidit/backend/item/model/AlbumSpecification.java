package org.kns.traidit.backend.item.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kns.traidit.backend.user.model.TraidItUser;

/*
 * Crested by Jeevan on July 15, 2014
 */
@Entity
@Table(name="kns_traidit_album_specification")
public class AlbumSpecification implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue
	@Column(name="album_id")
	private Integer albumId;
	
	
	@ManyToOne
	@JoinColumn(name="item_id")
	private TraidItItems item;
	
	
	@ManyToOne
	@JoinColumn(name="vendor_id")
	private TraidItUser vendor;
	
	@Column(name="track")
	private String track;
	
	@Column(name="singers")
	private String singers;

	public Integer getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
	}

	public TraidItItems getItem() {
		return item;
	}

	public void setItem(TraidItItems item) {
		this.item = item;
	}

	public TraidItUser getVendor() {
		return vendor;
	}

	public void setVendor(TraidItUser vendor) {
		this.vendor = vendor;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getSingers() {
		return singers;
	}

	public void setSingers(String singers) {
		this.singers = singers;
	}
	
	
	
	
	
}
