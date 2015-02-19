/**
 * Created by     : Soujanya

 * Created Date	  : June 23,2014
 * file Name	  : UserCommentsDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */


package org.kns.traidit.frontend.item.dto;

import java.util.ArrayList;
import java.util.Date;

import org.kns.traidit.backend.item.model.UserComments;
import org.kns.traidit.frontend.user.dto.UserDto;

public class UserCommentsDto {
	private Integer commentId;
	private UserDto commentedUser;
	private String comment;
	private Integer rating;
	private Date commentedDate;
	private ItemsDto itemId;
	private Integer totalComments;
	public Integer getCommentId() {
		return commentId;
	}
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	public UserDto getCommentedUser() {
		return commentedUser;
	}
	public void setCommentedUser(UserDto commentedUser) {
		this.commentedUser = commentedUser;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public Date getCommentedDate() {
		return commentedDate;
	}
	public void setCommentedDate(Date commentedDate) {
		this.commentedDate = commentedDate;
	}
	public ItemsDto getItemId() {
		return itemId;
	}
	public void setItemId(ItemsDto itemId) {
		this.itemId = itemId;
	}
	
	
	
	public Integer getTotalComments() {
		return totalComments;
	}
	public void setTotalComments(Integer totalComments) {
		this.totalComments = totalComments;
	}
	public static UserCommentsDto populateUserCommentsDto(UserComments comments){
	    UserCommentsDto userCommentsDto=new UserCommentsDto();
		userCommentsDto.setComment(comments.getComment());
		userCommentsDto.setCommentedDate(comments.getCommentedDate());
		userCommentsDto.setCommentId(comments.getCommentId());
		userCommentsDto.setRating(comments.getRating());
		userCommentsDto.setCommentedUser(UserDto.populateUserDto(comments.getCommentedUser()));
		userCommentsDto.setItemId(ItemsDto.populateItemsDto(comments.getItemId()));
		return userCommentsDto;
		
	}
	
	public static ArrayList<UserCommentsDto> populateUserCommentsDto(ArrayList<UserComments> comments){
		ArrayList<UserCommentsDto> userCommentsDto=new ArrayList<UserCommentsDto>();
		for(UserComments comment:comments){
			UserCommentsDto userCommentDto=populateUserCommentsDto(comment);
			userCommentsDto.add(userCommentDto);
		}
		return userCommentsDto;
		
		
	}
	
}
