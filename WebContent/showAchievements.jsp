<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
    <%@ page import = "com.accounts.*"%>
    <%@ page import = "com.quizzes.*"%>
	<%@ page import = "java.util.*"%>
	<%@ page import = "javax.swing.*" %>
	<%@ page import = "java.awt.*" %>
	<%@ page import = "java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>
<%
String name = (String)getServletContext().getAttribute("session_user");
	Map<String,Record> achievements = (Map<String,Record>)AccountManager.getAccount(name).getAchievements();
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css//main\.css" ></link>
<title>Achievements</title>
</head>
<body>

<table id="header">
			<tr>
				<%
				Account acct = AccountManager.getAccount(name);
				if(acct.isAdmin()) {
					out.println("<th><a href = \"adminHomepage.jsp\">Homepage</a></th>");
				} else {
					out.println("<th><a href = \"homepage.jsp\">Homepage</a></th>");
				}
				%> 
					
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


<div id="achievements">Achievements
				<%if (achievements != null) { 
					out.println("<ul>");
					for (String achievement : achievements.keySet()) { 
						out.println("<li>"+achievement+"</li>");
					} 
					out.println("</ul>");
				} else {
					out.println("<br></br>Hello, no new achievements.");
				}
				%>	
</div>

	<table id="achievements">
		<tr>
			<th>Achievement Earned</th>
			<th>Quiz</th>
			<th>Score</th>
			<th>Date</th>
			<th>Elapsed Time (min)</th>
		</tr>
		
		<%
			for (String achievement : achievements.keySet()) {
				Record record = achievements.get(achievement);
				
				out.println("<td>" + achievement + "</td>" 
				+ "<td>" + record.getQuizName() + "</td>" 
				+ "<td>" + record.getScore() + "</td>" 
				+ "<td>" + record.getDate() + "</td>" 
				+ "<td>" + record.getElapsedTime() + "</td>");
			}		
		%>
	</table>
	
</body>
</html>