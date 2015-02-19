<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
	<c:when test="${not empty categories }">
		<label>Search Results</label>
		<table class="table table-bordered" style="margin: 0 auto;width: 60%;">
			<thead>
				  <tr>
					<th>Category Id </th>
					<th>Category Name </th>
					<th>Parent Categories </th>
				 </tr>
			</thead>
			<tbody class="searchResults">
				  <c:forEach var="category" items="${categories}">
				  		<tr>
				  			<td> ${category.categoryId } </td> 
				  			<td>${category.categoryName }</td>
				  			<td>${category.categoryBreadCumb }</td>
				  		</tr>
				  
				  </c:forEach>									
			</tbody>												
		</table>
	</c:when>
	<c:otherwise>
		<h3 align="center">${message }</h3>
	</c:otherwise>
</c:choose>

														