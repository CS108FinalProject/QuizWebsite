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
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>

	<%
		String quizName = "MyNewQuiz";//(String) request.getAttribute(Constants.QUIZ_NAME);
	%>
	
	<h1>Edit Quiz <%=quizName%></h1>
	
	<%
		String errMsg = (String) request.getAttribute(Constants.ERROR_MESSAGE);
  		if (errMsg != null) {
 			 out.println("<p>"+errMsg+"</p><BR>");
  		}
  	%>
	
	<form action="EditQuiz" method="post"> 
		<label> Title</label>
		<textarea rows = "1" cols = "20"  name=<%=Constants.MODIFIED_QUIZ_NAME%>><%=quizName%></textarea><BR>
		<label> Description</label>
		<textarea rows = "4" cols = "20"  name=<%=Constants.DESCRIPTION%> ><%=QuizManager.getQuiz(quizName).getDescription() %></textarea><BR><BR>
		<input type="checkbox" name=<%=Constants.IS_IMMEDIATE%>> Is Immediate<BR>
		<input type="checkbox" name=<%=Constants.IS_RANDOM%>> Is Random<BR>
		<input type="checkbox" name=<%=Constants.IS_ONE_PAGE%>> Is One Page<BR><BR>
		<input type = "hidden" name=<%=Constants.QUIZ_NAME%> value = <%=quizName%>>
		<input type="submit" name=<%=Constants.REQUEST%> value="Remove Quiz">
		<input type="submit" name=<%=Constants.REQUEST%> value="Remove Questions">
		<input type="submit" name=<%=Constants.REQUEST%> value="Save Changes">
	</form>

</body>
</html>