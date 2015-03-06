package com.accounts.pages;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;

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
		String recipient = request.getParameter("recipient");
		String sender = request.getParameter("sender");
		
		System.out.println("recipient: " + recipient);
		System.out.println("sender: " + sender);
		
		Account receiver_account = AccountManager.getAccount(recipient);
		Account sender_account = AccountManager.getAccount(sender);
		
		// read message
		List<Message> messages = receiver_account.getReceivedMessages();
		for (Message msg : messages) {
			if (msg.getSender().equals(sender) && msg.getType().equals(Constants.MESSAGE_FRIEND_REQUEST)) {
				receiver_account.readMessage(msg);
			}
		}
		//System.out.println("msg: " + msg);
		//receiver_account.readMessage(msg);
		
		// case of friend request confirmation
		if (msg_type.equals("Confirm")) {
			//request.setAttribute("message_read", "yes");
			try {
				receiver_account.addFriend(sender_account);
				System.out.println("approved friend");
				//request.setAttribute("friendship_status", "Friend Added!");
			} catch (RuntimeException e) {
				//request.setAttribute("friendship_error", e.getMessage());
				System.out.println("<script>");
				System.out.println("alert(\"This user is already your friend\")");
				System.out.println("</script>");
				
				return;
				//System.out.println("Exception thrown");
			}
			RequestDispatcher dispatch = request.getRequestDispatcher("showMessage.jsp");
			System.out.println("dispatch: " + dispatch);
			dispatch.forward(request, response);
			System.out.println("dispatch: after");
			
		// case of friend request decline
		} else if (msg_type.equals("Decline")) {
			// do nothing
			//request.setAttribute("friendship_status", "Friend request declined!");
			//request.setAttribute("message_read", "yes");
			RequestDispatcher dispatch = request.getRequestDispatcher("showMessage.jsp");
			dispatch.forward(request, response);
		
		// case of read note 
		} else if (msg_type.equals("Read Message")) {
			// forward to show note page
			//request.setAttribute("message_read", "yes");
			request.setAttribute("sender", request.getParameter("sender"));
			request.setAttribute("message_content", request.getParameter("message_content"));
			RequestDispatcher dispatch = request.getRequestDispatcher("showNote.jsp");
			dispatch.forward(request, response);
		}
	}

}
