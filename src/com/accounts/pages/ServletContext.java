package com.accounts.pages;
import java.security.NoSuchAlgorithmException;

import com.accounts.*;
import com.dbinterface.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.*;

/**
 * Application Lifecycle Listener implementation class servletContext
 *
 */
@WebListener
public class ServletContext implements ServletContextListener {

	

    /**
     * Default constructor. 
     */
    public ServletContext() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
    	AccountManager accounts = new AccountManager();
    	arg0.getServletContext().setAttribute("accounts", accounts);;

    	/*
    	try {
			Account administrator = AccountManager.createAccount("admin", "admin");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	*/
    	//Want the admin accounts , so that I can call admin.getMessages()
    	ArrayList<String> admin_anmts = new ArrayList<String>();
    	admin_anmts.add("Test1");
    	admin_anmts.add("Test2");
    	arg0.getServletContext().setAttribute("Received Messages",admin_anmts);
    	ArrayList<String> announcements = new ArrayList<String>();
    	arg0.getServletContext().setAttribute("announcements",announcements);

    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }
	
}
