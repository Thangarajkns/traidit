/**
 * Created by     : Soujanya

 * Created Date	  : June 20,2014
 * file Name	  : TradesDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */


package org.kns.traidit.frontend.trade.dto;

import java.util.ArrayList;
import java.util.Date;

import org.kns.traidit.backend.trade.model.OfferedBy;
import org.kns.traidit.backend.trade.model.Trades;
import org.kns.traidit.frontend.inventory.dto.InventoryDto;
import org.kns.traidit.frontend.item.dto.ItemsDto;
import org.kns.traidit.frontend.user.dto.UserDto;

public class TradesDto {
	
	private Integer tradeId;
	private UserDto trader;
	private UserDto vendor;
	private String status;
    private InventoryDto tradeFromInventories;
    private InventoryDto tradeToInventories;
    private Boolean isCurrentUserCanOffer = false;
    private OfferedBy currentUserIs;
    private Integer currentLevel;
    private ArrayList<OffersDto> offers = new ArrayList<OffersDto>();


	public Integer getTradeId() {
		return tradeId;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	public UserDto getTrader() {
		return trader;
	}

	public void setTrader(UserDto trader) {
		this.trader = trader;
	}

	public UserDto getVendor() {
		return vendor;
	}

	public void setVendor(UserDto vendor) {
		this.vendor = vendor;
	}

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

	public InventoryDto getTradeFromInventories() {
		return tradeFromInventories;
	}

	public void setTradeFromInventories(InventoryDto tradeFromInventories) {
		this.tradeFromInventories = tradeFromInventories;
	}

	public InventoryDto getTradeToInventories() {
		return tradeToInventories;
	}

	public void setTradeToInventories(InventoryDto tradeToInventories) {
		this.tradeToInventories = tradeToInventories;
	}

	public Boolean getIsCurrentUserCanOffer() {
		return isCurrentUserCanOffer;
	}

	public void setIsCurrentUserCanOffer(Boolean isCurrentUserCanOffer) {
		this.isCurrentUserCanOffer = isCurrentUserCanOffer;
	}

	public OfferedBy getCurrentUserIs() {
		return currentUserIs;
	}

	public void setCurrentUserIs(OfferedBy currentUserIs) {
		this.currentUserIs = currentUserIs;
	}

	public ArrayList<OffersDto> getOffers() {
		return offers;
	}

	public void setOffers(ArrayList<OffersDto> offers) {
		this.offers = offers;
	}
	
	public Integer getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Integer currentLevel) {
		this.currentLevel = currentLevel;
	}

	/* For Populating Dtos from Model                        */	
	public static TradesDto populateTradesDto(Trades trade){
		TradesDto tradeDto=new TradesDto();
		tradeDto.setTradeId(trade.getTradeId());
		tradeDto.setTrader(UserDto.populateUserDto(trade.getTrader()));
		tradeDto.setVendor(UserDto.populateUserDto(trade.getVendor()));
		tradeDto.setStatus(trade.getStatus());
		return tradeDto;
		
	}
	
	public static ArrayList<TradesDto> populateTradesDto(ArrayList<Trades> trades){
		ArrayList<TradesDto> tradesDto=new ArrayList<TradesDto>();
		for(Trades trade:trades){
			TradesDto tradeDto=populateTradesDto(trade);
			tradesDto.add(tradeDto);
		}
		return tradesDto;
		
		
	}
	
}
