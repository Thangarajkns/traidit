package org.kns.traidit.backend.category.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.kns.traidit.backend.category.exception.CategoryNotFoundException;
import org.kns.traidit.backend.category.exception.CategoryNotSavedOrUpdatedException;
import org.kns.traidit.backend.category.exception.FavouriteCategoryNotFound;
import org.kns.traidit.backend.category.exception.HomePageCategorySequenceNotFoundException;
import org.kns.traidit.backend.category.exception.HomePageCategorySequenceNotSavedException;
import org.kns.traidit.backend.category.model.EbayCategoryMapper;
import org.kns.traidit.backend.category.model.HomePageCategorySequence;
import org.kns.traidit.backend.category.model.SimilarCategory;
import org.kns.traidit.backend.category.model.TraidItItemCategories;
import org.kns.traidit.backend.user.exception.FavouritesNotFoundException;
import org.kns.traidit.backend.user.exception.FavouritesNotSavedOrUpdatedException;
import org.kns.traidit.backend.user.favourites.model.FavouriteCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



/*
 * created by Soujanya on 19th June,2014
 * DAO class for Category related activities
 */

@Repository("categoryDao")
@Transactional
public class CategoryDaoImpl implements CategoryDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public static Integer totalCategories;
	
	public static Integer totalHomePageCategories;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private static Logger log=Logger.getLogger(CategoryDaoImpl.class);
/*------------------------------------------------------------------------------------------------*/

	/*
	 * method to save and update category
	 */
	public Integer saveOrUpdateCategory(TraidItItemCategories category)throws CategoryNotSavedOrUpdatedException{
		 sessionFactory.getCurrentSession().saveOrUpdate(category);
		 sessionFactory.getCurrentSession().flush();
		 return category.getCategoryId();
	 }
	
	
	/*
	 * added by Soujanya on september 29.2014
	 * method to save and update favourite category
	 */
	public Integer saveOrUpdateFavouriteCategory(FavouriteCategory favouriteCategory) throws FavouritesNotSavedOrUpdatedException{
		sessionFactory.getCurrentSession().saveOrUpdate(favouriteCategory);
		 sessionFactory.getCurrentSession().flush();
		 return favouriteCategory.getId();
	}
	
	
	/*
	 * method to retrieve all categories
	 */
	@SuppressWarnings("unchecked")
	 public ArrayList<TraidItItemCategories> getAllCategories(Integer pageNo,Integer pageSize,String sortBy,String sortOrder,String searchText) throws CategoryNotFoundException{
		 log.info("inside getAllCategories()");
		 System.out.println("inside dao.getAllCategories()");
		 ArrayList<TraidItItemCategories> categories=null;
		 Criteria criteria=sessionFactory.getCurrentSession().createCriteria(TraidItItemCategories.class);
		 if(searchText != null && !searchText.isEmpty())
			 criteria.add(Restrictions.ilike("categoryName",searchText , MatchMode.ANYWHERE));
		 //criteria.addOrder(Order.asc("categoryName"));
		 //criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 totalCategories=criteria.list().size();
		 
		 if(sortBy == null || sortBy.isEmpty())
			 sortBy = "categoryName";
		 if(sortOrder != null && sortOrder.equals("DESC"))
			 criteria.addOrder(Order.desc(sortBy));
		 else
			 criteria.addOrder(Order.asc(sortBy));
		 
		 if(null!=pageNo && null!=pageSize){
				criteria.setFirstResult(pageNo*pageSize);
				criteria.setMaxResults(pageSize);
			}
			
		 categories=(ArrayList<TraidItItemCategories>) criteria.list();
		 if(!categories.isEmpty()){
			 System.out.println(categories.size());
			 return categories;
			 
		 }
		 else{
			 throw new CategoryNotFoundException();
		 }
			 
	 }
	
	/*
	 * method to retrieve category by Id
	 */
	@SuppressWarnings("unchecked")
	public TraidItItemCategories getCategoryById(Integer id) throws CategoryNotFoundException {
		log.info("inside getCategoryById()");
		ArrayList<TraidItItemCategories> category = (ArrayList<TraidItItemCategories>) sessionFactory.getCurrentSession().createCriteria(TraidItItemCategories.class)
				.add(Restrictions.eq("categoryId", id))
				.list();
		
		if(!category.isEmpty()){
			return category.get(0);
		}
		else{
			throw new CategoryNotFoundException();
		}	
	}
	
	/*
	 * added by Soujanya on september 29,2014
	 * method to get favourite category by id
	 */
	@SuppressWarnings("unchecked")
	public FavouriteCategory getFavouriteCategoryById(Integer favCategoryId) throws FavouritesNotFoundException{
		log.info("inside getFavouriteCategoryById()");
		ArrayList<FavouriteCategory> favCategories = (ArrayList<FavouriteCategory>) sessionFactory.getCurrentSession().createCriteria(FavouriteCategory.class)
				.add(Restrictions.eq("id",favCategoryId ))
				.list();
		
		if(!favCategories.isEmpty()){
			return favCategories.get(0);
		}
		else{
			throw new FavouritesNotFoundException();
		}	
	}
	
	
	/*
	 * added by Soujanya on october 1st,2014
	 * method to retrieve favourite category id based on category id and user id
	 */
	@SuppressWarnings("unchecked")
	public FavouriteCategory getFavouriteCategoryByCategoryAndUser(Integer categoryId, Integer userId) throws FavouritesNotFoundException{
		log.info("inside getFavouriteCategoryByCategoryAndUser()");
		ArrayList<FavouriteCategory> favCategories = (ArrayList<FavouriteCategory>) sessionFactory.getCurrentSession().createCriteria(FavouriteCategory.class)
				.add(Restrictions.eq("category.categoryId", categoryId))
				.add(Restrictions.eq("userId.userId", userId))
				.list();
		if(!favCategories.isEmpty()){
			return favCategories.get(0);
		}
		else{
			throw new FavouritesNotFoundException();
		}	
	}
	
	/*
	 * added by Soujanya on september 29,2014
	 * method to delete favourite category 
	 */
	public void deleteFavouriteCategoryFromDB(FavouriteCategory favouriteCategory) throws FavouritesNotFoundException{
	  log.info("inside deleteFavouriteCategory()");
	  sessionFactory.getCurrentSession().delete(favouriteCategory);
		sessionFactory.getCurrentSession().flush();
  }
	
	
	
	
	/*
	 * method to retrieve category by name
	 */
	@SuppressWarnings("unchecked")
	public TraidItItemCategories getCategoryByName(String name) throws CategoryNotFoundException {
		log.info("inside getCategoryByName()");
		ArrayList<TraidItItemCategories> category=(ArrayList<TraidItItemCategories>) sessionFactory.getCurrentSession().createCriteria(TraidItItemCategories.class)
				.add(Restrictions.eq("categoryName", name))
				.list();
		if(!category.isEmpty()){
			return category.get(0);
		}
		else{
			throw new CategoryNotFoundException();
		}	
	}
	
	/*
	 * method to delete category
	 * deletes category
	 */
	public void deleteCategory(TraidItItemCategories category) throws CategoryNotFoundException {
		sessionFactory.getCurrentSession().delete(category);
		sessionFactory.getCurrentSession().flush();
	}

	/**
	 * Returns list of Category items with the give id's.
	 * @param categoryIds An Integer array consists of category Id's
	 * @return ArrayList of Categories (TraidItItemCategories) of given Id's
	 * @throws CategoryNotFoundException if no category found
	 * @author Thangaraj(KNSTEK)
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TraidItItemCategories> getCategoriesByListOfIds(Integer[] categoryIds)throws CategoryNotFoundException{
		ArrayList<TraidItItemCategories> categories = (ArrayList<TraidItItemCategories>)sessionFactory.getCurrentSession().createCriteria(TraidItItemCategories.class)
				.add(Restrictions.in("categoryId", categoryIds)).list();
		if(!categories.isEmpty()){
			return categories;
		}
		else{
			throw new CategoryNotFoundException();
		}	
		
	}

	/*
	 * method to retrieve all homepage categories
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TraidItItemCategories> getAllHomePageCategories() throws CategoryNotFoundException{
		 log.info("inside getAllHomePageCategories()");
		 System.out.println("inside dao.getAllHomePageCategories() DAO");
		 Criteria criteria=sessionFactory.getCurrentSession().createCriteria(TraidItItemCategories.class);
		 criteria.addOrder(Order.asc("categoryName"));
		 criteria.add(Restrictions.eq("isHomePageCategory", true));
		 totalCategories=criteria.list().size();
//		 totalHomePageCategories = totalCategories;
		 ArrayList<TraidItItemCategories> categories=(ArrayList<TraidItItemCategories>) criteria.list();
		 if(!categories.isEmpty()){
			 return categories;
		 }
		 else{
			 throw new CategoryNotFoundException();
		 }
			 
	 }
	
	/*
	 * method to retrieve all categories
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TraidItItemCategories> getAllCategories() throws CategoryNotFoundException{
		 log.info("inside getAllHomePageCategories()");
		 System.out.println("enetered dao");
		 System.out.println("inside dao.getAllHomePageCategories() DAO");
		 Criteria criteria=sessionFactory.getCurrentSession().createCriteria(TraidItItemCategories.class);
		 criteria.addOrder(Order.asc("categoryName"));
		 //criteria.add(Restrictions.eq("isHomePageCategory", true));
		 totalCategories=criteria.list().size();
//		 totalHomePageCategories = totalCategories;
		 ArrayList<TraidItItemCategories> categories=(ArrayList<TraidItItemCategories>) criteria.list();
		 System.out.println(categories+"dao");
		 if(!categories.isEmpty()){
			 return categories;
		 }
		 else{
			 throw new CategoryNotFoundException();
		 }
			 
	 }
	
	/*
	 * added by Soujanya on september 15,2014
	 * method to retrieve all favourite categories of user
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TraidItItemCategories> getFavouriteCategoriesOfUser(Integer userId) throws FavouriteCategoryNotFound{
		log.info("inside getFavouriteCategoriesOfUser() DAO");
		System.out.println("inside getFavouriteCategoriesOfUser() DAO");
		//ArrayList<FavouriteCategory> favCategory=null;
		 Criteria criteria=sessionFactory.getCurrentSession().createCriteria(FavouriteCategory.class);
		 
		 criteria.addOrder(Order.asc("id"));
		 criteria.add(Restrictions.eq("userId.userId", userId));
		 criteria.setProjection(Projections.property("category"));
		 //totalFavCategory=criteria.list().size();
		// List<Object[]>
		 ArrayList<TraidItItemCategories> categories=(ArrayList<TraidItItemCategories>) criteria.list();
		 //favCategory=(ArrayList<FavouriteCategory>) criteria.list();
		 if(!categories.isEmpty() && null!=categories){
			 return categories;
			 
		 }
		 else{
			 throw new FavouriteCategoryNotFound();
		 }		 
	 }
	
	/**
	 * added by Soujanya on november 13,2014
	 * method to get favourite category list of user from favourite category table o
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<FavouriteCategory> getFavouriteCategoriesOfUserFromDB(Integer userId) throws FavouritesNotFoundException{
		log.info("inside getFavouriteCategoriesOfUserFromDB()");
		ArrayList<FavouriteCategory> favCategories=null;
		 Criteria criteria=sessionFactory.getCurrentSession().createCriteria(FavouriteCategory.class);
		 criteria.add(Restrictions.eq("userId.userId", userId));
		 criteria.addOrder(Order.asc("id"));
		 favCategories=(ArrayList<FavouriteCategory>) criteria.list();
		 
		 if(!favCategories.isEmpty()){
			 return favCategories;
		 }
		 else{
			 throw new FavouritesNotFoundException();
		 }
	}
	
	/**
	 * 
	 * @param userId current/requesting user id
	 * @param favCategory Boolean flag to decide filter only the favourite categories or not
	 * @throws CategoryNotFoundException
	 * @return ArrayList<TraidItItemCategories>
	 * @author Thangaraj(KNSTEK)
	 * @since 22-Dec-2014
	 */
	public ArrayList<TraidItItemCategories> getAllParentCategories(Integer userId,Boolean favCategory) throws CategoryNotFoundException{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TraidItItemCategories.class);
		criteria.add(Restrictions.isEmpty("parentCategories"));
		if(favCategory){
			DetachedCriteria dc = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			DetachedCriteria dc1 = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			DetachedCriteria dc2 = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			dc.setProjection(Projections.property("category.categoryId"));
			dc1.setProjection(Projections.property("parentCategory.categoryId"));
			dc2.setProjection(Projections.property("grandParentCategory.categoryId"));
			criteria.add(Restrictions.or(Subqueries.propertyIn("categoryId", dc),Restrictions.or(Subqueries.propertyIn("categoryId", dc1),Subqueries.propertyIn("categoryId", dc2))));
		}
		@SuppressWarnings("unchecked")
		ArrayList<TraidItItemCategories> categories = (ArrayList<TraidItItemCategories>)criteria.list();
		if(categories == null || categories.isEmpty())
			throw new CategoryNotFoundException();
		totalCategories = categories.size();
		return categories;
	}
	
	/**
	 * 
	* Created by Jeevan on 23-Sep-2014 8:54:15 pm	
	*  Method for:getting Categories By Ebay Category Ids
	* @param ebayId
	* @return
	* @throws CategoryNotFoundException
	* This method works on the assumption that Ebay Category Id is mapped only to a single local category and it is the task of Admin
	* to maintain that
	* 
	 */
	@SuppressWarnings("unchecked")
	public TraidItItemCategories getCategoryByEbayCategoryId(String ebayId)throws CategoryNotFoundException{
		log.info("inside getCategoryByEbayCategoryItems");
		/*ArrayList<TraidItItemCategories> categories=(ArrayList<TraidItItemCategories>) sessionFactory.getCurrentSession().createCriteria(TraidItItemCategories.class)
				.add(Restrictions.ilike("eBayIds", "%"+ebayId))
				.list();
		*/
		ArrayList<EbayCategoryMapper> ebayCategoryMappers=(ArrayList<EbayCategoryMapper>) sessionFactory.getCurrentSession().createCriteria(EbayCategoryMapper.class)
				.add(Restrictions.eq("ebayId", ebayId))
				.list();
		if(!ebayCategoryMappers.isEmpty()){
			return ebayCategoryMappers.get(0).getCategory();
		}
		else{
			throw new CategoryNotFoundException();
		}
		/*
		if(!categories.isEmpty()){
			return categories.get(0);
		}
		else{
			throw new CategoryNotFoundException();
		}*/
	}
	
	/**
	 * 
	 * @param categoryId
	 * @return ArrayList<String>
	 * @author Thangaraj
	 * @since 07-10-2014
	 */
	public ArrayList<String> getEBayCategoryIdsByCategoryId(Integer categoryId){
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(EbayCategoryMapper.class);
		criteria.add(Restrictions.eq("category.categoryId",categoryId));
		criteria.setProjection(Projections.property("ebayId"));
		@SuppressWarnings("unchecked")
		ArrayList<String> eBayIds = (ArrayList<String>)criteria.list();
		return eBayIds;
	}

	/**
	 * Saves the EbayCategoryMapper object to database
	 * 
	 * @param ebayMapper of type EbayCategoryMapper
	 * @return Integer id of saved EbayCategoryMapper
	 * @author Thangaraj
	 * @since 24-09-2014 
	 */
	@Override
	public Integer saveEbayCategoryMapper(EbayCategoryMapper ebayMapper) {
		 sessionFactory.getCurrentSession().saveOrUpdate(ebayMapper);
		 sessionFactory.getCurrentSession().flush();
		 log.info("saving EbayCategoryMapper of ebay id"+ebayMapper.getEbayId());
		 return ebayMapper.getId();
	}
	
	@Override
	public void deleteAllEbayCategoryMappersByCategoryId(Integer categoryId){
		Session hibSession = sessionFactory.getCurrentSession();
		Criteria criteria = hibSession.createCriteria(EbayCategoryMapper.class);
		@SuppressWarnings("unchecked")
		ArrayList<EbayCategoryMapper> mappers = (ArrayList<EbayCategoryMapper>) criteria.add(Restrictions.eq("category.categoryId",categoryId)).list();
		if(mappers != null && !mappers.isEmpty()){
			for(EbayCategoryMapper mapper : mappers){
				hibSession.delete(mapper);
			}
		}
		sessionFactory.getCurrentSession().flush();
	}
	
 	public List<Integer> getSubCategoryIdsByCategoryId(Integer userId, Integer categoryId,Boolean favCategory) throws CategoryNotFoundException{
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(TraidItItemCategories.class);
		criteria.add(Restrictions.eq("categoryId",categoryId));

		if(favCategory){
			DetachedCriteria dc = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			DetachedCriteria dc1 = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			DetachedCriteria dc2 = DetachedCriteria.forClass(FavouriteCategory.class).add(Restrictions.eq("userId.userId", userId));
			dc.setProjection(Projections.property("category.categoryId"));
			dc1.setProjection(Projections.property("parentCategory.categoryId"));
			dc2.setProjection(Projections.property("grandParentCategory.categoryId"));
			criteria.add(Restrictions.or(Subqueries.propertyIn("categoryId", dc),Restrictions.or(Subqueries.propertyIn("categoryId", dc1),Subqueries.propertyIn("categoryId", dc2))));
		}
		
		criteria.createCriteria("subCategories").setProjection(Projections.property("categoryId"));
		@SuppressWarnings("unchecked")
		List<Integer> subCategories = (List<Integer>)criteria.list();
		if(subCategories.isEmpty())
			throw new CategoryNotFoundException();
		return subCategories;
	}
	
 	/**
 	 * Created by Jeevan on Nov 03, 2014
 	 * Method tosave or Update Similar Categories
 	 * 
 	 * @param similarCategory
 	 * @return
 	 * @throws Exception
 	 */
 	public Integer saveOrUpdateSimilarCategories(SimilarCategory similarCategory)throws Exception{
 		log.info("inside saveOrUpdateSimilarCategories()");
 		sessionFactory.getCurrentSession().saveOrUpdate(similarCategory);
 		sessionFactory.getCurrentSession().flush();
 		return similarCategory.getId();
 	}
 	
 	/**
 	 * Created by Jeevan on Nov 03, 2014
 	 * Method to delete Similar Categories
 	 * 
 	 * @param similarCategory
 	 * @throws Exception
 	 */
 	public void deleteSimilarCategories(SimilarCategory similarCategory)throws Exception{
 		log.info("inside deleteSimilarCategories()");
 		sessionFactory.getCurrentSession().delete(similarCategory);
 		sessionFactory.getCurrentSession().flush();
 	}
 	
    /**
     * Created by Jeevan on Nov 05, 2014
     * Method to get SimilarCategoriesByCategory
     * 
     * @param categoryId
     * @return
     * @throws CategoryNotFoundException
     */
 	@SuppressWarnings("unchecked")
	public ArrayList<SimilarCategory> getSimilarCategoriesByCategory(Integer categoryId)throws CategoryNotFoundException{
 		log.info("inside getSimilarCategoriesByCategory()");
 		ArrayList<SimilarCategory> similarCategories=(ArrayList<SimilarCategory>) sessionFactory.getCurrentSession().createCriteria(SimilarCategory.class)
 				.add(Restrictions.eq("category", categoryId))
 				.list();
 		if(!similarCategories.isEmpty()){
 			return similarCategories;
 		}
 		else{
 			throw new CategoryNotFoundException();
 		}
 	}
 	
 	/**
 	 * Created by Jeevan on Nov 05, 2014
 	 * Method to get Similar Categories By Similar Category Id
 	 * 
 	 * @param similarCategoryId
 	 * @return
 	 * @throws CategoryNotFoundException
 	 */
 	@SuppressWarnings("unchecked")
	public ArrayList<SimilarCategory> getSimilarCategoriesBySimilarCategory(Integer similarCategoryId)throws CategoryNotFoundException{
 		log.info("inside getSimilarCategoriesBySimilarCategory()");
 		ArrayList<SimilarCategory> similarCategories=(ArrayList<SimilarCategory>) sessionFactory.getCurrentSession().createCriteria(SimilarCategory.class)
 				.add(Restrictions.eq("similarCategory", similarCategoryId))
 				.list();
 		if(!similarCategories.isEmpty()){
 			return similarCategories;
 		}
 		else{
 			throw new CategoryNotFoundException();
 		}
 	}
 	
 	/**
 	 * Created by Jeevan on Nov 05, 2014
 	 * Method to get Category By Category Name.... USed for Getting Categories having entered Category Name.. It needs to be change if founds to be slow
 	 * @param categoryName
 	 * @return
 	 * @throws CategoryNotFoundException
 	 */
 	@SuppressWarnings("unchecked")
	public ArrayList<TraidItItemCategories> getCategoriesByCategoryNameforSearch(String categoryName) throws CategoryNotFoundException{
 		log.info("inside getCategoriesByCategoryNameforSearch()");
 		System.out.println("CAtegory "+categoryName);
 		ArrayList<TraidItItemCategories> categories=(ArrayList<TraidItItemCategories>) sessionFactory.getCurrentSession().createCriteria(TraidItItemCategories.class)
 				.add(Restrictions.like("categoryName", "%"+categoryName+"%"))
 				.list();
 		if(!categories.isEmpty()){
 			return categories;
 		}
 		else{
 			throw new CategoryNotFoundException();
 		}
 		
 	}
 	
 	/**
 	 * Created by Jeevan on November 06, 2014
 	 * Method to getSimilarCategoryById()
 	 * @param id
 	 * @return
 	 * @throws CategoryNotFoundException
 	 */
 	@SuppressWarnings("unchecked")
	public SimilarCategory getSimilarCategoryById(Integer id)throws CategoryNotFoundException{
 		log.info("inside getSimilarCategoryById()");
 		ArrayList<SimilarCategory> similarCategories=(ArrayList<SimilarCategory>) sessionFactory.getCurrentSession().createCriteria(SimilarCategory.class)
 				.add(Restrictions.eq("id", id))
 				.list();
 		if(!similarCategories.isEmpty()){
 			return similarCategories.get(0);
 		}
 		else{
 			throw new CategoryNotFoundException();
 		}
 		
 	}
 	
 	/**
	 * method to get count of user's favourite category
	 * @author Soujanya
	 * @since nov 17,2014
	 */
	public Integer getCountOfFavCategoriesOfUser(Integer userId) throws FavouritesNotFoundException{
		log.info("inside getCountOfFavCategoriesOfUser()");
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(FavouriteCategory.class);
		 criteria.add(Restrictions.eq("userId.userId", userId));
		 criteria.setProjection(Projections.count("id"));
		 Integer count=Integer.parseInt(criteria.list().get(0).toString());
			return count;
	}
 	
	/**
	 * 
	 * @throws HomePageCategorySequenceNotFoundException
	 * @return Map<Integer,Integer>
	 * @author Thangaraj(KNSTEK)
	 * @since 14-Jan-2015
	 */
	public Map<Integer,Integer> getHomePageCategorySequences() throws HomePageCategorySequenceNotFoundException{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HomePageCategorySequence.class);
		criteria.setProjection(Projections.projectionList().add(Projections.property("category.categoryId")).add(Projections.property("sequenceNo")));
		ArrayList<Object[]> list = (ArrayList<Object[]>) criteria.list();
		if(list == null || list.isEmpty())
			throw new HomePageCategorySequenceNotFoundException();
		Map<Integer,Integer> sequences = new HashMap<Integer,Integer>(); 
		for(Object[] sequence : list){
			sequences.put((Integer)sequence[0],(Integer) sequence[1]);
		}
		return sequences;
	}

	/**
	 * 
	 * @param sequence
	 * @throws HomePageCategorySequenceNotSavedException
	 * @return Integer
	 * @author Thangaraj(KNSTEK)
	 * @since 20-Jan-2015
	 */
	public Integer saveOrUpdateHomePageCategorySequences(HomePageCategorySequence sequence) throws HomePageCategorySequenceNotSavedException{
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(sequence);
			sessionFactory.getCurrentSession().flush();
			return sequence.getId();
		}
		catch(Exception e){
			throw new HomePageCategorySequenceNotSavedException();
		}
	}

	/**
	 * Truncates(deletes) all sequence data of home page categories
	 * 
	 * @return void
	 * @author Thangaraj(KNSTEK)
	 * @since 20-Jan-2015
	 */
	public void truncateHomePageCategorySequences(){
		//fetch mapped table name of HomePageCategorySequence class
		Class<?> c = HomePageCategorySequence.class;
		Table table = c.getAnnotation(Table.class);
		String tableName = table.name();
		//create truncate sql query
		Query query = sessionFactory.getCurrentSession().createSQLQuery("TRUNCATE table "+tableName+" ");
		query.executeUpdate();
	}

}
