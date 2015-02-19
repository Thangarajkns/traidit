package org.kns.traidit.frontend.ebay.service;

import org.kns.traidit.backend.item.exception.ItemNotFoundException;
import org.kns.traidit.frontend.item.dto.ItemsDto;

public interface EbayAPIService {
	public ItemsDto getItemDetailsFromEbayByUPC(String upc)throws Exception;
	public ItemsDto getItemByUPC(String upc)throws ItemNotFoundException;
}
