<!--  
@author: Kelsey
path: /WebContent/reLogin.jsp
 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css//main.css" ></link>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ReLogin</title>
</head>
<body>
	<h1>Sorry, the specified username or password was invalid. Please try again.</h1>
	<form action = "LoginServlet" method = "POST">
		<p>User: <input type = "text" name = "username"/></p>
		<p>Password:<input type = "text" name = "password"/></p>
		<input type = "submit"/>
	</form>
	<p>New User? <a href = "registration.jsp">Register here!</a></p>
</body>
</html>
