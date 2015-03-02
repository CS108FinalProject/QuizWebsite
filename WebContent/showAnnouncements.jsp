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
<%
String name = (String)getServletContext().getAttribute("session_user");
ArrayList<String> admin_anmts = (ArrayList<String>)getServletContext().getAttribute("announcements");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="homepage.css" ></link>
<title>Announcements</title>
</head>
<body>

	<table id="header">
			<tr>
				<%/*if(acct.isAdmin()) {
					out.println("<th<a href = \"adminHomepage.jsp\">My Homepage</a></th>");
				} else {
					out.println("<th<a href = \"homepage.jsp\">My Homepage</a></th>");
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
						</select>
						<input type = "submit" value = "Go">
					</form>
				</th>
				<th>Find Friends</th>
				<th>Quizzes</th>
			</tr>
	</table>

<div id="announcements">Announcements
				<%if (admin_anmts != null) { 
					out.println("<ul>");
					int anmts_len = admin_anmts.size();
					for (int i = 0; i < anmts_len; i++) { 
						out.println("<li>"+admin_anmts.get(i)+"</li>");
					} 
					out.println("</ul>");
				} else {
					out.println("<br></br>Hello, no new announcements.");
				}
				%>	
</div>
</body>
</html>