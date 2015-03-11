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
	<ul>
	<nav>
    <ul>
      <%
	  	out.println("<a href = \"editQuiz.jsp\">Edit Questions</a></li>");
      %> 
    </ul>
	<%
		// get questions
		String quizName;
		if (request.getAttribute(Constants.QUIZ_NAME) != null) {
			quizName = (String) request.getAttribute(Constants.QUIZ_NAME);
		} else {
			quizName = request.getParameter("quizName");	
		}
		//System.out.println("quizName: " + quizName);
		Quiz quiz = QuizManager.getQuiz(quizName);
		List<Question> questions = quiz.getQuestions();
		
		// remove question
		if (request.getParameter("index") != null) {
			int index = Integer.parseInt(request.getParameter("index"));
			System.out.println(questions.get(index).getQuestion());
			quiz.removeQuestion(questions.get(index));
			questions.remove(index);
		}
		
		// print all questions 
		if (questions != null) {
			for (int i=0; i< questions.size(); i++) {
				int question_num = i+1;
				out.println("<li>" + question_num + ". " + questions.get(i).getQuestion() + "    <a href = \"removeQuestions.jsp?quizName=" + quizName + "&index=" + i + "\">Remove</a>");
			}
		}
	%>
	</ul>
</body>
</html>