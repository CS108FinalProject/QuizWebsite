/**
 * A way to handle the login and registration actions dynamically
 *  Author: Kelsey Young Stanford University '15
 *  
 */
(function(window, document, undefined) {
	var indexDiv = document.getElementById('index_box');
	
// script elements that correspond to Handlebars templates
	var loginFormTemplate = document.getElementById('login-form-template');
	var registrationFormTemplate = document.getElementById('registration-form-template');

	var templates = {
			renderLoginForm: Handlebars.compile(loginFormTemplate.innerHTML),
			renderRegistrationForm: Handlebars.compile(registrationFormTemplate.innerHTML)
	
	};
	
	// Initial render
	$('#index_box').html( templates.renderLoginForm() );
	console.log( templates.renderLoginForm() );
	
    /* Returns the pendingUsername stored in localStorage from either registration or login. */
    function getPendingUsername() {
        if (!localStorage.username) {
            localStorage.username = "";
        }
        return  localStorage.username;
    };
    
    
    /* Returns the pendingPassword stored in localStorage from either registration or login. */
    function getPendingPassword() {
        if (!localStorage.password) {
            localStorage.password = "";
        }
        return  localStorage.password;
    };
    
    /* Returns the pendingisAdmin stored in localStorage from either registration. */
    function getPendingAdmin() {
        if (!localStorage.pendingAdmin) {
            localStorage.pendingAdmin = "";
        }
        return  localStorage.pendingAdmin;
    };
	
	$(document).ready(function() {
		$('#registration_button').click(function() {
			templates.renderRegistrationForm();
		});
		
		$('#login_button').click(function() {
			
			templates.renderLoginForm();
		});
		
		
		//listen to the registration submit
		$('#index_box').on('click','#registration-submit', function(event) {
			event.preventDefault();
			var username = $('#username').val();
			var password = $('#password').val();
			var isAdmin = $('#isAdministrator').val().is(':checked');
			localStorage.username = username;
			localStorage.password = password;
			localStorage.admin = isAdmin;
			
			
		});
		
		
		// Listen to the login submit
		$('#index_box').on('click','#registration-submit', function(event) {
			event.preventDefault();
			var username = $('#username').val();
			var password = $('#password').val();
			localStorage.username = username;
			localStorage.password = password;
		});
		
		
		indexDiv.addEventListener('click',function(event) {
			if (event.target.className === "btn-submit")  {
                var URL = "";
				switch(event.target.value) {
					case "Register!":
						URL = "/QuizWebsite/RegistrationServlet";
						break;
					case "Login!":
						URL = "/QuizWebsite/LoginServlet"
							break;
					default:
				}
				$.ajax({
					url:URL,
					type: 'POST',
					asynch: true,
					data: {username: getPendingUsername(), password:getPendingPassword(), isAdmin:getPendingAdmin() },
                    contentType: 'application/x-www-form-urlencoded',
                    success: function(data, textStatus, jqXHR) {
                        console.log( data );
                    }              					
				});				
			}
			
		});		
	});
	
	
	
	
    // Utiliity Functions
    // Given a query string "?to=email&why=because&first=John&Last=smith"
    // getUrlVar("to")  will return "email"
    // getUrlVar("last") will return "smith"
     
    // Source: (https://gist.github.com/varemenos/2531765#file-getparam-js)
    // Slightly more concise and improved version based on http://www.jquery4u.com/snippets/url-parameters-jquery/
    function getUrlVar(key){
        var result = new RegExp(key + "=([^&]*)", "i").exec(window.location.search); 
        return result && unescape(result[1]) || ""; 
    }
	
	
})(this, this.document);