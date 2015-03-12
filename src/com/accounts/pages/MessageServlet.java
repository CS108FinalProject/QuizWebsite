package com.accounts.pages;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.accounts.Account;
import com.accounts.AccountManager;
import com.accounts.Message;
import com.util.Constants;

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
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String msg_type = request.getParameter("message_button");
		request.setAttribute("username", request.getParameter("recipient"));
		String recipient = request.getParameter("recipient");
		String sender = request.getParameter("sender");
		
		Account receiver_account = AccountManager.getAccount(recipient);
		Account sender_account = AccountManager.getAccount(sender);
		
		// read message
		List<Message> messages = receiver_account.getReceivedMessages();
		for (Message msg : messages) {
			if (msg.getSender().equals(sender) && msg.getType().equals(Constants.MESSAGE_FRIEND_REQUEST)) {
				receiver_account.readMessage(msg);
			}
		}
		
		// case of friend request confirmation
		if (msg_type.equals("Confirm")) {
			try {
				receiver_account.addFriend(sender_account);
			} catch (RuntimeException e) {
				return;
			}
			RequestDispatcher dispatch = request.getRequestDispatcher("showMessage.jsp");
			dispatch.forward(request, response);
			
		// case of friend request decline
		} else if (msg_type.equals("Decline")) {
			// do nothing
			RequestDispatcher dispatch = request.getRequestDispatcher("showMessage.jsp");
			dispatch.forward(request, response);
		
		// case of read note 
		} else if (msg_type.equals("Read Message")) {
			request.setAttribute("sender", request.getParameter("sender"));
			request.setAttribute("message_content", request.getParameter("message_content"));
			RequestDispatcher dispatch = request.getRequestDispatcher("showNote.jsp");
			dispatch.forward(request, response);
		}
	}

}
