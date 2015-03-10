package com.accounts.pages;

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
    	ArrayList<String> messages = new ArrayList<String>();
    	messages.add("Test1");
    	messages.add("Test2");
    	arg0.getServletContext().setAttribute("Received Messages",messages);
    	ArrayList<String> announcements = new ArrayList<String>();
    	/*
    	announcements.add("I love the Coco");
    	announcements.add("Heath is beautiful");
    	announcements.add("Bands will make her dance");
    	announcements.add("I ain't got no type");
    	announcements.add("A milli, A milli, A milli");
    	announcements.add("If it ain't broke don't fix it");
    	*/
    	arg0.getServletContext().setAttribute("announcements",announcements);
    	arg0.getServletContext().setAttribute("session_user", "");

    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  {}
	
}
