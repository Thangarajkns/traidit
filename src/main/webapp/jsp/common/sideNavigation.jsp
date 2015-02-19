
    <div class="vd_navbar vd_nav-width vd_navbar-tabs-menu vd_navbar-left  ">
	
	<div class="navbar-menu clearfix">
    	<h3 class="menu-title hide-nav-medium hide-nav-small"></h3>
        <div class="vd_menu">
        	 <ul>
    <li>
    	<a href="${pageContext.request.contextPath}/" >
        	<span class="menu-icon"><i class="fa fa-dashboard"></i></span> 
            <span class="menu-text">Dashboard</span>  
            <span class="menu-badge"><span class="badge vd_bg-yellow"><i class="fa fa-star"></i></span></span>
       	</a>
     	
    </li>  
 	<%-- <li>
    	<a href="${pageContext.request.contextPath}/web/user/listUsers.htm">
        	<span class="menu-icon"><i class="fa fa-signal"></i></span> 
            <span class="menu-text">User Management</span>  
            
       	</a> 
    </li>   --%> 
    <li>
    	<a data-action="click-trigger" href="javascript:void(0);" class="">
        	<span class="menu-icon"><i class="fa fa-sitemap"></i></span> 
            <span class="menu-text">User Management</span>  
            <span class="menu-badge"><span class="badge vd_bg-black-30"><i class="fa fa-angle-down"></i></span></span>
       	</a>
     	<div data-action="click-target" class="child-menu" style="display: none;">
            <ul>
                <li>
                    <a href="${pageContext.request.contextPath}/web/user/listUsers.htm">
                        <span class="menu-text">User List</span>  
                    </a>
                </li>              
                <li>
                    <a href="${pageContext.request.contextPath}/web/depositslist.htm">
                        <span class="menu-text">User Deposits</span>  
                    </a>
                </li>                
                <li>
                    <a href="${pageContext.request.contextPath}/web/taxeslist.htm">
                        <span class="menu-text">User Taxes</span>  
                    </a>
                </li>                  
                <li>
                    <a href="${pageContext.request.contextPath}/web/user/listusernotifications.htm">
                        <span class="menu-text">User Notifications</span>  
                    </a>
                </li>            
                <li>
                    <a href="${pageContext.request.contextPath}/web/privacypolicies.htm">
                        <span class="menu-text">Privacy Policies</span>  
                    </a>
                </li>                                                                             
            </ul>   
      	</div>
    </li>  
    <li>
    	<a data-action="click-trigger" href="javascript:void(0);" class="">
        	<span class="menu-icon"><i class="fa fa-sitemap"></i></span> 
            <span class="menu-text">Categories</span>  
            <span class="menu-badge"><span class="badge vd_bg-black-30"><i class="fa fa-angle-down"></i></span></span>
       	</a>
     	<div data-action="click-target" class="child-menu" style="display: none;">
            <ul>
                <li>
                    <a href="${pageContext.request.contextPath}/web/categories/list.htm">
                        <span class="menu-text">List Categories</span>  
                    </a>
                </li>              
                <li>
                    <a href="${pageContext.request.contextPath}/web/categories/search.htm">
                        <span class="menu-text">Search Categories</span>  
                    </a>
                </li>                
                <li>
                    <a href="${pageContext.request.contextPath}/web/categories/gethomepagecategories.htm">
                        <span class="menu-text">Home Page Categories</span>  
                    </a>
                </li>                                                                             
            </ul>   
      	</div>
    </li>
	  <li>
    	<a href="${pageContext.request.contextPath}/web/inventory/list.htm">
        	<span class="menu-icon"><i class="fa fa-sitemap"></i></span> 
            <span class="menu-text">Inventory</span>  
            
       	</a> 
    </li>  
     
    <li>
    	<a href="${pageContext.request.contextPath}/web/commission/home.htm">
        	<span class="menu-icon"><i class="fa fa-th-list"></i></span> 
            <span class="menu-text">Commissions</span>  
            
       	</a>
    </li>   
    <li>
    	<a data-action="click-trigger" href="javascript:void(0);" class="">
        	<span class="menu-icon"><i class="fa fa-sitemap"></i></span> 
            <span class="menu-text">Report</span>  
            <span class="menu-badge"><span class="badge vd_bg-black-30"><i class="fa fa-angle-down"></i></span></span>
       	</a>
     	<div data-action="click-target" class="child-menu" style="display: none;">
            <ul>
                <li>
                    <a href="${pageContext.request.contextPath}/reports/getpayments.htm">
                        <span class="menu-text">Reports 1</span>  
                    </a>
                </li>        
                <li>
                    <a href="${pageContext.request.contextPath}/web/reports/listpayment.htm">
                        <span class="menu-text">Payment Report</span>  
                    </a>
                </li>             
                <li>
                    <a href="${pageContext.request.contextPath}/web/reports/listthismonthcommission.htm">
                        <span class="menu-text">Commission Due this month</span>  
                    </a>
                </li>               
                <li>
                    <a href="${pageContext.request.contextPath}/web/reports/commissionsyeartodate.htm">
                        <span class="menu-text">Commissions Year to Date</span>  
                    </a>
                </li>                                                                           
            </ul>   
      	</div>
    </li>   
            
</ul>
<!-- Head menu search form ends -->         </div>             
    </div>
    <div class="navbar-spacing clearfix">
    </div>  
</div>        
    