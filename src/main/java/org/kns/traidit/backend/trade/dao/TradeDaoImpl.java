package org.kns.traidit.backend.trade.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.kns.traidit.backend.inventory.exception.InventoryNotFoundException;
import org.kns.traidit.backend.item.model.TraidItItems;
import org.kns.traidit.backend.trade.exception.MessageNotFoundException;
import org.kns.traidit.backend.trade.exception.MessageNotSavedOrUpdatedException;
import org.kns.traidit.backend.trade.exception.SavedTradeNotSavedOrUpdatedException;
import org.kns.traidit.backend.trade.exception.TradeInventoryNotFoundException;
import org.kns.traidit.backend.trade.exception.TradeNotFoundException;
import org.kns.traidit.backend.trade.exception.TradeNotSavedOrUpdatedException;
import org.kns.traidit.backend.trade.model.OfferState;
import org.kns.traidit.backend.trade.model.OfferedBy;
import org.kns.traidit.backend.trade.model.Ratings;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
 * created by Soujanya on 20thth June,2014
 * DAO class for trade related activities
 */
@Repository("tradeDao")
@Transactional
public class TradeDaoImpl implements TradeDao {

	@Autowired
	private SessionFactory sessionFactory;

	public static Integer totalMessages;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private static Logger log = Logger.getLogger(TradeDaoImpl.class);

	/*-----------------------------------------------------------------------------------------------*/
	/*
	 * added by Soujanya on June 20,2014 method to save and update trade
	 */
	public Integer saveOrUpdateTrade(Trades trade) throws TradeNotSavedOrUpdatedException {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(trade);
			sessionFactory.getCurrentSession().flush();
			return trade.getTradeId();
		} catch (Exception e) {
			throw new TradeNotSavedOrUpdatedException();
		}

	}

	/*
	 * added by Soujanya on June 20,2014 method to delete Trade
	 */
	public void deleteTrade(Trades trade) throws TradeNotFoundException {
		sessionFactory.getCurrentSession().delete(trade);
		sessionFactory.getCurrentSession().flush();
	}

	/*
	 * added by Soujanya on June 20,2014 method to retrieve trade by tradeId
	 */
	@SuppressWarnings("unchecked")
	public Trades getTradeByTradeId(Integer tradeId) throws TradeNotFoundException {
		ArrayList<Trades> trade = (ArrayList<Trades>) sessionFactory.getCurrentSession().createCriteria(Trades.class)
				.add(Restrictions.eq("tradeId", tradeId)).list();

		if (!trade.isEmpty()) {
			return trade.get(0);
		} else {
			throw new TradeNotFoundException();
		}
	}

	/*
	 * added by Soujanya on June 20,2014 method to retrieve trade by item id
	 */
	@SuppressWarnings("unchecked")
	public Trades getTradeByItemId(TraidItItems itemId) throws TradeNotFoundException {
		log.info("inside getTradeByItemId()");
		ArrayList<Trades> trade = (ArrayList<Trades>) sessionFactory.getCurrentSession().createCriteria(Trades.class)
				.add(Restrictions.eq("itemId", itemId)).list();

		if (!trade.isEmpty()) {
			return trade.get(0);
		} else {
			throw new TradeNotFoundException();
		}

	}

	/*
	 * added by Soujanya on June 20,2014 method to retrieve trade by tradeId
	 */
	@SuppressWarnings("unchecked")
	public Trades getTradeByTraderId(TraidItUser traderId) throws TradeNotFoundException {
		log.info("inside getTradeByTraderId()");
		ArrayList<Trades> trade = (ArrayList<Trades>) sessionFactory.getCurrentSession().createCriteria(Trades.class)
				.add(Restrictions.eq("traderId", traderId)).list();

		if (!trade.isEmpty()) {
			return trade.get(0);
		} else {
			throw new TradeNotFoundException();
		}
	}

	/*
	 * added by Soujanya on june 20,2014 method to retrieve trade by vendor id
	 */
	@SuppressWarnings("unchecked")
	public Trades getTradeByVendorId(TraidItUser vendorId) throws TradeNotFoundException {
		log.info("inside getTradeByVendorId()");
		ArrayList<Trades> trade = (ArrayList<Trades>) sessionFactory.getCurrentSession().createCriteria(Trades.class)
				.add(Restrictions.eq("vendorId", vendorId)).list();

		if (!trade.isEmpty()) {
			return trade.get(0);
		} else {
			throw new TradeNotFoundException();
		}
	}

	/*
	 * 
	 * method to retrieve trade offer id
	 * 
	 * @SuppressWarnings("unchecked") public Trades getTradeByOfferId(ItemOffers
	 * offerId) throws TradeNotFoundException{
	 * log.info("inside getTradeByOfferId()"); ArrayList<Trades> trade =
	 * (ArrayList<Trades>)
	 * sessionFactory.getCurrentSession().createCriteria(Trades.class)
	 * .add(Restrictions.eq("offerId", offerId)) .list();
	 * 
	 * if(!trade.isEmpty()){ return trade.get(0); } else{ throw new
	 * TradeNotFoundException(); } }
	 */

	/*
	 * method to retrieve trade by date
	 */
	@SuppressWarnings("unchecked")
	public Trades getTradeByDate(Date tradeDate) throws TradeNotFoundException {
		log.info("inside getTradeByOfferId()");
		ArrayList<Trades> trade = (ArrayList<Trades>) sessionFactory.getCurrentSession().createCriteria(Trades.class)
				.add(Restrictions.eq("tradeDate", tradeDate)).list();

		if (!trade.isEmpty()) {
			return trade.get(0);
		} else {
			throw new TradeNotFoundException();
		}
	}

	/*
	 * method to save or update saved tardes
	 */
	public Integer saveOrUpdateSavedTrade(SavedTrades trade) throws SavedTradeNotSavedOrUpdatedException {
		sessionFactory.getCurrentSession().saveOrUpdate(trade);
		sessionFactory.getCurrentSession().flush();
		return trade.getId();
	}

	/*
	 * added by Soujanya on october 14,2014 method to save or update trade
	 * inventories
	 */
	public Integer saveOrUpdateTradeInventories(TradeInventories tradeInventories)
			throws TradeNotSavedOrUpdatedException {
		sessionFactory.getCurrentSession().saveOrUpdate(tradeInventories);
		sessionFactory.getCurrentSession().flush();
		return tradeInventories.getId();
	}

	/*
	 * method to delete saved tarde
	 */
	public void deleteSavedTradeInventory(TradeInventories tradeInventory) throws TradeInventoryNotFoundException {
		sessionFactory.getCurrentSession().delete(tradeInventory);
		sessionFactory.getCurrentSession().flush();
	}

	/*
	 * method to retrieve saved trade by id
	 */
	@SuppressWarnings("unchecked")
	public SavedTrades getSavedTradeById(Integer id) throws TradeNotFoundException {
		log.info("inside getSavedTradeById()");
		ArrayList<SavedTrades> trade = (ArrayList<SavedTrades>) sessionFactory.getCurrentSession()
				.createCriteria(SavedTrades.class).add(Restrictions.eq("id", id)).list();

		if (!trade.isEmpty()) {
			return trade.get(0);
		} else {
			throw new TradeNotFoundException();
		}
	}

	/*
	 * method to retrieve saved trade by trade id
	 */
	@SuppressWarnings("unchecked")
	public SavedTrades getSavedTradeByTradeId(Trades tradeId) throws TradeNotFoundException {
		log.info("inside getSavedTradeByTradeId()");
		ArrayList<SavedTrades> trade = (ArrayList<SavedTrades>) sessionFactory.getCurrentSession()
				.createCriteria(SavedTrades.class).add(Restrictions.eq("tradeId", tradeId)).list();

		if (!trade.isEmpty()) {
			return trade.get(0);
		} else {
			throw new TradeNotFoundException();
		}
	}

	/*
	 * method to retrieve saved trade by user id
	 */
	@SuppressWarnings("unchecked")
	public SavedTrades getSavedTradeByUserId(TraidItUser userId) throws TradeNotFoundException {
		log.info("inside getSavedTradeByUserId()");
		ArrayList<SavedTrades> trade = (ArrayList<SavedTrades>) sessionFactory.getCurrentSession()
				.createCriteria(SavedTrades.class).add(Restrictions.eq("userId", userId)).list();

		if (!trade.isEmpty()) {
			return trade.get(0);
		} else {
			throw new TradeNotFoundException();
		}
	}

	/*
	 * method to save or update trade message
	 */
	public Integer saveOrUpdateTradeMessage(TradeMessages message) throws MessageNotSavedOrUpdatedException {
		sessionFactory.getCurrentSession().saveOrUpdate(message);
		sessionFactory.getCurrentSession().flush();
		return message.getId();
	}

	/*
	 * added by Soujanya on october 9,2014 method to save or update vendor
	 * ratings given by user
	 */
	public Integer saveOrUpdateVendorRating(VendorRatings vendorRating) throws Exception {
		log.info("inside saveOrUpdateVendorRating()");
		sessionFactory.getCurrentSession().saveOrUpdate(vendorRating);
		sessionFactory.getCurrentSession().flush();
		return vendorRating.getId();
	}

	/*
	 * method to delete trade message
	 */
	public void deleteTradeMessage(String message) throws MessageNotFoundException {
		sessionFactory.getCurrentSession().delete(message);
		sessionFactory.getCurrentSession().flush();
	}

	/*
	 * method to get all trade messages
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TradeMessages> getAllTradeMessages(Integer pageNo, Integer pageSize, Integer receiverId)
			throws MessageNotFoundException {
		log.info("inside getAllTradeMessages()");
		ArrayList<TradeMessages> messages = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TradeMessages.class);

		criteria.add(Restrictions.eq("receiver.userId", receiverId));
		criteria.addOrder(Order.desc("id"));
		// totalMessages=criteria.list().size();
		if (null != pageNo && null != pageSize) {
			criteria.setFirstResult(pageNo * pageSize);
			criteria.setMaxResults(pageSize);
		}

		messages = (ArrayList<TradeMessages>) criteria.list();
		if (!messages.isEmpty()) {
			return messages;

		} else {
			throw new MessageNotFoundException();
		}
	}

	/*
	 * added by Soujanya on july 23,2014 method to get trades based on criteria
	 * as in status of trade i.e,pending,accepted or rejected
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Trades> getTradesByTradeCriteria(TradeStates status, Integer traderId)
			throws TradeNotFoundException {
		log.info("inside getTradesByCriteria()");
		ArrayList<Trades> trades = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Trades.class);

		criteria.add(Restrictions.eq("status", status.toString()));
		criteria.add(Restrictions.or(Restrictions.eq("trader.userId", traderId),
				Restrictions.eq("vendor.userId", traderId)));
		criteria.addOrder(Order.desc("tradeId"));
		trades = (ArrayList<Trades>) criteria.list();
		if (!trades.isEmpty()) {
			return trades;
		} else {
			throw new TradeNotFoundException();
		}
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Trades> getNewTrades(TradeStates status, Integer traderId) throws TradeNotFoundException {
		log.info("inside getNewTrades()");
		ArrayList<Trades> trades = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Trades.class);

		criteria.add(Restrictions.eq("status", status.toString()));
		criteria.add(Restrictions.or(Restrictions.eq("trader.userId", traderId),
				Restrictions.eq("vendor.userId", traderId)));
		criteria.addOrder(Order.desc("tradeId"));
		trades = (ArrayList<Trades>) criteria.list();
		if (!trades.isEmpty()) {
			return trades;
		} else {
			throw new TradeNotFoundException();
		}
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Trades> getTradesByInventoryCriteria(OfferState status, Integer traderId)
			throws TradeNotFoundException {
		log.info("inside getTradesByCriteria()");
		ArrayList<Trades> trades = null;

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Trades.class);
		criteria.add(Restrictions.or(Restrictions.eq("trader.userId", traderId),
				Restrictions.eq("vendor.userId", traderId)));

		DetachedCriteria subquery = DetachedCriteria.forClass(TradeInventories.class);
		subquery.add(Restrictions.eq("offerType", status.toString()));
		subquery.add(Restrictions.eq("isCurrent", true));
		subquery.setProjection(Projections.property("trade.tradeId"));
		criteria.add(Subqueries.propertyIn("tradeId", subquery));
		criteria.addOrder(Order.desc("tradeId"));
		trades = (ArrayList<Trades>) criteria.list();
		if (!trades.isEmpty()) {
			return trades;
		} else {
			throw new TradeNotFoundException();
		}
	}

	/**
	 * 
	 */
	public List<Object[]> getSavedTradesByUserId(Integer userId) throws TradeNotFoundException {
		log.info("inside getSavedTradesByUserId()");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TradeInventories.class);
		criteria.createAlias("trade", "t");
		criteria.add(Restrictions.or(Restrictions.eq("t.trader.userId", userId),
				Restrictions.eq("t.vendor.userId", userId)));
		criteria.add(Restrictions.eq("offerType", "SAVED"));
		criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("t.tradeId"))
				.add(Projections.groupProperty("level")).add(Projections.property("trade"))
				.add(Projections.property("offeredBy")));
		@SuppressWarnings("unchecked")
		List<Object[]> projTrades = criteria.list();
		List<Object[]> savedTrades = new ArrayList<Object[]>();
		for (Object[] projTrade : projTrades) {
			if (null != projTrade) {
				Trades trade = (Trades) projTrade[2];
				if ((trade.getTrader().getUserId().equals(userId) && projTrade[3].equals(OfferedBy.TRADER.toString()))
						|| (trade.getVendor().getUserId().equals(userId) && projTrade[3].equals(OfferedBy.VENDOR
								.toString()))) {
					Object[] object = new Object[2];
					object[0] = trade;
					object[1] = projTrade[1];
					savedTrades.add(object);
				}
			}
		}
		if (!savedTrades.isEmpty()) {
			return savedTrades;
		} else {
			throw new TradeNotFoundException();
		}

	}

	/*
	 * method to get trade message by id
	 */
	@SuppressWarnings("unchecked")
	public TradeMessages getTradeMessageById(Integer id) throws MessageNotFoundException {
		log.info("inside getTradeMessageById()");

		ArrayList<TradeMessages> message = (ArrayList<TradeMessages>) sessionFactory.getCurrentSession()
				.createCriteria(TradeMessages.class).add(Restrictions.eq("id", id)).list();

		if (!message.isEmpty()) {
			return message.get(0);
		} else {
			throw new MessageNotFoundException();
		}
	}

	/*
	 * method to get trade message by trade Id
	 */
	@SuppressWarnings("unchecked")
	public TradeMessages getTradeMessageByTradeId(Trades tradeId) throws MessageNotFoundException {
		log.info("inside getTradeMessageByTradeId()");

		ArrayList<TradeMessages> message = (ArrayList<TradeMessages>) sessionFactory.getCurrentSession()
				.createCriteria(TradeMessages.class).add(Restrictions.eq("tradeId", tradeId)).list();

		if (!message.isEmpty()) {
			return message.get(0);
		} else {
			throw new MessageNotFoundException();
		}
	}

	/*
	 * method to get trade message by sender id
	 */
	@SuppressWarnings("unchecked")
	public TradeMessages getTradeMessageBySender(TraidItUser sender) throws MessageNotFoundException {
		log.info("inside getTradeMessageBySender()");

		ArrayList<TradeMessages> message = (ArrayList<TradeMessages>) sessionFactory.getCurrentSession()
				.createCriteria(TradeMessages.class).add(Restrictions.eq("sender.userId", sender)).list();

		if (!message.isEmpty()) {
			return message.get(0);
		} else {
			throw new MessageNotFoundException();
		}
	}

	/*
	 * method to get trade message by receiver id
	 */
	@SuppressWarnings("unchecked")
	public TradeMessages getTradeMessageByReceiver(TraidItUser receiver) throws MessageNotFoundException {
		log.info("inside getTradeMessageByReceiver()");

		ArrayList<TradeMessages> message = (ArrayList<TradeMessages>) sessionFactory.getCurrentSession()
				.createCriteria(TradeMessages.class).add(Restrictions.eq("receiver.userId", receiver)).list();

		if (!message.isEmpty()) {
			return message.get(0);
		} else {
			throw new MessageNotFoundException();
		}
	}

	/*
	 * added by Soujanya on october 9,2014 method to get count of total trades
	 * done by a vendor
	 */
	@SuppressWarnings("unchecked")
	public Integer getTotalTradeCountOfVendor(TraidItUser vendorId) throws UserNotFoundException {
		log.info("inside getTotalTradeCountOfVendor()");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Trades.class);
		criteria.add(Restrictions.eq("vendorId", vendorId));
		List<Long> temp = new ArrayList<Long>();
		try {
			temp = (List<Long>) criteria.setProjection(Projections.count("tradeId")).list();
		} catch (Exception e) {
			return 0;
		}
		Integer count = Integer.parseInt(temp.get(0).toString());
		return count;
	}

	/*
	 * added by Soujanya on october 9,2014 method to get count of total feedback
	 * ratings got by vendor
	 */
	@SuppressWarnings("unchecked")
	public Long getTotalRatingsCountOfVendor(Integer vendorId) throws UserNotFoundException {
		log.info("inside getTotalRatingsCountOfVendor()");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VendorRatings.class);
		criteria.add(Restrictions.eq("vendorId.userId", vendorId));
		List<Long> temp = new ArrayList<Long>();
		try {
			temp = (List<Long>) criteria.setProjection(Projections.count("id")).list();
		} catch (Exception e) {
			return new Long(0);
		}
		return temp.get(0);
	}

	/**
	 * 
	 * @param vendorId
	 * @return
	 * @author Thangaraj
	 * @since 09-10-2014
	 */
	public Long getSumOfRatingsOfVendor(Integer vendorId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VendorRatings.class);
		criteria.createAlias("vendorId", "vendor");
		criteria.add(Restrictions.eq("vendor.userId", vendorId));
		criteria.setProjection(Projections.sum("rating"));
		Long sum;
		if (null != criteria.list().get(0))
			sum = (Long) criteria.list().get(0);
		else
			sum = new Long(0);
		return sum;
	}

	/*
	 * added by Soujanya on october 9,2014 method to get individual rating count
	 * of vendor
	 */
	public Integer getIndividualRatingCount(TraidItUser vendorId, Integer rating) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VendorRatings.class);
		criteria.add(Restrictions.eq("vendorId", vendorId));
		criteria.add(Restrictions.eq("rating", rating));
		criteria.setProjection(Projections.count("id"));
		Integer count = Integer.parseInt(criteria.list().get(0).toString());
		return count;
	}

	/**
	 * method to get Favourite Vendor by Vendor Id and User Id
	 * 
	 * @author kns01 Soujanya
	 * @since october 13,2014
	 * 
	 */
	@SuppressWarnings("unchecked")
	public FavouriteVendor getFavouriteVendorByVendorIdAndUserId(Integer vendorId, Integer userId)
			throws FavouritesNotFoundException {
		log.info("inside getFavouriteVendorByVendorIdAndUserId()");
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(FavouriteVendor.class)
				.add(Restrictions.eq("userId.userId", userId)).add(Restrictions.eq("vendorId.userId", vendorId))
				.addOrder(Order.asc("id"));
		ArrayList<FavouriteVendor> favouriteVendors = (ArrayList<FavouriteVendor>) criteria.list();
		if (!favouriteVendors.isEmpty()) {
			return favouriteVendors.get(0);
		} else {
			throw new FavouritesNotFoundException();
		}

	}

	/**
	 * method to get Trade count of user by status of trade
	 * 
	 * @author Soujanya
	 * @since october 16,2014
	 * 
	 */
	public Integer getTradeCountByStatus(Integer userId, String status) throws TradeNotFoundException {
		log.info("inside getTradeCountByStatus()");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Trades.class);
		criteria.add(Restrictions.eq("userId.userId", userId));
		criteria.add(Restrictions.eq("status", status));
		criteria.setProjection(Projections.count("tradeId"));
		Integer count = Integer.parseInt(criteria.list().get(0).toString());

		return count;
	}

	/**
	 * 
	 * @param tradeId
	 * @return
	 * @throws InventoryNotFoundException
	 */
	public Integer getCurrentLevelOfTrade(Integer tradeId) throws InventoryNotFoundException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TradeInventories.class);
		criteria.add(Restrictions.eq("trade.tradeId", tradeId));
		criteria.setProjection(Projections.max("level"));
		if (criteria.list().isEmpty())
			throw new InventoryNotFoundException();
		return (Integer) criteria.list().get(0);
	}

	/**
	 * 
	 */
	public void unsetCurrentFlagOfTrade(Integer tradeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TradeInventories.class);
		criteria.add(Restrictions.eq("trade.tradeId", tradeId));
		criteria.add(Restrictions.eq("isCurrent", true));
		ScrollableResults inventories = criteria.scroll();
		while (inventories.next()) {
			TradeInventories inventory = (TradeInventories) inventories.get(0);
			inventory.setIsCurrent(false);
			sessionFactory.getCurrentSession().saveOrUpdate(inventory);
		}
		sessionFactory.getCurrentSession().flush();
	}

	/*
	 * added by Soujanya method to get list of vendor ratings
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<VendorRatings> getVendorRatingsFromDB(Integer vendorId, String rating) throws Exception {
		log.info("inside getVendorRatingsFromDB()");
		ArrayList<VendorRatings> vendorRatings = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VendorRatings.class);
		criteria.add(Restrictions.eq("vendorId.userId", vendorId));

		if (!rating.equals("all")) {
			Ratings ratings = Ratings.valueOf(rating);
			criteria.add(Restrictions.eq("rating", ratings.getRate()));
		}
		criteria.addOrder(Order.desc("id"));

		vendorRatings = (ArrayList<VendorRatings>) criteria.list();
		if (!vendorRatings.isEmpty()) {
			return vendorRatings;
		} else {
			throw new Exception();
		}

	}

	/**
	 * method to get the most recent feedbacks of user restricted to 10 added by
	 * Soujanya on december 7,2014
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<VendorRatings> getRecentVendorFeedbacks(Integer vendorId) throws Exception {
		log.info("inside getRecentVendorFeedbacks()");
		ArrayList<VendorRatings> vendorRatings = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VendorRatings.class);
		criteria.add(Restrictions.eq("vendorId.userId", vendorId));
		criteria.setFirstResult(1);
		criteria.setMaxResults(10);
		criteria.addOrder(Order.desc("id"));
		vendorRatings = (ArrayList<VendorRatings>) criteria.list();
		if (!vendorRatings.isEmpty()) {
			return vendorRatings;
		} else {
			throw new Exception();
		}
	}

	/**
	 * @author Soujanya
	 * @since 3rd november,2014 method to get inventory by trade returns info
	 *        about inventory involved in trade
	 */
	@SuppressWarnings("unchecked")
	public TradeInventories getInventoryByTradeId(Integer tradeId, String inventoryOf, Boolean listSavedOnly)
			throws TradeInventoryNotFoundException {
		log.info("inside getInventoryByTradeId()");
		System.out.println(tradeId);
		System.out.println(inventoryOf);
		System.out.println(listSavedOnly);
		ArrayList<TradeInventories> tradeInventories = new ArrayList<TradeInventories>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TradeInventories.class);
		System.out.println(tradeId);
		criteria.add(Restrictions.eq("trade.tradeId", tradeId));
		System.out.println(inventoryOf);
		criteria.add(Restrictions.eq("inventoryOf", inventoryOf));
		if (listSavedOnly)
			criteria.add(Restrictions.eq("offerType", OfferState.SAVED.toString()));
		else
			criteria.add(Restrictions.eq("isCurrent", true));
		tradeInventories = (ArrayList<TradeInventories>) criteria.list();
		System.out.println(tradeInventories + "printed");
		if (!tradeInventories.isEmpty()) {
			return tradeInventories.get(0);
		} else {
			throw new TradeInventoryNotFoundException();
		}
	}

	/**
	 * @author Thangaraj
	 * @since 03-11-2014
	 */
	public ArrayList<TradeInventories> getCurrentInventoriesByTraidId(Integer tradeId)
			throws InventoryNotFoundException {
		log.info("inside getCurrentInventoriesByTraidId() DAO");
		@SuppressWarnings("unchecked")
		ArrayList<TradeInventories> tradeInventories = (ArrayList<TradeInventories>) sessionFactory.getCurrentSession()
				.createCriteria(TradeInventories.class).add(Restrictions.eq("trade.tradeId", tradeId))
				.add(Restrictions.eq("isCurrent", true)).list();

		if (!tradeInventories.isEmpty()) {
			return tradeInventories;
		} else {
			throw new InventoryNotFoundException();
		}
	}

	/**
	 * 
	 * @param tradeId
	 * @param user
	 * @return
	 * @throws InventoryNotFoundException
	 * @author Thangaraj
	 * @since 03-11-2014
	 */
	public ArrayList<TradeInventories> getTraderInventories(Integer tradeId, String inventoryOf)
			throws InventoryNotFoundException {
		log.info("inside getTraderInventories() DAO");
		@SuppressWarnings("unchecked")
		ArrayList<TradeInventories> tradeInventories = (ArrayList<TradeInventories>) sessionFactory.getCurrentSession()
				.createCriteria(TradeInventories.class).add(Restrictions.eq("trade.tradeId", tradeId))
				.add(Restrictions.eq("inventoryOf", inventoryOf)).add(Restrictions.eq("isCurrent", true)).list();

		if (!tradeInventories.isEmpty()) {
			return tradeInventories;
		} else {
			throw new InventoryNotFoundException();
		}
	}

	/**
	 * 
	 * @param tradeId
	 * @param inventoryOf
	 * @return
	 * @throws InventoryNotFoundException
	 * @author Thangaraj
	 * @since 24-11-2014
	 */
	public ArrayList<TradeInventories> getTraderSavedInventories(Integer tradeId, Integer level, String inventoryOf)
			throws InventoryNotFoundException {
		log.info("inside getTraderSavedInventories() DAO");
		@SuppressWarnings("unchecked")
		ArrayList<TradeInventories> tradeInventories = (ArrayList<TradeInventories>) sessionFactory.getCurrentSession()
				.createCriteria(TradeInventories.class).add(Restrictions.eq("trade.tradeId", tradeId))
				.add(Restrictions.eq("inventoryOf", inventoryOf)).add(Restrictions.eq("level", level))
				.add(Restrictions.eq("offerType", "SAVED")).list();

		if (!tradeInventories.isEmpty()) {
			return tradeInventories;
		} else {
			throw new InventoryNotFoundException();
		}

	}

	/**
	 * method to get inventories of saved trade by level
	 * 
	 * @author Soujanya
	 * @since 26 nov,2014
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TradeInventories> getInventoriesOfSavedTradeByLevel(Integer tradeId, Integer level)
			throws TradeInventoryNotFoundException {
		log.info("inside getInventoriesOfSavedTradeByLevel() ");
		ArrayList<TradeInventories> tradeInventories = (ArrayList<TradeInventories>) sessionFactory.getCurrentSession()
				.createCriteria(TradeInventories.class).add(Restrictions.eq("trade.tradeId", tradeId))
				.add(Restrictions.eq("level", level)).add(Restrictions.eq("offerType", "SAVED")).list();

		if (!tradeInventories.isEmpty()) {
			return tradeInventories;
		} else {
			throw new TradeInventoryNotFoundException();
		}
	}

	public ArrayList<Trades> getTradeHistoryList(Integer userId) throws TradeNotFoundException {
		log.info("inside getTradesByCriteria()");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Trades.class);

		criteria.add(Restrictions.or(Restrictions.eq("trader.userId", userId), Restrictions.eq("vendor.userId", userId)));
		criteria.add(Restrictions.ne("status", TradeStates.SAVED.toString()));
		criteria.addOrder(Order.desc("tradeId"));
		@SuppressWarnings("unchecked")
		ArrayList<Trades> trades = (ArrayList<Trades>) criteria.list();
		if (!trades.isEmpty()) {
			return trades;
		} else {
			throw new TradeNotFoundException();
		}

	}

	public ArrayList<TradeInventories> getAllInventoriesOfTrade(Integer tradeId) throws TradeInventoryNotFoundException {
		log.info("inside getCurrentInventoriesByTraidId() DAO");
		@SuppressWarnings("unchecked")
		ArrayList<TradeInventories> tradeInventories = (ArrayList<TradeInventories>) sessionFactory.getCurrentSession()
				.createCriteria(TradeInventories.class).add(Restrictions.eq("trade.tradeId", tradeId)).list();

		if (!tradeInventories.isEmpty()) {
			return tradeInventories;
		} else {
			throw new TradeInventoryNotFoundException();
		}
	}

	/**
	 * method to get count of trades in trade history
	 * 
	 * @author Soujanya
	 * @since nov 13,2014
	 */
	public Integer getCountOfTradesOfTradeHistory(Integer userId) throws TradeNotFoundException {
		log.info("inside getCountOfTradesOfTradeHistory()");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Trades.class);

		criteria.add(Restrictions.or(Restrictions.eq("trader.userId", userId), Restrictions.eq("vendor.userId", userId)));
		criteria.add(Restrictions.ne("status", TradeStates.SAVED.toString()));
		criteria.setProjection(Projections.count("tradeId"));
		Integer count = Integer.parseInt(criteria.list().get(0).toString());
		return count;
	}

	/**
	 * method to get count of new offers
	 * 
	 * @author Soujanya
	 * @since nov 17,2014
	 */
	public Integer getCountOfOffersByStatus(Integer userId, TradeStates status) throws TradeNotFoundException {
		log.info("inside getCountOfNewTrades()");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Trades.class);
		criteria.add(Restrictions.eq("status", status.toString()));
		criteria.add(Restrictions.or(Restrictions.eq("trader.userId", userId), Restrictions.eq("vendor.userId", userId)));
		criteria.setProjection(Projections.count("tradeId"));
		Integer count = Integer.parseInt(criteria.list().get(0).toString());
		return count;
	}

	/**
	 * method to get count of saved trades
	 * 
	 * @author Soujanya
	 * @since nov 17,2014
	 * 
	 * modified by Thangaraj Feb 03, 2015
	 */
	public Integer getCountOfSavedTrades(Integer userId) throws TradeNotFoundException {
		log.info("inside getCountOfSavedTrades()");
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TradeInventories.class, "t");
		DetachedCriteria subquery = DetachedCriteria.forClass(TradeInventories.class, "s");
		subquery.setProjection(Projections.max("level"));
		subquery.add(Restrictions.eqProperty("t.trade.tradeId", "s.trade.tradeId"));

		criteria.add(Restrictions.eq("offerType", "SAVED"));
		criteria.add(Subqueries.propertyEq("level", subquery));
		criteria.setProjection(Projections.countDistinct("t.trade"));
		Integer count = Integer.parseInt(criteria.list().get(0).toString());*/
//		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TradeInventories.class, "t");
//		DetachedCriteria subquery = DetachedCriteria.forClass(TradeInventories.class, "s");
//		subquery.setProjection(Projections.projectionList().add(Projections.property("id")).add(Projections.property("trade.tradeId")).add(Projections.max("level")).add(Projections.groupProperty("trade.tradeId")));
//		//DetachedCriteria subquery = DetachedCriteria.forClass(TradeInventories.class, "s");
//		//subquery.setProjection(Projections.max("level"));
//		criteria.add(Subqueries.propertiesEq(, dc))
//		ArrayList<Object[]> list = (ArrayList<Object[]>) criteria.list();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TradeInventories.class, "t");
		DetachedCriteria subquery = DetachedCriteria.forClass(TradeInventories.class, "s");
		subquery.add(Restrictions.eqProperty("s.trade.tradeId", "t.trade.tradeId"));
		subquery.setProjection(Projections.max("s.level"));
		criteria.add(Subqueries.propertyEq("t.level", subquery));
		criteria.add(Restrictions.eq("t.offerType", "SAVED"));
		criteria.createAlias("t.trade", "trade");
		criteria.add(Restrictions.or(Restrictions.eq("trade.trader.userId", userId),Restrictions.eq("trade.vendor.userId", userId)));
		criteria.setProjection(Projections.countDistinct("t.trade.tradeId"));
		Integer count = Integer.parseInt(criteria.list().get(0).toString());
		return count;
	}

	/**
	 * method to save favourite trade
	 * 
	 * @author Soujanya
	 * @since dec 24,2014
	 */
	public Integer saveFavouriteTrade(FavouriteTrades favouriteTrade) throws FavInventoryNotSavedOrUpdatedException {
		log.info("inside saveFavouriteTrade()");
		sessionFactory.getCurrentSession().saveOrUpdate(favouriteTrade);
		sessionFactory.getCurrentSession().flush();
		return favouriteTrade.getId();
	}

	/*
	 * method to retrieve all fav vendors of users
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<FavouriteTrades> getAllFavouriteTradesOfUser(Integer userId) throws FavouritesNotFoundException {
		log.info("inside getAllFavouriteTradesOfUser()");
		ArrayList<FavouriteTrades> favTrades = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(FavouriteTrades.class);
		criteria.add(Restrictions.eq("userId.userId", userId));
		criteria.addOrder(Order.asc("id"));
		// totalFavVendors=criteria.list().size();

		favTrades = (ArrayList<FavouriteTrades>) criteria.list();
		if (!favTrades.isEmpty()) {
			return favTrades;

		} else {
			throw new FavouritesNotFoundException();
		}
	}

	/*
	 * added by Soujanya on dec 24,2014 method to retrieve favourite trade id
	 * based on trade id and user id
	 */
	@SuppressWarnings("unchecked")
	public FavouriteTrades getFavouriteTradeByTradeAndUser(Integer tradeId, Integer userId)
			throws FavouritesNotFoundException {
		log.info("inside getFavouriteTradeByTradeAndUser()");
		ArrayList<FavouriteTrades> favTrades = (ArrayList<FavouriteTrades>) sessionFactory.getCurrentSession()
				.createCriteria(FavouriteTrades.class).add(Restrictions.eq("tradeId.tradeId", tradeId))
				.add(Restrictions.eq("userId.userId", userId)).list();
		if (!favTrades.isEmpty()) {
			return favTrades.get(0);
		} else {
			throw new FavouritesNotFoundException();
		}
	}

	/*
	 * added by Soujanya on dec 24,2014 method to delete favourite category
	 */
	public void deleteFavouriteTradeFromDB(FavouriteTrades favouriteTrade) throws FavouritesNotFoundException {
		log.info("inside deleteFavouriteTradeFromDB()");
		sessionFactory.getCurrentSession().delete(favouriteTrade);
		sessionFactory.getCurrentSession().flush();
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
	public Boolean isUserHasTradedWithVendor(Integer userId, Integer vendorId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Trades.class);
		criteria.add(Restrictions.eq("status", TradeStates.COMPLETED.toString()));
		criteria.add(Restrictions.or(Restrictions.eq("trader.userId", userId),
				Restrictions.eq("trader.userId", vendorId)));
		criteria.add(Restrictions.or(Restrictions.eq("vendor.userId", userId),
				Restrictions.eq("vendor.userId", vendorId)));
		ArrayList<Trades> completedTrades = (ArrayList<Trades>) criteria.list();
		if (completedTrades == null || completedTrades.isEmpty())
			return false;
		return true;
	}

	/**
	 * 
	 * @param userId
	 * @throws TradeNotFoundException
	 * @return Integer
	 * @author Thangaraj(KNSTEK)
	 * @since 03-Feb-2015
	 */
	public Integer getTotalNoOfCompletedTrades(Integer userId) throws TradeNotFoundException {
		log.info("inside getTotalNoOfCompletedTrades()");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Trades.class);

		criteria.add(Restrictions.or(Restrictions.eq("trader.userId", userId), Restrictions.eq("vendor.userId", userId)));
		criteria.add(Restrictions.eq("status", TradeStates.COMPLETED.toString()));
		criteria.setProjection(Projections.count("tradeId"));
		Integer count = Integer.parseInt(criteria.list().get(0).toString());
		return count;
	}
	
}
