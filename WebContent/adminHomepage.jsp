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
<%String name = (String)getServletContext().getAttribute("session_user");
//Account acct = AccountManager.getAccount(name);
String sel_type = (String)request.getParameter("choice");

%>
<title>Welcome <%=name%></title>
</head>
	
<body>
	<%String errMsg = (String)request.getAttribute("errMsg");%>
	<%if (errMsg == null) { %>
		<%="<h1>Enter your desired username and password!</h1>"%>
	<%} else {%>
		<%= errMsg%>
	<%}%>
	<table id="header">
			<tr>
				<%/*if(acct.isAdmin()) {
					out.println("<th<a href = \"adminHomepage.jsp\">Homepage</a></th>");
				} else {
					out.println("<th<a href = \"homepage.jsp\">Homepage</a></th>");
				}
				*/
					%>
				<th><a href = <%="\"adminHomepage.jsp\""%>>Homepage</a></th>
				<th><a href = "showAnnouncements.jsp">Announcements</a></th>
				<th>My Achievements</th>
				<th>My Messages 
					<form action = <%="\"homepage.jsp?id="+name+"\""%>>
						<select name = "choice">
							<option>Received Messages</option>
							<option>Sent Messages</option>
							<option>Send A Message</option>
						</select>
						<input type = "submit" value = "Go">
					</form>
				</th>
				<th>Find Friends</th>
				<th>Quizzes</th>
			</tr>
	</table>
	<h2>Nice to see you, <%=name %></h2>
	<table id = "admin_content">
		<tr>
			<td>
				<div id = "addAnnouncement">
					Add an Announcement<br></br>
					<form action = "adminServlet/?id=<%=name%>">
						<textarea rows = "4" cols = "50" name = "new_ancmnt"></textarea>
						<input type = "submit"></input>
					</form>
				</div>
				<div id = "siteStats">
					Site Statistics<br></br>	
				</div>	
			</td>
		</tr>
		<tr>
			<td>
					<div id = "removeAcct">
						Remove Account<br></br>
						<form action = "adminServlet/?id=<%=name%>">
							Account id: <input type = "text" name = "remove_acct" value = <% 
							String removed_id = request.getParameter("remove_success");
							if (removed_id != null ) {
								out.println("\"Account "+removed_id+" removed.\"");
							} else {
								out.println("\"No Account Removed\"");
							}
							%>></input>
							Re-enter account id:<input type = "text" name = "conf_remove_acct"></input>							
							<input type = "submit"></input>
						</form>
					</div>									
			</td>
		</tr>
	</table>
	
	
	<table id = "content">
		<tr>
			<%//Displays any admin announcements as a list%>
			<%
			ArrayList<String> admin_anmts = (ArrayList<String>)getServletContext().getAttribute("announcements");
			%>
			<td><div id="announcements"><a href = "showAnnouncements.jsp">Announcements</a>
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
				<form action = <%="\"adminHomepage.jsp?id="+name+"\""%>>
					<select name = "choice">
						<option>Received Messages</option>
						<option>Sent Messages</option>
					</select>
					<input type = "submit">
				</form>
				Send A Message
				<form action = <%="\"MainMessageServlet?id="+name+"\""%>>
					<input type = "text" name = "friend_id" value = "Enter Friends Username"></input>
					<input type = "radio" name = "msg_type" value = "Add Friend" ></input>
					<input type = "radio" name = "msg_type" value = "Challenge"></input>
					<input type = "text" name = "quiz_name" value = "Enter Quiz Name"></input>
					<input type = "radio" name = "msg_type" value = "note"></input>
					<textarea rows="4" cols="20" name = "msg_content"></textarea>
					<input type = "submit" value = "Send Message">
				</form>
				<div id="messages">
					<%
						
					try {
						ArrayList<String> messages = (ArrayList<String>)getServletContext().getAttribute(sel_type);
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
		

						if (messages.size() > 0) {
							out.println("<table>");
							out.println("<a href =\"showMessage.jsp?choice="+sel_name+"\">View All Messages</a>");
							for ( int i = 0; i < messages.size();i++ ) {
								out.println("<tr>");
								Message msg = messages.get(i);
								out.println("<td>"+msg.getSender()+"</td>");
								out.println("<td>"+msg.getDate()+"</td>");
								out.println("<td>"+msg.getType()+"</td>");
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
			<td><div id="myHistory">My History</div></td>		
		</tr>
	</table>
</body>
</html>