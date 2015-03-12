package com.quizzes.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quizzes.*;
import com.util.*;

@WebServlet("/RemoveQuiz")
public class RemoveQuiz extends HttpServlet implements Constants {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     * 
     */
    public RemoveQuiz() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Re-purposed for HTML/JSP.
		response.setContentType("text/html; charset=UTF-8");		
		String quizName = (String) request.getParameter(QUIZ_NAME);
		if (quizName == null) quizName = (String) request.getAttribute(QUIZ_NAME);
		if (quizName != null) {
			Util.validateString(quizName);
			Quiz quiz = QuizManager.getQuiz(quizName);
			quiz.removeQuiz();
		}
		RequestDispatcher dispatch = request.getRequestDispatcher("homepage.jsp"); 
		dispatch.forward(request, response);
		
//		 //TODO:Find a way to test this
//		 response.setContentType("application/json");
//		 PrintWriter out = response.getWriter();
//		 Map<String, Object> response_map = new HashMap<String, Object>();
//		 
//		 //Will receive the quizName as an input
//		String quizName = (String)request.getParameter(QUIZ_NAME);
//		
//		//Only proceed to remove a quiz who's name exists 
//		if (QuizManager.quizNameInUse(quizName)) {
//			Quiz qz_to_remove = QuizManager.getQuiz(quizName);
//			QuizManager.removeQuiz(qz_to_remove);
//			Util.addStatus(true, "", response_map);
//		} else {
//			Util.addStatus(false, "The quiz name does not exist.", response_map);		
//		}
//		
//		//Turn the response_map into a "json" string
//		String response_str = Json.getJsonString(response_map);
//		//Give back to method which invoked this servlet
//		out.write(response_str);
	}

}
