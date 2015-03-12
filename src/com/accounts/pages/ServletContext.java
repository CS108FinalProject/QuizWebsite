package com.accounts.pages;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import java.util.*;

import com.accounts.*;
import com.dbinterface.Database;
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
    	arg0.getServletContext().setAttribute("session_user", "");
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  {
    }
	
}
