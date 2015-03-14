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
		
	}

}
