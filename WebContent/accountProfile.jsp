<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css//main.css" ></link>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>
	<%
		String sender_name =  (String) request.getParameter("username");
		String friend_name = (String)request.getParameter("friend_id");
		System.out.println( "Sender:" + sender_name + "\nFriend: " + friend_name);
	%>


	
	<form action="SendMessageServlet" method="post"> 
		<input type="text" value="" name="msg_content"> 
		<input type = "hidden" name = "id" value = <%="\""+sender_name+"\""%>>
		<input type = "hidden" name = "friend_id" value = <%="\""+friend_name+"\""%>>
		<input type="submit" name=message_type value="Send Note">
	</form>
	
		<form action="SendMessageServlet" method="post"> 
		<input type = "hidden" name = "id" value = <%="\""+sender_name+"\""%>>
		<input type = "hidden" name = "friend_id" value = <%="\""+friend_name+"\""%>>

		<input type="submit" name=message_type value="Add Friend">
	</form>
	<br>

	
	
</body>
</html>
