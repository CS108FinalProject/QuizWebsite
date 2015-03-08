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

import com.quizzes.*;
import com.util.*;

/**
 * Servlet implementation class RemoveQuiz
 */
@WebServlet("/RemoveQuiz")
public class RemoveQuiz extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveQuiz() {
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
		 response.setContentType("application/json");
		 PrintWriter out = response.getWriter();
		 Map<String, Object> response_map = new HashMap<String, Object>();
		 
		 //Will receive the quizName as an input
		String quizName = (String)request.getParameter("quizName");
		
		//Only proceed to remove who's name exists 
		if (QuizManager.quizNameInUse(quizName)) {
			Quiz qz_to_remove = QuizManager.getQuiz(quizName);
			QuizManager.removeQuiz(qz_to_remove);
			Util.addStatus(true, "", response_map);
			} else {
				Util.addStatus(false, "The quiz name does not exist.", response_map);		
		}
		
		//Turn the response_map into a "json" string
		String response_str = Json.getJsonString(response_map);
		//Give back to method which invoked this servlet
		out.write(response_str);
	}

}
