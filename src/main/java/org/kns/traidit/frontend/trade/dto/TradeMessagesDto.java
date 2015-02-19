/**
 * Created by     : Soujanya

 * Created Date	  : June 23,2014
 * file Name	  : TradeMessagesDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */



package org.kns.traidit.frontend.trade.dto;

import java.util.ArrayList;

import org.kns.traidit.backend.trade.model.TradeMessages;
import org.kns.traidit.frontend.user.dto.UserDto;

public class TradeMessagesDto {
	
	private Integer id;
	private TradesDto trade;
	private String message;
	private UserDto sender;
	private UserDto receiver;
	private Integer totalMessages;
	private Integer levelId;
	private Boolean isRead;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public TradesDto getTrade() {
		return trade;
	}
	public void setTrade(TradesDto trade) {
		this.trade = trade;
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
	public Integer getLevelId() {
		return levelId;
	}
	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}
	public Boolean getIsRead() {
		return isRead;
	}
	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
	public Integer getTotalMessages() {
		return totalMessages;
	}
	
	public void setTotalMessages(Integer totalMessages) {
		this.totalMessages = totalMessages;
	}
	/* For Populating Dtos from Model                        */	
	public static TradeMessagesDto populateTradeMessagesDto(TradeMessages message){
		TradeMessagesDto tradeMessagesDto=new TradeMessagesDto();
		tradeMessagesDto.setId(message.getId());
		tradeMessagesDto.setMessage(message.getMessage());
		tradeMessagesDto.setTrade(TradesDto.populateTradesDto(message.getTrade()));
		tradeMessagesDto.setSender(UserDto.populateUserDto(message.getSender()));
		tradeMessagesDto.setReceiver(UserDto.populateUserDto(message.getReceiver()));
		tradeMessagesDto.setLevelId(message.getLevelId());
		tradeMessagesDto.setIsRead(message.getIsRead());
		return tradeMessagesDto;
		
		
	}
	
	public static ArrayList<TradeMessagesDto> populateTradeMessagesDto(ArrayList<TradeMessages> messages){
		ArrayList<TradeMessagesDto> tradeMessagesDtos=new ArrayList<TradeMessagesDto>();
		for(TradeMessages message:messages){
			TradeMessagesDto tradeMessageDto=populateTradeMessagesDto(message);
			tradeMessagesDtos.add(tradeMessageDto);
		}
		return tradeMessagesDtos;
		
		
	}
	
	

}
