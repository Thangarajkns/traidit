 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
 <!DOCTYPE html>
<html>
	<head>
		<title>TRAIDIT-  Login Pages</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<meta name="keywords" content="TRAIDIT">
		<meta name="description" content="TRAIDIT-  Login Pages ">
		<meta name="author" content="TRAIDIT">
		
		<!-- Set the viewport width to device width for mobile -->
		<meta name="viewport" content="width=device-width, initial-scale=1.0">    
		
		
		<!-- Fav and touch icons -->
		<link rel="apple-touch-icon-precomposed" sizes="144x144" href="${pageContext.request.contextPath}/img/ico/apple-touch-icon-144-precomposed.png">
		<link rel="apple-touch-icon-precomposed" sizes="114x114" href="${pageContext.request.contextPath}/img/ico/apple-touch-icon-114-precomposed.png">
		<link rel="apple-touch-icon-precomposed" sizes="72x72" href="${pageContext.request.contextPath}/img/ico/apple-touch-icon-72-precomposed.png">
		<link rel="apple-touch-icon-precomposed" href="${pageContext.request.contextPath}/img/ico/apple-touch-icon-57-precomposed.png">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/ico/favicon.png">
		
		
		<!-- CSS -->
		
		
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css">
		<link href="${pageContext.request.contextPath}/css/font-awesome.min.css" rel="stylesheet" type="text/css">  
		<link href="${pageContext.request.contextPath}/css/font-entypo.css" rel="stylesheet" type="text/css">    
		<link href="${pageContext.request.contextPath}/css/fonts.css" rel="stylesheet" type="text/css">      
		<link href="${pageContext.request.contextPath}/css/theme.min.css" rel="stylesheet" type="text/css">
		<link href="${pageContext.request.contextPath}/css/traidit.css" rel="stylesheet" type="text/chrome">
		<link href="${pageContext.request.contextPath}/css/theme-responsive.min.css" rel="stylesheet" type="text/css"> 
		<link href="${pageContext.request.contextPath}/css/custom.css" rel="stylesheet" type="text/css">
		
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
		<script type="text/javascript" src="js/html5shiv.js"></script>
		<script type="text/javascript" src="js/respond.min.js"></script>     
		<![endif]-->
	
	</head>    

	<body id="pages" class="full-layout no-nav-left no-nav-right  nav-top-fixed background-login     responsive remove-navbar login-layout   clearfix" 
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
			                  <h2 class="mgbt-xs-5"><img src="${pageContext.request.contextPath}/img/logo.png" alt="logo"></h2>
			                </div>
			                <h4 class="text-center font-semibold vd_grey">LOGIN TO YOUR ACCOUNT</h4>
			              </div>
			              <div class="panel widget">
			                <div class="panel-body">
			                  <div class="login-icon entypo-icon"> <i class="icon-key"></i> </div>
			                  <form id="login-form" class="form-horizontal" method="post" action="<c:url value='/j_spring_security_check' />" role="form" novalidate="novalidate" >
			                  <div class="alert alert-danger vd_hidden">
			                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true"><i class="icon-cross"></i></button>
			                    <span class="vd_alert-icon"><i class="fa fa-exclamation-circle vd_red"></i></span> Please <strong>fill up the fields</strong> correctly </div>
                    		 
						<c:if test="${sessionScope[\"SPRING_SECURITY_LAST_EXCEPTION\"].message eq 'Bad credentials'}">
								<div class="alert alert-danger ">
				                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true"><i class="icon-cross"></i></button>
				                    <span class="vd_alert-icon"><i class="fa fa-exclamation-circle vd_red"></i></span> 
				                   Username/Password of admin account is incorrect.
			                   </div>
			                   <c:remove var = "SPRING_SECURITY_LAST_EXCEPTION" scope = "session" />
						</c:if> 
						<c:if test="${not empty errorMessage}">
								<div class="alert alert-danger">
				                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true"><i class="icon-cross"></i></button>
				                    <span class="vd_alert-icon"><i class="fa fa-exclamation-circle vd_red"></i></span> 
				                   ${errorMessage}
			                   </div>
						</c:if> 
						<c:if test="${not empty sucessMessage}">
			                  <div class="alert alert-success">
			                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true"><i class="icon-cross"></i></button>
			                    <span class="vd_alert-icon"><i class="fa fa-check-circle vd_green"></i></span><strong>${sucessMessage}</strong>. 
			                    </div> 
						</c:if>
			                  <div class="alert alert-success vd_hidden">
			                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true"><i class="icon-cross"></i></button>
			                    <span class="vd_alert-icon"><i class="fa fa-check-circle vd_green"></i></span><strong>Well done!</strong>. 
			                    </div>                  
			                    <div class="form-group  mgbt-xs-20">
			                      <div class="col-md-12">
			                        <div class="label-wrapper sr-only">
			                          <label class="control-label" for="email">Email</label>
			                        </div>
			                        <div class="vd_input-wrapper" id="email-input-wrapper"> <span class="menu-icon"> <i class="fa fa-envelope"></i> </span>
			                        	<!-- <input type="text" class="form-control uname" placeholder="Username" name="j_username" id="uname"/> -->
			                          <input id="text" class="required" type="text" placeholder="Username"  name="j_username"  required="">
			                        </div>
			                        <div class="label-wrapper">
			                          <label class="control-label sr-only" for="password">Password</label>
			                        </div>
			                        <div class="vd_input-wrapper" id="password-input-wrapper"> <span class="menu-icon"> <i class="fa fa-lock"></i> </span>
			                        	<!-- <input type="password" class="form-control pword" placeholder="Password" name="j_password" id="pwd"/> -->
			                          <input type="password" placeholder="Password" id="password" name="j_password" class="required" required="">
			                        </div>
			                      </div>
			                    </div>
			                    <div id="vd_login-error" class="alert alert-danger hidden"><i class="fa fa-exclamation-circle fa-fw"></i> Please fill the necessary field </div>
			                    
			                   
			                    <div class="form-group">
			                      <div class="col-md-12 text-center mgbt-xs-5">
			                      	<!-- <button class="btn btn-success btn-block">Sign In</button> -->
			                        <button class="btn vd_bg-green vd_white width-100" type="submit" id="login-submit">Sign In</button>
			                      </div>
			                      
	                          <div class="form-group">
									<div class="col-sm-7 controls" style="margin-left: 15px;">
									  <div class="vd_checkbox checkbox-danger">
									  <input type="checkbox" value="true" id="_spring_security_remember_me" name="_spring_security_remember_me"  >
										<label for="_spring_security_remember_me">Remember Me</label>
									  </div>
									</div>
								</div>
								
			                      <div class="col-md-12">
			                        <div class="row text-center">
			                            <div class=""> <a href="${pageContext.request.contextPath}/web/forgotpassword.htm">Forgot Password? </a> </div>
			                        </div>
			                      </div>
			                    </div>
			                  </form>
			                </div>
			              </div>
			              <!-- Panel Widget -->
			              <div class="register-panel text-center font-semibold" style="display:none;"> <a href="${pageContext.request.contextPath}/pages-register.html">CREATE AN ACCOUNT<span class="menu-icon"><i class="fa fa-angle-double-right fa-fw"></i></span></a> </div>
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
			                	Copyright ©2014 TRAIDIT. All Rights Reserved  <br> Design &amp; Developed by <a href="http://knstek.com/" target="_blank">KNS TECHNOLOGIES PVT Ltd...</a>
			              </div>
			            </div>
			          </div><!-- row -->
			   </div><!-- container -->
			  </div>
			</footer>
<!-- Footer END -->

		</div>
	
	
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script> 
		<!--[if lt IE 9]>
		  <script type="text/javascript" src="js/excanvas.js"></script>      
		<![endif]-->
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/plugins.js"></script>
		<script type="text/javascript">
		$(document).ready(function() {
				var submittable = false;
		        var form_register_2 = $('#login-form');
		        var error_register_2 = $('.alert-danger', form_register_2);
		        var success_register_2 = $('.alert-success', form_register_2);
		
		        form_register_2.validate({
		            errorElement: 'div', //default input error message container
		            errorClass: 'vd_red', // default input error message class
		            focusInvalid: false, // do not focus the last invalid input
		            ignore: "",
		            rules: {
		                email: {
		                    required: true,
		                    //email: true
		                },				
		                password: {
		                    required: true,
							minlength: 6
		                },
						
		            },
					
					errorPlacement: function(error, element) {
						if (element.parent().hasClass("vd_checkbox") || element.parent().hasClass("vd_radio")){
							element.parent().append(error);
						} else if (element.parent().hasClass("vd_input-wrapper")){
							error.insertAfter(element.parent());
						}else {
							error.insertAfter(element);
						}
					}, 
					
		            invalidHandler: function (event, validator) { //display error alert on form submit              
		                success_register_2.hide();
		                error_register_2.show();
		
		
		            },
		
		            highlight: function (element) { // hightlight error inputs
				
						$(element).addClass('vd_bd-red');
						$(element).parent().siblings('.help-inline').removeClass('help-inline hidden');
						if ($(element).parent().hasClass("vd_checkbox") || $(element).parent().hasClass("vd_radio")) {
							$(element).siblings('.help-inline').removeClass('help-inline hidden');
						}
		
		            },
		
		            unhighlight: function (element) { // revert the change dony by hightlight
		                $(element)
		                    .closest('.control-group').removeClass('error'); // set error class to the control group
		            },
		
		            success: function (label, element) {debugger;
		                label
		                    .addClass('valid').addClass('help-inline hidden') // mark the current input as valid and display OK icon
		                	.closest('.control-group').removeClass('error').addClass('success'); // set success class to the control group
						$(element).removeClass('vd_bd-red');
		
							
		            } ,
		            /* 
		            submitHandler: function (form) {
						$(form).find('#login-submit').prepend('<i class="fa fa-spinner fa-spin mgr-10"></i>');					
		                success_register_2.show();
		                error_register_2.hide();
		                ;
						setTimeout(function(){window.location.href = "index.php"},2000)	 ; 				
						submittable = true;
		            }  */
		        });	
		});
		</script>
<!-- Specific Page Scripts END -->
<!-- begin olark code -->
<!--
<script data-cfasync="false" type='text/javascript'>/*<![CDATA[*/window.olark||(function(c){var f=window,d=document,l=f.location.protocol=="https:"?"https:":"http:",z=c.name,r="load";var nt=function(){
f[z]=function(){
(a.s=a.s||[]).push(arguments)};var a=f[z]._={
},q=c.methods.length;while(q--){(function(n){f[z][n]=function(){
f[z]("call",n,arguments)}})(c.methods[q])}a.l=c.loader;a.i=nt;a.p={
0:+new Date};a.P=function(u){
a.p[u]=new Date-a.p[0]};function s(){
a.P(r);f[z](r)}f.addEventListener?f.addEventListener(r,s,false):f.attachEvent("on"+r,s);var ld=function(){function p(hd){
hd="head";return["<",hd,"></",hd,"><",i,' onl' + 'oad="var d=',g,";d.getElementsByTagName('head')[0].",j,"(d.",h,"('script')).",k,"='",l,"//",a.l,"'",'"',"></",i,">"].join("")}var i="body",m=d[i];if(!m){
return setTimeout(ld,100)}a.P(1);var j="appendChild",h="createElement",k="src",n=d[h]("div"),v=n[j](d[h](z)),b=d[h]("iframe"),g="document",e="domain",o;n.style.display="none";m.insertBefore(n,m.firstChild).id=z;b.frameBorder="0";b.id=z+"-loader";if(/MSIE[ ]+6/.test(navigator.userAgent)){
b.src="javascript:false"}b.allowTransparency="true";v[j](b);try{
b.contentWindow[g].open()}catch(w){
c[e]=d[e];o="javascript:var d="+g+".open();d.domain='"+d.domain+"';";b[k]=o+"void(0);"}try{
var t=b.contentWindow[g];t.write(p());t.close()}catch(x){
b[k]=o+'d.write("'+p().replace(/"/g,String.fromCharCode(92)+'"')+'");d.close();'}a.P(2)};ld()};nt()})({
loader: "static.olark.com/jsclient/loader0.js",name:"olark",methods:["configure","extend","declare","identify"]});
/* custom configuration goes here (www.olark.com/documentation) */
olark.identify('2846-819-10-6044');/*]]>*/</script><noscript><a href="https://www.olark.com/site/2846-819-10-6044/contact" title="Contact us" target="_blank">Questions? Feedback?</a> powered by <a href="http://www.olark.com?welcome" title="Olark live chat software">Olark live chat software</a></noscript>
 --><!-- end olark code -->
	</body>
</html>
 
 
 
 
 
 
 
 