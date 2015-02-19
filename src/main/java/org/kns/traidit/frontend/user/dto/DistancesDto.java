/**
 * Created by     : Soujanya

 * Created Date	  : Sep 2,2014
 * file Name	  : DistancesDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */

package org.kns.traidit.frontend.user.dto;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.kns.traidit.backend.user.model.Distances;

public class DistancesDto {

	private Integer Id;
	private String zip1;
	private String zip2;
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
	
	public static DistancesDto populateDistancesDto(Distances distance){
		DistancesDto distanceDto=new DistancesDto();
		distanceDto.setId(distance.getId());
		distanceDto.setZip1(distance.getZip1());
		distanceDto.setZip2(distance.getZip2());
		distanceDto.setDistance(distance.getDistance());
		return distanceDto;
	}
	
	public static ArrayList<DistancesDto> populateDistances(ArrayList<Distances> distances){
		ArrayList<DistancesDto> distancesDtos=new ArrayList<DistancesDto>();
		for(Distances distance:distances){
			DistancesDto distanceDto=DistancesDto.populateDistancesDto(distance);
			distancesDtos.add(distanceDto);
		}
		return distancesDtos;
	}
}
