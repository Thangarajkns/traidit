/**
 * @since 21-Jan-2015
 */
package org.kns.traidit.frontend.mobile.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.kns.traidit.backend.category.exception.CategoryMappingLimitReachedException;
import org.kns.traidit.backend.category.exception.CategoryNotFoundException;
import org.kns.traidit.backend.refferral.exception.RefferralTokenNotFoundException;
import org.kns.traidit.backend.refferral.exception.RefferralTokenNotSavedException;
import org.kns.traidit.backend.refferral.model.RefferralToken;
import org.kns.traidit.backend.user.exception.PlanNotFoundException;
import org.kns.traidit.backend.user.exception.RoleNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotFoundException;
import org.kns.traidit.backend.user.exception.UserNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.category.dto.CategoriesDto;
import org.kns.traidit.frontend.category.dto.SimilarCategoryDto;
import org.kns.traidit.frontend.category.service.CategoryService;
import org.kns.traidit.frontend.common.utility.JsonParser;
import org.kns.traidit.frontend.refferral.service.RefferralService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

/**
 * @author Thangaraj(KNSTEK)
 *
 */

@Controller("refferralController")
public class RefferralController {
	
	@Resource(name="refferralService")
	private RefferralService refferralService;
	
	@Resource(name="categoryService")
	private CategoryService categoryService;
	
	private static Logger log = Logger.getLogger(RefferralController.class);

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws JSONException
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 21-Jan-2015
	 */
	@ResponseBody
	@RequestMapping(value="/getrefferralurl.htm")
	public String getReferalUrl(HttpServletRequest request, HttpServletResponse response) throws JSONException{
		log.info("inside getReferalUrl()");
		JSONObject obj = new JSONObject();
		JSONObject json = JsonParser.getJson(request, response);
		log.info("request : " + json.toString());
		System.out.println("request : " + json.toString());
		try{
			Integer userId = json.getInt("userid");
			Properties properties = new Properties();
			String propFileName = "application.properties";
	        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
	        properties.load(inputStream);
			String baseUrl = properties.getProperty("baseUrl");

			obj.accumulate("refferralurl", baseUrl+"refferedby.htm?tk=user"+userId);
		}
		catch(JSONException e){
			obj.accumulate("status", "error while reading user id");
		}
		catch(Exception e){
			obj.accumulate("status", "error while manupulating refferral url");
		}
		String resultString = obj.toString();
		log.info("response : " + resultString);
		System.out.println("response : " + resultString);
		return resultString;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * @throws JSONException
	 * @throws IOException
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 21-Jan-2015
	 */
	@RequestMapping(value="/handlerefferralurl.htm")
	public String handleRefferralURL(HttpServletRequest request, HttpServletResponse response,@RequestParam("tk")String token) throws JSONException, IOException{
		log.info("inside handleRefferralURL()");
		System.out.println("inside handleRefferralURL()");
		/*
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String paramName = (String) headerNames.nextElement();
			String paramValue = request.getHeader(paramName);
			System.out.println(paramName+" :" + paramValue);
		}*/
		String userAgent = request.getHeader("user-agent");
		String osVersion;
		if (userAgent.matches(".*iPhone.*") || userAgent.matches(".*iPad.*"))
		{
			osVersion = userAgent.substring(userAgent.indexOf(" OS ")+4).split(" ",2)[0].replace('_', '.').trim();
			System.out.println(osVersion);
		}
		else
		{
			return "access from apple mobile";
		}
		
		Integer userId = Integer.parseInt(token.substring(4));
		RefferralToken refferralToken;
		try {
			refferralToken = this.refferralService.getRefferralTokenByIP(request.getRemoteAddr());
		} catch (RefferralTokenNotFoundException e1) {
			refferralToken = new RefferralToken();
		}
		refferralToken.setRefferralId(new TraidItUser(userId));
		refferralToken.setUserIP(request.getRemoteAddr());
		refferralToken.setOsVersion(osVersion);
		try {
			this.refferralService.saveOrUpdateRefferralToken(refferralToken);
		} catch (RefferralTokenNotSavedException e) {
			e.printStackTrace();
		}
		return "redirect:http://store.apple.com/";
	}
	
	@RequestMapping(value = "/testexcelupload1.htm")
	@ResponseBody
	public String extractExcelContentByColumnIndex1(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject obj=new JSONObject();
        try {
        	try{
    			this.categoryService.getAllCategoriesWeb(0,10,null,null,null);
   			 log.info("No categories found. Starting to upload excel categories");
    			return "Categories table must be empty before uploading";
    		}catch(CategoryNotFoundException e){
    			 System.out.println("No categories found. Starting to upload excel categories");
    			 log.info("No categories found. Starting to upload excel categories");
    		}
        	// sub class created to store the data from excel file in proper category hierarchy structure with ebay and similar categories mapped
			class ExcelCategory{
				String categoryName;
				String ebayCategoryIds;
				String categoryHierarchy;
				Set<String> similarCategoryHierarchy = new HashSet<String>();
				ArrayList<ExcelCategory> subCategories = new ArrayList<ExcelCategory>();
				
				public String getCategoryName() {
					return categoryName;
				}
				public void setCategoryName(String categoryName) {
					this.categoryName = categoryName;
				}
				public String getEbayCategoryIds() {
					return ebayCategoryIds;
				}
				public void setEbayCategoryIds(String ebayCategoryIds) {
					this.ebayCategoryIds = ebayCategoryIds;
				}
				public ArrayList<ExcelCategory> getSubCategories() {
					return subCategories;
				}
				public void setSubCategories(ArrayList<ExcelCategory> subCategories) {
					this.subCategories = subCategories;
				}
				public String getCategoryHierarchy() {
					return categoryHierarchy;
				}
				public void setCategoryHierarchy(String categoryHierarchy) {
					this.categoryHierarchy = categoryHierarchy;
				}
				public Set<String> getSimilarCategoryHierarchy() {
					return similarCategoryHierarchy;
				}
				public void setSimilarCategoryHierarchy(Set<String> similarCategoryHierarchy) {
					this.similarCategoryHierarchy = similarCategoryHierarchy;
				}
				
				@Override
				public boolean equals(Object obj) { 	
				   return ((ExcelCategory)obj).getCategoryName().equals(this.getCategoryName());  	
				}
				
				int hashCode(Object ob){
			        return(categoryName.hashCode()); //Only determining equality based on that field
			    }
				
				public String toString(){
					String result = "\n"+ categoryName+" : "+ebayCategoryIds+" { \n";
					for (ExcelCategory excelCategory : subCategories) {
						result += excelCategory.toString(); 
					}
					result += " \n } \n";
					return result;
				}
			}//end of sub class
			//initialise variables to store data read from excel file
            ArrayList<ExcelCategory> parentCategories = new ArrayList<ExcelCategory>();
            Map<String,Integer> categoryIdNameMapping = new HashMap<String,Integer>(); 
            Map<String,Set<String>> similarCategoryMapping = new HashMap<String, Set<String>>();
            //read file data from excel file
        	FileInputStream file = new FileInputStream(new File("/var/traidit/ebayCategories Cross reference.xls"));
        	Workbook workbook = WorkbookFactory.create(file);
        	org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            //for each row of excel sheet
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                System.out.println(row.getRowNum());
                if(row.getRowNum() > 0){ //To filter column headings
                	// read all required cell data
	                String mainCategory,subcategory,subsubcategory,ebayId,similarMainCategory,similarSubcategory,similarSubsubcategory,similarCategoryHierarchy;
	                Cell cell1 = row.getCell(0);
	                Cell cell2 = row.getCell(1);
	                Cell cell3 = row.getCell(2);
	                Cell cell4 = row.getCell(3);
	                Cell cell5 = row.getCell(4);
	                Cell cell6 = row.getCell(5);
	                Cell cell7 = row.getCell(6);
	                ebayId 						= getStringData(cell1);
	                mainCategory 				= getStringData(cell2);
	                subcategory 				= getStringData(cell3);
	                subsubcategory 				= getStringData(cell4);
	                similarMainCategory 		= getStringData(cell5);
	                similarSubcategory 			= getStringData(cell6);
	                similarSubsubcategory 		= getStringData(cell7);
	                similarCategoryHierarchy	= similarMainCategory == ""?"":(similarSubcategory == ""?similarMainCategory:(similarSubsubcategory == ""?similarMainCategory+"_"+similarSubcategory:similarMainCategory+"_"+similarSubcategory+"_"+similarSubsubcategory));
	                
	                //for main category
	                //skip if main/parent category name not exist
	                if(mainCategory == null || mainCategory.isEmpty())
	                	continue;
	                //create main/parent category of type ExcelCategory(sub class created)
	                ExcelCategory category1 = new ExcelCategory();
	                category1.setCategoryName(mainCategory);
	                //if parent category not exists in populated list, add it
	                if(!parentCategories.contains(category1)){
	                	parentCategories.add(category1);
	                	category1.setCategoryHierarchy(mainCategory);
	                }
	                //if it exists load same category to get existing data
	                else{
	                	category1 = parentCategories.get(parentCategories.indexOf(category1));
	                }
                	//for sub category
	                //if second/sub category name not exist
	                if(subcategory == null || subcategory.isEmpty()){
	                	// append ebay id to main category, as there are no further level categories exists in current row
	                	String ebayIds = category1.getEbayCategoryIds();
	                	ebayIds = (ebayIds == null ||ebayIds.isEmpty())?ebayId:ebayIds+";"+ebayId;
	                	category1.setEbayCategoryIds(ebayIds);
	                	// append similar category hierarchy to main category, as there are no further level categories exists in current row
	                	if(!similarCategoryHierarchy.equals(""))
	                		category1.getSimilarCategoryHierarchy().add(similarCategoryHierarchy);
	                	//skip iteration
	                	continue;
	                }
	                //create second/sub category
	                ExcelCategory category2 = new ExcelCategory();
	                category2.setCategoryName(subcategory);
                	ArrayList<ExcelCategory> subCategories = category1.getSubCategories();
	                //if sub category not exists in populated list, add it
                	 if(!subCategories.contains(category2)){
                		 subCategories.add(category2);
 	                	 category2.setCategoryHierarchy(mainCategory+"_"+subcategory);
 	                }
 	                //if it exists load same category to get existing data
 	                else{
 	                	category2 = subCategories.get(subCategories.indexOf(category2));
 	                }
                	 category1.setSubCategories(subCategories);
                	//for sub sub category
 	                //if third/subsub category name not exist
	                if(subsubcategory == null || subsubcategory.isEmpty()){
	                	// append ebay id to main category, as there are no further level categories exists in current row
	                	String ebayIds = category2.getEbayCategoryIds();
	                	ebayIds = (ebayIds == null ||ebayIds.isEmpty())?ebayId:ebayIds+";"+ebayId;
	                	category2.setEbayCategoryIds(ebayIds);
	                	// append similar category hierarchy to main category, as there are no further level categories exists in current row
	                	if(!similarCategoryHierarchy.equals(""))
	                		category2.getSimilarCategoryHierarchy().add(similarCategoryHierarchy);
	                	//skip iteration
	                	continue;
	                }
	                //create third/subsub category
 	                ExcelCategory category3 = new ExcelCategory();
 	               category3.setCategoryName(subsubcategory);
                 	ArrayList<ExcelCategory> subSubCategories = category2.getSubCategories();
	                //if subsub category not exists in populated list, add it
                 	 if(!subSubCategories.contains(category3)){
                 		subSubCategories.add(category3);
                    	category3.setCategoryHierarchy(mainCategory+"_"+subcategory+"_"+subsubcategory);
  	                }
                 	//if it exists load same category to get existing data
  	                else{
  	                	category3 = subSubCategories.get(subSubCategories.indexOf(category3));
  	                }
                 	category2.setSubCategories(subSubCategories);
                 	
                 	// if all three category exists, append ebay id to last level category
                	String ebayIds = category3.getEbayCategoryIds();
                	ebayIds = (ebayIds == null ||ebayIds.isEmpty())?ebayId:ebayIds+";"+ebayId;
                	category3.setEbayCategoryIds(ebayIds);
                	// append similar category hierarchy to main category, as there are no further level categories exists in current row
                	if(!similarCategoryHierarchy.equals(""))
                		category3.getSimilarCategoryHierarchy().add(similarCategoryHierarchy);
                }
            }//iteration through excel file is over
            
            file.close();
            
            //for each first level categories
            for (ExcelCategory excelCategory : parentCategories) {
            	//create main/parent category
            	CategoriesDto parentCategory = new CategoriesDto();
            	parentCategory.setCategoryName(excelCategory.getCategoryName());
            	parentCategory.setIsHomePageCategory(true);//first level parents are set to home page categories 
            	parentCategory.setCategoryId(this.categoryService.saveOrUpdateCategory(parentCategory));
            	//map this category with hierarchy to its id, which is used later while mapping similar categories
            	categoryIdNameMapping.put(excelCategory.getCategoryHierarchy(),parentCategory.getCategoryId());
            	//populate similar category mapping to map : will loded to db later after getting all categories id
            	similarCategoryMapping.put(excelCategory.getCategoryHierarchy(), excelCategory.getSimilarCategoryHierarchy());
            	//if this category has ebay category ids mapped, store it in db
            	if(excelCategory.getEbayCategoryIds()!=null && !excelCategory.getEbayCategoryIds().isEmpty()){
            		this.categoryService.mapEbayCategoryIds(excelCategory.getEbayCategoryIds(), parentCategory.getCategoryId());
            	}
            	//for each second level categories
                for (ExcelCategory excelSubCategory : excelCategory.getSubCategories()) {
                	//create second/sub category
                	CategoriesDto subCategory = new CategoriesDto();
                	subCategory.setCategoryName(excelSubCategory.getCategoryName());
                	subCategory.setIsHomePageCategory(false);
                	Set<CategoriesDto> parentCategoryDtos = subCategory.getParentCategories();
                	parentCategoryDtos.add(parentCategory);
                	subCategory.setParentCategories(parentCategoryDtos);
                	subCategory.setCategoryId(this.categoryService.saveOrUpdateCategory(subCategory));
                	//map this category with hierarchy to its id, which is used later while mapping similar categories
                	categoryIdNameMapping.put(excelSubCategory.getCategoryHierarchy(),subCategory.getCategoryId());
                	//populate similar category mapping to map : will loded to db later after getting all categories id
                	similarCategoryMapping.put(excelSubCategory.getCategoryHierarchy(), excelSubCategory.getSimilarCategoryHierarchy());
                	//if this category has ebay category ids mapped, store it in db
                	if(excelSubCategory.getEbayCategoryIds()!=null && !excelSubCategory.getEbayCategoryIds().isEmpty()){
                		this.categoryService.mapEbayCategoryIds(excelSubCategory.getEbayCategoryIds(), subCategory.getCategoryId());
                	}
                	//for each third level categories
                	for (ExcelCategory excelSubSubCategory : excelSubCategory.getSubCategories()) {
                		//create third/subsub category
                    	CategoriesDto subsubCategory = new CategoriesDto();
                    	subsubCategory.setCategoryName(excelSubSubCategory.getCategoryName());
                    	subsubCategory.setIsHomePageCategory(false);
                    	Set<CategoriesDto> subsubparentCategoryDtos = subsubCategory.getParentCategories();
                    	subsubparentCategoryDtos.add(subCategory);
                    	subsubCategory.setParentCategories(subsubparentCategoryDtos);
                    	subsubCategory.setCategoryId(this.categoryService.saveOrUpdateCategory(subsubCategory));
                    	//map this category with hierarchy to its id, which is used later while mapping similar categories
                    	categoryIdNameMapping.put(excelSubSubCategory.getCategoryHierarchy(), subsubCategory.getCategoryId());
                    	//populate similar category mapping to map : will loded to db later after getting all categories id
                    	similarCategoryMapping.put(excelSubSubCategory.getCategoryHierarchy(), excelSubSubCategory.getSimilarCategoryHierarchy());
                    	//if this category has ebay category ids mapped, store it in db
                    	if(excelSubSubCategory.getEbayCategoryIds()!=null && !excelSubSubCategory.getEbayCategoryIds().isEmpty()){
                    		this.categoryService.mapEbayCategoryIds(excelSubSubCategory.getEbayCategoryIds(), subsubCategory.getCategoryId());
                    	}
                    }
                	
                }
            	
			}
            //end of creating and mapping category hierarchy and ebay category mapping
            
            //populate similar categories mapping
            //for each categories having similar categories mapping
            for (Map.Entry<String, Set<String>> entry : similarCategoryMapping.entrySet()) {
            	//for each similar categories of given category
            	for (String similarCategoryName : entry.getValue()) {
            		//if similar category name not exists or empty name skip iteration
            		if(similarCategoryName == null || similarCategoryName.isEmpty())
            			continue;
            		//create similar category mapping 
            		SimilarCategoryDto similarCategoryDto = new SimilarCategoryDto();
					similarCategoryDto.setCategory(categoryIdNameMapping.get(entry.getKey()));
					similarCategoryDto.setSimilarCategory(categoryIdNameMapping.get(similarCategoryName));
					similarCategoryDto.setIsBidirection(false);
					try{
						this.categoryService.saveSimilarCategory(similarCategoryDto);
					}
					catch(CategoryMappingLimitReachedException e){
						System.out.println("===== "+entry.getKey()+" : "+similarCategoryDto.getCategory()+" "+similarCategoryName+" : "+similarCategoryDto.getSimilarCategory());
					}
					catch(Exception e){
						System.out.println(entry.getKey()+" : "+similarCategoryDto.getCategory()+" "+similarCategoryName+" : "+similarCategoryDto.getSimilarCategory());
					}
					
				}
            }

            /*BufferedWriter writer = null;
            try
            {
                writer = new BufferedWriter( new FileWriter( "D:/projects/traidit/doc/categories.txt"));
                for (Map.Entry<String, Set<String>> entry : similarCategoryMapping.entrySet()) {
                	writer.write(entry.getKey()+"{"+categoryIdNameMapping.get(entry.getKey())+"}"+" : "+entry.getValue()+"\n");
				}
            }
            catch ( IOException e)
            {
            }
            finally
            {
                try
                {
                    if ( writer != null)
                    writer.close( );
                }
                catch ( IOException e)
                {
                }
            }*/
            
            /*
             * scipt to write all category data from populated arraylist to text file
             * 
             BufferedWriter writer = null;
            try
            {
                writer = new BufferedWriter( new FileWriter( "D:/projects/traidit/doc/categories.txt"));
                for (ExcelCategory excelCategory : parentCategories) {
                	for (String cat1 : excelCategory.getEbayCategoryIds().split(";")) {
                		writer.write(cat1); 
					}
                	
    			}

            }
            catch ( IOException e)
            {
            }
            finally
            {
                try
                {
                    if ( writer != null)
                    writer.close( );
                }
                catch ( IOException e)
                {
                }
            }*/
            /*for (String entry : categoriesList.keySet()) {
            	String key = entry;
            	String[] categoryNames = key.split("_");
				Set<String> test = categoriesList.get(entry);
			}*/
            obj.accumulate("status", "success");
        } catch (Exception e) {
            e.printStackTrace();
            obj.accumulate("status", "failed");
        }
        return obj.toString();
    }
	
	/**
	 * 
	 * @param cell
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 21-Jan-2015
	 */
	private String getStringData(Cell cell){
		if(cell == null)
			return "";
		switch (cell.getCellType()) {
	        case Cell.CELL_TYPE_NUMERIC:
	        	Double value = cell.getNumericCellValue();
	        	return String.format("%.0f", value) ;
	        case Cell.CELL_TYPE_STRING:
	            return cell.getStringCellValue();
	    }
    	return "";
	}

	@RequestMapping(value="/updaterefferral.htm")
	@ResponseBody
	private String updateRefferral(HttpServletRequest request, HttpServletResponse response) throws JSONException{
		log.info("inside updateRefferral()");
		JSONObject obj = new JSONObject();
		try {
			JSONObject json = JsonParser.getJson(request, response);
			log.info("request : " + json.toString());
			System.out.println("request : " + json.toString());
			Integer userId = json.getInt("userid");
			this.refferralService.updateRefferral(userId, 1);
			obj.accumulate("status", "Refferral Updated");
		} catch(JSONException e){
			String message = "Something Went Wrong While parsing json";
			log.error(message + " " + e.toString());
			obj.accumulate("status", message);
		} catch (UserNotFoundException e) {
			String message = "Some of Given user was not found";
			log.error(message);
			obj.accumulate("status", message);
		} catch (RoleNotFoundException e) {
			String message = "RoleNotFoundException";
			log.error(message);
			obj.accumulate("status", "Some thing went wrong while updating refferrals");
		} catch (UserNotSavedOrUpdatedException e) {
			String message = "UserNotSavedOrUpdatedException";
			log.error(message);
			obj.accumulate("status", "Some thing went wrong while updating refferrals");
		} catch (PlanNotFoundException e) {
			String message = "PlanNotFoundException";
			log.error(message);
			obj.accumulate("status", "Some thing went wrong while updating refferrals");
		} catch (Exception e) {
			String message = "Some thing went wrong while updating refferrals "+ e.toString();
			log.error(message);
			obj.accumulate("status", "Some thing went wrong while updating refferrals");
		}
		String result = obj.toString();
		log.info("response : " + result);
		System.out.println("response : " + result);
		return result;
	}
	
}

