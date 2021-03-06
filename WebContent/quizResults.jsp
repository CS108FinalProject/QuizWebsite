<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ page import = "com.accounts.*"%>
<%@ page import = "com.quizzes.*"%>
<%@ page import = "com.util.*"%>
<%@ page import = "java.util.*"%>
<%@ page import = "javax.swing.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css//style.css" ></link>
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

<%
// Get quiz name and instantiate a Quiz object.
String quizName = (String)request.getParameter(Constants.QUIZ_NAME);
Util.validateString(quizName);
Quiz quiz = QuizManager.getQuiz(quizName);


// Get logged in user.
String loggedInUser = (String)request.getSession().getAttribute("session_user");
Util.validateString(loggedInUser);
Account user = AccountManager.getAccount(loggedInUser);
%>

	<header>	
		<table id="header">
			<tr>
				<%
				out.println("<th class = \"btn\"><a href = \"homepage.jsp\">Homepage</a></th>");
				%> 			
					
				<th class = "btn"><a href = "showAnnouncements.jsp">Announcements</a></th>
				<th class = "btn"><a href = "showAchievements.jsp">My Achievements</a></th>

				<th class = "btn">My Messages 
					<form action = <%="\"showMessage.jsp?id="+loggedInUser+"\""%>>					
						<select name = "choice">
							<option>Received Messages</option>
							<option>Sent Messages</option>
						</select>
						<input name="choice" type="hidden" value=<%=(String)request.getParameter("choice")%>>
						<input name="id" type="hidden" value=<%=loggedInUser%>>
						<input type = "submit" value = "Go">
					</form>
				</th>
				<th class = "btn"> <a href="searchFriends.jsp?id=<%=loggedInUser%>"> Lookup Users</a> </th>
				<th class = "btn"><a href = <%="\"quizHome.html?user="+loggedInUser+"\""%>> Create Quiz</a></th>
				<th class = "btn"><a href = "history.jsp"> History</a></th>
			</tr>
		</table>
	</header>	

<!-- Print Page title and quiz name -->
<br>
<h1>Quiz Results for <%=loggedInUser %></h1>
<h2><%= quiz.getName() %></h2>

<!-- Print quiz description -->
<h3>Description: </h3>
<p> <%=quiz.getDescription()%>
<br><br>

<%
// Get Record Parameters
String date = request.getParameter(Constants.DATE);
double score = Double.parseDouble(request.getParameter(Constants.SCORE));
double elapsedTime = Double.parseDouble(request.getParameter(Constants.ELAPSED_TIME));
%>

<table id="quizResultsTable">
		<tr>
			<th>Date</th>
			<th>Score</th>
			<th>Elapsed Time</th>
		</tr>
		
		<%
					
			out.println("<td>" + date + "</td>" + "<td>" + score + "</td>" + "<td>" + elapsedTime + "</td>");			
			
			// create record
			Record record = new Record(quizName, user, score, date, elapsedTime);
			
			// update achievements table
			Achievement.checkQuizTakingGoals(user, record, quizName, score);
			
			// add record
			History.addRecord(record);
		%>
</table>

</body>
</html>
