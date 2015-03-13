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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
// Get quiz name and instantiate a Quiz object.
String quizName = (String)request.getParameter(Constants.QUIZ_NAME);
if (quizName == null) quizName = (String)request.getAttribute(Constants.QUIZ_NAME);
Util.validateString(quizName);
Quiz quiz = QuizManager.getQuiz(quizName);

// Get logged in user.
String loggedInUser = (String)request.getSession().getAttribute("session_user");
Util.validateString(loggedInUser);
Account user = AccountManager.getAccount(loggedInUser);
DecimalFormat formatter = new DecimalFormat("0.00");
%>
<header>
	<table id="header">
			<tr>			
				<%
				String name = (String)request.getSession().getAttribute("session_user");
				Account acct = AccountManager.getAccount(name);
				out.println("<th class = \"btn\"><a href = \"homepage.jsp\">Homepage</a></th>");
				%> 
				<th class = "btn"><a href="quizHome.html?user=<%=loggedInUser%>&quiz=<%=quizName%>">Take Quiz</a></th>
				<%
				if (loggedInUser.equals(quiz.getCreator().getUserName()) || acct.isAdmin()) {
					out.println("<th class = \"btn\"><a href = \"EditQuizInit?quiz=" + quizName + "\">Edit Quiz</a></th>");
				}
				if (acct.isAdmin()) {
					out.println("<th class = \"btn\"><a href = \"ClearHistory?quiz_name=" + quizName + "\">Clear History</a></th>");
				}
				%>
			</tr>
	</table>
</header>

<!-- Print Page title and quiz name -->
<br>
<h1>Quiz Profile</h1>
<h2><%= quiz.getName() %></h2>


<!-- Print creator with link -->
<h5>created by: <a href="accountProfile.jsp?friend_id=<%=quiz.getCreator().getUserName()%>&username=<%=loggedInUser%>"><%= quiz.getCreator().getUserName() %></a></h5>


<!-- Print quiz description -->
<h3>Description: </h3>
<p> <%=quiz.getDescription()%></p>
<br/>



<%
// Get Past Performance.
List<Record> pastPerformance = quiz.getPastUserPerformance(user, 0);
%>

<!--  Past performance table -->
<h4>Your Past Performance for this Quiz:</h4>
<table id="pastPerformanceTable" class="statTable">
		<tr>
			<th>Date</th>
			<th>Score</th>
			<th>Elapsed Time</th>
		</tr>
		
		<%
			for (Record record : pastPerformance) {
				out.println("<tr><td>" + record.getDate() + "</td>" 
				+ "<td>" + formatter.format(record.getScore()) + "</td>" 
				+ "<td>" + formatter.format(record.getElapsedTime()) + "</td></tr>");
			}		
		%>
</table>
<br/>


<%
// Get top scorers
List<Record> topScorers = quiz.getTopPerformers(0);
%>


<!-- Top Scorers table -->
<h4>Top Scorers:</h4>
<table id="topScorers">
		<tr>
			<th>Username</th>
			<th>Date</th>
			<th>Score</th>
			<th>Elapsed Time</th>
		</tr>
		
		<%
			for (Record record : topScorers) {
				out.println("<tr><td>" + record.getUser().getUserName() + "</td>"
				+ "<td>" + record.getDate() + "</td>" 
				+ "<td>" + formatter.format(record.getScore()) + "</td>" 
				+ "<td>" + formatter.format(record.getElapsedTime()) + "</td></tr>");
			}		
		%>
</table>
<br/>


<%
// Get top scorers in the past 15 minutes.
List<Record> recentTopScorers = quiz.topScorersWithinTimePeriod(0.25);
%>

<!-- Recent Top Scorers table -->
<h4>Recent Top Scorers:</h4>
<table id="recentTopScorers">
		<tr>
			<th>Username</th>
			<th>Date</th>
			<th>Score</th>
			<th>Elapsed Time</th>
		</tr>
		
		<%
			for (Record record : recentTopScorers) {
				out.println("<tr><td>" + record.getUser().getUserName() + "</td>"
				+ "<td>" + record.getDate() + "</td>" 
				+ "<td>" + formatter.format(record.getScore()) + "</td>" 
				+ "<td>" + formatter.format(record.getElapsedTime()) + "</td></tr>");
			}		
		%>
</table>
<br/>


<%
// Get recent scores of all users.
List<Record> recentScores = quiz.getRecentPerformance(0);
%>

<!-- Recent Performance of All Users -->
<h4>Recent Performance of All Users:</h4>
<table id="recentPeformance">
		<tr>
			<th>Username</th>
			<th>Date</th>
			<th>Score</th>
			<th>Elapsed Time</th>
		</tr>
		
		<%
			for (Record record : recentTopScorers) {
				out.println("<tr><td>" + record.getUser().getUserName() + "</td>"
				+ "<td>" + record.getDate() + "</td>" 
				+ "<td>" + formatter.format(record.getScore()) + "</td>" 
				+ "<td>" + formatter.format(record.getElapsedTime()) + "</td></tr>");
			}		
		%>
</table>
<br/>


<%
// Get average statistics for this quiz.
Map<String, Double> average = quiz.getAveragePerformance();
%>

<!-- Average performance table -->
<h4>Average Performance:</h4>
<table id="averagePerformance">
		<tr>
			<th>Score</th>
			<th>Elapsed Time</th>
		</tr>
		
		<%
			out.println("<tr><td>" + formatter.format(average.get(Constants.SCORE)) + "</td>"
				+ "<td>" + formatter.format(average.get(Constants.ELAPSED_TIME)) + "</td></tr>"); 
		%>
</table>
<br/>

</body>
</html>