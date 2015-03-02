<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ page import="com.accounts.pages.SearchFriendServlet, com.accounts.Account, com.accounts.AccountManager, com.accounts.Message, com.util.Constants, java.util.*, java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css//main.css" ></link>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>
	
	<%
		request.setAttribute("account", "not_searched");
	%>
	
	<h2> Find Friends</h2>
	
	<form action="SearchFriendServlet" method="post"> 
		<input type="text" value="" name="friend_id">
		<input type="submit" value="Search Friend">
	</form>
	<br>
	
	<%
	String name = (String)request.getParameter("id");
	//out.println("<p>" + name + "</p>");
	
	// Check whether account exists and prints a message accordingly
	if ((request.getAttribute("account") != null) && (!((String) request.getAttribute("account")).equals("not_searched"))) {
		// Case of account exists - print link to acount's profile
		out.println("<p> Friend found: </p><br>");
		out.println("<p><a href=\"accountProfile.jsp?friend_id=" + name + "\">" + name + "</a></p>");
	} else if (request.getAttribute("account") == null){
		out.println("<p> Friend not found </p>");
	}
	
	%>
	
</body>
</html>