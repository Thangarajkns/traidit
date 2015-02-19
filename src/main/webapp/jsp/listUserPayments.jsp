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
									<h1>User Payment details</h1>
									<small class="subtitle"></small>
									<div class="vd_panel-menu">
									<form action="${pageContext.request.contextPath }/web/user/makemanualpayment.htm" method="post" style="float:left;margin:0 10px">
										<input type="hidden" name="userId" value="${userDto.userId } ">
										<button	type="submit" class="btn vd_btn vd_bg-green vd_white">Add Manual Payment</button>
									</form>
									<c:choose>
										<c:when test="${userDto.plans.planId == 2  }">
											<form action="${pageContext.request.contextPath }/web/user/extendtrailplan.htm" method="post" style="float:left;margin:0 10px">
												<input type="hidden" name="userId" value="${userDto.userId } ">
														<button	type="submit" class="btn vd_btn vd_bg-green vd_white">Extend Trial Period</button>
											</form>
										</c:when>
									</c:choose>
									<a
										href="${pageContext.request.contextPath }/web/user/listUsers.htm"
										class="btn vd_btn vd_bg-green vd_white">Back</a>
										<!-- vd_mega-menu -->
									</div>
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
													</span> List of Payments
												</h3>
											</div>
											<div class="panel-body  table-responsive" id="switch-input">
												<div class="row">
							                        <div class="col-sm-6 controls">
							                        	<div class="row">
									                        <div class="col-sm-6 controls">
									                        	User Name
									                        </div>
									                        <div class="col-sm-6 controls">
									                        	${userDto.userName } 
									                        </div>
								                      	</div>
								                      	<div class="row">
									                        <div class="col-sm-6 controls">
									                        	User E-mail
									                        </div>
									                        <div class="col-sm-6 controls">
									                        	${userDto.email } 
									                        </div>
								                      	</div>
								                      	<div class="row">
									                        <div class="col-sm-6 controls">
									                        	Account Creation Date
									                        </div>
									                        <div class="col-sm-6 controls">
									                        	<fmt:formatDate value="${userDto.accountCreationDate }" pattern="yyyy-MM-dd" />
									                        </div>
								                      	</div>
							                        </div>
							                        <div class="col-sm-6 controls">
							                        	<div class="row">
									                        <div class="col-sm-6 controls">
									                        	Adderss
									                        </div>
									                        <div class="col-sm-6 controls">
									                        	${userDto.city }<br>
									                        	${userDto.state }<br>
									                        	${userDto.country } - ${userDto.zip }<br>
									                        </div>
								                        </div>
							                        	<div class="row">
									                        <div class="col-sm-6 controls">
									                        	Account Expiration Date
									                        </div>
									                        <div class="col-sm-6 controls">
									                        	<fmt:formatDate value="${userDto.accountExpiryDate }" pattern="yyyy-MM-dd" />
									                        </div>
								                        </div>
							                        </div>
						                      	</div>
												
											</div>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="panel widget">
											<div class="panel-heading vd_bg-grey">
												<h3 class="panel-title">
													<span class="menu-icon"> <i
														class="fa fa-dot-circle-o"></i>
													</span> List of Payments
												</h3>
											</div>
											<div class="panel-body  table-responsive" id="switch-input">
											<form:form 
								                commandName="payments" 
								                method="POST" 
								                action="${pageContext.request.contextPath}/web/users/list.htm" 
								                class="form-horizontal" 
								                role="form" 
								                enctype="multipart/form-data">
											
																
												<table class="table table-bordered">
													<thead>
														<tr>
															<th>Payment Id</th>
															<th>plan</th>
															<th>Amount</th>
															<th>Paid Date</th>
														</tr>
													</thead>
													<tbody>
														<c:forEach items="${payments}" var="payment" varStatus="loop">
															<tr>
																<td>${payment.subscriptionId }</td>
																<td>${payment.plan.planName }</td>
																<td>${payment.amount }</td>
																<td><fmt:formatDate value="${payment.paidDate }" pattern="yyyy-MM-dd" /></td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
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
	</script>
</body>
</html>