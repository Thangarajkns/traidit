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
								
								
								<div class="row">
										<div class="col-md-12">
											<div class="panel widget">
												<div class="panel-heading vd_bg-grey">
													<h3 class="panel-title">
														<span class="menu-icon"> <i
															class="fa fa-dot-circle-o"></i>
														</span>Direct Deposit Privacy Policy
													</h3>
												</div>
												<div class="panel-body  table-responsive" id="tab_directdepositpolicy" style="display:none">
												 <form:form 
									                commandName="directDepositPrivacyPolicyForm" 
									                method="POST" 
									                action="${pageContext.request.contextPath}/web/privacypolicy/directdeposit.htm" 
									                class="form-horizontal" 
									                role="form" 
									                enctype="multipart/form-data"
									                >
														<div class="form-group parent_category_breadcrumb">
															<label class="col-sm-4 control-label">Privacy Policy</label>
															<div class="col-sm-7 controls">
																<form:textarea path="PrivacyPolicy"  class="ckeditor"/>
															</div>
														</div>
														
														<div class="form-group form-actions">
									                        <div class="col-sm-4"> </div>
									                        <div class="col-sm-7">
									                        <form:button name="submit" value="save" class="btn vd_btn vd_bg-green vd_white"><i class="icon-ok"></i>Save</form:button>
									                        <form:button name="submit" value="reset" class="btn vd_btn cancel"><i class="icon-ok"></i>Reset</form:button>
									                        </div>
									                    </div>
									                </form:form>
												</div>
											</div>
											<!-- Panel Widget -->
										</div>
										<!-- col-md-12 -->
										
										<div class="col-md-12">
											<div class="panel widget">
												<div class="panel-heading vd_bg-grey">
													<h3 class="panel-title">
														<span class="menu-icon"> <i
															class="fa fa-dot-circle-o"></i>
														</span>Direct Deposit Terms and Conditions
													</h3>
												</div>
												<div class="panel-body  table-responsive" id="tab_directdepositterms"  style="display:none">
												 <form:form 
									                commandName="directDepositTerms" 
									                method="POST" 
									                action="${pageContext.request.contextPath}/web/termsncondition/directdeposit.htm" 
									                class="form-horizontal" 
									                role="form" 
									                enctype="multipart/form-data"
									                >
														<div class="form-group parent_category_breadcrumb">
															<label class="col-sm-4 control-label">Privacy Policy</label>
															<div class="col-sm-7 controls">
																<form:textarea path="PrivacyPolicy" class="ckeditor"/>
															</div>
														</div>
														
														<div class="form-group form-actions">
									                        <div class="col-sm-4"> </div>
									                        <div class="col-sm-7">
									                        <form:button name="submit" value="save" class="btn vd_btn vd_bg-green vd_white"><i class="icon-ok"></i>Save</form:button>
									                        <form:button name="submit" value="reset" class="btn vd_btn cancel"><i class="icon-ok"></i>Reset</form:button>
									                        </div>
									                    </div>
									                </form:form>
												</div>
											</div>
											<!-- Panel Widget -->
										</div>
										<!-- col-md-12 -->
										
										<div class="col-md-12">
											<div class="panel widget">
												<div class="panel-heading vd_bg-grey">
													<h3 class="panel-title">
														<span class="menu-icon"> <i
															class="fa fa-dot-circle-o"></i>
														</span>Tax Form Privacy Policy
													</h3>
												</div>
												<div class="panel-body  table-responsive" id="tab_taxformpolicy"  style="display:none">
												 <form:form 
									                commandName="taxFormPrivacyPolicyForm" 
									                method="POST" 
									                action="${pageContext.request.contextPath}/web/privacypolicy/taxform.htm" 
									                class="form-horizontal" 
									                role="form" 
									                enctype="multipart/form-data"
									                >
														<div class="form-group parent_category_breadcrumb">
															<label class="col-sm-4 control-label">Privacy Policy</label>
															<div class="col-sm-7 controls">
																<form:textarea path="PrivacyPolicy" class="ckeditor"/>
															</div>
														</div>
														
														<div class="form-group form-actions">
									                        <div class="col-sm-4"> </div>
									                        <div class="col-sm-7">
									                        <form:button name="submit" value="save" class="btn vd_btn vd_bg-green vd_white"><i class="icon-ok"></i>Save</form:button>
									                        <form:button name="submit" value="reset" class="btn vd_btn cancel"><i class="icon-ok"></i>Reset</form:button>
									                        </div>
									                    </div>
									                </form:form>
												</div>
											</div>
											<!-- Panel Widget -->
										</div>
										<!-- col-md-12 -->
										
										<div class="col-md-12">
											<div class="panel widget">
												<div class="panel-heading vd_bg-grey">
													<h3 class="panel-title">
														<span class="menu-icon"> <i
															class="fa fa-dot-circle-o"></i>
														</span>Tax Form Terms and Conditions
													</h3>
												</div>
												<div class="panel-body  table-responsive" id="tab_taxformterms"  style="display:none">
												 <form:form 
									                commandName="taxFormTerms" 
									                method="POST" 
									                action="${pageContext.request.contextPath}/web/termsncondition/taxform.htm" 
									                class="form-horizontal" 
									                role="form" 
									                enctype="multipart/form-data"
									                >
														<div class="form-group parent_category_breadcrumb">
															<label class="col-sm-4 control-label">Privacy Policy</label>
															<div class="col-sm-7 controls">
																<form:textarea path="PrivacyPolicy" class="ckeditor"/>
															</div>
														</div>
														
														<div class="form-group form-actions">
									                        <div class="col-sm-4"> </div>
									                        <div class="col-sm-7">
									                        <form:button name="submit" value="save" class="btn vd_btn vd_bg-green vd_white"><i class="icon-ok"></i>Save</form:button>
									                        <form:button name="submit" value="reset" class="btn vd_btn cancel"><i class="icon-ok"></i>Reset</form:button>
									                        </div>
									                    </div>
									                </form:form>
												</div>
											</div>
											<!-- Panel Widget -->
										</div>
										<!-- col-md-12 -->
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
<script type="text/javascript">
jQuery(document).ready(function(){
	jQuery(".panel-heading").click(function(){
		console.log(jQuery(this).closest(".panel-body"));
		jQuery(this).parent().find(".panel-body").toggle(500);
	});
	jQuery("#tab_${tab}").toggle(500);
});
</script>
</html>