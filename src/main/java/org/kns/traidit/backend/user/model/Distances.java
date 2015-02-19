package org.kns.traidit.backend.user.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * Created by Soujanya on september 2, 2014
 * Model for Distances between zip codes
 * Contians distance between two zip codes
 */
@Entity
@Table(name = "kns_traidit_distances")
public class Distances implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer Id;
	
	
	@Column(name = "zip1")
	private String zip1;
	
	@Column(name = "zip2")
	private String zip2;
	
	@Column(name = "distance")
	private BigDecimal distance;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getZip1() {
		return zip1;
	}

	public void setZip1(String zip1) {
		this.zip1 = zip1;
	}

	public String getZip2() {
		return zip2;
	}

	public void setZip2(String zip2) {
		this.zip2 = zip2;
	}

	
	public BigDecimal getDistance() {
		return distance;
	}

	
	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

	
	


	
	
}
