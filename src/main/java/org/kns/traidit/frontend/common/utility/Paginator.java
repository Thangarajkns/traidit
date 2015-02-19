package org.kns.traidit.frontend.common.utility;

public class Paginator {

	private Integer totalNoOfItems;
	private Integer noOfItemsPerPage = 10;
	private Integer currentPageNo = 0;
	
	
	public Integer getTotalNoOfItems() {
		return totalNoOfItems;
	}
	public void setTotalNoOfItems(Integer totalNoOfItems) {
		this.totalNoOfItems = totalNoOfItems;
	}
	public Integer getNoOfItemsPerPage() {
		return noOfItemsPerPage;
	}
	public void setNoOfItemsPerPage(Integer noOfItemsPerPage) {
		this.noOfItemsPerPage = noOfItemsPerPage;
	}
	public Integer getCurrentPageNo() {
		return currentPageNo;
	}
	public void setCurrentPageNo(Integer currentPageNo) {
		this.currentPageNo = currentPageNo;
	}
	public Integer getStarttIndex() {
		return this.noOfItemsPerPage * this.currentPageNo;
	}
	public Integer getLastIndex() {
		return this.noOfItemsPerPage * (this.currentPageNo + 1 );
	}
	public Integer getTotalNoOfPages() {
		Integer pages = this.totalNoOfItems / this.noOfItemsPerPage; 
		if(this.totalNoOfItems % this.noOfItemsPerPage != 0)
			pages++;
		return pages;
	}
	
	@Override
	public String toString() {

		String userListFormBean = "Paginator";
		userListFormBean += "{";
		userListFormBean += "{totalNoOfItems:"+this.totalNoOfItems+"}";
		userListFormBean += "{noOfItemsPerPage:"+this.noOfItemsPerPage+"}";
		userListFormBean += "{currentPageNo:"+this.currentPageNo+"}";
		userListFormBean += "}";
		return userListFormBean;
	}
	
	
}
