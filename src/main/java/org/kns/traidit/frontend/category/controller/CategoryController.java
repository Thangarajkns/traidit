package org.kns.traidit.frontend.category.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.kns.traidit.backend.category.dao.CategoryDao;
import org.kns.traidit.backend.category.exception.CategoryMappingLimitReachedException;
import org.kns.traidit.backend.category.exception.CategoryNotFoundException;
import org.kns.traidit.backend.category.exception.CategoryNotSavedOrUpdatedException;
import org.kns.traidit.backend.category.exception.HomePageCategorySequenceNotSavedException;
import org.kns.traidit.backend.user.model.TraidItUser;
import org.kns.traidit.frontend.admin.dto.CategoriesListFormBean;
import org.kns.traidit.frontend.category.dto.CategoriesDto;
import org.kns.traidit.frontend.category.dto.CategoryFormBean;
import org.kns.traidit.frontend.category.dto.CategoryListFormBean;
import org.kns.traidit.frontend.category.dto.SimilarCategoryDto;
import org.kns.traidit.frontend.category.dto.SimilarCategoryForm;
import org.kns.traidit.frontend.category.service.CategoryService;
import org.kns.traidit.frontend.common.utility.JsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.util.AutoPopulatingList;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import twitter4j.org.json.JSONArray;
import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

/**
 * CategoryController --- Controller class for category section. All the URL's with "/web/categories/*" will be handled by this controller
 * @author Thangaraj
 *
 */
@Controller("categoryController")
@RequestMapping(value="/web/categories")
public class CategoryController {
	
	private Logger log = Logger.getLogger(CategoryController.class);
	
	
	//category service provider
	@Resource(name="categoryService")
	private CategoryService categoryService;

	/**
	 * Handles request for listing the categories. URL with "/list.htm" is mapped to this function.
	 * @param map 
	 * @return string of value "listCategories" which is a view page name
	 */
	@RequestMapping(value="list.htm")
	public String listCategories(@ModelAttribute("categoryListFormBean")CategoryListFormBean categoryListFormBean,Map<String,Object> map){
		
		List<CategoriesDto> categoryNames = new ArrayList<CategoriesDto>();
		Integer totalCategories=0;
		String searchText = "";
		if( categoryListFormBean.getSearchText()!=null){
			 searchText = categoryListFormBean.getSearchText().trim();
		}
		try{
			categoryNames = (ArrayList<CategoriesDto>)this.categoryService.getAllCategoriesWeb(
					categoryListFormBean.getPaginator().getCurrentPageNo(),
					categoryListFormBean.getPaginator().getNoOfItemsPerPage(),
					categoryListFormBean.getSortBy(),
					categoryListFormBean.getSortOrder(),
					searchText
					);
		}catch(CategoryNotFoundException e){
			 System.out.println("No categories found for given criteria");
		}catch(Exception e){
			 System.out.println("Exception on CategoryController -> listCategories");
			 e.printStackTrace();
		}
		finally{
			//set up total no of categories in the filtered list for paginator.
			if(!categoryNames.isEmpty())
				totalCategories = categoryNames.get(0).getTotalCategories();
			categoryListFormBean.getPaginator().setTotalNoOfItems(totalCategories);
		}
		map.put("categoryName", categoryNames);
		return "listCategories";
	}

	/**
	 * Handles the request to add a new category. URL with "/add.htm" is mapped to this function.
	 * 
	 * @param categoriesDto modelAttribute 
	 * @param map
	 * @return string of value "addCategoriesForm" which is a view page name 
	 */
	@RequestMapping(value="add.htm",method=RequestMethod.GET)
	public String createCategory(@ModelAttribute("categoryFormBean")CategoryFormBean categoryFormBean,Map<String,Object> map){
		log.info("opening addCategoriesForm to add new category");
		return "addCategoriesForm";
	}

	/**
	 * 
	 * @param categoriesDto modelAttribute
	 * @param map
	 * @param option form submission option (cancel/save)
	 * @return string of value "redirect:list.htm" which spring to redirect to "list.htm" 
	 */
	@RequestMapping(value="add.htm",method=RequestMethod.POST)
	public String processCreateCategory(@ModelAttribute("categoryFormBean")CategoryFormBean categoryFormBean,Map<String,Object> map,@RequestParam("submit") String option)  {
		if(option.equals("cancel")){
			//add cancel message here
		}
		else{
			try{
				CategoriesDto categoriesDto = new CategoriesDto();
				//while editing category load category object and overwrite edited things, so that fields like subcategories will be preserved
				if(categoryFormBean.getCategoryId()!= null && categoryFormBean.getCategoryId() != 0)
					categoriesDto = this.categoryService.getCategoryById(categoryFormBean.getCategoryId());
				categoriesDto.setCategoryId(categoryFormBean.getCategoryId());
				categoriesDto.setCategoryName(categoryFormBean.getCategoryName());
				/*update only if file is replaced*/
				Long size = categoryFormBean.getCategoryIconFile().getSize();
				if(!size.equals(new Long(0)))
					categoriesDto.setCategoryIconFile(categoryFormBean.getCategoryIconFile());
				categoriesDto.setIsHomePageCategory(categoryFormBean.getIsHomePageCategory());
				Set<CategoriesDto> parentCategories = new HashSet<CategoriesDto>();
				try{
					parentCategories.add(this.categoryService.getCategoryById(categoryFormBean.getParentCategory()));
				}
				catch(CategoryNotFoundException e){
					
				}
				categoriesDto.setParentCategories(parentCategories);
				//not yet implemented
				//categoriesDto.setSimilarCategoryOf(categoryFormBean.getSimilarCategories());
				categoriesDto.setCategoryId(this.categoryService.saveOrUpdateCategory(categoriesDto));
				this.categoryService.mapEbayCategoryIds(categoryFormBean.geteBayIds(),categoriesDto.getCategoryId());
			}
			catch(CategoryNotSavedOrUpdatedException ex){
				ex.printStackTrace();
				 System.out.println("Exception on CategoryController -> createCategory save");
			}
			catch(Exception e){
				e.printStackTrace();
				 System.out.println("Exception on CategoryController -> createCategory submit");
			}
		}
		return "redirect:list.htm";
	}

	/**
	 * Handles request for edit any category provided the category id. URL with "/edit.htm" is mapped to this function.
	 * @param categoriesDto modelAttribute
	 * @param map
	 * @param id id of the category to be edited
	 * @return string of value "addCategoriesForm" which is a view page name
	 */
	@RequestMapping(value="/edit.htm",method=RequestMethod.GET)
	public String editCategory(@ModelAttribute("categoryFormBean")CategoryFormBean categoryFormBean,Map<String,Object> map,@RequestParam("id")Integer id){
		try{
			//load the category DTO of given id
			CategoriesDto categoryDto = this.categoryService.getCategoryById(id);
			
			//set all data to modelAttribute
			categoryFormBean.setCategoryId(categoryDto.getCategoryId());
			categoryFormBean.setCategoryName(categoryDto.getCategoryName());
			categoryFormBean.setIsHomePageCategory(categoryDto.getIsHomePageCategory());
			categoryFormBean.setCategoryIcon(categoryDto.getCategoryIcon());
			//populate parent category id
			Set<CategoriesDto> parentCategories = (HashSet<CategoriesDto>)categoryDto.getParentCategories();
			if(!parentCategories.isEmpty() || parentCategories != null){
				Iterator<CategoriesDto> it = parentCategories.iterator();
				if(it.hasNext()){
					categoryFormBean.setParentCategory(it.next().getCategoryId());
					//populate grand parent id
					CategoriesDto parentCategory = this.categoryService.getCategoryById(categoryFormBean.getParentCategory());
					if(!parentCategory.getParentCategories().isEmpty() || parentCategory.getParentCategories() != null){
						Iterator<CategoriesDto> grandParentIterator = parentCategory.getParentCategories().iterator();
						if(grandParentIterator.hasNext()){
							categoryFormBean.setGrandParentCategory(grandParentIterator.next().getCategoryId());
							
						}
					}
				}
			}
			
			map.put("categoryBreadCrumb",getCategoryBreadCrumb(categoryDto));
			//set ebay category Ids
			categoryFormBean.seteBayIds(categoryDto.geteBayIds());
		}catch(Exception e){
			 System.out.println("Exception on CategoryController -> createCategory");
		}
		return "addCategoriesForm";
	}
	
	
	
	private String getCategoryBreadCrumb(CategoriesDto categoryDto) throws CategoryNotFoundException{
		String breadCrumb = "";
		if(categoryDto.getParentCategories()!=null&&!categoryDto.getParentCategories().isEmpty()){
			breadCrumb = (String)categoryDto.getParentCategories().iterator().next().getCategoryName();
			breadCrumb = getCategoryBreadCrumb(this.categoryService.getCategoryById(categoryDto.getParentCategories().iterator().next().getCategoryId())) + breadCrumb;
		}
		if(breadCrumb != "")
			breadCrumb += " > ";
		
		return breadCrumb;
	}
	
	
	
	/**
	 * Handles request for delete any category provided the category id. URL with "/delete.htm" is mapped to this function.
	 * @param id id of the category to be deleted
	 * @return string of value "redirect:list.htm" which spring to redirect to "list.htm" 
	 */
	@RequestMapping(value="delete.htm",method=RequestMethod.GET)
	@ResponseBody
	public String deleteCategory(@RequestParam("id")Integer id){
		try{
			this.categoryService.deleteCategoryById(id);
		}
		catch(CategoryNotFoundException e){
			System.out.println("CategoryNotFoundException on  CategoryController -> deleteCategory()");
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Exception on  CategoryController -> deleteCategory()");
		}
		System.out.println(id);
		return "success";
	}

	@RequestMapping(value="getparentcategories.htm",method=RequestMethod.GET)	
	public String getParentCategories(@RequestParam("categoryId")Integer categoryId,@RequestParam("index")Integer index,Map<String,Object> map){
		List<CategoriesDto> categories = new ArrayList<CategoriesDto>();
		try{
			if(categoryId == 0){
				categories.addAll((ArrayList<CategoriesDto>)this.categoryService.getAllParentCategories(null,false));
			}
			else{
				categories.addAll(this.categoryService.getCategoryById(categoryId).getSubCategories());
			}
		}
		catch(Exception e){
			System.out.println("no categories found");
		}
		System.out.println(categories.toString());
		map.put("categoryNames", categories);
		map.put("index", index);
		map.put("categoryId", categoryId);
		return "parentCategoriesDropDown";
	}
	
	
	

	@RequestMapping(value="/search.htm",method=RequestMethod.GET)
	public String searchCategoryList(Map<String,Object> map,@ModelAttribute("categoriesListFormBean")CategoriesListFormBean categoriesListFormBean){
		map.put("formBean", new TraidItUser());
		ArrayList<CategoriesDto> parentCategories;
		try {
			parentCategories = this.categoryService.getAllParentCategories(null,false);
		} catch (CategoryNotFoundException e) {
			parentCategories = new ArrayList<CategoriesDto>();
		}
		map.put("parentCategories",parentCategories);
		return "listCategories1";
	}
	
	
	

	@RequestMapping(value="/search.htm",method=RequestMethod.POST)
	public String processSearchCategoryList(Map<String,Object> map,@ModelAttribute("categoriesListFormBean")CategoriesListFormBean categoriesListFormBean){
		if(!categoriesListFormBean.getSubmitOption().equals("cancel")){
			return "redirect:edit.htm?id="+categoriesListFormBean.getCategory().getCategoryId();
		}
		map.put("formBean", new TraidItUser());
		ArrayList<CategoriesDto> parentCategories;
		try {
			parentCategories = this.categoryService.getAllParentCategories(null,false);
		} catch (CategoryNotFoundException e) {
			parentCategories = new ArrayList<CategoriesDto>();
		}
		map.put("parentCategories",parentCategories);
		return "listCategories1";
	}
	
	@RequestMapping(value="/listCategories.htm",method=RequestMethod.POST)
	public String editCategory(Map<String, Object> map,@ModelAttribute("categoriesListFormBean")CategoriesListFormBean categoriesListFormBean){
		if(categoriesListFormBean.getSubmitOption().equals("cancel")){
			return "redirect:list.htm";
		}
		return "redirect:edit.htm?id="+categoriesListFormBean.getCategory().getCategoryId();
	}
	
	
	
	@RequestMapping(value="/getSubCategoryDropDown.htm",method = RequestMethod.POST)
	public String getSubcategoryDropDown(Map<String,Object> map,@RequestParam("categoryId")Integer categoryId,@RequestParam("index")Integer index){
		map.put("index", index);
		CategoriesDto category = new CategoriesDto();
		try {
			category = this.categoryService.getCategoryById(categoryId);
		} catch (CategoryNotFoundException e) {
			e.printStackTrace();
		}
		map.put("subCategories", category.getSubCategories());
		
		return "subCategoryDropDown";
	}
	
	
	
	
	
	/**
	 * Created by Jee
	 * @param categoryId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="similarCategories.htm",method=RequestMethod.GET)
	public String getSimilarCategoriesOfaCategory(@RequestParam("categoryId")Integer categoryId,Map<String, Object> map){
		log.info("inside getSimilarCategoriesOfaCategory()");
		ArrayList<SimilarCategoryDto> similarCategoryDtos=new ArrayList<SimilarCategoryDto>();
		try{
			  CategoriesDto categoryDto=this.categoryService.getCategoryById(categoryId);
			  map.put("title", "Similar Categories of "+categoryDto.getCategoryName());
			  map.put("category", categoryDto.getCategoryName());
			  similarCategoryDtos=this.categoryService.getSimilarCategoriesofCategory(categoryId);		
			  for(SimilarCategoryDto similarCategoryDto:similarCategoryDtos){
				  String breadCrumb=this.getCategoryBreadCrumb(similarCategoryDto.getSimilarCategoryDto()).trim();					
					if(breadCrumb.length()>0 && breadCrumb.charAt(breadCrumb.trim().length()-1)=='>'){
						breadCrumb=breadCrumb.substring(0,breadCrumb.length()-1);
					}
					similarCategoryDto.getSimilarCategoryDto().setCategoryBreadCumb(breadCrumb);
			  }
			  map.put("similarCategories", similarCategoryDtos);
		}
		catch(CategoryNotFoundException e){
			e.printStackTrace();
			log.error("No Similar Categories found for Category "+categoryId);
		    map.put("similarCategories",similarCategoryDtos);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		map.put("categoryId", categoryId);
		return "similarCategories";
	}
	
	
	
	/**
	 * 
	 * @param categoryName
	 * @param map
	 * @return
	 */
	@RequestMapping(value="searchCategory.htm",method=RequestMethod.POST)
	public String searchCategoriesByCategoryName(@RequestParam("category")String categoryName,Map<String,Object> map){
		log.info("inside searchCategoriesByCategoryName()");
		try{
			ArrayList<CategoriesDto> categoryDtos=this.categoryService.getCategoriesFromSearch(categoryName);
			for(CategoriesDto categoryDto:categoryDtos){
				String breadCrumb=this.getCategoryBreadCrumb(categoryDto).trim();					
				if(breadCrumb.length()>0 && breadCrumb.charAt(breadCrumb.trim().length()-1)=='>'){
					breadCrumb=breadCrumb.substring(0,breadCrumb.length()-1);
				}				
				categoryDto.setCategoryBreadCumb(breadCrumb);
				System.out.println("cat3"+categoryDto.getCategoryId());
			}
			map.put("categories", categoryDtos);
			return "categoryResult";
		}
		catch(CategoryNotFoundException e){
			e.printStackTrace();
			log.error("No Categories Found"+e.toString());
			map.put("message", "No Categories Found");
			return "categoryResult";
		}
		catch(Exception e){
			e.printStackTrace();
			log.error("Error Getting Categories "+e.toString());
			map.put("message", "Error While Getting Categories");
			return "categoryResult";
		}
	}
	
	
	
	
	/**
	 * Created by Jeevan on Nov 06, 2014
	 * Method to add Similar Category elements Dynamically
	 * 
	 * @param index
	 * @param map
	 * @return
	 * 
	 * As the error page not present, handled it directly without exception handling
	 */
	@RequestMapping(value="addSimilarCategory.htm",method=RequestMethod.POST)
	public String addSimilarCategory(@RequestParam("index")Integer index,@RequestParam("category") Integer category,Map<String, Object> map){
		log.info("inside addSimilarCategory()");
		map.put("index", index);
		map.put("category", category);
		return "addSimilarCategory";
	}
	
	
	
	
	
	@ModelAttribute("similarCategoryForm")
    public SimilarCategoryForm getSimilarCategoryForm()
    {
		SimilarCategoryForm similarCategoryForm=new SimilarCategoryForm();
		similarCategoryForm.setSimilarCategories(new AutoPopulatingList<SimilarCategoryDto>(SimilarCategoryDto.class));
		return similarCategoryForm;
    }
	
	
	/**
	 * Created by Jeevan on Nov 06, 2014
	 * Method to saveSimilarCategory..
	 * 
	 * @param similarCategoryForm
	 * @param map
	 * @return
	 */
	@RequestMapping(value="saveSimilarCategory.htm",method=RequestMethod.POST)
	public String saveSimilarCategory(@ModelAttribute("similarCategoryDto") SimilarCategoryDto similarCategoryDto, Map<String, Object> map,RedirectAttributes redAttribs){
		log.info("inside saveSimilarCategory()");
		System.out.println("INSIDE SAVE SIMILAR CATEGORIES");
		try{
			this.categoryService.saveSimilarCategory(similarCategoryDto);
		}
		catch(ConstraintViolationException e){
			log.error("Similar Category Already Mapped "+e.toString());		
			redAttribs.addFlashAttribute("message", "Error: Similar Category "+similarCategoryDto.getSimilarCategory()+" already mapped");
		}
		catch(CategoryNotFoundException e){
			log.error("No Category Found "+e.toString());
			redAttribs.addFlashAttribute("message", "Error: No Category Exists with ID "+similarCategoryDto.getSimilarCategory());
		}
		catch(CategoryMappingLimitReachedException e){
			log.error("Maximum No of Categories are mapped to Similar Category"+e.toString());
			redAttribs.addFlashAttribute("message", "Error: Similar Category "+similarCategoryDto.getSimilarCategory()+" mapped to Maximum Allowable Categories");
		}
		catch(Exception e){
			log.error("Error Adding Similar Category");
			redAttribs.addFlashAttribute("message", "Error: Error Mapping Similar Category, Try Again Later");
			e.printStackTrace();
		}
		return "redirect:/web/categories/similarCategories.htm?categoryId="+similarCategoryDto.getCategory();
	}
	
	
	
	/**
	 * Created by Jeevan on Nov 06, 2014
	 * Method to delete Similar Category...
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="deleteSimilarCategory.htm",method=RequestMethod.GET)
	public String deleteSimilarCategory(@RequestParam("id") Integer id,@RequestParam("categoryId")Integer categoryId,Map<String, Object> map,RedirectAttributes redAttribs){
		log.info("inside deleteSimilarCategory()");
		try{
			this.categoryService.deleteSimilarCategory(id);
			redAttribs.addFlashAttribute("message", "Deleted Successfully");
			
		}
		catch(Exception e){
			e.printStackTrace();
			redAttribs.addFlashAttribute("message", "Error While Deleting Similar Category");
		}
		return "redirect:/web/categories/similarCategories.htm?categoryId="+categoryId;
	}
	
	
	/**
	 * 
	 * BADLY WRITTEN CODE
	 * 
	 * Created by Jeevan on Nov 10, 2014
	 * Method to edit Similar Category Mapping
	 * @param id
	 * @param isBidirection
	 * @param map
	 * @return
	 */
	@RequestMapping(value="editSimilarCategory.htm",method=RequestMethod.POST)
	public String editSimilarCategory(@RequestParam("id") Integer id,@RequestParam("isBidirection") Boolean isBidirection,@RequestParam("categoryId") Integer categoryId,RedirectAttributes redAttribs){
		log.info("inside editSimilarCategory()");
		try{
			Integer result=this.categoryService.editSimilarCategoryMapping(id, isBidirection);
			if (result>0)
				redAttribs.addFlashAttribute("message","Edited Successfully");
			else
				throw new Exception();
		}
		catch(Exception e){
			log.error("Error While Editing Similar Category");
			redAttribs.addFlashAttribute("message", "Error While Editing Similar Category");
		}
		return "redirect:/web/categories/similarCategories.htm?categoryId="+categoryId;
	}
	
	/**
	 * @return
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 14-Jan-2015
	 */
	@RequestMapping(value="gethomepagecategories.htm")
	public String getHomepageCategories(){
		return "homePageCategories";
	}
	
	/**
	 * 
	 * @throws JSONException
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 14-Jan-2015
	 */
	@ResponseBody
	@RequestMapping(value="gethomepagecategoriesjson.htm",method=RequestMethod.GET)
	public String getHomePageCategoriesJson() throws JSONException{
		ArrayList<JSONObject> list = new ArrayList<JSONObject>();
		ArrayList<CategoriesDto> homePageCategories;
		try {
			homePageCategories = this.categoryService.getHomePageCategoriesWeb();
			
		} catch (CategoryNotFoundException e) {
			homePageCategories = new ArrayList<CategoriesDto>();
		}
		Integer i =1;
		for(CategoriesDto category : homePageCategories){
			JSONObject obj = new JSONObject();
			obj.accumulate("Index", i++);
			obj.accumulate("categoryName",category.getCategoryName());
			obj.accumulate("CategoryIcon", category.getCategoryIcon());
			obj.accumulate("CategoryId", category.getCategoryId());
			obj.accumulate("Sequence", category.getSequenceNo());
			list.add(obj);
		}
		return list.toString();
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws JSONException
	 * @return String
	 * @author Thangaraj(KNSTEK)
	 * @since 14-Jan-2015
	 */
	@RequestMapping(value="savehomepagecategories.htm",method=RequestMethod.POST)
	@ResponseBody
	public String saveHomePageCategoriesSequence(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		log.info("inside viewSubCategories() controller");
		System.out.println("inside viewSubCategories() controller");
		JSONObject obj=new JSONObject();
		try{
			JSONObject json=JsonParser.getJson(request, response);
			log.info("request : "+json.toString());
			System.out.println("request : "+json.toString());
			
			//fetch data from json
			JSONArray categories=json.getJSONArray("categories");
			
			//
			ArrayList<CategoriesDto> categoriesDto = new ArrayList<CategoriesDto>();
			for(int i=0;i<categories.length();i++){
				CategoriesDto tempCategory = new CategoriesDto();
				JSONObject tempCategoryJson = (JSONObject)categories.get(i);
				tempCategory.setCategoryId(tempCategoryJson.getInt("CategoryId"));
				try{
					tempCategory.setSequenceNo(tempCategoryJson.getInt("Sequence"));
				}catch(JSONException ex){
					continue;
				}
				categoriesDto.add(tempCategory);
			}
			this.categoryService.saveOrUpdateHomePageCategorySequences(categoriesDto);
		}
		catch(IllegalAccessError e){
			log.error(e.toString());
			String message="SOME ERROR OCCURRED WHILE SAVING CATEGORIES";
			obj.accumulate("status", message);
		} catch (HomePageCategorySequenceNotSavedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = obj.toString();
		log.info("response : "+ result);
		System.out.println("response : "+ result);
		return result;
	}
	
	
	
	
	
	
	
}
