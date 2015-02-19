
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<title>TRAIDIT - FORGET-PASSWORD</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta charset="utf-8">
<meta name="keywords" content="TRAIDIT,Login Pages">
<meta name="description" content="TRAIDIT- Forget Password Pages">
<meta name="author" content="TRAIDIT ">

<!-- Set the viewport width to device width for mobile -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">


<!-- Fav and touch icons -->
<link rel="apple-touch-icon-precomposed" sizes="144x144"
	href="${pageContext.request.contextPath}/img/ico/apple-touch-icon-144-precomposed.html">
<link rel="apple-touch-icon-precomposed" sizes="114x114"
	href="${pageContext.request.contextPath}/img/ico/apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72"
	href="${pageContext.request.contextPath}/img/ico/apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed"
	href="${pageContext.request.contextPath}/img/ico/apple-touch-icon-57-precomposed.png">
<link rel="shortcut icon"
	href="${pageContext.request.contextPath}/img/ico/favicon.png">


<!-- CSS -->


<link href="${pageContext.request.contextPath}/css/bootstrap.min.css"
	rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/font-entypo.css"
	rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/fonts.css"
	rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/theme.min.css"
	rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/traidit.css"
	rel="stylesheet" type="text/chrome">
<link
	href="${pageContext.request.contextPath}/css/theme-responsive.min.css"
	rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/custom.css"
	rel="stylesheet" type="text/css">


</head>

<body id="pages"
	class="full-layout no-nav-left no-nav-right  nav-top-fixed background-login     responsive remove-navbar login-layout   clearfix"
	data-active="pages " data-smooth-scrolling="1">
	<div class="vd_body">
		<!-- Header Start -->

		<!-- Header Ends -->
		<div class="content">
			<div class="container">





				<!-- Middle Content Start -->

				<div class="vd_content-wrapper">
					<div class="vd_container">

						<div class="vd_content clearfix">
							<div class="vd_content-section clearfix">

								<div class="vd_login-page">
									<div class="heading clearfix">
										<div class="logo">
											<h2 class="mgbt-xs-5">
												<img src="${pageContext.request.contextPath}/img/logo.png"
													alt="logo">
											</h2>
										</div>
										<h4 class="text-center font-semibold vd_grey">RESET
											PASSWORD FORM</h4>
									</div>

									<div class="panel widget">
										<div class="panel-body">

											<div class="login-icon">
												<i class="fa fa-lock"></i>
											</div>
											<div id="password-success"
												class="alert alert-success vd_hidden">
												<i class="fa fa-exclamation-circle fa-fw"></i> Your reset
												password form has been sent to your email
											</div>
																
											<c:if test="${not empty errorMessage}">
													<div class="alert alert-danger ">
									                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true"><i class="icon-cross"></i></button>
									                    <span class="vd_alert-icon"><i class="fa fa-exclamation-circle vd_red"></i></span> 
									                   ${errorMessage}
								                   </div>
								                   <c:remove var = "SPRING_SECURITY_LAST_EXCEPTION" scope = "session" />
											</c:if>
											<form class="form-horizontal" role="form" method="post"
												id="forget-password-form"
												action="<c:url value='/web/resetpassword.htm' />"
												novalidate="novalidate">
												<input type="hidden" name="userId" value="${userId }">
												<div class="alert alert-danger vd_hidden">
													<button type="button" class="close" data-dismiss="alert"
														aria-hidden="true">
														<i class="icon-cross"></i>
													</button>
													<span class="vd_alert-icon"><i
														class="fa fa-exclamation-circle vd_red"></i></span><strong>Oh
														snap!</strong> Change a few things up and try submitting again.
												</div>
												<div class="alert alert-success vd_hidden">
													<button type="button" class="close" data-dismiss="alert"
														aria-hidden="true">
														<i class="icon-cross"></i>
													</button>
													<span class="vd_alert-icon"><i
														class="fa fa-check-circle vd_green"></i></span>Your reset
													password form has been sent to your email.
												</div>
												<div class="form-group mgbt-xs-20">
													<div class="col-md-12">
														<p class="text-center">
															<strong>Please Enter your new password below.</strong>
														</p>

														<div class="vd_input-wrapper" id="email-input-wrapper">
															<span class="menu-icon"> <i class="fa fa-envelope"></i>
															</span> <input type="password" placeholder="New Password"
																id="password" name="password" class="required">
														</div>

													</div>
												</div>
												<div class="form-group mgbt-xs-20">
													<div class="col-md-12">
														<div class="vd_input-wrapper" id="email-input-wrapper">
															<span class="menu-icon"> <i class="fa fa-envelope"></i>
															</span> <input type="password"
																placeholder="Confirm New Password" id="cpassword"
																name="cpassword" class="required">
														</div>

													</div>
												</div>


												<div class="form-group" id="submit-password-wrapper">
													<div class="col-md-12 text-center mgbt-xs-5">
														<button class="btn vd_bg-green vd_white width-100" type="submit" id="submit-password" name="submit-password">Reset password</button>
													</div>

												</div>
											</form>


										</div>
									</div>
									<!-- Panel Widget -->
									<div class="register-panel text-center font-semibold">
										<a href="${pageContext.request.contextPath}/web/login.htm">LOGIN</a>
										<span class="mgr-10 mgl-10">|</span> <a
											href="${pageContext.request.contextPath}/pages-register.html">CREATE
											AN ACCOUNT</a>
									</div>
								</div>
								<!-- vd_login-page -->



							</div>
							<!-- .vd_content-section -->

						</div>
						<!-- .vd_content -->
					</div>
					<!-- .vd_container -->
				</div>
				<!-- .vd_content-wrapper -->

				<!-- Middle Content End -->

			</div>
			<!-- .container -->
		</div>
		<!-- .content -->



		<!-- Footer Start -->
		<footer class="footer-2" id="footer">
			<div class="vd_bottom ">
				<div class="container">
					<div class="row">
						<div class=" col-xs-12">
							<div class="copyright text-center">
								Copyright ©2014 TRAIDIT. All Rights Reserved <br> Design
								&amp; Developed by <a href="http://knstek.com/" target="_blank">KNS
									TECHNOLOGIES</a>
							</div>
						</div>
					</div>
					<!-- row -->
				</div>
				<!-- container -->
			</div>
		</footer>
		<!-- Footer END -->

	</div>


	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/jquery.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/plugins.js"></script>
	<!-- Specific Page Scripts Put Here -->
	<script type="text/javascript">
	</script>


	<!-- Specific Page Scripts END -->



</body>
</html>