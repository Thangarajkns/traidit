
//var host = "http://54.225.230.190:7090";
var host = "http://localhost:8080";

var userId = 27;
var vendorId = 25;
var deviceToken = "737906d52584a254cd16ea8534d06f790ec5c2afbe531f49dc822fad9c9de141";//ipad mini token

var tradeId = 58;

function getinput(){
$.ajax({
  type: "POST",
  url: host+"/traidit/loginuser.htm",
  data: JSON.stringify( {"username":"aaa","password":"123",devicetoken:deviceToken}),
  contentType: 'application/json',
  success: function(data) {
	  alert(data);
  }
});
}

function getfavitem(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/addfavitem.htm",
	  data: JSON.stringify({ itemid: "2", userid: userId}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getfavitemtodelete(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/deletefavitem.htm",
	  data: JSON.stringify({ itemid: "1", userid :userId}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getfavvendor(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/addfavouritevendor.htm",
	  data: JSON.stringify({ userid: userId, vendorid: vendorId}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getfavvendortodelete(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/deletefavouritevendor.htm",
	  data: JSON.stringify({ vendorid: vendorId,userid : userId}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getfavcategory(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/addfavouritecategory.htm",
	  data: JSON.stringify({ userid: userId, categoryid: "107"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getfavcategorytodelete(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/deletefavouritecategory.htm",
	  data: JSON.stringify({ categoryid: "27", userid: userId}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getallfavitems(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/displayallfavitems.htm",
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}

function getitemtoadd(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/additem.htm",
	  data: JSON.stringify({ description: "6th and gfdfsdhh  sense cooling technology", itemname: "whirlpoolafdfffwf fridge", manufacturer: "whidrlpoolk", photos: "tis.jpg",
		  videos: "fridgev111.swr", categoryid: "8",localdbitem:"0",userid:userId,itemphotos:jQuery("#itemphoto")[0].files[0]}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getitemtodelete(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/deleteitem.htm",
	  data: JSON.stringify({ itemid: "2"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getaddcomment(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/addcomment.htm",
	  data: JSON.stringify({ message: "excellent product", rating: "2", userid: userId, itemid: "2"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getitemtodisplay(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/displayitemdetails.htm",
		data: JSON.stringify({ itemid: "64"}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
			}	
		
	});
}

function getallitems(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/displayallitems.htm",
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}

function getallitemsofcategory(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/displayitemsbycategory.htm",
		data: JSON.stringify({ categoryid: "3"}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}

function getallfavvendors(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/displayallfavvendors.htm",
		data: JSON.stringify({ userid: "1"}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}

function getallfavinventories(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/displayallfavinventoriesofuser.htm",
		data: JSON.stringify({ userid: userId,favvendor:false,favcategory:false,maxdistance:2395}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}
function getallfavcategories(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/displayallfavcategoriesofuser.htm",
		data: JSON.stringify({ userid: userId,favvendor:true,maxdistance:10}),
        contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}

function getusertodisplay(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/displayuserdetails.htm",
	  data: JSON.stringify({ userid: userId}),
	  contentType: 'application/json',
	  success: function(JSONArray){
			alert(JSONArray);
				
			}	
	});
}

function getreceivedmessages(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/displayreceivedmessages.htm",
		 data: JSON.stringify({ userid: userId}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);	
		}
	});
}

function getuserratings(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/displayuserratings.htm",
	  data: JSON.stringify({ userid: userId}),
	  contentType: 'application/json',
	  success: function(data){
			alert(data);
			}	
	});
}

function getitemratings(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/displayitemratings.htm",
	  data: JSON.stringify({ itemid: "2"}),
	  contentType: 'application/json',
	  success: function(data){
			alert(data);
		   
				
			}	
	});
}

function getusertoadd(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/registeruser.htm",
	  data: JSON.stringify({ username: "souj", email: "soujanya@knstek.com", password: "1234", firstname: "Polaris", lastname: "Baverin", city: "Los angeles", state: "california", country: "USA", displayname: "POLO", tradeemail: "soujanyav91@gmail.com", rating: "4", zip: "45321"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getlogininfo(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/getlogininfo.htm",
	  data: JSON.stringify({ userid: userId}),
	  contentType: 'application/json',
	  success: function(JSONArray){
			alert(JSONArray);
			}	
	});
}

function saveediteduser(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/saveediteduserprofile.htm",
	  data: JSON.stringify({userid: userId, firstname: "Polaris", lastname: "Baverin", city: "Los angeles", state: "california", country: "USA", displayname: "POLO", tradeemail: "polo1@gmail.com", rating: "4", zip: "45321"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getcontactinfotoedit(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/getusercontactprofiletoedit.htm",
	  data: JSON.stringify({ userid: userId}),
	  contentType: 'application/json',
	  success: function(JSONArray){
			alert(JSONArray);
			}	
	});
}

function updatecontactprofileoftrader(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/updatecontactprofileoftrader.htm",
	  data: JSON.stringify({userid: userId, tradeemail: "sssty@gmail.com", email: "sss@gmail.com", phone: "234567821", tradephone: "676666767", city: "austin", street: "23rd wall", state: "Texas", country: "USA", zip: "90008"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function gettradephone(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/gettradephonetoedit.htm",
	  data: JSON.stringify({ userid: userId}),
	  contentType: 'application/json',
	  success: function(JSONArray){
			alert(JSONArray);
			}	
	});
}

function saveeditedtradephone(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/gettradephonetosave.htm",
	  data: JSON.stringify({userid: userId, tradephone: "789675"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

//====================================================
//                   	INVENTORIES
//====================================================

function getinventorytoadd(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/addinventory.htm",
	  data: JSON.stringify({ availablefortrade: "true", unitsavailable: "65", userid: userId, upc: "67670", flagedited: "0", flagmanual: "0", description: "aaaaaaaa", details: "gdshfsh", itemname: "sony tv", categoryid: "1", subcategoryid: "21", manufacturer: "sony", photos:"hddsh", videos: "dfdf"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function geteditinventory(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/editinventory.htm",
		data: JSON.stringify({ inventoryid: "2"}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
			}	
		
	});
}

function getinventorytodelete(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/deleteinventory.htm",
	  data: JSON.stringify({ inventoryid: "1"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getfavinventory(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/addfavouriteinventory.htm",
	  data: JSON.stringify({ userid: userId, inventoryid: 7}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getfavinventorytodelete(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/deletefavouriteinventory.htm",
	  data: JSON.stringify({ inventoryid: "2" , userid: userId}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getinventorytoupdate(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/saveeditedinventory.htm",
	  data: JSON.stringify({inventoryid: "2", availableforsale: "false",price: "750", unitsavailable: "95"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getinventorytodisplay(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/displayinventorydetails.htm",
		data: JSON.stringify({ inventoryid: "2",categoryid:"27", userid: userId}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
			}	
		
	});
}

function sendinventorylistofcategory(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/displayinventorylistbycategory.htm",
		data: JSON.stringify({categoryid: "27", userid: userId,favvendor:false,maxdistance:0}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}

function changeinventoryavailability(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/changeinventoryavailability.htm",
	  data: JSON.stringify({inventoryid: "1", availableforsale: "false"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function sendinventorylistofuser(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/displayinventoriesofuser.htm",
		data: JSON.stringify({ userid: userId}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}


function sendentireinventorylistforuser(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/displayentireinventoriesforuser.htm",
		data: JSON.stringify({ userid: userId,favinventory:false,favcategory : true,maxdistance:4444,category:1}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}



function saveeditedinventory(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/saveeditedinventory.htm",
		 data: JSON.stringify({ 
			 availablefortrade: "true", 
			 unitsavailable: "65", 
			 userid: userId, 
			 upc: "67670", 
			 flagedited: "0", 
			 flagmanual: "0", 
			 description: "aaaaaaaa", 
			 details: "gdshfsh", 
			 itemname: "sony tv", 
			 categoryid: "1", 
			 subcategoryid: "21", 
			 manufacturer: "sony", 
			 photos:"hddsh", 
			 videos: "dfdf"
				 }),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);	
		}
	});
}




//====================================================
//                 	 	TRADE 
//====================================================

function makeoffer(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/makeoffer.htm",

	  

	  data: JSON.stringify({
		  senderinventoryids	: [{"id" :6,"quantity": "1"},{"id" :7,"quantity": "1"}],
		  receiverinventoryids	: [{"id":1, "quantity": "1"},{"id": 3,"quantity": "1"}], 
		  userid				: userId, 
		  receiverid			: vendorId,
		  message				: "001"}),


	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function makecounteroffer(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/makecounteroffer.htm",
	  data: JSON.stringify({ 
		  senderinventoryids	: [{"id":1, "quantity": "1"},{"id": 2,"quantity": "1"}], 
		  receiverinventoryids	: [{"id" :6,"quantity": "1"},{"id" :7,"quantity": "1"}], 
		  userid				: userId,
		  receiverid			: vendorId, 
		  tradeid				: tradeId,
		  message				:"002"}),

	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function acceptoffer(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/acceptoffer.htm",
	  data: JSON.stringify({
		  tradeid	: tradeId,
		  userid	: userId,
		  message	: "003"}),

	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function completetrade(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/completetrade.htm",
	  data: JSON.stringify({tradeid: "2",userid:"2",message:"005"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function rejectoffer(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/rejectoffer.htm",
	  data: JSON.stringify({
		  tradeid	: tradeId,
		  userid	: userId,
		  message	: "004"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function saveoffer(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/saveoffer.htm",
	  data: JSON.stringify({
		  senderinventoryids	: [{"id" :1,"quantity": "1"},{"id" :2,"quantity": "1"}],
		  receiverinventoryids	: [{"id":4, "quantity": "1"},{"id":6,"quantity": "1"}],
		  tradeid				: tradeId,
		  userid				: userId,
		  receiverid			: vendorId,
		  message				: ""}),

	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function sendtrademessages(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/sendtrademessages.htm",
	  data: JSON.stringify({tradeid: tradeId,senderid: userId, receiverid: vendorId, message: "need more info"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function gettradesbycriteria(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/gettradesbycriteria.htm",
		data: JSON.stringify({ status: "SAVED" , userid: userId}),
        contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}



function initializeregistration(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/initializeregistration.htm",
	  data: JSON.stringify({ username: "ddd", email: "ddd@gmail.com", password: "123", firstname: "Tom", lastname: "Bav", middlename: "Los"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function completeregistration(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/completeregistration.htm",
	  data: JSON.stringify({ userid: userId, city: "NewYork", state: "NY", country: "USA", displayname: "ddd", tradeemail: "ddd1@gmail.com", tradephone: "4787655", zip: "10001", phone: "32451",devicetoken:deviceToken}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function sendplans(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/sendplans.htm",
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}

function saveplan(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/handlepayment.htm",
	  data: JSON.stringify({ userid: userId, planid: "3"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function handleCardPayment(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/handleCardPayment.htm",
	  data: JSON.stringify({ userid: userId, planid: "4", cardnumber: "4032036166330785", expirymonth: "10", expiryyear: "2019", cardtype: "VISA",cvv2: "123"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function addreferral(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/addreferral.htm",
	  data: JSON.stringify({email: "aaa@gmail.com", phonenumber: "876543", firstname: "fero",lastname: "zero",userid: userId}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function socialregistration(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/handlesocialregistration.htm",
	  data: JSON.stringify({ email: "bujji@gmail.com", firstname: "Tommy", lastname: "Baverin", middlename: "Los"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function performitemsearch(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/performItemSearch.htm",
	  data: JSON.stringify({ keyword: "tv"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function listhomepagecategoires(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/viewhomepagecategories.htm",
		data: JSON.stringify({ userid : userId,favvendor:false,maxdistance:0}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}

function listsubcategories(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/viewsubcategories.htm",
		data: JSON.stringify({categoryid: "1", userid : userId, noemptycategories : false,favvendor:true,favcategory : true,maxdistance:0}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}

function listparentcategories(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/getparentcategories.htm",
		data: JSON.stringify({userid: userId , noemptycategories : "false",favvendor:false,favcategory : false }),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);	
		}
	});
}

function getforgotpassword(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/initiateforgotpassword.htm",
	  data: JSON.stringify({ email: "arunkumar@knstek.com"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function validatePasswordResetToken(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/validatePasswordResetToken.htm",
		data: JSON.stringify({passwordResetToken: "767978"}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}

function saveupdatedpassword(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/passwordresetbyuser.htm",
	  data: JSON.stringify({userId: userId, password: "123"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function testsuccess(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/testsuccess.htm",
	  
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getPaymentStatus(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/paymentStatus.htm",
	  data: JSON.stringify({userId: userId}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	})
}

function senditemlist(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/getupc.htm",
		data: JSON.stringify({upc: "8902519002211"}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}

function loginguest(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/loginguestuser.htm",
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	})
}

function getdistance(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/getdistance.htm",
	  data: JSON.stringify({userid1: userId, userid2: vendorId}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	})
}

function rebuilditemindex(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/rebuilditemindex.htm",
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function sendusermessage(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/sendusermessage.htm",
	  data: JSON.stringify( {"senderid":userId,"receiverid":vendorId, message: "hi how are you", subject: ""}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function getVendorDetails(){

	$.ajax({
	  type: "POST",
	  url: host+"/traidit/getvendordetails.htm",
	  data: JSON.stringify( {"userid":userId,"vendorid":vendorId}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
}

function ratevendor(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/ratevendor.htm",
	  data: JSON.stringify( {"vendorid":vendorId,"userid":userId, comment: "hi how are you?", rating: "Not Satisfied"}),
	  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});
	}

function getvendordetails(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/getvendordetails.htm",
		data: JSON.stringify({vendorid: vendorId, userid: userId}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);	
		}
	});
}

function sendratingslistofuser(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/getvendorratings.htm",
		data: JSON.stringify({ vendorid: vendorId, rating: "all"}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}

function getoffer(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/getoffer.htm",
		data: JSON.stringify({ userid: userId, tradeid: tradeId}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}


function getsavedoffer(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/getsavedoffer.htm",
		data: JSON.stringify({ userid: userId, tradeid: tradeId,level:1}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
} 

function calculatejiuce(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/jiucecalculator.htm",
		data: JSON.stringify({ "x" : "3"}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}


function gettradehistorylist(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/gettradehistorylist.htm",
		data: JSON.stringify({ "userid" : userId}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}

function gettradehistory(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/gettradehistory.htm",
		data: JSON.stringify({
			 "userid" : userId,
			 "tradeid" : tradeId}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}


/**
 * replaced with the method saveOffer
 */
/**
function maketradesavedtrade(){
	$.ajax({
	  type: "POST",
	  url: host+"/traidit/saveoffer.htm",
	  data: JSON.stringify({receiverinventoryids	: [{"id":"4", "quantity": "1"},{"id": "6","quantity": "1"}],
		  senderinventoryids: [{"id" :"2","quantity": "1"},{"id" :"7","quantity": "1"}],
		  tradeid			: "1",
		  userid			: "25",
		  senderid			: "25", 
		  receiverid		: "27",
		  message			: "do you agree with this one?"}),
		  contentType: 'application/json',
	  success: function(data) {
		  alert(data);
	  }
	});

}

*/
function getsummaryoftrader(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/displaytradersummary.htm",
		data: JSON.stringify({ userid: userId}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}

function addratingrestriction(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/addratingrestriction.htm",
		data: JSON.stringify({ userid: "1262", ratingrestriction: "Delighted"}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);
		}
	});
}

function displayusermessagesbyflag(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/listusermessages.htm",
		 data: JSON.stringify({ userid: userId, flag:"unread"}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);	
		}
	});
}

function displayusermessagescountbyflag(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/getusermessagescount.htm",
		 data: JSON.stringify({ userid: userId, flag:"unread"}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);	
		}
	});
}

function deleteusermessage(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/deleteusermessage.htm",
		 data: JSON.stringify({ userid: userId, messageid: "1"}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);	
		}
	});
}

function getusermessagebyid(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/getusermessageByid.htm",
		 data: JSON.stringify({ userid: userId, messageid: "1"}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);	
		}
	});
}

function saveusermessageasdraft(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/saveusermessageasdraft.htm",
		data: JSON.stringify( {"senderid":userId,"receiverid":vendorId, message: "hi how are you?", subject: "invitation"}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);	
		}
	});
}

function deletesavedtradeinventories(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/deletesavedtrade.htm",
		 data: JSON.stringify({ tradeid: "209", level: "16"}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);	
		}
	});
}

function getrecentfeedbacks(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/getrecentfeedbacks.htm",
		 data: JSON.stringify({ userid: "10"}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);	
		}
	});
}


function saveusersuggestions(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/saveusersuggestedimprovement.htm",
		 data: JSON.stringify({ userid: "10", suggestion: ""}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);	
		}
	});
}

function saveuserreportedbugs(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/saveuserreportedbugs.htm",
		 data: JSON.stringify({ userid: "10", bug : ""}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);	
		}
	});
}

function saveuserreviews(){
	$.ajax({
		type: "POST",
		url: host+"/traidit/saveuserreviews.htm",
		 data: JSON.stringify({ userid: "10", review : ""}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);	
		}
	});
}


function getCommissionOfUser(){
	var currentUserId = prompt("Please enter User Id", userId);
	$.ajax({
		type: "POST",
		url: host+"/traidit/getcommissionofuser.htm",
		 data: JSON.stringify({ userid: currentUserId}),
		contentType: 'application/json',
		success: function(JSONArray){
			alert(JSONArray);	
		}
	});

	
}
	function testexcelupload(){
		$.ajax({
			type: "POST",
			url: host+"/traidit/testexcelupload.htm",
			 //data: JSON.stringify({ userid: "10", review : ""}),
			contentType: 'application/json',
			success: function(data){
				alert(data);	
			}
		});
	}


	function savefavtrade(){
		$.ajax({
			type: "POST",
			url: host+"/traidit/savefavouritetrade.htm",
			 data: JSON.stringify({ userid: "1062", tradeid :"18"}),
			contentType: 'application/json',
			success: function(JSONArray){
				alert(JSONArray);	
			}
		});
	}
	
	
	function listfavtrades(){
		$.ajax({
			type: "POST",
			url: host+"/traidit/viewlistoffavouritetrades.htm",
			 data: JSON.stringify({ userid: "1062"}),
			contentType: 'application/json',
			success: function(JSONArray){
				alert(JSONArray);	
			}
		});
	}
	
	
	function deletefavtrade(){
		$.ajax({
			type: "POST",
			url: host+"/traidit/deletefavouritetrade.htm",
			 data: JSON.stringify({ userid: "1062", tradeid:"18"}),
			contentType: 'application/json',
			success: function(JSONArray){
				alert(JSONArray);	
			}
		});
	}

	/*
	 * Added By Bhagya On Jan 05th,2015
	 * Function for getting the user commission details
	 */
	function getusercommissions(){
		$.ajax({
			type: "POST",
			url: host+"/traidit/usercommissions.htm",
			data: JSON.stringify({ userid:"404"}),
	        contentType: 'application/json',
			success: function(JSONArray){
				alert(JSONArray);
			}
		});
	}
	
	/**
	 * @author Thangaraj(KNSTEK)
	 * @since 06-01-2015
	 * 
	 */
	function getcountsfordashboard(){
		$.ajax({
			type: "POST",
			url: host+"/traidit/getcountsfordashboard.htm",
			data: JSON.stringify({ userid:userId}),
	        contentType: 'application/json',
			success: function(JSONArray){
				alert(JSONArray);
			}
		});
	}
	

	function updatenotificationsounds(){
		$.ajax({
			type: "POST",
			url: host+"/traidit/updatenotificationsounds.htm",
			data: JSON.stringify({ userid:userId,sound:"sound1",soundfor:"message"}),
	        contentType: 'application/json',
			success: function(JSONArray){
				alert(JSONArray);
			}
		});
	}
	

	function updatenotificationsettings(){
		$.ajax({
			type: "POST",
			url: host+"/traidit/updatenotificationsettings.htm",
			data: JSON.stringify({ userid:userId,notificationsoundon:true}),
	        contentType: 'application/json',
			success: function(JSONArray){
				alert(JSONArray);
			}
		});
	}

	/**
	 * @author Thangaraj(KNSTEK)
	 * @since 13-01-2015
	 */
	function listmessagecontacts(){
		$.ajax({
			type: "POST",
			url: host+"/traidit/listmessagecontacts.htm",
			data: JSON.stringify({ userid:userId}),
	        contentType: 'application/json',
			success: function(JSONArray){
				alert(JSONArray);
			}
		});
	}
	
	/**
	 * 
	 * @author Thangaraj(KNSTEK)
	 * @since 21-01-2015
	 */
	function getrefferralurl(){
		$.ajax({
			type: "POST",
			url: host+"/traidit/getrefferralurl.htm",
			data: JSON.stringify({ userid:userId}),
	        contentType: 'application/json',
			success: function(JSONArray){
				alert(JSONArray);
			}
		});
	}
	
	/**
	 * 
	 * @author Thangaraj(KNSTEK)
	 * @since 29-01-2015
	 */
	function emptytrash(){
		$.ajax({
			type: "POST",
			url: host+"/traidit/emptytrash.htm",
			data: JSON.stringify({ userid:userId}),
	        contentType: 'application/json',
			success: function(JSONArray){
				alert(JSONArray);
			}
		});
	}

	/**
	 * 
	 * @author Thangaraj(KNSTEK)
	 * @since 03-02-2015
	 */
	function updateRefferral(){
		$.ajax({
			type: "POST",
			url: host+"/traidit/updaterefferral.htm",
			data: JSON.stringify({ userid:userId}),
	        contentType: 'application/json',
			success: function(JSONArray){
				alert(JSONArray);
			}
		});
	}
	
	/**
	 * Created By Bhagya On jan 30th,2015
	 * function for getting direct deposit information for saving it to db
	 */
		function getdeposittosaveorupdate(){
			$.ajax({
			  type: "POST",
			  url: host+"/traidit/savedeposit.htm",
			  data: JSON.stringify({depositid:"0",userid:"99",bankname:"vijaya bank",accounttype:"current" ,routingnumber: "bc123",accountnumber: "02"}),
			  contentType: 'application/json',
			  success: function(data) {
				  alert(data);
			  }
			});
		}
		
		/**
		 * Created By Bhagya On Feb 03rd,2015
		 * function for getting Tax information for saving it to db
		 */
			function gettaxtosaveorupdate(){
				$.ajax({
				  type: "POST",
				  url: host+"/traidit/savetax.htm",
				  data: JSON.stringify({taxid:"0",userid:"4",socialsecuritynumber:"kns04",firstname:"sankar" ,middlename: "r",lastname: "sai",dateofbirth:"12/12/1992",termsandconditions:true}),
				  contentType: 'application/json',
				  success: function(data) {
					  alert(data);
				  }
				});
			}
			/**
			 * Created By Bhagya On Feb 04th,2015
			 * Function for displaying the depositdetails
			 */
			function getdeposittodisplay(){
				$.ajax({
					type: "POST",
					url: host+"/traidit/displaydepositdetails.htm",
					data: JSON.stringify({ userid: "25"}),
					contentType: 'application/json',
					success: function(JSONArray){
						alert(JSONArray);
						}	
					
				});
			}
			
			/**
			 * Created By Bhagya On Feb 04th,2015
			 * Function for displaying the taxdetails
			 */
			function gettaxtodisplay(){
				$.ajax({
					type: "POST",
					url: host+"/traidit/displaytaxdetails.htm",
					data: JSON.stringify({ userid: "100"}),
					contentType: 'application/json',
					success: function(JSONArray){
						alert(JSONArray);
						}	
					
				});
			}

	