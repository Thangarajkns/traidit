/*
 * Created by Soujanya on June 26,2014..
 * Service for Item Related Activites..
 */




package org.kns.traidit.frontend.item.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.category.dao.CategoryDao;
import org.kns.traidit.backend.category.exception.CategoryNotFoundException;
import org.kns.traidit.backend.category.model.TraidItItemCategories;
import org.kns.traidit.backend.item.dao.ItemDao;
import org.kns.traidit.backend.item.dao.ItemDaoImpl;
import org.kns.traidit.backend.item.exception.CommentNotFoundException;
import org.kns.traidit.backend.item.exception.CommentNotSavedOrUpdatedException;
import org.kns.traidit.backend.item.exception.ItemNotFoundException;
import org.kns.traidit.backend.item.exception.ItemNotSavedOrUpdatedException;
import org.kns.traidit.backend.item.model.ItemSpecifications;
import org.kns.traidit.backend.item.model.TraidItItems;
import org.kns.traidit.backend.item.model.UserComments;
import org.kns.traidit.backend.user.dao.UserDao;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.category.dto.CategoriesDto;
import org.kns.traidit.frontend.category.service.CategoryService;
import org.kns.traidit.frontend.common.utility.AmazonApiHitter;
import org.kns.traidit.frontend.item.dto.ItemsDto;
import org.kns.traidit.frontend.item.dto.UserCommentsDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Transactional
@Service("itemService")
public class ItemServiceImpl implements ItemService {
	
private static Logger log=Logger.getLogger(ItemServiceImpl.class);
	
	@Resource(name="itemDao")
	private ItemDao itemDao;
	
	@Resource(name="userDao")
	private UserDao userDao;
	
	@Resource(name="categoryDao")
	private CategoryDao categoryDao;
	
	@Resource(name="categoryService")
	private CategoryService categoryService; 
	
	private String itemImagePath;
	
	public String getItemImagePath() {
		return itemImagePath;
	}
	
	public void setItemImagePath(String itemImagePath) {
		this.itemImagePath = itemImagePath;
	}

	
	
	
	
	
	/*
	 * added by Soujanya
	 * method to view all items
	 */
	public ArrayList<ItemsDto> getAllItems(Integer pageNo,Integer pageSize) throws Exception{
		log.info("inside getAllItems()");
		ArrayList<TraidItItems> item=this.itemDao.getAllItems(pageNo,pageSize);
		ArrayList<ItemsDto> itemsDtos=new ArrayList<ItemsDto>();
		Integer totalItems=ItemDaoImpl.totalItems;
		for(TraidItItems items: item){
			
					ItemsDto itemsDto=ItemsDto.populateItemsDto(items);
					itemsDto.setTotalItems(totalItems);
					itemsDtos.add(itemsDto);	
				}
					
		
		return itemsDtos;
	}
	
	/*
	 * added by Soujanya on september 16, 2014
	 * method to list items under a particular category
	 */
	public ArrayList<ItemsDto> getItemsByCategoryId(Integer categoryId) throws ItemNotFoundException{
		log.info("inside getItemsByCategoryId()");
		ArrayList<TraidItItems> items= this.itemDao.getItemsByCategory(categoryId);
		ArrayList<ItemsDto> itemsDtos=new ArrayList<ItemsDto>();
		for(TraidItItems item: items){
			ItemsDto itemsDto=ItemsDto.populateItemsDto(item);
			itemsDtos.add(itemsDto);
		}
		return itemsDtos;
	}
	
	
	
	
	
	/*
	 * added by Soujanya on august 25th 2014
	 * method to add item by upc
	 */
	public ArrayList<ItemsDto> addItemByUpc(String upc) throws 	ItemNotFoundException{
		log.info("inside addItemByUpc()");
		try{
			ArrayList<TraidItItems> item=this.itemDao.getItemByUpc(upc);
			ArrayList<ItemsDto> itemsDtos=new ArrayList<ItemsDto>();
			for(TraidItItems items: item){
				ItemsDto itemsDto=ItemsDto.populateItemsDto(items);
				itemsDto.setLocalDBItem(itemsDto.getItemId());
				itemsDtos.add(itemsDto);
			}
			return itemsDtos;
		}
		catch(ItemNotFoundException e){
			try {
				ItemsDto ebayItem=AmazonApiHitter.getEBayItemIdFromUPC(upc);

				String categoryNames = ebayItem.getCategoryId().getCategoryName();
				String[] categories = categoryNames.split(":");
				if(categories[0] != ""){
					CategoriesDto parentCategory= new CategoriesDto();
					CategoriesDto childCategory= new CategoriesDto();
					try{
						parentCategory = this.categoryService.getCategoryByName(categories[0]);
						try{
							childCategory = this.categoryService.getCategoryByName(categories[1]);
							Integer temp = Arrays.binarySearch(childCategory.getParentCategoryIds(), parentCategory.getCategoryId());
							if(temp < 0 ){
								throw new CategoryNotFoundException();
							}
						}
						catch(CategoryNotFoundException cne){
							childCategory= new CategoriesDto();
							if(categories[1]!=null && categories[1].equals("")){
								childCategory.setCategoryName(categories[1]);
								Set<CategoriesDto> parentCategoryList = new HashSet<CategoriesDto>();
								parentCategoryList.add(parentCategory);
								childCategory.setParentCategories(parentCategoryList);
								childCategory.setCategoryId(this.categoryService.saveOrUpdateCategory(childCategory));
							}
							else{
								childCategory.setCategoryId(0);
								childCategory.setCategoryName("no sub-category");
							}
						}
					}
					catch(CategoryNotFoundException cne){
						Set<CategoriesDto> parentCategoryList = new HashSet<CategoriesDto>();

						if(categories[0]!=null && categories[0].equals("")){
							parentCategory.setCategoryName(categories[0]);
							parentCategory.setCategoryId(this.categoryService.saveOrUpdateCategory(parentCategory));
							parentCategoryList.add(parentCategory);
							childCategory.setParentCategories(parentCategoryList);
						}
						else{
							parentCategory.setCategoryId(0);
							parentCategory.setCategoryName("no category");
						}

						if(categories[1]!=null && categories[1].equals("")){
							childCategory.setCategoryName(categories[1]);
							childCategory.setCategoryId(this.categoryService.saveOrUpdateCategory(childCategory));
						}
						else{
							childCategory.setCategoryId(0);
							childCategory.setCategoryName("no sub-category");
						}
					}
					ebayItem.setGrandParent(parentCategory);
					ebayItem.setCategoryId(childCategory);
				}
				ArrayList<ItemsDto> itemsDtos=new ArrayList<ItemsDto>();
				itemsDtos.add(ebayItem);
				return itemsDtos;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				log.error("item doesn't exist in ebay and system,enter details manually");
				throw new ItemNotFoundException();
			}
			
		}
		
	}
	
	
	
	/*
	 * added by Soujanya on 4th august,2014
	 * method to get the list of search items
	 */
	public ArrayList<ItemsDto> getSearchItems(final String keyword,String sortBy,String direction,Integer pageNo,Integer pageSize) throws ItemNotFoundException{
		log.info("inside getSearchItems()");
		ArrayList<TraidItItems> item=this.itemDao.performItemSearch(keyword, sortBy, direction, pageNo, pageSize);
		ArrayList<ItemsDto> itemsDtos=new ArrayList<ItemsDto>();
		for(TraidItItems items: item){
			ItemsDto itemsDto=ItemsDto.populateItemsDto(items);
			itemsDtos.add(itemsDto);
		}
		return itemsDtos;
	}
	
	
	
	
	/*
	 * method to retrieve item by Id
	 */
	public ItemsDto getItemById(Integer id) throws ItemNotFoundException{
		log.info("inside getItemById()");
		try{
			TraidItItems items=this.itemDao.getItemById(id);
			ItemsDto itemsDto=ItemsDto.populateItemsDto(items);
			return itemsDto;
		}
		catch(ItemNotFoundException e){
			log.error("Error While Validating Item"+e.toString());
			throw e;
		}
	}
	
	
	
	
	
	
	/*
	 * method to retrieve all comments of user on items
	 */
	public ArrayList<UserCommentsDto> getAllComments(Integer pageNo,Integer pageSize) throws Exception{
		log.info("inside getAllComments()");
		ArrayList<UserComments> comment=this.itemDao.getAllComments(pageNo,pageSize);
		ArrayList<UserCommentsDto> userCommentsDtos=new ArrayList<UserCommentsDto>();
		Integer totalComments=ItemDaoImpl.totalComments;
		for(UserComments comments: comment){
			
					UserCommentsDto userCommentsDto=UserCommentsDto.populateUserCommentsDto(comments);
					userCommentsDto.setTotalComments(totalComments);
					userCommentsDtos.add(userCommentsDto);	
				}
					
		
		return userCommentsDtos;
	}
	
	
	
	
	
	
	
	
	/*
	 * method to retrieve comment by comment Id
	 */
	public UserCommentsDto getCommentByCommentId(Integer commentId) throws CommentNotFoundException{
		log.info("inside getCommentByCommentId()");
		try{
		UserComments userComments=this.itemDao.getCommentByCommentId(commentId);
		UserCommentsDto userCommentsDto=UserCommentsDto.populateUserCommentsDto(userComments);
		return userCommentsDto;
		}
		catch(Exception e){
			log.error("Error While Validating User Comments"+e.toString());
			return null;
		}
	}
	
	
	
	/*
	 * method to get comment by commented user
	 */
	public UserCommentsDto getCommentByCommentedUser(Integer userId) throws CommentNotFoundException{
		log.info("inside getCommentByCommentedUser()");
		try{
			TraidItUser user=this.userDao.getUserbyUserId(userId);
		UserComments userComments=this.itemDao.getCommentByCommentedUser(user);
		UserCommentsDto userCommentsDto=UserCommentsDto.populateUserCommentsDto(userComments);
		return userCommentsDto;
		}
		catch(Exception e){
			log.error("Error While Validating User Comments"+e.toString());
			return null;
		}
	}
	
	
	/*
	
	 * method to view all offers on items
	 
	public ArrayList<ItemOffersDto> getAllOffers(Integer pageNo,Integer pageSize) throws Exception{
		log.info("inside getAllOffers()");
		ArrayList<ItemOffers> offer=this.itemDao.getAllOffers(pageNo,pageSize);
		ArrayList<ItemOffersDto> itemOfferDtos=new ArrayList<ItemOffersDto>();
		Integer totalOffers=ItemDaoImpl.totalOffers;
		for(ItemOffers offers: offer){
			
					ItemOffersDto itemOffersDto=ItemOffersDto.populateItemOffersDto(offers);
					itemOffersDto.setTotalOffers(totalOffers);
					itemOfferDtos.add(itemOffersDto);	
				}
					
		
		return itemOfferDtos;
	}
	*/
	
	
	
/*	
	 * method to retrieve offer by offer id
	 
	public ItemOffersDto getOfferByOfferId(Integer offerId) throws OfferNotFoundException{
		log.info("inside getOfferByOfferId()");
		try{
			ItemOffers itemOffers=this.itemDao.getOfferByOfferId(offerId);
			ItemOffersDto itemOffersDto=ItemOffersDto.populateItemOffersDto(itemOffers);
			return itemOffersDto;
		}
		catch(Exception e){
			log.error("Error While Validating Item Offers"+e.toString());
			return null;
		}
	}
	
	*/
	
	
	/*
	 * added by Soujanya on july 8,2014
	 * method to add an item to DB
	 */
	public Integer addItem(Integer userId,String description,String itemName,String manufacturer,String details,String photos,String videos,Integer categoryId) throws ItemNotSavedOrUpdatedException, UserNotFoundException, CategoryNotFoundException{
		log.info("inside addItem()");
		try{
			TraidItItems item=new TraidItItems();
			item.setDescription(description);
			item.setItemName(itemName);
			item.setManufacturer(manufacturer);
			item.setPhotos(photos);
			item.setVideos(videos);
			TraidItItemCategories category=this.categoryDao.getCategoryById(categoryId);
			item.setCategoryId(category);
			Integer result=this.itemDao.saveOrUpdateItem(item);
			
			ItemSpecifications specification = new ItemSpecifications();
			specification.setKey("details");
			specification.setValue(details);
			specification.setVendor(this.userDao.getUserbyUserId(userId));
			specification.setUnitOfMeasure("nil");
			specification.setItem(item);
			specification = this.itemDao.saveOrUpdateItemSpecification(specification);
			return result;
		}
		catch(CategoryNotFoundException e){
			log.error("Error while retrieving category"+e.toString());
			throw e;
		}
}
	
	
	
	/*
	 * added by Soujanya on july 8,2014
	 * method to delete an item from DB
	 */
	public void deleteItem(Integer id) throws ItemNotFoundException{
		log.info("inside deleteItem()");
		TraidItItems item=this.itemDao.getItemById(id);
		this.itemDao.deleteItem(item);
		
	}

	
	
	/*
	 * added by Soujanya on july 9,2014
	 * method to add message to DB
	 */
	public Integer addComment(String message,Integer rating,Integer userId,Integer itemId ) throws CommentNotSavedOrUpdatedException, UserNotFoundException, ItemNotFoundException{
		log.info("inside addMessage()");
		TraidItUser user=this.userDao.getUserbyUserId(userId);
		TraidItItems item=this.itemDao.getItemById(itemId);
		UserComments userComment=new UserComments();
		userComment.setComment(message);
		userComment.setRating(rating);
		userComment.setCommentedUser(user);
		userComment.setItemId(item);
		Date commentedDate=new Date();
		userComment.setCommentedDate(commentedDate);
		Integer result=this.itemDao.saveOrUpdateComment(userComment);
		return result;
	}
	
	
	/*
	 * added by Soujanya on august 7,2014
	 * method to store item image in system for item
	 */
	public String saveItemPhotos(MultipartFile image,Integer itemId) throws Exception{
		log.info("inside getItemPhotos()");
		Properties properties = new Properties();
		String propFileName = "filePath.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        properties.load(inputStream);
		File file=null;
		
		File directory = new File(properties.getProperty("imageuploadurl")+"/"+itemId);
		if (!directory.exists()) {
			if (directory.mkdirs()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
	 
		if(image.getSize()>0)
		{
			file=new File(properties.getProperty("imageuploadurl")+"/"+itemId+"/"+image.getOriginalFilename());
			file.setReadable(true);
			file.setWritable(true);
			file.createNewFile();
			FileOutputStream fos=new FileOutputStream(file);
			fos.write(image.getBytes());
			fos.close();				
		}
		return image.getOriginalFilename();
	}
	
	
	/*
	 * added by Soujanya on august 7,2014
	 * method to store item video in system for item
	 */
	public String getItemVideos(MultipartHttpServletRequest request,MultipartFile video) throws Exception{
		log.info("inside getItemVideos()");
		File file=null;
		if(video.getSize()>0)
		{
			file=new File("D:/projects/traidit/Newfolder/video/"+video.getOriginalFilename());
			file.setReadable(true);
			file.setWritable(true);
			file.createNewFile();
			FileOutputStream fos=new FileOutputStream(file);
			fos.write(video.getBytes());
			fos.close();				
		}
		return video.getOriginalFilename();
	}

	/**
	 * Rebuilds entire index of TraiditItem class
	 * @author Thangaraj(KNSTEK)
	 * @since 02-09-2014
	 */
	public Boolean reBuildIndex()throws Exception{
		return this.itemDao.reBuildIndex();
	}
	
	/*
	 * added by Soujanya on september 19, 2014
	 * method to get item by name
	 * input: itemname
	 * returns: itemDto of matched item
	 */
	public ItemsDto getItemByName(String itemName) throws ItemNotFoundException{
		log.info("inside getItemByName()");
		TraidItItems item=this.itemDao.getItemByName(itemName);
		ItemsDto itemDto=ItemsDto.populateItemsDto(item);
		return itemDto;
	}
	
	/*
	 * added by Soujanya on september 19,2014
	 * method to get item by upc
	 * input: upc
	 * returns: itemDto of matched item
	 */
	public ItemsDto getItemByUPC(String upc) throws ItemNotFoundException{
		log.info("inside getItemByUPC()");
		TraidItItems item=this.itemDao.getItemByUPC(upc);
		ItemsDto itemDto=ItemsDto.populateItemsDto(item);
		return itemDto;
	}
	
	/*
	 * added by Soujanya on september 19,2014
	 * method to add item
	 */
	public Integer addItemToDB(String description,String itemName,String manufacturer,String details,String photos,String videos,Integer categoryId, String upc) throws ItemNotSavedOrUpdatedException, UserNotFoundException, CategoryNotFoundException{
		log.info("inside addItemToDB()");
		TraidItItems item=new TraidItItems();
		TraidItItemCategories category=this.categoryDao.getCategoryById(categoryId);
		item.setCategoryId(category);
		item.setDescription(description);
		item.setDetails(details);
		item.setItemName(itemName);
		item.setManufacturer(manufacturer);
		item.setPhotos(photos);
		item.setUpc(upc);
		item.setVideos(videos);
		
		Integer result=this.itemDao.saveOrUpdateItem(item);
		return result;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}


