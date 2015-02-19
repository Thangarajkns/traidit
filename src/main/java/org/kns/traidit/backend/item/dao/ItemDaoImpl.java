package org.kns.traidit.backend.item.dao;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.kns.traidit.backend.item.exception.CommentNotFoundException;
import org.kns.traidit.backend.item.exception.CommentNotSavedOrUpdatedException;
import org.kns.traidit.backend.item.exception.ItemNotFoundException;
import org.kns.traidit.backend.item.exception.ItemNotSavedOrUpdatedException;
import org.kns.traidit.backend.item.model.ItemSpecifications;
import org.kns.traidit.backend.item.model.TraidItItems;
import org.kns.traidit.backend.item.model.UserComments;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
 * created by Soujanya on 19th June,2014
 * DAO class for item related activities
 */

@Repository("itemDao")
@Transactional
public class ItemDaoImpl implements ItemDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public static Integer totalItems;
    
	public static Integer totalComments;
	
	public static Integer totalOffers;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private static Logger log=Logger.getLogger(ItemDaoImpl.class);
/*-------------------------------------------------------------------------------------------------*/
	/*
	 * added by Soujanya
	 * method to save or update Traidit items
	 * performs save
	 * performs update
	 */
	public Integer saveOrUpdateItem(TraidItItems item)throws ItemNotSavedOrUpdatedException{
		 sessionFactory.getCurrentSession().saveOrUpdate(item);
		 sessionFactory.getCurrentSession().flush();
		 return item.getItemId();
	 }
	
	/*
	 * added by Soujanya
	 * method to delete Traidit item
	 * deletes item
	 */
	public void deleteItem(TraidItItems item) throws ItemNotFoundException {
		sessionFactory.getCurrentSession().delete(item);
		sessionFactory.getCurrentSession().flush();
	}
	
	/*
	 * added by Soujanya
	 * method to get all items of Traidit
	 * lists all items
	 */
	@SuppressWarnings("unchecked")
	 public ArrayList<TraidItItems> getAllItems(Integer pageNo,Integer pageSize) throws Exception{
		 log.info("inside getAllItems()");
		 ArrayList<TraidItItems> items=null;
		 Criteria criteria=sessionFactory.getCurrentSession().createCriteria(TraidItItems.class);
	
		 criteria.addOrder(Order.asc("itemName"));
		 totalItems=criteria.list().size();
		 if(null!=pageNo && null!=pageSize){
				criteria.setFirstResult(pageNo*pageSize);
				criteria.setMaxResults(pageSize);
			}
			
		 items=(ArrayList<TraidItItems>) criteria.list();
		 if(!items.isEmpty()){
			 return items;
			 
		 }
		 else{
			 throw new Exception();
		 }
			 
	 }
	
	/*
	 * added by Soujanya on september 16,2014
	 * method to retrieve items in a particular category
	 * lists items based on category id
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TraidItItems> getItemsByCategory(Integer categoryId) throws ItemNotFoundException{
		log.info("inside getItemsByCategory()");
		ArrayList<TraidItItems> items=null;
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(TraidItItems.class);
		
		 criteria.add(Restrictions.eq("categoryId.categoryId", categoryId));
		 criteria.addOrder(Order.asc("itemName"));
		 totalItems=criteria.list().size();
		 
			
		 items=(ArrayList<TraidItItems>) criteria.list();
		 if(!items.isEmpty()){
			 return items;
			 
		 }
		 else{
			 throw new ItemNotFoundException();
		 }
	}
	
	/*
	 * added by Soujanya
	 * method to get item based on itemid
	 * returns item by itemid
	 */
	@SuppressWarnings("unchecked")
	public TraidItItems getItemById(Integer id) throws ItemNotFoundException {
		log.info("inside getItembyId()");
		ArrayList<TraidItItems> item = (ArrayList<TraidItItems>) sessionFactory.getCurrentSession().createCriteria(TraidItItems.class)
				.add(Restrictions.eq("itemId", id))
				.list();
		
		if(!item.isEmpty()){
			return item.get(0);
		}
		else{
			throw new ItemNotFoundException();
		}	
	}
	
	/*
	 * added by Soujanya on September 19,2014
	 * method to get item by upc
	 */
	
	@SuppressWarnings("unchecked")
	public TraidItItems getItemByUPC(String upc) throws ItemNotFoundException{
		log.info("inside getItemByUPC()");
		ArrayList<TraidItItems> item = (ArrayList<TraidItItems>) sessionFactory.getCurrentSession().createCriteria(TraidItItems.class)
				.add(Restrictions.eq("upc", upc))
				.list();
		
		if(!item.isEmpty()){
			return item.get(0);
		}
		else{
			throw new ItemNotFoundException();
		}	
	}
	
	/*
	 * added by Soujanya
	 * method to get item based on upc
	 * returns item by upc
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TraidItItems> getItemByUpc(String upc) throws ItemNotFoundException {
		log.info("inside getItembyUpc()");
		 ArrayList<TraidItItems> items=null;
		 Criteria criteria=sessionFactory.getCurrentSession().createCriteria(TraidItItems.class);
	criteria.add(Restrictions.eq("upc", upc));
		 criteria.addOrder(Order.asc("itemName"));
		 totalItems=criteria.list().size();
		
		 items=(ArrayList<TraidItItems>) criteria.list();
		 if(!items.isEmpty()){
			 return items;
			 
		 }
		 else{
			 throw new ItemNotFoundException();
		 }
	}
	
	
	
	/*
	 * added by Soujanya
	 * method to get user based on item name
	 * returns user bt item name
	 */
	@SuppressWarnings("unchecked")
	public TraidItItems getItemByName(String name) throws ItemNotFoundException {
		log.info("inside getItemByName()");
		ArrayList<TraidItItems> item=(ArrayList<TraidItItems>) sessionFactory.getCurrentSession().createCriteria(TraidItItems.class)
				.add(Restrictions.eq("itemName", name))
				.list();
		if(!item.isEmpty()){
			return item.get(0);
		}
		else{
			throw new ItemNotFoundException();
		}	
	}
	
	/*added by Soujanya
	 * method to save or update user comments
	 */
	
	public Integer saveOrUpdateComment(UserComments comment) throws CommentNotSavedOrUpdatedException{
		sessionFactory.getCurrentSession().saveOrUpdate(comment);
		 sessionFactory.getCurrentSession().flush();
		 return comment.getCommentId();
	}
	
	
	/*
	 * added by Soujanya
	 * method to delete user comment on item
	 */
	public void deleteComment(UserComments comment) throws CommentNotFoundException{
		sessionFactory.getCurrentSession().delete(comment);
		sessionFactory.getCurrentSession().flush();
	}
	
	
	/*
	 * added by Soujanya
	 * method to get all user comments
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<UserComments> getAllComments(Integer pageNo,Integer pageSize) throws Exception{
		log.info("inside getAllComments()");
		 ArrayList<UserComments> comments=null;
		 Criteria criteria=sessionFactory.getCurrentSession().createCriteria(UserComments.class);
	
		 criteria.addOrder(Order.asc("comment"));
		 totalComments=criteria.list().size();
		 if(null!=pageNo && null!=pageSize){
				criteria.setFirstResult(pageNo*pageSize);
				criteria.setMaxResults(pageSize);
			}
			
		 comments=(ArrayList<UserComments>) criteria.list();
		 if(!comments.isEmpty()){
			 return comments;
			 
		 }
		 else{
			 throw new Exception();
		 }
			 
		
	}
	
	
	/*
	 * added by Soujanya
	 * method to get comments based on comment id
	 */
	@SuppressWarnings("unchecked")
	public UserComments getCommentByCommentId(Integer commentId) throws CommentNotFoundException{
		log.info("inside  getCommentByCommentId()");
		ArrayList<UserComments> comment = (ArrayList<UserComments>) sessionFactory.getCurrentSession().createCriteria(UserComments.class)
				.add(Restrictions.eq("commentId", commentId))
				.list();
		
		if(!comment.isEmpty()){
			return comment.get(0);
		}
		else{
			throw new CommentNotFoundException();
		}	
	}
	
	/*
	 * added by Soujanya
	 * method to get comments based on commented user by using userid
	 */
	@SuppressWarnings("unchecked")
	public UserComments getCommentByCommentedUser(TraidItUser userId) throws CommentNotFoundException{
		log.info("inside  getCommentByCommentedUser()");
		ArrayList<UserComments> comment = (ArrayList<UserComments>) sessionFactory.getCurrentSession().createCriteria(UserComments.class)
				.add(Restrictions.eq("commentedUser", userId))
				.list();
		
		if(!comment.isEmpty()){
			return comment.get(0);
		}
		else{
			throw new CommentNotFoundException();
		}	
	}
	
	
	
	/*
	 * added by Soujanya
	 * method to get comments based on items using item id
	 */
	@SuppressWarnings("unchecked")
	public UserComments getCommentByItemId(TraidItItems itemId) throws CommentNotFoundException{
		log.info("inside  getCommentByItemId()");
		ArrayList<UserComments> comment = (ArrayList<UserComments>) sessionFactory.getCurrentSession().createCriteria(UserComments.class)
				.add(Restrictions.eq("itemId", itemId))
				.list();
		
		if(!comment.isEmpty()){
			return comment.get(0);
		}
		else{
			throw new CommentNotFoundException();
		}	
	}
	
	
	
	
	/*
	 * Added by Jeevan on July 21, 2014
	 * Method to get Items satisfying Search Criteria
	 * Made use of Hibernate Search and followed the pattern of Gracular
	 */ 
	public ArrayList<TraidItItems> performItemSearch(final String keyword,String sortBy,String direction,Integer pageNo,Integer pageSize)throws ItemNotFoundException{
		log.info("inside performItemSearch()");
		FullTextSession fullTextSession=org.hibernate.search.Search.getFullTextSession(sessionFactory.getCurrentSession());		
		QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity( TraidItItems.class ).get();	
	    org.apache.lucene.search.Query luceneQuery ;
		luceneQuery = queryBuilder
					.keyword()
					.fuzzy()
					.withThreshold(0.6f)
					.onFields("description","manufacturer","itemName")														
					.matching(keyword)
					.createQuery();		
		
		 FullTextQuery hibernateQuery=fullTextSession.createFullTextQuery(luceneQuery, TraidItItems.class);		 
		 //Sort sort=new Sort(new SortField("itemName", SortField.STRING, false)); 
		 /*if(null!=sortBy && sortBy!=""){						 
			 if(direction.equalsIgnoreCase("asc")){
				 sort=new Sort(new SortField(sortBy, SortField.STRING, false));
			 }
			else{
				sort=new Sort(new SortField(sortBy, SortField.STRING, true));
			}
		 }			 
		 hibernateQuery.setSort(sort);		*/
		 Integer totalItems=hibernateQuery.getResultSize();
		 if(pageNo!=null && pageSize != null){
			 hibernateQuery.setFirstResult(pageNo*pageSize);
			 hibernateQuery.setMaxResults(pageSize);
		 }
		 @SuppressWarnings("unchecked")
		java.util.List<TraidItItems> items=hibernateQuery.list(); 	
		 fullTextSession.clear();
		 if(!items.isEmpty()){
			// items.get(0).setTotalResults(totalItems);
			 return (ArrayList<TraidItItems>) items;
		 }
		 else{
			 throw new ItemNotFoundException();
		 }			 
	}
	
	/**
	 * Rebuilds entire index of TraiditItem class
	 * @author Thangaraj(KNSTEK)
	 * @since 02-09-2014
	 */
	public Boolean reBuildIndex()throws Exception{
		log.info("inside reBuildIndex()");
		Boolean flag=false;
		try{
			FullTextSession fullTextSession=Search.getFullTextSession(this.sessionFactory.getCurrentSession());
			/*	 fullTextSession.createIndexer()
	        .purgeAllOnStart(true)
	         .optimizeAfterPurge(true)
	         .optimizeOnFinish(true)
	         .batchSizeToLoadObjects( 25 )
	         .threadsToLoadObjects( 5 )
	         .threadsForSubsequentFetching( 20 )
	         .startAndWait();	*/
			
			
			ScrollableResults results = fullTextSession.createCriteria( TraidItItems.class )
				    .setFetchSize(100)
				    .scroll( ScrollMode.FORWARD_ONLY );
				int index = 0;
				while( results.next() ) {
				    index++;
				    fullTextSession.index( results.get(0) ); //index each element
				    if (index % 100 == 0) {
				        fullTextSession.flushToIndexes(); //apply changes to indexes
				        fullTextSession.clear(); //free memory since the queue is processed
				    }
				}
			
			 flag=true;
			 System.out.println("done");
			 
		}
		catch(Exception e){
			log.error("Interrupted Exception");
			flag=false;
		}
		return flag;		 
	}
	
	
	public ItemSpecifications saveOrUpdateItemSpecification(ItemSpecifications specification){
		sessionFactory.getCurrentSession().saveOrUpdate(specification);
		 sessionFactory.getCurrentSession().flush();
		return specification;
	}
	
	
	/*
	 * method to save and update offer on item
	 */
	/*public Integer saveOrUpdateOffer(ItemOffers offer) throws OfferNotSavedOrUpdatedException{
		sessionFactory.getCurrentSession().saveOrUpdate(offer);
		 sessionFactory.getCurrentSession().flush();
		 return offer.getOfferId();
	}
	*/
	
	/*
	 * method to delete an offer
	 */
/*	public void deleteOffer(ItemOffers offer) throws OfferNotFoundException{
		sessionFactory.getCurrentSession().delete(offer);
		sessionFactory.getCurrentSession().flush();
	}
	*/
	/*
	 * method to retrieve an offer by Offer id
	 
	@SuppressWarnings("unchecked")
	public ItemOffers getOfferByOfferId(Integer offerId) throws OfferNotFoundException{
		log.info(" getOfferByOfferId()");
		ArrayList<ItemOffers> offer = (ArrayList<ItemOffers>) sessionFactory.getCurrentSession().createCriteria(ItemOffers.class)
				.add(Restrictions.eq("offerId", offerId))
				.list();
		
		if(!offer.isEmpty()){
			return offer.get(0);
		}
		else{
			throw new OfferNotFoundException();
		}
		
		
	}
	*/
	
	
	/*
	 * method to retrieve an offer by Item Id
	 */
	/*@SuppressWarnings("unchecked")
	public ItemOffers getOfferByItemId(TraidItItems itemId) throws OfferNotFoundException{
		log.info("getOfferByItemId()");
		ArrayList<ItemOffers> offer = (ArrayList<ItemOffers>) sessionFactory.getCurrentSession().createCriteria(ItemOffers.class)
				.add(Restrictions.eq("itemId", itemId))
				.list();
		
		if(!offer.isEmpty()){
			return offer.get(0);
		}
		else{
			throw new OfferNotFoundException();
		}
		
	}*/
	
	
	
	/*
	 * method to retrieve all offers on items
	 */
	/*@SuppressWarnings("unchecked")
	
	public ArrayList<ItemOffers> getAllOffers(Integer pageNo,Integer pageSize) throws Exception{
		log.info("inside getAllOffers()");
		 ArrayList<ItemOffers> offers=null;
		 Criteria criteria=sessionFactory.getCurrentSession().createCriteria(ItemOffers.class);
	
		 criteria.addOrder(Order.asc("offerValue"));
		 totalOffers=criteria.list().size();
		 if(null!=pageNo && null!=pageSize){
				criteria.setFirstResult(pageNo*pageSize);
				criteria.setMaxResults(pageSize);
			}
			
		 offers=(ArrayList<ItemOffers>) criteria.list();
		 if(!offers.isEmpty()){
			 return offers;
			 
		 }
		 else{
			 throw new Exception();
		 }
	}*/
}
