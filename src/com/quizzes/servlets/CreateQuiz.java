package com.quizzes.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.accounts.Account;
import com.accounts.AccountManager;
import com.quizzes.Question;
import com.quizzes.Quiz;
import com.quizzes.QuizManager;
import com.util.Json;

/**
 * Servlet implementation class CreateQuiz
 */
@WebServlet("/CreateQuiz")
public class CreateQuiz extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateQuiz() {
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
		// get quiz Json
		String quiz_json = request.getParameter("quiz");
		Map<String, Object> quiz_map = Json.parseJsonObject(quiz_json);
		
		// create quiz
		Map<String, Object> quizMetadata = (Map<String, Object>) quiz_map.get("quizMetadata");
		String name = (String) quizMetadata.get("name");
		Account creator = AccountManager.getAccount((String) quizMetadata.get("creator"));
		String description =  (String) quizMetadata.get("description");
		String date = (String) quizMetadata.get("date");
		boolean isRandom = (Boolean) quizMetadata.get("isRandom");
		boolean isOnePage = (Boolean) quizMetadata.get("isOnePage");
		boolean isImmediate = (Boolean) quizMetadata.get("isImmediate");	
		Quiz quiz = QuizManager.createQuiz(name,creator,description,date,isRandom,isOnePage,isImmediate);
		
		// add questions
		Map<String, Object>[] questions = (Map<String, Object>[]) quiz_map.get("questions");
		for (Map<String, Object> question_map : questions) {
			String type = (String) question_map.get("type");
			Question question = (Question) question_map.get("question");
			String[] answers = (String[]) question_map.get("answers");
			quiz.addQuestion(question);
		}
		
		
		// forward request
		request.setAttribute("quiz", quiz);
		//RequestDispatcher dispatch = request.getRequestDispatcher("");
		//dispatch.forward(request, response);
	}

}
