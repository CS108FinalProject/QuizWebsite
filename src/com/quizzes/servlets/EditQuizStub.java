package com.quizzes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quizzes.Quiz;
import com.quizzes.QuizManager;
import com.util.Constants;
import com.util.Json;
import com.util.Util;

/**
 * Servlet implementation class EditQuizStub
 */
@WebServlet("/EditQuizStub")
public class EditQuizStub extends HttpServlet implements Constants {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditQuizStub() {
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
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean success = true;
		String errorMessage = "";
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			// Get original quiz name.
			String originalQuizName = (String)request.getParameter(ORIGINAL_QUIZ_NAME);
			Util.validateString(originalQuizName);
			
			// Get original Quiz (makes sure the quiz exists).
			Quiz original = QuizManager.getQuiz(originalQuizName);
			
			// Get quiz json and build map.
			String modifiedQuizJson = (String)request.getParameter(MODIFIED_QUIZ_JSON);
			Map<String, Object> jsonObject = Json.parseJsonObject(modifiedQuizJson);
			Util.validateObject(jsonObject);
			
			// Get modified quiz name.
			Map<String, Object> modifiedQuiz = (Map<String, Object>)jsonObject.get(QUIZ_METADATA);
			String modifiedQuizName = (String) modifiedQuiz.get(QUIZ_NAME);
			
			// Ensure modified name not already in use.
			if (!originalQuizName.equals(modifiedQuizName) 
					&& QuizManager.quizNameInUse(modifiedQuizName)) {
				
				throw new IllegalArgumentException(modifiedQuizName + " is already in use.");
			}
			
			// Remove original quiz and create modified one.
			original.removeQuiz();
			QuizManager.createQuiz(jsonObject);
			
			
		} catch (Exception e) {
			success = false;
			errorMessage = e.getMessage();
			if (errorMessage == null) errorMessage = "No message provided";
		}
		
		// Setup and send response to client.
		Util.addStatus(success, errorMessage, result);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.write(Json.getJsonString(result));
	}
}
