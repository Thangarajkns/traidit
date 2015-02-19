package org.kns.traidit.frontend.inventory.dto;


import org.kns.traidit.frontend.common.utility.Paginator;

public class InventoryFilterFormBeanDto {

	private Integer vendorId;
	private String itemSearcText;
	private Paginator paginator = new Paginator();
	private String optionChoosed;
	private String sortBy = null;
	private String sortOrder = null;

	
	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getItemSearcText() {
		return itemSearcText;
	}

	public void setItemSearcText(String itemSearcText) {
		this.itemSearcText = itemSearcText;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public String getOptionChoosed() {
		return optionChoosed;
	}

	public void setOptionChoosed(String optionChoosed) {
		this.optionChoosed = optionChoosed;
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
