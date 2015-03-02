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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");	

		AccountManager accounts = (AccountManager)getServletContext().getAttribute("accounts");
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (AccountManager.accountExists(username)) {
			try {
				if(AccountManager.passwordMatches(username, password)) {
					if (AccountManager.getAccount(username).isAdmin()) {
						RequestDispatcher dispatch = request.getRequestDispatcher("adminHomepage.jsp");
						dispatch.forward(request, response);
					} else {
						RequestDispatcher dispatch = request.getRequestDispatcher("homepage.jsp");
						dispatch.forward(request, response);
					}
				}
			} catch (NoSuchAlgorithmException e) {			
			}
			return;
		}
		RequestDispatcher dispatch = request.getRequestDispatcher("reLogin.jsp"); 
		dispatch.forward(request, response);	
	}

}
