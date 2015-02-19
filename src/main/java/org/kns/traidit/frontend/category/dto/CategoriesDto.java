/**
 * Created by     : Soujanya

 * Created Date	  : June 23,2014
 * file Name	  : CategoriesDto.java
 * 
 * Type			  : DTO
 * Database       : traidit
 * 
 * 
 * 
 */

package org.kns.traidit.frontend.category.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.kns.traidit.backend.category.dao.CategoryDaoImpl;
import org.kns.traidit.backend.category.model.TraidItItemCategories;
import org.springframework.web.multipart.MultipartFile;

public class CategoriesDto{

	private Integer categoryId;
	private String categoryName;
	private String categoryIcon;
	private MultipartFile categoryIconFile;
	private Boolean isHomePageCategory;
	private Integer totalCategories;
	private Integer[] subCategoryIds;
	private Integer[] parentCategoryIds;
	private Set<CategoriesDto> subCategories;
	private Set<CategoriesDto> parentCategories=new HashSet<CategoriesDto>();
	private ArrayList<CategoriesDto> similarCategoryTo = new ArrayList<CategoriesDto>();
	private ArrayList<CategoriesDto> similarCategoryOf = new ArrayList<CategoriesDto>();
	private Integer totalHomePageCategories;
	private Integer totalNoOfInventories; // total no of inventories that fall under this category
	private Boolean isFavouriteCategory;
	private Boolean hasSubCategories;
	private String eBayIds; 
	
	//Added by Jeevan on Nov 05, 2014. Thought having it in DTo would be useful
	private String categoryBreadCumb;
	
	//Added by Thangaraj on jan 14,2015: used to maintain home page category sequences.
	private Integer sequenceNo;
	
	

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryIcon() {
		return categoryIcon;
	}

	public void setCategoryIcon(String categoryIcon) {
		this.categoryIcon = categoryIcon;
	}

	public MultipartFile getCategoryIconFile() {
		return categoryIconFile;
	}

	public void setCategoryIconFile(MultipartFile categoryIconFile) {
		this.categoryIconFile = categoryIconFile;
	}

	public Boolean getIsHomePageCategory() {
		return isHomePageCategory;
	}

	public void setIsHomePageCategory(Boolean isHomePageCategory) {
		this.isHomePageCategory = isHomePageCategory;
	}

	public Integer getTotalCategories() {
		return totalCategories;
	}

	public void setTotalCategories(Integer totalCategories) {
		this.totalCategories = totalCategories;
	}

	public Integer[] getSubCategoryIds() {
		return subCategoryIds;
	}

	public void setSubCategoryIds(Integer[] subCategoryIds) {
		this.subCategoryIds = subCategoryIds;
	}

	public Integer[] getParentCategoryIds() {
		return parentCategoryIds;
	}

	public void setParentCategoryIds(Integer[] parentCategoryIds) {
		this.parentCategoryIds = parentCategoryIds;
	}

	public Set<CategoriesDto> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(Set<CategoriesDto> subCategories) {
		this.subCategories = subCategories;
	}

	public Set<CategoriesDto> getParentCategories() {
		return parentCategories;
	}
	
	public ArrayList<CategoriesDto> getSimilarCategoryTo() {
		return similarCategoryTo;
	}

	public void setSimilarCategoryTo(ArrayList<CategoriesDto> similarCategoryTo) {
		this.similarCategoryTo = similarCategoryTo;
	}

	public ArrayList<CategoriesDto> getSimilarCategoryOf() {
		return similarCategoryOf;
	}

	public void setSimilarCategoryOf(ArrayList<CategoriesDto> similarCategoryOf) {
		this.similarCategoryOf = similarCategoryOf;
	}

	public Boolean getHasSubCategories() {
		return hasSubCategories;
	}

	public void setHasSubCategories(Boolean hasSubCategories) {
		this.hasSubCategories = hasSubCategories;
	}

	public Boolean getIsFavouriteCategory() {
		return isFavouriteCategory;
	}

	public void setIsFavouriteCategory(Boolean isFavouriteCategory) {
		this.isFavouriteCategory = isFavouriteCategory;
	}
	
	
	
	
	
	

	public String getCategoryBreadCumb() {
		return categoryBreadCumb;
	}

	public void setCategoryBreadCumb(String categoryBreadCumb) {
		this.categoryBreadCumb = categoryBreadCumb;
	}

	public void setParentCategories(Set<CategoriesDto> parentCategories) {
		//populate parentCategory id's to Integer array which is used in view page
		Integer[] parentCategoryIds = new Integer[parentCategories.size()];
		Integer i = 0;
		for (CategoriesDto traidItItemCategories : parentCategories) {
			parentCategoryIds[i++] = traidItItemCategories.getCategoryId();
		}
		this.setParentCategoryIds(parentCategoryIds);
		this.parentCategories = parentCategories;
	}
	
	public Integer getTotalHomePageCategories() {
		return totalHomePageCategories;
	}

	public void setTotalHomePageCategories(Integer totalHomePageCategories) {
		this.totalHomePageCategories = totalHomePageCategories;
	}

	public Integer getTotalNoOfInventories() {
		return totalNoOfInventories;
	}

	public void setTotalNoOfInventories(Integer totalNoOfInventories) {
		this.totalNoOfInventories = totalNoOfInventories;
	}
	
	public String geteBayIds() {
		return eBayIds;
	}

	public void seteBayIds(String eBayIds) {
		this.eBayIds = eBayIds;
	}
	
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	

	/**
	 * Populates all the data to a DTO object (CategoriesDto) from a given category Model object(TraidItItemCategories) 
	 * @param categories Category model object
	 * @return CategoriesDto --Populated DTO object of given category(CategoriesDto)
	 */
	public static CategoriesDto populateCategoriesDto(
			TraidItItemCategories categories) {
		//populate all current level category values from model to DTO Object
		CategoriesDto categoriesDto = CategoriesDto.populateLazyCategoriesDto(categories); 
		
		//populate all sub category values from model to DTO Object
		Set<TraidItItemCategories> subCategories = categories.getSubCategories();
		Set<CategoriesDto> subCategoriesDto = new HashSet<CategoriesDto>();
		for (TraidItItemCategories subCategory : subCategories) {
			CategoriesDto categoryDto = CategoriesDto.populateLazyCategoriesDto(subCategory);
			subCategoriesDto.add(categoryDto);
		}
		categoriesDto.setSubCategories(subCategoriesDto);
		
		//populate all parent category values from model to DTO Object
		Set<TraidItItemCategories> parentCategories = categories.getParentCategories();
		Set<CategoriesDto> parentCategoriesDto = new HashSet<CategoriesDto>();
		for (TraidItItemCategories parentCategory : parentCategories) {
			CategoriesDto parentCategoryDto = CategoriesDto.populateLazyCategoriesDto(parentCategory);
			parentCategoriesDto.add(parentCategoryDto);
		}
		categoriesDto.setParentCategories(parentCategoriesDto);
		
		//populate subCategory id's to Integer array which is used in view page
		Integer[] subCategoriesids = new Integer[subCategories.size()];
		int i = 0;
		for (TraidItItemCategories traidItItemCategories : subCategories) {
			subCategoriesids[i++] = traidItItemCategories.getCategoryId();
		}
		categoriesDto.setSubCategoryIds(subCategoriesids);

//		commented as it is moved to setter method
		/*//populate parentCategory id's to Integer array which is used in view page
		Integer[] parentCategoryIds = new Integer[parentCategories.size()];
		i = 0;
		for (TraidItItemCategories traidItItemCategories : parentCategories) {
			parentCategoryIds[i++] = traidItItemCategories.getCategoryId();
		}
		categoriesDto.setParentCategoryIds(parentCategoryIds);*/
		Integer totalCategories=CategoryDaoImpl.totalHomePageCategories;
	
		categoriesDto.setTotalHomePageCategories(totalCategories);
		return categoriesDto;

	}

	/**
	 * Populates only the data to a DTO object (CategoriesDto) from a given category Model object(TraidItItemCategories) 
	 * @param categories Category model object
	 * @return CategoriesDto --Populated DTO object of category(CategoriesDto)
	 */
	public static ArrayList<CategoriesDto> populateCategoriesDto(Collection<TraidItItemCategories> categories, Integer totalCategories) {
		ArrayList<CategoriesDto> categoriesDto = new ArrayList<CategoriesDto>();
		if(!categories.isEmpty())
			for (TraidItItemCategories category : categories) {
				CategoriesDto categoryDto = populateCategoriesDto(category);
				categoryDto.setTotalCategories(totalCategories);
				categoriesDto.add(categoryDto);
			}
		return categoriesDto;

	}

	/**
	 * Populates all the data except sub and parent categories to a DTO object (CategoriesDto) from a given category Model object(TraidItItemCategories) 
	 * @param categories Category model object
	 * @return CategoriesDto --Populated DTO object without sub and parent categories data of given category(CategoriesDto)
	 */
	public static CategoriesDto populateLazyCategoriesDto(
			TraidItItemCategories categories) {
		CategoriesDto categoriesDto = new CategoriesDto(); //create new DTO Object
		
		//populate all values from model to DTO Object
		categoriesDto.setCategoryId(categories.getCategoryId());
		categoriesDto.setCategoryName(categories.getCategoryName());
		categoriesDto.setCategoryIcon(categories.getCategoryIcon());
		categoriesDto.setIsHomePageCategory(categories.getIsHomePageCategory());
		
		//populate subCategories and parentCategories with empty set of categories
		Set<CategoriesDto> subCategories = new HashSet<CategoriesDto>();
		categoriesDto.setParentCategories(subCategories);
		categoriesDto.setSubCategories(subCategories);

		//populate subCategory and parentCategory id's with empty array
		Integer[] categoriesids = new Integer[subCategories.size()];
		categoriesDto.setSubCategoryIds(categoriesids);
		categoriesDto.setParentCategoryIds(categoriesids);
		
		return categoriesDto;

	}


	 public static Comparator<CategoriesDto> SequenceComparator = new Comparator<CategoriesDto>() {

			public int compare(CategoriesDto c1, CategoriesDto c2) {
			   Integer categorySequence1 = c1.getSequenceNo();
			   Integer categorySequence2 = c2.getSequenceNo();
			   if(categorySequence1 == null || categorySequence2 == null)
				   return 0;
			   //ascending order
			   return categorySequence1.compareTo(categorySequence2);

			   //descending order
			   //return StudentName2.compareTo(StudentName1);
		    }};
}
