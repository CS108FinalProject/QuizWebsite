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
String name = (String)getServletContext().getAttribute("session_user");
	ArrayList<String> admin_anmts = (ArrayList<String>)AccountManager.getAnnouncements();
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css//style.css" ></link>

<title>Announcements</title>
</head>
<body>

<table id="header">
			<tr>
				<%
				Account acct = AccountManager.getAccount(name);
				if(acct.isAdmin()) {
					out.println("<th><a href = \"homepage.jsp\">Homepage</a></th>");
				} else {
					out.println("<th><a href = \"homepage.jsp\">Homepage</a></th>");
				}
				%> 
				<th><a href = "homepage.jsp">Homepage</a></th>					
				<th><a href = "showAnnouncements.jsp">Announcements</a></th>
				<th>My Achievements</th>

				<th>My Messages 
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
				<th> <a href="searchFriends.jsp?id=<%=name%>"> Lookup Users</a> </th>
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
