package org.kns.traidit.frontend.ebay.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.kns.traidit.backend.category.dao.CategoryDao;
import org.kns.traidit.backend.category.exception.CategoryNotFoundException;
import org.kns.traidit.backend.category.model.TraidItItemCategories;
import org.kns.traidit.backend.item.dao.ItemDao;
import org.kns.traidit.backend.item.exception.ItemNotFoundException;
import org.kns.traidit.backend.item.model.TraidItItems;
import org.kns.traidit.frontend.category.dto.CategoriesDto;
import org.kns.traidit.frontend.item.dto.ItemsDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * 
 * Created by Jeevan on September 23, 2014
 * Service related class to interact with Ebay API and get ITem Related Details
 * 
 * Over Head:
 * 
 * Only 5000 Requests per day ..
 * Made use of Finding API and Shopping API
 *
 */

@Transactional
@Service("ebayAPIService")
public class EbayAPIServiceImpl implements EbayAPIService {

	private static Logger log=Logger.getLogger(EbayAPIServiceImpl.class);
	
	
	@Resource(name="itemDao")
	private ItemDao itemDao;
	
	@Resource(name="categoryDao")
	private CategoryDao categoryDao;
	
	
	/**
	 * 
	* Created by Jeevan on 23-Sep-2014 7:31:02 pm	
	* Original method created by Thangaraj / Soujanya
	*  Method for: gettingItemDetailsFromEbayByUPC
	* @param upc
	* @return
	* 
	* Steps:
	* 1. Get Item Id from UPC using Finding API
	* 2. Get Complete ITem Details Using Shopping API by Item Id and populate Item Dto
	 * @throws ItemNotFoundException 
	* 
	* 
	 */
	public ItemsDto getItemDetailsFromEbayByUPC(String upc) throws ItemNotFoundException{
		log.info("inside getItemDetailsFromEbayByUPC()");
		ItemsDto itemDto;
		try{
			String itemId=this.getItemIdByUPC(upc);
			itemDto=this.getItemDetailsFromItemId(itemId);
		}
		catch(Exception e){
			//send item not found on exception
			throw new ItemNotFoundException();
		}
		return itemDto;
	}
	
	
	
	/**Original method created by Thangaraj / Soujanya
	 * 
	* Created by Jeevan on 23-Sep-2014 7:34:18 pm	
	*  Method for:
	* @param upc
	* @return
	* @throws Exception
	 */
	private String getItemIdByUPC(String upc)throws Exception{
		log.info("inside getItemByUPC()");
		String url = "http://svcs.ebay.com/services/search/FindingService/v1" +
				"?OPERATION-NAME=findItemsByProduct" +
				"&SERVICE-VERSION=1.0.0" +
				"&SECURITY-APPNAME=kns73ad0d-01c8-436e-a495-c335d8d71ad" +
				"&RESPONSE-DATA-FORMAT=XML" +
				"&REST-PAYLOAD" +
				"&paginationInput.entriesPerPage=2" +
				"&productId.@type=UPC" +
				"&productId="+upc;
		System.out.println(url);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(new URL(url).openStream());
		XPath xPath =  XPathFactory.newInstance().newXPath();
        String expression = "/findItemsByProductResponse/searchResult/item[1]/itemId";
        String itemId = xPath.compile(expression).evaluate(document);
        if(null==itemId|| itemId.trim()==""){
        	throw new ItemNotFoundException();
        }
        return itemId;
	}
	
	
	
	/**
	 * Original method created by Thangaraj / Soujanya
	* Created by Jeevan on 23-Sep-2014 7:43:31 pm	
	*  Method for: gettingItemDetailsFromItemId
	* @param itemId
	* @return
	* @throws Exception
	 */
	
	private ItemsDto getItemDetailsFromItemId(String itemId)throws Exception{
		log.info("inside getItemDetailsFromItemId()");
		ItemsDto itemDto=new ItemsDto();
		String url = "http://open.api.ebay.com/shopping" +
        		"?callname=GetSingleItem" +
        		"&responseencoding=XML" +
        		"&appid=kns73ad0d-01c8-436e-a495-c335d8d71ad" +
        		"&siteid=0" +
        		"&version=515" +
        		"&ItemID="+itemId +
        		"&IncludeSelector=Variations,ItemSpecifics,Details,Description,TextDescription,ShippingCosts,Compatibility";
		System.out.println(url);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(new URL(url).openStream());
        XPath xPath =  XPathFactory.newInstance().newXPath();
        String expression1 = "/GetSingleItemResponse/Item/Description";
        String desc = xPath.compile(expression1).evaluate(document);       
        String expression3= "/GetSingleItemResponse/Item/GalleryURL";
        String galleryUrl= xPath.compile(expression3).evaluate(document);       
        String expression4= "/GetSingleItemResponse/Item/PictureURL";
        String pictureUrl= xPath.compile(expression4).evaluate(document);
        String expression5= "/GetSingleItemResponse/Item/Title";
        String title= xPath.compile(expression5).evaluate(document);
        String expression6= "/GetSingleItemResponse/Item/PrimaryCategoryName";
        String expression8="/GetSingleItemResponse/Item/PrimaryCategoryID";
        String category= xPath.compile(expression6).evaluate(document);
        String categoryId=xPath.compile(expression8).evaluate(document);
        String expression7= "/GetSingleItemResponse/Item/ItemSpecifics/NameValueList[1]" ;
        XPathExpression exp = xPath.compile("//NameValueList");
        NodeList nl = (NodeList)exp.evaluate(document, XPathConstants.NODESET);       
        HashMap<String, String> newMap = new HashMap<String, String>();
        for (int i = 0; i < nl.getLength(); i++) {
        	 Element ele = (Element) nl.item(i);       	 
        	 newMap.put(ele.getElementsByTagName("Name").item(0)
             .getTextContent(),ele.getElementsByTagName("Value").item(0)
             .getTextContent());        	 
        }
        String manufacturer = (String) newMap.get("Brand");          
        String details= xPath.compile(expression7).evaluate(document);       
        CategoriesDto categoryDto=new CategoriesDto();
        categoryDto.setCategoryName(category);
        itemDto.setDescription(desc);
        itemDto.setItemName(title);
        itemDto.setPhotos(pictureUrl);
        itemDto.setCategoryId(categoryDto);
        ArrayList<String> photoList=new ArrayList<String>();
        photoList = new ArrayList<String>();
        photoList.add(pictureUrl);
        photoList.add(galleryUrl);
        itemDto.setPhotoList(photoList);
        itemDto.setManufacturer(manufacturer);
        itemDto.setDetails(details);
        itemDto.setCategoryNo(categoryId);
        return itemDto;
	}
	
	
	
	
	/**
	 * 
	* Created by Jeevan on 23-Sep-2014 8:00:58 pm	
	*  Method for: Getting ItemDto bY UPC
	* @param upc
	* @return
	 * @throws ItemNotFoundException 
	* @throws Exception
	* 
	* Steps:
	* 1. Check if Item  Exists By UPC.
	*    a. If YEs goto Steo 5
	*    b. If not goto step 2.
	*  2. From Ebay API get Item Dto
	*  3. From Category IDS Obtained, get relavent local category
	*  4. FRom Local category, get its Parent Category and its Parent
	*  5. Obtain Category / Subcategories or Parent Categories
	*  6. Populate Item Dto and return to controller
	* 
	* 
	* 
	 */
	public ItemsDto getItemByUPC(String upc) throws ItemNotFoundException{
		log.info("inside getItemsByUPC()");
		ItemsDto itemDto=null;
		try{
			TraidItItems item=this.itemDao.getItemByUPC(upc);
			itemDto=ItemsDto.populateItemsDto(item);
			itemDto=this.populateCategoryHierarcy(itemDto, item.getCategoryId());
			return itemDto;
		}
		catch(ItemNotFoundException e){
			itemDto=this.getItemDetailsFromEbayByUPC(upc); 
			//Get Category By Ebay Category Number
			try{
				TraidItItemCategories category=this.categoryDao.getCategoryByEbayCategoryId(itemDto.getCategoryNo());
				itemDto=this.populateCategoryHierarcy(itemDto, category);	
			}
			catch(CategoryNotFoundException e3){
				TraidItItemCategories category = null;
				try {
					category = this.categoryDao.getCategoryByName("Others");//hardcoded for now
				} catch (CategoryNotFoundException e1) {} 
				itemDto=this.populateCategoryHierarcy(itemDto, category);
			}
			return itemDto;
		}
	}
	
	
	
	
	
	/**
	 * 
	* Created by Jeevan on 23-Sep-2014 9:09:58 pm	
	*  Method for: Suport Method for Populating Category Hierarchy
	* @param itemDto
	* @param category
	* @return
	 */	
	private ItemsDto populateCategoryHierarcy(ItemsDto itemDto,TraidItItemCategories category){
		if(null!=category.getParentCategories() && !category.getParentCategories().isEmpty()){
			TraidItItemCategories parentCategory=category.getParentCategories().iterator().next();
			if(null!=parentCategory.getParentCategories() && !parentCategory.getParentCategories().isEmpty()){
				TraidItItemCategories grandParentCategory=parentCategory.getParentCategories().iterator().next();
				itemDto.setCategoryId(CategoriesDto.populateCategoriesDto(grandParentCategory));   //category, Sub category, Sub sub category
				itemDto.setSubCategory(CategoriesDto.populateCategoriesDto(parentCategory));
				itemDto.setSubSubCategory(CategoriesDto.populateCategoriesDto(category));
			}
			else{
		         //category sub category
				itemDto.setCategoryId(CategoriesDto.populateCategoriesDto(parentCategory));
				itemDto.setSubCategory(CategoriesDto.populateCategoriesDto(category));
			}
		}
		else{
			itemDto.setCategoryId(CategoriesDto.populateCategoriesDto(category));    //category
		}
		
		return itemDto;
	}
	
	
	
	
}
