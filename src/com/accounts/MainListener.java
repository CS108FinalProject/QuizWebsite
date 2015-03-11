package com.accounts;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.dbinterface.Database;
import com.quizzes.QuizManager;

/**
 * Application Lifecycle Listener implementation class MainListener
 *
 */
@WebListener
public class MainListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public MainListener() {}

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
        // Initiate db and Account tables. 
       new Database();
       AccountManager.initTables();     
       QuizManager.initTables();
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  {}
	
}
