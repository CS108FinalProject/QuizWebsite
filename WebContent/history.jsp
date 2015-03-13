<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import = "com.accounts.*"%>
<%@ page import = "com.quizzes.*"%>
<%@ page import = "com.util.*"%>
<%@ page import = "java.util.*"%>
<%@ page import = "java.text.DecimalFormat" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>

<link rel="stylesheet" href="css//style.css" ></link>
</head>
<style>
table, th, td {
    border: 1px solid black;
    text-align: center;
}

td {
    padding: 10px;
}

th {
    background-color: #34495E;
    color: white;
}
</style>


<body>
<%
// Get logged in user.
String name = (String)getServletContext().getAttribute("session_user");
Util.validateString(name);
Account user = AccountManager.getAccount(name);
DecimalFormat formatter = new DecimalFormat("0.00");
%>

<!-- Main Navigation Bar -->
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
						<th class = "btn"><a href = <%="\"login.jsp?errMsg=\"LoggedOut\""%>>Logout</a></th>						 
					</tr>
			</table>
</header>


<!-- Print Page title and quiz name -->
<br>
<h1>User History</h1>

<%
// Get Past Performance.
List<Record> pastPerformance = user.getPastPerformance(0);
%>

<!--  Past performance table -->
<h4>Your Past Performance:</h4>
<table id="pastPerformanceTable" class="statTable">
		<tr>
			<th>Quiz</th>
			<th>Date</th>
			<th>Score</th>
			<th>Elapsed Time</th>
		</tr>
		
		<%
			for (Record record : pastPerformance) {
				out.println("<tr><td>" + record.getQuizName() + "</td>" 
				+ "<td>" + record.getDate() + "</td>" 
				+ "<td>" + formatter.format(record.getScore()) + "</td>" 
				+ "<td>" + formatter.format(record.getElapsedTime()) + "</td></tr>");
			}		
		%>
</table>

</body>
</html>