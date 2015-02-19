<%@ include file="/jsp/common/common.jsp"%>
<html>
<head>
<title>Add Categories</title>
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

						<div class="vd_title-section clearfix">
							<div class="vd_panel-header">
								<h1>User Notification</h1>
								<small class="subtitle"></small>
							</div>      
							<!-- vd_panel-header -->
							<div class="vd_content-section clearfix">
							
								<div class="row" id="form-basic">
								  <div class="col-md-12">
									<div class="panel widget">
									  <div class="panel-heading vd_bg-grey">
										<h3 class="panel-title"> <span class="menu-icon"> <i class="fa fa-bar-chart-o"></i> </span> Add User Notification</h3>
									  </div>
									  <div class="panel-body">
						                <form:form 
							                commandName="allUserNotificationFormBean" 
							                method="POST" 
							                action="" 
							                class="form-horizontal" 
							                role="form" 
							                enctype="multipart/form-data"
							                >
							                <div class="form-group">
						                        <label class="col-sm-4 control-label">Notification Message</label>
						                        <div class="col-sm-7 controls">
						                        	<form:textarea path="notificationMessage"/>
					                        	</div>
					                      	</div>
					                      	<div class="form-group form-actions">
						                        <div class="col-sm-4"> </div>
						                        <div class="col-sm-7">
							                        <form:button name="optionSelected" value="save" class="btn vd_btn vd_bg-green vd_white"><i class="icon-ok"></i>Save</form:button>
							                        <form:button name="optionSelected" value="send" class="btn vd_btn vd_bg-green vd_white"><i class="icon-ok"></i>Send</form:button>
							                        <form:button name="optionSelected" value="cancel" class="btn vd_btn cancel"><i class="icon-ok"></i>Cancel</form:button>
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
</body>
</html>