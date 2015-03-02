package com.accounts;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.dbinterface.Database;

/**
 * Application Lifecycle Listener implementation class MainListener
 *
 */
@WebListener
public class MainListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public MainListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
        // Initiate db and Account tables. 
      new Database();
      AccountManager.initTables();
       
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }
	
}
