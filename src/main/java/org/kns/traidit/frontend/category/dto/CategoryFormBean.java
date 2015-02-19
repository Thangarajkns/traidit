package org.kns.traidit.frontend.category.dto;

import java.util.ArrayList;

import javassist.expr.NewArray;

import org.springframework.web.multipart.MultipartFile;

public class CategoryFormBean {

	private Integer categoryId;
	private String categoryName;
	private String eBayIds;
	private MultipartFile categoryIconFile;
	private String categoryIcon;
	private Boolean isHomePageCategory;
	private Integer parentCategory = 0;
	private Integer grandParentCategory = 0;
	private ArrayList<CategoriesDto> similarCategories = new ArrayList<CategoriesDto>() ;
	private String similarCategoryIds;
	
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
	public String geteBayIds() {
		return eBayIds;
	}
	public void seteBayIds(String eBayIds) {
		this.eBayIds = eBayIds;
	}
	public MultipartFile getCategoryIconFile() {
		return categoryIconFile;
	}
	public void setCategoryIconFile(MultipartFile categoryIconFile) {
		this.categoryIconFile = categoryIconFile;
	}
	public String getCategoryIcon() {
		return categoryIcon;
	}
	public void setCategoryIcon(String categoryIcon) {
		this.categoryIcon = categoryIcon;
	}
	public Boolean getIsHomePageCategory() {
		return isHomePageCategory;
	}
	public void setIsHomePageCategory(Boolean isHomePageCategory) {
		this.isHomePageCategory = isHomePageCategory;
	}
	public Integer getParentCategory() {
		return parentCategory;
	}
	public void setParentCategory(Integer parentCategory) {
		this.parentCategory = parentCategory;
	}
	public Integer getGrandParentCategory() {
		return grandParentCategory;
	}
	public void setGrandParentCategory(Integer grandParentCategory) {
		this.grandParentCategory = grandParentCategory;
	}
	/*
	 * populate before return the result
	 */
	public ArrayList<CategoriesDto> getSimilarCategories() {
		String[] similarCategoryList = similarCategoryIds.split(";");
		for(String similarCategoryId : similarCategoryList){
			CategoriesDto similarCategory = new CategoriesDto();
			similarCategory.setCategoryId(Integer.parseInt(similarCategoryId));
			similarCategories.add(similarCategory);
		}
		return similarCategories;
	}
	public String getSimilarCategoryIds() {
		return similarCategoryIds;
	}
	public void setSimilarCategoryIds(String similarCategoryIds) {
		this.similarCategoryIds = similarCategoryIds;
	}
	public void setSimilarCategories(ArrayList<CategoriesDto> similarCategories) {
		this.similarCategories = similarCategories;
	}


	
	
}
