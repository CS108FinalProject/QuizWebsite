<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import = "com.accounts.*"%>
	<%@ page import = "java.util.*"%>
	<%@ page import = "javax.swing.*" %>

	
<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet" href="css//style.css" ></link>
<!--
 <link rel="stylesheet" href="homepage.css" ></link>
 -->
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%String name = (String)getServletContext().getAttribute("session_user");
Account acct = AccountManager.getAccount(name);
String sel_type = (String)request.getParameter("choice");
%>
<title>Welcome <%=name%></title>
</head>
	
	
<body>
	
	<div id = "homepage-wrapper"> 
		<%String errMsg = (String)request.getAttribute("errMsg");%>
		<%if (errMsg != null) {%>
			<%= errMsg%>
		<%}%>
		<header>
			<table id="header">
					<tr>
						<%
						if(acct.isAdmin()) {
							out.println("<th class = \"btn\"><a href = \"adminHomepage.jsp\">Homepage</a></th>");
						} else {
							out.println("<th class = \"btn\"><a href = \"homepage.jsp\">Homepage</a></th>");
						}
						%>					
						<th class = "btn"><a href = "showAnnouncements.jsp">Announcements</a></th>
						<th class = "btn"><a href = "showAchievements.jsp">My Achievements</a></th>
						<th class = "btn">My Messages 
							<form action = <%="\"showMessage.jsp?id="+name+"\""%>>					
								<select name = "choice">
									<option>Received Messages</option>
									<option>Sent Messages</option>
									<option>Send A Message</option>
								</select>
								<input name="choice" type="hidden" value=<%=(String)request.getParameter("choice")%>>
								<input name="id" type="hidden" value=<%=name%>>
								<input type = "submit" value = "Go">
							</form>
						</th>
						<th class = "btn"> <a href="searchFriends.jsp?id=<%=name%>"> Lookup Users</a> </th>
						<th class = "btn"><a href = <%="\"quizHome.html?user="+name+"\""%>> Create Quiz</a></th>
						<th class = "btn"><a href = "history.jsp"> History</a></th>
						<th class = "btn"><a href = <%="\"login.jsp?errMsg=\"LoggedOut\""%>>Logout</a></th>
						 
					</tr>
			</table>
		</header>
		<h2>Nice to see you, <%=name %></h2>

		<% if(acct.isAdmin()) {%>
			<%/*Begin adminContent div*/ %>
			<%out.println("<div class = \"divs-to-float\" id = \"div-admin-content\">"); %>
			<%out.println("<table id = \"table-admin-content\">");%>
			<%out.println("<tr>");%>
			<%out.println("<td>");%>
			
			<%/*Begin addAnnouncement div*/ %>
			<% out.println("<div id = \"addAnnouncement\">"); %>
			<%out.println("Add an Announcement<br></br>");%>		
			<%out.println("<form class = \"cf\" action = \"AdminServlet\" method = \"POST\">");%>
			<%out.println("<input type = \"hidden\""+ name+"= \"id\" value = \"name\">"); %>
			<%out.println("<textarea rows = \"4\" cols = \"15\" name = \"new_ancmnt\"></textarea>");%>
			<%out.println("<input type = \"submit\"></input>"); %>
			<%out.println("</form></div>");%>
			<%/*End addAnnouncement div*/ %>
			
			<%/*Begin siteStats div*/ %>
			<%out.println("<div id = \"siteStats\">");%>
			<%out.println("\"Site Statistics\"<br></br></div></td></tr><tr><td>");%>
			<%/*End siteStats div*/ %>
			
			<%/*Begin removeAcct div*/ %>
			<%out.println("<div id = \"removeAcct\">"+"Remove Account<br></br>");%>
			<%out.println("<form action = \"AdminServlet\" method = \"POST\">");%>
			<%out.println("Account id: <input type = \"text\" name = \"remove_acct\"");%> 
			<%out.println("placeholder = ");%>
			<%Object removed_id = request.getAttribute("remove_acct");
					if (removed_id != null ) {
						removed_id = (String)removed_id;
						out.println("Account "+removed_id+" removed.");
					} else {
						out.println("\"No Account Removed\"");
					}
			%>
			<%out.println("</input>");%>	
			<%out.println("Re-enter account id:<input type = \"text\" name = \"conf_remove_acct\"></input>");%>	
			<%out.println("<input type = \"hidden\" name = \"id\" value = \""+name+"\" >");%>
			<%out.println("<input type = \"submit\"></input></form>");%>				
			
			<%/*End removeAcct div*/ %>
			<%out.println("</div></td></tr><tr>");%>
			<%out.println("<td> Promote User To Admin");%>
			<% out.println("<form action = \"AdminServlet\" method = \"POST\">Account<input type = \"text\" name = \"promote_acct\" placeholder = ");%>
			<% 
							Object promoted_id = request.getAttribute("promote_acct");
							if (promoted_id != null ) {
								promoted_id = (String)promoted_id;
								out.println("\"Account "+promoted_id+" promoted to Administrator.\"");
							} else {
								out.println("\"No Account Promoted\"");
							}

							%>></input>
							<input type = "submit"></input>
				</form>
			</td>
		</tr>
	</table>
	
	<table id = "content">
		<tr>
			<%//Displays any admin announcements as a list%>
			<%	List<String> admin_anmts = AccountManager.getAnnouncements(); %>
			<td><div id="announcements"><a href = "showAnnouncements.jsp">Announcements</a>
				<%if (admin_anmts != null) { 
					out.println("<ul>");
					int anmts_len = admin_anmts.size();
					for (int i = anmts_len - 1; i > -1; i--) { 
						out.println("<li>"+admin_anmts.get(i)+"</li>");
						if (i == anmts_len - 5) i = -1;
					} 
					out.println("</ul>");
				} else {
					out.println("<br></br>Hello, no new announcements.");
				}
				%>	
			</div> </td>		
		
					<td>
						<div id = "send_messages">
							<a href = "searchFriends.jsp?id=<%=name%>">Lookup User</a>
						</div>
						<div id="read_messages">
							<form action = <%="\"showMessage.jsp?id="+name+"\""%>>
								<select name = "choice">
									<option>Received Messages</option>
									<option>Sent Messages</option>
								</select>
								<input name="choice" type="hidden" value=<%=(String)request.getParameter("choice")%>>
								<input name="id" type="hidden" value=<%=name%>>
								<input type = "submit" value = "Display Messages">
							</form>
							<%		
								//TODO: The ReceivedMessages is buggy.						
								List<Message> messages = acct.getReceivedMessages();	
								out.println("<a href =\"showMessage.jsp?choice="+sel_type+"&id="+name+"\""+">View All Messages</a>");
								if (messages.size() > 0) {
									out.println("<table>");
									for ( int i = messages.size() -1; i > -1;i-- ) {
										out.println("<tr>");
										Message msg = messages.get(i);
										out.println("<td>"+msg.getSender()+"</td>");
										out.println("<td>"+msg.getDate()+"</td>");
										out.println("<td>"+msg.getType()+"</td>");
										if (i == messages.size() -5) {
											i = -1;
										}
										
										}
									}
									out.println("</table>");
								}
								
							//TODO::Extension to add Sorting mechanisms to table cols					
							%>				
						</div>
					</td>
				</tr>
			</table>
		</div>
			<div class = "divs-to-float" id = "div-quiz-content">
				<h3>Quiz Index</h3>
				<table id = "table-quiz-content">
					<tr>
						<td><div  class = "btn" id="achievements">Achievements</div></td>
						<td><div class = "btn" id="friendsAchievements">Recent Friends Achievements</div></td>
					</tr>
					<tr>
						<td><div class = "btn" id="createdQuizzes">My created Quizzes</div></td>
						<td><div class = "btn" id="popularQuizzes">Popular Quizzes</div></td>
					</tr>
					<tr>
						<td><div  class = "btn" id="recentQuizzes">Recent Quizzes</div></td>
						<td><div  class = "btn" id="myHistory">My History</div></td>		
					</tr>
				</table>
				<div id = "result-quiz-content">
				</div>
			</div>
		</div>
</body>
</html>
