<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>
	<link rel="stylesheet" href="./css/style.css">	

<title>
	Business Card Data
</title>

	<script type="text/javascript">
		$(document).ready(function() {
			
			$("tr").each(function () {
	            $(this).removeClass("striped");
	        });
			
	        $("tr:even").each(function () {
	            $(this).addClass("striped");
	        });
			
		});
	
	</script>
	
</head>
<body>

		<div id="header">
		
			<img src="./img/logo.png" class="logo"/>
				<div class="logo_text">
					Business Card Scanned Data
				</div>
		</div>
		
		<div id="content">
			<table class="datatable" border="1" cellspacing="0" cellpadding="0">
			<tr class="head">
				<th>NAME</th>
				<th>COMPANY NAME</th>
				<th>JOB</th>
				<th>ADDRESS</th>
				<th>PHONE</th>
				<th>MOBILE</th>
				<th>EMAIL</th>
			</tr>
			<c:forEach var="card" items="${cards}">
				<tr class="content_data">
				<td> ${card.name}</td>
				<td> ${card.company}</td>
				<td> ${card.jobTitle}</td>
				<td> ${card.businessAddress}</td>
				<td> ${card.businessPhone}</td>
				<td> ${card.mobile}</td>
				<td> ${card.email}</td>
				
			</tr>
			
			
			</c:forEach>
			
			
			
			</table>
			<div class="bottom_img"><img src="./img/table_shadow.png"/></div>
	</div>
</body>
</html>