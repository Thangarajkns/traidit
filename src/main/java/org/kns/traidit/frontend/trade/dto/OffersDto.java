package org.kns.traidit.frontend.trade.dto;

import java.util.ArrayList;

import org.kns.traidit.backend.trade.model.OfferState;
import org.kns.traidit.backend.trade.model.OfferedBy;
import org.kns.traidit.frontend.inventory.dto.InventoryDto;

public class OffersDto {
	private OfferedBy offeredBy;
	private ArrayList<InventoryDto> traderInventories = new ArrayList<InventoryDto>();
	private ArrayList<InventoryDto> vendorInventories = new ArrayList<InventoryDto>();
	private Integer level;
	private OfferState offerState;
	
	public OfferedBy getOfferedBy() {
		return offeredBy;
	}
	public void setOfferedBy(OfferedBy offeredBy) {
		this.offeredBy = offeredBy;
	}
	public ArrayList<InventoryDto> getTraderInventories() {
		return traderInventories;
	}
	public void setTraderInventories(ArrayList<InventoryDto> traderInventories) {
		this.traderInventories = traderInventories;
	}
	public ArrayList<InventoryDto> getVendorInventories() {
		return vendorInventories;
	}
	public void setVendorInventories(ArrayList<InventoryDto> vendorInventories) {
		this.vendorInventories = vendorInventories;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public OfferState getOfferState() {
		return offerState;
	}
	public void setOfferState(OfferState offerState) {
		this.offerState = offerState;
	}
	
	
	
	
}
