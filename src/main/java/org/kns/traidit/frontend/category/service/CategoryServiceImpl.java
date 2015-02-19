/*
 * Created by Soujanya on June 26,2014..
 * Service for Category Related Activites..
 */

package org.kns.traidit.frontend.category.service;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.category.dao.CategoryDao;
import org.kns.traidit.backend.category.dao.CategoryDaoImpl;
import org.kns.traidit.backend.category.exception.CategoryMappingLimitReachedException;
import org.kns.traidit.backend.category.exception.CategoryNotFoundException;
import org.kns.traidit.backend.category.exception.CategoryNotSavedOrUpdatedException;
import org.kns.traidit.backend.category.exception.FavouriteCategoryNotFound;
import org.kns.traidit.backend.category.exception.HomePageCategorySequenceNotFoundException;
import org.kns.traidit.backend.category.exception.HomePageCategorySequenceNotSavedException;
import org.kns.traidit.backend.category.model.EbayCategoryMapper;
import org.kns.traidit.backend.category.model.HomePageCategorySequence;
import org.kns.traidit.backend.category.model.SimilarCategory;
import org.kns.traidit.backend.category.model.TraidItItemCategories;
import org.kns.traidit.backend.user.dao.UserDao;
import org.kns.traidit.backend.user.exception.FavouritesNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.favourites.model.FavouriteCategory;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.category.dto.CategoriesDto;
import org.kns.traidit.frontend.category.dto.FavouriteCategoryDto;
import org.kns.traidit.frontend.category.dto.SimilarCategoryDto;
import org.kns.traidit.frontend.category.dto.SimilarCategoryForm;
import org.kns.traidit.frontend.inventory.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * CategoryServiceImpl --- Implementation class of
 * org.kns.traidit.frontend.category.service.CategoryService interface which
 * provides services related to Categories
 * 
 * @author Thangaraj
 * 
 */

@Transactional
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

	private static Logger log = Logger.getLogger(CategoryServiceImpl.class);

	@Resource(name = "categoryDao")
	private CategoryDao categoryDao;

	@Resource(name = "userDao")
	private UserDao userDao;

	@Resource(name = "inventoryService")
	private InventoryService inventoryService;

	private String categoryImagePath;

	private String categoryImageUrl;

	public String getCategoryImagePath() {
		return categoryImagePath;
	}

	public void setCategoryImagePath(String categoryImagePath) {
		this.categoryImagePath = categoryImagePath;
	}

	public String getCategoryImageUrl() {
		return categoryImageUrl;
	}

	public void setCategoryImageUrl(String categoryImageUrl) {
		this.categoryImageUrl = categoryImageUrl;
	}

	/*
	 * method to get category by category id
	 */
	public CategoriesDto getCategoryById(Integer id) throws CategoryNotFoundException {
		log.info("inside getCategoryById()");
		try {
			TraidItItemCategories category = this.categoryDao.getCategoryById(id);
			CategoriesDto categoryDto = CategoriesDto.populateCategoriesDto(category);
			ArrayList<String> eBayIds = this.categoryDao.getEBayCategoryIdsByCategoryId(id);
			String ids = "";
			for (String eBayId : eBayIds) {
				ids += eBayId + ";";
			}
			categoryDto.seteBayIds(ids);
			return categoryDto;
		} catch (CategoryNotFoundException e) {
			log.error("Error While Retrieving Category" + e.toString());
			throw e;
		}
	}

	/**
	 * Returns a list of categories populated with information favourite, has
	 * subcategories and icon image url
	 * 
	 * @param categoryId
	 *            Integer category id whose sub categories has to be listed
	 * @param userId
	 *            Integer user id who is accessing this list.
	 * @param favVendor
	 *            flag stating that filter only favourite vendors Inventories or
	 *            not
	 * @throws CategoryNotFoundException
	 * @return ArrayList<CategoriesDto> list of category dto's
	 * @author Thangaraj
	 * @since 26-09-2014
	 */
	public ArrayList<CategoriesDto> getSubCategoryWithFavourites(Integer categoryId, Integer userId, Boolean favVendor,
			Boolean favCategory, Integer maxDistance) throws CategoryNotFoundException, Exception {
		log.info("inside getCategoryWithFavourites() Service");
		System.out.println("inside getCategoryWithFavourites() Service");
		// get sub categories of given category id
		TraidItItemCategories categories = this.categoryDao.getCategoryById(categoryId);
		Set<TraidItItemCategories> subCategories = categories.getSubCategories();
		// populate DTO's
		ArrayList<CategoriesDto> categoryDtos = CategoriesDto
				.populateCategoriesDto(subCategories, subCategories.size());
		// populate favourite, has sub category and inventory count informations
		// to each categories
		categoryDtos = this.populateFavHasSubAndInventoryCount(categoryDtos, userId, favVendor, favCategory,
				maxDistance);
		return categoryDtos;
	}

	/**
	 * Populates given list of category dto's with favourite, has sub category,
	 * icon image URL and inventory count informations
	 * 
	 * @param categories
	 *            ArrayList<CategoriesDto> list of categories which needs to be
	 *            populated with additional information
	 * @param userId
	 *            Integer user id of the one who accessing it
	 * @param favVendor
	 *            flag stating that filter only favourite vendors Inventories or
	 *            not
	 * @return ArrayList<CategoriesDto> populated categories list
	 * @author Thangaraj
	 * @since 26-09-2014
	 */
	public ArrayList<CategoriesDto> populateFavHasSubAndInventoryCount(ArrayList<CategoriesDto> categories,
			Integer userId, Boolean favVendor, Boolean favouriteCategory, Integer maxDistance) throws Exception {
		log.info("inside populateFavHasSubAndInventoryCount() service");
		ArrayList<TraidItItemCategories> favCategories = null;
		ArrayList<Integer> favCategoryIds = new ArrayList<Integer>();
		ArrayList<CategoriesDto> resultCategories = new ArrayList<CategoriesDto>();
		// collect all favourite category ids in an arraylist
		try {
			favCategories = this.categoryDao.getFavouriteCategoriesOfUser(userId);
			log.info(favCategories.size() + " favourite categories found for user " + userId);
			for (TraidItItemCategories favCategory : favCategories) {
				favCategoryIds.add(favCategory.getCategoryId());
			}
		} catch (FavouriteCategoryNotFound e) {
			log.info("No fav categories found for user" + userId);
		}
		if (null != categories && categories.size() > 0) {
			for (CategoriesDto category : categories) {
				CategoriesDto tempCategory = category;
				// check favourite categories id list having this id and set
				// favourite category flag
				if (favouriteCategory && !favCategoryIds.contains(category.getCategoryId())) {
					continue;
				}
				// if favourite category only flag is set then skip non
				// favourite categories
				else if (favCategoryIds.contains(category.getCategoryId())) {

					tempCategory.setIsFavouriteCategory(true);
				} else {
					tempCategory.setIsFavouriteCategory(false);
				}
				// populate total inventory count under this category
				tempCategory.setTotalNoOfInventories(this.inventoryService.getInventoryCount(category.getCategoryId(),userId, favVendor, favouriteCategory, maxDistance));
				// set category icon url
				if (null != category.getCategoryIcon() && category.getCategoryIcon().trim().length() > 0) {
					tempCategory.setCategoryIcon(categoryImageUrl + category.getCategoryId() + "/"
							+ category.getCategoryIcon());
				} else {
					tempCategory.setCategoryIcon("null");
				}
				// set hasSubCategory flag of category if it has sub categories
				if (null != category.getSubCategories() && !category.getSubCategories().isEmpty()) {
					tempCategory.setHasSubCategories(true);
				} else {
					tempCategory.setHasSubCategories(false);
				}
				resultCategories.add(tempCategory);
			}
		}
		return resultCategories;
	}

	/*
	 * method to get category by name
	 */
	public CategoriesDto getCategoryByName(String name) throws CategoryNotFoundException {
		log.info("inside getCategoryByName()");
		TraidItItemCategories categories = this.categoryDao.getCategoryByName(name);
		CategoriesDto categoriesDto = CategoriesDto.populateCategoriesDto(categories);
		return categoriesDto;
	}

	/*
	 * modified by Soujanya on september 15, 2014 method to view all categories
	 * 
	 * commented by thangaraj on nov 26, 2014 this is dead function, called
	 * nowhere
	 */
	/*
	 * public ArrayList<CategoriesDto> getAllCategories(Integer pageNo, Integer
	 * pageSize, Integer userId) throws Exception {
	 * log.info("inside getAllCategories()");
	 * System.out.println("inside getAllCategories()");
	 * ArrayList<TraidItItemCategories> category = this.categoryDao
	 * .getAllCategories(pageNo, pageSize,null);
	 * ArrayList<TraidItItemCategories> favCategories=null; try{
	 * favCategories=this.categoryDao.getFavouriteCategoriesOfUser(userId); }
	 * catch(FavouriteCategoryNotFound e){
	 * 
	 * } if( favCategories != null && favCategories.size() > 0){
	 * ArrayList<CategoriesDto> categoriesDtos = new ArrayList<CategoriesDto>();
	 * for(TraidItItemCategories categories: category){ Integer totalCategories
	 * = CategoryDaoImpl.totalCategories; CategoriesDto categoriesDto =
	 * CategoriesDto.populateCategoriesDto(categories);
	 * categoriesDto.setTotalCategories(totalCategories); Boolean temp=false;
	 * for(TraidItItemCategories favCategory: favCategories){
	 * if(categories.getCategoryId().equals(favCategory.getCategoryId())){
	 * 
	 * temp=true; } } if(temp==true){
	 * categoriesDto.setIsFavouriteCategory(true); } else{
	 * categoriesDto.setIsFavouriteCategory(false); }
	 * categoriesDtos.add(categoriesDto); } return categoriesDtos; } else{
	 * ArrayList<CategoriesDto> categoriesDtos = new ArrayList<CategoriesDto>();
	 * for(TraidItItemCategories categories: category){ Integer totalCategories
	 * = CategoryDaoImpl.totalCategories; CategoriesDto categoriesDto =
	 * CategoriesDto.populateCategoriesDto(categories);
	 * categoriesDto.setTotalCategories(totalCategories); Boolean temp=false;
	 * categoriesDto.setIsFavouriteCategory(temp); System.out.println(temp);
	 * categoriesDtos.add(categoriesDto); } return categoriesDtos; }
	 * 
	 * }
	 */
	/**
	 * method to view all categories
	 * 
	 * @since 16-09-2014
	 */
	public ArrayList<CategoriesDto> getAllCategoriesWeb(Integer pageNo, Integer pageSize, String sortBy,
			String sortOrder, String searchText) throws Exception {
		log.info("inside getAllCategories()");
		System.out.println("inside getAllCategories()");
		ArrayList<TraidItItemCategories> categories = this.categoryDao.getAllCategories(pageNo, pageSize, sortBy,
				sortOrder, searchText);
		ArrayList<CategoriesDto> categoriesDtos = new ArrayList<CategoriesDto>();
		Integer totalCategories = CategoryDaoImpl.totalCategories;
		for (TraidItItemCategories category : categories) {

			CategoriesDto categoriesDto = CategoriesDto.populateCategoriesDto(category);
			// load parent categories details of this category
			categoriesDto.getParentCategories().clear();
			if (categoriesDto.getParentCategoryIds().length != 0)
				categoriesDto.getParentCategories().add(this.getCategoryById(categoriesDto.getParentCategoryIds()[0]));
			categoriesDto.setTotalCategories(totalCategories);
			categoriesDtos.add(categoriesDto);
		}

		return categoriesDtos;
	}

	/**
	 * Saves or Updates the TraidItItemCategories object to the database
	 * <p>
	 * Saves or Updates all the attributes of TraidItItemCategories and maps its
	 * corresponding ParentCategories and SubCategories
	 * </p>
	 * 
	 * @param categoriesDto
	 *            CategoriesDto type object(DTO object of TraidItItemCategories)
	 *            which has to be saved
	 * @return Integer type value. returns 1 if saved successfully else returns
	 *         0
	 * @author Thangaraj
	 * 
	 */
	public Integer saveOrUpdateCategory(CategoriesDto categoriesDto) throws Exception {
		TraidItItemCategories traidItCategoryItem;
		if(categoriesDto.getCategoryId()!=null )
			traidItCategoryItem = this.categoryDao.getCategoryById(categoriesDto.getCategoryId());
		else
			traidItCategoryItem = new TraidItItemCategories();
		// populate all the data of DTO to created category
		traidItCategoryItem.setCategoryName(categoriesDto.getCategoryName());
		traidItCategoryItem.setCategoryId(categoriesDto.getCategoryId());
		traidItCategoryItem.setIsHomePageCategory(categoriesDto.getIsHomePageCategory());
		try {
			MultipartFile icon = categoriesDto.getCategoryIconFile();
			// if icon file is updated set it
			if (icon != null && !icon.isEmpty()) {
				traidItCategoryItem.setCategoryIcon(sanitiseImageName(icon.getOriginalFilename()));
			}
			// if icon file not updated and file already exists, update old file
			// else skip
			else if (categoriesDto.getCategoryIcon() != null) {
				traidItCategoryItem.setCategoryIcon(categoriesDto.getCategoryIcon());
			}
			// if parent categories are setted then map all of them as the
			// parent of this category
			if (categoriesDto.getParentCategoryIds() != null && categoriesDto.getParentCategoryIds().length != 0) {
				Set<TraidItItemCategories> categories = new HashSet<TraidItItemCategories>();
				ArrayList<TraidItItemCategories> list = (ArrayList<TraidItItemCategories>) this
						.getCategoriesByListOfIds(categoriesDto.getParentCategoryIds());
				categories.addAll(list);
				traidItCategoryItem.setParentCategories(categories);
			}

			// populate similarof categories
			/*
			 * if(!categoriesDto.getSimilarCategoryOf().isEmpty() &&
			 * categoriesDto.getSimilarCategoryOf().size() != 0 ){
			 * for(CategoriesDto similarCategoryOf :
			 * categoriesDto.getSimilarCategoryOf()){ TraidItItemCategories
			 * similarCategory
			 * =this.categoryDao.getCategoryById(similarCategoryOf
			 * .getCategoryId());// new TraidItItemCategories();
			 * //similarCategory
			 * .setCategoryId(similarCategoryOf.getCategoryId());
			 * traidItCategoryItem.getSimilarCategoryOf().add(similarCategory);
			 * } }
			 * 
			 * //populate Similarto categories
			 * if(!categoriesDto.getSimilarCategoryTo().isEmpty() &&
			 * categoriesDto.getSimilarCategoryTo().size() != 0 ){
			 * for(CategoriesDto similarCategoryTo :
			 * categoriesDto.getSimilarCategoryTo()){ TraidItItemCategories
			 * similarCategory = new TraidItItemCategories();
			 * similarCategory.setCategoryId(similarCategoryTo.getCategoryId());
			 * traidItCategoryItem.getSimilarCategoryTo().add(similarCategory);
			 * } }
			 */
			// save the category
			traidItCategoryItem.setCategoryId(this.categoryDao.saveOrUpdateCategory(traidItCategoryItem));
			// save category image to corressponding path
			if (icon != null && !icon.isEmpty()) {
				saveCategoryIconFile(categoriesDto.getCategoryIconFile(), traidItCategoryItem.getCategoryId());
			}
			// reload the same category
			traidItCategoryItem = this.categoryDao.getCategoryById(traidItCategoryItem.getCategoryId());
			// if there any sub categories exists already for this category,
			// then un map this parent category from all sub categories
			// individually
			if (traidItCategoryItem.getSubCategories() != null && traidItCategoryItem.getSubCategories().size() != 0) {
				Set<TraidItItemCategories> list = (Set<TraidItItemCategories>) traidItCategoryItem.getSubCategories();
				for (TraidItItemCategories subCategory : list) {
					subCategory.getParentCategories().remove(traidItCategoryItem);
					this.categoryDao.saveOrUpdateCategory(subCategory);
				}
			}
			// if any categories mapped as its sub category, then map this
			// category as a parent category for all sub categories individually
			if (categoriesDto.getSubCategoryIds() != null && categoriesDto.getSubCategoryIds().length != 0) {
				ArrayList<TraidItItemCategories> list = (ArrayList<TraidItItemCategories>) this
						.getCategoriesByListOfIds(categoriesDto.getSubCategoryIds());
				for (TraidItItemCategories subCategory : list) {
					subCategory.getParentCategories().add(traidItCategoryItem);
					this.categoryDao.saveOrUpdateCategory(subCategory);
				}
			}

			return traidItCategoryItem.getCategoryId();
		} catch (CategoryNotSavedOrUpdatedException ex) {
			ex.printStackTrace();
			System.out.println("Exception on CategoryController -> createCategory save");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception on CategoryController -> createCategory submit");
		}
		return 0;
	}

	/**
	 * Returns list of Category items with the give id's.
	 * 
	 * @param categoryIds
	 *            An Integer array consists of category Id's
	 * @return ArrayList of Categories (TraidItItemCategories) of given Id's
	 * 
	 */
	public ArrayList<TraidItItemCategories> getCategoriesByListOfIds(Integer[] categoryIds) throws Exception {
		ArrayList<TraidItItemCategories> categories = new ArrayList<TraidItItemCategories>();
		try {
			categories = this.categoryDao.getCategoriesByListOfIds(categoryIds);
		} catch (CategoryNotFoundException e) {
			System.out.println("CategoryNotFoundException");
		}
		return categories;
	}

	/**
	 * saves the given image file of any category into the file system
	 * 
	 * @param icon
	 *            MultipartFile icon file of category which has to be saved
	 * @return Boolean. true if file exists and saved and false if there is no
	 *         file
	 * @throws IOException
	 */
	private Boolean saveCategoryIconFile(MultipartFile icon, Integer categoryId) throws IOException {
		if (icon != null && !icon.isEmpty()) {
			File directory = new File(categoryImagePath + categoryId + "/");
			if (!directory.exists()) {
				if (directory.mkdirs()) {
					log.info("directory " + directory.getPath() + " is created!");
					System.out.println("directory " + directory.getPath() + " is created!");
				} else {
					log.info("directory " + directory.getPath() + " is Failed to created!");
					System.out.println("directory " + directory.getPath() + " is Failed to created!");
				}
			}
			File file = new File(categoryImagePath + categoryId + "/" + sanitiseImageName(icon.getOriginalFilename()));
			if (file.exists()) {
				file.createNewFile();
			}
			icon.transferTo(file);
			return true;
		}
		return false;
	}

	/**
	 * Deletes the category of type TraidItItemCategories of given id
	 * <p>
	 * First un maps all of its parent and sub categories and deletes given
	 * category
	 * </p>
	 * 
	 * @param id
	 *            Id of the category
	 * @return void returns nothing
	 */
	public void deleteCategoryById(Integer id) throws Exception {
		TraidItItemCategories category = this.categoryDao.getCategoryById(id);
		for (TraidItItemCategories traidItItemCategories : category.getSubCategories()) {
			traidItItemCategories.getParentCategories().remove(category);
			this.categoryDao.saveOrUpdateCategory(traidItItemCategories);
		}
		for (TraidItItemCategories traidItItemCategories : category.getParentCategories()) {
			traidItItemCategories.getSubCategories().remove(category);
			this.categoryDao.saveOrUpdateCategory(traidItItemCategories);
		}
		this.categoryDao.deleteCategory(category);
	}

	/**
	 * 
	 * @throws CategoryNotFoundException
	 * @return ArrayList<CategoriesDto>
	 * @author Thangaraj(KNSTEK)
	 * @since 14-Jan-2015
	 */
	public ArrayList<CategoriesDto> getHomePageCategoriesWeb() throws CategoryNotFoundException {
		System.out.println("inside getAllHomePageCategories()");
		ArrayList<TraidItItemCategories> categories = this.categoryDao.getAllHomePageCategories();
		ArrayList<CategoriesDto> categoryDtos = CategoriesDto.populateCategoriesDto(categories, categories.size());
		categoryDtos = populateHomePageCategorySequences(categoryDtos);
		return categoryDtos;
	}

	/**
	 * 
	 * @param categoryDtos
	 * @return ArrayList<CategoriesDto>
	 * @author Thangaraj(KNSTEK)
	 * @since 14-Jan-2015
	 */
	private ArrayList<CategoriesDto> populateHomePageCategorySequences(ArrayList<CategoriesDto> categoryDtos) {
		ArrayList<CategoriesDto> result = new ArrayList<CategoriesDto>();
		try {
			Map<Integer, Integer> homePageCategorySequences = this.categoryDao.getHomePageCategorySequences();
			for (CategoriesDto category : categoryDtos) {
				try {
					category.setSequenceNo(homePageCategorySequences.get(category.getCategoryId()));
				} catch (Exception e) {
					log.info("Category " + category.getCategoryId() + " doesn't have any sequence mentioned");
				}
				result.add(category);
			}
		} catch (HomePageCategorySequenceNotFoundException e) {
			log.info("No Home Page Category Sequence Found.");
			result.addAll(categoryDtos);
		}
		return result;
	}

	/**
	 * Returns a list of all categories marked as homepage categories
	 * <p>
	 * Returns an ArrayList<CategoriesDto> consisting of list of categories
	 * which are marked as home page categories along with its list size and
	 * total no of inventories falling under the specific category.
	 * </p>
	 * 
	 * @param userId
	 *            current/requesting user id
	 * @param favVendor
	 *            flag stating that filter only favourite vendors Inventories or
	 *            not
	 * @return ArrayList<CategoriesDto> list of homepage categories
	 * @throws CategoryNotFoundException
	 *             if there is no categories found as home page categories
	 * @throws IOException
	 * @throws UserNotFoundException
	 * @author Thangaraj(KNSTEK)
	 * 
	 */
	public ArrayList<CategoriesDto> getAllHomePageCategories(Integer userId, Boolean favVendor, Boolean favCategory,
			Integer maxDistance) throws CategoryNotFoundException, IOException, Exception {
		System.out.println("inside getAllHomePageCategories()");
		ArrayList<TraidItItemCategories> categories = this.categoryDao.getAllHomePageCategories();
		ArrayList<CategoriesDto> categoryDtos = CategoriesDto.populateCategoriesDto(categories, categories.size());
		// populate favourite, has sub category and inventory count informations
		// to each categories
		categoryDtos = this.populateFavHasSubAndInventoryCount(categoryDtos, userId, favVendor, favCategory,
				maxDistance);
		categoryDtos = this.populateHomePageCategorySequences(categoryDtos);
		return categoryDtos;
	}

	public ArrayList<CategoriesDto> getAllCategories() throws CategoryNotFoundException, IOException, Exception {
		System.out.println("inside getAllHomePageCategories()");
		System.out.println("enetered service");
		ArrayList<TraidItItemCategories> categories = this.categoryDao.getAllCategories();
		System.out.println(categories + "service");
		ArrayList<CategoriesDto> categoryDtos = CategoriesDto.populateCategoriesDto(categories, categories.size());
		// populate favourite, has sub category and inventory count informations
		// to each categories
		// categoryDtos =
		// this.populateFavHasSubAndInventoryCount(categoryDtos,userId);
		return categoryDtos;
	}

	/**
	 * returns all first level parent categories
	 * 
	 * @return ArrayList of CategoriesDto objects
	 * @throws CategoryNotFoundException
	 * @author Thangaraj(KNSTEK)
	 * @since 24-09-2014
	 */
	public ArrayList<CategoriesDto> getAllParentCategories(Integer userId, Boolean favCategory)
			throws CategoryNotFoundException {
		ArrayList<TraidItItemCategories> categories = this.categoryDao.getAllParentCategories(userId, favCategory);
		return CategoriesDto.populateCategoriesDto(categories, CategoryDaoImpl.totalCategories);
	}

	/**
	 * Maps given ebay categories to given local category
	 * 
	 * @throws CategoryNotFoundException
	 * @author Thangaraj
	 * @since 24-09-2014
	 */
	@Override
	public void mapEbayCategoryIds(String ebayIds, Integer categoryId) throws CategoryNotFoundException {
		this.categoryDao.deleteAllEbayCategoryMappersByCategoryId(categoryId);
		String[] ebayIdList = ebayIds.split(";");
		for (String ebayId : ebayIdList) {
			if (ebayId == null || ebayId.isEmpty())
				continue;
			TraidItItemCategories category = this.categoryDao.getCategoryById(categoryId);
			EbayCategoryMapper ebayMapper = new EbayCategoryMapper();
			ebayMapper.setEbayId(ebayId);
			ebayMapper.setCategory(category);
			this.categoryDao.saveEbayCategoryMapper(ebayMapper);
		}
	}

	/**
	 * 
	 * @param similarCategoryIds
	 *            String containing list of category id's seperated by ";"
	 * @param categoryId
	 *            Integer
	 * @throws CategoryNotFoundException
	 * @return
	 */
	/*
	 * public void mapSimilarCategoryIds(String similarCategoryIds,Integer
	 * categoryId) throws CategoryNotFoundException{
	 * this.categoryDao.deleteAllEbayCategoryMappersByCategoryId(categoryId);
	 * String[] similarCategoryIdList = similarCategoryIds.split(";");
	 * for(String similarCategoryId : similarCategoryIdList){
	 * if(similarCategoryId == null || similarCategoryId.isEmpty()) continue;
	 * TraidItItemCategories category =
	 * this.categoryDao.getCategoryById(categoryId); EbayCategoryMapper
	 * ebayMapper = new EbayCategoryMapper();
	 * ebayMapper.setEbayId(similarCategoryId);
	 * ebayMapper.setCategory(category);
	 * this.categoryDao.saveEbayCategoryMapper(ebayMapper); } }
	 */

	/**
	 * 
	 * @author Thangaraj
	 * @since 07-10-2014
	 */
	/*
	 * public ArrayList<CategoriesDto>
	 * getBiDirectionalSimilarCategoriesByCategoryId(Integer CategoryId) throws
	 * CategoryNotFoundException{ TraidItItemCategories category =
	 * this.categoryDao.getCategoryById(CategoryId);
	 * ArrayList<TraidItItemCategories> similarCategories = new
	 * ArrayList<TraidItItemCategories>();
	 * similarCategories.addAll(category.getSimilarCategoryOf());
	 * similarCategories.addAll(category.getSimilarCategoryTo()); return
	 * CategoriesDto.populateCategoriesDto(similarCategories,
	 * similarCategories.size()); }
	 */

	/**
	 * 
	 * @author Thangaraj
	 * @since 08-10-2014
	 */
	/*
	 * public ArrayList<CategoriesDto> getSimilarCategoriesByCategoryId(Integer
	 * CategoryId) throws CategoryNotFoundException{ TraidItItemCategories
	 * category = this.categoryDao.getCategoryById(CategoryId);
	 * ArrayList<TraidItItemCategories> similarCategories = new
	 * ArrayList<TraidItItemCategories>();
	 * //similarCategories.addAll(category.getSimilarCategoryOf()); return
	 * CategoriesDto.populateCategoriesDto(similarCategories,
	 * similarCategories.size()); }
	 */

	/**
	 * method to add Favourite Category
	 * 
	 * @author Soujanya
	 * @since september 29,2014
	 * 
	 *        modified by Thangaraj on Dec 22, 2014. added parent and
	 *        grandParent categories of fav categories to simplify listing fav
	 *        categories.
	 */
	public Integer addFavouriteCategory(Integer userId, Integer categoryId) throws Exception {
		log.info("inside addFavouriteCategory()");
		FavouriteCategory favouriteCategory = new FavouriteCategory();
		TraidItUser traidItUser = this.userDao.getUserbyUserId(userId);
		TraidItItemCategories category = this.categoryDao.getCategoryById(categoryId);
		favouriteCategory.setCategory(category);
		favouriteCategory.setUserId(traidItUser);
		Iterator<TraidItItemCategories> it = category.getParentCategories().iterator();
		if (it.hasNext()) {
			TraidItItemCategories parentCategory = it.next();
			favouriteCategory.setParentCategory(parentCategory);
			it = parentCategory.getParentCategories().iterator();
			if (it.hasNext()) {
				TraidItItemCategories grandParentCategory = it.next();
				favouriteCategory.setGrandParentCategory(grandParentCategory);
			}
		}
		Integer result = this.categoryDao.saveOrUpdateFavouriteCategory(favouriteCategory);
		return result;
	}

	/**
	 * method to delete favourite category from favourites
	 * 
	 * @author Soujanya
	 * @since september 29,2014
	 */
	public void deleteFavouriteCategory(Integer categoryId, Integer userId) throws FavouritesNotFoundException {
		log.info("inside deleteFavouriteCategory()");
		FavouriteCategory favouriteCategory = this.categoryDao
				.getFavouriteCategoryByCategoryAndUser(categoryId, userId);
		this.categoryDao.deleteFavouriteCategoryFromDB(favouriteCategory);
	}

	/**
	 * 
	 * @param categoryId
	 * @return
	 */
	public List<Integer> getSubCategoryIdsByCategoryId(Integer userId, Integer categoryId, Boolean favCategory) {
		List<Integer> AllCategoryIds = new ArrayList<Integer>();
		List<Integer> categoryIds = new ArrayList<Integer>();
		try {
			categoryIds = this.categoryDao.getSubCategoryIdsByCategoryId(userId, categoryId, favCategory);
			AllCategoryIds.addAll(categoryIds);
		} catch (CategoryNotFoundException e) {
			// log.info("No sub categories found for category id "+categoryId);
			// System.out.println("No sub categories found for category id "+categoryId);
		}
		for (Integer subCategoryId : categoryIds) {
			List<Integer> temp = this.getSubCategoryIdsByCategoryId(userId, subCategoryId, favCategory);
			AllCategoryIds.addAll(temp);
		}
		return AllCategoryIds;
	}

	/**
	 * Removes all the special characters from the give string
	 * 
	 * @param originalName
	 * @return String sanitised string
	 * @author Thangaraj
	 * @since 04-11-2014
	 */
	private String sanitiseImageName(String originalName) {
		String sanitisedString = Normalizer.normalize(originalName, Normalizer.Form.NFD).replaceAll(
				"[^a-zA-Z0-9|.|-|_| ]", "");
		return sanitisedString;
	}

	/**
	 * Created by Jeevan on Nov 05, 2014 Method to get Similar Categories of
	 * Category
	 * 
	 * @param categoryId
	 * @return
	 * @throws Exception
	 * 
	 *             Procedure:
	 * 
	 *             1. Get all Similar Categories By Category 2. Get all
	 *             Categories where given category is a similar category 3.
	 *             Populate SimilarCategoryDto based on way (Similar or actual)
	 *             4. Send it to Controller
	 * 
	 */
	public ArrayList<SimilarCategoryDto> getSimilarCategoriesofCategory(Integer categoryId)
			throws CategoryNotFoundException {
		log.info("inside getSimilarCategoriesofCategory()");
		ArrayList<SimilarCategoryDto> similarCategoryDtos = new ArrayList<SimilarCategoryDto>();
		ArrayList<SimilarCategory> similarCategories1 = new ArrayList<SimilarCategory>();
		ArrayList<SimilarCategory> similarCategories2 = new ArrayList<SimilarCategory>();
		try {
			similarCategories1 = this.categoryDao.getSimilarCategoriesByCategory(categoryId);
		} catch (CategoryNotFoundException e) {
		}
		try {
			similarCategories2 = this.categoryDao.getSimilarCategoriesBySimilarCategory(categoryId);
		} catch (CategoryNotFoundException e) {
		}
		for (SimilarCategory similarCategory : similarCategories1) {
			SimilarCategoryDto similarCategoryDto = SimilarCategoryDto.populateSimilarCategoryDto(similarCategory);
			CategoriesDto similarCategoriesDto = this.getCategoryById(similarCategoryDto.getSimilarCategory());
			similarCategoryDto.setSimilarCategoryDto(similarCategoriesDto);
			similarCategoryDto.setIsBidirection(null);
			similarCategoryDtos.add(similarCategoryDto);
		}
		for (SimilarCategory similarCategory : similarCategories2) {
			SimilarCategoryDto similarCategoryDto = new SimilarCategoryDto();
			similarCategoryDto.setCategory(similarCategory.getSimilarCategory());
			similarCategoryDto.setSimilarCategory(similarCategory.getCategory());
			similarCategoryDto.setId(similarCategory.getId());
			similarCategoryDto.setIsBidirection(similarCategory.getIsBidirectional());
			CategoriesDto similarCategoriesDto = this.getCategoryById(similarCategoryDto.getSimilarCategory());
			similarCategoryDto.setSimilarCategoryDto(similarCategoriesDto);
			similarCategoryDtos.add(similarCategoryDto);
		}
		return similarCategoryDtos;
	}

	/**
	 * Created by Jeevan on Nov 05, 2014.... Method to get Categories From
	 * Search Result
	 * 
	 * @param categoryName
	 * @return
	 * @throws CategoryNotFoundException
	 */
	public ArrayList<CategoriesDto> getCategoriesFromSearch(String categoryName) throws CategoryNotFoundException {
		log.info("inside getCategoriesFromSearch()");
		ArrayList<TraidItItemCategories> categories = this.categoryDao
				.getCategoriesByCategoryNameforSearch(categoryName);
		ArrayList<CategoriesDto> categoryDtos = new ArrayList<CategoriesDto>();
		for (TraidItItemCategories category : categories) {
			System.out.println("Cat ID " + category.getCategoryId());
			CategoriesDto categoryDto = CategoriesDto.populateCategoriesDto(category);
			System.out.println("cat2 " + categoryDto.getCategoryId() + " " + categoryDto.getCategoryIcon());

			categoryDtos.add(categoryDto);
		}
		return categoryDtos;
	}

	/**
	 * Created by Jeevan on Nov 06, 2014 Method to saveSimilarCategoriesToDB..
	 * 
	 * @param similarCategoryForm
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Integer saveSimilarCategoriesToDB(SimilarCategoryForm similarCategoryForm) throws Exception {
		log.info("inside saveSimilarCategoriesToDB()");
		Integer result = 0;
		List<SimilarCategoryDto> similarCategoryDtos = similarCategoryForm.getSimilarCategories();
		for (SimilarCategoryDto similarCategoryDto : similarCategoryDtos) {
			TraidItItemCategories category = this.categoryDao.getCategoryById(similarCategoryDto.getSimilarCategory());
			if (null != category) {
				SimilarCategory similarCategory = new SimilarCategory();
				similarCategory.setCategory(similarCategoryDto.getCategory());
				similarCategory.setSimilarCategory(similarCategoryDto.getSimilarCategory());
				if (null != similarCategoryDto.getIsBidirection()) {
					similarCategory.setIsBidirectional(similarCategoryDto.getIsBidirection());
				} else {
					similarCategory.setIsBidirectional(false);
				}
				result = this.categoryDao.saveOrUpdateSimilarCategories(similarCategory);
			}
		}
		return result;
	}

	/***
	 * Created by Jeevan on Nov 10, 2014 Method to save Similar Category..
	 * 
	 * @param similarCategoryDto
	 * @return
	 * @throws Exception
	 * 
	 *             Steps:
	 * 
	 *             Needs to check of SimilarCategory mapped to <2..
	 */
	public Integer saveSimilarCategory(SimilarCategoryDto similarCategoryDto) throws Exception {
		log.info("inside saveSimilarCategory()");
		TraidItItemCategories category = this.categoryDao.getCategoryById(similarCategoryDto.getSimilarCategory());
		if (null != category) {
			if (this.getMappedCategoriesCount(similarCategoryDto.getSimilarCategory()) < 2) {
				SimilarCategory similarCategory = new SimilarCategory();
				similarCategory.setCategory(similarCategoryDto.getCategory());
				similarCategory.setSimilarCategory(similarCategoryDto.getSimilarCategory());
				if (null != similarCategoryDto.getIsBidirection()) {
					similarCategory.setIsBidirectional(similarCategoryDto.getIsBidirection());
				} else {
					similarCategory.setIsBidirectional(false);
				}
				Integer result = this.categoryDao.saveOrUpdateSimilarCategories(similarCategory);
				return result;
			} else {
				throw new CategoryMappingLimitReachedException();
			}
		} else {
			throw new CategoryNotFoundException();
		}
	}

	/**
	 * 
	 * Created by Jeevan on November 10, 2014 Method to get Count of No of
	 * Categories a category Mapped to.
	 * 
	 * @param categoryId
	 * @return
	 */
	private Integer getMappedCategoriesCount(Integer categoryId) {
		ArrayList<SimilarCategory> similarCategories1 = new ArrayList<SimilarCategory>();
		ArrayList<SimilarCategory> similarCategories2 = new ArrayList<SimilarCategory>();
		try {
			similarCategories1 = this.categoryDao.getSimilarCategoriesByCategory(categoryId);
		} catch (CategoryNotFoundException e) {
		}
		try {
			similarCategories2 = this.categoryDao.getSimilarCategoriesBySimilarCategory(categoryId);
		} catch (CategoryNotFoundException e) {
		}
		return similarCategories1.size() + similarCategories2.size();
	}

	/**
	 * Created by Jeevan on November 06, 2014 Method to deleteSimilarRecord();
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void deleteSimilarCategory(Integer id) throws Exception {
		log.info("inside deleteSimilarCategory()");
		SimilarCategory similarCategory = this.categoryDao.getSimilarCategoryById(id);
		this.categoryDao.deleteSimilarCategories(similarCategory);
	}

	/**
	 * Created by Jeevan on Nov 10, 2014 Method to edit Similar Category
	 * 
	 * @param id
	 * @param isBidirection
	 * @return
	 * @throws Exception
	 */
	public Integer editSimilarCategoryMapping(Integer id, Boolean isBidirection) throws Exception {
		log.info("inside editSimilarCategoryMapping()");
		SimilarCategory similarCategory = this.categoryDao.getSimilarCategoryById(id);
		similarCategory.setIsBidirectional(isBidirection);
		Integer result = this.categoryDao.saveOrUpdateSimilarCategories(similarCategory);
		return result;
	}

	/**
	 * 
	 * @param userId
	 *            current/requesting user id
	 * @param favVendor
	 *            flag stating that filter only favourite vendors Inventories or
	 *            not
	 * 
	 * @author Soujanya
	 * @since november 13,2014 method to get all favourite categories of user
	 * 
	 * modified by Thangaraj : 04-02-2015 : code and logging optimised a bit
	 */
	public ArrayList<FavouriteCategoryDto> getFavCategoriesOfUser(Integer userId, Boolean favVendor, Integer maxDistance)
			throws FavouritesNotFoundException, Exception {
		log.info("inside getFavCategoriesOfUser()");
		ArrayList<FavouriteCategory> favouriteCategories = this.categoryDao.getFavouriteCategoriesOfUserFromDB(userId);
		ArrayList<FavouriteCategoryDto> favCategoryDtos = new ArrayList<FavouriteCategoryDto>();
		log.info("For each FavouriteCategory, calls {FavouriteCategoryDto.populateFavouriteCategoryDto()} and {inventoryService.getInventoryCount()} and populates data");
		for (FavouriteCategory favouriteCategory : favouriteCategories) {
			FavouriteCategoryDto favCategoryDto = FavouriteCategoryDto.populateFavouriteCategoryDto(favouriteCategory);
			Integer inventoryCount = this.inventoryService.getInventoryCount(favouriteCategory.getCategory().getCategoryId(),userId, favVendor, true, maxDistance);
			favCategoryDto.setTotalNoOfInventories(inventoryCount);
			favCategoryDtos.add(favCategoryDto);
		}
		return favCategoryDtos;
	}

	/**
	 * 
	 * @param categoryDtos
	 * @return void
	 * @author Thangaraj(KNSTEK)
	 * @throws HomePageCategorySequenceNotSavedException
	 * @since 20-Jan-2015
	 */
	@Transactional(rollbackFor = { HomePageCategorySequenceNotSavedException.class })
	public void saveOrUpdateHomePageCategorySequences(ArrayList<CategoriesDto> categoryDtos)
			throws HomePageCategorySequenceNotSavedException {
		// initially flush out all sequence data in database
		this.categoryDao.truncateHomePageCategorySequences();
		// save each home page category sequence data
		for (CategoriesDto categoriesDto : categoryDtos) {
			HomePageCategorySequence sequence = new HomePageCategorySequence();
			TraidItItemCategories category = new TraidItItemCategories();
			category.setCategoryId(categoriesDto.getCategoryId());
			sequence.setCategory(category);
			sequence.setSequenceNo(categoriesDto.getSequenceNo());
			try {
				this.categoryDao.saveOrUpdateHomePageCategorySequences(sequence);
			} catch (HomePageCategorySequenceNotSavedException e) {
				log.error("could not save sequence : " + sequence.getSequenceNo() + " of categoryId : "
						+ sequence.getCategory().getCategoryId());
				throw new HomePageCategorySequenceNotSavedException();
			}
		}
	}


}
