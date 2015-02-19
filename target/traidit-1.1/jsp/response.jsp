<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="">
  <title>${title }</title>

</head>

<body class="signin">

	<!-- Preloader -->
	<div id="preloader">
	    <div id="status"><i class="fa fa-spinner fa-spin"></i></div>
	</div>

	<section>  
	    <div class="signinpanel">
			<div class="row">           
		            
		            <!-- col-sm-7 -->
		               <div class="col-md-4">
					   </div>
			            <div class="col-md-5">
			                <div class="logopanel">
			                        <h1 style="text-align:center;"><img src="images/logo.png"></h1>
			                 </div>
			                <form method="post" action="<c:url value='${pageContext.request.contextPath}/forgetpassword.htm' />" >
			                    	<div class="title">
			                    			${message}
			                    			
			                    			<br />
			                    			<br />
			                    		<c:choose>
			                    			<c:when test="${ empty requestScope.user}">
			                    				<a href="${pageContext.request.contextPath}/web/login.htm" style="text-decoration: underline;color:#1962CF;">	Click here to Login </a>
			                    			</c:when>	
			                    			<c:otherwise>
			                    				<a href="${pageContext.request.contextPath}/web/home.htm" style="text-decoration: underline;color:#1962CF;">	Click here for Home </a>
			                    			</c:otherwise>
			                    		</c:choose>
			                    		
			                    	</div>
			                    	
			                </form>
			            </div>
						<div class="col-md-3">
						 </div>
		            <!-- col-sm-5 -->            
		        </div>        
	       	 <!-- row -->        
		        <div class="signup-footer">
		            <div class="pull-left">
		                CopyRight &copy; 2014. All Rights Reserved.
		            </div>
		            <div class="pull-right">
		                Created By: <a href="http://knstek.com/" target="_blank">KNS TECHNOLOGIES</a>
		            </div>
		        </div>        
	    	</div><!-- signin -->	  
	</section>



</body>
</html>
