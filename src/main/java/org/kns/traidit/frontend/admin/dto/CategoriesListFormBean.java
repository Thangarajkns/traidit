package org.kns.traidit.frontend.admin.dto;

import org.kns.traidit.frontend.category.dto.CategoriesDto;

public class CategoriesListFormBean {
	private CategoriesDto category;
	private String submitOption;
	
	public CategoriesDto getCategory() {
		return category;
	}
	public void setCategory(CategoriesDto category) {
		this.category = category;
	}
	public String getSubmitOption() {
		return submitOption;
	}
	public void setSubmitOption(String submitOption) {
		this.submitOption = submitOption;
	}
	
	
}
