package com.accounts.pages;
import com.accounts.*;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Parameter;

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
		String message_type = (String)request.getParameter("msg_type");
		String friend_id = (String)request.getParameter("friend_id");
		String curr_acct_id = (String)request.getParameter("id");
		Account curr_acct = AccountManager.getAccount(curr_acct_id);
		//If friend not found?
		if (AccountManager.accountExists(friend_id)) {
			RequestDispatcher dispatch = request.getRequestDispatcher("SendMessageServlet"); 
			dispatch.forward(request, response);
		} else {
			request.setAttribute("errMsg", "Friend not found.");
			/*
			if (curr_acct.isAdmin()) {
				RequestDispatcher dispatch = request.getRequestDispatcher("adminHomepage.jsp?id="+curr_acct_id); 
				dispatch.forward(request, response);
			} else {
				RequestDispatcher dispatch = request.getRequestDispatcher("homepage.jsp?id="+curr_acct_id); 
				dispatch.forward(request, response);
			}
			*/
			RequestDispatcher dispatch = request.getRequestDispatcher("adminHomepage.jsp?id="+curr_acct_id); 
			dispatch.forward(request, response);
		}
				
	}

}
