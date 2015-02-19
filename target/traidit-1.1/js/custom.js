// JavaScript Document
jQuery(document).ready(function($){
	//Put Your Custom Jquery or Javascript Code Here
	
});

function confirmCategoryDelete(url){
	console.log("asdf"); 
	var r = confirm("you are about to delete a category. do you want to continue");
	if (r == true) {
		jQuery.ajax({
			url:url,
			type:"GET",
			success:function(data){
				if(data=="success"){
					alert("Category has been deleted successfully! please wait until page reloads..");
					jQuery("#categoryListFormBean").submit();
				}
			}
		});
	} else {
	    return false;
	}
}