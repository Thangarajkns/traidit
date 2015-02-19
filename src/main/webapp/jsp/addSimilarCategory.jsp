<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="addCategory${index}" class="addCategories" style="margin-top: 10px;margin-bottom: 15px;">
    <form:form action="${pageContext.request.contextPath}/web/categories/saveSimilarCategory.htm" method="POST" commandName="similarCategoryForm" onsubmit="return validateSimilarCategory(${index });">
		<label>Similar Category Id:</label>
		<input type="text" name="similarCategory" id="similarCategory${index}" class="similarCategoryTxt" style="width: 15%;padding: 1px;margin-right: 20px;" /> <span id="simCategoryError"></span>
		<input type="hidden" name="category"  value="${category }" />
		<label >Allow Bi-directional Mapping:</label>
		<input type="checkbox" name="isBidirection" id="bidirection${index}" class="bidirectional"  style="margin-right: 20px;"/> 
		<input type="submit" value="Save" id="saveBtn${index}" />
		<input type="button" value="Cancel" onClick="cancelAddSimilarCategory()" />
	</form:form>
</div>

