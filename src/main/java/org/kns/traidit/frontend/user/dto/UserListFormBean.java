package org.kns.traidit.frontend.user.dto;

import java.util.ArrayList;

import org.kns.traidit.frontend.common.utility.Paginator;

public class UserListFormBean {
	private String searchText;
	private Paginator paginator = new Paginator();
	private String sortBy = null;
	private String sortOrder = null;
	private ArrayList<String> fieldsToBeShown = new ArrayList<String>();	
	private ArrayList<String> fields = new ArrayList<String>();	

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

	public ArrayList<String> getFieldsToBeShown() {
		return fieldsToBeShown;
	}

	public void setFieldsToBeShown(ArrayList<String> fieldsToBeShown) {
		this.fieldsToBeShown = fieldsToBeShown;
	}

	public ArrayList<String> getFields() {
		return fields;
	}

	public void setFields(ArrayList<String> fields) {
		this.fields = fields;
	}
	
	@Override
	public String toString() {
		String userListFormBean = "UserListFormBean";
		userListFormBean += "{";
		userListFormBean += "{searchText:"+this.searchText+"}";
		userListFormBean += "{sortBy:"+this.sortBy+"}";
		userListFormBean += "{sortOrder:"+this.sortOrder+"}";
		userListFormBean += "{fields:"+this.fields+"}";
		userListFormBean += "{fieldsToBeShown:"+this.fieldsToBeShown+"}";
		userListFormBean += "{paginator:"+this.paginator.toString()+"}";
		userListFormBean += "}";
		return userListFormBean;
	}
	
}
