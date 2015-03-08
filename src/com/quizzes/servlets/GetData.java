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
 * Servlet implementation class GetData
 */
@WebServlet("/GetData")
public class GetData extends HttpServlet implements Constants {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetData() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean success = true;
		String errorMessage = "";
		Map<String, Object> result = null;
		
		try {
			// Get JSON string and build a Map from it.
			String jsonString = (String)request.getParameter(JSON);
			Util.validateString(jsonString);
			
			Map<String, Object> jsonObject = Json.parseJsonObject(jsonString);
			Util.validateObject(jsonObject);
			
			// Get request type and name.
			Map<String, Object> requestMap = (Map<String, Object>) jsonObject.get(REQUEST);
			Util.validateObject(requestMap);
			
			// Validate type for requestMap
			if (!(requestMap instanceof Map<?, ?>)) {
				throw new RuntimeException("For " + requestMap + " expecting type Map but got " 
						+ requestMap.getClass());
			}
			
			String requestType = (String) requestMap.get(TYPE);
			Util.validateString(requestType);
			String objectName = (String) requestMap.get(NAME);
			Util.validateString(objectName);
			
			// Determine request type.
			
			// Quiz.
			if (requestType.equals(QUIZ)) {
				Quiz quiz = QuizManager.getQuiz(objectName);
				result = quiz.toMap();
			}
			
		
		} catch (Exception e) {
			success = false;
			errorMessage = e.getMessage();
			if (errorMessage == null) errorMessage = "No message provided";
		}
		
		if (result == null) result = new HashMap<String, Object>();
		Util.addStatus(success, errorMessage, result);
		
		// Setup and send response to client.
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.write(Json.getJsonString(result));
	}

}
