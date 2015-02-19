<%@ include file="/jsp/common/common.jsp"%>
<!DOCTYPE html>
<html>
<head>

<title>User Payments</title>
 	<%@ include file="/jsp/common/commonStyles.jsp"%>
 	


		
		<style type="text/css">
.reporttitle{
		clear: both;
		text-align: center;
		margin-top: 25px;
		font-size: 20px;
		color: #a34b00;
		font-family: serif;
}

</style>
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
									<h1>User Payments</h1>
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
														</span> User Payment Reports
													</h3>
												</div>
								 <form method="post" action="/traidit/reports/gettodaypayments.htm" target="_blank"   >
								 <div>
								 	<div class="reporttitle">Today Payment Dues</div>
								 		<div>
								 			
											<button type="submit" style="clear: both;margin: 25px auto;margin-left:20%" class="btn btn-success">Payments PDF Report</button>
											<a href="/traidit/reports/gettodaypayments.htm?export=excel" target="_blank" class="btn btn-success"> Payments Excel Report	</a>
										</div>
								</div>
								<div>
									<div class="reporttitle">Weekly Payment Dues</div>
									<div>
										<label style="margin-left: 15%;margin-top:10px;font-size: 17px;">This Weeks:</label>
											<a href="/traidit/reports/getweeklypayments.htm?term=week" target="_blank" class="btn btn-success"> Payments PDF Report	</a>
											<a href="/traidit/reports/getweeklypayments.htm?term=week&export=excel" target="_blank" class="btn btn-success"> Payments Excel Report	</a>	
									</div>
								</div>
								<div>
									<div class="reporttitle">Monthly Commission Payments </div>
									<div>
										<label style="margin-left:15%;margin-top:10px;font-size: 17px;">For This Month:</label>
											<a href="/traidit/reports/getmonthlycommissions.htm" target="_blank" class="btn btn-success">Commission Payments PDF Report	</a>
											<a href="/traidit/reports/getmonthlycommissions.htm?export=excel" target="_blank" class="btn btn-success">Commission Payments Excel Report	</a>	
									</div>
								</div>
								<div>
									<div class="reporttitle">Commission From Start Of Year To Date </div>
									<div>
										<label style="margin-left: 15%;margin-top:10px;font-size: 17px;">From start of Year:</label>
											<a href="/traidit/reports/getcommissionsfromstartofyear.htm" target="_blank" class="btn btn-success">Commissions PDF Report	</a>
											<a href="/traidit/reports/getcommissionsfromstartofyear.htm?export=excel" target="_blank" class="btn btn-success">Commissions Excel Report	</a>	
									</div>
								</div>
								 </form>		
							</div>
							</div>
							</div>
							</div>
							</div>
						
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