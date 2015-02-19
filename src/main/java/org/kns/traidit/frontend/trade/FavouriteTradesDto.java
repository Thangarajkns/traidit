package org.kns.traidit.frontend.trade;

import java.util.ArrayList;

import org.kns.traidit.backend.user.favourites.model.FavouriteTrades;
import org.kns.traidit.backend.user.favourites.model.FavouriteVendor;
import org.kns.traidit.frontend.inventory.dto.InventoryDto;
import org.kns.traidit.frontend.trade.dto.TradesDto;
import org.kns.traidit.frontend.user.dto.FavouriteVendorDto;
import org.kns.traidit.frontend.user.dto.UserDto;

/**
 * Created by     : Soujanya

 * Created Date	  : Dec 24th,2014
 * file Name	  : FavouriteTradesDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */

public class FavouriteTradesDto {
	
	private Integer id;
	private UserDto userId;
	private TradesDto tradeId;
	private Integer totalFavTrades;
	private InventoryDto tradeFromInventories;
    private InventoryDto tradeToInventories;
    private Integer currentLevel;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public UserDto getUserId() {
		return userId;
	}
	public void setUserId(UserDto userId) {
		this.userId = userId;
	}
	public TradesDto getTradeId() {
		return tradeId;
	}
	public void setTradeId(TradesDto tradeId) {
		this.tradeId = tradeId;
	}
	
	
	public Integer getTotalFavTrades() {
		return totalFavTrades;
	}
	public void setTotalFavTrades(Integer totalFavTrades) {
		this.totalFavTrades = totalFavTrades;
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
	
	public Integer getCurrentLevel() {
		return currentLevel;
	}
	public void setCurrentLevel(Integer currentLevel) {
		this.currentLevel = currentLevel;
	}
	public static FavouriteTradesDto populateFavouriteTradesDto(FavouriteTrades favTrade){
		FavouriteTradesDto favouriteTradeDto=new FavouriteTradesDto();
		favouriteTradeDto.setId(favTrade.getId());
		favouriteTradeDto.setUserId(UserDto.populateUserDto(favTrade.getUserId()));
		favouriteTradeDto.setTradeId(TradesDto.populateTradesDto(favTrade.getTradeId()));
		return favouriteTradeDto;
	}
	
	public static ArrayList<FavouriteTradesDto> populateFavouriteTradesDto(ArrayList<FavouriteTrades> favTrades){
		ArrayList<FavouriteTradesDto> favouriteTradeDto=new ArrayList<FavouriteTradesDto>();
		for(FavouriteTrades favTrade:favTrades){
			FavouriteTradesDto favouriteTradesDto=populateFavouriteTradesDto(favTrade);
			favouriteTradeDto.add(favouriteTradesDto);
		}
		return favouriteTradeDto;
		
		
	}

}
