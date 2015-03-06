package com.quizzes;

import java.util.HashMap;
import java.util.Map;

import com.dbinterface.Database;
import com.util.Constants;

/**
 * Provides functionality to operate on the History database table.
 * @author Sam
 *
 */
public class History implements Constants {

	/**
	 * Adds a quiz record to the database.
	 * @param quizName that was taken
	 * @param user Account of the quiz taker
	 * @param score on a scale of 100
	 * @param date when was the quiz taken
	 * @param elapsedTime how long did it take the user to finish
	 */
	public static void addRecord(Record record) {
		
		
		Map<String, Object> row = new HashMap<String, Object>();
		row.put(QUIZ_NAME, record.getQuizName());
		row.put(USERNAME, record.getUser().getUserName());
		row.put(SCORE, record.getScore());
		row.put(DATE, record.getDate());
		row.put(ELAPSED_TIME, record.getElapsedTime());
		Database.addRow(HISTORY, row);
	}
	
	
	
}
