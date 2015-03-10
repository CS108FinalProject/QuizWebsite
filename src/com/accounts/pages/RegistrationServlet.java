		package com.accounts.pages;
import com.accounts.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationServlet() {
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
		response.setContentType("application/json");	
		
		String username = request.getParameter("user");
		String password = request.getParameter("password");
		
		//Redirect if username not valid
		if (AccountManager.accountExists(username)) {
			RequestDispatcher dispatch = request.getRequestDispatcher("registration.jsp");
			request.setAttribute("errMsg", "<h1>Sorry, the username, "+username+", already exists. Please choose another.</h1>");
			dispatch.forward(request, response);
		} else {
			//Try to create new account with input info			
			try {
				AccountManager.createAccount(username,password);
			} catch (NoSuchAlgorithmException e) {
				//Need to redirect to site homepage(login.jsp) if our password can not be saved correctly
				request.setAttribute("errMsg", "<h1>We are currently updating our security systems. To ensure your safety, accounts can not be created at this time.</h1>");
				RequestDispatcher dispatch = request.getRequestDispatcher("login.jsp"); 
				dispatch.forward(request, response);
			}
			
			getServletContext().setAttribute("session_user",username);
			// case of new administrative account
			if (request.getParameter("isAdministrator") != null) { 
				AccountManager.getAccount(username).setAdmin(true);
				RequestDispatcher dispatch = request.getRequestDispatcher("adminHomepage.jsp?id="+username);
				dispatch.forward(request, response);
			} else {
				RequestDispatcher dispatch = request.getRequestDispatcher("homepage.jsp?id="+username);
				dispatch.forward(request, response);
			}
		}
	}

}
