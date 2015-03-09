package com.quizzes;

import java.util.HashMap;
import java.util.Map;

import com.dbinterface.Database;
import com.util.Util;

public class Achievement implements com.util.Constants{
	
	/**
	 * Add the passed record and achievement type to the Achievements table.
	 * @param record
	 * @param type of Achievement.
	 */
	public static void add(Record record, String type) {
		Util.validateObject(record);
		Util.validateString(type);
		
		
		// Add row to achievements table
		Map<String, Object> row = new HashMap<String, Object>();
		row.put(QUIZ_NAME, record.getQuizName());
		row.put(USERNAME, record.getUser().getUserName());
		row.put(SCORE, record.getScore());
		row.put(DATE, record.getDate());
		row.put(ELAPSED_TIME, record.getElapsedTime());
		row.put(ACHIEVEMENT, type);		
		Database.addRow(ACHIEVEMENTS, row);
	}
}
