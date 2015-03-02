<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css//main.css" ></link>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ExQuizit!</title>
</head>
<body>	
	<h1>Welcome to ExQuizit, Login!</h1>
	<form action = "LoginServlet" method = "POST">
		<p>User: <input type = "text" name = "username"/></p>
		<p>Password:<input type = "password" name = "password"/></p>
		<input type = "submit"/>
	</form>
	<p>New User? <a href = "registration.jsp">Register here!</a></p>
</body>
</html>