<%@ include file="/jsp/common/common.jsp"%>
<!DOCTYPE html>
<!--[if IE 8]>			<html class="ie ie8"> <![endif]-->
<!--[if IE 9]>			<html class="ie ie9"> <![endif]-->
<!--[if gt IE 9]><!-->	<html><!--<![endif]-->

<head>
<title>List Categories</title>
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
								<c:when test="${successMessage }">
									<div class="col-md-12 success_message">${successMessage }</div>
								</c:when>
								<c:when test="${failureMessage }">
									<div class="col-md-12 error_message">${failureMessage }</div>
								</c:when>
								<c:otherwise>
									<div class="col-md-12 success_message"></div>
								</c:otherwise>
							</c:choose>
							<c:if test="${failureMessage }">
								<div class="col-md-12 success_message">asdfasdf</div>
							</c:if>
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
														commandName="categoriesListFormBean"
														method="POST">
															<form:hidden path="category.categoryId"/>
															<input type="hidden" name="subcategorylevel" value="0">
															<div class="form-group category_0_wrapper">
																<label class="col-sm-4 control-label">Category Name</label>
																<div class="col-sm-7 controls">
										                			<select name="category_0" id="category_0" class="width-50" data-category_level="0" onchange="updateSubCategoryList(this)" >
																		<option value="0">--select--</option>
																		<c:forEach items="${parentCategories}" var="category">
																			<option value="${category.categoryId}">${category.categoryName}</option>
																		</c:forEach>
										                			</select >
										                			<input value="Get SubCategories" data-category_level="0" class="btn vd_btn vd_bg-green vd_white category_list_trigger_0" type="button" onclick="getSubCategories(this);">
															 	</div>
															</div>
														  	<div id="CategoryInsertionArea"></div>
															<div class="form-group form-actions">
										                        <div class="col-sm-4"> </div>
										                        <div class="col-sm-7">
										                        <form:button name="submitOption" value="save" class="btn vd_btn vd_bg-green vd_white"><i class="icon-ok"></i>Edit</form:button>
										                        <form:button name="submitOption" value="cancel" class="btn vd_btn"><i class="icon-ok"></i>Cancel</form:button>
										                        </div>
										                    </div>
								                    </form:form>
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
		var maxCategoryLevel = 0;
		function getSubCategories(element){
			var index = maxCategoryLevel;
			var categoryId = jQuery("#category_"+index).val();
			if(categoryId == 0){
				alert("please select some category");
				return;
			}
			jQuery.ajax({
				type:"POST",
				url:"${pageContext.request.contextPath}/web/categories/getSubCategoryDropDown.htm",
				data:{index:index+1,categoryId:categoryId},
				success:function(data){
					if(jQuery.trim(data) == ""){
						alert("sorry, no sub categories found");
						jQuery(element).remove();
						return;
					}
					jQuery(data).insertBefore(jQuery("#CategoryInsertionArea"));
					jQuery(element).siblings(".removeSubCategoryTrigger").remove();
					jQuery(element).remove();
					maxCategoryLevel++;
				}
			});
		}
		function updateSubCategoryList(element){
			jQuery("input[id='category.categoryId']").val(jQuery(element).val());
			var index = jQuery(element).data("category_level");
			for(var i = index+1; i <= maxCategoryLevel; i++){
				jQuery(".category_"+i+"_wrapper").empty();
			}
			maxCategoryLevel = index;
			if(jQuery(".category_list_trigger_"+index).val() == undefined){

				jQuery(".removeSubCategoryTrigger").remove();
				if(index>0){
					jQuery('<input value="X" data-category_level="'+index+'" class="btn vd_btn vd_bg-green vd_white removeSubCategoryTrigger" type="button" onclick="removeSubCategoryList(this);">').insertAfter("#category_"+index);
				}
				jQuery("<input value='Get SubCategories' data-category_level='"+index+"' class='btn vd_btn vd_bg-green vd_white category_list_trigger_"+index+"' type='button' onclick='getSubCategories(this);'>").insertAfter("#category_"+index);
				
			}
		}
		function removeSubCategoryList(element){debugger;
			console.log("asd");
			var targetIndex = jQuery(element).data("category_level");
			if(targetIndex > 0){
				jQuery("#category_"+(targetIndex-1)).change();
			}
		}
		jQuery(document).ready(function(){
			jQuery("button[type=submit]").click(function() {
				jQuery("input[type=submit]", jQuery(this).parents("form")).removeAttr("clicked");
				jQuery(this).attr("clicked", "true");
			});
			jQuery("#categoriesListFormBean").submit(function(event){
				 var val = jQuery("button[type=submit][clicked=true]").val();
				 if(val != "save")
					 return true;
				if(jQuery("#category_"+maxCategoryLevel).val()==0){
					alert("please select a category to edit");
					return false;
				}
			});
		});
	</script>
</body>
</html>