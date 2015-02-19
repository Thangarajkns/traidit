<%@ include file="/jsp/common/common.jsp"%>
<!DOCTYPE html>
<!--[if IE 8]>			<html class="ie ie8"> <![endif]-->
<!--[if IE 9]>			<html class="ie ie9"> <![endif]-->
<!--[if gt IE 9]><!-->	<html><!--<![endif]-->

<head>
<title>Traidit Categories</title>
<%@ include file="/jsp/common/commonStyles.jsp"%>
</head>
<body id="dashboard"
	class="full-layout  nav-right-hide nav-right-start-hide  nav-top-fixed      responsive    clearfix"
	data-active="dashboard " data-smooth-scrolling="1">
	<div class="vd_body">
		<%@ include file="/jsp/common/adminHeader.jsp"%>
		<div class="content">
			<div class="container">
				<%@ include file="/jsp/common/sideNavigation.jsp"%>
				<!-- Middle Content Start -->

				<div class="vd_content-wrapper">
					<div class="vd_container">
						<div class="vd_content clearfix">
							<div class="clearfix"></div>
							<c:choose>
								<c:when test="${not empty successMessage }">
									<div class="col-md-12 success_message">${successMessage }</div>
								</c:when>
								<c:when test="${failureMessage }">
									<div class="col-md-12 error_message">${failureMessage }</div>
								</c:when>
								<c:otherwise>
									<div class="col-md-12 success_message"></div>
								</c:otherwise>
							</c:choose>
							<div class="vd_title-section clearfix">
								<div class="vd_panel-header">
									<h1>Categories</h1>
									<small class="subtitle"></small>
									<div class="vd_panel-menu">
										<a
											href="${pageContext.request.contextPath }/web/categories/add.htm"
											class="btn vd_btn vd_bg-green vd_white">Add Category</a>
										<!-- vd_mega-menu -->
									</div>
								</div>
								
								<!-- vd_panel-header -->
								<div class="vd_content-section clearfix">
									<div class="row">
										<div class="col-md-12">
											<div class="panel widget">
												<div class="panel-heading vd_bg-grey">
													<h3 class="panel-title">
														<span class="menu-icon"> <i
															class="fa fa-dot-circle-o"></i>
														</span> Category Management
													</h3>
												</div>
												<div class="panel-body  table-responsive">	
												<form:form
													commandName="categoryListFormBean"
													method="POST"
													action="${pageContext.request.contextPath }/web/categories/list.htm"
													class="form-horizontal" 
									                role="form" 
									                enctype="multipart/form-data"
									                >
									                <form:hidden path="sortBy"/>
									                <form:hidden path="sortOrder"/>
									                <form:hidden path="paginator.noOfItemsPerPage"/>
													<form:hidden path="paginator.currentPageNo"/>
													
									                      
														<div class="form-group">
									                        <label class="col-sm-4 control-label">Search for category</label>
									                        <div class="col-sm-7 controls">
									                        	<form:input path="searchText"/>
									                        </div>
									                    </div>
									                      <div class="form-group form-actions">
									                        <div class="col-sm-4"> </div>
									                        <div class="col-sm-7">
									                        	<form:button class="btn vd_btn vd_bg-green vd_white" name="optionChoosed" value="filter"><i class="icon-ok"></i> Filter</form:button>
									                        	<button class="btn vd_btn" type="button" onclick="resetcategorySearchForm()">Reset</button>
									                     	</div>
									                      </div>
									                      
													<table class="table table-bordered">
														<thead>
															<tr>
																<th><a href="#" data-sort-by="categoryId" onclick="sortResultsBy(this)">Category Id</a></th>
																<th>parent Categories</th>
																<th><a href="#" data-sort-by="categoryName" onclick="sortResultsBy(this)">Category Name</a></th>
																<th>Category Icon</th>
																<th>Sub Categories</th>
																<th> Similar Categories </th>
																<th>Action</th>
															</tr>
														</thead>
														<tbody>
															<c:forEach items="${categoryName}" var="category">
																<tr>
																	<td>${category.categoryId }</td>
																	<td class="center">
																		<%@ include file="/jsp/templates/listCategoriesParentLink.jsp" %>
																	</td>
																	<td>${category.categoryName }</td>
																	<td class="center"><c:if
																			test="${category.categoryIcon != null}">
																			<div style="height:100px;width:100px;background-image: url('/uploaded_images/categoryimages/background.png');background-size:100%;">
													                        	<!-- if image name exists, mentions source of image -->
													                        	<c:if test="${not empty category.categoryIcon }">
													                        		<img id="category_image" src="/uploaded_images/categoryimages/${category.categoryId}/${category.categoryIcon}">
													                        	</c:if>
												                        	</div>
																		</c:if></td>
																	<td class="center">
																		<c:forEach
																			items="${category.subCategories }" var="subCategory">
																			${subCategory.categoryName }<br>
																		</c:forEach>
																	</td>
																	
																	<td class="center">
																		<a href="${pageContext.request.contextPath}/web/categories/similarCategories.htm?categoryId=${category.categoryId}">Similar</a>
																	</td>
																	
																	
																	
																	<td class="menu-action"><a
																		href="${pageContext.request.contextPath }/web/categories/edit.htm?id=${category.categoryId }"
																		class="btn menu-icon vd_bg-yellow"
																		data-placement="top" data-toggle="tooltip"
																		data-original-title="edit"> <i
																			class="fa fa-pencil"></i>
																	</a> <a href="javascript:confirmCategoryDelete('${pageContext.request.contextPath }/web/categories/delete.htm?id=${category.categoryId }');"
																		class="btn menu-icon vd_bg-red" data-placement="top"
																		data-toggle="tooltip" data-original-title="delete">
																			<i class="fa fa-times"></i>
																	</a></td>
																</tr>
															</c:forEach>
															<c:if test="${empty categoryName }">
																<tr>
																	<td colspan="6" style="text-align:center"> Sorry, No results found </td>
																</tr>
															</c:if>
														</tbody>
													</table>
												</div>
												
												</form:form>
												<%--following variables are used by pager.jsp and it has to be setted --%>
												<c:set var="totalNoOfPages" value="${categoryListFormBean.paginator.totalNoOfPages }"/>
												<c:set var="currentPage" value="${categoryListFormBean.paginator.currentPageNo}"/>
								                      <div style="text-align:center;">
														<c:if test="${loopCount != 0 }">
															<%@ include file="/jsp/pager.jsp" %>
														</c:if>
													</div>
											</div>
											<!-- Panel Widget -->
										</div>
										<!-- col-md-12 -->
									</div>

								</div>
								
								<!-- vd_content-section -->
							</div>
							<!-- vd_title-section -->

							<div class="vd_content-section clearfix">
								<div class="row">

									<!--col-md-7 -->

									<!-- .col-md-5 -->
								</div>

							</div>
							<!-- .vd_content-section -->

						</div>
						<!-- .vd_content -->
					</div>
					<!-- .vd_container -->
				</div>
				<!-- .vd_content-wrapper -->
			</div>
			<!-- .container -->
		</div>
		<!-- .content -->

	<%@ include file="/jsp/common/adminFooter.jsp"%>
	</div>
	<%@ include file="/jsp/common/commonScripts.jsp"%>
	<script type="text/javascript">
	function resetcategorySearchForm(){
		jQuery("#searchText").val("");
		jQuery("input[id$='currentPageNo']").val(0);
		jQuery("form").submit();
	}

	function sortResultsBy(element){debugger;
		var sortOrder = jQuery("#sortOrder").val();
		var sortBy = jQuery(element).data("sort-by");
		if(jQuery('#sortBy').val() == sortBy && sortOrder == "ASC")
			sortOrder = "DESC";
		else
			sortOrder = "ASC";
		jQuery("input[name='paginator.currentPageNo']").val(0);
		jQuery('#sortOrder').val(sortOrder);
		jQuery('#sortBy').val(sortBy).closest('form').submit();
	}
	jQuery(document).ready(function(){
		jQuery("form button[type=submit]").click(function() {
			jQuery("button[type=submit]", jQuery(this).parents("form")).removeAttr("clicked");
			jQuery(this).attr("clicked", "true");
		});
		jQuery("form").submit(function(){
			var val = jQuery("button[type=submit][clicked=true]").val();
			if(val == "filter"){
				jQuery("input[id$='currentPageNo']").val(0);
			}
		});
	});
	</script>
</body>
</html>