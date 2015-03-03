package com.accounts.pages;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
 * Servlet implementation class addFriendServlet
 */
@WebServlet("/addFriendServlet")
public class SendMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendMessageServlet() {
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
		// get date
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String date = timeStamp.toString();
		
		// get accounts
		String sender_name = (String)request.getParameter("id");
		System.out.println(sender_name);
		String friend_name = (String)request.getParameter("friend_id");
		Account sender = AccountManager.getAccount(sender_name);
		
		// create and send message according to message type
		if (request.getParameter("message_type").equals("Add Friend")) {
			Message msg = new Message(sender_name, friend_name, "", Constants.MESSAGE_FRIEND_REQUEST, date, false);
			sender.sendMessage(msg);
		} else if (request.getParameter("message_type").equals("Challenge")) {
		
		} else if (request.getParameter("message_type").equals("Send Note")) {
			Message msg = new Message(sender_name, friend_name, request.getParameter("msg_content"), Constants.MESSAGE_NOTE, date, false);
			sender.sendMessage(msg);
		}
	}

}
