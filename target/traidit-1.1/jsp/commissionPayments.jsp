<%@ include file="/jsp/common/common.jsp"%>
<html>
<head>
<title>Commissions</title>
<%@ include file="/jsp/common/commonStyles.jsp"%>
<style type="text/css">
.vd_menu{
	margin-top: 75px;
}
</style>
</head>
<body id="dashboard"
	class="full-layout  nav-right-hide nav-right-start-hide nav-top-fixed responsive clearfix"
	data-active="dashboard" data-smooth-scrolling="1">
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
								<h1>Commissions</h1>
								<!-- vd_panel-menu -->
							</div>
							<!-- vd_panel-header -->
						</div>
						<!-- vd_title-section -->
						
						<div class="vd_content-section clearfix">
							<div class="row" id="form-basic">
							  <div class="col-md-12">
								<div class="panel widget">
								  <div class="panel-heading vd_bg-grey">
									<h3 class="panel-title"> <span class="menu-icon"> <i class="fa fa-bar-chart-o"></i> </span>Users Commission List</h3>
								  </div>
								  <div class="panel-body">
									  <form:form
									  	commandName="commissionPaymentsForm"
									  	method="POST"
									  	action=""
									  	class="form-horizontal" 
									    role="form" 
									    enctype="multipart/form-data">
									    
											<form:hidden path="paginator.noOfItemsPerPage"/>
											<form:hidden path="paginator.currentPageNo"/>
											<form:hidden path="sortBy"/>
											<form:hidden path="sortOrder"/>
						                	 <table class="table table-bordered">
												<thead>
													<tr>
														<th>Name</th>
														<th><a href="#" data-sort-by="paymentType" onclick="sortResultsBy(this)">Payment Type</a></th>
														<th><a href="#" data-sort-by="date" onclick="sortResultsBy(this)">Date</a></th>
														<th><a href="#" data-sort-by="amount" onclick="sortResultsBy(this)">Amount</a></th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${commissionPayments }" var="commissionPayment">
														<c:set var="loopCount" value="${loop.index+1}"></c:set>
														<tr>
															<td>${commissionPayment.benificiary.firstName }</td>
															<td>${commissionPayment.paymentType}</td>
															<td><fmt:formatDate value="${commissionPayment.date}" pattern="dd-MM-yyyy"/></td>
															<td>$${commissionPayment.amount}</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</form:form>
								  </div>
								  	<%--following variables are used by pager.jsp and it has to be setted --%>
									<c:set var="totalNoOfPages" value="${commissionPaymentsForm.paginator.totalNoOfPages}"/>
									<c:set var="currentPage" value="${commissionPaymentsForm.paginator.currentPageNo}"/>
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
							<!-- row -->      
						</div>
						<!-- vd_content-section -->
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
	function sortResultsBy(element){
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