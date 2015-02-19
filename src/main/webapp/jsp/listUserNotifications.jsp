<%@ include file="/jsp/common/common.jsp"%>
<!DOCTYPE html>
<!--[if IE 8]>			<html class="ie ie8"> <![endif]-->
<!--[if IE 9]>			<html class="ie ie9"> <![endif]-->
<!--[if gt IE 9]><!-->	<html><!--<![endif]-->

<head>
<title>Admin Dashboard</title>
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
									<h1>User Management</h1>
									<small class="subtitle"> </small>
									<div class="vd_panel-menu">
										<a
											href="${pageContext.request.contextPath }/web/user/addallusernotification.htm"
											class="btn vd_btn vd_bg-green vd_white">Send New Norification</a>
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
														</span> List of Users
													</h3>
												</div>
												<div class="panel-body  table-responsive" id="switch-input">	
													<div style="overflow:scroll;overflow-y:hidden;">		
															<table class="table table-bordered">
																<thead>
																	<tr>
																		<th>Notification ID</th>
																		<th>Notification Message</th>
																		<th>Created On</th>
																		<th>Sent On</th>
																	</tr>
																</thead>
																<tbody>
																<c:forEach items="${notificationList}" var="notification">
																	<tr>
																		<td>${notification.id }</td>
																		<td>${notification.notificationMessage }</td>
																		<td>${notification.createdOn }</td>
																		<c:if test="${notification.notificationSent }">
																		<td>${notification.notificationSentOn }</td>
																		</c:if>
																		<c:if test="${not notification.notificationSent }">
																		<td>
																		<form action="sendallusernotification.htm" method="post">
																			<input type="hidden" name="notificationId" value="${notification.id }">
																			<input type="submit" name="Send" value="Send"/>
																			
																		</form>
																		</td>
																		</c:if>
																	</tr>
																	
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
</body>
</html>