<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import = "com.accounts.*"%>
	<%@ page import = "java.util.*"%>
	<%@ page import = "javax.swing.*" %>
	<%@ page import = "java.awt.*" %>
	<%@ page import = "java.awt.List" %>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="homepage.css" ></link>
<%String name = (String)request.getParameter("id");
//Account acct = AccountManager.getAccount(name);
%>
<title>Welcome <%=name%></title>
</head>
	
<body>
	<table id="header">
			<tr>
				<th>Admin. Announcements</th>
				<th>My Achievements</th>
				<th>My Messages</th>
				<th>Find Friends</th>
				<th>Quizzes</th>
			</tr>
	</table>
	<h2>Nice to see you,<%=name %></h2>
	<table id = "content">
		<tr>
			<%//Displays any admin announcements as a list%>
			<%ArrayList<String> admin_anmts = (ArrayList<String>)getServletContext().getAttribute("admin_anmts");%>
			<td><div id="announcements">Announcements
				<%if (admin_anmts != null) { 
					out.println("<ul>");
					int anmts_len = admin_anmts.size();
					for (int i = 0; i < anmts_len; i++) { 
						out.println("<li>"+admin_anmts.get(i)+"</li>");
					} 
					out.println("</ul>");
				}%>	
			</div></td>
			<td>
				<form action = <%="\"homepage.jsp?id="+name+"\""%>>
					<select name = "choice">
						<option>Received Messages</option>
						<option>Sent Messages</option>
					</select>
					<input type = "submit">
				</form>
				<div id="messages">
					<%
						String msgToDisplay = (String)request.getParameter("choice");
						ArrayList<Message> messages;
						/*
						if (msgToDisplay != null) {
							if (msgToDisplay.equals("Inbox")) { 
								messages = acct.getReceivedMessages();		
							} else {
								messages = acct.getSentMessages();		
							}
							out.println("<table>");
							for ( int i = 0; i < messages.size();i++ ) {
								out.println("<tr>");
								Message msg = messages.get(i);
								out.println("<td>"+msg.getSender()+"</td>");
								out.println("<td>"+msg.getDate()+"</td>");
								out.println("<td>"+msg.getType()+"</td>");
								out.println("</tr>");
								if (i == 4) i = messages.size();
							}
							out.println("</table>");
						}
						*/
					//TODO::Extension to add Sorting mechanisms to table cols
						
					%>
				</div>
			</td>
		</tr>
		<tr>
			<td><div id="achievements">Achievements</div></td>
			<td><div id="friendsAchievements">Recent Friends
					Achievements</div></td>
		</tr>
		<tr>
			<td><div id="createdQuizzes">My created Quizzes</div></td>
			<td><div id="popularQuizzes">Popular Quizzes</div></td>
		</tr>
		<tr>
			<td><div id="recentQuizzes">Recent Quizzes</div></td>
			<td><div id="popularQuizzes">Popular Quizzes</div></td>
		</tr>
		<tr>
			<td><div id="recentQuizzes">My History</div></td>
		</tr>
	</table>
</body>
</html>