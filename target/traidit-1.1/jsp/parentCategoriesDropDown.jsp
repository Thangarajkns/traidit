<%@ include file="/jsp/common/common.jsp"%>
<c:if test="${not empty categoryNames}">
	<div class="form-group">
		<label class="col-sm-4 control-label">Category Name</label>
		<div class="col-sm-7 controls">
			<select name="parentCategory_${index }" id="parentCategory_${index }" onchange="updateParentCategory(this)">
			<option value="0">--select--</option>
			<c:forEach items="${categoryNames}" var="category">
				<option value="${category.categoryId}">${category.categoryName}</option>
			</c:forEach>
			</select>
			<a href="javascript:void(0)" onclick="removeParent(${index},this)" id="remove_parent_${index }" class="show">Remove this category</a>
			<c:if test="${index lt 2 }">
				<a href="javascript:void(0)" onclick="addParent(${index},this)" id="add_parent_${index }" class="show">Add sub categories</a>
			</c:if>
		</div>
	</div>
</c:if>
<c:if test="${empty categoryNames}">
</c:if>
