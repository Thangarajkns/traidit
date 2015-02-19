<%@ include file="/jsp/common/common.jsp"%>
<html>
<head>
<title>Add Categories</title>
<%@ include file="/jsp/common/commonStyles.jsp"%>

<style type="text/css">
.vd_menu{
	margin-top: 75px;
}
</style>
</head>
<body id="dashboard"
	class="full-layout  nav-right-hide nav-right-start-hide  nav-top-fixed      responsive    clearfix"
	data-active="dashboard " data-smooth-scrolling="1">
	<div class="vd_body">
		<%@ include file="/jsp/common/adminHeader.jsp"%>
	</div>
	<div class="content">
		<div class="container">
			<%@ include file="/jsp/common/sideNavigation.jsp"%>
			<!-- Middle Content Start -->

			<div class="vd_content-wrapper">
				<div class="vd_container">
					<div class="vd_content clearfix">
						<div class="clearfix"></div>

						<div class="vd_title-section clearfix">
							<div class="vd_panel-header">
								<h1>Categories</h1>
								<small class="subtitle"></small>
							</div>      
							<!-- vd_panel-header -->
							<div class="vd_content-section clearfix">
							
								<div class="row" id="form-basic">
								  <div class="col-md-12">
									<div class="panel widget">
									  <div class="panel-heading vd_bg-grey">
										<h3 class="panel-title"> <span class="menu-icon"> <i class="fa fa-bar-chart-o"></i> </span> Add Category</h3>
									  </div>
									  <div class="panel-body">
						                <form:form 
							                commandName="categoryFormBean" 
							                method="POST" 
							                action="${pageContext.request.contextPath}/web/categories/add.htm" 
							                class="form-horizontal" 
							                role="form" 
							                enctype="multipart/form-data"
							                >
						                	<form:hidden path="categoryId"/>
						                	<form:hidden path="parentCategory"/>
						                	<c:if test="${not empty categoryBreadCrumb }">
						                	<c:set var="classOnEdit" value="hide"/>
						                	 <div class="form-group parent_category_breadcrumb">
						                        <label class="col-sm-4 control-label">Parent Categories</label>
						                        <div class="col-sm-7 controls">
						                        	${categoryBreadCrumb}<a href="javascript:editCategoryParents(${categoryFormBean.parentCategory},${categoryFormBean.grandParentCategory})" class="btn vd_btn vd_bg-green vd_white">edit</a>
						                        </div>
						                      </div>
						                      </c:if>
						                      <c:if test="${not empty categoryFormBean.categoryId }">
							                	 <div class="form-group parent_category_breadcrumb">
							                        <label class="col-sm-4 control-label">Similar Categories</label>
							                        <div class="col-sm-7 controls">
							                        	<a href="${pageContext.request.contextPath}/web/categories/similarCategories.htm?categoryId=${categoryFormBean.categoryId }" class="btn vd_btn vd_bg-green vd_white">Edit Similar Categories</a>
							                        </div>
							                      </div>
						                      </c:if>
						                      <c:if test="${empty categoryBreadCrumb }"><c:set var="classOnEdit" value="show"/></c:if>
											  <div class="form-group">
												<label class="col-sm-4 control-label">Category Name</label>
												<div class="col-sm-7 controls">
						                			<form:input path="categoryName" class="width-50 required" placeholder="Category Name" required=""/>
												  </div>
											  </div>	
											  <div class="form-group">
						                        <label class="col-sm-4 control-label">Category Icon</label>
						                        <div class="col-sm-7 controls">
						                        <c:if test="${not empty categoryFormBean.categoryId }">
						                        	<div style="height:100px;width:100px;background-image: url('/uploaded_images/categoryimages/background.png');background-size:100%;">
						                        	<!-- if image name exists, mentions source of image -->
						                        	<c:if test="${not empty categoryFormBean.categoryIcon }">
						                        		<img id="category_image" src="/uploaded_images/categoryimages/${categoryFormBean.categoryId }/${categoryFormBean.categoryIcon }">
						                        	</c:if>
						                        	<!-- if image name doesnt exists, mentions source of image as empty string -->
						                        	<c:if test="${empty categoryFormBean.categoryIcon }">
						                        		<img id="category_image" src="">
						                        	</c:if>
						                        	</div>
					                        	</c:if>
						                          <form:input type="file" path="categoryIconFile"  class="width-50"/>
						                        </div>
						                      </div>
											
											  <div class="form-group">
												<label class="col-sm-4 control-label">eBay Category Ids</label>
												<div class="col-sm-7 controls">
						                			<form:input path="eBayIds" class="width-50" placeholder="0000;1111;2222" />
												  </div>
											  </div>	
											  <%-- <div class="form-group">
												<label class="col-sm-4 control-label">Similar Category Ids</label>
												<div class="col-sm-7 controls">
						                			<form:input path="similarCategoryIds" class="width-50" placeholder="0000;1111;2222" />
												  </div>
											  </div> --%>	
											<div class="form-group">
												<label class="col-sm-4 control-label"></label>
												<div class="col-sm-7 controls">
												  <div class="vd_checkbox checkbox-danger">
												  <c:if test="${categoryFormBean.isHomePageCategory}"><c:set var="isHomePageCategory" value="checked='checked'"/></c:if>
												  <c:if test="${!categoryFormBean.isHomePageCategory}"><c:set var="isHomePageCategory" value=""/></c:if>
												  <input type="checkbox" value="true" id="isHomePageCategory" name="isHomePageCategory" ${isHomePageCategory } >
													<label for="isHomePageCategory"> Show Category in home page </label>
												  </div>
												</div>
											</div>
											
						                	<div class="sub_categories_section">
							                	<div class="form-group">
							                		<label class="col-sm-4 control-label"></label>
													<div class="col-sm-7 controls">
							                			<a href="javascript:void(0)" id="add_parent_0" class="add_parent ${classOnEdit }" onclick="addParent('0',this);">add parent category</a>
							                		</div>
					                			</div>
				                			</div>
											<div class="form-group form-actions">
						                        <div class="col-sm-4"> </div>
						                        <div class="col-sm-7">
							                        <form:button name="submit" value="save" class="btn vd_btn vd_bg-green vd_white"><i class="icon-ok"></i>Save</form:button>
							                        <form:button name="submit" value="cancel" class="btn vd_btn cancel"><i class="icon-ok"></i>Cancel</form:button>
						                        </div>
						                    </div>
						                      
						                	
						                </form:form>
									  </div>
									</div>
									<!-- Panel Widget --> 
								  </div>
								  <!-- col-md-12 --> 
								</div>
								<!-- row -->      
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
	<%@ include file="/jsp/common/commonScripts.jsp"%>
	<script type="text/javascript">
		var parents = 0;
		var parentId = 0;
		var grandParentId = 0;
		/* function addParent(id){
			ele = jQuery("#"+id);
			if(ele.val() == 0){
				alert("Please select a parent");
				return;
			}
			parents++;
			var newEle = ele.parents('.form-group').clone();
			ele.attr('name',ele.attr('name')+''+parents);
			ele.attr('id',ele.attr('id')+''+parents);
			jQuery('.add_parent').remove();
			newEle.insertAfter(ele.parents('.form-group'));
		} */
		function addParent(index,ele){
			var selectedValue = 0;
			if(index != 0){
				selectedValue = jQuery("#parentCategory_"+index).val();
				if(selectedValue==0){
					//add error class
					jQuery("#parentCategory_"+index).addClass("vd_bd-red");
					return 0;
				}
			}
			jQuery.ajax({
				url:"${pageContext.request.contextPath}/web/categories/getparentcategories.htm",
				data:{"categoryId":selectedValue,"index":index+1},
				success:function(html){
					if(jQuery.trim(html) != ""){
						jQuery("#parentCategory_"+index).attr('disabled',"disabled");
						jQuery(".sub_categories_section").append(html);
						parents++;
						jQuery("#add_parent_"+index).removeClass("show").addClass("hide");
						jQuery("#remove_parent_"+index).removeClass("show").addClass("hide");
						prePopulateCategoriesIfEdit();
					}
					else{
						alert("no sub categories found");
					}
				}
			});
		}
		function updateParentCategory(ele){
			jQuery(ele).removeClass("vd_bd-red");
			jQuery('div[for*="parentCategory"]').remove();
			jQuery("#parentCategory").val(jQuery(ele).val());
			}
		function editCategoryParents(parentCategoryId,grandCategoryParentId){
			parentId = parentCategoryId;
			grandParentId = grandCategoryParentId;
			jQuery(".parent_category_breadcrumb").remove();
			jQuery("#parentCategory").val(0);
			jQuery(".add_parent").click();

		}
		function prePopulateCategoriesIfEdit(){
			if(grandParentId > 0){
				jQuery("#parentCategory_1").val(grandParentId);
				grandParentId = -1;
				jQuery("#add_parent_1").click();
				return;
			}
			else if(parentId != 0 && grandParentId < 0){
				jQuery("#parentCategory_2").val(parentId);
				grandParentId = 0;
			}
			else if(parentId != 0 && grandParentId == 0){
				jQuery("#parentCategory_1").val(parentId);
				grandParentId = 0;
			}
			
		}
		function removeParent(index,ele){
			if(index >1)
				var selectedValue = jQuery("#parentCategory_"+(index-1)).val();
			else
				var selectedValue = 0;
			jQuery("#parentCategory").val(selectedValue);
			jQuery(ele).closest(".form-group").remove();
			jQuery("#add_parent_"+(index-1)).removeClass("hide").addClass("show");
			jQuery("#remove_parent_"+(index-1)).removeClass("hide").addClass("show");
			parents--;
			jQuery("#parentCategory_"+parents).removeAttr("disabled");
		}
		jQuery(document).ready(function(){
			jQuery("form button[type=submit]").click(function() {
				jQuery("button[type=submit]", jQuery(this).parents("form")).removeAttr("clicked");
				jQuery(this).attr("clicked", "true");
			});
			jQuery("form").submit(function(){debugger;
				var val = jQuery("button[type=submit][clicked=true]").val();
				if(jQuery.trim(val)=="cancel")
					return true;
				if(jQuery("#parentCategory_"+parents).val() == 0){
					jQuery("#parentCategory_"+parents).addClass("vd_bd-red");
					jQuery('<div for="parentCategory_'+parents+'" class="vd_red show">This field is required.</div>').insertAfter("#parentCategory_"+parents);
					return false;
				}
			});
			
			//code for validations
			var submittable = false;
	        var categoryFormBean = $('#categoryFormBean');
	
	        categoryFormBean.validate({
	            errorElement: 'div', //default input error message container
	            errorClass: 'vd_red', // default input error message class
	            focusInvalid: false, // do not focus the last invalid input
	            ignore: "",
	            rules: {
	            	categoryName: {
	                    required: true,
	                },
	                /* add this rule only while adding category not while editing category */
	                <c:if test="${empty categoryFormBean.categoryId }">
	                categoryIconFile:{
	                    required:true,
	                },
	                </c:if>
	            },
				errorPlacement: function(error, element) {
					if (element.parent().hasClass("vd_checkbox") || element.parent().hasClass("vd_radio")){
						element.parent().append(error);
					} else if (element.parent().hasClass("vd_input-wrapper")){
						error.insertAfter(element.parent());
					}else {
						error.insertAfter(element);
					}
				}, 
				
	            invalidHandler: function (event, validator) { //display error alert on form submit
	
	            },
	
	            highlight: function (element) { // hightlight error inputs
			
					$(element).addClass('vd_bd-red');
					$(element).parent().siblings('.help-inline').removeClass('help-inline hidden');
					if ($(element).parent().hasClass("vd_checkbox") || $(element).parent().hasClass("vd_radio")) {
						$(element).siblings('.help-inline').removeClass('help-inline hidden');
					}
	
	            },
	
	            unhighlight: function (element) { // revert the change dony by hightlight
	                $(element)
	                    .closest('.control-group').removeClass('error'); // set error class to the control group
	            },
	
	            success: function (label, element) {debugger;
	                label
	                    .addClass('valid').addClass('help-inline hidden') // mark the current input as valid and display OK icon
	                	.closest('.control-group').removeClass('error').addClass('success'); // set success class to the control group
					$(element).removeClass('vd_bd-red');
	
						
	            } ,
	            /* 
	            submitHandler: function (form) {
					$(form).find('#login-submit').prepend('<i class="fa fa-spinner fa-spin mgr-10"></i>');					
	                success_register_2.show();
	                error_register_2.hide();
	                ;
					setTimeout(function(){window.location.href = "index.php"},2000)	 ; 				
					submittable = true;
	            }  */
	        });	

	        //category image preview on upload
	        var reader = new FileReader();
	        reader.onload = function(event) {
	            var dataUri = event.target.result,
	                img     = document.getElementById("category_image");

	            img.src = dataUri;
	        };

	        reader.onerror = function(event) {
	            console.error("File could not be read! Code " + event.target.error.code);
	        };

	          function handleFileSelect(evt) {
	            var files = evt.target.files; // FileList object

	            for (var i = 0, f; f = files[i]; i++) {
	            	reader.readAsDataURL(f);
	            }
	          }

	          document.getElementById('categoryIconFile').addEventListener('change', handleFileSelect, false);
	});
	</script>
	
</body>
</html>