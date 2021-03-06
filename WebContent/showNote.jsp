<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ page import="com.accounts.Account, com.accounts.AccountManager, com.accounts.Message, com.util.Constants, java.util.*, java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css//style.css" ></link>
<title>Insert title here</title>
</head>
<body>

<%
	String content = (String) request.getAttribute("message_content");
	String sender = (String) request.getAttribute("sender");
%>
<header>
	<table id="header">
			<tr>
				<%
				String name = (String)request.getSession().getAttribute("session_user");
				Account acct = AccountManager.getAccount(name);
				out.println("<th class = \"btn\"><a href = \"homepage.jsp\">Homepage</a></th>");
				%> 
				<th class = "btn"><a href = "showAnnouncements.jsp">Announcements</a></th>
				<th class = "btn"><a href = "showAchievements.jsp">My Achievements</a></th>

				<th class = "btn">My Messages 
					<form action = <%="\"showMessage.jsp?id="+(String)request.getSession().getAttribute("session_user")+"\""%>>					
						<select name = "choice">
							<option>Received Messages</option>
							<option>Sent Messages</option>
						</select>
						<input name="choice" type="hidden" value=<%=(String)request.getParameter("choice")%>>
						<input name="id" type="hidden" value=<%=name%>>
						<input type = "submit" value = "Go">
					</form>
				</th>
				<th class = "btn"> <a href="searchFriends.jsp?id=<%=sender%>"> Lookup Users</a> </th>
				<th class = "btn"><a href = <%="\"quizHome.html?user="+name+"\""%>> Create Quiz</a></th>
				<th class = "btn"><a href = "history.jsp"> History</a></th>
			</tr>
	</table>
</header>
<BR>

	<%
		if (sender.equals(name)) {
			out.println("<h2> Here is your message</h2>");
		} else {
			out.println("<h2> Message from " + sender + "</h2>");
		}
	%>
<br> <br>
<p> <%=content%> </p>

</body>
</html>