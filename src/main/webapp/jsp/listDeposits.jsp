<%@ include file="/jsp/common/common.jsp"%>
<!DOCTYPE html>
<!--[if IE 8]>			<html class="ie ie8"> <![endif]-->
<!--[if IE 9]>			<html class="ie ie9"> <![endif]-->
<!--[if gt IE 9]><!-->	<html><!--<![endif]-->

<head>
<title>User Deposits</title>
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
									<h1>Users Deposits</h1>
									<small class="subtitle"> </small>
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
														</span> List of Deposited Users
													</h3>
												</div>
												<div class="panel-body  table-responsive" id="switch-input">
												<form:form 
									                commandName="categoryListFormBean" 
									                method="POST" 
									                action="${pageContext.request.contextPath}/web/depositslist.htm" 
									                class="form-horizontal" 
									                role="form" 
									               >
									                
													<form:hidden path="paginator.noOfItemsPerPage"/>
													<form:hidden path="paginator.currentPageNo"/>
													<form:hidden path="sortBy"/>
													<form:hidden path="sortOrder"/>
													
														
									                      
														<div class="form-group">
									                        <label class="col-sm-4 control-label">Search</label>
									                        <div class="col-sm-7 controls">
									                        	<form:input path="searchText"/>
									                        	<span class="help-inline">Search for first name,middle name,last name</span>
									                        </div>
									                    </div>
									                      <div class="form-group form-actions">
									                        <div class="col-sm-4"> </div>
									                        <div class="col-sm-7">
									                        	<form:button class="btn vd_btn vd_bg-green vd_white" name="optionChoosed" value="filter"><i class="icon-ok"></i> Filter</form:button>
									                        	<button class="btn vd_btn" type="button" onclick="resetUserFilterForm()">Reset</button>
									                     	</div>
									                      </div>
														<div style="overflow:scroll;overflow-y:hidden;">		
															<table class="table table-bordered">
																<thead>
																	<tr>
																		<th>Sno</th>
																		<th><a href="#" data-sort-by="user.firstName" onclick="sortResultsBy(this)">First Name</a></th>
																		<th><a href="#" data-sort-by="user.middleName" onclick="sortResultsBy(this)">Middle Name</a></th>
																		<th><a href="#" data-sort-by="user.lastName" onclick="sortResultsBy(this)">Last Name</a></th>
																		<th><a href="#" data-sort-by="bankName" onclick="sortResultsBy(this)">Bank Name</a></th>
																		<th><a href="#" data-sort-by="accountType" onclick="sortResultsBy(this)">Account Type</a></th>
																		<th><a href="#" data-sort-by="routingNumber" onclick="sortResultsBy(this)">Routing Number</a></th>
																		<th><a href="#" data-sort-by="accountNumber" onclick="sortResultsBy(this)">Account Number</a></th>
																			
																	</tr>
																</thead>
																<tbody>
																	<c:if test="${not empty deposits }">
																		<c:forEach items="${deposits}" var="deposit" varStatus="loop">
																		<c:set var="loopCount" value="${loop.index+1}"></c:set>
																			<tr>
																				<td>${i}</td>
																				<td>${deposit.traiditUser.firstName}</td>
																				<td>${deposit.traiditUser.middleName}</td>
																				<td>${deposit.traiditUser.lastName}</td>
																				<td>${deposit.bankName}</td>
																				<td>${deposit.accountType}</td>
																				<td>${deposit.routingNumber}</td>
																				<td>${deposit.accountNumber}</td>
																				
																			</tr>
																			 <c:set var="i" value="${i+1}" />
																		</c:forEach>
																	</c:if>
																</tbody>
															</table>
															<c:if test="${empty deposits }"><td style="text-allign:center;">no deposits found</td></c:if>
															<c:if test="${not empty errorMsg }"><td style="text-allign:center;">No Deposits Found For Search Criteria</td></c:if>
														</div>
													</form:form>	
												</div>
													<%--following variables are used by pager.jsp and it has to be setted --%>
													<c:set var="totalNoOfPages" value="${categoryListFormBean.paginator.totalNoOfPages}"/>
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
	function resetUserFilterForm(){
		jQuery("#fieldsToBeShown").val(function() {
	        return this.defaultValue;
	    });
		jQuery("#searchText").val("");
		jQuery("form").submit();
	}
	function onChangeOfUserStatus(element){
		var status = jQuery(element).prop("checked");
		var userId = jQuery(element).data("user-id");
		jQuery.ajax({
				type : "post",
				url  : "${pageContext.request.contextPath}/web/user/setStatus.htm",
				data : 'userId='+userId+'&isEnabled='+status,
				success : function(data){debugger;
					response = JSON.parse(data);
					if(response.result == 1){
						alert("updated successfully");
					}
					else if(response.result == 0){
						status = !status;
						jQuery(element).prop("checked",status);
						switchCheckboxToCurrentValue(element);
						alert("sorry, some error occured");
					}
				},
				error : function(){alert("error")}
			});
	}
	function switchCheckboxToCurrentValue(element){
		var status = jQuery(element).prop("checked");
		if(status == true){
			jQuery(element).parents('.bootstrap-switch').removeClass("bootstrap-switch-off").addClass("bootstrap-switch-on");
		}
		else{
			jQuery(element).parents('.bootstrap-switch').removeClass("bootstrap-switch-on").addClass("bootstrap-switch-off");
		}
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
	
	</script>
</body>
</html>