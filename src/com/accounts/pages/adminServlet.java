package com.accounts.pages;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * Servlet implementation class adminServlet
 */
@WebServlet("/adminServlet")
public class adminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public adminServlet() {
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
		String user_id = (String)request.getParameter("id");
		//Check validity
		System.out.println(user_id);
		ArrayList<String> announcements = (ArrayList<String>)getServletContext().getAttribute("announcements");
		if (new_ancmnt != null) {
			announcements.add(new_ancmnt);
			RequestDispatcher dispatch = request.getRequestDispatcher("homepage.jsp?id="+user_id);
			dispatch.forward(request, response);
		}
	}

}
