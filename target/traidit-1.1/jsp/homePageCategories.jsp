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
						<div class="vd_content clearfix"  ng-app="changeExample" ng-controller="customersController">
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
							<div class="vd_title-section clearfix">
								<div class="vd_panel-header">
									<h1>Home page Categories</h1>
									<small class="subtitle"></small>
									<div class="vd_panel-menu">
										<a href="javascript:void(0)" class="btn vd_btn vd_bg-green vd_white" ng-click="save()">save</a>
										<a href="${pageContext.request.contextPath }/web/user/listUsers.htm" class="btn vd_btn vd_bg-green vd_white">Back</a>
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
													</span> Home page Categories
												</h3>
											</div>
											<div class="panel-body  table-responsive" id="switch-input">
												<table class="table table-bordered" >
													<thead>
														<tr>
															<th>Category Name</th>
															<th>Category Icon</th>
															<th>Sequence</th>
														</tr>
													</thead>
													<tbody>
														<tr ng-repeat="x in categories | orderBy:'Sequence':false">
															<td>{{ x.categoryName }}</td>
															<td><img src="/uploaded_images/categoryimages/{{ x.CategoryId }}/{{ x.CategoryIcon }}"
																				height="100" width="100" /></td>
															<td>
																<select 
																	ng-init="Sequence=x.Sequence"
																	ng-change="change({{x}},this)" 
																	ng-model="x.Sequence" 
																	ng-options="y.Index as y.Index for y in categories|orderBy:'Index':false">
																</select> 
															</td>
													  	</tr>
													</tbody>
												</table>
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

<script>
var jsonArray = new Array();
for(var i = 1; i<=10 ; i++){
	var obj = new Object();
	obj.Name = "name"+i;
	obj.City = "city"+i;
	obj.Country = "Country"+i;
	obj.Sequence = i;
	jsonArray.push(obj);
}
angular.module('changeExample', []).controller('customersController', ['$scope','$http',
	function ($scope,$http) {
		$http.get("/traidit/web/categories/gethomepagecategoriesjson.htm")
		.success(function(response) {$scope.categories = response;});
		$scope.change = function(ele,scope) {
			var selectedSequence = scope.x.Sequence;
			for(var temp in $scope.categories){
				if(selectedSequence <= $scope.categories[temp].Sequence && ele.Sequence>$scope.categories[temp].Sequence && selectedSequence < ele.Sequence ){
					$scope.categories[temp].Sequence += 1;
				}
				else if(selectedSequence >= $scope.categories[temp].Sequence && ele.Sequence<=$scope.categories[temp].Sequence && selectedSequence > ele.Sequence ){
					$scope.categories[temp].Sequence -= 1;
				}
			}
			scope.x.Sequence = selectedSequence;
		};
		$scope.save = function(){
			var data = {categories:$scope.categories};
			$http.post("/traidit/web/categories/savehomepagecategories.htm",data).success(function(){
				alert("Sequence Saved");	
			});
			console.log($scope.categories);
		} 
	}
]);
</script>
</body>
</html>