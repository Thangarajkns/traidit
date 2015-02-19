package org.kns.traidit.frontend.category.dto;

import org.kns.traidit.backend.category.model.SimilarCategory;

/**
 * Created by Jeevan on Nov 05, 2014
 * Dto for Similar Categories
 * @author KNS-ACCONTS
 *
 */

public class SimilarCategoryDto {

	private Integer id;
	private Integer category;
	private Integer similarCategory;
	private Boolean isBidirection;
	private CategoriesDto categoryDto;
	private CategoriesDto similarCategoryDto;
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		this.category = category;
	}
	public Integer getSimilarCategory() {
		return similarCategory;
	}
	public void setSimilarCategory(Integer similarCategory) {
		this.similarCategory = similarCategory;
	}
	public Boolean getIsBidirection() {
		return isBidirection;
	}
	public void setIsBidirection(Boolean isBidirection) {
		this.isBidirection = isBidirection;
	}
	
	
	
	
	public CategoriesDto getCategoryDto() {
		return categoryDto;
	}
	public void setCategoryDto(CategoriesDto categoryDto) {
		this.categoryDto = categoryDto;
	}
	public CategoriesDto getSimilarCategoryDto() {
		return similarCategoryDto;
	}
	public void setSimilarCategoryDto(CategoriesDto similarCategoryDto) {
		this.similarCategoryDto = similarCategoryDto;
	}
	/**
	 * Created by Jeevan on Nov 06, 2014
	 * @param similarCategory
	 * @return
	 */
	public static SimilarCategoryDto populateSimilarCategoryDto(SimilarCategory similarCategory){
		SimilarCategoryDto categoryDto=new SimilarCategoryDto();
		categoryDto.setId(similarCategory.getId());
		categoryDto.setCategory(similarCategory.getCategory());
		categoryDto.setSimilarCategory(similarCategory.getSimilarCategory());
		categoryDto.setIsBidirection(similarCategory.getIsBidirectional());
		return categoryDto;
	}
	
	


}
