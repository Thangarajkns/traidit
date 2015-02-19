/**
 * Created by     : Soujanya

 * Created Date	  : June 23,2014
 * file Name	  : SavedTradesDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */


package org.kns.traidit.frontend.trade.dto;

import java.util.ArrayList;

import org.kns.traidit.backend.trade.model.SavedTrades;
import org.kns.traidit.frontend.user.dto.UserDto;

public class SavedTradesDto {
	
	private Integer id;
	private TradesDto tradeId;
	private UserDto userId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public TradesDto getTradeId() {
		return tradeId;
	}
	public void setTradeId(TradesDto tradeId) {
		this.tradeId = tradeId;
	}
	public UserDto getUserId() {
		return userId;
	}
	public void setUserId(UserDto userId) {
		this.userId = userId;
	}
	
	/* For Populating Dtos from Model                        */	
	public static SavedTradesDto populateSavedTradesDto(SavedTrades savedTrade){
		SavedTradesDto savedTradesDto=new SavedTradesDto();
		savedTradesDto.setId(savedTrade.getId());
		savedTradesDto.setTradeId(TradesDto.populateTradesDto(savedTrade.getTradeId()));
		savedTradesDto.setUserId(UserDto.populateUserDto(savedTrade.getUserId()));
		return savedTradesDto;
		
	}
	
	public static ArrayList<SavedTradesDto> populateSavedTradesDto(ArrayList<SavedTrades> savedTrades){
		ArrayList<SavedTradesDto> savedTradesDto=new ArrayList<SavedTradesDto>();
		for(SavedTrades savedTrade:savedTrades){
			SavedTradesDto savedTradeDto=populateSavedTradesDto(savedTrade);
			savedTradesDto.add(savedTradeDto);
		}
		return savedTradesDto;
		
		
	}
	

}
