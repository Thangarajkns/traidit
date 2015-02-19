package org.kns.traidit.backend.item.dao;

import java.util.ArrayList;

import org.kns.traidit.backend.item.exception.CommentNotFoundException;
import org.kns.traidit.backend.item.exception.CommentNotSavedOrUpdatedException;
import org.kns.traidit.backend.item.exception.ItemNotFoundException;
import org.kns.traidit.backend.item.exception.ItemNotSavedOrUpdatedException;
import org.kns.traidit.backend.item.model.ItemSpecifications;
import org.kns.traidit.backend.item.model.TraidItItems;
import org.kns.traidit.backend.item.model.UserComments;
import org.kns.traidit.backend.user.model.TraidItUser;

/*
 * created by Soujanya on 19th June,2014
 * DAO interface for item,comments and offer related activities
 */

public interface ItemDao {
	
	public Integer saveOrUpdateItem(TraidItItems item) throws ItemNotSavedOrUpdatedException;
	public ArrayList<TraidItItems> getAllItems(Integer pageNo,Integer pageSize) throws Exception;
	public TraidItItems getItemById(Integer id) throws ItemNotFoundException;
	public ArrayList<TraidItItems> getItemsByCategory(Integer categoryId) throws ItemNotFoundException;
	public TraidItItems getItemByName(String name) throws ItemNotFoundException;
	public void deleteItem(TraidItItems item) throws ItemNotFoundException;
	public Integer saveOrUpdateComment(UserComments comment) throws CommentNotSavedOrUpdatedException;
	public ArrayList<UserComments> getAllComments(Integer pageNo,Integer pageSize) throws Exception;
	public UserComments getCommentByCommentId(Integer commentId) throws CommentNotFoundException;
	public UserComments getCommentByCommentedUser(TraidItUser userId) throws CommentNotFoundException;
	public UserComments getCommentByItemId(TraidItItems itemId) throws CommentNotFoundException;
	public void deleteComment(UserComments comment) throws CommentNotFoundException;
	public ArrayList<TraidItItems> performItemSearch(final String keyword,String sortBy,String direction,Integer pageNo,Integer pageSize)throws ItemNotFoundException;
	public ItemSpecifications saveOrUpdateItemSpecification(ItemSpecifications specification);
	public ArrayList<TraidItItems> getItemByUpc(String upc) throws ItemNotFoundException;
	public Boolean reBuildIndex()throws Exception;
	public TraidItItems getItemByUPC(String upc) throws ItemNotFoundException;
	/*public Integer saveOrUpdateOffer(ItemOffers offer) throws OfferNotSavedOrUpdatedException;
	public ArrayList<ItemOffers> getAllOffers(Integer pageNo,Integer pageSize) throws Exception;
	public ItemOffers getOfferByOfferId(Integer offerId) throws OfferNotFoundException;
	public ItemOffers getOfferByItemId(TraidItItems itemId) throws OfferNotFoundException;
	public void deleteOffer(ItemOffers offer) throws OfferNotFoundException;*/
	
}
