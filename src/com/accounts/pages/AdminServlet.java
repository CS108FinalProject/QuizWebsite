
package com.accounts.pages;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.accounts.AccountManager;

import java.util.*;

/**
 * Servlet implementation class adminServlet
 */
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
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
		
		String new_ancmnt =  (String)request.getParameter("new_ancmnt");
		String user_id = (String)getServletContext().getAttribute("session_user");
		String acct_to_remove = (String)request.getParameter("remove_acct");
		//Check validity
		System.out.println(user_id);
		ArrayList<String> announcements = (ArrayList<String>)getServletContext().getAttribute("announcements");
		if (new_ancmnt != null) {
			announcements.add(new_ancmnt);
			System.out.println("New Announcement recognized");
		} else if (acct_to_remove != null && acct_to_remove != "") {
			AccountManager.removeAccount(acct_to_remove);
			System.out.println("Remove Success");
		}
		//Send back to the user homepage regardless
		if (AccountManager.getAccount(user_id).isAdmin()) {
			RequestDispatcher dispatch = request.getRequestDispatcher("adminHomepage.jsp");
			dispatch.forward(request, response);
		} else {
			RequestDispatcher dispatch = request.getRequestDispatcher("homepage.jsp");
			dispatch.forward(request, response);
		}
	}

}
