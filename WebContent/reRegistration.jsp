<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ReRegistration</title>
</head>
<body>
<p>Please, Enter another username and password.</p>
	<form action = "RegistrationServlet" method = "POST">
		<p>User: <input type = "text" name = "user"/></p>
		<p>Password:<input type = "text" name = "password"/></p>
		<input type = "submit"/>
	</form>
</body>
</html>