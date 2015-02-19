package org.kns.traidit.backend.category.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kns.traidit.backend.category.exception.CategoryNotFoundException;
import org.kns.traidit.backend.category.exception.CategoryNotSavedOrUpdatedException;
import org.kns.traidit.backend.category.exception.FavouriteCategoryNotFound;
import org.kns.traidit.backend.category.exception.HomePageCategorySequenceNotFoundException;
import org.kns.traidit.backend.category.exception.HomePageCategorySequenceNotSavedException;
import org.kns.traidit.backend.category.model.EbayCategoryMapper;
import org.kns.traidit.backend.category.model.HomePageCategorySequence;
import org.kns.traidit.backend.category.model.SimilarCategory;
import org.kns.traidit.backend.category.model.TraidItItemCategories;
import org.kns.traidit.backend.user.exception.FavouritesNotFoundException;
import org.kns.traidit.backend.user.exception.FavouritesNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.favourites.model.FavouriteCategory;


/*
 * created by Soujanya on 19th June,2014
 * Interface for category related activities
 */

public interface CategoryDao {
	
	public Integer saveOrUpdateCategory(TraidItItemCategories category) throws CategoryNotSavedOrUpdatedException;
	public TraidItItemCategories getCategoryById(Integer id) throws CategoryNotFoundException;
	public TraidItItemCategories getCategoryByName(String name) throws CategoryNotFoundException;
	public ArrayList<TraidItItemCategories> getAllCategories(Integer pageNo,Integer pageSize,String sortBy,String sortOrder,String searchText) throws CategoryNotFoundException;
	public void deleteCategory(TraidItItemCategories category) throws CategoryNotFoundException;
	public ArrayList<TraidItItemCategories> getCategoriesByListOfIds(Integer[] categoryIds)throws CategoryNotFoundException;
	public ArrayList<TraidItItemCategories> getAllHomePageCategories() throws CategoryNotFoundException;
	public ArrayList<TraidItItemCategories> getFavouriteCategoriesOfUser(Integer userId) throws FavouriteCategoryNotFound;
	public ArrayList<TraidItItemCategories> getAllParentCategories(Integer userId,Boolean favCategory) throws CategoryNotFoundException;
	public TraidItItemCategories getCategoryByEbayCategoryId(String ebayId)throws CategoryNotFoundException;
	public ArrayList<String> getEBayCategoryIdsByCategoryId(Integer categoryId);
	public Integer saveEbayCategoryMapper(EbayCategoryMapper ebayMapper);
	public void deleteAllEbayCategoryMappersByCategoryId(Integer categoryId);

	public Integer saveOrUpdateFavouriteCategory(FavouriteCategory favouriteCategory) throws FavouritesNotSavedOrUpdatedException;
	public FavouriteCategory getFavouriteCategoryById(Integer favCategoryId) throws FavouritesNotFoundException;
	public void deleteFavouriteCategoryFromDB(FavouriteCategory favouriteCategory) throws FavouritesNotFoundException;

	public List<Integer> getSubCategoryIdsByCategoryId(Integer userId,Integer categoryId,Boolean favCategory) throws CategoryNotFoundException;
	public FavouriteCategory getFavouriteCategoryByCategoryAndUser(Integer categoryId, Integer userId) throws FavouritesNotFoundException;
	
	
	public Integer saveOrUpdateSimilarCategories(SimilarCategory similarCategory)throws Exception;
	public void deleteSimilarCategories(SimilarCategory similarCategory)throws Exception;
	public ArrayList<SimilarCategory> getSimilarCategoriesByCategory(Integer categoryId)throws CategoryNotFoundException;
	public ArrayList<SimilarCategory> getSimilarCategoriesBySimilarCategory(Integer similarCategoryId)throws CategoryNotFoundException;
	
    //Added by Jeevan on Nov 05, 2014
	public ArrayList<TraidItItemCategories> getCategoriesByCategoryNameforSearch(String categoryName) throws CategoryNotFoundException;
	public SimilarCategory getSimilarCategoryById(Integer id)throws CategoryNotFoundException;
    public ArrayList<FavouriteCategory> getFavouriteCategoriesOfUserFromDB(Integer userId) throws FavouritesNotFoundException;
    public Integer getCountOfFavCategoriesOfUser(Integer userId) throws FavouritesNotFoundException;
    public ArrayList<TraidItItemCategories> getAllCategories() throws CategoryNotFoundException;
    public Map<Integer,Integer> getHomePageCategorySequences() throws HomePageCategorySequenceNotFoundException;
    public Integer saveOrUpdateHomePageCategorySequences(HomePageCategorySequence sequence) throws HomePageCategorySequenceNotSavedException;
    public void truncateHomePageCategorySequences();
	
}
