package org.kns.traidit.frontend.trade.dto;

import java.util.ArrayList;

import org.kns.traidit.backend.trade.model.VendorRatings;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.backend.user.model.UserMessages;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.kns.traidit.frontend.user.dto.UserMessagesDto;

/**
 * Created by     : Soujanya

 * Created Date	  : October 8,2014
 * file Name	  : VendorRatingsDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */

public class VendorRatingsDto {

	private Integer id;
	private UserDto vendorId;
	private UserDto userId;
	private Integer rating;
	private String comment;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public UserDto getVendorId() {
		return vendorId;
	}
	public void setVendorId(UserDto userDto) {
		this.vendorId = userDto;
	}
	public UserDto getUserId() {
		return userId;
	}
	public void setUserId(UserDto userDto) {
		this.userId = userDto;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/*
	 * populating Dtos from model
	 */
	public static VendorRatingsDto populateVendorRatingsDto(VendorRatings vendorRating){
		VendorRatingsDto vendorRatingsDto=new VendorRatingsDto();
		vendorRatingsDto.setId(vendorRating.getId());
		vendorRatingsDto.setComment(vendorRating.getComment());
		vendorRatingsDto.setRating(vendorRating.getRating());
		vendorRatingsDto.setUserId(UserDto.populateUserDto(vendorRating.getUserId()));
		vendorRatingsDto.setVendorId(UserDto.populateUserDto(vendorRating.getUserId()));
		return vendorRatingsDto;
		
		
	}
	
	public static ArrayList<VendorRatingsDto> populateVendorRatingsDto(ArrayList<VendorRatings> vendorRatings){
		ArrayList<VendorRatingsDto> vendorRatingsDtos=new ArrayList<VendorRatingsDto>();
		for(VendorRatings vendorRating: vendorRatings){
			VendorRatingsDto vendorRatingDto=populateVendorRatingsDto(vendorRating);
			vendorRatingsDtos.add(vendorRatingDto);
		}
		return vendorRatingsDtos;
		
		
	}
	
	
}
