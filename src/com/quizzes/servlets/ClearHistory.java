package com.quizzes.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quizzes.Quiz;
import com.quizzes.QuizManager;
import com.util.Constants;
import com.util.Util;

/**
 * Servlet implementation class ClearHistory
 */
@WebServlet("/ClearHistory")
public class ClearHistory extends HttpServlet implements Constants {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClearHistory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String quizName = (String) request.getParameter(QUIZ_NAME);
		Util.validateString(quizName);
		Quiz quiz = QuizManager.getQuiz(quizName);
		quiz.clearHistory();
		
		request.setAttribute(QUIZ_NAME, quizName);
		RequestDispatcher dispatch = request.getRequestDispatcher("quizSummary.jsp"); 
		dispatch.forward(request, response);
	}
}
