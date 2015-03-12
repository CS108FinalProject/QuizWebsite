package com.accounts.pages;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.*;
import com.accounts.*;
/**
 * Application Lifecycle Listener implementation class servletContext
 *
 */
@WebListener
public class ServletContext implements ServletContextListener {

	

    /**
     * Default constructor. 
     */
    public ServletContext() {}

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 

    	/*
    	try {
			Account administrator = AccountManager.createAccount("admin", "admin");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
    	*/
    	//Want the admin accounts , so that I can call admin.getMessages()

    

    	arg0.getServletContext().setAttribute("session_user", "");

    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  {}
	
}
