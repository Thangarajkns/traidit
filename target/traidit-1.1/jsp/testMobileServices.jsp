<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
		<script language="JavaScript" src="/traidit/sampleinput.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Insert title here</title>
	</head>
	<body>
		login user <input type="button" name="Button1" value="login user" onclick="getinput();"><br>
		add fav item <input type="button" name="Button2" value="add fav item" onclick="getfavitem();"><br>
		delete fav item <input type="button" name="Button3" value="delete fav item" onclick="getfavitemtodelete();"><br>
		
		display all fav items <input type="button" name="Button6" value="display all fav items" onclick="getallfavitems();"><br>
		add item <input type="button" name="Button7" value="add item" onclick="getitemtoadd();"><br>
		delete item <input type="button" name="Button8" value="delete item" onclick="getitemtodelete();"><br>
		add comment <input type="button" name="Button9" value="add comment" onclick="getaddcomment();"><br>
		add inventory <input type="button" name="Button10" value="add inventory" onclick="getinventorytoadd();"><br>
		delete inventory <input type="button" name="Button11" value="delete inventory" onclick="getinventorytodelete();"><br>
		get edit inventory <input type="button" name="Button12" value="get edit inventory" onclick="geteditinventory();"><br>
		save edited inventory <input type="button" name="Button13" value="save edited inventory" onclick="getinventorytoupdate();"><br>
		display inventory data<input type="button" name="Button14" value="display inventory data" onclick="getinventorytodisplay();"><br>
		display item data <input type="button" name="Button15" value="display item data" onclick="getitemtodisplay();"><br>
		display all items <input type="button" name="Button16" value="display all items" onclick="getallitems();"><br>
		display all fav vendors <input type="button" name="Button17" value="display all fav vendors" onclick="getallfavvendors();"><br>
		display user details <input type="button" name="Button18" value="display user details" onclick="getusertodisplay();"><br>
		display trade messages received <input type="button" name="Button19" value="display trade messages received" onclick="getreceivedmessages();"><br>
		display vendor ratings <input type="button" name="Button18" value="display vendor ratings" onclick="getuserratings();"><br>
		display item ratings <input type="button" name="Button18" value="display item ratings" onclick="getitemratings();"><br>
		register user<input type="button" name="Button19" value="register user" onclick="getusertoadd();"><br>
		get login info <input type="button" name="Button20" value="get login info" onclick="getlogininfo();"><br>
		save edited user <input type="button" name="Button21" value="save edited user" onclick=" saveediteduser();"><br>
		get contact info to edit <input type="button" name="Button22" value="get contact info to edit" onclick="getcontactinfotoedit();"><br>
		update contact profile <input type="button" name="Button23" value="update contact profile" onclick="updatecontactprofileoftrader();"><br>
		get trade phone to edit <input type="button" name="Button24" value="get trade phone to edit" onclick="gettradephone();"><br>
		save edited trade phone <input type="button" name="Button25" value="save edited trade phone" onclick="saveeditedtradephone();"><br>
		change inventory availablity <input type="button" name="Button26" value="change inventory availablity" onclick="changeinventoryavailability();"><br>
		list inventories of user <input type="button" name="Button60" value="list inventories of user" onclick="sendinventorylistofuser();"><br>
		make offer <input type="button" name="Buttom27" value="make offer" onclick="makeoffer();"><br>
		make counter offer <input type="button" name="Buttom28" value="make counter offer" onclick="makecounteroffer();"><br>
		accept offer <input type="button" name="Buttom29" value="accept offer" onclick="acceptoffer();"><br>
		reject offer <input type="button" name="Buttom30" value="reject offer" onclick="rejectoffer();"><br>
		save offer <input type="button" name="Button63" value="save trade to db" onclick="saveoffer();"><br>
		get offer <input type="button" name="Button62" value="get offer" onclick="getoffer();"><br>
		get saved offer <input type="button" name="Button62" value="get saved offer" onclick="getsavedoffer();"><br>
		get trades by criteria<input type="button" name="Button33" value="get trades by criteria" onclick="gettradesbycriteria();"><br>
		<!-- <input type="button" name="Buttom31" value="move trade to saved trade" onclick="maketradesavedtrade();"> -->
		send trade message <input type="button" name="Button32" value="send trade message" onclick="sendtrademessages();"><br>
		initialize registration <input type="button" name="Button34" value="initialize registration" onclick="initializeregistration();"><br>
		complete registration <input type="button" name="Button35" value="complete registration" onclick="completeregistration();"><br>
		display plans <input type="button" name="Button36" value="display plans" onclick="sendplans();"><br>
		save plan <input type="button" name="Button37" value="save plan" onclick="saveplan();"><br>
		save referral <input type="button" name="Button38" value="save referral" onclick="addreferral();"><br>
		social registration <input type="button" name="Button39" value="social registration" onclick="socialregistration();"><br>
		Item Search <input type="button" name="Button40" value="Item Search" onclick="performitemsearch();"><br>
		list parent categories<input type="button" name="Button41" value="list parent categories" onclick="listparentcategories();"><br>
		list subcategories <input type="button" name="Button44" value="list subcategories" onclick="listsubcategories();"><br>
		list homepage categories <input type="button" name="Button44" value="list homepage categories" onclick="listhomepagecategoires();"><br>
		forgot password <input type="button" name="Button42" value="forgot password" onclick="getforgotpassword();"><br>
		validate token <input type="button" name="Button43" value="validate token" onclick="validatePasswordResetToken();"><br>
		update password <input type="button" name="Button25" value="update password" onclick="saveupdatedpassword();"><br>
		test success <input type="button" name="Button45" value="test success" onclick="testsuccess();"><br>
		get payment status <input type="button" name="Button46" value="get payment status" onclick="getPaymentStatus();"><br>
		get item list details <input type="button" name="Button47" value="get item list details" onclick="senditemlist();"><br>
		calculate distance <input type="button" name="Button48" value="calculate distance" onclick="getdistance();"><br>
		login guest user <input type="button" name="Button49" value="login guest user" onclick="loginguest();"><br/>
		category items <input type="button" name="Button51" value="category items" onclick="getallitemsofcategory();"><br>
		Rebuild Item Index <input type="button" name="Button50" value="Rebuild Item Index" onclick="rebuilditemindex();"><br/>
		display inventory of category <input type="button" name="Button52" value="display inventory of category" onclick="sendinventorylistofcategory();"><br/>
		add favorite vendor <input type="button" name="Button4" value="add favorite vendor" onclick="getfavvendor();"><br>
		delete favorite vendor <input type="button" name="Button5" value="delete favorite vendor" onclick="getfavvendortodelete();"><br>
		add favourite inventory <input type="button" name="Button53" value="add favourite inventory" onclick="getfavinventory();"><br>
		delete favourite inventory <input type="button" name="Button54" value="delete favourite inventory" onclick="getfavinventorytodelete();"><br>
		add favourite category <input type="button" name="Button55" value="add favourite category" onclick="getfavcategory();"><br>
		delete favourite category <input type="button" name="Button56" value="delete favourite category" onclick="getfavcategorytodelete();"><br>
		send user message <input type="button" name="Button57" value="send user message" onclick="sendusermessage();"><br>
		rate vendor <input type="button" name="Button58" value="rate vendor" onclick="ratevendor();"><br>
		get vendor details <input type="button" name="Button59" value="get vendor details" onclick="getvendordetails();"><br>
		list vendor ratings <input type="button" name="Button61" value="list vendor ratings" onclick="sendratingslistofuser();"><br>
		complete trade<input type="button" name="Button64" value="complete trade" onclick="completetrade();"><br>
		jiuce calculator<input type="button" name="Button65" value="calculate jiuce" onclick="calculatejiuce();"><br>
		get trade history list<input type="button" name="Button66" value="get trade history list" onclick="gettradehistorylist();"><br>
		get trade history <input type="button" name="Button66" value="get trade history " onclick="gettradehistory();"><br>
		fav inventory list of user <input type="button" name="Button66" value="fav inventory list of user" onclick="getallfavinventories();"><br>
		fav category list of user <input type="button" name="Button67" value="fav category list of user" onclick="getallfavcategories();"><br>
		trader summary <input type="button" name="Button68" value="trader summary" onclick="getsummaryoftrader();"><br>
		add rating restriction <input type="button" name="Button69" value="add rating restriction" onclick="addratingrestriction();"><br>
		list user messages<input type="button" name="Button70" value="list user messages based on flag" onclick="displayusermessagesbyflag();"><br>
		display user message count<input type="button" name="Button71" value="user message count" onclick="displayusermessagescountbyflag();"><br>
		display user message by id<input type="button" name="Button72" value="get user message by id" onclick="getusermessagebyid();"><br>
		delete user message<input type="button" name="Button73" value="delete user message by id" onclick="deleteusermessage();"><br>
		save draft<input type="button" name="Button74" value="save user message as draft" onclick="saveusermessageasdraft();"><br>
		delete saved trade<input type="button" name="Button75" value="delete saved trade" onclick="deletesavedtradeinventories();"><br>
		list recent feedbacks<input type="button" name="Button76" value="list recent feedbacks" onclick="getrecentfeedbacks();"><br>
		handle Card Payment<input type="button" name="Button77" value="handle Card Payment" onclick="handleCardPayment();"><br>
		save user suggestion<input type="button" name="Button78" value="save suggestion" onclick="saveusersuggestions();"><br>
		save user reported bugs<input type="button" name="Button79" value="save reported bug" onclick="saveuserreportedbugs();"><br>
		save user review<input type="button" name="Button80" value="save review" onclick="saveuserreviews();"><br>
		get commission of user<input type="button" name="Button81" value="get commission of user" onclick="getCommissionOfUser();"><br>
		Entire list of inventories for user <input type="button" name="Button82" value="Entire list of inventories for user" onclick="sendentireinventorylistforuser();"><br>
	    save fav trade<input type="button" name="Button78" value="save fav trade" onclick="savefavtrade();"><br>
		list fav trades<input type="button" name="Button79" value="list fav trades" onclick="listfavtrades();"><br>
		delete fav trade<input type="button" name="Button80" value="delete fav trade" onclick="deletefavtrade();"><br>
		test excel upload<input type="button" name="Button81" value="test excel upload" onclick="testexcelupload();"><br>
		test user commissions<input type="button" name="Button82" value="test user commissions" onclick="getusercommissions();"><br>
		update notification sounds<input type="button" name="Button84" value="updatenotificationsounds" onclick="updatenotificationsounds();"><br>
		update notification settings<input type="button" name="Button85" value="updatenotificationsettings" onclick="updatenotificationsettings();"><br>
		list message contacts<input type="button" name="Button86" value="list message contacts" onclick="listmessagecontacts();"><br>
		get refferral url<input type="button" name="Button87" value="get refferral url" onclick="getrefferralurl();"><br>
		empty trash<input type="button" name="Button88" value="empty trash" onclick="emptytrash();"><br>
		Update Refferral<input type="button" name="Button89" value="update refferral" onclick="updateRefferral();"><br>
		save deposit<input type="button" name="Button88" value="save deposit" onclick="getdeposittosaveorupdate()"><br>
		save tax<input type="button" name="Button88" value="save tax" onclick="gettaxtosaveorupdate()"><br>
				display deposit details<input type="button" name="Button88" value="display deposit details" onclick="getdeposittodisplay()"><br>
		display tax details<input type="button" name="Button88" value="display tax details" onclick="gettaxtodisplay()"><br>
	</body>

</html>