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
				<th>Announcements</th>
				<th>My Achievements</th>
				<th>My Messages 
					<form action = <%="\"homepage.jsp?id="+name+"\""%>>
						<select name = "choice">
							<option>Received Messages</option>
							<option>Sent Messages</option>
						</select>
						<input type = "submit" value = "Go">
					</form>
				</th>
				<th>Find Friends</th>
				<th>Quizzes</th>
			</tr>
	</table>
	<h2>Nice to see you,<%=name %></h2>
	<table id = "content">
		<tr>
			<%//Displays any admin announcements as a list%>
			<%
			ArrayList<String> admin_anmts = (ArrayList<String>)request.getAttribute("admin_anmts");
			%>
			<td><div id="announcements">Announcements
				<%if (admin_anmts != null) { 
					out.println("<ul>");
					int anmts_len = admin_anmts.size();
					for (int i = 0; i < anmts_len; i++) { 
						out.println("<li>"+admin_anmts.get(i)+"</li>");
						if (i == 4) i = anmts_len;
					} 
					out.println("</ul>");
				} else {
					out.println("<br></br>Hello, no new announcements.");
				}
				%>	
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
						String sel_name = (String)request.getParameter("choice");
					try {
						ArrayList<String> messages = (ArrayList<String>)getServletContext().getAttribute("");
						out.println("<table>");			
						int list_len = messages.size();
						for (int i = 0;i < list_len;i++) {
							out.println("<tr>");
							out.println("<td>");
							out.println("<a href = \"showMessage.jsp?id="+name+"\">"+messages.get(i)+"</a>");
							out.println("</td>");
							out.println("</tr>");
							if  (i == 4) i = list_len;
						}
						out.println("</table>");
					} catch (Exception e) {
						out.println("<table>");
							out.println("<tr>");
							out.println("<td>");
							out.println("Select messages to display from form above.");
							out.println("</td>");
							out.println("</tr>");
						out.println("</table>");
					}
						/*
						ArrayList<Message> messages = acct.getReceivedMessages();	
						int messageSz = messages.size();
;
						if (messageSz > 0) {
							out.println("<table>");
							out.println("<a href =\"showMessage.jsp?choice="+sel_name+"\">View All Messages</a>");
							for ( int i = messageSz-1; i > -1;i-- ) {
								out.println("<tr>");
								Message msg = messages.get(i);
								out.println("<td>"+msg.getSender()+"</td>");
								out.println("<td>"+msg.getDate()+"</td>");
								out.println("<td>"+msg.getType()+"</td>");
								if (i == messageSz -5) i = -1;
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
			<td><div id="myHistory">My History</div></td>		
		</tr>
	</table>
</body>
</html>