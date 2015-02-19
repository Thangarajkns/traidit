/**
 *  Created by     : Soujanya
 * Created Date	  : June 26th,2014
 * file Name	  : TradeService.java
 * Purpose		  : Service

 * 
 * 
 */

package org.kns.traidit.frontend.trade.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.kns.traidit.backend.inventory.exception.InventoryNotFoundException;
import org.kns.traidit.backend.inventory.exception.InventoryNotSavedOrUpdatedException;
import org.kns.traidit.backend.trade.exception.MessageNotFoundException;
import org.kns.traidit.backend.trade.exception.MessageNotSavedOrUpdatedException;
import org.kns.traidit.backend.trade.exception.TradeInventoryNotFoundException;
import org.kns.traidit.backend.trade.exception.TradeNotFoundException;
import org.kns.traidit.backend.trade.exception.TradeNotSavedOrUpdatedException;
import org.kns.traidit.backend.trade.exception.TradeStateNotFoundException;
import org.kns.traidit.backend.trade.model.OfferState;
import org.kns.traidit.backend.trade.model.TradeInventories;
import org.kns.traidit.backend.user.exception.FavouritesNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotSavedOrUpdatedException;
import org.kns.traidit.frontend.inventory.dto.InventoryDto;
import org.kns.traidit.frontend.trade.FavouriteTradesDto;
import org.kns.traidit.frontend.trade.dto.SavedTradesDto;
import org.kns.traidit.frontend.trade.dto.TradeMessagesDto;
import org.kns.traidit.frontend.trade.dto.TradesDto;
import org.kns.traidit.frontend.trade.dto.VendorRatingsDto;
import org.kns.traidit.frontend.user.dto.UserDto;

public interface TradeService {

	public TradesDto getTradeByTradeId(Integer tradeId) throws TradeNotFoundException;

	public TradesDto getTradeByDate(Date tradeDate) throws TradeNotFoundException;

	public SavedTradesDto getSavedTradeById(Integer id) throws TradeNotFoundException;

	public ArrayList<TradeMessagesDto> viewTradeMessages(Integer pageNo, Integer pageSize, Integer receiverId)
			throws MessageNotFoundException;

	public ArrayList<TradesDto> getTradesByCriteria(String status, Integer traderId)
			throws TradeInventoryNotFoundException, TradeNotFoundException, TradeStateNotFoundException,
			InventoryNotFoundException;

	public ArrayList<TradesDto> getTradesBySavedCriteria(Integer userId) throws TradeNotFoundException,
			InventoryNotFoundException, FavouritesNotFoundException;

	public TradeMessagesDto getTradeMessageById(Integer id) throws MessageNotFoundException;

	public Integer acceptOffer(Integer tradeId, Integer userId, String message) throws TradeNotSavedOrUpdatedException,
			TradeNotFoundException, InventoryNotFoundException, InventoryNotSavedOrUpdatedException,
			MessageNotSavedOrUpdatedException, UserNotFoundException;

	public Integer rejectOffer(Integer tradeId, Integer userId, String message) throws TradeNotSavedOrUpdatedException,
			TradeNotFoundException, UserNotFoundException;

	public Integer addTradeMessageToDB(String message, Integer senderId, Integer receiverId, Integer tradeId,
			Integer level) throws MessageNotSavedOrUpdatedException, TradeNotFoundException, UserNotFoundException;

	public Integer saveVendorRating(Integer vendorId, Integer userId, String rating, String comment) throws Exception;

	public void updateVendorRatings(Integer vendorId) throws UserNotFoundException, UserNotSavedOrUpdatedException;

	public Integer getTotalTradeCountOfVendor(Integer vendorId) throws Exception;

	public Map<String, Integer> getIndividualRatingCountOfVendor(Integer vendorId) throws Exception;

	public Integer getPercentagePositiveRatingOfVendor(Integer vendorId) throws Exception;

	public UserDto getIsFavouriteVendor(UserDto userDto, Integer userId) throws FavouritesNotFoundException;

	public Integer getInventoryCountOfVendor(Integer vendorId) throws Exception;

	public Integer getCurrentLevelOfTrade(Integer tradeId) throws InventoryNotFoundException;

	public ArrayList<VendorRatingsDto> getVendorRatings(Integer vendorId, String rating) throws Exception;

	public String getCurrentOfferType(Integer tradeId) throws InventoryNotFoundException;

	public ArrayList<InventoryDto> getTraderInventoriesOfTrade(Integer tradeId, Integer userId)
			throws InventoryNotFoundException;

	public ArrayList<InventoryDto> getVendorInventoriesOfTrade(Integer tradeId, Integer userId)
			throws InventoryNotFoundException;

	public InventoryDto populateTradeInventoryData(TradeInventories tradeInventory) throws InventoryNotFoundException;

	public Integer sendOffer(Integer tradeId, ArrayList<InventoryDto> senderInventories,
			ArrayList<InventoryDto> receiverInventories, Integer senderId, Integer receiverId, String message,
			OfferState state) throws TradeNotSavedOrUpdatedException, MessageNotSavedOrUpdatedException,
			TradeNotFoundException, UserNotFoundException, InventoryNotFoundException;

	public void unsetIsReadTrade(Integer userId, Integer tradeId) throws Exception;

	public Integer completeTrade(Integer tradeId, Integer userId, String message, Boolean complete)
			throws TradeNotSavedOrUpdatedException, TradeNotFoundException;

	public Double calculateJiucePerMonth(Integer x) throws Exception;

	public Double calculateJiucePerYear(Integer x) throws Exception;

	public ArrayList<TradesDto> getTradeHistoryList(Integer userId) throws TradeNotFoundException, Exception;

	public TradesDto getTradeHistory(Integer tradeId, Integer userId) throws TradeNotFoundException,
			TradeInventoryNotFoundException, InventoryNotFoundException;

	public Map<String, Integer> populateCountsForSummary(Integer userId) throws Exception;

	public ArrayList<InventoryDto> getSavedTraderInventoriesOfTrade(Integer userId, Integer tradeId, Integer level)
			throws InventoryNotFoundException;

	public ArrayList<InventoryDto> getSavedVendorInventoriesOfTrade(Integer userId, Integer tradeId, Integer level)
			throws InventoryNotFoundException;

	public void deleteSavedTradeInventories(Integer tradeId, Integer level) throws Exception;

	public ArrayList<VendorRatingsDto> getRecentVendorRatings(Integer vendorId) throws Exception;

	public Integer saveFavouriteTrade(Integer userId, Integer tradeId) throws Exception;

	public ArrayList<FavouriteTradesDto> getAllFavouriteTradesOfUser(Integer userId) throws Exception;

	public void deleteFavouriteTrade(Integer tradeId, Integer userId) throws FavouritesNotFoundException;

	public Integer getNewTradesCountOfUser(Integer userId);

	public Boolean isUserHasTradedWithVendor(Integer userId, Integer vendorId);
}
