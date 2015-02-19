<c:forEach items="${category.parentCategories }" var="parentCategory">
<c:forEach items="${parentCategory.parentCategories }" var="greatParentCategory">
<a href="${pageContext.request.contextPath }/web/categories/edit.htm?id=${ greatParentCategory.categoryId}">${greatParentCategory.categoryName }</a>  > 
</c:forEach>
	<a href="${pageContext.request.contextPath }/web/categories/edit.htm?id=${ parentCategory.categoryId}">${parentCategory.categoryName }</a>
</c:forEach>