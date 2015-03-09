package com.quizzes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quizzes.Achievement;
import com.quizzes.Quiz;
import com.quizzes.QuizManager;
import com.quizzes.Record;
import com.util.Constants;
import com.util.Json;
import com.util.Util;

/**
 * Servlet implementation class CreateQuiz
 */
@WebServlet("/CreateQuiz")
public class CreateQuiz extends HttpServlet implements Constants {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateQuiz() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	/**
	 * Performs server-side operations to create a new Quiz.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean success = true;
		String errorMessage = "";
		Map<String, Object> result = new HashMap<String, Object>();
		
		// TODO: uncomment try catch block.
		
	//	try {
			// get quiz Json
			String dataString = (String)request.getParameter(JSON);
			System.out.println(dataString);
			Map<String, Object> dataMap = Json.parseJsonObject(dataString);
			
			// create quiz
			Quiz quiz = QuizManager.createQuiz(dataMap);
			
			// updates achievements table
			Record record = new Record(quiz.getName(), quiz.getCreator(), 0, quiz.getCreationDate(), 0);
			List<Quiz> quizzes = QuizManager.getQuizzes(quiz.getCreator());
			if (quizzes.size() == 1) {
				Achievement.add(record,AMATEUR_AUTHOR);
			} else if (quizzes.size() == 5) {
				Achievement.add(record,PROLIFIC_AUTHOR);
			} else if (quizzes.size() == 10) {
				Achievement.add(record,PRODIGIOUS_AUTHOR);
			}
			
			
		// Catch all possible errors.
//		} catch (Exception e) {
//			success = false;
//			errorMessage = e.getMessage();
//			if (errorMessage == null) errorMessage = "No message provided";
//			System.out.println(errorMessage);
//		}
		
		// Setup and send response to client.
		Util.addStatus(success, errorMessage, result);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.write(Json.getJsonString(result));
	}

}
