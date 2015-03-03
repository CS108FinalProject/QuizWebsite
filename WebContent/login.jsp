<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css//main.css" ></link>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ExQuizit!</title>

<script>
	function validateForm() {
		var username = document.getElementById('username').value;
		var password = document.getElementById('password').value;
		
		if ( username == null || username == "" ) {
			alert("Username cannot be empty");
			return false;
		} else if ( password == null || password == "" ) {
			alert("Password cannot be empty");
			return false;
		}
		return true;
	}
</script type="text/javascript">
</head>

<body>	
		<%String errMsg = (String)request.getAttribute("errMsg");%>
	<%if (errMsg == null) { %>
			<%="<h1>Welcome to ExQuizit</h1>"%>	
	<%} else {%>
		<%= errMsg%>
	<%}%>
	<form class="login_form" action = "LoginServlet" method = "POST"
	onsubmit="return validateForm();">
		<p>User <input class="usr" id="username" type = "text" name = "username"/></p>
		<p>Password<input class="pw" id="password" type = "password" name = "password"/></p>
		<input class="btn" type = "submit"/>
	</form>
	<p>New User? <a href = "registration.jsp">Register here!</a></p>
	
</body>
</html>