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
import com.util.*;

@WebServlet("/EditQuiz")
public class EditQuiz extends HttpServlet implements com.util.Constants {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditQuiz() {
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
		
		String requestType = (String) request.getParameter(REQUEST);
		Util.validateString(requestType);
		
		String quizName = (String) request.getParameter(QUIZ_NAME);
		Util.validateString(quizName);
		Quiz quiz = QuizManager.getQuiz(quizName);
		
		if (requestType.equals(SAVE_CHANGES)) {
			String modQuizName = request.getParameter(MODIFIED_QUIZ_NAME);			
			String description = request.getParameter(DESCRIPTION);
			
			String isRandomString = request.getParameter(IS_RANDOM);
			boolean isRandom = (isRandomString != null);
			
			String isOnePageString = request.getParameter(IS_ONE_PAGE);
			boolean isOnePage = (isOnePageString != null);
			
			String isImmediateString = request.getParameter(IS_IMMEDIATE);
			boolean isImmediate = (isImmediateString != null);
			
			if (description != null && !description.equals(quiz.getDescription())) {
				quiz.setDescription(description);
			}
			
			if (isRandom != quiz.isRandom()) {
				quiz.setRandom(isRandom);
			}
			
			if (isOnePage != quiz.isOnePage()) {
				quiz.setOnePage(isOnePage);
			}
			
			if (isImmediate != quiz.isImmediate()) {
				quiz.setImmediate(isImmediate);
			}
			
			if (modQuizName != null && !modQuizName.equals(quiz.getName())) { 
				// Quiz name was changed and is already taken.
				if (QuizManager.quizNameInUse(modQuizName)) {
					request.setAttribute(ERROR_MESSAGE, "The selected quiz name is already in use.");
					RequestDispatcher dispatch = request.getRequestDispatcher("editQuiz.jsp"); 
					dispatch.forward(request, response);
				
				} else {
					quiz.setName(modQuizName);
				}
			}
			
			request.setAttribute(QUIZ_NAME, modQuizName);
			RequestDispatcher dispatch = request.getRequestDispatcher("editQuiz.jsp"); 
			dispatch.forward(request, response);
			
		} else if (requestType.equals(REMOVE_QUIZ)) {
			quiz.removeQuiz();
			RequestDispatcher dispatch = request.getRequestDispatcher("homepage.jsp"); 
			dispatch.forward(request, response);
			
		} else if (requestType.equals(REMOVE_QUESTIONS)) {
			String modQuizName = request.getParameter(MODIFIED_QUIZ_NAME);
			request.setAttribute(QUIZ_NAME, modQuizName);
			request.setAttribute(QUIZ_NAME, quizName);
			RequestDispatcher dispatch = request.getRequestDispatcher("removeQuestions.jsp"); 
			dispatch.forward(request, response);
			
		} else if (requestType.equals("Return to Homepage")) {
			RequestDispatcher dispatch = request.getRequestDispatcher("homepage.jsp"); 	
			dispatch.forward(request, response);
			
		} else {
			throw new IllegalArgumentException("Cannot recognize request type " + requestType);
		}
		
	}
}
