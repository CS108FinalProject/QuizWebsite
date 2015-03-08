
package com.accounts.pages;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.accounts.AccountManager;

import java.util.*;

import com.accounts.*;
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
		String acct_to_promote = (String)request.getParameter("promote_acct");
		/*Checks for empty string/null parameters
		 * for all the cases of the admin servlet's purposes.
		 */
		ArrayList<String> announcements = (ArrayList<String>)getServletContext().getAttribute("announcements");
		if (new_ancmnt != null) {
			announcements.add(new_ancmnt);
		} else if (acct_to_remove != null && !acct_to_remove.equals("")) {
			 if (AccountManager.accountExists(acct_to_remove)) {
					AccountManager.removeAccount(acct_to_remove);
					request.setAttribute("remove_acct", acct_to_remove);
			 } else {
					request.setAttribute("remove_acct", null);
			 }
		} else if (acct_to_promote != null && !acct_to_promote.equals("")) {
			if (AccountManager.accountExists(acct_to_promote)) {
				Account to_prom = AccountManager.getAccount(acct_to_promote);
				to_prom.setAdmin(true);
				request.setAttribute("promote_acct", acct_to_promote);

			} else {
				request.setAttribute("promote_acct", null);
			}
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
