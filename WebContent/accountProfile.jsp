<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
    	<%@ page import = "com.accounts.*"%>
	<%@ page import = "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css//style.css" ></link>
</head>
<body>
	
	<%
		String sender_name = (String)request.getSession().getAttribute("session_user");
		String friend_name;
		Account acct = AccountManager.getAccount(sender_name);
		if (request.getParameter("friend_id") != null) {
			friend_name = (String)request.getParameter("friend_id");
		} else {
			friend_name = (String)request.getAttribute("friend_id");
		}
		
	%>

	<%String errMsg = (String)request.getAttribute("errMsg");%>
	<%if (errMsg != null) { %>
		<%= errMsg%>
	<%}%>

	<header>	
		<table id="header">
			<tr>
				<%

					out.println("<th class = \"btn\"><a href = \"homepage.jsp\">Homepage</a></th>");
				
				%> 	
				<th class = "btn"><a href = "showAnnouncements.jsp">Announcements</a></th>

				<th class = "btn"><a href = "showAchievements.jsp">My Achievements</a></th>

				<th class = "btn">My Messages 
					<form action = <%="\"showMessage.jsp?id="+sender_name+"\""%>>					
						<select name = "choice">
							<option>Received Messages</option>
							<option>Sent Messages</option>
						</select>
						<input name="choice" type="hidden" value=<%=(String)request.getParameter("choice")%>>
						<input name="id" type="hidden" value=<%=sender_name%>>
						<input type = "submit" value = "Go">
					</form>
				</th>
				<th class = "btn"> <a href="searchFriends.jsp?id=<%=sender_name%>"> Lookup Users</a> </th>
				<th class = "btn"><a href = <%="\"quizHome.html?user="+sender_name+"\""%>> Create Quiz</a></th>
				<th class = "btn"><a href = "history.jsp"> History</a></th>
			</tr>
		</table>
	</header>

	<BR>
	<h2><%=friend_name %>'s Profile</h2>
	<BR>
	<form action="SendMessageServlet" method="post"> 
		<input type = "hidden" name = "id" value = <%="\""+sender_name+"\""%>>
		<input type = "hidden" name = "friend_id" value = <%="\""+friend_name+"\""%>>
		<input type="submit" name=message_type value="Send Note">
		<textarea rows = "4" cols = "10"  name="msg_content" ></textarea>
	</form>
	<BR>
	<form action="SendMessageServlet" method="post"> 
		<input type = "hidden" name = "id" value = <%="\""+sender_name+"\""%>>
		<input type = "hidden" name = "friend_id" value = <%="\""+friend_name+"\""%>>

		<input type="submit" name=message_type value="Send Friend Request">
	</form>
	<BR>
	<form action="SendMessageServlet" method="post"> 
		<input type = "hidden" name = "id" value = <%="\""+sender_name+"\""%>>
		<input type = "hidden" name = "friend_id" value = <%="\""+friend_name+"\""%>>

		<input type="submit" name=message_type value="Unfriend">
	</form>
	<BR>
	<br/>
	<p>Enter a quiz name to send a challenge to this user.</p>
	<form action="SendMessageServlet" method="post"> 
		<input type = "hidden" name = "id" value = <%="\""+sender_name+"\""%>>
		<input type = "hidden" name = "friend_id" value = <%="\""+friend_name+"\""%>>
		<input type="submit" name=message_type value="Challenge">
		<textarea rows = "1" cols = "10"  name="quizName" ></textarea>
	</form>
	
	<br>
	<br>
	
	<%
		// prints friendship errors (already friends, not friends, user befriends herself)
		if (request.getAttribute("friendship_error") != null) {
			out.println("<p>" + (String)request.getAttribute("friendship_error") + "</p>");
		}
	
	%>
	
	
</body>
</html>
