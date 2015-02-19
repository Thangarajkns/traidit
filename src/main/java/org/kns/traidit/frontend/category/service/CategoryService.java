/**
 *  Created by     : Soujanya
 * Created Date	  : June 26th,2014
 * file Name	  : CategoryService.java
 * Purpose		  : Service

 * 
 * 
 */




package org.kns.traidit.frontend.category.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kns.traidit.backend.category.exception.CategoryNotFoundException;
import org.kns.traidit.backend.category.exception.HomePageCategorySequenceNotSavedException;
import org.kns.traidit.backend.category.model.TraidItItemCategories;
import org.kns.traidit.backend.user.exception.FavouritesNotFoundException;
import org.kns.traidit.frontend.category.dto.CategoriesDto;
import org.kns.traidit.frontend.category.dto.FavouriteCategoryDto;
import org.kns.traidit.frontend.category.dto.SimilarCategoryDto;
import org.kns.traidit.frontend.category.dto.SimilarCategoryForm;

public interface CategoryService {
	
	public CategoriesDto getCategoryById(Integer id) throws CategoryNotFoundException;
	public ArrayList<CategoriesDto> getSubCategoryWithFavourites(Integer id,Integer userId,Boolean favVendor,Boolean favCategory,Integer maxDistance) throws CategoryNotFoundException,Exception;
	public CategoriesDto getCategoryByName(String name) throws CategoryNotFoundException;
	//public ArrayList<CategoriesDto> getAllCategories(Integer pageNo,Integer pageSize, Integer userId) throws Exception;
	public ArrayList<CategoriesDto> getAllCategoriesWeb(Integer pageNo,Integer pageSize,String sortBy,String sortOrder,String searchText) throws Exception;
	public Integer saveOrUpdateCategory(CategoriesDto categoriesDto) throws Exception;
	public ArrayList<TraidItItemCategories> getCategoriesByListOfIds(Integer[] categoryIds)throws Exception;
	public void deleteCategoryById(Integer id)throws Exception;
	public ArrayList<CategoriesDto> getHomePageCategoriesWeb() throws CategoryNotFoundException;
	public ArrayList<CategoriesDto> getAllHomePageCategories(Integer userId,Boolean favVendor,Boolean favCategory,Integer maxDistance) throws CategoryNotFoundException, IOException, Exception;
	public ArrayList<CategoriesDto> getAllParentCategories(Integer userId,Boolean favCategory) throws CategoryNotFoundException;
	public void mapEbayCategoryIds(String ebayIds,Integer categoryId) throws CategoryNotFoundException;
	//public void mapSimilarCategoryIds(String similarCategoryIds,Integer categoryId) throws CategoryNotFoundException;
	//public ArrayList<CategoriesDto> getSimilarCategoriesByCategoryId(Integer CategoryId) throws CategoryNotFoundException;
	//public ArrayList<CategoriesDto> getBiDirectionalSimilarCategoriesByCategoryId(Integer CategoryId) throws CategoryNotFoundException;
    public Integer addFavouriteCategory(Integer userId, Integer categoryId) throws Exception;
	public void deleteFavouriteCategory(Integer categoryId, Integer userId) throws FavouritesNotFoundException;
	
    public List<Integer> getSubCategoryIdsByCategoryId(Integer userId, Integer categoryId,Boolean favCategory);
	public ArrayList<CategoriesDto> populateFavHasSubAndInventoryCount(ArrayList<CategoriesDto> categories,Integer userId,Boolean favVendor,Boolean favCategory,Integer maxDistance) throws Exception;
	public ArrayList<SimilarCategoryDto> getSimilarCategoriesofCategory(Integer categoryId)throws CategoryNotFoundException;
	
	//Added by Jeevan
	public ArrayList<CategoriesDto> getCategoriesFromSearch(String categoryName)throws CategoryNotFoundException;
	public Integer saveSimilarCategoriesToDB(SimilarCategoryForm similarCategoryForm)throws Exception;
	public Integer saveSimilarCategory(SimilarCategoryDto similarCategoryDto) throws Exception;
	public void deleteSimilarCategory(Integer id)throws Exception;
	public Integer editSimilarCategoryMapping(Integer id,Boolean isBidirection)throws Exception;
	public ArrayList<FavouriteCategoryDto> getFavCategoriesOfUser(Integer userId,Boolean favVendor,Integer maxDistance) throws FavouritesNotFoundException, Exception;
	public ArrayList<CategoriesDto> getAllCategories() throws CategoryNotFoundException, IOException, Exception;
	public void saveOrUpdateHomePageCategorySequences(ArrayList<CategoriesDto> categoryDtos) throws HomePageCategorySequenceNotSavedException;
	
}
