package org.kns.traidit.frontend.trade.dto;

import java.util.ArrayList;
import java.util.Date;

import org.kns.traidit.backend.trade.model.TradeInventories;
import org.kns.traidit.frontend.inventory.dto.InventoryDto;

/**
 * Created by     : Soujanya

 * Created Date	  : november 3rd,2014
 * file Name	  : TradeInventoriesDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */

public class TradeInventoriesDto {

	private Integer id;
	private InventoryDto inventory;
	private String offerType;
	private String offeredBy;
	private String inventoryOf;
	private Boolean isCurrent;
	private Date tradeInventoryDate;
	private Integer quantity;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public InventoryDto getInventory() {
		return inventory;
	}
	public void setInventory(InventoryDto inventory) {
		this.inventory = inventory;
	}
	public String getOfferType() {
		return offerType;
	}
	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}
	public String getOfferedBy() {
		return offeredBy;
	}
	public void setOfferedBy(String offeredBy) {
		this.offeredBy = offeredBy;
	}
	public String getInventoryOf() {
		return inventoryOf;
	}
	public void setInventoryOf(String inventoryOf) {
		this.inventoryOf = inventoryOf;
	}
	public Boolean getIsCurrent() {
		return isCurrent;
	}
	public void setIsCurrent(Boolean isCurrent) {
		this.isCurrent = isCurrent;
	}
	public Date getTradeInventoryDate() {
		return tradeInventoryDate;
	}
	public void setTradeInventoryDate(Date tradeInventoryDate) {
		this.tradeInventoryDate = tradeInventoryDate;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
	
	
	/*
	 * populating Dtos from model
	 */
	public static TradeInventoriesDto populateTradeInventoriesDto(TradeInventories tradeInventories){
		TradeInventoriesDto tradeInventoriesDto=new TradeInventoriesDto();
		tradeInventoriesDto.setId(tradeInventories.getId());
		tradeInventoriesDto.setInventory(InventoryDto.populateInventoryDto(tradeInventories.getInventory()));
		tradeInventoriesDto.setIsCurrent(tradeInventories.getIsCurrent());
		tradeInventoriesDto.setOfferedBy(tradeInventories.getOfferedBy());
		tradeInventoriesDto.setInventoryOf(tradeInventories.getInventoryOf());
		tradeInventoriesDto.setOfferType(tradeInventories.getOfferType());
		tradeInventoriesDto.setQuantity(tradeInventories.getQuantity());
		tradeInventoriesDto.setTradeInventoryDate(tradeInventories.getTradeInventoryDate());
		return tradeInventoriesDto;
		
		
	}
	
	public static ArrayList<TradeInventoriesDto> populateTradeInventoriesDto(ArrayList<TradeInventories> tradeInventories){
		ArrayList<TradeInventoriesDto> tradeInventoriesDtos=new ArrayList<TradeInventoriesDto>();
		for(TradeInventories tradeInventory: tradeInventories){
			TradeInventoriesDto tradeInventoriesDto=populateTradeInventoriesDto(tradeInventory);
			tradeInventoriesDtos.add(tradeInventoriesDto);
		}
		return tradeInventoriesDtos;
		
		
	}
	
	
}
