/*
 * Created by Soujanya on June 26,2014..
 * Service for Trade Related Activites..
 */

package org.kns.traidit.frontend.trade.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.category.dao.CategoryDao;
import org.kns.traidit.backend.inventory.dao.InventoryDao;
import org.kns.traidit.backend.inventory.exception.InventoryNotFoundException;
import org.kns.traidit.backend.inventory.exception.InventoryNotSavedOrUpdatedException;
import org.kns.traidit.backend.inventory.model.Inventory;
import org.kns.traidit.backend.notification.exception.MobileDeviceNotFoundException;
import org.kns.traidit.backend.notification.exception.NotificationNotSentException;
import org.kns.traidit.backend.trade.dao.TradeDao;
import org.kns.traidit.backend.trade.dao.TradeDaoImpl;
import org.kns.traidit.backend.trade.exception.MessageNotFoundException;
import org.kns.traidit.backend.trade.exception.MessageNotSavedOrUpdatedException;
import org.kns.traidit.backend.trade.exception.TradeInventoryNotFoundException;
import org.kns.traidit.backend.trade.exception.TradeNotFoundException;
import org.kns.traidit.backend.trade.exception.TradeNotSavedOrUpdatedException;
import org.kns.traidit.backend.trade.exception.TradeStateNotFoundException;
import org.kns.traidit.backend.trade.model.OfferState;
import org.kns.traidit.backend.trade.model.OfferedBy;
import org.kns.traidit.backend.trade.model.Ratings;
import org.kns.traidit.backend.trade.model.SavedTrades;
import org.kns.traidit.backend.trade.model.TradeInventories;
import org.kns.traidit.backend.trade.model.TradeMessages;
import org.kns.traidit.backend.trade.model.TradeStates;
import org.kns.traidit.backend.trade.model.Trades;
import org.kns.traidit.backend.trade.model.VendorRatings;
import org.kns.traidit.backend.user.dao.UserDao;
import org.kns.traidit.backend.user.exception.FavouritesNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.favourites.model.FavouriteInventory;
import org.kns.traidit.backend.user.favourites.model.FavouriteTrades;
import org.kns.traidit.backend.user.favourites.model.FavouriteVendor;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.inventory.dto.InventoryDto;
import org.kns.traidit.frontend.inventory.service.InventoryService;
import org.kns.traidit.frontend.notification.service.NotificationService;
import org.kns.traidit.frontend.trade.FavouriteTradesDto;
import org.kns.traidit.frontend.trade.dto.OffersDto;
import org.kns.traidit.frontend.trade.dto.SavedTradesDto;
import org.kns.traidit.frontend.trade.dto.TradeMessagesDto;
import org.kns.traidit.frontend.trade.dto.TradesDto;
import org.kns.traidit.frontend.trade.dto.VendorRatingsDto;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("tradeService")
public class TradeServiceImpl implements TradeService {

	private static Logger log = Logger.getLogger(TradeServiceImpl.class);

	@Resource(name = "tradeDao")
	private TradeDao tradeDao;

	@Resource(name = "userDao")
	private UserDao userDao;

	@Resource(name = "inventoryDao")
	private InventoryDao inventoryDao;

	@Resource(name = "inventoryService")
	private InventoryService inventoryService;

	@Resource(name = "categoryDao")
	private CategoryDao categoryDao;

	@Resource(name = "notificationService")
	private NotificationService notificationService;

	// added by thangaraj on Nov 12, 2014
	public static String currentUserCanOffer = "";

	/*
	 * added by Soujanya on June 26,2014 method to retrieve trade by trade Id
	 */

	public TradesDto getTradeByTradeId(Integer tradeId) throws TradeNotFoundException {
		log.info("inside getTradeByTradeId()" + tradeId);
		try {
			Trades trade = this.tradeDao.getTradeByTradeId(tradeId);
			TradesDto tradesDto = TradesDto.populateTradesDto(trade);
			return tradesDto;
		} catch (TradeNotFoundException e) {
			log.error("Error While retrieving Trade " + e.toString());
			throw e;
		}
	}

	/*
	 * added by Soujanya on june 26,2014 method to retrieve trade according to
	 * date
	 */
	public TradesDto getTradeByDate(Date tradeDate) throws TradeNotFoundException {
		log.info("inside getTradeByDate()");
		try {
			Trades trade = this.tradeDao.getTradeByDate(tradeDate);
			TradesDto tradesDto = TradesDto.populateTradesDto(trade);
			return tradesDto;
		} catch (Exception e) {
			log.error("Error While Validating Trade " + e.toString());
			return null;
		}
	}

	/*
	 * added by Soujanya on june 26,2014 method to retrieve saved trade by Id
	 */
	public SavedTradesDto getSavedTradeById(Integer id) throws TradeNotFoundException {
		log.info("inside getSavedTradeById()");
		try {
			SavedTrades savedTrade = this.tradeDao.getSavedTradeById(id);
			SavedTradesDto savedTradesDto = SavedTradesDto.populateSavedTradesDto(savedTrade);
			return savedTradesDto;
		} catch (Exception e) {
			log.error("Error While Validating Saved Trade " + e.toString());
			return null;
		}
	}

	/*
	 * added by Soujanya on June 26,2014 method to retrieve trade messages by Id
	 */
	public TradeMessagesDto getTradeMessageById(Integer id) throws MessageNotFoundException {
		log.info("inside getTradeMessageById()");
		try {
			TradeMessages tradeMessage = this.tradeDao.getTradeMessageById(id);
			TradeMessagesDto tradeMessagesDto = TradeMessagesDto.populateTradeMessagesDto(tradeMessage);
			return tradeMessagesDto;
		} catch (Exception e) {
			log.error("Error While Validating Trade Message " + e.toString());
			return null;
		}
	}

	/*
	 * added by Soujanya on June 26,2014 edited on July 11,2014 method to view
	 * all trade messages
	 */
	public ArrayList<TradeMessagesDto> viewTradeMessages(Integer pageNo, Integer pageSize, Integer receiverId)
			throws MessageNotFoundException {
		log.info("inside viewTradeMessages()");
		ArrayList<TradeMessages> tradeMessage = this.tradeDao.getAllTradeMessages(pageNo, pageSize, receiverId);
		ArrayList<TradeMessagesDto> tradeMessageDtos = new ArrayList<TradeMessagesDto>();
		Integer totalMessages = TradeDaoImpl.totalMessages;
		for (TradeMessages tradeMessages : tradeMessage) {

			TradeMessagesDto tradeMessagesDto = TradeMessagesDto.populateTradeMessagesDto(tradeMessages);
			tradeMessagesDto.setTotalMessages(totalMessages);
			tradeMessageDtos.add(tradeMessagesDto);
		}

		return tradeMessageDtos;
	}

	/**
	 * added by Soujanya on July 23,2014
	 * 
	 * edited by Soujanya on november 3rd,2014 method to retrieve trades based
	 * on criteria ie, status of trade as such pending,accepted or rejected
	 * 
	 * @return arraylist of trades dto
	 * @throws TradeNotFoundException
	 * @throws TradeStateNotFoundException
	 *             ,Exception
	 * @throws InventoryNotFoundException 
	 * @implementation: populates each trade dto with information of inventories
	 *                  traded sets isfavourite inventory to user's inventory
	 */
	@Transactional
	public ArrayList<TradesDto> getTradesByCriteria(String status, Integer traderId)
			throws TradeInventoryNotFoundException, TradeNotFoundException, TradeStateNotFoundException, InventoryNotFoundException {
		log.info("inside getTradesByCriteria()");
		ArrayList<Trades> trades;
		Boolean listSavedOnly = false;
		if (status.equals("ACCEPT") || status.equals("REJECT")) {
			trades = this.tradeDao.getTradesByInventoryCriteria(OfferState.valueOf(status), traderId);
		} else if (status.equals("COMPLETED") || status.equals("PENDING")) {
			trades = this.tradeDao.getTradesByTradeCriteria(TradeStates.valueOf(status), traderId);
		} else if (status.equals("NEW")) {
			ArrayList<Trades> unfilteredTrades = this.tradeDao.getNewTrades(TradeStates.valueOf("PENDING"), traderId);
			trades = this.filterNewTrades(unfilteredTrades, traderId);
		} else {
			throw new TradeStateNotFoundException();
		}
		ArrayList<TradesDto> tradeDtos = new ArrayList<TradesDto>();
		ArrayList<Integer> favInventories = new ArrayList<Integer>();
		try {
			for (FavouriteInventory fav : this.inventoryDao.getFavouriteInventoriesOfUser(traderId, false, false, 0)) {
				favInventories.add(fav.getInventory().getInventoryId());
			}
		} catch (FavouritesNotFoundException e) {
			log.info("no favourite inventories found for the user");
		}
		for (Trades trade : trades) {
			TradesDto tradeDto = TradesDto.populateTradesDto(trade);
			TradeInventories tradeFromInventory = this.tradeDao.getInventoryByTradeId(tradeDto.getTradeId(),
					OfferedBy.TRADER.toString(), listSavedOnly);
			InventoryDto fromInventory = this.populateTradeInventoryData(tradeFromInventory);
			if (trade.getTrader().getUserId().equals(traderId)
					&& favInventories.contains(fromInventory.getInventoryId())) {
				fromInventory.setIsFavouriteInventory(true);
			} else {
				fromInventory.setIsFavouriteInventory(false);
			}
			tradeDto.setTradeFromInventories(fromInventory);
			TradeInventories tradeToInventory = this.tradeDao.getInventoryByTradeId(tradeDto.getTradeId(),
					OfferedBy.VENDOR.toString(), listSavedOnly);
			InventoryDto toInventory = this.populateTradeInventoryData(tradeToInventory);
			if (trade.getVendor().getUserId().equals(traderId)
					&& favInventories.contains(fromInventory.getInventoryId())) {
				fromInventory.setIsFavouriteInventory(true);
			} else {
				fromInventory.setIsFavouriteInventory(false);
			}
			tradeDto.setTradeToInventories(toInventory);
			tradeDto.setCurrentLevel(tradeToInventory.getLevel());
			tradeDtos.add(tradeDto);
		}
		return tradeDtos;
	}

	/**
	 * @author Thangaraj
	 * @throws FavouritesNotFoundException
	 * @since 26-11-2014
	 */
	public ArrayList<TradesDto> getTradesBySavedCriteria(Integer userId) throws TradeNotFoundException,
			InventoryNotFoundException {
		log.info("inside getTradesBySavedCriteria(" + userId + ")");
		List<Object[]> trades;
		trades = this.tradeDao.getSavedTradesByUserId(userId);
		ArrayList<TradesDto> tradeDtos = new ArrayList<TradesDto>();
		ArrayList<Integer> favInventories = new ArrayList<Integer>();
		ArrayList<FavouriteInventory> favouriteInventories;
		try {
			favouriteInventories = this.inventoryDao.getFavouriteInventoriesOfUser(userId, false, false, 0);
		} catch (FavouritesNotFoundException e) {
			favouriteInventories = new ArrayList<FavouriteInventory>();
		}
		for (FavouriteInventory fav : favouriteInventories) {
			favInventories.add(fav.getInventory().getInventoryId());
		}
		for (Object[] savedtrade : trades) {
			Trades trade = (Trades) savedtrade[0];
			Integer level = (Integer) savedtrade[1];
			TradesDto tradeDto = TradesDto.populateTradesDto(trade);
			TradeInventories tradeFromInventory = this.tradeDao.getTraderSavedInventories(trade.getTradeId(), level,
					OfferedBy.TRADER.toString()).get(0);
			InventoryDto fromInventory = this.populateTradeInventoryData(tradeFromInventory);
			if (trade.getTrader().getUserId().equals(userId) && favInventories.contains(fromInventory.getInventoryId())) {
				fromInventory.setIsFavouriteInventory(true);
			} else {
				fromInventory.setIsFavouriteInventory(false);
			}
			tradeDto.setTradeFromInventories(fromInventory);
			TradeInventories tradeToInventory = this.tradeDao.getTraderSavedInventories(trade.getTradeId(), level,
					OfferedBy.VENDOR.toString()).get(0);
			InventoryDto toInventory = this.populateTradeInventoryData(tradeToInventory);
			if (trade.getVendor().getUserId().equals(userId) && favInventories.contains(fromInventory.getInventoryId())) {
				fromInventory.setIsFavouriteInventory(true);
			} else {
				fromInventory.setIsFavouriteInventory(false);
			}
			tradeDto.setTradeToInventories(toInventory);
			tradeDto.setCurrentLevel(tradeToInventory.getLevel());
			tradeDtos.add(tradeDto);
		}
		return tradeDtos;
	}

	/**
	 * @author Soujanya
	 * @since 6th november,2014 method to filter new trades
	 * 
	 *        modified by thangaraj :
	 */
	private ArrayList<Trades> filterNewTrades(ArrayList<Trades> unfilteredTrades, Integer userId) {
		log.info("inside filterNewTrades()");
		ArrayList<Trades> newList = new ArrayList<Trades>();
		for (Trades trade : unfilteredTrades) {
			if (trade.getTrader().getUserId().equals(userId) && trade.getIsTraderRead() == false) {
				newList.add(trade);
			} else if (trade.getVendor().getUserId().equals(userId) && trade.getIsVendorRead() == false) {
				newList.add(trade);
			}
		}
		return newList;

	}

	/**
	 * @author Soujanya
	 * @throws InventoryNotFoundException
	 * @since november 3rd,2014 method to populate trade inventory information
	 * 
	 *        modified by thangaraj on nov 18, 2014 added units available and
	 *        changed exception type
	 */
	public InventoryDto populateTradeInventoryData(TradeInventories tradeInventory) throws InventoryNotFoundException {
		System.out.println(tradeInventory.getInventory().getInventoryId());
		Inventory inventory = this.inventoryDao.getInventoryByInventoryId(tradeInventory.getInventory()
				.getInventoryId());
		InventoryDto inventoryInfo = InventoryDto.populateInventoryDto(tradeInventory.getInventory());
		inventoryInfo.setPhotoList(inventoryService.populateInventorywithImages(inventoryInfo, inventory));
		inventoryInfo.setUnitsAvailable(tradeInventory.getQuantity());
		return inventoryInfo;
	}

	/**
	 * sends / saves the offer from trader/vendor for make offer,make counter
	 * offer, accept offer and save offer requests
	 * 
	 * @param tradeId
	 * @param senderInventories
	 * @param receiverInventories
	 * @param senderId
	 * @param receiverId
	 * @param message
	 * @param state
	 * @throws TradeNotSavedOrUpdatedException
	 * @throws MessageNotSavedOrUpdatedException
	 * @throws TradeNotFoundException
	 * @throws UserNotFoundException
	 * @throws InventoryNotFoundException
	 * @return Integer
	 * @author Thangaraj(KNSTEK)
	 * @since 24-Nov-2014
	 * 
	 *        modifed by Thangaraj on Jan 07, 2015 : added send notification
	 *        feature
	 */
	public Integer sendOffer(Integer tradeId, ArrayList<InventoryDto> senderInventories,
			ArrayList<InventoryDto> receiverInventories, Integer senderId, Integer receiverId, String message,
			OfferState state) throws TradeNotSavedOrUpdatedException, MessageNotSavedOrUpdatedException,
			TradeNotFoundException, UserNotFoundException, InventoryNotFoundException {

		log.info("inside addTradeToDB()");

		// initialisation
		Integer level = 1;
		Trades trade = new Trades();
		OfferedBy senderUserType = null, receiverUserType = null;
		String notificationMessage = message;
		// if trade id exists
		if (tradeId != null && tradeId > 0) {
			level = getCurrentLevelOfTrade(tradeId) + 1;
			trade = this.tradeDao.getTradeByTradeId(tradeId);

			if (trade.getTrader().getUserId().equals(senderId)) {
				senderUserType = OfferedBy.TRADER;
				receiverUserType = OfferedBy.VENDOR;
			} else if (trade.getVendor().getUserId().equals(senderId)) {
				senderUserType = OfferedBy.VENDOR;
				receiverUserType = OfferedBy.TRADER;
			} else {
				throw new TradeNotSavedOrUpdatedException();
			}
		}
		// if trade id does not exists
		else {
			senderUserType = OfferedBy.TRADER;
			receiverUserType = OfferedBy.VENDOR;
			trade.setTrader(new TraidItUser(senderId));
			trade.setVendor(new TraidItUser(receiverId));
		}

		// update trade status
		if (state.equals(OfferState.SAVED)
				&& (trade.getStatus() == null || trade.getStatus().equals(OfferState.SAVED.toString())))
			trade.setStatus(TradeStates.SAVED.toString());
		else if (state.equals(OfferState.OFFER) || state.equals(OfferState.COUNTER_OFFER)
				|| state.equals(OfferState.SAVED) || state.equals(OfferState.ACCEPT) || state.equals(OfferState.REJECT))
			trade.setStatus(TradeStates.PENDING.toString());

		// set notification read status
		if (senderUserType.toString().equals("TRADER") && !state.equals(OfferState.SAVED)) {
			trade.setIsTraderRead(true);
			trade.setIsVendorRead(false);
		} else if (!state.equals(OfferState.SAVED)) {
			trade.setIsTraderRead(false);
			trade.setIsVendorRead(true);
		}

		// save trade
		trade.setTradeId(this.tradeDao.saveOrUpdateTrade(trade));

		// unset current inventory flag
		if (!state.equals(OfferState.SAVED)) {
			this.unsetCurrentFlagOfTrade(tradeId);
		}

		// save trade inventories
		Date offerDate = new Date();
		try {
			for (InventoryDto userInventoryId : senderInventories) {
				Integer savedInventoryId = createAndAddTradeInventoryToDb(trade, userInventoryId, offerDate, state,
						senderUserType, senderUserType, level);
				log.info("created tradeInventoryId : " + savedInventoryId);
				System.out.println("created tradeInventoryId : " + savedInventoryId);
			}
			for (InventoryDto vendorInventoryId : receiverInventories) {
				Integer savedInventoryId = createAndAddTradeInventoryToDb(trade, vendorInventoryId, offerDate, state,
						senderUserType, receiverUserType, level);
				log.info("created tradeInventoryId : " + savedInventoryId);
				System.out.println("created tradeInventoryId : " + savedInventoryId);
			}
			if (!state.equals(OfferState.SAVED)) {
				this.addTradeMessageToDB(message, senderId, receiverId, trade.getTradeId(), level);

			}
		} catch (TradeNotSavedOrUpdatedException e) {
			throw e;
		} catch (InventoryNotFoundException e) {
			throw e;
		}

		// send notification to the user if request is not to save offers
		if (!state.equals(OfferState.SAVED)) {
			TraidItUser receiver;
			if (senderUserType.equals(OfferedBy.TRADER)) {
				receiver = this.userDao.getUserbyUserId(trade.getVendor().getUserId());
			} else {
				receiver = this.userDao.getUserbyUserId(trade.getTrader().getUserId());
			}
			String senderName = receiver.getDisplayName();
			if (state.equals(OfferState.OFFER)) {
				notificationMessage = "You got a new offer from " + senderName;
			} else if (state.equals(OfferState.COUNTER_OFFER)) {
				notificationMessage = "You got a counter offer from " + senderName;
			} else if (state.equals(OfferState.ACCEPT)) {
				notificationMessage = "Your offer has been accepted by " + senderName;
			}
			try {
				this.notificationService.sendNotification(receiverId, notificationMessage);
			} catch (NotificationNotSentException e1) {
				log.error(e1.toString() + " {message : " + notificationMessage + ", receiverId : " + receiverId + "}");
			} catch (MobileDeviceNotFoundException e) {
				log.error(e.toString() + " {message : " + notificationMessage + ", receiverId : " + receiverId + "}");
			} catch (Exception e) {
				log.error(e.toString() + " {message : " + notificationMessage + ", receiverId : " + receiverId + "}");
			}
		}
		return trade.getTradeId();

	}

	/**
	 * 
	 * @param trade
	 * @param itemInventory
	 * @param offeredBy
	 * @param offerDate
	 * @return
	 * @throws TradeNotSavedOrUpdatedException
	 * @throws InventoryNotFoundException
	 * @author Thangaraj
	 * @since 14-10-2014
	 */
	private Integer createAndAddTradeInventoryToDb(Trades trade, InventoryDto itemInventory, Date offerDate,
			OfferState state, OfferedBy offeredBy, OfferedBy inventoryOf, Integer level)
			throws TradeNotSavedOrUpdatedException, InventoryNotFoundException {
		log.info("createAndAddTradeInventoryToDb inventoryId:" + itemInventory.getInventoryId() + " level:" + level
				+ "");
		System.out.println("createAndAddTradeInventoryToDb inventoryId:" + itemInventory.getInventoryId() + " level:"
				+ level + "");
		TradeInventories tradeInventories = new TradeInventories();
		Inventory inventory = new Inventory(itemInventory.getInventoryId());
		tradeInventories.setInventory(inventory);
		tradeInventories.setTrade(trade);
		tradeInventories.setOfferType(state.toString());
		tradeInventories.setTradeInventoryDate(offerDate);
		tradeInventories.setOfferedBy(offeredBy.toString());
		tradeInventories.setInventoryOf(inventoryOf.toString());
		tradeInventories.setQuantity(itemInventory.getUnitsAvailable());
		if (state.equals(OfferState.SAVED))
			tradeInventories.setIsCurrent(false);
		else
			tradeInventories.setIsCurrent(true);
		tradeInventories.setLevel(level);
		return this.tradeDao.saveOrUpdateTradeInventories(tradeInventories);
	}

	/**
	 *
	 * @author Thangaraj
	 * @since 15-10-2014
	 */
	public Integer getCurrentLevelOfTrade(Integer tradeId) throws InventoryNotFoundException {
		return this.tradeDao.getCurrentLevelOfTrade(tradeId);
	}

	/**
	 * 
	 * @param tradeId
	 * @param level
	 * @author Thangaraj
	 * @since 15-10-2014
	 */
	private void unsetCurrentFlagOfTrade(Integer tradeId) {
		this.tradeDao.unsetCurrentFlagOfTrade(tradeId);
	}

	/*
	 * originally added by Soujanya on july 22,2014 method to accept offer
	 * 
	 * modified by thangaraj on 03-11-2014
	 * 
	 * modified by thangaraj on 24-11-2014
	 */
	public Integer acceptOffer(Integer tradeId, Integer userId, String message) throws TradeNotSavedOrUpdatedException,
			TradeNotFoundException, InventoryNotFoundException, InventoryNotSavedOrUpdatedException,
			MessageNotSavedOrUpdatedException, UserNotFoundException {
		log.info("inside acceptOffer()");
		Trades trade = this.tradeDao.getTradeByTradeId(tradeId);
		Integer result = 0;
		try {
			OfferedBy currentUserType = null;
			Integer senderId = null, receiverId = null;
			if (trade.getTrader().getUserId().equals(userId)) {
				currentUserType = OfferedBy.valueOf("TRADER");
				senderId = trade.getTrader().getUserId();
				receiverId = trade.getVendor().getUserId();
			} else if (trade.getVendor().getUserId().equals(userId)) {
				currentUserType = OfferedBy.valueOf("VENDOR");
				senderId = trade.getVendor().getUserId();
				receiverId = trade.getTrader().getUserId();
			}

			// first fetch trader and vendor inventories and then unset current
			// flag of trade
			ArrayList<InventoryDto> traderInvenories = getTraderInventoriesOfTrade(tradeId, userId);
			ArrayList<InventoryDto> vendorInvenories = getVendorInventoriesOfTrade(tradeId, userId);
			ArrayList<InventoryDto> senderInventories = currentUserType.equals(OfferedBy.valueOf("TRADER")) ? traderInvenories
					: vendorInvenories;
			ArrayList<InventoryDto> receiverInventories = currentUserType.equals(OfferedBy.valueOf("VENDOR")) ? traderInvenories
					: vendorInvenories;
			result = this.sendOffer(tradeId, senderInventories, receiverInventories, senderId, receiverId, message,
					OfferState.ACCEPT);
			traderInvenories.addAll(vendorInvenories);
			this.updateTradedInventoryStatus(traderInvenories);

		} catch (InventoryNotFoundException e) {
			System.out.println("InventoryNotFoundException : no inventories found for given trade");
			log.error("InventoryNotFoundException : no inventories found for given trade");
			// roll back trade status
			trade.setStatus(TradeStates.PENDING.toString());
			this.tradeDao.saveOrUpdateTrade(trade);
			throw e;
		} catch (InventoryNotSavedOrUpdatedException e) {
			System.out.println("InventoryNotSavedOrUpdatedException : cannot update inventories");
			log.error("InventoryNotSavedOrUpdatedException : cannot update inventories");
			// roll back trade status
			trade.setStatus(TradeStates.PENDING.toString());
			this.tradeDao.saveOrUpdateTrade(trade);
			throw e;
		} catch (MessageNotSavedOrUpdatedException e) {
			System.out.println("MessageNotSavedOrUpdatedException : cannot save messages");
			log.error("MessageNotSavedOrUpdatedException : cannot save messages");
			// roll back trade status
			trade.setStatus(TradeStates.PENDING.toString());
			this.tradeDao.saveOrUpdateTrade(trade);
			throw e;
		} catch (UserNotFoundException e) {
			System.out.println("UserNotFoundException ");
			log.error("UserNotFoundException");
			// roll back trade status
			trade.setStatus(TradeStates.PENDING.toString());
			this.tradeDao.saveOrUpdateTrade(trade);
			throw e;
		}

		return result;
	}

	/**
	 * 
	 * @param inventories
	 * @author Thangaraj
	 * @throws InventoryNotSavedOrUpdatedException
	 * @throws InventoryNotFoundException
	 * @since 24-11-2014
	 */
	private void updateTradedInventoryStatus(ArrayList<InventoryDto> inventories)
			throws InventoryNotSavedOrUpdatedException, InventoryNotFoundException {
		// decrement each inventory count by the count of that inventory
		// involved in this trade.
		for (InventoryDto inventoryDto : inventories) {
			Inventory inventory = this.inventoryDao.getInventoryByInventoryId(inventoryDto.getInventoryId());
			inventory.setUnitsAvailable(inventory.getUnitsAvailable() - inventoryDto.getUnitsAvailable());
			// if inventory count is less than 1, set available for sale to
			// false
			if (inventory.getUnitsAvailable() < 1)
				inventory.setAvailableForTrade(false);
			this.inventoryDao.saveOrUpdateInventory(inventory);
		}
	}
	
	/**
	 * 
	 * @param inventories
	 * @throws InventoryNotSavedOrUpdatedException
	 * @throws InventoryNotFoundException
	 * @return void
	 * @author Thangaraj(KNSTEK)
	 * @since 29-Jan-2015
	 */
	private void updateNonTradedInventoryStatus(ArrayList<InventoryDto> inventories)
			throws InventoryNotSavedOrUpdatedException, InventoryNotFoundException {
		// decrement each inventory count by the count of that inventory
		// involved in this trade.
		for (InventoryDto inventoryDto : inventories) {
			Inventory inventory = this.inventoryDao.getInventoryByInventoryId(inventoryDto.getInventoryId());
			inventory.setUnitsAvailable(inventory.getUnitsAvailable() + inventoryDto.getUnitsAvailable());
			inventory.setAvailableForTrade(true);
			this.inventoryDao.saveOrUpdateInventory(inventory);
		}
	}

	/*
	 * added by Soujanya on july 22,2014 method to reject offer
	 * 
	 * modified by Thangaraj on Jan 7, 2015 : added send notification feature
	 */
	public Integer rejectOffer(Integer tradeId, Integer userId, String message) throws TradeNotSavedOrUpdatedException,
			TradeNotFoundException, UserNotFoundException {
		log.info("inside rejectOffer()");

		// Initialisation
		Integer receiverId = 0;
		TraidItUser receiver = this.userDao.getUserbyUserId(userId);
		String notificationMessage = "Your offer has been rejected by " + receiver.getDisplayName();

		Trades trade = this.tradeDao.getTradeByTradeId(tradeId);
		trade.setStatus(TradeStates.PENDING.toString());
		Integer result = this.tradeDao.saveOrUpdateTrade(trade);
		if (result > 0) {
			OfferedBy currentUserType = null;
			if (trade.getTrader().getUserId().equals(userId)) {
				currentUserType = OfferedBy.valueOf("TRADER");
				receiverId = trade.getVendor().getUserId();
			} else if (trade.getVendor().getUserId().equals(userId)) {
				currentUserType = OfferedBy.valueOf("VENDOR");
				receiverId = trade.getTrader().getUserId();
			}
			try {
				Date offerDate = new Date();
				// current level of trade
				Integer level = this.getCurrentLevelOfTrade(tradeId);

				// first fetch trader and vendor inventories and then unset
				// current flag of trade
				ArrayList<InventoryDto> traderInvenories = getTraderInventoriesOfTrade(tradeId, userId);
				ArrayList<InventoryDto> vendorInvenories = getVendorInventoriesOfTrade(tradeId, userId);
				this.unsetCurrentFlagOfTrade(tradeId);

				// save trader and vendor inventory details
				for (InventoryDto inventoryDto : traderInvenories) {
					createAndAddTradeInventoryToDb(trade, inventoryDto, offerDate, OfferState.REJECT, currentUserType,
							OfferedBy.TRADER, level + 1);
				}
				for (InventoryDto inventoryDto : vendorInvenories) {
					createAndAddTradeInventoryToDb(trade, inventoryDto, offerDate, OfferState.REJECT, currentUserType,
							OfferedBy.VENDOR, level + 1);
				}
				// update trade message
				this.addTradeMessageToDB(message, trade.getVendor().getUserId(), trade.getTrader().getUserId(),
						tradeId, level + 1);

				// send notification to the user
				try {
					this.notificationService.sendNotification(receiverId, notificationMessage);
				} catch (NotificationNotSentException e1) {
					log.error(e1.toString() + " {message : " + notificationMessage + ", receiverId : " + receiverId
							+ "}");
				} catch (MobileDeviceNotFoundException e1) {
					log.error(e1.toString() + " {message : " + notificationMessage + ", receiverId : " + receiverId
							+ "}");
				}
				catch (Exception e) {
					log.error(e.toString() + " {message : " + notificationMessage + ", receiverId : " + receiverId
							+ "}");
				}
			} catch (Exception e) {
				log.error(e.toString() + " : some error occured while rejecting trade");
				// roll back trade status
//				trade.setStatus(TradeStates.PENDING.toString());
//				this.tradeDao.saveOrUpdateTrade(trade);
				result = 0;
			}
		}
		return result;
	}

	/*
	 * modified by Thangaraj on oct 16, 2014 added by Soujanya on july 23,2014
	 * method to add trade message to DB
	 */
	public Integer addTradeMessageToDB(String message, Integer senderId, Integer receiverId, Integer tradeId,
			Integer level) throws MessageNotSavedOrUpdatedException, TradeNotFoundException, UserNotFoundException {
		log.info("inside addTradeMessageToDB()");
		TradeMessages tradeMessage = new TradeMessages();
		Trades trade = new Trades();
		trade.setTradeId(tradeId);
		TraidItUser sender = new TraidItUser();
		TraidItUser receiver = new TraidItUser();
		sender.setUserId(senderId);
		receiver.setUserId(receiverId);

		// added by Soujanya on november 27,2014
		// reading trade messages from properties file from messagecode sent as
		// input
		TraidItUser loggedInUser = this.userDao.getUserbyUserId(senderId);
		Properties prop = new Properties();
		String tradeMessageFile = "trade_message_codes.properties";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(tradeMessageFile);
		try {
			prop.load(inputStream);
		} catch (IOException e) {
			throw new MessageNotSavedOrUpdatedException();

		}

		String messageFromFile = MessageFormat.format((String) prop.get(message), loggedInUser.getUsername());
		System.out.println(messageFromFile);

		// save the trade message data to database
		tradeMessage.setMessage(messageFromFile);
		tradeMessage.setSender(sender);
		tradeMessage.setReceiver(receiver);
		tradeMessage.setTrade(trade);
		tradeMessage.setIsRead(false);
		tradeMessage.setLevelId(level);
		tradeMessage.setId(this.tradeDao.saveOrUpdateTradeMessage(tradeMessage));
		return tradeMessage.getId();
	}

	/*
	 * added by Soujanya on october 8,2014 method to save vendor rating given by
	 * user to database
	 */
	public Integer saveVendorRating(Integer vendorId, Integer userId, String rating, String comment) throws Exception {
		log.info("inside saveVendorRating()");
		VendorRatings vendorRating = new VendorRatings();
		TraidItUser vendor = this.userDao.getUserbyUserId(vendorId);
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		vendorRating.setVendorId(vendor);
		vendorRating.setUserId(user);
		vendorRating.setComment(comment);
		Ratings ratings = Ratings.valueOf(rating);

		vendorRating.setRating(ratings.getRate());
		Integer result = this.tradeDao.saveOrUpdateVendorRating(vendorRating);
		return result;
	}

	/*
	 * added by Soujanya on october 9,2014 method to get total trade count of
	 * vendor
	 */
	public Integer getTotalTradeCountOfVendor(Integer vendorId) throws Exception {
		log.info("inside getTotalTradeCountOfVendor()");
		TraidItUser vendor = this.userDao.getUserbyUserId(vendorId);
		Integer totalTradesOfVendor = this.tradeDao.getTotalTradeCountOfVendor(vendor);
		return totalTradesOfVendor;
	}

	/*
	 * added by Soujanya on october 9,2014 method to get invidual rating count
	 */
	public Map<String, Integer> getIndividualRatingCountOfVendor(Integer vendorId) throws Exception {
		log.info("inside getIndividualRatingCountOfVendor()");
		TraidItUser vendor = this.userDao.getUserbyUserId(vendorId);
		Map<String, Integer> values = new HashMap<String, Integer>();
		Integer count = 0;
		log.info("for each Ratings, calls {tradeDao.getIndividualRatingCount(vendor, rate)}");
		for (Ratings rate : Ratings.values()) {
			count = this.tradeDao.getIndividualRatingCount(vendor, rate.getRate());

			values.put(rate.toString(), count);
		}
		return values;
	}

	/**
	 * 
	 * @param vendorId
	 * @throws UserNotFoundException
	 * @throws UserNotSavedOrUpdatedException
	 */
	public void updateVendorRatings(Integer vendorId) throws UserNotFoundException, UserNotSavedOrUpdatedException {
		Long sum = this.tradeDao.getSumOfRatingsOfVendor(vendorId);
		Long noOfRatings = this.tradeDao.getTotalRatingsCountOfVendor(vendorId);
		Long avg;
		try {
			avg = sum / noOfRatings;
		} catch (ArithmeticException e) {
			avg = new Long(0);
		}
		TraidItUser vendor = this.userDao.getUserbyUserId(vendorId);
		Integer value = Integer.parseInt(avg.toString());
		vendor.setRating(value);
		value = Integer.parseInt(noOfRatings.toString());
		vendor.setNoOfRatings(value);
		this.userDao.saveOrUpdateUser(vendor);
		sum = new Long(0);
	}

	/**
	 * @author Soujanya
	 * @since october 10,2014 purpose: method to return percentage positive
	 *        rating of vendor calculates positive rating in percentage
	 * @throws UserNotFoundException
	 * @returns percentagePositiveRating
	 */
	public Integer getPercentagePositiveRatingOfVendor(Integer vendorId) throws Exception {
		log.info("inside getPercentagePositiveRatingOfVendor()");
		TraidItUser vendor = this.userDao.getUserbyUserId(vendorId);
		
		Integer totalDelighted = this.tradeDao.getIndividualRatingCount(vendor, 1);
		Integer totalSatisfied = this.tradeDao.getIndividualRatingCount(vendor, 2);
		Integer totalNotSatisfied = this.tradeDao.getIndividualRatingCount(vendor, 4);
		Integer totalAngry = this.tradeDao.getIndividualRatingCount(vendor, 5);
		Integer totalPostiveRatings = totalDelighted + totalSatisfied;
		Integer totalNegativeRatings = totalNotSatisfied + totalAngry;
		Integer sum = totalPostiveRatings + totalNegativeRatings;
		double positiveRatings = (double) totalPostiveRatings / sum;

		int percentagePositiveRating = (int) (positiveRatings * 100);
		return percentagePositiveRating;

	}

	/**
	 * @author Soujanya
	 * @since october 13,2014
	 * @purpose method to determine if a vendor is favourite of user
	 * @inputs vendorid, userid
	 * @throws FavouritesNotFoundException
	 * @return userDto
	 */
	public UserDto getIsFavouriteVendor(UserDto userDto, Integer userId) throws FavouritesNotFoundException {
		log.info("inside getIsFavouriteVendor()");

		try {
			FavouriteVendor favouriteVendor = this.tradeDao.getFavouriteVendorByVendorIdAndUserId(userDto.getUserId(),
					userId);
			if (null != favouriteVendor) {
				userDto.setIsFavouriteVendor(true);
				return userDto;
			} else {
				throw new FavouritesNotFoundException();
			}
		} catch (FavouritesNotFoundException e) {
			userDto.setIsFavouriteVendor(false);
			return userDto;
		}
	}

	/**
	 * @author Soujanya
	 * @since october 13,2014
	 * @purpose method to get count of inventories of a vendor
	 * @throws Exception
	 * @returns count
	 */
	public Integer getInventoryCountOfVendor(Integer vendorId) throws Exception {
		log.info("inside getInventoryCountOfVendor()");
		Integer count = this.inventoryDao.getInventoryCountOfVendor(vendorId);

		return count;
	}

	/**
	 * @author Soujanya
	 * @since 31st october,2014 method to get list of ratings of vendor
	 * @return arraylist of ratings of vendor
	 */
	public ArrayList<VendorRatingsDto> getVendorRatings(Integer vendorId, String rating) throws Exception {
		log.info("inside getVendorRatings()");

		ArrayList<VendorRatings> vendorRatings = this.tradeDao.getVendorRatingsFromDB(vendorId, rating);
		ArrayList<VendorRatingsDto> vendorRatingsDtos = new ArrayList<VendorRatingsDto>();
		for (VendorRatings vendorRating : vendorRatings) {
			VendorRatingsDto vendorRatingsDto = VendorRatingsDto.populateVendorRatingsDto(vendorRating);
			vendorRatingsDtos.add(vendorRatingsDto);
		}
		return vendorRatingsDtos;
	}

	/**
	 * @author Soujanya
	 * @since december 8th,2014 method to get recent list of ratings of vendor
	 * @return arraylist of ratings of vendor
	 */
	public ArrayList<VendorRatingsDto> getRecentVendorRatings(Integer vendorId) throws Exception {
		log.info("inside getRecentVendorRatings()");

		ArrayList<VendorRatings> vendorRatings = this.tradeDao.getRecentVendorFeedbacks(vendorId);
		ArrayList<VendorRatingsDto> vendorRatingsDtos = new ArrayList<VendorRatingsDto>();
		for (VendorRatings vendorRating : vendorRatings) {
			VendorRatingsDto vendorRatingsDto = VendorRatingsDto.populateVendorRatingsDto(vendorRating);
			vendorRatingsDtos.add(vendorRatingsDto);
		}
		return vendorRatingsDtos;
	}

	/**
	 *
	 * @param tradeId
	 * @return
	 * @author Thangaraj
	 * @throws InventoryNotFoundException
	 * @since 03-11-2014
	 */
	public String getCurrentOfferType(Integer tradeId) throws InventoryNotFoundException {
		ArrayList<TradeInventories> inventories = this.tradeDao.getCurrentInventoriesByTraidId(tradeId);
		return inventories.get(0).getOfferType();
	}

	/**
	 * 
	 * @param tradeId
	 * @param userId
	 * @return
	 * @throws InventoryNotFoundException
	 */
	public ArrayList<InventoryDto> getTraderInventoriesOfTrade(Integer tradeId, Integer userId)
			throws InventoryNotFoundException {
		ArrayList<InventoryDto> result = new ArrayList<InventoryDto>();
		ArrayList<TradeInventories> inventories = this.tradeDao.getTraderInventories(tradeId, "TRADER");
		for (TradeInventories tradeInventory : inventories) {
			/*
			 * InventoryDto inventory =
			 * InventoryDto.populateInventoryDto(tradeInventories
			 * .getInventory()); //update quantity with quantity involved in
			 * this trade
			 * inventory.setUnitsAvailable(tradeInventories.getQuantity());
			 */
			InventoryDto inventory = this.populateTradeInventoryData(tradeInventory);
			currentUserCanOffer = tradeInventory.getOfferedBy();
			result.add(inventory);
		}
		return result;
	}

	/**
	 * 
	 * @param tradeId
	 * @param userId
	 * @return
	 * @throws InventoryNotFoundException
	 */
	public ArrayList<InventoryDto> getVendorInventoriesOfTrade(Integer tradeId, Integer userId)
			throws InventoryNotFoundException {
		ArrayList<InventoryDto> result = new ArrayList<InventoryDto>();
		ArrayList<TradeInventories> inventories = this.tradeDao.getTraderInventories(tradeId, "VENDOR");
		for (TradeInventories tradeInventory : inventories) {
			/*
			 * InventoryDto inventory =
			 * InventoryDto.populateInventoryDto(tradeInventories
			 * .getInventory()); //update quantity with quantity involved in
			 * this trade
			 * inventory.setUnitsAvailable(tradeInventories.getQuantity());
			 */
			InventoryDto inventory = this.populateTradeInventoryData(tradeInventory);
			currentUserCanOffer = tradeInventory.getOfferedBy();
			result.add(inventory);
		}
		return result;
	}

	/**
	 * 
	 * @param userId
	 * @param tradeId
	 * @param level
	 * @return
	 * @throws InventoryNotFoundException
	 * @author Thangaraj
	 * @since 24-11-2014
	 */
	public ArrayList<InventoryDto> getSavedTraderInventoriesOfTrade(Integer userId, Integer tradeId, Integer level)
			throws InventoryNotFoundException {
		ArrayList<InventoryDto> result = new ArrayList<InventoryDto>();
		ArrayList<TradeInventories> inventories = this.tradeDao.getTraderSavedInventories(tradeId, level, "TRADER");
		for (TradeInventories tradeInventory : inventories) {
			InventoryDto inventory = this.populateTradeInventoryData(tradeInventory);
			result.add(inventory);
		}
		return result;
	}

	/**
	 * 
	 * @param userId
	 * @param tradeId
	 * @param level
	 * @return
	 * @throws InventoryNotFoundException
	 * @author Thangaraj
	 * @since 24-11-2014
	 */
	public ArrayList<InventoryDto> getSavedVendorInventoriesOfTrade(Integer userId, Integer tradeId, Integer level)
			throws InventoryNotFoundException {
		ArrayList<InventoryDto> result = new ArrayList<InventoryDto>();
		ArrayList<TradeInventories> inventories = this.tradeDao.getTraderSavedInventories(tradeId, level, "VENDOR");
		for (TradeInventories tradeInventory : inventories) {
			InventoryDto inventory = this.populateTradeInventoryData(tradeInventory);
			result.add(inventory);
		}
		try {
			ArrayList<TradeInventories> currentinventories = this.tradeDao.getCurrentInventoriesByTraidId(tradeId);
			currentUserCanOffer = currentinventories.get(0).getOfferedBy();
		} catch (InventoryNotFoundException e) {
			currentUserCanOffer = OfferedBy.VENDOR.toString();
		}
		return result;
	}

	/**
	 * @author Soujanya
	 * @since 6th november,2014 method to unset isRead flag of trade for user
	 */
	public void unsetIsReadTrade(Integer userId, Integer tradeId) throws Exception {
		Trades trade = this.tradeDao.getTradeByTradeId(tradeId);
		if (trade.getTrader().getUserId().equals(userId)) {
			trade.setIsTraderRead(true);
		} else if (trade.getVendor().getUserId().equals(userId)) {
			trade.setIsVendorRead(true);
		}
		this.tradeDao.saveOrUpdateTrade(trade);
	}

	/**
	 * @author Soujanya
	 * @since november 12,2014
	 * 
	 * Modified by Thangaraj : 29-01-2015 : update inventories ;set back to available if trade is withdrawn
	 */
	public Integer completeTrade(Integer tradeId, Integer userId, String message,Boolean complete)
			throws TradeNotSavedOrUpdatedException, TradeNotFoundException {
		log.info("inside completeTrade()");
		Trades trade = this.tradeDao.getTradeByTradeId(tradeId);
		trade.setStatus(TradeStates.COMPLETED.toString());
		Integer result = this.tradeDao.saveOrUpdateTrade(trade);
		if (result > 0) {
			OfferedBy currentUserType = null;
			if (trade.getTrader().getUserId().equals(userId)) {
				currentUserType = OfferedBy.valueOf("TRADER");
			} else if (trade.getVendor().getUserId().equals(userId)) {
				currentUserType = OfferedBy.valueOf("VENDOR");
			}
			try {
				Date offerDate = new Date();
				// current level of trade
				Integer level = this.getCurrentLevelOfTrade(tradeId);

				// first fetch trader and vendor inventories and then unset
				// current flag of trade
				ArrayList<InventoryDto> traderInvenories = getTraderInventoriesOfTrade(tradeId, userId);
				ArrayList<InventoryDto> vendorInvenories = getVendorInventoriesOfTrade(tradeId, userId);
				this.unsetCurrentFlagOfTrade(tradeId);
				
				OfferState offerState;
				if(complete){
					offerState = OfferState.COMPLETE;
				}
				else{
					offerState = OfferState.WITHDRAW;
				}
				// save trader and vendor inventory details
				for (InventoryDto inventoryDto : traderInvenories) {
					createAndAddTradeInventoryToDb(trade, inventoryDto, offerDate, offerState,
							currentUserType, OfferedBy.TRADER, level + 1);
				}
				for (InventoryDto inventoryDto : vendorInvenories) {
					createAndAddTradeInventoryToDb(trade, inventoryDto, offerDate, offerState,
							currentUserType, OfferedBy.VENDOR, level + 1);
				}
				traderInvenories.addAll(vendorInvenories);
				//Thangaraj : 29-01-2015 : if trade is withdrawn, set its inventories to available
				if(!complete){
					this.updateNonTradedInventoryStatus(traderInvenories);
				}
				// update trade message
				this.addTradeMessageToDB(message, trade.getVendor().getUserId(), trade.getTrader().getUserId(),
						tradeId, level + 1);
			} catch (InventoryNotFoundException e) {
				System.out.println("InventoryNotFoundException : no inventories found for given trade");
				// roll back trade status
				trade.setStatus(TradeStates.PENDING.toString());
				this.tradeDao.saveOrUpdateTrade(trade);
				result = 0;
			} catch (Exception e) {
				e.printStackTrace();
				// roll back trade status
				trade.setStatus(TradeStates.PENDING.toString());
				this.tradeDao.saveOrUpdateTrade(trade);
				result = 0;
			}
		}
		return result;
	}

	/**
	 * @author Soujanya method to calculate jiuce per month
	 */
	public Double calculateJiucePerMonth(Integer x) throws Exception {
		Double jiucePerMonth = ((25.74 * x) + (61.776 * (Math.pow(x, 2))));
		System.out.println(jiucePerMonth + "month");
		return jiucePerMonth;

	}

	/**
	 * @author Soujanya method to calculate jiuce per year
	 */
	public Double calculateJiucePerYear(Integer x) throws Exception {
		Double jiucePerMonth = this.calculateJiucePerMonth(x);
		Double jiucePerYear = jiucePerMonth * 12;
		System.out.println(jiucePerYear + "year");
		return jiucePerYear;
	}

	/**
	 * @author Thangaraj
	 * @since 13-11-2014
	 */
	public ArrayList<TradesDto> getTradeHistoryList(Integer userId) throws TradeNotFoundException, Exception {
		ArrayList<Trades> trades = this.tradeDao.getTradeHistoryList(userId);
		ArrayList<TradesDto> tradeDtos = new ArrayList<TradesDto>();
		for (Trades trade : trades) {
			TradesDto tradeDto = TradesDto.populateTradesDto(trade);
			TradeInventories tradeFromInventory = this.tradeDao.getInventoryByTradeId(tradeDto.getTradeId(),
					OfferedBy.TRADER.toString(), false);
			InventoryDto fromInventory = this.populateTradeInventoryData(tradeFromInventory);
			if (trade.getTrader().getUserId().equals(userId)) {
				tradeDto.setCurrentUserIs(OfferedBy.TRADER);
				if (tradeFromInventory.getOfferedBy().equals(OfferedBy.TRADER.toString()))
					tradeDto.setIsCurrentUserCanOffer(true);
				try {
					FavouriteInventory favouriteInventory = this.inventoryDao
							.getFavouriteInventoryByInventoryIdAndUserId(fromInventory.getInventoryId(), userId);
					if (null != favouriteInventory) {
						fromInventory.setIsFavouriteInventory(true);
					} else {
						throw new FavouritesNotFoundException();
					}
				} catch (FavouritesNotFoundException e) {
					fromInventory.setIsFavouriteInventory(false);
				}
			}
			TradeInventories tradeToInventory = this.tradeDao.getInventoryByTradeId(tradeDto.getTradeId(),
					OfferedBy.VENDOR.toString(), false);
			InventoryDto toInventory = this.populateTradeInventoryData(tradeToInventory);
			if (trade.getVendor().getUserId().equals(userId)) {
				tradeDto.setCurrentUserIs(OfferedBy.VENDOR);
				if (tradeFromInventory.getOfferedBy().equals(OfferedBy.VENDOR.toString()))
					tradeDto.setIsCurrentUserCanOffer(true);
				try {
					FavouriteInventory favouriteInventory = this.inventoryDao
							.getFavouriteInventoryByInventoryIdAndUserId(toInventory.getInventoryId(), userId);
					if (null != favouriteInventory) {
						toInventory.setIsFavouriteInventory(true);
					} else {
						throw new FavouritesNotFoundException();
					}
				} catch (FavouritesNotFoundException e) {
					toInventory.setIsFavouriteInventory(false);
				}
			}
			tradeDto.setTradeFromInventories(fromInventory);
			tradeDto.setTradeToInventories(toInventory);
			tradeDtos.add(tradeDto);
		}
		return tradeDtos;
	}

	public TradesDto getTradeHistory(Integer tradeId, Integer userId) throws TradeNotFoundException,
			TradeInventoryNotFoundException, InventoryNotFoundException {
		Trades trade = this.tradeDao.getTradeByTradeId(tradeId);
		TradesDto tradeDto = TradesDto.populateTradesDto(trade);
		ArrayList<OffersDto> offers = new ArrayList<OffersDto>();
		Map<Integer, OffersDto> offersMap = new HashMap<Integer, OffersDto>();
		Integer maxLevel = this.getCurrentLevelOfTrade(tradeId);
		for (int i = 1; i <= maxLevel; i++) {
			offersMap.put(i, new OffersDto());
		}
		ArrayList<TradeInventories> inventories = this.tradeDao.getAllInventoriesOfTrade(tradeId);
		for (TradeInventories inventory : inventories) {
			OffersDto offer = offersMap.get(inventory.getLevel());
			if(offer == null)
				continue;
			if (inventory.getInventoryOf().equals("TRADER")) {
				InventoryDto inventoryDto = this.populateTradeInventoryData(inventory);
				offer.getTraderInventories().add(inventoryDto);
			} else {
				InventoryDto inventoryDto = this.populateTradeInventoryData(inventory);
				offer.getVendorInventories().add(inventoryDto);
			}
			offer.setLevel(inventory.getLevel());
			offer.setOfferedBy(OfferedBy.valueOf(inventory.getOfferedBy()));
			offer.setOfferState(OfferState.valueOf(inventory.getOfferType()));
			if(offer.getOfferState().equals(OfferState.SAVED) && 
					!(offer.getOfferedBy().equals(OfferedBy.TRADER) && trade.getTrader().getUserId().equals(userId) 
							|| offer.getOfferedBy().equals(OfferedBy.VENDOR) && trade.getVendor().getUserId().equals(userId)))
				offersMap.remove(inventory.getLevel());
			else
				offersMap.put(inventory.getLevel(), offer);
		}
		offers.addAll(offersMap.values());
		OfferedBy currentUserIs = tradeDto.getTrader().getUserId().equals(userId) ? OfferedBy.TRADER : OfferedBy.VENDOR;
		tradeDto.setCurrentUserIs(currentUserIs);
		tradeDto.setIsCurrentUserCanOffer(!currentUserIs.equals(offersMap.get(maxLevel).getOfferedBy()));
		tradeDto.setOffers(offers);
		return tradeDto;
	}

	/**
	 * method to populate counts of trades by status, user message count and
	 * favourites count
	 * 
	 * @author Soujanya
	 * @since nov 17,2014
	 * 
	 *        modified by Thangaraj on 06-01-2015 , moved new trades count to
	 *        individual function
	 * 
	 */
	public Map<String, Integer> populateCountsForSummary(Integer userId) throws Exception {
		log.info("inside populateCountsForSummary()");

		Integer userMessageCount 			= this.userDao.getTotalUserMessageCount(userId, "unread");
		Integer pendingTradesCount 			= this.tradeDao.getCountOfOffersByStatus(userId, TradeStates.valueOf("PENDING"));
		Integer savedTradesCount 			= this.tradeDao.getCountOfSavedTrades(userId);
		Integer newTradesCount 				= this.getNewTradesCountOfUser(userId);
		Integer favVendors 					= this.userDao.getCountOfFavVendorsOfUser(userId);
		Integer favInventories 				= this.inventoryDao.getCountOfFavInventoriesOfUser(userId);
		Integer favCategories 				= this.categoryDao.getCountOfFavCategoriesOfUser(userId);
		Integer totalFavourites 			= favCategories + favInventories + favVendors;
		Integer tradeHistoryCount 			= this.tradeDao.getCountOfTradesOfTradeHistory(userId);
		Integer totalNoOfCompletedTrades 	= this.tradeDao.getTotalNoOfCompletedTrades(userId);
		
		log.info("tradehistoryCount : "+tradeHistoryCount);
		Map<String, Integer> countsForSummary = new HashMap<String, Integer>();

		countsForSummary.put("totalUserMessages", userMessageCount);
		countsForSummary.put("totalPendingOffers", pendingTradesCount);
		countsForSummary.put("totalNewOffers", newTradesCount);
		countsForSummary.put("totalFavourites", totalFavourites);
		countsForSummary.put("totalHistoryTrades", tradeHistoryCount);
		countsForSummary.put("totalSavedTrades", savedTradesCount);
		countsForSummary.put("totalnooftrades", totalNoOfCompletedTrades);
		
		log.info("countsForSummary : "+countsForSummary);
		return countsForSummary;

	}

	/**
	 * method to delete saved trade inventories
	 * 
	 * @author Soujanya
	 * @since nov 26,2014
	 */
	public void deleteSavedTradeInventories(Integer tradeId, Integer level) throws Exception {
		log.info("inside deleteSavedTrade()");
		// gets list of trade inventories by tradeid and level
		ArrayList<TradeInventories> tradeInventories = this.tradeDao.getInventoriesOfSavedTradeByLevel(tradeId, level);

		if (!tradeInventories.isEmpty()) {
			for (TradeInventories tradeInventory : tradeInventories) {
				this.tradeDao.deleteSavedTradeInventory(tradeInventory);
			}
		} else {
			throw new TradeInventoryNotFoundException();
		}
	}

	/**
	 * method to save a favourite trade
	 * 
	 * @author Soujanya
	 * @since dec 24th,2014
	 */
	public Integer saveFavouriteTrade(Integer userId, Integer tradeId) throws Exception {
		log.info("inside saveFavouriteTrade()");
		TraidItUser user = this.userDao.getUserbyUserId(userId);
		Trades trade = this.tradeDao.getTradeByTradeId(tradeId);
		FavouriteTrades favouriteTrade = new FavouriteTrades();
		favouriteTrade.setUserId(user);
		favouriteTrade.setTradeId(trade);
		Integer result = this.tradeDao.saveFavouriteTrade(favouriteTrade);
		return result;
	}

	/**
	 * method to get list of fav trades of user
	 * 
	 * @author Soujanya
	 * @since dec 24th,2014
	 */
	public ArrayList<FavouriteTradesDto> getAllFavouriteTradesOfUser(Integer userId) throws Exception {
		log.info("inside getAllFavouriteTradesOfUser()");
		ArrayList<FavouriteTrades> favouriteTrades = this.tradeDao.getAllFavouriteTradesOfUser(userId);
		Boolean listSavedOnly = false;
		ArrayList<FavouriteTradesDto> favTradeDtos = new ArrayList<FavouriteTradesDto>();
		ArrayList<Integer> favInventories = new ArrayList<Integer>();
		for (FavouriteInventory fav : this.inventoryDao.getFavouriteInventoriesOfUser(userId, false, false, 0)) {
			favInventories.add(fav.getInventory().getInventoryId());
		}
		for (FavouriteTrades favouriteTrade : favouriteTrades) {
			FavouriteTradesDto favTradeDto = FavouriteTradesDto.populateFavouriteTradesDto(favouriteTrade);
			TradeInventories tradeFromInventory = this.tradeDao.getInventoryByTradeId(favTradeDto.getTradeId()
					.getTradeId(), OfferedBy.TRADER.toString(), listSavedOnly);
			InventoryDto fromInventory = this.populateTradeInventoryData(tradeFromInventory);
			if (favouriteTrade.getTradeId().getTrader().getUserId().equals(userId)
					&& favInventories.contains(fromInventory.getInventoryId())) {
				fromInventory.setIsFavouriteInventory(true);
			} else {
				fromInventory.setIsFavouriteInventory(false);
			}
			favTradeDto.setTradeFromInventories(fromInventory);
			TradeInventories tradeToInventory = this.tradeDao.getInventoryByTradeId(favTradeDto.getTradeId()
					.getTradeId(), OfferedBy.VENDOR.toString(), listSavedOnly);
			InventoryDto toInventory = this.populateTradeInventoryData(tradeToInventory);
			if (favouriteTrade.getTradeId().getVendor().getUserId().equals(userId)
					&& favInventories.contains(fromInventory.getInventoryId())) {
				fromInventory.setIsFavouriteInventory(true);
			} else {
				fromInventory.setIsFavouriteInventory(false);
			}
			favTradeDto.setTradeToInventories(toInventory);
			favTradeDto.setCurrentLevel(tradeToInventory.getLevel());

			// Integer
			// in=this.inventoryService.getInventoryCount(favouriteCategory.getCategory().getCategoryId(),userId,
			// favVendor,true);
			// System.out.println(in+"count");
			// favTradeDto.setTotalNoOfInventories(this.inventoryService.getInventoryCount(favouriteCategory.getCategory().getCategoryId(),userId,
			// favVendor,true));
			favTradeDtos.add(favTradeDto);
		}
		return favTradeDtos;
	}

	/**
	 * method to delete favourite trade from favourites
	 * 
	 * @author Soujanya
	 * @since dec 24,2014
	 */
	public void deleteFavouriteTrade(Integer tradeId, Integer userId) throws FavouritesNotFoundException {
		log.info("inside deleteFavouriteTrade()");
		FavouriteTrades favouriteTrade = this.tradeDao.getFavouriteTradeByTradeAndUser(tradeId, userId);
		this.tradeDao.deleteFavouriteTradeFromDB(favouriteTrade);
	}

	/**
	 * 
	 * @param userId
	 * @return
	 * @return Integer
	 * @author Thangaraj(KNSTEK)
	 * @since 06-Jan-2015
	 */
	public Integer getNewTradesCountOfUser(Integer userId) {

		Integer newTradesCount = 0;
		try {
			ArrayList<Trades> unFilteredTrades = this.tradeDao.getNewTrades(TradeStates.PENDING, userId);
			ArrayList<Trades> filteredNewTrades = this.filterNewTrades(unFilteredTrades, userId);
			newTradesCount = filteredNewTrades.size();
		} catch (TradeNotFoundException e) {
			newTradesCount = 0;
		}
		return newTradesCount;
	}

	/**
	 * 
	 * @param userId
	 * @param vendorId
	 * @return Boolean
	 * @author Thangaraj(KNSTEK)
	 * @since 29-Jan-2015
	 */
	@Override
	public Boolean isUserHasTradedWithVendor(Integer userId,Integer vendorId){
		return this.tradeDao.isUserHasTradedWithVendor(userId, vendorId);
	}
	
}
