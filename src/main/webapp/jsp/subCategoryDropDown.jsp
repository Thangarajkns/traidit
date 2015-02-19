<%@ include file="/jsp/common/common.jsp"%>
<c:if test="${not empty subCategories}">
	<div class="form-group category_${index}_wrapper" >
		<label class="col-sm-4 control-label">Sub Category Name</label>
		<div class="col-sm-7 controls">
			<select class="width-50" data-category_level="${index }" name="category_${index }" id="category_${index }" onchange="updateSubCategoryList(this)">
				<option value="0">--select--</option>
				<c:forEach items="${subCategories}" var="category">
					<option value="${category.categoryId}">${category.categoryName}</option>
				</c:forEach>
			</select>
			<input value="Get SubCategories" data-category_level="${index}" class="btn vd_btn vd_bg-green vd_white category_list_trigger_${index}" type="button" onclick="getSubCategories(this);">
			<input value="X" data-category_level="${index}" class="btn vd_btn vd_bg-green vd_white removeSubCategoryTrigger" type="button" onclick="removeSubCategoryList(this);">
		</div>
	</div>
</c:if>
<c:if test="${empty subCategories}">
</c:if>
