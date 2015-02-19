<%@ include file="/jsp/common/common.jsp"%>
<html>
<head>
<title>Admin Dashboard</title>
<%@ include file="/jsp/common/commonStyles.jsp"%>
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
						<div class="vd_title-section clearfix">
							<div class="vd_panel-header">
								<h1>Payments</h1>
								<small class="subtitle">To be paid this ${option}</small>

								<!-- vd_panel-menu -->
							</div>
							<!-- vd_panel-header -->
						</div>
						<!-- vd_title-section -->

						<div class="vd_content-section clearfix">
						
									<div class="row">
										<div class="col-md-12">
											<div class="panel widget">
												<div class="panel-heading vd_bg-grey">
													<h3 class="panel-title">
														<span class="menu-icon"> <i
															class="fa fa-dot-circle-o"></i>
														</span> Users Payment dues
													</h3>
												</div>
												<div class="panel-body  table-responsive" id="switch-input">	
													<div style="overflow:scroll;overflow-y:hidden;">		
															<table class="table table-bordered">
																<thead>
																	<tr>
																		<th>User Name</th>
																		<th>Plan Name</th>
																		<th>Plan Price</th>
																		<th>Member Since</th>
																		<th>Renewal Date</th>
																	</tr>
																</thead>
																<tbody>
																<c:forEach items="${paymentDues }" var="pamentDue">
																${paymentDue }
																</c:forEach>
																</tbody>
															</table>
														</div>
												</div>
											</div>
											<!-- Panel Widget -->
										</div>
										<!-- col-md-12 -->
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
</body>
</html>