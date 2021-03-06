<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import = "com.accounts.*"%>
	<%@ page import = "java.util.*"%>
	<%@ page import = "javax.swing.*" %>
	<%@ page import = "java.awt.*" %>
	<%@ page import = "java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	String name = (String)request.getSession().getAttribute("session_user");
	List<Announcement> announcements = AccountManager.getAnnouncementObjects();
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css//style.css" ></link>

<title>Announcements</title>
</head>

<style>
table, th, td {
    border: 1px solid black;
    text-align: center;
}

th {
    background-color: #34495E;
    color: white;
}

td {
    padding: 7px;
}
</style>

<body>
	<header>
		<table id="header">
			<tr>
				<%
				Account acct = AccountManager.getAccount(name);
				out.println("<th class = \"btn\"><a href = \"homepage.jsp\">Homepage</a></th>");
				%> 
				<th class = "btn"><a href = "showAnnouncements.jsp">Announcements</a></th>
				<th class = "btn"><a href = "showAchievements.jsp">My Achievements</a></th>

				<th class = "btn">My Messages 
					<form action = <%="\"showMessage.jsp?id="+name+"\""%>>					
						<select name = "choice">
							<option>Received Messages</option>
							<option>Sent Messages</option>
						</select>
						<input name="choice" type="hidden" value=<%=(String)request.getParameter("choice")%>>
						<input name="id" type="hidden" value=<%=name%>>
						<input type = "submit" value = "Go">
					</form>
				</th>
				<th class = "btn"> <a href="searchFriends.jsp?id=<%=name%>"> Lookup Users</a> </th>
				<th class = "btn"><a href = <%="\"quizHome.html?user="+name+"\""%>> Create Quiz</a></th>
				<th class = "btn"><a href = "history.jsp"> History</a></th>
			</tr>
		</table>
	</header>
<BR>
<h2>Announcements</h2>
<div id="announcements">
	<!-- Top Scorers table -->
	<table id="Announcements">
			<tr>
				<th>Announcement</th>
				<th>Posted by</th>
				<th>Date</th>
			</tr>
			
			<%
				for (Announcement announcement : announcements) {
					out.println("<tr><td>" + announcement.getContent() + "</td>"
					+ "<td>" + announcement.getUsername() + "</td>" 
					+ "<td>" + announcement.getDate() + "</td></tr>"); 
				}		
			%>
	</table>
</div>
</body>
</html>
