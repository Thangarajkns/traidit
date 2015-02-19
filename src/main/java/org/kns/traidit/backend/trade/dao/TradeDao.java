package org.kns.traidit.backend.trade.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kns.traidit.backend.inventory.exception.InventoryNotFoundException;
import org.kns.traidit.backend.item.model.TraidItItems;
import org.kns.traidit.backend.trade.exception.MessageNotFoundException;
import org.kns.traidit.backend.trade.exception.MessageNotSavedOrUpdatedException;
import org.kns.traidit.backend.trade.exception.SavedTradeNotSavedOrUpdatedException;
import org.kns.traidit.backend.trade.exception.TradeInventoryNotFoundException;
import org.kns.traidit.backend.trade.exception.TradeNotFoundException;
import org.kns.traidit.backend.trade.exception.TradeNotSavedOrUpdatedException;
import org.kns.traidit.backend.trade.model.OfferState;
import org.kns.traidit.backend.trade.model.SavedTrades;
import org.kns.traidit.backend.trade.model.TradeInventories;
import org.kns.traidit.backend.trade.model.TradeMessages;
import org.kns.traidit.backend.trade.model.TradeStates;
import org.kns.traidit.backend.trade.model.Trades;
import org.kns.traidit.backend.trade.model.VendorRatings;
import org.kns.traidit.backend.user.exception.FavInventoryNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.FavouritesNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.favourites.model.FavouriteTrades;
import org.kns.traidit.backend.user.favourites.model.FavouriteVendor;
import org.kns.traidit.backend.user.model.TraidItUser;

/*
 * created by Soujanya on 20th June,2014
 * DAO interface for trade related activities
 */
public interface TradeDao {

	public Integer saveOrUpdateTrade(Trades trade) throws TradeNotSavedOrUpdatedException;

	public void deleteTrade(Trades trade) throws TradeNotFoundException;

	public Trades getTradeByTradeId(Integer tradeId) throws TradeNotFoundException;

	public Trades getTradeByItemId(TraidItItems itemId) throws TradeNotFoundException;

	public Trades getTradeByTraderId(TraidItUser traderId) throws TradeNotFoundException;

	public Trades getTradeByVendorId(TraidItUser vendorId) throws TradeNotFoundException;

	/*
	 * public Trades getTradeByOfferId(ItemOffers offerId) throws
	 * TradeNotFoundException;
	 */
	public Trades getTradeByDate(Date tradeDate) throws TradeNotFoundException;

	public Integer saveOrUpdateSavedTrade(SavedTrades trade) throws SavedTradeNotSavedOrUpdatedException;

	public SavedTrades getSavedTradeById(Integer id) throws TradeNotFoundException;

	public SavedTrades getSavedTradeByTradeId(Trades tradeId) throws TradeNotFoundException;

	public SavedTrades getSavedTradeByUserId(TraidItUser userId) throws TradeNotFoundException;

	public Integer saveOrUpdateTradeMessage(TradeMessages message) throws MessageNotSavedOrUpdatedException;

	public void deleteTradeMessage(String message) throws MessageNotFoundException;

	public ArrayList<TradeMessages> getAllTradeMessages(Integer pageNo, Integer pageSize, Integer receiverId)
			throws MessageNotFoundException;

	public ArrayList<Trades> getTradesByTradeCriteria(TradeStates status, Integer traderId)
			throws TradeNotFoundException;

	public ArrayList<Trades> getNewTrades(TradeStates status, Integer traderId) throws TradeNotFoundException;

	public ArrayList<Trades> getTradesByInventoryCriteria(OfferState status, Integer traderId)
			throws TradeNotFoundException;

	public TradeMessages getTradeMessageById(Integer id) throws MessageNotFoundException;

	public TradeMessages getTradeMessageByTradeId(Trades tradeId) throws MessageNotFoundException;

	public TradeMessages getTradeMessageBySender(TraidItUser sender) throws MessageNotFoundException;

	public TradeMessages getTradeMessageByReceiver(TraidItUser receiver) throws MessageNotFoundException;

	public Integer saveOrUpdateVendorRating(VendorRatings vendorRating) throws Exception;

	public Integer getTotalTradeCountOfVendor(TraidItUser vendorId) throws UserNotFoundException;

	public Long getTotalRatingsCountOfVendor(Integer vendorId) throws UserNotFoundException;

	public Long getSumOfRatingsOfVendor(Integer vendorId);

	public Integer getIndividualRatingCount(TraidItUser vendorId, Integer rating) throws Exception;

	public FavouriteVendor getFavouriteVendorByVendorIdAndUserId(Integer vendorId, Integer userId)
			throws FavouritesNotFoundException;

	public Integer saveOrUpdateTradeInventories(TradeInventories tradeInventories)
			throws TradeNotSavedOrUpdatedException;

	public Integer getTradeCountByStatus(Integer userId, String status) throws TradeNotFoundException;

	public Integer getCurrentLevelOfTrade(Integer tradeId) throws InventoryNotFoundException;

	public void unsetCurrentFlagOfTrade(Integer tradeId);

	public ArrayList<VendorRatings> getVendorRatingsFromDB(Integer vendorId, String rating) throws Exception;

	public TradeInventories getInventoryByTradeId(Integer tradeId, String inventoryOf, Boolean listSavedOnly)
			throws TradeInventoryNotFoundException;

	public ArrayList<TradeInventories> getCurrentInventoriesByTraidId(Integer tradeId)
			throws InventoryNotFoundException;

	public ArrayList<TradeInventories> getTraderInventories(Integer tradeId, String inventoryOf)
			throws InventoryNotFoundException;

	public List<Object[]> getSavedTradesByUserId(Integer userId) throws TradeNotFoundException;

	public ArrayList<Trades> getTradeHistoryList(Integer userId) throws TradeNotFoundException;

	public ArrayList<TradeInventories> getAllInventoriesOfTrade(Integer tradeId) throws TradeInventoryNotFoundException;

	public Integer getCountOfOffersByStatus(Integer userId, TradeStates status) throws TradeNotFoundException;

	public Integer getCountOfSavedTrades(Integer userId) throws TradeNotFoundException;

	public Integer getCountOfTradesOfTradeHistory(Integer userId) throws TradeNotFoundException;

	public ArrayList<TradeInventories> getTraderSavedInventories(Integer tradeId, Integer level, String inventoryOf)
			throws InventoryNotFoundException;

	public ArrayList<TradeInventories> getInventoriesOfSavedTradeByLevel(Integer tradeId, Integer level)
			throws TradeInventoryNotFoundException;

	public void deleteSavedTradeInventory(TradeInventories tradeInventory) throws TradeInventoryNotFoundException;

	public ArrayList<VendorRatings> getRecentVendorFeedbacks(Integer vendorId) throws Exception;

	public Integer saveFavouriteTrade(FavouriteTrades favouriteTrade) throws FavInventoryNotSavedOrUpdatedException;

	public ArrayList<FavouriteTrades> getAllFavouriteTradesOfUser(Integer userId) throws FavouritesNotFoundException;

	public FavouriteTrades getFavouriteTradeByTradeAndUser(Integer tradeId, Integer userId)
			throws FavouritesNotFoundException;

	public void deleteFavouriteTradeFromDB(FavouriteTrades favouriteTrade) throws FavouritesNotFoundException;

	public Boolean isUserHasTradedWithVendor(Integer userId, Integer vendorId);
	
	public Integer getTotalNoOfCompletedTrades(Integer userId) throws TradeNotFoundException;
	
}
