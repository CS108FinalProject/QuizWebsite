package com.quizzes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.accounts.Account;
import com.dbinterface.Database;
import com.util.Constants;
import com.util.Util;

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
			columns.put(ELAPSED_TIME, STRING); 
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
			columns.put(LEFT, STRING);
			columns.put(RIGHT, STRING);
			Database.createTable(MATCHING, columns);
		}
	}
	
	
	/**
	 * Creates a new Quiz.
	 * @param name quiz name
	 * @param creator the Account of the creator
	 * @param description a brief description of the quiz
	 * @param date creation time in string form
	 * @param isRandom should the order be randomized
	 * @param isOnePage will it be delivered in one page
	 * @param isImmediate will the user get immediate answer feedback
	 * @return the Quiz object
	 */
	public static Quiz createQuiz(String name, Account creator, String description, 
			String date, boolean isRandom, boolean isOnePage, boolean isImmediate) {
		return new Quiz(name, creator, description, date, isRandom, isOnePage, 
				isImmediate);
	}
	
	
	/**
	 * Given a quiz name, returns its Quiz object.
	 */
	public static Quiz getQuiz(String quizName) {
		Util.validateString(quizName);
		return new Quiz(quizName);
	}
	
	
	/**
	 * Removes the passed quiz.
	 */
	public static void removeQuiz(Quiz quiz) {
		Util.validateObject(quiz);
		quiz.removeQuiz();
	}
	
	
	/**
	 * Checks if the quiz name is already in use.
	 * @return true if it does, false otherwise
	 */
	public static boolean quizNameInUse(String quizName) {
		Util.validateString(quizName);
		try {
			new Quiz(quizName);
		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * Returns all quizzes in the database or null if none exist.
	 */
	public static List<Quiz> qetAllQuizzes() {
		List<Map<String, Object>> table = Database.getTable(QUIZZES);
		if (table == null || table.size() == 0) return null;
		
		List<Quiz> result = new ArrayList<Quiz>();
		
		for (Map<String, Object> row : table) {
			result.add(new Quiz((String) row.get(QUIZ_NAME)));
		}
		return result;
	}
	
	
	/**
	 * Given a creator account, returns a list of quizzes made by that creator.
	 */
	public static List<Quiz> getQuizzes(Account creator) {
		Util.validateObject(creator);
		List<Map<String, Object>> table = Database.getRows(QUIZZES, CREATOR, creator);
		if (table == null || table.size() == 0) return null;
		
		List<Quiz> result = new ArrayList<Quiz>();
		
		for (Map<String, Object> row : table) {
			result.add(new Quiz((String) row.get(QUIZ_NAME)));
		}
		return result;
	}
	
	
	/**
	 * Returns a sorted list of most popular quizzes (index 0 is most popular and so on...) 
	 * @param numRecords number of desired entries in the list (0 for all)
	 * @return a list of Quiz objects.
	 */
	public static List<Quiz> getMostPopularQuizzes(int numRecords) {
		if (numRecords < 0) {
			throw new IllegalArgumentException(numRecords + " cannot be less than 0");
		}
		
		List<Quiz> result = new LinkedList<Quiz>();
		
		List<Map<String, Object>> rows = Database.getTable(HISTORY);
		if (rows == null) return null;
		
		// Count all quiz occurrences.
		Map<String, Integer> quizCount = new HashMap<String, Integer>();
		for (Map<String, Object> row : rows) {
			String quizName = (String) row.get(QUIZ_NAME);
			
			if (quizCount.containsKey(quizName)) {
				quizCount.put(quizName, quizCount.get(quizName) + 1);
				
			} else {
				quizCount.put(quizName, 1);
			}
		}
		
		// Create a new TreeMap and switch key-value for value-key so that
		// TreeMap sorts automatically.
		Map<Integer, List<String>> sortedCount = new TreeMap<Integer, List<String>>();
		for (String quizName : quizCount.keySet()) {
			if (sortedCount.containsKey(quizCount.get(quizName))) {
				sortedCount.get(quizCount.get(quizName)).add(quizName);
				
			} else {
				List<String> quizzes = new ArrayList<String>();
				quizzes.add(quizName);
				sortedCount.put(quizCount.get(quizName), quizzes);
			}
		}
		
		// Iterate over sorted TreeMap inserting the values in the resulting
		// list so that sort order is reversed.
		for (int count : sortedCount.keySet()) {
			for (String quizName : sortedCount.get(count)) {
				result.add(0, QuizManager.getQuiz(quizName));
			}
		}
		
		if (numRecords != 0 && numRecords < result.size()) {
			for (int i = result.size() - 1; i > numRecords - 1; i--) {
				result.remove(i);
			}
		}
		
		if (result.size() == 0) return null;
		return result;
	}
	
	
	/**
	 * Returns a list of recently created quizzes.
	 * @param numRecords desired number of entries in list (0 for all)
	 * @return a list of Quiz objects.
	 */
	public static List<Quiz> getRecentlyCreatedQuizzes(int numRecords) {
		if (numRecords < 0) {
			throw new IllegalArgumentException(numRecords + " cannot be less than 0.");
		}
		
		List<Quiz> result = new ArrayList<Quiz>();
		
		List<Map<String, Object>> rows = Database.getSortedTable(QUIZZES, DATE_CREATED, true);
		if (rows == null) return null;
		
		// Get all records.
		if (numRecords == 0) {
			for (Map<String, Object> row : rows) {
				result.add(getQuiz((String) row.get(QUIZ_NAME)));
			}
			
		} else {
			for (int i = 0; i < Math.min(numRecords, rows.size()); i++) {
				Map<String, Object> row = rows.get(i);
				result.add(getQuiz((String) row.get(QUIZ_NAME)));
			}
		}
		return result;
	}

}
