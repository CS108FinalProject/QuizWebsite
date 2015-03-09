package com.quizzes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.accounts.Account;
import com.accounts.AccountManager;
import com.dbinterface.Database;
import com.util.Util;

public class Achievement implements com.util.Constants{
	
	public static void add(Map<String, Object> quizMap, String type) {
		Util.validateObject(quizMap);
		
		// Validate Type
		if (!(quizMap.get(QUIZ_METADATA) instanceof Map<?, ?>)) { 
			throw new IllegalArgumentException("Invalid quizMap.");
		}
		
		Map<String, Object> metadata = (Map<String, Object>) quizMap.get(QUIZ_METADATA);
		
		// Add row to achievements table
		Map<String, Object> row = new HashMap<String, Object>();
		row.put(QUIZ_NAME, (String) metadata.get(QUIZ_NAME));
		row.put(CREATOR, (String) metadata.get(CREATOR));
		row.put(SCORE, (Double) metadata.get(CREATOR));
		row.put(DATE, (String) metadata.get(DATE));
		row.put(ELAPSED_TIME, (Double) metadata.get(ELAPSED_TIME));
		row.put(ACHIEVEMENT, (String) metadata.get(ACHIEVEMENT));		
		Database.addRow(ACHIEVEMENTS, row);
	}
	
//	/**
//	 * @return the Account of the quiz creator.
//	 */
//	public Account getCreator() {
//		List<Object> values = Database.getValues(ACHIEVEMENTS, QUIZ_NAME, name, CREATOR);
//		if (values == null) {
//			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
//					" . Cannot find creator for " + name);
//		}
//		
//		if (values.size() > 1) {
//			throw new RuntimeException("Corrupt table status for " + QUIZZES + 
//					" . Duplicate entries for " + name);
//		}
//		
//		Util.validateObjectType(values.get(0), STRING);
//		return AccountManager.getAccount((String) values.get(0));
//	}
}
