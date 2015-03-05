package com.quizzes;

import java.util.HashMap;
import java.util.Map;

import com.accounts.Account;
import com.dbinterface.Database;
import com.util.Constants;
import com.util.Util;

/**
 * Contains Quiz information. Talks to the database interface layer
 * to obtain and set fields.
 * 
 * @author Sam
 *
 */
public class Quiz implements Constants {
	
	private String name;
	
	
	/**
	 * Constructs a new Quiz object and adds the quiz info to the database.
	 */
	public Quiz(String name, Account creator, String description, String date, 
			boolean isRandom, boolean isOnePage, boolean isImmediate) {
		
		Util.validateString(name);
		Util.validateObject(creator);
		Util.validateString(description);
		Util.validateString(date);
		
		this.name = name;
		
		// Ensure quiz doesn't already exists.
		if (Database.getValues(QUIZZES, QUIZ_NAME, name, QUIZ_NAME) != null) {
			throw new IllegalArgumentException("Quiz " + name + "already exists");
		}
		
		Map<String, Object> row = new HashMap<String, Object>();
		row.put(QUIZ_NAME, name);
		row.put(CREATOR, creator.getUserName());
		row.put(DESCRIPTION, description);
		row.put(DATE_CREATED, date);
		row.put(IS_RANDOM, isRandom);
		row.put(IS_ONE_PAGE, isOnePage);
		row.put(IS_IMMEDIATE, isImmediate);
		Database.addRow(QUIZZES, row);
	}
	
	
	/**
	 * Provides an interface to interact with the Quizzes table of the database.
	 * Expects the passed quiz name to already exist in the database. Throws an exception
	 * if it doesn't. 
	 * @param userName
	 */
	public Quiz(String name) {
		Util.validateString(name);
		
		// Make sure quiz name exists in DB.
		if (Database.getValues(QUIZZES, QUIZ_NAME, name, QUIZ_NAME) == null) {
			throw new IllegalArgumentException("Cannot find quiz " + name);
		}
		this.name = name;
	}
	
	
	/**
	 * Removes the quiz from the database and sets the name to null
	 * so it can no longer be used.
	 */
	public void removeQuiz() {
		int removed = Database.removeRows(QUIZZES, QUIZ_NAME, name);
		
		// Only 1 row should be removed.
		if (removed != 1) {
			throw new RuntimeException("Problem removing rows. Removed " + removed + " rows");
		}
		name = null;
	}
	
	
	/**
	 * @return the quiz name for this Quiz.
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * Adds the passed question to the database.
	 * @param question
	 */
	public <T extends Question> void addQuestion(T question) {
		Util.validateObject(question);
		if (question instanceof Response) addResponse((Response) question);
		else if (question instanceof FillBlank) addFillBlank((FillBlank) question);
		else if (question instanceof MultipleChoice) addMultipleChoice((MultipleChoice) question);
		else if (question instanceof Picture) addPicture((Picture) question);
		else if (question instanceof MultiResponse) addMultiResponse((MultiResponse) question);
		else if (question instanceof Matching) addMatching((Matching) question);
		else {
			throw new IllegalArgumentException("Passed question is invalid");
		}
	}
	
	
	public <T extends Question> void editQuestion(T question) {
		// TODO: implement 
	}
	
	
	public <T extends Question> void removeQuestion(T question) {
		// TODO: implement
	}
	
	
	
	//--------------------------- Helper Methods -------------------------------//
	
	
	// Adds a Response question to the database.
	private void addResponse(Response response) {
		// Add a row for every answer in the question.
		for (String answer : response.getAnswers()) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put(QUIZ_NAME, name);
			row.put(QUESTION, response.getQuestion());
			row.put(ANSWER, answer);
			
			// If row already exists, do nothing.
			if (Database.getRows(RESPONSE, row) != null) {
				return;
			}
			
			Database.addRow(RESPONSE, row);
		}
	}
	
	
	// Adds a FillBlank question to the database.
	private void addFillBlank(FillBlank fillBlank) {
		// Iterate over every blank.
		for (String blank : fillBlank.getBlanksAndAnswers().keySet()) {
			// Iterate over every answer per each blank.
			for (String answer : fillBlank.getBlanksAndAnswers().get(blank)) {
				Map<String, Object> row = new HashMap<String, Object>();
				row.put(QUIZ_NAME, name);
				row.put(QUESTION, fillBlank.getQuestion());
				row.put(BLANK, blank);
				row.put(ANSWER, answer);
				
				// If row already exists, do nothing.
				if (Database.getRows(FILL_BLANK, row) != null) {
					return;
				}
				
				Database.addRow(FILL_BLANK, row);
			}
		}
	}
	
	
	// Adds a MultipleChoice question to the database.
	private void addMultipleChoice(MultipleChoice multipleChoice) {
		// Add a row for every answer in the question.
		for (String option : multipleChoice.getOptions().keySet()) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put(QUIZ_NAME, name);
			row.put(QUESTION, multipleChoice.getQuestion());
			row.put(OPTION, option);
			row.put(IS_ANSWER, multipleChoice.getOptions().get(option));
			
			// If row already exists, do nothing.
			if (Database.getRows(MULTIPLE_CHOICE, row) != null) {
				return;
			}
			
			Database.addRow(MULTIPLE_CHOICE, row);
		}
	}


	// Adds a Picture question to the database.
	private void addPicture(Picture picture) {
		// Add a row for every answer in the question.
		for (String answer : picture.getAnswers()) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put(QUIZ_NAME, name);
			row.put(QUESTION, picture.getQuestion());
			row.put(PICTURE_URL, picture.getPictureUrl());
			row.put(ANSWER, answer);
			
			// If row already exists, do nothing.
			if (Database.getRows(PICTURE, row) != null) {
				return;
			}
			
			Database.addRow(PICTURE, row);
		}
	}


	// Adds a MultiResponse question to the database.
	private void addMultiResponse(MultiResponse multiResponse) {
		// Add a row for every answer in the question.
		for (int i = 0; i < multiResponse.getAnswers().size(); i++) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put(QUIZ_NAME, name);
			row.put(QUESTION, multiResponse.getQuestion());
			row.put(ANSWER, multiResponse.getAnswers().get(i));
			row.put(IS_ORDERED, multiResponse.isOrdered());
			row.put(ORDER, i);
			
			// If row already exists, do nothing.
			if (Database.getRows(MULTI_RESPONSE, row) != null) {
				return;
			}
			
			Database.addRow(MULTI_RESPONSE, row);
		}
	}


	// Adds a Matching question to the database.
	private void addMatching(Matching matching) {
		// Add a row for every answer in the question.
		for (String left : matching.getMatches().keySet()) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put(QUIZ_NAME, name);
			row.put(QUESTION, matching.getQuestion());
			row.put(QUESTION_ID, matching.getId());
			row.put(LEFT, left);
			row.put(RIGHT, matching.getMatches().get(left));
			
			// If row already exists, do nothing.
			if (Database.getRows(MATCHING, row) != null) {
				return;
			}
			
			Database.addRow(MATCHING, row);
		}
	}
}
