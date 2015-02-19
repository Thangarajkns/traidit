<%@ include file="/jsp/common/common.jsp"%>
<!DOCTYPE html>
<!--[if IE 8]>			<html class="ie ie8"> <![endif]-->
<!--[if IE 9]>			<html class="ie ie9"> <![endif]-->
<!--[if gt IE 9]><!-->
<html>
<!--<![endif]-->

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

							<div class="vd_title-section clearfix">
								<div class="vd_panel-header">
									<h1>Make Manual Payment</h1>
									<small class="subtitle"></small>
								</div>
								<!-- vd_panel-header -->

							</div>
							<!-- vd_title-section -->

							<div class="vd_content-section clearfix">

								<div class="row" id="form-basic">
									<div class="col-md-12">
										<div class="panel widget">
											<div class="panel-heading vd_bg-grey">
												<h3 class="panel-title">
													<span class="menu-icon"> <i
														class="fa fa-bar-chart-o"></i>
													</span> Add Payment
												</h3>
											</div>
											<div class="panel-body">
												<form:form commandName="makeManualPayment" method="POST"
													action="${pageContext.request.contextPath}/web/user/processmanualpayment.htm"
													class="form-horizontal" role="form"
													enctype="multipart/form-data">
													<form:hidden path="user.userId"/>
													<div class="form-group ">
								                        <label class="col-sm-4 control-label">User Name (Read Only)</label>
								                        <div class="col-sm-7 controls">
								                        	<form:input path="user.userName" readonly="true"/>
								                        </div>
							                      	</div>
							                      	<div class="form-group ">
								                        <label class="col-sm-4 control-label">Plan</label>
								                        <div class="col-sm-7 controls">
								                        	<form:select path="plan.planId" items="${plans}" itemLabel="planName" itemValue="planId" onchange="updatePlanPrice(this)"/>
								                        </div>
							                      	</div>
							                      	<div class="form-group ">
								                        <label class="col-sm-4 control-label">Plan Amount (Read Only)</label>
								                        <div class="col-sm-7 controls">
								                        	<form:select path="plan.price" items="${plans}" itemLabel="price" itemValue="planId" disabled="true"/>
								                        </div>
							                      	</div>
							                      	<div class="form-group ">
								                        <label class="col-sm-4 control-label">Transaction/Invoice Id (optional)</label>
								                        <div class="col-sm-7 controls">
								                        	<form:input path="transactionId"/>
								                        </div>
							                      	</div>
							                      	<div class="form-group ">
								                        <label class="col-sm-4 control-label">Pay Commissions to Refferrals?</label>
								                        <div class="col-sm-7 controls">
								                        	<form:checkbox path="payCommissions"/>
								                        </div>
							                      	</div>
							                      	<div class="form-group" style="text-align:right">
							                      		<form:button name="submitOption" value="Pay" class="btn vd_btn vd_bg-green vd_white"><i class="icon-ok"></i>Pay</form:button>
							                      		<form:button name="submitOption" value="Cancel" class="btn vd_btn vd_bg-green vd_white"><i class="icon-ok"></i>Cancel</form:button>
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
<script type="text/javascript">
function updatePlanPrice(element){
	jQuery("[id='plan.price']").val(jQuery(element).val());
}
</script>
</html>