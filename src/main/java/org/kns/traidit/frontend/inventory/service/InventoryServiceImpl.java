/*
 * Created by Soujanya on June 26,2014..
 * Service for Inventory Related Activites..
 */

package org.kns.traidit.frontend.inventory.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.kns.traidit.backend.category.dao.CategoryDao;
import org.kns.traidit.backend.category.exception.CategoryNotFoundException;
import org.kns.traidit.backend.category.model.SimilarCategory;
import org.kns.traidit.backend.category.model.TraidItItemCategories;
import org.kns.traidit.backend.inventory.dao.InventoryDao;
import org.kns.traidit.backend.inventory.dao.InventoryDaoImpl;
import org.kns.traidit.backend.inventory.exception.InventoryNotFoundException;
import org.kns.traidit.backend.inventory.exception.InventoryNotSavedOrUpdatedException;
import org.kns.traidit.backend.inventory.model.Inventory;
import org.kns.traidit.backend.item.dao.ItemDao;
import org.kns.traidit.backend.item.exception.ItemNotFoundException;
import org.kns.traidit.backend.item.model.TraidItItems;
import org.kns.traidit.backend.user.dao.UserDao;
import org.kns.traidit.backend.user.exception.DistanceNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.exception.FavouritesNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.exception.ZipNotFoundException;
import org.kns.traidit.backend.user.favourites.model.FavouriteInventory;
import org.kns.traidit.backend.user.model.Distances;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.category.service.CategoryService;
import org.kns.traidit.frontend.inventory.dto.InventoryDto;
import org.kns.traidit.frontend.item.dto.ItemsDto;
import org.kns.traidit.frontend.user.dto.FavouriteInventoryDto;
import org.kns.traidit.frontend.user.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

@Transactional
@Service("inventoryService")
public class InventoryServiceImpl implements InventoryService {

	private static Logger log = Logger.getLogger(InventoryServiceImpl.class);

	@Resource(name = "inventoryDao")
	private InventoryDao inventoryDao;

	@Resource(name = "itemDao")
	private ItemDao itemDao;

	@Resource(name = "userDao")
	private UserDao userDao;
	
	@Resource(name = "categoryDao")
	private CategoryDao categoryDao;

	@Resource(name="categoryService")
	private CategoryService categoryService;
	
	private String inventoryImagePath;
	
	public String getItemImageUrl() {
		return itemImageUrl;
	}

	public void setItemImageUrl(String itemImageUrl) {
		this.itemImageUrl = itemImageUrl;
	}

	public String getInventoryImageUrl() {
		return inventoryImageUrl;
	}

	public void setInventoryImageUrl(String inventoryImageUrl) {
		this.inventoryImageUrl = inventoryImageUrl;
	}







	private String itemImagePath;
	
	private String itemImageUrl;
	private String inventoryImageUrl;
	
	
	
	
	
	public String getItemImagePath() {
		return itemImagePath;
	}

	public void setItemImagePath(String itemImagePath) {
		this.itemImagePath = itemImagePath;
	}

	public String getInventoryImagePath() {
		return inventoryImagePath;
	}

	public void setInventoryImagePath(String inventoryImagePath) {
		this.inventoryImagePath = inventoryImagePath;
	}
	
	public static Integer totalNoOfInventories;
	
	public Integer inventoryCount=0;
	/*
	 * method to retrieve InventoryByInventoryId
	 */
	public InventoryDto getInventoryByInventoryId(Integer inventoryId)
			throws InventoryNotFoundException {
		log.info("inside getInventoryByInventoryId");
		try {
			Inventory inventory = this.inventoryDao
					.getInventoryByInventoryId(inventoryId);
			InventoryDto inventoryDto = InventoryDto
					.populateInventoryDto(inventory);
			return inventoryDto;
		} catch (Exception e) {
			log.error("Error While Retrieving Inventory" + e.toString());
			return null;
		}
	}

	/*
	 * added by Soujanya on july 9,2014 method to add Inventory to DB
	 */
	public Integer addInventory(Boolean availableForSale,
			Integer unitsAvailable, Integer itemId, Integer vendorId, String description)
			throws InventoryNotSavedOrUpdatedException, ItemNotFoundException,
			UserNotFoundException {
		log.info("inside addInventory()");
		
		Inventory inventory = new Inventory();
		TraidItItems item = this.itemDao.getItemById(itemId);
		TraidItUser user = this.userDao.getUserbyUserId(vendorId);
		inventory.setAvailableForTrade(availableForSale);
		//inventory.setPrice(price);
		inventory.setUnitsAvailable(unitsAvailable);
		inventory.setItemId(item);
		inventory.setVendorId(user);
		Date listedDate = new Date();
		inventory.setListedDate(listedDate);
		inventory.setDescription(description);
		System.out.println(description);
		System.out.println(inventory.getDescription());
		Integer result = this.inventoryDao.saveOrUpdateInventory(inventory);
		return result;

	}

	/*
	 * added by Soujanya on july 9,2014 method to delete inventory
	 */

	public void deleteInventory(Integer inventoryId)
			throws InventoryNotFoundException {
		log.info("inside deleteInventory()");
		Inventory inventory = this.inventoryDao
				.getInventoryByInventoryId(inventoryId);
		if(null != inventory){
		this.inventoryDao.deleteInventory(inventory);
		}
		else{
			throw new InventoryNotFoundException();
		}
	}

	/*
	 * added by Soujanya on July 9,2014 method to edit inventory details
	 */
	public InventoryDto editInventory(Integer inventoryId)
			throws InventoryNotFoundException {
		log.info("inside editInventory()");
		InventoryDto inventoryDto = this.getInventoryByInventoryId(inventoryId);
		if(null != inventoryDto){
		return inventoryDto;
		}
		else{
			throw new InventoryNotFoundException();
		}
	}

	
	
	/*
	 * added by Soujanya on July 10,2014 method to save and update inventory
	 * details in DB that are edited
	 * edited by thangaraj 29-12-2014
	 * 
	 * Edited on Jan 02, 2015 by Jeevan. Item id needs to be populated
	 */
	public Integer saveEditedInventory(InventoryDto inventoryDto,MultipartHttpServletRequest multiRequest)throws Exception {
		log.info("inside saveEditedInventory()");
		inventoryDto.setFlagEdited(1);
		inventoryDto.setFlagImageEdited(1);
		Integer item=this.inventoryDao.getInventoryByInventoryId(inventoryDto.getInventoryId()).getItemId().getItemId();
		inventoryDto.setLocalDBItem(item);
		Integer result = handleSavingExistingItemandInventory(inventoryDto, multiRequest);
		if(result > 0){
			return result;
		}
		else {
			throw new InventoryNotFoundException();
		}
	}
	
	

	/*
	 * added by Soujanya on July 10,2014 method to retrieve inventory details to
	 * display
	 * 
	 * Modified by Jeevan on November 11, 2014. added Category id on signature to handle dynamic category allocation..
	 */
	public InventoryDto getInventoryToDisplay(Integer inventoryId, Integer userId,Integer categoryId)
			throws Exception {
		log.info("inside getInventoryToDisplay()");
		Inventory inventory = this.inventoryDao.getInventoryByInventoryId(inventoryId);
		//Needs to Populate Category BAsed on Id sent...
		//By Passing actual category(category1) and replacing category through which the inventory viewed drill down..
		TraidItItemCategories category=this.categoryDao.getCategoryById(categoryId);
		inventory.getItemId().setCategoryId(category);
		InventoryDto inventoryDto=InventoryDto.populateInventoryDto(inventory);
		if(null!=inventory.getDescription() && inventory.getDescription().trim().length()>0){
			inventoryDto.setDescription(inventory.getDescription());
		}
		else{
			inventoryDto.setDescription(inventory.getItemId().getDescription());
		}
		if(null!=inventory.getDetails() && inventory.getDetails().trim().length()>0){
			inventoryDto.setDetails(inventory.getDetails());
		}
		else{
			inventoryDto.setDetails(inventory.getItemId().getDetails());
		}
		inventoryDto.setPhotoList(this.populateInventorywithImages(inventoryDto, inventory));
		BigDecimal distance=this.calculateDistance(inventory.getVendorId().getUserId(), userId);
		if(null != distance && distance != new BigDecimal(0)){
			inventoryDto.setDistance(distance);				
		}
		else{
			inventoryDto.setDistance(null);
		}
		try{
			FavouriteInventory favouriteInventory=this.inventoryDao.getFavouriteInventoryByInventoryIdAndUserId(inventoryId, userId);
			if(null != favouriteInventory){
			inventoryDto.setIsFavouriteInventory(true);
			}
			else{
				throw new FavouritesNotFoundException();
			}
		}
		catch(FavouritesNotFoundException e){
			inventoryDto.setIsFavouriteInventory(false);
		}
		return inventoryDto;

	}

	/*
	  * added by Soujanya on July 22,2014
	  * method to change inventory availability
	  */
	 public Integer changeInventoryAvailability(Integer inventoryId, Boolean availableForSale) throws InventoryNotFoundException,InventoryNotSavedOrUpdatedException{
		 log.info("inside changeInventoryAvailability()");
		 Inventory inventory=this.inventoryDao.getInventoryByInventoryId(inventoryId);
		 inventory.setAvailableForTrade(availableForSale);
		 Integer result=this.inventoryDao.saveOrUpdateInventory(inventory);
		 return result;
	 }
	 
	 
	 /*
	  * added by Soujanya on august 6,2014
	  * method to get the list of all inventories available for sale
	  */
	 public ArrayList<InventoryDto> getAllInventories(Integer page,Integer pageSize) throws InventoryNotFoundException{
		 log.info("inside getAllInventories()");
		 ArrayList<Inventory> inventory=this.inventoryDao.getAllInventoriesInDB(page,pageSize);
			ArrayList<InventoryDto> inventoryDtos=new ArrayList<InventoryDto>();
			Integer totalInventory=InventoryDaoImpl.totalInventoryForSale;
			for(Inventory inventories: inventory){				
						InventoryDto inventoryDto=InventoryDto.populateInventoryDto(inventories);
						//categoriesDto.setCategoryIcon(logoUrl+"/"+categoriesDto.getCategoryIcon());
						inventoryDto.setTotalInventory(totalInventory);
						inventoryDtos.add(inventoryDto);	
					}						
			return inventoryDtos;
		}
	 
	 /*
	  * added by Soujanya on august 7,2014
	  * method to get inventory count based on category id
	  */
//	 public Integer getTotalInventoriesByCategory(Integer categoryId) throws InventoryNotFoundException, CategoryNotFoundException{
//		 log.info("inside  getTotalInventoriesByCategory()");
//		 TraidItItemCategories category=this.categoryDao.getCategoryById(categoryId);
//		 Integer categoriesInventories=this.inventoryDao.getInventoryCountByCategory(category);
//		 inventoryCount  = categoriesInventories;
//		 if(null != category.getSubCategories() && !category.getSubCategories().isEmpty()){
//			 getCountOfSubCategories(category.getSubCategories());
//		 }
//		 return inventoryCount;
//	 }
			
		
		
		
		
/*	public void getCountOfSubCategories(Set<TraidItItemCategories> subCategories) throws InventoryNotFoundException{
			
			for(TraidItItemCategories subCategory:subCategories){
				Integer subCategoryInventories=this.inventoryDao.getInventoryCountByCategory(subCategory);
				inventoryCount+=subCategoryInventories;
				if(null!=subCategory.getSubCategories() && !subCategory.getSubCategories().isEmpty()){
					this.getCountOfSubCategories(subCategory.getSubCategories());
				}
			}
		}*/

	/**
	 * Returns list of DTO's for all inventories available
	 * @throws  InventoryNotFoundException if there is no inventory
	 * @return returns ArrayList of type Inventory model object consisting of all inventories
	 * @author Thangaraj(KNSTEK)
	 * @since 31-07-2014
	 */
	@Override
	public ArrayList<InventoryDto> getAllInventories(Integer vendorId,String itemSearcText, Integer limit,Integer startIndex,String sortBy,String sortOrder)
			throws InventoryNotFoundException ,UserNotFoundException{
		TraidItUser vendor = null;
		if(vendorId != null)
			vendor = this.userDao.getUserbyUserId(vendorId);
		ArrayList<Inventory> inventories = this.inventoryDao
				.getAllInventories(vendor, itemSearcText, limit, startIndex,sortBy,sortOrder);
		totalNoOfInventories = InventoryDaoImpl.totalNoOfInventories;
		return InventoryDto.populateInventoryDto(inventories);
	}

	/**
	 * Changes the Enabled status of the given inventory to the given status
	 * @throws InventoryNotFoundException if there is no inventory 
	 * @return Integer inventory Id if updated successfully
	 * @author Thangaraj(KNSTEK)
	 * @since 31-07-2014
	 */
	@Override
	public Integer changeInventoryEnabledStatus(Integer inventoryId,
			Boolean isEnabled) throws InventoryNotFoundException,
			InventoryNotSavedOrUpdatedException {
		log.info("inside changeInventoryEnabledStatus()");
		Inventory inventory = this.inventoryDao.getInventoryByInventoryId(inventoryId);
		inventory.setIsEnabled(isEnabled);
		Integer result = this.inventoryDao.saveOrUpdateInventory(inventory);
		return result;
	}

	/**
	 * Returns a list of all available vendors 
	 * <p>
	 * Returns an ArrayList of UserDto of all available active vendors of active inventories
	 * </p>
	 * @return ArrayList<UserDto> list of vendors
	 * @throws UserNotFoundException if no vendors are there
	 */
	@Override
	public ArrayList<UserDto> getAllVendors() throws UserNotFoundException{
		ArrayList<TraidItUser> vendors = this.inventoryDao.getAllVendors();
		ArrayList<UserDto> vendorsDto = UserDto.populateUser(vendors);
		return vendorsDto;
	}

	/**
	 * Returns the total no of inventories which falls under given category id at any level
	 * 
	 * @param categoryId category Id
	 * @param userId current/requesting user id
	 * @param favVendor flag stating that filter only favourite vendors Inventories or not
	 * @return Integer number of inventories
	 * @throws CategoryNotFoundException if there is no category found on the given id
	 */
	@Override
	public Integer getInventoryCount(Integer categoryId,Integer userId,Boolean favVendor,Boolean favCategory,Integer maxDistance) throws Exception{
		Integer inventoryCount = 0;
//		log.info("trying to get InventoryCount of category "+categoryId);
//		System.out.println("trying to get InventoryCount of category "+categoryId);
		List<Integer> CategoryIds = this.categoryService.getSubCategoryIdsByCategoryId(userId, categoryId, favCategory);
		CategoryIds.add(categoryId);
		TraidItUser user=this.userDao.getUserbyUserId(userId);
		inventoryCount = this.inventoryDao.getInventoryCountByCategoryIds(CategoryIds,userId,user.getRatingRestriction(),favVendor);
		return inventoryCount;
	}
		
	/*public CategoriesDto populateCategoriesWithInventoryCount(CategoriesDto categoriesDto){
		System.out.println(categoriesDto.getCategoryId());
		for (CategoriesDto category : categoriesDto.getSubCategories()) {
			category.setTotalNoOfInventories(this.getInventoryCount(category.getCategoryId()));
		}
		System.out.println(categoriesDto.getCategoryName() +" "+categoriesDto.getTotalNoOfInventories());
		
		return categoriesDto;
	}*/
	
	
	
	@Override
 	public Integer saveOrUpdateInventory(InventoryDto inventoryDto) throws ItemNotFoundException, UserNotFoundException, InventoryNotSavedOrUpdatedException {
		Inventory inventory = new Inventory();
		inventory.setInventoryId(inventoryDto.getInventoryId());
		inventory.setAvailableForTrade(inventoryDto.getAvailableForTrade());
		inventory.setIsEnabled(inventoryDto.getIsEnabled());
		inventory.setItemId(this.itemDao.getItemById(inventoryDto.getItemId().getItemId()));
		inventory.setListedDate(inventoryDto.getListedDate());
		inventory.setPrice(inventoryDto.getPrice());
		inventory.setUnitsAvailable(inventoryDto.getUnitsAvailable());
		inventory.setVendorId(this.userDao.getUserbyUserId(inventoryDto.getVendorId().getUserId()));
		inventory.setDescription(inventoryDto.getDescription());
		inventory.setPhotos(inventoryDto.getPhotos());
		inventory.setVideo(inventoryDto.getVideo());
		return this.inventoryDao.saveOrUpdateInventory(inventory);
	}
	
	
	
	
	/**
	 * added by Soujanya on september 23, 2014
	 * method to get list of inventories under a particular category
	 * 
	 * @param categoryId category id
	 * @param userId current/requesting user id
	 * @param favVendor flag stating that filter only favourite vendors Inventories or not
	 * @return arraylist of inventories
	 * @throws CategoryNotFoundException 
	 * @throws inventory not found exception
	 */
	public ArrayList<InventoryDto> getInventoriesByCategoryId(Integer categoryId,Integer userId,Boolean favVendor,Integer maxDistance) throws Exception{
		log.info("inside getInventoriesByCategoryId()");
		TraidItUser user=this.userDao.getUserbyUserId(userId);
		Integer vendorRatingRestriction=user.getRatingRestriction();
		ArrayList<Inventory> inventories=this.inventoryDao.getInventoriesByCategory(categoryId,userId,vendorRatingRestriction,favVendor, maxDistance);
		ArrayList<InventoryDto> inventoryDtos=new ArrayList<InventoryDto>();
		for(Inventory inventory: inventories){
			//Added by Jeevan on November 10, 2014....
			TraidItItemCategories category=this.categoryDao.getCategoryById(categoryId);
			inventory.getItemId().setCategoryId(category);
			InventoryDto inventoryDto=InventoryDto.populateInventoryDto(inventory);
			
			inventoryDto.setPhotoList(this.populateInventorywithImages(inventoryDto, inventory));
			BigDecimal distance=this.calculateDistance(inventory.getVendorId().getUserId(), userId);
			if(null != distance && distance != new BigDecimal(0)){
				inventoryDto.setDistance(distance);				
			}
			else{
				//setted to 0 if no appropriate distance is found : Thangaraj : 04-02-2015
				inventoryDto.setDistance(new BigDecimal(0));	
			}
			inventoryDtos.add(inventoryDto);			
		}
//		System.out.println(timeFormatt.format(new Date()));
		return inventoryDtos;
	}
	
	
	/*
	  * added by Soujanya on september 2nd 2014
	  * method to calculate distance between two zipcodes
	  * if distance between the given zip codes is already entered in database, distance is retrieved from database
	  * if distance is not entered in database, then distance api is called, distance and zips saved in database, distance retrieved
	  * returns distance in miles
	  * In case zip is is out of range, returns custom invalid zip message
	  */
	 public BigDecimal calculateDistance(Integer userId1,Integer userId2) throws UserNotFoundException, JSONException, DistanceNotSavedOrUpdatedException, IOException{
		 log.info("inside calculateDistance()");	 
		 TraidItUser user1=this.userDao.getUserbyUserId(userId1);
		 TraidItUser user2=this.userDao.getUserbyUserId(userId2);
		 String zip1=user1.getZip();
		 String zip2=user2.getZip();
		 if(zip1 == null || zip2 == null || zip1 == zip2){
			 return null;
		 }
		 try{
			 Distances distancePresent=this.userDao.getDistanceByZipCodes(zip1, zip2);
			 return distancePresent.getDistance();
		 }
		 catch(ZipNotFoundException e){
			 Properties properties = new Properties();
				String propFileName = "distance_api.properties";
		        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		        properties.load(inputStream);
				String key = properties.getProperty("api_key");
				String url="http://zipcodedistanceapi.redline13.com/rest/"+key+"/distance.json/"+zip1+"/"+zip2+"/mile";
				JSONObject result=this.getJson(url);			 
				 Distances distance=new Distances();
				 distance.setZip1(zip1);
				 distance.setZip2(zip2);
			 if(result.toString().contains("distance")){		
					 distance.setDistance(new BigDecimal(result.getString("distance")));
					 this.userDao.saveOrUpdateDistance(distance);
					 try{
						 Distances distanceSaved=this.userDao.getDistanceByZipCodes(zip1, zip2);
						 log.info("url : "+url+" ; distance : "+distance + " ; saved in database ");
						 return distanceSaved.getDistance();
					 }
					 catch(ZipNotFoundException e2)
					 {
						 return null;
					 }
			 }
			 else{
				 distance.setDistance(new BigDecimal(0));
				 this.userDao.saveOrUpdateDistance(distance);
				 return null;
			 }
		 }
	 }
	 
	 /*
	  * added by Soujanya on July 2nd,2014
	  * Method to parse Json input as a stream 
	  */
	 public JSONObject getJson(String url){
			
			InputStream is = null;
			String result = "";
			JSONObject jsonObject = null;			
			// HTTP
			try {	    	
				HttpClient httpclient = new DefaultHttpClient(); // for port 80 requests!
				HttpGet httppost = new HttpGet(url);
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			} catch(Exception e) {
				return new JSONObject();
			}
		    
			// Read response to string
			try {	    	
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString();	            
			} catch(Exception e) {
				return null;
			}	 
			// Convert string to object
			try {
				jsonObject = new JSONObject(result);            
			} catch(JSONException e) {
				return null;
			}	    
			return jsonObject;
	  }
	
	 
	 /**
	  * 
	 * Created by Jeevan on 25-Sep-2014 6:15:45 pm	
	 *  Method for:
	 * @param inventoryDto
	 * @param inventory
	 * @return
	  */
	 	public ArrayList<String> populateInventorywithImages(InventoryDto inventoryDto,Inventory inventory){
	 		ArrayList<String> photoList=new ArrayList<String>();
			if(null!=inventory.getPhotos() && inventory.getPhotos().trim().length()>0){
				String[] photos=inventory.getPhotos().split(";");
				for(String photo:photos){
					photoList.add(inventoryImageUrl+inventory.getInventoryId()+"/"+photo);
				}
			}
			else{
				String[] photos=inventory.getItemId().getPhotos().split(";");
				for(String photo:photos){
					photoList.add(itemImageUrl+inventory.getItemId().getItemId()+"/"+photo);
				}
			}
			return photoList;
	 	}
	
	 
	 /**
		 * ORIGINALLY ADDED BY SOUJANYA
		* Created by Jeevan on 24-Sep-2014 5:24:27 pm	
		*  Method for: Adding Inventory
		* @param inventoryDto
		* @param multiPartRequest
		* @return
		* @throws Exception
		* 
		* 
		* Steps:
		* 1. Check if localdbitem==0
		*    a. If yes,
		*         1. Check if flagManual=1 (Item Added Manually),
		*            i. If yes, get Item by Item name. if found retreive the item, Proceed  to step 1.b.1
		*            ii. If no, proceed to step 1.a.2
		*         2.  if No, Add Item to DB.
		*              i. Map Item to Inventory
		*              ii. Save Inventory
		*              iii. save Images under Item
		*              iv. Exit
		*     
		*     b. If No (ie Item already exists in DB) 
		*           1. Check if edited=1 (Local Data Item is edited by User),
		*                  i. If yes, add edited details to Inventory, map Item to Inventory and save Inventory
		*                  ii. If no, No item edited, Get Item, add it to Inventory and save to database.
		*           2. Check if flagImagechanged=1 (Inventory images are different from what sent while searching bar code )
		*                  i. If yes, Save Images under Inventory ID.
		*                  ii. If No, dont worry about saving Images..
		*          
		*  2. EXIT                 
		*         NOTE :: IF ANY DETAILS OR IMAGES ARE MISSING FROM INVENTORY THEN THAT IMPLIES WHILES GETTING INVENTORY, THEY SHOULD BE FETCHED FROM ITEM
		* 
		 */
		public Integer addInventoryToDB(InventoryDto inventoryDto,MultipartHttpServletRequest multiPartRequest)throws Exception{
			log.info("inside addInventoryToDB()");
			Integer inventorySavedResult;
			if(inventoryDto.getLocalDBItem()<1){
				if(inventoryDto.getFlagManual()==1){
					try{
						TraidItItems item=this.itemDao.getItemByName(inventoryDto.getItemId().getItemName());						
						if(checkIfItemAlreadyExists(item, inventoryDto)){
							inventoryDto.setLocalDBItem(item.getItemId());
							inventorySavedResult=this.handleSavingExistingItemandInventory(inventoryDto, multiPartRequest);
						}
						else{ 
							throw new ItemNotFoundException();
						}
					}
					catch(ItemNotFoundException e){
						e.printStackTrace();
						inventorySavedResult=this.handleSavingItemandInventory(inventoryDto, multiPartRequest);
					}
				}				
				else{
					inventorySavedResult=this.handleSavingItemandInventory(inventoryDto, multiPartRequest);
				}
			}
			else{				
				inventorySavedResult=this.handleSavingExistingItemandInventory(inventoryDto, multiPartRequest);				
			}	
			if(inventorySavedResult>0)
				return inventorySavedResult;
			else
				throw new InventoryNotSavedOrUpdatedException();
		}
		
		
		
		/**
		 * Created by Jeevan on November 10, 2014
		 * Method to Determine whetehr to use existing Item or create a new one..............
		 * 		 * 
		 * @param item
		 * @param inventoryDto
		 * @return
		 * @throws Exception
		 */
		private Boolean checkIfItemAlreadyExists(TraidItItems item,InventoryDto inventoryDto)throws Exception{
			log.info("inside checkIfItemAlreadyExists()");
			boolean result=false;
			TraidItItemCategories category=this.categoryDao.getCategoryById(inventoryDto.getItemId().getCategoryId().getCategoryId());
			ArrayList<TraidItItemCategories> newItemSimilarCategories=this.getSimilarCategoriesofaCategory(category);			
			newItemSimilarCategories.add(category);
			for(TraidItItemCategories cat: newItemSimilarCategories){
               //Thinking that it is enough
				if(item.getCategoryId().getCategoryId().equals(cat.getCategoryId()) ){
					result=true;
				}
			}
			return result;
		}
		
		
		
		
		/**
		 * 
		* Created by Jeevan on 24-Sep-2014 9:10:08 pm	
		*  Method for: Handling Saving Existing Item and Inventory
		* @param inventoryDto
		* @param multiPartRequest
		 * @throws Exception 
		 * 
		 * Steps: 1. Saves Item by Local DB ITem
		 *        2. Based on Edit Flag, saves Inventory differently
		 *        3. If Images modified, saves Images
		 */
		private Integer handleSavingExistingItemandInventory(InventoryDto inventoryDto,MultipartHttpServletRequest multiPartRequest) throws Exception{
			log.info("inside handleSavingExisitngItemandInventory()");
			TraidItItems item=this.itemDao.getItemById(inventoryDto.getLocalDBItem());
			Integer inventorySavedResult;
			if(inventoryDto.getFlagEdited()==1){
				inventorySavedResult=this.saveEditedInventoryToDB(inventoryDto, item);
			}
			else{
				inventorySavedResult=this.saveInventoryToDB(inventoryDto, item);				
			}
			if(inventoryDto.getFlagImageEdited()==1){
				String photos=this.handleSavingItemImages(multiPartRequest, inventorySavedResult, inventoryImagePath);
				Inventory inventory=this.inventoryDao.getInventoryByInventoryId(inventorySavedResult);
				inventory.setPhotos(photos);
				this.inventoryDao.saveOrUpdateInventory(inventory);
			}
			else{
				String photos=this.handleSavingItemImages(multiPartRequest, item.getItemId(),itemImagePath);
				item.setPhotos(photos);
				this.itemDao.saveOrUpdateItem(item);
			}
			return inventorySavedResult;
		}
		
		
		/**
		 * 
		* Created by Jeevan on 24-Sep-2014 8:48:08 pm	
		*  Method for: Saving New Item and Inventory
		* @param inventoryDto
		* @param multipPartRequest
		* @return
		* @throws Exception
		*  
		*  Steps :1 Gets Item by Item Id
		*         2 Saves Inventory
		*         3 Save Images under Item path
		 */
		
		private Integer handleSavingItemandInventory(InventoryDto inventoryDto,MultipartHttpServletRequest multipPartRequest) throws Exception{
			log.info("inside handleSavingItemandInventory()");
			TraidItItems item=this.saveItem(inventoryDto.getItemId());
			Integer inventorySavedResult=this.saveInventoryToDB(inventoryDto, item);
			String photos=this.handleSavingItemImages(multipPartRequest, item.getItemId(),itemImagePath);
			item.setPhotos(photos);
			this.itemDao.saveOrUpdateItem(item);
			return inventorySavedResult;
		}
		
		/**
		 * 
		* Created by Jeevan on 24-Sep-2014 8:48:12 pm	
		*  Method for: savingItemorInventoryRelatedImages
		* @param multiPartRequest
		* @param item
		* @return
		 */
		private String handleSavingItemImages(MultipartHttpServletRequest multiPartRequest, Integer id,String filePath){
			log.info("inside handleSavingItemImages()");
			List<MultipartFile> image=multiPartRequest.getFiles("itemphoto");
			String photos = "";
			for (MultipartFile multipartFile : image) {
				String imgName = sanitiseImageName(multipartFile.getOriginalFilename());//this.itemService.saveItemPhotos(multipartFile);
				if(!imgName.equals(""))
					photos += imgName+";";
				try {
					this.saveItemPhotos(multipartFile,id,filePath,imgName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return photos;
		}
				
		/**
		 * 
		* Created by Jeevan on 24-Sep-2014 8:48:17 pm	
		*  Method for: Saving Item/ Inventory Photos
		* @param image
		* @param itemId
		* @throws Exception
		 */
		private void saveItemPhotos(MultipartFile image,Integer itemId,String filePath,String imgName) throws Exception{
			log.info("inside saveItemPhotos()");
			
			File file=null;
			File directory = new File(filePath+"/"+itemId);
			if (!directory.exists()) {
				if (directory.mkdirs()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}
		 
			if(image.getSize()>0)
			{
				file=new File(filePath+"/"+itemId+"/"+imgName);
				file.setReadable(true);
				file.setWritable(true);
				file.createNewFile();
				FileOutputStream fos=new FileOutputStream(file);
				fos.write(image.getBytes());
				fos.close();				
			}
		}
		
		
		/**
		 * 
		* Created by Jeevan on 24-Sep-2014 5:47:22 pm	
		*  Method for: saving Item 
		*  Everything common except handling categories
		* @param itemDto
		* @return
		* @throws Exception
		 */
		private TraidItItems saveItem(ItemsDto itemDto)throws Exception{
			log.info("inside saveItem()");
			TraidItItems traiditItems=new TraidItItems();
			traiditItems.setItemName(itemDto.getItemName());
			traiditItems.setManufacturer(itemDto.getManufacturer());
			traiditItems.setDetails(itemDto.getDetails());
			traiditItems.setDescription(itemDto.getDescription());
			traiditItems.setUpc(itemDto.getUpc());
			TraidItItemCategories category=null;
			if(null!=itemDto.getSubSubCategory().getCategoryId() && itemDto.getSubSubCategory().getCategoryId()>0){
				category=this.categoryDao.getCategoryById(itemDto.getSubSubCategory().getCategoryId());
			}
			else if(null!=itemDto.getSubCategory().getCategoryId() && itemDto.getSubCategory().getCategoryId()>0){
				category=this.categoryDao.getCategoryById(itemDto.getSubCategory().getCategoryId());
			}
			else{
				category=this.categoryDao.getCategoryById(itemDto.getCategoryId().getCategoryId());			
			}			
			traiditItems.setCategoryId(category);
			ArrayList<TraidItItemCategories> categories=this.getSimilarCategoriesofaCategory(category);
			if(!categories.isEmpty()){
				traiditItems.setCategory2(categories.get(0));
				if(categories.size()>1){
					traiditItems.setCategory3(categories.get(1));
				}
			}
			traiditItems.setItemId(this.itemDao.saveOrUpdateItem(traiditItems));
			return traiditItems;
		}
		
		
		/**
		 * Created by Jeevan on November 10, 2014
		 * Method to get Similar Categories of Category to them against an Item
		 * @param category
		 * @return
		 * @throws CategoryNotFoundException
		 * Did nt find more effective way to acheive this... Needs to be screened in future
		 * 
		 * Modified on Nov 11.. Considered Bidirectional Condition
		 * 
		 */
		private ArrayList<TraidItItemCategories> getSimilarCategoriesofaCategory(TraidItItemCategories category)throws CategoryNotFoundException{
			log.info("inside getSimilarCategoriesOfaCategory()");
			ArrayList<TraidItItemCategories> categories=new ArrayList<TraidItItemCategories>();
			ArrayList<SimilarCategory> similarCategories1=new ArrayList<SimilarCategory>();
			ArrayList<SimilarCategory> similarCategories2=new ArrayList<SimilarCategory>();
			try{
				similarCategories1=this.categoryDao.getSimilarCategoriesByCategory(category.getCategoryId());
			}
			catch(CategoryNotFoundException e){					
			}
			try{
				similarCategories2=this.categoryDao.getSimilarCategoriesBySimilarCategory(category.getCategoryId());
			}
			catch(CategoryNotFoundException e){				
			}
			for(SimilarCategory simCategory:similarCategories1){
				TraidItItemCategories similarCategory=this.categoryDao.getCategoryById(simCategory.getSimilarCategory());
				categories.add(similarCategory);
			}
			for(SimilarCategory simCategory:similarCategories2){
				if(simCategory.getIsBidirectional()){ //Added Recently
					TraidItItemCategories similarCategory=this.categoryDao.getCategoryById(simCategory.getCategory());
					categories.add(similarCategory);
				}
			}
			return categories;
		}
		
		
		/**
		 * 
		* Created by Jeevan on 24-Sep-2014 8:48:33 pm	
		*  Method for:
		* @param inventoryDto
		* @param item
		* @return
		* @throws Exception
		 */
		private Integer saveInventoryToDB(InventoryDto inventoryDto,TraidItItems item)throws Exception{
			log.info("inside saveInventoryToDB()");
			Inventory inventory=new Inventory();
			inventory.setItemId(item);
			inventory.setAvailableForTrade(inventoryDto.getAvailableForTrade());
			inventory.setListedDate(new Date());
			inventory.setUnitsAvailable(inventoryDto.getUnitsAvailable());
			TraidItUser traiditUser=this.userDao.getUserbyUserId(inventoryDto.getVendorId().getUserId());
			inventory.setVendorId(traiditUser);
			Integer planId=traiditUser.getPlan().getPlanId();
			if(planId==1 ){
				inventory.setIsEnabled(false);
			}
			else{
				inventory.setIsEnabled(true);
			}
			return this.inventoryDao.saveOrUpdateInventory(inventory);			
		}
		
		/**
		 * 
		* Created by Jeevan on 24-Sep-2014 9:18:44 pm	
		*  Method for:
		* @param inventoryDto
		* @param item
		* @return
		* @throws Exception
		 */
		private Integer saveEditedInventoryToDB(InventoryDto inventoryDto,TraidItItems item)throws Exception{
			log.info("inside saveInventoryToDB()");
			Inventory inventory;
			try{
				inventory=this.inventoryDao.getInventoryByInventoryId(inventoryDto.getInventoryId());
			}
			catch(InventoryNotFoundException e){
				inventory=new Inventory();
			}
			inventory.setItemId(item);
			inventory.setAvailableForTrade(inventoryDto.getAvailableForTrade());
			inventory.setListedDate(new Date());
			inventory.setUnitsAvailable(inventoryDto.getUnitsAvailable());
			inventory.setDescription(inventoryDto.getItemId().getDescription());
			inventory.setDetails(inventoryDto.getItemId().getDescription());
			inventory.setManufacturer(inventoryDto.getItemId().getManufacturer());
			TraidItUser traiditUser=this.userDao.getUserbyUserId(inventoryDto.getVendorId().getUserId());
			inventory.setVendorId(traiditUser);
			inventory.setIsEnabled(true);
			return this.inventoryDao.saveOrUpdateInventory(inventory);			
		}
		
		
		

		/**
		 * Returns the total no of inventories
		 * 
		 * @param userId current/requesting user id
		 * @param ratingRestriction minimum rating of the vendor user wants to see
		 * @param favVendor flag stating that filter only favourite vendors Inventories or not
		 * @return Integer total no of inventories available.
		 * @author Thangaraj
		 * @since 24-09-2014
		 */
		@Override
		public Integer getTotalInventoryCount(Integer userId,Integer ratingRestriction,Boolean favVendor,Integer maxDistance) {
			return this.inventoryDao.getTotalInventoryCount(userId,ratingRestriction,favVendor, maxDistance);
		}
		
		
		/**
		  * @author Soujanya
		  * @since september 29, 2014
		  * method to add Favourite Inventory by an User
		  * saves an inventory to favourites.
		  */
		 public Integer addFavouriteInventory(Integer vendorId, Integer inventoryId) throws Exception{
			 log.info("inside addFavouriteInventory()");
			 FavouriteInventory favouriteInventory= new FavouriteInventory();
			 Inventory inventory=this.inventoryDao.getInventoryByInventoryId(inventoryId);
			 TraidItUser traidItUser=this.userDao.getUserbyUserId(vendorId);
			 favouriteInventory.setInventory(inventory);
			 favouriteInventory.setTraiditUser(traidItUser);
			 Integer result=inventoryDao.saveOrUpdateFavouriteInventory(favouriteInventory);
			 return result;
		 }
		 
		 
		 
		 /**
		  * @author Soujanya
		  * @since september 29,2014
		  * method to delete Favourite Inventory
		  */
		 public void deleteFavouriteInventory(Integer inventoryId, Integer userId) throws Exception{
			 log.info("inside deleteFavouriteInventory()");
			 FavouriteInventory favouriteInventory=this.inventoryDao.getFavouriteInventoryByInventoryIdAndUserId(inventoryId, userId);
			 this.inventoryDao.deleteFavouriteInventory(favouriteInventory);
			 
		 }
		 
		 
		 /**
			 * added by Soujanya on october 13, 2014
			 * method to get list of inventories of a user
			 * @return arraylist of inventories
			 * 
			 * @throws inventory not found exception
			 */
			public ArrayList<InventoryDto> getInventoriesOfUser(Integer userId,Integer vendorId,Boolean favCategory,Boolean favInventory) throws Exception{
				log.info("inside getInventoriesOfUser()");
				ArrayList<Inventory> inventories=this.inventoryDao.getInventoriesByUser(userId, vendorId, favCategory, favInventory);
				ArrayList<InventoryDto> inventoryDtos=new ArrayList<InventoryDto>();
				for(Inventory inventory: inventories){
					InventoryDto inventoryDto=InventoryDto.populateInventoryDto(inventory);
					inventoryDto.setPhotoList(this.populateInventorywithImages(inventoryDto, inventory));
					
					inventoryDtos.add(inventoryDto);			
				}
				return inventoryDtos;
			}
			
			
			/**
			 * 
			 * @param userId
			 * @return
			 * @throws Exception
			 * @return ArrayList<InventoryDto>
			 * @author Thangaraj(KNSTEK)
			 * @throws InventoryNotFoundException 
			 * @since 24-Dec-2014
			 */
			public ArrayList<InventoryDto> getEntireInventoriesForUser(Integer userId,Boolean favInventory,Boolean favCategory,Integer maxdistance) throws InventoryNotFoundException{
				log.info("inside getEntireInventoriesForUser()");
				ArrayList<Inventory> inventories=this.inventoryDao.getEntireInventoriesForUser(userId,favInventory,favCategory, maxdistance);
				ArrayList<InventoryDto> inventoryDtos=new ArrayList<InventoryDto>();
				for(Inventory inventory: inventories){
					InventoryDto inventoryDto=InventoryDto.populateInventoryDto(inventory);
					inventoryDto.setPhotoList(this.populateInventorywithImages(inventoryDto, inventory));					
					inventoryDtos.add(inventoryDto);			
				}
				return inventoryDtos;
			}
			
			/**
			 * 
			 * @param userId
			 * @param favInventory
			 * @param favCategory
			 * @param maxdistance
			 * @param categoryId
			 * @return
			 * @throws Exception
			 * @return ArrayList<InventoryDto>
			 * @author Thangaraj(KNSTEK)
			 * @throws InventoryNotFoundException 
			 * @since 06-Jan-2015
			 */
			public ArrayList<InventoryDto> getEntireInventoriesOfCategoryForUser(Integer userId,Boolean favInventory,Boolean favCategory,Integer maxdistance,Integer categoryId) throws InventoryNotFoundException{
				log.info("inside getEntireInventoriesOfCategoryForUser()");
				List<Integer> categoryIds = this.categoryService.getSubCategoryIdsByCategoryId(userId, categoryId, favCategory);
				categoryIds.add(categoryId);
				ArrayList<Inventory> inventories=this.inventoryDao.getEntireInventoriesOfCategoryForUser(userId,favInventory,favCategory, maxdistance,categoryIds);
				ArrayList<InventoryDto> inventoryDtos=new ArrayList<InventoryDto>();
				for(Inventory inventory: inventories){
					InventoryDto inventoryDto=InventoryDto.populateInventoryDto(inventory);
					inventoryDto.setPhotoList(this.populateInventorywithImages(inventoryDto, inventory));					
					inventoryDtos.add(inventoryDto);			
				}
				return inventoryDtos;
			}
			
			
			/**
			 * @author Soujanya
			 * @since november 13,2014
			 * method to get all favourite inventories of user
			 * @return arraylist of favourite inventories
			 * @param userId
			 * 
			 */
			public ArrayList<FavouriteInventoryDto> getFavouriteInventoriesOfUser(Integer userId,Boolean favVendor,Boolean favCategory,Integer maxDistance) throws FavouritesNotFoundException,Exception{
				log.info("inside getFavouriteInventoriesOfUser()");
				ArrayList<FavouriteInventory> favInventories=this.inventoryDao.getFavouriteInventoriesOfUser(userId,favVendor,favCategory,maxDistance);
				System.out.println(favInventories+"got fav list from db");
				ArrayList<FavouriteInventoryDto> favInventoryDtos=new ArrayList<FavouriteInventoryDto>();
				if(favInventories.isEmpty())
					throw new FavouritesNotFoundException();
				for(FavouriteInventory favInventory: favInventories){
					FavouriteInventoryDto favInventoryDto=FavouriteInventoryDto.populateInventory(favInventory);
					System.out.println(favInventoryDto+"able to populate from fav inventory dto");
					favInventoryDto.setPhotoList(this.populateInventorywithImages(InventoryDto.populateInventoryDto(favInventory.getInventory()), favInventory.getInventory()));
					BigDecimal distance=this.calculateDistance(favInventory.getTraiditUser().getUserId(), userId);
					if(null != distance && distance != new BigDecimal(0)){
						favInventoryDto.setDistance(distance);
					}
					else{
						favInventoryDto.setDistance(null);
					}		
					favInventoryDtos.add(favInventoryDto);
				}
				return favInventoryDtos;
			}
		
		

			
	/**
	 * Removes all the special characters from the give string
	 * @param originalName
	 * @return String sanitised string
	 * @author Thangaraj
	 * @since 04-11-2014
	 */
	private String sanitiseImageName(String originalName){
		String sanitisedString =  Normalizer.normalize(originalName, Normalizer.Form.NFD).replaceAll("[^a-zA-Z|.]", "");
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(10000);
		return randomInt+"_"+sanitisedString;
	}

		
		
		
		
		
	
	
	
	
	
}