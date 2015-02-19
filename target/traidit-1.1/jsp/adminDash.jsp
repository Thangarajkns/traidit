<%@ include file="/jsp/common/common.jsp"%>
<html>
<head>
<title>Admin Dashboard</title>
<%@ include file="/jsp/common/commonStyles.jsp"%>
<style type="text/css">
.vd_menu{
	margin-top: 75px;
}
</style>
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
								<h1>Dashboard</h1>
								<small class="subtitle">Default dashboard for
									multipurpose demonstration</small>

								<!-- vd_panel-menu -->
							</div>
							<!-- vd_panel-header -->
						</div>
						<!-- vd_title-section -->

						<div class="vd_content-section clearfix">
							<div class="row">

								<div class="col-md-6">
									<div id="RevenueGraph" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
								</div>
								<div class="col-md-6">
									<div id="userStatisticsGraphs" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-1"></div>
								<div class="col-md-5">
									<table class="table table-bordered">
										<tr>
											<td colspan="2" style="text-align:center">Revenue Statistics</td>
										</tr>
										<tr>
											<td>Revenue this month</td>
											<td>${revenueThisMonth }</td>
										</tr>
										<tr>
											<td>Revenue this year</td>
											<td>${revenueThisYear }</td>
										</tr>
										<tr>
											<td>Revenue same month last year to date</td>
											<td>${revenueSameMonthLastYearToDate }</td>
										</tr>
										<tr>
											<td>Revenue last year to date</td>
											<td>${revenueLastYearToDate }</td>
										</tr>
										<tr>
											<td># of subscribers to pay commissions</td>
											<td>${subscribersToPayCommission }</td>
										</tr>
										<tr>
											<td># of subscription payments to process</td>
											<td>${subscribersPaymentToProcess }</td>
										</tr>
									</table>
								</div><div class="col-md-1"></div>
								<div class="col-md-5">
									<table class="table table-bordered">
										<tr>
											<td colspan="2" style="text-align:center">User Statistics</td>
										</tr>
										<c:forEach items="${tradersCount }" var="user" >
											<tr>
												<td>${user.key }</td>
												<td>${user.value } </td>
											</tr>
										</c:forEach>
										<tr>
											<td>New subscribers this month</td>
											<td>${newSubscribersThisMonth }</td>
										</tr>
									</table>
								</div>
								<!--col-md-7 -->
								<!-- .col-md-5 -->
							</div>
							<div class="row">
						
	
								<div id="drillDownBar" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
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
	<script type="text/javascript">
	
	var test = new Object();
	<c:forEach items="${ userscount}" var="userCount">
	<c:if test="${ userCount[1] != null}">
	<c:set var="year"><fmt:formatDate value="${userCount[1] }" pattern="yyyy" /></c:set>
	<c:set var="month"><fmt:formatDate value="${userCount[1] }" pattern="MM" /></c:set>
	<c:set var="date"><fmt:formatDate value="${userCount[1] }" pattern="dd" /></c:set>
		populateData(${year},${month},${date},${userCount[0]},${userCount[2]});
	</c:if>
	</c:forEach>

	function populateData(year,month,date,planId,count){
		if(test[planId] == undefined)
			test[planId] = new Object();
		if(test[planId][year] == undefined)
			test[planId][year] = new Object();
		if(test[planId][year][month] == undefined)
			test[planId][year][month] = new Object();
		test[planId][year][month][date] = count;
	}
	$(function () {

		$('#RevenueGraph').highcharts({
	        chart: {
	            type: 'column'
	        },
	        title: {
	            text: 'Revenue Statistics'
	        },
	        yAxis: {
	            title: {
	                text: 'Revenue Earned'
	            }
	        },
	        xAxis: {
	            categories: ['this month', ' this year', 'same month last year to date','last year to date']
	        },
	        series: [{
	            name: 'Revenue',
	            data: [${revenueThisMonth }, ${revenueThisYear },${revenueSameMonthLastYearToDate },${revenueLastYearToDate }]
	        }]
	    });
		
		$('#userStatisticsGraphs').highcharts({
	        chart: {
	            type: 'column'
	        },
	        title: {
	            text: 'User Statistics'
	        },
	        yAxis: {
	            title: {
	                text: 'No Of Users'
	            }
	        },
	        xAxis: {
	            categories: ['New subscribers'<c:forEach items="${tradersCount }" var="user" >,'${user.key}'</c:forEach>]
	        },
	        series: [{
	            name: 'Users',
	            data: [${newSubscribersThisMonth }<c:forEach items="${tradersCount }" var="user" >,${user.value}</c:forEach>]
	        }]
	    });
	});
	
	 series = [];
	 years = [];
	 plans = [];
	 planusercount = [];
	 planusercountmonth = [];
	 categories = [];
	 drilldown = [];
for(plankey in test){
	 plans[plankey] = plankey;
	for(yearkey in test[plankey]){
		years[yearkey] = yearkey;
	}
}
for(yearkey in years){
	categories.push(yearkey);
	planusercount[yearkey] = new Object();
	planusercountmonth[yearkey] = new Object();
	for(plankey in plans){
		yearcount=0;
		for(monthkey in test[plankey][yearkey]){
			
			if(planusercountmonth[yearkey][monthkey] == undefined)
				planusercountmonth[yearkey][monthkey] = new Object();
			if(planusercountmonth[yearkey][monthkey][plankey] == undefined)
				planusercountmonth[yearkey][monthkey][plankey] = new Object();
			
			monthcount = 0;
			for(daykey in test[plankey][yearkey][monthkey]){
				monthcount += test[plankey][yearkey][monthkey][daykey];
			}
			yearcount += monthcount;
			planusercountmonth[yearkey][monthkey][plankey] = monthcount;
		}
		planusercount[yearkey][plankey] = yearcount;
	}
}
for(plankey in plans){
	 values = [];
	 for(yearkey in years){
		 values.push({
			 name: yearkey+' plan'+plankey,
             y: planusercount[yearkey][plankey],
             drilldown: 'drilldown_'+yearkey});
	 }
	series.push({
	    name: 'Plan'+plankey,
	    data:values
	});
}

for(yearkey in years){
	values = [];
	for(plankey in plans){
		 values = [];
		 for(monthkey = 1;monthkey <= 12 ; monthkey++){
			 if(planusercountmonth[yearkey][monthkey] == undefined || planusercountmonth[yearkey][monthkey][plankey] == undefined)
				 continue;
			 values.push({
				 name: monthkey,
	             y: planusercountmonth[yearkey][monthkey][plankey]
				});
		 }
	}
	 drilldown.push({
		    id: "drilldown_"+yearkey,
		    data:values
		});
}


	var drillDownBarchart = $('#drillDownBar').highcharts({
        chart: {
            type: 'column'
        },
        yAxis: {
            title: {
                text: 'Users'
            }
        },
        xAxis: {
            categories: categories
        },
        title: {
            text: 'User Resgistration Statistics'
        },
        series: series,/*
        drilldown: {series:drilldown, xAxis: {
            categories: ["1","2",""]
        }}*/
    });

	</script>
</body>
</html>