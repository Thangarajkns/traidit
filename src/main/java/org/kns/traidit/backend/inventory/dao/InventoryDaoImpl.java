package org.kns.traidit.backend.inventory.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.kns.traidit.backend.inventory.exception.InventoryNotFoundException;
import org.kns.traidit.backend.inventory.exception.InventoryNotSavedOrUpdatedException;
import org.kns.traidit.backend.inventory.model.Inventory;
import org.kns.traidit.backend.item.model.TraidItItems;
import org.kns.traidit.backend.user.exception.FavouritesNotFoundException;
import org.kns.traidit.backend.user.exception.FavouritesNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.favourites.model.FavouriteCategory;
import org.kns.traidit.backend.user.favourites.model.FavouriteInventory;
import org.kns.traidit.backend.user.favourites.model.FavouriteVendor;
import org.kns.traidit.backend.user.model.Distances;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
/*
 * created by Soujanya on 19th June,2014
 * DAO class for supplier related activities
 */

@Repository("inventoryDao")
@Transactional
public class InventoryDaoImpl implements InventoryDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public static Integer totalInventory;
	
	public static Integer totalInventoryForSale;
	
	

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private static Logger log=Logger.getLogger(InventoryDaoImpl.class);
	
	public static Integer totalNoOfInventories;
	
	public static Integer totalInventoriesOfCategory;
/*------------------------------------------------------------------------------------------*/	
	
	/*
	 * Method to get Inventory by Inventory Id
	 */
	@SuppressWarnings("unchecked")
	public Inventory getInventoryByInventoryId(Integer inventoryId) throws InventoryNotFoundException{
		log.info("inside getInventoryByInventoryId()");
		ArrayList<Inventory> inventory = (ArrayList<Inventory>) sessionFactory.getCurrentSession().createCriteria(Inventory.class)
				.add(Restrictions.eq("inventoryId", inventoryId))
				.list();
		
		if(!inventory.isEmpty()){
			return inventory.get(0);
		}
		else{
			throw new InventoryNotFoundException();
		}	
	}
	
	
	/*
	 * added by Soujanya on september 29,2014
	 * method to get favourite inventory by Id
	 */
	@SuppressWarnings("unchecked")
	public FavouriteInventory getFavouriteInventoryById(Integer favouriteInventoryId) throws FavouritesNotFoundException{
		log.info("inside getFavouriteInventoryById()");
		ArrayList<FavouriteInventory> favouriteInventory=(ArrayList<FavouriteInventory>) sessionFactory.getCurrentSession().createCriteria(FavouriteInventory.class)
				.add(Restrictions.eq("id", favouriteInventoryId))
				.list();
		
		if(!favouriteInventory.isEmpty()){
			return favouriteInventory.get(0);
		}
		else{
			throw new FavouritesNotFoundException();
		}
	}
	
	
	/*
	 * added by Soujanya on september 29,2014
	 * method to delete favourite Inventory
	 * 
	 */
	public void deleteFavouriteInventory(FavouriteInventory favouriteInventory) throws FavouritesNotFoundException{
	log.info("inside deleteFavouriteInventory()");
	sessionFactory.getCurrentSession().delete(favouriteInventory);
	sessionFactory.getCurrentSession().flush();
	
}
	
	

	/*method to get Inventory by vendor id
	 * (non-Javadoc)
	 * @see org.kns.traidit.backend.inventory.dao.InventoryDao#getInventoryByInventoryId(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public Inventory getInventoryByVendorId(TraidItUser vendorId) throws InventoryNotFoundException{
		log.info("inside getInventoryByItemId()");
		ArrayList<Inventory> inventory = (ArrayList<Inventory>) sessionFactory.getCurrentSession().createCriteria(Inventory.class)
				.add(Restrictions.eq("vendorId", vendorId))
				.list();
		
		if(!inventory.isEmpty()){
			return inventory.get(0);
		}
		else{
			throw new InventoryNotFoundException();
		}	
	}

	
	/*method to get Inventory by item id
	 * (non-Javadoc)
	 * @see org.kns.traidit.backend.inventory.dao.InventoryDao#getInventoryByInventoryId(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public Inventory getInventoryByItemId(TraidItItems itemId) throws InventoryNotFoundException{
		log.info("inside getInventoryByItemId()");
		ArrayList<Inventory> inventory = (ArrayList<Inventory>) sessionFactory.getCurrentSession().createCriteria(Inventory.class)
				.add(Restrictions.eq("itemId", itemId))
				.list();
		
		if(!inventory.isEmpty()){
			return inventory.get(0);
		}
		else{
			throw new InventoryNotFoundException();
		}	
	}
	
	/**
	 * added by Soujanya on august 7,2014
	 * method to get inventory count based on category id
	 * 
	 * Modified By Jeevan on November 10, 2014
	 * add logic to consider category2 & category3 while getting list of inventories of a category
	 * 
	 * Modified by Thangaraj on November 11, 2014
	 * added restriction not to list current user inventories
	 * and added parameter
	 * @param userId Integer current logged in userid 
	 * @param categoryIds list of category ids whose inventories count has to be calculated
	 * @param ratingRestriction minimum rating of the vendor user wants to see
	 * @param favVendor flag stating that filter only favourite vendors Inventories or not
	 */
	@SuppressWarnings("unchecked")
	public Integer getInventoryCountByCategoryIds(List<Integer> categoryIds,Integer userId,Integer ratingRestriction,Boolean favVendor){
		//log.info("inside getInventoryCountByCategory()");
		//ArrayList<Inventory> inventories=null;
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Inventory.class);
		criteria.createAlias("vendorId", "vendor");
		criteria.add(Restrictions.ne("vendor.userId", userId));
		if(favVendor){
			DetachedCriteria dc = DetachedCriteria.forClass(FavouriteVendor.class);
			dc.add(Restrictions.eq("userId.userId", userId)).setProjection(Projections.property("vendorId.userId"));
			criteria.add(Subqueries.propertyIn("vendor.userId", dc));
		}
		//criteria.createCriteria("itemId").add(Restrictions.in("categoryId.categoryId", categoryIds));
		criteria.add(Restrictions.ge("vendor.rating", ratingRestriction));
		criteria.add(Restrictions.eq("isEnabled", true));
		criteria.add(Restrictions.eq("availableForTrade", true));
		Criteria subCriteria=criteria.createCriteria("itemId");
		Disjunction or=Restrictions.disjunction();
		or.add(Restrictions.in("categoryId.categoryId", categoryIds));
		or.add(Restrictions.in("category2.categoryId", categoryIds));
		or.add(Restrictions.in("category3.categoryId", categoryIds));
		subCriteria.add(or);
		List<Long> temp = new ArrayList<Long>();
		Integer count = 0;
		try{
			temp = (List<Long>) criteria.setProjection(Projections.sum("unitsAvailable")).list();
			count =Integer.parseInt(temp.get(0).toString());
		}
		catch(NullPointerException e){
			count = 0;
		}
		catch(Exception e){
			log.error("exception in getInventoryCountByCategoryIds DAO : "+e.toString());
			return 0;
		}
        return count;
		
	}

	/**
	 * 
	 * @param userId current/requesting user id
	 * @param ratingRestriction minimum rating of the vendor user wants to see
	 * @param favVendor flag stating that filter only favourite vendors Inventories or not
	 * @return
	 * @return Integer
	 * @author Thangaraj(KNSTEK)
	 * @since 
	 */
	@SuppressWarnings("unchecked")
	public Integer getTotalInventoryCount(Integer userId,Integer ratingRestriction,Boolean favVendor,Integer maxDistance){
		log.info("inside getTotalInventoryCount() DAO");
		ArrayList<String> zipcodes	= new ArrayList<String>();
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Inventory.class);
		criteria.add(Restrictions.eq("isEnabled", true));
		criteria.add(Restrictions.eq("availableForTrade", true));
		criteria.createAlias("vendorId", "vendor");
		if(favVendor){
			DetachedCriteria dc = DetachedCriteria.forClass(FavouriteVendor.class);
			dc.add(Restrictions.eq("userId.userId", userId)).setProjection(Projections.property("vendorId.userId"));
			criteria.add(Subqueries.propertyIn("vendor.userId", dc));
		}
		if(maxDistance!=null && maxDistance != 0){
			Criteria criteria1=sessionFactory.getCurrentSession().createCriteria(Distances.class);
			criteria1.add(Restrictions.le("distance", new BigDecimal(maxDistance)));
			criteria1.setProjection(Projections.projectionList().add(Projections.property("zip1")).add(Projections.property("zip2")));
			ArrayList<Object[]> result = (ArrayList<Object[]>) criteria1.list();
			for(Object[] entry : result){
				zipcodes.add((String)entry[0]);
				zipcodes.add((String)entry[1]);
			}
			criteria.add(Restrictions.in("vendor.zip", zipcodes));
		}
		criteria.add(Restrictions.ne("vendor.userId", userId));
		criteria.add(Restrictions.ge("vendor.rating", ratingRestriction));
		criteria.add(Restrictions.eq("isEnabled", true));
		criteria.add(Restrictions.eq("availableForTrade", true));
		
		List<Long> temp = new ArrayList<Long>();
		Integer count = 0;
		try{
			temp = (List<Long>) criteria.setProjection(Projections.sum("unitsAvailable")).list();
			count =Integer.parseInt(temp.get(0).toString());
		}
		catch(NullPointerException e){
			count = 0;
		}
		catch(Exception e){
			return 0;
		}
        return count;
	}
	
	
	/*
	 * added by Soujanya on july 9,2014
	 * method to save or update inventory details
	 */
	public Integer saveOrUpdateInventory(Inventory inventory) throws InventoryNotSavedOrUpdatedException{
		log.info("inside saveOrUpdateInventory()");
		sessionFactory.getCurrentSession().saveOrUpdate(inventory);
		sessionFactory.getCurrentSession().flush();
		return inventory.getInventoryId();
	}
	
	
	/*
	 * added by Soujanya on september 29,2014
	 * method to save or update Favourite Inventory 
	 */
	public Integer saveOrUpdateFavouriteInventory(FavouriteInventory favouriteInventory) throws FavouritesNotSavedOrUpdatedException{
		log.info("inside saveOrUpdateFavouriteInventory()");
		sessionFactory.getCurrentSession().saveOrUpdate(favouriteInventory);
		sessionFactory.getCurrentSession().flush();
		return favouriteInventory.getId();
	}
	
	
	/*
	 * added by Soujanya on july 9,2014
	 * method to delete inventory details from database
	 */
	public void deleteInventory(Inventory inventory) throws InventoryNotFoundException{
		sessionFactory.getCurrentSession().delete(inventory);
		sessionFactory.getCurrentSession().flush();
	}
	
	/*
	 * added by Soujanya on august 6,2014
	 * method to get list of inventories available for sale from database
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Inventory> getAllInventoriesInDB(Integer page,Integer pageSize) throws InventoryNotFoundException{
		log.info("inside getAllInventoriesInDB()");
		 //System.out.println("inside dao.getAllCategories()");
		 ArrayList<Inventory> inventories=null;
		 Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Inventory.class);
		 totalInventory= criteria.list().size();
		 criteria.addOrder(Order.asc("inventoryId"));
		 criteria.add(Restrictions.eq("availableForSale", true));
		 totalInventoryForSale=criteria.list().size();
		 inventories=(ArrayList<Inventory>) criteria.list();
		 if(!inventories.isEmpty()){
			 System.out.println(inventories.size());
			 return inventories;
		 }
		 else{
			 throw new InventoryNotFoundException();
		 }
			 
	 }
	
	
	
	
	
	/**
	 * added by Soujanya on september 23,2014
	 * method to retrieve inventory list under a category
	 * @return arraylist of inventories 
	 * @throws InventoryNotFoundException if no inventory is found under the given category
	 * 
	 * 
	 * Modified by Jeevan on November 10. 2014
	 * Handled Categories on basis of Cat1,Cat2,Cat3
	 * 
	 * Modified by Thangaraj on November 11, 2014
	 * added restriction not to list current user inventories
	 * and added parameter
	 * @param userId Integer current logged in userid 
	 * @param favVendor boolean flag telling fetch only favourite vendor inventories
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Inventory> getInventoriesByCategory(Integer categoryId,Integer userId,Integer ratingRestriction,Boolean favVendor,Integer maxDistance) throws InventoryNotFoundException{
		log.info("getInventoriesByCategory()");
		
		ArrayList<Inventory> inventories=null;
		Set<String> zipcodes = new HashSet<String>();
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Inventory.class);
		criteria.createAlias("vendorId", "vendor");
		if(favVendor){
			DetachedCriteria dc = DetachedCriteria.forClass(FavouriteVendor.class);
			dc.add(Restrictions.eq("userId.userId", userId)).setProjection(Projections.property("vendorId.userId"));
			criteria.add(Subqueries.propertyIn("vendor.userId", dc));
		}
		if(maxDistance!=null && maxDistance != 0){
			Criteria criteria1=sessionFactory.getCurrentSession().createCriteria(Distances.class);
			criteria1.add(Restrictions.le("distance", new BigDecimal(maxDistance)));
			criteria1.setProjection(Projections.projectionList().add(Projections.property("zip1")).add(Projections.property("zip2")));
			ArrayList<Object[]> result = (ArrayList<Object[]>) criteria1.list();
			for(Object[] entry : result){
				zipcodes.add((String)entry[0]);
				zipcodes.add((String)entry[1]);
			}
			if(zipcodes.isEmpty())
				throw new InventoryNotFoundException();
			criteria.add(Restrictions.in("vendor.zip", zipcodes));
		}
		criteria.add(Restrictions.ne("vendor.userId", userId));
		criteria.add(Restrictions.ge("vendor.rating", ratingRestriction));
		criteria.add(Restrictions.eq("isEnabled", true));
		criteria.add(Restrictions.eq("availableForTrade", true));
		Criteria subCriteria = criteria.createCriteria("itemId");
		Disjunction or=Restrictions.disjunction();
		or.add(Restrictions.eq("categoryId.categoryId", categoryId));
		or.add(Restrictions.eq("category2.categoryId", categoryId));
		or.add(Restrictions.eq("category3.categoryId", categoryId));
		subCriteria.add(or);
		
		 criteria.addOrder(Order.asc("inventoryId"));
		 totalInventoriesOfCategory = criteria.list().size();
		 inventories=(ArrayList<Inventory>) criteria.list();
		 if(!inventories.isEmpty()){
			 return inventories;
		 }
		 else{
			 throw new InventoryNotFoundException();
		 }
	}
	

	/**
	 * returns all the inventories
	 * @throws  InventoryNotFoundException if there is no inventory
	 * @return returns ArrayList of type Inventory model object consisting of all inventories
	 * @author Thangaraj
	 * @since 31-07-2014
	 * 
	 */
	@Override
	public ArrayList<Inventory> getAllInventories(TraidItUser vendor,String itemSearcText,Integer limit,Integer startIndex,String sortBy,String sortOrder) throws InventoryNotFoundException {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Inventory.class);
		criteria.createAlias("vendorId", "vendor");
		criteria.createAlias("itemId", "item");

		/*remove users with plan id 1 (guest users)*/
		criteria.add(Restrictions.ne("vendor.plan.planId", 1));
		if(vendor != null)
			criteria.add(Restrictions.eq("vendorId", vendor));
		if(itemSearcText != null && itemSearcText != ""){
			criteria.add(Restrictions.ilike("item.itemName", itemSearcText, MatchMode.ANYWHERE));
		}
		totalNoOfInventories = criteria.list().size(); 
		if(sortBy == null || sortBy == "")
			sortBy = "item.itemName";
		if(sortOrder != null && sortOrder.equals("DESC"))
			criteria.addOrder(Order.desc(sortBy));
		else
			criteria.addOrder(Order.asc(sortBy));
		criteria.setMaxResults(limit).setFirstResult(startIndex);
		@SuppressWarnings("unchecked")
		ArrayList<Inventory> inventories = (ArrayList<Inventory>)criteria.list();
		if(!inventories.isEmpty()){
			return inventories;
		}
		else{
			throw new InventoryNotFoundException();
		}
	}
	
	/**
	 * Returns a list of all available vendors 
	 * <p>
	 * Returns an ArrayList of TraidItUser of all available active vendors of active inventories
	 * </p>
	 * @return ArrayList<TraidItUser> list of vendors model object
	 * @throws UserNotFoundException if no vendors are there
	 */
	@Override
	public ArrayList<TraidItUser> getAllVendors() throws UserNotFoundException{
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Inventory.class)
				.setProjection(Projections.distinct(Projections.property("vendorId")))
				.createCriteria("vendorId")
				.add(Restrictions.eq("isEnabled", true))
				/*remove users with plan id 1 (guest users)*/
				.add(Restrictions.ne("plan.planId", 1));
		@SuppressWarnings("unchecked")
		ArrayList<TraidItUser> vendors = (ArrayList<TraidItUser>)criteria.list();
		if(!vendors.isEmpty()){
			return vendors;
		}	
		else{
			throw new UserNotFoundException();
		}
	}
	
	/**
	 * method to get Favourite Inventory by Inventory Id and User Id
	 * @author kns01 Soujanya
	 * @since september 30,2014
	 * 
	 */
	@SuppressWarnings("unchecked")
	public FavouriteInventory getFavouriteInventoryByInventoryIdAndUserId(Integer inventoryId, Integer userId) throws FavouritesNotFoundException{
		log.info("inside getFavouriteInventoryByInventoryIdAndUserId()");
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(FavouriteInventory.class)
				.add(Restrictions.eq("traiditUser.userId", userId))
				.add(Restrictions.eq("inventory.inventoryId", inventoryId))
				.addOrder(Order.asc("id"));
		ArrayList<FavouriteInventory> favouriteInventories=(ArrayList<FavouriteInventory>)criteria.list();
		if(!favouriteInventories.isEmpty()){
			return favouriteInventories.get(0);
		}
		else{
			throw new FavouritesNotFoundException();
		}
		
		
	}
	/**
	 * @author Soujanya
	 * @since october 13,2014
	 * method to return count of total inventories of a particular vendor
	 */
	@SuppressWarnings("unchecked")
	public Integer getInventoryCountOfVendor(Integer vendorId) throws Exception{
		log.info("inside getInventoryCountOfVendor()");
		Criteria criteria= this.sessionFactory.getCurrentSession().createCriteria(Inventory.class);
				criteria.add(Restrictions.eq("vendorId.userId", vendorId));
				criteria.add(Restrictions.eq("availableForTrade", true));
				criteria.add(Restrictions.eq("isEnabled", true));
		List<Long> temp = new ArrayList<Long>();
		try{
			temp = (List<Long>) criteria.setProjection(Projections.count("inventoryId")).list();
		}
		catch(Exception e){
			return 0;
		}
		Integer count =Integer.parseInt(temp.get(0).toString());
        return count;
}
	
	
	/**
	 * added by Soujanya on october 13,2014
	 * method to retrieve inventory list of a user
	 * @return arraylist of inventories 
	 * @throws InventoryNotFoundException if no inventory is found under the given category
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Inventory> getInventoriesByUser(Integer userId,Integer vendorId,Boolean favCategory,Boolean favInventory) throws InventoryNotFoundException{
		log.info("inside getInventoriesByUser()");
		ArrayList<Inventory> inventories=null;
		Set<String> zipcodes = new HashSet<String>();
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Inventory.class);
		criteria.add(Restrictions.eq("availableForTrade", true));
		criteria.add(Restrictions.eq("isEnabled", true));
		criteria.add(Restrictions.eq("vendorId.userId", vendorId));
		if(favInventory){
			DetachedCriteria dc = DetachedCriteria.forClass(FavouriteInventory.class);
			dc.add(Restrictions.eq("traiditUser.userId", userId)).setProjection(Projections.property("inventory.inventoryId"));
			criteria.add(Subqueries.propertyIn("inventoryId", dc));
		}
		if(favCategory){
			criteria.createAlias("itemId", "item");
			DetachedCriteria dc = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			DetachedCriteria dc1 = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			DetachedCriteria dc2 = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			dc.setProjection(Projections.property("category.categoryId"));
			dc1.setProjection(Projections.property("parentCategory.categoryId"));
			dc2.setProjection(Projections.property("grandParentCategory.categoryId"));
			criteria.add(Restrictions.or(Subqueries.propertyIn("item.categoryId", dc),Restrictions.or(Subqueries.propertyIn("item.categoryId", dc1),Subqueries.propertyIn("item.categoryId", dc2))));
		}
			
		 criteria.addOrder(Order.asc("inventoryId"));
		// totalInventories=criteria.list().size();
		 
			
		 inventories=(ArrayList<Inventory>) criteria.list();
		 if(!inventories.isEmpty()){
			 return inventories;
			 
		 }
		 else{
			 throw new InventoryNotFoundException();
		 }
	}
	
	/**
	 * 
	 * @param userId
	 * @param favInventory
	 * @param favCategory
	 * @param maxDistance
	 * @throws InventoryNotFoundException
	 * @return ArrayList<Inventory>
	 * @author Thangaraj(KNSTEK)
	 * @since 24-Dec-2014
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Inventory> getEntireInventoriesForUser(Integer userId,Boolean favInventory,Boolean favCategory,Integer maxDistance) throws InventoryNotFoundException{
		log.info("inside getInventoriesByUser()");
		ArrayList<Inventory> inventories=null;
		Set<String> zipcodes = new HashSet<String>();
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Inventory.class);
		criteria.createAlias("vendorId", "vendor");
		criteria.add(Restrictions.eq("availableForTrade", true));
		criteria.add(Restrictions.eq("isEnabled", true));
		criteria.add(Restrictions.ne("vendorId.userId", userId));
		criteria.addOrder(Order.asc("inventoryId"));
		
		if(favInventory){
			DetachedCriteria dc = DetachedCriteria.forClass(FavouriteInventory.class);
			dc.add(Restrictions.eq("traiditUser.userId", userId)).setProjection(Projections.property("inventory.inventoryId"));
			criteria.add(Subqueries.propertyIn("inventoryId", dc));
		}
		 if(maxDistance!=null && maxDistance != 0){
				Criteria criteria1=sessionFactory.getCurrentSession().createCriteria(Distances.class);
				criteria1.add(Restrictions.le("distance", new BigDecimal(maxDistance)));
				criteria1.setProjection(Projections.projectionList().add(Projections.property("zip1")).add(Projections.property("zip2")));
				ArrayList<Object[]> result = (ArrayList<Object[]>) criteria1.list();
				for(Object[] entry : result){
					zipcodes.add((String)entry[0]);
					zipcodes.add((String)entry[1]);
				}
				if(zipcodes.isEmpty())
					throw new InventoryNotFoundException();
				criteria.add(Restrictions.in("vendor.zip", zipcodes));
		}
		if(favCategory){
			criteria.createAlias("itemId", "item");
			DetachedCriteria dc = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			DetachedCriteria dc1 = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			DetachedCriteria dc2 = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			dc.setProjection(Projections.property("category.categoryId"));
			dc1.setProjection(Projections.property("parentCategory.categoryId"));
			dc2.setProjection(Projections.property("grandParentCategory.categoryId"));
			criteria.add(Restrictions.or(Subqueries.propertyIn("item.categoryId", dc),Restrictions.or(Subqueries.propertyIn("item.categoryId", dc1),Subqueries.propertyIn("item.categoryId", dc2))));
		}
		
		
		
		//criteria.setFetchMode("itemId", FetchMode.SELECT);
		inventories=(ArrayList<Inventory>) criteria.list();
		if(!inventories.isEmpty()){
			return inventories;
		}
		else{
			throw new InventoryNotFoundException();
		}
	}

	/**
	 * 
	 * @param userId
	 * @param favInventory
	 * @param favCategory
	 * @param maxDistance
	 * @throws InventoryNotFoundException
	 * @return ArrayList<Inventory>
	 * @author Thangaraj(KNSTEK)
	 * @since 06-Jan-2015
	 */
	public ArrayList<Inventory> getEntireInventoriesOfCategoryForUser(Integer userId,Boolean favInventory,Boolean favCategory,Integer maxDistance,List<Integer> categoryIds) throws InventoryNotFoundException{
		log.info("inside getInventoriesByUser()");
		ArrayList<Inventory> inventories=null;
		Set<String> zipcodes = new HashSet<String>();
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Inventory.class);
		criteria.createAlias("itemId", "item");
		criteria.createAlias("vendorId", "vendor");
		criteria.add(Restrictions.eq("availableForTrade", true));
		criteria.add(Restrictions.eq("isEnabled", true));
		criteria.add(Restrictions.ne("vendorId.userId", userId));
		criteria.addOrder(Order.asc("inventoryId"));
		criteria.add(Restrictions.in("item.categoryId.categoryId", categoryIds));
		if(favInventory){
			DetachedCriteria dc = DetachedCriteria.forClass(FavouriteInventory.class);
			dc.add(Restrictions.eq("traiditUser.userId", userId)).setProjection(Projections.property("inventory.inventoryId"));
			criteria.add(Subqueries.propertyIn("inventoryId", dc));
		}
		 if(maxDistance!=null && maxDistance != 0){
				Criteria criteria1=sessionFactory.getCurrentSession().createCriteria(Distances.class);
				criteria1.add(Restrictions.le("distance", new BigDecimal(maxDistance)));
				criteria1.setProjection(Projections.projectionList().add(Projections.property("zip1")).add(Projections.property("zip2")));
				ArrayList<Object[]> result = (ArrayList<Object[]>) criteria1.list();
				for(Object[] entry : result){
					zipcodes.add((String)entry[0]);
					zipcodes.add((String)entry[1]);
				}
				if(zipcodes.isEmpty())
					throw new InventoryNotFoundException();
				criteria.add(Restrictions.in("vendor.zip", zipcodes));
		}
		if(favCategory){
			DetachedCriteria dc = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			DetachedCriteria dc1 = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			DetachedCriteria dc2 = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			dc.setProjection(Projections.property("category.categoryId"));
			dc1.setProjection(Projections.property("parentCategory.categoryId"));
			dc2.setProjection(Projections.property("grandParentCategory.categoryId"));
			criteria.add(Restrictions.or(Subqueries.propertyIn("item.categoryId", dc),Restrictions.or(Subqueries.propertyIn("item.categoryId", dc1),Subqueries.propertyIn("item.categoryId", dc2))));
		}
		
		
		
		//criteria.setFetchMode("itemId", FetchMode.SELECT);
		inventories=(ArrayList<Inventory>) criteria.list();
		if(!inventories.isEmpty()){
			return inventories;
		}
		else{
			throw new InventoryNotFoundException();
		}
	}
	
	/**
	 * @author Soujanya
	 * @throws InventoryNotFoundException 
	 * @since november 13, 2014
	 * method to get list of all favourite inventories of user
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<FavouriteInventory> getFavouriteInventoriesOfUser(Integer userId,Boolean favVendor,Boolean favCategory,Integer maxDistance) throws FavouritesNotFoundException {
		log.info("inside getFavouriteInventoriesOfUser()");
		ArrayList<FavouriteInventory> favInventories=null;
		ArrayList<String> zipcodes = new ArrayList<String>();
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(FavouriteInventory.class);
		criteria.createAlias("inventory", "inventory");
		criteria.createAlias("inventory.vendorId", "vendor");
		criteria.createAlias("inventory.itemId", "item");
		criteria.add(Restrictions.ne("vendor.userId", userId));
		if(favVendor){
			DetachedCriteria dc = DetachedCriteria.forClass(FavouriteVendor.class);
			dc.add(Restrictions.eq("userId.userId", userId)).setProjection(Projections.property("vendorId.userId"));
			criteria.add(Subqueries.propertyIn("vendor.userId", dc));
		}
		if(maxDistance!=null && maxDistance != 0){
			Criteria criteria1=sessionFactory.getCurrentSession().createCriteria(Distances.class);
			criteria1.add(Restrictions.le("distance", new BigDecimal(maxDistance)));
			criteria1.setProjection(Projections.projectionList().add(Projections.property("zip1")).add(Projections.property("zip2")));
			ArrayList<Object[]> result = (ArrayList<Object[]>) criteria1.list();
			for(Object[] entry : result){
				zipcodes.add((String)entry[0]);
				zipcodes.add((String)entry[1]);
			}
			if(zipcodes.isEmpty())
				throw new FavouritesNotFoundException();
			criteria.add(Restrictions.in("vendor.zip", zipcodes));
		}
		if(favCategory){
			DetachedCriteria dc = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			DetachedCriteria dc1 = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			DetachedCriteria dc2 = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			dc.setProjection(Projections.property("category.categoryId"));
			dc1.setProjection(Projections.property("parentCategory.categoryId"));
			dc2.setProjection(Projections.property("grandParentCategory.categoryId"));
			criteria.add(Restrictions.or(Subqueries.propertyIn("item.categoryId", dc),Restrictions.or(Subqueries.propertyIn("item.categoryId", dc1),Subqueries.propertyIn("item.categoryId", dc2))));
		}
		criteria.add(Restrictions.eq("traiditUser.userId", userId));
		criteria.addOrder(Order.asc("id"));
		favInventories=(ArrayList<FavouriteInventory>) criteria.list();
		if(favInventories.isEmpty() || favInventories == null)
			throw new FavouritesNotFoundException();
		return favInventories;
		
	}
	
	/**
	 * method to get count of user's favourite inventories
	 * @author Soujanya
	 * @since nov 17,2014
	 */
	public Integer getCountOfFavInventoriesOfUser(Integer userId) throws FavouritesNotFoundException{
		log.info("inside getCountOfFavInventoriesOfUser()");
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(FavouriteInventory.class);
		 criteria.add(Restrictions.eq("traiditUser.userId", userId));
		 criteria.setProjection(Projections.count("id"));
		 Integer count=Integer.parseInt(criteria.list().get(0).toString());
			return count;
	}
	
	
	
	

	
	
	
}
