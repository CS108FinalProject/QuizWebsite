package com.quizzes;

import java.util.LinkedHashMap;
import java.util.Map;

import com.dbinterface.Database;
import com.util.Constants;

/**
 * Provides the necessary functionality to perform Quiz operations.
 * @author Sam
 */
public class QuizManager implements Constants {
	
	/**
	 * Checks if the necessary DB tables for Quiz operations exist, and creates them
	 * if they don't. 
	 */
	public static void initTables() {
		if (!Database.tableExists(QUIZZES)) {
			Map<String, String> columns = new LinkedHashMap<String, String>();
			columns.put(QUIZ_NAME, STRING);
			columns.put(CREATOR, STRING);
			columns.put(DESCRIPTION, STRING); 
			columns.put(DATE_CREATED, STRING); 
			columns.put(IS_RANDOM, BOOLEAN); 
			columns.put(IS_ONE_PAGE, BOOLEAN); 
			columns.put(IS_IMMEDIATE, BOOLEAN); 
			Database.createTable(QUIZZES, columns);
		}
		
		if (!Database.tableExists(HISTORY)) {
			Map<String, String> columns = new LinkedHashMap<String, String>();
			columns.put(QUIZ_NAME, STRING);
			columns.put(USERNAME, STRING);
			columns.put(SCORE, DOUBLE);
			columns.put(DATE, STRING); 
			columns.put(TIME, STRING); 
			Database.createTable(HISTORY, columns);
		}
		
		if (!Database.tableExists(RESPONSE)) {
			Map<String, String> columns = new LinkedHashMap<String, String>();
			columns.put(QUIZ_NAME, STRING);
			columns.put(QUESTION, STRING);
			columns.put(ANSWER, STRING);
			Database.createTable(RESPONSE, columns);
		}
		
		if (!Database.tableExists(FILL_BLANK)) {
			Map<String, String> columns = new LinkedHashMap<String, String>();
			columns.put(QUIZ_NAME, STRING);
			columns.put(QUESTION, STRING);
			columns.put(BLANK, STRING);
			columns.put(ANSWER, STRING);
			Database.createTable(FILL_BLANK, columns);
		}
		
		if (!Database.tableExists(MULTIPLE_CHOICE)) {
			Map<String, String> columns = new LinkedHashMap<String, String>();
			columns.put(QUIZ_NAME, STRING);
			columns.put(QUESTION, STRING);
			columns.put(OPTION, STRING);
			columns.put(IS_ANSWER, BOOLEAN);
			Database.createTable(MULTIPLE_CHOICE, columns);
		}
		
		if (!Database.tableExists(PICTURE)) {
			Map<String, String> columns = new LinkedHashMap<String, String>();
			columns.put(QUIZ_NAME, STRING);
			columns.put(QUESTION, STRING);
			columns.put(PICTURE_URL, STRING);
			columns.put(ANSWER, STRING);
			Database.createTable(PICTURE, columns);
		}
		
		if (!Database.tableExists(MULTI_RESPONSE)) {
			Map<String, String> columns = new LinkedHashMap<String, String>();
			columns.put(QUIZ_NAME, STRING);
			columns.put(QUESTION, STRING);
			columns.put(ANSWER, STRING);
			columns.put(IS_ORDERED, BOOLEAN);
			columns.put(ORDER, INT);
			Database.createTable(MULTI_RESPONSE, columns);
		}
		
		if (!Database.tableExists(MATCHING)) {
			Map<String, String> columns = new LinkedHashMap<String, String>();
			columns.put(QUIZ_NAME, STRING);
			columns.put(QUESTION, STRING);
			columns.put(GROUP, INT);
			columns.put(LEFT, STRING);
			columns.put(RIGHT, STRING);
			Database.createTable(MATCHING, columns);
		}
	}

}
