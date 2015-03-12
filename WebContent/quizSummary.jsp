<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import = "com.accounts.*"%>
<%@ page import = "com.quizzes.*"%>
<%@ page import = "com.util.*"%>
<%@ page import = "java.util.*"%>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
String quizName = "AllQuizzes2"; //= (String)request.getParameter(Constants.QUIZ_NAME);
//Util.validateString(quizName);

Quiz quiz = QuizManager.getQuiz(quizName);

String loggedInUser = "notadmin"; //(String)getServletContext().getAttribute("session_user");
Account user = AccountManager.getAccount(loggedInUser);

%>

<h1>Quiz Profile</h1>
<h2><%= quiz.getName() %></h2>

<h5>created by: <a href="accountProfile.jsp?friend_id=<%=quiz.getCreator().getUserName()%>&username=<%=loggedInUser%>"><%= quiz.getCreator().getUserName() %></a></h5>

<h3>Description: </h3>
<p> <%=quiz.getDescription()%>

<%
List<Record> pastPerformance = user.getPastPerformance(0);
%>

<h4>Past Performance:</h4>
<table id="pastPerformanceTable">
		<tr>
			<th>Date</th>
			<th>Score</th>
			<th>Elapsed Time</th>
		</tr>
		
		<%
			for (Record record : pastPerformance) {
				out.println("<td>" + record.getDate() + "</td>" 
				+ "<td>" + record.getScore() + "</td>" 
				+ "<td>" + record.getElapsedTime() + "</td>");
			}		
		%>
</table>


<%
List<Record> topScorers = quiz.getTopPerformers(0);
%>

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
				out.println("<td>" + record.getDate() + "</td>" 
				+ "<td>" + record.getScore() + "</td>" 
				+ "<td>" + record.getElapsedTime() + "</td>");
			}		
		%>
</table>


</body>
</html>