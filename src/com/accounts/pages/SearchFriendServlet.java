package com.accounts.pages;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.accounts.AccountManager;

/**
 * Servlet implementation class SearchFriendServlet
 */
@WebServlet("/SearchFriendServlet")
public class SearchFriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchFriendServlet() {
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
		String friend_id = request.getParameter("friend_id");
		String sender = (String) getServletContext().getAttribute("session_user");
//		request.setAttribute("sender", sender);
		System.out.println("Recieved Eliezer?: " + sender);
		
		// Update request according to account existence 
		if (AccountManager.accountExists(friend_id)) {
			request.setAttribute("account", friend_id);
		} else request.setAttribute("account", null);
		
		// forward request back to searchFriends.jsp
		RequestDispatcher dispatch = request.getRequestDispatcher("searchFriends.jsp?id=" + sender);
		dispatch.forward(request, response);
	}

}
