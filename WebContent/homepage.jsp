<!--
 @author: Kelsey
path: /WebContent/homepage.jsp
 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import = "com.accounts.*"%>
	<%@ page import = "com.quizzes.*"%>
	<%@ page import = "java.util.*"%>
	<%@ page import = "com.util.*" %>
	<%@ page import = "javax.swing.*" %>

	
<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet" href="css//style.css" ></link>
<!--
 <link rel="stylesheet" href="homepage.css" ></link>
 -->
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	String name = (String)request.getSession().getAttribute("session_user");
	
	/*Temp hack while we solve the session_user servletcontext issue*/
	Account acct = null;
	String sel_type = (String)request.getParameter("choice");

	List<String> content_to_display = content_to_display = (ArrayList<String>)request.getAttribute("content_to_display");

	/*See if account exists based off current session name.*/
 	try {
		acct = AccountManager.getAccount(name);
 	} catch(Exception e ) {
 		/*Clear session*/
 		request.getSession().setAttribute("session_user", null);
 		/*set Error*/
		request.setAttribute("errMsg", "<h1>Sorry, an account couldn't be found.</h1>");
		RequestDispatcher dispatch = request.getRequestDispatcher("index.jsp"); 
		dispatch.forward(request, response);
 	}

%>
<title>Welcome <%=name%></title>

<style>
#messagesTable th, #messagesTable td {
    text-align: center;
    padding: 5px;
}

</style>

</head>
	
	
<body>
	
	<div id = "homepage-wrapper"> 
		<%String errMsg = (String)request.getAttribute("errMsg");%>
		<%if (errMsg != null) {%>
			<%= errMsg%>
			<%request.setAttribute("errMsg", null);%>
		<%}%>
		<header>
			<table id="header">
					<tr >
						<th class = "btn"><a href = "homepage.jsp">Homepage</a></th>		
						<th class = "btn"><a href = "showAnnouncements.jsp">Announcements</a></th>
						<th class = "btn"><a href = "showAchievements.jsp">My Achievements</a></th>
						<th  id = "msg-header" class = "btn">My Messages 
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
						<th class = "btn"><a href = <%="\"index.jsp?errMsg=\"LoggedOut\""%>>Logout</a></th>						 
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
			<%out.println("Site Statistics<br><br>");%>
			<label> Number of users: </label> <%= "" + AccountManager.getAllUsers().size() %>
			<label> Number of quizzes taken: </label> <%= "" + QuizManager.getNumQuizzesTaken() %>
			</div>
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
			<br><br><br>
			<%out.println("</input>");%>	
			<%out.println("Re-enter account id: <input type = \"text\" name = \"conf_remove_acct\"></input>");%>	
			<br>
			<%out.println("<input type = \"hidden\" name = \"id\" value = \""+name+"\" >");%>
			<br> 
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
			%>
			<%out.println("</input>");%>
			<%out.println("<input type = \"submit\"></input>");%>
			<%out.println("</form>"); %>
			<%out.println("</td>"); %>
			<%out.println("</tr></table>");%>
			
			<%/*End adminContent div*/ %>
			<%out.println("</div>"); %>		
			
			
			
		<%}%>
		

			<div class = "divs-to-float"  id = "div-account-content">
				<table id = "table-account-content">
					<tr>
						<%//Displays any admin announcements as a list%>
						<%	ArrayList<String> admin_anmts =(ArrayList<String>)AccountManager.getAnnouncements(); %>
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
						</div> </td></tr>		
						<tr>
							<td>
								<div id = "send_messages">
									<a href = "searchFriends.jsp?id=<%=name%>">Lookup User</a>
								</div>
								<div id="read_messages">
									<% 
										out.println("<div><a  href = \"showMessage.jsp?id="+name+"&choice=Sent+Messages\">View ALL Sent Messages</a></div>");
										out.println("<div><a  href = \"showMessage.jsp?id="+name+"&choice=Received+Messages\">View ALL Received Messages</a></div>");
									%>
									<form action = <%="\"homepage.jsp?id="+name+"\""%>>
										<select name = "choice">
											<option >Received Messages</option>
											<option>Sent Messages</option>
										</select>
										<input name="choice" type="hidden" value=<%=(String)request.getParameter("choice")%>>
										<input name="id" type="hidden" value=<%=name%>>
										<input type = "submit" value = "Display Messages">
									</form>
									<%	
									List<Message> messages;
								

									/* If no choice of messages are specified*/
									if (sel_type != null) {
										if (sel_type.equals("Sent Messages")) {
											 messages = acct.getSentMessages();
										} else {
											messages = acct.getReceivedMessages();	
										}
									} else {
										messages = acct.getReceivedMessages();	

									}
										if (messages.size() > 0) {
											out.println("<table id=\"messagesTable\">");											
											out.println("<tr class = \"homepage-content-headers\"><td>Sender</td><td>Date Sent</td><td>Type</td></tr>");
											for ( int i = messages.size() -1; i > -1;i-- ) {
												out.println("<tr>");
												Message msg = messages.get(i);
												out.println("<td><a href = \"accountProfile.jsp?friend_id="+msg.getSender()+"\">"+msg.getSender()+"</a></td>");
												out.println("<td>"+msg.getDate()+"</td>");
												out.println("<td>"+msg.getType()+"</td>");
												if (i == messages.size() -5) i = -1;
											}
											out.println("</table>");
										}
										
									%>				
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<table id = "quiz-index">	
									<tr><th>Quiz Index</th></tr>				
									<tr>
										<td><a href = "HomepageQuizIndexServlet?type_to_display=allQuizzes"  class = "btn" id="allQuizzes">View All Quizzes</a></td>
										<td><a  href = "HomepageQuizIndexServlet?type_to_display=myTakenQuizzes" class = "btn" id="myTakenQuizzes">My Recently Taken Quizzes</a></td>					
									</tr>
									<tr>
										<td><a href = "HomepageQuizIndexServlet?type_to_display=createdQuizzes" class = "btn" id="createdQuizzes">My Created Quizzes</a></td>
										<td><a  href = "HomepageQuizIndexServlet?type_to_display=recentQuizzes" class = "btn" id="recentQuizzes">Recently Created Quizzes</a></td>
									</tr>
									<tr>
										<td><a href = "HomepageQuizIndexServlet?type_to_display=friendActivities" class = "btn" id="friendActivities">Recent Friends' Activities</a></td>		
										<td><a href = "HomepageQuizIndexServlet?type_to_display=popularQuizzes" class = "btn" id="popularQuizzes">Popular Quizzes</a></td>					
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<div id = "result-quiz-content">
						<%if(content_to_display != null) {
							String cont_type = (String)request.getParameter("type_to_display"); 
							int cont_size = content_to_display.size();
							out.println("<table id = \"table-result-quiz-content\"> ");
							for (int i = cont_size - 1; i >= 0;i-- ) {	
								out.println("<tr>");
								out.println("<td>"+content_to_display.get(i)+"</td>");
								out.println("</tr>");
							}
							out.println("</table>");			
						} 
						%>											
					</div>
				</div>
			</div>			
</body>
</html>
