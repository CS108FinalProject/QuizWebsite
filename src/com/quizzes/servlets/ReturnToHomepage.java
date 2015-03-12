package com.quizzes.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.accounts.AccountManager;

/**
 * Servlet implementation class ReturnToHomepage
 */
@WebServlet("/ReturnToHomepage")
public class ReturnToHomepage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReturnToHomepage() {
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
		String username = (String)getServletContext().getAttribute("session_user");
		RequestDispatcher dispatch = null;
		if ((AccountManager.getAccount(username)).isAdmin()) {
			dispatch = request.getRequestDispatcher("adminHomepage.jsp"); 
		} else dispatch = request.getRequestDispatcher("homepage.jsp"); 	
		dispatch.forward(request, response);
	}

}
