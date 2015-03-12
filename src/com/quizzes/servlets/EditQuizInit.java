package com.quizzes.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.Constants;
import com.util.Util;

/**
 * Servlet implementation class EditQuizInit
 */
@WebServlet("/EditQuizInit")
public class EditQuizInit extends HttpServlet implements Constants {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditQuizInit() {
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
		response.setContentType("text/html; charset=UTF-8");
		
		// get quiz Json
		String quizName = (String)request.getParameter("quiz");
		Util.validateString(quizName);
		
		request.setAttribute(QUIZ_NAME, quizName);
		RequestDispatcher dispatch = request.getRequestDispatcher("editQuiz.jsp"); 
		dispatch.forward(request, response);
	}
}
