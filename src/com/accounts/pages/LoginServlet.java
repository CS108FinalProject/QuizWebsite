package com.accounts.pages;
import com.accounts.*;
import java.security.*;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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
		response.setContentType("text/html; charset=UTF-8");	

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (AccountManager.accountExists(username)) {
			try {
				if(AccountManager.passwordMatches(username, password)) {
					Account acct = AccountManager.getAccount(username);
					getServletContext().setAttribute("session_user",username);
					if (acct.isAdmin()) {
						RequestDispatcher dispatch = request.getRequestDispatcher("homepage.jsp"); 
						dispatch.forward(request, response);
					} else {
						RequestDispatcher dispatch = request.getRequestDispatcher("homepage.jsp"); 
						dispatch.forward(request, response);
					}
				} else {
					/*Needed for the case that the Account Exists but the Password Fails*/
					request.setAttribute("errMsg", "<h1>Sorry, the username or password was invalid.</h1>");
					RequestDispatcher dispatch = request.getRequestDispatcher("login.jsp"); 
					dispatch.forward(request, response);
				}
			} catch (NoSuchAlgorithmException e) {
				/*Needed for the case that the Password hashing can not be done.*/
				request.setAttribute("errMsg", "<h1>Sorry, the username or password was invalid.</h1>");
				RequestDispatcher dispatch = request.getRequestDispatcher("login.jsp"); 
				dispatch.forward(request, response);
			}
		} else {
			/*Needed for the case that the Account fails to exist */
			request.setAttribute("errMsg", "<h1>Sorry, the username or password was invalid.</h1>");
			RequestDispatcher dispatch = request.getRequestDispatcher("login.jsp"); 
			dispatch.forward(request, response);
		}

	}

}
