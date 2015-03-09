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

import com.accounts.Account;
import com.accounts.AccountManager;
import com.quizzes.Achievement;
import com.quizzes.History;
import com.quizzes.Quiz;
import com.quizzes.QuizManager;
import com.quizzes.Record;
import com.util.Constants;
import com.util.Json;
import com.util.Util;

/**
 * Servlet implementation class AddRecord
 */
@WebServlet("/AddRecord")
public class AddRecord extends HttpServlet implements Constants {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddRecord() {
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
		boolean success = true;
		String errorMessage = "";
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			// Get JSON string and build a Map from it.
			String jsonString = (String)request.getParameter(JSON);
			Util.validateString(jsonString);
			
			Map<String, Object> jsonObject = Json.parseJsonObject(jsonString);
			Util.validateObject(jsonObject);
			
			String quizName = (String) jsonObject.get(QUIZ_NAME);
			Util.validateString(quizName);
			
			String username = (String) jsonObject.get(USERNAME);
			Util.validateString(username);
			Account userName = AccountManager.getAccount(username);
			
			double score = (Double) jsonObject.get(SCORE);
			String date = (String) jsonObject.get(DATE);
			Util.validateString(date);
			
			double elapsedTime = (Double) jsonObject.get(ELAPSED_TIME);
			
			Record record = new Record(quizName, userName, score, date, elapsedTime);
			
			// update achievements table
			Quiz quiz = QuizManager.getQuiz(quizName);
			if (score > quiz.getTopPerformers(1).get(0).getScore()) {
				Achievement.add(jsonObject,I_AM_THE_GREATEST);
			}
			if (userName.getNumQuizzesTaken() == 10) {
				Achievement.add(jsonObject,QUIZ_MACHINE);
			}
			
			History.addRecord(record);
			
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
