<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>
	<%
		String sender_name = (String)request.getParameter("id");
		String friend_name = (String)request.getParameter("friend_id");
	%>
	<form action="addFriendServlet" method="post"> 
		<input type="submit" name=message_type value="Add Friend">
	</form>
	<br>
	
	<form action="SendMessageServlet" method="post"> 
		<input type="text" value="" name="msg_content">
		<input type="submit" name=message_type value="Send Note">
	</form>
	
	
</body>
</html>