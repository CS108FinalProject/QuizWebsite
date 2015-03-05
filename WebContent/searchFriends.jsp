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
	
	<table id="header">
			<tr>
				<%/*if(acct.isAdmin()) {
					out.println("<th<a href = \"adminHomepage.jsp\">Homepage</a></th>");
				} else {
					out.println("<th<a href = \"homepage.jsp\">Homepage</a></th>");
				}
					*/%> 
					
				<th><a href = <%="\"homepage.jsp?id="+ (String) request.getAttribute("sender") +"\""%>>Homepage</a></th>
				<th><a href = "showAnnouncements.jsp">Announcements</a></th>
				<th>My Achievements</th>

				<th>My Messages 
					<form action = <%="\"showMessage.jsp?id="+(String) request.getAttribute("sender")+"\""%>>					
						<select name = "choice">
							<option>Received Messages</option>
							<option>Sent Messages</option>
							<option>Send A Message</option>
						</select>
						<input name="choice" type="hidden" value=<%=(String)request.getParameter("choice")%>>
						<input name="id" type="hidden" value=<%=(String) getServletContext().getAttribute("session_user")%>>
						<input type = "submit" value = "Go">
					</form>
				</th>
				<th> <a href="searchFriends.jsp?id=<%=(String) request.getAttribute("sender")%>"> Find Friends</a> </th>
				<th>Quizzes</th>
			</tr>
	</table>
	
	<h2> Lookup Users</h2>
	
	<form action="SearchFriendServlet" method="post"> 
		<input type="text" value="" name="friend_id">
		<input type="submit" value="Search Friend">
	</form>
	<br>
	
	<%
	// Check whether account exists and prints a message accordingly
	if ((request.getAttribute("account") != null) ) {
		// Case of account exists - print link to acount's profile
		String friend = (String) request.getAttribute("account");
		String sender2 = (String) request.getAttribute("sender");
		if (AccountManager.accountExists(friend) ) {
			
			out.println("<p> Friend found: </p><br>");
			out.println("<p><a href=\"accountProfile.jsp?friend_id=" + friend 
					+ "&username=" + sender2 + "\">" + friend + "</a></p>");
		} else {
			out.println("<p> Friend Not found: </p><br>");
		}
	} 
	
	%>
	
</body>
</html>
