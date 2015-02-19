/**
 * Created by     : Soujanya

 * Created Date	  : October 1st,2014
 * file Name	  : UserMessagesDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */

package org.kns.traidit.frontend.user.dto;

import java.util.ArrayList;
import java.util.Date;

import org.kns.traidit.backend.trade.model.TradeMessages;
import org.kns.traidit.backend.user.model.UserMessages;
import org.kns.traidit.frontend.trade.dto.TradeMessagesDto;
import org.kns.traidit.frontend.trade.dto.TradesDto;

public class UserMessagesDto {

	
	private Integer id;
	private String message;
	private UserDto sender;
	private UserDto receiver;
	private Integer totalMessages;
	private Boolean isRead;
	
	//added by Soujanya on nov 24,2014
	private String subject;
	private Integer isSenderDeleted;
	private Integer isReceiverDeleted;
	private Boolean isDraft;
	private Date date;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public UserDto getSender() {
		return sender;
	}
	public void setSender(UserDto sender) {
		this.sender = sender;
	}
	public UserDto getReceiver() {
		return receiver;
	}
	public void setReceiver(UserDto receiver) {
		this.receiver = receiver;
	}
	public Integer getTotalMessages() {
		return totalMessages;
	}
	public void setTotalMessages(Integer totalMessages) {
		this.totalMessages = totalMessages;
	}
	public Boolean getIsRead() {
		return isRead;
	}
	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
	
	
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
	public Boolean getIsDraft() {
		return isDraft;
	}
	public Integer getIsSenderDeleted() {
		return isSenderDeleted;
	}
	public void setIsSenderDeleted(Integer isSenderDeleted) {
		this.isSenderDeleted = isSenderDeleted;
	}
	public Integer getIsReceiverDeleted() {
		return isReceiverDeleted;
	}
	public void setIsReceiverDeleted(Integer isReceiverDeleted) {
		this.isReceiverDeleted = isReceiverDeleted;
	}
	public void setIsDraft(Boolean isDraft) {
		this.isDraft = isDraft;
	}
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	/*
	 * populating Dtos from model
	 */
	public static UserMessagesDto populateUserMessagesDto(UserMessages userMessage){
		UserMessagesDto userMessagesDto=new UserMessagesDto();
		userMessagesDto.setId(userMessage.getId());
		userMessagesDto.setMessage(userMessage.getMessage());
		userMessagesDto.setIsRead(userMessage.getIsRead());
		userMessagesDto.setSender(UserDto.populateUserDto(userMessage.getSender()));
		userMessagesDto.setSubject(userMessage.getSubject());
		userMessagesDto.setIsDraft(userMessage.getIsDraft());
		userMessagesDto.setIsReceiverDeleted(userMessage.getIsReceiverDeleted());
		userMessagesDto.setIsSenderDeleted(userMessage.getIsSenderDeleted());
		userMessagesDto.setDate(userMessage.getDate());
	userMessagesDto.setReceiver(UserDto.populateUserDto(userMessage.getReceiver()));
		return userMessagesDto;
		
		
	}
	
	public static ArrayList<UserMessagesDto> populateUserMessagesDto(ArrayList<UserMessages> userMessages){
		ArrayList<UserMessagesDto> userMessagesDtos=new ArrayList<UserMessagesDto>();
		for(UserMessages userMessage: userMessages){
			UserMessagesDto userMessageDto=populateUserMessagesDto(userMessage);
			userMessagesDtos.add(userMessageDto);
		}
		return userMessagesDtos;
		
		
	}
	
	
}
