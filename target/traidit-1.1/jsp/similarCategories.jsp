<%@ include file="/jsp/common/common.jsp"%>
<!DOCTYPE html>
<!--[if IE 8]>			<html class="ie ie8"> <![endif]-->
<!--[if IE 9]>			<html class="ie ie9"> <![endif]-->
<!--[if gt IE 9]><!-->	<html><!--<![endif]-->

<head>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<title>${title }</title>
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
								<c:when test="${not empty failureMessage }">
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
									<h1>Similar Categories</h1>
									<small class="subtitle"></small>
									
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
														</span> Similar Categories 
													</h3>
												</div>
												<div class="panel-body  table-responsive">	
													<input type="hidden" id="categoryId" value="${categoryId }" />												
													<div class="feedback">
															${message}
													</div>
													<div class="similarCategories" >
														<div class="categoryListHdr"> "${fn:length(similarCategories)}" Similar Categories Found for "${category}" </div>
														 <c:if test="${fn:length(similarCategories) gt 0}">
															<table class="table table-bordered">
																	<thead>
																	  	<tr>
																			<th> Similar Category </th>
																			<th> Parent Categories </th>
																			<th> Is Bidirectional </th>
																			<th>Remove</th>
																	  	</tr>
																	</thead>
																	<tbody>
																		<c:forEach var="similarCategory" items="${similarCategories }">																			
																				   	<tr>
																				   	  <form:form method="POST" action="${pageContext.request.contextPath}/web/categories/editSimilarCategory.htm">
																				   		<td> ${similarCategory.similarCategoryDto.categoryName }</td>
																				   		<td>${similarCategory.similarCategoryDto.categoryBreadCumb }</td>
																				   		<input type="hidden" name="id" value="${similarCategory.id }" />	
																				   		<input type="hidden" name="categoryId" value="${categoryId}" />
																				   		<td>
																					   		  <c:choose>
																					   		  	<c:when test="${ not empty similarCategory.isBidirection}">
																						   			<select name="isBidirection" style="width:50%;">																					   				
																						   				<option value="true" ${similarCategory.isBidirection eq true ? 'selected' : ''}>Yes</option>
																						   				<option value="false" ${similarCategory.isBidirection eq false ? 'selected' : ''}>No</option>
																						   			</select>	
																						   			<input type="submit" value="Edit" />	
																					   			</c:when>	
																					   			<c:otherwise>
																					   				NA
																					   			</c:otherwise>																	   			
																					   		</c:choose>
																				   		</td>
																				   		<td><a href="#" onclick="deleteSimilarCategory(${similarCategory.id});"><img src="${pageContext.request.contextPath}/img/delete_32.png" style="width: 18%;height: 50%;"/></a></td>
																				   	</form:form>
																				   </tr>																			
																		</c:forEach>
																	</tbody>
																</table>
																
																
																** NA indicates Un-Conditional Mapping
													    </c:if>	
												 </div>
												 <div class="similarCategories" >
													<!--  Adding of Similar Categories -->    
														<div class="similarCategory" style="text-align:center;" >
															<h3 align="Center">Add Similar Categories</h3>
															<form:form method="POST" commandName="similarCategoryForm" id="similarCategoryForm" action="/asd/htm">
																
															
																<!-- <button type="submit" class="addBtn">Save All</button> -->
															</form:form>
														
														</div>
													
													
													  <!--  Restricting Adding Similar Categories to 2  --> 
													     <c:if test="${fn:length(similarCategories) lt 2}">
													        <div class="addSimilarCategory">
													        	<a href="#" onClick="addSimilarCategory();" id="addSimilarCatAnchor" class="btn vd_btn vd_bg-green vd_white" style="margin-left: 30px; margin-bottom: 10px;">Add Similar Category</a>													        
													        </div>
													     </c:if>						
												</div>
												
												<div class="searchBox" style="">
														<h3> Search Categories</h3>
														
														<div class="searchHead">
																<label>Enter Category:</label>
																<input type="text" name="category" id="categoryKeyword" style="width:40%" />
																<button value="Search" type="submit" onclick="searchCategory();">Search</button>
														</div>
														<span id="searchError"></span>
														<div class="searchResultTable" style="margin-top:20px; text-align: center;">
															
														</div>
													
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
	
		var index=${fn:length(similarCategories)};

		
		/*
			Method to Search Category
		*/
		function searchCategory(){
			$('.searchResultTable').empty();
			var category=$('#categoryKeyword').val();
			if(null!=category && category.trim().length>0){
				$('#searchError').text("");
				$.ajax({
					method:'POST',
					url:'${pageContext.request.contextPath}/web/categories/searchCategory.htm',
					data:{
						category:category
					},
					success:function(data){
						$('.searchResultTable').append(data);
						//alert(data);
					},
					error:function(data){
						alert('error');
						alert(data);
					}
				});
			}
			else{
				$('#searchError').text("* Enter Category to Search");
			}
		}
	
	
		
		/*
	 	  Method to add Similar Category
	    */
		function addSimilarCategory(){
			if(index<2){
				var category=$('#categoryId').val();				
				$.ajax({
					method:'POST',
					url: '${pageContext.request.contextPath}/web/categories/addSimilarCategory.htm',
					data:{
						index:index,
						category:category
					},
					success:function(data){
						$('#similarCategoryForm').append(data);
						$('#addSimilarCatAnchor').hide();
						index=index+1;
					},
					error:function(data){
						
					}					
				});		
				
			}else{
				alert("Only Two Similar Categories can be Mapped");
			}			
		}
	
		
		
	/* 
		Method to delete Similar Category
	*/
	function deleteSimilarCategory(id){
		var category=$('#categoryId').val();
		var r = confirm("You are about to delete Similar Category, Confirm to continue");
		if(r==true){
			window.location="${pageContext.request.contextPath}/web/categories/deleteSimilarCategory.htm?id="+id+"&categoryId="+category;
		}
	}
	
	
	/*
		Method to cancel process of addingg Similar Category
	*/
	function cancelAddSimilarCategory(){
		$('#similarCategoryForm').empty();
		index=index-1;
		$('#addSimilarCatAnchor').show();
	}
	
	
	function validateSimilarCategory(id){
		var similarCategory=$('#similarCategory'+id).val();
		if(null!=similarCategory && similarCategory.trim().length>0){
			$('#simCategoryError').text('');
			return true;
		}
		else{
			$('#simCategoryError').text('* Required');
			return false;
		}
	}
	
	
	</script>
</body>
</html>