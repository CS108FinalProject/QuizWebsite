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
		
		if (type_to_display.equals("achievements")) {
	    	List<String> arg1 =  (ArrayList<String>)getServletContext().getAttribute("Received Messages");

			request.setAttribute("content_to_display", arg1);
			
		} else if (type_to_display.equals("friendsAchievements")) {
			
		} else if (type_to_display.equals("createdQuizzes")) {
			
		} else if  (type_to_display.equals("popularQuizzes")) {
			
		} else if ( type_to_display.equals("recentQuizzes")) {
			
		} else if (type_to_display.equals("myHistory")) {
			
		} else {
			request.setAttribute("errMsg", "<h1> Sorry we could not process your request at this time</h1>");
		}
		
		RequestDispatcher dispatch = request.getRequestDispatcher("homepage.jsp"); 
		dispatch.forward(request, response);	

	}

}
