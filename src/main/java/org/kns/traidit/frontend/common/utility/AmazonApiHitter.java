package org.kns.traidit.frontend.common.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.kns.traidit.backend.item.exception.ItemNotFoundException;
import org.kns.traidit.frontend.category.dto.CategoriesDto;
import org.kns.traidit.frontend.item.dto.ItemsDto;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AmazonApiHitter {

	/*public static void main(String[] args) throws Exception{

		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//dd/MM/yyyy
		System.out.println(sdfDate.format(new Date()));
	    
        System.out.println("*************************");

	   // System.out.println("ASIN Id :" + getAmazonItemIdFromUPC("885909727469"));
	    
        System.out.println("*************************");
		System.out.println(sdfDate.format(new Date()));
        System.out.println("*************************");
	    
	    System.out.println("Ebay item description :" + getEBayItemIdFromUPC("885909727469"));
	    
	    
	    
        System.out.println("*************************");
		System.out.println(sdfDate.format(new Date()));
	} */
	
	private static ArrayList<String> photolist;

	/*private static String getAmazonItemIdFromUPC(String ItemId) throws Exception{

		Signature signer = new Signature();
		Map<String,String> params = new HashMap<String,String>(); 
		params.put("Service", "AWSECommerceService"); 
		params.put("Operation", "ItemLookup"); 
		params.put("ItemId", ItemId); 
//		params.put("ItemId", "716123123846"); 
		
		params.put("IdType", "UPC"); 
		params.put("ResponseGroup", "Large,ItemAttributes,Images,Offers,EditorialReview,Reviews");
		params.put("Condition", "All");
		params.put("AssociateTag", "wwwtraiditcom-21");
		params.put("SearchIndex", "All");
		

		String url = signer.sign(params);
		System.out.println(url);
		
		DocumentBuilderFactory builderFactory =
		        DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
		    builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		    e.printStackTrace();  
		}
		Document document = null;
		
		// or if you prefer DOM:
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		document = db.parse(new URL(url).openStream());
		XPath xPath =  XPathFactory.newInstance().newXPath();
        String expression = "/ItemLookupResponse/Items/Item[1]/ASIN";
        String email = xPath.compile(expression).evaluate(document);
        return email;
	} */
	public static ItemsDto getEBayItemIdFromUPC(String UPCId) throws ParserConfigurationException, MalformedURLException, SAXException, IOException, XPathExpressionException, ItemNotFoundException {
		String url = "http://svcs.ebay.com/services/search/FindingService/v1" +
				"?OPERATION-NAME=findItemsByProduct" +
				"&SERVICE-VERSION=1.0.0" +
				"&SECURITY-APPNAME=kns73ad0d-01c8-436e-a495-c335d8d71ad" +
				"&RESPONSE-DATA-FORMAT=XML" +
				"&REST-PAYLOAD" +
				"&paginationInput.entriesPerPage=2" +
				"&productId.@type=UPC" +
				"&productId="+UPCId;
		System.out.println(url);
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
		    builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		    e.printStackTrace();  
		}
		Document document = null;
		// or if you prefer DOM:
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		document = db.parse(new URL(url).openStream());
		XPath xPath =  XPathFactory.newInstance().newXPath();
        String expression = "/findItemsByProductResponse/searchResult/item[1]/itemId";
        String itemId = xPath.compile(expression).evaluate(document);
        if(itemId==null || itemId==""){
        	throw new ItemNotFoundException();
        }
        System.out.println(itemId);
        url = "http://open.api.ebay.com/shopping" +
        		"?callname=GetSingleItem" +
        		"&responseencoding=XML" +
        		"&appid=kns73ad0d-01c8-436e-a495-c335d8d71ad" +
        		"&siteid=0" +
        		"&version=515" +
        		"&ItemID="+itemId +
        		"&IncludeSelector=Variations,ItemSpecifics,Details,Description,TextDescription,ShippingCosts,Compatibility";
        document = db.parse(new URL(url).openStream());
        System.out.println(url);
        String expression1 = "/GetSingleItemResponse/Item/Description";
        String desc = xPath.compile(expression1).evaluate(document);
        System.out.println(desc);
        //String expression2= "/GetSingleItemResponse/Item/ViewItemURLForNaturalSearch";
       // String itemUrlForNaturalSearch= xPath.compile(expression2).evaluate(document);
       // System.out.println(itemUrlForNaturalSearch);
        String expression3= "/GetSingleItemResponse/Item/GalleryURL";
        String galleryUrl= xPath.compile(expression3).evaluate(document);
        System.out.println(galleryUrl);
        String expression4= "/GetSingleItemResponse/Item/PictureURL";
        String pictureUrl= xPath.compile(expression4).evaluate(document);
        System.out.println(pictureUrl);
//        expression= "/GetSingleItemResponse/Item/Seller/CurrentPrice";
//        String price= xPath.compile(expression).evaluate(document)+"USD";
        String expression5= "/GetSingleItemResponse/Item/Title";
        String title= xPath.compile(expression5).evaluate(document);
        String expression6= "/GetSingleItemResponse/Item/PrimaryCategoryName";
        String category= xPath.compile(expression6).evaluate(document);
        String expression7= "/GetSingleItemResponse/Item/ItemSpecifics/NameValueList[1]" ;
        XPathExpression exp = xPath.compile("//NameValueList");
        NodeList nl = (NodeList)exp.evaluate(document, XPathConstants.NODESET);
        System.out.println("Found " + nl.getLength() + " results");
        HashMap<String, String> newMap = new HashMap<String, String>();
        for (int i = 0; i < nl.getLength(); i++) {
        	 Element ele = (Element) nl.item(i);
        	 System.out.println(ele.getElementsByTagName("Name").item(0)
             .getTextContent());
        	 System.out.println(ele.getElementsByTagName("Value").item(0)
                     .getTextContent());
        	 
        	 newMap.put(ele.getElementsByTagName("Name").item(0)
             .getTextContent(),ele.getElementsByTagName("Value").item(0)
             .getTextContent());
        	 
        }
        String manufacturer = (String) newMap.get("Brand");
      	 System.out.println(manufacturer);
        
        String details= xPath.compile(expression7).evaluate(document);
        System.out.println(details);
       
        CategoriesDto ctae=new CategoriesDto();
        ctae.setCategoryName(category);
        
        ItemsDto ebayItemDto= new ItemsDto();
        ebayItemDto.setDescription(desc);
        ebayItemDto.setItemName(title);
        ebayItemDto.setPhotos(pictureUrl);
        ebayItemDto.setCategoryId(ctae);
        photolist = new ArrayList<String>();
        photolist.add(pictureUrl);
        photolist.add(galleryUrl);
       // photolist.add(itemUrlForNaturalSearch);
        ebayItemDto.setPhotoList(photolist);
        ebayItemDto.setManufacturer(manufacturer);
        return ebayItemDto;
        
	}
}
