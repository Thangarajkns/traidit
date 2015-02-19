package org.kns.traidit.frontend.category.dto;

import org.kns.traidit.frontend.common.utility.Paginator;

public class CategoryListFormBean {
	private String searchText;
	private Paginator paginator = new Paginator();
	private String sortBy = null;
	private String sortOrder = null;
	
	
	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	public Paginator getPaginator() {
		return paginator;
	}
	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	
}
