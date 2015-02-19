package org.kns.traidit.frontend.category.dto;

/**
 * Created by Jeevan on Spetember 24, 2014
 * as Saving Ebay Categories as mapping makes much sense
 * @author KNS-ACCONTS
 *
 */
public class EbayCategoryMapperDto {

	
	private Integer id;
	private CategoriesDto category;
	private String ebayId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public CategoriesDto getCategory() {
		return category;
	}
	public void setCategory(CategoriesDto category) {
		this.category = category;
	}
	public String getEbayId() {
		return ebayId;
	}
	public void setEbayId(String ebayId) {
		this.ebayId = ebayId;
	}




}
