package com.accounts.pages;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.accounts.Account;
import com.accounts.AccountManager;
import com.accounts.Message;

/**
 * Servlet implementation class MessageServlet
 */
@WebServlet("/MessageServlet")
public class MessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessageServlet() {
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

		String msg_type = request.getParameter("message_button");
		request.setAttribute("username", request.getParameter("recipient"));

		// case of friend request confirmation
		if (msg_type.equals("Confirm")) {
			Account receiver_account = AccountManager.getAccount(request.getParameter("recipient"));
			Account sender_account = AccountManager.getAccount(request.getParameter("sender"));
			try {
				receiver_account.addFriend(sender_account);
				request.setAttribute("isAdded", "added");
			} catch (RuntimeException e) {
				request.setAttribute("isAdded", "already_friends");
			}
			RequestDispatcher dispatch = request.getRequestDispatcher("showMessage.jsp");
			dispatch.forward(request, response);
			
		// case of friend request decline
		} else if (msg_type.equals("Decline")) {
			// do nothing
			request.setAttribute("isAdded", "declined");
			RequestDispatcher dispatch = request.getRequestDispatcher("showMessage.jsp");
			dispatch.forward(request, response);
		
		// case of read note 
		} else if (msg_type.equals("Read Message")) {
			// forward to show note page
			request.setAttribute("sender", request.getParameter("sender"));
			request.setAttribute("message_content", request.getParameter("message_content"));
			RequestDispatcher dispatch = request.getRequestDispatcher("showNote.jsp");
			dispatch.forward(request, response);
		}
	}

}
