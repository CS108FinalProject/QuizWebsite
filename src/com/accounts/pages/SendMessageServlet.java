package com.accounts.pages;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.quizzes.QuizManager;
import com.util.Constants;

/**
 * Servlet implementation class addFriendServlet
 */
@WebServlet("/SendMessageServlet")
public class SendMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendMessageServlet() {
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
		// get date
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime());
		String date = timeStamp.toString();
		
		// get accounts
		String sender_name = (String)request.getParameter("id");
		String friend_name = (String)request.getParameter("friend_id");
		Account sender = AccountManager.getAccount(sender_name);
		Account recipient = AccountManager.getAccount(friend_name);
		
		//get content
		String msg_content = request.getParameter("msg_content");

		// create and send message according to message type
		if (request.getParameter("message_type").equals("Send Friend Request")) {
			if ((!sender.friendshipPending(recipient)) && (!sender.isFriend(recipient)) && (!recipient.isFriend(sender))) {
				Message msg = new Message(sender_name, friend_name, "friend_request", Constants.MESSAGE_FRIEND_REQUEST, date, false);
				sender.sendMessage(msg);
				try {
					sender.addFriend(recipient);
				} catch (RuntimeException e) {
					request.setAttribute("friendship_error", e.getMessage());
				}
			}
		} else if (request.getParameter("message_type").equals("Unfriend")) {
			if (sender.isFriend(recipient) || recipient.isFriend(sender)) {
				try {
					sender.unfriend(recipient);
				} catch (RuntimeException e) {
					request.setAttribute("friendship_error", e.getMessage()); 
				}
				// remove message
				List<Message> messages1 = sender.getReceivedMessages();
				for (Message msg : messages1) {
					if (msg.getSender().equals(friend_name) && msg.getType().equals(Constants.MESSAGE_FRIEND_REQUEST)) {
						sender.removeMessage(msg);
					}
				}
				List<Message> messages2 = recipient.getReceivedMessages();
				for (Message msg : messages2) {
					if (msg.getSender().equals(sender_name) && msg.getType().equals(Constants.MESSAGE_FRIEND_REQUEST)) {
						recipient.removeMessage(msg);
					}
				}
			}	
		} else if (request.getParameter("message_type").equals("Challenge")) {
			String quizName = request.getParameter("quizName");
			if (quizName == null || quizName.isEmpty()) {
				request.setAttribute("errMsg", "Please enter a non-empty quiz.");
			} else if (QuizManager.quizNameInUse(quizName)) {
				Message msg = new Message(sender_name, friend_name, quizName, Constants.MESSAGE_CHALLENGE, date, false);
				sender.sendMessage(msg);
			}
		} else if (request.getParameter("message_type").equals("Send Note") && !msg_content.equals("")) {
			Message msg = new Message(sender_name, friend_name,msg_content , Constants.MESSAGE_NOTE, date, false);
			sender.sendMessage(msg);
		} else if (msg_content.equals("")) {
			request.setAttribute("errMsg", "Please enter a non-empty message.");
		}
		
		request.setAttribute("friend_id", friend_name);
		RequestDispatcher dispatch = request.getRequestDispatcher("accountProfile.jsp");
		dispatch.forward(request, response);
	}

}
