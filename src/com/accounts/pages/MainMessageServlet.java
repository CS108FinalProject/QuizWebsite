package com.accounts.pages;
import com.accounts.*;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class MainMessageServlet
 */
@WebServlet("/MainMessageServlet")
public class MainMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainMessageServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String friend_id = (String)request.getParameter("friend_id");
		String curr_acct_id = (String)request.getParameter("id");
		//If friend not found?
		if (AccountManager.accountExists(friend_id)) {
			RequestDispatcher dispatch = request.getRequestDispatcher("SendMessageServlet"); 
			dispatch.forward(request, response);
		} else {
			request.setAttribute("errMsg", "Friend not found.");
			RequestDispatcher dispatch = request.getRequestDispatcher("homepage.jsp?id="+curr_acct_id); 
			dispatch.forward(request, response);
		}
				
	}

}
