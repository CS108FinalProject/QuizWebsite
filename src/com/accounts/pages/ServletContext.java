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
    	ArrayList<String> messages = new ArrayList<String>();
    	messages.add("Test1");
    	messages.add("Test2");
    	arg0.getServletContext().setAttribute("Received Messages",messages);
    	ArrayList<String> announcements = new ArrayList<String>();
    	announcements.add("I love the Coco");
    	announcements.add("Heath is beautiful");
    	announcements.add("Bands will make her dance");
    	announcements.add("I ain't got no type");
    	announcements.add("A milli, A milli, A milli");
    	announcements.add("If it ain't broke don't fix it");
    	arg0.getServletContext().setAttribute("announcements",announcements);
    	String session_user = "Kelsey";
    	arg0.getServletContext().setAttribute("session_user", session_user);

    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }
	
}
