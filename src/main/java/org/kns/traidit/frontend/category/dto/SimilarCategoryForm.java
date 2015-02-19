package org.kns.traidit.frontend.category.dto;

import org.springframework.util.AutoPopulatingList;

/**
 * Created by Jeevan on Nov 06, 2014
 * Class to percorm Autopopulating saving of Similar Categories
 * 
 * 
 * @author KNS-ACCONTS
 *
 */

public class SimilarCategoryForm {
	
	private AutoPopulatingList<SimilarCategoryDto> similarCategories=new AutoPopulatingList<SimilarCategoryDto>(SimilarCategoryDto.class);

	public AutoPopulatingList<SimilarCategoryDto> getSimilarCategories() {
		return similarCategories;
	}

	public void setSimilarCategories(
			AutoPopulatingList<SimilarCategoryDto> similarCategories) {
		this.similarCategories = similarCategories;
	}
	
	
	
}
