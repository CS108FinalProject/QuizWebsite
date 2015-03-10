package com.quizzes.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.accounts.AccountManager;
import com.quizzes.Quiz;
import com.quizzes.QuizManager;
import com.util.*;

import java.util.*;
/**
 * Servlet implementation class EditQuiz
 * Author: Kelsey Young Stanford University '15
 */
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
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 response.setContentType("application/json");
		 //TODO:Find a way to test this

		//Pull old_quiz_data
		String old_quiz_data = (String)request.getParameter("old_quiz_json");
		Map<String, Object> map_old_quiz = Json.parseJsonObject(old_quiz_data);
		Map<String,Object> old_quiz_metadata = (Map<String,Object>)map_old_quiz.get("quizMetadata");
		String old_quiz_name = (String)old_quiz_metadata.get("name");
		
		
		//Pull new_quiz_data
		String new_quiz_data = (String)request.getParameter("new_quiz_json");
		Map<String, Object> map_new_quiz = Json.parseJsonObject(new_quiz_data);
		Map<String, Object> new_quiz_metadata = (Map<String, Object>)map_new_quiz.get("quizMetadata");
		String new_quiz_name = (String)new_quiz_metadata.get("name");
		
		//Initiate Response Data
		Map<String,Object> response_map = new HashMap<String, Object>();
		String response_str = "";
		PrintWriter out = response.getWriter();
		
		String old_quiz_creator = (String)old_quiz_metadata.get("creator");
		String new_quiz_creator = (String)new_quiz_metadata.get("creator");
		

		
		//If a user other than the creator tries to edit the quiz, deny edit
		if (!old_quiz_creator.equals(new_quiz_creator)) {
			Util.addStatus(false, "You may only edit quizzes that you created", response_map);
			response_str = Json.getJsonString(response_map);
			out.print(response_str);
			return;
		}	
		
		//Ensure account exists with that  user.
		if (!AccountManager.accountExists(new_quiz_creator)) {
			Util.addStatus(false,"There was no account found named, " + new_quiz_creator,response_map);
			response_str = Json.getJsonString(response_map);
			out.print(response_str);
			return;
		}		
		
		//TODO: Ask TA whether editQuiz is a requirement(as on page 7) or extension (as on page 9)		
		//Make sure new quiz name is not already taken.
		if (QuizManager.quizNameInUse(new_quiz_creator) && !(new_quiz_name.equals(old_quiz_name))) {
			Util.addStatus(false, "Sorry, you are trying to change the quizname to a name that already exists.", response_map);
			response_str = Json.getJsonString(response_map);
			out.print(response_str);
			return;
			//Deny if the quiz to remove(old quiz) doesn't exist
		} else if (!QuizManager.quizNameInUse(old_quiz_name)) {
			Util.addStatus(false, "You are trying to edit a quiz that doesn't exist.", response_map);
			response_str = Json.getJsonString(response_map);
			out.print(response_str);
			return;
			//Remove the old quiz if all else passes.
		} else {
			Quiz old_quiz = QuizManager.getQuiz(old_quiz_name);
			old_quiz.removeQuiz();	
		}
	
			

			//Only create quiz if we get to this check point
		try{
			QuizManager.createQuiz(map_new_quiz);
			Util.addStatus(true, "", response_map);
			response_str = Json.getJsonString(response_map);
			out.print(response_str);
		} catch (Exception e ) {
			// Display error and include Exception type on a failure.
			Util.addStatus(false, e.getMessage(), response_map);
			response_str = Json.getJsonString(response_map);
			out.print(response_str);
			return;
		}
	}
}
