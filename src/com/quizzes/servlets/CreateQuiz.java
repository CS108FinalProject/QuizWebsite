package com.quizzes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.accounts.Account;
import com.accounts.AccountManager;
import com.quizzes.FillBlank;
import com.quizzes.Matching;
import com.quizzes.MultiResponse;
import com.quizzes.MultipleChoice;
import com.quizzes.Picture;
import com.quizzes.Question;
import com.quizzes.Quiz;
import com.quizzes.QuizManager;
import com.quizzes.Response;
import com.util.Json;
import com.util.Util;

/**
 * Servlet implementation class CreateQuiz
 */
@WebServlet("/CreateQuiz")
public class CreateQuiz extends HttpServlet implements com.util.Constants {
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
		String dataString = (String)request.getParameter("json");
		Map<String, Object> dataMap = Json.parseJsonObject(dataString);
		
		// create quiz
		Map<String, Object> quizMetadata  =  (Map<String, Object>)dataMap.get("quizMetaData");
		Quiz quiz = QuizManager.createQuiz(quizMetadata);
//		String name = (String) quizMetadata.get("name");
//		Account creator = null;
//		if (AccountManager.accountExists((String) quizMetadata.get("creator"))) {
//			creator = AccountManager.getAccount((String) quizMetadata.get("creator")); 
//			Util.addStatus(true,"", dataMap);
//		} else {
//			Util.addStatus(false,"There was no account found named, " + name, dataMap);
//		}
//		String description =  (String) quizMetadata.get("description"); 
//		String date = (String) quizMetadata.get("date");
//		boolean isRandom = (Boolean) quizMetadata.get("isRandom");
//		boolean isOnePage = (Boolean) quizMetadata.get("isOnePage");
//		boolean isImmediate = (Boolean) quizMetadata.get("isImmediate");	
//		Quiz quiz = QuizManager.createQuiz(name,creator,description,date,isRandom,isOnePage,isImmediate);
		
		// add questions
		List<Map<String, Object>> questions = (List<Map<String, Object>>) dataMap.get("questions");
		String quizName = (String) quizMetadata.get("name");
		for (Map<String, Object> question_map : questions) {
			String type = (String) question_map.get("type");
			String question_content = (String) question_map.get("question");
			Question question = null;
			if (type.equals(FILL_BLANK)) {
				Map<String, List<String>> blanksAndAnswers = (Map<String, List<String>>) question_map.get("answers");
				question = new FillBlank(quizName, question_content, blanksAndAnswers);
			} else if (type.equals(MULTIPLE_CHOICE)) {
				Map<String, Boolean> options = (Map<String, Boolean>) question_map.get("answers");
				question = new MultipleChoice(quizName, question_content, options);				
			} else if (type.equals(PICTURE)) {
				List<String> answers = (List<String>) question_map.get("answers");
				String pictureUrl = (String) question_map.get("pictureUrl");
				question = new Picture(quizName, question_content, pictureUrl, answers);
			} else if (type.equals(MULTI_RESPONSE)) {
				TreeMap<Integer, String> answers = (TreeMap<Integer, String>) question_map.get("answers");
				boolean isOrdered = (Boolean) question_map.get("isOrdered");
				question = new MultiResponse(quizName, question_content, answers, isOrdered);
			} else if (type.equals(MATCHING)) {
				Map<String, String> matches = (Map<String, String>) question_map.get("answers");
				question = new Matching(quizName, question_content, matches);
			} else if (type.equals(RESPONSE)) {
				List<String> answers = (List<String>)question_map.get("answers");
				question = new Response(quizName, question_content, answers);
			}	
			
			quiz.addQuestion(question);
		}
		
		// turn the response_map into a Json string
		String response_str = Json.getJsonString(dataMap);
		
		// give back to method which invoked this servlet
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.write(response_str);

	}

}
