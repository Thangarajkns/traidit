/**
 *  Created by     : Soujanya
 * Created Date	  : June 26th,2014
 * file Name	  : ItemService.java
 * Purpose		  : Service

 * 
 * 
 */


package org.kns.traidit.frontend.item.service;

import java.util.ArrayList;
import java.util.Date;

import org.kns.traidit.backend.category.exception.CategoryNotFoundException;
import org.kns.traidit.backend.item.exception.CommentNotFoundException;
import org.kns.traidit.backend.item.exception.CommentNotSavedOrUpdatedException;
import org.kns.traidit.backend.item.exception.ItemNotFoundException;
import org.kns.traidit.backend.item.exception.ItemNotSavedOrUpdatedException;
import org.kns.traidit.backend.item.exception.OfferNotFoundException;
import org.kns.traidit.backend.item.model.TraidItItems;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.frontend.item.dto.ItemsDto;
import org.kns.traidit.frontend.item.dto.UserCommentsDto;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface ItemService {
	
	public ArrayList<ItemsDto> getAllItems(Integer pageNo,Integer pageSize) throws Exception;
	public ArrayList<ItemsDto> getItemsByCategoryId(Integer categoryId) throws ItemNotFoundException;
	public ItemsDto getItemById(Integer id) throws ItemNotFoundException;
	public ArrayList<ItemsDto> getSearchItems(final String keyword,String sortBy,String direction,Integer pageNo,Integer pageSize) throws ItemNotFoundException;
	public ArrayList<UserCommentsDto> getAllComments(Integer pageNo,Integer pageSize) throws Exception;
	public UserCommentsDto getCommentByCommentId(Integer commentId) throws CommentNotFoundException;
	public UserCommentsDto getCommentByCommentedUser(Integer userId) throws CommentNotFoundException;
//	public ArrayList<ItemOffersDto> getAllOffers(Integer pageNo,Integer pageSize) throws Exception;
//	public ItemOffersDto getOfferByOfferId(Integer offerId) throws OfferNotFoundException;
	public String saveItemPhotos(MultipartFile image,Integer itemId) throws Exception;
	public String getItemVideos(MultipartHttpServletRequest request,MultipartFile video) throws Exception;
	public Integer addItem(Integer userId,String description,String itemName,String manufacturer,String details,String photos,String videos,Integer categoryId) throws ItemNotSavedOrUpdatedException, UserNotFoundException, CategoryNotFoundException;
	public void deleteItem(Integer id) throws ItemNotFoundException;
	public ArrayList<ItemsDto> addItemByUpc(String upc) throws ItemNotFoundException;
	public Integer addComment(String message,Integer rating,Integer userId,Integer itemId ) throws CommentNotSavedOrUpdatedException,UserNotFoundException,ItemNotFoundException;
	public Boolean reBuildIndex()throws Exception;
	public ItemsDto getItemByName(String itemName) throws ItemNotFoundException;
	public ItemsDto getItemByUPC(String upc) throws ItemNotFoundException;
	public Integer addItemToDB(String description,String itemName,String manufacturer,String details,String photos,String videos,Integer categoryId, String upc) throws ItemNotSavedOrUpdatedException, UserNotFoundException, CategoryNotFoundException;

}
