<%@ include file="/jsp/common/common.jsp"%>
<!DOCTYPE html>
<!--[if IE 8]>			<html class="ie ie8"> <![endif]-->
<!--[if IE 9]>			<html class="ie ie9"> <![endif]-->
<!--[if gt IE 9]><!-->	<html><!--<![endif]-->

<head>
<title>Inventory List</title>
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
									<h1>Inventory List</h1>
									<small class="subtitle"></small>
								</div>
								<!-- vd_panel-header -->
								<div class="vd_content-section clearfix">
									<div class="row">
									</div>
									<div class="row">
										<div class="col-md-12">
											<div class="panel widget">
												<div class="panel-heading vd_bg-grey">
													<h3 class="panel-title">
														<span class="menu-icon"> <i
															class="fa fa-dot-circle-o"></i>
														</span> List of Inventories
													</h3>
												</div>
												<div class="panel-body  table-responsive" id="switch-input">
												
												<form:form 
									                commandName="inventoryFilter" 
									                method="POST" 
									                action="${pageContext.request.contextPath}/web/inventory/list.htm" 
									                class="form-horizontal" 
									                role="form" >
												
														<div class="form-group">
									                        <label class="col-sm-4 control-label">Filter By Vendor</label>
									                        <div class="col-sm-7 controls">
									                        	<form:select path="vendorId" multiple="false" class="width-40">
									                        		<form:option value="">select</form:option>
									                        		<form:options items="${listOfVendors }" itemLabel="userName" itemValue="userId" />
									                        	</form:select>
									                        </div>
									                      </div>
														<div class="form-group">
									                        <label class="col-sm-4 control-label">Search for Item</label>
									                        <div class="col-sm-7 controls">
									                        	<form:input path="itemSearcText"/>
									                        </div>
									                      </div>
									                      <div class="form-group form-actions">
									                        <div class="col-sm-4"> </div>
									                        <div class="col-sm-7">
									                        	<form:button class="btn vd_btn vd_bg-green vd_white" name="optionChoosed" value="filter"><i class="icon-ok"></i> Filter</form:button>
									                          	<button class="btn vd_btn" type="button" onclick="resetInventoriesFilterForm()">Reset</button>
									                        </div>
									                      </div>
													<form:hidden path="paginator.noOfItemsPerPage"/>
													<form:hidden path="paginator.currentPageNo"/>
													<form:hidden path="sortBy"/>
													<form:hidden path="sortOrder"/>
												</form:form>
												<form:form 
									                commandName="inventories" 
									                method="POST" 
									                action="${pageContext.request.contextPath}/web/inventory/list.htm" 
									                class="form-horizontal" 
									                role="form" >
												
																	
													<table class="table table-bordered">
														<thead>
															<tr>
																<th>Inventory Id</th>
																<th><a href="#" data-sort-by="vendor.username" onclick="sortResultsBy(this)">Vendor Name</a></th>
																<th><a href="#" data-sort-by="item.itemName" onclick="sortResultsBy(this)">Item Name</a></th>
																<th><a href="#" data-sort-by="listedDate" onclick="sortResultsBy(this)">Listed date</a></th>
																<%-- <th><a href="#" data-sort-by="price" onclick="sortResultsBy(this)">Price</a></th> --%>
																<th><a href="#" data-sort-by="unitsAvailable" onclick="sortResultsBy(this)">Units Available</a></th>
																<th><a href="#" data-sort-by="availableForTrade" onclick="sortResultsBy(this)">Available for Trade</a></th>
																<th><a href="#" data-sort-by="isEnabled" onclick="sortResultsBy(this)">Status</a></th>
															</tr>
														</thead>
														<tbody>
															<c:forEach items="${inventories}" var="inventory" varStatus="loop">
																<c:set var="loopCount" value="${loop.index+1}"></c:set>
																<tr>
																	<td>${inventory.inventoryId }</td>
																	<td>${inventory.vendorId.userName }</td>
																	<td>${inventory.itemId.itemName }</td>
																	<td><fmt:formatDate value="${inventory.listedDate }" pattern="yyyy-MM-dd" /></td>
																	<%-- <td>${inventory.price }</td> --%>
																	<td>${inventory.unitsAvailable }</td>
																	<td>${inventory.availableForTrade }</td>
																	<td>
																		<div class="form-group">
																			<div class="col-sm-10 controls">
																				<c:if test="${inventory.isEnabled == true}"><c:set var="enabledStatus" value="checked"></c:set></c:if>
																				<c:if test="${inventory.isEnabled == false }"><c:set var="enabledStatus" value=""></c:set></c:if>
																				<input 
																					type ="checkbox" 
																					name="inventory[${loop.index}].availableForTrade" 
																					data-inventory-id="${inventory.inventoryId }" ${enabledStatus } 
																					data-rel="switch" 
																					data-size="mini" 
																					data-wrapper-class="yellow" 
																					onchange="onChangeOfInventoryEnabledStatus(this)"/>
																			</div>
																		</div>
																	</td>
																</tr>
															</c:forEach>
														</tbody>
													</table>
												</form:form>
												</div>
													<%--following variables are used by pager.jsp and it has to be setted --%>
													<c:set var="totalNoOfPages" value="${inventoryFilter.paginator.totalNoOfPages}"/>
													<c:set var="currentPage" value="${inventoryFilter.paginator.currentPageNo}"/>
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

	function resetInventoriesFilterForm(){
		jQuery("#vendorId").val(function() {
	        return this.defaultValue;
	    });
		jQuery("#itemSearcText").val("");
		jQuery("#inventoryFilter").submit();
	}
	function onChangeOfInventoryEnabledStatus(element){
		var status = jQuery(element).prop("checked");
		var userId = jQuery(element).data("inventory-id");
		jQuery.ajax({
				type : "post",
				url  : "${pageContext.request.contextPath}/web/inventory/setInventoryEnabledStatus.htm",
				data : 'inventoryId='+userId+'&enabledStatus='+status,
				success : function(data){
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
				error : function(){
					status = !status;
					jQuery(element).prop("checked",status);
					switchCheckboxToCurrentValue(element);
					alert("sorry, some error occured");
				}
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