(function(window, document, undefined) {
	
	document.getElementsByClassName("btn")[0].addEventListener("submit", function() 
			{
		var username = document.forms[".login_form"]["username"].value;
		var password = document.forms[".login_form"]["password"].value;
	    if (username == null || username == "") {
	    	alert("Username cannot be empty");
	    } else if ( password == "" || password == null)
	    	alert("Password Must be filled out");
	    }
)

})(this, this.document);
