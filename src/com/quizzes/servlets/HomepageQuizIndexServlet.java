package com.quizzes.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.accounts.AccountManager;
import com.quizzes.Quiz;
import com.quizzes.QuizManager;

/**
 * Servlet implementation class HomepageQuizIndexServlet
 */
@WebServlet("/HomepageQuizIndexServlet")
public class HomepageQuizIndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomepageQuizIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");	

		String type_to_display = (String)request.getParameter("type_to_display");
		String username = (String)getServletContext().getAttribute("session_user");
		int num_records = 5;
		if (type_to_display.equals("achievements")) {
	    	List<String> arg1 =  (ArrayList<String>)getServletContext().getAttribute("Received Messages");

			request.setAttribute("content_to_display", arg1);
			
		} else if (type_to_display.equals("friendsAchievements")) {
			
		} else if (type_to_display.equals("createdQuizzes")) {
			try {
				List<Quiz> usr_quizzes = QuizManager.getQuizzes(AccountManager.getAccount(username));
				request.setAttribute("content_to_display",usr_quizzes);
			} catch (Exception e) {
				request.setAttribute("errMsg", "<h1> The current user, "+username+" could not be found.  </h1>");

			}

		} else if  (type_to_display.equals("popularQuizzes")) {
			
			List<Quiz> cpy_popular_quizzes =  QuizManager.getMostPopularQuizzes(num_records);
			System.out.println(cpy_popular_quizzes.size());
			List<Quiz> popular_quizzes =  new ArrayList<Quiz>();

			//Put popular quizzes in correct order
			int pop_len = num_records;
			for (int i = 0 ; i < pop_len;i++) {
				popular_quizzes.add(i, cpy_popular_quizzes.get(pop_len - i));
			}
			request.setAttribute("content_to_display",popular_quizzes);
		} else if ( type_to_display.equals("recentQuizzes")) {
			List<Quiz> recent_quizzes =  QuizManager.getRecentlyCreatedQuizzes(num_records);
			request.setAttribute("content_to_display",recent_quizzes);
		} else if (type_to_display.equals("myHistory")) {
			
		} else {
			request.setAttribute("errMsg", "<h1> Sorry we could not process your request at this time</h1>");
		}
		
		RequestDispatcher dispatch = request.getRequestDispatcher("homepage.jsp"); 
		dispatch.forward(request, response);	

	}

}
