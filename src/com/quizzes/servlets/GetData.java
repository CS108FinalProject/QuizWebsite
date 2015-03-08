package com.quizzes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.accounts.Account;
import com.accounts.AccountManager;
import com.quizzes.Quiz;
import com.quizzes.QuizManager;
import com.quizzes.Record;
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
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			// Get JSON string and build a Map from it.
			String jsonString = (String)request.getParameter(JSON);
			Util.validateString(jsonString);
			
			Map<String, Object> jsonObject = Json.parseJsonObject(jsonString);
			Util.validateObject(jsonObject);
			
			// Get and validate type for requestMap
			if (!(jsonObject.get(REQUEST) instanceof Map<?, ?>)) {
				throw new RuntimeException("Expecting type Map but got " 
						+ jsonObject.get(REQUEST).getClass());
			}
			
			Map<String, Object> requestMap = (Map<String, Object>) jsonObject.get(REQUEST);
			Util.validateObject(requestMap);
			
			// Get request type
			String requestType = (String) requestMap.get(TYPE);
			Util.validateString(requestType);
			
			// Determine request type.
			
			
			// Quiz.
			if (requestType.equals(QUIZ)) {
				String quizName = (String) requestMap.get(QUIZ_NAME);
				Util.validateString(quizName);
				
				Quiz quiz = QuizManager.getQuiz(quizName);
				result.put(DATA, quiz.toMap());
				
				
			// All Quizzes.
			} else if (requestType.equals(ALL_QUIZZES)) {
				List<Quiz> allQuizzes = QuizManager.getAllQuizzes();
				List<Object> resultList = new ArrayList<Object>();
				for (Quiz quiz : allQuizzes) {
					resultList.add(quiz.toMap());
				}
				result.put(DATA, resultList);
				
				
			// All Quizzes of Creator
			} else if (requestType.equals(ALL_CREATOR_QUIZZES)) {
				String creatorName = (String) requestMap.get(CREATOR);
				Util.validateString(creatorName);
				
				Account creator = AccountManager.getAccount(creatorName);
				List<Quiz> creatorQuizzes = QuizManager.getQuizzes(creator);
				List<Object> resultList = new ArrayList<Object>();
				for (Quiz quiz : creatorQuizzes) {
					resultList.add(quiz.toMap());
				}
				result.put(DATA, resultList);
				
				
			// Most popular quizzes overall
			} else if (requestType.equals(MOST_POPULAR_QUIZZES)) {
				// Get number of records requested.
				int numRecords = (Integer) requestMap.get(NUM_RECORDS);
				
				List<Quiz> popular = QuizManager.getMostPopularQuizzes(numRecords);
				List<Object> resultList = new ArrayList<Object>();
				for (Quiz quiz : popular) {
					resultList.add(quiz.toMap());
				}
				result.put(DATA, resultList);
				
				
			// Recently created Quizzes	
			} else if (requestType.equals(RECENTLY_CREATED_QUIZZES)) {
				// Get number of records requested.
				int numRecords = (Integer) requestMap.get(NUM_RECORDS);
				
				List<Quiz> recent = QuizManager.getRecentlyCreatedQuizzes(numRecords);
				List<Object> resultList = new ArrayList<Object>();
				for (Quiz quiz : recent) {
					resultList.add(quiz.toMap());
				}
				result.put(DATA, resultList);
				
				
			// Past User Performance
			} else if (requestType.equals(PAST_USER_PERFORMANCE)) {
				// Get Quiz.
				String quizName = (String) requestMap.get(QUIZ_NAME);
				Util.validateString(quizName);
				Quiz quiz = QuizManager.getQuiz(quizName);
				
				//Get User.
				String userName = (String) requestMap.get(USERNAME);
				Util.validateString(userName);
				Account user = AccountManager.getAccount(userName);
				
				// Get number of records requested.
				int numRecords = (Integer) requestMap.get(NUM_RECORDS);
				
				List<Record> records = quiz.getPastUserPerformance(user, numRecords);
				List<Object> recordResultList = new ArrayList<Object>();
				
				// Get result.
				for (Record record : records) {
					recordResultList.add(record.toMap());
				}
				result.put(DATA, recordResultList);
				
				
			// Top performers of all time
			} else if (requestType.equals(TOP_PERFORMERS)) {
				// Get Quiz.
				String quizName = (String) requestMap.get(QUIZ_NAME);
				Util.validateString(quizName);
				Quiz quiz = QuizManager.getQuiz(quizName);
				
				// Get number of records requested.
				int numRecords = (Integer) requestMap.get(NUM_RECORDS);
				
				List<Record> highest = quiz.getTopPerformers(numRecords);
				List<Object> recordResultList = new ArrayList<Object>();
				for (Record record : highest) {
					recordResultList.add(record.toMap());
				}
				result.put(DATA, recordResultList);
				
				
			// Top performers within time period	
			} else if (requestType.equals(TOP_PERFORMERS_WITHIN_TIME_PERIOD)) {
				// Get Quiz.
				String quizName = (String) requestMap.get(QUIZ_NAME);
				Util.validateString(quizName);
				Quiz quiz = QuizManager.getQuiz(quizName);
				
				// Get number of records requested.
				Double hours = (Double) requestMap.get(HOURS);
				
				List<Record> highest = quiz.topScorersWithinTimePeriod(hours);
				List<Object> recordResultList = new ArrayList<Object>();
				for (Record record : highest) {
					recordResultList.add(record.toMap());
				}
				result.put(DATA, recordResultList);
				
			
			// Recent test takers
			} else if (requestType.equals(RECENT_PERFORMANCE)) {
				// Get Quiz.
				String quizName = (String) requestMap.get(QUIZ_NAME);
				Util.validateString(quizName);
				Quiz quiz = QuizManager.getQuiz(quizName);
				
				// Get number of records requested.
				int numRecords = (Integer) requestMap.get(NUM_RECORDS);
				
				List<Record> recent = quiz.getRecentPerformance(numRecords);
				List<Object> recordResultList = new ArrayList<Object>();
				for (Record record : recent) {
					recordResultList.add(record.toMap());
				}
				result.put(DATA, recordResultList);
				
				
			// Summary statistics
			} else if (requestType.equals(AVG_PERFORMANCE)) {
				// Get Quiz.
				String quizName = (String) requestMap.get(QUIZ_NAME);
				Util.validateString(quizName);
				Quiz quiz = QuizManager.getQuiz(quizName);
				
				Map<String, Double> avgPerformance = quiz.getAveragePerformance();
				result.put(DATA, avgPerformance);
			
				
			} else {
				throw new IllegalArgumentException("Cannot recognize request " + requestType);
			}
			
		
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
